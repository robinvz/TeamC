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
            <form id="form-createTrip" action="/createTimeLessTrip" method="POST">
                <fieldset>
                    <legend><spring:message code="CreateTrip" /></legend>
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
                            <td><input type="radio" name="privacy" value="PUBLIC">Public</input>
                                <input type="radio" name="privacy" value="PROTECTED">Protected</input>
                                <input type="radio" name="privacy" value="PRIVATE">Private   </input>
                            </td>
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
                            <td><input type="submit" value="Create" class="btn-submit btn-blue"></td>
                        </tr>
                    </table>
                </fieldset>
            </form>
        </div>
    </div>

</div>
<script src="${pageContext.request.contextPath}/resources/js/jquery-1.9.0.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/createtrip.js"></script>
</body>
</html>