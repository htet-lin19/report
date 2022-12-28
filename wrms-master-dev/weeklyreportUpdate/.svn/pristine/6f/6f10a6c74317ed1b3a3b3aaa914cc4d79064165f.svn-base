<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>顧客登録</title>
<link href="resources/css/bootstrap.min.css" rel="stylesheet">
<link href="resources/css/jquery-ui.css" rel="stylesheet">
<link href="resources/css/navbar-fixed-top.css" rel="stylesheet">
<link rel="shortcut icon" href="resources/img/favicon.jpg" />
</head>
<body>
<!-- Sessiontimeout Message -->
<%
	    session=request.getSession(false);
	    if(session.getAttribute("ID")==null)
	        response.sendRedirect("sessionTimeOut.jsp");
	%>
<!-- End of Sessiontimeout Message -->

<div><jsp:include page="menu.jsp"></jsp:include></div>

	<div class="col-md-6 col-md-offset-3">
		<!-- Duplicate Message -->
		<div class="panel panel-primary">
			<div class="panel-heading">顧客登録</div>
			<div class="panel-body">
			
			<div class="col-md-7 col-md-offset-2">
			<s:if test="hasActionErrors()">
   				<div class="alert alert-danger alerts" id="error">
      				<s:actionerror/>
   				</div>
			</s:if>
			</div>
			<div class="col-md-5 col-md-offset-3">
			<s:if test="hasActionMessages()">
   				<div class="alert alert-success alerts" id="msg">
      				<s:actionmessage/>
   				</div>
			</s:if>	
			</div>
		<!-- End of Duplicate Message -->	
			
				<form id="customer" name="customerEntryFrm" class="form-horizontal" method="post">
				<div class="col-md-8 col-md-offset-2">
					<div class='alert alert-danger' id="Error" style="display:none;"></div>
				</div> 
				
				<s:textfield name="id" type="hidden" />
					<div class="col-md-10 col-md-offset-1">
					<div class="form-group">
						<label for="customer_cd" class="control-label col-md-3"><font color="red">*</font>顧客番号:</label>
						<div class="col-md-6">
							<s:textfield name="customerEntryFrm.customer_cd" maxlength="6" requiredLabel="true" cssClass="form-control" id="customer_cd"/>
						</div>
					</div>
					
					<div class="form-group">
						<label for="customer_name" class="control-label col-md-3"><font color="red">*</font>顧客名:</label>
						<div class="col-md-6">
							<s:textfield name="customerEntryFrm.customer_name" maxlength="45" cssClass="form-control" id="customer_name"/>
						</div>
					</div>
					
					<div class="form-group">
						<label for="email" class="control-label col-md-3">メール:</label>
						<div class="col-md-6">
							<s:textfield name="customerEntryFrm.email" type="email" cssClass="form-control" maxlength="30" id="email"/>
						</div>
					</div>
					
					<div class="form-group">
						<label for="phone" class="control-label col-md-3">電話番号:</label>
						<div class="col-md-6">
							<s:textfield name="customerEntryFrm.phone" data-intger="" cssClass="form-control" maxlength="20"  id="phone" data-toggle="validator" data-maxlength="20"/>
						</div>
					</div>
					
					<div class="form-group">
						<label for="address" class="control-label col-md-3">住所:</label>
						<div class="col-md-6">
							<s:textarea id="address" name="customerEntryFrm.address" cssClass="form-control" rows="5" maxlength="200"/>
						</div>
					</div>
					</div>
					<s:textfield type="hidden" name="created_date" />
					<s:textfield type="hidden" name="modified_date" />

					<div class="form-group">
						<div class="col-md-8 col-md-offset-4">
							<button type="submit" class="btn btn-success" id="btnInsert">登録</button>
							<button type="reset" class="btn btn-danger" id="btnClear">クリア</button>
						</div>
					</div>

				</form>
			</div>
		</div>
	</div>

	<script src="resources/js/jquery-2.1.4.min.js"></script>
	<script src="resources/js/validator.min.js"></script>
	<script src="resources/js/bootstrap.min.js"></script>
	<script src="resources/js/jquery-ui.min.js"></script>
	<script>
	$("#btnClear").click(function(){ 
		document.getElementById('customer_cd').value = "";
		document.getElementById('customer_name').value = "";
		document.getElementById('email').value = "";
		document.getElementById('phone').value = "";
		document.getElementById('address').value = "";
		$('#Error').html("");
		$('#Error').hide();   
     });
	$("#btnInsert").click(function(){
     	var boo;
		if(!validateform())
			boo = false;		
		else
			boo = true;
		
		if(!boo)
			return false;
   	  	document.customerEntryFrm.action="CustomerInsert";
   	  	document.customerEntryFrm.submit();
			
    });
	
	function validateform(){
		//msgエリア非表示		
		$('#msg').html("");
		$('#msg').hide();
		
		var cus=document.getElementById('customer_cd').value.trim();
		var name=document.getElementById('customer_name').value.trim();
		var email=document.getElementById('email').value.trim();
		var ph=document.getElementById('phone').value.trim();
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
	</script>	
</body>