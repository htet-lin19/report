<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="/struts-bootstrap-tags" prefix="sj" %>
<!DOCTYPE html>
<html>
<head>
<sj:head/>
<meta charset="UTF-8">
<title>グループ一覧</title>
<link href="resources/css/bootstrap.min.css" rel="stylesheet">
<link href="resources/css/jquery.dataTables.min.css" rel="stylesheet">
<link href="resources/css/mdl_modified.css" rel="stylesheet">
<link href="resources/css/mdl2_modified.css" rel="stylesheet">
<link href="resources/css/fixedColumns.dataTables.min.css" rel="stylesheet">
<link href="resources/css/navbar-fixed-top.css" rel="stylesheet">
<link href="resources/css/dataTables.bootstrap.min.css" rel="stylesheet">
<link rel="shortcut icon" href="resources/img/favicon.jpg" />
<!-- Table Style/Modal Style -->
<style>

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

<form name="groupListForm" method="post">
<div><jsp:include page="menu.jsp"></jsp:include></div>
	<!-- Search -->
	<div class="col-md-10 col-md-offset-1">
		<div class="panel panel-primary">
			<div class="panel-heading">グループ一覧</div>
			<div class="row">
				<div class="col-md-6 col-md-offset-3" style="margin-top: 10px;">	
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
					<label for="group_cd" class="control-label col-md-5">グループ番号</label>
					<div class="col-md-7">
						<input type='text' class="form-control" name="groupListFrm.group_cd" id="group_cd" value="${groupListFrm.group_cd}" maxlength="3"/>
					</div>
				</div>
			</div>
			<div class="col-md-4">
				<div class="form-group">
					<label for="group_name" class="control-label col-md-4">グループ名</label>
					<div class="col-md-8">
						<input type='text' class="form-control" name="groupListFrm.group_name" id="group_name" value="${groupListFrm.group_name}" maxlength="13"/>
					</div>
				</div>
			</div>
			<div class="col-md-4">							
					<button type="submit" class="btn btn-success" id="btnSearch">検索</button>
					<button type="button" class="btn btn-danger" id="btnClear">キャンセル</button>
				
			 </div>
			</div>
			</div>
		</div>
	</div>

<!-- Data Table -->	
<div class="container-fluid" id="panel-body">	
<div class="col-md-12">
	<table id="group-list" class="stripe row-border cell-border order-column" style="width:200%">
		<thead>
			<tr>
				<th style="text-align: center;" width="120">グループ番号</th>
				<th style="text-align: center;" width="40">グループ名</th>
				<th style="text-align: center;" width="30">更新</th>
				<th style="text-align: center;" width="30">削除</th>
			</tr>
		</thead>
						
		<c:forEach var="groupList" items="${groupListFrm.groupListDetail}" varStatus="status">
			<tr style="text-align: right;" id="${groupList.id}" >
				<td style="text-align: left;" id="list_group_cd${groupList.id}"><c:out value="${groupList.group_cd}"/></td>
				<td style="text-align: left;" id="list_group_name${groupList.id}"><c:out value="${groupList.group_name}"/></td>
				<td style="text-align: center;"><a href="javascript:editGroup(${groupList.id});"><span class="glyphicon glyphicon-pencil"></span></a></td>
				<td style="text-align: center;"><a href="javascript:deleteGroup(${groupList.id});"><span class="glyphicon glyphicon-trash"></span></a></td>
            </tr>
		</c:forEach>
	</table>
</div>
</div>
<s:hidden name="groupListFrm.button_event" value="search"></s:hidden>
<s:hidden name="groupListFrm.search" id="search"></s:hidden>
<!-- Delete Modal -->
<a href="#" class="mdl2" id="mdl_delete" aria-hidden="true"></a>
	<div class="mdl2-dialog">
    <div class="mdl2-header">
     <h2>グループ削除</h2>
      <a href="#" class="btn-close" aria-hidden="true">×</a>
    </div>
    <div class="mdl2-body">
    	<div class="form-group">
          	<label for="delete_id" class="control-label">削除したいですか。</label>
          	<s:hidden id="delete_id" name="groupListFrm.delete_id"></s:hidden>
        </div>
    </div>
    <div class="mdl2-footer">
        <button type="submit" class="btn btn-default" id="btnDelete">削除</button>
        <button type="button" class="btn btn-default" OnClick="closemdl_delete()">キャンセル</button>
    </div>
</div>
</form>

<!-- Edit Modal -->			
<a href="#" class="mdl" id="mdl_edit" aria-hidden="true"></a>
<div class="mdl-dialog">
	<div class="mdl-header">
	<h2>グループ更新</h2>
		<a href="#" class="btn-close" aria-hidden="true">×</a>
		<div class='alert alert-danger' id="Error" style="display: none;"></div>
   	</div>
   	<div class="mdl-body">
	<form class="form-horizontal" method="post" name="groupListEidtFrm">
  		<div class="form-group">
           	<s:hidden name="groupListEidtFrm.id" id="edit_id"></s:hidden>
       	</div>
   		<div class="form-group">
       		<label for="edit_group_cd" class="control-label"><font color="red">*</font>グループ番号</label>
           	<input type="text" class="form-control" name="groupListEidtFrm.group_cd" id="edit_group_cd" disabled>
       	</div>
       	<div class="form-group">
       		<label for="edit_group_name" class="control-label"><font color="red">*</font>グループ名</label>
           	<input type="text" class="form-control" name="groupListEidtFrm.group_name" id="edit_group_name" maxlength="13" value="">
       	</div>
       	<div class="form-group">
			<div class='alert alert-danger' id="error_GroupList" style="display:none; margin-bottom:-15px;"></div>
		</div>
	</form>
	</div>
	<div class="mdl-footer">
		<button type="submit" class="btn btn-default" id="btnEdit">更新</button>
	    <button type="button" class="btn btn-default" OnClick="closemdl_edit()">キャンセル</button>
	</div>
</div>

	<script src="resources/js/jquery-2.1.4.min.js"></script>
	<script src="resources/js/bootstrap.min.js"></script>
	<script src="resources/js/jquery-ui.min.js"></script>
	<script src="resources/js/jquery.dataTables.min.js"></script>
	<script src="resources/js/dataTables.fixedColumns.min.js"></script>
	
	<script type="text/javascript">
	$(document).ready(function() {
		$('#group-list').dataTable({
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
		$("#btnEdit").click(function(){
			if(validateform()) {
				document.groupListEidtFrm.action = "GroupListEdit";
				document.groupListEidtFrm.submit();
			}
		});
		$("#btnDelete").click(function(){
			document.groupListForm.action = "GroupListDelete";
			document.groupListForm.submit();
		});
		$("#btnSearch").click(function(){
			document.groupListForm.action="GroupListSearch";
			document.groupListForm.submit();
		});
		$("#btnClear").click(function(){
			document.getElementById("group_cd").value='';
			document.getElementById("group_name").value='';
		});
	});

	function editGroup(val){
		$('#Error').hide();
		var id;
		if(!isNaN(val)){
			id=val;
		}else{
			id=val.id;
		}
		
		var group_cd = document.getElementById('list_group_cd'+id).innerHTML;
	    var group_name = document.getElementById('list_group_name'+id).innerHTML;
	    document.getElementById('edit_id').value = id;
	    document.getElementById('edit_group_cd').value = group_cd;
	    document.getElementById('edit_group_name').value = group_name;
	    location.href = "#mdl_edit";
	}
	 
	function deleteGroup(val){
		var id;
		if(!isNaN(val)){
			id=val;
		}else{
			id=val.id;
		}
		document.getElementById('delete_id').value = id;
		location.href = "#mdl_delete";
	}
	
	function closemdl_delete(){
		$('#mdl_delete').modal('hide');
		document.groupListForm.action = "GroupListSearch";
		document.groupListForm.submit();
	}

	function closemdl_edit(){
		$('#mdl_edit').modal('hide');
		document.groupListForm.action = "GroupListSearch";
		document.groupListForm.submit();
	}
	
	function validateform(){
		var name=document.getElementById('edit_group_name').value.trim();
		  
		if(name==null || name=="" || name.lenght==0){
			message =" 『グループ名』 を入力してください。";
			$('#Error').html("<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"+message);
			$('#Error').show();   
		   	return false;
		} else{
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
