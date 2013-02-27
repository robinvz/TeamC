$(document).ready(function(){
    $('.optionsTimeBound').hide();

    $('#checkTimeBound').on("click", function() {
        if($('#checkTimeBound').is(':checked')){
            $('.optionsTimeBound').show('600');
            $('#form-createTrip').attr("action", "/createTimeBoundTrip");
        }else{
            $('.optionsTimeBound').hide('600');
            $('#form-createTrip').attr("action", "/createTimeLessTrip");
        }
    });

});