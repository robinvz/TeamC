<div id="trip-header">
    <h2>${trip.title}</h2>
</div>
<aside>
    <nav class="trip-nav">
        <h3>Trip</h3>
        <ul class="trip-nav">
            <li><a href="#">Info</a></li>
            <li><a href="#">Requirements</a></li>
            <li><a href="#">Stops</a></li>
            <li><a href="#">Chat</a></li>
            <li><a href="#">Participants</a></li>
            <li><a href="#">Results</a></li>
            <li><a href="#">Start</a></li>
            <li><a href="#">Edit</a></li>
            <li>
                <form id="deleteTripForm" action="/deleteTrip" method="GET">
                    <button type="submit" id="deleteBtn">Delete</button>
                </form>
            </li>
        </ul>
    </nav>
</aside>