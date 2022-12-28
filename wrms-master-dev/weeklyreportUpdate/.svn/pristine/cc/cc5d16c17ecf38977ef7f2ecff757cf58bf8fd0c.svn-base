<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>パスワード更新</title>
<link href="resources/css/bootstrap.min.css" rel="stylesheet">
<link href="resources/css/jquery-ui.css" rel="stylesheet">
<link href="resources/css/navbar-fixed-top.css" rel="stylesheet">
<link rel="shortcut icon" href="resources/img/favicon.jpg" />
</head>
<body onload="Password('${loginForm.password}')">
	<!-- Sessiontimeout Message -->
	<%
		session = request.getSession(false);
		if (session.getAttribute("ID") == null)
			response.sendRedirect("sessionTimeOut.jsp");
	%>
	<!-- End of Sessiontimeout Message -->

	<div><jsp:include page="menu.jsp"></jsp:include></div>

	<div class="col-xs-6 col-xs-offset-3">
		<!-- Duplicate Message -->
		<div class="panel panel-default" style="margin-top: 40px;">
			<div class="panel-heading">パスワード更新</div>
			<div class="panel-body">

				<div class="col-md-9 col-md-offset-2">
					<s:if test="hasActionErrors()">
						<div class="alert alert-danger alerts" id="error">
							<s:actionerror />
						</div>
					</s:if>
				</div>
				<div class="col-md-9 col-md-offset-2">
					<s:if test="hasActionMessages()">
						<div class="alert alert-success alerts" id="msg">
							<s:actionmessage />
						</div>
					</s:if>
				</div>

				<form name="loginForm" class="form-horizontal" method="post">
					<div class="col-md-7 col-md-offset-2">
						<div class='alert alert-danger' id="Error" style="display: none;"></div>
					</div>
					<s:textfield name="id" type="hidden" />

					<div class="form-group">
						<label for="emp_cd1" class="control-label col-xs-4">社員番号</label>
						<div class="col-xs-6">
							<input class="form-control" name="loginForm.emp_cd"
								readonly="readonly" id="emp_cd"
								value="<%out.print(session.getAttribute("ID"));%>" />
						</div>
					</div>

					<div class="form-group">
						<label for="password1" class="control-label col-xs-4"><font
							color="red">*</font>古いパスワード</label>
						<div class="col-xs-6">
							<input type="password" id="password" name="loginForm.password"
								class="form-control" value="${loginForm.password}"
								data-maxlength="20" />
						</div>
					</div>

					<div class="form-group">
						<label for="inputpassword1" class="control-label col-xs-4"><font
							color="red">*</font>新しいパスワード</label>
						<div class="col-xs-6">
							<input type="password" id="inputpassword"
								name="loginForm.inputpassword" class="form-control"
								value="${loginForm.inputpassword}" data-maxlength="20" />
						</div>
					</div>

					<div class="form-group">
						<label for="repassword" class="control-label col-xs-4"><font
							color="red">*</font>パスワード再入カ</label>
						<div class="col-xs-6">
							<input type="password" id="repassword" class="form-control"
								data-maxlength="20">
						</div>
					</div>

					<div class="form-group">
						<div class="col-xs-6 col-xs-offset-4">
							<button type="submit" class="btn btn-primary" id="btnInsert">更新</button>
							<button type="reset" class="btn btn-success">キャンセル</button>
						</div>
					</div>

				</form>

			</div>
		</div>
	</div>


	<script src="resources/js/jquery-2.1.4.min.js"></script>
	<script src="resources/js/validator.min.js"></script>
	<script src="resources/js/bootstrap.min.js"></script>
	<script src="resources/js/jquery-ui.min.js"></script>
	<script>
		$("#btnClear").click(function() {
			document.form.action = "ClearSignup";
			document.form.submit();
		});
		$("#btnInsert").click(function() {
			var boo;
			if (!validateform())
				boo = false;
			else
				boo = true;

			if (!boo)
				return false;

			document.loginForm.action = "EmpSignUp";
			document.loginForm.submit();
		});

		function validateform() {
			var pass = document.getElementById('password').value.trim();
			var password = document.getElementById('inputpassword').value
					.trim();
			var repass = document.getElementById('repassword').value.trim();

			if (pass == null || pass == "" || pass.length == 0) {
				message = " 『古いパスワード』 を入力してください。";
				$('#Error').html(
						"<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"
								+ message);
				$('#Error').show();
				return false;
			}

			if (password == null || password == "" || password.length == 0) {
				message = " 『新しいパスワード』 を入力してください。";
				$('#Error').html(
						"<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"
								+ message);
				$('#Error').show();
				return false;
			}

			if (repass == null || repass == "" || repass.length == 0) {
				message = " 『パスワード再入カ』 を入力してください。";
				$('#Error').html(
						"<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"
								+ message);
				$('#Error').show();
				return false;
			}

			if (repass != password) {
				message = "新しいパスワードとパスワード再入カが一致していません。";
				$('#Error').html(	
						"<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"
								+ message);
				$('#Error').show();
				return false;
			} else {
				return true;
			}
		}

		function Password(password) {
			document.getElementById('password').value = password;
		}
	</script>

</body>