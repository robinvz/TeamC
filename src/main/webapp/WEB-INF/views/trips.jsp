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
    <jsp:include page="header.jsp"/>

    <h2>Overview</h2>
    <form action="/createTrip" method="GET">
        <input type="submit" value="Create a timebound trip" class="btn-submit"></button>
    </form>
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
            <form action="/selectTrip" method="GET">
                <c:if test="${not empty tripsList}">
                    <c:forEach items="${sessionScope.tripsList}" var="trip">
                        <tr>
                            <div class="form-row">
                                <td>
                                    <output>${trip.title}</output>
                                </td>
                                <td>
                                    <output>${trip.description}</output>
                                </td>
                                <td>
                                    <output>${trip.privacy}</output>
                                </td>
                            </div>
                            <!--  <div class="form-buttons">
                                  <div class="button">
                                      <td><input name="submit" type="submit" value="Select"/></td>
                                  </div>
                              </div>   -->
                        </tr>
                    </c:forEach>
                </c:if>
            </form>
            </tbody>
        </table>
    </div>

    <footer><p class="footer">Trips - 2013</p></footer>
</div>
</body>
<script src="${pageContext.request.contextPath}/resources/js/jquery-1.9.0.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/knockout.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/trips.js"></script>
<!--[if lt IE 9]>
<script src="${pageContext.request.contextPath}/resources/js/html5shiv.js"></script>
<![endif]-->
</html>