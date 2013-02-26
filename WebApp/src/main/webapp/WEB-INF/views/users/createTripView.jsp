<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <title>Create trip page</title>
</head>
<body>
<div id="page">
    <jsp:include page="../baseView.jsp"/>

    <div id="content">
        <div id="timeboundTrip">
            <form action="/createTimeBoundTrip" method="POST">
                <fieldset>
                    <legend>Create a timebound trip</legend>
                    <table>
                        <tr>
                            <td><label>Title: </label></td>
                            <td><input type="text" name="title" required="true"></td>
                        </tr>
                        <tr>
                            <td><label>Description: </label></td>
                            <td><input type="text" name="description" required="true"></td>
                        </tr>
                        <tr>
                            <td><label>Privacy: </label></td>
                            <td><input type="radio" name="privacy" value="PUBLIC">Public</input>
                                <input type="radio" name="privacy" value="PROTECTED">Protected</input>
                                <input type="radio" name="privacy" value="PRIVATE">Private
                            </td>
                        </tr>
                        <tr>
                            <td><label>Start date: </label></td>
                            <td><input type="date" name="startDate">
                        </tr>
                        <tr>
                            <td><label>End date: </label></td>
                            <td><input type="date" name="endDate"></td>
                            </td>
                        </tr>
                    </table>
                </fieldset>
                <input type="submit" value="Create" class="btn-submit">
            </form>
            </fieldset>
        </div>

        <div id="timelessTrip">
            <form action="/createTimeLessTrip" method="POST">
                <fieldset>
                    <legend>Create a timeless trip</legend>
                    <table>
                        <tr>
                            <td><label>Title: </label></td>
                            <td><input type="text" name="title" required="true"></td>
                        </tr>
                        <tr>
                            <td><label>Description: </label></td>
                            <td><input type="text" name="description" required="true"></td>
                        </tr>
                        <tr>
                            <td><label>Privacy: </label></td>
                            <td><input type="radio" name="privacy" value="PUBLIC">Public</input>
                                <input type="radio" name="privacy" value="PROTECTED">Protected</input>
                                <input type="radio" name="privacy" value="PRIVATE">Private
                            </td>
                        </tr>
                    </table>
                </fieldset>
                <input type="submit" value="Create" class="btn-submit">
            </form>
            </fieldset>
        </div>
    </div>
</div>
</body>
</html>