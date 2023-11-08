<%@ page language="java" contentType="text/html; charset=BIG5"
    pageEncoding="BIG5"%>
<%@ page import="javax.ws.rs.client.Client" %>
<%@ page import="javax.ws.rs.client.ClientBuilder" %>
<%@ page import="javax.ws.rs.client.Entity" %>
<%@ page import="javax.ws.rs.client.WebTarget" %>
<%@ page import="javax.ws.rs.core.MediaType" %>
<%@ page import="javax.ws.rs.core.Response" %>
<%@ page import="javax.json.Json" %>
<%@ page import="javax.json.JsonObject" %>
<%@ page import="java.io.StringReader" %>
<%@ page import="javax.json.JsonReader" %>
<%@ include file="./initial.jsp" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<style>
table{
border-collapse:collapse;
width:50%;
word-wrap: break-word;
table-layout:fixed;
}
</style>
<%
	
	
	final Client client = ClientBuilder.newClient();
    
	WebTarget t = client.target(url).path(appname).path("rest");

	String senddata = null;
	String Authorization = request.getParameter("Authorization");
	String encDBData = request.getParameter("encDBData");
	String data = request.getParameter("data");
	String encKey = request.getParameter("encKey");
	
	Response res = null;
	String action = request.getParameter("submit");
    if( "handshake".equals(action) ){		
		t = t.path("handshake");	
		senddata = Json.createObjectBuilder().add("wsUser","Jason").build().toString();
		res = t.request(MediaType.APPLICATION_JSON_TYPE)
    		.post(Entity.entity(senddata, MediaType.APPLICATION_JSON_TYPE));
			
	}else if("SetPassword".equals(action)){
		t =t.path("data").path("encrypt");
		senddata = Json.createObjectBuilder().add("data",data).add("encKey",encKey).build().toString();
		res = t.request(MediaType.APPLICATION_JSON_TYPE).header("Authorization","Bearer " + Authorization)
    		.post(Entity.entity(senddata, MediaType.APPLICATION_JSON_TYPE))
    		;
	}else if ("verify".equals(action)){
		t =t.path("data").path("verify");
		senddata = Json.createObjectBuilder().add("data",data).add("encKey",encKey).add("encDBData",encDBData).build().toString();
		res = t.request(MediaType.APPLICATION_JSON_TYPE).header("Authorization","Bearer " + Authorization)
    		.post(Entity.entity(senddata, MediaType.APPLICATION_JSON_TYPE));
	}else{
		t =t.path("handshake");
		data = "42D0C739F3146E85299B44DFAF4330D233761381CA9651734A8D22D0D065D48611FC9F0A597328C11ACE2DF65904DDC1B581C3D9684535D1CD17A1C874680FBF5D017852065F2993EE6E39E7CED22D6FB537E770A9922C3BE6E135054D3F45EFC24FCD81EE9957CE0B9F327AAC50BE316E7D18BCF2C621F89E87FEF3AC98748CC4E11CF20BF7CE73AB2E3ADCCB03BE6150FBDD78C0B4BD2119521ACCC3D32FB4C42FC1B941DBC684BBA31DCA4EB7441D62211D3DDCE56977E90368F75318F0CDDC4A96EA603BA4894F6DD0932A3DFEFB06F4BF347B8340DC04470C85CC70C70B618C79DA4F8670F023D630F73314F1A5C32B9AD2A0C90C89FBFD2B444CF759DFCDAE6243A2ADDF9B86220ED251E265E4C0716BA189191335B21511CFB4BAB3D63D298676DE2614FC9E867ED346B5A1B462FDA2A79179D24880EB898084C99C221652206D69449F9046F64C90146329819C40FB7F2EB109AFA871851649A19BFF17CEF9D44CDFA4B389DBB3D83C87D7C416C071E758AE9B952317B52012AF8CC9B659897E11499DC97BD0D3BCB2624F32AA99872E899E997F9133427F9AB428E6812E4B3B0195AA4147CE012923504002FA884DFFAEAE324E3261F2FB89B50498276BB3C86D344879DAC77CD2FA9936DC0EB49A1BEA6983E81F76C18E88CC0B53263BD10DA264F248063DFAE6E4224D7F";
		encKey = "3D958F116E6429D9F13D880FDAE7D3DEC39F2941402AA566F63271A5EBA2E7E5F9B52F0FEF0E84814849E944DDB4EFBCB03F45770BFA28F282D2760E75963FE85C23EB7EAF89EAF9469314D01C3A66CA8CD35EE8E38A0194E8ECE8FC95D858663EFBF6D8902034373295C9834F18CFA672D0A65949AA36E80C2735A8B11A5FF9614E2A1BA87D69659B90F2A23EDF1EA6BE20C310BC9C161DA14ECBA3E6A4D02065B201A46AC0F2AD36236AF3209F00272DAF5977BC4B03B72A101AC37D07E60378133D0EC317F780BBBAC29013E986AAA3D3646F4F0999448736CAE288C3653BFD2FDB1D75D9C00B141560919C4FC3A8644355D58DC07C178DC8037A372CA4C2";
		encDBData = "";
		Authorization = "";
		senddata = Json.createObjectBuilder().add("wsUser","Jason").build().toString();
		res = t.request(MediaType.APPLICATION_JSON_TYPE)
    		.post(Entity.entity(senddata, MediaType.APPLICATION_JSON_TYPE));
	}
	
    
    int status = res.getStatus();
	String rtndata = res.readEntity(String.class);
    res.close();
	
	if( status == 200 ){
			JsonReader reader = Json.createReader(new StringReader(rtndata));
			JsonObject json = reader.readObject();
			if( action == null || "handshake".equals(action) ){
				Authorization = json.getString("access_token");
			}else if( "SetPassword".equals(action) ){
				encDBData = json.getString("encrypted");
			}
			reader.close();
			
	
	}
    
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>E2EE Set/Verify Test</title>
</head>
<body>
<script language="JavaScript">
function clearText()
{
	document.form1.Authorization.value = "";
	document.form1.encDBData.value = "";
	document.form1.status.value = "";
	document.form1.rtnData.value = "";
}
</script>
<center>
<h1>E2EE 功能測試頁</h1>
</center>
<form name="form1" method="post" onload="clearText();">
<table  align="left">
<%--
		<tr>
			<td>
				傳送值
			</td>			
		</tr>
		<tr>
			<td>
				<textarea rows="10" cols="80" name="senddata"><%=exampleData%></textarea>
			</td>
		</tr>
--%>
		<tr>
			<td >
				E2EE使用者
			</td>			
		</tr>
		<tr>
			<td>
				<input type="text" name="wsUser" value="HiTrust" >
			</td>
		</tr>
		<tr>
			<td>
				<br>1. 按下[handshake]，取得登入WebService之授權碼
			</td>
			<td>
				<input type="submit" name="login" value="handshake">
			</td>
		</tr>
		<tr>
			<td>
				<textarea rows="8" cols="80" name="Authorization"><%=Authorization%></textarea>
			</td>
		</tr>
		<tr>
			<td >
				<br>2. 填入原始資料用session key(AES)加密之值(HEX)
			</td>
		</tr>
		<tr>
			<td>
				<textarea rows="10" cols="80" name="data"><%=data%></textarea>
			</td>
		</tr>
		<br>
		<tr>
			<td>
				<br>3. 填入用 HSM RSA公鑰加密session key(AES)之值(HEX)
			</td>
		</tr>
		<tr>
			<td>
				<textarea rows="10" cols="80" name="encKey"><%=encKey%></textarea>
			</td>
		</tr>
		<br>
		<tr>
			<td>
				<br>4. E2EE SetPassword 加密 <input type="submit" name="submit" value="SetPassword">
			</td>
		</tr>
		<tr>
			<td>
				E2EE SetPassword 加密結果(HEX)
			</td>
		</tr>
		<tr>
			<td>
				<textarea rows="10" cols="80" name="encDBData"><%=encDBData%></textarea>
			</td>
		</tr>		
		<tr>
			<td>
				<br>5. E2EE VerifyPassword <input type="submit" name="submit" value="verify">
			</td>
		</tr>		
		<tr>
			<td>
				WebService 回傳代碼 <input type="text" name="status" value="<%=status%>">
			</td>
		</tr>
		<tr>
			<td>
				
			</td>
		</tr>
		<tr>
			<td>
				E2EE WebService 回傳值
			</td>
		</tr>
		<tr>
			<td>
				<textarea rows="10" cols="80" name="rtnData"><%=rtndata%></textarea><br><br>
			</td>
		</tr>
	</table>	
<table>
<tr>
	<td>
		原始資料:<br>
		<b>476F6F676C6520E68EA8E587BAE585A8E696B0E8A88AE681AFE4B8B2E6B581E69C8DE58B99EFBC8CE695B4E59088E4BA86E69687E7ABA0E38081E5BDB1E78987E5928CE585B6E4BB96E585A7E5AEB9EFBC8CE8A9B2E68789E794A8E7A88BE5BC8FE5B087E782BA20476F6F676C6520E59CA820416E64726F696420E5928C20694F5320E5B9B3E58FB0E79A84E4B8BBE68EA8E7A88BE5BC8FE4B98BE4B880EFBC8CE5BE9EE98099E6ACBEE69C8DE58B99E79A84E5AE9AE5908DE5B0B1E883BDE79C8BE588B020476F6F676C6520E5B08DE585B6E79A84E9878DE8A696E7A88BE5BAA6EFBC8CE59BA0E782BAE5AE83E5B0B1E58FAB20476F6F676C65E38082E8A88AE681AFE6B581E58C85E68BACE4BA86E794A8E688B6E4BBA5E5BE80E79A84E6909CE5B08BE7B480E98C84E5928CE8A882E996B1E79A84E69687E7ABA0E585A7E5AEB9EFBC8CE5B08720476F6F676C6520E79A84E6909CE5B08BE69C8DE58B99E8AE8AE68890E4B880E5808BE68789E794A8E7A88BE5BC8FE59188E78FBEEFBC8CE5B0B1E5838FE6AF8FE5A4A9E4B88A2046616365626F6F6B20E5928C205477697474657220E982A3E6A8A3EFBC8CE59CA820476F6F676C6520E79A84E69C8DE58B99E4B8ADE78DB2E5BE97E6AF8FE5A4A9E69C80E696B0E79A84E8B387E8A88AE38082</b>
		<br/>
		<br/>
		SESSION KEY(AES 256, 32 bytes) :<br>
		<b>
		<input type="text" name="aes_sess" size=100 value="A8E587BAE585A8E696B0E8A88AE681AFE4B8B2E6B581E69C8DE58B99EFBC8CE6">
		</b>
		<br/>
		<br/>
		測試流程<br/>
		1.handshake 取得授權碼 <br>
		2.填入原始資料用session key(AES)加密之值 <br>
		3.填入用 HSM RSA公鑰加密session key(AES)之值 <br>
		4.執行 E2EE SetPassword 加密 <br> 
		5.執行 E2EE VerifyPassword 加密 <br>
	</td>
</tr>
</table>	
</form>
</body>
</html>