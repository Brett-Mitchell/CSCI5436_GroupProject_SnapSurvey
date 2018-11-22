var textAreaHeight;
var actions = [];
var lastAddedQuestion = -1;

function clearModal() {
    var textArea = $('#new-question-textarea');
    textArea.val('');
    textArea.css('height', textAreaHeight);
    $('#new-question-type-select').val('Text');
}

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

function submit() {
    submitOneAction(0).then(function() {
        window.location.reload(true);
    });
}

function submitCancellation() {
    /*for (var a = actions.pop(); actions.length > 0; a = actions.pop()) 
        a.revert();*/
    
    back();
}

function back() {
    window.location.href = '/content/r/dashboard';
}

function deleteQuestion(list_idx) {
    // Get the database id corresponding to the given question list index
    var id = questions[list_idx];
    
    $('#question-' + id.toString()).hide();

    actions.push({
        parameters: {
            url: '/api/delete-survey-form-question',
            method: 'POST',
            data: { id: id }
        },
        onDone: function(d) {
            for (var i = id + 1; id <= $('#question-list').childCount; i++)
                $('#question-' + i.toString()).attr('id', 'question-' + (i - 1).toString());
        },
        onFail: function() {
            alert('Failed to delete question ' + id);
        },
        revert: function() {
            $('#question-' + id.toString()).show();
        }
    });
}

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

function addQuestion() {
    var text = $('#new-question-textarea').val();
    var questionType = $('#new-question-type-select').val();

    var newQuestion = newQuestionHTML(++lastAddedQuestion, text);

    $('#question-list').append(newQuestion);

    actions.push({
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
        onFail: function() {

        },
        revert: function() {

        }
    });
}

function updateQuestion(id, text) {
    actions.push({
        parameters: {
            url: '/api/update-survey-form-question',
            method: 'POST',
            data: {
                formId: formId,
                text: text,
                questionId: id
            }
        }
    });
}

window.onload = function() {
    textAreaHeight = $('#new-question-textarea').css('height');
    // Register clear modal function on modal close
    $('#new-question-modal').on('hidden.bs.modal', clearModal);
    $('#confirm-new-question-button').click(addQuestion);

    $('#confirm-cancel').click(submitCancellation);

    var numQuestions = $('#question-list >').length;
    for (var i = 1; i <= numQuestions; i++) {
        $('#question-delete-button-' + i).click((function(_i) {
            return function() { deleteQuestion(_i); };
        })(i));
    }

    $('#back').click(back);
    $('#submit').click(submit);
};
