<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
	String ctxPath = request.getContextPath();
    //     /begin
%>    
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Ajax 연습</title>

<style type="text/css">
	table, th, td {
		border: solid 1px gray;
		border-collapse: collapse;
	}
</style>

<script type="text/javascript" src="<%= ctxPath%>/resources/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		
		func_ajax_select();
		
		$("button#btnOK").click(function(){
						
			const no_val = $("input:text[name='no']").val();
			const name_val = $("input:text[name='name']").val();
			
			const reg_exp = /^[0-9]+$/;
			if( !reg_exp.test(no_val) ) {
				alert("숫자로만 입력하세요");
				return;
			}
			
			if(name_val.trim().length == 0) {
				alert("성명을 입력하세요");
				return;
			}
			
			const param = {"no" : no_val
				          ,"name" : name_val};
			
			$.ajax({
				url:"<%= ctxPath%>/test/ajax_insert_1.action", 
				type:"post",
				data:param,
				dataType:"json",
				success:function(json){
				//	console.log(JSON.stringify(json));
					// {"n":1}
					
					if(json.n == 1){
						func_ajax_select();
						$("input:text[name='no']").val("");
						$("input:text[name='name']").val("");
					}
					
				},
				error: function(request, status, error){
				   alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
				}
			});
			
		});
		
	});// end of $(document).ready(function(){})---------
	
	
	function func_ajax_select(){
		
		$.ajax({
			url:"<%= ctxPath%>/test/ajax_select_1.action",
			type:"get",
			dataType:"json",
			success:function(json){
			//	console.log(JSON.stringify(json));
			/*    
			   [{"no":"5006","name":"오천육","writeday":"2025-01-16 14:42:12"}
			   ,{"no":"5005","name":"오천오","writeday":"2025-01-16 14:30:42"}
			   ,{"no":"5004","name":"오천사","writeday":"2025-01-15 00:00:00"}
			   ,{"no":"5003","name":"오천삼","writeday":"2025-01-16 12:19:29"}
			   ,{"no":"5002","name":"오천이","writeday":"2025-01-16 12:09:07"}
			   ,{"no":"5001","name":"오천일","writeday":"2025-01-16 11:46:45"}
			   ,{"no":"103","name":"변우석","writeday":"2025-01-15 17:20:54"}
			   ,{"no":"102","name":"김태희","writeday":"2025-01-15 17:20:54"}
			   ,{"no":"101","name":"이순신","writeday":"2025-01-15 16:49:51"}]
			*/ 
			   let v_html = `<table>
			                    <tr>
			                       <th>번호</th>
			                       <th>입력번호</th>
			                       <th>성명</th>
			                       <th>작성일자</th>
			                    </tr>`;
			                    
			    $.each(json, function(index, item){
			    	v_html += `<tr>
			    	              <td>\${index+1}</td>
			    	              <td>\${item.no}</td>
			    	              <td>\${item.name}</td>
			    	              <td>\${item.writeday}</td>
			    	           </tr>`;
			    });
		
		        v_html += `</table>`;
		        
		        $("div#view").html(v_html);
			},
			error: function(request, status, error){
			   alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			}
		});
		
	}// end of function func_ajax_select(){}--------------
	
</script>

</head>
<body>

    <h2>Ajax 연습</h2>
    <p>
        안녕하세요?<br>
        여기는 /test/ajax_form_1.action 페이지 입니다.
    </p>
	 
		번호 : <input type="text" name="no" /><br>
		성명 : <input type="text" name="name" /><br>
	    <button type="button" id="btnOK">확인</button>
	    <button type="reset" id="btnCancel">취소</button>
	    <br><br> 
	
	<div id="view"></div>

</body>
</html>