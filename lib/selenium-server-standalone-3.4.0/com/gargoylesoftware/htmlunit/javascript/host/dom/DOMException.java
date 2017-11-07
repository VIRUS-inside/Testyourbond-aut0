package com.gargoylesoftware.htmlunit.javascript.host.dom;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;



























































@JsxClass
public class DOMException
  extends SimpleScriptable
{
  @JsxConstant
  public static final short DOMSTRING_SIZE_ERR = 2;
  @JsxConstant
  public static final short HIERARCHY_REQUEST_ERR = 3;
  @JsxConstant
  public static final short INDEX_SIZE_ERR = 1;
  @JsxConstant
  public static final short INUSE_ATTRIBUTE_ERR = 10;
  @JsxConstant
  public static final short INVALID_ACCESS_ERR = 15;
  @JsxConstant
  public static final short INVALID_CHARACTER_ERR = 5;
  @JsxConstant
  public static final short INVALID_MODIFICATION_ERR = 13;
  @JsxConstant
  public static final short INVALID_STATE_ERR = 11;
  @JsxConstant
  public static final short NAMESPACE_ERR = 14;
  @JsxConstant
  public static final short NO_DATA_ALLOWED_ERR = 6;
  @JsxConstant
  public static final short NO_MODIFICATION_ALLOWED_ERR = 7;
  @JsxConstant
  public static final short NOT_FOUND_ERR = 8;
  @JsxConstant
  public static final short NOT_SUPPORTED_ERR = 9;
  @JsxConstant
  public static final short SYNTAX_ERR = 12;
  @JsxConstant
  public static final short WRONG_DOCUMENT_ERR = 4;
  @JsxConstant
  public static final short VALIDATION_ERR = 16;
  @JsxConstant
  public static final short TYPE_MISMATCH_ERR = 17;
  @JsxConstant
  public static final short SECURITY_ERR = 18;
  @JsxConstant
  public static final short NETWORK_ERR = 19;
  @JsxConstant
  public static final short ABORT_ERR = 20;
  @JsxConstant
  public static final short URL_MISMATCH_ERR = 21;
  @JsxConstant
  public static final short QUOTA_EXCEEDED_ERR = 22;
  @JsxConstant
  public static final short TIMEOUT_ERR = 23;
  @JsxConstant
  public static final short INVALID_NODE_TYPE_ERR = 24;
  @JsxConstant
  public static final short DATA_CLONE_ERR = 25;
  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public static final short PARSE_ERR = 81;
  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public static final short SERIALIZE_ERR = 82;
  private final short code_;
  private final String message_;
  private int lineNumber_;
  private String fileName_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public DOMException()
  {
    code_ = -1;
    message_ = null;
  }
  




  public DOMException(String message, short errorCode)
  {
    code_ = errorCode;
    message_ = message;
  }
  



  @JsxGetter
  public Object getCode()
  {
    if (code_ == -1) {
      return Undefined.instance;
    }
    return Short.valueOf(code_);
  }
  



  @JsxGetter
  public Object getMessage()
  {
    if (message_ == null) {
      return Undefined.instance;
    }
    return message_;
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public Object getLineNumber()
  {
    if (lineNumber_ == -1) {
      return Undefined.instance;
    }
    return Integer.valueOf(lineNumber_);
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public Object getFilename()
  {
    if (fileName_ == null) {
      return Undefined.instance;
    }
    return fileName_;
  }
  




  public void setLocation(String fileName, int lineNumber)
  {
    fileName_ = fileName;
    lineNumber_ = lineNumber;
  }
}
