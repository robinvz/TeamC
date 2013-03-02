var map;
var locations = [];
$(document).ready(function () {
    btnListeners();
    initializeMap();
});

function initializeMap() {
    map = new google.maps.Map(document.getElementById('mapcanvas'), {
        mapTypeId: google.maps.MapTypeId.ROADMAP});
}

function createMarker(lat, lng) {
    new google.maps.Marker({
        position: new google.maps.LatLng(lat, lng),
        map: map
    });
}

function zoomFit() {
    google.maps.event.trigger(map, 'resize');
    if (locations.length > 1) {
        var bounds = new google.maps.LatLngBounds();
        $.each(locations, function (i, location) {
            bounds.extend(new google.maps.LatLng(location.latitude, location.longitude));
        });
        map.fitBounds(bounds);
    }else if(locations.length == 0){
        map.setZoom(20);
        map.setCenter(new google.maps.LatLng(0,0))
    } else {
        map.setZoom(12);
        map.setCenter(new google.maps.LatLng(locations[0].latitude,locations[0].longitude))
    }
}

function btnListeners() {
    $('#btn-toggleLocations').on('click', function () {
        if ($('#mapcanvas').is(":hidden")) {
            $(this).text('Text Overview');
            $('#example').hide();
            $('#mapcanvas').show();
        } else {
            $(this).text('Map Overview');
            $('#mapcanvas').hide();
            $('#example').show();
        }
    });
}

function getLatLng(tripId) {
    $.getJSON("/trip/" + tripId + "/locations/getLocationsLatLng", function (coordinates) {
        $.each(coordinates, function (i, coordinate) {
            createMarker(coordinate.latitude, coordinate.longitude)
            locations[i] = coordinate;
        });
        zoomFit();
    });
}