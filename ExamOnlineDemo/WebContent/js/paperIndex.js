/**
 * 
 */

var url = location.href;
var es=/PaperClassId=/;
es.exec(url);
var PaperClassId=RegExp.rightContext;

$(document).ready(
		function(){			
			$.getJSON("../PaperClassJson", function(jsonData) {
				var list="";
				var htm="";
				for (var i = 0; i < jsonData.length; i++) {
					list+="<li><a href=\"PaperIndex.html?PaperClassId="+jsonData[i].PaperClassId+"\" >"+jsonData[i].PaperClassName+"</a></li>";
					htm+="<li><a href='#' onclick='filter("+jsonData[i].PaperClassId+");'>"+jsonData[i].PaperClassName+"</a></li>";
				}
				$("#filter").append(htm);
				$(".paperClassList").append(list);
				$('input').iCheck({
					checkboxClass: 'icheckbox_square-aero',
					radioClass: 'iradio_square-aero',
					increaseArea: '20%'
				});
			});
			
			$.getJSON("../PapersJson",{PaperClassId:PaperClassId,method:"normal"}, function(jsonData) {
				var htm="";
				for (var i = 0; i < jsonData.length; i++) {
					htm+="<tr><td><input type='checkbox' name='normalPapers' class='check' value='normal_"+jsonData[i].PaperId+"' /></td><td>人工/自动组卷</td><td>";
					htm+=jsonData[i].PaperClassName+"</td><td><a href='#'>"+jsonData[i].PaperName+"</a></td><td><button class=\"btn btn-default\">编辑</button>&emsp;";
					htm+="<button class=\"btn btn-default\" onclick=\"singleDelte('normal',"+jsonData[i].PaperId+");\">删除</button></td></tr>";
				}
				$("#paperList").append(htm);
				$('input').iCheck({
				    checkboxClass: 'icheckbox_square-blue',
				    radioClass: 'iradio_square-blue',
				    increaseArea: '20%'
				});
			});
			$.getJSON("../PapersJson",{PaperClassId:PaperClassId,method:"imported"}, function(jsonData) {
				var html="";
				for (var i = 0; i < jsonData.length; i++) {
					html+="<tr><td><input type='checkbox' name='importedPapers' class='check' value='imported_"+jsonData[i].PaperId+"' /></td><td>导入试卷</td><td>";
					html+=jsonData[i].PaperClassName+"</td><td><a href='TempPaperPreview.html?paperId="+jsonData[i].PaperId+"'>"+jsonData[i].PaperName+"</a></td><td><button class=\"btn btn-default disabled\">编辑</button>";
					html+="&emsp;<button class=\"btn btn-default\" onclick=\"singleDelte('imported',"+jsonData[i].PaperId+");\">删除</button></td></tr>";
				}
				$("#paperList").append(html);
				$('input').iCheck({
				    checkboxClass: 'icheckbox_square-blue',
				    radioClass: 'iradio_square-blue',
				    increaseArea: '20%'
				});
			});
			
		});

function checkAll(){
	var p=false;
	$(".icheckbox_square-blue").each(function(){
		//只要有一个未选中就为true，可执行全选操作
		if($(this).css("background-position")!="-48px 0px"){
			p=true;
			return false;//跳出循环
		};
	});	
	if(p){
		$(".icheckbox_square-blue").each(function(){
			$(this).attr("class","icheckbox_square-blue checked");
			$(".check").attr("checked","checked");
		});
	}else{
		$(".icheckbox_square-blue").each(function(){
			$(this).attr("class","icheckbox_square-blue");
			$(".check").removeAttr("checked");
		});
	}
}

function deletePapers(){
	var count=0;
	var pcId=new Array();
	$(".icheckbox_square-blue").each(function(){
		if($(this).css("background-position")=="-48px 0px"){
			count++;
			pcId[count]=$(this).children("input").val();
		};
	});
	if(count==0){
		alert("请至少选择一个试卷分类");
	}else{
		if(confirm("操作不可恢复，确认要删除选中的试卷吗？")==true){
			window.location.href="../DeletePapers?method=batch&count="+count+"&paperId="+pcId; 
		}
	}
}

function singleDelte(type,id){
	var href="../DeletePapers?method=single&";
	href+="type="+type+"&paperId="+id;
	if(confirm("操作不可恢复，确认要删除选中的试卷吗？")==true){
		window.location.href=href;
	}
}