
//breadcrumb
  $('.breadcrumb .b_menu > li > a').on('mouseover focus', function(event){
    var speed = 400;
    if ($(this).next('.breadcrumb .dep_list').css('display')=='none') {
      $('.breadcrumb .dep_list').slideUp(speed);
      $(this).next('.breadcrumb .dep_list').slideDown(speed);
      $('.breadcrumb .dep_list').removeClass('on');
      $(this).parent('li').addClass('on');
    }else{
      $('.breadcrumb .dep_list').slideUp(speed);
      $('.breadcrumb .b_menu > li').removeClass('on');
    }
  });

  $(".breadcrumb .b_menu > li").on("mouseleave", function(event){
    $(".breadcrumb .dep_list").slideUp("600");
    $('.breadcrumb .b_menu > li').removeClass('on');
  });

  $(window).on('resize',function(){
    if($(window).innerWidth() > 768){
      $('#fullmenu_nav').animate({left: '-100%'}, 350);
      $('.mask').remove();
      $('body').removeClass('hidden');
      $('#fullmenu_nav ul li').removeClass('open');
      $('#fullmenu_nav ul').removeAttr('style');
      $('ul.tab_list').removeAttr('style');
    }
  });

  $('.breadcrumb .b_menu > li.no a').removeAttr("href");
  $('.breadcrumb .b_menu > li.no').children('ul').remove();