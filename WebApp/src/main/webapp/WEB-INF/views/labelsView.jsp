<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/login.css"/>
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
        <h3>Labels</h3>
        <c:if test="${error != null}">
            <span class="errorblock">${error}</span>
        </c:if>
        <c:if test="${success != null}">
            <span class="successblock">${success}</span>
        </c:if>

        <c:if test="${not empty user && user == trip.organizer}">
            <table>
                <form action="/labels/${trip.id}" method="POST">
                    <tr>
                        <td><spring:message code="EnterLabel" /></td>
                        <td><input type="text" name="label"></td>
                        <td><input id="btn-save" type="submit" value="<spring:message code="Save" />" class="btn-blue"></td>
                    </tr>
                </form>
            </table>


            <c:if test="${not empty trip.labels}">
                <table>

                    <tbody>
                    <c:forEach items="${trip.labels}" var="label">
                        <tr>
                            <td>${label}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:if>
        </c:if>
    </div>
</div>
</body>
</html>