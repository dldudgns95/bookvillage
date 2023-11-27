/**
 * 메인페이지
 */

    document.addEventListener('DOMContentLoaded', function () {
      const swiper = new Swiper('.swiper', {
          autoplay: {
         delay: 5000, 
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