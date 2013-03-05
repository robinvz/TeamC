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
    <jsp:include page="baseView.jsp"/>
    <jsp:include page="tripHeaderView.jsp"/>

    <div class="inner-content">
        <c:if test="${error != null}">
            <span class="errorblock">${error}</span>
        </c:if>

        <c:if test="${not empty user && user == trip.organizer}">
            <table>
                <form action="/requirements/${trip.id}" method="POST">
                    <tr>
                        <td><spring:message code="EnterRequisite" /></td>
                        <td><input type="text" name="requisite"></td>
                        <td><input id="btn-save" type="submit" value="<spring:message code="Save" />" class="btn-blue"></td>
                    </tr>
                </form>
            </table>
        </c:if>
    </div>

</div>

</body>
</html>