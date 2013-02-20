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
                <h3>Trip</h3>
                <ul class="trip-nav">
                    <li><a href="#">Info</a></li>
                    <li><a href="#">Requirements</a></li>
                    <li><a href="#">Stops</a></li>
                    <li><a href="#">Chat</a></li>
                    <li><a href="#">Participants</a></li>
                    <li><a href="#">Results</a></li>
                    <li><a href="#">Start</a></li>
                    <li><a href="#">Edit</a></li>
                    <li><a href="#">Delete</a></li>
                </ul>
            </nav>
        </aside>

        <div class="inner-content">
            <section>
                <article>
                    <p>CONTENT</p>
                </article>
            </section>
        </div>
    </div>
    <footer><p class="footer">Trips - 2013</p></footer>
</div>
<script src="${pageContext.request.contextPath}/resources/js/jquery-1.9.0.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/knockout.js"></script>
<!--[if lt IE 9]>
<script src="${pageContext.request.contextPath}/resources/js/html5shiv.js"></script>
<![endif]-->
</body>
</html>