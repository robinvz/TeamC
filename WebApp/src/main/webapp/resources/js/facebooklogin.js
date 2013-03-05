window.fbAsyncInit = function () {
    FB.init({appId: '118452395005980', status: true, cookie: true, xfbml: true});
    FB.Event.subscribe('auth.login', function (response) {
        login();
    });
    FB.Event.subscribe('auth.logout', function (response) {
        logout();
    });
    FB.getLoginStatus(function (response) {
        if (response.session) {
            greet();
        }
    });

};
(function () {
    var e = document.createElement('script');
    e.type = 'text/javascript';
    e.src = document.location.protocol + '//connect.facebook.net/en_US/all.js';
    e.async = true;
    document.getElementById('fb-root').appendChild(e);
}());


function login() {
    FB.login(function (response) {
        if (response.authResponse) {
            testAPI();
        } else {
            // cancelled
        }
    });
};

function logout() {
    FB.logout(function(response) {
        console.log('User is now logged out');
    });
};
function testAPI() {
    console.log('Welcome!  Fetching your information.... ');
    FB.api('/me', function (response) {
        $.ajax({
            type: 'POST',
            url: 'facebooklogin',
            data: { username: response.email + 'facebook', password: response.id },
            success:function(data){
                window.location.href="/";
            },
            error:function(){
            }
        });
    });
}

f