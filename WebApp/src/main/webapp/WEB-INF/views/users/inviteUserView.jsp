<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/trip.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/inviteUser.css"/>
    <link rel="stylesheet" media="(max-width:1040px)" href="${pageContext.request.contextPath}/resources/css/main-responsive.css">
    <link rel="stylesheet" media="(min-width:1039px)" href="${pageContext.request.contextPath}/resources/css/responsive-fix.css">
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

        <h3><spring:message code="Invitations"/></h3>
        <c:choose>
            <c:when test="${empty invitations}">
                <h3><spring:message code="NoInvitations"/>.</h3>
            </c:when>
            <c:otherwise>
                <c:set var="count" value="0" scope="page"/>
                <table id="invitedUsers-table" class="dataTable">
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
                    <c:forEach items="${invitations}" var="invitation">
                        <tr id="invitation-${invitation.id}">
                            <td>
                                <c:set var="count" value="${count + 1}" scope="page"/>
                                <c:out value="${count}"></c:out>
                            </td>
                            <td>
                                    ${invitation.getUser().getFirstName()}
                            </td>
                            <td>
                                    ${invitation.getUser().getLastName()}
                            </td>
                            <td>
                                    ${invitation.getUser().getEmail()}
                                <input name="uninviteEmail" value="${invitation.getUser().getEmail()}"
                                       hidden="hidden"/>
                            </td>
                            <td>
                                <c:if test="${not empty user && trip.organizer == user}">
                                    <c:choose>
                                        <c:when test="${invitation.user == trip.organizer}">
                                            <spring:message code="Organizer"/>
                                        </c:when>
                                        <c:when test="${invitation.answer == 'ACCEPTED'}">
                                            <spring:message code="Accepted"/>
                                        </c:when>
                                        <c:when test="${invitation.answer == 'DECLINED'}">
                                            <spring:message code="Declined"/>
                                        </c:when>
                                        <c:when test="${invitation.answer == 'UNANSWERED'}">
                                            <input class="uninvite-input" type="submit"
                                                   value="<spring:message code="Uninvite"/>"/>
                                        </c:when>
                                    </c:choose>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>

        <c:if test="${not empty user && trip.organizer == user}">
            <h3><spring:message code="InviteAUser"/></h3>

            <form class="above-footer" id="find" action="/inviteUser/${trip.id}/findUsersByKeyword" method="GET">
                <table id="findUsers-table">
                    <tr>
                        <td><spring:message code="User"/></td>
                        <td><input type="text" name="keyword"></td>
                        <td>
                            <input type="submit" id="btn-SearchUsers" class="btn-blue" value="Search"/>
                        </td>
                    </tr>
                </table>
            </form>
            <c:choose>
                <c:otherwise>
                    <c:set var="count" value="0" scope="page"/>
                    <table id="foundUsers-table" class="dataTable above-footer">
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
                            <c:set var="isInvited" value="false" scope="page"/>
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
                                    <input name="userByKeywordEmail" value="${userByKeyword.email}"
                                           hidden="hidden"/>
                                </td>
                                <td>
                                    <c:if test="${not empty invitations}">
                                        <c:forEach items="${invitations}" var="invitation">
                                            <c:if test="${invitation.getUser() == userByKeyword && invitation.getTrip() == trip}">
                                                <c:set var="isInvited" value="true" scope="page"/>
                                            </c:if>
                                        </c:forEach>
                                    </c:if>
                                    <c:if test="${isInvited == false}">
                                        <input class="invite-input" type="submit"
                                               value="<spring:message code="Invite"/>"/>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </c:if>
    </div>
</div>
<script src="${pageContext.request.contextPath}/resources/js/jquery-1.9.0.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/jquery.dataTables.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/inviteUser.js"></script>
<script>
    $(document).ready(function () {
        getTripId(${trip.id});
    });
</script>
</body>
</html>