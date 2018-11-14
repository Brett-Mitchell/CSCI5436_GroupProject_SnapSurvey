<#include "base.ftl"/>

<#macro head>
	<link rel="stylesheet" href="/css/home-page.css">
	<script src="/js/home-page.js"></script>
</#macro>

<#macro body>
<div class="jumbotron">
	<h2>Welcome to SnapSurvey! Login or register below</h2>
	<span>
		<button class="btn btn-primary" type="button" id="login" value="login">Login</button>
		<button class="btn btn-secondary" type="button" id="register" value="register">Register</button>
	</span>
</div>
</#macro>

<@run/>