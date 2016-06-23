/**
 * 
 */

var N = [ "零", "一", "二", "三", "四", "五", "六", "七", "八", "九","十"];					
function convertToChinese(num){
	 var str = num.toString();
	 var len = num.toString().length;
	 var C_Num = [];				
	 C_Num.push(N[str]);
	 return C_Num.join('');
}

//window.onload = function() {
	var ainput = document.getElementById('test');
	ainput.onblur = function() {
		var Qnum=0;
		var Point=0;
		
		var str=$("#addParts").attr('name');
		for (var i=1;i<=str;i++){
			var Tipsum=$("#Tipsnumb"+i).val();
			var partPoint=$("#Point"+i).val();
			//alert(Tipsum);
			var Pqnum=0;
			var Ppoint=0;
			for(var q=1;q<=Tipsum;q++){
				var parttipnum1=$("#simple"+i+q).val();
				var parttipnum2=$("#ordinary"+i+q).val();
				var parttipnum3=$("#hard"+i+q).val();
				alert(parttipnum3);
				parttipnum=parseInt(parttipnum1)+parseInt(parttipnum1)+parseInt(parttipnum1);
				if(parttipnum1&&parttipnum2&&parttipnum3&&parttipnum!=null&&parttipnum!=undefined){
					Pqnum=parseInt(parttipnum)+Pqnum;
				}else{
					continue;
				}
			}
			Ppoint=Pqnum*partPoint;
			$("#Part"+i+"-Count").empty();
			$("#Part"+i+"-Count").val(Pqnum);
			Qnum=Qnum+Pqnum;
			Point=Point+Ppoint;
		}
		$("#TotalPoint").empty();
		$("#TotalPoint").append(Point);
		$("#CountQuestions").empty();
		$("#CountQuestions").append(Qnum);
	}
//}

$(document).ready(
		function(){
			$.getJSON("../SubjectJson", function(jsonData) {
				var html = "";
				for (var i = 0; i < jsonData.length; i++) {
					html += "<option value=\""+jsonData[i].SubjectId+"\">"
							+ jsonData[i].SubjectName + "</option>";
				}
				$("#subject").append(html);
			});
			$.getJSON("../QTypeJson", function(jsonData) {
				var htm = "";
				for (var i = 0; i < jsonData.length; i++) {
					htm += "<option value=\""+jsonData[i].QTypeId+"\">"
							+ jsonData[i].TypeName + "</option>";
				}
				$("#QType").append(htm);
			});
			$("#subject").change(
					function() {
						var SubjectId = $("#subject").val();
						$("#Tips").empty();
						$("#Tips").append("<option value=\"0\">请选择...</option>");
						$.getJSON("../TipsJson?SubjectId="+ SubjectId,
							function(jsonData) {
								var html4 = "";
								for (var i = 0; i < jsonData.length; i++) {
									html4 += "<option value=\""+jsonData[i].TipsId+"\">"
									html4 += jsonData[i].TipsName+ "</option>";
								}
								$("#Tips").append(html4);
							});
						});
			
			 $("#addParts").click(
						function() {
							var count = $("#addParts").attr("name");
							var num = parseInt(count) + 1;
							if (num<20){
								var chinese = convertToChinese(num);		
								var part = "";
								var ModalLabel="";
								$("#addParts").attr("name",num.toString());
								/*
								part += "<div class=\"form-group\" style=\"margin-bottom: 10px;\"><label name=\"PartRemove\" class=\"form-control btn btn-block btn-success\"  onclick=\"removepart(this.id)\" id=\""+num+"\">删除该部分</label></div><hr style=\"border-bottom:1px dashed\"/>";			
								part +="<hr style=\"border-bottom: 1px dashed\" />";
								part +="<div class=\"modal fade\" id=\"modal-container-"+num+"\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\"></div></div>";*/
								/*ModalLabel +="<div class=\"modal-dialog\">" +
									         "<div class=\"modal-content\">";
							    ModalLabel +="<div class=\"modal-header\"><button type=\"button\" class=\"close\" style=\"float: right\" data-dismiss=\"modal\" aria-hidden=\"true\">×</button><h4 class=\"modal-title\" id=\"myModalLabel\">设置参数</h4></div><div class=\"modal-body\"><div class=\"contain-tip\" id=\"contain-tip\">"+
								 "<table align=\"center\"><tr><td>每题分值：</td><td><div class=\"input-group tippes\">" +
										"<input type=\"text\" id=\"Point"+num+"\" name=\"Point"+num+"\" placeholder=\"该部分每题分值\" class=\"form-control\" aria-describedby=\"basic-addon"+num+"\"/><span id=\"Part"+num+" basic-addon"+num+"\"  class=\"input-group-addon\">分</span></td></tr>" +
										"<tr><td>题目总数</td><td><div class=\"input-group\"><input type=\"text\" class=\"form-control\" readonly=\"readonly\" id=\"Part"+num+"-Count\" /><span class=\"input-group-addon\">个</span> </div></td></tr>" +
										"<input type=\"hidden\" id=\"PartPoint"+num+"\" name=\"PartPoint"+num+"\" />" +
										"<tr><td>题目类型</td><td id=\"Questionstype"+num+"\"><select name=\"QType"+num+"\" id=\"QType\" class=\"form-control\"> <option value=\"0\"> 请选择...</option></select></td></tr><tr><td colspan=\"2\"><hr /></td></tr>" +
										"<input type=\"hidden\" id=\"PartCount"+num+"\" name=\"PartCount"+num+"\" /></div>"+
										"<div class=\"contain-tip"+num+"\" id=\"contain-tip"+num+"\">"+	
										"<div class=\"input-group\" id=\"selecttips"+num+"\">" +
										"<tr><td>知识点类型</td><td id=\"TipsType\" class=\"form-control\"><select name=\"Tips"+num+"1\" id=\"Tips\"> <option value=\"0\"> 请选择...</option></select></td></tr>" +
										"<tr><td rowspan=\"3\" valign=\"middle\">难度比例：</td><td>"+
										"<div class=\"input-group\">"+
										"<input type=\"text\" name=\"questionnum"+num+"1\" id=\"questionnum"+num+"1\" class=\"num form-control\" placeholder=\"题目数量\"><span id=\"Part1-Count\"  class=\"input-group-addon\">题</span>"+
										"<label class=\"form-control\">难度比例</label>"+
										"<span id=\"diffculty1\"  class=\"input-group-addon\">简单</span> <input type=\"text\" name=\"simple"+num+"1\" id=\"simple\" class=\"diffculty form-control\" placeholder=\"请填入题目数量\"><span class=\"input-group-addon\">个</span></div></td></tr><tr><td><div class=\"input-group\">"+
										"<span id=\"diffculty2\"  class=\"input-group-addon\">普通</span> <input type=\"text\" name=\"ordinary"+num+"1\" id=\"ordinary\" class=\"num form-control\" placeholder=\"请填入题目数量\"><span class=\"input-group-addon\">个</span></div></td></tr><tr><td><div class=\"input-group\">"+
										"<span id=\"diffculty3\"  class=\"input-group-addon\">困难</span> <input type=\"text\" name=\"hard"+num+"1\" id=\"hard\" class=\"num form-control\" placeholder=\"请填入题目数量\"><span class=\"input-group-addon\">个</span></div></td></tr></table>"+
										"<input type=\"hidden\" id=\"Tipsnumb"+num+"\" name=\"Tipsnumb"+num+"\" value=\"1\">"+
										"<label id=\"addTips"+num+"\"  name=\"1\" class=\"btn btn-block btn-info\" onclick=\"addtip("+num+");\">点此添加知识点</label></div></div>"+
										"<div class=\"modal-footer\"><button type=\"button\" name=\""+num+"\" data-dismiss=\"modal\" class=\"btn btn-primary\" data-dismiss=\"modal\">保存</button><button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">关闭</button></div></div></div></div>";*/
								part+="<div id=\"parts"+num+"\"><label id=\"name"+num+"\" class=\"name\">第"+chinese+"部分</label><button type=\"button\" class=\"btn btn-info\" role='button' data-toggle='modal' href='#modal-container-"+num+"'><span class=\"glyphicon glyphicon-cog\" aria-hidden=\"true\"></span>设置参数</button><div class=\"form-group\" style=\"margin-bottom: 10px;\"><input class=\"form-control\" type=\"text\" name=\"PartName"+num+"\"placeholder=\"部分名称，必填项\" /></div>"+
							          "<div class=\"form-group\" style=\"margin-bottom: 10px;\"><textarea class=\"form-control\" name=\"Description"+num+"\"placeholder=\"答题说明，必填项\"></textarea></div>	";
								part+="<div class=\"form-group\" style=\"margin-bottom: 10px;\"><label name=\"PartRemove\" class=\"form-control btn btn-block btn-success\"  onclick=\"removepart(this.id)\" id=\""+num+"\">删除该部分</label></div><hr style=\"border-bottom:1px dashed\"/></div>";			
								ModalLabel+="<div class='modal fade' id='modal-container-"+num+"' role='dialog' aria-labelledby='myModalLabel' aria-hidden='true'><div class='modal-dialog'>";
								ModalLabel+="<div class='modal-content'><div class='modal-header'><button type='button' class='close' style='float: right' data-dismiss='modal' aria-hidden='true'>×</button>";
								ModalLabel+="<h4 class='modal-title' id='myModalLabel'>设置参数</h4></div><div class='modal-body'><div class='contain-tip' id='contain-tip"+num+"'>";
								ModalLabel+="<table align='center' id='table"+num+"'><tr><td>每题分值：</td><td><div class='input-group tippes'><input type='text' id='Point"+num+"' name='Point"+num+"' placeholder='该部分每题分值' class='form-control' aria-describedby='basic-addon"+num+"' />";
								ModalLabel+="<span id='Part"+num+"-Point basic-addon"+num+"' class='input-group-addon'>分</span></div></td>";
								ModalLabel+="</tr><tr><td>题目总数：</td><td><div class='input-group'><input type='text' id='Part"+num+"-Count' class='form-control'readonly='readonly' /> <span class='input-group-addon'>个</span>";
								ModalLabel+="</div></td></tr><tr><td>题目类型：</td><td id='Questionstype"+num+"'><select name='QType"+num+"' id='QType' class='form-control'><option value='0'>请选择...</option>";
								ModalLabel+="</select></td></tr><tr><td colspan='2'><hr /></td></tr><tr class='findtips'><td>知识点类型：</td><td id='TipsType"+num+"'><select name='Tips"+num+"1' id='Tips' class='form-control'>";
								ModalLabel+="<option value='0'>请选择...</option></select></td></tr><tr><td rowspan='3' valign='middle'>难度比例：</td><td><div class='input-group'>";
								ModalLabel+="<span id='diffculty1' class='input-group-addon'>简单</span> <input type='text' name='simple"+num+"1' id='simple"+num+"1' class='diffculty form-control' placeholder='请填入题目数量' />";
								ModalLabel+="<span class='input-group-addon'>个</span></div></td></tr><tr><td><div class='input-group'><span id='diffculty2' class='input-group-addon'>普通</span>";
								ModalLabel+="<input type='text' name='ordinary"+num+"1' id='ordinary"+num+"1' class='num form-control' placeholder='请填入题目数量' /><span class='input-group-addon'>个</span>";
								ModalLabel+="</div></td></tr><tr><td><div class='input-group'><span id='diffculty3' class='input-group-addon'>困难</span>";
								ModalLabel+="<input type='text' name='hard"+num+"1' id='hard"+num+"1' class='num form-control' placeholder='请填入题目数量' /> <span class='input-group-addon'>个</span>";
								ModalLabel+="</div></td></tr></table>";
								ModalLabel+="</div><input type='hidden' id='Tipsnumb"+num+"' name='Tipsnumb"+num+"' value='1' /><label id='addTips"+num+"' name='1' class='btn btn-block btn-info' onclick='addtip("+num+");'>点此添加知识点</label></div><div class='modal-footer'><button type='button' name='1' data-dismiss='modal' class='btn btn-primary' data-dismiss='modal'>保存</button>";
								ModalLabel+="<button type='button' class='btn btn-default' data-dismiss='modal'>关闭</button></div></div></div></div>";
								$("#PaperPart").append(part);
								$("body").append(ModalLabel);
								$('input').iCheck({
									checkboxClass: 'icheckbox_square-blue',
									radioClass: 'iradio_square-blue',
									increaseArea: '20%'
								});
							}else{
								$("#addParts").attr("disabled","disabled");
								$("#addParts").empty();
								$("#addParts").append("最多只能添加19个部分");
							}
							qtype(num);
							Tips(num);
							changeparts();
							});
			 });


function changeparts(){
	var p=2;
	$(".name").each(function(){		
			var newchinese = convertToChinese(p);	
			$(this).html("第"+newchinese+"部分");
		p++;
	}
)}

function removepart(j){
	if(confirm("确认要执行该操作吗？")==true){	
		var count = $("#addParts").attr("name");
		/*var oldnum = parseInt(count);
		var newnum = parseInt(count)-1;*/
		$("#parts"+j).remove();
	}	
	changeparts();
	}

function removetips(k){
	if(confirm("确认要执行该操作吗？")==true){	
		/*var count = $("#addParts").attr("name");*/
		/*var oldnum = parseInt(count);
		var newnum = parseInt(count)-1;*/
		$("#tipsss"+k).remove();
	}	
	
	}
	
function qtype(num){
	var str=$("#QType").html();
	/*var i=num-1;*/
	var obj1=$("#Questionstype"+num).find("#QType");
	obj1.empty();
	obj1.append(str);
	/*读取第一个的信息*/
}
function Tips(num){
	var str=$("#Tips").html();
	/*alert(str);*/
	var obj2=$("#TipsType"+num).find("#Tips");
	obj2.empty();
	obj2.append(str);
}	

function tipssss(num){
	var str=$("#Tips").html();
	if(num==1){
		var obj3=$("#contain-tip").find(".findtips:last").children("#TipsType1").children("#Tips");
	}else{ 
		var obj3=$("#contain-tip"+num).find(".findtips:last").children("#TipsType"+num).children("#Tips");
	}
	obj3.empty();
	obj3.append(str);
}

function addtip(num){
	if(num==null){
		num=1;
	}
	var count = $("#addTips"+num).attr("name");
	
	var Tipsnum = parseInt(count) + 1;
	if (Tipsnum<100){
		var tipss = "";
		$("#addTips"+num).attr("name",Tipsnum.toString());
		$("#Tipsnumb"+num).attr("value",Tipsnum.toString());
		/* "<div class=\"input-group tippes\" id=\"tipsss"+num+Tipsnum+"\">" +
			"<tr class='findtips'><td>知识点类型</td><td id=\"TipsType\" class=\"form-control\"><select name=\"Tips"+num+Tipsnum+"\" id=\"Tips\"> <option value=\"0\"> 请选择...</option></select></td></tr>" +
			"<tr><td rowspan=\"3\" valign=\"middle\">难度比例：</td>"+
			"<td><div class=\"input-group\">"+
			"<input type=\"text\" name=\"questionnum"+num+Tipsnum+"\" id=\"questionnum"+num+Tipsnum+"\" class=\"num form-control\" placeholder=\"题目数量\"><span id=\"Part1-Count\"  class=\"input-group-addon\">题</span>"+
			"<label class=\"form-control\">难度比例</label>"+
			"<span id=\"diffculty1\"  class=\"input-group-addon\">简单</span> <input type=\"text\" name=\"simple"+num+Tipsnum+"\" id=\"simple\" class=\"diffculty form-control\" placeholder=\"请填入题目数量\"><span class=\"input-group-addon\">个</span></div></td></tr>"+
			"<tr><td><div class=\"input-group\"><span id=\"diffculty2\"  class=\"input-group-addon\">普通</span> <input type=\"text\" name=\"ordinary"+num+Tipsnum+"\" id=\"ordinary\" class=\"num form-control\" placeholder=\"请填入题目数量\"><span class=\"input-group-addon\">个</span></div></td></tr>"+
			"<tr><td><div class=\"input-group\"><span id=\"diffculty3\"  class=\"input-group-addon\">困难</span> <input type=\"text\" name=\"hard"+num+Tipsnum+"\" id=\"hard\" class=\"num form-control\" placeholder=\"请填入题目数量\"><span class=\"input-group-addon\">个</span></div></td></tr>";*/
		//tipss+= "<div class=\"form-group\" style=\"margin-bottom: 10px;\"><label name=\"TipsRemove\" class=\"form-control btn btn-block btn-success\"  onclick=\"removetips(this.id)\" id=\""+num+Tipsnum+"\">删除该知识点</label></div><hr style=\"border-bottom:1px dashed\"/></div>";			
		/*$("#parts"+num).find(".contain-tip").append(tipss);*/
		tipss +="<table align='center' class='Tfindtips' id=\"tipsss"+num+Tipsnum+"\"><tr><td colspan='2'><hr /></td></tr><tr class='findtips'><td>知识点类型：</td><td id='TipsType"+num+"'><select name='Tips"+num+Tipsnum+"' id='Tips' class='form-control'><option value='0'>请选择...</option></select></td></tr><tr><td rowspan='3' valign='middle'>难度比例：</td><td>";
		tipss +="<div class='input-group'><span id='diffculty1' class='input-group-addon'>简单</span><input type='text' name='simple"+num+Tipsnum+"' id='simple"+num+Tipsnum+"' class='diffculty form-control' placeholder='请填入题目数量' /><span class='input-group-addon'>个</span></div></td></tr>";
		tipss +="<tr><td><div class='input-group'><span id='diffculty2' class='input-group-addon'>普通</span><input type='text' name='ordinary"+num+Tipsnum+"' id='ordinary"+num+Tipsnum+"' class='num form-control' placeholder='请填入题目数量' /><span class='input-group-addon'>个</span></div></td></tr>";
		tipss +="<tr><td><div class='input-group'><span id='diffculty3' class='input-group-addon'>困难</span> <input type='text' name='hard"+num+Tipsnum+"' id='hard"+num+Tipsnum+"' class='num form-control' placeholder='请填入题目数量' /><span class='input-group-addon'>个</span></div></td></tr>";
		tipss+= "<tr><td><label name=\"TipsRemove\" align='center' class='btn btn-block btn-info'  onclick=\"removetips(this.id)\" id=\""+num+Tipsnum+"\">删除该知识点</label></td></tr></table>";
		
		if(num==1){
		$("#contain-tip").append(tipss);
		}else{
		$("#contain-tip"+num).append(tipss);
		}
		$('input').iCheck({
			checkboxClass: 'icheckbox_square-blue',
			radioClass: 'iradio_square-blue',
			increaseArea: '20%'
		});
	}else{
		$("#addTips"+num).attr("disabled","disabled");
		$("#addTips"+num).empty();
		$("#addTips"+num).append("最多只能添加99个知识点");
	}
	tipssss(num);
	/* Tips(num);*/
}


function submited(){
	if (myForm.PaperName.value =="") {
		alert("请填写试卷名称");
	}
	else if (myForm.ExamDescription.value == "") {
		alert("请填写考试说明");
	}
	else if (myForm.TotalTime.value == "") {
		alert("请填写考试时长");
	}
	else {
		/*var num=$("#addParts").attr("name");
		/*myForm.setAttribute("action",myForm.getAttribute("action")+"?TotalPoint="+TotalPoint+"&num="+num+"&CountQuestions="+CountQuestions);*/
		var CountQuestions=$("#CountQuestions").html();
		var TotalPoint=$("#TotalPoint").html();
		var action=myForm.getAttribute("action");
		var num=$("#addParts").attr("name");
		myForm.setAttribute("action",myForm.getAttribute("action")+"?TotalPoint="+TotalPoint+"&num="+num+"&CountQuestions="+CountQuestions);
		myForm.submit();
	}
}	
	
	
	
	
	
	


