
<#macro question q>
    <div class="card mt-2">
        <form>
            <div class="form-group mb-1 p-1">
                <textarea
                    id="question-text-${q.id}"
                    class="form-control w-100"
                >${q.text}</textarea>
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
