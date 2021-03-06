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
        <c:if test="${not empty user}">
            <form id="form-createCost" action="/users/costs/${trip.id}/createCost" method="POST">
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
                <button id="btn-addcost" type="submit" class="btn-blue"><spring:message code="addCost"/></button>

            </form>
        </c:if>
        <c:choose>
            <c:when test="${not empty totalTripCosts}">
                <table>
                    <thead>
                    <th><spring:message code="User"/></th>
                    <th><spring:message code="Cost"/></th>
                    <th><spring:message code="Price"/></th>
                    <th></th>
                    </thead>
                    <c:if test="${trip.organizer == user}">
                        <c:forEach items="${totalTripCosts}" var="cost">
                            <c:forEach items="${cost.value}" var="costLine">
                                <tr>
                                    <td>
                                            ${cost.key}
                                    </td>
                                    <td>
                                            ${costLine.key}
                                    </td>
                                    <td>
                                            ${costLine.value}
                                    </td>
                                    <td>
                                        <form action="/users/costs/${trip.id}/deleteCost/${costLine.key}/${costLine.value}">
                                            <button id="btn-removeCost" type="submit" class="btn-blue"><spring:message code="removeCost"/></button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:forEach>
                    </c:if>
                    <c:if test="${trip.organizer != user}">
                        <c:forEach items="${totalTripCosts}" var="cost">
                            <c:forEach items="${cost.value}" var="costLine">
                                <c:if test="${cost.key == user}">
                                    <tr>
                                        <td>
                                                ${cost.key}
                                        </td>
                                        <td>
                                                ${costLine.key}
                                        </td>
                                        <td>
                                                ${costLine.value}
                                        </td>
                                        <td>
                                            <form action="/users/costs/${trip.id}/deleteCost/${costLine.key}/${costLine.value}">
                                                <button type="submit" class="btn-blue"><spring:message code="removeCost"/></button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:if>
                            </c:forEach>
                        </c:forEach>
                    </c:if>
                </table>
            </c:when>
            <c:otherwise>
                <h3><spring:message code="NoCosts"/></h3>
            </c:otherwise>
        </c:choose>
    </div>
</div>

</body>
</html>