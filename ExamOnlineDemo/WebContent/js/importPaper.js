/**
 * 
 */

function submitFunction() {
	if (myForm.paperClass.value == "0") {
		alert("请先选择试卷分类");
	}
	else {
		var action="uploadFiles.jsp?";
		action+="paperClass="+$("#paperClass").val();
		$("#myForm").attr("action",action);
		
		myForm.submit();
	}
}

function FileUpload(obj,inputName){ 
	var fileObject=$("#docx");
	var filepath=fileObject.val(); 
	var fileArr=filepath.split("\\");
	var fileTArr=fileArr[fileArr.length-1].toLowerCase().split("."); 
	var filetype=fileTArr[fileTArr.length-1]; 
	if(filetype!="doc"){
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
			$.getJSON("../PaperClassJson", function(jsonData) {
				var html = "";
				var list="";
				for (var i = 0; i < jsonData.length; i++) {
					html += "<option value=\""+jsonData[i].PaperClassId+"\">"
							+ jsonData[i].PaperClassName + "</option>";
					list+="<li><a href=\"PaperIndex.html?PaperClassId="+jsonData[i].PaperClassId+"\" >"+jsonData[i].PaperClassName+"</a></li>";
				}
				$("#paperClass").append(html);
				$(".paperClassList").append(list);
			});
			
		});