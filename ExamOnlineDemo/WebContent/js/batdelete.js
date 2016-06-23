/**
 * 
 */

	var pages="";	
	var url=window.location.search;
	var pageNow="1",Qtype="";	
	if(url.indexOf("?")!=-1){   
		var str=url.substr(1)   
		strs=str.split("&");
		for(i=0;i<strs.length;i++){   
	    	if([strs[i].split("=")[0]]=='pageNow') pageNow=unescape(strs[i].split("=")[1]);
	    	if([strs[i].split("=")[0]]=='Qtype') Qtype=unescape(strs[i].split("=")[1]);
	  	}  	  	
	}
	
	$(document).ready(
		function(){
			$.getJSON("../QTypeJson",function(jsonData){
				var list="";
				for(var i=0; i < jsonData.length; i++) {
					list+="<li><a href=\"QuestionIndex.html?Qtype="+jsonData[i].QTypeId+"\" >"+jsonData[i].TypeName+"</a>";
				}
				$(".questionTypeList").append(list);
			});
		});
	
	var s_pageNow=parseInt(pageNow); 
	$.getJSON("../QuestionJson",{pageNow:pageNow,Qtype:Qtype}, function(jsonData) {
		for (var i = 0; i < jsonData.length; i++) {
			var question= "<div class=\"items\" id=\"items"+(i+1)+"\"><li><span><span id='checkbox'><input type='checkbox' name='question' id='batch"+(i+1)+"' value=\""+jsonData[i].QuestionID+"\"></span>"+(i+1)+".<label class=\"type\" name=\""+jsonData[i].TypeName+"\" id=\"TypeName"+i+"\">["+jsonData[i].TypeName+"]</label><label class=\"tips\" id=\"tips"+i+"\">"+jsonData[i].SubjectName+"-"+jsonData[i].TipsName+"</label><label class=\"Difficulty\" id=\"Difficulty"+i+"\">"+jsonData[i].Difficulty+"星</label></span>";
			question+="<label onclick='addoptions(this.id)'  id=\""+i+"\">展开<span class=\"glyphicon glyphicon-menu-down\" aria-hidden=\"true\"></span></label><label  name=\""+jsonData[i].QuestionID+"\" id=\"addoptions"+i+"\"></label></li>";
			question+= "<span class='title'>"+jsonData[i].QuestionDetails+"</span><div class=\"options\" id=\"option"+i+"\" ></div>"
			if(jsonData[i].Labels!="null"){
				question+= "<label class='label'>关键词："+jsonData[i].Labels+"</label></div>"
			}
			$("#question").append(question);
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
	});
	
	$.getJSON("../PageJson",{Qtype:Qtype}, function(jsonData) {			
		for (var i = 0; i < jsonData.length; i++) {
			if(pageNow!="1"){
				pages+="<li><a href='batDelete.html?pageNow="+(s_pageNow-1)+"&Qtype="+Qtype+"' aria-label='Previous'><span aria-hidden='true'>&laquo;</span></a></li>";
			}else{
				pages+="<li id='disabled'><a aria-label='Previous'><span aria-hidden='true'>&laquo;</span></a></li>";
			}
			var s_pageCount=jsonData[i].pageCount
			var pageCount=parseInt(s_pageCount);
 			for(var j=1;j<=pageCount;j++){
 				/*if(j>pageNow-1){
 					pages+="<li><a>……</a></li>";
 					j=pageCount-1;
 				}*/
 				if(j>(parseInt(pageNow)+2)){
 					pages+="<li><a>……</a></li>";
 					break;
 				}
 				pages+="<li id='page"+j+"'><a href='batDelete.html?pageNow="+j+"&Qtype="+Qtype+"'>"+j+"</a></li>";
 			}
   			if(pageNow!=s_pageCount){
   	 			pages+="<li><a href='batDelete.html?pageNow="+(s_pageNow+1)+"&Qtype="+Qtype+"' aria-label='Next'><span aria-hidden='true'>&raquo;</span></a></li>";
   			}else{
   				pages+="<li id='disabled'><a aria-label='Next'><span aria-hidden='true'>&raquo;</span></a></li>";
   			}
		}
	 	$("#page").append(pages);  
	 	$("#page"+pageNow).attr("class","active");
	 	$("#disabled").attr("class","disabled");
	});		
	
	function addoptions(j){
		var id = $("#addoptions"+j).attr("name");
		
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
		
		});
		
	}
	
	function deleteoptions(j){
		$("label[id="+j+"]").html("展开<span class=\"glyphicon glyphicon-menu-down\" aria-hidden=\"true\"></span>");
		$("label[id="+j+"]").attr("onclick", "addoptions(this.id)"); 
		$("div[id=option"+j+"]").empty(); 						
	}

	function Search(){
		var content = $("#search").val();
		var pages="";	
		var question="";
		$("ul[id=question]").empty(); 
		$("ul[id=page]").empty(); 
		var url=window.location.search;
		var page="1";	
		if(url.indexOf("?")!=-1){   
			var str=url.substr(1)   
			strs=str.split("&");   
			for(i=0;i<strs.length;i++){   
		    	if([strs[i].split("=")[0]]=='page') page=unescape(strs[i].split("=")[1]);
		  	}  	  	
		}
		var s_page=parseInt(page); 
		
		$.getJSON("../SearchJson",{Qtype:Qtype,searchcontent:content,page:page}, function(jsonData) {			
			for (var i = 0; i < jsonData.length; i++) {
				question= "<div class=\"items\" id=\"items"+(i+1)+"\"><li><span><span id='checkbox'><input type='checkbox' name='question' id='batch"+(i+1)+"' value=\""+jsonData[i].QuestionID+"\"></span>"+(i+1)+".<label class=\"type\" name=\""+jsonData[i].TypeName+"\" id=\"TypeName"+i+"\">["+jsonData[i].TypeName+"]</label><label class=\"tips\" id=\"tips"+i+"\">"+jsonData[i].SubjectName+"-"+jsonData[i].TipsName+"</label><label class=\"Difficulty\" id=\"Difficulty"+i+"\">"+jsonData[i].Difficulty+"星</label></span>";
				question+="<label onclick='addoptions(this.id)'  id=\""+i+"\">展开<span class=\"glyphicon glyphicon-menu-down\" aria-hidden=\"true\"></span></label><label  name=\""+jsonData[i].QuestionID+"\" id=\"addoptions"+i+"\"></label></li>";
				question+= "<span class='title'>"+jsonData[i].QuestionDetails+"</span><div class=\"options\" id=\"option"+i+"\" ></div>"
				if(jsonData[i].Labels!="null"){
					 question+= "<span class='label'>关键词："+jsonData[i].Labels+"</span></div>";
				 }
				 $("#question").append(question);						    
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
	 		$("#page").append(pages); 
	 		$("#page"+page).attr("class","active");
	 		$("#disabled").attr("class","disabled");
		});
		
		$('input').iCheck({
		    checkboxClass: 'icheckbox_square-blue',
		    radioClass: 'iradio_square-blue',
		    increaseArea: '20%'
		});
	}
	
	function SearchNext(page){
		var content = $("#search").val();
		var pages="";	
		var question="";
		$("ul[id=question]").empty(); 
		$("ul[id=page]").empty(); 
		var s_page=parseInt(page); 
		$.getJSON("../SearchJson",{Qtype:Qtype,searchcontent:content,page:page}, function(jsonData) {
			for (var i = 0; i < jsonData.length; i++) {
				question= "<div class=\"items\" id=\"items"+(i+1)+"\"><li><span><span id='checkbox'><input type='checkbox' name='question' id='batch"+(i+1)+"' value=\""+jsonData[i].QuestionID+"\"></span>"+(i+1)+".<label class=\"type\" name=\""+jsonData[i].TypeName+"\" id=\"TypeName"+i+"\">["+jsonData[i].TypeName+"]</label><label class=\"tips\" id=\"tips"+i+"\">"+jsonData[i].SubjectName+"-"+jsonData[i].TipsName+"</label><label class=\"Difficulty\" id=\"Difficulty"+i+"\">"+jsonData[i].Difficulty+"星</label></span>";
				question+="<label onclick='addoptions(this.id)'  id=\""+i+"\">展开<span class=\"glyphicon glyphicon-menu-down\" aria-hidden=\"true\"></span></label><label  name=\""+jsonData[i].QuestionID+"\" id=\"addoptions"+i+"\"></label></li>";
				question+= "<span class='title'>"+jsonData[i].QuestionDetails+"</span><div class=\"options\" id=\"option"+i+"\" ></div>"
				if(jsonData[i].Labels!="null"){
					 question+= "<span class='label'>关键词："+jsonData[i].Labels+"</span></div>";
				 }
				 $("#question").append(question);						    
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
		 	$("#page").append(pages);  
		 	$("#page"+page).attr("class","active");
		 	$("#disabled").attr("class","disabled");
		});
		
		$('input').iCheck({
		    checkboxClass: 'icheckbox_square-blue',
		    radioClass: 'iradio_square-blue',
		    increaseArea: '20%'
		});
	}
	
	function CheckForm(){
		var b="";
		$("input[name='question']:checked").each(function(){
			b=$(this).val();
		});
		if(b!=""){
			if(confirm("确认要执行该操作吗？")==true)
		    	deleteQuestions.submit();
		  	else
		   		return false;
		} else{
			alert("您未选中任何题目");
		}
	} 