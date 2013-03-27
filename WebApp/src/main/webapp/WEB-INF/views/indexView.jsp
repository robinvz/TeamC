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

        <div id="myCarousel" class="carousel slide">
          <ol class="carousel-indicators">
            <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
            <li data-target="#myCarousel" data-slide-to="1"></li>
            <li data-target="#myCarousel" data-slide-to="2"></li>
          </ol>
          <!-- Carousel items -->
          <div class="carousel-inner">
                    <div class="active item">
                        <img alt="Logo" src="${pageContext.request.contextPath}/resources/res/img/welcome.jpg" width="100%" height="400">
                        <div class="carousel-caption">
                            <h4><spring:message code="WelcomeTitle" /></h4>
                            <p><spring:message code="WelcomeMessage" /></p>
                        </div>
                    </div>
                <c:forEach items="${trips}" var="trip">
                    <div class="item">
                        <c:choose>
                            <c:when test="${not empty trip.image}">
                                <a href="/trip/${trip.id}"><img alt="Logo" src="/trip/${trip.id}/banner" width="100%" height="400"></a>
                            </c:when>
                            <c:otherwise>
                                <a href="/trip/${trip.id}"><img alt="Logo" src="${pageContext.request.contextPath}/resources/res/img/img_antwerpen.png" width="100%" height="400"></a>
                            </c:otherwise>
                        </c:choose>
                        <div class="carousel-caption">
                           <h4>${trip.title}</h4>
                           <p>${trip.description}</p>
                        </div>
                    </div>
                </c:forEach>
          </div>
          <!-- Carousel nav -->
          <a class="carousel-control left" href="#myCarousel" data-slide="prev">&lsaquo;</a>
          <a class="carousel-control right" href="#myCarousel" data-slide="next">&rsaquo;</a>
        </div>

        <div id="fb-root"></div>
        <script>(function(d, s, id) {
            var js, fjs = d.getElementsByTagName(s)[0];
            if (d.getElementById(id)) return;
            js = d.createElement(s); js.id = id;
            js.src = "//connect.facebook.net/en_GB/all.js#xfbml=1&appId=118452395005980";
            fjs.parentNode.insertBefore(js, fjs);
        }(document, 'script', 'facebook-jssdk'));
        </script>
        <div class="fb-like-box" data-href="https://www.facebook.com/pages/Trips-Team-C/277962849000758"
             data-width="292" data-show-faces="true" data-stream="true" data-header="true">
        </div>
    </div>

</div>
<script src="${pageContext.request.contextPath}/resources/js/jquery-1.9.0.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/bootstrap-carousel.js"></script>
<script>
    $(function(){
        $('.carousel').carousel({
            interval: 4000
        })
    });
</script>
</body>
</html>