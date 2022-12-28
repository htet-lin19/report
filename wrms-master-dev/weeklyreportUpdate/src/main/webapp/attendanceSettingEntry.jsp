<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/struts-bootstrap-tags" prefix="sj"%>
<!DOCTYPE html>
<html>
<head>
<sj:head />
<meta charset="UTF-8">
<title>所定勤務時間設定登録</title>
<script src="resources/js/timecalculateSetting.js"></script>
<link href="resources/css/bootstrap.min.css" rel="stylesheet">
<link href="resources/css/jquery-ui.css" rel="stylesheet">
<link href="resources/css/jquery-clockpicker.min.css" rel="stylesheet">
<link href="resources/css/navbar-fixed-top.css" rel="stylesheet">
<link rel="shortcut icon" href="resources/img/favicon.jpg" />
<link href="resources/css/timepicki.css" rel="stylesheet">
</head>
<body onload="calculateTime()">
	<!-- Sessiontimeout Message -->
	<%
		session = request.getSession(false);
		if (session.getAttribute("ID") == null)
			response.sendRedirect("sessionTimeOut.jsp");
	%>
	<!--End of Sessiontimeout Message -->
	<div><jsp:include page="menu.jsp"></jsp:include></div>
	<div class="col-md-6 col-md-offset-3">

		<!-- DuplicateMessage -->
		<div class="panel panel-warning">
			<div class="panel-heading">所定勤務時間設定登録</div>
			<div class="panel-body">
				<div class="col-md-5 col-md-offset-4">
					<s:if test="hasActionErrors()">
						<div class="alert alert-danger alerts" id="error">
							<s:actionerror />
						</div>
					</s:if>
				</div>
				<div class="col-md-5 col-md-offset-4">
					<s:if test="hasActionMessages()">
						<div class="alert alert-success alerts" id="msg">
							<s:actionmessage />
						</div>
					</s:if>
				</div>
				<!--End of DuplicateMessage -->
				<form name="attendance" class="form-horizontal" method="post">
					<div class="col-md-4 col-md-offset-3">
						<div class='alert alert-danger' id="Error" style="display: none;"></div>
					</div>
					<s:textfield name="id" type="hidden" />
					<!-- First half -->
					<div class="col-md-10 col-md-offset-1">
						<div class="form-group">
							<label for="emp_cd" class="control-label col-md-4">社員番号</label>
							<div class="col-md-6">
								<input class="form-control" disabled="disabled" id="emp_cd"
									value="<%out.print(session.getAttribute("ID"));%>" /> <input
									class="form-control" name="attendance.emp_cd" id="emp_cd"
									type="hidden"
									value="<%out.print(session.getAttribute("ID"));%>" />
							</div>
						</div>

						<div class="form-group">
							<label for="start_time" class="control-label col-md-4"><font
								color="red">*</font>開始時刻</label>
							<div class="col-md-6">
								<input name="attendance.start_time"
									class="time_element form-control" id="start_time" maxlength="7"
									onmouseout="calculateTime()" value="${attendance.start_time}" />
							</div>
						</div>

						<div class="form-group">
							<label for="end_time" class="control-label col-md-4"><font
								color="red">*</font>終了時刻</label>
							<div class="col-md-6" onchange="calculateTime()">
								<input name="attendance.end_time"
									class="time_element form-control" id="end_time" maxlength="7"
									onmouseout="calculateTime()" value="${attendance.end_time}" />
							</div>
						</div>

						<div class="form-group">
							<label for="break_time" class="control-label col-md-4"><font
								color="red">*</font>休憩時間</label>
							<div class="col-md-6">
								<input name="attendance.break_time"
									class="time_element form-control" id="break_time" maxlength="7"
									onmouseout="calculateTime()" value="${attendance.break_time}" />
							</div>
						</div>

						<div class="form-group">
							<label for="work_hour" class="control-label col-md-4">作業時間</label>
							<div class="col-md-6">
								<input name="attendance.work_hour" readonly="readonly"
									class="time_element form-control" id="work_hour" maxlength="7"
									onmouseout="calculateTime()" value="${attendance.work_hour}" />
							</div>
						</div>
						<%-- 2020/10/06 GICM KZP シフト勤務　 対応 Start --%>
						<div class="form-group">
							<label for="shift_work" class="control-label col-md-4">シフト勤務</label>
							<div class="col-md-6">
								<input name="attendance.shift_work" type="checkbox" class="form-control"
									id="shift_work" style="width: 20px; height: 20px"
									value="${attendance.shift_work}" />
							</div>
						</div>
						<%-- //2020/10/06 GICM KZP シフト勤務　 対応 End --%>
						<s:textfield type="hidden" name="created_date" />
						<s:textfield type="hidden" name="modified_date" />
					</div>
					<div class="form-group">
						<div class="col-md-7 col-md-offset-5">
							<button onmouseover="calculateTime()" type="submit"
								class="btn btn-success" id="btnInsert">登録</button>
							<%-- 2020/10/06 GICM KZP シフト勤務　 対応 Start --%>
							<%--<button type="reset" class="btn btn-danger" id="btnClear">クリア</button>--%>
							<button type="reset" class="btn btn-danger"
							 onclick="getElementById('shift_work').removeAttr('checked');"
							 id="btnClear">クリア</button>
							 <%-- 2020/10/06 GICM KZP シフト勤務　 対応 End --%>
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
	<script src="resources/js/jquery-clockpicker.min.js"></script>
	<script src="resources/js/timepicker.js"></script>
	<script>
		$(document).ready(function() {

			$('#start_time').timepicki({
				start_time : [ "09", "00" ],
				show_meridian : false,
				min_hour_value : 0,
				max_hour_value : 23,
				min_min_value : 00,
				step_size_minutes : 5,
				overflow_minutes : false,
				increase_direction : 'up',
				disabled_keyboard_mobile : true,

			});

			$('#end_time').timepicki({
				start_time : [ "18", "00" ],
				show_meridian : false,
				min_hour_value : 0,
				max_hour_value : 23,
				min_min_value : 00,
				step_size_minutes : 5,
				overflow_minutes : false,
				increase_direction : 'up',
				disabled_keyboard_mobile : true,
			});
			$('#break_time').timepicki({
				start_time : [ "00", "00" ],
				show_meridian : false,
				min_hour_value : 0,
				max_hour_value : 23,
				min_min_value : 00,
				step_size_minutes : 5,
				overflow_minutes : false,
				increase_direction : 'up',
				disabled_keyboard_mobile : true,
			});
			/* 	$('#work_hour').timepicki({
					start_time : [ "00", "00" ],
					show_meridian : false,
					min_hour_value : 0,
					max_hour_value : 23,
					min_min_value : 00,
					step_size_minutes : 5,
					overflow_minutes : false,
					increase_direction : 'up',
					disabled_keyboard_mobile : true,
				}); */
			$("#btnInsert").click(function() {
				var boo;
				if (!validateform())
					boo = false;
				else
					boo = true;

				if (!boo)
					return false;

				if (!validateform())
					boo = false;
				else
					boo = true;

				if (!boo)
					return false;
				document.attendance.action = "AttendanceSettingInsert";
				document.attendance.submit();
			});
			$("#btnClear").click(function() {
				document.attendance.action = "AttendanceSettingClear";
				document.attendance.submit();
			});
			
			//2020/10/06 GICM KZP シフト勤務　 対応 Start
			if($("#shift_work").val() == 1){
				$("#shift_work").attr('checked', true);
			}else{
				$("#shift_work").attr('checked', false);
				$("#shift_work").val(0);
			}
			
			$('input[type="checkbox"]').change(function() { 
			    if($(this).prop('checked')){
			    	$("#shift_work").val(1);
			    }else{
			    	$("#shift_work").val(0);
			    }
			});
			//2020/10/06 GICM KZP シフト勤務　 対応 End
		});

		function validateform() {
			var st = document.getElementById('start_time').value;
			var en = document.getElementById('end_time').value;
			var breaktime = document.getElementById('break_time').value.trim();
			var workhour = document.getElementById('work_hour').value.trim();
			if (st == null || st == "") {
				message = " 『開始時刻』 を入力してください。";
				$('#Error').html(
						"<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"
								+ message);
				$('#Error').show();
				return false;
			}
			if (en == null || en == "") {
				message = " 『終了時刻』 を入力してください。";
				$('#Error').html(
						"<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"
								+ message);
				$('#Error').show();
				return false;
			}
			if (st > en) {
				message = " 『終了時刻』は『開始時刻』より早いです。";
				$('#Error').html(
						"<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"
								+ message);
				$('#Error').show();
				return false;
			}
			if (breaktime == null || breaktime == "") {
				message = " 『休憩時間』 を入力してください。";
				$('#Error').html(
						"<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"
								+ message);
				$('#Error').show();
				return false;
			}
			if (workhour == null || workhour == "") {
				message = " 『作業時間』 を入力してください.";
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
