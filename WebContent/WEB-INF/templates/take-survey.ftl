<#include "base.ftl">
<#include "take-survey-question.ftl">

<#macro head>
    <script>
    var questions = {
        <#assign i=1>
        <#list survey.questions as q>
            '${i}': {
                id: ${q.id},
                text: '${q.text}',
                type: '${q.type}',
                choices: [
                    <#list q.choices as c>
                        {
                            id: ${c.id},
                            text: '${c.text}'
                        },
                    </#list>
                ],
                answer: null
            },
            <#assign i++>
        </#list>
    };
    </script>
    <link href="/css/take-survey.css" rel="stylesheet">
    <script src="/js/take-survey.js"></script>
</#macro>

<#macro headertag>
    <h3 class="my-auto">| &nbsp;${survey.title}</h3>
</#macro>

<#macro body>
    
    <div class="content-wrapper">
        <#list survey.questions as q>
            <@question q />
        </#list>

        <div id="button-row" class="card">
            <button id="previous" type="button" class="btn btn-secondary">Previous</button>
            <button id="submit" type="button" class="btn btn-primary">Submit</button>
            <button id="next" type="button" class="btn btn-secondary">Next</button>
        </div>
    <div>

</#macro>

<@run />