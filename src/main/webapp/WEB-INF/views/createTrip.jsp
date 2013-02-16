<%--
  Created by IntelliJ IDEA.
  User: Robin
  Date: 13/02/13
  Time: 19:18
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/profiel.css"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <title>Create trip page</title>
</head>
<body>
<div id="page">
    <jsp:include page="header.jsp"/>

    <div id="contact">
        <h2>Create a timebound trip</h2>
    </div>
    <c:choose>
        <c:when test="${not empty sessionScope.user}">
            <form action="/createTrip" method="POST">
                <fieldset>
                    <table id="createTripTable">
                        <tr>
                            <td><label for="tripTitle">Title: </label></td>
                            <td><input id="tripTitle" type="text" required="true"></td>
                        </tr>
                        <tr>
                            <td><label for="tripDescription">Description: </label></td>
                            <td><input id="tripDescription" type="text" required="true"></td>
                        </tr>
                        <tr>
                            <td><label id="tripPrivacy">Privacy: </label></td>
                            <td><input type="radio" name="radios" value="PUBLIC">Public</input>
                                <input type="radio" name="radios" value="PROTECTED">Protected</input>
                                <input type="radio" name="radios" value="PRIVATE">Private</td>
                        </tr>
                        <tr>
                            <!--Kenmerken van de trip kunnen ingeven & toevoegen aan lijst van labels-->
                            <td><label for="tripLabels">Labels: </label></td>
                            <td><input id="tripLabels" type="text"></td>
                        </tr>
                        <tr>
                            <td><label for="tripStartDate">Start date: </label></td>
                            <td><input id="tripStartDate" type="date">
                        </tr>
                        <tr>
                            <td><label for="tripEndDate">End date: </label></td>
                            <td><input id="tripEndDate" type="date"></td></td>
                        </tr>
                    </table>
                </fieldset>
                <input type="submit" value="Create" class="btn-submit">
            </form>
            </fieldset>
        </c:when>
        <c:otherwise>
            <h3>You must be logged in to create trips.</h3>
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