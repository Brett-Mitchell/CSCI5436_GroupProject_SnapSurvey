
var currentQuestion = 1;

function next() {
    if (currentQuestion < Object.keys(questions).length) {
        recordAnswer(currentQuestion);
        $('#question-' + questions[currentQuestion.toString()].id).hide();
        currentQuestion++;
        $('#question-' + questions[currentQuestion.toString()].id).show();
    }
}

function previous() {
    if (currentQuestion > 1) {
        recordAnswer(currentQuestion);
        $('#question-' + questions[currentQuestion.toString()].id).hide();
        currentQuestion--;
        $('#question-' + questions[currentQuestion.toString()].id).show();
    }
}

function submit() {

}

function recordAnswer(questionIdx) {
    var qToRecord = questions[questionIdx.toString()];
    if (qToRecord.type === 'text') {
        qToRecord.answer = $('#question-answer-text-' + qToRecord.id).val();
    } else if (qToRecord.type === 'multiple_choice') {
        qToRecord.answer = [];
        for (var cIdx in qToRecord.choices) {
            var c = qToRecord.choices[cIdx];
            qToRecord.answer.push({
                choiceId: c.id,
                value: $('#question-' + qToRecord.id + '-choice-' + c.id).is(':checked')
            });
        }
    }

    var canSubmit = false;
    for (var q in questions)
        canSubmit = canSubmit && (!!questions[q].answer);

    if (canSubmit)
        $('#submit').removeAttr('disabled');
}

$(document).ready(function() {

    // Hide all questions and re-show the first one
    for (var i in questions) {
        $('#question-' + questions[i].id).hide();
    }

    $('#question-' + questions['1'].id).show();

    // Add callbacks to navigation and submission buttons
    $('#next').click(next);
    $('#previous').click(previous);
    $('#submit').click(submit);
    $('#submit').attr('disabled', 'disabled');

});
