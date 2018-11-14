<#include "base.ftl">

<#macro head>
	<script>
	window.onload=function() {
		$('#home').click(function() {
			window.location.href = '/content/home-page';
		});
	}
	</script>
</#macro>

<#macro body>

<div class="jumbotron mt-5">
	<h1>404</h1>
	<hr />
	<p class="">
		That page doesn't exist on this server. Why don't you try
		<span><button id="home" class="btn btn-primary mx-1">Going Back Home</button></span>
	</p>
</div>

</#macro>

<@run/>