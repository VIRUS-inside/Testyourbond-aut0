package com.gargoylesoftware.htmlunit;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.XHtmlPage;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Locale;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;





























































public class DefaultPageCreator
  implements PageCreator, Serializable
{
  private static final byte[] markerUTF8_ = { -17, -69, -65 };
  private static final byte[] markerUTF16BE_ = { -2, -1 };
  private static final byte[] markerUTF16LE_ = { -1, -2 };
  


  public static enum PageType
  {
    HTML, 
    
    JAVASCRIPT, 
    
    XML, 
    
    TEXT, 
    
    UNKNOWN;
  }
  





  public static PageType determinePageType(String contentType)
  {
    if (contentType == null) {
      return PageType.UNKNOWN;
    }
    
    String str = contentType; switch (contentType.hashCode()) {case -1248326952:  if (str.equals("application/xml")) {} break; case -1082243251:  if (str.equals("text/html")) break; break; case -1004727243:  if (str.equals("text/xml")) {} break; case -723118015:  if (str.equals("application/x-javascript")) {} break; case -227171396:  if (str.equals("image/svg+xml")) break; break; case 1440428940:  if (str.equals("application/javascript")) {} break; case 2006143018:  if (str.equals("text/vnd.wap.wml")) {} break; case 2132236175:  if (!str.equals("text/javascript"))
      {
        break label196;
        return PageType.HTML;

      }
      else
      {
        return PageType.JAVASCRIPT;
        



        return PageType.XML; }
      break; }
    label196:
    if (contentType.endsWith("+xml")) {
      return PageType.XML;
    }
    
    if (contentType.startsWith("text/")) {
      return PageType.TEXT;
    }
    
    return PageType.UNKNOWN;
  }
  







  public DefaultPageCreator() {}
  






  public Page createPage(WebResponse webResponse, WebWindow webWindow)
    throws IOException
  {
    String contentType = determineContentType(webResponse.getContentType().toLowerCase(Locale.ROOT), 
      webResponse.getContentAsStream());
    
    PageType pageType = determinePageType(contentType);
    switch (pageType) {
    case HTML: 
      return createHtmlPage(webResponse, webWindow);
    
    case JAVASCRIPT: 
      return createHtmlPage(webResponse, webWindow);
    
    case TEXT: 
      SgmlPage sgmlPage = createXmlPage(webResponse, webWindow);
      DomElement doc = sgmlPage.getDocumentElement();
      if ((doc != null) && ("http://www.w3.org/1999/xhtml".equals(doc.getNamespaceURI()))) {
        return createXHtmlPage(webResponse, webWindow);
      }
      return sgmlPage;
    
    case UNKNOWN: 
      return createTextPage(webResponse, webWindow);
    }
    
    return createUnexpectedPage(webResponse, webWindow);
  }
  









  protected String determineContentType(String contentType, InputStream contentAsStream)
    throws IOException
  {
    try
    {
      if (!StringUtils.isEmpty(contentType)) {
        return contentType;
      }
      
      byte[] bytes = read(contentAsStream, 500);
      if (bytes.length == 0) {
        return "text/plain";
      }
      
      String asAsciiString = new String(bytes, "ASCII").toUpperCase(Locale.ROOT);
      if (asAsciiString.contains("<HTML")) {
        return "text/html";
      }
      if ((startsWith(bytes, markerUTF8_)) || (startsWith(bytes, markerUTF16BE_)) || 
        (startsWith(bytes, markerUTF16LE_))) {
        return "text/plain";
      }
      if (asAsciiString.trim().startsWith("<SCRIPT>")) {
        return "application/javascript";
      }
      if (isBinary(bytes)) {
        return "application/octet-stream";
      }
    }
    finally {
      IOUtils.closeQuietly(contentAsStream); } IOUtils.closeQuietly(contentAsStream);
    
    return "text/plain";
  }
  



  private static boolean isBinary(byte[] bytes)
  {
    byte[] arrayOfByte = bytes;int j = bytes.length; for (int i = 0; i < j; i++) { byte b = arrayOfByte[i];
      if ((b < 8) || 
        (b == 11) || 
        ((b >= 14) && (b <= 26)) || (
        (b >= 28) && (b <= 31))) {
        return true;
      }
    }
    return false;
  }
  
  private static boolean startsWith(byte[] bytes, byte[] lookFor) {
    if (bytes.length < lookFor.length) {
      return false;
    }
    
    for (int i = 0; i < lookFor.length; i++) {
      if (bytes[i] != lookFor[i]) {
        return false;
      }
    }
    
    return true;
  }
  
  private static byte[] read(InputStream stream, int maxNb) throws IOException {
    byte[] buffer = new byte[maxNb];
    int nbRead = stream.read(buffer);
    if (nbRead == buffer.length) {
      return buffer;
    }
    return ArrayUtils.subarray(buffer, 0, nbRead);
  }
  






  protected HtmlPage createHtmlPage(WebResponse webResponse, WebWindow webWindow)
    throws IOException
  {
    return HTMLParser.parseHtml(webResponse, webWindow);
  }
  






  protected XHtmlPage createXHtmlPage(WebResponse webResponse, WebWindow webWindow)
    throws IOException
  {
    return HTMLParser.parseXHtml(webResponse, webWindow);
  }
  






  protected TextPage createTextPage(WebResponse webResponse, WebWindow webWindow)
  {
    TextPage newPage = new TextPage(webResponse, webWindow);
    webWindow.setEnclosedPage(newPage);
    return newPage;
  }
  






  protected UnexpectedPage createUnexpectedPage(WebResponse webResponse, WebWindow webWindow)
  {
    UnexpectedPage newPage = new UnexpectedPage(webResponse, webWindow);
    webWindow.setEnclosedPage(newPage);
    return newPage;
  }
  






  protected SgmlPage createXmlPage(WebResponse webResponse, WebWindow webWindow)
    throws IOException
  {
    SgmlPage page = new XmlPage(webResponse, webWindow);
    webWindow.setEnclosedPage(page);
    return page;
  }
}
