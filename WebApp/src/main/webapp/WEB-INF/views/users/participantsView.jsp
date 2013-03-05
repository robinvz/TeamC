<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/login.css"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <title><spring:message code="TripPage"/></title>
</head>
<body>
<div id="page">
    <jsp:include page="../baseView.jsp"/>
    <jsp:include page="../tripHeaderView.jsp"/>

    <div class="inner-content">
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
                        </tr>
                    </c:forEach>
                </table>
            </c:otherwise>
        </c:choose>
    </div>

</div>
</body>