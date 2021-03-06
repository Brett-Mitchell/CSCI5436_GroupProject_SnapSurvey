<#assign useBackButton = 0>

<#macro head>

</#macro>

<#macro headertag>

</#macro>

<#macro profile>

</#macro>

<#macro header>
    <!-- Header bar. Contains software name and user profile image -->
    <div class="foreground row mp-0" id="header-bar">
        <#if useBackButton == 1>
            <button id="back"><</button>
        </#if>
        <h3 class="col-auto my-auto ml-3" id="software-name">SnapSurvey</h3>
        <@headertag />
        <@profile />
    </div>
</#macro>

<#macro body>

</#macro>

<#macro footer>

</#macro>

<#macro run>

    <!DOCTYPE html>

    <html id="root">
        <head>
            <meta charset="UTF-8"> 

            <!-- JQuery CDN -->
            <script src="https://code.jquery.com/jquery-3.3.1.js"></script>

            <!-- Bootstrap CDN -->
        	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
            <link rel="stylesheet" href="/css/base.css">
        	<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.bundle.js"></script>
            
            <!-- Bootstrap Select library CDN -->
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.2/css/bootstrap-select.min.css">
            <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.2/js/bootstrap-select.min.js"></script>
            <@head/>
        </head>

        <body id="root" class="background">
            <@header/>
            <@body/>
        </body>

        <footer>
            <@footer/>
        </footer>
    </html>

</#macro>