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
    <jsp:include page="baseView.jsp"/>
    <jsp:include page="tripHeaderView.jsp"/>

    <div class="inner-content">
            <c:if test="${error != null}">
                <span class="errorblock">${error}</span>
            </c:if>
            <table>
                <form action="/labelsView/${trip.id}" method="POST">
                    <tr>
                        <td>Enter a label</td>
                        <td><input type="text" name="label"></td>
                        <td><input id="btn-save" type="submit" value="<spring:message code="Save" />" class="btn-blue">
                        </td>
                    </tr>
                </form>
            </table>
    </div>

</div>
</body>
</html>