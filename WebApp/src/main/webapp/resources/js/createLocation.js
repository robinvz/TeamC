var latCoord;
var lngCoord;
var map;
var geocoder;
var answerNumber = 1;
var tmpVal = '';
var usedAddressComponents = [];

$(document).ready(function () {
    $('#hiddenAnswer').hide();
    $('#addressfields input').attr('readonly', 'readonly');
    $('#btn-next').attr('disabled', 'disabled');
    btnListeners();
    initializeMap();
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
    $('#latitude').val(latCoord);
    $('#longitude').val(lngCoord);
    $('#btn-next').removeAttr('disabled');
    checkAddress();
}

function clearInputAddress() {
    $.each(usedAddressComponents, function (i, usedAddressComponent) {
        $('#' + usedAddressComponent).val('');
    });
    usedAddressComponents = [];
    $('#btn-next').attr('disabled', 'disabled');
}

function checkAddress() {
    if (usedAddressComponents.indexOf('sublocality') == -1) {
        if (usedAddressComponents.indexOf('locality') != -1) {
            $('#sublocality').val(usedAddressComponents[usedAddressComponents.indexOf('locality') + 1]);
        } else if (usedAddressComponents.indexOf('administrative_area_level_3') != -1) {
            $('#sublocality').val(usedAddressComponents[usedAddressComponents.indexOf('administrative_area_level_3') + 1]);
        } else if (usedAddressComponents.indexOf('administrative_area_level_2') != -1) {
            $('#sublocality').val(usedAddressComponents[usedAddressComponents.indexOf('administrative_area_level_2') + 1]);
        } else if (usedAddressComponents.indexOf('administrative_area_level_1') != -1) {
            $('#sublocality').val(usedAddressComponents[usedAddressComponents.indexOf('administrative_area_level_1') + 1]);
        }
    }
}

function btnListeners() {
    $('#btn-next').on('click', function () {
        $('#add-location').animate({marginLeft: '-=960'}, 1500, function () {
        });
    });

    $('#btn-back').on('click', function () {
        $('#add-location').animate({marginLeft: '+=960'}, 1500, function () {
        });
    });

    $('#btn-answer').on('click', function () {
        $('#answers').append('<label>Answer ' + answerNumber + '</label>');
        $('#answers').append('<input class="poss" name="possibleAnswers" value="' + $('#new-answer').val() + '"/>');
        $('#correct-answer').append('<option>' + $('#new-answer').val() + '</option>');
        $('#new-answer').val('');
        answerNumber += 1;
        $('.poss').attr('readonly','readonly');
        $('.poss').style.marginLeft = "4px";
    });
}