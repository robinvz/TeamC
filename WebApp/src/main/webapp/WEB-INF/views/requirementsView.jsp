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

        <p>
        <c:if test="${not empty user && user == trip.organizer}">
            <table>
                <form action="/requirements/${trip.id}" method="POST">
                    <tr>
                        <td><spring:message code="EnterRequisite"/></td>
                        <td><input type="text" name="requisite"></td>
                    </tr>
                    <tr>
                        <td><spring:message code="Amount"/></td>
                        <td><input type="text" name="amount"></td>
                    </tr>
                    <tr>
                        <td><input id="btn-save" type="submit" value="<spring:message code="Save" />" class="btn-blue"></td>
                    </tr>
                </form>
            </table>
        </c:if>
        </p>

        <p>
        <c:choose>
            <c:when test="${not empty trip.requisites}">
                <table>
                    <thead>
                        <th><spring:message code="Requisites"/></th>
                        <th><spring:message code="Amount"/></th>
                    </thead>
                    <c:forEach items="${trip.requisites}" var="requisite">
                        <tr>
                            <td>${requisite.key}</td>
                            <td>${requisite.value}</td>
                        </tr>
                    </c:forEach>
                </table>
            </c:when>
            <c:otherwise>
                <h3><spring:message code="NoRequisites"/></h3>
            </c:otherwise>
        </c:choose>
        </p>
    </div>

</div>

</body>
</html>