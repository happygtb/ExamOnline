/**
 * 
 */

var url = location.href;
var es=/paperId=/;
es.exec(url);
var paperId=RegExp.rightContext;

$(document).ready(
	function(){
		$.getJSON("../PaperClassJson", function(jsonData) {
			var list="";
			for (var i = 0; i < jsonData.length; i++) {
				list+="<li><a href=\"PaperIndex.html?PaperClassId="+jsonData[i].PaperClassId+"\" >"+jsonData[i].PaperClassName+"</a></li>";
			}
			$(".paperTypeList").append(list);
		});
		
		$.getJSON("../TempPaperJson",{paperId:paperId},function(jsonData) {
			$("#PaperName").append(jsonData[0].PaperName);
			$("#PaperDetails").append("<blockquote><p>试卷描述：</p><p>"+jsonData[0].ExamDescription.replace(/\r\n/g,"</p><p>")+"</p><p>试卷限时："+jsonData[0].TimeLimited+"</p></blockquote>");
			var str="";
			var questionCount=0;
			for(var i=1;i<jsonData.length;i++){
				str+="<center><h3>"+jsonData[i].partName+"</h3></center><hr />";
				questionCount=jsonData[i].questionCount;
				if(jsonData[i].partDescription!=""){
					str+="<blockquote><p>"+jsonData[i].partDescription.replace(/\r\n/g,"</p><p>")+"(共"+(questionCount-1)+"题，每题"+jsonData[i].perPoint+")</p></blockquote>";
				}else{
					str+="<blockquote><p>共"+(questionCount-1)+"题，每题"+jsonData[i].perPoint+"</p></blockquote>";
				}
				str+="<div id='part"+i+"'>";
				var questionDetails="";
				var questionType="";
				for(var j=1;j<questionCount;j++){
					questionDetails=jsonData[i]["questionDetail"+j];
					questionType="["+questionDetails.split(". [")[1].split("] ")[0]+"]";
					questionDetails=questionDetails.split("] ")[1].split("[图片")[0];
					str+="<p id='part"+i+"_questionDetails"+j+"'>"+j+". <label class='type'>"+questionType+"</label>"+questionDetails.replace(/\r\n/g,"</p><p>")+"</p>";
					if(jsonData[i]["questionDetail"+j].indexOf("[图片")!=-1){
						var picNum=jsonData[i]["questionDetail"+j].split("[图片")[1].split("]")[0];
						var pic=jsonData[0].filename+"_"+picNum+".jpg";
						str+="<p><img src='../file/TempPaper/"+pic+"' /></p>"
					}
					str+="<div class='options'>";
					var answer=jsonData[i]["answer"+j];
					if(questionType.indexOf("单选")!=-1||questionType.indexOf("单项")!=-1){
						var optionNum=1;
						var num=letterToNumber(answer);
						while(jsonData[i]["option"+j+optionNum]!=null){
							if(optionNum==num){
								str+="<p><input type='radio' name='part"+i+"_question"+j+"' checked='checked'>"+jsonData[i]["option"+j+optionNum]+"</p>";
							}else{
								str+="<p><input type='radio' name='part"+i+"_question"+j+"'>"+jsonData[i]["option"+j+optionNum]+"</p>";
							}
							optionNum++;
						}
					}else if(questionType.indexOf("多选")!=-1||questionType.indexOf("多项")!=-1||questionType.indexOf("不定项")!=-1){
						var optionNum=1;
						while(jsonData[i]["option"+j+optionNum]!=null){
							var optionLetter=jsonData[i]["option"+j+optionNum].substring(0,1);
							if(answer.indexOf(optionLetter)!=-1){
								str+="<p><input type='checkbox' name='part"+i+"_question"+j+"' checked='checked'>"+jsonData[i]["option"+j+optionNum]+"</p>";
							}else{
								str+="<p><input type='checkbox' name='part"+i+"_question"+j+"'>"+jsonData[i]["option"+j+optionNum]+"</p>";
							}
							optionNum++;
						}
					}else if(questionType.indexOf("判断")!=-1){
						if(answer.indexOf("正确")!=-1||answer.indexOf("对")!=-1||answer.indexOf("是")!=-1||answer.indexOf("T")!=-1||answer.indexOf("√")!=-1){
							str+="<p><input type='radio' name='part"+i+"_question"+j+"' checked='checked'>正确</p><p><input type='radio' name='part"+i+"_question"+j+"'>错误</p>";
						}else{
							str+="<p><input type='radio' name='part"+i+"_question"+j+"'>正确</p><p><input type='radio' name='part"+i+"_question"+j+"' checked='checked'>错误</p>";
						}
					}else{
						str+="<textarea class='form-control' id='ShortAnswer' rows='5' name='part"+i+"_question"+j+"' readonly>"+answer+"</textarea>";
					}
					str+="</div><hr />";
				}
				str+="</div>";
			}
			$("#PaperBody").append(str);
			$("input").each(function(){
				$(this).attr("disabled","disabled");
			});
			$("input:eq(0)").removeAttr("disabled");
			$("textarea").each(function(){
				$(this).parent().css("margin-left","0")
			});
			$('input').iCheck({
			    checkboxClass: 'icheckbox_square-blue',
			    radioClass: 'iradio_square-blue',
			    increaseArea: '20%'
			});
			$("p").each(function(){
				var length=$(this).html().length;
				if(length<2){
					$(this).css("display","none");
				}
			});
		});
	});

function letterToNumber(letter){
	var num=0;
	if(letter=="A"||letter=="a"){
		num=1;
	}else if(letter=="B"||letter=="b"){
		num=2;
	}else if(letter=="C"||letter=="c"){
		num=3;
	}else if(letter=="D"||letter=="d"){
		num=4;
	}else if(letter=="E"||letter=="e"){
		num=5;
	}else if(letter=="F"||letter=="f"){
		num=6;
	}else if(letter=="G"||letter=="g"){
		num=7;
	}else if(letter=="H"||letter=="h"){
		num=8;
	}else if(letter=="I"||letter=="i"){
		num=9;
	}else if(letter=="J"||letter=="j"){
		num=10;
	}else if(letter=="K"||letter=="k"){
		num=11;
	}else if(letter=="L"||letter=="l"){
		num=12;
	}else if(letter=="M"||letter=="m"){
		num=13;
	}else if(letter=="N"||letter=="n"){
		num=14;
	}else if(letter=="O"||letter=="o"){
		num=15;
	}else if(letter=="P"||letter=="p"){
		num=16;
	}else if(letter=="Q"||letter=="q"){
		num=17;
	}else if(letter=="R"||letter=="r"){
		num=18;
	}else if(letter=="S"||letter=="s"){
		num=19;
	}else if(letter=="T"||letter=="t"){
		num=20;
	}else if(letter=="U"||letter=="u"){
		num=21;
	}else if(letter=="V"||letter=="v"){
		num=22;
	}else if(letter=="W"||letter=="w"){
		num=23;
	}else if(letter=="X"||letter=="x"){
		num=24;
	}else if(letter=="Y"||letter=="y"){
		num=25;
	}else{
		num=26;
	}
	return num;
}