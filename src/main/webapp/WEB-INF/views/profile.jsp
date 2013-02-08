<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/profiel.css" />
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <title>Profile page</title>
</head>
<body>
<div id="page">
    <jsp:include page="header.jsp" />

    <div id="content">
        <aside>
            <div id="profielfoto"> </div>
            <nav class="trip-nav">
                <h3>Profiel</h3>
                <ul class="trip-nav">
                    <li><a href="#">Algemeen</a></li>
                    <li><a href="#">Contactgegevens</a></li>
                    <li><a href="#">Huidige trips</a></li>
                    <li><a href="#">Afgelopen trips</a></li>
                    <li><a href="#">Bewerken</a></li>
                </ul>
            </nav>
        </aside>

        <div class="inner-content">
            <section>

                <article>
                    <h2>JAN JANSSENS</h2>
                    <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris dictum libero quis libero facilisis eu pharetra sem volutpat. Sed faucibus lectus vel diam dignissim vitae blandit nisl hendrerit. Praesent fringilla dui id dui mollis ultricies. Duis libero ligula, hendrerit cursus ultricies sed, pulvinar eu velit. Donec ut commodo nibh. Etiam placerat sem vel eros lacinia pretium. Vivamus malesuada, sapien quis rutrum eleifend, mi lacus cursus augue, non molestie felis nibh ac lacus. Nunc dapibus, augue vel consequat lacinia, urna arcu convallis turpis, quis malesuada magna mi a dolor. Phasellus vel nunc vel dui sodales tristique in tempor diam.

                        Morbi lacinia libero eu velit pharetra pharetra id vel mi. Maecenas varius neque dolor, sed elementum dui. Nulla laoreet mauris non lectus feugiat a facilisis dolor commodo. Praesent faucibus, eros vel sagittis varius, elit leo egestas lacus, quis pulvinar est est at enim. Nulla a diam quis est pretium feugiat. Mauris et massa leo. Donec ac dolor at est porttitor tempor id vitae sem. Sed rutrum cursus sagittis. Nam posuere lorem ac augue placerat id ultricies turpis mollis. Etiam eu velit dolor. Aenean velit massa, pharetra nec sodales ut, interdum vitae leo. Aliquam erat volutpat. Aliquam fermentum, nisl eget laoreet placerat, mauris ligula lobortis sem, at pulvinar ante mi in nisi. Integer dolor ipsum, lacinia a euismod ut, pellentesque quis nulla.

                        Duis ac nunc vitae diam commodo adipiscing. Maecenas vel metus purus. Donec faucibus, orci id fermentum laoreet, elit mauris dictum metus, et volutpat odio ipsum nec nisi. Sed eu sem leo, ut vestibulum mi. Aliquam vel lectus ipsum, quis accumsan ante. Praesent magna massa, aliquet at convallis tempor, ultrices sed purus. Etiam commodo, massa eu venenatis varius, ipsum augue pharetra nulla, id fermentum mauris nisi quis leo. Nunc pellentesque, eros quis semper porta, mi erat suscipit magna, sodales fermentum diam sem quis turpis. Ut ornare semper suscipit. Nam mollis congue faucibus. Cras varius ultrices volutpat. Curabitur ultricies ultrices aliquam. Aenean dignissim tellus sed augue malesuada eget malesuada libero vehicula. Duis eu justo ut mi pulvinar porta. Proin elementum est vel orci semper quis tempus orci posuere. Curabitur commodo, dolor in gravida feugiat, sapien nibh porttitor dolor, et pretium lorem mi ac nulla.

                        Pellentesque vitae justo odio. Morbi mi urna, rhoncus non cursus vel, blandit quis dolor. Aenean luctus posuere sollicitudin. Etiam ante augue, dapibus at ultricies vel, imperdiet non massa. Mauris vestibulum dignissim est ut laoreet. Nam ornare commodo velit in accumsan. Praesent aliquam turpis vel turpis dignissim ullamcorper molestie magna faucibus. Proin sit amet arcu mi, sed dictum est. Nam eleifend nisi diam, non dictum velit. Duis aliquet accumsan blandit.

                        Maecenas ipsum diam, pharetra quis egestas at, porta ut sem. Suspendisse a nisl lorem, ac congue risus. Suspendisse at cursus mi. Nullam pretium diam at nisl tempor sit amet pulvinar odio scelerisque. Pellentesque lacinia, neque vitae hendrerit volutpat, nunc erat aliquam libero, eget hendrerit nisi magna vel magna. Etiam malesuada diam at dui ultricies dignissim ut quis lorem. Vivamus felis elit, posuere mattis consequat in, rhoncus non massa. Praesent felis tellus, luctus sit amet aliquam sed, varius in nunc. Nunc sed nisi ante, vel mollis risus. Sed accumsan dui non ipsum mollis eu feugiat purus luctus. Nullam accumsan, libero eget vulputate ullamcorper, lorem tellus posuere tellus, sed luctus turpis libero auctor lacus. Maecenas ac rutrum leo. Mauris pretium, libero at varius iaculis, dui arcu varius nulla, ut varius massa nisi id lectus. Aliquam erat volutpat. Maecenas viverra placerat egestas. Integer libero velit, posuere id fermentum nec, dignissim in urna.</p>

                </article>
            </section>
        </div>
    </div>

    <footer><p class="footer">Trips - 2013</p></footer>
</div>
</body>
<script src="${pageContext.request.contextPath}/resources/js/jquery-1.9.0.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/knockout.js"></script>
<!--[if lt IE 9]>
<script src="${pageContext.request.contextPath}/resources/js/html5shiv.js"></script>
<![endif]-->
</html>