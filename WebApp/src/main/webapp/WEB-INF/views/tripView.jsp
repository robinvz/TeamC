<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/trip.css"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
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
                <div class="trip-info">
                    <table>
                        <tr>
                            <td><spring:message code="Description"/></td>
                            <td>${trip.description}</td>
                        </tr>
                        <tr>
                            <td><spring:message code="Privacy"/></td>
                            <td>${trip.privacy}</td>
                        </tr>
                        <tr>
                            <td><label><spring:message code="Active"/></label></td>
                            <td>${trip.active}</td>
                        </tr>
                        <tr>
                            <c:if test="${trip.published==true}">
                                <spring:message code="IsPublished"/>
                            </c:if>
                            <spring:message code="IsNotPublished"/>
                        </tr>
                        <c:choose>
                            <c:when test="${not empty trip.labels}">
                                <c:forEach items="${trip.labels}" var="label">
                                    <tr>
                                        <td>${label}</td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <label>Add labels here</label>
                            </c:otherwise>
                        </c:choose>
                    </table>
                </div>

                <div class="trip-requirements">

                </div>

                <div class="trip-stops">

                </div>

                <div class="trip-participants">
                    <c:choose>
                        <c:when test="${trip.privacy == 'PUBLIC'}">
                            <!--Do nothing-->
                        </c:when>
                        <c:when test="${empty trip.enrollments}">
                            <spring:message code="NoEnnrollmentsYet"/>
                        </c:when>
                        <c:otherwise>
                            <table>
                                <c:forEach items="${trip.enrollments}" var="enrollment">
                                    <tr>
                                        <td>${enrollment.user.email}</td>
                                    </tr>
                                </c:forEach>
                            </table>
                        </c:otherwise>
                    </c:choose>
                </div>

                <c:choose>
                    <c:when test="${not empty user  && trip.published==true && trip.privacy == 'PROTECTED' && not empty trip.enrollments}">
                        <c:forEach items="${trip.enrollments}" var="enrollment">
                            <c:if test="${enrollment.user == user}">
                                <a href="/unSubscribe?tripId=${trip.id}">
                                    <img id="unSubscribeButton" src="${pageContext.request.contextPath}/resources/res/img/unsubscribe.jpg">
                                </a>
                            </c:if>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <c:if test="${not empty user && trip.published==true && trip.privacy == 'PROTECTED'}">
                        <a href="/subscribe?tripId=${trip.id}">
                            <img id="subscribeButton" src="${pageContext.request.contextPath}/resources/res/img/subscribe.jpg">
                        </a>
                        </c:if>
                    </c:otherwise>
                </c:choose>
            </article>
        </section>
    </div>

</div>
</body>
</html>