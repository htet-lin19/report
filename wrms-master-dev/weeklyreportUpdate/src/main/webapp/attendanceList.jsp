<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@ taglib uri="/struts-bootstrap-tags" prefix="sj"%>
<!DOCTYPE html>
<html>
<head>
<sj:head />
<meta charset="UTF-8">
<title>勤務時間一覧</title>
<link href="resources/css/bootstrap.min.css" rel="stylesheet">
<link href="resources/css/mdl_modified.css" rel="stylesheet">
<link href="resources/css/mdl2_modified.css" rel="stylesheet">
<link href="resources/css/jquery-ui.css" rel="stylesheet">
<link href="resources/css/jquery-clockpicker.min.css" rel="stylesheet">
<link href="resources/css/navbar-fixed-top.css" rel="stylesheet">
<link rel="shortcut icon" href="resources/img/favicon.jpg" />
<link href="resources/css/datepicker.css" rel="stylesheet">
<link href="resources/css/fixedColumns.dataTables.min.css" rel="stylesheet">
<link href="resources/css/timepicki.css" rel="stylesheet"
	type="text/css">
<link href="resources/css/jquery.dataTables.min.css" rel="stylesheet">
<!-- table style/modal style -->
<style>
.mdl-body {
	margin-top: 20px;
	height: 480px;
	overflow-y: auto;
}

.mdl:target+.mdl-dialog {
	top: 5%;
}

div.dataTables_wrapper {
	width: 1300px;
	margin: 0 auto;
}
</style>
<link rel="shortcut icon" href="favicon.jpg"/>
</head>
<body onload="hideDiv()">
	<!-- Sessiontimeout Message -->
	<%
		session = request.getSession(false);
		if (session.getAttribute("ID") == null)
			response.sendRedirect("sessionTimeOut.jsp");
		
	%>
	<!--End of Sessiontimeout Message -->

	<form name="attendanceListForm" method="post" autocomplete="off">
		<div><jsp:include page="menu.jsp"></jsp:include></div>

		<!-- Search Form -->
		<div class="col-md-10 col-md-offset-1">
			<div class="panel panel-warning">
				<div class="panel-heading">勤務時間一覧</div>
				<div class="row" style="margin-top: 10px;">
					<div class="col-md-6 col-md-offset-3">	
					<s:if test="hasActionMessages()">
				   		<div class="alert alert-success" id="msg">
				      		<s:actionmessage/>
				   		</div>
					</s:if>		
				</div>
				</div>
				<div class="panel-body">
					<div class="row">
						<div class="col-md-6" style="margin-top: 5px;">
							<div class="form-group">
								<label for="emp_cd" class="control-label col-md-3">社員番号</label>
								<div class="col-md-6">
									<c:if test="${attendanceListFrm.disable_empcd eq 'disabled'}">
										<input type='text' class="form-control" name="attendanceListFrm.emp_cd" id="emp_cd" maxlength="8" value="${attendanceListFrm.emp_cd}" readonly="readonly"/>
									</c:if>
									<c:if test="${attendanceListFrm.disable_empcd eq ''}">
										<input type='text' class="form-control" name="attendanceListFrm.emp_cd" id="emp_cd" maxlength="8" value="${attendanceListFrm.emp_cd}" />
									</c:if>
								</div>
							</div>
						</div>
						<div class="col-md-6" style="margin-top: 5px;">
							<div class="form-group">
								<label for="yearAndMonth" class="control-label col-md-3">期間</label>
								<div class="col-md-4">
									<input type='text' class="form-control" name="attendanceListFrm.start_date" id="start_date" maxlength="10" value="${attendanceListFrm.start_date}" />
								</div>
								<label for="yearAndMonth" class="control-label col-md-1">~</label>
								<div class="col-md-4">
									<input type='text' class="form-control" name="attendanceListFrm.end_date" id="end_date" maxlength="10" value="${attendanceListFrm.end_date}" />
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6" style="margin-top: 5px;">
							<div class="form-group">
								<label for="emp_cd" class="control-label col-md-3">社員名</label>
								<div class="col-md-6">
									<c:if test="${attendanceListFrm.disable_empname eq 'disabled'}">
										<input type='text' class="form-control" name="attendanceListFrm.emp_name" id="emp_name" maxlength="20" value="${attendanceListFrm.emp_name}" readonly="readonly"/>
									</c:if>
									<c:if test="${attendanceListFrm.disable_empname eq ''}">
										<input type='text' class="form-control" name="attendanceListFrm.emp_name" id="emp_name" maxlength="20" value="${attendanceListFrm.emp_name}" />
									</c:if>
								</div>
							</div>
						</div>
						<div class="" style="margin-top: 5px;">
							<button type="submit" class="btn btn-success" id="btn_search">検索</button>
							<button type="button" class="btn btn-danger" id="btn_clear">キャンセル</button>
						</div>
						</div>
					
				</div>
			</div>
		</div>
		<!--End of Search Form -->

		<!-- Data Table -->
		<div class="container-fluid" id="panel-body">
			<div class="col-md-12">
				<table id="att-list" class="stripe row-border cell-border order-column"
					style="width: 180%; table-layout: fixed; word-wrap: break-word;">
					<thead>
						<tr>
							<th style="text-align: center;">社員番号-社員名</th>
							<!-- <th style="text-align: center;">社員名</th> -->
							<th style="text-align: center; width: 2%;">日付</th>
							<th style="text-align: center; width: 6%;">休日</th>
							<th style="text-align: center; width: 6%;">代休取得該当日</th>
							<th style="text-align: center; width: 5%;">開始時刻</th>
							<th style="text-align: center; width: 5%;">終了時刻</th>
							<th style="text-align: center; width: 5%;">休憩時間</th>
							<th style="text-align: center; width: 5%;">勤務時間</th>
							<th style="text-align: center; width: 5%;">法定時間外</th>
							<th style="text-align: center; width: 8%;">法定休日</th>
							<th style="text-align: center;">深夜早朝</th>
							<th style="text-align: center;">その他</th>
							<th style="text-align: center;">備考</th>
							<!-- <th style="text-align: center;" class="col-md-1">更新</th> -->
							<th style="text-align: center;">更新/削除</th>
						</tr>
					</thead>

					<c:forEach var="attendacneList" items="${attendanceListFrm.attendanceListDetail}" varStatus="status">
						<tr style="text-align: right;" id="${attendacneList.id}">
							<%-- <td style="text-align: left;vertical-align: text-top;" id="list_emp_cd${attendacneList.id}"><c:out
									value="${attendacneList.emp_cd}" /></td> --%>
							<td style="text-align: left;vertical-align: text-top;" id="list_emp_cd${attendacneList.id}">
							<c:out value="${attendacneList.emp_cd}" />          <br/>
							<c:out value="${attendacneList.emp_name}" /></td>
							<td style="text-align: right;vertical-align: text-top;" id="list_choose_date${attendacneList.id}"><c:out
									value="${attendacneList.choose_date}" /></td>
							<td style="text-align: right;vertical-align: text-top;" id="list_day${attendacneList.id}">
								<c:set var="day" value="${attendacneList.day}"/>
								<c:set var="cl" value="${attendacneList.compensatory_leave}"/>
								<c:if test="${day == 1}"><c:out value="全休 "/></c:if>
								<c:if test="${day == 2}"><c:out value="午前半休" /></c:if>
								<c:if test="${day == 3}"><c:out value="午後半休" /></c:if>
								<c:if test="${day == 4}"><c:out value="特別休暇" /></c:if>
								<c:if test="${day != 0 && cl != 0 && day != null && cl != null}"><c:out value="," /></c:if>
								<c:if test="${cl == 1}"><c:out value="代休" /></c:if>
							</td>
							<td style="text-align: right;vertical-align: text-top;" id="list_compensatory_date${attendacneList.id}"><c:out
									value="${attendacneList.compensatory_date}" /></td>
							<td style="text-align: right;vertical-align: text-top;" id="list_start_time${attendacneList.id}"><c:out
									value="${attendacneList.start_time}" /></td>
							<td style="text-align: right;vertical-align: text-top;" id="list_end_time${attendacneList.id}"><c:out
									value="${attendacneList.end_time}" /></td>
							<td style="text-align: right;vertical-align: text-top;" id="list_break_time${attendacneList.id}"><c:out
									value="${attendacneList.break_time}" /></td>
							<td style="text-align: right;vertical-align: text-top;" id="list_work_hour${attendacneList.id}"><c:out
									value="${attendacneList.work_hour}" /></td>
							<td style="text-align: right;vertical-align: text-top;" id="list_overtime${attendacneList.id}"><c:out
									value="${attendacneList.overtime}" /></td>
							<td style="text-align: right;vertical-align: text-top;" id="list_overtime_sunday${attendacneList.id}"><c:out
									value="${attendacneList.overtime_sunday}" /></td>
							<td style="text-align: right;vertical-align: text-top;" id="list_midnight_overtime${attendacneList.id}"><c:out
									value="${attendacneList.midnight_overtime}" /></td>
							<td style="text-align: right;vertical-align: text-top;" id="list_compensatory_comment${attendacneList.id}"><c:out
									value="${attendacneList.compensatory_comment}" /></td>
							<td style="text-align: left;vertical-align: text-top;" id="list_task_description${attendacneList.id}"><c:out
									value="${attendacneList.task_description}" /></td>
							<%-- <td style="text-align: center;vertical-align: text-top;" ><a href="javascript:editAttendance(${attendacneList.id});">
								<span class="glyphicon glyphicon-pencil"></span></a></td> --%>
							<td style="text-align: center;vertical-align: text-top;">
								<c:if test="${attendacneList.position eq '2' && attendanceListFrm.disabled_glaction eq ''}">
								<a href="javascript:editAttendance(${attendacneList.id});">
									<span class="glyphicon glyphicon-pencil"></span></a>
								<a href="javascript:deleteAttendance(${attendacneList.id});">
									<span class="glyphicon glyphicon-trash"></span></a>
								</c:if>
								<c:if test="${attendanceListFrm.disabled_glaction eq 'disabled'}">
								<a href="javascript:editAttendance(${attendacneList.id});">
									<span class="glyphicon glyphicon-pencil"></span></a>
								<a href="javascript:deleteAttendance(${attendacneList.id});">
									<span class="glyphicon glyphicon-trash"></span></a>
								</c:if>
							</td>
						</tr>
					</c:forEach>
				</table>
			</div>
		</div>
		
		<!-- for edit page -->
		<input type="hidden" name="hiddenId" id="hiddenId" />
		<input type="hidden" name="pageNo" id="pageNo" />
		<s:hidden name="pageNo" id="pageNo" /> 
		<s:hidden name="attendanceListFrm.button_event" value="search"></s:hidden>
		<s:hidden id="search" name="attendanceListFrm.search"></s:hidden>
		<!-- Delete Modal -->
		<a href="#" class="mdl2" id="mdl_delete" aria-hidden="true"></a>
		<div class="mdl2-dialog">
			<div class="mdl2-header">
				<h2>時間削除</h2>
				<a href="#" class="btn-close" aria-hidden="true">×</a>
			</div>
			<div class="mdl2-body">
				<div class="form-group">
					<label for="delete_id" class="control-label">削除したいですか。</label> 
					<s:hidden id="delete_id" name="attendanceListFrm.delete_id"></s:hidden>
				</div>
			</div>
			<div class="mdl2-footer">
				<button type="submit" class="btn btn-danger" id="btn_delete">削除</button>
				<button type="button" class="btn btn-warning" OnClick="closemdl_delete()">キャンセル</button>
			</div>
		</div>
		<!-- End of Data Table -->
		</form>
		<!-- Edit Modal -->
		<a href="#" class="mdl" id="mdl_edit" aria-hidden="true"></a>
		<div class="mdl-dialog">
			<div class="mdl-header">
				<h2>時間更新</h2>
				<a href="#" class="btn-close" aria-hidden="true">×</a>
				<div class='alert alert-danger' id="Error" style="display: none;"></div>
			</div>
			<div class="mdl-body">
				<s:form class="form-horizontal" name="attendanceListEidtFrm" method="post">
					<div class="form-group">
						<s:hidden name="attendanceListEidtFrm.id" id="edit_id"></s:hidden>
						<input type="hidden" class="form-control" name="attendanceListEidtFrm.id" id="edit_id" disabled />
					</div>
					<div class="form-group">
						<label for="emp_cd" class="control-label"><font color="red">*</font>社員番号</label>
						<input type="text" class="form-control" name="attendanceListEidtFrm.emp_cd" id="edit_emp_cd" disabled />
					</div>
					<div class="form-group">
						<label for="choose_date" class="control-label"><font color="red">*</font>日付</label> <input
							type="text" class="form-control" name="attendanceListEidtFrm.choose_date" id="edit_choose_date"
							maxlength="10" value="">
					</div>
					<div class="form-group">
						<label for="day" class="control-label">休日</label> <input
							type="checkbox" class="form-control" name="attendanceListEidtFrm.day" id="edit_day"
							style="width: 10px; height: 50px; transform: scale(1.5);" />
					</div>
					<div class="form-group">
						<label for="start_time" class="control-label"><font color="red">*</font>開始時刻</label> <input
							type="text" class="form-control" name="attendanceListEidtFrm.start_time" id="edit_start_time"
							maxlength="7" value="">
					</div>
					<div class="form-group">
						<label for="end_time" class="control-label"><font color="red">*</font>終了時刻</label> <input
							type="text" class="form-control" name="attendanceListEidtFrm.end_time" id="edit_end_time"
							maxlength="7" value="">
					</div>
					<div class="form-group">
						<label for="break_time" class="control-label"><font color="red">*</font>休憩時間</label>  <input
							type="text" class="form-control" name="attendanceListEidtFrm.break_time" id="edit_break_time"
							maxlength="7" value="">
					</div>
					<div class="form-group">
						<label for="work_hour" class="control-label"><font color="red">*</font>作業時間</label> <input
							type="text" class="form-control" name="attendanceListEidtFrm.work_hour" id="edit_work_hour"
							maxlength="7" value="">
					</div>
					<div class="form-group">
						<label for="overtime" class="control-label">勤務時間</label> <input
							type="text" class="form-control" name="attendanceListEidtFrm.overtime" id="edit_overtime"
							maxlength="7" value="">
					</div>
					<div class="form-group">
						<label for="midnight_overtime" class="control-label">深夜勤務時間</label> <input
							type="text" class="form-control" name="attendanceListEidtFrm.midnight_overtime" id="edit_midnight_overtime"
							maxlength="7" value="">
					</div>
					<div class="form-group">
						<label for="compensatory_leave_hour" class="control-label">代休時間</label> <input
							type="text" class="form-control" name="attendanceListEidtFrm.compensatory_leave_hour" id="edit_compensatory_leave_hour"
							maxlength="7" value="">
					</div>
					<div class="form-group">
						<label for="releaving_leave_hour" class="control-label">代休勤務</label> <input
							type="text" class="form-control" name="attendanceListEidtFrm.releaving_leave_hour" id="edit_releaving_leave_hour"
							maxlength="7" value="">
					</div>
					<div class="form-group">
						<label for="midnight_compensatory_leave_hour" class="control-label">深夜代休勤務</label> <input
							type="text" class="form-control" name="attendanceListEidtFrm.midnight_compensatory_leave_hour" id="edit_midnight_compensatory_leave_hour"
							maxlength="7" value="">
					</div>
					<div class="form-group">
						<label for="attendanceListEidtFrm.task_description" class="control-label">備考</label> <input
							type="text" class="form-control" id="edit_description" name="attendanceListEidtFrm.task_description"
							maxlength="100" value="">
					</div>
					<div class="form-group">
						<div class='alert alert-danger' id="error_calendarlist"
							style="display: none; margin-bottom: -15px;"></div>
					</div>
					<div class="mdl-footer">
						<button type="button" class="btn btn-success" id="btn_edit">更新</button>
						<button type="button" class="btn btn-warning" OnClick="closemdl_edit()">キャンセル</button><c:if test=""></c:if>
					</div>
				</s:form>
			</div>
		</div>
		
	<script src="resources/js/jquery.js"></script>
	<script src="resources/js/jquery.dataTables.min.js"></script>
	<script src="resources/js/bootstrap-datepicker.js"></script>
	<script src="resources/js/jquery-clockpicker.min.js"></script>
	<script src="resources/js/bootstrap.min.js"></script>
	<script src="resources/js/jquery-ui.min.js"></script>
	<script src="resources/js/dataTables.fixedColumns.min.js"></script>
	<script src="resources/js/timepicker.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {

			$('#att-list').dataTable({
				scrollY : false,
				scrollX : true,
				scrollCollapse : true,
				paging : true,
				fixedColumns : {
					rightColumns : 1,
					leftColumns  : 2
				},
				"lengthMenu": [[7, 30], [7, 30]]
			});
			$('#edit_choose_date').datepicker({
				dateFormat : "yy-mm-dd",
				changeMonth : true,
				changeYear : true,
				onSelect : function() {
					this.focus();
				},
				onClose : function() {
					this.focus();
				}
			});
			$('#edit_start_time').timepicki({
				start_time: ["09","00"],
				show_meridian:false,
				min_hour_value:0,
				max_hour_value:23,
				min_min_value:00,
				step_size_minutes:5,
				overflow_minutes:false,
				increase_direction:'up',
				disabled_keyboard_mobile:true,
				format:'HH:mm',
			});
			
			$('#edit_end_time').timepicki({
				start_time: ["18","00"],
				show_meridian:false,
				min_hour_value:0,
				max_hour_value:23,
				min_min_value:00,
				step_size_minutes:5,
				overflow_minutes:false,
				increase_direction:'up',
				disabled_keyboard_mobile:true,
				format:'HH:mm',
			}); 
			
			$('#edit_work_hour').timepicki({
				start_time: ["00","00"],
				show_meridian:false,
				min_hour_value:0,
				max_hour_value:23,
				min_min_value:00,
				step_size_minutes:5,
				overflow_minutes:false,
				increase_direction:'up',
				disabled_keyboard_mobile:true,
				format:'HH:mm',
			});
			
			$('#edit_break_time,  #edit_overtime, #edit_midnight_overtime, #edit_compensatory_leave_hour, #edit_releaving_leave_hour, #edit_midnight_compensatory_leave_hour').timepicki({
				start_time : [ "00", "00" ],
				show_meridian : false,
				min_hour_value : 0,
				max_hour_value : 23,
				min_min_value : 00,
				step_size_minutes : 5,
				overflow_minutes : false,
				increase_direction : 'up',
				disabled_keyboard_mobile : true
			});
			$('#start_date').datepicker({
				dateFormat : "yy-mm-dd",
				changeMonth : true,
				changeYear : true
			});
			$('#start_date').on('changeDate', function(ev) {
				$(this).datepicker('hide');
			});			
			$('#end_date').datepicker({
				dateFormat : "yy-mm-dd",
				changeMonth : true,
				changeYear : true
			});
			$('#end_date').on('changeDate', function(ev) {
				$(this).datepicker('hide');
			});
		});

		$("#btn_search").click(function() {
			document.attendanceListForm.action = "AttendanceListSearch";
			document.attendanceListForm.submit();
		});
		
		$("#btn_clear").click(function() {
			if(!$('#emp_cd').prop('readonly'))
				document.getElementById("emp_cd").value = '';
			if(!$('#emp_cd').prop('readonly'))
				document.getElementById("emp_name").value = '';
			document.getElementById("start_date").value = '';
			document.getElementById("end_date").value = '';
			$('#msg').html("");
		    $('#msg').hide();		
		});
		
		$("#btn_edit").click(function() {
			if(validateform()) {
				document.attendanceListEidtFrm.action = "AttendanceListEdit";
				document.attendanceListEidtFrm.submit();
			}
		});
		
		$("#btn_delete").click(function() {
			document.attendanceListForm.action = "AttendanceListDelete";
			document.attendanceListForm.submit();
		});

		function editAttendance(id) {
			
			document.getElementById('hiddenId').value = id;
			var table = $('#att-list').DataTable();
			var info = table.page.info();
			document.getElementById("pageNo").value = info.page;
			document.attendanceListForm.action = "AttendanceEntryEdit";
			document.attendanceListForm.submit();
			/* $('#Error').hide();
			
			var id;
			if (!isNaN(val)) {
				id = val;
			} else {
				id = val.id;
			}

			var emp_cd = document.getElementById('list_emp_cd' + id).innerHTML;
			var choose_date = document.getElementById('list_choose_date' + id).innerHTML;
			var day = document.getElementById('list_day' + id).innerHTML;
			var start_time = document.getElementById('list_start_time' + id).innerHTML;
			var end_time = document.getElementById('list_end_time' + id).innerHTML;
			var break_time = document.getElementById('list_break_time' + id).innerHTML;
			var work_hour = document.getElementById('list_work_hour' + id).innerHTML;
			var overtime = document.getElementById('list_overtime' + id).innerHTML;
			var midnight_overtime = document.getElementById('list_midnight_overtime' + id).innerHTML;
			var compensatory_leave_hour = document.getElementById('compensatory_date' + id).innerHTML;
			var releaving_leave_hour = document.getElementById('list_overtime_sunday' + id).innerHTML;
			var midnight_compensatory = document.getElementById('list_compensatory_comment' + id).innerHTML;
			var description = document.getElementById('list_task_description' + id).innerHTML;

			document.getElementById('edit_id').value = id;
			document.getElementById('edit_emp_cd').value = emp_cd;
			document.getElementById('edit_choose_date').value = choose_date;
			if (day.trim() == '休日')
				document.getElementById('edit_day').checked = true;
			else
				document.getElementById('edit_day').checked = false;
			document.getElementById('edit_start_time').value = start_time;
			document.getElementById('edit_end_time').value = end_time;
			document.getElementById('edit_break_time').value = break_time;
			document.getElementById('edit_work_hour').value = work_hour;
			document.getElementById('edit_overtime').value = overtime;
			document.getElementById('edit_midnight_overtime').value = midnight_overtime;
			document.getElementById('edit_compensatory_leave_hour').value = compensatory_leave_hour;
			document.getElementById('edit_releaving_leave_hour').value = releaving_leave_hour;
			document.getElementById('edit_midnight_compensatory_leave_hour').value = midnight_compensatory;
			document.getElementById('edit_description').value = description;
			location.href = "#mdl_edit"; */
		}
		
		function deleteAttendance(val) {
			var id;
			if (!isNaN(val)) {
				id = val;
			} else {
				id = val.id;
			}
			document.getElementById('delete_id').value = id;
			location.href = "#mdl_delete";
		}

		function closemdl_delete(val) {
			$('#mdl_delete').modal('hide');
			document.attendanceListForm.action = "AttendanceListSearch";
			document.attendanceListForm.submit();
		}

		/* function closemdl_edit() {
			$('#mdl_edit').modal('hide');
			document.attendanceListForm.action = "AttendanceListSearch";
			document.attendanceListForm.submit();
		}
		 */
		function validateform() {
			var date = document.getElementById('edit_choose_date').value;
			var st = document.getElementById('edit_start_time').value;
			var en = document.getElementById('edit_end_time').value;
			var breaktime=document.getElementById('edit_break_time').value.trim();
			var workhour=document.getElementById('edit_work_hour').value.trim();
			var re = /^\d{4}-\d{1,2}-\d{1,2}$/;
			if (date == null || date == "") {
				message = " 『日付』 を入力してください。";
				$('#Error').html(
						"<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"
								+ message);
				$('#Error').show();
				return false;
			}
			if (!date.match(re)) {
				message = " 『日付』のフォーマット が間違っています。 ";
				$('#Error').html(
						"<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"
								+ message);
				$('#Error').show();
				return false;
			}
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
			if(breaktime==null || breaktime==""){
				message =" 『休憩時間』 を入力してください。<br />『休憩時間』ない場合は、<b>『0』</b> を入力してください。";
				$('#Error').html("<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"+message);
			    $('#Error').show();		
			    return false;
		   }							
		   if(workhour==null || workhour==""){
				message =" 『作業時間』 を入力してください。";
				$('#Error').html("<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"+message);
			    $('#Error').show();		
			    return false;
		   }		
			return true;
		}
		
		function hideDiv() {
			if(document.getElementById("search").value=="") {
				target = document.getElementById("panel-body");
				target.style.display="none";
			}
		}
	</script>
</body>
</html>