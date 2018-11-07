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
        <div class="col no-spacing">
            <!-- Survey forms -->
            <div class="foreground card dashboard-section-title">
                <h4 class="my-auto">Survey Forms</h4>
            </div>
            <div class="row no-spacing dashboard-item-row-wrapper">
                <div class="dashboard-item-row"
                     id="survey-forms-row">
                    <#list survey_forms as survey>
                        <div class="foreground card survey-forms-card"
                             id="survey-form-${survey.id}">
                            <p class="my-auto mx-auto">${survey.title}</p>
                        </div>
                    </#list>
                </div>
            </div>
            <!-- Current surveys -->
            <div class="foreground card dashboard-section-title">
                <h4 class="my-auto">Current Survey Deployments</h4>
            </div>
            <div class="row no-spacing dashboard-item-row-wrapper">
                <div class="dashboard-item-row"
                     id="current-deploys-row">
                    <#list current_survey_deploys as deploy>
                        <div class="foreground card survey-forms-card"
                             id="current-deploy-${deploy.id}">
                            <p class="my-auto mx-auto">${deploy.surveyForm}</p>
                        </div>
                    </#list>
                </div>
            </div>
            <!-- Past surveys -->
            <div class="foreground card dashboard-section-title">
                <h4 class="my-auto">Past Survey Deployments</h4>
            </div>
            
            <div class="row no-spacing dashboard-item-row-wrapper">
                <div class="dashboard-item-row">
                </div>
            </div>
        </div>
    </div>

</#macro>

<@run />