
<#macro question q>
    <div id="question-${q.id}" class="card question-card-raised mb-1">
        <form>
            <div class="form-group mb-1 p-1">
                <textarea
                    id="question-text-${q.id}"
                    class="form-control w-100"
                >${q.text}</textarea>
            </div>
            <div class="form-group mb-1 p-1">
                <select class="selectpicker w-100"
                        id="question-${q.id}-type-picker">
                    <option value="multiple_choice">Multiple choice</option>
                    <option value="number">Number</option>
                    <option value="radio_select">Radio select</option>
                    <option value="text">Text</option>
                </select>
            </div>
            <div id="question-${q.id}-choices" class="choices-column form-group p-1">
                <#list q.choices as c>
                    <div id='question-${q.id}-choice-${c.id}' class="choice mb-1">
                        <input id="question-${q.id}-choice-${c.id}-text"
                                class="form-control my-auto"
                                type="text"
                                value="${c.text}" />
                        <button type="button"
                                id="question-${q.id}-choice-${c.id}-delete"
                                class="btn btn-secondary m-2 my-auto h-80">
                        Delete
                        </button>
                    </div>
                </#list>
                <div id="question-${q.id}-new-choice-row" class="row">
                    <button id="question-${q.id}-new-choice"
                            type="button"
                            class="btn btn-primary mx-auto new-choice-button">
                    +
                    </button>
                </div>
            </div>
            <div class="form-group">
                <button type="button"
                        id="question-delete-button-${q.id}"
                        class="float-right mb-1 mr-1 btn btn-danger">
                Delete
                </button>
            </div>
        </form>
    </div>
</#macro>
