<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/login.css"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <title><spring:message code="LoginPage"/></title>
</head>
<body>
<div id="page">
    <jsp:include page="baseView.jsp"/>

    <h2><spring:message code="PleaseLogIn"/></h2>
    <div id="content">
        <c:if test="${success != null}">
            <span class="successblock">${success}</span>
        </c:if>

        <form:form action="/login" commandName="loginBean" method="post" id="loginform" name="loginform" dir="loginform">
            <form:errors path="*" cssClass="errorblock" element="div"/>
            <div id="fb-root"></div>
            <div id="login-buttons">
                <fb:login-button autologoutlink='true'
                                 perms='email,user_birthday,status_update,publish_stream'></fb:login-button>
                <button id="btn-login" class="btn-blue" type="button"><spring:message code="LogIn" /></button>
            </div>

            <table id="logintable">
                <tr>
                    <td>Email</td>
                    <td>
                        <form:input path="email"/>
                    </td>
                </tr>
                <tr>
                    <td><spring:message code="Password" /></td>
                    <td>
                        <form:password path="password"/>
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                        <button class="btn-blue" type="submit"><spring:message code="LogIn" /></button>
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                        <a href="retrievePassword"><spring:message code="forgotPassword"/></a>
                    </td>
                </tr>
            </table>
        </form:form>
            <script>
                if (window.location.hash) {
                    var pathArray = window.location.pathname.split('/');
                    var tripId = window.location.hash.substring(1);//pathArray[pathArray.length -1];
                    if ('${user}' != ''){
                        window.location = '/trip/' + tripId;
                    } else {
                        $('#loginform').attr('action', '/login/' + tripId);
                    }
                }
            </script>
    </div>

</div>
<script src="${pageContext.request.contextPath}/resources/js/jquery-1.9.0.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/login.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/facebooklogin.js"></script>
</body>
</html>