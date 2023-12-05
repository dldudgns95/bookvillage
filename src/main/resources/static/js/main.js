/**
 * 메인페이지
 */


// 메인슬라이드
    document.addEventListener('DOMContentLoaded', function () {
  const firstSwiper = new Swiper('.swiper.first', {
    autoplay: {
    delay: 4000,
    },
    direction: 'horizontal',
    loop: true,
    pagination: {
      el: '.swiper-pagination',
    },
    navigation: {
      nextEl: '.swiper-button-next',
      prevEl: '.swiper-button-prev',
    },
    scrollbar: {
      el: '.swiper-scrollbar',
    },
  });
});

// 도서 목록 슬라이드
    document.addEventListener('DOMContentLoaded', function () {
  const secondSwiper  = new Swiper('.swiper.second', {
    spaceBetween: 10,    // 슬라이드 사이 여백
    slidesPerView : 5, // 한 슬라이드에 보여줄 갯수
    autoHeight : true,  // 현재 활성 슬라이드높이 맞게 높이조정
    autoWidth: true,
    a11y : false, // 접근성 매개변수(접근성 관련 대체 텍스트 설정이 가능) 

    autoplay: {
      delay: 3500,
    },
    direction: 'horizontal',
    loop: true,
    pagination: {
      el: '.swiper-pagination',
    },
    navigation: {
      nextEl: '.swiper-button-next',
      prevEl: '.swiper-button-prev',
    },
    scrollbar: {
      el: '.swiper-scrollbar',
    },
  });
});





 // 공지사항 목록
  $(document).ready(function() {
      $.ajax({
          type: 'get',
          url: '/main/noticeList.do',
          dataType: 'json',
          success: function(ntList) {
              var ntBoby = $('#ntBoby');
              ntBoby.empty();
              
              for (var i = 0; i < Math.min(5, ntList.length); i++) {
                  var nt = ntList[i];
                  
                  var ntTitle = nt.ntTitle.length > 10 ? nt.ntTitle.substring(0, 15) + '...' : nt.ntTitle;    
             
                  var row = '<tr>' +
                      '<td>' + nt.ntNo + '</td>' +
                      '<td><a href="/support/faqdetail.do?faqNo=' + nt.ntNo + '">' + ntTitle + '</a></td>' +
                      '<td>' + nt.ntDate + '</td>' +
                      '</tr>';
                  ntBoby.append(row);
              }
          },
          error: function(error) {
              console.error('요청 에러:', error);
          }
      });
  });

    
    // FAQ 목록
$(document).ready(function() {
    $.ajax({
        type: 'get',
        url: '/main/faqList.do',
        dataType: 'json',
        success: function(faqList) {
            var faqTableBody = $('#faqTableBody');
            
            faqTableBody.empty();
            
            for (var i = 0; i < Math.min(5, faqList.length); i++) {
                var faq = faqList[i];
                
                // 컨텐츠를 최대 10글자로 제한
                var truncatedContent = faq.faqContent.length > 15 ? faq.faqContent.substring(0, 30) + '...' : faq.faqContent;
                var truncatedTitle = faq.faqTitle.length > 10 ? faq.faqTitle.substring(0, 15) + '...' : faq.faqTitle;
                
                var row = '<tr>' +
                    '<td>' + faq.faqNo + '</td>' +
                    '<td><a href="/support/faqdetail.do?faqNo=' + faq.faqNo + '">' + truncatedTitle + '</a></td>' +
                    '<td>' + truncatedContent + '</td>' + 
                    '</tr>';
                    
                faqTableBody.append(row);
            }
        },
        error: function(error) {
            console.error('요청 에러:', error);
        }
    });
});


// 
