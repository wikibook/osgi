<%@ page info="Hello SpringDM on Web" %>
<%@ page import="java.util.*,java.text.*"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link rel="stylesheet" href="./css/web.css" type="text/css" />	
	<title>Hello SpringDM</title>
</head>

<body>
<h1>Hello SpringDM on Web with JSP !!</h1>

<h2>HTTP Session Info by JSP</h2>  
  <% Format formatter = new SimpleDateFormat ("yyyy-MM-dd' 'HH:mm:ss"); %>  
  <center><table>
    <tr><td>Host</td><td><%=request.getRemoteAddr()%></td></tr>    
    <tr><td>Session Id</td><td><%=session.getId()%></td></tr>
    <tr><td>Creation Date</td><td><%=formatter.format(new Date(session.getCreationTime()))%></td></tr>    
  </table></center>

<h2>Image Resource Loading in Bundle</h2>
    
  <img src="./img/helloworld.jpg"/>


</body>
</html>