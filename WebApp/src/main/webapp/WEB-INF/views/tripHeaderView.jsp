<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!--[if lt IE 9]>
<script src="${pageContext.request.contextPath}/resources/js/html5shiv.js"></script>
<![endif]-->


<canvas id="myCanvas" width="960" height="200"></canvas>
<script>
    var canvas = document.getElementById('myCanvas');
    var context = canvas.getContext('2d');
    var imageObj = new Image();
    var x = canvas.width / 2;
    var y = canvas.height / 2;

    context.font = '30pt Calibri';
    context.textAlign = 'center';
    context.fillStyle = '#FFFFFF';
    context.fillText('${trip.title}', x, y);
    imageObj.onload = function() {
        context.drawImage(imageObj, 0, 0);
        context.fillText('${trip.title}', x, y);
    };
    imageObj.src = '/tripPic/${trip.id}';
</script>

<aside class="above-footer">
    <nav class="trip-nav">
        <h3>Trip</h3>
        <ul class="trip-nav">
            <li><a href="/trip/${trip.id}"><spring:message code="General"/></a></li>
            <li><a href="/requirements/${trip.id}"><spring:message code="Requisites"/></a></li>
            <li><a href="/trip/${trip.id}/locations"><spring:message code="Locations"/></a></li>
            <li><a href="#">Chat</a></li>
            <li><a href="#"><spring:message code="Results"/></a></li>
            <c:if test="${trip.privacy != 'PUBLIC' && trip.published == true}">
                <li><a href="/trip/${trip.id}/participants"><spring:message code="Participants"/></a></li>
                <c:forEach items="${trip.enrollments}" var="enrollment">
                    <c:if test="${enrollment.user == user && enrollment.status == 'READY'}">
                        <li><a href="/startTrip/${tripId}">Start trip</a></li>
                    </c:if>
                    <c:if test="${enrollment.user == user && enrollment.status == 'BUSY'}">
                        <li><a href="/stopTrip/${tripId}">Stop trip</a></li>
                    </c:if>
                </c:forEach>
            </c:if>

            <c:if test="${not empty user && trip.organizer == user}">
                <li><a href="/labels/${trip.id}">Labels</a></li>
                <li><a href="/editTripPic/${trip.id}"><spring:message code="EditTripHeader"/></a></li>
                <c:if test="${trip.published == false}">
                    <li><a href="/publishTrip/${trip.id}"><spring:message code="Publish"/></a></li>
                </c:if>
                <c:if test="${trip.privacy == 'PRIVATE'}">
                    <li><a href="/inviteUser/${trip.id}"><spring:message code="InviteUsers"/></a></li>
                </c:if>
                <li><a href="/deleteTrip/${trip.id}"><spring:message code="Delete"/></a></li>
            </c:if>
        </ul>
    </nav>
</aside>
