function setImage(tripId){
    var context = $('#testen').getContext('2d');
    var imageObj = new Image();
    $.get("/tripPic/"+tripId, function (image) {
      // $('#trip-header').css("background-image", image);
        imageObj.src = image;
    });
    context.drawImage(imageObj, 69, 50);
}




