package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.dom.AbstractList;
import net.sourceforge.htmlunit.corejs.javascript.Context;












































@JsxClass(domClass=HtmlTextArea.class)
public class HTMLTextAreaElement
  extends FormField
{
  private AbstractList labels_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLTextAreaElement() {}
  
  @JsxGetter
  public String getType()
  {
    return "textarea";
  }
  




  public String getValue()
  {
    return ((HtmlTextArea)getDomNodeOrDie()).getText();
  }
  




  public void setValue(Object value)
  {
    if ((value == null) && (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_TEXT_AREA_SET_VALUE_NULL))) {
      ((HtmlTextArea)getDomNodeOrDie()).setText("");
      return;
    }
    
    ((HtmlTextArea)getDomNodeOrDie()).setText(Context.toString(value));
  }
  



  @JsxGetter
  public int getCols()
  {
    String s = getDomNodeOrDie().getAttribute("cols");
    try {
      return Integer.parseInt(s);
    }
    catch (NumberFormatException e) {}
    return 20;
  }
  



  @JsxSetter
  public void setCols(String cols)
  {
    try
    {
      int i = Float.valueOf(cols).intValue();
      if (i < 0) {
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_TEXT_AREA_SET_COLS_NEGATIVE_THROWS_EXCEPTION)) {
          throw new NumberFormatException("New value for cols '" + cols + "' is smaller than zero.");
        }
        getDomNodeOrDie().setAttribute("cols", null);
        return;
      }
      getDomNodeOrDie().setAttribute("cols", Integer.toString(i));
    }
    catch (NumberFormatException e) {
      if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_TEXT_AREA_SET_COLS_THROWS_EXCEPTION)) {
        throw Context.throwAsScriptRuntimeEx(e);
      }
      getDomNodeOrDie().setAttribute("cols", "20");
    }
  }
  



  @JsxGetter
  public int getRows()
  {
    String s = getDomNodeOrDie().getAttribute("rows");
    try {
      return Integer.parseInt(s);
    }
    catch (NumberFormatException e) {}
    return 2;
  }
  



  @JsxSetter
  public void setRows(String rows)
  {
    try
    {
      int i = new Float(rows).intValue();
      if (i < 0) {
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_TEXT_AREA_SET_ROWS_NEGATIVE_THROWS_EXCEPTION)) {
          throw new NumberFormatException("New value for rows '" + rows + "' is smaller than zero.");
        }
        getDomNodeOrDie().setAttribute("rows", null);
        return;
      }
      getDomNodeOrDie().setAttribute("rows", Integer.toString(i));
    }
    catch (NumberFormatException e) {
      if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_TEXT_AREA_SET_ROWS_THROWS_EXCEPTION)) {
        throw Context.throwAsScriptRuntimeEx(e);
      }
      
      getDomNodeOrDie().setAttribute("rows", "2");
    }
  }
  




  @JsxGetter
  public String getDefaultValue()
  {
    return ((HtmlTextArea)getDomNodeOrDie()).getDefaultValue();
  }
  




  @JsxSetter
  public void setDefaultValue(String defaultValue)
  {
    ((HtmlTextArea)getDomNodeOrDie()).setDefaultValue(defaultValue);
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public int getTextLength()
  {
    return getValue().length();
  }
  



  @JsxGetter
  public int getSelectionStart()
  {
    return ((HtmlTextArea)getDomNodeOrDie()).getSelectionStart();
  }
  



  @JsxSetter
  public void setSelectionStart(int start)
  {
    ((HtmlTextArea)getDomNodeOrDie()).setSelectionStart(start);
  }
  



  @JsxGetter
  public int getSelectionEnd()
  {
    return ((HtmlTextArea)getDomNodeOrDie()).getSelectionEnd();
  }
  



  @JsxSetter
  public void setSelectionEnd(int end)
  {
    ((HtmlTextArea)getDomNodeOrDie()).setSelectionEnd(end);
  }
  




  @JsxFunction
  public void setSelectionRange(int start, int end)
  {
    setSelectionStart(start);
    setSelectionEnd(end);
  }
  


  @JsxFunction
  public void select()
  {
    ((HtmlTextArea)getDomNodeOrDie()).select();
  }
  



  @JsxGetter
  public boolean getReadOnly()
  {
    return ((HtmlTextArea)getDomNodeOrDie()).isReadOnly();
  }
  



  @JsxSetter
  public void setReadOnly(boolean readOnly)
  {
    ((HtmlTextArea)getDomNodeOrDie()).setReadOnly(readOnly);
  }
  



  @JsxGetter
  public Object getMaxLength()
  {
    String maxLength = getDomNodeOrDie().getAttribute("maxLength");
    try
    {
      return Integer.valueOf(Integer.parseInt(maxLength));
    }
    catch (NumberFormatException e) {
      if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_TEXT_AREA_GET_MAXLENGTH_MAX_INT))
        return Integer.valueOf(Integer.MAX_VALUE);
    }
    return Integer.valueOf(-1);
  }
  



  @JsxSetter
  public void setMaxLength(String maxLength)
  {
    try
    {
      int i = Integer.parseInt(maxLength);
      
      if ((i < 0) && (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_TEXT_AREA_SET_MAXLENGTH_NEGATIVE_THROWS_EXCEPTION))) {
        throw Context.throwAsScriptRuntimeEx(
          new NumberFormatException("New value for maxLength '" + maxLength + "' is smaller than zero."));
      }
      getDomNodeOrDie().setAttribute("maxLength", maxLength);
    }
    catch (NumberFormatException e) {
      getDomNodeOrDie().setAttribute("maxLength", "0");
      return;
    }
  }
  



  @JsxGetter
  public String getPlaceholder()
  {
    return ((HtmlTextArea)getDomNodeOrDie()).getPlaceholder();
  }
  



  @JsxSetter
  public void setPlaceholder(String placeholder)
  {
    ((HtmlTextArea)getDomNodeOrDie()).setPlaceholder(placeholder);
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public AbstractList getLabels()
  {
    if (labels_ == null) {
      labels_ = new LabelsHelper(getDomNodeOrDie());
    }
    return labels_;
  }
}
