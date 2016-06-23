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
/**
 * 
 */
	var Difficulty=0;
	var partnumber;
	var url=window.location.search;
	var pageNow="1",Qtype="";	
	if(url.indexOf("#")!=-1){   
		var str=url.substr(1)   
		strs=str.split("&");
		for(i=0;i<strs.length;i++){   
	    	if([strs[i].split("=")[0]]=='pageNow') pageNow=unescape(strs[i].split("=")[1]);
	    	if([strs[i].split("=")[0]]=='Qtype') Qtype=unescape(strs[i].split("=")[1]);
	  	}  	  	
	}
	function begin(n){
		partnumber=n;
		$.getJSON("../QTypeJson",function(jsonData){
			$(".questionTypeList").empty();
			var list="";
			for(var i=0; i < jsonData.length; i++) {
				list+="<li><a href=\"#Qtype="+jsonData[i].QTypeId+"\" id=\""+jsonData[i].QTypeId+"\" onclick='qtype(this.id)'>"+jsonData[i].TypeName+"</a>";					
			}
			$(".questionTypeList").append(list);
		});
	}	
	function qtype(qid){
		Qtype=qid;
		$("ul[id=page-"+partnumber+"]").empty();  
		$("ul[id=question"+partnumber+"]").empty(); 
	var s_pageNow=parseInt(pageNow); 
	$.getJSON("../QuestionJson",{pageNow:pageNow,Qtype:Qtype}, function(jsonData) {	
		for (var i = 0; i < jsonData.length; i++) {
			var question= "<div class=\"items\" id=\"items"+(i+1)+"\"><li><span><span id='checkbox' style=\"position: relative\"><input type='checkbox' name=\"question"+partnumber+"\" id='batch"+(i+1)+"' value=\""+jsonData[i].QuestionID+"\"></span>"+(i+1)+".<label class=\"type\" name=\""+jsonData[i].TypeName+"\" id=\"TypeName"+partnumber+i+"\">["+jsonData[i].TypeName+"]</label><label class=\"tips\" id=\"tips"+i+"\">"+jsonData[i].SubjectName+"-"+jsonData[i].TipsName+"</label><label class=\"Difficulty\" id=\"Difficulty"+i+"\">"+jsonData[i].Difficulty+"星</label></span>";
			question+="<label onclick='addoptions(this.id)'  id=\""+partnumber+i+"\">展开<span class=\"glyphicon glyphicon-menu-down\" aria-hidden=\"true\"></span></label><label  name=\""+jsonData[i].QuestionID+"\" id=\"addoptions"+partnumber+i+"\"></label><label name=\"1\" id=\"options1"+i+"\"></label></li>";
			question+= "<span class='title'>"+jsonData[i].QuestionDetails+"</span><div class=\"options\" id=\"option"+partnumber+i+"\" ></div>"
			if(jsonData[i].Labels!="null"){
				question+= "<label class='label'>关键词："+jsonData[i].Labels+"</label></div>"
			}
			$("#question"+partnumber).append(question);		
		}
		$('input').iCheck({
		    checkboxClass: 'icheckbox_square-blue', 
		    radioClass: 'iradio_square-blue',
		    increaseArea: '20%'
		});
		for (var j = 0; j < jsonData.length; j++) {
			if(j%2==0){
				$("#items"+(j+1)).css("background-color","#F8F8FF");
			}
		}
	});//QuestionJson	
	$.getJSON("../PageJson",{Qtype:Qtype}, function(jsonData) {			
		for (var i = 0; i < jsonData.length; i++) {
			var pages="";
			if(pageNow!="1"){
				pages+="<li><a name="+(s_pageNow-1)+" onclick=\"pagenow(this.name)\" aria-label='Previous'><span aria-hidden='true'>&laquo;</span></a></li>";
				
			}else{
				pages+="<li id='disabled'><a aria-label='Previous'><span aria-hidden='true'>&laquo;</span></a></li>";
			}
			var s_pageCount=jsonData[i].pageCount
			var pageCount=parseInt(s_pageCount);
 			for(var j=1;j<=pageCount;j++){
 				if(j>(parseInt(pageNow)+2)){
 					pages+="<li><a>……</a></li>";
 					break;
 				}
 				pages+="<li id='page"+j+"' class=\"page\"><a name="+j+" href=\"javascript:void(0)\" onclick=\"pagenow(this.name)\">"+j+"</a></li>";
 			}
   			if(pageNow!=s_pageCount){
   	 			pages+="<li><a name="+(s_pageNow+1)+" onclick=\"pagenow(this.name)\" aria-label='Next'><span aria-hidden='true'>&raquo;</span></a></li>";
   			}else{
   				pages+="<li id='disabled'><a aria-label='Next'><span aria-hidden='true'>&raquo;</span></a></li>";
   			}
		}
	 	$("#page-"+partnumber).append(pages);  
	 	$("#page"+pageNow).attr("class","active");
	 	$("#disabled").attr("class","disabled");
	});	//	PageJson
	}
	function pagenow(p){
		$("#page"+pageNow).removeAttr("class");
		pageNow=p;
		$("#page"+pageNow).attr("class","active");
		qtype(Qtype);
	}
	function addoptions(j){
		var id = $("#addoptions"+j).attr("name");
		var n = $("#options"+j).attr("name");	
		$.getJSON("../OptionJson?QuestionId="+id, function(jsonData) {
			$("label[id="+j+"]").attr("onclick", "deleteoptions(this.id)"); 
			$("label[id="+j+"]").html("收起<span class=\"glyphicon glyphicon-menu-up\" aria-hidden=\"true\"></span>");
			for (var i = 0; i < jsonData.length; i++) {
				var type = $("#TypeName"+j).attr("name");
				
				var option = "";
				if(type=="单选题"){
					if(jsonData[i].IsCorrect=="True"){
					option += "<p><input type='radio' class='optionCheck' name=\""+id+(i+1)+"\" checked='checked'>"+jsonData[i].OptionDetails+"</p>";
					}else{
					option += "<p><input type='radio' class='optionCheck' name=\""+id+(i+1)+"\">"+jsonData[i].OptionDetails+"</p>";
					} 
				}
				else if(type=="多选题"){
					if(jsonData[i].IsCorrect=="True"){
					option += "<p><input type='checkbox' class='optionCheck' name=\""+id+(i+1)+"\" checked='checked'>"+jsonData[i].OptionDetails+"</p>";
					}else{
					option += "<p><input type='checkbox' class='optionCheck' name=\""+id+(i+1)+"\">"+jsonData[i].OptionDetails+"</p>";
					} 
				}
				else if(type=="判断题"){
					if(jsonData[i].IsCorrect=="True"){
						option +="<p><input type='radio' class='optionCheck' name=\""+id+(i+1)+"\" checked='checked'/>正确</p>";
						option +="<p><input type='radio' class='optionCheck' name=\""+id+(i+1)+"\"/>错误</p>";
					}else{
						option +="<p><input type='radio' class='optionCheck' name=\""+id+(i+1)+"\"/>正确</p>";
						option +="<p><input type='radio' class='optionCheck' name=\""+id+(i+1)+"\" checked='checked'/>错误</p>";
					}
				}
				else{
					option +="<p><textarea rows='5' cols='60' id='ShortAnswer' class='form-control' readonly='readonly' name='ShortAnswer'>"+jsonData[i].OptionDetails+"</textarea></p>";
					}
				$("#option"+j).append(option);
				$(".optionCheck").attr("disabled","disabled");
				
				$('input').iCheck({
				    checkboxClass: 'icheckbox_square-blue',
				    radioClass: 'iradio_square-blue',
				    increaseArea: '20%'
				});
			}		
		});//OptionJson		
	}//addoptions	
	function deleteoptions(j){
		var n = $("#options"+j).attr("name");
		$("label[id="+j+"]").html("展开<span class=\"glyphicon glyphicon-menu-down\" aria-hidden=\"true\"></span>");
		$("label[id="+j+"]").attr("onclick", "addoptions(this.id)"); 
		$("div[id=option"+j+"]").empty(); 						
	}
	function Search(){
		var content = $("#search").val();
		var pages="";	
		$("ul[id=page-"+partnumber+"]").empty();  
		$("ul[id=question"+partnumber+"]").empty(); 
		var url=window.location.search;
		var page="1";	
		var s_page=parseInt(page); 
		
		$.getJSON("../SearchJson",{Qtype:Qtype,searchcontent:content,page:page}, function(jsonData) {			
			for (var i = 0; i < jsonData.length; i++) {
				var question= "<div class=\"items\" id=\"items"+(i+1)+"\"><li><span><span id='checkbox' style=\"position: relative\"><input type='checkbox' name=\"question"+partnumber+"\" id='batch"+(i+1)+"' value=\""+jsonData[i].QuestionID+"\"></span>"+(i+1)+".<label class=\"type\" name=\""+jsonData[i].TypeName+"\" id=\"TypeName"+partnumber+i+"\">["+jsonData[i].TypeName+"]</label><label class=\"tips\" id=\"tips"+i+"\">"+jsonData[i].SubjectName+"-"+jsonData[i].TipsName+"</label><label class=\"Difficulty\" id=\"Difficulty"+i+"\">"+jsonData[i].Difficulty+"星</label></span>";
				question+="<label onclick='addoptions(this.id)'  id=\""+partnumber+i+"\">展开<span class=\"glyphicon glyphicon-menu-down\" aria-hidden=\"true\"></span></label><label  name=\""+jsonData[i].QuestionID+"\" id=\"addoptions"+partnumber+i+"\"></label><label name=\"1\" id=\"options1"+i+"\"></label></li>";
				question+= "<span class='title'>"+jsonData[i].QuestionDetails+"</span><div class=\"options\" id=\"option"+partnumber+i+"\" ></div>"
				if(jsonData[i].Labels!="null"){
					question+= "<label class='label'>关键词："+jsonData[i].Labels+"</label></div>"
				}
				 $("#question"+partnumber).append(question);						    
			}
			$('input').iCheck({
			    checkboxClass: 'icheckbox_square-blue',
			    radioClass: 'iradio_square-blue',
			    increaseArea: '20%'
			});
		});
		
		$.getJSON("../SPageJson",{Qtype:Qtype,searchcontent:content}, function(jsonData) {		
			for (var i = 0; i < jsonData.length; i++) {
				if(page!="1"){
					pages+="<li><a id="+(s_page-1)+" onclick='SearchNext(this.id)' aria-label='Previous'><span aria-hidden='true'>&laquo;</span></a></li>";
				}else{
					pages+="<li id='disabled'><a aria-label='Previous'><span aria-hidden='true'>&laquo;</span></a></li>";
				}
				var s_pageCount=jsonData[i].pageCount			
				var pageCount=parseInt(s_pageCount);
				for(var j=1;j<=pageCount;j++){ 
					pages+="<li id='page"+j+"'><a id="+j+" onclick='SearchNext(this.id)'>"+j+"</a></li>";
				}     
				if(page!=s_pageCount){
					pages+="<li><a id="+(s_page+1)+" onclick='SearchNext(this.id)' aria-label='Next'><span aria-hidden='true'>&raquo;</span></a></li>";
				}else{
					pages+="<li id='disabled'><a aria-label='Next'><span aria-hidden='true'>&raquo;</span></a></li>";
				} 
			 }
	 		$("#page-"+partnumber).append(pages); 
	 		$("#page"+page).attr("class","active");
	 		$("#disabled").attr("class","disabled");
		});
		
		$('input').iCheck({
		    checkboxClass: 'icheckbox_square-blue',
		    radioClass: 'iradio_square-blue',
		    increaseArea: '20%'
		});
	}//Search
	
	function SearchNext(page){
		var content = $("#search").val();
		var page=page;
		var pages="";	;
		$("ul[id=page-"+partnumber+"]").empty();  
		$("ul[id=question"+partnumber+"]").empty(); 
		var s_page=parseInt(page); 
		$.getJSON("../SearchJson",{Qtype:Qtype,searchcontent:content,page:page}, function(jsonData) {
			for (var i = 0; i < jsonData.length; i++) {
				var question= "<div class=\"items\" id=\"items"+(i+1)+"\"><li><span><span id='checkbox' style=\"position: relative\"><input type='checkbox' name=\"question"+partnumber+"\" id='batch"+(i+1)+"' value=\""+jsonData[i].QuestionID+"\"></span>"+(i+1)+".<label class=\"type\" name=\""+jsonData[i].TypeName+"\" id=\"TypeName"+partnumber+i+"\">["+jsonData[i].TypeName+"]</label><label class=\"tips\" id=\"tips"+i+"\">"+jsonData[i].SubjectName+"-"+jsonData[i].TipsName+"</label><label class=\"Difficulty\" id=\"Difficulty"+i+"\">"+jsonData[i].Difficulty+"星</label></span>";
				question+="<label onclick='addoptions(this.id)'  id=\""+partnumber+i+"\">展开<span class=\"glyphicon glyphicon-menu-down\" aria-hidden=\"true\"></span></label><label  name=\""+jsonData[i].QuestionID+"\" id=\"addoptions"+partnumber+i+"\"></label><label name=\"1\" id=\"options1"+i+"\"></label></li>";
				question+= "<span class='title'>"+jsonData[i].QuestionDetails+"</span><div class=\"options\" id=\"option"+partnumber+i+"\" ></div>"
				if(jsonData[i].Labels!="null"){
					question+= "<label class='label'>关键词："+jsonData[i].Labels+"</label></div>"
				}
				 $("#question"+partnumber).append(question);						    
			}
			$('input').iCheck({
			    checkboxClass: 'icheckbox_square-blue',
			    radioClass: 'iradio_square-blue',
			    increaseArea: '20%'
			});
		});//SearchJson
		
		$.getJSON("../SPageJson",{Qtype:Qtype,searchcontent:content}, function(jsonData) {	
			for (var i = 0; i < jsonData.length; i++) {
				if(page!="1"){
					pages+="<li><a id="+(s_page-1)+" onclick='SearchNext(this.id)' aria-label='Previous'><span aria-hidden='true'>&laquo;</span></a></li>";
				}else{
					pages+="<li id='disabled'><a aria-label='Previous'><span aria-hidden='true'>&laquo;</span></a></li>";
				}
				var s_pageCount=jsonData[i].pageCount			
				var pageCount=parseInt(s_pageCount);
				for(var j=1;j<=pageCount;j++){ 
					pages+="<li id='page"+j+"'><a id="+j+" onclick='SearchNext(this.id)'>"+j+"</a></li>";
				}     
				if(page!=s_pageCount){
					pages+="<li><a id="+(s_page+1)+" onclick='SearchNext(this.id)' aria-label='Next'><span aria-hidden='true'>&raquo;</span></a></li>";
				}else{
					pages+="<li id='disabled'><a aria-label='Next'><span aria-hidden='true'>&raquo;</span></a></li>";
				}  
			}		 
		 	$("#page-"+partnumber).append(pages);  
		 	$("#page"+page).attr("class","active");
		 	$("#disabled").attr("class","disabled");
		});//SPageJson
		
		$('input').iCheck({
		    checkboxClass: 'icheckbox_square-blue',
		    radioClass: 'iradio_square-blue',
		    increaseArea: '20%'
		});//iCheck
	}//SearchNext	
	
$(document).ready(
		function(){
			//添加新部分按钮事件
			 $("#addParts").click(
						function() {
							var count = $("#addParts").attr("name");
							var num = parseInt(count) + 1;
							if (num<11){	
							var chinese = convertToChinese(num);		
							var part = "";
							var box = "";
							$("#addParts").attr("name",num.toString());					
							part += "<div id=\"parts"+num+"\" class=\"parts\"><label id=\"name"+num+"\" class=\"name\">第"+chinese+"部分</label><div class=\"form-group\" style=\"margin-bottom: 10px;\"><input  class=\"form-control\" type=\"text\" name=\"PartName"+num+"\" placeholder=\"部分名称，必填项\" /></div>";
							part += "<div class=\"form-group\" style=\"margin-bottom: 10px;\"><textarea  class=\"form-control\"  name=\"Description"+num+"\" placeholder=\"答题说明，必填项\"></textarea></div>";
							part += "<div class=\"input-group\"><label class=\"form-control\">部分分值</label><span id=\"Part"+num+"-Point\"  class=\"input-group-addon\">0</span> <label class=\"form-control\">题目总数</label><span id=\"Part"+num+"-Count\"  class=\"input-group-addon\">0</span> <input type=\"hidden\" id=\"PartPoint"+num+"\" name=\"PartPoint"+num+"\" /><input type=\"hidden\" id=\"PartCount"+num+"\" name=\"PartCount"+num+"\" /><input type=\"hidden\" id=\"oldPartCount"+num+"\" name=\"oldPartCount"+num+"\" /></div>";
							part += "<div class=\"form-group\" style=\"margin-bottom: 10px;\"><div id=\"aa\">题目列表 &emsp;<label class=\"btn btn-default\" onclick=\"showquestions(this.id)\" id=\"p"+num+"\"><span class=\"glyphicon glyphicon-chevron-up\" aria-hidden=\"true\"></span>收起</label>&emsp;<label id=\"q"+num+"\"  href=\"#modal-container-"+num+"\" onclick=\"begin("+num+")\" role=\"button\" data-toggle=\"modal\" class=\"btn  btn-primary\" >";
							part +="<span class=\"glyphicon glyphicon-plus\" aria-hidden=\"true\"></span>添加题目</label><div class=\"PartQuestions\" id=\"PartQuestions"+num+"\" ></div></div>";				
							part += "<div class=\"form-group\" style=\"margin-bottom: 10px;\"><label name=\"PartRemove\" class=\"form-control btn btn-block btn-success\"  onclick=\"removepart(this.id)\" id=\""+num+"\">删除该部分</label></div><hr style=\"border-bottom:1px dashed\"/></div>";	
							//弹框
							box += "<div class=\"modal fade\" id=\"modal-container-"+num+"\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\"><div class=\"modal-dialog\"><div class=\"modal-content\"><div class=\"modal-header\"><button type=\"button\" class=\"close\" style=\"float:right\" data-dismiss=\"modal\" aria-hidden=\"true\">×</button><h4 class=\"modal-title\" id=\"myModalLabel\">添加题目</h4></div>";
							box += "<div class=\"modal-body\"  >";
							box += "<div id=\"addQuestionbuttons\"><button id=\"allChecked\" class=\"btn btn-default\" onclick=\"selectAllDels("+num+");\">";
							box += "<span class=\"glyphicon glyphicon-check\" aria-hidden=\"true\"></span>全选</button></div>";
							box += "<div id=\"questionFilter\" class=\"btn-group\"><button id=\"addFolder\" class=\"btn btn-default dropdown-toggle\" data-toggle=\"dropdown\" aria-haspopup=\"true\" aria-expanded=\"false\">";
							box += "<span class=\"glyphicon glyphicon-filter\" aria-hidden=\"true\"></span>题型筛选&nbsp;<span class=\"caret\"></span></button>";
							box += "<ul class=\"dropdown-menu questionTypeList\" id=\"filter\"></ul></div><div id=\"questionSearch\" style=\"float:right;\">";
							box += "<input type=\"text\" name=\"search\" id=\"search\" placeholder=\"题干/知识点/题目标签\" style=\"width:15em;\" class=\"form-control\">";
							box += "<button  id=\"btn\" class=\"btn btn-default\" onclick=\"Search()\"><span class=\"glyphicon glyphicon-search\" style=\"padding-right: 0;\" aria-hidden=\"true\"></span></button></div>";
							box += "<ul class=\"article\" id=\"question"+num+"\"></ul><nav style=\"text-align:right;\"><ul class=\"pagination\" id=\"page-"+num+"\" style=\"margin:0 10px 10px 0;\"></ul></nav> ";
							box += "<div class=\"modal-footer\"><button type=\"button\" onclick=\"addQid(this.name)\" name="+num+" id=\"adds\" data-dismiss=\"modal\" class=\"btn btn-primary\">确认添加(单页添加！)</button><button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">取消</button></div></div></div></div>";
							//弹框里的问题列表
						
							$("#PaperPart").append(part);
							$("#allbox").append(box);
							$('input').iCheck({
								checkboxClass: 'icheckbox_square-blue',
								radioClass: 'iradio_square-blue',
								increaseArea: '20%'
							});
							}else{
								$("#addParts").attr("disabled","disabled");
								$("#addParts").empty();
								$("#addParts").append("最多只能添加十个部分");
							}
							
					});		 
});

function selectAllDels(num){
	var p=false;
	$("#question"+num).find(".icheckbox_square-blue").each(function(){
		//只要有一个未选中就为true，可执行全选操作
		if($(this).css("background-position")!="-48px 0px"){
			p=true;
			return false;//跳出循环
		};
	});	
	if(p){
		$("#question"+num).find(".icheckbox_square-blue").each(function(){
			$(this).attr("class","icheckbox_square-blue checked");
			$(this).children("input").attr("checked","checked");
		});
	}else{
		$("#question"+num).find(".icheckbox_square-blue").each(function(){
			$(this).attr("class","icheckbox_square-blue");
			$(this).children("input").removeAttr("checked");
		});
	}
}
 
//保存
function save() {
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
		var action=myForm.getAttribute("action");
		var TotalPoint=$("#TotalPoint").text();
		var CountQuestions=$("#CountQuestions").text();
		var Difficulty=$("#Difficulty").text();
		var num=$("#addParts").attr("name");
		myForm.setAttribute("action",myForm.getAttribute("action")+"?TotalPoint="+TotalPoint+"&num="+num+"&CountQuestions="+CountQuestions+"&Difficulty="+Difficulty);
		myForm.submit();
	}
}
//part里的题目显示隐藏以及	part的移除
function showquestions(j){
	$("#"+j).html("<span class=\"glyphicon glyphicon-chevron-down\" aria-hidden=\"true\"></span>展开");
	$("#"+j).attr("onclick", "closequestions(this.id)");
	var k=j.substring(1);
	$("#PartQuestions"+k).hide();

}
function closequestions(j){
	$("#"+j).html("<span class=\"glyphicon glyphicon-chevron-up\" aria-hidden=\"true\"></span>收起");
	$("#"+j).attr("onclick", "showquestions(this.id)");
	var k=j.substring(1);
	$("#PartQuestions"+k).show();						
}
function removepart(j){
	if(confirm("确认要执行该操作吗？")==true){	
		var count = $("#addParts").attr("name");
		var oldnum = parseInt(count);
		var newnum = parseInt(count)-1;
		$("#CountQuestions").text(parseInt($("#CountQuestions").text())-parseInt($("#Part"+j+"-Count").text()));
		$("#parts"+j).remove();
		$("#addParts").attr("name",newnum.toString());
		$("#addParts").attr("disabled",false);
		$("#addParts").empty();
		$("#addParts").append("点此添加新部分");
		var p=2;q=2;
		$(".parts").each(function(){
			$(this).attr("id","parts"+p);
			p++;
		});
		$(".name").each(function(){
			$(this).attr("id","name"+q);
			$(this).next().children("input").attr("name","PartName"+q);
			$(this).next().next().children("textarea").attr("name","Description"+q);
			$(this).next().next().next().children("span:eq(0)").attr("id","Part"+q+"-Point");
			$(this).next().next().next().children("span:eq(1)").attr("id","Part"+q+"-Count");
			$(this).next().next().next().children("input:eq(0)").attr("id","PartPoint"+q);
			$(this).next().next().next().children("input:eq(1)").attr("id","PartCount"+q);
			$(this).next().next().next().children("input:eq(2)").attr("id","oldPartCount"+q);
			$(this).next().next().next().children("input:eq(0)").attr("name","PartPoint"+q);
			$(this).next().next().next().children("input:eq(1)").attr("name","PartCount"+q);
			$(this).next().next().next().children("input:eq(2)").attr("name","oldPartCount"+q);
			$(this).next().next().next().next().children("div:eq(0)").children("label:eq(0)").attr("id","p"+q);
			$(this).next().next().next().next().children("div:eq(0)").children("label:eq(1)").attr("id","q"+q);
			$(this).next().next().next().next().children("div:eq(0)").children("label:eq(1)").attr("href","#modal-container-"+q);
			$(this).next().next().next().next().children("div:eq(0)").children("label:eq(1)").attr("onclick","begin("+q+")");
			$(this).next().next().next().next().children("div:eq(0)").children("div:eq(0)").attr("id","PartQuestions"+q);
			q++;
		});
		if(j<oldnum){
			for (var i = j; i <= newnum; i++) {
				var newchinese = convertToChinese(i);
				$("#name"+i).html("第"+newchinese+"部分");
				$("#"+oldnum).attr("id",newnum);
			}
		}
		
		$(".modal:last").remove();
		sumpart($("input[type='text']:last").attr("id"));
	}
}
//添加Part里的题目
function addQid(number){
	var my=new Array();
	var n=0;
	$("input[name='question"+number+"']:checked").each(function(){
		my[n]=$(this).val();
		n++;
		
	});
	if(my!=""){
		if(confirm("确认要执行该操作吗？")==true){	
			$("#adds").attr("data-dismiss","modal");
			for(var a=0;a<my.length;a++){	
				var v=parseInt($("#Part"+number+"-Count").text());
				$.getJSON("../PaperQuestion",{QuestionID:my[a]}, function(jsonData) {
					var PartQuestions="";
					//改样式
					PartQuestions+="<div class=\"container-fluid\" style='padding-left:0;border: 1px solid #eee;border-style:dashed;' ><div class=\"row-fluid items\" id=\"items"+number+v+"\"><input type=\"hidden\" name=\"perPoint"+number+v+"\" id=\"perPoint"+number+v+"\" /><div class=\"col-xs-12 col-md-3 col-lg-3\" id=\"quespoint\"><label style=\"margin-right:2em;\">分数：<input type=\"text\" class=\"form-control part"+number+"\" name=\""+number+"\" id=\"part"+number+v+"\" style=\"width:5em;font-size:50%;height:2.5em;\" onblur=\"sumpart(this.id)\"/></label><button type=\"button\" class=\"close\" style=\"float:right\" name=\"items"+number+v+"\" onclick=\"deletequestion(this.name)\">x</button></div>";
					PartQuestions+="<div class=\"col-xs-12 col-md-9 col-lg-9\"><li><span>"+(v+1)+".&emsp;<label class=\"type\" name=\""+jsonData.QTypeId+"\" id=\"TypeName"+v+"\"></label><label class=\"Difficulty"+number+"\" id=\"Difficulty"+number+v+"\">"+jsonData.Difficulty+"星</label></span> <span class='title'>"+jsonData.QuestionDetails+"</span>";
					PartQuestions+="<label onclick='up(this.id)' name=\""+v+"\" id=\"op"+number+v+"\">展开<span class=\"glyphicon glyphicon-menu-down\" aria-hidden=\"true\"></span></label><input type=\"hidden\" id=\"open"+number+v+"\" name=\"QuestionId"+number+v+"\" value=\""+jsonData.QuestionID+"\" /><div class=\"options\" id=\"openoptions"+number+v+"\"></div></div></li></div></div>";				
					v++;				
					$("#PartQuestions"+number).append(PartQuestions); 	
				});
			}	
			var count=my.length;
			var allcount=0;
			count+=parseInt($("#Part"+number+"-Count").text());
			$("#PartCount"+number).val(count);
			$("#oldPartCount"+number).val(count);
			$("#Part"+number+"-Count").text(count);
			for(var i=0;i<number;i++){
				allcount+=parseInt($("#Part"+(i+1)+"-Count").text());
			}
			$("#CountQuestions").text(allcount);
			$("input[name='question"+number+"']:checked").attr("disabled","disabled");
			$("input[name='question"+number+"']:disabled").attr("checked",false);
		}else{
			$("#adds").removeAttr("data-dismiss");
		}
	} else{
		alert("您未选中任何题目");
		$("#adds").removeAttr("data-dismiss");
	}
} 	
function deletequestion(itemsid){
	var numberv=itemsid.substring(5);
	var thenumber=$("#part"+numberv).attr("name");
	var itemsida=$("#perPoint"+numberv).val();
	$("#part"+numberv).val(0);
	sumpart("part"+numberv);
	$("#"+itemsid).remove();
	$("input[name='question"+thenumber+"']:disabled").attr("disabled",false);
	var allcount=parseInt($("#CountQuestions").text())-parseInt($("#Part"+thenumber+"-Count").text());
	var partcount=parseInt($("#PartCount"+thenumber).val());
	$("#PartCount"+thenumber).val(partcount-1);
	$("#Part"+thenumber+"-Count").text(partcount-1);
	allcount+=parseInt($("#Part"+thenumber+"-Count").text());
	$("#CountQuestions").text(allcount);
}

function sumpart(j){//Pno是第几部分；Jno是第几部分第几题;s是总分;d是每道题的难度;a是每道题的分；
	var Pno=$("#"+j).attr("name");
	var AllPno=parseInt($("#addParts").attr("name"))+1;
	var spart=0;
	var Jno=j.substring(4);	
	var difficulty=0;
	for (var o=0;o<=AllPno;o++){
		$(".part"+o).each(function(){
			var a=$(this).val();
			if(a.length>0){
				if(o==Pno){
					spart+=parseInt(a);
				}
				$("#perPoint"+Jno).val(a);
				var d=$(this).parent().parent().next().find("span:first").children(".Difficulty"+o).text();
				difficulty+=parseInt(a)*parseInt(d);
			}
		});
	}

	$("#Part"+Pno+"-Point").text(spart);
	$("#PartPoint"+Pno).val(spart); 	
	var s=0;
	var count = $("#addParts").attr("name");
	var Allno = parseInt(count);
	for(var i=0;i<Allno;i++){
			s+=parseInt($("#Part"+(i+1)+"-Point").text());
	}
	difficulty=difficulty/s;
	if(difficulty.toString().length>4){	
		difficulty=difficulty.toString().substring(0,4);
	}
	if(isNaN(difficulty)){
		
		$("#Difficulty").text(0);
	}else{
		
		$("#Difficulty").text(difficulty);
	}
	$("#TotalPoint").text(s);
	
}
function down(j){
	var k=j.substring(2);
	$("label[id="+j+"]").html("展开<span class=\"glyphicon glyphicon-menu-down\" aria-hidden=\"true\"></span>");
	$("label[id="+j+"]").attr("onclick", "up(this.id)"); 
	$("div[id=\openoptions"+k+"\]").empty(); 
}
function up(j){ 
	var k=j.substring(2);
	var id = $("#open"+k).val();
	var s=$("#"+j).attr("name");
	$.getJSON("../OptionJson?QuestionId="+id, function(jsonData) {
		$("label[id="+j+"]").attr("onclick", "down(this.id)"); 
		$("label[id="+j+"]").html("收起<span class=\"glyphicon glyphicon-menu-up\" aria-hidden=\"true\"></span>");
		for (var i = 0; i < jsonData.length; i++) {
			var type = $("#TypeName"+s).attr("name");
			var option = "";
			if(type=="1"){
				if(jsonData[i].IsCorrect=="True"){
				option += "<p><input type='radio' class='optionCheck' name=\""+id+(i+1)+"\" checked='checked'>"+jsonData[i].OptionDetails+"</p>";
				}else{
				option += "<p><input type='radio' class='optionCheck' name=\""+id+(i+1)+"\">"+jsonData[i].OptionDetails+"</p>";
				} 
			}
			else if(type=="2"){
				if(jsonData[i].IsCorrect=="True"){
				option += "<p><input type='checkbox' class='optionCheck' name=\""+id+(i+1)+"\" checked='checked'>"+jsonData[i].OptionDetails+"</p>";
				}else{
				option += "<p><input type='checkbox' class='optionCheck' name=\""+id+(i+1)+"\">"+jsonData[i].OptionDetails+"</p>";
				} 
			}
			else if(type=="3"){
				if(jsonData[i].IsCorrect=="True"){
					option +="<p><input type='radio' class='optionCheck' name=\""+id+(i+1)+"\" checked='checked'/>正确</p>";
					option +="<p><input type='radio' class='optionCheck' name=\""+id+(i+1)+"\"/>错误</p>";
				}else{
					option +="<p><input type='radio' class='optionCheck' name=\""+id+(i+1)+"\"/>正确</p>";
					option +="<p><input type='radio' class='optionCheck' name=\""+id+(i+1)+"\" checked='checked'/>错误</p>";
				}
			}
			else{
				option +="<p><textarea rows='5' cols='60' id='ShortAnswer' class='form-control' readonly='readonly' name='ShortAnswer'>"+jsonData[i].OptionDetails+"</textarea></p>";
				}
			$("#openoptions"+k).append(option);
			$(".optionCheck").attr("disabled","disabled");
			
			$('input').iCheck({
			    checkboxClass: 'icheckbox_square-blue',
			    radioClass: 'iradio_square-blue',
			    increaseArea: '20%'
			});
		}
	
	});
	
}







	
	

