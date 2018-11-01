<#include "base.ftl">

<#macro head>
    <script src="/dashboard.js"></script>
</#macro>

<#macro profile>
    <div class="col-auto"><img src="/generic-user-profile-img.jpeg" width="50px" height="50px" /></div>
</#macro>

<#macro body>

    <!-- Main content. Contains lists of survey forms and survey deploys -->
    <div class="row">
        <div class="col">
            <!-- Survey forms -->
            <div class="row" id="survey-forms-row">
                <div 
            </div>
            <!-- Current surveys -->
            <div class="row" id="deploy-current-row">
            </div>
            <!-- Past surveys -->
            <div class="row" id="deploy-past-row">
            </div>
        </div>
    </div>

</#macro>

<@run />