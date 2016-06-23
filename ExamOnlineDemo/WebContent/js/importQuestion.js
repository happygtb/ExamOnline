/**
 * 
 */

function submitFunction() {
	if (myForm.subject.value == "0") {
		alert("请先选择科目");
	}
	else if (myForm.Tips.value == "0") {
		alert("请先选择知识点");
	}
	else {
		var action="uploadFiles.jsp?";
		action+="subject="+$("#subject").val();
		action+="&Tips="+$("#Tips").val();
		$("#myForm").attr("action",action);
		
		myForm.submit();
	}
}

function FileUpload(obj,inputName){ 
	var fileObject=$("#excel");
	var filepath=fileObject.val(); 
	var fileArr=filepath.split("\\");
	var fileTArr=fileArr[fileArr.length-1].toLowerCase().split("."); 
	var filetype=fileTArr[fileTArr.length-1]; 
	if(filetype!="xlsx"&&filetype!="xls"){ 
		fileObject.focus(); 
		$("input[name='"+inputName+"']").val("");
		alert("上传文件类型不符！"); 
	}else{
		var file_name = fileArr[0]+"\\...\\"+fileArr[fileArr.length-1];
		$("input[name='"+inputName+"']").val(file_name);
	}
}

$(document).ready(
		function(){
			$.getJSON("../QTypeJson", function(jsonData) {
				var list="";
				for (var i = 0; i < jsonData.length; i++) {
					list+="<li><a href=\"QuestionIndex.html?Qtype="+jsonData[i].QTypeId+"\" >"+jsonData[i].TypeName+"</a>";
				}
				$(".questionTypeList").append(list);
			});
			
			$.getJSON("../SubjectJson", function(jsonData) {
				var html = "";
				for (var i = 0; i < jsonData.length; i++) {
					html += "<option value=\""+jsonData[i].SubjectId+"\">"
							+ jsonData[i].SubjectName + "</option>";
				}
				$("#subject").append(html);
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
		});