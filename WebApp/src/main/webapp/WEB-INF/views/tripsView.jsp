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
        <div id="control-bar">
    <form action="/users/createTrip" method="GET">
        <button id="btn-addtrip" type="submit" class="btn-submit">Create Trip</button>
    </form>
    <select id="filter" name="filterTripsMenu">
        <option>Filter</option>
        <option value="">Repeating trips</option>
        <option value="">Timebound trips</option>
    </select>
        </div>
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

            <tbody>
            <div>
                <c:if test="${not empty allNonPrivateTrips}">
                    <c:forEach items="${allNonPrivateTrips}" var="allNonPrivateTrip">
                            <tr id="trip${allNonPrivateTrip.id}">
                                <div class="form-row">
                                    <td>
                                            ${allNonPrivateTrip.title}
                                    </td>
                                    <td>
                                            ${allNonPrivateTrip.description}
                                    </td>
                                    <td>
                                            ${allNonPrivateTrip.privacy}
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
            </div>
            </form>
            </tbody>
        </table>
    </div>

    <footer><p class="footer">Trips - 2013</p></footer>
</div>
<script src="${pageContext.request.contextPath}/resources/js/jquery-1.9.0.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/trips.js"></script>
<!--[if lt IE 9]>
<script src="${pageContext.request.contextPath}/resources/js/html5shiv.js"></script>
<![endif]-->
</body>
</html>