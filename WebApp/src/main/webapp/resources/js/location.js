var tripId;
var locationId;
var latitude;
var longitude;
var map;
var answerNumber = 1;
var tmpVal = '';

function getIds(trip, location) {
    tripId = trip;
    locationId = location;
    getLatLng()
    updateCorrectAnswers();
}

function getLatLng() {
    $.getJSON("/trip/" + tripId + "/locations/getLocationsLatLng?amount=one&locationId=" + locationId, function (coordinates) {
        $.each(coordinates, function (i, coordinate) {
            latitude = coordinate.latitude;
            longitude = coordinate.longitude;
        });
        listeners();
        initializeMap();
        createMarker();
    });
}

function initializeMap() {
    var mapOptions = {
        center: new google.maps.LatLng(latitude, longitude),
        zoom: 12,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    map = new google.maps.Map(document.getElementById('locationCanvas'), mapOptions);
}

function createMarker() {
    new google.maps.Marker({
        position: new google.maps.LatLng(latitude, longitude),
        map: map
    });
}

function updateCorrectAnswers() {
    $('.correct-answer').empty();
    $('.answer').each(function () {
        $('.correct-answer').append('<option>' + $(this).val() + '</option>');
    });
}

function listeners() {
    $('.btn-answer').on('click', function () {
        if ($('#editQuestion').is(':hidden')) {
            $('#answersAdd').append('<label>Answer ' + answerNumber + '</label>');
            $('#answersAdd').append('<input class="answer" name="possibleAnswers" value="' + tmpVal + '"/>');
        } else {
            $('#answersEdit').append('<input class="answer" name="possibleAnswers" value="' + tmpVal + '"/>');
        }
        $('.new-answer').val('');
        answerNumber += 1;
        updateCorrectAnswers();
    });

    $('.new-answer').on('blur', function () {
        tmpVal = $('.new-answer').val();
        $('.new-answer').val('');
    });

    $('.answer').on('blur', function () {
        updateCorrectAnswers();
    });

    $('.btn-cancel').on('click', function () {
        location.reload();
    });

    $('#btn-toggleEditQuestion').on('click', function () {
        $('#showQuestion').hide();
        $('#editQuestion').show();
    })

    $('#btn-toggleEditLocation').on('click', function () {
        $('#showLocation').hide();
        $('#editLocation').show();
    })
}