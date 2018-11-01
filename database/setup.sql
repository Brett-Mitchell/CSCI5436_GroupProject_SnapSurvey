
DROP DATABASE IF EXISTS snapsurvey;
CREATE DATABASE snapsurvey;
USE snapsurvey;

-- users stores the username and authentication information for SnapSurvey users
CREATE TABLE users (
    id          INT NOT NULL AUTO_INCREMENT,
    username    VARCHAR(32) NOT NULL,
    email       VARCHAR(64) NOT NULL
    salt        VARCHAR(16) NOT NULL,
    auth_hash   VARCHAR(64) NOT NULL,

    PRIMARY KEY (id),
    UNIQUE  KEY (username),
    UNIQUE  KEY (email)
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
    FOREIGN KEY (researcher) REFERENCES researchers(id) ON DELETE CASCADE
);

-- survey_form_questions relates individual questions in a survey form to their
-- containing form
CREATE TABLE survey_form_questions (
    id     SMALLINT NOT NULL AUTO_INCREMENT,
    form   INT NOT NULL,
    `text` VARCHAR(500) NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (form) REFERENCES survey_forms(id) ON DELETE CASCADE
);

-- survey_form_question_choices only apply to multiple choice questions and
-- store the choices available to the users who answer the question
CREATE TABLE survey_form_question_choices (
    id       TINYINT NOT NULL AUTO_INCREMENT,
    question SMALLINT NOT NULL,
    `text`   VARCHAR(100) NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (question) REFERENCES survey_form_questions(id) ON DELETE CASCADE
);

-- survey_responses records the value of submitted responses. The submitting
-- participant and the related survey form are recorded. Only one response per
-- participant is allowed
CREATE TABLE survey_responses (
    id          INT NOT NULL AUTO_INCREMENT,
    survey      INT NOT NULL,
    participant INT NOT NULL,

    PRIMARY KEY (id),
    UNIQUE  KEY (survey, participant),
    FOREIGN KEY (survey) REFERENCES survey_forms(id) ON DELETE CASCADE,
    FOREIGN KEY (participant) REFERENCES participants(id) ON DELETE CASCADE
);

-- survey_response_values acts as a base type for text values and choice values,
-- providing unique ids for each response.
CREATE TABLE survey_response_values (
    id              INT NOT NULL AUTO_INCREMENT,
    question        SMALLINT NOT NULL,
    survey_response INT NOT NULL,

    PRIMARY KEY (id),
    UNIQUE  KEY (id, question, survey_response),
    FOREIGN KEY (question) REFERENCES survey_form_questions(id) ON DELETE CASCADE,
    FOREIGN KEY (survey_response) REFERENCES survey_responses(id) ON DELETE CASCADE
);

-- survey_response_text_values are bodies of text in response to a textual
-- response question.
CREATE TABLE survey_response_text_values (
    id      INT NOT NULL,
    `value` VARCHAR(1000),

    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES survey_response_values(id) ON DELETE CASCADE
);

-- survey_response_choice_values represent the selections made by the user on
-- questions with predefined choices. Each user is allow to submit multiple
-- choices in order to support multiselect questions, but is restricted from
-- submitting the same choice twice for the same question
CREATE TABLE survey_response_choice_values (
    id     INT NOT NULL,
    choice TINYINT NOT NULL,

    -- since id is unique, only choice can have multiple values in any given PK
    -- pair
    PRIMARY KEY (id, choice),
    FOREIGN KEY (id) REFERENCES survey_response_values(id) ON DELETE CASCADE,
    FOREIGN KEY (choice) REFERENCES survey_form_question_choices(id) ON DELETE CASCADE
);

-- STORED PROCEDURES

DELIMITER //

-- new_user takes a username, password and user type and creates the proper
-- table entries for the base user and user type tables. It creates a new salt
-- string and only stores the SHA256 hash of the user's password for security
CREATE PROCEDURE new_user(un VARCHAR(32), pw VARCHAR(32), usr_type BIT)
-- un VARCHAR(32): The username to add as a new user
-- pw VARCHAR(32): Th password for the new user
-- usr_type BIT: Represents the type of user: 0 -> researchers, 1 -> participant
BEGIN
    SET @salt = HEX(RANDOM_BYTES(8));
    SET @auth_hash = SHA2(CONCAT(pw, @salt), 256);

    INSERT INTO users (username, salt, auth_hash)
    VALUES (un, @salt, @auth_hash);

    SET @user_id = LAST_INSERT_ID();

    CASE WHEN usr_type = 0 THEN
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
CREATE PROCEDURE update_user(id INT, un VARCHAR(32), pw VARCHAR(32))
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