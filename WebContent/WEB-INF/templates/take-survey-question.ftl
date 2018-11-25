
<#macro question q>
    <div id="question-${q.id}" class="question-wrapper">
        <div class="card question-inner-wrapper">
            <div class="question-title">
            </div>
            <div class="question-text">
                <h5>${q.text}</h5>
            </div>
            
            <hr/>

            <div class="form-group question-input-wrapper">
                <#if q.type == "text">
                    <textarea id="question-answer-text-${q.id}" class="question-input questions-text-input"></textarea>
                <#elseif q.type == "multiple_choice">
                    <div class="question-choice-wrapper">
                        <#list q.choices as c>
                            <div class="checkbox-wrapper">
                                <input type="checkbox"
                                       class="question-input"
                                       id="question-${q.id}-choice-${c.id}"
                                       value="${c.id}">
                                <p class="checkbox-label">${c.text}</p>
                            </div>
                        </#list>
                    </div>
                <#elseif q.type == "radio_select">
                    <div class="question-choice-wrapper">
                        <#list q.choices as c>
                            <fieldset id="question-${q.id}-radio-wrapper" class="checkbox-wrapper">
                                <input type="radio"
                                       class="question-input"
                                       id="question-${q.id}-choice-${c.id}"
                                       value="${c.id}"
                                       name="question-${q.id}-radio-wrapper">
                                <p class="checkbox-label">${c.text}</p>
                            </fieldset>
                        </#list>
                    </div>
                <#else>
                    <div class="warning">Unsupported question type</div>
                </#if>
            </div>
        </div>
    </div>
</#macro>
