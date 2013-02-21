<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Robin
  Date: 8/02/13
  Time: 12:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/profiel.css"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
</head>
<body>
<header>
    <img class="header-img" src="${pageContext.request.contextPath}/resources/res/img/dragon.png" alt="Logo"
         title="Home"/>

    <h1 class="header-title">Trips</h1>
    <nav class="header-nav">
        <ul class="nav">
            <li><a id="nav-home" class="nav-link" href="/" title="Home">Home</a></li>
            <li><a id="nav-trips" class="nav-link" href="/trips" title="">Trips</a></li>
            <c:if test="${not empty sessionScope.user}">
                <li><a id="nav-profile" class="nav-link" href="/users/profile" title="">Profile</a></li>
            </c:if>
            <c:choose>
                <c:when test="${empty sessionScope.user}">
                    <li><a id="nav-login" class="nav-link" href="/login" title="">Log in</a></li>
                    <li><a id="nav-register" class="nav-link" href="/register" title="">Register</a></li>
                </c:when>
                <c:otherwise>
                    <li><a id="nav-logout" class="nav-link" href="/logout" title="">Log out</a></li>
                </c:otherwise>
            </c:choose>
            <li><a id="nav-contact" class="nav-link" href="/contact" title="">Contact</a></li>
        </ul>
    </nav>
</header>
<script src="${pageContext.request.contextPath}/resources/js/jquery-1.9.0.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/header.js"></script>

</body>
</html>