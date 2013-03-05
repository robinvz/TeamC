<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/trips.css"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <title><spring:message code="TripsPage" /></title>
</head>
<body>
<div id="page">
    <jsp:include page="baseView.jsp"/>

    <h2><spring:message code="Overview" /></h2>
    <c:if test="${not empty user}">
        <nav class="inner-nav">
            <ul class="nav">
                <li id="btn-trips" class="inner-nav-link inner-selected"><spring:message code="ShowAllTrips" /></li>
                <li id="btn-trips-participating" class="inner-nav-link"><spring:message code="ShowEnrolledTrips" /></li>
                <li id="btn-trips-organised" class="inner-nav-link"><spring:message code="ShowOrganizedTrips" /></li>
            </ul>
        </nav>
    </c:if>

    <div id="control-bar">
        <c:if test="${not empty user}">
            <form action="/users/createTrip" method="GET">
                <button id="btn-addtrip" type="submit" class="btn-blue"><spring:message code="CreateTrip" /></button>
            </form>
        </c:if>
        <select id="filter" name="filterTripsMenu">
            <option><spring:message code="AllTrips" /></option>
            <option value=""><spring:message code="TimeLessTrips" /></option>
            <option value=""><spring:message code="TimeBoundTrips" /></option>
        </select>
    </div>

    <div id="content">
        <div id="inner-content">
            <table class="tables">
                <!-- model heeft momenteel: type, id, description, privacy, published, title, userId -->
                <thead>
                <tr>
                    <th><spring:message code="Title" /></th>
                    <th><spring:message code="Description" /></th>
                        <th><spring:message code="Type"/></th>
                    <th>Privacy</th>
                </tr>
                </thead>

                <tbody>
                <div>
                    <c:if test="${not empty allNonPrivateTrips}">
                        <c:forEach items="${allNonPrivateTrips}" var="allNonPrivateTrip">
                            <tr id="trip${allNonPrivateTrip.id}"
                                    <c:if test="${allNonPrivateTrip.class == 'class be.kdg.trips.model.trip.TimelessTrip'}">
                                        class="t-less"
                                    </c:if>
                                    <c:if test="${allNonPrivateTrip.class == 'class be.kdg.trips.model.trip.TimeBoundTrip'}">
                                       class="t-bound"
                                    </c:if>
                                    >
                                <div class="form-row">
                                    <td>
                                            ${allNonPrivateTrip.title}
                                    </td>
                                    <td>
                                            ${allNonPrivateTrip.description}
                                    </td>
                                    <td>
                                        <c:if test="${allNonPrivateTrip.class == 'class be.kdg.trips.model.trip.TimelessTrip'}">
                                            <spring:message code="Timeless" />
                                        </c:if>
                                        <c:if test="${allNonPrivateTrip.class == 'class be.kdg.trips.model.trip.TimeBoundTrip'}">
                                            <spring:message code="TimeBound" />
                                        </c:if>
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
                            <tr id="trip${allPrivateTrip.id}"
                                    <c:if test="${allPrivateTrip.class == 'class be.kdg.trips.model.trip.TimelessTrip'}">
                                        class="t-less"
                                    </c:if>
                                    <c:if test="${allPrivateTrip.class == 'class be.kdg.trips.model.trip.TimeBoundTrip'}">
                                        class="t-bound"
                                    </c:if>
                                    >
                                <div class="form-row">
                                    <td>
                                            ${allPrivateTrip.title}
                                    </td>
                                    <td>
                                            ${allPrivateTrip.description}
                                    </td>
                                    <td>
                                        <c:if test="${allPrivateTrip.class == 'class be.kdg.trips.model.trip.TimelessTrip'}">
                                            <spring:message code="Timeless" />
                                        </c:if>
                                        <c:if test="${allNonPrivateTrip.class == 'class be.kdg.trips.model.trip.TimeBoundTrip'}">
                                            <spring:message code="TimeBound" />
                                        </c:if>
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
            <table class="tables">
                <!-- model heeft momenteel: type, id, description, privacy, published, title, userId -->
                <thead>
                <tr>
                    <th>Trip</th>
                    <th><spring:message code="Description" /></th>
                    <th>Privacy</th>
                    <th><spring:message code="SubscriptionDate" /></th>
                </tr>
                </thead>

                <tbody>
                    <div>
                        <c:if test="${not empty allEnrollments}">
                        <c:forEach items="${allEnrollments}" var="enrollment">
                            <tr id="trip${enrollment.trip.id}"
                                    <c:if test="${enrollment.trip.class == 'class be.kdg.trips.model.trip.TimelessTrip'}">
                                        class="t-less"
                                    </c:if>
                                    <c:if test="${enrollment.trip.class == 'class be.kdg.trips.model.trip.TimeBoundTrip'}">
                                        class="t-bound"
                                    </c:if>
                                    >
                                <div class="form-row">
                                    <td>
                                            ${enrollment.trip.title}
                                    </td>
                                    <td>
                                            ${enrollment.trip.description}
                                    </td>
                                    <td>
                                            ${enrollment.trip.privacy}
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
            <table class="tables">
                <!-- model heeft momenteel: type, id, description, privacy, published, title, userId -->
                <thead>
                <tr>
                    <th>Title</th>
                    <th><spring:message code="Description" /></th>
                    <th>Privacy</th>
                    <th><spring:message code="Published" /></th>
                </tr>
                </thead>

                <tbody>
                <div>
                    <c:if test="${not empty allOrganizedTrips}">
                        <c:forEach items="${allOrganizedTrips}" var="organizedTrip">
                            <tr id="trip${organizedTrip.id}"
                                    <c:if test="${organizedTrip.class == 'class be.kdg.trips.model.trip.TimelessTrip'}">
                                        class="t-less"
                                    </c:if>
                                    <c:if test="${organizedTrip.class == 'class be.kdg.trips.model.trip.TimeBoundTrip'}">
                                        class="t-bound"
                                    </c:if>
                                    >

                                <div class="form-row">
                                    <td>
                                            ${organizedTrip.title}
                                    </td>
                                    <td>
                                            ${organizedTrip.description}
                                    </td>
                                    <td>
                                            ${organizedTrip.privacy}
                                    </td>
                                    <td>
                                        <c:if test="${organizedTrip.published == true}">
                                            <img src="${pageContext.request.contextPath}/resources/res/img/icons/checked.png" alt="Yes" title="Published">
                                        </c:if>
                                        <c:if test="${organizedTrip.published == false}">
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
<script src="${pageContext.request.contextPath}/resources/js/jquery.dataTables.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.8.24/jquery-ui.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/jquery.dataTables.rowReordering.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/trips.js"></script>
</body>
</html>