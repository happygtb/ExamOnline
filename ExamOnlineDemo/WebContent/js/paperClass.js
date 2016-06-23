/**
 * 
 */

$(document).ready(
		function(){			
			$.getJSON("../PaperClassJson", function(jsonData) {
				var list="";
				var html="";
				for (var i = 0; i < jsonData.length; i++) {
					html+="<div id=\"folderdiv\" class=\"col-xs-12 col-md-2 col-lg-2\"><input type='checkbox' name=\"pcId\" class='check' value='"+jsonData[i].PaperClassId+"' />";
					html+="<div id=\"folder"+jsonData[i].PaperClassId+"\" class=\"folder\"><img src=\"../images/folder.jpg\" id=\"folder\"/>";
					html+="<label class=\"foldername\" id=\"foldername"+jsonData[i].PaperClassId+"\" >"+jsonData[i].PaperClassName+"</label></div>";
					html+="</div>";
					list+="<li><a href=\"PaperIndex.html?PaperClassId="+jsonData[i].PaperClassId+"\" >"+jsonData[i].PaperClassName+"</a></li>";
				}
				$("#paperClass").append(html);
				$(".paperClassList").append(list);
				$('input').iCheck({
					checkboxClass: 'icheckbox_square-aero',
					radioClass: 'iradio_square-aero',
					increaseArea: '20%'
				});
			});
		});

function addFolder(){
	var html="";
	html+="<div id=\"folderdiv\" class=\"col-xs-12 col-md-2 col-lg-2\"><div class=\"newfolder\"><img id=\"new\" src=\"../images/folder.jpg\" />";
	html+="<input type='text' name='newFolderName' id='newFolderName' onkeypress=\"if(event.keyCode==13){newfolder();}\" onblur='newfolder();' class='form-control' /></div></div>";
	$("#paperClass").append(html);
	$("#addFolder").attr("disabled","disabled");
	$("#newFolderName").focus();
}

function newfolder(){
	var newfolder=$("#newFolderName").val();
	$("#newFolderName").removeAttr("onblur");
	if(newfolder!=""&&confirm("确认要添加吗？")==true){
		submitNewFolder();
	}else{
		$(".newfolder").parent().remove();
		$("#addFolder").removeAttr("disabled");
	}
}

function submitNewFolder(){
	var newfolder=$("#newFolderName").val();
	var tf=true;
	$(".foldername").each(function(){
		var str=$(this).text();
		if(newfolder==str){
			tf=false;
		}
	});
	if(tf){
		window.location.href="../updatePaperClass?newPaperClass="+newfolder+"&id="; 
	}else{
		alert("试卷分类名称重复！");
	}
}

function checkAll(){
	var p=false;
	$(".icheckbox_square-aero").each(function(){
		//只要有一个未选中就为true，可执行全选操作
		if($(this).css("background-position")!="-48px 0px"){
			p=true;
			return false;//跳出循环
		};
	});	
	if(p){
		$(".icheckbox_square-aero").each(function(){
			$(this).attr("class","icheckbox_square-aero checked");
			$(".check").attr("checked","checked");
		});
	}else{
		$(".icheckbox_square-aero").each(function(){
			$(this).attr("class","icheckbox_square-aero");
			$(".check").removeAttr("checked");
		});
	}
}

function renameFolder(){
	var count=0;
	$(".icheckbox_square-aero").each(function(){
		if($(this).css("background-position")=="-48px 0px"){
			count++;
		};
	});	
	if(count==0){
		alert("请先选择一个试卷分类");
	}else if(count>1){
		alert("一次只能对一个试卷分类进行重命名");
	}else{
		var pcNum=$(".checked").children("input").val();
		var str=$("#foldername"+pcNum).text();
		var html="<input type='text' name='newFolderName' id='newFolderName' onkeypress=\"if(event.keyCode==13){updateFolder("+pcNum+");}\" onblur='doit(\""+str+"\","+pcNum+");' class='form-control' value='"+str+"' />";
		$("#folder"+pcNum).append(html);
		$("#newFolderName").focus();
	}
}
function updateFolder(id){
	var newName=$("#newFolderName").val();
	var tf=true;
	if(newName!=""){
		$(".foldername").each(function(){
			var str=$(this).text();
			if(newName==str){
				tf=false;
			}
		});
		if(tf){
			window.location.href="../updatePaperClass?id="+id+"&newPaperClass="+newName; 
		}else{
			alert("试卷分类名称重复！");
		}
	}else{
		alert("请输入新的试卷分类");
	}
}

function doit(str,num){
	var newfolder=$("#newFolderName").val();
	var p=$("#newFolderName").attr("onblur").split(",")[1].split(")")[0];
	if(newfolder!=""&&confirm("确认要更改该名称吗？")==true){
		updateFolder(p);
	}else{
		var q=$("#newFolderName").parent().attr("id").substring(6);
		$("#newFolderName").remove();
		$("#folder"+q).attr("onclick","editFolder("+q+","+p+");");
		$(".foldername").each(function(){
			var i=$(this).parent().attr("id").substring(6);
			var j=$(this).attr("id").substring(10);
			$(this).parent().attr("onclick","editFolder("+i+","+j+");");
		});
		var htm="<label class=\"foldername\" id=\"foldername"+p+"\" >"+str+"</label>";
		$("#folder"+q).append(htm);
	}
}

function deleteFolder(){
	var count=0;
	var pcId=new Array();
	$(".icheckbox_square-aero").each(function(){
		if($(this).css("background-position")=="-48px 0px"){
			count++;
			pcId[count]=$(this).children("input").val();
		};
	});
	if(count==0){
		alert("请至少选择一个试卷分类");
	}else{
		if(confirm("操作不可恢复，确认要删除吗？（该目录下的试卷不会删除）")==true){
			window.location.href="../deletePaperClass?count="+count+"&pcId="+pcId; 
			/*deletePaperClass.submit();*/
		}
	}
}