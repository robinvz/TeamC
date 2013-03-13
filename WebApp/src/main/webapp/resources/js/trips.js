$(document).ready(function(){
    var currentPage = 0;
    $('#trips').fadeOut(0);
    $('#trips').slideDown(300);

    $('.tables').dataTable({ "bPaginate": false, "bInfo": false, "bAutoWidth": false });


    $('#btn-trips').on('click', function(){
        setSelected($(this));
        if (currentPage == 0){
        }   else if (currentPage == 1){
            $('#inner-content').animate({marginLeft: '+=960'}, 700, function(){
            }) ;
        }   else{
            $('#inner-content').animate({marginLeft: '+=1920'}, 700, function(){
            }) ;
        }
         currentPage = 0;
    });
    $('#btn-trips-participating').on('click', function(){
        setSelected($(this));
        if (currentPage == 0){
            $('#inner-content').animate({marginLeft: '-=960'}, 700, function(){
            }) ;
        }   else if (currentPage == 1){
        }   else{
            $('#inner-content').animate({marginLeft: '+=960'}, 700, function(){
            }) ;
        }
        currentPage = 1;
    });
    $('#btn-trips-organised').on('click', function(){
        setSelected($(this));
        if (currentPage == 0){
            $('#inner-content').animate({marginLeft: '-=1920'}, 700, function(){
            }) ;
        }   else if (currentPage == 1){
            $('#inner-content').animate({marginLeft: '-=960'}, 700, function(){
            }) ;
        }   else{
        }
        currentPage = 2;
    });



    $("table tr").not(':first').on('click', function(){
      var  ref = '/trip/'+$(this).attr('id').substr(4) + '#loadtheme';
      window.location = ref;
    });


    $('#filter').change(filterResults);



});

function setSelected(link){
    $('#btn-trips').removeClass('inner-selected');
    $('#btn-trips-participating').removeClass('inner-selected');
    $('#btn-trips-organised').removeClass('inner-selected');
    link.addClass('inner-selected');
    filterResults();
}

function filterResults(){
    var options = $("#filter option");
    var index = options.index(options.filter(":selected"));
    $('.t-bound').show();
    $('.t-less').show();
    switch (index){
        case 1:
            $('.t-bound').hide();
            break;
        case 2:
            $('.t-less').hide();
            break;
    }
}
