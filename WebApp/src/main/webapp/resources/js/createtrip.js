$(document).ready(function(){
    $('.optionsTimeBound').hide();
    $('#amount').hide();

    $('#checkTimeBound').on("click", function() {
        if($('#checkTimeBound').is(':checked')){
            $('.optionsTimeBound').show('600');
            $('#form-createTrip').attr("action", "/createTimeBoundTrip");
        }else{
            $('.optionsTimeBound').hide('600');
            $('#form-createTrip').attr("action", "/createTimeLessTrip");
        }
    });

    $("[name='repeat']").on("click", function() {
        if($(this).val() === 'ONCE'){
            $('#amount').hide('600');
        }else{
            $('#amount').show('600');
        }
    });

});