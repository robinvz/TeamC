<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/trip.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/locations.css"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.1/themes/base/jquery-ui.css"/>

    <title>Locations page</title>
</head>
<body>
<div id="page">
    <jsp:include page="baseView.jsp"/>
    <jsp:include page="tripHeaderView.jsp"/>
    <div id="content">
        <h3><spring:message code="LocationsOverview"/></h3>
        <table id="example">
            <thead>
            <tr>
                <th></th>
                <th><spring:message code="Title"/></th>
                <th><spring:message code="Description"/></th>
                <th><spring:message code="Street"/></th>
                <th><spring:message code="HouseNr"/></th>
                <th><spring:message code="City"/></th>
                <th><spring:message code="Province"/></th>
                <th><spring:message code="PostalCode"/></th>
                <th><spring:message code="Country"/></th>
                <th></th>
            </tr>
            </thead>
            <tfoot>
            <tr>
                <th></th>
                <th><spring:message code="Title"/></th>
                <th><spring:message code="Description"/></th>
                <th><spring:message code="Street"/></th>
                <th><spring:message code="HouseNr"/></th>
                <th><spring:message code="City"/></th>
                <th><spring:message code="Province"/></th>
                <th><spring:message code="PostalCode"/></th>
                <th><spring:message code="Country"/></th>
                <th></th>
            </tr>
            </tfoot>
            <tbody>

            <c:choose>
                <c:when test="${empty locations}">
                    <h3><spring:message code="NoLocations"/>.</h3>
                </c:when>
                <c:otherwise>
                    <c:set var="count" value="0" scope="page"/>
                    <c:forEach items="${locations}" var="location">
                        <tr id="${trip.id}-${location.id}">
                            <td>
                                <c:set var="count" value="${count + 1}" scope="page"/>
                                <c:out value="${count}"></c:out>
                            </td>
                            <td>
                                    ${location.title}
                            </td>
                            <td>
                                    ${location.description}
                            </td>
                            <td>
                                    ${location.getAddress().street}
                            </td>
                            <td>
                                    ${location.getAddress().houseNr}
                            </td>
                            <td>
                                    ${location.getAddress().city}
                            </td>
                            <td>
                                    ${location.getAddress().province}
                            </td>
                            <td>
                                    ${location.getAddress().postalCode}
                            </td>
                            <td>
                                    ${location.getAddress().country}
                            </td>
                            <td>
                                <a href="/trip/${trip.id}/locations//${location.id}/deleteLocation">
                                    <button type="button" id="btn-deleteLocation"><spring:message code="Delete"/>
                                    </button>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
            <a href="/trip/${trip.id}/locations/createLocation">
                <button type="button" id="btn-createLocation" class="btn-blue"><spring:message
                        code="CreateLocation"/></button>
            </a>
            <button type="button" id="btn-toggleLocations" onclick="getLatLng(${trip.id})" class="btn-blue">
                <spring:message code="MapOverview"/>
            </button>
            </tbody>
        </table>
        <div id="mapcanvas" class="map-canvas"></div>
    </div>
</div>
<script src="http://code.jquery.com/jquery-1.8.3.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/jquery.dataTables.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.8.24/jquery-ui.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/jquery.dataTables.rowReordering.js"></script>
<script src="http://maps.googleapis.com/maps/api/js?sensor=false"></script>
<script src="${pageContext.request.contextPath}/resources/js/locations.js"></script>

<script>
    $(document).ready(function () {
        $('#example').dataTable({ 'bFilter': false, "bLengthChange": false, "bPaginate": false, "bInfo": false, "bAutoWidth": false }).rowReordering({ sURL: "/trip/switchLocation" });
    });
</script>

</body>
</html>