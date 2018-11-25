<#include "base.ftl">

<#macro head>
	<script>
	window.onload=function() {
		$('#dashboard').click(function() {
			window.location.href = '/content/p/dashboard';
		});
	}
	</script>
</#macro>

<#macro body>

<div class="jumbotron mt-5">
	<h1>Thank You!</h1>
	<hr />
	<p class="">
		We appreciate your submission! You can view your other survey invitations 
		<span><button id="dashboard" class="btn btn-primary mx-1">Here</button></span>
	</p>
</div>

</#macro>

<@run />