<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery-1.9.0.min.js"></script>
<!--[if lt IE 9]>
<script src="${pageContext.request.contextPath}/resources/js/html5shiv.js"></script>
<![endif]-->
<script type="text/javascript">
    $(document).ready(function () {
        var enrolled = null;
        $.ajax({
            type: "GET",
            url: "/isEnrolled/${trip.id}",
            success: function (data) {
                enrolled = data;
                enrolled ? $('#button').addClass('on') : $('#button').removeClass('on');
                enrolled ? $('#button2').addClass('on') : $('#button2').removeClass('on');
            }
        });
        $('#button').on('click', function () {
            if (enrolled != null) {
                if (enrolled) {
                    $.ajax({
                        type: "POST",
                        url: "/unSubscribe",
                        data: {tripId: ${trip.id}}
                    }).done(function () {
                                window.location = '?';
                            });
                    enrolled = false;
                } else {
                    $.ajax({
                        type: "POST",
                        url: "/subscribe",
                        data: {tripId: ${trip.id}}
                    }).done(function () {
                                window.location = '?';
                            });
                    enrolled = true;
                }

                $(this).toggleClass('on');
            }
        });
        $('#button2').on('click', function () {
            if (enrolled != null) {
                if (enrolled) {
                    $.ajax({
                        type: "POST",
                        url: "/declineInvitation",
                        data: {tripId: ${trip.id}}
                    }).done(function () {
                                window.location = '?';
                            });
                    enrolled = false;
                } else {
                    $.ajax({
                        type: "POST",
                        url: "/acceptInvitation",
                        data: {tripId: ${trip.id}}
                    }).done(function () {
                                window.location = '?';
                            });
                    enrolled = true;
                }

                $(this).toggleClass('on');
            }
        });
    });
</script>


<canvas id="myCanvas" width="960" height="200"></canvas>
<script>
    var canvas = document.getElementById('myCanvas');
    var context = canvas.getContext('2d');
    var imageObj = new Image();
    var x = canvas.width / 2;
    var y = canvas.height / 2;

    context.font = '40pt Calibri';
    context.textAlign = 'center';
    context.fillStyle = '#FFFFFF';
    context.fillText('${trip.title}', x, y);
    imageObj.onload = function () {
        context.drawImage(imageObj, 0, 0);
        context.fillText('${trip.title}', x, y);
    };
    imageObj.src = '/tripPic/${trip.id}';
</script>
<c:set value="false" var="validTrip"/>
<c:if test="${not empty user  && trip.published==true && trip.organizer != user && trip.privacy == 'PROTECTED'}">
    <c:set value="true" var="validTrip"/>
</c:if>
<c:set value="false" var="validPrivateTrip"/>
<c:if test="${not empty user  && trip.published==true && trip.privacy == 'PRIVATE'}">
    <c:set value="true" var="validPrivateTrip"/>
</c:if>

<c:if test="${validTrip == true}">
    <section class="attending">
        <a href="#" id="button" class="btn-attend"><spring:message code="Attending"></spring:message></a>
        <span class="attending-light"></span>
    </section>
</c:if>
<c:if test="${validPrivateTrip == true}">
    <section class="attending">
        <a href="#" id="button2" class="btn-attend"><spring:message code="Attending"></spring:message></a>
        <span class="attending-light"></span>
    </section>
</c:if>

<aside class="above-footer">
    <nav class="trip-nav">
        <h3>Trip</h3>
        <ul class="trip-nav">
            <li><strong>Trip info</strong></li>
            <li class="jump-in"><a href="/trip/${trip.id}"><spring:message code="General"/></a></li>
            <c:if test="${trip.privacy == 'PUBLIC' && empty user}">
                <li class="jump-in"><a href="/trip/${trip.id}/locations"><spring:message code="Locations"/></a></li>
                <li class="jump-in"><a href="/requirements/${trip.id}"><spring:message code="Requisites"/></a></li>
            </c:if>
            <c:if test="${not empty user}">
                <li class="jump-in"><a href="/trip/${trip.id}/locations"><spring:message code="Locations"/></a></li>
                <li class="jump-in"><a href="/requirements/${trip.id}"><spring:message code="Requisites"/></a></li>
                <c:if test="${trip.privacy != 'PUBLIC' && trip.published == true}">
                    <li class="jump-in"><a href="/trip/${trip.id}/participants"><spring:message
                            code="Participants"/></a></li>
                    <c:forEach items="${trip.enrollments}" var="enrollment">
                        <c:if test="${enrollment.user == user && enrollment.status == 'READY' || 'FINISHED'}">
                            <li class="jump-in"><a href="/costs/${trip.id}"><spring:message code="Costs"/></a></li>
                            <li class="jump-in"><a href="/startTrip/${tripId}">Start trip</a></li>
                        </c:if>
                        <c:if test="${enrollment.user == user && enrollment.status == 'BUSY'}">
                            <li class="jump-in"><a href="/stopTrip/${tripId}">Stop trip</a></li>
                        </c:if>
                    </c:forEach>
                </c:if>
                <c:if test="${trip.organizer == user}">
                    <li><strong>Admin Tools</strong></li>
                    <li class="jump-in"><a href="/users/labels/${trip.id}">Labels</a></li>
                    <li class="jump-in"><a href="/editTripPic/${trip.id}"><spring:message code="EditTripHeader"/></a>
                    </li>
                    <c:if test="${trip.published == false}">
                        <li class="jump-in"><a href="/users/publishTrip/${trip.id}"><spring:message code="Publish"/></a>
                        </li>
                    </c:if>
                    <c:if test="${trip.privacy == 'PRIVATE'}">
                        <li class="jump-in"><a href="/inviteUser/${trip.id}"><spring:message code="InviteUsers"/></a>
                        </li>
                    </c:if>
                    <c:if test="${trip.timeBoundTrip==true}">
                        <li class="jump-in"><a href="/users/addDate/${trip.id}"><spring:message code="AddDate"/></a>
                        </li>
                    </c:if>
                    <li class="jump-in"><a href="/users/deleteTrip/${trip.id}"><spring:message code="Delete"/></a></li>
                </c:if>
            </c:if>
        </ul>
    </nav>
</aside>
