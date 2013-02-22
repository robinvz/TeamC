<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/trips.css"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <title>Trip overview</title>
</head>
<body>
<div id="page">
    <jsp:include page="headerView.jsp"/>

    <h2>Overview</h2>

    <form action="/users/createTrip" method="GET">
        <input type="submit" value="Create a trip" class="btn-submit"></button>
    </form>
    <select name="filterTripsMenu">
        <option>Filter</option>
        <option value="">Repeating trips</option>
        <option value="">Timebound trips</option>
    </select>

    <div id="content">
        <table id="trips">
            <!-- model heeft momenteel: type, id, description, privacy, published, title, userId -->
            <thead>
            <tr>
                <th>Title</th>
                <th>Description</th>
                <th>Privacy</th>
            </tr>
            </thead>

            <tbody id="trips-list">
            <div id="timelessTrips">
                <c:if test="${not empty allNonPrivateTrips}">
                    <c:forEach items="${allNonPrivateTrips}" var="allNonPrivateTrip">
                        <a href="trip/${allNonPrivateTrip.id}">
                            <tr id="trip${allNonPrivateTrip.id}">
                                <div class="form-row">

                                    <td>
                                        <a href="/trip/${allNonPrivateTrip.id}">
                                                ${allNonPrivateTrip.title}
                                        </a>
                                    </td>
                                    <td>
                                        <output>${allNonPrivateTrip.description}</output>
                                    </td>
                                    <td>
                                        <output>${allNonPrivateTrip.privacy}</output>
                                    </td>
                                </div>
                                <!--  <div class="form-buttons">
                                      <div class="button">
                                          <td><input name="submit" type="submit" value="Select"/></td>
                                      </div>
                                  </div>   -->
                            </tr>
                        </a>
                    </c:forEach>
                </c:if>
            </div>
            </form>
            </tbody>
        </table>
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