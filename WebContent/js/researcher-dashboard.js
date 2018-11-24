
function goToEditSurvey(form) {
    return function() {
        window.location.href = '/content/r/edit-survey?id=' + form.id;
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

function clearModal() {
    $('#new-survey-title').val('');
}

function newSurveyHTML(title, id) {
    var templateString = 
'<div id="survey-form-' + id + '-wrapper" class="survey-forms-card-wrapper">' +
    '<div class="foreground card survey-forms-card"' +
        'id="survey-form-' + id + '">' +
        '<p class="my-auto mx-auto">' + title + '</p>' +
    '</div>' +
    '<button ' +
        'type="button"' +
        'class="btn btn-danger survey-form-delete-button"' +
        'id="survey-' + id + '-delete">' +
    'Delete' +
    '</button>' +
'</div>';

    return $('<div/>').html(templateString).contents();
}

function createSurvey(title) {
    $.ajax({
        url: '/api/add-survey-form',
        method: 'POST',
        data: {
            title: title
        }
    })
    .done(function(data) {
        var parsed = JSON.parse(data);
        if (!parsed.success)
            alert('Could not create survey: ' + title);
        else {
            surveys[parsed.id.toString()] = {
                id: parsed.id,
                title: title
            };
            $('#survey-forms-column').prepend(newSurveyHTML(title, parsed.id));
            $('#survey-form-' + parsed.id).click(goToEditSurvey(parsed.id));
            $('#survey-' + parsed.id + '-delete').click(deleteSurvey(surveys[parsed.id.toString()]));
        }
    })
    .fail(function(err) {
        console.error(err);
        alert('Could not create survey ' + title);
    });
}

function performDeleteSurvey(form) {
    $.ajax({
        url: '/api/delete-survey-form',
        method: 'POST',
        data: {
            formId: form.id
        }
    })
    .done(function(data) {
        var parsed = JSON.parse(data);
        if (!parsed.success)
            alert('Could not delete survey ' + form.title);
        else
            $('#survey-form-' + form.id + '-wrapper').remove();
    })
    .fail(function(err) {
        console.error(err);
        alert('Could not delete survey ' + form.title);
    });
}

function deleteSurvey(form) {
    return function() {
        $('#confirm-delete-modal').modal('toggle');
        $('#confirm-delete-survey-title').text("'" + form.title + "'");
        $('#confirm-delete-survey-button').click(function() {
            performDeleteSurvey(form);
            $('#confirm-delete-modal').modal('toggle');
        });
    };
}

window.onload = function() {
    $('#new-survey-form-modal').on('hidden.bs.modal', clearModal);
    $('#confirm-create-survey-form').click(function() {
        createSurvey($('#new-survey-title').val());
        $('#new-survey-form-modal').modal('toggle');
    });

    $('#survey-forms-tab').css('display', 'flex');
    $('#current-surveys-tab').css('display', 'none');
    $('#past-surveys-tab').css('display', 'none');
    $('#show-survey-forms-button').click(openTab('survey-forms-tab'));
    $('#show-current-surveys-button').click(openTab('current-surveys-tab'));
    $('#show-past-surveys-button').click(openTab('past-surveys-tab'));

    for (var i in surveys) {
        $('#survey-form-' + surveys[i.toString()].id).click(goToEditSurvey(surveys[i.toString()]));
        $('#survey-' + surveys[i.toString()].id + '-delete').click(deleteSurvey(surveys[i.toString()]));
    }
    
    for (i in survey_deploys)
        $('#current-deploy-' + i).click(goToViewSurvey(survey_deploys[i.toString()]));
};