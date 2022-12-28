<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>サインアップ</title>
<link href="resources/css/bootstrap.min.css" rel="stylesheet">
<link href="resources/css/jquery-ui.css" rel="stylesheet">
<link href="resources/css/navbar-fixed-top.css" rel="stylesheet">
<link rel="shortcut icon" href="resources/img/favicon.jpg" />
</head>
<body onload="Password('${signUp.emp_cd}', '${signUp.emp_name}' ,'${sign.password}')">
<!-- Sessiontimeout Message -->
<%
	    session=request.getSession(false);
	    if(session.getAttribute("ID")==null)
	        response.sendRedirect("sessionTimeOut.jsp");
	%>
<!-- End of Sessiontimeout Message -->

<div><jsp:include page="menu.jsp"></jsp:include></div>

	<div class="col-xs-6 col-xs-offset-3">
		<!-- Duplicate Message -->
		<div class="panel panel-primary">
			<div class="panel-heading">サインアップ</div>
			<div class="panel-body">
			
			<div class="col-md-7 col-md-offset-3">
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
			
			<form class="form-horizontal" method="post" name="signUp">
			   <div class="col-md-6 col-md-offset-3">
				<div class='alert alert-danger' id="Error" style="display:none;"></div>
				</div> 
					<s:textfield name="id" type="hidden" />
					
					<div class="form-group">
						<label for="emp_cd" class="control-label col-xs-4"><font color="red">*</font>社員番号</label>
						<div class="col-xs-6">
							<input name="signUp.emp_cd" class="form-control" id="emp_cd" maxlength="8" value="${signUp.emp_cd}"/>	
						</div>
					</div>
					<div class="form-group">
						<label for="emp_name" class="control-label col-xs-4"><font color="red">*</font>社員名</label>
						<div class="col-xs-6">
							<input name="signUp.emp_name" class="form-control" id="emp_name" maxlength="30" value="${signUp.emp_name}"/>
						</div>
					</div>
					
					<div class="form-group">
						<label for="inputpassword" class="control-label col-xs-4"><font color="red">*</font>パスワード</label>
						<div class="col-md-6">
							<input type="password" id="password" name="signUp.password" class="form-control" maxlength="20" value="${signUp.password}"/>
						</div>
					</div>					
					<div class="form-group">
						<div class="col-xs-6 col-xs-offset-4">
							 <button type="submit" class="btn btn-primary" id="btnInsert">サインアップ</button>
							 <button type="reset" class="btn btn-success" id="btnClear">キャンセル</button>
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
		document.getElementById('emp_cd').value = "";
		document.getElementById('emp_name').value = "";
		document.getElementById('password').value = "";
    });
	
	$("#btnInsert").click(function(){
     	var boo;
		if(!validateform())
			boo = false;		
		else
			boo = true;
		
		if(!boo)
			return false;
   	  	document.signUp.action="SignUpInsert";
   	  	document.signUp.submit();
			
    });
	
	function Password(emp_cd, emp_name, password){

		document.getElementById('emp_cd').value=emp_cd;
		document.getElementById('emp_name').value=emp_name;
		document.getElementById('password').value=password;
	}

	function validateform(){
		var emp=document.getElementById('emp_cd').value.trim();
		var name=document.getElementById('emp_name').value.trim();
		var password=document.getElementById('password').value.trim();
		 		 
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
	</script>	
</body>