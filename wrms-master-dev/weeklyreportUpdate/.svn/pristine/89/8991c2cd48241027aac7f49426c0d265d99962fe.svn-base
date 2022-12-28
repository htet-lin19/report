<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>ホーム</title>
	<meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Report Management System">
    <meta name="author" content="Global Innovation Consulting Inc.">
    <link rel="shortcut icon" href="resources/img/favicon.jpg">

    <title>WRMS Home</title>
    <link href="resources/css/bootstrap.min.css" rel="stylesheet">
	<link rel="stylesheet" href="resources/css/font-awesome.min.css">
	<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
	<link href="resources/css/navbar-fixed-top.css" rel="stylesheet">
</head>
<body data-spy="scroll" data-offset="0" data-target="#theMenu">
	<%
	    session=request.getSession(false);
	    if(session.getAttribute("ID")==null)
	        response.sendRedirect("loginerror.jsp");
	%>
<div><jsp:include page="menu.jsp"></jsp:include></div>

	<div class="container">
		<form action="attendanceListForm" method="post">
			<s:if test="%{attendanceListFrm.notification_date != null}">
				<div class="col-md-12">
				<div class="jumbotron">
					<div class="panel panel-danger">
						<div class="panel-heading">月の休日を除いて、下記の日に登録してください</div>
						<div class="panel-body">
							<c:forEach items="${attendanceListFrm.notification_date}" var="notification">
								<c:url value="attendanceEntry.jsp" var="displayURL">
									<c:param name="noti" value="${notification}" />
								</c:url>
								<a href="${displayURL}">${notification}の出席を登録してください</a>
								<br>
							</c:forEach>
						</div>
					</div>
				</div>
			</div>
			</s:if>
		</form>
		  <div class="col-md-12">
		    <div class="jumbotron">
		    	<h3><strong>Improvements</strong></h3>
		    	<p>
		    		- Excel Export and Excel Template Update<br/>
		    		- Report and Attendance's search list column adjustment<br/>
		    		- Report Entry's input text box adjustment<br/>
		    		- Removing space from time picker input
		    		- Browser Cache problem has been solved (2017-04-27)
				</p>
		    </div>
		  </div>
		  <div class="col-md-12">
			<div class="jumbotron">
				<h3><strong>Weekly Report Management System</strong></h3>	
				<p>
					Hello, We are from GIC Myanmar Team. This is OJT project.In this project, we used "STRUTS2" java framework.
					Database is MySQL. For front-end design, we used Twitter Bootstrap3.
				</p>
			</div>
		</div>
	</div>

	<!-- /container -->
	
	<footer class="footer">
      <div class="container">
        <p class="text-muted">Copyright © by GIC. All Right Reserved.</p>
      </div>
    </footer>
    
	<script src="resources/js/jquery-2.1.4.min.js"></script>	
	<script src="resources/js/classie.js"></script>
    <script src="resources/js/bootstrap.min.js"></script>
    <script src="resources/js/smoothscroll.js"></script>
	<script src="resources/js/main.js"></script>
</body>
</html>