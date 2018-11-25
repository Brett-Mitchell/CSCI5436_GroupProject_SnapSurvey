
var currentQuestion = 1;

function next() {
    var numQuestions = Object.keys(questions).length;
    if (currentQuestion < numQuestions) {
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
    var submission = [];
    for (var q in questions) {
        submission.push(questions[q].answer);
    }

    $.ajax({
        url: '/api/submit-survey',
        method: 'POST',
        data: {
            submission: JSON.stringify(submission),
            deployId: deployId
        }
    })
    .done(function(data) {
        window.location.href = '/content/submission-thanks';
    })
    .fail(function(err) {
        console.error(err);
    });
}

function recordAllAnswers() {
    var canSubmit = true;
    for (var i in questions) {
        recordAnswer(i);
        var vals = questions[i].answer.answerValues;
        canSubmit = canSubmit && (!!vals[0] || vals.length === 0);
    }

    if (canSubmit)
        $('#submit').removeAttr('disabled');
}

function recordAnswer(questionIdx) {
    var qToRecord = questions[questionIdx.toString()];
    if (qToRecord.type === 'text') {
        qToRecord.answer = {
            question: qToRecord.id,
            answerType: qToRecord.type,
            answerValues: [$('#question-answer-text-' + qToRecord.id).val()]
        };
    } else if (qToRecord.type === 'multiple_choice' || qToRecord.type === 'radio_select') {
        qToRecord.answer = {
            question: qToRecord.id,
            answerType: qToRecord.type,
            answerValues: []
        };
        for (var cIdx in qToRecord.choices) {
            var c = qToRecord.choices[cIdx];
            if ($('#question-' + qToRecord.id + '-choice-' + c.id).is(':checked'))
                qToRecord.answer.answerValues.push(c.id.toString());
        }
    }
}

$(document).ready(function() {

    // Hide all questions and re-show the first one
    for (var i in questions) {
        $('#question-' + questions[i].id).hide();
    }

    $('.question-input').blur(function() { recordAllAnswers(); });

    $('#question-' + questions['1'].id).show();

    // Add callbacks to navigation and submission buttons
    $('#next').click(next);
    $('#previous').click(previous);
    $('#submit').click(submit);
    $('#submit').attr('disabled', 'disabled');

});
