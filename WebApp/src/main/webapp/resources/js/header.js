$(document).ready(function(){
    //Get the last part of the url for the page name
    var urlParts = $(location).attr('href').split("/");
    var currentPage = urlParts[urlParts.length - 1];
    //Remove previous selections and select right page
    $(".selected").removeClass("selected");
    switch (currentPage){
       case "":
           $("#nav-home").addClass("selected");
           break;
       case "trips":
           $("#nav-trips").addClass("selected");
           break;
       case "profile":
           $("#nav-profile").addClass("selected");
           break;
       case "login":
           $("#nav-login").addClass("selected");
           break;
       case "register":
           $("#nav-register").addClass("selected");
           break;
       case "contact":
           $("#nav-contact").addClass("selected");
           break;
       //Other Pages which are not directly displayed in main navigation
       case "createTrip?":
           $("#nav-trips").addClass("selected");
           break;
       case "editCredentials":
           $("#nav-profile").addClass("selected");
           break;
        case "editProfilePic":
            $("#nav-profile").addClass("selected");
            break;
        case "trip":
           $("#nav-trips").addClass("selected");
           break;
        case "retrievePassword":
            $("#nav-login").addClass("selected");
            break;
        case "viewTripsHistory":
            $("#nav-profile").addClass("selected");
            break;
        default :
            $("#nav-trips").addClass("selected");
            break;
    }
    if (currentPage.matches(new RegExp("createTrip"))){
        $("#nav-trips").addClass("selected");
    };
});