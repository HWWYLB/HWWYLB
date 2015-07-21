$(document).ready(function(){

	if($("#visitor").attr("uid") != undefined){
		$(".nav-collapse ul").hide();
		$(".nav-collapse .navbar-text").show();
		getRoomList();
		setTimeout('getJoinedRoomList()', 1000);
	}

	//切换显示登录框
	$(".login").click(function(){
		if($("#form-login").is(":visible")){
			$("#form-login").hide();
		}else{
			$("#form-register").hide();
			$("#form-login").show();
		}
	})

	//切换显示注册框
	$(".register").click(function(){
		if($("#form-register").is(":visible")){
			$("#form-register").hide();
		}else{
			$("#form-login").hide();
			$("#form-register").show();
		}
	})

	//登录表单提交
	$("#form-login").submit(function(e){
		var username = $("#username").val();
		var password = $("#password").val();
		var check = $(this).find("input[type='checkbox']").attr("checked");
		check = check === undefined ? false : true;

		console.log("用户名：" + username + " 密码：" + password + " 记住：" + check);

		if(username === '' || password === ''){
			$(".nav-collapse .label-warning").html("用户名和密码不能为空!").show();
		}else{
			$.ajax({
				type: "post",
				url: "../AuthLogin",
				data: {
					"username" : username, 
					"password" : password, 
					"check" : check
				},
				dataType: "json",
				success: function(data){
					console.log(data);
					if(data['state'] === "success"){
						console.log("login success");
						$(".nav-co llapse .label-success").html(data['info']).show();
						$(".nav-collapse ul").hide();
						$("#visitor").html(data['username']);
						$("#visitor").attr("uid", data['uid']);
						$(".nav-collapse .navbar-text").show();
						getRoomList();
						setTimeout('getJoinedRoomList()', 2000);
					}else{
						$(".nav-collapse .label-important").html(data['info']).show();
					}
				},
				error: function(){
					$(".nav-collapse .label-important").html("服务器错误，请稍后再试...").show();
				}
			});
		}
		setTimeout('$(".nav-collapse .label").hide()', 3000);
	})
	
	//注册表单提交
	$("#form-register").submit(function(e){
		var username = $("#username-reg").val();
		var password = $("#password-reg").val();
		var password2 = $("#password2-reg").val();

		console.log("用户名：" + username + " 密码：" + password + " 确认密码：" + password2);

		if(password !== password2){
			alert("两次输入密码不一致！");
			return false;
		}

		if(username === '' || password === '' || password2 === ''){
			$(".nav-collapse .label-warning").html("输入不能为空!").show();
		}else{
			$.ajax({
				type: "post",
				url: "../Register",
				data: {
					"username" : username, 
					"password" : password, 
					"password2" : password2
				},
				dataType: "json",
				success: function(data){
					console.log(data);
					if(data['state'] === "success"){
						$(".nav-collapse .label-success").html(data['info']).show();
					}else if(data['state'] == "unsame"){
						$(".nav-collapse .label-warning").html(data['info']).show();
					}else{
						$(".nav-collapse .label-important").html(data['info']).show();
					}
				},
				error: function(){
					$(".nav-collapse .label-important").html("服务器错误，请稍后再试...").show();
				}
			});
		}
		setTimeout('$(".nav-collapse .label").hide()', 3000);
	})
	
	//创建聊天室表单提交
	$("#form-addroom-submit").click(function(){
		var roomname = $("#room-name").val();
		var roominfo = $("#room-info").val();
		var roompwd = $("#room-password").val();

		if(roomname == ""){
			return alert("聊天室名称不能为空");
		}else{
			$.ajax({
				type: "post",
				url: "../CreateRoom",
				data: {
					"roomname" : roomname,
					"roominfo" : roominfo,
					"password" : roompwd
				},
				dataType: "json",
				success: function(data){
					console.log(data);

					alert(data['info']);
					if(data['state'] == "success"){
						$("#add-room").modal("hide");
					}
				},
				error: function(){
					$(".nav-collapse .label-important").html("服务器错误，请稍后再试...").show();
				}
			})
		}
	})





	//登出
	$("#logout").click(function(){
		$.ajax({
			type: "get",
			url: "../Logout",
			dataType: "text",
			success: function(data){
				console.log(data);
				$(".nav-collapse .label-success").html(data).show();
				setTimeout("window.location.href = 'index.jsp'", 2000);
			},
			error: function(){
				$(".nav-collapse .label-important").html("服务器错误，请稍后再试...").show();
			}
		});		
	})

	//富文本编辑器
	initToolbarBootstrapBindings(); 
	$('#editor').wysiwyg({ fileUploadError: showErrorAlert} );

	createRoom("home");
	addInfo($("#chatroom-home"), "欢迎使用ChatOL...");


	//发送消息
	$("#send").click(function(){
		var msg = trim($("#editor").html());
		var rid = $("#room-tab .active").attr("rid");
		var to = "*";
		var type = "message";

		msg = msg.split(" ");

		if(msg[0].search(/^@\S+/g) == 0){
			to = msg[0].substring(1, msg[0].length);
			type = "private";
			//console.log("to : " + to);
		}
		msg = msg.join(" ");
		//console.log("msg : " + msg);

		console.log("message:"+msg+" "+rid+" "+to+" "+type);
		if(msg == '') return $("#tips .label-warning").html("发送消息不能为空！").show();
		if(to == '') return $("#tips .label-warning").html("私信对象不能为空！").show();
		if(rid == "home") return;

		$.ajax({
			type: "post",
			url: "../AcceptMessage",
			data: {
				"content" : msg,
				"rid" : rid,
				"to" : to,
				"type" : type
			},
			dataType: "json",
			success: function(data){
				console.log(data);
				if(data['state'] == 'success'){
					console.log(data['info']);
					$("#tips .label-success").html(data['info']).show();
					$("#editor").html("");
				}else{
					console.log(data['info']);
					$("#tips .label-important").html(data['info']).show();
				}
			},
			error: function(){
				$(".nav-collapse .label-important").html("服务器错误!").show();
				setTimeout('$(".nav-collapse .label").hide()', 3000);
			}
		})
		setTimeout('$("#tips .label").hide()', 4000);
	})
})