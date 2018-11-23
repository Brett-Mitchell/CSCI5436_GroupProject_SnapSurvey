<#include "base.ftl">
<#include "edit-survey-form-question.ftl">

<#macro head>
    <script>
    var formId = ${survey_form.id};
    var questions = [
        <#assign i=1>
        <#list survey_form.questions as q>
            {
                id: ${q.id},
                choices: [
                    <#list q.choices as c>
                    ${c.id},
                    </#list>
                ]
            },
            <#assign i++>
        </#list>
    ];
    </script>
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
                    <div class="form-group">
                        <textarea
                            id="new-question-textarea"
                            class="form-control mb-1 w-100"
                        ></textarea>
                        <select class="form-control" id="new-question-type-select">
                            <option>Text</option>
                            <option>Multiple answer</option>
                            <option>Radio select</option>
                            <option>Number</option>
                        </select>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                    <button id="confirm-new-question-button" type="button" class="btn btn-primary" data-dismiss="modal">Confirm</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Cancel confirmation modal -->
    <div id="confirm-cancel-modal" class="modal fade" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h4>Are you sure you want to cancel?</h4>
                </div>
                <div class="modal-footer">
                    <button id="confirm-cancel" type="button" class="btn btn-secondary">Yes</button>
                    <button type="button" class="btn btn-primary" data-dismiss="modal">No</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Main content. Contains a list of question forms -->
    <div class="content-wrapper">
        <div id="question-list" class="p-1">
            <#assign i=1>
            <#list survey_form.questions as q>
                <@question q />
                <#assign i++>
            </#list>
        </div>
        
        <div class="row mp-0">
            <div class="mx-auto my-4">
                <button type="button" data-toggle="modal" data-target="#new-question-modal" class="btn btn-secondary">New Question</button>
                <button id="submit" type="button" class="btn btn-primary">Save Form</button>
                <button id="cancel" type="button" data-toggle="modal" data-target="#confirm-cancel-modal" class="btn btn-danger">Cancel</button>
            </div>
        </div>
    </div>

</#macro>

<@run />