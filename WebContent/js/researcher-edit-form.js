var textAreaHeight;
var actions = [];
var lastAddedQuestion = -1;
var actionCounter = 0;

function clearModal() {
    var textArea = $('#new-question-textarea');
    textArea.val('');
    textArea.css('height', textAreaHeight);
    $('#new-question-type-select').val('Text');
}

// Remove all redundant or overwritten actions
function squashActions() {
    var squashedActions = [];

    while(actions.length) {
        // Pull out all actions for the first target id
        var first = actions.shift();
        var actionSequence = [first];
        for (var i = 0; i < actions.length; i++) {
            var a = actions[i];
            if (a.target_type === first.target_type &&
                a.target      === first.target      )
            {
                actionSequence.push(a);
                actions.splice(i, 1);
            }
        }
        // Remove all redundant actions. If there is a terminating delete
        // action, all other preceding actions can be discarded. All but the
        // last update action may be discarded. An initial add action must be
        // kept.
        var last = actionsSequence[actionSequence.length - 1];

        if (first.type === 'create') {
            // If an object is created and then deleted, the result is a no-op
            if (last.type === 'delete') {
                continue;
            // The most recent update will contain the most up-to-date content
            } else if (last.type === 'update') {
                squashedActions.concat(first, last);
            }
        // If the actions in actionSequence pertain to a pre-existing item, only
        // the last action applies
        } else {
            squashedActions.push(last);
        }
    }

    // Update the action chain with squashed actions, reset the action counter,
    // and update the id's of the remaining actions
    actions = squashedActions;
    actionCounter = 0;
    actions.forEach(function(a) {
        a.id = actionCounter++;
    });
}

// Promise-based recursive execution of the recorded action chain
function submitOneAction(i) {
    var a = actions[i];
    if (i < actions.length - 1)
        return $.ajax(a.parameters)
                .done(a.onDone)
                .fail(a.onFail)
                .then(function() { return submitOneAction(++i); });
    else
        return $.ajax(a.parameters)
                .done(a.onDone)
                .fail(a.onFail);
}

// Submits all the actions in the action chain and then reloads the page
function submit() {
    submitOneAction(0).then(function() {
        window.location.reload(true);
    });
}

// Cancels all actions
function submitCancellation() {
    /*for (var a = actions.pop(); actions.length > 0; a = actions.pop()) 
        a.revert();*/
    
    back();
}

// Navigates back to the researcher's dashboard
function back() {
    window.location.href = '/content/r/dashboard';
}

// Deletes a question
function deleteQuestion(list_idx) {
    // Get the database id corresponding to the given question list index
    var id = questions[list_idx];
    
    $('#question-' + id).hide();

    function revert() {
        $('#question-' + id).show();
    }

    actions.push({
        id: actionCounter++,
        type: 'delete',
        target_type: 'question',
        target: id,
        parameters: {
            url: '/api/delete-survey-form-question',
            method: 'POST',
            data: { id: id }
        },
        onDone: function(data) {
            var parsed = JSON.parse(data);
            if (!parsed.success)
                revert();
        },
        onFail: function(err) {
            console.error(err);
            revert();
        },
        revert: revert
    });
}

// Returns a DOM element containing the HTML necessary for a question
function newQuestionHTML(id, text) {
    var templateString = 
'<div id="question-new-' + id + '" class="card question-card-raised mb-1">' +
    '<form>' +
        '<div class="form-group mb-1 p-1">' +
            '<textarea ' +
                'id="question-text-new-' + id + '"' +
                'class="form-control w-100"' +
            '>' + text + '</textarea>' +
        '</div>' +
        '<div id="question-new-' + id + '-choices" class="choices-column form-group p-1">' +
        '</div>' +
        '<div class="form-group">' +
            '<button type="button" ' +
                    'id="question-delete-button-new-' + id + '"' +
                    'class="float-right mb-1 mr-1 btn btn-danger">' +
            'Delete' +
            '</button>' +
        '</div>' +
    '</form>' +
'</div>';

    return $('<div/>').html(templateString).contents();
}

// Adds a new question
function addQuestion() {
    var text = $('#new-question-textarea').val();
    var questionType = $('#new-question-type-select').val();

    // Since the new question doesn't exist in the database yet, the add
    // action's id is used to identify the new question until changes are pushed
    // to the server
    actionCounter++;
    var newQuestion = newQuestionHTML(actionCounter, text);

    $('#question-list').append(newQuestion);

    actions.push({
        id: actionCounter,
        type: 'add',
        target_type: 'question',
        target: lastAddedQuestion,
        parameters: {
            url: '/api/add-survey-form-question',
            method: 'POST',
            data: {
                formId: formId,
                text: text,
                type: questionType
            }
        },
        onDone: function(d) {
            console.log(d);
        },
        onFail: function(err) {
            console.error(err);
        },
        revert: function() {
            console.log('reverting addQuestion');
        }
    });
}

// Updates a question that was created in the current edit session and does not
// exist in the database yet
function updateNewQuestion(actionId, text) {
    actions.find(function(a) {
        return a.id === actionId;
    }).parameters.text = text;
}

// Updates a question that exists in the database
function updateExistingQuestion(id, text) {
    actions.push({
        id: actionCounter++,
        type: 'update',
        target_type: 'question',
        target: id,
        parameters: {
            url: '/api/update-survey-form-question',
            method: 'POST',
            data: {
                formId: formId,
                text: text,
                questionId: id
            }
        },
        onDone: function(d) {
            for (var i = id + 1; id <= $('#question-list').childCount; i++)
                $('#question-' + i.toString()).attr('id', 'question-' + (i - 1));
        },
        onFail: function() {
            alert('Failed to delete question ' + id);
        },
        revert: function() {
            $('#question-' + id.toString()).show();
        }
    });
}

// Deletes a choice from a multiple choice question
function deleteChoice(question, choice) {

    $('#question-' + question + '-choice-' + choice).hide();

    function revert() {
        $('#question-' + question + '-choice-' + choice).show();
    }

    actions.push({
        id: actionCounter++,
        type: 'delete',
        target_type: 'choice',
        target: question + '.' + choice,
        parameters: {
            url: '/api/delete-survey-form-question-choice',
            method: 'POST',
            data: {
                questionId: question,
                choiceId: choice
            }
        },
        onDone: function(data) {
            var parsed = JSON.parse(d);
            if (!parsed.success)
                revert();
        },
        onFail: function(err) {
            console.log(err);
            revert();
        },
        revert: revert
    });
}

// Adds a choice to a multiple choice question
function addChoice(question) {
    var choices = questions[question].choices;
    actions.push({
        id: actionCounter++,
        type: 'add',
        target_type: 'choice',
        target: question + '.' + (choices[choices.length - 1] + 1),
        parameters: {
            url: '/api/add-survey-form-question-choice',
            method: 'POST',
            data: {
                questionId: question,
                text: ''
            }
        },
        onDone: function(data) {

        },
        onFail: function(err) {

        },
        revert: function() {

        }
    });
}

// Sets up event listeners and prepares the new question modal to clear and
// resize on close
$(document).ready(function() {
    textAreaHeight = $('#new-question-textarea').css('height');
    // Register clear modal function on modal close
    $('#new-question-modal').on('hidden.bs.modal', clearModal);
    $('#confirm-new-question-button').click(addQuestion);

    $('#confirm-cancel').click(submitCancellation);

    for (var i = 0; i < questions.length; i++) {
        var q = questions[i];
        $('#question-delete-button-' + q.id).click((function(_i) {
            return function() { deleteQuestion(_i); };
        })(i));
        
        for (var c in q.choices) {
            $('#question-' + q.id + '-choice-' + q.choices[c] + '-delete').click((function(qId, cId) {
                return function() { deleteChoice(qId, cId); };
            })(q.id, q.choices[c]));
        }
    }

    $('#back').click(back);
    $('#submit').click(submit);
});
