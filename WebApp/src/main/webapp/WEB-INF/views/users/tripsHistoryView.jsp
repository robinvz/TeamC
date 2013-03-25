<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/profiel.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/trips.css"/>
    <title><spring:message code="TripsHistory"/></title>
</head>
<body>
    <div id="page">
        <jsp:include page="../baseView.jsp"/>
        <jsp:include page="profileHeaderView.jsp"/>
        <div class="content">
            <h2><spring:message code="TripsHistory"/></h2>
            <p>Please be aware that public trips don't require any kind of authentication and therefore aren't shown in trips history</p>
            <table class="tables">
                <thead>
                <tr>
                    <th>Title</th>
                    <th>Description</th>
                    <th>Enrollment date</th>
                    <th>Status</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${user.enrollments}" var="enrollment">
                   <tr>
                       <td>${enrollment.trip.title}</td>
                       <td>${enrollment.trip.description}</td>
                       <td>${enrollment.date}</td>
                       <td>${enrollment.status}</td>
                   </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/resources/js/jquery-1.9.0.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/jquery.dataTables.js"></script>
    <script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.8.24/jquery-ui.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/jquery.dataTables.rowReordering.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/trips.js"></script>
</body>
</html>