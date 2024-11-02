// Fetch user info and update UI
$.get("/user", function (data) {
    $("#user").html('Logged in as: ' + data.name);
    $(".unauthenticated").hide();
    $(".authenticated").show();
});
