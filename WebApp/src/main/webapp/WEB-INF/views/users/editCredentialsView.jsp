<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/profiel.css"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <title><spring:message code="EditCredentialsPage"/>/title>
</head>
<body>
<div id="page">
    <jsp:include page="../baseView.jsp"/>
    <jsp:include page="profileHeaderView.jsp"/>

    <div class="content">
        <h2><spring:message code="EditPwFor"/> ${user.firstName} ${user.lastName}</h2>

        <form action="/users/editCredentials" method="POST">
            <table>
                <tr>
                    <td>
                        <label><spring:message code="OldPassword"/></label>
                    </td>

                    <td>
                        <input type="password" name="oldPassword">
                    </td>
                </tr>
                <tr>
                    <td>
                        <label><spring:message code="NewPassword"/></label>
                    </td>

                    <td>
                        <input type="password" name="newPassword">
                    </td>
                </tr>
                <input id="btn-save" type="submit" value="Save" class="btn-blue">
            </table>
        </form>
    </div>

</div>
</body>
</html>