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
String action = request.getParameter("submit");
final Client client = ClientBuilder.newClient();
    WebTarget t = client.target(url).path(appname)
    		.path("rest");

	String senddata = null;
	String Authorization = request.getParameter("Authorization");
	String encDBData = request.getParameter("encDBData");
	String data = request.getParameter("data");
	String encKey = request.getParameter("encKey");
	Response res = null;
    if( "handshake".equals(action) ){		
		t = t.path("handshake");	
		senddata = Json.createObjectBuilder().add("wsUser","Jason").build().toString();
		res = t.request(MediaType.APPLICATION_JSON_TYPE)
    		.post(Entity.entity(senddata, MediaType.APPLICATION_JSON_TYPE))
    		;
	}else if("encrypt".equals(action)){
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
		data = "0D297DAA48AB5765E47B5F2E346B8B05536225CDB8F37986D208CFDA611A0DDA280D85AD42F586A9D2554DD4E428610972D350021710F14F091D00676548838801121A454D9B6498172B606CF01EAF121B8CCF207B3498C0D1D7F084EF0689BE5C89F492013F974D31C5B786370F974725D0DC0FF87F8393A19D2E7264498AB3E8B35266AC53FA91A78C20853248781AAFDD90730E1655E29FBFD880FA7CCC042626EE0E3AE5F36974AE3B4D56B5D59B0861ADAA2CED39400519466EDB17F8BCFEFB283D857B746AD165B7A095989B911B39195D7D242EFA4140803184D21637429742541FB982B4C32819ADA1F8733BA83D66CBBBFD0F06A47091C5A75BDB5033C3572EB67CDDF879CB77F363793FFD517FAFF158EF4CE67D76C236024B56949D2F2AF1FF2ABFE5651506CC68F3112E82B278E7A6265AE0F0480D47AD69A1C9D358E5699528776A8FE75A004F3C96626534A6237537D93833D0023E4E6C9968748FCA980B0BB1A04C655EEB24C974E0FEE473BCD718CA0E39AC931DB92DA83F176FB5FEF865EDCAA2504FA2C7E5FB6FFD55F9726D1E466D7D4CB4BD5BD2BA9F5C08C7B6884BBD1E60283357A9A0E03851C8ABB3DE523F421D2E36EDA3EFD5FECD9A6BEE7F9755C1EC0CD34B5DBF431468EBD44CA07B07054907D2E5123351659D69D0E952FF94DBCF771DB25FBFF0F3";
		encKey = "3BDF1B14726B600FD76B63D6A8D3D17288BB846AD7771B44F741601A5F7423F875B393D46D2428712C06BBB9EE734D2F9B5A7AF328AEFEF8BABD7FBC8189185D296670F234EAA7E0C18BAE180975F522CBE8B0615DD5F44F2390040EC4DFBE7E99EA513C576D6DFD3E990A57A1C334263A83810583B658E3131F7C7ACC2280A691C8D92176832F9BB0D34998BDD17B9BA9DDE20890FD6E563E946EF4E3A31144921451B9870514062C13F5DC715D93624A7EDFC694CFA6889B3A0885182F4F4DEBD457CEAD971FB18457BC44172DA06752EDD576B786F53CB82E251290D5DBA47F3FE0D8AD4F9D27EEF3CF121FF176A9A0D3DBA46D3F0ADC7E6F270388F54BE2";
		encDBData = "";
		Authorization = "";
		senddata = Json.createObjectBuilder().add("wsUser","Jason").build().toString();
		res = t.request(MediaType.APPLICATION_JSON_TYPE)
    		.post(Entity.entity(senddata, MediaType.APPLICATION_JSON_TYPE))
    		;
		
	}
	
    
    int status = res.getStatus();
	String rtndata = res.readEntity(String.class);
    res.close();
	
	if( status == 200 ){
			JsonReader reader = Json.createReader(new StringReader(rtndata));
			JsonObject json = reader.readObject();
			if( action == null || "handshake".equals(action) ){
				Authorization = json.getString("access_token");
			}else if( "encrypt".equals(action) ){
				encDBData = json.getString("encrypted");
			}
			reader.close();
			
	
	}
    
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Login</title>
</head>
<body>
<center>
<h1>E2EE 功能測試頁</h1>
</center>
<form name="form1" method="post">
<table  align="left">
<%--
		<tr>
			<td>
				傳送值
			</td>			
		</tr>
		<tr>
			<td>
				<textarea rows="10" cols="50" name="senddata"><%=exampleData%></textarea>
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
				<input type="submit" name="login" value="handshake">
			</td>
		</tr>
		<tr>
			<td>
				授權碼
			</td>
		</tr>
		<tr>
			<td>
				<input type="text" name="Authorization" value="<%=Authorization%>" size="50">
			</td>
		</tr>
		<tr>
			<td >
				session key(AES)加密資料<HEX>
			</td>
		</tr>
		<tr>
			<td>
				<input type="text" name="data" value="<%=data%>" size="50">
			</td>
		</tr>
		<tr>
			<td>
				HSM's RSA 加密 session key(AES)<HEX>
			</td>
		</tr>
		<tr>
			<td>
				<input type="text" name="encKey" value="<%=encKey%>" size="50">
			</td>
		</tr>
		<tr>
			<td>
				<input type="submit" name="submit" value="encrypt">
			</td>
		</tr>
		<tr>
			<td>
				HSM's AES 加密資料<HEX>
			</td>
		</tr>
		<tr>
			<td>
				<input type="text" name="encDBData" value="<%=encDBData%>" size="50">
			</td>
		</tr>		
		<tr>
			<td><input type="submit" name="submit" value="verify"></td>
		</tr>		
		<tr>
			<td>
				回傳代碼
			</td>
		</tr>
		<tr>
			<td>
				<%=status%>
			</td>
		</tr>
		<tr>
			<td>
				回傳值
			</td>
		</tr>
		<tr>
			<td>
				<textarea rows="10" cols="50"><%=rtndata%></textarea>
			</td>
		</tr>
	</table>	
<table>
<tr>
	<td>
		原始資料:				<b>476F6F676C6520E68EA8E587BAE585A8E696B0E8A88AE681AFE4B8B2E6B581E69C8DE58B99EFBC8CE695B4E59088E4BA86E69687E7ABA0E38081E5BDB1E78987E5928CE585B6E4BB96E585A7E5AEB9EFBC8CE8A9B2E68789E794A8E7A88BE5BC8FE5B087E782BA20476F6F676C6520E59CA820416E64726F696420E5928C20694F5320E5B9B3E58FB0E79A84E4B8BBE68EA8E7A88BE5BC8FE4B98BE4B880EFBC8CE5BE9EE98099E6ACBEE69C8DE58B99E79A84E5AE9AE5908DE5B0B1E883BDE79C8BE588B020476F6F676C6520E5B08DE585B6E79A84E9878DE8A696E7A88BE5BAA6EFBC8CE59BA0E782BAE5AE83E5B0B1E58FAB20476F6F676C65E38082E8A88AE681AFE6B581E58C85E68BACE4BA86E794A8E688B6E4BBA5E5BE80E79A84E6909CE5B08BE7B480E98C84E5928CE8A882E996B1E79A84E69687E7ABA0E585A7E5AEB9EFBC8CE5B08720476F6F676C6520E79A84E6909CE5B08BE69C8DE58B99E8AE8AE68890E4B880E5808BE68789E794A8E7A88BE5BC8FE59188E78FBEEFBC8CE5B0B1E5838FE6AF8FE5A4A9E4B88A2046616365626F6F6B20E5928C205477697474657220E982A3E6A8A3EFBC8CE59CA820476F6F676C6520E79A84E69C8DE58B99E4B8ADE78DB2E5BE97E6AF8FE5A4A9E69C80E696B0E79A84E8B387E8A88AE38082</b>
		<br/>
		<br/>
		SESSION KEY(AES):
		<b>
		16CF6F4ECEFD4F99BF1CD34CB1FAA7DD99AB50C604784761318A7D9EB3F4E0EC
		</b>
		<br/>
		<br/>
		測試流程<br/>
		1.handshake 取得授權碼 2.encrypt or verify
	</td>
</tr>
</table>	
</form>
</body>
</html>