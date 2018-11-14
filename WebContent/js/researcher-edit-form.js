
function deleteQuestion(list_idx) {
    // Get the database id corresponding to the given question list index
    var id = questions[list_idx.toString()];

    $.ajax({
        url: '/api/delete-survey-form-question',
        method: 'POST',
        data: { id: id },

    })
    .done(function(d) {
        for (var i = id + 1; id <= $('#question-list').childCount; i++)
            $('#question-' + i.toString()).attr('id', 'question-' + String(i - 1));
        console.log(d);
        $('#question-' + id.toString()).remove();
    })
    .fail(function(a, b, c) {
        console.error(a, b, c);
        alert('Failed to delete question ' + id);
    });
}

window.onload = function() {
    var numQuestions = $('#question-list >').length;
    for (var i = 1; i <= numQuestions; i++) {
        $('#question-delete-button-' + i).click((function(_i) {
            return function() { deleteQuestion(_i); };
        })(i));
    }
};
