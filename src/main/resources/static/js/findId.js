/**
 * 아이디 찾기
 */

$(document).ready(function(){
    $("#find_id").click(function() {

        var formData = {
          name: $("#name").val(),
          mobile: $("#mobile").val()
        };

        // 유효성 검사
        if (formData.name.trim() === "") {
            alert("이름을 입력하세요");
            return;
        }

        if (formData.mobile.trim() === "") {
            alert("전화 번호를 입력하세요");
            return;
        }

        $.ajax({
            type: "POST",
            url: "/user/findId.do",
            data: formData,
            success: function(resData) {
                if (resData.email === "") {
                    alert("조회 결과가 없습니다.");
                } else {
                    // 성공 시 화면에 결과를 출력
                    $("#result_id").html("<span>아이디는 " + resData.email + "입니다.</span>");
                }
            },
            error: function(xhr) {
                console.error("Error! Response Data:", xhr.responseText); // 콘솔에 출력
                alert("에러 발생: " + xhr.responseText);
            }
        });
    });
});