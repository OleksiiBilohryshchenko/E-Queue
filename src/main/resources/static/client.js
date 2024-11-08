
var countdownInterval;

// Fetch user info and update UI
$.get("/user", function (data) {
    $("#user").html('Logged in as: ' + data.name);
    $(".unauthenticated").hide();
    $(".authenticated").show();

    loadBookingSlots();

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

// Load booking slots into the dropdown
function loadBookingSlots() {
    $.get("/api/booking/slots", function (data) {
        var $slotSelect = $('#booking-slot');
        $slotSelect.empty();

        console.log('Loading booking slots: ' + data.length);
        if (data.length > 0) {
            data.forEach(function (slot) {
                $slotSelect.append('<option value="' + slot.id + '">' + slot.startTime + ' - ' + slot.endTime + '</option>');
            });
            $('#no-slots-message').hide();
            $('#booking-form').show();
        } else {
            // No slots available
            $('#booking-form').hide();
            $('#no-slots-message').show().text('No slots available. Please try next day.');
        }
    });
}

function submitBooking() {
    var selectedSlotId = $('#booking-slot').val();
    $.post("/api/booking/book", {slotId: selectedSlotId}, function (response) {
        // Disable the booking form since the user has booked
        $('#booking-form').hide();
        $('#booking-message').show().text('You have already booked a slot in this window.');
    }).fail(function (xhr) {
        if (xhr.status === 403) {
            // Hide the booking form
            $('#booking-form').hide();
            $('#booking-message').show().text('You have already booked a slot in this window.');
        } else {
            alert('Booking failed. Please try again.');
        }
    });
}