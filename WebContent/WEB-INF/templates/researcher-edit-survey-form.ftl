<#include "base.ftl">
<#include "edit-survey-form-question.ftl">

<#macro head>
    <script src="/js/researcher-edit-form.js"></script>
    <link href="/css/edit-form.css" rel="stylesheet">
</#macro>

<#macro headertag>
    <h3 class="my-auto">| Edit ${survey_form.title}</h3>
</#macro>

<#macro profile>
    <div class="col-auto ml-auto"><img src="/generic-user-profile-img.jpeg" width="50px" height="50px" /></div>
</#macro>

<#macro body>

    <!-- New question modal. Defined as high in the DOM as possible to avoid
         position:fixed issues -->
    <div id="new-question-modal" class="modal fade" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
            <div class="modal-header">
                <h4>New Question</h4>
            </div>
            <div class="modal-body">
                <p>Example content...</p>
            </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                    <button type="button" class="btn btn-primary">Confirm</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Main content. Contains a list of question forms -->
    <div class="content-wrapper">
        <#list questions as q>
            <@question q />
        </#list>
        
        <div class="row no-spacing">
            <div class="mx-auto mt-4">
                <button type="button" data-toggle="modal" data-target="#new-question-modal" class="btn btn-secondary">New Question</button>
                <button type="button" class="btn btn-primary">Save Form</button>
            </div>
        </div>
    </div>

</#macro>

<@run />