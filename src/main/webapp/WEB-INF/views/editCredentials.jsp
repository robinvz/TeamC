<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Mathias
  Date: 8-2-13
  Time: 10:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/profiel.css"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <title>Edit credentials page</title>
</head>
<body>
<div id="page">
    <jsp:include page="header.jsp" />

    <div id="contact">
        <h2>Credentials</h2>
    </div>
    <c:if test="${not empty sessionScope.user}">
        <form:form method="POST" modelAttribute="user">
            <fieldset>
                <legend>Edit Profile</legend>
                <table>
                    <tr>
                        <td><form:label path="email">Email: </form:label></td>
                        <td><form:input path="email"/></td>
                    </tr>
                    <tr>
                        <td><form:label path="password">Password: </form:label></td>
                        <td><form:input path="password"/></td>
                    </tr>
                </table>
            </fieldset>
            <button id="search">Save</button>
        </form:form>
    </c:if>
    <footer><p class="footer">Trips - 2013</p></footer>
</div>
</body>
<script src="${pageContext.request.contextPath}/resources/js/jquery-1.9.0.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/knockout.js"></script>
<!--[if lt IE 9]>
<script src="${pageContext.request.contextPath}/resources/js/html5shiv.js"></script>
<![endif]-->
</html>