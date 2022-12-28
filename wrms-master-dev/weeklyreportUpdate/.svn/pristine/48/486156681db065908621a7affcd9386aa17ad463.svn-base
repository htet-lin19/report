<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/struts-bootstrap-tags" prefix="sj" %>
<!DOCTYPE html>
<html>
<head>
<sj:head/>
<meta charset="UTF-8">
<title>社員登録</title>
<link href="resources/css/bootstrap.min.css" rel="stylesheet">
<link href="resources/css/jquery-ui.css" rel="stylesheet">
<link href="resources/css/navbar-fixed-top.css" rel="stylesheet">
<link rel="shortcut icon" href="resources/img/favicon.jpg" />
</head>
<body onload="RdoManage('${employeeMaster.position}')">
<!-- Sessiontimeout Message -->
<%
	    session=request.getSession(false);
	    if(session.getAttribute("ID")==null)
	        response.sendRedirect("sessionTimeOut.jsp");
	%>
<!-- End of Sessiontimeout Message -->	
<div><jsp:include page="menu.jsp"></jsp:include></div>

<div class="col-md-8 col-md-offset-2">
		<!-- Duplicate Message -->
		<div class="panel panel-primary">
			<div class="panel-heading">社員登録</div>
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
			
			<form name="employeeMaster" class="form-horizontal" method="post">
				<div class="col-md-8 col-md-offset-2">
				<div class='alert alert-danger' id="Error" style="display:none;"></div>
				</div> 
				
				<s:textfield name="id" type="hidden" />
				<s:textfield name="emp.group_cd_hidden" id="group_cd_hidden" type="hidden"/>
				<!-- First half -->
				<div class="col-md-10 col-md-offset-1">
					<div class="form-group">
						<label for="emp_cd" class="control-label col-md-4"><font color="red">*</font>社員番号</label>
						<div class="col-md-6">
							 <s:textfield name="employeeMaster.emp_cd" requiredLabel="true" cssClass="form-control" id="emp_cd"  maxlength="8"/> 
						</div>
					</div>
					
					<div class="form-group">
						<label for="emp_name" class="control-label col-md-4"><font color="red">*</font>社員名</label>
						<div class="col-md-6">
							<s:textfield name="employeeMaster.emp_name" cssClass="form-control" id="emp_name" maxlength="20"/>
						</div>
					</div>
					
					<div class="form-group">
						<label for="emp_mail" class="control-label col-md-4"><font color="red">*</font>メールアドレス</label>
						<div class="col-md-6">
							<s:textfield name="employeeMaster.emp_mail" cssClass="form-control" id="emp_mail" maxlength="30"/>
						</div>
					</div>
						
					<div class="form-group">
						<label for="group_name" class="control-label col-md-4">グループ名</label>
						<div class="col-md-6">
							<select name="employeeMaster.group_cd" class="form-control" id="group_cd" >
								<option value="-1">Select</option>
								<c:forEach var="groupList" items="${employeeMaster.groupList}" varStatus="status">	
									<c:choose>
									    <c:when test="${employeeMaster.group_cd eq groupList.group_cd}">
									        <option value="${groupList.group_cd}" selected="selected">${groupList.group_name}</option>
									    </c:when>
									    <c:otherwise>
									        <option value="${groupList.group_cd}">${groupList.group_name}</option>
									    </c:otherwise>
									</c:choose>
								</c:forEach>
							</select> 
						</div>
					</div>
					
					<div class="form-group">
        				<label class="col-md-4 control-label" for="position"><font color="red">*</font>職位</label>
        				<div class="col-md-8">
           					<div class="btn-group" data-toggle="buttons">
               				<label class="btn btn-default">
               					<input type="radio" name="employeeMaster.position" value="1" id="position1" /> マネージャー
               				</label>
                			<label class="btn btn-default">
                    			<input type="radio" name="employeeMaster.position" value="2" id="position2"/> グループリーダー
                			</label>
                			<label class="btn btn-default">
                    			<input type="radio" name="employeeMaster.position" value="3" id="position3" /> メンバー
                			</label>
                   			</div>
    					</div>
    				</div>
    				<div class="form-group">
						<label for="emp_gl" class="control-label col-md-4">GL</label>
						<div class="col-md-6">
							<s:checkbox  name="employeeMaster.gl_flag" id="glFlag" style="width:10px;height:30px;transform:scale(1.5);"/> 
						</div>
					</div>
				</div>
				<s:textfield type="hidden" name="created_date" />
				<s:textfield type="hidden" name="modified_date" />

				<div class="col-md-offset-6 col-md-6">
				<button type="submit" class="btn btn-success" id="btnInsert">登録</button>
				<button type="reset" class="btn btn-danger" id="btnClear">クリア</button>
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
	$(document).ready(function() {	
		$('#dob,#employed_date').datepicker({
			dateFormat: "yy-mm-dd",
			changeMonth: true,
			changeYear: true,
			onSelect: function ()
		    {
		        this.focus();
		    },
			onClose: function ()
		    {
		        this.focus();
		    }
		});
		
		$("#btnInsert").click(function(){
		  	var boo;
			if(!validateform())
				boo = false;		
			else
				boo = true;
				
			if(!boo)
				return false;
			
			document.employeeMaster.action="EmployeeInsert";
		  	document.employeeMaster.submit();					
		});
		$("#btnClear").click(function(){ 
			document.employeeMaster.action="EmployeeClear";
	   	  	document.employeeMaster.submit();
	    }); 	
	});
	
	function RdoManage(position){
		/* alert(position); */
		if(position!=""){
			if (position == "1") {
		    	$('#position1').click(); 
		    } else if (position == "2") {
		    	$('#position2').click(); 
		    } else {
		    	$('#position3').click(); 
		    }
		}
	}
		
	function validateform(){
		// msgエリア非表示		
		$('#msg').html("");
		$('#msg').hide();
		
		var number=document.getElementById('emp_cd').value;
		var no=number.trim();
		var name=document.getElementById('emp_name').value.trim();
		var email=document.getElementById('emp_mail').value;//eem
		var mailFormat= /^[a-z][a-zA-Z0-9_.]*(\.[a-zA-Z][a-zA-Z0-9_.]*)?@gicjp.com?$/;
		var mailFormat1= /^[a-z][a-zA-Z0-9_.]*(\.[a-zA-Z][a-zA-Z0-9_.]*)?@gicjp.biz?$/;

		if(number==null || number=="" || number.trim().length==0){
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
		if(email==null || email=="" || email.length==0){
			message =" 『メールアドレス』 を入力してください。";
			$('#Error').html("<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"+message);
			$('#Error').show();   
		   	return false;
		}
		if(!(mailFormat.test(email) || mailFormat1.test(email))){
			message =" 『メールアドレス』 をGICメールで記入してください。";
			$('#Error').html("<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"+message);
			$('#Error').show();   
		   	return false;
		}
		if(position1.checked==false && position2.checked==false && position3.checked==false){
			message =" 『職位』 を入力してください。";
			$('#Error').html("<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"+message);
			$('#Error').show();   
		   	return false;
		}
		else{
			return true;
		}
	} 
	</script>
</body>
</html>
