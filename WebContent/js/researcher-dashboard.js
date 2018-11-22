
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

var currentTab = 'survey-forms-tab';

function openTab(tabName) {
    return function() {
        $('#' + currentTab).css('display', 'none');
        $('#' + tabName).css('display', 'flex');
        currentTab = tabName;
    };
}

window.onload = function() {
    var survey_form_column = document.getElementById('survey-forms-column');

    $('#survey-forms-tab').css('display', 'flex');
    $('#current-surveys-tab').css('display', 'none');
    $('#past-surveys-tab').css('display', 'none');
    $('#show-survey-forms-button').click(openTab('survey-forms-tab'));
    $('#show-current-surveys-button').click(openTab('current-surveys-tab'));
    $('#show-past-surveys-button').click(openTab('past-surveys-tab'));

    for (var i = 1; i <= survey_form_column.childElementCount; i++) {
        document.getElementById('survey-form-' + i.toString())
                .onclick = goToEditSurvey(i);
    }
    
    var current_deploy_row = document.getElementById('current-deploys-row');
    
    for (i = 1; i <= current_deploy_row.childElementCount; i++) {
        document.getElementById('current-deploy-' + i.toString())
                .onclick = goToViewSurvey(i);
    }
};