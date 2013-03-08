var tripId;
var prob;
function getTripId(id) {
    tripId = id;
    initDataTable();
    inviteUser();
    uninviteUser();
}

function initDataTable() {
    prob = $('.dataTable').dataTable({
        /*bProcessing: true,
         bServerSide: true,
         sAjaxSource: "/inviteUser/" + tripId + "/sendInvite"*/
    });
}

function inviteUser() {
    $('.invite-input').click(function () {
        var data = [
            {'name': 'userByKeywordEmail', 'value': $(this).closest('tr').find('input').val()}
        ];
        $.post('/inviteUser/' + tripId + '/sendInvite', data);
    });
}

function uninviteUser() {
    $('.uninvite-input').click(function () {
        var data = [
            {'name': 'uninviteEmail', 'value': $(this).closest('tr').find('input').val()}
        ];
        $.post('/inviteUser/' + tripId + '/uninvite/', data);
    });
}