<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/trip.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/locations.css"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <title>Locations page</title>
</head>
<body>
<div id="page">
    <jsp:include page="baseView.jsp"/>
    <jsp:include page="tripHeaderView.jsp"/>

    <div class="inner-content">
        <h3>Locations Overview</h3>
        <a href="/trip/${trip.id}/locations/createLocation">
            <button type="button" id="btn-createLocation" class="btn-blue">Create Location</button>
        </a>

        <button type="button" id="btn-toggleLocations" class="btn-blue">Map Overview</button>

        <table id="tbl-locations">
            <thead>
            <tr>
                <th>Title</th>
                <th>Description</th>
            </tr>
            </thead>

            <tbody>
            <c:choose>
                <c:when test="${empty locations}">
                    <h3>There are no locations.</h3>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${locations}" var="location">
                        <tr id="location${location.id}">
                            <div class="form-row">
                                <td>
                                        ${location.title}
                                </td>
                                <td>
                                        ${location.description}
                                </td>
                                <td>
                                    <a href="/trip/${trip.id}/locations/${location.id}/deleteLocation">
                                        <button type="button" id="btn-deleteLocation">Delete
                                        </button>
                                    </a>
                                </td>
                            </div>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
        <div id="mapcanvas" class="map-canvas"></div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/resources/js/jquery-1.9.0.min.js"></script>
<script src="http://maps.googleapis.com/maps/api/js?sensor=false"></script>
<script src="${pageContext.request.contextPath}/resources/js/locations.js"></script>
</body>
</html>