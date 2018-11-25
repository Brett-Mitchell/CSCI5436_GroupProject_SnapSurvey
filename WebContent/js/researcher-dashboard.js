
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
            $('#survey-form-' + parsed.id).click(goToEditSurvey(surveys[parsed.id.toString()]));
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
        else {
            $('#survey-form-' + form.id + '-wrapper').remove();
            $('.deploy-for-' + form.id).remove();
        }
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

function findSurvey(id) {
    for (var k in surveys) 
        if (surveys[k].id == id) return surveys[k];
}

// Obtained from: https://stackoverflow.com/questions/179355/clearing-all-cookies-with-javascript
function deleteAllCookies() {
    var cookies = document.cookie.split(";");

    for (var i = 0; i < cookies.length; i++) {
        var cookie = cookies[i];
        var eqPos = cookie.indexOf("=");
        var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
        document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
    }
}

function logout() {
    deleteAllCookies();
    window.location.href = '/content/home-page';
}

window.onload = function() {

    // Force the browser to hard reload when navigating to the dashboard with
    // the browser's buttons
    $(window).bind("pageshow", function(event) {
        if (event.originalEvent.persisted) {
            window.location.reload(); 
        }
    });
    
    $('#logout-button').click(function() { logout(); });

    $('#new-survey-form-modal').on('hidden.bs.modal', clearModal);
    $('#confirm-create-survey-form').click(function() {
        createSurvey($('#new-survey-title').val());
        $('#new-survey-form-modal').modal('toggle');
    });

    $('#survey-forms-tab').css('display', 'flex');
    $('#deploys-tab').css('display', 'none');
    $('#show-survey-forms-button').click(openTab('survey-forms-tab'));
    $('#show-deploys-button').click(openTab('deploys-tab'));

    for (var i in surveys) {
        $('#survey-form-' + surveys[i.toString()].id).click(goToEditSurvey(surveys[i.toString()]));
        $('#survey-' + surveys[i.toString()].id + '-delete').click(deleteSurvey(surveys[i.toString()]));
    }
    
    for (i in current_survey_deploys) {
        var d = current_survey_deploys[i.toString()];
        $('#deploy-' + d.id).click(goToViewSurvey(d.id));
        var form = findSurvey(d.form);
        $('#deploy-' + d.id + '-title').text(form.title + ' - Deploy #' + d.id);
    }
};