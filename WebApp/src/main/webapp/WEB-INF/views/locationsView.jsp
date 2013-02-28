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

        <table>
            <thead>
            <tr>
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

            <tbody>
            <div>
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
                                </div>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
                <a href="/trip/${trip.id}/locations/createLocation">
                    <button type="button" id="btn-createLocation" class="btn-blue">Create Location</button>
                </a>
            </div>
            </form>
            </tbody>
        </table>
    </div>
</div>
</div>
</div>
</body>
</html>