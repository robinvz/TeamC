<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
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
                <h2>Change picture</h2>

                <div class="profile-general">
                 <%--   <form id="profile-form" action="/users/editProfilePic" method="POST">
                        <table id="profile-data">
                            <tr>
                                <td>
                                    <label>Enter the path of your picture: </label>
                                </td>
                                <td>
                                    <input type="text" name="picPath">
                                </td>

                            </tr>
                            <tr>
                                <input id="btn-save" type="submit" value="Save changes" class="btn-submit btn-blue">
                            </tr>
                        </table>
                    </form>--%>

                    <form id="profile-form" action="/users/editProfilePic" method="POST" enctype="multipart/form-data">
                        <spring:message code="UploadFile" /><input type="file" name="file" />
                            <input type="submit" value="<spring:message code="UploadButton" />"  >
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