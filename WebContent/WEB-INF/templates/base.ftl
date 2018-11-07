<#macro head>

</#macro>

<#macro headertag>

</#macro>

<#macro profile>

</#macro>

<#macro header>
    <!-- Header bar. Contains software name and user profile image -->
    <div class="foreground" id="header-bar">
        <div class="row no-spacing">
            <h3 class="col-auto my-auto ml-3" id="software-name">SnapSurvey</h3>
            <@headertag />
            <@profile />
        </div>
    </div>
</#macro>

<#macro body>

</#macro>

<#macro footer>

</#macro>

<#macro run>

    <!DOCTYPE html>

    <html>
        <head>
            <script src="https://code.jquery.com/jquery-3.3.1.js"></script>
        	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
            <link rel="stylesheet" href="/css/base.css">
        	<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.bundle.js"></script>
            <@head/>
        </head>

        <body class="background">
            <@header/>
            <@body/>
        </body>

        <footer>
            <@footer/>
        </footer>
    </html>

</#macro>