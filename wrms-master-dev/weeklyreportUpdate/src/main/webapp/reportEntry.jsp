<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/struts-bootstrap-tags" prefix="sj"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE>
<html>
<head>
<sj:head />
<meta charset="UTF-8">
<c:if test="${reportContentFrm.search eq '0'}">
	<title>週報更新</title>
</c:if>
<c:if
	test="${reportContentFrm.search eq '1' || reportContentFrm.search eq '2'}">
	<title>週報登録</title>
</c:if>
<script src="resources/js/timecalculate.js"></script>
<link href="resources/css/bootstrap.min.css" rel="stylesheet">
<link href="resources/css/jquery-ui.css" rel="stylesheet">
<link href="resources/css/jquery-clockpicker.min.css" rel="stylesheet">
<link href="resources/css/timepicki.css" rel="stylesheet">
<link href="resources/css/navbar-fixed-top.css" rel="stylesheet">
<link rel="shortcut icon" href="resources/img/favicon.jpg" />
<style>
.timepicker_wrap {
width: 192px;
height: 150px;
}
</style>
</head>
<body onload="hideDiv()">

	<!-- Sessiontimeout Message -->
	<%
		session = request.getSession(false);
		if (session.getAttribute("ID") == null)
			response.sendRedirect("sessionTimeOut.jsp");
	%>
	<!--End of Sessiontimeout Message -->
	<div id="reportEntryRow">
		<div><jsp:include page="menu.jsp"></jsp:include></div>

		<div class="col-md-10 col-md-offset-1">
			<div class="panel panel-success">
				<c:if test="${reportContentFrm.search eq '0'}">
					<div class="panel-heading">週報更新</div>
				</c:if>
				<c:if
					test="${reportContentFrm.search eq '1' || reportContentFrm.search eq '2'}">
					<div class="panel-heading">週報登録</div>
				</c:if>
				<div class="panel-body">
					<div class="col-md-4 col-md-offset-4">
						<s:if test="hasActionErrors()">
							<div class="alert alert-danger" id="error">
								<s:actionerror />
							</div>
						</s:if>
					</div>
					<div class="col-md-4 col-md-offset-4">
						<s:if test="hasActionMessages()">
							<div class="alert alert-success" id="msg">
								<s:actionmessage />
							</div>
						</s:if>
					</div>

					<form id="reportEntry" class="form-horizontal" name="form"
						method="post" autocomplete="off">
						<div class="row">
							<div class="col-md-4 col-md-offset-4">
								<div class='alert alert-danger' id="Error"
									style="display: none; text-align: center;"></div>
							</div>
						</div>
						<s:textfield name="report_cd" type="hidden" />
						<s:textfield name="status" type="hidden" value="N" />
						<s:textfield name="report.customer_name_hidden"
							id="customer_name_hidden" type="hidden" />
						<!-- First half -->
						<div class="row">
							<!-- Employee No -->
							<div class="col-md-3">
								<div class="form-group">
									<label for="emp_cd" class="control-label col-md-4">社員番号</label>
									<div class="col-md-8">
										<input class="form-control" disabled="disabled" id="emp_cd"
											value="${reportContentFrm.emp_cd}" /> <input
											class="form-control" name="reportContentFrm.emp_cd"
											id="emp_cd" type="hidden" value="${reportContentFrm.emp_cd}" />
									</div>
								</div>
							</div>
							<!-- Dispatching Office -->
							<div class="col-md-3">
								<div class="form-group">
									<label for="customer_name" class="control-label col-md-4">顧客名</label>
									<div class="col-md-8">
										<select name="reportContentFrm.customer_name"
											class="form-control" id="customer_name">
											<option value="-1">Select</option>
											<c:forEach var="cuslst" items="${customerList}"
												varStatus="status">
												<c:choose>
													<c:when test="${reportContentFrm.customer_name eq cuslst}">
														<option value="${cuslst}" selected="selected">${cuslst}</option>
													</c:when>
													<c:otherwise>
														<option value="${cuslst}">${cuslst}</option>
													</c:otherwise>
												</c:choose>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<!-- Week Start Date -->
							<div class="col-md-3">
								<div class="form-group">
									<label for="choose_date" class="control-label col-md-4"><font
										color="red">*</font>日付</label>
									<div class="col-md-8">
										<c:if test="${reportContentFrm.search eq ''}">
											<s:textfield name="reportContentFrm.started_date"
												cssClass="form-control" id="choose_date" maxlength="10" />
										</c:if>
										<c:if
											test="${reportContentFrm.search eq '1' || reportContentFrm.search eq '0' || reportContentFrm.search eq '2'}">
											<s:textfield name="reportContentFrm.started_date"
												cssClass="form-control" id="choose_date" maxlength="10"
												disabled="true" />
										</c:if>
										<s:textfield name="reportContentFrm.started_date_hidden"
											id="choose_date_hidden" type="hidden" value="" />
										<br />
									</div>
								</div>
							</div>
							<!-- Button Group -->
							<div class="col-md-3">
								<c:if test="${reportContentFrm.search eq ''}">
									<button type="button" class="btn btn-success" id="btnSearch">検索</button>
									<button type="reset" class="btn btn-danger" id="btnClear1">クリア</button>
								</c:if>
								<c:if
									test="${reportContentFrm.search eq '1' || reportContentFrm.search eq '0' || reportContentFrm.search eq '2'}">
									<button type="button" class="btn btn-success id="
										btnSearch" disabled="disabled">検索</button>
								</c:if>
								<c:if
									test="${reportContentFrm.search eq '0' || reportContentFrm.search eq '2'}">
									<button type="reset" class="btn btn-danger" id="btnClear1"
										disabled="disabled">クリア</button>
									<button type="reset" class="btn btn-warning" id="btnBack">一覧に戻る</button>
								</c:if>
								<c:if test="${reportContentFrm.search eq '1'}">
									<button type="reset" class="btn btn-danger" id="btnClear1">クリア</button>
								</c:if>
							</div>
						</div>

						<div id="panel-body">
							<div class="panel panel-body">
								<div>
									<hr />
								</div>

								<!-- Report Entry Label Start -->
								<div class="row">
									<div class="col-md-2 text-center">
										<h5>
											<strong>日付</strong>
										</h5>
									</div>
									<div class="col-md-3 text-center">
										<h5>
											<strong>作業内容</strong>
										</h5>
									</div>

									<div class="col-md-1 text-center">
										<h5>
											<strong>進捗予定(%)</strong>
										</h5>
									</div>

									<div class="col-md-1 text-center">
										<h5>
											<strong>計画時間(H)</strong>
										</h5>
									</div>

									<div class="col-md-3 text-center">
										<h5>
											<strong>実績</strong>
										</h5>
									</div>

									<div class="col-md-1 text-center">
										<h5>
											<strong>進捗結果(%)</strong>
										</h5>
									</div>

									<div class="col-md-1 text-center">
										<h5>
											<strong>実績時間(H)</strong>
										</h5>
									</div>
								</div>
								<!-- End of Report Entry Label -->
								<!-- Start Report Entry -->
								<div class="row">
									<div id="datatable">

										<c:forEach var="reportList"
											items="${reportContentFrm.reportContentDetail}"
											varStatus="status">
											<s:set name="paramName">reportContentFrm.reportContentDetail[${status.index}]</s:set>
											<div id="row" class="row">
												<div class="col-md-5">
													<div class="form-group">
														<div class="col-md-4">
															<c:if test="${reportList.mode ne '0'}">
																<s:textfield cssClass="form-control"
																	value="%{#attr.reportList.work_date}"
																	name="%{#paramName}.work_date" id="work_date"
																	readonly="true" />
															</c:if>
														</div>
														<div class="col-md-8">
															<s:textarea id="contents"
																value="%{#attr.reportList.contents}"
																name="%{#paramName}.contents" cssClass="form-control"
																maxlength="200" rows="4" />
															<br />
															<div class="col-md-12">
																<s:hidden value="%{#attr.reportList.work_date_hidden}"
																	name="%{#paramName}.work_date_hidden"></s:hidden>
																<s:hidden value="%{#attr.reportList.mode}"
																	name="%{#paramName}.mode"></s:hidden>
																<a href="javascript:void(0);"
																	onclick="btnAdd(${status.index});showhidecomment;"
																	class="btn btn-success btn-xs"> <span
																	class="glyphicon glyphicon-plus" aria-hidden="true"></span>Add
																	Row
																</a>
																<c:if test="${reportList.mode eq '0'}">
																	<a href="javascript:void(0);"
																		onclick="btnRemove(${status.index},${attr.reportList.mode});"
																		class="btn btn-danger btn-xs"> <span
																		class="glyphicon glyphicon-minus" aria-hidden="true"></span>Remove
																	</a>
																</c:if>
															</div>
														</div>

													</div>
												</div>

												<div class="col-md-1">
													<div class="form-group">
														<div class="col-md-11">
															<s:textfield value="%{#attr.reportList.plan_progress}"
																name="%{#paramName}.plan_progress"
																cssClass="form-control" id="plan_progress" maxlength="3" />
														</div>
													</div>
												</div>

												<div class="col-md-1">
													<div class="form-group">
														<div class="col-md-11">
															<s:textfield value="%{#attr.reportList.plan_time}" 
																name="%{#paramName}.plan_time" cssClass="form-control plan_time"
																id="%{#paramName}.plan_time" maxlength="5" onblur=""
																onmouseover="calculateTotal()"/>
														</div>
													</div>
												</div>

												<div class="col-md-3">
													<div class="form-group">
														<div class="col-md-11">
															<s:textarea value="%{#attr.reportList.result}" 
																name="%{#paramName}.result" cssClass="form-control"
																id="result" maxlength="100" rows="4" readonly="true"/>
														</div>
													</div>
												</div>

												<div class="col-md-1">
													<div class="form-group">
														<div class="col-md-11">
															<s:textfield value="%{#attr.reportList.actual_progress}"
																name="%{#paramName}.actual_progress"
																cssClass="form-control" id="actual_progress"
																maxlength="3" />

														</div>
													</div>
												</div>

												<div class="col-md-1">
													<div class="form-group">
														<div class="col-md-11">
															<s:textfield value="%{#attr.reportList.actual_time}"
																name="%{#paramName}.actual_time" cssClass="form-control"
																id="%{#paramName}.actual_time" maxlength="5" onblur=""
																onmouseover="calculateTotal()" readonly="true"/>
														</div>
													</div>
												</div>

											</div>
										</c:forEach>
									</div>
								</div>

								<!-- 合計計画時間と合計実績時間を計算する -->
								<div class="col-md-4"></div>
								<div class="col-md-2">
									<label>合計計画時間(H)</label>
								</div>

								<div class="col-md-1">
									<div class="form-group">
										<div class="col-md-12">
											<s:textfield cssClass="form-control" onmouseover="calculateTotal()"
												name="total_planned_time" readonly="true"
												id="total_planned_time" maxlength="5" onblur="" />
										</div>
									</div>
								</div>

								<div class="col-md-2"></div>

								<div class="col-md-2">
									<label>合計実績時間(H)</label>
								</div>

								<div class="col-md-1">
									<div class="form-group">
										<div class="col-md-12">
											<s:textfield cssClass="form-control" name="total_actual_time"
												readonly="true" id="total_actual_time" maxlength="5"
												onblur="" />
										</div>
									</div>
								</div>
								<!-- 合計 -->
								<!-- hmk -->
								<div class="row">
									<div class="col-md-10">
										<div class="form-group">
											<label for="emp_comment" class="control-label col-md-2">社員コメント</label>
											<div class="col-md-10">
												<c:if
													test="${reportContentFrm.disabled_empcomment eq 'disabled'}">
													<s:textarea id="emp_comment"
														name="reportContentFrm.emp_comment"
														cssClass="form-control" maxlength="1000" rows="4"
														readonly="true" />
												</c:if>
												<c:if test="${reportContentFrm.disabled_empcomment eq ''}">
													<s:textarea id="emp_comment"
														name="reportContentFrm.emp_comment"
														cssClass="form-control" maxlength="1000" rows="4" />
												</c:if>
											</div>
										</div>
										<div class="form-group">
											<label for="gl_comment" class="control-label col-md-2">GLコメント</label>
											<div class="col-md-10">
												<c:if
													test="${reportContentFrm.disabled_glcomment eq 'disabled'}">
													<s:textarea id="gl_comment"
														name="reportContentFrm.gl_comment" cssClass="form-control"
														maxlength="1000" rows="4" readonly="true" />
												</c:if>
												<c:if test="${reportContentFrm.disabled_glcomment eq ''}">
													<s:textarea id="gl_comment"
														name="reportContentFrm.gl_comment" cssClass="form-control"
														maxlength="1000" rows="4" />
												</c:if>
											</div>
										</div>
										<div class="form-group">
											<label for="pe_comment" class="control-label col-md-2">TLコメント</label>
											<div class="col-md-10">
												<c:if
													test="${reportContentFrm.disabled_pecomment eq 'disabled'}">
													<s:textarea id="pe_comment"
														name="reportContentFrm.pe_comment" cssClass="form-control"
														maxlength="1000" rows="4" readonly="true" />
												</c:if>
												<c:if test="${reportContentFrm.disabled_pecomment eq ''}">
													<s:textarea id="pe_comment"
														name="reportContentFrm.pe_comment" cssClass="form-control"
														maxlength="1000" rows="4" />
												</c:if>
											</div>
										</div>
										<div class="form-group">
											<label for="manager_comment" class="control-label col-md-2">事業部長コメント</label>
											<div class="col-md-10">
												<c:if
													test="${reportContentFrm.disabled_managercomment eq 'disabled'}">
													<s:textarea id="manager_comment"
														name="reportContentFrm.manager_comment"
														cssClass="form-control" maxlength="1000" rows="4"
														readonly="true" />
												</c:if>
												<c:if
													test="${reportContentFrm.disabled_managercomment eq ''}">
													<s:textarea id="manager_comment"
														name="reportContentFrm.manager_comment"
														cssClass="form-control" maxlength="1000" rows="4" />
												</c:if>
											</div>
										</div>
										<div class="col-md-offset-10">
											<c:if test="${reportContentFrm.search eq '0'}">
												<button type="submit" onmouseover="calculateTotal()" class="btn btn-success" id="btnInsert">更新</button>
											</c:if>
											<c:if
												test="${reportContentFrm.search eq '1' || reportContentFrm.search eq '2'}">
												<button type="submit" onmouseover="calculateTotal()" class="btn btn-success" id="btnInsert">登録</button>
											</c:if>
											<!-- 	<button type="submit" class="btn btn-success" id="btnInsert">登録</button> -->
											<c:if
												test="${reportContentFrm.search eq '0' || reportContentFrm.search eq '2'}">
												<button type="reset" class="btn btn-danger" id="btnClear2"
													disabled="disabled">クリア</button>
											</c:if>
											<c:if test="${reportContentFrm.search eq '1'}">
												<button type="reset" class="btn btn-danger" id="btnClear2">クリア</button>
											</c:if>
										</div>
									</div>
								</div>
							</div>
						</div>
						<!--  End of Report Entry -->

						<s:textfield name="rowIndex" type="hidden" />
						<s:hidden id="search" name="reportContentFrm.search"></s:hidden>
					</form>
				</div>
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
		$('.plan_time').timepicki({
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
		
		jQuery(window).load(function () {
		    var cust = document.getElementById("customer_name_hidden").value;
		    if(cust!="") {
		    	$("div.col-md-6 select").val(cust);
		    }

		    /*Calculate the total value of plan time and actual time when document is loaded*/
		    calculateTotal();    
		});
		
		
		
		$("#btnSearch").click(function(){
			if(checkMonday(document.getElementById("choose_date").value)) {
				var choose_date = document.getElementById("choose_date").value;
				document.getElementById("choose_date_hidden").value=choose_date;
				document.form.action = "ReportEntrySearch";
				document.form.submit();
			}
		});

		$("#btnInsert").click(function(){
			var choose_date = document.getElementById("choose_date").value;
			document.getElementById("choose_date_hidden").value=choose_date;
			document.form.action="ReportEntryInsert";
	   	  	document.form.submit();
	     	});
		
		$("#btnClear1").click(function(){
			document.form.action="ReportEntryClear1";
	   	  	document.form.submit();
	    });
		
		$("#btnClear2").click(function(){
			document.form.action="ReportEntryClear2";
	   	  	document.form.submit();
	    });
		
		$("#btnBack").click(function(){
			document.form.action="ReportListSearch";
	   	  	document.form.submit();
	    });
		
		$('#choose_date').datepicker({
			dateFormat: "yy-mm-dd",
			changeMonth: true,
			changeYear: true,
			onSelect: function ()
		    {
		        this.focus();
		    },
			onClose: function ()
		    {
		        this.focus();
		    }
		});
		
		$('#choose_date').keydown(function(e) {
		    if (e.keyCode == 13) {
		        e.preventDefault();
		    }
		});
	});
	
	function btnAdd(index) {
		var choose_date = document.getElementById("choose_date").value;
		document.getElementById("choose_date_hidden").value=choose_date;
		document.getElementById("rowIndex").value= index;
		document.form.action="ReportEntryAdd";
		var form = $('#reportEntry');
		 $.ajax({
			 data: form.serialize(),
			 url: form.attr('action'),
		     type: form.attr('method'), // post or get
		     success: function (data) {
		    	 $('#reportEntryRow').html(data);
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
			/*Calculate the total value of plan time and actual time when Remove button is clicked*/
			calculateTotal();
			
			var choose_date = document.getElementById("choose_date").value;
			document.getElementById("choose_date_hidden").value=choose_date;
			document.getElementById("rowIndex").value= index;
			document.form.action="ReportEntryRemove";
			var form = $('#reportEntry');
			 $.ajax({
			     url: form.attr('action'),
			     data: form.serialize(),
			     type: form.attr('method'),
			     success: function (data) {
			    	 $('#reportEntryRow').html(data);
			     },
			     error: function(){
		                alert("Remove Row error");
		            }
			     }); 			
		} else {
			var message ="最初の行は削除できません。";
			$('#Error').html("<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"+message);
			$('#Error').show();
			return false;
		}
	}
	
	function checkMonday(date) {
		//msgエリア非表示		
		$('#msg').html("");
		$('#msg').hide();
		
		var selDate = ""+ date; 
		selDate = new Date(selDate);
		if(selDate.getDay() != 1) {
			var message ="月曜日を選択してください。";
			$('#Error').html("<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"+message);
			$('#Error').show();	
			return false;
		}
		return true;
	}
	
	function hideDiv() {
		if(document.getElementById("search").value=="" || document.getElementById("search").value=="2") {
			target = document.getElementById("panel-body");
			target.style.display="none";
		}
	}
	
	function findTotal(){
	    /* var arr = document.getElementsByName('plan_time'); */
	    var arr = document.getElementById('plan_time');
	    var tot=0;
	    for(var i=0;i<arr.length;i++){
	        if(parseInt(arr[i].value))
	            tot += parseInt(arr[i].value);
	    }
	    document.getElementById('total_planned_time').value = tot;
	}
	
	window.onclick = calculateTotal;

	/*合計計画時間と合計実績時間を計算する*/
	function calculateTotal(){
		 /*リストのサイズを得る*/
		var totalCount = JSON.parse('${fn:length(reportContentFrm.reportContentDetail)}');	

		var totPTime = 0;
		
		/*合計計画時間を得る */
		for(var i=0; i<totalCount; i++) {
			
			var planName = "reportContentFrm.reportContentDetail["+i+"].plan_time";
			
			var booleanVal = checkFont(planName);

			if(booleanVal){
				var planTime = (document.getElementById(planName).value).split(':');
				var ptime_tot_min = Number(parseInt(planTime[0]) * 60) + Number(parseInt(planTime[1]));
				if (ptime_tot_min == 0){
					document.getElementById(planName).value = "";
				}
				if (!isNaN(ptime_tot_min)) {
				
				totPTime += ptime_tot_min;
				}
			}else{
				alert("半角数字でテキストボックスを入力してください。");
			}			
		}			
		
		if (!isNaN(totPTime)) {
			totPTime = (('0' + Math.floor(totPTime / 60)).slice(-2))
				+ ":" + (('0' + (totPTime % 60)).slice(-2));
		}
		/*合計計画時間をセット*/
		document.getElementById('total_planned_time').value = totPTime;
		
		var totATime = 0;
		
		/*合計実績時間を得る */
		for(var i=0; i<totalCount; i++) {
			
			var planName = "reportContentFrm.reportContentDetail["+i+"].actual_time";
			
			var booleanVal = checkFont(planName);
			
			if(booleanVal){
				var actualTime = (document.getElementById(planName).value).split(':');
				var atime_tot_min = Number(parseInt(actualTime[0]) * 60) + Number(parseInt(actualTime[1]));
				if (atime_tot_min == 0){
					document.getElementById(planName).value = "";
				}
				if (!isNaN(atime_tot_min)) {
				
					totATime += atime_tot_min;
				}
			}else{
				alert("半角数字でテキストボックスを入力してください。");
			}			
		}
		if (!isNaN(totATime)) {
			totATime = (('0' + Math.floor(totATime / 60)).slice(-2))
				+ ":" + (('0' + (totATime % 60)).slice(-2));
		}
		/*合計実績時間をセット*/
		document.getElementById('total_actual_time').value = totATime;
				
	}
	 
	 /*半角／全角をチェック */
	 function checkFont(name){		 
		 
		 var el = document.getElementById(name);
		 var booleanVal;
		 		 
		 if ( /[０-９]/.test(el.value) || /[一-龯]/.test(el.value) || /[ぁ-んァ-ン！：／]/.test(el.value) || /[ｧ-ﾝﾞﾟ]/.test(el.value) || /[Ａ-ｚ]/.test(el.value) || /[A-z,]/.test(el.value)) {
			 booleanVal = false;
		}else{
			booleanVal = true;
		}
		 return booleanVal;
	 }
	</script>
</body>
</html>