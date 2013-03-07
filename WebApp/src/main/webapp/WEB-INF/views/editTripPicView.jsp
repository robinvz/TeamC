<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/trip.css"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <script src="${pageContext.request.contextPath}/resources/js/jquery-1.9.0.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/trip.js"></script>
    <title>Profile page</title>
</head>
<body>
<div id="page">
    <jsp:include page="baseView.jsp"/>
    <jsp:include page="tripHeaderView.jsp"/>

    <div class="inner-content">
        <section>
            <article>
                <h3>Change picture</h3>

                <div class="trip-general">


                    <form id="profile-form" action="/editTripPic/${trip.id}" method="POST" enctype="multipart/form-data">
                        <spring:message code="UploadFile" /><input type="file" name="file" />
                        <input type="submit" value="<spring:message code="UploadButton" />"  >
                    </form>
                </div>


            </article>
        </section>
    </div>

</div>

</body>
</html>