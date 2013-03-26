<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/profiel.css"/>
    <link rel="stylesheet" media="(max-width:1040px)" href="${pageContext.request.contextPath}/resources/css/main-responsive.css">
    <link rel="stylesheet" media="(min-width:1039px)" href="${pageContext.request.contextPath}/resources/css/responsive-fix.css">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <title><spring:message code="EditCredentialsPage" /></title>
</head>
<body>
<div id="page">
    <jsp:include page="../baseView.jsp"/>
    <jsp:include page="profileHeaderView.jsp"/>

    <div class="content">
        <h2><spring:message code="EditPassword"/></h2>
        <c:if test="${error != null}">
            <span class="errorblock">${error}</span>
        </c:if>
        <form action="/users/editCredentials" method="POST">
            <table>
                <tr>
                    <td><spring:message code="OldPassword"/></td>
                    <td><input type="password" name="oldPassword"></td>
                </tr>
                <tr>
                    <td><spring:message code="NewPassword"/></td>
                    <td><input type="password" name="newPassword1"></td>
                </tr>
                <tr>
                    <td><spring:message code="NewPassword"/></td>
                    <td><input type="password" name="newPassword2"></td>
                </tr>
                <tr>
                    <td></td>
                    <td><input id="btn-save" type="submit" value="<spring:message code="Save" />" class="btn-blue"></td>
                </tr>
            </table>
        </form>
    </div>

</div>
</body>
</html>