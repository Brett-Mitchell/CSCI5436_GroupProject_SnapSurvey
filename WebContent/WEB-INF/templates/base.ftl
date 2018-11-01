<#macro head>

</#macro>

<#macro body>

</#macro>

<#macro footer>

</#macro>

<#macro profile>

</#macro>

<#macro run>

    <!DOCTYPE html>

    <html>
        <head>
        	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
        	<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.bundle.min.js"></script>
            <@head/>
        </head>

        <body>

            <!-- Header bar. Contains software name and user profile image -->
            <div class="row" id="header-bar">
                <h5 class="col-auto mr-auto my-auto ml-3" id="software-name">SnapSurvey</h5>
                <@profile />
            </div>

            <@body/>
        </body>

        <footer>
            <@footer/>
        </footer>
    </html>

</#macro>