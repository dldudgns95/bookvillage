

//검색어(st) 없으면 검색 막기
$(document).ready(function() {
            $('#search_main').submit(function(event) {
                var searchInputValue = $('#st_main').val().trim();
                if (!searchInputValue) {
                    event.preventDefault();
                    alert('검색어를 입력하세요.');
                }
            });
        });

//뒤로가기 
$(document).ready(function() {
    $("#BackToSearch").on("click", () => {
        window.history.back();
    });
});

//별점
function saveReview() {
   // 사용자가 선택한 별점
   const selectedStar = document.querySelector('.score input[name="star"]:checked');
   
   if (!selectedStar) {
      alert("별점을 선택해주세요.");
      return;
   }
   
   
   
   const starValue = selectedStar.value;
   const isbn = document.querySelector('.score input[name="isbn"]').value;
   const userNo = document.querySelector('.score input[name="userNo"]').value;
   
   if (userNo === '0') {
    var confirmLogin = confirm('로그인이 필요합니다. 로그인 페이지로 이동하시겠습니까?');
    if (confirmLogin) {
      window.location.href = '/user/login.form';
    }
    return false; // 이벤트 막기
   }
   
   const checkScore = document.getElementById('checkScore').value;
   if (checkScore != 0) {
     alert("이미 한줄평을 등록했습니다. 도서 당 하나만 등록 가능합니다");
     return false; // 이벤트 막기
   }
   
   const reviewText = document.getElementById('review').value;
   const currentDate = new Date();

   $.ajax({
      type: 'POST',
      url: '/book/addScore.do',
      contentType: 'application/json',
      data: JSON.stringify({isbn: isbn, userNo: userNo,  star: starValue, review: reviewText, reviewDate: currentDate.toISOString()}),  // JSON 데이터로 변환하여 전송
      success: function(response) {
         // 서버에서 받은 응답 처리
         alert("한줄평이 등록되었습니다.");
         window.location.href = '/book/search/detail?isbn='+isbn; 
      },
      error: function(error) {
         console.error('AJAX 요청 에러:', error);
      }
   });
}

//관심도서
// result에서 관심도서 추가
$(document).ready(function() {
    function refresh(){
    location.reload(); //페이지 새로고침 없이 리로드
  }
  
    $(".wishBtn").click(function() {
        var currentDate = new Date();
        var timestamp = currentDate.getTime();
        var isbn = $(this).closest('form').find("input[name='isbn']").val();
        document.getElementById('wishDate').value = timestamp;
        
        // userNo가 0이면 이벤트 막기
        var userNo = $(this).closest('form').find("input[name='userNo']").val();
        if (userNo === '0') {
            var confirmLogin = confirm('로그인이 필요합니다. 로그인 페이지로 이동하시겠습니까?');
            if (confirmLogin) {
                window.location.href = '/user/login.form';
            }
            return false;
        }

        var formData = {
            userNo: $(this).closest('form').find("input[name='userNo']").val(),
            isbn: isbn,
            wishDate: $("#wishDate").val()
        };

        // checkWish 수행 후 0이면 add, 1이면 delete
        $.ajax({
            type: "POST",
            url: "/book/checkWish.do",
            contentType: "application/json",
            data: JSON.stringify(formData),
            success: function(response) {
                const responseData = JSON.parse(response);
                console.log(responseData.checkWish);
                console.log(response);
                //관심도서 추가
                if (responseData.checkWish === 0) {
                    $.ajax({
                        type: "POST",
                        url: "/book/addWish.do",
                        contentType: "application/json",
                        data: JSON.stringify(formData),
                        success: function(response) {
                            alert("관심도서에 추가되었습니다.");
                            console.log('Wish added successfully.');
                            refresh();
                        },
                        error: function(error) {
                            console.log("addWish Error:", error);
                        }
                    });
                }
                //관심도서 삭제  
                else if (responseData.checkWish === 1) {
                    $.ajax({
                        type: "POST",
                        url: "/book/deleteWish.do",
                        contentType: "application/json",
                        data: JSON.stringify(formData),
                        success: function(response) {
                            alert("관심도서에서 삭제되었습니다.");
                            console.log('Wish deleted successfully.');
                            refresh();                          
                        },
                        error: function(error) {
                            console.log("deleteWish Error:", error);
                        }
                    });
                }
            },
            error: function(error) {
                console.log("checkWish Error:", error);
            }
        });
    });
});


//대출
$(document).ready(function() {
    function refresh(){
    location.reload(); //페이지 새로고침 없이 리로드
  }
  
  $("#updateCheckoutForm button").click(function() {

    // 폼 데이터 수집
    var formData = {
      userNo: $("input[name='userNo']").val(),
      isbn: $("input[name='isbn']").val(),
    };

    // userNo가 0이면 이벤트 막기
    var userNo = $("input[name='userNo']").val();
    if (userNo === '0') {
      var confirmLogin = confirm('로그인이 필요합니다. 로그인 페이지로 이동하시겠습니까?');
      if (confirmLogin) {
        window.location.href = '/user/login.form';
      }
      return false; // 이벤트 막기
     }

      /* 대출 불가능한 경우 알림 창 표시 */
      var checkBookCkCnt = $("input[name='checkBookCkCnt']").val();        
      console.log(checkBookCkCnt);
      if (checkBookCkCnt >= 5) {
          alert('대출이 불가능합니다. 대출 권수를 초과하셨습니다.');
          return false;
      }
      var checkUserStatus = $("input[name='checkUserStatus']").val(); 
      console.log('0이면 연체 :'+ checkUserStatus);
      if (checkUserStatus == 0) {
          alert('대출이 불가능합니다. 현재 연체 상태입니다..');
          return false;
      }
      
    
    // 서버로 POST 요청 보내기
    $.ajax({
      type: "POST",
      url: "/book/updateBook.do",
      contentType: "application/json",
      data: JSON.stringify(formData),
      success: function(response) {

        console.log('Book Status update successfully.');
      },
      error: function(error) {
        console.log("Error:", error);
      }
    });
    
    $.ajax({
      type: "POST",
      url: "/book/updateCheckout.do",
      contentType: "application/json",
      data: JSON.stringify(formData),
      success: function(response) {
        console.log('Checkout Status insert successfully.');
        refresh();
        alert('대출 신청되었습니다.');
        
      },
      error: function(error) {
        console.log("Error:", error);
      }
    });
    
  });
});

//이용안내
$(document).ready(function() {
  const guideEl = document.querySelector('.detail_guide');
  const GuideBtn  = document.querySelector('.toggle-guide')
  let isHide = true;
  GuideBtn.addEventListener('click', function () {
    isHide = !isHide 
    if (isHide) {
      // true면 숨김처리
      guideEl.classList.add('hide')
    } else {
      //false면 보임처리
      guideEl.classList.remove('hide')
    }
  })
});