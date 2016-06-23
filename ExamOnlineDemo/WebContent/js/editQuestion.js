/**
 * 
 */

function editFunction() {
		var action=editForm.getAttribute("action");
		var str="=";
		var qtype=action.split(str);
		qtype=qtype[3];	
		if(qtype=="SingleChoice"){
			var num=document.getElementById("addOptions").getAttribute("name");
			var TrueId = $(".checked").parent().next().next().find("label[name='SCRemove']").attr("id");
			$("div[id='"+"items"+TrueId+"']").find("input[name='SCoptions']").val(TrueId);
		}
		if(qtype=="MultipleChoice"){
			var num=document.getElementById("addMCoptions").getAttribute("name");
		}
		editForm.setAttribute("action",editForm.getAttribute("action")+"&num="+num);
		editForm.submit();
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