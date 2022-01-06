$(document).ready(function() {

    let bno = $('input[name="bno"]').val();

    // 댓글이 추가 될 영역
    let listGroup = $(".replyList");

    // 날짜 처리를 위한 함수
    function formatTime(str) {
        const date = new Date(str);

        return date.getFullYear() + '/' +
            (date.getMonth() + 1) + '/' +
            date.getDate() + ' ' +
            date.getHours() + ':' +
            date.getMinutes();
    }

    // 특정 게시글의 댓글 처리
    function loadJSONData() {
        $.getJSON('/replies/board/' + bno, function(arr) {
            let str = "";

            $('.replyCount').html(" Reply Count " + arr.length);

            $.each(arr, function(idx, reply) {
                str += '    <div class="card-body" data-rno="' + reply.rno + '"><b>' + reply.rno + '</b>';
                str += '        <h5 class="card-title">' + reply.text + '</h5>';
                str += '        <h6 class="card-subtitle mb-2 text-muted">' + reply.replyer + '</h6>';
                str += '        <p class="card-text">' + formatTime(reply.regDate) + '</p>';
                str += '    </div>';
            });

            listGroup.html(str);

        });
    }

    $(".replyCount").click(function() {
        loadJSONData();
    });

    const modal = $('.modal');

    // 추가 버튼 클릭 시
    $(".addReply").click(function () {
        modal.modal('show');

        // 초기화
        $('input[name="replyText"]').val('');
        $('input[name="replyer"]').val('');

        // 필요 버튼 노출
        $('.modal-footer .btn').hide();
        $('.replySave, .replyClose').show();
    });

    // 상세 댓글 클릭 시
    $('.replyList').on("click", ".card-body", function () {
        let rno = $(this).data("rno");

        $('input[name="replyText"]').val($(this).find(".card-title").html());
        $('input[name="replyer"]').val($(this).find(".card-subtitle").html());
        $('input[name="rno"]').val(rno);

        $(".modal-footer .btn").hide();
        $(".replyRemove, .replyModify, .replyClose").show();

        modal.modal('show');
    });

    // 저장 버튼 클릭 시
    $(".replySave").click(function () {

        let reply = {
            bno : bno,
            text : $('input[name="replyText"]').val(),
            replyer : $('input[name="replyer"]').val()
        }

        $.ajax({
            url : '/replies/insert',
            method : 'POST',
            data : JSON.stringify(reply),
            contentType : 'application/json; charset=utf-8',
            dataType : 'JSON',
            success : function (data) {
                let newRno = parseInt(data);

                alert(newRno + "번 댓글이 등록되었습니다.");

                modal.modal('hide');

                loadJSONData();
            }
        });

    });

    // 삭제 버튼 클릭 시
    $(".replyRemove").on("click", function () {
       let rno = $('input[name="rno"]').val();

       $.ajax({
           url : '/replies/delete/' + rno,
           method : 'DELETE',
           success : function (result) {
               if (result == 'success') {
                   alert("댓글이 삭제되었습니다.");

                   modal.modal('hide');

                   loadJSONData();
               }
           }
       });
    });

    // 수정 버튼 클릭 시
    $(".replyModify").on("click", function () {
        let rno = $('input[name="rno"]').val();

        let reply = {
            rno : rno,
            text : $('input[name="replyText"]').val(),
            replyer : $('input[name="replyer"]').val()
        }

        $.ajax({
            url : '/replies/update',
            method : 'PUT',
            data : JSON.stringify(reply),
            contentType : 'application/json; charset=utf-8',
            dataType : 'JSON',
            success : function (result) {
                if (result == 'success') {
                    alert("댓글이 수정되었습니다.");

                    modal.modal('hide');

                    loadJSONData();
                }
            }
        });
    });

});// ready end