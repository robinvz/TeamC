<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/login.css"/>
</head>
<div id="page">
    <jsp:include page="baseView.jsp"/>

    <div id="content">
        <h2>Please fill in the forms</h2>
        <form:form method="POST" commandName="user" action="register" id="registerform">
            <form:errors path="*" cssClass="errorblock" element="div" />

                <table>
                    <tr>
                        <td>
                            <label>Email Address*</label>
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
                            <label>First Name</label>
                        </td>
                        <td>
                            <form:input path="firstName"></form:input>
                            <form:errors path="firstName" cssClass="error"></form:errors>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>Last Name</label>
                        </td>
                        <td>
                            <form:input path="lastName"></form:input>
                            <form:errors path="lastName" cssClass="error"></form:errors>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>Street</label>
                        </td>
                        <td>
                            <form:input path="address.street"></form:input>
                            <form:errors path="address.street" cssClass="error"></form:errors></td>
                    </tr>
                    <tr>
                        <td>
                            <label>House Nr</label>
                        </td>
                        <td>
                            <form:input path="address.houseNr"></form:input>
                            <form:errors path="address.houseNr" cssClass="error"></form:errors></td>
                    </tr>
                    <tr>
                        <td>
                            <label>City</label>
                        </td>
                        <td>
                            <form:input path="address.city"></form:input>
                            <form:errors path="address.city" cssClass="error"></form:errors></td>
                    </tr>
                    <tr>
                        <td>
                            <label>Postal code</label>
                        </td>
                        <td>
                            <form:input path="address.postalCode"></form:input>
                            <form:errors path="address.postalCode" cssClass="error"></form:errors></td>
                    </tr>
                    <tr>
                        <td>
                            <label>Province</label>
                        </td>
                        <td>
                            <form:input path="address.province"></form:input>
                            <form:errors path="address.province" cssClass="error"></form:errors></td>
                    </tr>
                    <tr>
                        <td>
                            <label>Country</label>
                        </td>
                        <td>
                            <form:input path="address.country"></form:input>
                            <form:errors path="address.country" cssClass="error"></form:errors></td>
                    </tr>
                    <tr>
                        <td><small>*required fields</small></td>
                        <td> <button class="btn-blue" type="submit">Register</button></td>
                    </tr>
                </table>


        </form:form>
    </div>
</div>
</body>
</html>