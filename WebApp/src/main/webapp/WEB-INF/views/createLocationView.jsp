<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/trip.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/createLocation.css">
    <link rel="stylesheet" media="(max-width:1040px)" href="${pageContext.request.contextPath}/resources/css/main-responsive.css">
    <link rel="stylesheet" media="(min-width:1039px)" href="${pageContext.request.contextPath}/resources/css/responsive-fix.css">
    <link rel="stylesheet" href="<spring:theme code="css"/>" type="text/css"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <title><spring:message code="CreateLocationPage"/></title>
</head>
<body>
<div id="page">
    <jsp:include page="baseView.jsp"/>
    <jsp:include page="tripHeaderView.jsp"/>
    <div id="content">
        <div id="inner-content">
            <div id="add-location">
                <form:form action="createLocation" method="POST" enctype="multipart/form-data">
                    <div class="page">
                        <div id="mapcanvas" class="map-canvas"></div>
                        <h3><spring:message code="SelectLocation"/></h3>

                        <div id="addressfields">
                            <div id="latlng">
                                <input id="latitude" name="latitude" type="text">
                                <input id="longitude" name="longitude" type="text">
                            </div>
                            <label><spring:message code="Street"/>: </label>
                            <input id="route" name="street" type="text">
                            <label><spring:message code="HouseNr"/>: </label>
                            <input id="street_number" name="houseNr" type="text">
                            <label><spring:message code="City"/>: </label>
                            <input id="sublocality" name="city" type="text">
                            <label><spring:message code="PostalCode"/>: </label>
                            <input id="postal_code" name="postalCode" type="text">
                            <label><spring:message code="Country"/>: </label>
                            <input id="country" name="country" type="text">
                            <button id="btn-next" type="button"><spring:message code="Next"/></button>
                        </div>
                    </div>
                    <div class="page page-right">
                        <h3><spring:message code="EnterLocationDetails"/></h3>

                        <div id="questionfields">
                            <label><spring:message code="Title"/>: </label>
                            <input id="location-title" name="title" type="text">
                            <label><spring:message code="Description"/>: </label>
                            <textarea id="location-description" name="description" type="text"></textarea>
                            <label><spring:message code="Question"/>: </label>
                            <input id="location-question" name="question" type="text">
                            <input id="btn-picture" type="file" name="file" value="<spring:message code="LocationPicture"/>"/>
                            <div id="answers">
                                <input id="hiddenAnswer" name="possibleAnswers"/>
                            </div>
                            <label><spring:message code="AddAnswer"/>: </label>
                            <input id="new-answer" type="text">
                            <button id="btn-answer" type="button"><spring:message code="Save"/></button>
                            <label><spring:message code="SelectCorrectAnswer"/>: </label>
                            <select id="correct-answer" name="correctAnswer">
                                <option hidden="hidden"></option>
                            </select>
                            <button id="btn-back" type="button"><spring:message code="Back"/></button>
                            <button id="btn-submit" type="submit"><spring:message code="Submit"/></button>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/resources/js/jquery-1.9.0.min.js"></script>
<script src="http://maps.googleapis.com/maps/api/js?sensor=false"></script>
<script src="${pageContext.request.contextPath}/resources/js/createLocation.js"></script>
</body>
</html>