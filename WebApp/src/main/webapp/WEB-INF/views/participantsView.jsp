<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/login.css"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <title><spring:message code="LoginPage"/></title>
</head>
<body>
<div id="page">
    <jsp:include page="baseView.jsp"/>

    <h2><spring:message code="PleaseLogIn"/></h2>
    <div id="content">

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
    </div>

</div>
</body>