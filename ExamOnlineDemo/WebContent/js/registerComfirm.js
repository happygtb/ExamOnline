function getLength(str) {
	return str.replace(/[^\x00-xff]/g, "xx").length;
}
function findStr(str, n) {
	var tmp = 0;
	for (var i = 0; i < str.length; i++) {
		if (str.charAt(i) == n) {
			tmp++
		}
	}
	return tmp;
}
window.onload = function() {
	var ainput = document.getElementsByTagName('input');
	var oName = ainput[1];
	var pwd = ainput[2];
	var pwd2 = ainput[3];
	var aP = document.getElementsByTagName('p');
	var name_msg = aP[0];
	var pwd_msg = aP[1];
	var pwd2_msg = aP[2];
	var count = document.getElementById('count');
	var aEm = document.getElementsByTagName('em');
	var name_length = 0;
	var re = /[^\w\u4e00-\u9fa5]/g;

	var xmlhttp;

	oName.onfocus = function() {
		name_msg.innerHTML = '<i class="ati"></i>5-25个字符';
	}

	oName.onkeyup = function() {
		count.style.visibility = "visible";
		name_length = getLength(this.value);
		count.innerHTML = name_length + "个字符";
		if (name_length == 0) {
			count.style.visibility = "hidden";
		}
	}

	oName.onblur = function() {
		var re = /[^\w\u4e00-\u9fa5]/g;
		if (re.test(this.value)) {
			name_msg.innerHTML = '<i class="err"></i>含有非法字符'
		} else if (this.value == "") {
			name_msg.innerHTML = '<i class="err"></i>不能为空'
		} else if (name_length > 25) {
			name_msg.innerHTML = '<i class="err"></i>字符太长'
		} else if (name_length < 5) {
			name_msg.innerHTML = '<i class="err"></i>字符太短'
		} else {

			if (window.XMLHttpRequest) {
				xmlhttp = new XMLHttpRequest();
			} else {
				xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
			}

			xmlhttp.onreadystatechange = function() {
				if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
					name_msg.innerHTML = '<i class="ati">'
							+ xmlhttp.responseText;
				}
			}
			//取得输入的值  
			var username = oName.value;
			//第一个参数表示post请求，第二个参数表示提交给AjaxServlet，第三个参数为true，表示异步请求  
			xmlhttp.open("POST", "RegisterComfirm", true);
			xmlhttp.setRequestHeader("Content-Type",
					"application/x-www-form-urlencoded");
			xmlhttp.send("username=" + username);
		}
	}
	pwd.onfocus = function() {
		pwd_msg.style.display = "inline";
		pwd_msg.innerHTML = '<i class="ati"><i>6-16个字符'

	}
	pwd.onkeyup = function() {
		if (this.value.length > 5) {
			aEm[1].className = "active"
			pwd2.removeAttribute("disabled");
			pwd2_msg.style.display = "inline"
		} else {
			aEm[1].className = "";
			pwd2.setAttribute("disabled", "disabled");
			pwd2_msg.style.display = "none"

		}
		if (this.value.length > 10) {
			aEm[2].className = "active"
			pwd2.removeAttribute("disabled");
			pwd2_msg.style.display = "inline"
		} else {
			aEm[2].className = "";

		}

	}

	pwd.onblur = function() {
		var m = findStr(pwd.value, pwd.value[0]);
		var re_n = /[^\d]/g;
		var re_t = /[^a-zA-Z]/g;
		if (pwd.value == "") {
			pwd_msg.innerHTML = '<i class="err"></i>密码不能为空';
		} else if (m == pwd.value.length) {
			pwd_msg.innerHTML = '<i class="err"></i>不能使用相同字符';
		} else if (this.value.length < 6 || this.value.length > 16) {
			pwd_msg.innerHTML = '<i class="err"></i>长度应为6-16个字符！';
		} else if (!re_n.test(this.value)) {
			pwd_msg.innerHTML = '<i class="err"></i>不能全为数字';
		} else if (!re_t.test(this.value)) {
			pwd_msg.innerHTML = '<i class="err"></i>不能全为字母';
		} else {
			pwd_msg.innerHTML = '<i class="ati"></i>OK!';
		}

	}
	pwd2.onblur = function() {
		if (this.value != pwd.value) {
			pwd2_msg.innerHTML = '<i class="err"></i>两次输入的密码不一致！';
		} else {
			pwd2_msg.innerHTML = '<i class="ok"></i>OK！';
		}
	}

}