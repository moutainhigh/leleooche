<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>智能柜箱门</title>
<meta name="description" content="">
<meta name="viewport" content="width=device-width, initial-scale=1">
<t:base type="bootstrap,layer,validform,bootstrap-form"></t:base>
</head>
 <body style="overflow:hidden;overflow-y:auto;">
 <div class="container" style="width:100%;">
	<div class="panel-heading"></div>
	<div class="panel-body">
	<form class="form-horizontal" role="form" id="formobj" action="smartCabinetDoorController.do?doAdd" method="POST">
		<input type="hidden" id="btn_sub" class="btn_sub"/>
		<input type="hidden" id="id" name="id"/>
		<div class="form-group">
			<label for="cabinetId" class="col-sm-3 control-label">智能柜ID：</label>
			<div class="col-sm-7">
				<div class="input-group" style="width:100%">
					<input id="cabinetId" name="cabinetId" type="text" maxlength="36" class="form-control input-sm" placeholder="请输入智能柜ID"  ignore="ignore" />
				</div>
			</div>
		</div>
		<div class="form-group">
			<label for="rowNo" class="col-sm-3 control-label">箱门行号：</label>
			<div class="col-sm-7">
				<div class="input-group" style="width:100%">
					<input id="rowNo" name="rowNo" type="text" maxlength="10" class="form-control input-sm" placeholder="请输入箱门行号"  datatype="n"  ignore="ignore" />
				</div>
			</div>
		</div>
		<div class="form-group">
			<label for="columnNo" class="col-sm-3 control-label">箱门列号：</label>
			<div class="col-sm-7">
				<div class="input-group" style="width:100%">
					<input id="columnNo" name="columnNo" type="text" maxlength="10" class="form-control input-sm" placeholder="请输入箱门列号"  datatype="n"  ignore="ignore" />
				</div>
			</div>
		</div>
		<div class="form-group">
			<label for="boxNo" class="col-sm-3 control-label">箱门编号：</label>
			<div class="col-sm-7">
				<div class="input-group" style="width:100%">
					<input id="boxNo" name="boxNo" type="text" maxlength="32" class="form-control input-sm" placeholder="请输入箱门编号"  ignore="ignore" />
				</div>
			</div>
		</div>
		<div class="form-group">
			<label for="isValid" class="col-sm-3 control-label">箱门有效状态：</label>
			<div class="col-sm-7">
				<div class="input-group" style="width:100%">
					<input id="isValid" name="isValid" type="text" maxlength="10" class="form-control input-sm" placeholder="请输入箱门有效状态"  datatype="n"  ignore="ignore" />
				</div>
			</div>
		</div>
		<div class="form-group">
			<label for="isOpen" class="col-sm-3 control-label">箱门是否打开：</label>
			<div class="col-sm-7">
				<div class="input-group" style="width:100%">
					<input id="isOpen" name="isOpen" type="text" maxlength="10" class="form-control input-sm" placeholder="请输入箱门是否打开"  datatype="n"  ignore="ignore" />
				</div>
			</div>
		</div>
		<div class="form-group">
			<label for="isOccupancy" class="col-sm-3 control-label">箱门是否占用：</label>
			<div class="col-sm-7">
				<div class="input-group" style="width:100%">
					<input id="isOccupancy" name="isOccupancy" type="text" maxlength="10" class="form-control input-sm" placeholder="请输入箱门是否占用"  datatype="n"  ignore="ignore" />
				</div>
			</div>
		</div>
		<div class="form-group">
			<label for="storageStatus" class="col-sm-3 control-label">储物状态：</label>
			<div class="col-sm-7">
				<div class="input-group" style="width:100%">
					<input id="storageStatus" name="storageStatus" type="text" maxlength="10" class="form-control input-sm" placeholder="请输入储物状态"  datatype="n"  ignore="ignore" />
				</div>
			</div>
		</div>
	</form>
	</div>
 </div>
<script type="text/javascript">
var subDlgIndex = '';
$(document).ready(function() {
	//单选框/多选框初始化
	$('.i-checks').iCheck({
		labelHover : false,
		cursor : true,
		checkboxClass : 'icheckbox_square-green',
		radioClass : 'iradio_square-green',
		increaseArea : '20%'
	});
	
	//表单提交
	$("#formobj").Validform({
		tiptype:function(msg,o,cssctl){
			if(o.type==3){
				validationMessage(o.obj,msg);
			}else{
				removeMessage(o.obj);
			}
		},
		btnSubmit : "#btn_sub",
		btnReset : "#btn_reset",
		ajaxPost : true,
		beforeSubmit : function(curform) {
		},
		usePlugin : {
			passwordstrength : {
				minLen : 6,
				maxLen : 18,
				trigger : function(obj, error) {
					if (error) {
						obj.parent().next().find(".Validform_checktip").show();
						obj.find(".passwordStrength").hide();
					} else {
						$(".passwordStrength").show();
						obj.parent().next().find(".Validform_checktip").hide();
					}
				}
			}
		},
		callback : function(data) {
			var win = frameElement.api.opener;
			if (data.success == true) {
				frameElement.api.close();
			    win.reloadTable();
			    win.tip(data.msg);
			} else {
			    if (data.responseText == '' || data.responseText == undefined) {
			        $.messager.alert('错误', data.msg);
			        $.Hidemsg();
			    } else {
			        try {
			            var emsg = data.responseText.substring(data.responseText.indexOf('错误描述'), data.responseText.indexOf('错误信息'));
			            $.messager.alert('错误', emsg);
			            $.Hidemsg();
			        } catch (ex) {
			            $.messager.alert('错误', data.responseText + "");
			            $.Hidemsg();
			        }
			    }
			    return false;
			}
		}
	});
		
});
</script>
</body>
</html>