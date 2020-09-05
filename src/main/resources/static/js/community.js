/**
 * 提交回复
 */

function post() {
    let questionId = $("#question_id").val();
    let content = $("#comment_content").val();
    comment2target(questionId, 1, content)
}

function comment2target(targetId, type, content) {
    if (!content) {
        alert("不能回复空内容~~~")
        return;
    }
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/comment",
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
        }),
        success: function (response) {
            if (response.code == 200) {
                location.reload();
            } else {
                if (response.code == 2003) {
                    var isAccept = confirm(response.message);
                    if (isAccept) {
                        window.open("https://github.com/login/oauth/authorize?client_id=56d812aee8b78ee91c69&redirect_uri=http://localhost:8887/callback&scope=user&state=1");
                        window.localStorage.setItem("closable", true);
                    }
                } else {
                    alert(response.message);
                }
            }
        },
        dataType: "json"
    });

}

function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-" + commentId).val();
    comment2target(commentId, 2, content)
}

/**
 * 展开二级评论
 */
function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comments = $("#comment-" + id);

    //获取二级评论的展开状态
    var collapse = e.getAttribute("data-collapse");
    if (collapse) {
        //折叠二级评论
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    } else {
        var subCommentContainer = $("#comment-" + id);
        if (subCommentContainer.children().length != 1) {
            comments.addClass("in");
            //标记二级评论展开状态
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");
        } else {
            $.getJSON("/comment/" + id, function (data) {
                console.log(data);
                $.each(data.data.reverse(), function (index, comment) {

                    var avatarElement = $("<img/>", {
                        "class": "edia-object img-circle",
                        "src": comment.user.avatarUrl,
                        "height": "40px",
                        "width": "40px",
                    });

                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left",
                    }).append(avatarElement);
                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body",
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        html: comment.user.name,
                    })).append($("<div/>", {
                        html: comment.content,
                    })).append($("<div/>", {
                        "class": "menu",
                    }).append($("<span/>", {
                        "class": "pull-right",
                        html: moment(comment.gmtCreate).format('YYYY-MM-DD'),
                    })));
                    var mediaElement = $("<div/>", {
                        "class": "media",
                    }).append(mediaLeftElement)
                        .append(mediaBodyElement);
                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments",
                    }).append(mediaElement);
                    subCommentContainer.prepend(commentElement);
                });
                comments.addClass("in");
                //标记二级评论展开状态
                e.setAttribute("data-collapse", "in");
                e.classList.add("active");
            });
        }
    }
}

function selectTag(value) {
    var previous = $("#tag").val();
    var strs=previous.split(",");
    if(strs.indexOf(value) == -1)
    {
        if(previous){
            $("#tag").val(previous+','+value);
        }else {
            $("#tag").val(value);
        }
    }
}

function showSelectRag(){
    $("#select-tag").show();
}
