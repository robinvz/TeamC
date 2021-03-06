<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/profiel.css"/>
    <link rel="stylesheet" media="(max-width:1040px)" href="${pageContext.request.contextPath}/resources/css/main-responsive.css">
    <link rel="stylesheet" media="(min-width:1039px)" href="${pageContext.request.contextPath}/resources/css/responsive-fix.css">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <title><spring:message code="ProfilePage"/></title>
</head>
<body>
<div id="page">
    <jsp:include page="../baseView.jsp"/>
    <jsp:include page="profileHeaderView.jsp"/>

    <div class="content">
        <section>
            <article>
                <h2><spring:message code="ChangePicture"/></h2>

                <div class="profile-general">
                    <form id="profile-form" action="/users/editProfilePic" method="POST" enctype="multipart/form-data">
                        <spring:message code="UploadFile" /><input type="file" name="file" value="<spring:message code="ChooseFile"/>"/>
                        <input type="submit" value="<spring:message code="UploadButton" />"  >
                    </form>
                </div>
            </article>
        </section>
    </div>

</div>
<script src="${pageContext.request.contextPath}/resources/js/jquery-1.9.0.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/profile.js"></script>
</body>
</html>