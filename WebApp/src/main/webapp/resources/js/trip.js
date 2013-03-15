var tripId;


function setCurrentTheme(currentTheme, currentTripId){
    switch (currentTheme){
        case 'default' : $('#default').attr('checked', true);
            break;
        case 'blue' : $('#blue').attr('checked', true);
            break;
        case 'dark' : $('#dark').attr('checked', true);
            break;
    }
    tripId = currentTripId;
}




$(document).ready(function(){
    $('.rdb-theme').on('click', function(){
        $.ajax({
            type: "POST",
            url: "/editTripTheme/" + tripId,
            data: {theme : $(this).attr('value')}
        }).done(function(){
            });
        //$.post('/editTripTheme/' + tripId,  {theme : "blue"});
    });

    $('.trip-edit').hide();

    $('#btn-edit').on("click", function(){
        $('.trip-view').hide();
     /*   $('tr').each(function() {
            $(this).children(':last-child').children(':last-child').val($(this).children(':last-child').children(':first-child').text());
        });                       */
        $('.trip-edit').show();
    });

    $('#btn-save').on("click", function(){
        $('.trip-view').show();
        $('.trip-edit').hide();
    });

    $('#btn-cancel').on("click", function(){
        $('.trip-view').show();
        $('.trip-edit').hide();
    });
});

