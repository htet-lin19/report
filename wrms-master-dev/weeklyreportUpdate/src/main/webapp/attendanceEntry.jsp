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
<s:if test="%{hiddenId !=null && hiddenId !=''}">
	<title>勤務時間更新</title>
</s:if>
<s:else>
	<title>勤務時間登録</title>
</s:else>

<link href="resources/css/bootstrap.min.css" rel="stylesheet">
<link href="resources/css/jquery-ui.css" rel="stylesheet">
<link href="resources/css/jquery-clockpicker.min.css" rel="stylesheet">
<link href="resources/css/timepicki.css" rel="stylesheet">
<link href="resources/css/navbar-fixed-top.css" rel="stylesheet">
<link rel="shortcut icon" href="resources/img/favicon.jpg" />
</head>
<body>
	<!-- Sessiontimeout Message -->
	<%
		session = request.getSession(false);
		if (session.getAttribute("ID") == null)
			response.sendRedirect("sessionTimeOut.jsp");
	%>
	<!--End of Sessiontimeout Message -->

	<div><jsp:include page="menu.jsp"></jsp:include></div>
	<div class="col-md-10 col-md-offset-1">

		<!-- DuplicateMessage -->
		<div class="panel panel-warning">
			<s:if test="%{hiddenId !=null && hiddenId !=''}">
				<div class="panel-heading">勤務時間更新</div>
			</s:if>
			<s:else>
				<div class="panel-heading">勤務時間登録</div>
			</s:else>
			<div class="panel-body">
				<div class="col-md-5 col-md-offset-4">
					<s:if test="hasActionErrors()">
						<div class="alert alert-danger alerts" id="error">
							<s:actionerror />
						</div>
					</s:if>
				</div>
				<div class="col-md-3 col-md-offset-4">
					<s:if test="hasActionMessages()">
						<div class="alert alert-success alerts" id="msg">
							<s:actionmessage />
						</div>
					</s:if>
				</div>
				<!--End of DuplicateMessage -->
				<form id="attendanceEntry" name="attendanceListEidtFrm"
					class="form-horizontal" method="post" autocomplete="off">
					<input type="hidden" id="shift_work"
						value="${attendanceListEidtFrm.shift_work}" />
					<div class="col-md-4 col-md-offset-3">
						<div class='alert alert-danger' id="Error" style="display: none;"></div>
					</div>
					<s:textfield name="id" type="hidden" />
					<!-- First half -->
					<div class="col-md-6">
						<c:set var="emp_cd" value="${attendanceListEidtFrm.emp_cd}" />
						<div class="form-group">
							<label for="emp_cd" class="control-label col-md-4">社員番号</label>
							<c:set var="emp_cd" value="${attendanceListEidtFrm.emp_cd}" />
							<div class="col-md-6">
								<c:if test="${not empty emp_cd }">
									<input class="form-control" name="attendanceListEidtFrm.emp_cd"
										id="emp_cd" disabled="disabled"
										value="${attendanceListEidtFrm.emp_cd}" />
									<input class="form-control" name="attendanceListEidtFrm.emp_cd"
										id="emp_cd" type="hidden"
										value="${attendanceListEidtFrm.emp_cd}" />
								</c:if>
								<c:if test="${empty emp_cd}">
									<input class="form-control" name="attendanceListEidtFrm.emp_cd"
										disabled="disabled" id="emp_cd"
										value="<%out.print(session.getAttribute("ID"));%>" />
									<input class="form-control" name="attendanceListEidtFrm.emp_cd"
										id="emp_cd" type="hidden"
										value="<%out.print(session.getAttribute("ID"));%>" />
								</c:if>
							</div>

						</div>

						<div class="form-group">
							<label for="choose_date" class="control-label col-md-4"><font
								color="red">*</font>日付</label>
							<div class="col-md-6">
								<s:if test="%{hiddenId !=null && hiddenId !=''}">
									<s:textfield type="hidden"
										name="attendanceListEidtFrm.choose_date" />
									<input name="attendanceListEidtFrm.choose_date"
										disabled="disabled" class="form-control choose_datepicker"
										maxlength="10" id="choose_date"
										value="${attendanceListEidtFrm.choose_date}" />
								</s:if>
								<s:else>
									<input name="attendanceListEidtFrm.choose_date"
										id="choose_date" class="form-control choose_datepicker"
										maxlength="10" value="${attendanceListEidtFrm.choose_date}" />
								</s:else>
							</div>
						</div>

						<div class="form-group">
							<label for="choose_date" class="control-label col-md-4"></label>
							<div class="col-md-6">
								<s:label name="attention" value="※休暇取得の場合はチェックボックスをONにしてください。"
									style="color: #af2d2d" />
							</div>
						</div>

						<div class="form-group">
							<label for="day" class="control-label col-md-4">全体</label>
							<div class="col-md-2">

								<!--勤務時間一覧から勤務時間更新へ遷移した場合  -->
								<s:if test="%{hiddenId !=null && hiddenId !=''}">
									<c:set var="day" value="${attendanceListEidtFrm.day}" />
									<c:if test="${day == 1}">
										<input type="checkbox" name="attendanceListEidtFrm.day"
											id="day"
											style="width: 10px; height: 30px; transform: scale(1.5);"
											maxlength="5"
											onClick="handleDisableWorkHourandBreakTime(this.checked, 'Day')"
											value="true" checked="checked" />

									</c:if>
									<c:if test="${day !=1}">
										<input type="checkbox" name="attendanceListEidtFrm.day"
											id="day"
											style="width: 10px; height: 30px; transform: scale(1.5);"
											maxlength="5"
											onClick="handleDisableWorkHourandBreakTime(this.checked, 'Day')"
											value="false" />
									</c:if>

								</s:if>
								<!--メニューから勤務時間登録する場合 -->
								<s:else>
									<input type="checkbox" name="attendanceListEidtFrm.day"
										id="day"
										style="width: 10px; height: 30px; transform: scale(1.5);"
										maxlength="5"
										onClick="handleDisableWorkHourandBreakTime(this.checked, 'Day')"
										value="false" />
								</s:else>
							</div>

							<label for="daikyu" class="control-label col-md-3">代休</label>
							<div class="col-md-1">
								<input type="checkbox"
									name="attendanceListEidtFrm.compensatory_leave" id="daikyu"
									style="width: 10px; height: 30px; transform: scale(1.5);"
									maxlength="5"
									onClick="handleDisableWorkHourandBreakTime(this.checked, 'DaiKyu')"
									value="true" />
							</div>
						</div>

						<div class="form-group">
							<label for="day" class="control-label col-md-4">午前半休</label>
							<div class="col-md-2">
								<c:if test="${day == 2}">
									<input type="checkbox" name="attendanceListEidtFrm.day"
										id="morning"
										style="width: 10px; height: 30px; transform: scale(1.5);"
										maxlength="5"
										onClick="handleDisableWorkHourandBreakTime(this.checked, 'Morning')"
										value="morning" checked="checked" />
								</c:if>
								<c:if test="${day !=2}">
									<input type="checkbox" name="attendanceListEidtFrm.day"
										id="morning"
										style="width: 10px; height: 30px; transform: scale(1.5);"
										maxlength="5"
										onClick="handleDisableWorkHourandBreakTime(this.checked, 'Morning')"
										value="morning" />
								</c:if>

							</div>
							<label for="day" class="control-label col-md-3">午後半休</label>
							<div class="col-md-1">
								<c:if test="${day == 3}">
									<input type="checkbox" name="attendanceListEidtFrm.day"
										id="evening" checked="checked"
										style="width: 10px; height: 30px; transform: scale(1.5);"
										maxlength="5"
										onClick="handleDisableWorkHourandBreakTime(this.checked, 'Evening')"
										value="evening" />
								</c:if>
								<c:if test="${day != 3}">
									<input type="checkbox" name="attendanceListEidtFrm.day"
										id="evening"
										style="width: 10px; height: 30px; transform: scale(1.5);"
										maxlength="5"
										onClick="handleDisableWorkHourandBreakTime(this.checked, 'Evening')"
										value="evening" />
								</c:if>
							</div>

						</div>

						<div class="form-group">
							<label for="day" class="control-label col-md-4">特別休暇</label>
							<div class="col-md-2">
								<c:if test="${day == 4}">
									<input type="checkbox" name="attendanceListEidtFrm.day"
										id="special" checked="checked"
										style="width: 10px; height: 30px; transform: scale(1.5);"
										maxlength="5" value="special"
										onClick="handleDisableWorkHourandBreakTime(this.checked, 'Special')" />
								</c:if>
								<c:if test="${day != 4}">
									<input type="checkbox" name="attendanceListEidtFrm.day"
										id="special"
										style="width: 10px; height: 30px; transform: scale(1.5);"
										maxlength="5" value="special"
										onClick="handleDisableWorkHourandBreakTime(this.checked, 'Special')" />
								</c:if>
							</div>

						</div>

						<div class="form-group hidden" id="compensatory">
							<label for="compensatory_date" class="control-label col-md-4">
								代休取得該当日</label>
							<div class="col-md-6">
								<input name="attendanceListEidtFrm.compensatory_date"
									id="compensatory_date"
									value="${attendanceListEidtFrm.compensatory_date}"
									class="form-control choose_datepicker" maxlength="10" />
							</div>
						</div>
						<div class="form-group">
							<label for="start_time" class="control-label col-md-4"><font
								color="red">*</font>開始時刻</label>
							<div class="col-md-6">
								<s:if test="%{hiddenId !=null && hiddenId !=''}">
									<c:set var="day" value="${attendanceListEidtFrm.day}" />
									<c:if test="${day == 1}">
										<input name="attendanceListEidtFrm.start_time"
											class="time_element form-control" id="start_time"
											maxlength="7" onmouseout="calculateTime()" disabled="true"
											value="${attendanceListEidtFrm.start_time}" />
									</c:if>
									<c:if test="${day != 1}">
										<input name="attendanceListEidtFrm.start_time"
											class="time_element form-control" id="start_time"
											maxlength="7" onmouseout="calculateTime()"
											value="${attendanceListEidtFrm.start_time}" />
									</c:if>
								</s:if>
								<s:else>
									<input name="attendanceListEidtFrm.start_time"
										class="time_element form-control" id="start_time"
										maxlength="7" onmouseout="calculateTime()"
										value="${attendanceListEidtFrm.start_time}" />
								</s:else>
							</div>
						</div>

						<div class="form-group">
							<label for="end_time" class="control-label col-md-4"><font
								color="red">*</font>終了時刻</label>
							<div class="col-md-6">
								<s:if test="%{hiddenId !=null && hiddenId !=''}">
									<c:set var="day" value="${attendanceListEidtFrm.day}" />
									<c:if test="${day == 1}">
										<input name="attendanceListEidtFrm.end_time"
											class="time_element form-control" id="end_time" maxlength="7"
											onmouseout="calculateTime()" disabled="true"
											value="${attendanceListEidtFrm.end_time}" />
									</c:if>
									<c:if test="${day != 1}">
										<input name="attendanceListEidtFrm.end_time"
											class="time_element form-control" id="end_time" maxlength="7"
											onmouseout="calculateTime()"
											value="${attendanceListEidtFrm.end_time}" />
									</c:if>
								</s:if>
								<s:else>
									<input name="attendanceListEidtFrm.end_time"
										class="time_element form-control" id="end_time" maxlength="7"
										onmouseout="calculateTime()"
										value="${attendanceListEidtFrm.end_time}" />
								</s:else>
							</div>
						</div>

						<div class="form-group">
							<label for="break_time" class="control-label col-md-4"><font
								color="red">*</font>休憩時間</label>
							<div class="col-md-6">
								<s:if test="%{hiddenId !=null && hiddenId !=''}">
									<c:set var="day" value="${attendanceListEidtFrm.day}" />
									<c:if test="${day == 1}">
										<input name="attendanceListEidtFrm.break_time"
											class="time_element form-control" id="break_time"
											maxlength="7" onmouseout="calculateTime()" disabled="true"
											value="${attendanceListEidtFrm.break_time}" />
									</c:if>
									<c:if test="${day != 1}">
										<input name="attendanceListEidtFrm.break_time"
											class="time_element form-control" id="break_time"
											maxlength="7" onmouseout="calculateTime()"
											value="${attendanceListEidtFrm.break_time}" />
									</c:if>
								</s:if>
								<s:else>
									<input name="attendanceListEidtFrm.break_time"
										class="time_element form-control" id="break_time"
										maxlength="7" onmouseout="calculateTime()"
										value="${attendanceListEidtFrm.break_time}" />
								</s:else>
							</div>
						</div>

						<div class="form-group">
							<label for="work_hour" class="control-label col-md-4">勤務時間</label>
							<div class="col-md-6">
								<input name="attendanceListEidtFrm.work_hour"
									class="time_element form-control" readonly="readonly"
									id="work_hour" maxlength="7"
									value="${attendanceListEidtFrm.work_hour}" />
								<!-- <input id="work_hour" name="attendanceListEidtFrm.work_hour" class="form-control" maxlength="5" value="${attendanceListEidtFrm.work_hour}"/>   -->
							</div>
						</div>

					</div>
					<!-- Second Half -->

					<div class="col-md-6">
						<div class="form-group">
							<label for="overtime" class="control-label col-md-4">法定時間外</label>
							<div class="col-md-6">
								<input name="attendanceListEidtFrm.overtime" readonly="readonly"
									class="time_element form-control" id="overtime" maxlength="7"
									value="${attendanceListEidtFrm.overtime}" />
							</div>
						</div>
						<div class="form-group">
							<label for="midnight_overtime" class="control-label col-md-4">法定休日</label>
							<div class="col-md-6">
								<input name="attendanceListEidtFrm.overtime_sunday"
									readonly="readonly" class="time_element form-control"
									id="overtime_sunday" maxlength="7"
									value="${attendanceListEidtFrm.overtime_sunday}" />

							</div>
						</div>

						<div class="form-group">
							<label for="midnight_overtime" class="control-label col-md-4">深夜早朝</label>
							<div class="col-md-6">
								<input name="attendanceListEidtFrm.midnight_overtime"
									readonly="readonly" class="time_element form-control"
									id="midnight_overtime" maxlength="7"
									value="${attendanceListEidtFrm.midnight_overtime}" />
							</div>
						</div>

						<div class="form-group">
							<label for="compensatory_comment" class="control-label col-md-4">その他</label>
							<div class="col-md-6">
								<s:textarea id="compensatory_comment" readonly="true"
									name="attendanceListEidtFrm.compensatory_comment"
									cssClass="form-control" rows="3" maxlength="200" />
							</div>
						</div>

						<div class="form-group">
							<label for="description" class="control-label col-md-4">備考</label>
							<div class="col-md-6">
								<s:textarea id="description"
									name="attendanceListEidtFrm.task_description"
									cssClass="form-control" rows="5" maxlength="200" />
							</div>
						</div>
						<c:forEach var="reportList"
							items="${reportContentFrm.reportContentDetail}"
							varStatus="status">
							<s:set name="paramName">reportContentFrm.reportContentDetail[${status.index}]</s:set>

							<div class="form-group">
								<c:if test="${reportList.mode ne '0'}">
									<label for="result" class="control-label col-md-4">作業実績</label>
								</c:if>
								<c:if test="${reportList.mode eq '0'}">
									<label for="result" class="control-label col-md-4"></label>
								</c:if>
								<div class="col-md-6">
									<s:textarea id="%{#paramName}.result"
										value="%{#attr.reportList.result}" name="%{#paramName}.result"
										cssClass="form-control" rows="5" maxlength="100" />
									<br />
									<div class="col-md-12">
										<s:hidden value="%{#attr.reportList.mode}"
											name="%{#paramName}.mode"></s:hidden>
										<a href="javascript:void(0);"
											onclick="btnAdd(${status.index});"
											class="btn btn-success btn-xs"> <span
											class="glyphicon glyphicon-plus" aria-hidden="true"></span>Add
										</a> <a href="javascript:void(0);"
											onclick="btnRemove(${status.index},${attr.reportList.mode});"
											class="btn btn-danger btn-xs"> <span
											class="glyphicon glyphicon-minus" aria-hidden="true"></span>Remove
										</a>
									</div>
								</div>
							</div>
						</c:forEach>
						<s:textfield id="rowIndex" name="rowIndex" type="hidden" />
						<s:textfield type="hidden" name="created_date" />
						<s:textfield type="hidden" name="modified_date" />
						<s:textfield type="hidden" name="hiddenId" />
						<!-- 土日祝日配列 -->
						<input type="hidden" name="attendanceListEidtFrm.holidayList"
							id="holidayList" value="${attendanceListEidtFrm.holidayList}" />
						<input type="hidden" name="attendanceListEidtFrm.kyuka" id="kyuka"
							value="${attendanceListEidtFrm.day}" /> <input type="hidden"
							name="attendanceListEidtFrm.compensatory_flg"
							id="compensatory_flg"
							value="${attendanceListEidtFrm.compensatory_leave}" />
					</div>
					<div class="form-group">
						<div class="col-md-9 col-md-offset-5">
							<s:if test="%{hiddenId !=null && hiddenId !=''}">
								<button onmouseover="calculateTime()" type="submit"
									class="btn btn-success" id="btnUpdate">更新</button>
								<button type="reset" class="btn btn-danger" id="btnCancel">キャンセル</button>
							</s:if>
							<s:else>
								<button onmouseover="calculateTime()" type="submit"
									class="btn btn-success" id="btnInsert">登録</button>
								<button onmouseover="calculateTime()" type="reset"
									class="btn btn-danger" id="btnClear">クリア</button>
							</s:else>

						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
	<script src="resources/js/timecalculate.js"></script>
	<script src="resources/js/jquery-2.1.4.min.js"></script>
	<script src="resources/js/validator.min.js"></script>
	<script src="resources/js/bootstrap.min.js"></script>
	<script src="resources/js/jquery-ui.min.js"></script>
	<script src="resources/js/jquery-clockpicker.min.js"></script>
	<script src="resources/js/timepicker.js"></script>
	<!-- Javascript -->
	<script>
		$(document).ready(function(){

					//2020/10/14 GICM NC 勤務時間登録機能追加対応 Start
		        	$('#choose_date,#compensatory_date').datepicker({						
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

					//代休チェックボックス変更イベント
					$("#daikyu").change(function(){
						$('#compensatory_date').val('')
						if($(this).prop('checked')) {
							$('#compensatory').removeClass('hidden');
						}else {
							$('#compensatory').addClass('hidden');
						}
						
					});
		        	 
		             var compensatory_date=$('#compensatory_date').val();
		             //　代休チェックOnする場合、 , 代休取得該当日エリアを表示する
		             if (compensatory_date) {
						$("#daikyu").prop('checked', true);
						$('#compensatory').removeClass('hidden');
		              }
					 
		             var kyuka=$('#kyuka').val();
		             //休暇チェック
					 if(kyuka=='1'){
						 $("#day").prop('checked', true);
					 } else if (kyuka=='2') {
						 $("#morning").prop('checked', true);
					 } else if (kyuka=='3') {
						 $("#evening").prop('checked', true);
					 }else if (kyuka=='4') {
						 $("#special").prop('checked', true); 
					 }
					
					 var compensatory_flg=$('#compensatory_flg').val();
					//代休チェック
					 if(compensatory_flg=='1'){
						 $("#daikyu").prop('checked', true);
						 $('#compensatory').removeClass('hidden');
					 }
		            
					//非活性チェック
					var isDaiKyuChecked = $('#daikyu').prop('checked');
					var isDayChecked =$("#day").prop("checked");
					var isMorningChecked =$('#morning').prop("checked");
					var isEveningChecked = $("#evening").prop("checked");
					var isSpecialChecked =$("#special").prop("checked");
					var shouldDisable = isDayChecked && !(isMorningChecked || isEveningChecked || isSpecialChecked) || isDaiKyuChecked && (isMorningChecked || isEveningChecked || isSpecialChecked)
					DisableTextBox(shouldDisable);					
					 
		            var compensatory_comment=$('#compensatory_comment').val();
		             // 代休データある場合、以下の項目を非活性にする
		            if (compensatory_comment) {
		            	 document.getElementById("day").disabled = true;
		            	 document.getElementById("daikyu").disabled = true;
		            	 document.getElementById("morning").disabled = true;
		            	 document.getElementById("evening").disabled = true;
		            	 document.getElementById("special").disabled = true;
		            	 document.getElementById('compensatory_date').disabled=true;
						 document.getElementById('start_time').disabled=true;
						 document.getElementById('end_time').disabled=true;
						 document.getElementById('break_time').disabled=true;
		             }
					//2020/10/14 GICM NC 勤務時間登録機能追加対応 End
					
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
						format : 'HH:mm',
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
						format : 'HH:mm',
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
						format : 'HH:mm',
					});

					/* $('#work_hour').timepicki({
						start_time : [ "00", "00" ],
						show_meridian : false,
						min_hour_value : 0,
						max_hour_value : 23,
						min_min_value : 00,
						step_size_minutes : 5,
						overflow_minutes : false,
						increase_direction : 'up',
						disabled_keyboard_mobile : true,
						format : 'HH:mm',
					}); */

					$(
							/* '#overtime, #midnight_overtime, #compensatory_leave_hour, #releaving_leave_hour, #midnight_compensatory_leave_hour') */
							' #compensatory_leave_hour, #releaving_leave_hour, #midnight_compensatory_leave_hour')
							.timepicki({
								start_time : [ "00", "00" ],
								show_meridian : false,
								min_hour_value : 0,
								max_hour_value : 23,
								min_min_value : 00,
								step_size_minutes : 15,
								overflow_minutes : false,
								increase_direction : 'up',
								disabled_keyboard_mobile : true,
								format : 'HH:mm',
							});
		

					$("#btnInsert")
							.click(
									function() {
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
										
										RemoveDisalbed(false);
										if(CheckWorkingTime()== false)
											return false;
											
										document.attendanceListEidtFrm.action = "AttendanceEntry";
										document.attendanceListEidtFrm
												.submit();
									});
					$("#btnUpdate")
							.click(
									function() {
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
										
										RemoveDisalbed(false);
										if(CheckWorkingTime()== false)
											return false;
										document.attendanceListEidtFrm.action = "AttendanceUpdate";
										document.attendanceListEidtFrm
												.submit();
									});
					$("#btnClear")
							.click(
									function() {
										document.attendanceListEidtFrm.action = "AttendanceEntryClear";
										document.attendanceListEidtFrm
												.submit();
									});
					$("#btnCancel")
					.click(
							function() {
								document.attendanceListEidtFrm.action = "AttendanceListSearch";
								document.attendanceListEidtFrm
										.submit();
							});
					
					/*$("#day")
					.click(
							function(){
								DisableTextBox(document.getElementById('day').checked);
								alert("来た");
							}); */
							
						});
		/* 
		 $('.form-group').on('change',function() {
		 alert("FFFFF");
		 }); */
		/* 		$('#start_time').on('DOMSubtreeModified', function() {
		 calculateTime();
		 //alert("FFFFF");
		 });
		 */

		 
		function validateform() {
			//msgエリア非表示
			$('#msg').html("");
			$('#msg').hide();

			var date = document.getElementById('choose_date').value;
			var compensatory_date = document.getElementById('compensatory_date').value;
			var st = document.getElementById('start_time').value;
			var en = document.getElementById('end_time').value;
			var breaktime = document.getElementById('break_time').value;
			var workhour = document.getElementById('work_hour').value;

			var re = /^\d{4}-\d{1,2}-\d{1,2}$/;
			if (date == null || date == "") {
				message = " 『日付』 を入力してください。 ";
				$('#Error').html(
						"<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"
								+ message);
				$('#error').addClass('hidden');
				$('#Error').show();
				return false;
			}
			if (!date.match(re)) {
				message = " 『日付』のフォーマット が間違っています。";
				$('#Error').html(
						"<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"
								+ message);
				$('#error').addClass('hidden');
				$('#Error').show();
				return false;
			}
			if (st == null || st == "") {
				message = " 『開始時刻』 を入力してください。";
				$('#Error').html(
						"<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"
								+ message);
				$('#error').addClass('hidden');
				$('#Error').show();
				return false;
			}
			if (en == null || en == "") {
				message = " 『終了時刻』 を入力してください。";
				$('#Error').html(
						"<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"
								+ message);
				$('#error').addClass('hidden');
				$('#Error').show();
				return false;
			}
			if (breaktime == null || breaktime == "") {
				message = " 『休憩時間』 を入力してください。<br>『休憩時間』ない場合は、<b>『0』</b> を入力してください。";
				$('#Error').html(
						"<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"
								+ message);
				$('#error').addClass('hidden');
				$('#Error').show();
				return false;
			}
			if (workhour == null || workhour == "") {
				message = " 『勤務時間』 を入力してください。";
				$('#Error').html(
						"<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"
								+ message);
				$('#error').addClass('hidden');
				$('#Error').show();
				return false;
			}
			//2020/10/20 GICM NC 勤務時間登録機能追加対応 Start           
            var checkBoxEls = [
            	$('#day'),
            	$('#morning'),
            	$('#evening'),
            	$('#special')
            ];
            
            if (!isValidatedKyuKa(checkBoxEls)) {
            	message = " 複数休暇使用は不可能です。";
    			$('#Error').html(
    					"<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"
    							+ message);
    			$('#Error').show();
    			$('#error').addClass('hidden');
    			return false;
            }
            
            if ($('#daikyu').prop("checked") && $('#compensatory_date').val()=='') {
				message = " 『代休取得該当日』 を入力してください。";
				$('#Error').html(
						"<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"
								+ message);
				$('#error').addClass('hidden');
				$('#Error').show();
				return false;
            }
			
			if ($('#daikyu').prop("checked") && !compensatory_date.match(re)) {
				message = " 『代休取得該当日』のフォーマット が間違っています。";
				$('#Error').html(
						"<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"
								+ message);
				$('#error').addClass('hidden');
				$('#Error').show();
				return false;
			}
			
            if ($('#daikyu').prop("checked") && $('#compensatory_date').val()) {        	
            	var choose_date=$('#choose_date').val();
            	var compensatory_date=$('#compensatory_date').val();
            	var choose_month=choose_date.substring(0,7);
            	var compensatory_month=compensatory_date.substring(0,7);
            	
            	if (Date.parse(compensatory_date) > Date.parse(choose_date)) {
    				message = " 代休日付が代休取得該当日より、小さい値は入力できません。 ";
    				$('#Error').html(
    						"<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"
    								+ message);
    				$('#error').addClass('hidden');
    				$('#Error').show();
    				return false;
            	}
            	
            	if (choose_month!==compensatory_month) {
    				message = " 前月の法定時間外や法定休日の作業時間を代休申請できません。";
    				$('#Error').html(
    						"<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"
    								+ message);
    				$('#error').addClass('hidden');
    				$('#Error').show();
    				return false;
            	}

            }
			
            if ($('#day').prop("checked") && $('#daikyu').prop("checked") ) {
				message = " 全体休暇の場合、代休取得不可能です。 ";
				$('#Error').html(
						"<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"
								+ message);
				$('#error').addClass('hidden');
				$('#Error').show();
				return false;
            }
            
            if ($('#special').prop("checked") && $('#daikyu').prop("checked")) {
    			message = " 特別休暇の場合、代休取得不可能です。";
    			$('#Error').html(
    					"<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"
    							+ message);
				$('#error').addClass('hidden');
				$('#Error').show();
    			return false;
            }

            $('#Error').hide();
            //2020/10/20 GICM NC 勤務時間登録機能追加対応 End
            return true;
		}	 
		
		//2020/10/20 GICM NC 勤務時間登録機能追加対応 Start
		//非活性チェック
		function handleDisableWorkHourandBreakTime(isChecked, field) {
			var isDaiKyuChecked = field === 'DaiKyu' ? isChecked : $('#daikyu').prop('checked');
			var isDayChecked = field === 'Day' ? isChecked : $("#day").prop("checked");
			var isMorningChecked = field === 'Morning' ? isChecked : $('#morning').prop("checked");
			var isEveningChecked = field === 'Evening' ? isChecked : $("#evening").prop("checked");
			var isSpecialChecked = field === 'Special' ? isChecked : $("#special").prop("checked");
			var shouldDisable = isDayChecked && !(isMorningChecked || isEveningChecked || isSpecialChecked) || isDaiKyuChecked && (isMorningChecked || isEveningChecked || isSpecialChecked)
			
			if (isDayChecked) {
				$('#kyuka').val('1');
			} else if (isMorningChecked) {
				$('#kyuka').val('2');
			} else if (isEveningChecked) {
				$('#kyuka').val('3');
			} else if (isSpecialChecked) {
				$('#kyuka').val('4');
			} else {
				$('#kyuka').val('0');
			}
			
			if (isDaiKyuChecked) {
				$('#compensatory_flg').val('1');
			} else {
				$('#compensatory_flg').val('0');
			}
			DisableTextBox(shouldDisable);
		}
		
		//休暇チェックボックス複数チェック
		function isValidatedKyuKa(checkBoxEls) {
			var checkStatusArray = checkBoxEls.map(item => item.prop('checked'));
			return checkStatusArray.filter(status => status === true).length < 2;
		}
		//2020/10/20 GICM NC 勤務時間登録機能追加対応 End
		 
		 //一日中休暇をチェックONする事により「開始時刻、終了時刻、休憩時間」を入力不可能にする
		 function DisableTextBox(ischecked)
		{
			 if (ischecked)
			{	 
				 document.getElementById('start_time').value="00:00";
				 document.getElementById('end_time').value="00:00";
				 document.getElementById('break_time').value="00:00";
					 
				 document.getElementById('work_hour').value="00:00";
				 document.getElementById('overtime').value="";
				 document.getElementById('overtime_sunday').value="";
				 document.getElementById('midnight_overtime').value="";				
				 
				 document.getElementById('start_time').disabled=true;
				 document.getElementById('end_time').disabled=true;
				 document.getElementById('break_time').disabled=true;
				 
				 document.getElementById('day').value = "true";
				 
			}
			 else
			{
				 document.getElementById('start_time').disabled=false;
				 document.getElementById('end_time').disabled=false;
				 document.getElementById('break_time').disabled=false;
				 document.getElementById('day').value = "false";
			}			 			 
			 
			 return true;
		}
		 
		function ShowCompensationDate(isChecked){
			
			$("#compensation").show();
		} 
		 
		 //一日中休暇を選択する時に「開始時刻、終了時刻、休憩時間」をdisabled=tureにし値セットして「登録/更新」
		 //をするとdisabled=trueにしたせいで値が取得できないのを避けるため、Formを送信する前にdisabled=tureにした
		 //オブジェクトを元に戻す
		function RemoveDisalbed(bol)
		{
			document.getElementById('start_time').disabled=bol;
			document.getElementById('end_time').disabled=bol;
			document.getElementById('break_time').disabled=bol;
		}		
		 
		//作業時間確認
		function CheckWorkingTime()
		{
			var workhr = document.getElementById('work_hour').value;	
			var data = workhr.split(":");
			var hr=Number(data[0]);
			var min=Number(data[1]);
			var hr_min=String(hr)+"."+String(min);
			var intworkhr = Number(hr_min);
						
			if (intworkhr > 14)
			{
				var res = confirm("作業時間は" + workhr + "で宜しいでしょうか？");
				if(res == true)
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		  return true;
		}
		
		function getFormNameValue(el){
			if(el.length === 0) return null;
			return {
				name: el.attr('name'),
				value: el.val()
			}
		}

		var formFieldIds = [
			'emp_cd',
			'choose_date',
			'compensatory_date',
			'kyuka',
			'compensatory_flg',
			'start_time',
			'end_time',
			'break_time',
			'work_hour',
			'overtime',
			'overtime_sunday',
			'midnight_overtime',
			'compensatory_comment',
			'description',
			'hiddenId',
			'rowIndex'
		]
		
		//フォームデータ取得
		function getFormData() {
			var formData = formFieldIds.map(fieldId =>getFormNameValue($('#' + fieldId)) && getFormNameValue($('#' + fieldId)))
			// add report content
			var reportContents =$("[name ^= 'reportContentFrm.reportContentDetail']").map(function() {
				return getFormNameValue($(this))
			}).get();
			
			// Param Serialization
			return $.param([...formData,...reportContents]);
		}
		
		function btnAdd(index) {
		    document.getElementById("rowIndex").value= index;
			document.attendanceListEidtFrm.action="AttendanceEntryAdd";
			var form = $('#attendanceEntry');
			 $.ajax({
			　　　 // data: form.serialize(),　//GICM NCによるコメント
				 data: getFormData(),
				 url: form.attr('action'),
			     type: form.attr('method'), // post or get
			     success: function (data) {
			    	 $('body').html(data);
			    	 $('.choose_datepicker').datepicker({						
							dateFormat : "yy-mm-dd",
							changeMonth : true,
							changeYear : true,
							onSelect : function() {
								this.focus();
							},
							onClose : function() {
								this.focus();
							}
						})			    	
			     },
			     error: function(){
	                alert("Add Row error");
	            }
			   });  
		}
		
		function btnRemove(index, mode) {
			//msgエリア非表示		
			$('#msg').html("");
			$('#msg').hide();
			if(mode=="0") {
				document.getElementById("rowIndex").value= index;
				document.attendanceListEidtFrm.action="AttendanceEntryRemove";
				var form = $('#attendanceEntry');
				 $.ajax({
				     url: form.attr('action'),
					 //data: form.serialize(),　//GICM NCによるコメント
				     data: getFormData(),
				     type: form.attr('method'),
				     success: function (data) {
				    	 $('body').html(data);
				    	 $('.choose_datepicker').datepicker({						
								dateFormat : "yy-mm-dd",
								changeMonth : true,
								changeYear : true,
								onSelect : function() {
									this.focus();
								},
								onClose : function() {
									this.focus();
								}
							})	
				    	 
				     },
				     error: function(){
			                alert("Remove Row error");
			            }
				     }); 			
			} else {
				var message =" 最初の行は削除できません。";
				$('#Error').html("<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"+message);
				$('#error').addClass('hidden');
				$('#Error').show();
				return false;
			}
		}
		
		window.onclick = calculateTime;
		
	</script>

</body>
</html>