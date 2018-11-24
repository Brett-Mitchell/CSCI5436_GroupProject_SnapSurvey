<#include "base.ftl"/>

<#macro head>
    <link rel="stylesheet" href="/css/login-register.css">
    <script src="/js/register.js"></script>
</#macro>

<#macro body>

<div id="registration-failed" class="alert alert-danger">
    Registration failed:&nbsp;<span id="registration-failed-msg"></span>
</div>

<div class="card mt-5 p-1 mx-auto login-wrapper">
    <form>
        <input id="username" class="mb-1 form-control" type="text" placeholder="Username">
        <input id="password" class="mb-1 form-control" type="password" placeholder="Password">
        <input id="email" class="mb-1 form-control" type="text" placeholder="Email">
        <label for="account-type-picker">Account type:</label>
        <select id="account-type-picker" class="selectpicker">
            <option value="p" selected>Participant</option>
            <option value="r">Researcher</option>
        </select>
        <button id="register-button" class="float-right btn btn-primary" type="button">Register</button>
    </form>
</div>

</#macro>

<@run/>
