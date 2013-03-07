<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/trip.css"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <title><spring:message code="TripPage"/></title>
</head>
<body>
<div id="page">
    <jsp:include page="../baseView.jsp"/>
    <jsp:include page="../tripHeaderView.jsp"/>

    <div class="inner-content">
        <c:if test="${error != null}">
            <span class="errorblock">${error}</span>
        </c:if>

        <c:if test="${not empty user && trip.organizer == user}">
            <h3>Invite a user</h3>

            <form action="/inviteUser/${trip.id}/findUsersByKeyword" method="GET">
                <table>
                    <tr>
                        <td><spring:message code="User"/></td>
                        <td><input type="text" name="keyword"></td>
                        <td>
                            <input type="submit" id="btn-SearchUsers" class="btn-blue" value="Search"/>
                        </td>
                    </tr>
                </table>
            </form>
        </c:if>
        <c:choose>
            <c:when test="${empty usersByKeyword}">
                <h3>No users found.</h3>
            </c:when>
            <c:otherwise>
                <c:set var="count" value="0" scope="page"/>
                <table>
                    <thead>
                    <tr>
                        <th></th>
                        <th><spring:message code="FirstName"/></th>
                        <th><spring:message code="LastName"/></th>
                        <th>Email</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${usersByKeyword}" var="userByKeyword">
                        <tr id="user-${userByKeyword.id}">
                            <td>
                                <c:set var="count" value="${count + 1}" scope="page"/>
                                <c:out value="${count}"></c:out>
                            </td>
                            <td>
                                    ${userByKeyword.firstName}
                            </td>
                            <td>
                                    ${userByKeyword.lastName}
                            </td>
                            <td>
                                    ${userByKeyword.email}
                            </td>
                            <td>
                                <c:if test="${invitation.user != user}">
                                    <a href="/inviteUser/${trip.id}/sendInvite/${userByKeyword.email}">
                                        <button type="button" id="btn-inviteUser"><spring:message code="Invite"/>
                                        </button>
                                    </a>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>