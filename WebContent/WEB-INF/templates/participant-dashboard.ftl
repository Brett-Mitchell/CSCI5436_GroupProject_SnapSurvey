<#include "base.ftl">

<#macro head>
    <script src="/dashboard.js"></script>
</#macro>

<#macro profile>
    <div class="col-auto"><img src="/generic-user-profile-img.jpeg" width="50px" height="50px" /></div>
</#macro>

<#macro body>

    <!-- Main content. Contains lists of active invite, missed invites, and past completed invites -->
    <div class="row">
        <div class="col">
            <!-- Current surveys -->
            <div class="row" id="current-row">
                <#list current_invites as c>
                    <div class="card" width="15rem">
                        <p>${c.title}</p>
                    </div>
                </#list>
            </div>
            <!-- Past surveys -->
            <div class="row" id="past-missed-row">
            </div>
            <!-- Past surveys -->
            <div class="row" id="past-complete-row">
            </div>
        </div>
    </div>

</#macro>

<@run />