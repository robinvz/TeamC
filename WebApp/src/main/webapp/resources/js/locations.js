var tripId;
var map;
var locations = [];

function getTripId(id) {
    tripId = id;
    initDataTable();
    listeners();
    initializeMap();
    listOfWaypoints();
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
        var ids = $(this).attr('id').split('-');
        var ref = '/trip/' + tripId + '/locations/' + ids[1];
        window.location = ref;
    });
}

function getLatLng() {
    $.getJSON("/trip/" + tripId + "/locations/getLocationsLatLng?amount=all&locationId=0", function (coordinates) {
        $.each(coordinates, function (i, coordinate) {
            //createMarker(coordinate.latitude, coordinate.longitude)
            locations[i] = coordinate;
        });
        zoomFit();
    });
}

function initDataTable() {
    $('#locations-table').dataTable({ 'bFilter': false, "bLengthChange": false, "bPaginate": false, "bInfo": false, "bAutoWidth": false })
        .rowReordering({ sURL: "/trip/switchLocation"});
}

function listOfWaypoints() {
    $('#directionsPanel').empty();
    $.getJSON("/trip/" + tripId + "/locations/getLocationsLatLng?amount=all&locationId=0", function (coordinates) {
        $.each(coordinates, function (i, coordinate) {
            locations[i] = coordinate;
        });
        var listOfListOfWaypoints = [];
        var MAXWAYPOINTS = 10; // google API max(1 start, 1 stop, 8 waypoints)
        var listCounter = 0;
        var locationsExist = locations.length > 0;

        while (locationsExist) {
            var listOfWaypoints = [];
            var subListCounter = 0;

            for (var i = listCounter; i < locations.length; i++) {
                subListCounter++;
                listOfWaypoints.push({
                    location: new window.google.maps.LatLng(locations[i].latitude, locations[i].longitude),
                    stopover: true
                });
                if (subListCounter == MAXWAYPOINTS) {
                    break;
                }
            }

            listCounter += subListCounter;
            listOfListOfWaypoints.push(listOfWaypoints);
            locationsExist = listCounter < locations.length;
            // If it runs again there are still points. Minus 1 before continuing to
            // start up with end of previous tour leg [[a,b,c,d],[d,e,f,g],[g,h]]
            listCounter--;
        }
        calcRoute(listOfListOfWaypoints);
    });
}

function calcRoute(listOfList) {
    var listOfListOfWaypoints = listOfList;
    var directionsService = new google.maps.DirectionsService();
    var directionsDisplay = new google.maps.DirectionsRenderer();
    directionsDisplay.setMap(map);
    directionsDisplay.setPanel(document.getElementById('directionsPanel'));
    var combinedResults;
    var directionsResultsReturned = 0;

    for (var i = 0; i < listOfListOfWaypoints.length; i++) {
        var lastIndex = listOfListOfWaypoints[i].length - 1;
        var start = listOfListOfWaypoints[i][0].location;
        var end = listOfListOfWaypoints[i][lastIndex].location;

        // trim last and first entry from array
        var waypts = listOfListOfWaypoints[i];
        waypts.splice(lastIndex, 1);
        waypts.splice(0, 1);

        var request = {
            origin: start,
            destination: end,
            waypoints: waypts,
            travelMode: window.google.maps.TravelMode.WALKING
        };
        directionsService.route(request, function (result, status) {
            if (status == window.google.maps.DirectionsStatus.OK) {
                if (directionsResultsReturned == 0) { // first bunch of results in. new up the combinedResults object
                    combinedResults = result;
                    directionsResultsReturned++;
                }
                else {
                    // only building up legs, overview_path, and bounds in my consolidated object. This is not a complete
                    // directionResults object, but enough to draw a path on the map, which is all I need
                    combinedResults.routes[0].legs = combinedResults.routes[0].legs.concat(result.routes[0].legs);
                    combinedResults.routes[0].overview_path = combinedResults.routes[0].overview_path.concat(result.routes[0].overview_path);

                    combinedResults.routes[0].bounds = combinedResults.routes[0].bounds.extend(result.routes[0].bounds.getNorthEast());
                    combinedResults.routes[0].bounds = combinedResults.routes[0].bounds.extend(result.routes[0].bounds.getSouthWest());
                    directionsResultsReturned++;
                }
                if (directionsResultsReturned == listOfListOfWaypoints.length) {
                    directionsDisplay.setDirections(combinedResults);
                }
            }
        });
    }
}