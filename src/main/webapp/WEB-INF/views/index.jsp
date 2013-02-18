<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <title>Home page</title>
</head>
<body>
<div id="page">
    <jsp:include page="header.jsp"/>

    <div id="content">
        <h2>Welcome</h2>
        <p>
            The trips system was developed by six young students. It is an easy tool to enjoy outings to the fullest.
            You can manage your costs, chat with friends, participate in a quiz and so on. Visit the trips page
            to view all current public trips.
        <p>
            If you are interested in organizing trips yourself, please register first. For further questions or
            remarks please consult our contact page.
        </p>
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