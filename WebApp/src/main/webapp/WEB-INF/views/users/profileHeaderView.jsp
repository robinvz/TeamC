<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<aside class="above-footer">
    <img id="profilepic" src="/users/profilePic" title="kjsfdkl">

    <nav class="trip-nav">
        <ul class="trip-nav">
            <li><a href="/users/profile"><spring:message code="General" /></a></li>
            <li><a href="#"><spring:message code="TripsHistory" /></a></li>
            <li><a href="/users/editProfilePic"><spring:message code="EditPicture" /></a></li>
            <li><a href="/users/editCredentials"><spring:message code="EditPassword" /></a></li>
            <li><a href="#"><spring:message code="ImportFb" /></a></li>
            <li>
                <form id="deleteProfileForm" action="/deleteProfile" method="get">
                    <button type="submit" id="deleteBtn" class="btn-blue"><spring:message code="DeleteProfile" /></button>
                </form>
            </li>
        </ul>
    </nav>
</aside>