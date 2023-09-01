$(document).ready(function(){
    $('.custom-dropdown').change(function(){
        var selectedValue = $(this).val();
        if(selectedValue == "0") {
            window.location.href = "/posts";
        } else {
            window.location.href = "/posts/category/" + selectedValue;
        }
    });
});

// $(document).ready(function(){
//     var currentCategoryId = $('body').data('category-id');
//     $('.custom-dropdown').val(currentCategoryId);
//
//     $('.custom-dropdown').change(function(){
//         var selectedValue = $(this).val();
//         if(selectedValue == "0") {
//             window.location.href = "/posts";
//         } else {
//             window.location.href = "/posts/category/" + selectedValue;
//         }
//     });
// });

