<#include "base.ftl">

<#macro head>
    <script>
    var survey_deploys = [
        <#list survey_deploys as d>
            {
                id: ${d.id},
                survey_form: ${d.surveyForm}
            },
        </#list>
    ];
    var survey_invites = [
        <#list survey_invites as i>
            {
                id: ${i.id},
                deploy: ${i.surveyDeploy},
                email: '${i.email}'
            },
        </#list>
    ];
    var survey_forms = [
        <#list survey_forms as f>
            {
                id: ${f.id},
                title: '${f.title}'
            },
        </#list>
    ];
    </script>
    <link rel="stylesheet" href="/css/dashboard.css">
    <script src="/js/participant-dashboard.js"></script>
</#macro>

<#macro headertag>
    <h3 class="my-auto">| Dashboard</h3>
</#macro>

<#macro profile>
    <div class="ml-auto my-auto col-auto">
        <button id="logout-button"
                class="btn btn-primary">
        Log out
        </button>
    </div>
</#macro>

<#macro body>

    <div class="content-wrapper">
        <!-- Header -->
        <h4 class="card p-3">Survey Invites</h4>

        <!-- Survey invites -->
        <div class="dashboard-item-column-wrapper">
            <div class="dashboard-item-column" id="survey-invites-column">
                <#list survey_invites as invite>
                <div id="survey-invite-${invite.id}-wrapper" class="survey-invite-card-wrapper">
                    <div class="foreground card dashboard-card survey-invite-card"
                         id="survey-invite-${invite.id}">
                        <p id="survey-invite-title-${invite.id}" class="my-auto mx-auto"></p>
                    </div>
                </div>
                </#list>
            </div>
        </div>
    </div>

</#macro>

<@run />