<%@ page import="net.tanesha.recaptcha.ReCaptchaImpl" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaResponse" %>
<%@ page import="net.tanesha.recaptcha.ReCaptcha" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaFactory" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/login.css"/>
    <title><spring:message code="RetrievePassword"/></title>
</head>
<body>
<div id="page">
    <jsp:include page="baseView.jsp"/>
    <h2><spring:message code="RetrievePassword"/></h2>
    <c:if test="${error != null}">
        <span class="errorblock">${error}</span>
    </c:if>

    <form action="/retrievePassword" method="POST" id="forgotpwform">
        <p><spring:message code="retrievePasswordExplanation"/></p>
        <table>
            <tr>
                <td>Email</td>
                <td>
                    <input type="text" name="email"/>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <noscript>
                        <iframe src="http://api.recaptcha.net/noscript?k=6LfJ2N4SAAAAAJWDZEtWCVKRHKmQyWWLst5VIDeF"
                                height="300" width="500" frameborder="0"></iframe><br>
                        <textarea name="recaptcha_challenge_field" rows="3" cols="40">
                        </textarea>
                        <input type="hidden" name="recaptcha_response_field" value="manual_challenge">
                    </noscript>
                </td>
            </tr>
            <tr>
                <td></td>
                <td>
                    <button class="btn-blue" type="submit"><spring:message code="Send" /></button>
                </td>
            </tr>
        </table>

    </form>
</div>
<script type="text/javascript" src="http://api.recaptcha.net/challenge?k=6LfJ2N4SAAAAAJWDZEtWCVKRHKmQyWWLst5VIDeF"></script>
</body>
</html>