package com.gargoylesoftware.htmlunit.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;






























public final class EncodingSniffer
{
  private static final Log LOG = LogFactory.getLog(EncodingSniffer.class);
  

  private static final byte[][] COMMENT_START = {
    { 60 }, 
    { 33 }, 
    { 45 }, 
    { 45 } };
  


  private static final byte[][] META_START = {
    { 60 }, 
    { 109, 77 }, 
    { 101, 69 }, 
    { 116, 84 }, 
    { 97, 65 }, 
    { 9, 10, 12, 13, 32, 47 } };
  


  private static final byte[][] OTHER_START = {
    { 60 }, 
    { 33, 47, 63 } };
  


  private static final byte[][] CHARSET_START = {
    { 99, 67 }, 
    { 104, 72 }, 
    { 97, 65 }, 
    { 114, 82 }, 
    { 115, 83 }, 
    { 101, 69 }, 
    { 116, 84 } };
  




  private static final Map<String, String> ENCODING_FROM_LABEL = new HashMap();
  
  static
  {
    ENCODING_FROM_LABEL.put("unicode-1-1-utf-8", "utf-8");
    ENCODING_FROM_LABEL.put("utf-8", "utf-8");
    ENCODING_FROM_LABEL.put("utf8", "utf-8");
    




    ENCODING_FROM_LABEL.put("866", "ibm866");
    ENCODING_FROM_LABEL.put("cp866", "ibm866");
    ENCODING_FROM_LABEL.put("csibm866", "ibm866");
    ENCODING_FROM_LABEL.put("ibm866", "ibm866");
    

    ENCODING_FROM_LABEL.put("csisolatin2", "iso-8859-2");
    ENCODING_FROM_LABEL.put("iso-8859-2", "iso-8859-2");
    ENCODING_FROM_LABEL.put("iso-ir-101", "iso-8859-2");
    ENCODING_FROM_LABEL.put("iso8859-2", "iso-8859-2");
    ENCODING_FROM_LABEL.put("iso88592", "iso-8859-2");
    ENCODING_FROM_LABEL.put("iso_8859-2", "iso-8859-2");
    ENCODING_FROM_LABEL.put("iso_8859-2:1987", "iso-8859-2");
    ENCODING_FROM_LABEL.put("l2", "iso-8859-2");
    ENCODING_FROM_LABEL.put("latin2", "iso-8859-2");
    

    ENCODING_FROM_LABEL.put("csisolatin2", "iso-8859-3");
    ENCODING_FROM_LABEL.put("csisolatin3", "iso-8859-3");
    ENCODING_FROM_LABEL.put("iso-8859-3", "iso-8859-3");
    ENCODING_FROM_LABEL.put("iso-ir-109", "iso-8859-3");
    ENCODING_FROM_LABEL.put("iso8859-3", "iso-8859-3");
    ENCODING_FROM_LABEL.put("iso88593", "iso-8859-3");
    ENCODING_FROM_LABEL.put("iso_8859-3", "iso-8859-3");
    ENCODING_FROM_LABEL.put("iso_8859-3:1988", "iso-8859-3");
    ENCODING_FROM_LABEL.put("l3", "iso-8859-3");
    ENCODING_FROM_LABEL.put("latin3", "iso-8859-3");
    

    ENCODING_FROM_LABEL.put("csisolatin4", "iso-8859-4");
    ENCODING_FROM_LABEL.put("iso-8859-4", "iso-8859-4");
    ENCODING_FROM_LABEL.put("iso-ir-110", "iso-8859-4");
    ENCODING_FROM_LABEL.put("iso8859-4", "iso-8859-4");
    ENCODING_FROM_LABEL.put("iso88594", "iso-8859-4");
    ENCODING_FROM_LABEL.put("iso_8859-4", "iso-8859-4");
    ENCODING_FROM_LABEL.put("iso_8859-4:1988", "iso-8859-4");
    ENCODING_FROM_LABEL.put("l4", "iso-8859-4");
    ENCODING_FROM_LABEL.put("latin4", "iso-8859-4");
    

    ENCODING_FROM_LABEL.put("csisolatincyrillic", "iso-8859-5");
    ENCODING_FROM_LABEL.put("csisolatincyrillic", "iso-8859-5");
    ENCODING_FROM_LABEL.put("cyrillic", "iso-8859-5");
    ENCODING_FROM_LABEL.put("iso-8859-5", "iso-8859-5");
    ENCODING_FROM_LABEL.put("iso-ir-144", "iso-8859-5");
    ENCODING_FROM_LABEL.put("iso8859-5", "iso-8859-5");
    ENCODING_FROM_LABEL.put("iso88595", "iso-8859-5");
    ENCODING_FROM_LABEL.put("iso_8859-5", "iso-8859-5");
    ENCODING_FROM_LABEL.put("iso_8859-5:1988", "iso-8859-5");
    

    ENCODING_FROM_LABEL.put("arabic", "iso-8859-6");
    ENCODING_FROM_LABEL.put("asmo-708", "iso-8859-6");
    ENCODING_FROM_LABEL.put("csiso88596e", "iso-8859-6");
    ENCODING_FROM_LABEL.put("csiso88596i", "iso-8859-6");
    ENCODING_FROM_LABEL.put("csisolatinarabic", "iso-8859-6");
    ENCODING_FROM_LABEL.put("ecma-114", "iso-8859-6");
    ENCODING_FROM_LABEL.put("iso-8859-6", "iso-8859-6");
    ENCODING_FROM_LABEL.put("iso-8859-6-e", "iso-8859-6");
    ENCODING_FROM_LABEL.put("iso-8859-6-i", "iso-8859-6");
    ENCODING_FROM_LABEL.put("iso-ir-127", "iso-8859-6");
    ENCODING_FROM_LABEL.put("iso8859-6", "iso-8859-6");
    ENCODING_FROM_LABEL.put("iso88596", "iso-8859-6");
    ENCODING_FROM_LABEL.put("iso_8859-6", "iso-8859-6");
    ENCODING_FROM_LABEL.put("iso_8859-6:1987", "iso-8859-6");
    

    ENCODING_FROM_LABEL.put("csisolatingreek", "iso-8859-7");
    ENCODING_FROM_LABEL.put("ecma-118", "iso-8859-7");
    ENCODING_FROM_LABEL.put("elot_928", "iso-8859-7");
    ENCODING_FROM_LABEL.put("greek", "iso-8859-7");
    ENCODING_FROM_LABEL.put("greek8", "iso-8859-7");
    ENCODING_FROM_LABEL.put("iso-8859-7", "iso-8859-7");
    ENCODING_FROM_LABEL.put("iso-ir-126", "iso-8859-7");
    ENCODING_FROM_LABEL.put("iso8859-7", "iso-8859-7");
    ENCODING_FROM_LABEL.put("iso88597", "iso-8859-7");
    ENCODING_FROM_LABEL.put("iso_8859-7", "iso-8859-7");
    ENCODING_FROM_LABEL.put("iso_8859-7:1987", "iso-8859-7");
    ENCODING_FROM_LABEL.put("sun_eu_greek", "iso-8859-7");
    

    ENCODING_FROM_LABEL.put("csisolatingreek", "iso-8859-8");
    ENCODING_FROM_LABEL.put("csiso88598e", "iso-8859-8");
    ENCODING_FROM_LABEL.put("csisolatinhebrew", "iso-8859-8");
    ENCODING_FROM_LABEL.put("hebrew", "iso-8859-8");
    ENCODING_FROM_LABEL.put("iso-8859-8", "iso-8859-8");
    ENCODING_FROM_LABEL.put("iso-8859-8-e", "iso-8859-8");
    ENCODING_FROM_LABEL.put("iso-ir-138", "iso-8859-8");
    ENCODING_FROM_LABEL.put("iso8859-8", "iso-8859-8");
    ENCODING_FROM_LABEL.put("iso88598", "iso-8859-8");
    ENCODING_FROM_LABEL.put("iso_8859-8", "iso-8859-8");
    ENCODING_FROM_LABEL.put("iso_8859-8:1988", "iso-8859-8");
    ENCODING_FROM_LABEL.put("visual", "iso-8859-8");
    

    ENCODING_FROM_LABEL.put("csiso88598i", "iso-8859-8-i");
    ENCODING_FROM_LABEL.put("iso-8859-8-i", "iso-8859-8-i");
    ENCODING_FROM_LABEL.put("logical", "iso-8859-8-i");
    

    ENCODING_FROM_LABEL.put("csisolatin6", "iso-8859-10");
    ENCODING_FROM_LABEL.put("iso-8859-10", "iso-8859-10");
    ENCODING_FROM_LABEL.put("iso-ir-157", "iso-8859-10");
    ENCODING_FROM_LABEL.put("iso8859-10", "iso-8859-10");
    ENCODING_FROM_LABEL.put("iso885910", "iso-8859-10");
    ENCODING_FROM_LABEL.put("l6", "iso-8859-10");
    ENCODING_FROM_LABEL.put("latin6", "iso-8859-10");
    

    ENCODING_FROM_LABEL.put("iso-8859-13", "iso-8859-13");
    ENCODING_FROM_LABEL.put("iso8859-13", "iso-8859-13");
    ENCODING_FROM_LABEL.put("iso885913", "iso-8859-13");
    

    ENCODING_FROM_LABEL.put("iso-8859-14", "iso-8859-14");
    ENCODING_FROM_LABEL.put("iso8859-14", "iso-8859-14");
    ENCODING_FROM_LABEL.put("iso885914", "iso-8859-14");
    

    ENCODING_FROM_LABEL.put("csisolatin9", "iso-8859-15");
    ENCODING_FROM_LABEL.put("iso-8859-15", "iso-8859-15");
    ENCODING_FROM_LABEL.put("iso8859-15", "iso-8859-15");
    ENCODING_FROM_LABEL.put("iso885915", "iso-8859-15");
    ENCODING_FROM_LABEL.put("iso_8859-15", "iso-8859-15");
    ENCODING_FROM_LABEL.put("l9", "iso-8859-15");
    

    ENCODING_FROM_LABEL.put("iso-8859-16", "iso-8859-16");
    

    ENCODING_FROM_LABEL.put("cskoi8r", "koi8-r");
    ENCODING_FROM_LABEL.put("koi", "koi8-r");
    ENCODING_FROM_LABEL.put("koi8", "koi8-r");
    ENCODING_FROM_LABEL.put("koi8-r", "koi8-r");
    ENCODING_FROM_LABEL.put("koi8_r", "koi8-r");
    

    ENCODING_FROM_LABEL.put("koi8-u", "koi8-u");
    

    ENCODING_FROM_LABEL.put("csmacintosh", "macintosh");
    ENCODING_FROM_LABEL.put("mac", "macintosh");
    ENCODING_FROM_LABEL.put("macintosh", "macintosh");
    ENCODING_FROM_LABEL.put("x-mac-roman", "macintosh");
    

    ENCODING_FROM_LABEL.put("dos-874", "windows-874");
    ENCODING_FROM_LABEL.put("iso-8859-11", "windows-874");
    ENCODING_FROM_LABEL.put("iso8859-11", "windows-874");
    ENCODING_FROM_LABEL.put("iso885911", "windows-874");
    ENCODING_FROM_LABEL.put("tis-620", "windows-874");
    ENCODING_FROM_LABEL.put("windows-874", "windows-874");
    

    ENCODING_FROM_LABEL.put("cp1250", "windows-1250");
    ENCODING_FROM_LABEL.put("windows-1250", "windows-1250");
    ENCODING_FROM_LABEL.put("x-cp1250", "windows-1250");
    

    ENCODING_FROM_LABEL.put("cp1251", "windows-1251");
    ENCODING_FROM_LABEL.put("windows-1251", "windows-1251");
    ENCODING_FROM_LABEL.put("x-cp1251", "windows-1251");
    

    ENCODING_FROM_LABEL.put("ansi_x3.4-1968", "windows-1252");
    ENCODING_FROM_LABEL.put("ascii", "windows-1252");
    ENCODING_FROM_LABEL.put("cp1252", "windows-1252");
    ENCODING_FROM_LABEL.put("cp819", "windows-1252");
    ENCODING_FROM_LABEL.put("csisolatin1", "windows-1252");
    ENCODING_FROM_LABEL.put("ibm819", "windows-1252");
    ENCODING_FROM_LABEL.put("iso-8859-1", "windows-1252");
    ENCODING_FROM_LABEL.put("iso-ir-100", "windows-1252");
    ENCODING_FROM_LABEL.put("iso8859-1", "windows-1252");
    ENCODING_FROM_LABEL.put("iso88591", "windows-1252");
    ENCODING_FROM_LABEL.put("iso_8859-1", "windows-1252");
    ENCODING_FROM_LABEL.put("iso_8859-1:1987", "windows-1252");
    ENCODING_FROM_LABEL.put("l1", "windows-1252");
    ENCODING_FROM_LABEL.put("latin1", "windows-1252");
    ENCODING_FROM_LABEL.put("us-ascii", "windows-1252");
    ENCODING_FROM_LABEL.put("windows-1252", "windows-1252");
    ENCODING_FROM_LABEL.put("x-cp1252", "windows-1252");
    

    ENCODING_FROM_LABEL.put("cp1253", "windows-1253");
    ENCODING_FROM_LABEL.put("windows-1253", "windows-1253");
    ENCODING_FROM_LABEL.put("x-cp1253", "windows-1253");
    

    ENCODING_FROM_LABEL.put("cp1254", "windows-1254");
    ENCODING_FROM_LABEL.put("csisolatin5", "windows-1254");
    ENCODING_FROM_LABEL.put("iso-8859-9", "windows-1254");
    ENCODING_FROM_LABEL.put("iso-ir-148", "windows-1254");
    ENCODING_FROM_LABEL.put("iso8859-9", "windows-1254");
    ENCODING_FROM_LABEL.put("iso88599", "windows-1254");
    ENCODING_FROM_LABEL.put("iso_8859-9", "windows-1254");
    ENCODING_FROM_LABEL.put("iso_8859-9:1989", "windows-1254");
    ENCODING_FROM_LABEL.put("l5", "windows-1254");
    ENCODING_FROM_LABEL.put("latin5", "windows-1254");
    ENCODING_FROM_LABEL.put("windows-1254", "windows-1254");
    ENCODING_FROM_LABEL.put("x-cp1254", "windows-1254");
    

    ENCODING_FROM_LABEL.put("cp1255", "windows-1255");
    ENCODING_FROM_LABEL.put("windows-1255", "windows-1255");
    ENCODING_FROM_LABEL.put("x-cp1255", "windows-1255");
    

    ENCODING_FROM_LABEL.put("cp1256", "windows-1256");
    ENCODING_FROM_LABEL.put("windows-1256", "windows-1256");
    ENCODING_FROM_LABEL.put("x-cp1256", "windows-1256");
    

    ENCODING_FROM_LABEL.put("cp1257", "windows-1257");
    ENCODING_FROM_LABEL.put("windows-1257", "windows-1257");
    ENCODING_FROM_LABEL.put("x-cp1257", "windows-1257");
    

    ENCODING_FROM_LABEL.put("cp1258", "windows-1258");
    ENCODING_FROM_LABEL.put("windows-1258", "windows-1258");
    ENCODING_FROM_LABEL.put("x-cp1258", "windows-1258");
    

    ENCODING_FROM_LABEL.put("x-mac-cyrillic", "x-mac-cyrillic");
    ENCODING_FROM_LABEL.put("x-mac-ukrainian", "x-mac-cyrillic");
    




    ENCODING_FROM_LABEL.put("chinese", "gb18030");
    ENCODING_FROM_LABEL.put("csgb2312", "gb18030");
    ENCODING_FROM_LABEL.put("csiso58gb231280", "gb18030");
    ENCODING_FROM_LABEL.put("gb18030", "gb18030");
    ENCODING_FROM_LABEL.put("gb2312", "gb18030");
    ENCODING_FROM_LABEL.put("gb_2312", "gb18030");
    ENCODING_FROM_LABEL.put("gb_2312-80", "gb18030");
    ENCODING_FROM_LABEL.put("gbk", "gb18030");
    ENCODING_FROM_LABEL.put("iso-ir-58", "gb18030");
    ENCODING_FROM_LABEL.put("x-gbk", "gb18030");
    

    ENCODING_FROM_LABEL.put("hz-gb-2312", "hz-gb-2312");
    




    ENCODING_FROM_LABEL.put("big5", "big5");
    ENCODING_FROM_LABEL.put("big5-hkscs", "big5");
    ENCODING_FROM_LABEL.put("cn-big5", "big5");
    ENCODING_FROM_LABEL.put("csbig5", "big5");
    ENCODING_FROM_LABEL.put("x-x-big5", "big5");
    




    ENCODING_FROM_LABEL.put("cseucpkdfmtjapanese", "euc-jp");
    ENCODING_FROM_LABEL.put("euc-jp", "euc-jp");
    ENCODING_FROM_LABEL.put("x-euc-jp", "euc-jp");
    

    ENCODING_FROM_LABEL.put("csiso2022jp", "iso-2022-jp");
    ENCODING_FROM_LABEL.put("iso-2022-jp", "iso-2022-jp");
    

    ENCODING_FROM_LABEL.put("csshiftjis", "shift_jis");
    ENCODING_FROM_LABEL.put("ms_kanji", "shift_jis");
    ENCODING_FROM_LABEL.put("shift-jis", "shift_jis");
    ENCODING_FROM_LABEL.put("shift_jis", "shift_jis");
    ENCODING_FROM_LABEL.put("sjis", "shift_jis");
    ENCODING_FROM_LABEL.put("windows-31j", "shift_jis");
    ENCODING_FROM_LABEL.put("x-sjis", "shift_jis");
    




    ENCODING_FROM_LABEL.put("cseuckr", "euc-kr");
    ENCODING_FROM_LABEL.put("csksc56011987", "euc-kr");
    ENCODING_FROM_LABEL.put("euc-kr", "euc-kr");
    ENCODING_FROM_LABEL.put("iso-ir-149", "euc-kr");
    ENCODING_FROM_LABEL.put("korean", "euc-kr");
    ENCODING_FROM_LABEL.put("ks_c_5601-1987", "euc-kr");
    ENCODING_FROM_LABEL.put("ks_c_5601-1989", "euc-kr");
    ENCODING_FROM_LABEL.put("ksc5601", "euc-kr");
    ENCODING_FROM_LABEL.put("ksc_5601", "euc-kr");
    ENCODING_FROM_LABEL.put("windows-949", "euc-kr");
    




    ENCODING_FROM_LABEL.put("csiso2022kr", "replacement");
    ENCODING_FROM_LABEL.put("iso-2022-cn", "replacement");
    ENCODING_FROM_LABEL.put("iso-2022-cn-ext", "replacement");
    ENCODING_FROM_LABEL.put("iso-2022-kr", "replacement");
    

    ENCODING_FROM_LABEL.put("utf-16be", "utf-16be");
    

    ENCODING_FROM_LABEL.put("utf-16", "utf-16le");
    ENCODING_FROM_LABEL.put("utf-16le", "utf-16le");
    

    ENCODING_FROM_LABEL.put("x-user-defined", "x-user-defined");
  }
  
  private static final byte[] XML_DECLARATION_PREFIX = "<?xml ".getBytes(StandardCharsets.US_ASCII);
  








  private static final int SIZE_OF_HTML_CONTENT_SNIFFED = 4096;
  








  private static final int SIZE_OF_XML_CONTENT_SNIFFED = 512;
  









  private EncodingSniffer() {}
  









  public static Charset sniffEncoding(List<NameValuePair> headers, InputStream content)
    throws IOException
  {
    if (isHtml(headers)) {
      return sniffHtmlEncoding(headers, content);
    }
    if (isXml(headers)) {
      return sniffXmlEncoding(headers, content);
    }
    
    return sniffUnknownContentTypeEncoding(headers, content);
  }
  






  static boolean isHtml(List<NameValuePair> headers)
  {
    return contentTypeEndsWith(headers, new String[] { "text/html" });
  }
  





  static boolean isXml(List<NameValuePair> headers)
  {
    return contentTypeEndsWith(headers, new String[] { "text/xml", "application/xml", "text/vnd.wap.wml", "+xml" });
  }
  








  static boolean contentTypeEndsWith(List<NameValuePair> headers, String... contentTypeEndings)
  {
    for (NameValuePair pair : headers) {
      String name = pair.getName();
      if ("content-type".equalsIgnoreCase(name)) {
        String value = pair.getValue();
        int i = value.indexOf(';');
        if (i != -1) {
          value = value.substring(0, i);
        }
        value = value.trim().toLowerCase(Locale.ROOT);
        for (String ending : contentTypeEndings) {
          if (value.endsWith(ending.toLowerCase(Locale.ROOT))) {
            return true;
          }
        }
        return false;
      }
    }
    return false;
  }
  














  public static Charset sniffHtmlEncoding(List<NameValuePair> headers, InputStream content)
    throws IOException
  {
    Charset encoding = sniffEncodingFromHttpHeaders(headers);
    if ((encoding != null) || (content == null)) {
      return encoding;
    }
    
    byte[] bytes = read(content, 3);
    encoding = sniffEncodingFromUnicodeBom(bytes);
    if (encoding != null) {
      return encoding;
    }
    
    bytes = readAndPrepend(content, 4096, bytes);
    encoding = sniffEncodingFromMetaTag(bytes);
    return encoding;
  }
  













  public static Charset sniffXmlEncoding(List<NameValuePair> headers, InputStream content)
    throws IOException
  {
    Charset encoding = sniffEncodingFromHttpHeaders(headers);
    if ((encoding != null) || (content == null)) {
      return encoding;
    }
    
    byte[] bytes = read(content, 3);
    encoding = sniffEncodingFromUnicodeBom(bytes);
    if (encoding != null) {
      return encoding;
    }
    
    bytes = readAndPrepend(content, 512, bytes);
    encoding = sniffEncodingFromXmlDeclaration(bytes);
    return encoding;
  }
  














  public static Charset sniffUnknownContentTypeEncoding(List<NameValuePair> headers, InputStream content)
    throws IOException
  {
    Charset encoding = sniffEncodingFromHttpHeaders(headers);
    if ((encoding != null) || (content == null)) {
      return encoding;
    }
    
    byte[] bytes = read(content, 3);
    encoding = sniffEncodingFromUnicodeBom(bytes);
    return encoding;
  }
  






  static Charset sniffEncodingFromHttpHeaders(List<NameValuePair> headers)
  {
    Charset encoding = null;
    for (NameValuePair pair : headers) {
      String name = pair.getName();
      if ("content-type".equalsIgnoreCase(name)) {
        String value = pair.getValue();
        encoding = extractEncodingFromContentType(value);
        if (encoding != null) {
          break;
        }
      }
    }
    if ((encoding != null) && (LOG.isDebugEnabled())) {
      LOG.debug("Encoding found in HTTP headers: '" + encoding + "'.");
    }
    return encoding;
  }
  







  static Charset sniffEncodingFromUnicodeBom(byte[] bytes)
  {
    if (bytes == null) {
      return null;
    }
    
    Charset encoding = null;
    if (startsWith(bytes, ByteOrderMark.UTF_8)) {
      encoding = StandardCharsets.UTF_8;
    }
    else if (startsWith(bytes, ByteOrderMark.UTF_16BE)) {
      encoding = StandardCharsets.UTF_16BE;
    }
    else if (startsWith(bytes, ByteOrderMark.UTF_16LE)) {
      encoding = StandardCharsets.UTF_16LE;
    }
    
    if ((encoding != null) && (LOG.isDebugEnabled())) {
      LOG.debug("Encoding found in Unicode Byte Order Mark: '" + encoding + "'.");
    }
    return encoding;
  }
  





  private static boolean startsWith(byte[] bytes, ByteOrderMark bom)
  {
    byte[] bomBytes = bom.getBytes();
    byte[] firstBytes = Arrays.copyOfRange(bytes, 0, Math.min(bytes.length, bomBytes.length));
    return Arrays.equals(firstBytes, bomBytes);
  }
  






  static Charset sniffEncodingFromMetaTag(byte[] bytes)
  {
    for (int i = 0; i < bytes.length; i++) {
      if (matches(bytes, i, COMMENT_START)) {
        i = indexOfSubArray(bytes, new byte[] { 45, 45, 62 }, i);
        if (i == -1) {
          break;
        }
        i += 2;
      }
      else if (matches(bytes, i, META_START)) {
        i += META_START.length;
        for (Attribute att = getAttribute(bytes, i); att != null; att = getAttribute(bytes, i)) {
          i = att.getUpdatedIndex();
          String name = att.getName();
          String value = att.getValue();
          if (("charset".equals(name)) || ("content".equals(name))) {
            Charset charset = null;
            if ("charset".equals(name)) {
              charset = toCharset(value);
            }
            else if ("content".equals(name)) {
              charset = extractEncodingFromContentType(value);
              if (charset == null) {
                continue;
              }
            }
            if ((StandardCharsets.UTF_16BE == charset) || (StandardCharsets.UTF_16LE == charset)) {
              charset = StandardCharsets.UTF_8;
            }
            if (charset != null) {
              if (LOG.isDebugEnabled()) {
                LOG.debug("Encoding found in meta tag: '" + charset + "'.");
              }
              return charset;
            }
          }
        }
      }
      else if ((i + 1 < bytes.length) && (bytes[i] == 60) && (Character.isLetter(bytes[(i + 1)]))) {
        i = skipToAnyOf(bytes, i, new byte[] { 9, 10, 12, 13, 32, 62 });
        if (i == -1) {
          break;
        }
        Attribute att;
        while ((att = getAttribute(bytes, i)) != null) { Attribute att;
          i = att.getUpdatedIndex();
        }
      }
      else if ((i + 2 < bytes.length) && (bytes[i] == 60) && (bytes[(i + 1)] == 47) && (Character.isLetter(bytes[(i + 2)]))) {
        i = skipToAnyOf(bytes, i, new byte[] { 9, 10, 12, 13, 32, 62 });
        if (i == -1) {
          break;
        }
        Attribute attribute;
        while ((attribute = getAttribute(bytes, i)) != null) { Attribute attribute;
          i = attribute.getUpdatedIndex();
        }
      }
      else if (matches(bytes, i, OTHER_START)) {
        i = skipToAnyOf(bytes, i, new byte[] { 62 });
        if (i == -1) {
          break;
        }
      }
    }
    return null;
  }
  








  static Attribute getAttribute(byte[] bytes, int i)
  {
    if (i >= bytes.length) {
      return null;
    }
    while ((bytes[i] == 9) || (bytes[i] == 10) || (bytes[i] == 12) || (bytes[i] == 13) || (bytes[i] == 32) || (bytes[i] == 47)) {
      i++;
      if (i >= bytes.length) {
        return null;
      }
    }
    if (bytes[i] == 62) {
      return null;
    }
    StringBuilder name = new StringBuilder();
    StringBuilder value = new StringBuilder();
    for (;; i++) {
      if (i >= bytes.length) {
        return new Attribute(name.toString(), value.toString(), i);
      }
      if ((bytes[i] == 61) && (name.length() != 0)) {
        i++;
        break;
      }
      if ((bytes[i] != 9) && (bytes[i] != 10) && (bytes[i] != 12) && (bytes[i] != 13)) { if (bytes[i] != 32) {}
      } else { while ((bytes[i] == 9) || (bytes[i] == 10) || (bytes[i] == 12) || (bytes[i] == 13) || (bytes[i] == 32)) {
          i++;
          if (i >= bytes.length) {
            return new Attribute(name.toString(), value.toString(), i);
          }
        }
        if (bytes[i] != 61) {
          return new Attribute(name.toString(), value.toString(), i);
        }
        i++;
        break;
      }
      if ((bytes[i] == 47) || (bytes[i] == 62)) {
        return new Attribute(name.toString(), value.toString(), i);
      }
      name.append((char)bytes[i]);
    }
    if (i >= bytes.length) {
      return new Attribute(name.toString(), value.toString(), i);
    }
    while ((bytes[i] == 9) || (bytes[i] == 10) || (bytes[i] == 12) || (bytes[i] == 13) || (bytes[i] == 32)) {
      i++;
      if (i >= bytes.length) {
        return new Attribute(name.toString(), value.toString(), i);
      }
    }
    if ((bytes[i] == 34) || (bytes[i] == 39)) {
      byte b = bytes[i];
      for (i++; i < bytes.length; i++) {
        if (bytes[i] == b) {
          i++;
          return new Attribute(name.toString(), value.toString(), i);
        }
        if ((bytes[i] >= 65) && (bytes[i] <= 90)) {
          byte b2 = (byte)(bytes[i] + 32);
          value.append((char)b2);
        }
        else {
          value.append((char)bytes[i]);
        }
      }
      return new Attribute(name.toString(), value.toString(), i);
    }
    if (bytes[i] == 62) {
      return new Attribute(name.toString(), value.toString(), i);
    }
    if ((bytes[i] >= 65) && (bytes[i] <= 90)) {
      byte b = (byte)(bytes[i] + 32);
      value.append((char)b);
      i++;
    }
    else {
      value.append((char)bytes[i]);
      i++;
    }
    for (; i < bytes.length; i++) {
      if ((bytes[i] == 9) || (bytes[i] == 10) || (bytes[i] == 12) || (bytes[i] == 13) || (bytes[i] == 32) || (bytes[i] == 62)) {
        return new Attribute(name.toString(), value.toString(), i);
      }
      if ((bytes[i] >= 65) && (bytes[i] <= 90)) {
        byte b = (byte)(bytes[i] + 32);
        value.append((char)b);
      }
      else {
        value.append((char)bytes[i]);
      }
    }
    return new Attribute(name.toString(), value.toString(), i);
  }
  








  static Charset extractEncodingFromContentType(String s)
  {
    if (s == null) {
      return null;
    }
    byte[] bytes = s.getBytes(StandardCharsets.US_ASCII);
    
    for (int i = 0; i < bytes.length; i++) {
      if (matches(bytes, i, CHARSET_START)) {
        i += CHARSET_START.length;
        break;
      }
    }
    if (i == bytes.length) {
      return null;
    }
    while ((bytes[i] == 9) || (bytes[i] == 10) || (bytes[i] == 12) || (bytes[i] == 13) || (bytes[i] == 32)) {
      i++;
      if (i == bytes.length) {
        return null;
      }
    }
    if (bytes[i] != 61) {
      return null;
    }
    i++;
    if (i == bytes.length) {
      return null;
    }
    while ((bytes[i] == 9) || (bytes[i] == 10) || (bytes[i] == 12) || (bytes[i] == 13) || (bytes[i] == 32)) {
      i++;
      if (i == bytes.length) {
        return null;
      }
    }
    if (bytes[i] == 34) {
      if (bytes.length <= i + 1) {
        return null;
      }
      int index = ArrayUtils.indexOf(bytes, (byte)34, i + 1);
      if (index == -1) {
        return null;
      }
      String charsetName = new String(ArrayUtils.subarray(bytes, i + 1, index), StandardCharsets.US_ASCII);
      return toCharset(charsetName);
    }
    if (bytes[i] == 39) {
      if (bytes.length <= i + 1) {
        return null;
      }
      int index = ArrayUtils.indexOf(bytes, (byte)39, i + 1);
      if (index == -1) {
        return null;
      }
      String charsetName = new String(ArrayUtils.subarray(bytes, i + 1, index), StandardCharsets.US_ASCII);
      return toCharset(charsetName);
    }
    int end = skipToAnyOf(bytes, i, new byte[] { 9, 10, 12, 13, 32, 59 });
    if (end == -1) {
      end = bytes.length;
    }
    String charsetName = new String(ArrayUtils.subarray(bytes, i, end), StandardCharsets.US_ASCII);
    return toCharset(charsetName);
  }
  






  static Charset sniffEncodingFromXmlDeclaration(byte[] bytes)
  {
    Charset encoding = null;
    if ((bytes.length > 5) && 
      (XML_DECLARATION_PREFIX[0] == bytes[0]) && 
      (XML_DECLARATION_PREFIX[1] == bytes[1]) && 
      (XML_DECLARATION_PREFIX[2] == bytes[2]) && 
      (XML_DECLARATION_PREFIX[3] == bytes[3]) && 
      (XML_DECLARATION_PREFIX[4] == bytes[4]) && 
      (XML_DECLARATION_PREFIX[5] == bytes[5])) {
      int index = ArrayUtils.indexOf(bytes, (byte)63, 2);
      if ((index + 1 < bytes.length) && (bytes[(index + 1)] == 62)) {
        String declaration = new String(bytes, 0, index + 2, StandardCharsets.US_ASCII);
        int start = declaration.indexOf("encoding");
        if (start != -1) {
          start += 8;
          
          for (;;)
          {
            switch (declaration.charAt(start)) {
            case '"': 
            case '\'': 
              char delimiter = declaration.charAt(start);
              start++;
              break;
            
            default: 
              start++; }
          }
          char delimiter;
          int end = declaration.indexOf(delimiter, start);
          encoding = toCharset(declaration.substring(start, end));
        }
      }
    }
    if ((encoding != null) && (LOG.isDebugEnabled())) {
      LOG.debug("Encoding found in XML declaration: '" + encoding + "'.");
    }
    return encoding;
  }
  





  public static Charset toCharset(String charsetName)
  {
    if (StringUtils.isEmpty(charsetName)) {
      return null;
    }
    try {
      return Charset.forName(charsetName);
    }
    catch (IllegalCharsetNameException|UnsupportedCharsetException e) {}
    return null;
  }
  










  static boolean matches(byte[] bytes, int i, byte[][] sought)
  {
    if (i + sought.length > bytes.length) {
      return false;
    }
    for (int x = 0; x < sought.length; x++) {
      byte[] possibilities = sought[x];
      boolean match = false;
      for (int y = 0; y < possibilities.length; y++) {
        if (bytes[(i + x)] == possibilities[y]) {
          match = true;
          break;
        }
      }
      if (!match) {
        return false;
      }
    }
    return true;
  }
  
  static int skipToAnyOf(byte[] bytes, int i, byte[] targets)
  {
    for (; 
        






        i < bytes.length; i++) {
      if (ArrayUtils.contains(targets, bytes[i])) {
        break;
      }
    }
    if (i == bytes.length) {
      i = -1;
    }
    return i;
  }
  








  static int indexOfSubArray(byte[] array, byte[] subarray, int startIndex)
  {
    for (int i = startIndex; i < array.length; i++) {
      boolean found = true;
      if (i + subarray.length > array.length) {
        break;
      }
      for (int j = 0; j < subarray.length; j++) {
        byte a = array[(i + j)];
        byte b = subarray[j];
        if (a != b) {
          found = false;
          break;
        }
      }
      if (found) {
        return i;
      }
    }
    return -1;
  }
  








  static byte[] read(InputStream content, int size)
    throws IOException
  {
    byte[] bytes = new byte[size];
    int count = content.read(bytes);
    if (count == -1) {
      bytes = new byte[0];
    }
    else if (count < size) {
      byte[] smaller = new byte[count];
      System.arraycopy(bytes, 0, smaller, 0, count);
      bytes = smaller;
    }
    return bytes;
  }
  










  static byte[] readAndPrepend(InputStream content, int size, byte[] prefix)
    throws IOException
  {
    byte[] bytes = read(content, size);
    byte[] joined = new byte[prefix.length + bytes.length];
    System.arraycopy(prefix, 0, joined, 0, prefix.length);
    System.arraycopy(bytes, 0, joined, prefix.length, bytes.length);
    return joined;
  }
  
  static class Attribute {
    private final String name_;
    private final String value_;
    private final int updatedIndex_;
    
    Attribute(String name, String value, int updatedIndex) { name_ = name;
      value_ = value;
      updatedIndex_ = updatedIndex;
    }
    
    String getName() { return name_; }
    
    String getValue() {
      return value_;
    }
    
    int getUpdatedIndex() { return updatedIndex_; }
  }
  






  public static String translateEncodingLabel(Charset encodingLabel)
  {
    if (encodingLabel == null) {
      return null;
    }
    String encLC = encodingLabel.name().toLowerCase(Locale.ROOT);
    String enc = (String)ENCODING_FROM_LABEL.get(encLC);
    if (encLC.equals(enc)) {
      return encodingLabel.name();
    }
    return enc;
  }
}
