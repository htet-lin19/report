<!--
勤務時間サマリ情報画面	
作成履歴：2020/10/06 GICM AMTD
 作成概要：新規作成　勤務時間サマリ情報を表示する
  -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/struts-bootstrap-tags" prefix="sj"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<style>
td {
	border: 1px solid #dddddd;
	border-collapse: collapse;
}

th{
	width: 75px;
}

tr:nth-child(even) {
  background-color: #f1f1f1;
}

td {
	width: 75px;
	height: 35px;
}
</style>
<meta charset="UTF-8">
<title>勤務時間サマリ情報</title>
<link href="resources/css/bootstrap.min.css" rel="stylesheet">
<link href="resources/css/jquery-ui.css" rel="stylesheet">
<link href="resources/css/navbar-fixed-top.css" rel="stylesheet">
<link rel="shortcut icon" href="resources/img/favicon.jpg" />
</head>
<body>
	<%
		session = request.getSession(false);
		if (session.getAttribute("ID") == null)
			response.sendRedirect("sessionTimeOut.jsp");
	%>
	<div><jsp:include page="menu.jsp"></jsp:include></div>
	<div class="col-md-10 col-md-offset-1">
		<div class="panel panel-warning">
			<div class="panel-heading">
				<strong>勤務時間サマリ情報</strong>
			</div>
			<div class="panel-body">
				<form id="attendanceSummaryInfoFrm" name="attendanceSummaryInfoFrm"
					class="form-horizontal" method="post" data-toggle="validator">
					<div class="col-md-12">
						<div class="form-group">
							<div class="row">
								<div class="col-md-5">
									<label for="emp_cd" class="control-label col-md-4">社員番号</label>
									<div class="col-md-7">
										<s:textfield name="attendanceSummaryInfoFrm.emp_cd" maxlength="8" readonly="true" 
										     requiredLabel="true" cssClass="form-control" id="emp_cd" />
									</div>
								</div>
							</div>
						</div>
						<div class="form-group">
							<div class="row">
								<div class="col-md-5">
									<label for="summary_month" class="control-label col-md-4">対象年度</label>
									<div class="col-md-7">
										<s:select name="attendanceSummaryInfoFrm.target_year" list="attendanceSummaryInfoFrm.yearlist" 
										     class="form-control" id="years" onchange="setProcess()" />
									</div>
								</div>
							</div>
						</div>
						<div class="form-group">
							<div class="row">
								<div class="col-md-5">
									<label for="lastyear_carry_leave"
										class="control-label col-md-4">前年度繰越分</label>
									<div class="col-md-7">
										<c:if test= "${attendanceSummaryInfoFrm.lastyear_carry_leave==0.0}">
											<s:textfield name="attendanceSummaryInfoFrm.lastyear_carry_leave" maxlength="2" value=''
										     	disabled="true" requiredLabel="true" cssClass="form-control" id="lastyear_carry_leave" />
										 </c:if>
										 <c:if test= "${attendanceSummaryInfoFrm.lastyear_carry_leave!=0.0}">   
										 	<s:textfield name="attendanceSummaryInfoFrm.lastyear_carry_leave" maxlength="2"
										     	disabled="true" requiredLabel="true" cssClass="form-control" id="lastyear_carry_leave" />
										 </c:if> 
									</div>
								</div>
								<div class="col-md-5">
									<label for="cur_year_allowed_leave"
										class="control-label col-md-4">今年度付与分</label>
									<div class="col-md-7">
										<c:if test= "${attendanceSummaryInfoFrm.cur_year_allowed_leave==0.0}">
											<s:textfield name="attendanceSummaryInfoFrm.cur_year_allowed_leave" maxlength="2" value=''
										   		disabled="true" requiredLabel="true" cssClass="form-control" id="cur_year_allowed_leave" />
										</c:if>
										<c:if test= "${attendanceSummaryInfoFrm.cur_year_allowed_leave!=0.0}">
											<s:textfield name="attendanceSummaryInfoFrm.cur_year_allowed_leave" maxlength="2"
										    	disabled="true" requiredLabel="true" cssClass="form-control" id="cur_year_allowed_leave" />
										</c:if>
									</div>
								</div>
							</div>
						</div>
						<div class="form-group">
							<div class="row">
								<div class="col-md-5">
									<label for="total_allowed_leave" class="control-label col-md-4">取得可能日数</label>
									<div class="col-md-7">
										<c:if test= "${attendanceSummaryInfoFrm.total_allowed_leave==0.0}">
											<s:textfield name="attendanceSummaryInfoFrm.total_allowed_leave" maxlength="2" value=''
										     	disabled="true" requiredLabel="true" cssClass="form-control" id="total_allowed_leave" />
									 	</c:if>
									 	<c:if test= "${attendanceSummaryInfoFrm.total_allowed_leave!=0.0}">
											<s:textfield name="attendanceSummaryInfoFrm.total_allowed_leave" maxlength="2" 
										     	disabled="true" requiredLabel="true" cssClass="form-control" id="total_allowed_leave" />
									 	</c:if>
									</div>
								</div>
								<div class="col-md-5">
									<label for="remaining_leave" class="control-label col-md-4">有給残日数</label>
									<div class="col-md-7">
										<c:if test= "${attendanceSummaryInfoFrm.remaining_leave==0.0}">
											<s:textfield name="attendanceSummaryInfoFrm.remaining_leave" maxlength="2" value=''
										    	disabled="true" requiredLabel="true" cssClass="form-control" id="remaining_leave" />
									 	</c:if>
									 	<c:if test= "${attendanceSummaryInfoFrm.remaining_leave!=0.0}">
											<s:textfield name="attendanceSummaryInfoFrm.remaining_leave" maxlength="2" 
										    	disabled="true" requiredLabel="true" cssClass="form-control" id="remaining_leave" />
									 	</c:if>
									</div>
								</div>
							</div>
						</div>
					</div>
				</form>
				<div class="col-md-12" style="margin-left: 25px; margin-top: 20px; margin-bottom: 20px;">
					<table style="width: 95%">
						<tr>
							<th style="text-align: center;"></th>
							<th style="text-align: center;">4月</th>
							<th style="text-align: center;">5月</th>
							<th style="text-align: center;">6月</th>
							<th style="text-align: center;">7月</th>
							<th style="text-align: center;">8月</th>
							<th style="text-align: center;">9月</th>
							<th style="text-align: center;">10月</th>
							<th style="text-align: center;">11月</th>
							<th style="text-align: center;">12月</th>
							<th style="text-align: center;">1月</th>
							<th style="text-align: center;">2月</th>
							<th style="text-align: center;">3月</th>
						</tr>
						<tr>
							<td style="text-align: center;">勤務時間</td>
							<c:forEach var="detailList" items="${attendanceSummaryInfoFrm.detailList}" varStatus="status">
								<td style="text-align: center;" id="${detailList.total_work_hour}">
									<c:out value="${detailList.total_work_hour}"/></td>
							</c:forEach>	
						</tr>
						<tr>
							<td style="text-align: center;">法定時間外</td>
							<c:forEach var="detailList" items="${attendanceSummaryInfoFrm.detailList}" varStatus="status">
								<td style="text-align: center;" id="${detailList.legal_overtime}">
									<c:out value="${detailList.legal_overtime}"/></td>
							</c:forEach>
						</tr>
						<tr>
							<td style="text-align: center;">法定休日</td>
							<c:forEach var="detailList" items="${attendanceSummaryInfoFrm.detailList}" varStatus="status">
								<td style="text-align: center;" id="${detailList.legal_over_workday}">
									<c:out value="${detailList.legal_over_workday}"/></td>
							</c:forEach>
						</tr>
						<tr>
							<td style="text-align: center; ">深夜早朝</td>
							<c:forEach var="detailList" items="${attendanceSummaryInfoFrm.detailList}" varStatus="status">
								<td style="text-align: center;" id="${detailList.midnight_work_hour}">
									<c:out value="${detailList.midnight_work_hour}"/></td>
							</c:forEach>
						</tr>
						<tr>
							<td style="text-align: center;">全休</td>
							<c:forEach var="detailList" items="${attendanceSummaryInfoFrm.detailList}" varStatus="status">
								<td style="text-align: center;" id="${detailList.wholeday_leave}">
									<c:out value="${detailList.wholeday_leave}"/></td>
							</c:forEach>
						</tr>
						<tr>
							<td style="text-align: center;">半休</td>
							<c:forEach var="detailList" items="${attendanceSummaryInfoFrm.detailList}" varStatus="status">
								<td style="text-align: center;" id="${detailList.halfday_leave}">
									<c:out value="${detailList.halfday_leave}"/></td>
							</c:forEach>
							</tr>
						<tr>
							<td style="text-align: center;">特別休暇</td>
							<c:forEach var="detailList" items="${attendanceSummaryInfoFrm.detailList}" varStatus="status">
								<td style="text-align: center;" id="${detailList.releaving_leave}">
									<c:out value="${detailList.releaving_leave}"/></td>
							</c:forEach>
							</tr>
						<tr>
							<td style="text-align: center;">代休</td>
							<c:forEach var="detailList" items="${attendanceSummaryInfoFrm.detailList}" varStatus="status">
								<td style="text-align: center;" id="${detailList.compensatory_leave}">
									<c:out value="${detailList.compensatory_leave}"/></td>
							</c:forEach>
						</tr>
					</table>
				</div>
				<div class="row">
					<div class="form-group">
						<div class="col-md-12" align="center">
							<button type="submit" class="btn btn-success" id="btnClose">閉じる</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script src="resources/js/jquery-2.1.4.min.js"></script>
	<script src="resources/js/validator.min.js"></script>
	<script src="resources/js/bootstrap.min.js"></script>
	<script src="resources/js/jquery-ui.min.js"></script>
	<script type="text/javascript">
		$("#btnClose").click(function() {
			document.attendanceSummaryInfoFrm.action = "home";
			document.attendanceSummaryInfoFrm.submit();
		});

		function setProcess() {
			document.attendanceSummaryInfoFrm.action = "AttendanceSummaryInfo";
			document.attendanceSummaryInfoFrm.submit();
		}
	</script>
</body>
</html>