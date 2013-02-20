
var latCoord;
var lngCoord;
var map;
var geocoder;
var answerNumber = 1;
var tmpVal = '';

$(document).ready(function(){
    initializeMap();

    $('#btn-next').on('click', function(){
        $('#add-location').animate({marginLeft: '-=960'}, 700, function(){
        }) ;
    });

    $('#btn-answer').on('click', function(){
        if(tmpVal != ''){
            $('#answers').append('<label>Answer '+ answerNumber+'</label>');
            $('#answers').append('<input value="'+ tmpVal +'"/>')
            $('#new-anwser').val('');
            $('#correct-answer').append('<option>'+ tmpVal +'</option>')
            $("#new-answer").val('leeg');
            tmpVal = '';
            answerNumber +=1;
        }

    });

    $('#new-anwser').on('blur', function(){
        tmpVal = $('#new-anwser').val();
        $('#new-anwser').val('');
    });

    $('#location-question').on("keyup", function() {
        if($('#location-question').val() == ''){
            $('#enable-answer').slideUp(500);
        }
        else{
            $('#enable-answer').slideDown(500);
        }
    });


});

function initializeMap() {
    var mapDiv = $('#mapcanvas')[0];
    map = new google.maps.Map(mapDiv, {
        center: new google.maps.LatLng(51.21788, 4.3994),
        zoom: 13, minZoom: 1,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    });
    geocoder = new google.maps.Geocoder();
    google.maps.event.addListener(map, 'click', function(event) {
        placeMarker(event.latLng);
    });

    var marker;
    function placeMarker(location) {
        if(marker){ //verify if the marker exists
            marker.setPosition(location); //cange the position
        }else{
            marker = new google.maps.Marker({ //create a marker
                position: location,
                map: map
            });
        }
        latCoord=location.lat();
        lngCoord=location.lng();
        getAddress(location);
    }

    function getAddress(latLng) {
        clearInputAddress();
        geocoder.geocode({'latLng':latLng},
            function (results, status) {
                if (status == google.maps.GeocoderStatus.OK) {
                    if (results[0]) {
                        $.each(results[0].address_components, function (i, component) {
                            fillInputAddress(component.types[0], component.long_name);
                            if ($("#sublocality").val() == "" && component.types[0] == "locality") {
                                $("#sublocality").val(component.long_name);
                            }
                        });
                    }
                    else {
                        alert('No address found.');
                    }
                }
                else {
                    alert('status: ' + status);
                }
            });
    }
}

function fillInputAddress(type, value) {
    $.each(usedAddressComponents, function (i, usedAddressComponent) {
        if (usedAddressComponent == type) {
            $("#" + usedAddressComponent).val(value);
        }
    });
}

function clearInputAddress() {
    $.each(usedAddressComponents, function (i, cmp) {
        $("#" + cmp).val("");
    });
}
