<#include "base.ftl"/>

<#macro head>
	<link rel="stylesheet" href="/css/home-page.css">
</#macro>

<#macro body>
<div id="container" align="center">
	<h2>Welcome to SnapSurvey! Login or register below</h2>
	<span>
		<button id="login" value="login">Login</button>
		<button id="register" value="register">Register</button>
	</span>
</div>
</#macro>

<@run/>