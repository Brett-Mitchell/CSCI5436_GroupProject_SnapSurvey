<#include "base.ftl">

<#macro head>
	<script>
	window.onload=function() {
		$('#login').click(function() {
			window.location.href = '/content/login';
		});
		$('#register').click(function() {
			window.location.href = '/content/register';
		});
	}
	</script>
</#macro>

<#macro body>

<div class="jumbotron mt-5">
	<h1>401: Unauthorized</h1>
	<hr />
	<p class="">
		You're not authorized to access this content. Why don't you try to
		<span><button id="login" class="btn btn-primary mx-1">Log In</button></span>
		 or 
		<span><button id="register" class="btn btn-primary mx-1">Register</button></span>
	</p>
</div>

</#macro>

<@run/>