<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/trip.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/locations.css"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.1/themes/base/jquery-ui.css" />

    <title>Locations page</title>
</head>
<body>
<div id="page">
    <jsp:include page="baseView.jsp"/>
    <jsp:include page="tripHeaderView.jsp"/>

    <div class="inner-content">
        <h3>Locations Overview</h3>
        <table id="example">
            <thead>
            <tr>
                <th></th>
                <th>Title</th>
                <th>Description</th>
                <th>Street</th>
                <th>Number</th>
                <th>City</th>
                <th>Province</th>
                <th>Postal Code</th>
                <th>Country</th>
            </tr>
            </thead>
            <tfoot>
            <tr>
                <th></th>
                <th>Title</th>
                <th>Description</th>
                <th>Street</th>
                <th>Number</th>
                <th>City</th>
                <th>Province</th>
                <th>Postal Code</th>
                <th>Country</th>
            </tr>
            </tfoot>
            <tbody>

            <c:choose>
                <c:when test="${empty locations}">
                    <h3>There are no locations.</h3>
                </c:when>
                <c:otherwise>
                    <c:set var="count" value="0" scope="page" />
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


                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
            <a href="/trip/${trip.id}/locations/createLocation">
                <button type="button" id="btn-createLocation" class="btn-blue">Create Location</button>
            </a>

            </form>
            </tbody>

        </table>
    </div>
</div>
<script src="http://code.jquery.com/jquery-1.8.3.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/jquery.dataTables.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.8.24/jquery-ui.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/jquery.dataTables.rowReordering.js"></script>

<script>
    $(document).ready(function(){
        $('#example').dataTable({ 'bFilter': false, "bLengthChange": false, "bPaginate": false, "bInfo": false, "bAutoWidth": false }).rowReordering({ sURL: "/trip/switchLocation" });
    });
</script>

</body>
</html>