<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/trip.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/locations.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/location.css"/>
    <link rel="stylesheet" href="<spring:theme code="css"/>" type="text/css"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/res/favicon.ico">
    <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.1/themes/base/jquery-ui.css"/>
    <title>Location page</title>
</head>
<body>
<div id="page">
    <jsp:include page="../baseView.jsp"/>
    <jsp:include page="../tripHeaderView.jsp"/>
    <div id="content">
        <div id="showLocation">
            <h3 id="titleH">${location.title}</h3>
            <button type="button" id="btn-toggleEditLocation"><spring:message code="Edit"/>
            </button>
            <h3><spring:message code="Description"/></h3>

            <p>${location.description}</p>
        </div>
        <div id="editLocation">
            <form action="/trip/${trip.id}/locations/${location.id}/editLocation" method="POST">
                <input id="title" name="title" type="text" value="${location.title}"/>

                <h3><spring:message code="Description"/></h3>
                <input id="description" name="description" type="text" value="${location.description}"/>
                <button id="btn-submitLocation" type="submit"><spring:message code="Submit"/></button>
                <button class="btn-cancel" type="button"><spring:message code="Cancel"/></button>
            </form>
        </div>
        <div id="locationCanvas" class="map-canvas"></div>
        <div id="address">
            <h3><spring:message code="Location"/></h3>

            <p><spring:message code="LocationIsSituated"/> ${location.getAddress().country},
                <spring:message code="In"/> ${location.getAddress().postalCode} ${location.getAddress().city}.
                <spring:message code="AtAddress"/> ${location.getAddress().street}, <spring:message
                        code="Number"/> ${location.getAddress().houseNr}.
            </p>
        </div>

        <div id="questionContainer">
            <h3><spring:message code="Question"/></h3>
            <c:choose>
                <c:when test="${location.question != null}">
                    <div id="showQuestion">
                        <c:choose>
                            <c:when test="${location.question.image == null}">
                                <div class="bgImage">
                                    <form action="/trip/${trip.id}/locations/${location.id}/editLocationPic"
                                          method="POST" enctype="multipart/form-data">
                                        <input class="onImage" type="file" name="file"
                                               value="<spring:message code="LocationPicture"/>"/>
                                        <input type="submit" value="<spring:message code="UploadButton" />">
                                    </form>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="bgImage"
                                     style="background-image: url('/trip/${trip.id}/locations/${location.id}/questionPic');">
                                </div>
                                <a href="/trip/${trip.id}/locations/${location.id}/deleteQuestionImage">
                                    <button class="onImage" type="button" id="btn-deletePicture">Delete image
                                    </button>
                                </a>

                                <form action="/trip/${trip.id}/locations/${location.id}/editLocationPic"
                                      method="POST" enctype="multipart/form-data">
                                    <input class="onImage" type="file" name="file"
                                           value="<spring:message code="LocationPicture"/>"/>
                                    <input type="submit" value="<spring:message code="UploadButton" />">
                                </form>
                            </c:otherwise>
                        </c:choose>
                        <label type="text">${location.getQuestion().question}</label>
                        <ul>
                            <c:forEach items="${location.question.possibleAnswers}" var="answer" varStatus="status">
                                <c:set var="correctAnswerBold" value="normal"/>
                                <c:if test="${status.index == location.question.correctAnswerIndex}">
                                    <c:set var="correctAnswerBold" value="bold"/>
                                </c:if>
                                <li><label class="${correctAnswerBold}" type="text">${status.index +1}.${answer}</label>
                                </li>
                            </c:forEach>
                        </ul>
                        <button type="button" id="btn-toggleEditQuestion"><spring:message code="Edit"/>
                        </button>
                        <a href="/trip/${trip.id}/locations/${location.id}/deleteQuestion">
                            <button type="button" id="btn-deleteQuestion"><spring:message code="Delete"/>
                            </button>
                        </a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div id="addQuestion">
                        <form:form action="/trip/${trip.id}/locations/${location.id}/addQuestion" method="POST"
                                   enctype="multipart/form-data">
                            <label><spring:message code="AddQuestion"/></label>
                            <input id="location-question" name="question" type="text">
                            <label><spring:message code="AddImage"/></label>
                            <input class="btn-addPicture" type="file" name="file"
                                   value="<spring:message code="LocationPicture"/>"/>

                            <div id="answersAdd">
                            </div>
                            <label><spring:message code="AddAnswer"/></label>
                            <input class="new-answer" type="text">
                            <button class="btn-answer" type="button"><spring:message code="Save"/></button>
                            <label><spring:message code="SelectCorrectAnswer"/></label>
                            <select class="correct-answer" name="correctAnswer">
                                <option></option>
                            </select>
                            <button id="btn-submitAdd" type="submit"><spring:message code="Submit"/></button>
                        </form:form>
                    </div>
                </c:otherwise>
            </c:choose>

            <div id="editQuestion">
                <form:form action="/trip/${trip.id}/locations/${location.id}/editQuestion" method="POST"
                           enctype="multipart/form-data">
                    <c:choose>
                        <c:when test="${location.question.image == null}">
                            <div class="bgImage">
                                <label>No image added.</label>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="bgImage"
                                 style="background-image: url('/trip/${trip.id}/locations/${location.id}/questionPic');">
                            </div>
                        </c:otherwise>
                    </c:choose>
                    <input id="question" name="question" type="text" value="${location.getQuestion().question}"/>

                    <div id="answersEdit">
                        <c:forEach items="${location.getQuestion().possibleAnswers}" var="answer" varStatus="status">
                            <input class="answer" name="possibleAnswers" type="text" value="${answer}"/>
                        </c:forEach>
                    </div>
                    <input class="new-answer" type="text" value="--<spring:message code="AddAnswer"/>--">
                    <button class="btn-answer" type="button"><spring:message code="Save"/></button>
                    <select class="correct-answer" name="correctAnswer">
                        <option>--<spring:message code="SelectCorrectAnswer"/>--</option>
                    </select>
                    <button id="btn-submitEdit" type="submit"><spring:message code="Submit"/></button>
                    <button class="btn-cancel" type="button"><spring:message code="Cancel"/></button>
                </form:form>
            </div>
        </div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/resources/js/jquery-1.9.0.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/jquery.dataTables.js"></script>
<script src="http://maps.googleapis.com/maps/api/js?sensor=false"></script>
<script src="${pageContext.request.contextPath}/resources/js/location.js"></script>
<script>
    $(document).ready(function () {
        $('#editQuestion').hide();
        $('#editLocation').hide();
        getLatLng(${location.latitude}, ${location.longitude});
        getIds(${trip.id}, ${location.id});
    });
</script>
</body>
</html>