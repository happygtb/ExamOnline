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
								$("#addParts").attr("name",num.toString());
								part += "<div id=\"parts"+num+"\" class=\"parts\"><label id=\"name"+num+"\" class=\"name\">第"+chinese+"部分</label><div class=\"form-group\" style=\"margin-bottom: 10px;\"><input  class=\"form-control\" type=\"text\" name=\"PartName"+num+"\" placeholder=\"部分名称，必填项\" /></div>";
								part += "<div class=\"form-group\" style=\"margin-bottom: 10px;\"><textarea  class=\"form-control\"  name=\"Description"+num+"\" placeholder=\"答题说明，必填项\"></textarea></div>";
								part += "<div class=\"input-group\">" +
										"<input type=\"text\" id=\"Point"+num+"\" name=\"Point"+num+"\" placeholder=\"该部分每题分值\" class=\"form-control\"/><span id=\"Part"+num+"\"  class=\"input-group-addon\">分</span>" +
										"<label class=\"form-control\">题目总数</label><span id=\"Part"+num+"-Count\"  class=\"input-group-addon\">0</span> "/* +
										"<input type=\"hidden\" id=\"PartPoint"+num+"\" name=\"PartPoint"+num+"\" />"*/ +
										"<label class=\"form-control\">题目类型</label><span id=\"Questionstype"+num+"\" class=\"input-group-addon\"><select name=\"QType"+num+"\" id=\"QType\"> <option value=\"0\"> 请选择...</option></select></span>" +
										"<input type=\"hidden\" id=\"PartCount"+num+"\" name=\"PartCount"+num+"\" /></div>"+
										"<div class=\"contain-tip"+num+"\" id=\"contain-tip"+num+"\">"+	
										"<div class=\"input-group\" id=\"selecttips"+num+"\">" +
										"<label class=\"form-control\">知识点类型</label><span id=\"TipsType\" class=\"input-group-addon\"><select name=\"Tips"+num+"1\" id=\"Tips\"> <option value=\"0\"> 请选择...</option></select></span></div>" +
										"<div class=\"input-group\">"+
										"<input type=\"text\" name=\"questionnum"+num+"1\" id=\"questionnum"+num+"1\" class=\"num form-control\" placeholder=\"题目数量\"><span id=\"Part1-Count\"  class=\"input-group-addon\">题</span>"+
										"<label class=\"form-control\">难度比例</label>"+
										"<span id=\"diffculty\"  class=\"input-group-addon\">简单</span> <input type=\"text\" name=\"sample"+num+"1\" id=\"sample\" class=\"diffculty form-control\" placeholder=\"填题数\">"+
										"<span id=\"diffculty\"  class=\"input-group-addon\">普通</span> <input type=\"text\" name=\"ordinary"+num+"1\" id=\"ordinary\" class=\"num form-control\" placeholder=\"填题数\">"+
										"<span id=\"diffculty\"  class=\"input-group-addon\">困难</span> <input type=\"text\" name=\"hard"+num+"1\" id=\"hard\" class=\"num form-control\" placeholder=\"填题数\"></div></div>"+
										"<input type=\"hidden\" id=\"Tipsnumb"+num+"\" name=\"Tipsnumb"+num+"\" value=\"1\">"+
										"<label id=\"addTips"+num+"\"  name=\"1\" class=\"btn btn-block btn-info\" onclick=\"addtip("+num+");\">点此添加知识点</label>";
								part += "<div class=\"form-group\" style=\"margin-bottom: 10px;\"><label name=\"PartRemove\" class=\"form-control btn btn-block btn-success\"  onclick=\"removepart(this.id)\" id=\""+num+"\">删除该部分</label></div><hr style=\"border-bottom:1px dashed\"/></div>";			
										
							
								$("#PaperPart").append(part);
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
/**
 * 
 */

	



	
function qtype(num){
	var str=$("#QType").html();
	/*var i=num-1;*/
	var obj1=$("#parts"+num).find(".input-group").children("#Questionstype"+num).children("#QType");
	obj1.empty();
	obj1.append(str);
	/*读取第一个的信息*/
}
function Tips(num){
	var str=$("#Tips").html();
	/*alert(str);*/
	var obj2=$("#parts"+num).find(".input-group").children("#TipsType").children("#Tips");
	obj2.empty();
	obj2.append(str);
}	

function tipssss(num){
	var str=$("#Tips").html();
	if(num==1){
		var obj3=$("#contain-tip").find(".tippes:last").children("#TipsType").children("#Tips");
	}else{ 
		var obj3=$("#contain-tip"+num).find(".tippes:last").children("#TipsType").children("#Tips");
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
		tipss += "<div class=\"input-group tippes\" id=\"tipsss"+num+Tipsnum+"\">" +
			"<label class=\"form-control\">知识点类型</label><span id=\"TipsType\" class=\"input-group-addon\"><select name=\"Tips"+num+Tipsnum+"\" id=\"Tips\"> <option value=\"0\"> 请选择...</option></select></span>" +
			"<div class=\"input-group\">"+
			"<input type=\"text\" name=\"questionnum"+num+Tipsnum+"\" id=\"questionnum"+num+Tipsnum+"\" class=\"num form-control\" placeholder=\"题目数量\"><span id=\"Part1-Count\"  class=\"input-group-addon\">题</span>"+
			"<label class=\"form-control\">难度比例</label>"+
			"<span id=\"diffculty1\"  class=\"input-group-addon\">简单</span> <input type=\"text\" name=\"sample"+num+Tipsnum+"\" id=\"sample\" class=\"diffculty form-control\" placeholder=\"填题数\">"+
			"<span id=\"diffculty2\"  class=\"input-group-addon\">普通</span> <input type=\"text\" name=\"ordinary"+num+Tipsnum+"\" id=\"ordinary\" class=\"num form-control\" placeholder=\"填题数\">"+
			"<span id=\"diffculty3\"  class=\"input-group-addon\">困难</span> <input type=\"text\" name=\"hard"+num+Tipsnum+"\" id=\"hard\" class=\"num form-control\" placeholder=\"填题数\">";
		tipss+= "<div class=\"form-group\" style=\"margin-bottom: 10px;\"><label name=\"TipsRemove\" class=\"form-control btn btn-block btn-success\"  onclick=\"removetips(this.id)\" id=\""+num+Tipsnum+"\">删除该知识点</label></div><hr style=\"border-bottom:1px dashed\"/></div></div>";			
		/*$("#parts"+num).find(".contain-tip").append(tipss);*/
		
		if(num==1){
		$("#contain-tip").append(tipss);
		}else{
		$(".contain-tip"+num).append(tipss);
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


window.onload = function() {
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
			var parttipnum=$("#questionnum"+i+q).val();
			if(parttipnum!=null){
				Pqnum=parseInt(parttipnum)+Pqnum;
			}else{
				continue;
			}
		}
	//	alert(Pqnum);
		Ppoint=Pqnum*partPoint;
		$("#Part"+i+"-Count").empty();
		$("#Part"+i+"-Count").append(Pqnum);
		Qnum=Qnum+Pqnum;
		Point=Point+Ppoint;
	}
	$("#TotalPoint").empty();
	$("#TotalPoint").append(Point);
	$("#CountQuestions").empty();
	$("#CountQuestions").append(Qnum);
}
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
		/*
		
		
		var num=$("#addParts").attr("name");
		/*myForm.setAttribute("action",myForm.getAttribute("action")+"?TotalPoint="+TotalPoint+"&num="+num+"&CountQuestions="+CountQuestions);*/
		var CountQuestions=$("#CountQuestions").html();
		var TotalPoint=$("#TotalPoint").html();
		var action=myForm.getAttribute("action");
		var num=$("#addParts").attr("name");
		myForm.setAttribute("action",myForm.getAttribute("action")+"?TotalPoint="+TotalPoint+"&num="+num+"&CountQuestions="+CountQuestions);
		myForm.submit();
	}
}	
	
	
	
	
	
	


