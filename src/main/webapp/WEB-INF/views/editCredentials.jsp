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
    <jsp:include page="header.jsp"/>

    <div id="contact">
        <h2>Credentials</h2>
    </div>
    <c:choose>
        <c:when test="${not empty sessionScope.user}">
            <fieldset id="form-login">
                <legend>Change your password</legend>
                <form action="/editCredentials" method="POST">
                    <table>
                        <tr>
                            <td>
                                <label for="oldPassword">Old password: </label>
                                <input type="password" id="oldPassword">
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label for="newPassword">New password: </label>
                                <input type="password" id="newPassword">
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <input type="submit" value="Save" class="btn-submit">
                            </td>
                        </tr>
                    </table>
                </form>
            </fieldset>
        </c:when>
        <c:otherwise>
            <h2>You must be logged in to modify your account.</h2>
        </c:otherwise>
    </c:choose>
    <footer><p class="footer">Trips - 2013</p></footer>
</div>
</body>
<script src="${pageContext.request.contextPath}/resources/js/jquery-1.9.0.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/knockout.js"></script>
<!--[if lt IE 9]>
<script src="${pageContext.request.contextPath}/resources/js/html5shiv.js"></script>
<![endif]-->
</html>