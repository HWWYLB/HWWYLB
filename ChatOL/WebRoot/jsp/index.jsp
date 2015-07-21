<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String uid = (String)session.getAttribute("uid");
String username = (String)session.getAttribute("username");
%>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>ChatOL</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="chatroom-online">
    <meta name="author" content="xiaomu">

    <!-- Le styles -->
    <link href="../assets/css/bootstrap.css" rel="stylesheet">
    <link href="../assets/css/bootstrap-responsive.css" rel="stylesheet">
    <link href="../assets/css/index.css" rel="stylesheet">

    <link href="../assets/css/main.css" rel="stylesheet">
    
  </head>

  <body>

    <div class="navbar navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container-fluid">
          <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </a>
          <a class="brand" href="index.jsp#">在线聊天室 ChatOL</a>
          <div class="nav-collapse">
            <ul class="nav pull-right">
              <li>
                <form class="form-inline" id="form-login">
                  <input type="text" class="input-small" id="username" placeholder="用户名">
                  <input type="password" class="input-small" id="password" placeholder="密码">
                  <label class="checkbox">
                    <input type="checkbox"> 记住我
                  </label>
                  <button type="submit" class="btn">登录</button>
                </form>
              </li>
              <li>
                <form class="form-inline" id="form-register">
                  <input type="text" class="input-small" id="username-reg" placeholder="用户名">
                  <input type="password" class="input-small" id="password-reg" placeholder="密码">
                  <input type="password" class="input-small" id="password2-reg" placeholder="确认密码">
                  <button type="submit" class="btn">注册</button>
                </form>
              </li>
              <li><a href="#" class="login">登录</a></li>
              <li><a href="#" class="register">注册</a></li>
            </ul><!-- end ul -->
            <p class="navbar-text pull-right">欢迎您,<span id="visitor" <% if(uid != null) %> uid="<%= uid %>"><%= username %></span><input type="button" class="btn btn-danger" id="logout" value="注销"/></p>
            <span class="pull-right label label-success"></span>
            <span class="pull-right label label-warning"></span>
            <span class="pull-right label label-important"></span>
          </div><!--/.nav-collapse -->
        </div>
      </div>
    </div>

    <div class="container-fluid">
      <div class="hero-unit">
        <div class="row-fluid">
          <div class="span2">
            <div class="well sidebar-nav">
              <ul class="nav nav-list" id="room-list">
                <li class="nav-header">
                  <div class="row-fluid">
                    聊天室列表 
                    <a href="#" class="pull-right" data-toggle="tooltip" title="创建聊天室"><i class="icon-plus" data-toggle="modal" data-target="#add-room"></i></a>
                  </div>
                </li>
              </ul>
            </div><!--/.well -->
          </div><!--/span2-->
            <div class="span8">
              <div>
                <ul id="room-tab" class="nav nav-tabs">
                </ul>
                <div class="tab-content" id="tab-content">
                </div>
              </div>
              <div><!--富文本-->
                  <div class="btn-toolbar" data-role="editor-toolbar" data-target="#editor">
                    <div class="btn-group">
                      <a class="btn dropdown-toggle" data-toggle="dropdown" title="Font"><i class="icon-font"></i><b class="caret"></b></a>
                        <ul class="dropdown-menu">
                        </ul>
                      </div>
                    <div class="btn-group">
                      <a class="btn dropdown-toggle" data-toggle="dropdown" title="Font Size"><i class="icon-text-height"></i>&nbsp;<b class="caret"></b></a>
                        <ul class="dropdown-menu">
                        <li><a data-edit="fontSize 5"><font size="5">Huge</font></a></li>
                        <li><a data-edit="fontSize 3"><font size="3">Normal</font></a></li>
                        <li><a data-edit="fontSize 1"><font size="1">Small</font></a></li>
                        </ul>
                    </div>
                    <div class="btn-group">
                      <a class="btn" data-edit="bold" title="Bold (Ctrl/Cmd+B)"><i class="icon-bold"></i></a>
                      <a class="btn" data-edit="italic" title="Italic (Ctrl/Cmd+I)"><i class="icon-italic"></i></a>
                      <!--
                      <a class="btn" data-edit="strikethrough" title="Strikethrough"><i class="icon-strikethrough"></i></a>
                      <a class="btn" data-edit="underline" title="Underline (Ctrl/Cmd+U)"><i class="icon-underline"></i></a>
                      -->
                    </div>
                    <!--
                    <div class="btn-group">
                    
                      <a class="btn" data-edit="insertunorderedlist" title="Bullet list"><i class="icon-list-ul"></i></a>
                      <a class="btn" data-edit="insertorderedlist" title="Number list"><i class="icon-list-ol"></i></a>
                      
                      <a class="btn" data-edit="outdent" title="Reduce indent (Shift+Tab)"><i class="icon-indent-left"></i></a>
                      <a class="btn" data-edit="indent" title="Indent (Tab)"><i class="icon-indent-right"></i></a>
                    </div>

                    <div class="btn-group">
                      <a class="btn" data-edit="justifyleft" title="Align Left (Ctrl/Cmd+L)"><i class="icon-align-left"></i></a>
                      <a class="btn" data-edit="justifycenter" title="Center (Ctrl/Cmd+E)"><i class="icon-align-center"></i></a>
                      <a class="btn" data-edit="justifyright" title="Align Right (Ctrl/Cmd+R)"><i class="icon-align-right"></i></a>
                      <a class="btn" data-edit="justifyfull" title="Justify (Ctrl/Cmd+J)"><i class="icon-align-justify"></i></a>
                    </div>
                    -->
                    <!--
                    <div class="btn-group">
                    <a class="btn dropdown-toggle" data-toggle="dropdown" title="Hyperlink"><i class="icon-link"></i></a>
                      <div class="dropdown-menu input-append">
                        <input class="span2" placeholder="URL" type="text" data-edit="createLink"/>
                        <button class="btn" type="button">Add</button>
                      </div>
                      <a class="btn" data-edit="unlink" title="Remove Hyperlink"><i class="icon-cut"></i></a>
                    </div>
                    -->
                    <div class="btn-group">
                    <!--
                      <a class="btn" data-edit="undo" title="Undo (Ctrl/Cmd+Z)"><i class="icon-undo"></i></a>
                      -->
                      <a class="btn" data-edit="redo" title="Redo (Ctrl/Cmd+Y)"><i class="icon-repeat"></i></a>
                    </div>
                    <input type="text" data-edit="inserttext" id="voiceBtn" x-webkit-speech="">
                  </div><!--/btn-toolbar-->
               
                  <div id="editor">
                    输入消息&hellip;
                  </div>
                  <div class="row-fluid" id="tips">
                    <input class="btn btn-primary pull-right" type="button" id="send" value="发送"/>
                    <span class="pull-right label label-success"></span>
                    <span class="pull-right label label-warning"></span>
                    <span class="pull-right label label-important"></span>
                  </div>
              </div>
            </div><!--/span8-->
            <div class="span2">
              <div class="sidebar-nav well">
                 <ul class="nav nav-list" id="user-list">
                  <li class="nav-header">成员列表</li>
                </ul>
              </div>
            </div><!--/span4-->
      </div><!--/row-->
    </div><!--/div-->

    <!-- 模态框（Modal） -->
    <div class="modal fade" id="add-room" tabindex="-1" role="dialog" aria-labelledby="addroom" aria-hidden="true">
       <div class="modal-dialog">
          <div class="modal-content">
             <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                      &times;
                </button>
                <h4 class="modal-title" id="addroom">
                   创建聊天室
                </h4>
             </div>
               <div class="modal-body">
                <form class="well form-inline" id="form-addroom">
                    <div class="control-group">
                      <input type="text" class="input-xlarge" placeholder="聊天室名称" id="room-name">
                    </div>
                    <div class="control-group">
                      <input type="text" class="input-xlarge" placeholder="公告信息" id="room-info">
                    </div>
                    <div class="control-group">
                      <input type="password" class="input-xlarge" placeholder="密码，如不加密则不要填写" id="room-password">
                    </div>
                </form>
               </div>
               <div class="modal-footer">
                  <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                  <button type="button" class="btn btn-primary" id="form-addroom-submit">创建</button>
               </div> 
          </div><!-- /.modal-content -->
        </div>
      </div><!-- /.modal -->  

      <hr>

      <footer>
        <p>&copy; 在线聊天室 2015</p>
      </footer>

    </div><!--/.fluid-container-->

    <!-- Le javascript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="../assets/js/jquery.js"></script>
    <script src="../assets/js/bootstrap-transition.js"></script>
    <script src="../assets/js/bootstrap-alert.js"></script>
    <script src="../assets/js/bootstrap-modal.js"></script>
    <script src="../assets/js/bootstrap-dropdown.js"></script>
    <script src="../assets/js/bootstrap-scrollspy.js"></script>
    <script src="../assets/js/bootstrap-tab.js"></script>
    <script src="../assets/js/bootstrap-tooltip.js"></script>
    <script src="../assets/js/bootstrap-popover.js"></script>
    <script src="../assets/js/bootstrap-button.js"></script>
    <script src="../assets/js/bootstrap-collapse.js"></script>
    <script src="../assets/js/bootstrap-carousel.js"></script>
    <script src="../assets/js/bootstrap-typeahead.js"></script>
    <script src="../assets/js/jquery.hotkeys.js"></script>
    <script src="../assets/js/bootstrap-wysiwyg.js"></script>

    <script src="../assets/js/application/function.js"></script>
    <script src="../assets/js/application/main.js"></script>

  </body>
</html>