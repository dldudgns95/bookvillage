//검색어(st) 없으면 검색 막기
$(document).ready(function() {
            $('#search').submit(function(event) {
                var searchInputValue = $('#st').val().trim();
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
   
   if (userNo==0) {
      alert("로그인이 필요합니다.");
      event.preventDefault();
      return;
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

//관심도선
//관심도서 토글
$(document).ready(function() {
  $("#WishForm button").click(function() {
    const toggleWishBtn = document.getElementById('toggleWishBtn');
    
    var currentDate = new Date();
    var timestamp = currentDate.getTime();
    document.getElementById('wishDate').value = timestamp;
    
    var formData = {
      userNo: $("input[name='userNo']").val(),
      isbn: $("input[name='isbn']").val(),
      wishDate: $("#wishDate").val()
  };
  
// checkWish 수행 후 0이면 add, 1이면 delete
  $.ajax({
    type: "POST",
    url: "/book/checkWish.do",
    contentType: "application/json",
    data: JSON.stringify(formData),
    success: function(response) {
      const responseData = JSON.parse(response); // {"checkWish":0} 의 json 데이터로 값을 불러왔으므로 parse 해줘야 0만 저장됨 
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
            toggleWishBtn.classList.add("wishAdded");
            localStorage.setItem('wishStatus', 'wishAdded');
            toggleWishBtn.classList.remove("notAdded");
          },
          error: function(error) {
            console.log("addWish Error:", error);
          }
        });
//관심도서 삭제  
      } else if (responseData.checkWish === 1) {
         $.ajax({
          type: "POST",
          url: "/book/deleteWish.do",
          contentType: "application/json",
          data: JSON.stringify(formData),
          success: function(response) {
            alert("관심도서에서 삭제되었습니다.");
            console.log('Wish deleted successfully.');
            toggleWishBtn.classList.remove("wishAdded");
            toggleWishBtn.classList.add("notAdded");
            sessionStorage.setItem('wishStatus', 'notAdded');
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
// 페이지 로드 시 세션 스토리지에서 상태 확인 및 클래스 적용 (페이지 나가도 관심도서 상태 유지)
$(document).ready(function() {
    const toggleWishBtn = document.getElementById('toggleWishBtn');
    const wishStatus = sessionStorage.getItem('wishStatus');

    if (wishStatus === 'wishAdded') {
        toggleWishBtn.classList.add("wishAdded");
    } else {
        toggleWishBtn.classList.add("notAdded");
    }
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

    // 서버로 POST 요청 보내기
    $.ajax({
      type: "POST",
      url: "/book/updateBook.do",
      contentType: "application/json",
      data: JSON.stringify(formData),
      success: function(response) {
        alert("대출 확인되었습니다. 반납 기한은 7일입니다.");
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
      },
      error: function(error) {
        console.log("Error:", error);
      }
    });
  });
});