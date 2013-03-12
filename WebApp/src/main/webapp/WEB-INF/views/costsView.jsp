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
        <h3><spring:message code="Costs"/></h3>

         <c:choose>
            <c:when test="${not empty totalTripCosts}">
                <table>
                    <thead>
                        <th><spring:message code="Cost"/></th>
                        <th><spring:message code="Price"/></th>
                    </thead>
                    <c:forEach items="${totalTripCosts}" var="cost">
                        <tr>
                            <td>${cost.key}</td>
                            <td>${cost.value}</td>
                        </tr>
                    </c:forEach>
                </table>
            </c:when>
            <c:otherwise>
                <h3><spring:message code="NoCosts"/></h3>
            </c:otherwise>
        </c:choose>
        <c:if test="${not empty user}">
                <form id="form-createCost" action="/costs/${trip.id}/createCost" method="POST">
                    <table>
                        <tr>
                            <td><label><spring:message code="Name"/></label></td>
                            <td><input type="text" name="name" required="true"></td>
                        </tr>
                        <tr>
                            <td><label><spring:message code="Amount"/></label></td>
                            <td><input type="number" name="amount" required="true"></td>
                        </tr>
                    </table>
                <button id="btn-addcost" type="submit" class="btn-blue"><spring:message code="addCost" /></button>

            </form>
        </c:if>
    </div>
</div>

</body>
</html>