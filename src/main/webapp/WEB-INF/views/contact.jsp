<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/trips.css"/>
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
                <li><a class="nav-link" href="../views/index.html" title="Home">Home</a></li>
                <li><a class="selected nav-link" href="trips.html" title="">Trips</a></li>
                <li><a class="nav-link" href="../html/Profiel.html" title="">Profiel</a></li>
                <li><a class="nav-link" href="../html/Login.html" title="">Inloggen</a></li>
                <li><a class="nav-link" href="../html/Contact.html" title="">Contact</a></li>
            </ul>
        </nav>
    </header>
    <div id="contact">
        <h2>Contact</h2>
    </div>

    <div id="content">
        <fieldset id="form-contact">
            <form>
                <label for="name"> Name: </label> <input type="text" name="name" id="name">
                <label for="email"> Email: </label> <input type="text" name="email" id="email">
                <label for="type"> Type: </label>
                <select name="type" id="type">
                    <option value="question">Question</option>
                    <option value="remark">Remark or Suggestion</option>
                    <option value="bug_error">Bug or Error</option>
                </select>
                <textarea name="message" id="message" rows="5"></textarea>
                <input type="submit" value="Send" class="btn-submit">
            </form>
        </fieldset>
    </div>

    <footer><p class="footer">Trips - 2013</p></footer>
</div>
</body>
<script src="${pageContext.request.contextPath}/resources/js/jquery-1.9.0.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/knockout.js"></script>
<!--[if lt IE 9]>
<script src="${pageContext.request.contextPath}/resources/js/html5shiv.js"></script>
<![endif]-->
</html>