package com.gargoylesoftware.htmlunit.javascript.host.dom;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;

@JsxClass
public class NodeFilter
  extends SimpleScriptable
{
  @JsxConstant
  public static final short FILTER_ACCEPT = 1;
  @JsxConstant
  public static final short FILTER_REJECT = 2;
  @JsxConstant
  public static final short FILTER_SKIP = 3;
  @JsxConstant
  public static final long SHOW_ALL = 4294967295L;
  @JsxConstant
  public static final int SHOW_ELEMENT = 1;
  @JsxConstant
  public static final int SHOW_ATTRIBUTE = 2;
  @JsxConstant
  public static final int SHOW_TEXT = 4;
  @JsxConstant
  public static final int SHOW_CDATA_SECTION = 8;
  @JsxConstant
  public static final int SHOW_ENTITY_REFERENCE = 16;
  @JsxConstant
  public static final int SHOW_ENTITY = 32;
  @JsxConstant
  public static final int SHOW_PROCESSING_INSTRUCTION = 64;
  @JsxConstant
  public static final int SHOW_COMMENT = 128;
  @JsxConstant
  public static final int SHOW_DOCUMENT = 256;
  @JsxConstant
  public static final int SHOW_DOCUMENT_TYPE = 512;
  @JsxConstant
  public static final int SHOW_DOCUMENT_FRAGMENT = 1024;
  @JsxConstant
  public static final int SHOW_NOTATION = 2048;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public NodeFilter() {}
}
