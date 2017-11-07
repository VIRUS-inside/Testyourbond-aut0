package com.gargoylesoftware.htmlunit.javascript.host.xml;

import com.gargoylesoftware.htmlunit.FormEncodingType;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.host.file.File;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLFormElement;
import com.gargoylesoftware.htmlunit.util.KeyDataPair;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import org.apache.commons.lang3.StringUtils;



























@JsxClass
public class FormData
  extends SimpleScriptable
{
  private final List<NameValuePair> requestParameters_ = new ArrayList();
  




  public FormData() {}
  



  @JsxConstructor
  public FormData(Object formObj)
  {
    if ((formObj instanceof HTMLFormElement)) {
      HTMLFormElement form = (HTMLFormElement)formObj;
      requestParameters_.addAll(form.getHtmlForm().getParameterListForSubmit(null));
    }
  }
  






  @JsxFunction
  public void append(String name, Object value, Object filename)
  {
    if ((value instanceof File)) {
      File file = (File)value;
      String fileName = null;
      String contentType = null;
      if ((filename instanceof String)) {
        fileName = (String)filename;
      }
      contentType = file.getType();
      if (StringUtils.isEmpty(contentType)) {
        contentType = "application/octet-stream";
      }
      requestParameters_.add(new KeyDataPair(name, file.getFile(), fileName, contentType, null));
    }
    else {
      requestParameters_.add(new NameValuePair(name, Context.toString(value)));
    }
  }
  



  @JsxFunction(functionName="delete", value={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public void delete_js(String name)
  {
    if (StringUtils.isEmpty(name)) {
      return;
    }
    
    Iterator<NameValuePair> iter = requestParameters_.iterator();
    while (iter.hasNext()) {
      NameValuePair pair = (NameValuePair)iter.next();
      if (name.equals(pair.getName())) {
        iter.remove();
      }
    }
  }
  



  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public String get(String name)
  {
    if (StringUtils.isEmpty(name)) {
      return null;
    }
    
    Iterator<NameValuePair> iter = requestParameters_.iterator();
    while (iter.hasNext()) {
      NameValuePair pair = (NameValuePair)iter.next();
      if (name.equals(pair.getName())) {
        return pair.getValue();
      }
    }
    return null;
  }
  



  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public Scriptable getAll(String name)
  {
    if (StringUtils.isEmpty(name)) {
      return Context.getCurrentContext().newArray(this, 0);
    }
    
    List<Object> values = new ArrayList();
    Iterator<NameValuePair> iter = requestParameters_.iterator();
    while (iter.hasNext()) {
      NameValuePair pair = (NameValuePair)iter.next();
      if (name.equals(pair.getName())) {
        values.add(pair.getValue());
      }
    }
    
    Object[] stringValues = values.toArray(new Object[values.size()]);
    return Context.getCurrentContext().newArray(this, stringValues);
  }
  



  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public boolean has(String name)
  {
    if (StringUtils.isEmpty(name)) {
      return false;
    }
    
    Iterator<NameValuePair> iter = requestParameters_.iterator();
    while (iter.hasNext()) {
      NameValuePair pair = (NameValuePair)iter.next();
      if (name.equals(pair.getName())) {
        return true;
      }
    }
    return false;
  }
  






  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public void set(String name, Object value, Object filename)
  {
    if (StringUtils.isEmpty(name)) {
      return;
    }
    
    int pos = -1;
    
    Iterator<NameValuePair> iter = requestParameters_.iterator();
    int idx = 0;
    while (iter.hasNext()) {
      NameValuePair pair = (NameValuePair)iter.next();
      if (name.equals(pair.getName())) {
        iter.remove();
        if (pos < 0) {
          pos = idx;
        }
      }
      idx++;
    }
    
    if (pos < 0) {
      pos = requestParameters_.size();
    }
    
    if ((value instanceof File)) {
      File file = (File)value;
      String fileName = null;
      if ((filename instanceof String)) {
        fileName = (String)filename;
      }
      requestParameters_.add(pos, 
        new KeyDataPair(name, file.getFile(), fileName, file.getType(), null));
    }
    else {
      requestParameters_.add(pos, new NameValuePair(name, Context.toString(value)));
    }
  }
  



  public void fillRequest(WebRequest webRequest)
  {
    webRequest.setEncodingType(FormEncodingType.MULTIPART);
    webRequest.setRequestParameters(requestParameters_);
  }
}
