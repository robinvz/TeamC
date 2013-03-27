<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/trip.css"/>
    <link rel="stylesheet" media="(max-width:1040px)" href="${pageContext.request.contextPath}/resources/css/main-responsive.css">
    <link rel="stylesheet" media="(min-width:1039px)" href="${pageContext.request.contextPath}/resources/css/responsive-fix.css">
    <link rel="stylesheet" href="<spring:theme code="css"/>" type="text/css"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <!--[if lt IE 9]>
    <script src="${pageContext.request.contextPath}/resources/js/html5shiv.js"></script>
    <![endif]-->
    <title><spring:message code="TripPage"/></title>
</head>
<body>
<div id="page">
    <jsp:include page="baseView.jsp"/>
    <jsp:include page="tripHeaderView.jsp"/>

    <div class="inner-content">
        <section>
            <article>
                <c:if test="${error != null}">
                    <span class="errorblock">${error}</span>
                </c:if>
                <c:if test="${success != null}">
                    <span class="successblock">${success}</span>
                </c:if>
                <h3><spring:message code="General"/></h3>

                <div class="trip-info">
                    <form id="trip-form" action="/trip/${trip.id}/editTrip" method="POST">
                        <table>
                            <tr>
                                <td><label><spring:message code="Title"></spring:message></label></td>
                                <td><label class="trip-view">${trip.title}</label></td>
                                <td><input class="trip-edit" type="text" name="title"></td>
                            </tr>
                            <tr>
                                <td><spring:message code="Description"/></td>
                                <td><label class="trip-view">${trip.description}</label></td>
                                <td><input class="trip-edit" type="text" name="description"></td>
                            </tr>
                            <tr class="trip-edit">
                                <td><label><spring:message code="chatAllowed"></spring:message></label></td>
                                <td>
                                    <input type="checkbox" value="1" name="chatAllowed" >
                                    <input type="hidden" value="0" name="chatAllowed">
                                </td>
                            </tr>
                            <tr class="trip-edit">
                                <td><label><spring:message code="positionVisible"></spring:message></label></td>
                                <td>
                                    <input type="checkbox" value="1" name="positionVisible" >
                                    <input type="hidden" value="0" name="positionVisible">
                                </td>
                            </tr>
                            <tr>
                                <td><spring:message code="Privacy"/></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${trip.privacy == 'PUBLIC'}">
                                            <spring:message code="Public"/>
                                        </c:when>
                                        <c:when test="${trip.privacy == 'PROTECTED'}">
                                            <spring:message code="Protected"/>
                                        </c:when>
                                        <c:otherwise>
                                            <spring:message code="Private"/>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                            <c:if test="${trip.timeBoundTrip==true}">
                                <tr>
                                    <td><label><spring:message code="Active"/></label></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${trip.active==true}">
                                                <spring:message code="IsActive"/>
                                            </c:when>
                                            <c:otherwise>
                                                <spring:message code="IsNotActive"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:if>
                            <tr>
                                <td>
                                    <c:forEach items="${trip.enrollments}" var="enrollment">
                                        <c:choose>
                                            <c:when test="${enrollment.status == 'BUSY'}">
                                                <spring:message code="TripStarted"/>
                                            </c:when>
                                            <c:when test="${enrollment.status == 'FINISHED'}">
                                                <spring:message code="TripStopped"/>
                                            </c:when>
                                        </c:choose>
                                    </c:forEach>
                                </td>
                            </tr>
                            <tr>
                                <c:choose>
                                    <c:when test="${trip.published==true}">
                                        <td><spring:message code="IsPublished"/></td>
                                    </c:when>
                                    <c:otherwise>
                                        <td><spring:message code="IsNotPublished"/></td>
                                    </c:otherwise>
                                </c:choose>
                            </tr>
                            <c:if test="${not empty trip.labels}">
                                <table>
                                    <tbody>
                                        <thead>
                                            <th>Labels</th>
                                        </thead>
                                        <c:forEach items="${trip.labels}" var="label">
                                            <tr>
                                                <td>${label}</td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </c:if>
                        <tr>
                            <td>
                                <input id="btn-save" type="submit" value="<spring:message code="Save" />" class="trip-edit btn-blue">
                            </td>
                        </tr>
                    </form>
                    <c:if test="${trip.organizer == user}">
                        <tr>
                            <td>
                                <button id="btn-edit" type="submit" class="trip-view btn-blue"><spring:message code="Edit"/></button>
                            </td>
                        </tr>
                    </c:if>
                </table>
                    <c:if test="${not empty requisites}">
                        <h3><spring:message code="EnrollmentRequisites"/></h3>
                        <table class="EnrollReqTable">
                            <c:forEach items="${requisites}" var="requisite">
                                <thead>
                                    <th>
                                        <spring:message code="Requisites"/>
                                    </th>
                                    <th>
                                        <spring:message code="Amount"/>
                                    </th>
                                </thead>
                                <tr>
                                    <td>
                                        ${requisite.key}
                                    </td>
                                    <td>
                                        ${requisite.value}
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                    </c:if>
                </div>

                <div id="invitation-buttons">
                    <c:set value="false" var="validPrivateTrip"/>
                    <c:set value="false" var="invited"/>
                    <c:set value="false" var="enrolled"/>
                    <c:set value="false" var="showButton"/>
                    <c:if test="${not empty user  && trip.published==true && trip.privacy == 'PRIVATE'}">
                        <c:set value="true" var="validPrivateTrip"/>
                        <c:if test="${not empty trip.invitations}">
                            <c:forEach items="${trip.invitations}" var="invitation">
                                <c:if test="${invitation.user == user && invitation.user != trip.organizer && invitation.answer == 'UNANSWERED'}">
                                    <c:forEach items="${trip.enrollments}" var="enrollment">
                                        <c:if test="${enrollment.user == user}">
                                            <c:set value="true" var="enrolled"/>
                                        </c:if>
                                    </c:forEach>
                                    <c:if test="${enrolled == false}">
                                        <c:set value="true" var="invited"/>
                                    </c:if>
                                </c:if>
                                <c:if test="${invitation.user == user && invitation.user != trip.organizer && invitation.answer == 'ACCEPTED'}">
                                    <c:set value="true" var="showButton"></c:set>
                                </c:if>
                            </c:forEach>
                        </c:if>
                    </c:if>
                    <c:if test="${validPrivateTrip == true && invited == true}">
                        <a href="/users/acceptInvitation?tripId=${trip.id}">
                            <button class="btn-blue"><spring:message code="Accept"/></button>
                        </a>
                        <a href="/users/declineInvitation?tripId=${trip.id}">
                            <button class="btn-blue"><spring:message code="Decline"/></button>
                        </a>
                    </c:if>
                    <script type="text/javascript">
                        $(document).ready(function(){
                            var enrolled = null;
                            $.ajax({
                                type: "GET",
                                url: "/isEnrolled/${trip.id}",
                                success:function(data){
                                    enrolled = data;
                                    enrolled ?  $('#button2').addClass('on') :  $('#button2').removeClass('on');
                                }
                            });
                            $('#button2').on('click', function(){
                                if(enrolled != null){
                                    if(enrolled){
                                        $.ajax({
                                            type: "POST",
                                            url: "/unSubscribe",
                                            data: {tripId : ${trip.id}}
                                        }).done(function(){
                                                    window.location = '?';
                                                });
                                        enrolled = false;
                                    }else{
                                        $.ajax({
                                            type: "POST",
                                            url: "/subscribe",
                                            data: {tripId : ${trip.id}}
                                        }).done(function(){
                                                    window.location = '?';
                                                });
                                        enrolled = true;
                                    }
                                    $(this).toggleClass('on');
                                }
                            });
                        });
                    </script>

                    <c:if test="${validPrivateTrip == true && showButton == true}">
                        <section class="attending">
                            <a href="#" id="button2" class="btn-attend"><spring:message code="Attending"></spring:message></a>
                            <span class="attending-light"></span>
                        </section>
                    </c:if>

                </div>
            </article>
        </section>
    </div>
</div>
<script src="${pageContext.request.contextPath}/resources/js/jquery-1.9.0.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/trip.js"></script>
<script>
    if (window.location.hash) {
        window.location = '?theme=${trip.theme}';
    }
</script>
</body>
</html>