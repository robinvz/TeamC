<%--
  Created by IntelliJ IDEA.
  User: Mathias
  Date: 15-2-13
  Time: 17:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
<div id="page">
    <jsp:include page="header.jsp"/>

    <div id="content">
        <form method="POST" action="register">
            <fieldset>
                <legend>Required</legend>
                <table>
                    <tr>
                        <td>
                            <label>Email Address</label>
                        </td>
                        <td>
                            <input type="text" name="email">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>Password</label>
                        </td>
                        <td>
                            <input type="password" name="password">
                        </td>
                    </tr>
                </table>
            </fieldset>
            <fieldset>
                <legend>Optional</legend>
                <header>Personal Information</header>
                <table>
                    <tr>
                        <td>
                            <label>First Name</label>
                        </td>
                        <td>
                            <input type="text" name="firstName">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>Last Name</label>
                        </td>
                        <td>
                            <input type="text" name="lastName">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>Street</label>
                        </td>
                        <td>
                            <input type="text" name="street">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>House Nr</label>
                        </td>
                        <td>
                            <input type="text" name="houseNr">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>City</label>
                        </td>
                        <td>
                            <input type="text" name="city">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>Postal code</label>
                        </td>
                        <td>
                            <input type="text" name="postalCode">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>Province</label>
                        </td>
                        <td>
                            <input type="text" name="province">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>Country</label>
                        </td>
                        <td>
                            <input type="text" name="country">
                        </td>
                    </tr>
                </table>
            </fieldset>
            <button type="submit">Register</button>
        </form>
    </div>

    <footer><p class="footer">Trips - 2013</p></footer>
</div>
</body>
<script src="${pageContext.request.contextPath}/resources/js/jquery-1.9.0.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/knockout.js"></script>
<!--[if lt IE 9]>
<script src="${pageContext.request.contextPath}/resources/js/html5shiv.js"></script>
<![endif]-->
</html>