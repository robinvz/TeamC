<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <link rel="stylesheet" href="../css/main.css" />
    <link rel="stylesheet" href="../css/login.css" />
    <link rel="shortcut icon" href="favicon.ico">
    <title>Home</title>
</head>
<body>
<div id="page">
    <header>
        <img class="header-img" src="../res/img/dragon.png" alt="Logo" title="Home"/>
        <h1 class="header-title">Trips</h1>
        <nav class="header-nav">
            <ul class="nav">
                <li><a class="nav-link" href="../views/index.html" title="Home">Home</a></li>
                <li><a class="nav-link" href="trips.html" title="">Trips</a></li>
                <li><a class="nav-link" href="../html/Profiel.html" title="">Profiel</a></li>
                <li><a class="selected nav-link" href="../html/Login.html" title="">Inloggen</a></li>
                <li><a class="nav-link" href="../html/Contact.html" title="">Contact</a></li>
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
            <form:form action="account/register" method="POST">
                <form:label path="email">First Name</form:label>
                <form:input path="email" />
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
<script src="../js/jquery-1.9.0.min.js"></script>
<script src="../js/knockout.js"></script>
<script src="../js/login.js"></script>
<!--[if lt IE 9]>
<script src="../js/html5shiv.js"></script>
<![endif]-->
</html>