<#include "base.ftl">

<#macro head>
    <script>
        var surveys = {
            <#assign i=1>
            <#list survey_forms as s>
                '${i}': {
                    id: ${s.id},
                    title: '${s.title}'
                },
                <#assign i++>
            </#list>
        };
        var current_survey_deploys = {
            <#assign i=1>
            <#list current_survey_deploys as d>
                '${i}': {
                    id: ${d.id},
                    form: '${d.surveyForm}'
                },
                <#assign i++>
            </#list>
        };
    </script>
    <script src="/js/researcher-dashboard.js"></script>
    <link href="/css/dashboard.css" rel="stylesheet">
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

    <!-- New survey modal -->
    <div id="new-survey-form-modal" class="modal fade" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content p-3">
                <div class="modal-header">
                    <h4>New Survey Form</h4>
                </div>
                <h5 class="mt-3">Survey title:</h5>
                <div class="form-group">
                    <input id="new-survey-title" class="form-control" type="text"/>
                </div>
                <hr/>
                <div class="modal-footer">
                    <button type="button" data-dismiss="modal" class="btn btn-secondary">Cancel</button>
                    <button id="confirm-create-survey-form" type="button" class="btn btn-primary">Create</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Confirm delete survey modal -->
    <div id="confirm-delete-modal" class="modal fade" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div id="confirm-delete-modal-header" class="modal-header">
                    <h4>
                        Are you sure you want to delete survey
                        <span id="confirm-delete-survey-title"></span>?
                    </h4>
                    <h6>This action cannot be undone.</h6>
                </div>
                <div class="modal-footer">
                    <button id="confirm-delete-survey-button" type="button" class="btn btn-secondary">Yes</button>
                    <button type="button" class="btn btn-primary" data-dismiss="modal">No</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Main content. Contains lists of survey forms and survey deploys -->
    <div class="content-wrapper">
        <div class="btn-group w-100" id="nav-row">
            <button id="show-survey-forms-button" type="button" class="btn btn-light">Forms</button>
            <button id="show-deploys-button" type="button" class="btn btn-light">Deployments</button>
        </div>

        <!-- Survey forms -->
        <div id="survey-forms-tab" class="dashboard-item-column-wrapper">
            <div class="dashboard-item-column" id="survey-forms-column">
                <#list survey_forms as survey>
                <div id="survey-form-${survey.id}-wrapper" class="survey-forms-card-wrapper">
                    <div class="foreground card dashboard-card survey-forms-card"
                         id="survey-form-${survey.id}">
                        <p class="my-auto mx-auto">${survey.title}</p>
                    </div>
                    <button
                        type="button"
                        class="btn btn-danger survey-form-delete-button"
                        id="survey-${survey.id}-delete">
                    Delete
                    </button>
                </div>
                </#list>
            </div>
            <div id="dashboard-survey-forms-add-button-wrapper" class="card">
                <button
                    type="button"
                    data-toggle="modal"
                    data-target="#new-survey-form-modal" 
                    class="btn btn-primary"
                    id="dashboard-add-survey-form-button"
                    >
                New Survey
                </button>
            </div>
        </div>

        <!-- Deploys -->
        <div id="deploys-tab" class="dashboard-item-column-wrapper">
            <div class="dashboard-item-column" id="deploys-column">
                <#list current_survey_deploys as deploy>
                    <div class="foreground card dashboard-card deploy-card deploy-for-${deploy.surveyForm}"
                         id="deploy-${deploy.id}">
                        <p id="deploy-${deploy.id}-title" class="my-auto mx-auto"></p>
                    </div>
                </#list>
            </div>
        </div>
    </div>

</#macro>

<@run />