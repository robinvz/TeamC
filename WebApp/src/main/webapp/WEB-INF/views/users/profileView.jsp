<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/profiel.css"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <title><spring:message code="ProfilePage"/></title>
</head>
<body>
<div id="page">
    <jsp:useBean id="today" class="java.util.Date" scope="page" />
    <jsp:include page="../baseView.jsp"/>
    <jsp:include page="profileHeaderView.jsp"/>

    <div class="content">
        <section>
            <article>
                <c:if test="${error != null}">
                    <span class="errorblock">${error}</span>
                </c:if>
                <h2>${user.firstName} ${user.lastName}</h2>
                <div class="profile-general">
                    <button id="btn-edit" class="profile-view btn-blue"><spring:message code="Edit"/></button>
                    <form id="profile-form" action="/users/editProfile" method="POST">
                        <table id="profile-data">
                            <tr>
                                <td>
                                    <label><spring:message code="FirstName"/></label>
                                </td>
                                <td>
                                    <output class="profile-view">${user.firstName}</output>
                                    <input class="profile-edit" type="text" name="firstName">
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label><spring:message code="LastName"/></label>
                                </td>
                                <td>
                                    <output class="profile-view">${user.lastName}</output>
                                    <input class="profile-edit" type="text" name="lastName">
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label><spring:message code="Street"/></label>
                                </td>
                                <td>
                                    <output class="profile-view">${user.address.street}</output>
                                    <input class="profile-edit" type="text" name="street">
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label><spring:message code="HouseNr"/></label>
                                </td>
                                <td>
                                    <output class="profile-view">${user.address.houseNr}</output>
                                    <input class="profile-edit" type="text" name="houseNr">
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label><spring:message code="City"/></label>
                                </td>
                                <td>
                                    <output class="profile-view">${user.address.city}</output>
                                    <input class="profile-edit" type="text" name="city">
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label><spring:message code="PostalCode"/></label>
                                </td>
                                <td>
                                    <output class="profile-view">${user.address.postalCode}</output>
                                    <input class="profile-edit" type="text" name="postalCode">
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label><spring:message code="Country"/></label>
                                </td>
                                <td>
                                    <output class="profile-view">${user.address.country}</output>
                                    <input class="profile-edit" type="text" name="country">
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label><spring:message code="MemberSince"/></label>
                                </td>
                                <td>
                                    ${user.registerDate}
                                </td>
                            </tr>
                            <tr>
                                <input id="btn-save" type="submit" value="<spring:message code="Edit" />"
                                       class="btn-submit profile-edit btn-blue">
                            </tr>
                        </table>
                    </form>
                </div>
                <!-- Only active trips -->
                <div class="profile-currentTrips">

                </div>
                <!-- Only non-active trips -->
                <div class="profile-tripsHistory">

                </div>

            </article>
        </section>
    </div>

</div>
<script src="${pageContext.request.contextPath}/resources/js/jquery-1.9.0.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/profile.js"></script>
</body>
</html>