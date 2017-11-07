package com.gargoylesoftware.htmlunit.javascript.host.dom;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.DomCharacterData;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import net.sourceforge.htmlunit.corejs.javascript.Context;
































@JsxClass
public class CharacterData
  extends Node
{
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public CharacterData() {}
  
  @JsxGetter
  public Object getData()
  {
    DomCharacterData domCharacterData = (DomCharacterData)getDomNodeOrDie();
    return domCharacterData.getData();
  }
  



  @JsxSetter
  public void setData(String newValue)
  {
    DomCharacterData domCharacterData = (DomCharacterData)getDomNodeOrDie();
    domCharacterData.setData(newValue);
  }
  



  @JsxGetter
  public int getLength()
  {
    DomCharacterData domCharacterData = (DomCharacterData)getDomNodeOrDie();
    return domCharacterData.getLength();
  }
  



  @JsxFunction
  public void appendData(String arg)
  {
    DomCharacterData domCharacterData = (DomCharacterData)getDomNodeOrDie();
    domCharacterData.appendData(arg);
  }
  




  @JsxFunction
  public void deleteData(int offset, int count)
  {
    if (offset < 0) {
      throw Context.reportRuntimeError("Provided offset: " + offset + " is less than zero.");
    }
    
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOM_CDATA_DELETE_THROWS_NEGATIVE_COUNT)) {
      if (count < 0) {
        throw Context.reportRuntimeError("Provided count: " + count + " is less than zero.");
      }
      if (count == 0) {
        return;
      }
    }
    
    DomCharacterData domCharacterData = (DomCharacterData)getDomNodeOrDie();
    if (offset > domCharacterData.getLength()) {
      throw Context.reportRuntimeError("Provided offset: " + offset + " is greater than length.");
    }
    
    domCharacterData.deleteData(offset, count);
  }
  





  @JsxFunction
  public void insertData(int offset, String arg)
  {
    DomCharacterData domCharacterData = (DomCharacterData)getDomNodeOrDie();
    domCharacterData.insertData(offset, arg);
  }
  







  @JsxFunction
  public void replaceData(int offset, int count, String arg)
  {
    DomCharacterData domCharacterData = (DomCharacterData)getDomNodeOrDie();
    domCharacterData.replaceData(offset, count, arg);
  }
  






  @JsxFunction
  public String substringData(int offset, int count)
  {
    DomCharacterData domCharacterData = (DomCharacterData)getDomNodeOrDie();
    return domCharacterData.substringData(offset, count);
  }
}
