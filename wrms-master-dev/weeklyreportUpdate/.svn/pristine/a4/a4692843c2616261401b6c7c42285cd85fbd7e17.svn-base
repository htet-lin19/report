<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>社員一覧</title>
<link href="resources/css/bootstrap.min.css" rel="stylesheet">
<link href="resources/css/jquery.dataTables.min.css" rel="stylesheet">
<link href="resources/css/mdl_modified.css" rel="stylesheet">
<link href="resources/css/mdl2_modified.css" rel="stylesheet">
<link href="resources/css/jquery-ui.css" rel="stylesheet">
<link href="resources/css/bootstrap-toggle.min.css" rel="stylesheet">
<link href="resources/css/navbar-fixed-top.css" rel="stylesheet">
<link rel="shortcut icon" href="resources/img/favicon.jpg" />
<link href="resources/css/fixedColumns.dataTables.min.css" rel="stylesheet">
<link href="resources/css/dataTables.bootstrap.min.css" rel="stylesheet">
<!-- Table Style/Modal Style -->
<style>
.mdl-body {
	height: 480px;
	overflow-y: auto;
}

.mdl:target+.mdl-dialog {
	top: 5%;
	width:35%;
}

.table{
  	margin: 0 auto;
  	clear: both;
 	border-collapse: collapse;
  	table-layout: fixed; 
  	overflow-y: auto;
  	overflow-x: auto;
   	word-wrap:break-word; 
} 

div.dataTables_wrapper {
	width: 1300px;
	margin: 0 auto;
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

	<s:form name="employeeMasterFrm" method="post" theme="simple" cssClass="form-horizontal">
		<div><jsp:include page="menu.jsp"></jsp:include></div>
		<!-- Search -->
		<div class="col-md-10 col-md-offset-1">
		
			<div class="panel panel-primary">
				<div class="panel-heading">社員一覧</div>
				<div class="row" style="margin-top: 10px;">
					<div class="col-md-6 col-md-offset-3">	
						<s:if test="hasActionMessages()">
					   		<div class="alert alert-success" id="msg">
					      		<s:actionmessage/>
					   		</div>
						</s:if>		
					</div>
				</div>
				<div class="row" style="margin-top: 10px;">
					<div class="col-md-4">
						<div class="form-group">
							<label for="emp_cd" class="control-label col-md-4">社員番号</label>
							<div class="col-md-8">
								<input type='text' class="form-control" name="employeeMasterFrm.emp_cd"
									id="emp_cd" maxlength="8" value="${employeeMasterFrm.emp_cd}" />
							</div>
						</div>
					</div>
					<div class="col-md-4">
						<div class="form-group">
							<label for="emp_name" class="control-label col-md-4">社員名</label>
							<div class="col-md-8">
								<input type='text' class="form-control" name="employeeMasterFrm.emp_name"
									id="emp_name" value="${employeeMasterFrm.emp_name}" maxlength="20" />
							</div>
						</div>
					</div>
					<div class="col-md-4">
						<button type="submit" class="btn btn-success" id="btn_search">検索</button>
						<button type="button" class="btn btn-danger" id="btn_clear">キャンセル</button>
					</div>
					</div>
				</div>
			</div>
		</div>
		<!-- Data Table -->
		<div class="container-fluid" id="panel-body">
			<div class="col-md-12">
				<table id="employee-list" class="stripe row-border cell-border order-column" style="width: 100%">
					<thead>
						<tr>
							<th style="text-align: center;" class="col-md-2">社員番号</th>
							<th style="text-align: center;" class="col-md-2">社員名</th>
							<th style="text-align: center;" class="col-md-2">グループ名</th>
							<th style="text-align: center;" class="col-md-2">職位</th>
							<th style="text-align: center;" class="col-md-2">GL</th>
							<th style="text-align: center;" class="col-md-2">メールアドレス</th>
							<th style="text-align: center;" class="col-md-1">更新</th>
							<th style="text-align: center;" class="col-md-1">削除</th>
						</tr>
					</thead>

					<c:forEach var="employeeList" items="${employeeMasterFrm.employeeFrmDetail}" varStatus="status">
						<tr style="text-align: right;" id="${employeeList.id}">
							<td style="text-align: left;" id="emp_cd${employeeList.id }"><c:out
									value="${employeeList.emp_cd}"></c:out>
							<td style="text-align: left;" id="emp_name${employeeList.id}"><c:out
									value="${employeeList.emp_name}" /></td>
							<td style="text-align: left;" id="group_name${employeeList.id}"><c:out
									value="${employeeList.group_name}" />
							</td>
							<td style="text-align: left;" id="position${employeeList.id}"><c:out
									value="${employeeList.position}" /></td>
							<td style="text-align: left;vertical-align: text-top;" id="gl_flag${employeeList.id}">
								<c:set var="gl" value="${employeeList.gl_flag}"/>
								<c:if test="${gl == 1}"><c:out value="GL" /></c:if>
							</td>
							<td style="text-align: left;" id="emp_email${employeeList.id}"><c:out
									value="${employeeList.emp_mail}" /></td>
							<td style="text-align: center;"><a
								href="javascript:editEmployee(${employeeList.id })"><span
									class="glyphicon glyphicon-pencil"></span></a></td>
							<td style="text-align: center;"><a
								href="javascript:deleteEmployee(${employeeList.id});"><span
									class="glyphicon glyphicon-trash"></span></a></td>
						</tr>
						<input id="group_cd${employeeList.id}" value="${employeeList.group_cd}"
							style="display: none;">
						<input id="role${employeeList.id}" value="${employeeList.role}"
							style="display: none;">
						
					</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
		
		<!-- Delete Modal -->
		<a href="#" class="mdl2" id="mdl_delete" aria-hidden="true"></a>
		<div class="mdl2-dialog">
			<div class="mdl2-header">
				<h2>社員削除</h2>
				<a href="#" class="btn-close" aria-hidden="true">×</a>
			</div>
			<div class="mdl2-body">
				<div class="form-group">
					<label for="delete_id" class="control-label">削除したいですか。</label> 
					<s:hidden name="employeeMasterFrm.delete_id" id="delete_id"></s:hidden>
				</div>
			</div>
			<div class="mdl2-footer">
				<button type="submit" class="btn btn-default" id="btn_deleteOk">削除</button>
				<button type="button" class="btn btn-default"
					OnClick="closemdl_delete()">キャンセル</button>
			</div>
		</div>
		<s:hidden name="employeeMasterFrm.button_event" value="search"></s:hidden>
		<s:hidden id="search" name="employeeMasterFrm.search"></s:hidden>
	</s:form>

		<!-- Edit Modal -->
		<a href="#" class="mdl" id="mdl_edit" aria-hidden="true"></a>
		<div class="mdl-dialog">
			<div class="mdl-header">
				<h2>社員更新</h2>
				<a href="#" class="btn-close" aria-hidden="true">×</a>
			</div>
			<div class="mdl-body">
				<form class="form-horizontal" method="post" action="contactform" name="employeeMasterFrmDetail">
					<div class='alert alert-danger' id="Error" style="display: none;"></div>
					<div class="form-group">
						<s:hidden name="employeeMasterFrmDetail.id" id="edit_id"></s:hidden>
					</div>
					<div class="form-group">
						<label for="edit_emp_cd" class="control-label"><font color="red">*</font>社員番号</label> <input
							type="text" name="employeeMasterFrmDetail.emp_cd" class="form-control" id="edit_emp_cd" maxlength="8" disabled>
					</div>
					<div class="form-group">
						<label for="edit_emp_name" class="control-label"><font color="red">*</font>社員名</label> <input
							type="text" class="form-control" id="edit_emp_name" name="employeeMasterFrmDetail.emp_name"
							maxlength="20" value="">
					</div>
					<div class="form-group">
						<label for="edit_emp_email" class="control-label"><font color="red">*</font>メールアドレス</label> <input
							type="text" name="employeeMasterFrmDetail.emp_mail" class="form-control" id="edit_emp_email" maxlength="30" value="">
					</div>
					<div class="form-group">
						<label for="edit_group_name" class="control-label">グループ名</label> 
							
						<select name="employeeMasterFrmDetail.group_cd" class="form-control" required="required" id="edit_group_cd">
							<option value="-1">Select</option>
							<c:forEach var="groupList" items="${employeeMasterFrmDetail.groupList}" varStatus="status">	
								<c:choose>
								    <c:when test="${edit_group_cd eq groupList.group_cd}">
								        <option value="${groupList.group_cd}" selected="selected">${groupList.group_name}</option>
								    </c:when>
								    <c:otherwise>
								        <option value="${groupList.group_cd}">${groupList.group_name}</option>
								    </c:otherwise>
								</c:choose>
							</c:forEach>
						</select> 
					</div>
					
					<div class="form-group">
						<label for="edit_dropPosition" class="control-label"><font color="red">*</font>職位</label>
						<div class="btn-group" data-toggle="buttons">
							<label class="col-md-4 btn btn-default"> <input
								type="radio" name="employeeMasterFrmDetail.role" value="1"
								id="position1" /> マネージャー
							</label> <label class="col-md-5 btn btn-default"> <input
								type="radio" name="employeeMasterFrmDetail.role" value="2"
								id="position2" /> グループリーダー
							</label> <label class="col-md-3 btn btn-default"> <input
								type="radio" name="employeeMasterFrmDetail.role" value="3"
								id="position3" /> メンバー
							</label>
						</div>
					</div>
					<div class="form-group">
						<label for="edit_gl_flag" class="control-label">GL</label>
						<s:checkbox  name="employeeMasterFrmDetail.gl_flag" id="edit_gl_flag" style="width:10px;height:30px;transform:scale(1.5);"/> 
					</div>
				</form>
			</div>
			<div class="mdl-footer">
				<button type="submit" class="btn btn-default" id="btn_editOk">更新</button>
				<button type="button" class="btn btn-default"
					OnClick="closemdl_edit()">キャンセル</button>
			</div>
		</div>
		
	<script src="resources/js/jquery.js"></script>
	<script src="resources/js/jquery.dataTables.min.js"></script>
	<script src="resources/js/bootstrap-datepicker.js"></script>
	<script src="resources/js/bootstrap.min.js"></script>
	<script src="resources/js/jquery-ui.min.js"></script>
	<script src="resources/js/dataTables.editor.min.js"></script>
	<script src="resources/js/bootstrap-toggle.min.js"></script>
	<script src="resources/js/dataTables.fixedColumns.min.js"></script>

	<script type="text/javascript">
		$(document).ready(function() {
			$('#employee-list').dataTable({
				scrollY : false,
				scrollX : true,
				scrollCollapse : true,
				paging : true,
				
				"lengthMenu" : [ [ 10, 25 ], [ 10, 25 ] ]
			});
			
			$('[data-toggle="tooltip"]').tooltip();

			$("#btn_deleteOk").click(function() {
				var id = document.getElementById('delete_id').value;
				document.employeeMasterFrm.action = "EmployeeListDelete?id="+ id;
				document.employeeMasterFrm.submit();
			});
			
			$("#btn_editOk").click(function() {
				var boo;
				if(!validateform())
					boo = false;	
				else
					boo = true;
				
				if(!boo)
					return false;
				document.employeeMasterFrmDetail.action = "EmployeeListEdit";
				document.employeeMasterFrmDetail.submit();
			});
			
			$("#btn_search").click(function() {
				document.employeeMasterFrm.action = "EmployeeListSearch";
				document.employeeMasterFrm.submit();
			});
			$("#btn_clear").click(function() {
				document.getElementById("emp_cd").value = '';
				document.getElementById("emp_name").value = '';
				$('#msg').html("");
			    $('#msg').hide();
			});
		});

		function editEmployee(val) {
			$('#Error').hide();
			var id = val.id;
			if (!isNaN(val)) {
				id = val;
			} else {
				id = val.id;
			}
			var emp_cd = document.getElementById('emp_cd' + id).innerHTML.trim();
			var emp_name = document.getElementById('emp_name' + id).innerHTML.trim();
			var group_name = document.getElementById('group_name' + id).innerHTML.trim();

			if (group_name == "") {
				group_name = "-1";
			}
			var group_cd = document.getElementById('group_cd' + id).value.trim();
			var position = document.getElementById('role' + id).value.trim();
			var gl_flag = document.getElementById('gl_flag' + id).innerHTML.trim();
			var emp_email = document.getElementById('emp_email' + id).innerHTML.trim();
			
			document.getElementById('edit_id').value = id;
			document.getElementById('edit_emp_cd').value = emp_cd;
			document.getElementById('edit_emp_name').value = emp_name;
			document.getElementById('edit_group_cd').value = group_cd;
			document.getElementById('edit_emp_email').value = emp_email;

			if (position == "1") {
				$('#position1').click();
			} else if (position == "2") {
				$('#position2').click();
			} else {
				$('#position3').click();
			}
			
			if (gl_flag.trim() == 'GL')
				document.getElementById('edit_gl_flag').checked = true;
			else
				document.getElementById('edit_gl_flag').checked = false;
			
			location.href = "#mdl_edit";
		}
		
		function deleteEmployee(val) {
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
			document.employeeMasterFrm.action = "EmployeeListSearch";
			document.employeeMasterFrm.submit();
		}

		function closemdl_edit() {
			$('#mdl_delete').modal('hide');
			document.employeeMasterFrm.action = "EmployeeListSearch";
			document.employeeMasterFrm.submit();
		}
		function validateform() {
			var number = document.getElementById('edit_emp_cd').value.trim();
			var name = document.getElementById('edit_emp_name').value.trim();
			var email=document.getElementById('edit_emp_email').value;
			var reg= /^[a-z][a-zA-Z0-9_.]*(\.[a-zA-Z][a-zA-Z0-9_.]*)?@gicjp*\.[a-z]+(\.[a-z]+)?$/;
			if (number == null || number == "" || number.length == 0) {
				message = " 『社員番号』 を入力してください。";
				$('#Error').html(
						"<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"
								+ message);
				$('#Error').show();
				return false;
			}
			if(email==null || email=="" || email.length==0){
				message =" 『メールアドレス』 を入力してください。";
				$('#Error').html("<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"+message);
				$('#Error').show();   
			   	return false;
			}

			if(!reg.test(email)) {
				message =" 『メールアドレス』 でGICメールを記入してください。";
				$('#Error').html("<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"+message);
				$('#Error').show();   
			   	return false;
			}
			if (name == null || name == "" || name.length == 0) {
				message = " 『社員名』 を入力してください。";
				$('#Error').html(
						"<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"
								+ message);
				$('#Error').show();
				return false;
			}
			else {
				return true;
			}
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