var tripId;
var map;
var locations = [];

function getTripId(id) {
    tripId = id;
    initDataTable();
    listeners();
    initializeMap();
}

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
    } else if (locations.length == 0) {
        map.setZoom(1);
        map.setCenter(new google.maps.LatLng(0, 0))
    } else {
        map.setZoom(12);
        map.setCenter(new google.maps.LatLng(locations[0].latitude, locations[0].longitude))
    }
}

function listeners() {
    $('#btn-toggleLocations').on('click', function () {
        if ($('#mapcanvas').is(":hidden")) {
            getLatLng();
            $(this).text('Text Overview');
            $('#locations-table').hide();
            $('#mapcanvas').show();
        } else {
            $(this).text('Map Overview');
            $('#mapcanvas').hide();
            $('#locations-table').show();
        }
    });

    $("table tr").not(':first').on('click', function () {
        var ref = '/trip/' + tripId + '/locations/' + $(this).attr('id').substr(9);
        window.location = ref;
    });
}

function getLatLng() {
    $.getJSON("/trip/" + tripId + "/locations/getLocationsLatLng?amount=all&locationId=0", function (coordinates) {
        $.each(coordinates, function (i, coordinate) {
            createMarker(coordinate.latitude, coordinate.longitude)
            locations[i] = coordinate;
        });
        zoomFit();
    });
}

function initDataTable() {
    $('#locations-table').dataTable({ 'bFilter': false, "bLengthChange": false, "bPaginate": false, "bInfo": false, "bAutoWidth": false })
        .rowReordering({ sURL: "/trip/switchLocation" })
        .makeEditable({sUpdateURL: "/trip/" + tripId + "/locations/editLocation",
            sReadOnlyCellClass: "read_only"});
}