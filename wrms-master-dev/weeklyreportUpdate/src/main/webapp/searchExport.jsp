<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/struts-bootstrap-tags" prefix="sj"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<!DOCTYPE html>
<html>
<head>
<sj:head />
<meta charset="UTF-8">
<title>勤務時間情報印刷</title>
<link href="resources/css/bootstrap.min.css" rel="stylesheet">
<link href="resources/css/jquery-ui.css" rel="stylesheet">
<link href="resources/css/navbar-fixed-top.css" rel="stylesheet">
<link rel="shortcut icon" href="resources/img/favicon.jpg" />
</head>
<style type="text/css">
#start_date {
	display: block;
	width: 100%;
	height: 34px;
	padding: 6px 12px;
	font-size: 14px;
	line-height: 1.42857143;
	color: #555;
	background-color: #fff;
	background-image: none;
	border: 1px solid #ccc;
	border-radius: 4px;
}
</style>
<body>
	<%
		session = request.getSession(false);
		if (session.getAttribute("ID") == null)
			response.sendRedirect("sessionTimeOut.jsp");
	%>

	<div><jsp:include page="menu.jsp"></jsp:include></div>

	<div class="col-md-6 col-md-offset-3">
		<div class="panel panel-primary">
			<div class="panel-heading">勤務時間情報印刷</div>
			<div class="panel-body">
				<div class="row">
					<div class="col-md-6 col-md-offset-3">
						<div class='alert alert-danger' id="Error" style="display: none;">
							<s:actionerror />
						</div>
					</div>
				</div>
				<div class="row">
					<s:if test="hasActionErrors()">
						<div class="col-md-6 col-md-offset-3">
							<div class='alert alert-danger' id="Error1">
								<s:actionerror />
							</div>
						</div>
					</s:if>
				</div>

				<form name="attendanceListFrm" class="form-horizontal" method="post"
					data-toggle="validator">
					<div class="form-group">
						<label for="emp_cd" class="control-label col-md-3">社員番号</label>
						<div class="col-md-6">
							<c:if test="${attendanceListFrm.disable_empcd eq 'disabled'}">
								<input type='text' class="form-control"
									name="attendanceListFrm.emp_cd" id="emp_cd" maxlength="8"
									value="${attendanceListFrm.emp_cd}" readonly="readonly" />
							</c:if>
							<c:if test="${attendanceListFrm.disable_empcd eq ''}">
								<input type='text' class="form-control"
									name="attendanceListFrm.emp_cd" id="emp_cd" maxlength="8"
									value="${attendanceListFrm.emp_cd}" />
							</c:if>
						</div>
					</div>
					<div class="form-group">
						<label for="start_date" class="control-label col-md-3"><font
							color="red">*</font>対象年月</label>
						<div class="col-md-6">
							<input type="text" id="start_date"
								name="attendanceListFrm.start_date" class="monthPicker" readonly="readonly" />
						</div>
					</div>
					<div class="col-md-6 col-md-offset-3">
						<button type="button" class="btn btn-success" onclick="Excel();"
							id="btnExcel">Excel出し</button>
						<button type="reset" class="btn btn-warning" onclick="Clear();"
							id="btnClear">キャンセル</button>
					</div>
				</form>
			</div>
		</div>
	</div>
	<!-- Data table Start -->
	<div class="col-md-6 col-md-offset-3">
		<display:table name="AttList" pagesize="5" cellpadding="10px;" id="m"
			cellspacing="10px;" class="table table-hover table-bordered"
			requestURI="">
			<display:column property="emp_cd" title="社員番号" />
			<display:column property="choose_date" title="日付" />
			<display:column property="work_hour" title="作業時間" />
			<display:column property="overtime" title="時間外勤務" />
			<display:column property="midnight_overtime" title="深夜時間外勤務" />
		</display:table>
	</div>

	<script src="resources/js/jquery-2.1.4.min.js"></script>
	<script src="resources/js/validator.min.js"></script>
	<script src="resources/js/bootstrap.min.js"></script>
	<script src="resources/js/jquery-ui.min.js"></script>
	<script type="text/javascript">
		$(document)
				.ready(
						function() {
							$(document)
									.ready(
											function() {
												$(".monthPicker")
														.datepicker(
																{
																	dateFormat : 'yy-mm',
																	changeMonth : true,
																	changeYear : true,
																	showButtonPanel : true,

																	onClose : function(
																			dateText,
																			inst) {
																		var month = $(
																				"#ui-datepicker-div .ui-datepicker-month :selected")
																				.val();
																		var year = $(
																				"#ui-datepicker-div .ui-datepicker-year :selected")
																				.val();
																		$(this)
																				.val(
																						$.datepicker
																								.formatDate(
																										'yy-mm',
																										new Date(
																												year,
																												month,
																												1)));
																	}
																});

												$(".monthPicker")
														.focus(
																function() {
																	$(
																			".ui-datepicker-calendar")
																			.hide();
																	$(
																			"#ui-datepicker-div")
																			.position(
																					{
																						my : "center top",
																						at : "center bottom",
																						of : $(this)
																					});
																});

											});
						});

		function Excel() {
			ClearMsg();
			var emp_cd = document.getElementById('emp_cd').value;
			var start_date = document.getElementById('start_date').value;
			if (emp_cd == null || emp_cd == "") {
				message = " 『社員番号』 を入力してください。";
				$('#Error').html(
						"<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"
								+ message);
				$('#Error').show();
				return false;
			}
			if (start_date == null || start_date == "") {
				message = " 『対象年月』 を入力してください。";
				$('#Error').html(
						"<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"
								+ message);
				$('#Error').show();
				return false;
			}
			document.attendanceListFrm.action = "AttendanceExportExcel";
			document.attendanceListFrm.submit();
		}

		function Clear() {
			ClearMsg();
			document.getElementById('emp_cd').value = "";
			document.getElementById('start_date').value = "";
		}

		function ClearMsg() {
			$('#Error').html("");
			$('#Error').hide();

			$('#Error1').html("");
			$('#Error1').hide();
		}
	</script>
</body>
</html>