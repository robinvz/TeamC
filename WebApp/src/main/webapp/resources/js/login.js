

$(document).ready(function(){
    $('#logintable').hide();

    $('#btn-login').on('click', function()
    {
        $('#login-buttons').css({opacity: 1.0}).animate({opacity: 0}, 300, function()
        {
            $('#login-buttons').css({ display:"none"});
            $('#logintable').css({opacity: 0.0, display: "block"}).animate({opacity: 1.0}, 300);
        });
    });

});

