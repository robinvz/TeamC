$(document).ready(function(){

    $('.profile-edit').hide();

    $('#btn-edit').on("click", function(){
            $('.profile-view').hide();
            $('tr').each(function() {
                $(this).children(':last-child').children(':last-child').val($(this).children(':last-child').children(':first-child').text());
            });
            $('.profile-edit').show();
    });

    $('#btn-save').on("click", function(){
        $('.profile-view').show();
        $('.profile-edit').hide();
    });

    $('#btn-cancel').on("click", function(){
        $('.profile-view').show();
        $('.profile-edit').hide();
    });



});

//<form action="/users/editProfile" method="POST">