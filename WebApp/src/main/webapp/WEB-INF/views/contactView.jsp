<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/contact.css"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <title>Contact page</title>
</head>
<body>
<div id="page">
    <jsp:include page="baseView.jsp"/>

    <div id="contact">
        <h2>Contact</h2>
    </div>

    <div id="content">
        <fieldset id="form-contact">
            <form>
                <label for="name"> Name: </label> <input type="text" name="name" id="name">
                <label for="email"> Email: </label> <input type="text" name="email" id="email">
                <label for="type"> Type: </label>
                <select name="type" id="type">
                    <option value="question">Question</option>
                    <option value="remark">Remark or Suggestion</option>
                    <option value="bug_error">Bug or Error</option>
                </select>
                <textarea name="message" id="message" rows="5"></textarea>
                <input type="submit" value="Send" class="btn-submit">
            </form>
        </fieldset>
    </div>
</div>
</body>
</html>