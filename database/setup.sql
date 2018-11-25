
DROP DATABASE IF EXISTS snapsurvey;
CREATE DATABASE snapsurvey;
USE snapsurvey;

-- users stores the username and authentication information for SnapSurvey users
CREATE TABLE users (
    id          INT NOT NULL AUTO_INCREMENT,
    username    VARCHAR(32) NOT NULL,
    email       VARCHAR(64) NOT NULL,
    salt        VARCHAR(16) NOT NULL,
    auth_hash   VARCHAR(64) NOT NULL,

    PRIMARY KEY (id),
    UNIQUE  KEY (username),
    UNIQUE  KEY (email)
);

-- sessions stores active sessions for logged-in users
CREATE TABLE user_sessions (
    user    INT NOT NULL,
    id      VARCHAR(128) NOT NULL,
    expiry  TIMESTAMP NOT NULL,

    PRIMARY KEY (user),
    FOREIGN KEY (user) REFERENCES users (id)
);

-- Researchers are a class of users who are allowed to create and activate
-- survey forms and view survey results
CREATE TABLE researchers (
    id INT NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
);

-- Participants are a class of users who are allowed to take surveys and submit
-- survey results
CREATE TABLE participants (
    id INT NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
);

-- Survey forms store the structure of surveys and relate survey forms to the
-- researchers who created them
CREATE TABLE survey_forms (
    id          INT NOT NULL AUTO_INCREMENT,
    title       VARCHAR(32),
    researcher  INT NOT NULL,

    PRIMARY KEY (id),
    UNIQUE  KEY (researcher, title),
    FOREIGN KEY (researcher) REFERENCES researchers(id) ON DELETE CASCADE
);

-- survey_form_questions relates individual questions in a survey form to their
-- containing form
CREATE TABLE survey_form_questions (
    id     SMALLINT NOT NULL AUTO_INCREMENT,
    form   INT NOT NULL,
    `type` VARCHAR(16) NOT NULL,
    `text` VARCHAR(500) NOT NULL,

    PRIMARY KEY (form, id),
    FOREIGN KEY (form) REFERENCES survey_forms(id) ON DELETE CASCADE
) ENGINE=MyISAM;

-- survey_form_question_choices only apply to multiple choice questions and
-- store the choices available to the users who answer the question
CREATE TABLE survey_form_question_choices (
    id       TINYINT NOT NULL AUTO_INCREMENT,
    question SMALLINT NOT NULL,
    `text`   VARCHAR(100) NOT NULL,

    PRIMARY KEY (question, id),
    UNIQUE  KEY (question, `text`),
    FOREIGN KEY (question) REFERENCES survey_form_questions(id) ON DELETE CASCADE
) ENGINE=MyISAM;

-- Stores survey deployments. Can be restricted with a date range or be provided
-- with only a start date.
CREATE TABLE survey_deploys (
    id          INT NOT NULL AUTO_INCREMENT,
    survey_form INT NOT NULL,
    ended       BIT NOT NULL DEFAULT 0,
    `start`     TIMESTAMP NOT NULL,
    `end`       TIMESTAMP NULL,

    PRIMARY KEY (survey_form, id),
    FOREIGN KEY (survey_form) REFERENCES survey_forms (id) ON DELETE CASCADE
) ENGINE=MyISAM;

-- MyISAM does not support cascading deletes, so this must be emulated in a
-- trigger
DELIMITER //

CREATE TRIGGER survey_deploys_survey_form_on_delete_cascade AFTER DELETE ON survey_forms
FOR EACH ROW BEGIN
    DELETE FROM survey_deploys
    WHERE survey_deploys.survey_form = OLD.id;
END//

DELIMITER ;

-- Associates survey deployments with an email. The association is linked only
-- to email to facilitate invites to both anonymous and registered participants.
-- There is no table for anonymous responses. If an invited email has no
-- associated participant entry, that invite and its associated response are
-- considered anonymous, and if the email is used to create an account,
-- previously anonymous responses will have an associated participant account.
CREATE TABLE survey_deploy_invites (
    id            INT NOT NULL AUTO_INCREMENT,
    survey_deploy INT NOT NULL,
    email         VARCHAR(64) NOT NULL,

    PRIMARY KEY (id),
    UNIQUE  KEY (survey_deploy, email),
    FOREIGN KEY (survey_deploy) REFERENCES survey_deploys (id) ON DELETE CASCADE
) ENGINE=MyISAM;

-- survey_responses records the value of submitted responses. The submitting
-- participant and the related survey form are recorded. Only one response per
-- participant is allowed
CREATE TABLE survey_responses (
    id            INT NOT NULL AUTO_INCREMENT,
    survey_deploy INT NOT NULL,
    invite        INT NOT NULL,

    PRIMARY KEY (id),
    UNIQUE  KEY (survey_deploy, invite),
    FOREIGN KEY (survey_deploy) REFERENCES survey_deploys (id) ON DELETE CASCADE,
    FOREIGN KEY (invite) REFERENCES survey_deploy_invites (id) ON DELETE CASCADE
) ENGINE=MyISAM;

-- survey_response_values acts as a base type for text values and choice values,
-- providing unique ids for each response.
CREATE TABLE survey_response_values (
    id              INT NOT NULL AUTO_INCREMENT,
    question        SMALLINT NOT NULL,
    survey_response INT NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (question) REFERENCES survey_form_questions(id) ON DELETE CASCADE,
    FOREIGN KEY (survey_response) REFERENCES survey_responses(id) ON DELETE CASCADE
) ENGINE=MyISAM;

DELIMITER //

CREATE TRIGGER survey_response_values_survey_response_on_delete_cascade AFTER DELETE ON survey_responses
FOR EACH ROW BEGIN
    DELETE FROM survey_response_values
    WHERE survey_response_values.survey_response = OLD.id;
END//

DELIMITER ;

-- survey_response_text_values are bodies of text in response to a textual
-- response question.
CREATE TABLE survey_response_text_values (
    id      INT NOT NULL,
    `value` VARCHAR(1000),

    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES survey_response_values(id) ON DELETE CASCADE
) ENGINE=MyISAM;

-- survey_response_choice_values represent the selections made by the user on
-- questions with predefined choices. Each user is allow to submit multiple
-- choices in order to support multiselect questions, but is restricted from
-- submitting the same choice twice for the same question
CREATE TABLE survey_response_choice_values (
    id       INT NOT NULL,
    choice   TINYINT NOT NULL,

    -- since id is unique, only choice can have multiple values in any given PK
    -- pair
    PRIMARY KEY (id, choice),
    FOREIGN KEY (id) REFERENCES survey_response_values(id) ON DELETE CASCADE,
    FOREIGN KEY (choice) REFERENCES survey_form_question_choices(id)
) ENGINE=MyISAM;

-- STORED PROCEDURES

DELIMITER //

-- new_user takes a username, password and user type and creates the proper
-- table entries for the base user and user type tables. It creates a new salt
-- string and only stores the SHA256 hash of the user's password for security
CREATE PROCEDURE new_user(un VARCHAR(32), pw VARCHAR(32), email VARCHAR(64), user_type BIT)
-- un VARCHAR(32): The username to add as a new user
-- pw VARCHAR(32): The password for the new user
-- email VARCHAR(64): The email of the new user
-- usr_type BIT: Represents the type of user: 0 -> researchers, 1 -> participant
BEGIN
    SET @salt = HEX(RANDOM_BYTES(8));
    SET @auth_hash = SHA2(CONCAT(pw, @salt), 256);

    INSERT INTO users (username, email, salt, auth_hash)
    VALUES (un, email, @salt, @auth_hash);

    SET @user_id = LAST_INSERT_ID();

    CASE WHEN user_type = 0 THEN
        INSERT INTO researchers (id)
        VALUES (@user_id);
    WHEN user_type = 1 THEN
        INSERT INTO participants (id)
        VALUES (@user_id);
    END CASE;

    SELECT id
      FROM users
     WHERE users.username = un;
END//

-- update_user takes a user id, new username and new password and sets the
-- username and auth_hash of the user corresponding to the id. This will fail
-- for usernames which already exist in the database under a different user id.
CREATE PROCEDURE update_user(id INT, un VARCHAR(32), pw VARCHAR(32), email VARCHAR(64))
-- id INT: id is the integer id of the user row to change
-- un VARCHAR(32): un is the new username
-- pw VARCHAR(32): pw is the new password
__proc:BEGIN
    SELECT COUNT(username)
      INTO @matching_users
      FROM users
     WHERE users.id = id;
    
    IF (@matching_users = 1) THEN
        SELECT salt
          INTO @salt
          FROM users
         WHERE users.id = id;
        
        SET @auth_hash = SHA2(CONCAT(pw, @salt), 256);

        UPDATE users
           SET username  = un,
               email     = email,
               auth_hash = @auth_hash
         WHERE users.id = id;
    END IF;
END//

-- authenticate_user takes a username and password and returns whether or not
-- the given credentials match any in the database
CREATE PROCEDURE authenticate_user(un VARCHAR(32), pw VARCHAR(32))
-- un VARCHAR(32): un is the username to test
-- pw VARCHAR(32): pw is the password to test
-- valid_auth BIT: valid_auth is the return value: 0 -> invalid credentials,
--                                                 1 ->   valid credentials
-- valid_auth is not defined as an OUT parameter; rather, a value of 0 or 1 will
-- be SELECTed as output from the procedure
__proc:BEGIN
    SELECT COUNT(username)
      INTO @matching_usernames
      FROM users
     WHERE users.username = un;

    IF (@matching_usernames <> 1) THEN
        SELECT 0 AS 'valid';
        LEAVE __proc;
    END IF;

    SELECT salt, auth_hash
      INTO @salt, @previous_auth_hash
      FROM users
     WHERE users.username = un;

    SET @given_auth_hash = SHA2(CONCAT(pw, @salt), 256);

    IF @given_auth_hash = @previous_auth_hash THEN
        SELECT 1 AS 'valid';
        LEAVE __proc;
    END IF;

    SELECT 0 AS 'valid';
END//

DELIMITER ;

-- Demo data for SnapSurvey

-- Create 2 researchers
CALL new_user("researcher1", "password", "r1@gmail.com", 0);
CALL new_user("researcher2", "password", "r2@gmail.com", 0);

-- Create 10 participants
CALL new_user("participant1", "password", "p1@gmail.com", 1);
CALL new_user("participant2", "password", "p2@gmail.com", 1);
CALL new_user("participant3", "password", "p3@gmail.com", 1);
CALL new_user("participant4", "password", "p4@gmail.com", 1);
CALL new_user("participant5", "password", "p5@gmail.com", 1);
CALL new_user("participant6", "password", "p6@gmail.com", 1);
CALL new_user("participant7", "password", "p7@gmail.com", 1);
CALL new_user("participant8", "password", "p8@gmail.com", 1);
CALL new_user("participant9", "password", "p9@gmail.com", 1);
CALL new_user("participant10", "password", "p10@gmail.com", 1);

-- Create 7 demo forms for researcher1
INSERT INTO survey_forms (title, researcher)
VALUES
('Burger Survey', 1),
('Invasive Survey', 1),
('Survey 3', 1),
('Survey 4', 1),
('Survey 5', 1),
('Survey 6', 1),
('Survey 7', 1),
('Survey 8', 1),
('Survey 9', 1),
('Survey 10', 1);

INSERT INTO survey_form_questions (form, `type`, `text`)
VALUES
-- survey 1, researcher 1
(1, "text", "Describe the perfect burger"),
(1, "multiple_choice", "If you could only have one, which of the following condiments would you prefer?"),
(1, "multiple_choice", "Would you eat a quinoa burger?"),
(1, "text", "What is the best kind of burger bun?"),
-- survey 2, researcher 1
(2, "number", "What is your SSN?"),
(2, "text", "Please list all credit/debit card numbers"),
(2, "text", "What is your billing address?");

INSERT INTO survey_form_question_choices (question, `text`)
VALUES
-- survey 1, question 2
(2, "Ketchup"),
(2, "Mayonaise"),
(2, "Mustard"),
(2, "Relish"),
-- survey 1, question 3
(3, "Definitely no"),
(3, "Probably (but definitely) no");

INSERT INTO survey_deploys (survey_form, ended, `start`)
VALUES (1, 0, NOW());

INSERT INTO survey_deploy_invites (survey_deploy, email)
VALUES (1, 'p1@gmail.com');