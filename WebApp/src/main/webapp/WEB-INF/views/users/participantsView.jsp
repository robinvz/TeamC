<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/login.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/trip.css"/>
    <link rel="stylesheet" media="(max-width:1040px)" href="${pageContext.request.contextPath}/resources/css/main-responsive.css">
    <link rel="stylesheet" media="(min-width:1039px)" href="${pageContext.request.contextPath}/resources/css/responsive-fix.css">
    <link rel="stylesheet" href="<spring:theme code="css"/>" type="text/css"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <title><spring:message code="TripPage"/></title>
</head>
<body>
<div id="page">
    <jsp:include page="../baseView.jsp"/>
    <jsp:include page="../tripHeaderView.jsp"/>

    <div class="inner-content">
        <h3><spring:message code="Participants"/></h3>
        <c:choose>
            <c:when test="${trip.privacy == 'PUBLIC'}">
                <!--Do nothing-->
            </c:when>
            <c:when test="${empty trip.enrollments}">
                <h3><spring:message code="NoEnnrollmentsYet"/></h3>
            </c:when>
            <c:otherwise>
                <table>
                    <c:forEach items="${trip.enrollments}" var="enrollment">
                        <tr>
                            <td>${enrollment.user.email}</td>
                            <c:if test="${user == trip.organizer}">
                                <td>
                                    <form action="/addRequirementToEnrollmentView/${enrollment.user.email}/${trip.id}" method="GET">
                                        <input type="submit" value="<spring:message code="AddRequisite"/>"/>
                                    </form>
                                </td>
                            </c:if>
                        </tr>
                    </c:forEach>
                </table>
            </c:otherwise>
        </c:choose>
    </div>

</div>
</body>