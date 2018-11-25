
function goToTakeSurvey(survey, deploy) {
    return function() {
        window.location.href = '/content/p/take-survey?survey=' + survey.id + '&deploy=' + deploy.id;
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

    $('#logout-button').click(logout);

    for (var i in survey_invites) {
        var si = survey_invites[i];
        var deploy = survey_deploys.find(function(d) {
            return d.id === si.deploy;
        });
        var form = survey_forms.find(function(f) {
            return f.id === deploy.survey_form;
        });
        $('#survey-invite-' + si.id).click(goToTakeSurvey(si, deploy));
        $('#survey-invite-title-' + si.id).text(form.title);
    }
};