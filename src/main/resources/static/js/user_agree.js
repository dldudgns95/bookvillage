/**
 * 가입 이전 단계 : 약관 동의 페이지
 */

$(() => {
  fnChkAll();
  fnChkEach();
  fnJoinForm();
})

const fnChkAll = () => {
  $('#chk_all').click((ev) => {
    $('.chk_each').prop('checked', $(ev.target).prop('checked'));
  })
}

const fnChkEach = () => {
  $(document).on('click', '.chk_each', () => {
    var total = 0;
    $.each($('.chk_each'), (i, elem) => {
      total += $(elem).prop('checked');
    })
    $('#chk_all').prop('checked', total === $('.chk_each').length);
  })
}

const fnJoinForm = () => {
  $('#frm_agree').submit((ev) => {
    if(!$('#service').is(':checked')){
      alert('필수 약관에 동의하세요.');
      ev.preventDefault();
      return;
    }
  })
}