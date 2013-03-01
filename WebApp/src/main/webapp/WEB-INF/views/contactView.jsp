<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/contact.css"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <title><spring:message code="ContactPage"/></title>
</head>
<body>
<div id="page">
    <jsp:include page="baseView.jsp"/>

    <div id="content">
        <div id="contact">
            <h2>Contact</h2>
        </div>

        <form id="form-contact">
            <label for="name"><spring:message code="Name"/></label>
            <input type="text" name="name" id="name">

            <label for="email">Email: </label>
            <input type="text" name="email" id="email">

            <label for="type">Type: </label>
            <select name="type" id="type">
                <option value="question"><spring:message code="Question"/></option>
                <option value="remark"><spring:message code="Remark"/></option>
                <option value="bug_error"><spring:message code="Bug"/></option>
            </select>
            <textarea name="message" id="message" rows="5"></textarea>
            <input type="submit" value="<spring:message code="Send" />" class="btn-submit btn-blue">
        </form>
    </div>

</div>
</body>
</html>