<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/project.jsp"%>
<%@ include file="../common/taglibs.jsp"%>
<html>
<head>

<script language="javascript" type="text/javascript" src="${ctx}/js/generic/gwgl/commonfunc.js"></script>
<script type="text/javascript" src="${ctx}/js/common/kindeditor-4.1.9/kindeditor.js"></script>
<script type="text/javascript" src="${ctx}/js/common/kindeditor-4.1.9/lang/zh_CN.js"></script>
</head>
<script type="text/javascript">
$(document).ready(function(){
	KindEditor.ready(function(K) {
        var editor = K.create('#remarks',{
        	                           width : '600px',
        	                           height: '200px',
        	                           items : [''] ,
        	                           readonlyMode : true,
        	                           resizeType : 0
                                      });
        });
	var statusNO = $("#statusNO").val();
    if(statusNO != null && statusNO == '02'){
        $("#reason").show(); 
        $("#reason1").show();
        $("#reason2").show(); 
        $("#reason3").show();
        $("#reason4").show(); 
     }else{
    	$("#reason").hide(); 
    	//$("#reason1").hide();
    	//$("#reason2").hide(); 
    	//$("#reason3").hide();
    	//$("#reason4").hide(); 
     }
});
function cancel(){
	closePopupWindow("popupSubWindow");
}
//点击附件时，执行下载动作
function downLoadAppendix(fileId){
	if(fileId.length<1){
		$.messager.alert('提示','下载失败！','info');
		return;
	}
	//location.href="gwglDownLoadFile.action?fileId="+fileId;
	var downloadframe = document.getElementById('downloadframe');
	downloadframe.src = "gwglDownLoadFile.action?fileId="+fileId;
	downloadframe.style.display = "none";
}
</script>

<body>
<div id="popupContent" style="margin-rigth: 25px;"> 
   <input type="hidden" id="statusNO" name="statusNO" value="${ detail.DOCSTATUSNO}"/>
  <table width="100%" cellspacing="0px" align="left" class="tab_style_02" style="line-height:120%">
	<tr>
		<td  align="right" style="font-size:13px;font-weight:700;">发文角色:</td>
		<td align="left">
			${detail.SENDERNAME}
		</td>
		<td  align="right" style="font-size:13px;font-weight:700;">文件编号:</td>
		<td align="left">
			${detail.DOCNUMBER}
		<td  align="right" style="font-size:13px;font-weight:700;">公文类别:</td>
		<td align="left">
			${detail.DOCTYPE}
		</td>
	</tr>
	<tr>
		<td  align="right" style="font-size:13px;font-weight:700;">成文日期:</td>
		<td align="left">
			${fn:substring(detail.BUILDDATE, 0, 10)}
		</td>
		<td  align="right" style="font-size:13px;font-weight:700;">紧急程度:</td>
		<td align="left">
			${detail.EMERLEVEL}
		</td>
		<td  align="right" style="font-size:13px;font-weight:700;">密级:</td>
		<td align="left">
			${detail.SECRETLEVEL}
		</td>
	</tr>
	<tr>
		<td  align="right" style="font-size:13px;font-weight:700;">文件标题:</td>
		<td colspan="5" align="left">
			${detail.DOCTITLE}
		</td>
	</tr>
	<tr>
		<td  align="right" style="font-size:13px;font-weight:700;" valign="top"><span class="fcolor_blue fweight_bold" >正文:</span></td>
		<td colspan="5" align="left">
			<textarea  id="remarks" name="remarks" >${detail.REMARKS}</textarea>
		</td>
	</tr>
	<tr id="reason" name="reason">
		<td id="reason1" name="reason1" align="right" style="font-size:13px;font-weight:700;" valign="top"><span  style="font-size:13px;font-weight:700;" id="reason4" name="reason4">撤销原因:</span></td>
		<td id="reason2" name="reason2" colspan="5" align="left">
			<textarea id="reason3" name="reason3"  rows="5" cols="105" >${detail.CANCELREASON}</textarea>
		</td>
	</tr>
	<c:if test="${detail.DOCSTATUSNO == '01'}">
	  <c:forEach var="file" items="${files}" varStatus="status">
		<tr id="${status.index}">
			<td  align="right" style="font-size:13px;font-weight:700;" >附件:</td>
			<td colspan="5" align="left">
				<a href="#" class="fcolor_red fweight_bold"  onclick="javascript:downLoadAppendix('${file.FILEID}')">【附件${status.index + 1}：${file.ORIGINALNAME}】</a>
		   </td>
		</tr>
	  </c:forEach>
	</c:if>
	<c:if test="${detail.DOCSTATUSNO == '02'}">
	  <c:forEach var="file" items="${files}" varStatus="status">
		<tr id="${status.index}">
			<td  align="right" style="font-size:13px;font-weight:700;" >附件:</td>
			<td colspan="5" align="left">
				<a class="fcolor_red fweight_bold"  >【附件${status.index + 1}：${file.ORIGINALNAME}】</a>
		   </td>
		</tr>
	  </c:forEach>
	</c:if>
	<c:if test="${fn:length(files)<4}">
		<c:forEach begin="1" end="${4-fn:length(files)}">
			<tr>
				<td height="32" colspan="6" >&nbsp;</td>
			</tr>
		</c:forEach>
	</c:if>
  </table>
 </div>
  <iframe id="downloadframe" style="display:none"></iframe>
</body>
</html>
