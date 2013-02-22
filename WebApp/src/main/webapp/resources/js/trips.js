$(document).ready(function(){
    $('#trips').fadeOut(0);
    $('#trips').slideDown(300);


    $("#trips tr").not(':first').on('click', function(){
      var  ref = '/trip/'+$(this).attr('id').substr(4);
      window.location = ref;
    });

});