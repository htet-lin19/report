<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>グループ登録</title>
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
			<div class="panel-heading">グループ登録</div>
			<div class="panel-body">
			
			<div class="col-md-6 col-md-offset-3">
			<s:if test="hasActionErrors()">
   				<div class="alert alert-danger alerts" id="error">
      				<s:actionerror/>
   				</div>
			</s:if>
			<s:if test="hasActionMessages()">
   				<div class="alert alert-success alerts" id="msg">
      				<s:actionmessage/>
   				</div>
			</s:if>	
			</div>
<!-- End of Duplicate Message -->	
			
			<form id="group" name="groupEntryFrm" class="form-horizontal" method="post" data-toggle="validator">
			<div class="col-md-7 col-md-offset-3">
			<div class='alert alert-danger' id="Error" style="display:none;"></div>
			</div> 
				
				<s:textfield name="id" type="hidden" />
					<div class="col-md-10 col-md-offset-1">
					<div class="form-group">
						<label for="group_cd" class="control-label col-md-4"><font color="red">*</font>グループ番号:</label>
						<div class="col-md-6">
							<s:textfield name="groupEntryFrm.group_cd" maxlength="3" requiredLabel="true" cssClass="form-control" id="group_cd"/>
						</div>
					</div>
					
					<div class="form-group">
						<label for="group_name" class="control-label col-md-4"><font color="red">*</font>グループ名:</label>
						<div class="col-md-6">
							<s:textfield name="groupEntryFrm.group_name" maxlength="13" cssClass="form-control" id="group_name"/>
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
		document.getElementById('group_cd').value = "";
		document.getElementById('group_name').value = "";
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
   	  	document.groupEntryFrm.action="GroupEntryInsert";
   	  	document.groupEntryFrm.submit();		
    });
	function validateform(){
		//msgエリア非表示		
		$('#msg').html("");
		$('#msg').hide();
		
		var groupCd=document.getElementById('group_cd').value.trim();
		var name=document.getElementById('group_name').value.trim();
	 
		if(groupCd==null || groupCd=="" || groupCd.length==0){
			message =" 『グループ番号』 を入力してください。";
			$('#Error').html("<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"+message);
			$('#Error').show();   
			return false;
		}
		 	
		if(name==null || name=="" || name.length==0){
			message =" 『グループ名』 を入力してください。";
			$('#Error').html("<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"+message);
			$('#Error').show();   
		   	return false;
		}else {
		  return true;
		}
	}
	</script>
	
</body>