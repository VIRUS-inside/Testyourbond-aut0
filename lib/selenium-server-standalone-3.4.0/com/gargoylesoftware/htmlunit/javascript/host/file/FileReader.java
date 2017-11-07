package com.gargoylesoftware.htmlunit.javascript.host.file;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget;

@JsxClass
public class FileReader
  extends EventTarget
{
  @JsxConstant
  public static final short EMPTY = 0;
  @JsxConstant
  public static final short LOADING = 1;
  @JsxConstant
  public static final short DONE = 2;
  
  @JsxConstructor
  public FileReader() {}
}
