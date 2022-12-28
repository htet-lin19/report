<!-- 勤務時間インポート
 作成履歴：2020/10/19 GICM AMTD
 作成概要：更新　勤務時間情報をインポートする -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/struts-bootstrap-tags" prefix="sj"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>勤務時間登録</title>
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
				<div class="panel-heading"><strong>勤務時間インポート</strong></div>
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
					<s:form id="attendanceImport" action="attendanceImport"
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
						<s:form action="insertImportList" enctype="multipart/form-data" method="post">
							<s:hidden name="month"/>
							<div class="row container-fluid">
								<div class="table-responsive">
									<table class="table table-hover table-striped table-bordered">
										<tr class="success">
											<th class="text-center" width="5%">No.</th>
											<th class="text-center" width="5%">日付</th>
											<!-- <th class="text-center" width="5%">曜日</th>
											<th class="text-center" width="5%">休日<br>割増<br>扱い
											</th>
											<th class="text-center" width="5%">開始<br>時刻
											</th>
											<th class="text-center" width="5%">終了<br>時刻
											</th>
											<th class="text-center" width="5%">休憩<br>時間
											</th>
											<th class="text-center" width="5%">勤務<br>時間
											</th>
											<th class="text-center" width="6%">時間外<br>勤務<br>(深夜外)
											</th>
											<th class="text-center" width="5%">深夜<br>時間<br>外勤務
											</th>
											<th class="text-center" width="6%">代休<br> 勤務<br>(深夜外)
											</th>
											<th class="text-center" width="5%">深夜<br> 代休<br>勤務
											</th>
											<th class="text-center" width="5%">代休<br> 取得
											</th>
											<th class="text-center" width="13%">備考</th>
											<th class="text-center" width="5%">所属長<br> 確認
											</th> -->
											<!-- 2020/10/19 GICM AMTD 対応　start -->
											<th class="text-center" width="6%">曜日</th>
											<th class="text-center" width="6%">休日<br>割増<br>扱い
											</th>
											<th class="text-center" width="6%">開始<br>時刻
											</th>
											<th class="text-center" width="6%">終了<br>時刻
											</th>
											<th class="text-center" width="6%">休憩<br>時間
											</th>
											<th class="text-center" width="6%">勤務<br>時間
											</th>
											<th class="text-center" width="6%">法定<br>時間外
											</th>
											<th class="text-center" width="6%">法定<br>休日
											</th>
											<th class="text-center" width="6%">深夜<br>早朝
											</th>
											<th class="text-center" width="13%">その他
											</th>
											<th class="text-center" width="13%">備考</th>
											<th class="text-center" width="7%">所属長<br> 確認
											</th>
											<!-- 2020/10/19 GICM AMTD 対応　end -->
										</tr>
										<tbody>
											<s:iterator value="reportTimes" var="report" status="stat">
												<tr>
													<s:if test="%{#stat.count!=reportTimes.size()}">
														<td class="text-center"><s:property
																value="%{#stat.count}" /></td>
													</s:if>
													<s:else>
														<td></td>
													</s:else>

													<s:iterator value="#report.excelData" var="data"
														status="rowCount">
														<s:if test="%{#rowCount.index==12 && #data!=''}">
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