<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/profiel.css"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <title>Profile page</title>
</head>
<body>
<div id="page">
    <jsp:include page="../baseView.jsp"/>
    <jsp:include page="profileHeaderView.jsp"/>

    <div class="content">
        <section>
            <article>
                <h2>${user.firstName} ${user.lastName}</h2>
                <div class="profile-general">
                     <button id="btn-edit" class="profile-view">Edit</button>
                    <form id="profile-form" action="/users/editProfile" method="POST">
                    <table id="profile-data">

                        <tr>

                            <td>
                                <label>First name: </label>
                            </td>
                            <td>
                                <output class="profile-view">${user.firstName}</output>
                                <input class="profile-edit" type="text" name="firstName">
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label>Last name: </label>
                            </td>
                            <td>
                                <output class="profile-view">${user.lastName}</output>
                                <input class="profile-edit" type="text" name="lastName">
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label>Street: </label>
                            </td>
                            <td>
                                <output class="profile-view">${user.address.street}</output>
                                <input class="profile-edit" type="text" name="street">
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label>House nr: </label>
                            </td>
                            <td>
                                <output class="profile-view">${user.address.houseNr}</output>
                                <input class="profile-edit" type="text" name="houseNr">
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label>City: </label>
                            </td>
                            <td>
                                <output class="profile-view">${user.address.city}</output>
                                <input class="profile-edit" type="text" name="city">
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label>Postal code: </label>
                            </td>
                            <td>
                                <output class="profile-view">${user.address.postalCode}</output>
                                <input class="profile-edit" type="text" name="postalCode">
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label>Province: </label>
                            </td>
                            <td>
                                <output class="profile-view">${user.address.province}</output>
                                <input class="profile-edit" type="text" name="province">
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label>Country: </label>
                            </td>
                            <td>
                                <output class="profile-view">${user.address.country}</output>
                                <input class="profile-edit" type="text" name="country">
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label>Member since: </label>
                            </td>
                            <td>
                                <output>${user.registerDate}</output>
                                <input style="display: none" type="text">
                            </td>
                        </tr>
                        <tr>
                            <input id="btn-save" type="submit" value="Save changes" class="btn-submit profile-edit">
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