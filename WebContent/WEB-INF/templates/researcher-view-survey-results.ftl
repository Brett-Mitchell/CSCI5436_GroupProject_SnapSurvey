<#include "base.ftl">

<#macro head>
    <script>
    var survey_form = {
        id: ${survey_form.id},
        title: '${survey_form.title}',
        questions: [
        <#list survey_form.questions as q>
            {
                id: ${q.id},
                text: '${q.text}',
                choices: [
                    <#list q.choices as c>
                    {
                        id: ${c.id},
                        text: '${c.text}'
                    },
                    </#list>
                ]
            },
        </#list>
        ]
    };

    var responses = [
        <#list survey_responses as r>
            {
                id: ${r.id},
                values: [
                    <#list r.values as v>
                        {
                            id: ${v.id},
                            question: ${v.question},
                            <#if v.value??>
                                value: '${v.value}',
                            <#elseif v.choice??>
                                choice: ${v.choice},
                            </#if>
                        },
                    </#list>
                ]
            }
        </#list>
    ];
    </script>
    <script src="/js/view-survey.js"></script>
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
        <div id="responses" class="dashboard-item-column-wrapper">
            <div class="dashboard-item-column" id="response-column">
                <#assign i=1>
                <#list survey_form.questions as q>
                <div id="question-${q.id}-answers" class="card">
                    <h4 class="p-3">${q.text}</h4>

                    <div id="question-${q.id}-answers-column">
                        <#list survey_responses as r>
                        <div id="question-${q.id}-answers-${r.id}" class="card p-2 response-row">
                            <div>Response #${r.id}: </div>
                            <div class="ml-3">
                            <#assign comma=''>
                            <#list r.values as v>
                                <#if v.question == q.id>
                                    <#if q.type == "multiple_choice" || q.type == "radio_select">
                                        ${comma}<span id="question-${q.id}-response-${r.id}-choice-${v.choice}"></span>
                                        <#assign comma=', '>
                                    <#elseif q.type == "text" || q.type == "number">
                                        ${v.value}
                                    </#if>
                                </#if>
                            </#list>
                            </div>
                        </div>
                        </#list>
                    </div>
                </div>
                <#assign i++>
                </#list>
            </div>
        </div>

        <div id="button-row" class="card">
            <button id="back" type="button" class="btn btn-primary">Back</button>
            <button id="previous" type="button" class="btn btn-secondary">Previous</button>
            <button id="next" type="button" class="btn btn-secondary">Next</button>
        </div>
    </div>

</#macro>

<@run />