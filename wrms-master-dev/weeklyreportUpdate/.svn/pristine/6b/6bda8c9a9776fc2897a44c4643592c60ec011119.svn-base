<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>パスワードリセット　　</title>
<link href="resources/css/bootstrap.min.css" rel="stylesheet">
<link rel="shortcut icon" href="resources/img/favicon.jpg" />
</head>
<!-- Login Form -->
<body style="background-color: #FAFFBD;">
	<div class="col-xs-offset-5" style="margin-top: 30px;">
		<img alt="GIC Myanmar OJT Project" src="resources/fonts/gic_mm.png">
	</div>
	<div class="col-xs-6 col-xs-offset-3">

		<div id="activationCodeForm" class="panel panel-warning"
			style="margin-top: 30px;">

			<div class="panel-heading">
				<h4>
					<b>パスワードリセット</b>
				</h4>
			</div>
			<div class="panel-body">
				<div>
					<label>GICメールに送信されたアクティベーションコードを入力してください。</label>
				</div>
				<br>
				<div class="col-md-9 col-md-offset-2">
					<s:if test="hasActionErrors()">
						<div class="alert alert-danger alerts" id="error">
							<s:actionerror />
						</div>
					</s:if>
				</div>
				<form name="form" class="form-horizontal" method="post">
					<div class="col-md-7 col-md-offset-3">
						<div class='alert alert-danger' id="Error" style="display: none;"></div>
					</div>
					<div class="form-group">
						<label for="emp_cd" class="control-label col-md-4">社員番号</label>
						<div class="col-md-6">
							<s:if test="%{!hasActionErrors() && emp_cd != null}">
								<input type="text" name="emp_cd" class="form-control"
									id="emp_cd" readonly="readonly" maxlength="8" value="${emp_cd}" />
							</s:if>
							<s:elseif test="%{emp_cd == null}">
								<input type="text" name="emp_cd" class="form-control"
									id="emp_cd" maxlength="8"
									placeholder="Enter login Id (Employee Code)" />
							</s:elseif>
							<s:else>
								<input type="text" name="emp_cd" class="form-control"
									id="emp_cd" maxlength="8" value="${emp_cd}" />
							</s:else>
						</div>
					</div>
					<div class="form-group">
						<label for="atv_code" class="control-label col-xs-4">アクティベーションコード</label>
						<div class="col-xs-6">
							<input type="text" id="atv_code" class="form-control"
								name="atv_code" placeholder="Enter activation code"
								maxlength="6" />
						</div>
					</div>
					<div class="form-group">
						<div class="col-xs-6 col-xs-offset-4">
							<button type="submit" class="btn btn-primary" id="btnNext">次へ</button>
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
		$("#btnNext").click(function() {
			var boo;
			if (!validateform())
				boo = false;
			else
				boo = true;

			if (!boo)
				return false;
			document.form.action = "checkCode";
			document.form.submit();
		});

		function validateform() {	
			$('#error').html("");
			$('#error').hide();
			
			var employee_cd = document.getElementById('emp_cd').value.trim();
			var code = document.getElementById('atv_code').value.trim();

			if (employee_cd == null || employee_cd == ""
					|| employee_cd.length == 0) {
				message = " 『社員番号』 を入力してください。";
				$('#Error').html(
						"<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"
								+ message);
				$('#Error').show();
				return false;
			}

			if (code == null || code == "" || code.length == 0) {
				message = " 『アクティベーションコード』 を入力してください。";
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