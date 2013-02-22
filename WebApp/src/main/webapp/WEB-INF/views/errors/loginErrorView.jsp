<%--
  Created by IntelliJ IDEA.
  User: Robin
  Date: 17/02/13
  Time: 13:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <title>Error page</title>
</head>
<body>
<div id="page">
    <jsp:include page="../headerView.jsp"/>

    <div id="content">
        <h3>Please log in first</h3>
    </div>
</div>
<!--[if lt IE 9]>
<script src="${pageContext.request.contextPath}/resources/js/html5shiv.js"></script>
<![endif]-->
</body>
</html>