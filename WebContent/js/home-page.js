
function gotoLogin() {
    window.location.href = "/content/login";
}

function gotoRegister() {
    window.location.href = "/content/register";
}

window.onload = function() {
    $('#login').click(gotoLogin);
    $('#register').click(gotoRegister);
};
