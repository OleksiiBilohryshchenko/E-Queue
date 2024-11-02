
var countdownInterval;

// Fetch user info and update UI
$.get("/user", function (data) {
    $("#user").html('Logged in as: ' + data.name);
    $(".unauthenticated").hide();
    $(".authenticated").show();
});

function logout() {
    $.post("/logout", function () {
        $("#user").html('');
        $(".authenticated").hide();
        $(".unauthenticated").show();

        // Clear booking slots
        $('#booking-slot').empty();
        if (countdownInterval) {
            clearInterval(countdownInterval);
        }
    });
    return false;
}
