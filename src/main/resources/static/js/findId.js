/**
 * 페이지 로드 시 초기 상태 설정
 * 아이디(메일) 찾기
 * 비밀번호 찾기
 */

/* 전역변수 선언 */
var emailPassed = false;

// findId.js 파일 내용


// 아이디 찾기
$(document).ready(() => {
    $("#find_id").click(() => {

        let formData = {
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
            success: (resData) => {
                if (resData.email === "") {
                    alert("조회 결과가 없습니다.");
                } else {
                    // 성공 시 화면에 결과를 출력
                    $("#result_id").html("<span>아이디는 " + resData.email + "입니다.</span>");
                
                }
            },
            error: (xhr) => {
                console.error("Error! Response Data:", xhr.responseText); // 콘솔에 출력
                alert("에러 발생: " + xhr.responseText);
            }
        });
    });
});


$(document).ready(() => {
  // 비밀번호 찾기
  $('#email').keyup(() => {
    let email = $('#email').val();

    new Promise((resolve, reject) => {
      // 1. 정규식 검사
      let regEmail = /^[A-Za-z0-9-_]+@[A-Za-z0-9]{2,}([.][A-Za-z]{2,6}){1,2}$/;
      if (!regEmail.test(email)) {
        reject(1);
        return;
      }

      // 2. 이메일 중복 체크
      $.ajax({
        type: 'get',
        url: '/user/checkPwEmail.do',
        data: 'email=' + email,
        dataType: 'json',
        success: (resData) => {
          if (!resData.enableEmail) {          // 회원인 경우
            $('#msg_emailTmpPW').text('');
            resolve();
          } else {                            // 회원이 아닌경우 
            reject(2);
          }
        }
      });
    }).then(() => {
      // 3. 임시 비번 인증 메일 발송
     
          $.ajax({
            url: '/user/sendTmpPw.do',
            method: 'GET',
            data: 'email=' + email,
            success: (resData) => {
              // Ajax 요청이 성공할 경우 수행할 동작
              console.log('Ajax 요청 성공', resData);

              alert(email + "로 임시 비밀번호를 전송했습니다.");
              $('#pwCode').prop('disabled', false);
              $('#btn_verify_pwCode').prop('disabled', false);
              $('#btn_verify_pwCode').click(() => {
                emailPassed = $('#pwCode').val() === resData.email;
                if (emailPassed) {
                  // 여기 수정 ///////////////////////////////////
                  alert('임시비밀번호 설정이 완료되었습니다.');
                } else {
                  alert('임시비밀번호 설정이 실패하였습니다.');
                }
              });
            },
            error: function (error) {
              // Ajax 요청이 실패할 경우 수행할 동작
              console.error('Ajax 요청 실패', error);
            }
          });
        
    
    }).catch((state) => {
      // Promise reject 시의 처리
      console.error('Promise rejected with code:', state);
      emailPassed = false;
      switch(state){
      case 1: $('#msg_emailTmpPW').text('이메일 형식이 올바르지 않습니다.'); break;
      case 2: $('#msg_emailTmpPW').text('회원이 아닙니다.'); break;
      }
    });
  });
});
