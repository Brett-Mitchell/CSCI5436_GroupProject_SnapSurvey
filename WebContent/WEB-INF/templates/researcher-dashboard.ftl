<#include "base.ftl">

<#macro head>
    <script src="/js/researcher-dashboard.js"></script>
    <link href="/css/dashboard.css" rel="stylesheet">
</#macro>

<#macro headertag>
    <h3 class="my-auto">| Dashboard</h3>
</#macro>

<#macro profile>
    <div class="col-auto ml-auto"><img src="/generic-user-profile-img.jpeg" width="50px" height="50px" /></div>
</#macro>

<#macro body>

    <!-- Main content. Contains lists of survey forms and survey deploys -->
    <div class="content-wrapper">
        <div class="btn-group w-100" id="nav-row">
            <button id="show-survey-forms-button" type="button" class="btn btn-light">Forms</button>
            <button id="show-current-surveys-button" type="button" class="btn btn-light">Current Deployments</button>
            <button id="show-past-surveys-button" type="button" class="btn btn-light">Past Deployments</button>
        </div>

        <!-- Survey forms -->
        <div id="survey-forms-tab" class="dashboard-item-column-wrapper">
            <div class="dashboard-item-column" id="survey-forms-column">
                <#list survey_forms as survey>
                    <div class="foreground card survey-forms-card"
                         id="survey-form-${survey.id}">
                        <p class="my-auto mx-auto">${survey.title}</p>
                    </div>
                </#list>
            </div>
        </div>

        <!-- Current surveys -->
        <div id="current-surveys-tab" class="dashboard-item-column-wrapper">
            <div class="dashboard-item-column" id="current-deploys-column">
                <#list current_survey_deploys as deploy>
                    <div class="foreground card survey-forms-card"
                         id="current-deploy-${deploy.id}">
                        <p class="my-auto mx-auto">${deploy.surveyForm}</p>
                    </div>
                </#list>
            </div>
        </div>
        
        <!-- Past surveys -->
        <div id="past-surveys-tab" class="dashboard-item-column-wrapper">
            <div class="dashboard-item-column">
            </div>
        </div>
    </div>

</#macro>

<@run />