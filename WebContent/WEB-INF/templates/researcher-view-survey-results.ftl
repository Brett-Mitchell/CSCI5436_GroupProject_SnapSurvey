<#include "base.ftl">

<#macro head>
    <script>
        
    </script>
    <script src="/js/researcher-view-survey-results.js"></script>
    <link href="/css/survey-results.css" rel="stylesheet">
</#macro>

<#macro headertag>
    <h3 class="my-auto">| ${survey_form.title}</h3>
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

    <!-- Main content -->
    <div class="content-wrapper">
        <!-- Responses forms -->
        <div id="response-tab" class="dashboard-item-column-wrapper">
            <div class="dashboard-item-column" id="response-column">
                <#assign i=1>
                <#list survey_form.questions as q>
                <div id="question-${q.id}-answers" class="card">
                    <h4>${q.text}</h4>

                    <div id="question-${q.id}-answers-column">
                        <#list survey_responses as r>
                        <div id="question-${q.id}-answers-${r.id}" class="card">
                            <#list r.values as v>
                                <#if v.question == q.id>
                                    <#if q.type == "multiple_choice" || q.type == "radio_select">
                                        ${v.choice}
                                    <#elseif q.type == "text" || q.type == "number">
                                        ${v.value}
                                    </#if>
                                </#if>
                            </#list>
                        </div>
                        </#list>
                    </div>
                </div>
                <#assign i++>
                </#list>
            </div>
        </div>
    </div>

</#macro>

<@run />