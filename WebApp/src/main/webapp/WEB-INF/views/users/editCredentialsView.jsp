<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/profiel.css"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <title>Edit credentials page</title>
</head>
<body>
<div id="page">
    <jsp:include page="../baseView.jsp"/>
    <jsp:include page="profileHeaderView.jsp"/>
    <div class="content">

            <form action="/users/editCredentials" method="POST">
                <fieldset>
                    <legend>Credentials</legend>
                    <h2>Edit password for ${user.firstName} ${user.lastName}</h2>
                    <table>
                        <tr>
                            <td>
                                <label>Old password</label>
                            </td>

                            <td>
                                <input type="password" name="oldPassword">
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label>New password</label>
                            </td>

                            <td>
                                <input type="password" name="newPassword">
                            </td>
                        </tr>
                        <input id="btn-save" type="submit" value="Save" class="btn-submit">

                    </table>
                </fieldset>

            </form>

    </div>
</div>
</body>
</html>