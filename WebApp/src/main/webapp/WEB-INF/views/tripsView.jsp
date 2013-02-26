<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/trips.css"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <title>Trip overview</title>
</head>
<body>
<div id="page">
    <jsp:include page="baseView.jsp"/>

    <h2>Overview</h2>
    <c:if test="${not empty user}">
        <nav class="inner-nav">
            <ul class="nav">
                <li id="btn-trips" class="inner-nav-link inner-selected">Display all trips</li>
                <li id="btn-trips-participating" class="inner-nav-link">Show my enrolled trips</li>
                <li id="btn-trips-organised" class="inner-nav-link">Show trips organised by me</li>
            </ul>
        </nav>
    </c:if>

    <div id="control-bar">
        <c:if test="${not empty user}">
            <form action="/users/createTrip" method="GET">
                <button id="btn-addtrip" type="submit" class="btn-submit">Create Trip</button>
            </form>
        </c:if>
        <select id="filter" name="filterTripsMenu">
            <option>Filter</option>
            <option value="">Repeating trips</option>
            <option value="">Timebound trips</option>
        </select>
    </div>

    <div id="content">
        <div id="inner-content">
            <table>
                <!-- model heeft momenteel: type, id, description, privacy, published, title, userId -->
                <thead>
                <tr>
                    <th>Title</th>
                    <th>Description</th>
                    <th>Privacy</th>
                </tr>
                </thead>

                <tbody>
                <div>
                    <c:if test="${not empty allNonPrivateTrips}">
                        <c:forEach items="${allNonPrivateTrips}" var="allNonPrivateTrip">
                            <tr id="trip${allNonPrivateTrip.id}">
                                <div class="form-row">
                                    <td>
                                            ${allNonPrivateTrip.title}
                                    </td>
                                    <td>
                                            ${allNonPrivateTrip.description}
                                    </td>
                                    <td>
                                            ${allNonPrivateTrip.privacy}
                                    </td>
                                </div>
                            </tr>
                        </c:forEach>
                    </c:if>
                    <c:if test="${not empty allPrivateTrips}">
                        <c:forEach items="${allPrivateTrips}" var="allPrivateTrip">
                            <tr id="trip${allPrivateTrip.id}">
                                <div class="form-row">
                                    <td>
                                            ${allPrivateTrip.title}
                                    </td>
                                    <td>
                                            ${allPrivateTrip.description}
                                    </td>
                                    <td>
                                            ${allPrivateTrip.privacy}
                                    </td>
                                </div>
                            </tr>
                        </c:forEach>
                    </c:if>
                </div>
                </form>
                </tbody>
            </table>
            <table >
                <!-- model heeft momenteel: type, id, description, privacy, published, title, userId -->
                <thead>
                <tr>
                    <th>Trip</th>
                    <th>Date</th>

                </tr>
                </thead>

                <tbody>
                    <div>
                        <c:if test="${not empty allEnrollments}">
                        <c:forEach items="${allEnrollments}" var="enrollment">
                            <tr id="trip${enrollment.trip.id}">
                                <div class="form-row">
                                    <td>
                                            ${enrollment.trip.title}
                                    </td>
                                    <td>
                                            ${enrollment.date}
                                    </td>
                                </div>
                            </tr>
                        </c:forEach>
                    </c:if>
                    </div>
                </tbody>
            </table>
            <table >
                <!-- model heeft momenteel: type, id, description, privacy, published, title, userId -->
                <thead>
                <tr>
                    <th>Title</th>
                    <th>Description</th>
                    <th>Privacy</th>
                    <th>Published</th>
                </tr>
                </thead>

                <tbody>
                <div>
                    <c:if test="${not empty allOrganisedTrips}">
                        <c:forEach items="${allOrganisedTrips}" var="organisedTrip">
                            <tr id="trip${organisedTrip.id}">
                                <div class="form-row">
                                    <td>
                                            ${organisedTrip.title}
                                    </td>
                                    <td>
                                            ${organisedTrip.description}
                                    </td>
                                    <td>
                                            ${organisedTrip.privacy}
                                    </td>
                                    <td>
                                        <c:if test="${organisedTrip.published == true}">
                                            <img src="${pageContext.request.contextPath}/resources/res/img/icons/checked.png" alt="Yes" title="Published">
                                        </c:if>
                                        <c:if test="${organisedTrip.published == false}">
                                            <img src="${pageContext.request.contextPath}/resources/res/img/icons/false.png" alt="No" title="Not Published">
                                        </c:if>

                                    </td>
                                </div>
                            </tr>
                        </c:forEach>
                    </c:if>
                </div>
                </tbody>
            </table>
        </div>
    </div>

</div>
<script src="${pageContext.request.contextPath}/resources/js/jquery-1.9.0.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/trips.js"></script>
</body>
</html>