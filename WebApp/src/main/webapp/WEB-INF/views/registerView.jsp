<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/login.css"/>
</head>
<div id="page">
    <jsp:include page="headerView.jsp"/>

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
</html>