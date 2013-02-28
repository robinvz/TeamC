<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/trip.css"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <title>Trip page</title>
</head>
<body>
<div id="page">
    <jsp:include page="baseView.jsp"/>
    <jsp:include page="tripHeaderView.jsp"/>

    <div class="inner-content">
        <section>
            <article>
                <c:if test="${error != null}">
                    <span class="errorblock">${error}</span>
                </c:if>
                <div class="trip-info">
                    <table>
                        <tr>
                            <td><label>Description:</label></td>
                            <td>${trip.description}</td>
                        </tr>
                        <tr>
                            <td><label>Privacy:</label></td>
                            <td>${trip.privacy}</td>
                        </tr>
                        <tr>
                            <td><label>Active:</label></td>
                            <td>${trip.active}</td>
                        </tr>

                        <tr>
                            <td><label>Published:</label></td>
                            <td>${trip.published}</td>
                        </tr>
                        <c:choose>
                            <c:when test="${not empty trip.labels}">
                                <c:forEach items="${trip.labels}" var="label">
                                    <tr>
                                        <td>${label}</td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <label>Add labels here</label>
                            </c:otherwise>
                        </c:choose>
                    </table>
                </div>

                <div class="trip-requirements">

                </div>

                <div class="trip-stops">

                </div>

                <div class="trip-participants">

                </div>
                <c:if test="${trip.privacy == 'PROTECTED' and user != null}">
                    <a href="/subscribe?tripId=${trip.id}">
                        <img id="subscribeButton"
                             src="${pageContext.request.contextPath}/resources/res/img/subscribe.jpg">
                    </a>
                </c:if>
            </article>
        </section>
    </div>
</div>
</body>
</html>