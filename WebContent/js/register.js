
function register() {

    var data = {
        username: $('#username').val(),
        password: $('#password').val(),
        email: $('#email').val(),
        accountType: $('#account-type-picker').selectpicker('val')
    };
    console.log(data);
    $.ajax({
        url: '/api/register',
        method: 'POST',
        data: data
    })
    .done(function(d) {
        data = JSON.parse(d);
        if (data.failed) {
            $('#registration-failed-msg').text(data.message);
            $('#registration-failed').show();
        } else {
            $('#login-failed').hide();
            window.location.href = '/content/' + data.accountType + '/dashboard';
        }
    })
    .fail(function() {
        $('#login-failed').show();
    })
    ;
}

window.onload = function() {
    $('#registration-failed').hide();
    $('#register-button').click(register);
};
