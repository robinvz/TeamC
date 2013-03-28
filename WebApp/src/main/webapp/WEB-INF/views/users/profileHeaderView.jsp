<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<aside class="above-footer">
    <img id="profilepic" src="/users/profilePic" title="It's you!">

    <nav class="trip-nav">
        <ul class="trip-nav">
            <li><a href="/users/profile"><spring:message code="General" /></a></li>
            <li><a href="/users/viewTripsHistory"><spring:message code="TripsHistory" /></a></li>
            <li><a href="/users/editProfilePic"><spring:message code="EditPicture" /></a></li>
            <li><a href="/users/editCredentials"><spring:message code="EditPassword" /></a></li>
            <li>
                <form id="deleteProfileForm" action="/users/deleteProfile" method="get">
                    <button type="submit" id="deleteBtn" class="btn-blue" style="float: left;"><spring:message code="DeleteProfile" /></button>
                </form>
            </li>
        </ul>
    </nav>
</aside>