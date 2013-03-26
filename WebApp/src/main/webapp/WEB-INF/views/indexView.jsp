<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css">
    <link rel="stylesheet" media="(max-width:1040px)" href="${pageContext.request.contextPath}/resources/css/main-responsive.css">
    <link rel="stylesheet" media="(min-width:1039px)" href="${pageContext.request.contextPath}/resources/css/responsive-fix.css">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <title>Home page</title>
</head>
<body>
<div id="page">
    <jsp:include page="baseView.jsp"/>

    <div id="content">
        <c:choose>
            <c:when test="${not empty user}">
                <h2><spring:message code="Welcome" /> ${user.firstName}</h2>
            </c:when>
            <c:otherwise>
                <h2><spring:message code="Welcome" /></h2>
            </c:otherwise>
        </c:choose>

        <p>
            The trips system was developed by six young students. It is an easy tool to enjoy outings to the fullest.
            You can manage your costs, chat with friends, participate in a quiz and so on. Visit the trips page
            to view all current public trips.   </p>
        <p>
            If you are interested in organizing trips yourself, please register first. For further questions or
            remarks please consult our contact page.
        </p>

        <div id="fb-root"></div>
        <script>(function(d, s, id) {
            var js, fjs = d.getElementsByTagName(s)[0];
            if (d.getElementById(id)) return;
            js = d.createElement(s); js.id = id;
            js.src = "//connect.facebook.net/en_GB/all.js#xfbml=1&appId=118452395005980";
            fjs.parentNode.insertBefore(js, fjs);
        }(document, 'script', 'facebook-jssdk'));</script>
        <div class="fb-like-box" data-href="https://www.facebook.com/pages/Trips-Team-C/277962849000758" data-width="292" data-show-faces="true" data-stream="true" data-header="true"></div>



</span>
    </div>

</div>
</body>
</html>