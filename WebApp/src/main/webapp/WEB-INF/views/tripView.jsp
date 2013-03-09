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
                        <c:if test="${trip.timeBoundTrip==true}">
                            <tr>
                                <td><spring:message code="Dates"/></td>
                            </tr>
                            <c:forEach items="${dates}" var="date">
                                <tr>
                                    <td>${date.key}</td>
                                    <td>${date.value}</td>
                                    <td>
                                        <c:if test="${not empty user && trip.organizer==user}">
                                            <a href="/trip/${trip.id}/deleteDate/${date.key}"><spring:message code="Delete"/></a>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:if>
                    </table>
                </div>

                <div id="subscribe-buttons">
                    <c:set value="false" var="validTrip"/>
                    <c:set value="false" var="subscribed"/>
                    <c:if test="${not empty user  && trip.published==true && trip.privacy == 'PROTECTED'}">
                        <c:set value="true" var="validTrip"/>
                        <c:if test="${not empty trip.enrollments}">
                            <c:forEach items="${trip.enrollments}" var="enrollment">
                                <c:if test="${enrollment.user == user}">
                                    <c:set value="true" var="subscribed"/>
                                </c:if>
                            </c:forEach>
                        </c:if>
                    </c:if>

                    <c:if test="${validTrip == true}">
                        <c:choose>
                            <c:when test="${subscribed}">
                                <a href="/unSubscribe?tripId=${trip.id}">
                                    <img id="unSubscribeButton"
                                         src="${pageContext.request.contextPath}/resources/res/img/unsubscribe.jpg">
                                </a>
                            </c:when>
                            <c:otherwise>
                                <a href="/subscribe?tripId=${trip.id}">
                                    <img id="subscribeButton"
                                         src="${pageContext.request.contextPath}/resources/res/img/subscribe.jpg">
                                </a>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                </div>
            </article>
        </section>
    </div>

</div>
<script src="${pageContext.request.contextPath}/resources/js/jquery-1.9.0.min.js"></script>
         <script>
             if(!window.location.hash) {
                 window.location = '?theme=${trip.theme}#loaded';
             }

         </script>
</body>
</html>