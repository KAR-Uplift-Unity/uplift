$(document).ready(function(){
    $('.custom-dropdown').change(function(){
        const selectedValue = $(this).val();
        if(selectedValue === "default") {
            return;
        } else if(selectedValue === "0") {
            window.location.href = "/posts";
        } else {
            window.location.href = "/posts/category/" + selectedValue;
        }
    });
});


