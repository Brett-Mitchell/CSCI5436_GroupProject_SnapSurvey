
function deleteQuestion(list_idx) {
    // Get the database id corresponding to the given question list index
    var id = questions[list_idx.toString()];
    console.log(list_idx.toString());
    console.log('Deleting question with id=' + id);

    $.ajax({
        url: '',
        method: 'POST',
        data: {},

    })
    .done(function() {
        for (var i = id + 1; id <= $('#question-list').childCount; i++)
            $('#question-' + String(i)).attr('id', 'question-' + String(i - 1));

        $('#question-' + String(id)).remove();
    })
    .fail(function() {
        alert('Failed to delete question ' + id);
    });
}

window.onload = function() {
    var numQuestions = $('#question-list >').length;
    for (var i = 1; i <= numQuestions; i++) {
        console.log($('#question-delete-button-' + i));
        var _i = i;
        $('#question-delete-button-' + i).click(function() {
            deleteQuestion(_i);
        });
    }
};
