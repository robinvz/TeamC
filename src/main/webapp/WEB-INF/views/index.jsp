<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css" />
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <title>Home page</title>
</head>
<body>
<div id="page">
    <jsp:include page="header.jsp" />

    <div id="content">
        <h2>Welcome</h2>
        <h3>Introduction -- We are blablabla and we blablabla.</h3>
    </div>
    <footer><p class="footer">Trips - 2013</p></footer>
</div>
</body>
<script src="../js/jquery-1.9.0.min.js"></script>
<script src="../js/knockout.js"></script>

<!--[if lt IE 9]>
<script src="../js/html5shiv.js"></script>
<![endif]-->
</html>