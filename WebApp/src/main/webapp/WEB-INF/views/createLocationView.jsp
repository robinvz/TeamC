<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/trip.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/maps.css">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <title>Create location page</title>
</head>
<body>
<div id="page">
    <jsp:include page="baseView.jsp"/>
    <jsp:include page="tripHeaderView.jsp"/>
    <div id="inner-content">
        <div id="add-location">
            <form:form action="createLocation" method="POST">
                <div class="page">
                    <h3>Select the location of your point of interest</h3>

                    <div id="mapcanvas" class="map-canvas"></div>
                    <div id="addressfields">
                        <div id="latlng">
                            <input id="latitude" name="latitude" type="text">
                            <input id="longitude" name="longitude" type="text">
                        </div>
                        <label>Street: </label>
                        <input id="route" name="street" type="text">
                        <label>Number: </label>
                        <input id="street_number" name="houseNr" type="text">
                        <label>City: </label>
                        <input id="sublocality" name="city" type="text">
                        <label>Province: </label>
                        <input id="province" name="province" type="text">
                        <label>Postal Code: </label>
                        <input id="postal_code" name="postalCode" type="text">
                        <label>Country: </label>
                        <input id="country" name="country" type="text">
                        <button id="btn-next" type="button">Next</button>
                    </div>
                </div>
                <div class="page page-right">
                    <h3>Please enter details for this point of interest</h3>
                    <label>Title: </label>
                    <input id="location-title" name="title" type="text">
                    <label>Description: </label>
                    <textarea id="location-description" name="description" type="text"></textarea>
                    <label>Question: </label>
                    <input id="location-question" name="question" type="text">

                    <div id="answers">
                    </div>
                    <label>Add a new answer: </label>
                    <input id="new-answer" type="text">
                    <button id="btn-answer" type="button">Save</button>
                    <label>Select the correct answer: </label>
                    <select id="correct-answer" name="correctAnswer">
                        <option></option>
                    </select>
                    <button id="btn-submit" type="submit">Submit</button>
                </div>
            </form:form>
        </div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/resources/js/jquery-1.9.0.min.js"></script>
<script src="http://maps.googleapis.com/maps/api/js?sensor=false"></script>
<script src="${pageContext.request.contextPath}/resources/js/mapscript.js"></script>
</body>
</html>