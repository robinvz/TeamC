<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <jsp:include page="../header.jsp"/>

    <div id="content">
        <c:choose>
            <c:when test="${not empty sessionScope.user}">
                <aside>
                    <div id="profielfoto"></div>
                    <nav class="trip-nav">
                        <ul class="trip-nav">
                            <li><a href="/profile">General</a></li>
                            <li><a href="#">Current trips</a></li>
                            <li><a href="#">Trips history</a></li>
                            <li><a href="#">Edit picture</a></li>
                            <li><a href="/editCredentials">Edit password</a></li>
                            <li><a href="#">Import Facebook account</a></li>
                            <li>
                                <form id="deleteProfileForm" action="/deleteProfile" method="get">
                                    <button type="submit" id="deleteBtn">Delete profile</button>
                                </form>
                            </li>
                        </ul>
                    </nav>
                </aside>

                <div class="inner-content">
                    <section>
                        <article>
                            <h2>${sessionScope.user.email}</h2>

                            <div class="profile-general">
                                <table>
                                    <tr>
                                        <td>
                                            <label>First name: </label>
                                            <output>${sessionScope.user.firstName}</output>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <label>Last name: </label>
                                            <output>${sessionScope.user.lastName}</output>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <label>Street: </label>
                                            <output>${sessionScope.user.address.street}</output>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <label>House nr: </label>
                                            <output>${sessionScope.user.address.houseNr}</output>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <label>City: </label>
                                            <output>${sessionScope.user.address.city}</output>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <label>Postal code: </label>
                                            <output>${sessionScope.user.address.postalCode}</output>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <label>Province: </label>
                                            <output>${sessionScope.user.address.province}</output>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <label>Country: </label>
                                            <output>${sessionScope.user.address.country}</output>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <label>Member since: </label>
                                            <output>${sessionScope.user.registerDate}</output>
                                        </td>
                                    </tr>
                                </table>
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

            </c:when>
            <c:otherwise>
                <h3>Please log in or register to view your profile.</h3>
            </c:otherwise>
        </c:choose>
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