/**
 * 
 */

var oldQtypeName="";
var c=0;

$(document).ready(
	function(){
		var pags=1;
  		var pageNow="";
  		var url=window.location.search;	
  		if(url.indexOf("?")!=-1){   
			var str=url.substr(1)   
			strs=str.split("&");   
			for(i=0;i<strs.length;i++){   
		    	if([strs[i].split("=")[0]]=='pageNow') pageNow=unescape(strs[i].split("=")[1]);
		  	}  	  	
		}
		if(pageNow!=""){ 
			pags=parseInt(pageNow); 
		}
		$.getJSON("../QTypeJson",function(jsonData){
			var qtype = "";
			var list="";
			$("#addQType").attr("name",jsonData.length);
			var count=jsonData.length;
			var pagcount=1;
			var percount=3;
			if(count%percount==0){    
				pagcount=count/percount;   
			}else{    
				pagcount=parseInt(count/percount)+1;   
			}	
			var pages="";
			if(pags!="1"){
				pages+="<li><a href='checkQuestionType.html?pageNow="+(pags-1)+"' aria-label='Previous'><span aria-hidden='true'>&laquo;</span></a></li>";
			}
			for(var j=1;j<=pagcount;j++){
				pages+="<li id='page"+j+"'><a href='checkQuestionType.html?pageNow="+j+"'>"+j+"</a></li>";
			}
 			if(pags!=pagcount){
 				pages+="<li><a href='checkQuestionType.html?pageNow="+(pags+1)+"' aria-label='Next'><span aria-hidden='true'>&raquo;</span></a></li>";
			}
	 		$("#page").append(pages);
	 		$("#page"+pags).attr("class","active");
	 		
			for(var i=(pags-1)*percount;i<pags*percount && i < jsonData.length; i++) {
				if(jsonData[i].QTypeId=="1"||jsonData[i].QTypeId=="2"||jsonData[i].QTypeId=="3"||jsonData[i].QTypeId=="4"){
					qtype += "<tr><td>"+(i+1)+"</td><td>"+jsonData[i].TypeName+"</td><td>默认题型</td></tr>";
				}else{
					qtype += "<tr><td>"+(i+1)+"</td><td>"+jsonData[i].TypeName+"</td><td><a id='modal-858340' href='#modal-container-858340' role='button' data-toggle='modal' onclick='myFunction("+jsonData[i].QTypeId+",\""+jsonData[i].TypeName+"\");' >修改</a></td></tr>";
				}
			}
			for(var i=0; i < jsonData.length; i++) {
				list+="<li><a href=\"QuestionIndex.html?Qtype="+jsonData[i].QTypeId+"\" >"+jsonData[i].TypeName+"</a>";
			}
			$("#QType").append(qtype);
			$(".questionTypeList").append(list);
		});
		
		$("#addSubQtype").click(
			function() {
				var count = $("#addSubQtype").attr("name");
				var num = parseInt(count) + 1;
				if(num<=3){
					var htm1 = "";
					$("#addSubQtype").attr("name",num.toString());
					htm1+="<tr id='SubQtype_"+num+"'><td>子题型</td><td><select name='QType"+num+"' id='QType"+num+"' class='selector form-control' style='width: 8em;'>";
					htm1+="<option value='1'>单选题</option><option value='2'>多选题</option><option value='3'>判断题</option><option value='4'>简答题</option></select>";
					htm1+="</td><td>数量</td><td><div class='input-group'><input name='amount"+num+"' type='text' class='form-control' >";
					htm1+="<span class='input-group-addon' id='basic-addon2'>个</span></div></td>";
					htm1+="<td><label id=\""+num+"\" name=\"SCRemove1\" onclick=\"removeSubQtype1(this.id)\" class=\"close\" aria-label=\"Close\">";
					htm1+="<span aria-hidden=\"true\">&times;</span></label></td></tr>";
					$("#addSubQtype").parent().parent().before(htm1);
				}
				if(count==2){
					$("#addSubQtype").attr("disabled","disabled");
					$("#addSubQtype").empty();
					$("#addSubQtype").append("最多只能添加三种子题型");
				}
				$("#myForm").attr("action","../AddQType?num="+num);
			});
		});

	
	function myFunction(id,name){
		$("#newQType").val(name);
		$("#qtypeAlarming").empty();
		oldQtypeName=name;
		$("#updateQType").attr("action","../UpdateQType?QTypeId="+id);
		$("#editQtype tr:gt(1)").remove();
		var str="";
		$.getJSON("../SingleQTypeJson",{QTypeId:id},function(jsonData){
			for(var i=0;i<jsonData.length;i++){
				str+="<tr id='SubQtype"+(i+1)+"'><td>子题型</td><td>";
				str+="<select name='QType"+(i+1)+"' id='QType"+(i+1)+"' class='selector form-control'>";
				str+="<option value='1'>单选题</option><option value='2'>多选题</option>";
				str+="<option value='3'>判断题</option><option value='4'>简答题</option></select></td>";
				str+="<td>数量</td><td><div class='input-group'><input name='amount"+(i+1)+"' type='text' class='form-control' value='"+jsonData[i].amount+"' >";
				str+="<span class='input-group-addon' id='basic-addon2'>个</span></div></td>";
				str+="<td><label id=\""+(i+1)+"\" name=\"SCRemove\" onclick=\"removeSubQtype(this.id)\" class=\"close\" aria-label=\"Close\">";
				str+="<span aria-hidden=\"true\">&times;</span></label></td></tr>";
			}
			$("#identity").after(str);
			for(var i=0;i<jsonData.length;i++){
				$("select[name='QType"+(i+1)+"'] option[value='"+jsonData[i].SubQTypeId+"']").attr("selected","selected");
			}
			var s="<tr><td colspan=\"5\"><label id=\"addSubQtype1\" onclick=\"addSubQtype();\" name=\""+jsonData.length+"\" class=\"btn btn-block btn-primary\">点此添加一个子题型</label></td></tr>";
			$("#editQtype tr:eq("+(jsonData.length+1)+")").after(s);
			if(jsonData.length=="3"){
				$("#addSubQtype1").attr("disabled","disabled");
				$("#addSubQtype1").empty();
				$("#addSubQtype1").append("最多只能添加三种子题型");
			}
			c=jsonData.length;
		});
	} 

	function addQType(){
		var str=$("#QTypeName").val();
		$.getJSON("../SingleJson",{table:"QuestionType",keyword:"TypeName",value:str},function(jsonData){
				var isExist = jsonData.exist;
				if(isExist=="1"){
					$("#qtypeWarning").append("该题型已存在，无需重复添加");
				}else{
					$("#myForm").attr("action","../AddQType?num="+parseInt($("#addSubQtype").attr("name")));
					myForm.submit();
				}
			});  
	}
	
	function editQtype(){
		var str=$("#newQType").val();
		$("#qtypeAlarming").empty();
		if(str!=oldQtypeName){
			$.getJSON("../SingleJson",{table:"QuestionType",keyword:"TypeName",value:str},function(jsonData){
					var isExist = jsonData.exist;
					if(isExist=="1"){
						$("#qtypeAlarming").append("与现有题型命名重复，请修改");
					}else{
						$("#updateQType").attr("action",$("#updateQType").attr("action")+"&num="+c);
						updateQType.submit();
					}
			});  
		}else{
			$("#updateQType").attr("action",$("#updateQType").attr("action")+"&num="+c);
			updateQType.submit();
		}
	}
	
	function emptyWarning(){
		$("#qtypeWarning").empty();
	}
	
	function addSubQtype(){
		var count=document.getElementById("addSubQtype1").getAttribute("name");
		var num = parseInt(count) + 1;
		c=num;
		if(num<=3){
			var htm1 = "";
			$("#addSubQtype1").attr("name",num.toString());
			htm1+="<tr id='SubQtype"+num+"'><td>子题型</td><td><select name='QType"+num+"' id='QType"+num+"' class='selector form-control' style='width: 8em;'>";
			htm1+="<option value='1'>单选题</option><option value='2'>多选题</option><option value='3'>判断题</option><option value='4'>简答题</option></select>";
			htm1+="</td><td>数量</td><td><div class='input-group'><input name='amount"+num+"' type='text' class='form-control' >";
			htm1+="<span class='input-group-addon' id='basic-addon2'>个</span></div></td>";
			htm1+="<td><label id=\""+num+"\" name=\"SCRemove\" onclick=\"removeSubQtype(this.id)\" class=\"close\" aria-label=\"Close\">";
			htm1+="<span aria-hidden=\"true\">&times;</span></label></td></tr>";
			$("#addSubQtype1").parent().parent().before(htm1);
		}
		if(num==3){
			$("#addSubQtype1").attr("disabled","disabled");
			$("#addSubQtype1").empty();
			$("#addSubQtype1").append("最多只能添加三种子题型");
		}
		$("#myForm").attr("action","../AddQType?num="+num);
	}
	
	function removeSubQtype(id){	
		$("label[name='SCRemove'][id='"+id+"']").parent().parent().remove();
		$("#addSubQtype1").attr("name",($("#addSubQtype1").attr("name")-1));
		c=parseInt($("#addSubQtype1").attr("name"));
		for(var i=1;i<=c;i++){
			$("#editQtype tr:eq("+(i+1)+")").attr("id","SubQtype"+i);
			$("#SubQtype"+i+" td:eq(1)").children(".selector").attr("name","QType"+i);
			$("#SubQtype"+i+" td:eq(1)").children(".selector").attr("id","QType"+i);
			$("#SubQtype"+i+" td:eq(3)").find(".form-control").attr("name","amount"+i);
			$("#SubQtype"+i+" td:eq(4)").children("label[name='SCRemove']").attr("id",i);
		}
		$("#addSubQtype1").removeAttr("disabled");
		$("#addSubQtype1").empty();
		$("#addSubQtype1").append("点此添加一个子题型");
	}
	
	function removeSubQtype1(id){	
		$("label[name='SCRemove1'][id='"+id+"']").parent().parent().remove();
		$("#addSubQtype").attr("name",($("#addSubQtype").attr("name")-1));
		var t=parseInt($("#addSubQtype").attr("name"));
		for(var i=1;i<=t;i++){
			$("#newQtype tr:eq("+(i+1)+")").attr("id","SubQtype_"+i);
			$("#SubQtype_"+i+" td:eq(1)").children(".selector").attr("name","QType"+i);
			$("#SubQtype_"+i+" td:eq(1)").children(".selector").attr("id","QType"+i);
			$("#SubQtype_"+i+" td:eq(3)").find(".form-control").attr("name","amount"+i);
			$("#SubQtype_"+i+" td:eq(4)").children("label[name='SCRemove1']").attr("id",i);
		}
		$("#addSubQtype").removeAttr("disabled");
		$("#addSubQtype").empty();
		$("#addSubQtype").append("点此添加一个子题型");
	}