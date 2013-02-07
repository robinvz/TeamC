

$(document).ready(function(){
    $('#login-facebook').on('click', function(){
        $('#login-buttons').hide('slow', function(){
            $('#form-register').css({display: "none"});
            $('#form-login').css({opacity: 0.0, display: "block"}).animate({opacity: 1.0}, 500);
            $('#login-title').text("Login with facebook").css({opacity: 0.0, display: "block"}).animate({opacity: 1.0}, 500);
        });
    });


    $('#register-facebook').on('click', function(){
        $('#login-buttons').hide('slow', function(){
            $('#form-login').css({ display: "none"});
            $('#form-register').css({opacity: 0.0, display: "block"}).animate({opacity: 1.0}, 500);
            $('#login-title').text("Sign up with facebook").css({opacity: 0.0, display: "block"}).animate({opacity: 1.0}, 500);
        });
    });

});

