/**************************************************
* @(#)Utility.java v1.00, 2002/08/12
*
* Copyright (c) 2002 Linkway Technology Inc.
* All Rights Reserved.
*
* Modify History:
*  v1.00, 2002/08/12, Jackie - first release
* 1. 2002/09/05 Phoebe Wang, getCRL() - ISecurity Exception modify
*
**************************************************/
package com.hitrust.e2ee.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.TimeZone;
import java.util.Vector;

/**
* ISecurity Utilities
*
* @version	1.00 2002/08/12
* @author	Jackie Yang
*/
public class Utility
{
	/**
	* Read file content into byte[]
	* @param fileName - file name to be read
	*/
	public static byte[] readFile(String fileName) throws IOException
	{
		// Read file content
		FileInputStream in = null;
		try
		{
			in = new FileInputStream(fileName);
			byte[] keyData = new byte[in.available()];
			in.read(keyData);
			return keyData;
		}
		finally
		{
			if (in != null)
				in.close();
		}
	}

	/**
	* Write data byte[] into file
	* @param fileName - file name to be read
	*        data     -data byte[] for writ to
	*/
	public static void writeFile(String fileName, byte[] data) throws IOException
	{
		// Write byte[] to the file.
		FileOutputStream out = null;
		try
		{
			out = new FileOutputStream(fileName);
			out.write(data);
			out.close();
		}
		finally
		{
			if (out != null)
				out.close();
		}
	}

	
	public static String bin2HexStr(byte bin)
	{
		byte[] a = new byte[]{bin};
		return new String(bin2Hex(a), StandardCharsets.UTF_8);
	}
	
	/**
	* Binary byte[] to Heximal byte[]
	* @param bin - data in binary for convert
	*/
	public static byte[] bin2Hex(byte[] bin)
	{
		// Allocate byte[] for return
		byte[] hex = new byte[2 * bin.length];

		// Convert one binary byte to two heximal bytes
		for (int i = 0, j = 0; i < bin.length; i++, j += 2)
		{
			int iByte = bin[i];
			if (iByte < 0)
				iByte += 256;
			// First byte
			int i4Bit = iByte >> 4;
			if (i4Bit < 10)
				hex[j] = (byte) ('0' + i4Bit);
			else
				hex[j] = (byte) ('A' + i4Bit - 10);
			// Second byte
			i4Bit = iByte & 0x000F;
			if (i4Bit < 10)
				hex[j + 1] = (byte) ('0' + i4Bit);
			else
				hex[j + 1] = (byte) ('A' + i4Bit - 10);
		}

		// Return the heximal byte[]
		return hex;
	}

	/**
	* Heximal byte[] to binary byte[]
	* @param hex - data in heximal for convert
	*/
	public static byte[] hex2Bin(byte[] hex)
	{
		// Allocate byte[] for return
		byte[] bin = new byte[hex.length / 2];

		// Convert two heximal bytes to one binary byte
		for (int i = 0, j = 0; i < bin.length; i++, j += 2)
		{
			// First byte
			int iL = hex[j] - '0';
			if (iL > 9)
				iL -= 7;
			iL <<= 4;
			// Second byte
			int iR = hex[j + 1] - '0';
			if (iR > 9)
				iR -= 7;
			bin[i] = (byte) (iL | iR);
		}

		// Return the binary byte[]
		return bin;
	}

	/**
	* Transfer String to SQL string of checking ['] char
	* @param org - the string for convert
	*/
	static public String toSQLString(String org)
	{
		// Contains no ch return itself
		char ch = '\'';
		if (org.indexOf(ch) == -1)
			return org;

		// Convert to char array for cehcking
		char[] chs = org.toCharArray();
		int cnt = 0;
		for (int i = 0; i < chs.length; i++)
		{
			if (chs[i] == ch)
				cnt++;
		}
		char[] chsNew = new char[chs.length + cnt];

		// Checking each ch, adding lead before it
		char lead = '\'';
		int idx = 0;
		for (int i = 0; i < chs.length; i++)
		{
			if (chs[i] == ch)
				chsNew[idx++] = lead;
			chsNew[idx++] = chs[i];
		}
		return new String(chsNew);
	}

	/************************************************************/

	/**
	 * formatDate
	 * @param     date	a Date Object
	 * @return    String a date format is "yyyyMMdd"
	 * @exception ISecurityException	if construct this class
	 */
	public static String getFormatDate(Date date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(date);
	}

	/************************************************************/
	/**
	 * SerialNumber2Hx
	 * @param     date	a Date Object
	 * @return    String a date format is "yyyyMMdd"
	 * @exception ISecurityException	if construct this class
	 */
	public static String getSerlNmbr2Hx(byte[] data)
	{
		return new String(bin2Hex(data), StandardCharsets.UTF_8);
	}

    public static String bin2HexStr(byte[] bin) {
		return new String(bin2Hex(bin), StandardCharsets.UTF_8);
    }
    
    public static byte[] hexStr2Bin(String hex) {
    	char[] hexary = hex.toCharArray();
		int length = hexary.length / 2;
		byte[] rawData = new byte[length];
		
		for (int i = 0; i < length; i++) {
		int high = Character.digit(hexary[i * 2], 16);
		int low = Character.digit(hexary[i * 2 + 1], 16);
		int value = (high << 4) | low;
		if (value > 127)
			value -= 256;
			rawData [i] = (byte) value;
		}
		return rawData ;
        //return hex2Bin(hex.getBytes());
    }

	public static String getISecurityDN(HashMap<String, String> hm){
		StringBuffer sb = new StringBuffer();
		String dnC = (String)hm.get("C");
		String dnO = (String)hm.get("O");
		String dnOU1 = (String)hm.get("OU1");
		String dnOU2 = (String)hm.get("OU2");
		String dnOU3 = (String)hm.get("OU3");
		String dnOU4 = (String)hm.get("OU4");
		String dnCN = (String)hm.get("CN");

		// c,o,ou,cn
		if (dnC!=null)
			sb.append("C="+dnC);
		if (dnO!=null)
			sb.append((sb.length() > 0 ? ",": "") + "O="+dnO);
		if (dnOU1!=null)
			sb.append((sb.length() > 0 ? ",": "") + "OU="+dnOU1);
		if(dnOU2!=null)
			sb.append((sb.length() > 0 ? ",": "") + "OU="+dnOU2);
		if(dnOU3!=null)
			sb.append((sb.length() > 0 ? ",": "") + "OU="+dnOU3);
		if(dnOU4!=null)
			sb.append((sb.length() > 0 ? ",": "") + "OU="+dnOU4);
		if (dnCN!=null)
			sb.append((sb.length() > 0 ? ",": "") + "CN="+dnCN);
		
		return sb.toString();
	}
	
	/**
	 * 
	 * @param src
	 * @return
	 */
	public static String getISecurityDN(String src){
		return getISecurityDN(parsingDN(src));
	}
	

    /**
     * If Java 1.4 is unavailable, the following technique may be used.
     *
     * @param aInput is the original String which may contain substring aOldPattern.
     * @param aOldPattern is the substring which is to be replaced
     * @param aNewPattern is the replacement for aOldPattern
     * @return replaced result string
     */
    public static String replaceString(
            final String aInput,
            final String aOldPattern,
            final String aNewPattern){

        final StringBuffer result = new StringBuffer();
        //startIdx and idxOld delimit various chunks of aInput; these
        //chunks always end where aOldPattern begins
        int startIdx = 0;
        int idxOld = 0;
        while ((idxOld = aInput.indexOf(aOldPattern, startIdx)) >= 0) {
            //grab a part of aInput which does not include aOldPattern
            result.append( aInput.substring(startIdx, idxOld) );
            //add aNewPattern to take place of aOldPattern
            result.append( aNewPattern );

            //reset the startIdx to just after the current match, to see
            //if there are any further matches
            startIdx = idxOld + aOldPattern.length();
        }
        //the final chunk will go to the end of aInput
        result.append( aInput.substring(startIdx) );
        return result.toString();
}

    private static final String SKIP_KW_SLASHDOT = "$HT_SKIP_SLASHDOT$";
	public static HashMap<String, String> parsingDN(String src){
		HashMap<String, String> rtn = new HashMap<String, String>();
		Vector<String> ou = new Vector<String>();
		boolean rv = false;

		src = replaceString(src, "\\,", SKIP_KW_SLASHDOT);
		
//		System.out.println("[[[[[[[["+src+"]]]]]]]]");
//		StringTokenizer strToken = new StringTokenizer(src,",");
		
//		String[] arrSrc = src.split("[,]");
		String[] arrSrc = splitString(src,",");
		for(int i=0;i<arrSrc.length;i++){
			if(arrSrc[i]==null||arrSrc[i].length()<=0)
				continue;
//			System.out.println(">>>>>>>>>>"+arrSrc[i]+"<<<<<<<<<<");
//			String[] item = arrSrc[i].trim().split("[=]");
			String[] item = splitString(arrSrc[i].trim(),"=");
			
			String key = item[0].trim();
			String value = item[1].trim();
			value = replaceString(value, SKIP_KW_SLASHDOT, ",");
			
			if(key.equalsIgnoreCase("CN")&&(!rtn.containsKey("C")))
				rv = true;
			
			if(key.equalsIgnoreCase("OU")){
				ou.add(value);
				continue;
			}
			rtn.put(key.toUpperCase(), value);
		}
		if(ou.size()>0){
			if(rv){
				for (int i = 1; i<=ou.size() ; i++) {
					String key = "OU" + Integer.toString(ou.size()-i+1);
					rtn.put(key, ou.get(i-1));
				}
			}else{
				for (int i = 1; i<=ou.size() ; i++) {
					String key = "OU" + Integer.toString(i);
					rtn.put(key, ou.get(i-1));
				}
			}
		}
		return rtn;
	}
	

    public static String[] splitString(String tar, String rex) {
		int idx = 0;
		if(tar==null||rex==null)
			throw new NullPointerException();
		
		if(tar.indexOf(rex)==-1)
			return new String[]{tar};
		
		if(rex.length()<=0)
			return new String[]{tar};
		
		ArrayList<String> al = new ArrayList<String>();
		
		
		while(idx<tar.length()){
			int fidx = tar.indexOf(rex, idx);
//System.out.println(idx+" "+fidx);
			
			if(fidx==-1){
				al.add(tar.substring(idx));
				break;
			}
			
//			System.out.println(">"+tar.substring(idx, fidx));
//			if((fidx-idx)==0)
//				fidx++;
			
			al.add(tar.substring(idx, fidx));
			
			idx = fidx+rex.length();
		}
		return (String[])al.toArray(new String[]{});
    }

    /**
     * Get relative Date
     * @param date a string, format is "yyyyMMdd"
     * @param n relative number
     * @param flag relative type("Y"-year, "M"-month, "D"-day)
     * @return String format is "yyyyMMdd"
     */
    public static String relativeDate(String date, int n, char flag) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        //get year, month, day
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(4, 6)) - 1;
        int day = Integer.parseInt(date.substring(6, 8));
        cal.set(year, month, day); //set data to Calendar

        switch (flag) {
            case 'Y':
                cal.add(Calendar.YEAR, n);
                break;
            case 'M':
                cal.add(Calendar.MONTH, n);
                break;
            case 'D':
                cal.add(Calendar.DATE, n);
                break;
            default:
            	// do nothing
            	break;
        }

        //get relative data and formate to "yyyyMMdd"
        return sdf.format(cal.getTime());
    }
    
   

    public static String getSHA1HEX(byte[] tar) throws NoSuchAlgorithmException{
		MessageDigest md = MessageDigest.getInstance("SHA1");
		return Utility.bin2HexStr(md.digest(tar));
    }
    
    /**
     */
    public static byte[] getByteArray(BigInteger bi){
    	byte[] tmp = bi.toByteArray();
    	byte[] rtn = null;
    	if(tmp[0]==0x0&&tmp.length>1){
    		rtn = new byte[tmp.length-1];
    		System.arraycopy(tmp, 1, rtn, 0, rtn.length);
    	}else
    		rtn = tmp;
    	
    	return rtn;
    }
    
	public static final String StrDateFormat = "yyyyMMddHHmmss";
	public static final String StrDayFormat = "yyyyMMdd";
	public static final String TxLogDateFormat = "yyyy/MM/dd HH:mm:ss.SSS";
	
	public static HashMap<String, String> getEnv(String propertiesLocate) throws IOException{
		Properties props = new Properties();
		InputStream inStream = null;
		try
		{
			inStream = Object.class.getResourceAsStream(propertiesLocate);
			props.load(inStream);
		}
		finally
		{
			if (inStream != null)
				inStream.close();
		}
		
		HashMap<String, String> hm = new HashMap<String, String>();
		Enumeration<?> enu = props.keys();
		while(enu.hasMoreElements()){
			String key = (String)enu.nextElement();
			String val = props.getProperty(key);
			hm.put(key, val);
		}
		
		return hm;
	}
	
	public static ArrayList<String> stringSplit(String input, String tag)
    {
    	int index = -1;
        int ibegin = 0;
        ArrayList<String> data = new ArrayList<String>();
        while(true)
        {
            index = input.indexOf(tag , ibegin);
            if (index > -1)
            {
            	data.add(input.substring(ibegin, index));
            	ibegin = index + 1;
            }
            else
            {
            	data.add(input.substring(ibegin));
            	break;
            }
        }
        return data;
    }
	
	public static byte[] hash(String hashMethod, byte[] sourceData){
		// TODO Auto-generated method stub
		byte[] digestValue = null;
		
		try {
			//
			MessageDigest messageDigest = MessageDigest.getInstance(hashMethod);
			messageDigest.update(sourceData);
			digestValue = messageDigest.digest();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return digestValue;
	}
	
	 public static Date getQueryDate(String date) {
        Calendar cal = Calendar.getInstance();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        //get year, month, day
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(4, 6)) - 1;
        int day = Integer.parseInt(date.substring(6, 8));
        cal.set(year, month, day, 0, 0, 0); //set data to Calendar

        //get relative data and formate to "yyyyMMdd"
        return cal.getTime();
    }
	private static String _timezone = "Asia/Taipei";
		
	public static void setTimezone(String timezone)
	{
		_timezone = timezone;
	}
	
	public static String getTimezone()
	{
		return _timezone;
	}
	
	public static java.util.Date getDateNow()
	{
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(_timezone));
		return cal.getTime();
	}

	public static java.sql.Timestamp getDateSqlNow()
	{
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(_timezone));
		return new java.sql.Timestamp(cal.getTimeInMillis());
	}

	
	public static Calendar getCalendarNow()
	{
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(_timezone));
		return cal;
	}
	
	public static String getNowStr()
	{
		Date date = getDateNow();
        DateFormat dateFormat = 
            new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return dateFormat.format(date);
	}
	
	public static String getDateStr(Date date)
	{
        DateFormat dateFormat = 
            new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return dateFormat.format(date);
	}
	
	public static Date getStr2Date(String src) throws ParseException
	{
		java.text.SimpleDateFormat simple = new java.text.SimpleDateFormat();
		simple.applyPattern("yyyy/MM/dd HH:mm:ss");
		return simple.parse(src);
	}
	
	public static java.util.Date getDate(Integer year, Integer month, Integer day, Integer hour, Integer minute, Integer second)
	{
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(_timezone));
		if (year != null)
			cal.add(Calendar.YEAR, year);
		if (month != null)
			cal.add(Calendar.MONTH, month);
		if (day != null)
			cal.add(Calendar.DATE, day);
		if (hour != null)
			cal.add(Calendar.HOUR, hour);
		if (minute != null)
			cal.add(Calendar.MINUTE, minute);
		if (second != null)
			cal.add(Calendar.SECOND, second);
		Date date = new Date();
		date.setTime(cal.getTimeInMillis());
		return date;
	}
	
	public static byte[] trimPadding(byte[] src)
	{
		byte lastbyte = src[src.length - 1];
		byte[] rtn = null;
		int index = src.length - 1;
		if (lastbyte >= 0x01 && lastbyte <= 0x10)
		{
			for (int i = src.length - 1 ; i > 0; i--)
			{
				if (src[i] == lastbyte)
					index -=1;
				else
					break;
			}
			index++;
			rtn = new byte[index];
			for (int i = 0 ; i < index; i++)
			{
				rtn[i] = src[i];
			}
		}
		else
			return src;
		return rtn;
	}
	
	public static boolean isBlank(String s) {
		return s == null || s.trim().equals("") || s.trim().equals("ALL");
	}
	public static boolean isBlank(Integer i) {
		return i == 0;
	}
	public static boolean isBlank(Long l) {
		return l == 0;
	}
	public static boolean isBlank(Date d) {
		return d == null;
	}
}
