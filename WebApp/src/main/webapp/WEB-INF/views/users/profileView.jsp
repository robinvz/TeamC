<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/profiel.css"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <title>Profile page</title>
</head>
<body>
<div id="page">
    <jsp:include page="../headerView.jsp"/>
    <jsp:include page="profileHeaderView.jsp"/>
    <div class="content">
        <section>
            <article>
                <h2>${sessionScope.user.email}</h2>

                <div class="profile-general">
                    <table>
                        <tr>
                            <td>
                                <label>First name: </label>
                                <output>${user.firstName}</output>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label>Last name: </label>
                                <output>${user.lastName}</output>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label>Street: </label>
                                <output>${user.address.street}</output>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label>House nr: </label>
                                <output>${user.address.houseNr}</output>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label>City: </label>
                                <output>${user.address.city}</output>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label>Postal code: </label>
                                <output>${user.address.postalCode}</output>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label>Province: </label>
                                <output>${user.address.province}</output>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label>Country: </label>
                                <output>${user.address.country}</output>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label>Member since: </label>
                                <output>${user.registerDate}</output>
                            </td>
                        </tr>
                        <!-- Indien edit profile op andere pagina komt
                            beter div profile general invisible maken en profile-edit visible?
                        <tr>
                            <td>
                                <a href="/users/editProfile">Edit profile</a>
                            </td>
                        </tr>
                        -->
                    </table>
                </div>

                <div class="profile-edit">
                    <form action="/users/editProfile" method="POST">
                        <table>
                            <tr>
                                <td><label>First name: </label></td>
                                <td><input type="text" name="firstName"></td>
                            </tr>
                            <tr>
                                <td><label>Last name: </label></td>
                                <td><input type="text" name="lasttName"></td>
                            </tr>
                            <tr>
                                <td><label>Street: </label></td>
                                <td><input type="text" name="street"></td>
                            </tr>
                            <tr>
                                <td><label>House nr: </label></td>
                                <td><input type="text" name="houseNr"></td>
                            </tr>
                            <tr>
                                <td><label>City: </label></td>
                                <td><input type="text" name="city"></td>
                            </tr>
                            <tr>
                                <td><label>Postal code: </label></td>
                                <td><input type="text" name="postalCode"></td>
                            </tr>
                            <tr>
                                <td><label>Province: </label></td>
                                <td><input type="text" name="province"></td>
                            </tr>
                            <tr>
                                <td><label>Country: </label></td>
                                <td><input type="text" name="country"></td>
                            </tr>
                        </table>
                        <input type="submit" value="Edit">
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

    <footer><p class="footer">Trips - 2013</p></footer>
</div>
</body>
</html>