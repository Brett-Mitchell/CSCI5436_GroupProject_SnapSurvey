<#macro head>

</#macro>

<#macro body>

</#macro>

<#macro footer>

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
        	<div id="title-bar"><h1>SnapSurvey</h1></div>
            <@body/>
        </body>

        <footer>
            <@footer/>
        </footer>
    </html>

</#macro>