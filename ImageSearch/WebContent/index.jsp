<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Java图片爬虫平台</title>
		
		<style type="text/css">
			body{
				background:#666699;
				margin:0;
				padding:0;
				font-family:"微软雅黑";
				font-size:16px;
				color:#fff;
			}
			.header{
				width:500px;
				margin:0 auto;
			}
			.header h1{
				font-size:30px;
				text-shodow:2px 5px 5px #000;
			}
			#wrapper{
				width:100%;
			}
			#content{
				position:relative;
				width:1100px;
				margin:0 auto 25px;
				padding-bottom:10px;
			}
			.grid{
				margin:8px;
				width:188px;
				min-hight:100px;
				padding:15px;
				background:#fff;
				box-shadow:2px 5px 5px rgba(0,0,0,0.8);
				float:left;
				-webkit-transition:all 1s ease;
				-moz-transition:all 1s ease;
				-o-transition:all 1s ease;
				-ms-transition:all 1s ease;
			}
			.grid strong{
				border-bottom:1px solid #000;
				display:block;
				margin:10px 0;
				padding:0 0 5px;
				font-size:15px;
				color:#000;
			}
			.grid meta{
				text-align:right;
			}
			.grid .imgholder img{
				max-width:100%;
				bockground:#ccc;
				dixplay:block;
				background:url(images/loading.gif) no-repeat center;
			}
		</style>
		<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="js/blocksit.min.js"></script>
		<script type="text/javascript" src="js/jquery.lazyload.min.js"></script>
		<script type="text/javascript" src="js/jquery.lightbox-0.5.min.js"></script>
		<link href="css/jquery.lightbox-0.5.css" type="text/css" rel="stylesheet">
		<script type="text/javascript">
			$(function(){
				var col = 5;
				var pageNum = 1;
				function blockImage(){
					$("#content").BlocksIt({
						numOfCol:col,
						offsetX:8,
						foosetY:8
					});
				}
				$("img.lazy").lazyload({load:blockImage});
				$("#btn").click(function(){
					loadImages();
					$("img.lazy").lazyload();
				});
				function loadImages(){
					var category = "/lvyou/ziran";
					$.ajax({
						type:"post",
						async:false,
						url:"data.jsp",
						data:{pageNum:pageNum,category:category},
						dataType:"json",
						success:function(data){
							for(var i = 0;i <data.length;i ++){
								var img = "";
								img += "<div class=\"grid\">";
								img += "<div class=\"imgholder\">";
								img += "<img class=\"lazy\" scr=\"images/pixel.gif\" data-original=\""+data[i].surl+"\" width=\"175\" stype=\"display:block;\">";
								img += "</div>";
								img += "<strong>"+data[i].title+"</strong>";
								img += "<div class=\"meta\">";
								img += "<a href=\""+data[i].burl+"\" class=\"lightbox\">高清无码原图</a>";
								img += "</div>";
								img += "</div>";
								$("#content").append(img);
							}
						}
					});
					pageNum ++;
					blockImage();
					$("a.lightbox").lightBox();
					$("img.lazy").lazyload();
				}
				$(window).scroll(function(){
					if($(document).height()-$(this).scrollTop()-$(this).height() < 50){
						loadImages();
					}
				});
				var currentWidth = 1100;
				$(window).resize(function(){
					var winWidth = $(window).width();
					var conWidth = 0;
					if(winWidth < 400){
						conWidth = 240;
						col = 1;
					}else if(winWidth < 660){
						conWidth = 440;
						col = 2;
					}else if(winWidth < 880){
						conWidth = 660;
						col = 3;
					}else if(winWidth < 1100){
						conWidth = 880;
						col = 4;
					}else{
						conWidth = 1100;
						col = 5;
					}
					if(conWidth != currentWidth){
						currentWidth = conWidth;
						$("content").width(conWidth);
					}
				});
			});
		</script>
	</head>
	<body>
		<div class="header">
			<h1>Java图片爬虫平台</h1>
			<a href = "javascript:void(0)" id="btn">点击加载</a>
		</div>
		<div id="wrapper">
			<div id="content">
			</div>
		</div>
	</body>
</html>