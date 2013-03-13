<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/trip.css"/>
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
                <h3><spring:message code="General"/></h3>
                <c:if test="${error != null}">
                    <span class="errorblock">${error}</span>
                </c:if>
                <c:if test="${success != null}">
                    <span class="successblock">${success}</span>
                </c:if>

                <div class="trip-info">
                                            <c:if test="${not empty enrollmentRequisites}">
                                                <h3>Requisites only for you!</h3>
                                                    <c:forEach items="${enrollmentRequisites}" var="enrollmentRequisite">
                                                        ${enrollmentRequisite.value}
                                                        ${enrollmentRequisite.key}
                                                    </c:forEach>
                                            </c:if>
                    <table>
                        <tr>
                            <td><spring:message code="Description"/></td>
                            <td>${trip.description}</td>
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
                            <c:if test="${not empty trip.labels}">
                                <td>Labels</td>
                                <c:forEach items="${trip.labels}" var="label">
                                    <td>-${label}-</td>
                                </c:forEach>
                            </c:if>
                        </tr>
                        <tr>
                            <td>
                                <c:forEach items="${trip.enrollments}" var="enrollment">
                                    <c:choose>
                                        <c:when test="${enrollment.status == 'BUSY'}">
                                            <spring:message code="EnrollmentStarted"/>
                                        </c:when>
                                        <c:when test="${enrollment.status == 'FINISHED'}">
                                            <spring:message code="EnrollmentStopped"/>
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
                    </table>



                </div>

                <div id="invitation-buttons">
                    <c:set value="false" var="validPrivateTrip"/>
                    <c:set value="false" var="invited"/>
                    <c:set value="false" var="enrolled"/>
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
                            </c:forEach>
                        </c:if>
                    </c:if>
                    <c:if test="${validPrivateTrip == true && invited == true}">
                        <a href="/acceptInvitation?tripId=${trip.id}">
                            <img id="acceptInvitationButton"
                                 src="${pageContext.request.contextPath}/resources/res/img/subscribe.jpg">
                        </a>
                        <a href="/declineInvitation?tripId=${trip.id}">
                            <img id="declineInvitationButton"
                                 src="${pageContext.request.contextPath}/resources/res/img/unsubscribe.jpg">
                        </a>
                    </c:if>
                </div>
            </article>
        </section>
    </div>
</div>
<script src="${pageContext.request.contextPath}/resources/js/jquery-1.9.0.min.js"></script>
<script>
    if (window.location.hash) {
        window.location = '?theme=${trip.theme}';
    }
</script>
</body>
</html>