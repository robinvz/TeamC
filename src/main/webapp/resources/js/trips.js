

$(document).ready(function(){
    $('#trips').fadeOut(0);
    $('#trips').slideDown(300);

    $("#trips tr").not(':first').hover(function(){
            $(this).addClass("tr-hover");
        },function(){
            $(this).removeClass("tr-hover");
        }
    );

    $("#trips tr").on('click', function(){
       window.location = '../html/Trip.html';
    });

});

