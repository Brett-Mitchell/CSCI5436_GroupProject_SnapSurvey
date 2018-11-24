<#include "base.ftl"/>

<#macro head>
    <link rel="stylesheet" href="/css/login-register.css">
    <script src="/js/login.js"></script>
</#macro>

<#macro body>

<div id="login-failed" class="alert alert-danger">
    Login failed!
</div>

<div class="card mt-5 p-1 mx-auto login-wrapper">
    <form>
        <input id="username" class="mb-1 form-control" type="text" placeholder="Username">
        <input id="password" class="mb-1 form-control" type="password" placeholder="Password">
        <button id="login-button" class="float-right btn btn-primary" type="button">Login</button>
    </form>
</div>

</#macro>

<@run/>
