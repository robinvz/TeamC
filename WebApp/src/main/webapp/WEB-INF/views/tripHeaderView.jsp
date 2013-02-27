<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="trip-header">
    <h2>${trip.title}</h2>
</div>
<aside>
    <nav class="trip-nav">
        <h3>Trip</h3>
        <ul class="trip-nav">
            <li><a href="#">Info</a></li>
            <li><a href="#">Requirements</a></li>
            <li><a href="/createLocation">Stops</a></li>
            <li><a href="#">Chat</a></li>
            <li><a href="#">Participants</a></li>
            <li><a href="#">Results</a></li>
            <li><a href="#">Edit</a></li>

            <c:if test="${not empty user && trip.organizer == user && trip.published == false}">
            <li>
                <form id="publishTripForm" action="/publishTrip/${trip.id}" method="GET">
                    <button type="submit" id="publishBtn">Publish</button>
                </form>
            </li>
            </c:if>

            <li>
                <form id="deleteTripForm" action="/deleteTrip/${trip.id}" method="GET">
                    <button type="submit" id="deleteBtn">Delete</button>
                </form>
            </li>
        </ul>
    </nav>
</aside>