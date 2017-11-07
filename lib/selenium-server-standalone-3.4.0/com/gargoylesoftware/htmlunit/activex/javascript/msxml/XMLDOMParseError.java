package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;


























@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
public class XMLDOMParseError
  extends MSXMLScriptable
{
  private int errorCode_;
  private int filepos_;
  private int line_;
  private int linepos_;
  private String reason_ = "";
  private String srcText_ = "";
  private String url_ = "";
  

  public XMLDOMParseError() {}
  
  @JsxGetter
  public int getErrorCode()
  {
    return errorCode_;
  }
  



  @JsxGetter
  public int getFilepos()
  {
    return filepos_;
  }
  



  @JsxGetter
  public int getLine()
  {
    return line_;
  }
  



  @JsxGetter
  public int getLinepos()
  {
    return linepos_;
  }
  



  @JsxGetter
  public String getReason()
  {
    return reason_;
  }
  



  @JsxGetter
  public String getSrcText()
  {
    return srcText_;
  }
  



  @JsxGetter
  public String getUrl()
  {
    return url_;
  }
  
  void setErrorCode(int errorCode) {
    errorCode_ = errorCode;
  }
  
  void setFilepos(int filepos) {
    filepos_ = filepos;
  }
  
  void setLine(int line) {
    line_ = line;
  }
  
  void setLinepos(int linepos) {
    linepos_ = linepos;
  }
  
  void setReason(String reason) {
    reason_ = reason;
  }
  
  void setSrcText(String srcText) {
    srcText_ = srcText;
  }
  
  void setUrl(String url) {
    url_ = url;
  }
}
