<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <title><spring:message code="ErrorPage"/></title>
</head>
<body>
<div id="page">
    <jsp:include page="../baseView.jsp"/>

    <div id="content">
        <h2><spring:message code="400Error"/></h2>
    </div>

</div>
</body>
</html>