document.addEventListener("DOMContentLoaded", function () {
    if (window.location.search.includes('messageSent=true')) {
        $('#successModal').modal('show');
    }
});
