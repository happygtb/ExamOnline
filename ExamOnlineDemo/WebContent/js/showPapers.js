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
var url = location.href;
var es=/paperId=/;
es.exec(url);
var paperId=RegExp.rightContext;
$(document).ready(
	function(){
		$.getJSON("../showAutopaper",{paperId:paperId},function(jsonData) {
			$("#PaperName").append(jsonData[0].PaperName);
			$("#PaperDetails").append("<blockquote><p>试卷描述：</p><p>"+jsonData[0].ExamDescription.replace(/\r\n/g,"</p><p>")+"</p><p>试卷总分："+jsonData[0].TotalPoint+"</p><p>试卷限时："+jsonData[0].TotalTime+"</p></blockquote>");
			var str="";
			var questionCount=0;
			
			for(var i=1;i<jsonData.length;i++){
				var chinese = convertToChinese(i);
				questionCount=jsonData[i].CountQuestion;
				str+="<center><h3>第"+chinese+"部分&emsp;"+jsonData[i].PartName+"(共"+(questionCount-1)+"题，满分"+jsonData[i].PartPoint+"分)</h3></center><hr />";
				str+="<blockquote><p>"+jsonData[i].Description.replace(/\r\n/g,"</p><p>")+"(共"+(questionCount-1)+"题，满分"+jsonData[i].PartPoint+"分)</p></blockquote>";			
				str+="<div id='part"+i+"'>";
				var questionDetails="";
				var questionType="";
				var questionPoint="";
				for(var j=1;j< questionCount;j++){		
					questionDetails=jsonData[i]["questionDetail"+j];
					questionType=jsonData[i]["TypeName"+j];
					questionPoint=jsonData[i]["perPoint"+j];
					str+="<p id='part"+i+"_questionDetails"+j+"'>"+j+". <label class='type'>["+questionType+"]</label>"+questionDetails.replace(/\r\n/g,"</p><p>")+"("+questionPoint+"分)</p>";
					str+="<div class='options'>";
					if(questionType.indexOf("单选")!=-1){
						var optionNum=1;

						while(jsonData[i]["option"+j+optionNum]!=null){
							
							if(jsonData[i]["isCorrect"+j+optionNum]=="True"){
								str+="<p><input type='radio' name='part"+i+"_question"+j+"' checked='checked'>"+jsonData[i]["option"+j+optionNum]+"</p>";
							}else{
								str+="<p><input type='radio' name='part"+i+"_question"+j+"'>"+jsonData[i]["option"+j+optionNum]+"</p>";
							}
							optionNum++;							
						}
					}
					else if(questionType.indexOf("多选")!=-1){
						var optionNum=1;
						while(jsonData[i]["option"+j+optionNum]!=null){
							if(jsonData[i]["isCorrect"+j+optionNum]=="True"){
								str+="<p><input type='checkbox' name='part"+i+"_question"+j+"' checked='checked'>"+jsonData[i]["option"+j+optionNum]+"</p>";
							}else{
								str+="<p><input type='checkbox' name='part"+i+"_question"+j+"'>"+jsonData[i]["option"+j+optionNum]+"</p>";
							}
							optionNum++;
						}
					}else if(questionType.indexOf("判断")!=-1){
						var optionNum=1;
						if(jsonData[i]["isCorrect"+j+optionNum]=="True"){
							str+="<p><input type='radio' name='part"+i+"_question"+j+"' checked='checked'>正确</p><p><input type='radio' name='part"+i+"_question"+j+"'>错误</p>";
						}else{
							str+="<p><input type='radio' name='part"+i+"_question"+j+"'>正确</p><p><input type='radio' name='part"+i+"_question"+j+"' checked='checked'>错误</p>";
						}
					}else{
						var optionNum=1;
						str+="<textarea class='form-control' id='ShortAnswer' rows='5' name='part"+i+"_question"+j+"' readonly>"+jsonData[i]["option"+j+"1"]+"</textarea>";
					}
					
					str+="</div><hr/>";				
				}//第二个for
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
function print(){
	$(document).ready(function(){
		var method="Auto";
		$.getJSON("../CreateWordDoc",{method:method,paperId:paperId},function(jsonData) {
			/*$("#PrintPaperName").append(jsonData.paperName);*/
			filename=jsonData.filename;
			$("#printer").attr("href","../file/PrintPapers/"+jsonData.filename);
		});
	});
}



  
