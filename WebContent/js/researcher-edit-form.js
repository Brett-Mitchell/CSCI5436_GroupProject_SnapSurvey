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
        var i = 0;
        while (i < actions.length) {
            var a = actions[i];
            if (a.target_type === first.target_type &&
                a.target      === first.target      )
            {
                actionSequence.push(actions.splice(i, 1)[0]);
            } else {
                i++;
            }
        }
        // Remove all redundant actions. If there is a terminating delete
        // action, all other preceding actions can be discarded. All but the
        // last update action may be discarded. An initial add action must be
        // kept.
        var last = actionSequence[actionSequence.length - 1];

        if (first.type === 'create') {
            // If an object is created and then deleted, the result is a no-op
            if (last.type === 'delete') {
                continue;
            // The most recent update will contain the most up-to-date content,
            // but a newly created item will not yet exist in the database, so
            // the data from the update event is used in the add action
            } else if (last.type === 'update') {
                first.parameters.data = last.parameters.data;
                squashedActions.push(first);
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
    squashActions();
    submitOneAction(0).then(function() {
        window.location.reload(true);
    });
}

// Cancels all actions
function submitCancellation() {
    back();
}

// Navigates back to the researcher's dashboard
function back() {
    window.location.href = '/content/r/dashboard';
}

// Deletes a question
function deleteQuestion(list_idx) {
    // Get the database id corresponding to the given question list index
    var id = questions[list_idx.toString()].id;
    
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
            if (!JSON.parse(data).success)
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
function newQuestionHTML(id, text, type) {
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
    var actionId = actionCounter++;
    questions['new-' + actionId] = {
        id: actionId,
        choices: []
    };

    var newQuestion = newQuestionHTML(actionId, text);

    $('#question-list').append(newQuestion);

    $('#question-text-new-' + actionId).blur((function(_id) {
        return function(focusEvent) { updateQuestion(_id, focusEvent.target.value); };
    })('new-' + actionId));
    $('#question-delete-button-new-' + actionId).click((function(_id) {
        return function() { deleteQuestion(_id); };
    })('new-' + actionId));

    function revert() {
        $('#question-new-' + actionId).remove();
        questions['new-' + actionId] = undefined;
    }

    actions.push({
        id: actionId,
        type: 'add',
        target_type: 'question',
        target: 'new-' + actionId,
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
            if (!JSON.parse(d).success)
                revert();
        },
        onFail: function(err) {
            console.error(err);
            revert();
        },
        revert: revert
    });
}

// Updates a question
function updateQuestion(id, text) {
    if ($('#question-text-' + id).data('previous_value') === text) return;

    var actionId = actionCounter++;

    // Reset the text of the question's textarea element
    var revert = (function(_id, _actionId) {
        return function() {
            var qText = $('#question-text-' + _id);
            // First look for a preceding update or add action to set the text to
            for (var i = actions.length - 1; i >= 0; i--) {
                var a = actions[i];
                if (['update', 'add'].includes(a.type) &&
                    a.target_type === 'question'       &&
                    a.target      ===  _id             &&
                    a.id          !==  _actionId         ) {
                    
                    qText.val(a.parameters.data.text);
                    qText.data('previous_value', a.parameters.data.text);
                    return;
                }
            }
    
            qText.val(qText.data('initial_value'));
        };
    })(id, actionId);

    $('#question-text-' + id).data('previous_value', text);

    actions.push({
        id: actionId,
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
        onDone: function(data) {
            if (!JSON.parse(data).success)
                revert();
        },
        onFail: function(err) {
            console.log(err);
            revert();
        },
        revert: revert
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
            if (!JSON.parse(data).success)
                revert();
        },
        onFail: function(err) {
            console.log(err);
            revert();
        },
        revert: revert
    });
}

function updateChoice(question, choice, text) {
    console.log('updating choice');
}

function newChoiceHTML(qId, cId) {
    var templateString = 
'<div id="question-' + qId + '-choice-' + cId + '" class="choice mb-1">' + 
    '<input class="form-control my-auto" type="text" value="" />' + 
    '<button type="button"' + 
        'id="question-' + qId + '-choice-' + cId + '-delete"' + 
        'class="btn btn-secondary m-2 my-auto h-80">' + 
    'Delete' + 
    '</button>' + 
'</div>';

    return $('<div/>').html(templateString).contents();
}

// Adds a choice to a multiple choice question
function addChoice(question) {
    var qId = questions[question.toString()].id;
    var choices = questions[question.toString()].choices;
    var cId = 'new-';
    if (choices && choices[choices.length - 1])
        cId += choices[choices.length - 1] + 1;
    else
        cId += 1;
    
    choices.push(cId);
    
    var newChoice = newChoiceHTML(qId, cId);
    newChoice.insertBefore('#question-' + qId + '-new-choice-row');

    function revert() {
        $('#question-' + qId + '-choice-' + cId).hide();
    }

    actions.push({
        id: actionCounter++,
        type: 'add',
        target_type: 'choice',
        target: qId + '.' + cId,
        parameters: {
            url: '/api/add-survey-form-question-choice',
            method: 'POST',
            data: {
                questionId: question,
                text: ''
            }
        },
        onDone: function(data) {
            if (!JSON.parse(data).success)
                revert();
        },
        onFail: function(err) {
            console.log(err);
            revert();
        },
        revert: revert
    });
}

function undo() {
    if (!actions.length) return;
    var a = actions.pop();
    a.revert();
}

// Sets up event listeners and prepares the new question modal to clear and
// resize on close
$(document).ready(function() {

    // Prevent default Ctrl-Z functionality in order to support custom undo
    // operation
    $("body").keydown(function(e){
        var zKey = 90;
        if ((e.ctrlKey || e.metaKey) && e.keyCode == zKey) {
            undo();
            e.preventDefault();
            return false;
        }
    });

    textAreaHeight = $('#new-question-textarea').css('height');
    // Register clear modal function on modal close
    $('#new-question-modal').on('hidden.bs.modal', clearModal);
    $('#confirm-new-question-button').click(addQuestion);

    // Add function to handle confirming cancellation of edits
    $('#confirm-cancel').click(submitCancellation);

    // Set events for questions in the form
    for (var i in questions) {
        var q = questions[i.toString()];

        var qText = $('#question-text-' + q.id);

        // Call updateQuestion when a question's textarea loses focus
        qText.on('blur', (function(_i) {
            return function(focusEvent) { updateQuestion(_i, focusEvent.target.value); };
        })(q.id));

        // Record the initial text value for reverting update actions
        qText.data('initial_value', qText.val());
        qText.data('previous_value', qText.val());

        // Call deleteQuestion when a question's delete button is clicked
        $('#question-delete-button-' + q.id).click((function(_i) {
            return function() { deleteQuestion(_i); };
        })(i));
        
        // Add delete click events to any existing multiple choice options
        for (var c in q.choices) {
            $('#question-' + q.id + '-choice-' + q.choices[c] + '-delete').click((function(qId, cId) {
                return function() { deleteChoice(qId, cId); };
            })(q.id, q.choices[c]));

            var cText = $('question-' + q.id + '-choice-' + q.choices[c] + '-text');
            cText.blur((function(_q, _c) {
                return function(focusEvent) {
                    console.log('in callback');
                    updateChoice(_q, _c, focusEvent.target.value);
                };
            })(q.id, q.choices[c]));
            cText.data('initial_value', cText.val());
            cText.data('previous_value', cText.val());
        }

        // Add a new choice callback to all questions with multiple choice
        // options
        var addChoiceButton = $('#question-' + q.id + '-new-choice');
        if (addChoiceButton)
            addChoiceButton.click((function(q) {
                return function() { addChoice(q); };
            })(q.id));
    }

    // Add navigation callbacks
    $('#undo').click(undo);
    $('#back').click(back);
    $('#submit').click(submit);
});
