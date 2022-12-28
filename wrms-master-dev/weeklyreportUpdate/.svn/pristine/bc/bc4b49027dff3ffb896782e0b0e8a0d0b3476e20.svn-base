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
<body style="background-color: #FAFFBD;">
	<div class="col-xs-offset-5" style="margin-top: 30px;">
		<img alt="GIC Myanmar OJT Project" src="resources/fonts/gic_mm.png">
	</div>

	<div class="col-xs-6 col-xs-offset-3">
		<!-- Duplicate Message -->
		<div id="changePasswordForm" class="panel panel-warning"
			style="margin-top: 30px;">
			<div class="panel-heading">
				<h4>
					<b>パスワード更新</b>
				</h4>
			</div>
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

				<form name="form" class="form-horizontal" method="post">
					<div class="col-md-7 col-md-offset-3">
						<div class='alert alert-danger' id="Error" style="display: none;"></div>
					</div>
					<s:textfield name="id" type="hidden" />

					<div class="form-group">
						<label for="emp_cd1" class="control-label col-xs-4">社員番号</label>
						<div class="col-xs-6">
							<input class="form-control" name="emp_cd" readonly="readonly"
								id="emp_cd" value="${emp_cd}" />
						</div>
					</div>

					<div class="form-group">
						<label for="inputPassword" class="control-label col-xs-4">新しいパスワード</label>
						<div class="col-xs-6">
							<input type="password" id="inputPassword" name="inputPassword"
								class="form-control" maxlength="20" placeholder="Enter new Password" />
						</div>
					</div>

					<div class="form-group">
						<label for="repassword" class="control-label col-xs-4">パスワード再入カ</label>
						<div class="col-xs-6">
							<input type="password" id="repassword" class="form-control"
								maxlength="20" placeholder="Re-enter new Password" />
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

			document.form.action = "changePassword";
			document.form.submit();
		});

		function validateform() {
			$('#error').html("");
			$('#error').hide();

			var password = document.getElementById('inputPassword').value
					.trim();
			var repass = document.getElementById('repassword').value.trim();

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
				message = " 『パスワード再入カ』 は間違っています。";
				$('#Error').html(
						"<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"
								+ message);
				$('#Error').show();
				return false;
			} else {
				return true;
			}
		}
	</script>
</body>