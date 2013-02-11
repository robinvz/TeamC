<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/trips.css" />
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <title>Trip overview</title>
</head>
<body>
<div id="page">
    <jsp:include page="header.jsp" />

    <div id="trip-pic">
        <h2>Overview</h2>
    </div>

    <div id="content">
        <table id="trips">
            <thead>
            <tr>
                <th>Trip</th>
                <th>Locatie</th>
                <th>Tijdstip</th>
            </tr>
            </thead>
            <tbody id="trips-list">

            <c:if test="${not empty tripsList}">
                <c:forEach items="${sessionScope.tripsList}" var="trip">
                    <form action="/selectTrip" method="GET">
                        <fieldset>
                            <tr>
                            <div class="form-row">
                                <td><label>Naam: ></label></td>
                                <td><output>${trip.naam}></output></td>
                                <td><label>Locatie: ></label></td>
                                <td><output>${trip.locatie}></output></td>
                                <td><label>Tijdstip: ></label></td>
                                <td><output>${trip.tijdstip}></output></td>
                            </div>
                            <div class="form-buttons">
                                <div class="button">
                                    <td><input name="submit" type="submit" value="Select" /></td>
                                </div>
                            </div>
                        </fieldset>
                        </tr>
                    </form>
                </c:forEach>
            </c:if>

            <tr>
                <td>tripnaam</td>
                <td>Antwerpen</td>
                <td>01/01/2013 - 02/01/2013</td>
                <td>Openbaar</td>
            </tr>
            <tr>
                <td>tripnaam</td>
                <td>Antwerpen</td>
                <td>01/01/2013 - 02/01/2013</td>
                <td>Openbaar</td>
            </tr>
            <tr>
                <td>tripnaam</td>
                <td>Antwerpen</td>
                <td>01/01/2013 - 02/01/2013</td>
                <td>Openbaar</td>
            </tr>
            </tbody>
        </table>
    </div>

    <footer><p class="footer">Trips - 2013</p></footer>
</div>
</body>
<script src="${pageContext.request.contextPath}/resources/js/jquery-1.9.0.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/knockout.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/trips.js"></script>
<!--[if lt IE 9]>
<script src="${pageContext.request.contextPath}/resources/js/html5shiv.js"></script>
<![endif]-->
</html>