
function login() {
    $.ajax({
        url: '/api/login',
        method: 'POST',
        data: {
            username: $('#username').val(),
            password: $('#password').val()
        }
    })
    .done(function(d) {
        data = JSON.parse(d);
        if (data.failed) {
            $('#login-failed').show();
            return;
        }

        $('#login-failed').hide();
        window.location.href = '/content/' + data.userType + '/dashboard';
    })
    .fail(function() {
        $('#login-failed').show();
    })
    ;
}

window.onload = function() {
    $('#login-failed').hide();
    $('#login-button').click(login);
};
