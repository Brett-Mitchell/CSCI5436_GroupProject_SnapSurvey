
var currentQuestion = 0;

function next() {
    var numQuestions = Object.keys(survey_form.questions).length;
    if (currentQuestion < numQuestions - 1) {
        $('#question-' + survey_form.questions[currentQuestion.toString()].id + '-answers').hide();
        currentQuestion++;
        $('#question-' + survey_form.questions[currentQuestion.toString()].id + '-answers').show();
    }
}

function previous() {
    if (currentQuestion > 0) {
        $('#question-' + survey_form.questions[currentQuestion.toString()].id + '-answers').hide();
        currentQuestion--;
        $('#question-' + survey_form.questions[currentQuestion.toString()].id + '-answers').show();
    }
}

function back() {
    window.location.href = '/content/r/dashboard';
}

$(document).ready(function() {

    for (var i in survey_form.questions) {
        $('#question-' + survey_form.questions[i].id + '-answers').hide();
    }

    $('#question-' + survey_form.questions[0].id + '-answers').show();

    for (var i in responses) {
        for (var j in responses[i].values) {
            var value = responses[i].values[j];
            if (value.choice !== null) {
                var questionChoice = survey_form.questions
                .find(function(q) {
                    return q.id === value.question;
                })
                .choices
                .find(function(c) {
                    return c.id === value.choice;
                });
                if (questionChoice)
                    $('#question-' + value.question + '-response-' + responses[i].id + '-choice-' + value.choice).text(questionChoice.text);
            }
        }
    }

    $('#back').click(back);
    $('#previous').click(previous);
    $('#next').click(next);

});
