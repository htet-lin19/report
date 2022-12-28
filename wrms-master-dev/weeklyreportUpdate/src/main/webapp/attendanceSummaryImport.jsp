<!-- 勤務時間サマリ情報インポート画面	2020/10/06 Theint Sandari Kyaw 新規作成　勤務時間サマリ情報をインポートする -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/struts-bootstrap-tags" prefix="sj"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>勤務時間サマリ情報登録</title>
<link href="resources/css/bootstrap.min.css" rel="stylesheet">
<link href="resources/css/jquery-ui.css" rel="stylesheet">
<link href="resources/css/jquery-clockpicker.min.css" rel="stylesheet">
<link href="resources/css/timepicki.css" rel="stylesheet">
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

	<script src="resources/js/jquery-2.1.4.min.js"></script>
	<script src="resources/js/validator.min.js"></script>
	<script src="resources/js/bootstrap.min.js"></script>
	<script src="resources/js/jquery-ui.min.js"></script>
	<script src="resources/js/jquery-clockpicker.min.js"></script>
	<script src="resources/js/timepicker.js"></script>
	<script src="resources/js/bootstrap-filestyle.min.js"></script>
	<script src="resources/js/excelImport.js"></script>

	<div class="row">
		<div class="col-md-10 col-md-offset-1">
			<div class="panel panel-warning">
				<div class="panel-heading"><strong>勤務時間サマリ情報インポート</strong></div>
				<div class="panel-body">
					<div class="row">
						<div class="col-md-6 col-md-offset-3">
							<s:if test="hasActionErrors()">
								<div class="alert alert-danger fade in" id="error">
									<a href="#" class="close" data-dismiss="alert">&times;</a>
									<s:actionerror />
								</div>
							</s:if>
						</div>
						<div class="col-md-6 col-md-offset-3">
							<s:if test="hasActionMessages()">
								<div class="alert alert-success" id="msg">
									<a href="#" class="close" data-dismiss="alert">&times;</a>
									<s:actionmessage />
								</div>
							</s:if>
						</div>
					</div>
					<s:form id="attendanceSummaryImport" action="attendanceSummaryImport"
						method="post" enctype="multipart/form-data" theme="bootstrap">
						<div class="row">
							<div class="col-md-3">
								<div class="form-group">
									<div class="col-md-12">
										<s:select name="month" list="months" listValue="month"
											cssClass="form-control" />
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<div class="col-md-12">
										<input type="file" name="file" class="filestyle"
											data-placeholder="No file" accept=".xlsx,.xls">
									</div>
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<div class="col-md-9">
										<s:submit value="アップロード"
											cssClass="glyphicon glyphicon-upload btn btn-success" />
									</div>
								</div>
							</div>
						</div>
					</s:form>
					<s:if test="%{!getReportTimes().isEmpty()}">
						<s:form action="insertSummaryImportList" enctype="multipart/form-data" method="post">
							<s:hidden name="month"/>
							<div class="row container-fluid">
								<div class="table-responsive">
									<table class="table table-hover table-striped table-bordered">
										<tr class="success">
											<th class="text-center" width="5%">No.</th>
											<th class="text-center" width="5%">社員番号</th>
											<th class="text-center" width="13%">氏名</th>
											<th class="text-center" width="5%">総労働時間</th>
											<th class="text-center" width="8%">法定時間外労働時間</th>
											<th class="text-center" width="8%">法定休日労働時間</th>
											<th class="text-center" width="8%">深夜早朝労働時間</th>
										</tr>
										<tbody>
											<s:iterator value="reportTimes" var="report" status="stat">
												<tr>
													<s:if test="%{#stat.count!=reportTimes.size()+1}">
														<td class="text-center"><s:property
																value="%{#stat.count}" /></td>
													</s:if>
													<s:else>
														<td></td>
													</s:else>

													<s:iterator value="#report.excelData" var="data"
														status="rowCount">
														<s:if test="%{#rowCount.index==7 && #data!=''}">
															<td class="text-center"><div class="col-sm-12">
																	<p class="text-viewer">
																		<s:property />
																	</p>
																</div></td>
														</s:if>
														<s:else>
															<td class="text-center"><s:property /></td>
														</s:else>
													</s:iterator>
												</tr>
											</s:iterator>
										</tbody>
									</table>
								</div>
							</div>
							<div class="row">
								<s:submit value="登録 "
									cssClass="btn btn-success center-block active" />
							</div>
						</s:form>
					</s:if>
				</div>
			</div>
		</div>
	</div>
</body>
</html>