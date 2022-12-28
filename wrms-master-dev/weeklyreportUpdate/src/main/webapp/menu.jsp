<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- For Admin -->
<nav class="navbar navbar-default navbar-fixed-top" id="roleadmin">
	<div class="container">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed"
				data-toggle="collapse" data-target="#navbaradmin" aria-expanded="false"
				aria-controls="navbar">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a href="home.jsp" class="navbar-brand" title="週報管理システム" >
			</a>
		</div>
		<div id="navbaradmin" class="navbar-collapse collapse">
			<ul class="nav navbar-nav">
				<li class="active">
				<a href="home.jsp"><span class="glyphicon glyphicon-home" style="margin-right: 5px;"></span>ホーム</a>
				</li>
				
				<li class="dropdown"><a href="#" class="dropdown-toggle"
					data-toggle="dropdown" role="button" aria-haspopup="true"
					aria-expanded="false">週報 <span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li class="dropdown-header">週報 </li>
						<li><a href="ReportEntryNew">
						<span class="glyphicon glyphicon-list-alt" style="margin-right: 5px;"></span>
						週報登録</a></li>
						<li role="separator" class="divider"></li>
						<li class="dropdown-header">週報一覧を探す</li>
						<li><a href="ReportListNew">
						<span class="glyphicon glyphicon-th-list" style="margin-right: 5px;"></span>
							週報一覧</a></li>
					</ul>
				</li>
								
				<li class="dropdown"><a href="#" class="dropdown-toggle"
					data-toggle="dropdown" role="button" aria-haspopup="true"
					aria-expanded="false">勤務時間 <span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li class="dropdown-header">勤務時間</li>
						<li><a href="AttendanceEntryNew">
						<span class="glyphicon glyphicon-check" style="margin-right: 5px;"></span>
						勤務時間登録</a></li>
						<li><a href="AttendanceSettingNew">
						<span class="glyphicon glyphicon-check" style="margin-right: 5px;"></span>
						所定勤務時間設定登録</a></li>
						<li role="separator" class="divider"></li>
						<li class="dropdown-header">勤務時間一覧を探す</li>
						<li><a href="AttendanceListNew">
						<span class="glyphicon glyphicon-th-list" style="margin-right: 5px;"></span>
							勤務時間一覧</a></li>
						<li><a href="doImport"><span class="glyphicon glyphicon-upload" style="margin-right: 5px;"></span>勤務時間インポート</a></li>
						<!-- 2020/10/06 GICM AMTD 勤務時間サマリ情報メニュータップ作成 Start -->
						<li role="separator" class="divider"></li>
						<li class="dropdown-header">勤務時間サマリ</li>
						<li><a href="AttendanceSummaryInfo">
						<span class="glyphicon glyphicon-th-list" style="margin-right: 5px;"></span>勤務時間サマリ情報</a></li>
						<!-- 2020/10/06 GICM AMTD 勤務時間サマリ情報メニュータップ作成 End -->
						<!-- 2020/10/06 GICM TSDK 勤務時間サマリ情報インポート対応 Start -->
						<li><a href="doSummaryImport"><span class="glyphicon glyphicon-upload" style="margin-right: 5px;"></span>勤務時間サマリ情報インポート</a></li>
					    <!-- 2020/10/06 GICM TSDK 勤務時間サマリ情報インポート対応 End -->
					    <!-- 2020/10/12 GICM KZP 休暇サマリ情報ダウンロード・アップロード対応 Start -->
						<li role="separator" class="divider"></li>
						<li class="dropdown-header">休暇サマリ情報</li>
						<li>
							<a href="leaveSummaryInfo">
							<span class="glyphicon glyphicon-upload" style="margin-right: 5px;"></span>休暇サマリ情報インポート・エクスポート
							</a>
						</li>
						<!-- 2020/10/12 GICM KZP 休暇サマリ情報ダウンロード・アップロード対応 End -->
					</ul>
				</li>
				
				<li class="dropdown"><a href="#" class="dropdown-toggle"
					data-toggle="dropdown" role="button" aria-haspopup="true"
					aria-expanded="false">社員 <span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li class="dropdown-header">新入社員</li>
						<li><a href="EmployeeNew">
						<span class="glyphicon glyphicon-user" style="margin-right: 5px;"></span>
						社員登録</a></li>
						<li role="separator" class="divider"></li>
						<li class="dropdown-header">社員一覧を探す</li>
						<li><a href="EmployeeListNew">
						<span class="glyphicon glyphicon-th-list" style="margin-right: 5px;"></span>
							社員一覧</a></li>
					</ul>
				</li>
							
				<li class="dropdown"><a href="#" class="dropdown-toggle"
					data-toggle="dropdown" role="button" aria-haspopup="true"
					aria-expanded="false">顧客 <span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li class="dropdown-header">顧客</li>
						<li><a href="CustomerNew">
						<span class="glyphicon glyphicon-shopping-cart" style="margin-right: 5px;"></span>
						顧客登録</a></li>
						<li role="separator" class="divider"></li>
						<li class="dropdown-header">顧客一覧を探す</li>
						<li><a href="CustomerListNew">
						<span class="glyphicon glyphicon-th-list" style="margin-right: 5px;"></span>
							顧客一覧</a></li>
					</ul>
				</li>
				
				<li class="dropdown"><a href="#" class="dropdown-toggle"
					data-toggle="dropdown" role="button" aria-haspopup="true"
					aria-expanded="false">グループ<span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li class="dropdown-header">グループ</li>
						<li><a href="GroupEntryNew">
						<span class="glyphicon glyphicon-user" style="margin-right: 5px;"></span>
						 グループ登録</a></li>
						 
						<li role="separator" class="divider"></li>
						<li class="dropdown-header">グループ一覧を探す</li>
						<li>
							<a href="GroupListNew">
							<span class="glyphicon glyphicon-th-list" style="margin-right: 5px;"></span>
							グループ一覧
							</a>
						</li>
												
					</ul>
				</li>
								
				<li class="dropdown"><a href="#" class="dropdown-toggle"
					data-toggle="dropdown" role="button" aria-haspopup="true"
					aria-expanded="false">サインアップ<span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li class="dropdown-header">新入社員のサインアップ</li>
						<li><a href="SignUpNew">
						<span class="glyphicon glyphicon-user" style="margin-right: 5px;"></span>
						サインアップ</a></li>
						<li role="separator" class="divider"></li>
						<li class="dropdown-header">サインアップ一覧を探す</li>
						<li><a href="signUpList.jsp">
						<span class="glyphicon glyphicon-th-list" style="margin-right: 5px;"></span>
							サインアップ一覧</a></li>
					</ul>
				</li>
			</ul>
			
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown">
				  <a href="Notificationhome"><span class="glyphicon glyphicon-bell" style="margin-right: 5px;"></span>
				   <% if(session.getAttribute("NOTIFICATION_COUNT") != "0")
				   {
					   out.print( session.getAttribute("NOTIFICATION_COUNT")); 
				   }
				    %></a>
				</li>			
				<li class="dropdown"><a href="#" class="dropdown-toggle"
					data-toggle="dropdown" role="button" aria-haspopup="true"
					aria-expanded="false">プロフィール<span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li class="dropdown-header">アクティブユーザー</li>
						<li>
						<a href="#">
						<span class="glyphicon glyphicon-user" style="margin-right: 5px;"></span>
						<% out.print( session.getAttribute("ID")); %> : <% out.print( session.getAttribute("NAME")); %>
						<br/>
						<span class="glyphicon glyphicon-briefcase" style="margin-right: 5px;"></span>
						ロールレベル : <% out.print( session.getAttribute("ROLE")); %>
						<br>
						<span class="glyphicon glyphicon-bookmark" style="margin-right: 5px;"></span>
						グループ番号 : <% out.print( session.getAttribute("GROUP_CD")); %>		
					
						</a>
						</li>
						<li role="separator" class="divider"></li>
						<li class="dropdown-header">パスワード更新しますか。</li>
						<li>
							<a href="EmpSignUpNew">
								<span class="glyphicon glyphicon-cog" style="margin-right: 5px;"></span>パスワード更新
							</a>
						</li>
						<li role="separator" class="divider"></li>
						<li class="dropdown-header">ログアウトしますか。</li>
						<li><a href="logout"><span class="glyphicon glyphicon-log-out" style="margin-right: 5px;"></span>ログアウト</a></li>
						<li role="separator" class="divider"></li>
						<li class="dropdown-header">データを印刷しますか。</li>
						<li>
							<a href="AttendanceExportNew">
							<span class="glyphicon glyphicon-floppy-save" style="margin-right: 5px;"></span>勤務時間情報印刷
							</a>
						</li>
					</ul>
				</li>
			</ul>
									
		</div>
	</div>
</nav>

<!-- For Member -->
<nav class="navbar navbar-default navbar-fixed-top" id="rolemember">
<div class="container">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed"
				data-toggle="collapse" data-target="#navbar" aria-expanded="false"
				aria-controls="navbar">
				<span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span> 
				<span class="icon-bar"></span> <span class="icon-bar"></span>
			</button>
			<a href="home.jsp" class="navbar-brand" title="週報管理システム" >
			</a>
		</div>
		<div id="navbar" class="navbar-collapse collapse">
			<ul class="nav navbar-nav">
				<li class="active">
				<a href="home.jsp"><span class="glyphicon glyphicon-home" style="margin-right: 5px;"></span>ホーム</a>
				</li>
				
				<li class="dropdown"><a href="#" class="dropdown-toggle"
					data-toggle="dropdown" role="button" aria-haspopup="true"
					aria-expanded="false">週報 <span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li class="dropdown-header">週報 </li>
						<li><a href="ReportEntryNew">
						<span class="glyphicon glyphicon-list-alt" style="margin-right: 5px;"></span>
						週報登録</a></li>
						<li role="separator" class="divider"></li>
						<li class="dropdown-header">週報一覧を探す</li>
						<li><a href="ReportListNew">週報一覧</a></li>
					</ul>
				</li>
								
				<li class="dropdown"><a href="#" class="dropdown-toggle"
					data-toggle="dropdown" role="button" aria-haspopup="true"
					aria-expanded="false">勤務時間 <span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li class="dropdown-header">勤務時間</li>
						<li><a href="AttendanceEntryNew">
						<span class="glyphicon glyphicon-check" style="margin-right: 5px;"></span>
						勤務時間登録</a></li>
						<li><a href="AttendanceSettingNew">
						<span class="glyphicon glyphicon-check" style="margin-right: 5px;"></span>
						所定勤務時間設定登録</a></li>
						<li role="separator" class="divider"></li>
						<li class="dropdown-header">勤務時間一覧を探す</li>
						<li><a href="AttendanceListNew">勤務時間一覧</a></li>
						<!-- 2020/10/06 GICM AMTD 勤務時間サマリ情報メニュータップ作成 Start -->
						<li role="separator" class="divider"></li>
						<li class="dropdown-header">勤務時間サマリ</li>
						<li><a href="AttendanceSummaryInfo">
						<span class="glyphicon glyphicon-th-list" style="margin-right: 5px;"></span>勤務時間サマリ情報</a></li>
						<!-- 2020/10/06 GICM AMTD 勤務時間サマリ情報メニュータップ作成 End -->
						<!-- 2020/10/07 GICM TSDK 勤務時間インポート権限設定対応 Start -->
						<!-- <li><a href="doImport"><span class="glyphicon glyphicon-upload" style="margin-right: 5px;"></span>勤務時間インポート</a></li> -->
						<!-- 2020/10/07 GICM TSDK 勤務時間インポート権限設定対応 End -->
						
					</ul>
				</li>
			</ul>
			
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown">
				  <a href="Notificationhome"><span class="glyphicon glyphicon-bell" style="margin-right: 5px;"></span>
				   	<% if(session.getAttribute("NOTIFICATION_COUNT") != "0")
				   {
					   out.print( session.getAttribute("NOTIFICATION_COUNT")); 
				   }
				    %>
				   </a>
				</li>			
				<li class="dropdown"><a href="#" class="dropdown-toggle"
					data-toggle="dropdown" role="button" aria-haspopup="true"
					aria-expanded="false">プロフィール <span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li class="dropdown-header">アクティブユーザー</li>
						<li>
						<a href="#">
						<span class="glyphicon glyphicon-user" style="margin-right: 5px;"></span>
						<% out.print( session.getAttribute("ID")); %> : <% out.print(session.getAttribute("NAME")); %>
						<br/>
						<span class="glyphicon glyphicon-briefcase" style="margin-right: 5px;"></span>
						ロールレベル : <% out.print( session.getAttribute("ROLE")); %>
						<br>
						<span class="glyphicon glyphicon-bookmark" style="margin-right: 5px;"></span>
						グループ番号 : <% out.print( session.getAttribute("GROUP_CD")); %>		
					
						</a>
						</li>
						<li role="separator" class="divider"></li>
						<li class="dropdown-header">パスワード更新しますか。</li>
						<li><a href="EmpSignUpNew">
						<span class="glyphicon glyphicon-cog" style="margin-right: 5px;"></span>パスワード更新</a></li>
						<li role="separator" class="divider"></li>
						<li class="dropdown-header">ログアウトしますか。</li>
						<li><a href="logout"><span class="glyphicon glyphicon-log-out" style="margin-right: 5px;"></span>ログアウト</a></li>
						<li role="separator" class="divider"></li>
						<li class="dropdown-header">データを印刷しますか。</li>
						<li>
							<a href="AttendanceExportNew">
							<span class="glyphicon glyphicon-floppy-save" style="margin-right: 5px;"></span>勤務時間情報印刷
							</a>
						</li>
					</ul>
				</li>
			</ul>
		</div>
	</div>
</nav>
<script type="text/javascript">
var temp=<%="'"+session.getAttribute("ROLE")+"'"%>;
if(temp=="マネージャー"){
	document.getElementById("roleadmin").style.display="block";
	document.getElementById("rolemember").style.display="none";
	}
else if(temp=="グループリーダー"){
	document.getElementById("roleadmin").style.display="none";
	document.getElementById("rolemember").style.display="block";
}
else{
	document.getElementById("roleadmin").style.display="none";
	document.getElementById("rolemember").style.display="block";
	}

</script>