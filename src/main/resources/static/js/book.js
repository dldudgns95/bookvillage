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
    $("#goBack").on("click", () => {
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
   // 사용자가 작성한 리뷰
   const reviewText = document.getElementById('review').value;
   
   // 현재 시간 생성
   const currentDate = new Date();

   // AJAX를 사용하여 서버에 데이터 전송
   $.ajax({
      type: 'POST',
      url: '/book/review.do', // 실제 엔드포인트에 따라 수정
      contentType: 'application/json',  // JSON 형식으로 데이터 전송
      data: JSON.stringify({isbn: isbn, userNo: userNo,  star: starValue, review: reviewText, reviewDate: currentDate.toISOString()}),  // JSON 데이터로 변환하여 전송
      success: function(response) {
         // 서버에서 받은 응답 처리
         alert(response.message);
      },
      error: function(error) {
         console.error('AJAX 요청 에러:', error);
      }
   });
}