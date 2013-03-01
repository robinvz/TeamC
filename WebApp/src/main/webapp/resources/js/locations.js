var map;

$(document).ready(function () {
    btnListeners();
    initializeMap();

    //voor elke location uit lijst do createMarker(lat, lng)
    //zoomFit();
});

function toggleOverview(locations) {
    for (var i = 0; i < locations.length; i++) {
        alert(locations[i]);
    }
}

function initializeMap() {
    var mapDiv = $('#mapcanvas')[0];
    map = new google.maps.Map(mapDiv, {
        center: new google.maps.LatLng(51.21788, 4.3994),
        zoom: 13, minZoom: 1,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    });
}

/*function createMarker(lat, lng) {
 var marker = new google.maps.Marker({
 position: new google.maps.LatLng(lat, lng),
 map: map
 });
 }

 function zoomFit() {
 var bounds = new google.maps.LatLngBounds();
 $.each(locations, function(i, location) {
 bounds.extend(new google.maps.LatLng(location.latitude, location.longitude));
 });
 map.fitBounds(bounds);
 } */

function btnListeners() {
    $('#btn-toggleLocations').on('click', function () {
        if ($('#mapcanvas').is(":hidden")) {
            $(this).text('Text Overview');
            $('#tbl-locations').hide();
            $('#mapcanvas').show();
        } else {
            $(this).text('Map Overview');
            $('#mapcanvas').hide();
            $('#tbl-locations').show();
        }
    });
}