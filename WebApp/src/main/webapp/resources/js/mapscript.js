var latCoord;
var lngCoord;
var map;
var geocoder;
var answerNumber = 1;
var tmpVal = '';
var usedAddressComponents = [];

$(document).ready(function () {
    initializeMap();

    $('#btn-next').on('click', function () {
        $('#add-location').animate({marginLeft: '-=960'}, 1500, function () {
        });
    });

    $('#btn-answer').on('click', function () {
        $('#answers').append('<label>Answer ' + answerNumber + '</label>');
        $('#answers').append('<input name="possibleAnswers" value="' + tmpVal + '"/>')
        $('#new-answer').val('');
        $('#correct-answer').append('<option>' + tmpVal + '</option>')
        $("#new-answer").val('leeg');
        answerNumber += 1;
    });

    $('#new-answer').on('blur', function () {
        tmpVal = $('#new-answer').val();
        $('#new-answer').val('');
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
    google.maps.event.addListener(map, 'click', function (event) {
        placeMarker(event.latLng);
    });

    var marker;

    function placeMarker(location) {
        if (marker) { //verify if the marker exists
            marker.setPosition(location); //cange the position
        } else {
            marker = new google.maps.Marker({ //create a marker
                position: location,
                map: map
            });
        }
        latCoord = location.lat();
        lngCoord = location.lng();
        getAddress(location);
    }

    function getAddress(latLng) {
        clearInputAddress();
        geocoder.geocode({'latLng': latLng},
            function (results, status) {
                if (status == google.maps.GeocoderStatus.OK) {
                    if (results[0]) {
                        $.each(results[0].address_components, function (i, component) {
                            usedAddressComponents.push(component.types[0], component.long_name);
                        });
                        fillInputAddress();
                        //checkAddress();
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

function fillInputAddress() {
    $.each(usedAddressComponents, function (i, usedAddressComponent) {
        $("#" + usedAddressComponent).val(usedAddressComponents[i + 1]);
    });
    $("#latitude").val(latCoord);
    $("#longitude").val(lngCoord);
}

function clearInputAddress() {
    $.each(usedAddressComponents, function (i, usedAddressComponent) {
        $("#" + usedAddressComponent).val("");
    });
    usedAddressComponents = [];
}


//adres checken op lege of andere velden
// (sublocality wordt soms administrative_area_level
/*function checkAddress() {
 }        */