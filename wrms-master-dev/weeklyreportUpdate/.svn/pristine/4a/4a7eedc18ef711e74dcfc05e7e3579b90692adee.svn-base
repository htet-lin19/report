<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ログイン / サインアップ</title>
<link href="resources/css/bootstrap.min.css" rel="stylesheet">
<link rel="shortcut icon" href="resources/img/favicon.jpg" />
</head>
<!-- Login Form -->
<body style="background-color: #FAFFBD;">
	<div class="col-xs-offset-5" style="margin-top: 30px;">
		<img alt="GIC Myanmar OJT Project" src="resources/fonts/gic_mm.png">
	</div>
	<div class="col-xs-6 col-xs-offset-3">

		<div id="loginForm" class="panel panel-warning"
			style="margin-top: 30px;">

			<div class="panel-heading">
				<%-- <button class="close" onclick="showSignUp();" data-toggle="tooltip"
					data-placement="top" title="Sign Up">
					サインアップ<span class="glyphicon glyphicon-import" aria-hidden="true"></span>
				</button> --%>
				<h4>
					<b>ログイン</b>
				</h4>
			</div>
			<div class="panel-body">
				<form name="form" class="form-horizontal" method="post">
					<div class="col-md-7 col-md-offset-3">
						<div class='alert alert-danger' id="Error" style="display: none;"></div>
					</div>
					<div class="form-group">
						<label for="emp_cd" class="control-label col-xs-4">社員番号</label>
						<div class="col-xs-6">
							<input type="text" id="emp_cd" class="form-control" name="emp_cd"
								placeholder="Enter login Id (Employee Code)" maxlength="8" autofocus="autofocus"/>
						</div>
					</div>
					<div class="form-group">
						<label for="password" class="control-label col-xs-4">パスワード</label>
						<div class="col-xs-6">
							<input type="password" id="password" class="form-control"
								name="password" placeholder="Enter Password" maxlength="20" />
						</div>
					</div>

					<!-- Reset Password 
					<div class="form-group">
						<a href="ResetPassword" class="control-label col-xs-4">パスワードリセット</a>
					</div>-->

					<div class="form-group">
						<div class="col-xs-6 col-xs-offset-4">
							<button type="submit" class="btn btn-primary" id="btnInsert">ログイン</button>
							<button type="reset" class="btn btn-success">キャンセル</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>

	<!-- jQuery -->
	<script src="resources/js/jquery-2.1.4.min.js"></script>
	<script src="resources/js/validator.min.js"></script>
	<!-- Bootstrap -->
	<script src="resources/js/bootstrap.min.js"></script>
	<script type="text/javascript">
		$(function() {
			$('[data-toggle="tooltip"]').tooltip()
		});

		function showSignUp() {
			$('#loginForm').hide();
			$('#signupForm').show();
		};

		function showLogin() {
			$('#loginForm').show();
			$('#signupForm').hide();
		};
		$("#btnInsert").click(function() {
			var boo;
			if (!validateform())
				boo = false;
			else
				boo = true;

			if (!boo)
				return false;
			document.form.action = "login";
			document.form.submit();

		});

		function validateform() {
			var emp = document.getElementById('emp_cd').value.trim();
			var password = document.getElementById('password').value.trim();

			if (emp == null || emp == "" || emp.length == 0) {
				message = " 『ログイン番号』 を入力してください。";
				$('#Error').html(
						"<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"
								+ message);
				$('#Error').show();
				return false;
			}

			if (password == null || password == "" || password.length == 0) {
				message = " 『パスワード』 を入力してください。";
				$('#Error').html(
						"<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"
								+ message);
				$('#Error').show();
				return false;
			}
			return true;
		}
	</script>
</body>
</html>