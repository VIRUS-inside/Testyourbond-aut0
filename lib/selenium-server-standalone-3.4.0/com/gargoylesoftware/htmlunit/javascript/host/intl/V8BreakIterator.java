package com.gargoylesoftware.htmlunit.javascript.host.intl;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import java.text.BreakIterator;
import java.util.Locale;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.NativeArray;
import net.sourceforge.htmlunit.corejs.javascript.NativeObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

































@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
public class V8BreakIterator
  extends SimpleScriptable
{
  private transient BreakIterator breakIterator_;
  private String text_;
  private boolean typeAlwaysNone_;
  
  public V8BreakIterator() {}
  
  @JsxConstructor
  public V8BreakIterator(Object locales, Object types)
  {
    Locale locale = new Locale("en", "US");
    if ((locales instanceof NativeArray)) {
      if (((NativeArray)locales).getLength() != 0L) {
        locale = new Locale(((NativeArray)locales).get(0).toString());
      }
    }
    else if ((locales instanceof String)) {
      locale = new Locale(locales.toString());
    }
    else if (locales != Undefined.instance) {
      throw Context.throwAsScriptRuntimeEx(new Exception("Unknown type " + locales.getClass().getName()));
    }
    
    if ((types instanceof NativeObject)) {
      Object obj = ((NativeObject)types).get("type", (NativeObject)types);
      if ("character".equals(obj)) {
        breakIterator_ = BreakIterator.getCharacterInstance(locale);
        typeAlwaysNone_ = true;
      }
      else if ("line".equals(obj)) {
        breakIterator_ = BreakIterator.getLineInstance(locale);
        typeAlwaysNone_ = true;
      }
      else if ("sentence".equals(obj)) {
        breakIterator_ = BreakIterator.getSentenceInstance(locale);
        typeAlwaysNone_ = true;
      }
    }
    if (breakIterator_ == null) {
      breakIterator_ = BreakIterator.getWordInstance(locale);
    }
  }
  



  public Object getDefaultValue(Class<?> hint)
  {
    return getClassName();
  }
  



  @JsxFunction
  public Object resolvedOptions()
  {
    return Context.getCurrentContext().evaluateString(getParentScope(), 
      "var x = {locale: 'en-US'}; x", "", -1, null);
  }
  



  @JsxFunction
  public int first()
  {
    return breakIterator_.first();
  }
  



  @JsxFunction
  public int next()
  {
    return breakIterator_.next();
  }
  



  @JsxFunction
  public int current()
  {
    return breakIterator_.current();
  }
  



  @JsxFunction
  public void adoptText(String text)
  {
    text_ = text;
    breakIterator_.setText(text);
  }
  



  @JsxFunction
  public String breakType()
  {
    if (!typeAlwaysNone_) {
      int current = current();
      int previous = breakIterator_.previous();
      if (previous == -1) {
        first();
      }
      else {
        next();
      }
      if ((current != -1) && (previous != -1)) {
        String token = text_.substring(previous, current);
        if (token.matches(".*[a-zA-Z]+.*")) {
          return "letter";
        }
        if (token.matches("[0-9]+")) {
          return "number";
        }
      }
    }
    return "none";
  }
}
