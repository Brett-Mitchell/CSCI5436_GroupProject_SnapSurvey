<#include "base.ftl"/>

<#macro head>
	<link rel="stylesheet" href="/css/home-page.css">
	<script src="/home-page.js"></script>
</#macro>

<#macro body>
<div class="jumbotron">
	<h2>Welcome to SnapSurvey! Login or register below</h2>
	<span>
		<button class="btn btn-primary" type="button" onclick="gotoLogin();" id="login" value="login">Login</button>
		<button class="btn btn-secondary" type="button" onclick="gotoRegister();" id="register" value="register">Register</button>
	</span>
</div>
</#macro>

<@run/>