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
<title>顧客一覧</title>
<link href="resources/css/bootstrap.min.css" rel="stylesheet">
<link href="resources/css/jquery.dataTables.min.css" rel="stylesheet">
<link href="resources/css/navbar-fixed-top.css" rel="stylesheet">
<link rel="shortcut icon" href="resources/img/favicon.jpg" />
<link href="resources/css/mdl_modified.css" rel="stylesheet">
<link href="resources/css/mdl2_modified.css" rel="stylesheet">
<link href="resources/css/fixedColumns.dataTables.min.css"
	rel="stylesheet">
<link href="resources/css/dataTables.bootstrap.min.css" rel="stylesheet">	
<!-- Table Style/Modal Style -->
<style>
.mdl-body {
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
</head>
<body onload="hideDiv()">
	<!-- Sessiontimeout Message -->
	<%
	    session=request.getSession(false);
	    if(session.getAttribute("ID")==null)
	        response.sendRedirect("sessionTimeOut.jsp");
	%>
	<!-- End of Sessiontimeout Message -->

	<s:form name="customerListFrm" method="post" theme="simple">
		<div><jsp:include page="menu.jsp"></jsp:include></div>
		<!-- Search -->
		<div class="col-md-10 col-md-offset-1">
			<div class="panel panel-primary">
				<div class="panel-heading">顧客一覧</div>
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
					<div class="col-md-4">
						<div class="form-group">
							<label for="customer_cd" class="control-label col-md-4">顧客番号</label>
							<div class="col-md-8">
								<input type='text' class="form-control" name="customerListFrm.customer_cd"
									id="customer_cd" value="${customerListFrm.customer_cd}" maxlength="6" />
							</div>
						</div>
					</div>
					<div class="col-md-4">
						<div class="form-group">
							<label for="customer_name" class="control-label col-md-4">顧客名</label>
							<div class="col-md-8">
								<input type='text' class="form-control" name="customerListFrm.customer_name"
									id="customer_name" value="${customerListFrm.customer_name}" maxlength="45" />
							</div>
						</div>
					</div>
					<div class="col-md-4">
						<button type="submit" class="btn btn-success" id="btn_searchOk">検索</button>
						<button type="button" class="btn btn-danger" id="btn_clear">キャンセル</button>
					</div>
				</div>
				</div>
			</div>
		</div>

		<!-- Data Table -->
		<div class="container-fluid" id="panel-body">
			<div class="col-md-12">
				<table id="customer-list"
					class="stripe row-border cell-border order-column"
					style="width: 200%">
					<thead>
						<tr>
							<th style="text-align: center;" width="100">顧客番号</th>
							<th style="text-align: center;" width="40">顧客名</th>
							<th style="text-align: center;" width="30">メール</th>
							<th style="text-align: center;" width="40">電話番号</th>
							<th style="text-align: center;" width="30">住所</th>
							<th style="text-align: center;" width="30">更新</th>
							<th style="text-align: center;" width="30">削除</th>
						</tr>
					</thead>

					<c:forEach var="customer" items="${customerListFrm.customersList}">
						<tr style="text-align: right;" id="${customer.id}">
							<td style="text-align: left;" id="customer_cd${customer.id}"><c:out value="${customer.customer_cd}"/></td>
							<td style="text-align: left;" id="customer_name${customer.id}"><c:out value="${customer.customer_name}"/></td>
							<td style="text-align: left;" id="email${customer.id}"><c:out value="${customer.email}"/></td>
							<td style="text-align: right;" id="phone${customer.id}"><c:out value="${customer.phone}"/></td>
							<td style="text-align: left;" id="address${customer.id}"><c:out value="${customer.address}"/></td>
							<td style="text-align: center;">
								<a href="javascript:editCust(${customer.id});"><span
									class="glyphicon glyphicon-pencil"></span></a>
							</td>
							<td style="text-align: center;">
								<a href="javascript:deleteCust(${customer.id});"><span
									class="glyphicon glyphicon-trash"></span></a>
							</td>
						</tr>
					</c:forEach>

				</table>
			</div>
		</div>
		
		<!-- Delete Modal -->
		<a href="#" class="mdl2" id="mdl_delete" aria-hidden="true"></a>
		<div class="mdl2-dialog">
			<div class="mdl2-header">
				<h2>顧客削除</h2>
				<a href="#" class="btn-close" aria-hidden="true">×</a>
			</div>
			<div class="mdl2-body">
				<div class="form-group">
					<label for="delete_id" class="control-label">削除したいですか。</label> 
					<s:hidden name="customerListFrm.delete_id" id="delete_id"></s:hidden>
				</div>
			</div>
			<div class="mdl2-footer">
				<button type="submit" class="btn btn-default" id="btn_deleteOk">削除</button>
				<button type="button" class="btn btn-default"
					OnClick="closemdl_delete()">キャンセル</button>
			</div>
		</div>
		<s:hidden name="customerListFrm.button_event" value="search"></s:hidden>
		<s:hidden name="customerListFrm.search" id="search"></s:hidden>
	</s:form>
		<!-- Edit Modal -->
		<a href="#" class="mdl" id="mdl_edit" aria-hidden="true"></a>
		<div class="mdl-dialog">
			<div class="mdl-header">
				<h2>顧客更新</h2>
				<a href="#" class="btn-close" aria-hidden="true">×</a>
				<div class='alert alert-danger' id="Error" style="display: none;"></div>
			</div>
			<div class="mdl-body">
				<form class="form-horizontal" method="post" action="" name="customerListEditFrm">
					<div class="form-group">
						<s:hidden name="customerListEditFrm.id" id="edit_id"></s:hidden>
					</div>
					<div class="form-group">
						<label for="edit_customer_cd" class="control-label"><font color="red">*</font>顧客番号</label> 
						<input type="text" name="customerListEditFrm.customer_cd" class="form-control" id="edit_customer_cd" disabled>
					</div>
					<div class="form-group">
						<label for="edit_customer_name" class="control-label"><font color="red">*</font>顧客名</label>
						<input type="text" name="customerListEditFrm.customer_name" class="form-control" id="edit_customer_name" maxlength="45" value="">
					</div>
					<div class="form-group">
						<label for="edit_email" class="control-label">メール</label> 
						<input type="email" name="customerListEditFrm.email" class="form-control" id="edit_email" maxlength="30" value="">
					</div>
					<div class="form-group">
						<label for="edit_phone" class="control-label">電話番号</label> 
						<input type="text" name="customerListEditFrm.phone" class="form-control" id="edit_phone" maxlength="20" value="">
					</div>
					<div class="form-group">
						<label for="edit_address" class="control-label">住所</label> 
						<input type="text" name="customerListEditFrm.address" class="form-control" id="edit_address" maxlength="200" value="">
					</div>

					<div class="form-group">
						<div class='alert alert-danger' id="error_CustList"
							style="display: none; margin-bottom: -15px;"></div>
					</div>
					<div class="mdl-footer">
						<button type="submit" class="btn btn-default" id="btn_editOk">更新</button>
						<button type="button" class="btn btn-default"
							OnClick="closemdl_edit()">キャンセル</button>
					</div>
				</form>
			</div>
		</div>
	<script src="resources/js/jquery-2.1.4.min.js"></script>
	<script src="resources/js/bootstrap.min.js"></script>
	<script src="resources/js/jquery-ui.min.js"></script>
	<script src="resources/js/jquery.dataTables.min.js"></script>
	<script src="resources/js/dataTables.fixedColumns.min.js"></script>

	<script type="text/javascript">
	$(document).ready(function() {
		$('#customer-list').dataTable({
			scrollY:        false,
			 scrollCollapse: true,
			 "lengthMenu": [[10, 25], [10, 25]]
		     
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
		    document.customerListEditFrm.action="CustomerListEdit";
	        document.customerListEditFrm.submit();
		});
		
		$("#btn_deleteOk").click(function(){
		    document.customerListFrm.action="CustomerListDelete";
	        document.customerListFrm.submit(); 
			});
		$("#btn_searchOk").click(function(){
			document.customerListFrm.action="CustomerListSearch";
			document.customerListFrm.submit();
		});
		$("#btn_clear").click(function(){
			document.getElementById("customer_cd").value='';
			document.getElementById("customer_name").value='';		
		});
	});

	function editCust(val){
		$('#Error').hide();
		var id;
		if(!isNaN(val)){
			id=val;
		}else{
			id=val.id;
		}		
		var customer_cd = document.getElementById('customer_cd'+id).innerHTML;
	    var customer_name = document.getElementById('customer_name'+id).innerHTML;
	    var email = document.getElementById('email'+id).innerHTML;
	    var phone = document.getElementById('phone'+id).innerHTML;
	    var address = document.getElementById('address'+id).innerHTML;
	    document.getElementById('edit_id').value = id;
	    document.getElementById('edit_customer_cd').value = customer_cd;
	    document.getElementById('edit_customer_name').value = customer_name;
	    document.getElementById('edit_email').value = email;
	    document.getElementById('edit_phone').value = phone;
	    document.getElementById('edit_address').value = address;
	    location.href = "#mdl_edit";
	}
	function deleteCust(val){
		var id;
		if(!isNaN(val)){
			id=val;
		}else{
			id=val.id;
		}
		document.getElementById('delete_id').value = id;
		location.href = "#mdl_delete";
	}
	function closemdl_delete(val){
		$('#mdl_delete').modal('hide');
		document.customerListFrm.action = "CustomerListSearch";
		document.customerListFrm.submit();
	}
	function closemdl_edit(){
		$('#mdl_edit').modal('hide');
		document.customerListFrm.action = "CustomerListSearch";
		document.customerListFrm.submit();
	}
	function validateform(){		
		var cus=document.getElementById('edit_customer_cd').value.trim();
		var name=document.getElementById('edit_customer_name').value.trim();
		var email=document.getElementById('edit_email').value.trim();
		var ph=document.getElementById('edit_phone').value.trim();
		var ma = /^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i;
		if(cus==null || cus=="" || cus.length==0){
			message =" 『顧客番号』 を入力してください。";
			$('#Error').html("<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"+message);
		    $('#Error').show();   
		   	return false;
		}		 	
		if(name==null || name=="" || name.length==0){
			message =" 『顧客名』 を入力してください。";
			$('#Error').html("<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"+message);
			$('#Error').show();   
		   	return false;
		}
		if(email != "" && !email.match(ma)){
			message =" 『メール』 のフォーマット が間違っています。";
			$('#Error').html("<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"+message);
			$('#Error').show();   
		   	return false;
		}	
		if(ph != "" && isNaN(ph)){
			message =" 『電話番号』 に英数字を入力してください。";
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
