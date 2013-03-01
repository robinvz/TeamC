<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/createtrip.css"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <title><spring:message code="CreateTripPage" /></title>
</head>
<body>
<div id="page">
    <jsp:include page="../baseView.jsp"/>

    <div id="content">
        <div id="timeboundTrip">
            <h2>Create a new Trip</h2>
            <form id="form-createTrip" action="/createTimeLessTrip" method="POST">
                    <table>
                        <tr>
                            <td><label><spring:message code="Title" /></label></td>
                            <td><input type="text" name="title" required="true"></td>
                        </tr>
                        <tr>
                            <td><label><spring:message code="Description" /></label></td>
                            <td><input type="text" name="description" required="true"></td>
                        </tr>
                        <tr>
                            <td><label><spring:message code="Privacy" /></label></td>
                            <td><input type="radio" name="privacy" value="PUBLIC" checked="true"><spring:message code="Public" /> </input></td>
                            <tr><td></td><td><input type="radio" name="privacy" value="PROTECTED"><spring:message code="Protected" /> </input></td></tr>
                            <tr><td></td><td><input type="radio" name="privacy" value="PRIVATE"><spring:message code="Private" /> </input></td></tr>
                        </tr>

                        <tr>
                            <td><label><spring:message code="CreateTbTrip" /></label></td>
                            <td><input type="checkbox" id="checkTimeBound"></td>
                        </tr>
                        <tr class="optionsTimeBound">
                            <td><label><spring:message code="StartDate" /></label></td>
                            <td><input type="date" name="startDate">
                        </tr>
                        <tr class="optionsTimeBound">
                            <td><label><spring:message code="EndDate" /></label></td>
                            <td><input type="date" name="endDate"></td>
                            </td>
                        </tr>
                        <tr>
                            <td></td>
                            <td><input type="submit" class="btn-submit btn-blue" value="<spring:message code="Create" />"></td>
                        </tr>
                    </table>
            </form>
        </div>
    </div>

</div>
<script src="${pageContext.request.contextPath}/resources/js/jquery-1.9.0.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/createtrip.js"></script>
</body>
</html>