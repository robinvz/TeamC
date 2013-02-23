<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <title>Create location page</title>
</head>
<body>
<div id="page">
    <jsp:include page="baseView.jsp"/>

    <div id="trip-header">
        <h2>${trip.naam}</h2>
    </div>

    <div id="content">
        <div id="add-location">
            <div class="page">
                <h2>Select the location of your point of interest</h2>

                <div id="mapcanvas" class="map-canvas"></div>
                <div id="addressfields">

                    <label>Street: </label>
                    <input id="route" type="text">
                    <label>Number: </label>
                    <input id="street_number" type="text">
                    <label>City: </label>
                    <input id="sublocality" type="text">
                    <label>Postcode: </label>
                    <input id="postal_code" type="text">
                    <label>Country: </label>
                    <input id="country" type="text">
                    <button id="btn-next">Next</button>
                </div>
            </div>
            <div class="page page-right">
                <h2>Please enter details for this point of interest</h2>
                <label>Title: </label>
                <input id="location-title" type="text">
                <label>Description: </label>
                <textarea id="location-description" type="text"></textarea>
                <label>Question: </label>
                <input id="location-question" type="text">

                <div id="answers">
                </div>
                <label>Add a new answer: </label>
                <input id="new-anwser" type="text">
                <button id="btn-answer">Save</button>
                <label>Select the correct answer: </label>
                <select id="correct-answer">
                    <option></option>
                </select>
            </div>
        </div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/resources/js/mapscript.js"></script>
</body>
</html>