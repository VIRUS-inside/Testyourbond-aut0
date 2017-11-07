package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import com.gargoylesoftware.htmlunit.html.DomCharacterData;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import net.sourceforge.htmlunit.corejs.javascript.Context;




























@JsxClass(domClass=DomCharacterData.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
public class XMLDOMCharacterData
  extends XMLDOMNode
{
  public XMLDOMCharacterData() {}
  
  @JsxGetter
  public Object getData()
  {
    DomCharacterData domCharacterData = getDomNodeOrDie();
    return domCharacterData.getData();
  }
  



  @JsxSetter
  public void setData(String newData)
  {
    if ((newData == null) || ("null".equals(newData))) {
      throw Context.reportRuntimeError("Type mismatch.");
    }
    
    DomCharacterData domCharacterData = getDomNodeOrDie();
    domCharacterData.setData(newData);
  }
  



  @JsxGetter
  public int getLength()
  {
    DomCharacterData domCharacterData = getDomNodeOrDie();
    return domCharacterData.getLength();
  }
  




  public void setText(Object newText)
  {
    setData(newText == null ? null : Context.toString(newText));
  }
  



  public Object getXml()
  {
    Object xml = super.getXml();
    if ((xml instanceof String)) {
      String xmlString = (String)xml;
      if (xmlString.indexOf('\n') >= 0) {
        xml = xmlString.replaceAll("([^\r])\n", "$1\r\n");
      }
    }
    return xml;
  }
  



  @JsxFunction
  public void appendData(String data)
  {
    if ((data == null) || ("null".equals(data))) {
      throw Context.reportRuntimeError("Type mismatch.");
    }
    
    DomCharacterData domCharacterData = getDomNodeOrDie();
    domCharacterData.appendData(data);
  }
  




  @JsxFunction
  public void deleteData(int offset, int count)
  {
    if (offset < 0) {
      throw Context.reportRuntimeError("The offset must be 0 or a positive number that is not greater than the number of characters in the data.");
    }
    
    if (count < 0) {
      throw Context.reportRuntimeError("The offset must be 0 or a positive number that is not greater than the number of characters in the data.");
    }
    
    if (count == 0) {
      return;
    }
    
    DomCharacterData domCharacterData = getDomNodeOrDie();
    if (offset > domCharacterData.getLength()) {
      throw Context.reportRuntimeError("The offset must be 0 or a positive number that is not greater than the number of characters in the data.");
    }
    

    domCharacterData.deleteData(offset, count);
  }
  




  @JsxFunction
  public void insertData(int offset, String data)
  {
    if ((data == null) || ("null".equals(data))) {
      throw Context.reportRuntimeError("Type mismatch.");
    }
    if (data.isEmpty()) {
      return;
    }
    if (offset < 0) {
      throw Context.reportRuntimeError("The offset must be 0 or a positive number that is not greater than the number of characters in the data.");
    }
    
    DomCharacterData domCharacterData = getDomNodeOrDie();
    if (offset > domCharacterData.getLength()) {
      throw Context.reportRuntimeError("The offset must be 0 or a positive number that is not greater than the number of characters in the data.");
    }
    

    domCharacterData.insertData(offset, data);
  }
  





  @JsxFunction
  public void replaceData(int offset, int count, String data)
  {
    if (offset < 0) {
      throw Context.reportRuntimeError("The offset must be 0 or a positive number that is not greater than the number of characters in the data.");
    }
    
    if (count < 0) {
      throw Context.reportRuntimeError("The offset must be 0 or a positive number that is not greater than the number of characters in the data.");
    }
    
    if ((data == null) || ("null".equals(data))) {
      throw Context.reportRuntimeError("Type mismatch.");
    }
    
    DomCharacterData domCharacterData = getDomNodeOrDie();
    if (offset > domCharacterData.getLength()) {
      throw Context.reportRuntimeError("The offset must be 0 or a positive number that is not greater than the number of characters in the data.");
    }
    

    domCharacterData.replaceData(offset, count, data);
  }
  






  @JsxFunction
  public String substringData(int offset, int count)
  {
    if (offset < 0) {
      throw Context.reportRuntimeError("The offset must be 0 or a positive number that is not greater than the number of characters in the data.");
    }
    
    if (count < 0) {
      throw Context.reportRuntimeError("The offset must be 0 or a positive number that is not greater than the number of characters in the data.");
    }
    

    DomCharacterData domCharacterData = getDomNodeOrDie();
    if (offset > domCharacterData.getLength()) {
      throw Context.reportRuntimeError("The offset must be 0 or a positive number that is not greater than the number of characters in the data.");
    }
    

    return domCharacterData.substringData(offset, count);
  }
  



  public DomCharacterData getDomNodeOrDie()
  {
    return (DomCharacterData)super.getDomNodeOrDie();
  }
}
