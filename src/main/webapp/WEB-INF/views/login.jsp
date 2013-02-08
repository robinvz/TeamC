<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ taglib prefix="fmt" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/login.css" />
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <title>Home</title>
</head>
<body>
<div id="page">
    <header>
        <img class="header-img" src="${pageContext.request.contextPath}/resources/res/img/dragon.png" alt="Logo" title="Home"/>
        <h1 class="header-title">Trips</h1>
        <nav class="header-nav">
            <ul class="nav">
                <li><a class="nav-link" href="/" title="Home">Home</a></li>
                <li><a class="nav-link" href="/trips" title="">Trips</a></li>
                <li><a class="nav-link" href="/profile" title="">Profiel</a></li>
                <li><a class="selected nav-link" href="/login" title="">Inloggen</a></li>
                <li><a class="nav-link" href="/contact" title="">Contact</a></li>
            </ul>
        </nav>
    </header>
    <div id="trip-pic">
        <h2>Overzicht trips</h2>
    </div>

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
        <form action="<c:url value="/login"/>" method="post">
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
                <tr><td colspan="2" align="center">
                    <button id="login">Login</button>
                </td></tr>
            </table>
        </fieldset>
        </form>


    </div>

    <footer><p class="footer">Trips -   2013</p></footer>
</div>
</body>
<script src="${pageContext.request.contextPath}/resources/js/jquery-1.9.0.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/knockout.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/login.js"></script>
<!--[if lt IE 9]>
<script src="${pageContext.request.contextPath}/resources/js/html5shiv.js"></script>
<![endif]-->
</html>