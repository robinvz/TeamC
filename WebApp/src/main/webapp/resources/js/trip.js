



$(document).ready(function(){
    $('.rdb-theme').on('click', function(){
        $.POST('',  $(this).attr('value'));
    });


});


function setCurrentTheme(currentTheme){
    switch (currentTheme){
        case 'default' : $('#default').attr('checked', true);
            break;
        case 'blue' : $('#blue').attr('checked', true);
            break;
        case 'dark' : $('#dark').attr('checked', true);
            break;
    }
}


