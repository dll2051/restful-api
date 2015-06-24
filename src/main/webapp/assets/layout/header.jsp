<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script type="text/javascript">
	/*
	*点击关闭时，执行该操作
	*/
	function OnExit(){
	  	parent.window.close();
	}
</script>
<div class="head">
	<div class="head_top">
		<div class="head_top_bg">
			<%--注释logo图片 <h1 class="logo">
				<img src="static/images/logo/${pageContext.request.getContextPath()}_logo.png" width="337" height="67" />
			</h1> --%>
			<div class="nav">
				<ul id="topMenuUL" style="margin: 0px">

				</ul>
			</div>
			<div class="site">
				<div class="site_gly">
					<div class="site_logo01">&nbsp;</div>
					<div class="site_logo_auto">
						<div id="cancelChangeUserRole" style="display: none;">
							<select id="rolesSelect" style="display: none;" onchange="changeRoleOver(this);"></select>&nbsp;&nbsp; 
							<a href="#" title="取消" onclick="cancelChangeUserRole();">取消</a>&nbsp;&nbsp;
						</div>
						<div id="userinfo" style="display:inline;">
							<c:if test="${not empty user.organName}">【${user.organName}】-</c:if>	<c:if test="${not empty user.loginName}">【${user.loginName}，】</c:if>欢迎您！ 
						</div>
					</div>

					<div class="site_logo_over">&nbsp;</div>
				</div>
				<div class="site_gly">
					<div class="site_exit">
						<div class="site_logo03" onclick="OnExit();"><a href="#">关闭</a></div>
					</div>
		        </div>
			</div>
		</div>
	</div>
</div>