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
    <jsp:include page="headerView.jsp"/>

    <div id="content">
        <div id="login-buttons">
            <button id="login-facebook"></button>
            <button id="register-facebook"></button>
        </div>
        <h2 id="login-title"></h2>
        <fieldset id="form-login">
            <form>
                <label for="email"> Email: </label> <input type="text" name="email" id="email">
                <label for="password"> Password: </label> <input type="password" name="password" id="password">
                <input type="submit" value="Login" class="btn-submit">
            </form>
        </fieldset>
        <form action="/login" method="post">
            <fieldset>
                <legend>Login</legend>
                <table>
                    <tr>
                        <td>Email</td>
                        <td>
                            <input type="text" id="emailLogin" name="email"
                                   placeholder="Email"/></td>
                    </tr>
                    <tr>
                        <td>Password</td>
                        <td>
                            <input type="password" id="passwordLogin" name="password"
                                   email="Password"/></td>
                    </tr>
                    <tr>
                        <td colspan="2" align="center">
                            <button id="login">Login</button>
                        </td>
                    </tr>
                </table>
            </fieldset>
        </form>
    </div>

    <footer><p class="footer">Trips - 2013</p></footer>
</div>
<script src="${pageContext.request.contextPath}/resources/js/jquery-1.9.0.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/login.js"></script>
<!--[if lt IE 9]>
<script src="${pageContext.request.contextPath}/resources/js/html5shiv.js"></script>
<![endif]-->
</body>
</html>