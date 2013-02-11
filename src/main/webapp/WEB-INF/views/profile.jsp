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
    <jsp:include page="header.jsp"/>

    <div id="content">
        <c:choose>
            <c:when test="${not empty sessionScope.user}">
                <aside>
                    <div id="profielfoto"></div>
                    <nav class="trip-nav">
                        <ul class="trip-nav">
                            <li><a href="/profile">General</a></li>
                            <li><a href="#">Contact</a></li>
                            <li><a href="#">Import Facebook account</a></li>
                            <li><a href="#">Current trips</a></li>
                            <li><a href="#">Trips history</a></li>
                            <li><a href="/editCredentials">Edit password</a></li>
                            <li><form id="deleteProfileForm" action="/deleteProfile" method="get">
                                <button type="submit" id="deleteBtn">Delete profile</button>
                            </form></li>
                        </ul>
                    </nav>
                </aside>

                <div class="inner-content">
                    <section>

                        <article>
                            <h2>${sessionScope.user.email}</h2>

                            <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris dictum libero quis libero
                                facilisis eu pharetra sem volutpat. Sed faucibus lectus vel diam dignissim vitae blandit
                                nisl
                                hendrerit. Praesent fringilla dui id dui mollis ultricies. Duis libero ligula, hendrerit
                                cursus
                                ultricies sed, pulvinar eu velit. Donec ut commodo nibh. Etiam placerat sem vel eros
                                lacinia
                                pretium. Vivamus malesuada, sapien quis rutrum eleifend, mi lacus cursus augue, non
                                molestie
                                felis nibh ac lacus. Nunc dapibus, augue vel consequat lacinia, urna arcu convallis
                                turpis, quis
                                malesuada magna mi a dolor. Phasellus vel nunc vel dui sodales tristique in tempor diam.

                                Morbi lacinia libero eu velit pharetra pharetra id vel mi. Maecenas varius neque dolor,
                                sed
                                elementum dui. Nulla laoreet mauris non lectus feugiat a facilisis dolor commodo.
                                Praesent
                                faucibus, eros vel sagittis varius, elit leo egestas lacus, quis pulvinar est est at
                                enim. Nulla
                                a diam quis est pretium feugiat. Mauris et massa leo. Donec ac dolor at est porttitor
                                tempor id
                                vitae sem. Sed rutrum cursus sagittis. Nam posuere lorem ac augue placerat id ultricies
                                turpis
                                mollis. Etiam eu velit dolor. Aenean velit massa, pharetra nec sodales ut, interdum
                                vitae leo.
                                Aliquam erat volutpat. Aliquam fermentum, nisl eget laoreet placerat, mauris ligula
                                lobortis
                                sem, at pulvinar ante mi in nisi. Integer dolor ipsum, lacinia a euismod ut,
                                pellentesque quis
                                nulla.
                            </p>

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