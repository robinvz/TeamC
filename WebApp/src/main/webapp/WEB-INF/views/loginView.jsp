<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ taglib prefix="fmt" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/login.css"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <title>Login page</title>
</head>
<body>
<div id="page">
    <jsp:include page="baseView.jsp"/>

    <div id="content">
        <div id="login-buttons">
            <button id="login-facebook"></button>
            <button id="register-facebook"></button>
        </div>
        <h2 id="login-title"></h2>

        <form:form action="/login" commandName="loginBean" method="post" id="loginform" name="loginform" dir="loginform">
            <fieldset>
                <form:errors path="*" cssClass="errorblock" element="div" />

                <legend>Login</legend>
                <table>
                    <tr>
                        <td>Email</td>
                        <td>
                            <form:input path="email"/>
                            <form:errors path="email" cssClass="error"></form:errors>
                        </td>
                    </tr>
                    <tr>
                        <td>Password</td>
                        <td>
                            <form:password path="password"/>
                            <form:errors path="password" cssClass="error"></form:errors>
                        </td>

                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" align="center">
                            <button type="submit">Login</button>
                        </td>
                    </tr>
                </table>
            </fieldset>
        </form:form>
    </div>
</div>
<script src="${pageContext.request.contextPath}/resources/js/login.js"></script>
</body>
</html>