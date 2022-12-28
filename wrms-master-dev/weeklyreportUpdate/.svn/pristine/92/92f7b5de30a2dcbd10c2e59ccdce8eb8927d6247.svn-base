<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/struts-bootstrap-tags" prefix="sj"%>
<!DOCTYPE>
<html>
<head>
<sj:head />
<meta charset="UTF-8">
<title>サインアップ一覧</title>
<link href="resources/css/bootstrap.min.css" rel="stylesheet">
<link href="resources/css/jquery.dataTables.min.css" rel="stylesheet">
<link href="resources/css/jquery-ui.css" rel="stylesheet">
<link href="resources/css/fixedColumns.dataTables.min.css"
	rel="stylesheet">
<link href="resources/css/mdl_modified.css" rel="stylesheet">
<link href="resources/css/navbar-fixed-top.css" rel="stylesheet">
<link href="resources/css/dataTables.bootstrap.min.css" rel="stylesheet">
<link rel="shortcut icon" href="resources/img/favicon.jpg" />
<style>
/* .mdl:target+.mdl-dialog {
	top: 5%;
}
 */
div.dataTables_wrapper {
	width: 1300px;
	margin: 0 auto;
}
</style>
</head>
<body onload="hideDiv()">
	<%
	    session=request.getSession(false);
	    if(session.getAttribute("ID")==null)
	        response.sendRedirect("sessionTimeOut.jsp");
	%>
	<s:form name="signUpForm" method="post" theme="simple">
		<div><jsp:include page="menu.jsp"></jsp:include></div>
		<!-- Search Form -->
		<div class="col-md-8 col-md-offset-2">
			<div class="panel panel-primary">
				<div class="panel-heading">サインアップ一覧</div>
				<div class="panel-body">
				<div class="row">
					<div class="col-md-4">
						<div class="form-group">
							<label for="emp_cd" class="control-label col-md-5"><font color="red">*</font>社員番号</label>
							<div class="col-md-7">
								<input type='text' class="form-control" name="signUpForm.emp_cd" id="emp_cd" value="${signUpForm.emp_cd}" maxlength="8" />
							</div>
						</div>
					</div>

					<div class="col-md-4">
						<div class="form-group">
							<label for="emp_name" class="control-label col-md-5">社員名</label>
							<div class="col-md-7">
								<input type='text' class="form-control" name="signUpForm.emp_name" id="emp_name" value="${signUpForm.emp_name}" maxlength="20" />
							</div>
						</div>
					</div>
						<div class="col-md-4">
							<button type="button" class="btn btn-default" id="btn_searchOk">検索</button>
							<button type="button" class="btn btn-success" id="btn_clear">キャンセル</button>
						</div>
				</div>
				</div>
			</div>
		</div>
		<!--End of Search Form -->

		<div class="container-fluid" id="panel-body">
			<div class="col-md-12">
				<table id="login-list"
					class="stripe row-border cell-border order-column"
					style="width: 230%">
					<thead>
						<tr>
							<th style="text-align: center;" class="col-md-2">社員番号</th>
							<th style="text-align: center;" class="col-md-2">社員名</th>
							<th style="text-align: center;" class="col-md-2">パスワード</th>
							<th style="text-align: center;" class="col-md-1">更新</th>
							<th style="text-align: center;" class="col-md-1">削除</th>
						</tr>
					</thead>

					<c:forEach var="signUp" items="${signUpForm.signUpFormList}">
						<tr style="text-align: right;" id="${signUp.id}">
							<td style="text-align: left;" id="list_empcd${signUp.id}"><c:out value="${signUp.emp_cd}" /></td>
							<td style="text-align: left;" id="list_empname${signUp.id}"><c:out value="${signUp.emp_name}" /></td>
							<td style="text-align: left;" id="list_password${signUp.id}">***</td>
							<td style="text-align: center;"><a href="javascript:editLogin(${signUp.id})">
								<span class="glyphicon glyphicon-pencil"></span></a>
							</td>
							<td style="text-align: center;"><a href="javascript:deleteLogin(${signUp.id});">
								<span class="glyphicon glyphicon-trash"></span></a>
							</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
		<a href="#" class="mdl" id="mdl_delete" aria-hidden="true"></a>
		<div class="mdl-dialog">
			<div class="mdl-header">
				<h2>サインアップ削除</h2>
				<a href="#" class="btn-close" aria-hidden="true">×</a>
			</div>
			<div class="mdl-body">
				<div class="form-group">
					<label for="delete_id" class="control-label">削除したいですか。</label> <input
						type="hidden" class="form-control" id="delete_id" name="signUpForm.delete_id">
				</div>
			</div>
			<div class="mdl-footer">
				<button type="submit" class="btn btn-default" id="btn_deleteOk">削除</button>
				<button type="button" class="btn btn-default"
					OnClick="closemdl_delete()">キャンセル</button>
			</div>
		</div>
		<s:hidden name="signUpForm.button_event" value="search"></s:hidden>
		<s:hidden id="search" name="signUpForm.search"></s:hidden>
	</s:form>
	<a href="#" class="mdl" id="mdl_edit" aria-hidden="true"></a>
	<div class="mdl-dialog">
		<div class="mdl-header">
			<h2>サインアップ更新</h2>
			<a href="#" class="btn-close" aria-hidden="true">×</a>
			<div class='alert alert-danger' id="Error" style="display: none;"></div>
		</div>
		<div class="mdl-body">
			<form class="form-horizontal" method="post" action="" name="signUpListtEidtFrm">
				<s:hidden name="signUpListtEidtFrm.edit_id" id="edit_id"></s:hidden>
				<div class="form-group">
					<label for="edit_emp_cd" class="control-label">社員番号</label> 
					<input type="text" class="form-control" name="signUpListtEidtFrm.emp_cd" id="edit_emp_cd" disabled>
				</div>
				<div class="form-group">
					<label for="edit_emp_name" class="control-label"><font color="red">*</font>社員名</label> 
					<input type="text" class="form-control" name="signUpListtEidtFrm.emp_name" id="edit_emp_name" maxlength="20" value=""/>
				</div>
				<div class="form-group">
					<label for="edit_password" class="control-label"><font color="red">*</font>パスワード</label> 
					<input type="password" class="form-control" name="signUpListtEidtFrm.password" id="edit_password" maxlength="20" value=""/>
				</div>

				<div class="form-group">
					<div class='alert alert-danger' id="error_calendarlist"
						style="display: none; margin-bottom: -15px;"></div>
				</div>
				<div class="mdl-footer">
					<button type="button" class="btn btn-default" id="btn_editOk">更新</button>
					<button type="button" class="btn btn-default"
						OnClick="closemdl_edit()">キャンセル</button>
				</div>
			</form>
		</div>
	</div>
	<script src="resources/js/jquery-2.1.4.min.js"></script>
	<script src="resources/js/bootstrap.min.js"></script>
	<script src="resources/js/jquery-ui.min.js"></script>
	<script src="resources/js/dataTables.fixedColumns.min.js"></script>
	<script src="resources/js/jquery.dataTables.min.js"></script>

	<script type="text/javascript">
	
	$(document).ready(function() {
		$('#login-list').dataTable({
			scrollY:        false,
		     scrollX:        false,
		     scrollCollapse: true,
		     paging:         true,
		     "lengthMenu": [[10, 25], [10, 25]]
		});
		$('#edit_choose_date').datepicker({
			dateFormat: "yy-mm-dd",
			changeMonth: true,
			changeYear: true
		});
		$('#yearAndMonth').datepicker({
			dateFormat: "yy-mm-dd",
			changeMonth: true,
			changeYear: true
		});
	});
	$(document).ready(function(){
		$('body').on('click', function (e) {
			  $('[data-toggle="popover"]').each(function () {
		          if (!$(this).is(e.target) && $(this).has(e.target).length === 0 && $('.popover').has(e.target).length === 0) {
		              $(this).popover('hide');
		          }
		      });
		});
			
		$('[data-toggle="popover"]').popover({
			html:true
		});
		$('[data-toggle="popover"]').popover({
	        container: 'body'
	    }); 
		$("#btn_editOk").click(function(){
			var boo;
			if(!validateform())
				boo = false;		
			else
				boo = true;
			
			if(!boo)
				return false;
		    document.signUpListtEidtFrm.action="SignUpListEdit";
	        document.signUpListtEidtFrm.submit();
		});
		$("#btn_deleteOk").click(function(){
		    document.signUpForm.action="SignUpListDelete";
	        document.signUpForm.submit(); 
			});
		$("#btn_searchOk").click(function(){
			document.signUpForm.action="SignUpListSearch";
			document.signUpForm.submit();
		});
		$("#btn_clear").click(function(){
			document.getElementById("emp_cd").value='';
			document.getElementById("emp_name").value='';
			
		});
	});

	function editLogin(val){
		$('#Error').hide();
		var id= val.id;
		if(!isNaN(val)){
			id=val;
		}else{
			id=val.id;
		} 
		var emp_cd = document.getElementById('list_empcd'+id).innerHTML;
	    var emp_name = document.getElementById('list_empname'+id).innerHTML;
	    var password = document.getElementById('list_password'+id).innerHTML;
	    document.getElementById('edit_id').value = id;
	    document.getElementById('edit_emp_cd').value = emp_cd;
	    document.getElementById('edit_emp_name').value = emp_name;
	    document.getElementById('edit_password').value = password;
	    location.href = "#mdl_edit";
	}
	function deleteLogin(val){
		var id;
		if(!isNaN(val)){
			id=val;
		} else {
			id=val.id;
		}
		document.getElementById('delete_id').value = id;
		location.href = "#mdl_delete";
	}
	function closemdl_delete(val){
		$('#mdl_delete').modal('hide');
		document.signUpForm.action = "SignUpListSearch";
		document.signUpForm.submit();
	}
	function closemdl_edit(){
		$('#mdl_edit').modal('hide');
		document.signUpForm.action = "SignUpListSearch";
		document.signUpForm.submit();
	}
	function validateform(){
		 var emp=document.getElementById('edit_emp_cd').value.trim();
		 var name=document.getElementById('edit_emp_name').value.trim();
		 var password=document.getElementById('edit_password').value.trim();
		 
		 if(emp==null || emp=="" || emp.length==0){
			  message =" 『社員番号』 を入力してください。";
				$('#Error').html("<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"+message);
			      $('#Error').show();   
		   return false;
			  }
		 	
		   if(name==null || name=="" || name.length==0){
			  message =" 『社員名』 を入力してください。";
				$('#Error').html("<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"+message);
			      $('#Error').show();   
		   return false;
			  }
		
		  if(password==null || password=="" || password.length==0){
				  message =" 『パスワード』 を入力してください。";
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