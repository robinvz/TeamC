<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <title><spring:message code="ErrorPage"/></title>
</head>
<body>
<div id="page" style="background-image:url(../../../resources/res/img/404-Pig.png); background-repeat:no-repeat; position:relative">
    <jsp:include page="../baseView.jsp"/>

    <div id="content">
        <h3><spring:message code="404Error"/></h3>
    </div>

</div>
</body>
</html>