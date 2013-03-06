<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/login.css"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <!--[if lt IE 9]>
    <script src="${pageContext.request.contextPath}/resources/js/html5shiv.js"></script>
    <![endif]-->
    <title><spring:message code="TripPage"/></title>
</head>
<body>
<div id="page">
    <jsp:include page="../baseView.jsp"/>
    <jsp:include page="../tripHeaderView.jsp"/>

    <div class="inner-content">
        <c:if test="${error != null}">
            <span class="errorblock">${error}</span>
        </c:if>

        <c:if test="${not empty user && trip.organizer == user}">
            <h2>Invite a user</h2>
            <table>
                <form action="/inviteUser/" method="GET">
                    <tr>
                        <td><spring:message code="User"/></td>
                        <td><input type="text" name="user"></td>
                        <td><button class="btn-blue" type="submit"><spring:message code="Invite" /></button></td>
                    </tr>
                </form>
            </table>
        </c:if>
    </div>

</div>

</body>
</html>