<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/createtrip.css"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <title><spring:message code="CreateTripPage"/></title>
</head>
<body>
<div id="page">
    <jsp:include page="baseView.jsp"/>

    <div id="content">
        <c:if test="${error != null}">
            <span class="errorblock">${error}</span>
        </c:if>

        <div id="timeboundTrip">
            <form id="form-createTrip" action="/createTimeLessTrip" method="POST">
                <table>
                    <tr>
                        <td><label><spring:message code="Title"/></label></td>
                        <td><input type="text" name="title" required="true" placeholder="Title"></td>
                    </tr>
                    <tr>
                        <td><label><spring:message code="Description"/></label></td>
                        <td><input type="text" name="description" required="true" placeholder="Description"></td>
                    </tr>
                    <tr>
                        <td><label><spring:message code="Privacy"/></label></td>
                        <td><input type="radio" name="privacy" value="PUBLIC" checked="true"><spring:message code="Public"/> </input></td>
                    <tr>
                        <td></td>
                        <td><input type="radio" name="privacy" value="PROTECTED"><spring:message code="Protected"/> </input></td>
                    </tr>
                    <tr>
                        <td></td>
                        <td><input type="radio" name="privacy" value="PRIVATE"><spring:message code="Private"/> </input></td>
                    </tr>
                    <tr>
                        <td><label><spring:message code="CreateTbTrip"/></label></td>
                        <td><input type="checkbox" id="checkTimeBound"></td>
                    </tr>
                    <tr class="optionsTimeBound">
                        <td><label><spring:message code="StartDate"/></label></td>
                        <td><input type="datetime-local" class="picker" name="startDate"></td>
                    </tr>
                    <tr class="optionsTimeBound">
                        <td><label><spring:message code="EndDate"/></label></td>
                        <td><input type="datetime-local" name="endDate"></td>
                    </tr>
                    <tr class="optionsTimeBound">
                        <td><label><spring:message code="Occurrence"/></label></td>
                        <td><input type="radio" name="repeat" value="ONCE" checked="true"><spring:message code="Once"/> </input></td>
                    </tr>
                    <tr class="optionsTimeBound">
                        <td></td>
                        <td><input type="radio" name="repeat" value="WEEKLY"><spring:message code="Weekly"/> </input></td>
                    </tr>
                    <tr class="optionsTimeBound">
                        <td></td>
                        <td><input type="radio" name="repeat" value="MONTHLY"><spring:message code="Monthly"/> </input></td>
                    </tr>
                    <tr class="optionsTimeBound">
                        <td></td>
                        <td><input type="radio" name="repeat" value="ANNUALLY"><spring:message code="Annually"/> </input></td>
                    </tr>
                    <tr id="amount">
                        <td><label><spring:message code="Frequency"/></label></td>
                        <td><input type="number" name="limit" min="1" max="15" value="1"/></td>
                    </tr>
                    <tr>
                        <td></td>
                        <td><input type="submit" class="btn-submit btn-blue" value="<spring:message code="Create" />">
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/resources/js/jquery-1.9.0.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/jquery-ui-1.10.1.custom.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/createtrip.js"></script>
</body>
</html>