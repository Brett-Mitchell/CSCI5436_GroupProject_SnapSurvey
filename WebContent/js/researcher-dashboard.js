
function goToEditSurvey(id) {
    return function() {
        window.location.href = '/content/r/edit-survey?id=' + id;
    };
}

function goToViewSurvey(id) {
    return function() {
        window.location.href = '/content/r/view-survey?id=' + id;
    };
}

window.onload = function() {
    var survey_form_row = document.getElementById('survey-forms-row');

    for (var i = 1; i <= survey_form_row.childElementCount; i++) {
        document.getElementById('survey-form-' + i.toString())
                .onclick = goToEditSurvey(i);
    }
    
    var current_deploy_row = document.getElementById('current-deploys-row');
    
    for (var i = 1; i <= current_deploy_row.childElementCount; i++) {
        document.getElementById('current-deploy-' + i.toString())
                .onclick = goToViewSurvey(i);
    }
};