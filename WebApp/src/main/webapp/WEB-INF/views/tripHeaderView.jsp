<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div id="trip-header">
    <h2>${trip.title}</h2>
</div>
<aside class="above-footer">
    <nav class="trip-nav">
        <h3>Trip</h3>
        <ul class="trip-nav">
            <li><a href="#"><spring:message code="General"/></a></li>
            <li><a href="#"><spring:message code="Requirements"/></a></li>
            <li><a href="/trip/${trip.id}/locations"><spring:message code="Locations"/></a></li>
            <li><a href="#">Chat</a></li>
            <c:if test="${trip.privacy != 'PUBLIC'}">
                <li><a href="#"><spring:message code="Participants"/></a></li>
            </c:if>
            <li><a href="#"><spring:message code="Results"/></a></li>
            <li><a href="#"><spring:message code="Edit"/></a></li>

            <c:if test="${not empty user && trip.organizer == user && trip.published == false}">
                <li>
                    <a href="/publishTrip/${trip.id}">
                        <spring:message code="Publish"/>
                    </a>
                </li>
            </c:if>
            <c:if test="${not empty user && trip.organizer == user}">
                <li>
                    <a href="/deleteTrip/${trip.id}">
                        <spring:message code="Delete"/>
                    </a>
                </li>
            </c:if>
        </ul>
    </nav>
</aside>