<%@ page language="java" contentType="text/html; charset=BIG5"
    pageEncoding="BIG5"%>
<%@ page import="java.io.FileInputStream" %>
<%@ page import="java.security.KeyPair" %>
<%@ page import="java.security.KeyStore" %>
<%@ page import="java.security.PrivateKey" %>
<%@ page import="java.security.cert.X509Certificate" %>
<%@ page import="java.security.interfaces.RSAPublicKey" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="java.security.Security" %>

<%!
	public static String url = "http://127.0.0.1" + ":" + "18080";
	public static String appname = "E2EEServer";
%>
