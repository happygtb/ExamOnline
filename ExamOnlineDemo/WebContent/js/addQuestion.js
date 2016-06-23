/**
 * 
 */

$(document).ready(
		function() {
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
				var list="";
				for (var i = 0; i < jsonData.length; i++) {
					htm += "<option value=\""+jsonData[i].QTypeId+"\">"
							+ jsonData[i].TypeName + "</option>";
					list+="<li><a href=\"QuestionIndex.html?Qtype="+jsonData[i].QTypeId+"\" >"+jsonData[i].TypeName+"</a>";
				}
				$(".questionTypeList").append(list);
				$("#QType").append(htm);
			});
			$.getJSON("../CollegeJson", function(jsonData) {
				var html1 = "";
				for (var i = 0; i < jsonData.length; i++) {
					html1 += "<option value=\""+jsonData[i].CollegeId+"\">"
							+ jsonData[i].CollegeName + "</option>";
				}
				$("#College").append(html1);
			});
			
			$("#College").change(
					function() {
						var CollegeId = $("#College").val();
						$("#School").empty();
						$("#School").append("<option value=\"0\">请选择学院</option>");
						$("#Major").empty();
						$("#Major").append("<option value=\"0\">请选择专业</option>");
						$.getJSON("../SchoolJson?CollegeId="+ CollegeId,
								function(jsonData) {
									var html2 = "";
									for (var i = 0; i < jsonData.length; i++) {
										html2 += "<option value=\""+jsonData[i].SchoolId+"\">"
										html2 += jsonData[i].SchoolName+ "</option>";
									}
									$("#School").append(html2);
								});
					});
			
			$("#School").change(
					function() {
						var SchoolId = $("#School").val();
						$("#Major").empty();
						$("#Major").append("<option value=\"0\">请选择专业</option>");
						$.getJSON("../MajorJson?SchoolId="+ SchoolId,
							function(jsonData) {
								var html3 = "";
								for (var i = 0; i < jsonData.length; i++) {
									html3 += "<option value=\""+jsonData[i].MajorId+"\">"
									html3 += jsonData[i].MajorName+ "</option>";
								}
								$("#Major").append(html3);
						});
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
			
			$("#QType").change(
					function(){
						var qtype=$("#QType").find("option:selected").text();
						var qid=$("#QType").find("option:selected").val();
						
						$("#options").children().attr("style","display:none")
						if(qtype=="单选题"){
							$("#SingleChoice").removeAttr("style");
							$("#myForm").attr("action","../AddQuestion?qtid="+qid+"&Qtype=SingleChoice");
						}
						if(qtype=="多选题"){
							$("#MultipleChoice").removeAttr("style");
							$("#myForm").attr("action","../AddQuestion?qtid="+qid+"&Qtype=MultipleChoice");
						}
						if(qtype=="判断题"){
							$("#TFChoice").removeAttr("style");
							$("#myForm").attr("action","../AddQuestion?qtid="+qid+"&Qtype=TFChoice");
						}
						if(qtype=="简答题"){
							$("#ShortAnswerQuestion").removeAttr("style");
							$("#myForm").attr("action","../AddQuestion?qtid="+qid+"&Qtype=ShortAnswerQuestion");
						}
						/* if(qtype=="组合题"){
							$("#MixedQuestion").removeAttr("style");
							$("#myForm").attr("action","");
						} */
					});
			
			$("#addOptions").click(
				function() {
					var count = $("#addOptions").attr("name");
					var num = parseInt(count) + 1;
					var htm1 = "";
					$("#addOptions").attr("name",num.toString());
					htm1 += "<div id=\"items"+num+"\"><p><div class=\"input-group\"><div class=\"input-group-addon\"><input type=\"radio\" value=\"False\" name=\"SCoptions\" id=\"SCoptions\" onchange=\"myFunction()\"></div>";
					htm1 += "<input type=\"text\" name=\"SCTipsDetail"+num+"\" class=\"form-control\">";
					htm1+="<div class=\"input-group-addon\"><label name=\"SCRemove\" onclick=\"removeOption(this.id)\" id=\""+num+"\" class=\"close\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></label></div></div></p></div>";
					$("#SCoption").append(htm1);
					$('input').iCheck({
						checkboxClass: 'icheckbox_square-blue',
						radioClass: 'iradio_square-blue',
						increaseArea: '20%'
					});
				});
			
			$("#addMCoptions").click(
					function() {
						var count = $("#addMCoptions").attr("name");
						var num = parseInt(count) + 1;
						var htm1 = "";
						$("#addMCoptions").attr("name",num.toString());
						htm1 += "<div id=\"MCitems"+num+"\"><p><div class=\"input-group\"><div class=\"input-group-addon\"><input type=\"checkbox\" value=\"False\" name=\"MCoptions"+num+"\" id=\"MCoptions"+num+"\">";
						htm1 += "</div><input type=\"text\" name=\"MCTipsDetail"+num+"\" class=\"form-control\"><div class=\"input-group-addon\">";
						htm1+="<label onclick=\"removeOptionII(this.id)\" id=\""+num+"\" class=\"close\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></label></div></div></p></div>";
						//form标签action属性传num的值

						$("#MCoption").append(htm1);
						$('input').iCheck({
							checkboxClass: 'icheckbox_square-blue',
							radioClass: 'iradio_square-blue',
							increaseArea: '20%'
						});
					});
			
			$('input').iCheck({
			    checkboxClass: 'icheckbox_square-blue',
			    radioClass: 'iradio_square-blue',
			    increaseArea: '20%'
			  });
			});

window.onload = function (){
	var oStar = document.getElementById("star");
	var aLi = oStar.getElementsByTagName("li");
	var oUl = oStar.getElementsByTagName("ul")[0];
	var oSpan = oStar.getElementsByTagName("span")[1];
	var oP = oStar.getElementsByTagName("p")[0];
	var i = iScore = iStar = 0;
	var aMsg = ["很简单","简单","一般","较难","非常难"];
	for (i = 1; i <= aLi.length; i++){
		aLi[i - 1].index = i;
		//鼠标移过显示分数
		aLi[i - 1].onmouseover = function (){
			fnPoint(this.index);
		};
		//鼠标离开后恢复上次评分
		aLi[i - 1].onmouseout = function (){
			fnPoint();
		};
		//点击后进行评分处理
		aLi[i - 1].onclick = function (){
			$("#star_span").empty();
			iStar = this.index;
			var html = "<strong>" + (this.index) + "星</strong> (" + aMsg[this.index - 1] + ")";
			$("#star_span").append(html);
			$("#Difficulty").val(this.index);
		}
	}
	//评分处理
	function fnPoint(iArg){
		//分数赋值
		iScore = iArg || iStar;
		for (i = 0; i < aLi.length; i++) aLi[i].className = i < iScore ? "on" : "";	
	}
};

function submitFunction() {
	if (myForm.subject.value == "0") {
		alert("请先选择科目");
	}
	else if (myForm.QType.value == "0") {
		alert("请先选择题型");
	}
	else if (myForm.Tips.value == "0") {
		alert("请先选择知识点");
	}
	else if (myForm.Difficulty.value == "0") {
		alert("题目难度不能为空");
	}
	else if (myForm.Major.value == "0") {
		alert("请完善出题人信息");
	}
	else {
		var action=myForm.getAttribute("action");
		var str="=";
		var qtype=action.split(str);
		qtype=qtype[2];
		if(qtype=="SingleChoice"){
			var num=document.getElementById("addOptions").getAttribute("name");
			var TrueId = $(".checked").parent().next().next().find("label[name='SCRemove']").attr("id");
			$("div[id='"+"items"+TrueId+"']").find("input[name='SCoptions']").val(TrueId);
		}
		if(qtype=="MultipleChoice"){
			var num=document.getElementById("addMCoptions").getAttribute("name");
		}
		myForm.setAttribute("action",myForm.getAttribute("action")+"&num="+num);
		myForm.submit();
	}
}

function myFunction() {
	$("input[name='SCoptions']").attr("value", "False");
	var Tnum=$("input[name='SCoptions']:checked").next().next().attr("id");
	$("input[name='SCoptions']:checked").attr("value", Tnum);
}
function removeOption(i){
	var name="items"+i;
	$("div[id="+name+"]").remove();
}
function removeOptionII(i){
	var name="MCitems"+i;
	$("div[id="+name+"]").remove();
}