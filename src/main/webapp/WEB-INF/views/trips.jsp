<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/trips.css" />
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
                <li><a class="selected nav-link" href="/trips" title="">Trips</a></li>
                <li><a class="nav-link" href="/profile" title="">Profiel</a></li>
                <li><a class="nav-link" href="/login" title="">Inloggen</a></li>
                <li><a class="nav-link" href="/contact" title="">Contact</a></li>
            </ul>
        </nav>
    </header>
    <div id="trip-pic">
        <h2>Overzicht trips</h2>
    </div>

    <div id="content">
        <table id="trips">
            <thead>
            <tr>
                <th>Trip</th>
                <th>Locatie</th>
                <th>Tijdstip</th>
                <th>Privacy</th>
            </tr>
            </thead>
            <tbody id="trips-list">
            <tr>
                <td>tripnaam</td>
                <td>Antwerpen</td>
                <td>01/01/2013 - 02/01/2013</td>
                <td>Openbaar</td>
            </tr>
            <tr>
                <td>tripnaam</td>
                <td>Antwerpen</td>
                <td>01/01/2013 - 02/01/2013</td>
                <td>Openbaar</td>
            </tr>
            <tr>
                <td>tripnaam</td>
                <td>Antwerpen</td>
                <td>01/01/2013 - 02/01/2013</td>
                <td>Openbaar</td>
            </tr>
            <tr>
                <td>tripnaam</td>
                <td>Antwerpen</td>
                <td>01/01/2013 - 02/01/2013</td>
                <td>Openbaar</td>
            </tr>
            <tr>
                <td>tripnaam</td>
                <td>Antwerpen</td>
                <td>01/01/2013 - 02/01/2013</td>
                <td>Openbaar</td>
            </tr>
            <tr>
                <td>tripnaam</td>
                <td>Antwerpen</td>
                <td>01/01/2013 - 02/01/2013</td>
                <td>Openbaar</td>
            </tr>
            <tr>
                <td>tripnaam</td>
                <td>Antwerpen</td>
                <td>01/01/2013 - 02/01/2013</td>
                <td>Openbaar</td>
            </tr>
            <tr>
                <td>tripnaam</td>
                <td>Antwerpen</td>
                <td>01/01/2013 - 02/01/2013</td>
                <td>Openbaar</td>
            </tr>
            <tr>
                <td>tripnaam</td>
                <td>Antwerpen</td>
                <td>01/01/2013 - 02/01/2013</td>
                <td>Openbaar</td>
            </tr>
            <tr>
                <td>tripnaam</td>
                <td>Antwerpen</td>
                <td>01/01/2013 - 02/01/2013</td>
                <td>Openbaar</td>
            </tr>
            </tbody>
        </table>
    </div>

    <footer><p class="footer">Trips -   2013</p></footer>
</div>
</body>
<script src="${pageContext.request.contextPath}/resources/js/jquery-1.9.0.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/knockout.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/trips.js"></script>
<!--[if lt IE 9]>
<script src="${pageContext.request.contextPath}/resources/js/html5shiv.js"></script>
<![endif]-->
</html>