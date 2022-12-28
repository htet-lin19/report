<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">
<title>週報一覧</title>
<link href="resources/css/bootstrap.min.css" rel="stylesheet">
<link href="resources/css/jquery.dataTables.min.css" rel="stylesheet">
<link href="resources/css/fixedColumns.dataTables.min.css"
	rel="stylesheet">
<link href="resources/css/mdl_modified.css" rel="stylesheet">
<link href="resources/css/mdl1_modified.css" rel="stylesheet">
<link href="resources/css/mdl2_modified.css" rel="stylesheet">
<link href="resources/css/jquery-ui.css" rel="stylesheet">
<link href="resources/css/navbar-fixed-top.css" rel="stylesheet">
<link href="resources/css/dataTables.bootstrap.min.css" rel="stylesheet">
<link rel="shortcut icon" href="resources/img/favicon.jpg" />

<style>
.mdl-body {
	height: 480px;
	overflow-y: auto;
}

.mdl:target+.mdl-dialog {
	top: 5%;
}

.mdl1-body {
	height: 560px;
	width: 1340px;
	text-align: center;
	overflow-y: auto;
}

.table {
	margin: 0 auto;
	width: 100%;
	clear: both;
	border-collapse: collapse;
	table-layout: fixed;
	word-wrap: break-word;
}

.mdl1:target+.mdl1-dialog {
	top: 2%;
	width: 1340px;
}

div.dataTables_wrapper {
	width: 1300px;
	margin: 0 auto;
}

div.excelbtn{
	padding: 0px 0px 15px 0px;
}
</style>
</head>
<body onload="hideDiv()">
	<%
		session = request.getSession(false);
		if (session.getAttribute("ID") == null) {
			response.sendRedirect("sessionTimeOut.jsp");
		}
	%>

	<s:form name="reportContentListFrm" method="post" theme="simple" autocomplete="off">
		<div><jsp:include page="menu.jsp"></jsp:include></div>
		<div class="col-md-10 col-md-offset-1">
			<div class="panel panel-success">
				<div class="panel-heading">週報一覧</div>
				<div class="row" style="margin-top: 10px;">
					<div class="col-md-6 col-md-offset-3">
						<s:if test="hasActionMessages()">
							<div class="alert alert-success" id="msg">
								<s:actionmessage />
							</div>
						</s:if>
					</div>
				</div>
				<div class="panel-body">
					<div class="row">
						<div class="col-md-4">
							<div class="form-group">
								<label for="emp_cd" class="control-label col-md-4">社員番号</label>
								<div class="col-md-8">
									<c:if
										test="${reportContentListFrm.disable_empcd eq 'disabled'}">
										<input type='text' class="form-control"
											name="reportContentListFrm.emp_cd" id="emp_cd"
											value="${reportContentListFrm.emp_cd}" maxlength="8"
											readonly="readonly" />
									</c:if>
									<c:if test="${reportContentListFrm.disable_empcd eq ''}">
										<input type='text' class="form-control"
											name="reportContentListFrm.emp_cd" id="emp_cd"
											value="${reportContentListFrm.emp_cd}" maxlength="8" />
									</c:if>
								</div>
							</div>
						</div>
						<div class="col-md-3">
							<div class="form-group">
								<label for="emp_name" class="control-label col-md-4">社員名</label>
								<div class="col-md-8">
									<c:if
										test="${reportContentListFrm.disable_empname eq 'disabled'}">
										<input type='text' class="form-control"
											name="reportContentListFrm.emp_name" id="emp_name"
											value="${reportContentListFrm.emp_name}" maxlength="20"
											readonly="readonly" />
									</c:if>
									<c:if test="${reportContentListFrm.disable_empname eq ''}">
										<input type='text' class="form-control"
											name="reportContentListFrm.emp_name" id="emp_name"
											value="${reportContentListFrm.emp_name}" maxlength="20" />
									</c:if>
								</div>
							</div>
						</div>
						<div class="col-md-5">
							<div class="form-group">
								<label for="yearAndMonthStart" class="control-label col-md-3">期間</label>
								<div class="col-md-4">
									<input type='text' class="form-control" id="start_date"
										name="reportContentListFrm.start_date"
										value="${reportContentListFrm.start_date}" maxlength="10" />
								</div>
								<label for="yearAndMonthEnd" class="control-label col-md-1">~</label>
								<div class="col-md-4">
									<input type='text' class="form-control" id="end_date"
										name="reportContentListFrm.end_date"
										value="${reportContentListFrm.end_date}" maxlength="10" />
								</div>
							</div>
						</div>
					</div>
					<div class="row" style="margin-top: 10px;">
						<div class="col-md-4">
							<div class="form-group">
								<label for="group_name" class="control-label col-md-4">グループ名</label>
								<div class="col-md-8">
									<c:if
										test="${reportContentListFrm.disable_groupcd eq 'disabled'}">
										<select name="reportContentListFrm.group_cd"
											class="form-control" id="group_cd" disabled="disabled">
											<option value="-1">Select</option>
											<c:forEach var="groupList"
												items="${reportContentListFrm.groupList}" varStatus="status">
												<c:choose>
													<c:when
														test="${reportContentListFrm.group_cd eq groupList.group_cd}">
														<option value="${groupList.group_cd}" selected="selected">${groupList.group_name}</option>
													</c:when>
													<c:otherwise>
														<option value="${groupList.group_cd}">${groupList.group_name}</option>
													</c:otherwise>
												</c:choose>
											</c:forEach>
										</select>
									</c:if>
									<c:if test="${reportContentListFrm.disable_groupcd eq ''}">
										<select name="reportContentListFrm.group_cd"
											class="form-control" id="group_cd">
											<option value="-1">Select</option>
											<c:forEach var="groupList"
												items="${reportContentListFrm.groupList}" varStatus="status">
												<c:choose>
													<c:when
														test="${reportContentListFrm.group_cd eq groupList.group_cd}">
														<option value="${groupList.group_cd}" selected="selected">${groupList.group_name}</option>
													</c:when>
													<c:otherwise>
														<option value="${groupList.group_cd}">${groupList.group_name}</option>
													</c:otherwise>
												</c:choose>
											</c:forEach>
										</select>
									</c:if>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label for="emp_gl" class="control-label col-md-3">GL</label>
								<div class="col-md-9">
									<c:if
										test="${reportContentListFrm.disable_groupcd eq 'disabled'}">
										<s:checkbox name="reportContentListFrm.gl_flag" id="glFlag"
											style="width:10px;height:30px;transform:scale(1.5);"
											disabled="true" />
									</c:if>
									<c:if test="${reportContentListFrm.disable_groupcd eq ''}">
										<s:checkbox name="reportContentListFrm.gl_flag" id="glFlag"
											style="width:10px;height:30px;transform:scale(1.5);" />
									</c:if>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<button type="submit" class="btn btn-success col-md-5"
								id="btn_search">検索</button>
							<div class="col-md-1"></div>
							<button type="button" class="btn btn-danger col-md-5"
								id="btn_clear">キャンセル</button>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="container-fluid" id="panel-body">
			<div class="col-md-12 col-md-offset-11 excelbtn">
				<div class="col-md-2">
					<s:if test="#session.ROLE=='マネージャー'">
				   		<button type="submit" class="btn btn-success col-md-5" id="btn_excel">Excel出し</button>
					</s:if>
				</div>
			</div>
			<br>
			<div class="col-md-12">
				<table id="report-list"
					class="stripe row-border cell-border order-column"
					style="table-layout: fixed; word-wrap: break-word;">
					<thead>
						<tr>
							<th style="text-align: center;" class="col-md-2">社員番号 | 名称</th>
							<th style="text-align: center;" class="col-md-1">報告週</th>
							<th style="text-align: center;" class="col-md-1">提出日 | 更新日</th>
							<th style="text-align: center;" class="col-md-2">社員コメント</th>
							<th style="text-align: center;" class="col-md-2">GLコメント</th>
							<th style="text-align: center;" class="col-md-2">TLコメント</th>
							<th style="text-align: center;" class="col-md-2">事業部長コメント</th>
							<th style="text-align: center;" class="col-md-1">更新削除</th>
						</tr>
					</thead>

					<c:forEach var="report"
						items="${reportContentListFrm.reportConetentDetailList}"
						varStatus="status">
						<tr style="text-align: right;" id="${report.report_cd}">
							<td style="text-align: left; vertical-align: text-top;"
								id="emp_cd${report.report_cd}"><c:out
									value="${report.emp_cd}" /><br /> <c:out value="${report.emp_name}" /></td>
							<%-- <td style="text-align:left;vertical-align: text-top;" id="emp_name${report.report_cd}"><c:out value="${report.emp_name}"/></td> --%>
							<td style="text-align: right; vertical-align: text-top;"
								id="started_date${report.report_cd}"><c:out value="${report.started_date}" /></td>
							<td style="text-align:right;vertical-align: text-top;" id="updated_date${report.report_cd}">
								<c:out value="${report.created_date}"/><br /><c:out value="${report.updated_date}"/></td>
							<td style="text-align: left; vertical-align: text-top;"
								id="emp_comment${report.report_cd}"><c:out value="${report.emp_comment}" /></td>
							<td style="text-align: left; vertical-align: text-top;"
								id="gl_comment${report.report_cd}"><c:out value="${report.gl_comment}" /></td>
							<td style="text-align: left; vertical-align: text-top;"
								id="pe_comment${report.report_cd}"><c:out value="${report.pe_comment}" /></td>
							<td style="text-align: left; vertical-align: text-top;"
								id="manager_comment${report.report_cd}"><c:out value="${report.manager_comment}" /></td>

							<%-- <td style="text-align:center;vertical-align: text-top;"><a href="javascript:editreport(${report.report_cd});"><span class="glyphicon glyphicon-pencil"></span></a></td> --%>
							<td style="text-align: center; vertical-align: text-top;"><a
								href="javascript:editreport(${report.report_cd});"><span
									class="glyphicon glyphicon-pencil"></span></a> <a
								href="javascript:deletereport(${report.report_cd});"><span
									class="glyphicon glyphicon-trash"></span></a></td>
						</tr>
					</c:forEach>
				</table>
			</div>
		</div>
		<s:hidden id="hiddenId" name="hiddenId" />
		<s:hidden id="pageNo" name="pageNo" />
		<s:hidden id="search" name="reportContentListFrm.search"></s:hidden>
		<s:hidden name="reportContentListFrm.button_event" value="search"></s:hidden>
		<a href="#" class="mdl2" id="mdl_delete" aria-hidden="true"></a>
		<div class="mdl2-dialog">
			<div class="mdl2-header">
				<h2>週報削除</h2>
				<a href="#" class="btn-close" aria-hidden="true">×</a>
			</div>
			<div class="mdl2-body">
				<div class="form-group">
					<label for="delete_report_cd" class="control-label">削除したいですか。</label>
					<s:hidden id="delete_report_cd"
						name="reportContentListFrm.delete_report_cd"></s:hidden>
				</div>
			</div>
			<div class="mdl2-footer">
				<button type="submit" class="btn btn-danger" id="btn_delete">削除</button>
				<button type="button" class="btn btn-warning"
					OnClick="closemdl_delete()">キャンセル</button>
			</div>
		</div>
		
		
	</s:form>
	<script src="resources/js/jquery.js"></script>
	<script src="resources/js/jquery.dataTables.min.js"></script>
	<script src="resources/js/validator.min.js"></script>
	<script src="resources/js/bootstrap-datepicker.js"></script>
	<script src="resources/js/dataTables.fixedColumns.min.js"></script>
	<script src="resources/js/bootstrap.min.js"></script>
	<script src="resources/js/jquery-ui.min.js"></script>

	<script type="text/javascript">
		$(document).ready(function() {
			$('#report-list').dataTable({
				scrollY : false,
				scrollCollapse : true,
				paging : true,
				fixedColumns : {
					rightColumns : 3,
					leftColumns : 2,
				},
				"lengthMenu" : [ [ 4, 8 ], [ 4, 8 ] ]
			});
			
			var page =<%=session.getAttribute("pageReportList")%>;
			var table = $('#report-list').DataTable();
			table.page(page).draw(false);
			
			$('#edit_Start_Date').datepicker({
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
			$('#edit_End_Date').datepicker({
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
			$('#start_date').datepicker({
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
			$('#end_date').datepicker({
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

			$("#btn_delete").click(function() {
				document.reportContentListFrm.action = "ReportListDelete";
				document.reportContentListFrm.submit();
			});

			$("#btn_search").click(function() {
				document.reportContentListFrm.action = "ReportListSearch";
				document.reportContentListFrm.submit();
			});
			
			$("#btn_excel").click(function() {
				document.reportContentListFrm.action = "ReportListExcel";
				document.reportContentListFrm.submit();
			});

			$("#btn_clear").click(function() {
				if (!$('#emp_cd').prop('readonly'))
					document.getElementById("emp_cd").value = '';
				if (!$('#emp_name').prop('readonly'))
					document.getElementById("emp_name").value = '';
				if (!$('#group_cd').prop('disabled'))
					document.getElementById("group_cd").value = '-1';

				document.getElementById("start_date").value = '';
				document.getElementById("end_date").value = '';
				$('#msg').html("");
				$('#msg').hide();
			});
		});

		function deletereport(val) {
			var report_cd;
			if (!isNaN(val)) {
				report_cd = val;
			} else {
				report_cd = val.id;
			}
			document.getElementById('delete_report_cd').value = report_cd;
			location.href = "#mdl_delete";
		}

		function closemdl_delete(val) {
			$('#mdl_delete').modal('hide');
			document.reportContentListFrm.action = "ReportListSearch";
			document.reportContentListFrm.submit();
		}

		function hideDiv() {
			if (document.getElementById("search").value == "") {
				target = document.getElementById("panel-body");
				target.style.display = "none";
			}
		}

		function editreport(id) {
			document.getElementById('hiddenId').value = id;
			var table = $('#report-list').DataTable();
			var info = table.page.info();
			document.getElementById("pageNo").value = info.page;
			document.reportContentListFrm.action = "ReportListEdit";
			document.reportContentListFrm.submit();
		}
	</script>
</body>
</html>
