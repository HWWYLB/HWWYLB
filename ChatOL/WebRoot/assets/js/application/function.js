//聊天室消息
function addMsg(container, title, content, isMine, mid){
	$(container).append("<div "+(isMine == true ? "class='mine'" : "")+" mid='"+mid+"'><span><i class='icon-envelope'></i> "+title+"</span><br/><p class='message'>"+content+"</p></div>");
	$(container).scrollTop(100000);
}

//聊天室公告
function addInfo(container, content, mid){
	$(container).append("<div mid='"+mid+"'><p class='info'>"+content+"</p></div>");
	$(container).scrollTop($(container).height());
}

//添加聊天室/成员列表
function addList(container, content, info){
	$(container).append("<li " + info + "><a href='#'>" + content + "</a></li>");
}

//清除列表
function clearList(list){
	$("#"+list+"-list li").each(function(){
		if($(this).hasClass("nav-header") == false){
			$(this).remove();
		}
	})
}

//创建聊天室界面
function createRoom(rid, nonactive){
	var name = $("#room-list li[rid='"+rid+"'] a").html();
	console.log("name:"+name+" rid:"+rid);
	name = name ? name.substring(26) : name;
	$("#room-tab").append('<li class="' + (nonactive ? '' : 'active') + '" rid="' + rid + '"><a href="#chatroom-' + rid + '" data-toggle="tab"><i class="icon-comment"></i> ' +  (name == null ? "首页" : name) + "</a></li>");
	$("#tab-content").append('<div class="chat-content well tab-pane ' + (nonactive ? '' : 'active') + '" id="chatroom-' + rid + '"></div>').css("background-color", "#f5f5f5");	
}


//进入房间
function joinRoom(rid, password){
	console.log("joinRoom:"+rid);
	if(rid == "home" || !rid) return;
	$.ajax({
		type: "post",
		url: "../JoinRoom",
		data: {
			"rid" : rid,
			"password" : password
		},
		dataType: "json",
		success: function(data){
			console.log(data);
			if(data['state'] == "success"){
				console.log(data['info']);
				$("#room-tab .active").removeClass("active");
				$("#tab-content .active").removeClass("active");
				createRoom(rid);
				tabClick();
				getUserList();
				acceptMessage();
			}else{
				console.log("join room fail");
				$(".nav-collapse .label-important").html(data['info']).show();
			}
		},
		error: function(){
			$(".nav-collapse .label-important").html("服务器错误，请稍后再试...").show();
		}
	})
	setTimeout('$(".nav-collapse .label").hide()', 5000);
}

//离开聊天室
function leaveRoomClick(rid){
	if(rid == "home" || !rid) return;
	$.ajax({
		type: "get",
		url: "../LeaveRoom",
		data: {
			"rid" : rid
		},
		dataType: "json",
		success: function(data){
			console.log(data);
			if(data['state'] == "success"){
				clearList("user");
				console.log(data['info']);
				$("#chatroom-"+rid).remove();
				$("#room-tab .active").remove();

				$("#room-tab li[rid='home']").addClass("active");
				$("#chatroom-home").addClass("active");
			}else{
				console.log(data['info']);
			}
		},
		error: function(){
			$(".nav-collapse .label-important").html("服务器错误，请稍后再试...").show();
		}
	});			
	setTimeout('$(".nav-collapse .label").hide()', 3000);
}

//从服务端获取聊天室列表
function getRoomList(){
	$.ajax({
		type: "get",
		url: "../RoomList",
		dataType: "json",
		success: function(data){
			console.log(data);
			if(data['state'] == "success"){
				clearList("room");
				for(var i = 0; i < data['mine'].length; i++){
					addList($("#room-list"), '<i class="icon-home"></i> ' + data['mine'][i]['roomname'] + (data['mine'][i]['isEncrypt'] == "false" ? "" : '<i class="icon-eye-close"></i>'), 'rid="'+ data['mine'][i]['rid'] +'" isEncrypt="' + data['mine'][i]['isEncrypt'] +'" info="' + data['mine'][i]['roominfo'] + '" class="mine"');
				}
				for(var i = 0; i < data['notmine'].length; i++){
					addList($("#room-list"), '<i class="icon-home"></i> ' + data['notmine'][i]['roomname'] + (data['notmine'][i]['isEncrypt'] == "false" ? "" : '<i class="icon-eye-close"></i>'), 'rid="'+ data['notmine'][i]['rid'] +'" isEncrypt="' + data['notmine'][i]['isEncrypt'] +'" info="' + data['notmine'][i]['roominfo'] + '"');
				}
				listClick();
				listDClick();

				setTimeout("getRoomList()", 5000);
			}else{
				$(".nav-collapse .label-important").html(data['info']).show();
				setTimeout("getRoomList()", 5000);
			}
		},
		error: function(){
			$(".nav-collapse .label-important").html('服务器错误').show();
		}
	})
	setTimeout('$(".nav-collapse .label").hide()', 3000);
}

//获取已加入过的房间
function getJoinedRoomList(){
	$.ajax({
		type: "get",
		url: "../JoinedRoomList",
		dataType: "json",
		success: function(data){
			console.log(data);
			if(data['state'] == "success"){
				console.log(data['info']);
				for(var i = 0; i < data['rids'].length; i++){
					console.log(data['rids'][i]);
					createRoom(data['rids'][i], true);
				}
				tabClick();
			}else{
				$(".nav-collapse .label-important").html(data['info']).show();
			}
		},
		error: function(){
			$(".nav-collapse .label-important").html('服务器错误').show();
		}
	})
	setTimeout('$(".nav-collapse .label").hide()', 3000);
}

//从服务端获取成员列表
function getUserList(){
	var rid = $("#room-tab .active").attr("rid");
	console.log("rid:" + rid);
	if(rid == "home" || !rid) return;
	$.ajax({
		type: "post",
		url: "../UserList",
		data: {
			"rid":rid
		},
		dataType: "json",
		success: function(data){
			console.log(data);
			if(data['state'] == 'success'){
				console.log(data['info']);
				clearList("user");				
				for(var i = 0; i < data['list'].length; i++){
					addList($("#user-list"), '<i class="icon-user"></i> ' + data['list'][i]['username'], 'uid="' + data['list'][i]['uid'] + '" username="' + data['list'][i]['username'] + '"');
				}
				setTimeout("getUserList()", 5000);
			}else{
				$(".nav-collapse .label-important").html(data['info']).show();
				setTimeout("getUserList()", 5000);
			}
		},
		error: function(){
			$(".nav-collapse .label-important").html("服务器错误!").show();
		}		
	})
	setTimeout('$(".nav-collapse .label").hide()', 3000);
}

//从服务端接收消息
function acceptMessage(){
	var rid = $("#room-tab .active").attr("rid");
	if(rid == "home" || !rid) return;
	var mid = $("#chatroom-" + rid + " div:last-child").attr('mid');
	mid = mid ? mid : 0;
	$.ajax({
		type: "post",
		url: "../SendMessage",
		data: {
			"rid" : rid,
			"mid" : mid
		},
		dataType: "json",
		success: function(data){
			console.log(data);
			for(var i = 0; i < data.length; i++){
				var isMine = data[i]['from'] == $("#visitor").html() ? true : false;
				var container = $("#chatroom-"+rid);
				switch(data[i]['type']){
					case 'message':
						addMsg(container, data[i]['from'] + " " + data[i]['time'], data[i]['content'], isMine, data[i]['mid']);
						break;
					case 'private':
						console.log('private message');
						addMsg(container, data[i]['from'] + " " + data[i]['time'], data[i]['content'], isMine, data[i]['mid']);
						break;
					case 'info':
						addInfo(container, '<i class="icon-info-sign"></i> ' + data[i]['content'], data[i]['mid']);
						break;
					default: break;
				}
			}
			setTimeout("acceptMessage()", 1000);
		},
		error: function(){
			$(".nav-collapse .label-important").html("服务器错误!").show();
			setTimeout('$(".nav-collapse .label").hide()', 3000);
		}
	})
}

//聊天室清屏
function clear(){
	$("#tab-content .active").html("");
}

//列表单击事件绑定
function listClick(){
	$("#room-list li").click(function(){
		if($(this).hasClass("active"))
			$(this).removeClass("active");
		else{
			$("#room-list .active").removeClass("active");
			$(this).addClass("active");
		}
	})

	$("#user-list li").click(function(){
		var uid = $(this).attr("uid");
		var uname = $(this).attr("username");
		console.log("私聊对象：" + uid + " " + uname);
		$("#editor").html("@" + uname + " ");
	})
}

//聊天室列表双击
function listDClick(){
	$("#room-list li").dblclick(function(){
		var rid = $(this).attr("rid");
		var isEncrypt = $(this).attr("isEncrypt");
		var password;

		console.log($("#chatroom-" + rid));
		if($("#chatroom-" + rid).hasClass("chat-content") === false){
			if(isEncrypt == "true" && $(this).hasClass("mine") === false){
				password = prompt("请输入房间密码：");
				if(password == "" || !password) return;
			} 		
			joinRoom(rid, password);			
		}else{
			$("#room-tab .active").removeClass("active");
			$("#tab-content .active").removeClass("active");			
			$("#room-tab li a[href='#chatroom-" + rid + "']").parent().addClass("active");
			$("#chatroom-" + rid).addClass("active");
		}
	})
}

function tabClick(){
	console.log($("#room-tab").html());
	$("#room-tab li a").click(function(e){
		console.log("click " + $(this).parent().attr("rid"));
		if($(this).parent().attr("rid") != 'home'){
			$("#room-tab .active").removeClass("active");
			$("#tab-content .active").removeClass("active");
			$(this).parent().addClass("active");
			$("#chatroom-"+$(this).parent().attr("rid")).addClass("active");
			getUserList();
			acceptMessage();
		}
	})

	$("#room-tab li a").dblclick(function(e){
		console.log("双击操作 退出的房间是：" + $(this).parent().attr("rid"));
		leaveRoomClick($(this).parent().attr("rid"));
	})
}



/////////////
//富文本相关//
/////////////
function initToolbarBootstrapBindings() {
	var fonts = ['Serif', 'Sans', 'Arial', 'Arial Black', 'Courier', 
	        'Courier New', 'Comic Sans MS', 'Helvetica', 'Impact', 'Lucida Grande', 'Lucida Sans', 'Tahoma', 'Times',
	        'Times New Roman', 'Verdana'],
	        fontTarget = $('[title=Font]').siblings('.dropdown-menu');
	  $.each(fonts, function (idx, fontName) {
	      fontTarget.append($('<li><a data-edit="fontName ' + fontName +'" style="font-family:\''+ fontName +'\'">'+fontName + '</a></li>'));
	  });
	  $('a[title]').tooltip({container:'body'});
		$('.dropdown-menu input').click(function() {return false;})
		    .change(function () {$(this).parent('.dropdown-menu').siblings('.dropdown-toggle').dropdown('toggle');})
	    .keydown('esc', function () {this.value='';$(this).change();});

	  $('[data-role=magic-overlay]').each(function () { 
	    var overlay = $(this), target = $(overlay.data('target')); 
	    overlay.css('opacity', 0).css('position', 'absolute').offset(target.offset()).width(target.outerWidth()).height(target.outerHeight());
	  });
	  if ("onwebkitspeechchange"  in document.createElement("input")) {
	    var editorOffset = $('#editor').offset();
	    $('#voiceBtn').css('position','absolute').offset({top: editorOffset.top, left: editorOffset.left+$('#editor').innerWidth()-35});
	  } else {
	    $('#voiceBtn').hide();
	  }
};
function showErrorAlert (reason, detail) {
	var msg='';
	if (reason==='unsupported-file-type') { msg = "Unsupported format " +detail; }
	else {
		console.log("error uploading file", reason, detail);
	}
	$('<div class="alert"> <button type="button" class="close" data-dismiss="alert">&times;</button>'+ 
	 '<strong>File upload error</strong> '+msg+' </div>').prependTo('#alerts');
};

function trim(str){
　　 return str.replace(/(^\s*)|(\s*$)/g, "");
}
