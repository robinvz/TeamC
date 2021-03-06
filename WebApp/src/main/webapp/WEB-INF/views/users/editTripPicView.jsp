<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/trip.css"/>
    <link rel="stylesheet" media="(max-width:1040px)" href="${pageContext.request.contextPath}/resources/css/main-responsive.css">
    <link rel="stylesheet" media="(min-width:1039px)" href="${pageContext.request.contextPath}/resources/css/responsive-fix.css">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <link rel="stylesheet" href="<spring:theme code="css"/>" type="text/css"/>
    <title><spring:message code="TripPage"/></title>
</head>
<body>
<div id="page">
    <jsp:include page="../baseView.jsp"/>
    <jsp:include page="../tripHeaderView.jsp"/>

    <div class="inner-content">
        <section>
            <article>
                <h3><spring:message code="EditPicture"/></h3>

                <div class="trip-general">

                    <form id="profile-form" action="/users/editTripPic/${trip.id}" method="POST" enctype="multipart/form-data">
                        <spring:message code="UploadFile" /><input type="file" name="file" />
                        <input type="submit" value="<spring:message code="UploadButton" />"  >
                    </form>

                    <h3><spring:message code="selectTheme"/></h3>
                    <table>
                        <tr>
                            <td class="theme default-theme"><input id="default" class="rdb-theme" type="radio" name="theme" value="default"><spring:message
                                    code="defaultTheme"/> </input></td>
                        <tr>
                            <td class="theme blue-theme"><input id="blue" class="rdb-theme" type="radio" name="theme" value="blue"><spring:message
                                    code="blueTheme"/> </input></td>
                        </tr>
                        <tr>
                            <td class="theme dark-theme"><input id="dark" class="rdb-theme" id="dark" type="radio" name="theme" value="dark"><spring:message
                                    code="darkTheme"/> </input></td>
                        </tr>
                    </table>
                </div>

            </article>
        </section>
    </div>

</div>
<script src="${pageContext.request.contextPath}/resources/js/jquery-1.9.0.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/trip.js"></script>
<script>
    $(document).ready(function () {
        setCurrentTheme('${trip.theme}', ${trip.id});
    });
</script>
</body>
</html>