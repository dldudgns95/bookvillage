/**
 * 아이디 찾기
 */


 function findId_click() {
    var name=$('#name').val()
    var mobile=$('#mobile').val()
    
    $.ajax({
      type:'POST',
      url: './user/findID',
      data:{"name":name, "mobile":mobile} ,
      
      dataType: 'json',
      success:function(resData){
        
                    console.log("서버 응답 데이터:", resData);

        if(!resData){
          alert('이름, 전화번호를 입력해주세요.')
        } else {
          $('#name').val('');
          $('#mobile').val('');
          
        }
      },
       error:function(){
                  alert("에러입니다");
              }
    });
  };

const modal = document.getElementById("modal")
const btnModal = document.getElementById("find_id")

btnModal.addEventListener("click", e => {
    modal.style.display = "flex"
})

    
const closeBtn = modal.querySelector(".close-area")
closeBtn.addEventListener("click", e => {
    modal.style.display = "none"
})

modal.addEventListener("click", e => {
    const evTarget = e.target
    if(evTarget.classList.contains("modal-overlay")) {
        modal.style.display = "none"
    }
})