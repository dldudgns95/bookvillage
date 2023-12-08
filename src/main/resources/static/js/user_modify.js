/**
 * 마이페이지
 */


/* 함수호출 */
$(() => {
  fnCheckName();
  fnCheckMobile();
  fnModifyUser();
  fnModifyPasswordForm();
})

/* 전역변수 선언 */
var namePassed = true;
var mobilePassed = true;

const fnCheckName = () => {
  $('#name').blur((ev) => {
    let name = ev.target.value;
    let bytes = 0;
    for(let i = 0; i < name.length; i++){
      if(name.charCodeAt(i) > 128){  // 코드값이 128을 초과하는 문자는 한 글자 당 2바이트임
        bytes += 2;
      } else {
        bytes++;
      }
    }
    namePassed = (bytes <= 50);
    if(!namePassed){
      $('#msg_name').text('이름은 50바이트 이내로 작성해야 합니다.');
    }
  })
}

const fnCheckMobile = () => {
  $('#mobile').keyup((ev) => {
    ev.target.value = ev.target.value.replaceAll('-', '');
    // 휴대전화번호 검사 정규식 (010숫자8개)
    let regMobile = /^010[0-9]{8}$/;
    mobilePassed = regMobile.test(ev.target.value);
    if(mobilePassed){
      $('#msg_mobile').text('');
    } else {
      $('#msg_mobile').text('휴대전화번호를 확인하세요.');       
    }
    $.ajax({
      type: 'post',
      url: '/mypage/modify.do',
      data: {mobile: ev.target.value},
      dataType: 'json',
      success: (resData) => {
        if(resData.result > 0) {
          $('#msg_mobile').text('이미 사용 중인 휴대전화번호입니다.');
          mobilePassed = false;
        } else {
          $('#msg_mobile').text('');
          mobilePassed = true;
        }
      }
    })
  })
}

const fnModifyUser = () => {
  $('#btn_modify').click(() => {
    if(!namePassed){
      alert('이름을 확인하세요.');
      return;
    } else if(!mobilePassed){
      alert('휴대전화번호를 확인하세요.');
      return;
    }
    $.ajax({
      // 요청
      type: 'post',
      url: '/mypage/modify.do',
      data: $('#frm_mypage').serialize(),
      // 응답
      dataType: 'json',
      success: (resData) => {  // {"modifyResult": 1}
        if(resData.modifyResult === 1){
          alert('회원 정보가 수정되었습니다.');
        } else {
          alert('회원 정보가 수정되지 않았습니다.');
        }
      }
    })
  })
}

const fnModifyPasswordForm = () => {
  $('#btn_modify_pw').click(() => {
    location.href = '/mypage/modifyPw.form';
  })
}


