$(document).ready(function() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");


    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    const heartBtn = $('#heart-btn');
    const heart = $('.fa-heart');
    const likeCountEl = $('#like-count');

    const postId = $('#postId').val();

    heartBtn.click(function() {
        $.post(`/posts/${postId}/toggle-like`, function(updatedLikeCount) {
            likeCountEl.text(updatedLikeCount);
            heart.toggleClass('fa-regular');
            heart.toggleClass('fa-solid');
        }).fail(function(jqXHR, textStatus, errorThrown) {
            window.location.href = "/login";
        });
    });
});



