<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css" />
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <title>Trip page</title>
</head>
<body>
<div id="page">
    <jsp:include page="header.jsp" />

    <div id="trip-header">
        <h2>${trip.naam}</h2>
    </div>

    <div id="content">
        <aside>
            <nav class="trip-nav">
                <h3>Trips</h3>
                <ul class="trip-nav">
                    <li><a href="#">Info</a></li>
                    <li><a href="#">Benodigdheden</a></li>
                    <li><a href="#">Stopplaatsen</a></li>
                    <li><a href="#">Chat</a></li>
                    <li><a href="#">Deelnemers</a></li>
                    <li><a href="#">Resultaten</a></li>
                    <li><a href="#">Trip starten</a></li>
                    <li><a href="#">Aanpassen</a></li>
                    <li><a href="#">Verwijderen</a></li>
                </ul>
            </nav>
        </aside>
        <div class="inner-content">
            <section>
                <article>
                    <p>content</p>
                </article>
            </section>
        </div>
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