<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css" />
    <link rel="shortcut icon" href="favicon.ico">
    <title>Home</title>
</head>
<body>
<div id="page">
    <header>
        <img class="header-img" src="${pageContext.request.contextPath}/resources/res/img/dragon.png" alt="Logo" title="Home"/>
        <h1 class="header-title">Trips</h1>
        <nav class="header-nav">
            <ul class="nav">
                <li><a class="selected nav-link" href="/" title="Home">Home</a></li>
                <li><a class="nav-link" href="../jsp/trips.html" title="">Trips</a></li>
                <li><a class="nav-link" href="../jsp/Profiel.html" title="">Profiel</a></li>
                <li><a class="nav-link" href="login" title="">Log in</a></li>
                <li><a class="nav-link" href="../jsp/Contact.html" title="">Contact</a></li>
            </ul>
        </nav>
    </header>


    <div id="content">
        <div class="title-block">
            <h1>Trips</h1>
            <h1>${message}</h1>
            <h2>De oplossing voor het plannen van uw uitstapen</h2>
        </div>
    </div>
    <footer><p class="footer">Trips -   2013</p></footer>
</div>
</body>
<script src="../js/jquery-1.9.0.min.js"></script>
<script src="../js/knockout.js"></script>

<!--[if lt IE 9]>
<script src="../js/html5shiv.js"></script>
<![endif]-->
</html>