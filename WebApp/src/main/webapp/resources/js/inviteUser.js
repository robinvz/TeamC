var tripId;

function getTripId(id) {
    $('#noneFound').hide();
    tripId = id;
    initDataTable();
    btnListeners();
}

function initDataTable() {
    $('.dataTable').dataTable({ "bPaginate": false, "bInfo": false, "bAutoWidth": false });
}

function btnListeners() {
    $('#btn-SearchUsers').click(function () {
        var count = $('#foundUsers-table > tbody > tr').length;
        if(count != 0){
            $('#foundUsers-table').show();
        }else{
            $('#noneFound').show();
        }
    });

    $('.invite-input').click(function () {
        var invitedUser = [
            {'name': 'userByKeywordEmail', 'value': $(this).closest('tr').find('input').val()}
        ];
        $.post('/inviteUser/' + tripId + '/sendInvite', invitedUser, function () {
            location.reload();
        });
    });

    $('.uninvite-input').click(function () {
        var uninvitedUser = [
            {'name': 'uninviteEmail', 'value': $(this).closest('tr').find('input').val()}
        ];
        $.post('/inviteUser/' + tripId + '/uninvite/', uninvitedUser, function () {
            location.reload();
        });
    });
}