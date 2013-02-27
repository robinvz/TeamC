$(document).ready(function(){
    $('.optionsTimeBound').hide();

    $('#checkTimeBound').on("click", function() {
        if($('#checkTimeBound').is(':checked')){
            $('.optionsTimeBound').slideDown('600');
            $('#form-createTrip').attr("action", "/createTimeBoundTrip");
        }else{
            $('.optionsTimeBound').slideUp('600');
            $('#form-createTrip').attr("action", "/createTimeLessTrip");
        }
    });

});