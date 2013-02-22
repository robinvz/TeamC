<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/profiel.css"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <title>Edit credentials page</title>
</head>
<body>
<div id="page">
    <jsp:include page="../headerView.jsp"/>

    <div id="content">
        <form action="/users/editCredentials" method="POST">
            <fieldset>
                <legend>Credentials</legend>
                <table>
                    <tr>
                        <td>
                            <label>Old password</label>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <input type="password" name="oldPassword">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>New password</label>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <input type="password" name="newPassword">
                        </td>
                    </tr>
                </table>
            </fieldset>
            <input type="submit" value="Save" class="btn-submit">
        </form>
    </div>

    <footer><p class="footer">Trips - 2013</p></footer>
</div>
<!--[if lt IE 9]>
<script src="${pageContext.request.contextPath}/resources/js/html5shiv.js"></script>
<![endif]-->
</body>
</html>