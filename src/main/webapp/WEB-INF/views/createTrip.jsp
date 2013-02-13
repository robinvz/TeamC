<%--
  Created by IntelliJ IDEA.
  User: Robin
  Date: 13/02/13
  Time: 19:18
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
        <h2>Create a trip</h2>
    </div>


    <fieldset id="form-login">
        <legend>Details</legend>
        <form action="/createTrip" method="POST">
            <table id="createTripTable">
                <tr>
                    <td><label for="tripTitle">Title: </label></td>
                    <td><input id="tripTitle" type="text"></td>
                </tr>
                <tr>
                    <td><label for="tripDescription">Description: </label></td>
                    <td><input id="tripDescription" type="text"></td>
                </tr>
                <tr>
                    <td><label id="tripPrivacy">Privacy: </label></td>
                    <td><input id="tripPublic" value="Public" type="radio" checked='checked'/>
                        <input id="tripProtected" name="Protected" value="Protected" type="radio"/>
                        <input id="tripPrivate" name="Private" value="Private" type="radio"/></td>
                </tr>
                <tr>
                    <!--Kenmerken van de trip kunnen ingeven & toevoegen aan lijst van labels-->
                    <td><label for="tripLabels">Labels: </label></td>
                    <td><input id="tripLabels" type="text"></td>
                </tr>
                <tr>
                    <td><label id="tripInfo">If you want to make a timebound trip please enter 2 dates.</label></td>
                </tr>
                <tr>
                    <td><label for="tripDates1">Start date: </label></td>
                    <td><input id="tripDates1" type="date">
                </tr>
                <tr>
                    <td><label for="tripDates2">End date: </label></td>
                    <td><input id="tripDates2" type="date"></td></td>
                </tr>
                <tr>
                    <td><input type="submit" value="Create" class="btn-submit"></td>
                </tr>
            </table>
        </form>
    </fieldset>


    <footer><p class="footer">Trips - 2013</p></footer>
</div>
</body>
<script src="${pageContext.request.contextPath}/resources/js/jquery-1.9.0.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/knockout.js"></script>
<!--[if lt IE 9]>
<script src="${pageContext.request.contextPath}/resources/js/html5shiv.js"></script>
<![endif]-->
</html>