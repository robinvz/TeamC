<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/login.css"/>
    <!--[if lt IE 9]>
    <script src="${pageContext.request.contextPath}/resources/js/html5shiv.js"></script>
    <![endif]-->
    <title><spring:message code="RegisterPage" /></title>
</head>
<div id="page">
    <jsp:include page="baseView.jsp"/>

    <div id="content">
        <h2><spring:message code="PleaseFillIn" /></h2>
        <form:form method="POST" commandName="user" action="register" id="registerform">
            <form:errors path="*" cssClass="errorblock" element="div" />
                <table>
                    <tr>
                        <td>
                            <label>Email*</label>
                        </td>
                        <td>
                            <form:input path="email"></form:input>
                            <form:errors path="email" cssClass="error"></form:errors>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>Password*</label>
                        </td>
                        <td>
                            <input type="password" name="password">
                        </td>
                    </tr>
                </table>

                <table>
                    <tr>
                        <td>
                            <label><spring:message code="FirstName" /></label>
                        </td>
                        <td>
                            <form:input path="firstName"></form:input>
                            <form:errors path="firstName" cssClass="error"></form:errors>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label><spring:message code="LastName" /></label>
                        </td>
                        <td>
                            <form:input path="lastName"></form:input>
                            <form:errors path="lastName" cssClass="error"></form:errors>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label><spring:message code="Street" /></label>
                        </td>
                        <td>
                            <form:input path="address.street"></form:input>
                            <form:errors path="address.street" cssClass="error"></form:errors></td>
                    </tr>
                    <tr>
                        <td>
                            <label><spring:message code="HouseNr" /></label>
                        </td>
                        <td>
                            <form:input path="address.houseNr"></form:input>
                            <form:errors path="address.houseNr" cssClass="error"></form:errors></td>
                    </tr>
                    <tr>
                        <td>
                            <label><spring:message code="City" /></label>
                        </td>
                        <td>
                            <form:input path="address.city"></form:input>
                            <form:errors path="address.city" cssClass="error"></form:errors></td>
                    </tr>
                    <tr>
                        <td>
                            <label><spring:message code="PostalCode" /></label>
                        </td>
                        <td>
                            <form:input path="address.postalCode"></form:input>
                            <form:errors path="address.postalCode" cssClass="error"></form:errors></td>
                    </tr>
                    <tr>
                        <td>
                            <label><spring:message code="Country" /></label>
                        </td>
                        <td>
                            <form:input path="address.country"></form:input>
                            <form:errors path="address.country" cssClass="error"></form:errors></td>
                    </tr>
                    <tr>
                        <td><small>*required fields</small></td>
                        <td><button class="btn-blue" type="submit"><spring:message code="Register" /></button></td>
                    </tr>
                </table>
        </form:form>
    </div>

</div>
</body>
</html>