<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
                <li><a class="nav-link" href="/index" title="Home">Home</a></li>
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

        <fieldset id="form-register" >
            <form:form action="login" method="POST">
                <form:label path="email">First Name</form:label>
                <form:input path="email" />
                <form:label path="password">Password</form:label>
                <form:input path="password" />
                <!--<label for="newemail"> Email: </label> <input type="text" name="newemail" id="newemail">
                <label for="newpassword"> Password: </label> <input type="password" name="newpassword" id="newpassword">
                <label for="newpasswordconf"> Confirm Password: </label> <input type="password" name="newpasswordconf" id="newpasswordconf">-->
                <input type="submit" value="Registreren" class="btn-submit">
            </form:form>
        </fieldset>


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