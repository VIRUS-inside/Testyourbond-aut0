package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlFileInput;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.html.InputElementFactory;
import com.gargoylesoftware.htmlunit.html.impl.SelectableTextInput;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.dom.AbstractList;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.MouseEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.PointerEvent;
import com.gargoylesoftware.htmlunit.javascript.host.file.FileList;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.xml.sax.helpers.AttributesImpl;

@JsxClass(domClass=HtmlInput.class)
public class HTMLInputElement
  extends FormField
{
  private AbstractList labels_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLInputElement() {}
  
  /* Error */
  @JsxGetter
  public String getType()
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 27	com/gargoylesoftware/htmlunit/javascript/host/html/HTMLInputElement:getDomNodeOrDie	()Lcom/gargoylesoftware/htmlunit/html/HtmlInput;
    //   4: invokevirtual 31	com/gargoylesoftware/htmlunit/html/HtmlInput:getTypeAttribute	()Ljava/lang/String;
    //   7: astore_1
    //   8: aload_0
    //   9: invokevirtual 36	com/gargoylesoftware/htmlunit/javascript/host/html/HTMLInputElement:getBrowserVersion	()Lcom/gargoylesoftware/htmlunit/BrowserVersion;
    //   12: astore_2
    //   13: aload_1
    //   14: getstatic 40	java/util/Locale:ROOT	Ljava/util/Locale;
    //   17: invokevirtual 46	java/lang/String:toLowerCase	(Ljava/util/Locale;)Ljava/lang/String;
    //   20: astore_1
    //   21: aload_1
    //   22: invokestatic 52	com/gargoylesoftware/htmlunit/html/InputElementFactory:isSupported	(Ljava/lang/String;)Z
    //   25: ifne +9 -> 34
    //   28: ldc 58
    //   30: astore_1
    //   31: goto +132 -> 163
    //   34: aload_2
    //   35: getstatic 60	com/gargoylesoftware/htmlunit/BrowserVersionFeatures:JS_INPUT_SET_VALUE_DATE_SUPPORTED	Lcom/gargoylesoftware/htmlunit/BrowserVersionFeatures;
    //   38: invokevirtual 66	com/gargoylesoftware/htmlunit/BrowserVersion:hasFeature	(Lcom/gargoylesoftware/htmlunit/BrowserVersionFeatures;)Z
    //   41: ifne +122 -> 163
    //   44: aload_1
    //   45: dup
    //   46: astore_3
    //   47: invokevirtual 72	java/lang/String:hashCode	()I
    //   50: lookupswitch	default:+113->163, -1023416679:+50->100, 3076014:+62->112, 3560141:+74->124, 3645428:+86->136, 104080000:+98->148
    //   100: aload_3
    //   101: ldc 76
    //   103: invokevirtual 78	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   106: ifne +54 -> 160
    //   109: goto +54 -> 163
    //   112: aload_3
    //   113: ldc 82
    //   115: invokevirtual 78	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   118: ifne +42 -> 160
    //   121: goto +42 -> 163
    //   124: aload_3
    //   125: ldc 84
    //   127: invokevirtual 78	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   130: ifne +30 -> 160
    //   133: goto +30 -> 163
    //   136: aload_3
    //   137: ldc 86
    //   139: invokevirtual 78	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   142: ifne +18 -> 160
    //   145: goto +18 -> 163
    //   148: aload_3
    //   149: ldc 88
    //   151: invokevirtual 78	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   154: ifne +6 -> 160
    //   157: goto +6 -> 163
    //   160: ldc 58
    //   162: astore_1
    //   163: ldc 90
    //   165: aload_1
    //   166: invokevirtual 78	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   169: ifeq +16 -> 185
    //   172: aload_2
    //   173: getstatic 92	com/gargoylesoftware/htmlunit/BrowserVersionFeatures:HTMLINPUT_FILES_UNDEFINED	Lcom/gargoylesoftware/htmlunit/BrowserVersionFeatures;
    //   176: invokevirtual 66	com/gargoylesoftware/htmlunit/BrowserVersion:hasFeature	(Lcom/gargoylesoftware/htmlunit/BrowserVersionFeatures;)Z
    //   179: ifeq +6 -> 185
    //   182: ldc 58
    //   184: astore_1
    //   185: aload_1
    //   186: areturn
    // Line number table:
    //   Java source line #95	-> byte code offset #0
    //   Java source line #96	-> byte code offset #8
    //   Java source line #97	-> byte code offset #13
    //   Java source line #98	-> byte code offset #21
    //   Java source line #99	-> byte code offset #28
    //   Java source line #100	-> byte code offset #31
    //   Java source line #101	-> byte code offset #34
    //   Java source line #102	-> byte code offset #44
    //   Java source line #108	-> byte code offset #160
    //   Java source line #114	-> byte code offset #163
    //   Java source line #115	-> byte code offset #182
    //   Java source line #117	-> byte code offset #185
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	187	0	this	HTMLInputElement
    //   7	179	1	type	String
    //   12	161	2	browserVersion	BrowserVersion
    //   46	103	3	str1	String
  }
  
  @JsxSetter
  public void setType(String newType)
  {
    HtmlInput input = getDomNodeOrDie();
    
    String currentType = input.getAttribute("type");
    
    if (!currentType.equalsIgnoreCase(newType)) {
      if ((newType != null) && (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_INPUT_SET_TYPE_LOWERCASE))) {
        newType = newType.toLowerCase(Locale.ROOT);
      }
      AttributesImpl attributes = readAttributes(input);
      int index = attributes.getIndex("type");
      if (index > -1) {
        attributes.setValue(index, newType);
      }
      else {
        attributes.addAttribute(null, "type", "type", null, newType);
      }
      

      if ((DomElement.ATTRIBUTE_NOT_DEFINED != currentType) || (!"text".equalsIgnoreCase(newType))) {
        HtmlInput newInput = (HtmlInput)InputElementFactory.instance
          .createElement(input.getPage(), "input", attributes);
        
        if (input.wasCreatedByJavascript()) {
          newInput.markAsCreatedByJavascript();
        }
        
        if (input.getParentNode() == null)
        {


          input = newInput;
        }
        else {
          input.getParentNode().replaceChild(newInput, input);
        }
        
        input.setScriptableObject(null);
        setDomNode(newInput, true);
      }
      else {
        super.setAttribute("type", newType);
      }
    }
  }
  





  public void setValue(Object newValue)
  {
    if (newValue == null) {
      getDomNodeOrDie().setValueAttribute("");
      return;
    }
    
    String val = Context.toString(newValue);
    BrowserVersion browserVersion = getBrowserVersion();
    if ((StringUtils.isNotEmpty(val)) && ("file".equalsIgnoreCase(getType()))) {
      if (browserVersion.hasFeature(BrowserVersionFeatures.JS_SELECT_FILE_THROWS)) {
        throw Context.reportRuntimeError("InvalidStateError: Failed to set the 'value' property on 'HTMLInputElement'.");
      }
      
      return;
    }
    
    getDomNodeOrDie().setValueAttribute(val);
  }
  







  @JsxSetter
  public void setChecked(boolean checked)
  {
    getDomNodeOrDie().setChecked(checked);
  }
  



  public HtmlInput getDomNodeOrDie()
  {
    return (HtmlInput)super.getDomNodeOrDie();
  }
  







  @JsxGetter
  public boolean getChecked()
  {
    return getDomNodeOrDie().isChecked();
  }
  


  @JsxFunction
  public void select()
  {
    HtmlInput input = getDomNodeOrDie();
    if ((input instanceof HtmlTextInput)) {
      ((HtmlTextInput)input).select();
    }
  }
  






  public void setAttribute(String name, String value)
  {
    if ("type".equalsIgnoreCase(name)) {
      setType(value);
      return;
    }
    if ("value".equalsIgnoreCase(name)) {
      setDefaultValue(value);
    }
    else {
      super.setAttribute(name, value);
    }
  }
  




  @JsxGetter
  public String getDefaultValue()
  {
    return getDomNodeOrDie().getDefaultValue();
  }
  




  @JsxSetter
  public void setDefaultValue(String defaultValue)
  {
    getDomNodeOrDie().setDefaultValue(defaultValue);
  }
  




  @JsxGetter
  public boolean getDefaultChecked()
  {
    return getDomNodeOrDie().isDefaultChecked();
  }
  




  @JsxSetter
  public void setDefaultChecked(boolean defaultChecked)
  {
    getDomNodeOrDie().setDefaultChecked(defaultChecked);
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public int getTextLength()
  {
    return getValue().length();
  }
  



  @JsxGetter
  public int getSelectionStart()
  {
    DomNode dom = getDomNodeOrDie();
    if ((dom instanceof SelectableTextInput)) {
      if (("number".equalsIgnoreCase(getType())) && (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_INPUT_NUMBER_NO_SELECTION))) {
        throw Context.reportRuntimeError("Failed to read the 'selectionStart' propertyfrom 'HTMLInputElement': The input element's type ('number') does not support selection.");
      }
      


      return ((SelectableTextInput)dom).getSelectionStart();
    }
    
    throw Context.reportRuntimeError("Failed to read the 'selectionStart' property from 'HTMLInputElement': The input element's type (" + 
      getType() + ") does not support selection.");
  }
  



  @JsxSetter
  public void setSelectionStart(int start)
  {
    DomNode dom = getDomNodeOrDie();
    if ((dom instanceof SelectableTextInput)) {
      if (("number".equalsIgnoreCase(getType())) && (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_INPUT_NUMBER_NO_SELECTION))) {
        throw Context.reportRuntimeError("Failed to set the 'selectionStart' propertyfrom 'HTMLInputElement': The input element's type ('number') does not support selection.");
      }
      


      ((SelectableTextInput)dom).setSelectionStart(start);
      return;
    }
    
    throw Context.reportRuntimeError("Failed to set the 'selectionStart' property from 'HTMLInputElement': The input element's type (" + 
      getType() + ") does not support selection.");
  }
  



  @JsxGetter
  public int getSelectionEnd()
  {
    DomNode dom = getDomNodeOrDie();
    if ((dom instanceof SelectableTextInput)) {
      if (("number".equalsIgnoreCase(getType())) && (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_INPUT_NUMBER_NO_SELECTION))) {
        throw Context.reportRuntimeError("Failed to read the 'selectionEnd' propertyfrom 'HTMLInputElement': The input element's type ('number') does not support selection.");
      }
      


      return ((SelectableTextInput)dom).getSelectionEnd();
    }
    
    throw Context.reportRuntimeError("Failed to read the 'selectionEnd' property from 'HTMLInputElement': The input element's type (" + 
      getType() + ") does not support selection.");
  }
  



  @JsxSetter
  public void setSelectionEnd(int end)
  {
    DomNode dom = getDomNodeOrDie();
    if ((dom instanceof SelectableTextInput)) {
      if (("number".equalsIgnoreCase(getType())) && (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_INPUT_NUMBER_NO_SELECTION))) {
        throw Context.reportRuntimeError("Failed to set the 'selectionEnd' propertyfrom 'HTMLInputElement': The input element's type ('number') does not support selection.");
      }
      


      ((SelectableTextInput)dom).setSelectionEnd(end);
      return;
    }
    
    throw Context.reportRuntimeError("Failed to set the 'selectionEnd' property from 'HTMLInputElement': The input element's type (" + 
      getType() + ") does not support selection.");
  }
  



  @JsxGetter
  public int getMaxLength()
  {
    String attrValue = getDomNodeOrDie().getAttribute("maxLength");
    return NumberUtils.toInt(attrValue, -1);
  }
  



  @JsxSetter
  public void setMaxLength(int length)
  {
    getDomNodeOrDie().setMaxLength(length);
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public int getMinLength()
  {
    String attrValue = getDomNodeOrDie().getAttribute("minLength");
    return NumberUtils.toInt(attrValue, -1);
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public void setMinLength(int length)
  {
    getDomNodeOrDie().setMinLength(length);
  }
  



  @JsxGetter
  public String getMin()
  {
    return getDomNodeOrDie().getAttribute("min");
  }
  



  @JsxSetter
  public void setMin(String min)
  {
    getDomNodeOrDie().setAttribute("min", min);
  }
  



  @JsxGetter
  public String getMax()
  {
    return getDomNodeOrDie().getAttribute("max");
  }
  



  @JsxSetter
  public void setMax(String max)
  {
    getDomNodeOrDie().setAttribute("max", max);
  }
  



  @JsxGetter
  public boolean getReadOnly()
  {
    return getDomNodeOrDie().isReadOnly();
  }
  



  @JsxSetter
  public void setReadOnly(boolean readOnly)
  {
    getDomNodeOrDie().setReadOnly(readOnly);
  }
  




  @JsxFunction
  public void setSelectionRange(int start, int end)
  {
    setSelectionStart(start);
    setSelectionEnd(end);
  }
  



  @JsxGetter
  public String getAlt()
  {
    return getDomNodeOrDie().getAttribute("alt");
  }
  



  @JsxSetter
  public void setAlt(String alt)
  {
    getDomNodeOrDie().setAttribute("alt", alt);
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public String getBorder()
  {
    return getDomNodeOrDie().getAttribute("border");
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setBorder(String border)
  {
    getDomNodeOrDie().setAttribute("border", border);
  }
  



  @JsxGetter
  public String getAlign()
  {
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_ALIGN_FOR_INPUT_IGNORES_VALUES)) {
      return "";
    }
    return getAlign(true);
  }
  



  @JsxSetter
  public void setAlign(String align)
  {
    boolean ignoreIfNoError = getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_ALIGN_FOR_INPUT_IGNORES_VALUES);
    setAlign(align, ignoreIfNoError);
  }
  



  @JsxGetter
  public String getSrc()
  {
    return getDomNodeOrDie().getSrcAttribute();
  }
  



  public String getValue()
  {
    HtmlInput htmlInput = getDomNodeOrDie();
    if ((htmlInput instanceof HtmlFileInput)) {
      File[] files = ((HtmlFileInput)getDomNodeOrDie()).getFiles();
      if ((files == null) || (files.length == 0)) {
        return DomElement.ATTRIBUTE_NOT_DEFINED;
      }
      File first = files[0];
      String name = first.getName();
      if (name.isEmpty()) {
        return name;
      }
      if (getBrowserVersion().hasFeature(BrowserVersionFeatures.HTMLINPUT_FILE_VALUE_FAKEPATH)) {
        return "C:\\fakepath\\" + name;
      }
      if (getBrowserVersion().hasFeature(BrowserVersionFeatures.HTMLINPUT_FILE_VALUE_NO_PATH)) {
        return name;
      }
      return first.getAbsolutePath();
    }
    return super.getValue();
  }
  



  public String getAttribute(String attributeName, Integer flags)
  {
    String superAttribute = super.getAttribute(attributeName, flags);
    if ("value".equalsIgnoreCase(attributeName)) {
      if (((superAttribute == null) || (!superAttribute.isEmpty())) && 
        (getDefaultValue().isEmpty())) {
        return null;
      }
      if (!"file".equals(getType())) {
        return getDefaultValue();
      }
    }
    return superAttribute;
  }
  


  public void click()
    throws IOException
  {
    HtmlInput domNode = getDomNodeOrDie();
    boolean originalState = domNode.isChecked();
    Event event;
    Event event; if (getBrowserVersion().hasFeature(BrowserVersionFeatures.EVENT_ONCLICK_USES_POINTEREVENT)) {
      event = new PointerEvent(domNode, "click", false, false, false, 0);
    }
    else {
      event = new MouseEvent(domNode, "click", false, false, false, 0);
    }
    domNode.click(event, true);
    
    boolean newState = domNode.isChecked();
    
    if ((originalState != newState) && (
      ((domNode instanceof HtmlRadioButtonInput)) || ((domNode instanceof HtmlCheckBoxInput)))) {
      domNode.fireEvent("change");
    }
  }
  



  protected boolean isEndTagForbidden()
  {
    return true;
  }
  



  @JsxGetter
  public boolean isRequired()
  {
    return getDomNodeOrDie().isRequired();
  }
  



  @JsxSetter
  public void setRequired(boolean required)
  {
    getDomNodeOrDie().setRequired(required);
  }
  



  @JsxGetter
  public String getSize()
  {
    return getDomNodeOrDie().getSize();
  }
  



  @JsxSetter
  public void setSize(String size)
  {
    getDomNodeOrDie().setSize(size);
  }
  



  @JsxGetter
  public String getAccept()
  {
    return getDomNodeOrDie().getAccept();
  }
  



  @JsxSetter
  public void setAccept(String accept)
  {
    getDomNodeOrDie().setAccept(accept);
  }
  



  @JsxGetter
  public String getAutocomplete()
  {
    return getDomNodeOrDie().getAutocomplete();
  }
  



  @JsxSetter
  public void setAutocomplete(String autocomplete)
  {
    getDomNodeOrDie().setAutocomplete(autocomplete);
  }
  



  @JsxGetter
  public Object getFiles()
  {
    HtmlInput htmlInput = getDomNodeOrDie();
    if ((htmlInput instanceof HtmlFileInput)) {
      FileList list = new FileList(((HtmlFileInput)htmlInput).getFiles());
      list.setParentScope(getParentScope());
      list.setPrototype(getPrototype(list.getClass()));
      return list;
    }
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.HTMLINPUT_FILES_UNDEFINED)) {
      return Undefined.instance;
    }
    return null;
  }
  



  @JsxGetter
  public String getPlaceholder()
  {
    return getDomNodeOrDie().getPlaceholder();
  }
  



  @JsxSetter
  public void setPlaceholder(String placeholder)
  {
    getDomNodeOrDie().setPlaceholder(placeholder);
  }
  




  @JsxGetter
  public int getWidth()
  {
    String value = getDomNodeOrDie().getAttribute("width");
    Integer intValue = HTMLCanvasElement.getValue(value);
    if (intValue != null) {
      return intValue.intValue();
    }
    return 0;
  }
  



  @JsxSetter
  public void setWidth(int width)
  {
    getDomNodeOrDie().setAttribute("width", Integer.toString(width));
  }
  




  @JsxGetter
  public int getHeight()
  {
    String value = getDomNodeOrDie().getAttribute("height");
    Integer intValue = HTMLCanvasElement.getValue(value);
    if (intValue != null) {
      return intValue.intValue();
    }
    return 0;
  }
  



  @JsxSetter
  public void setHeight(int height)
  {
    getDomNodeOrDie().setAttribute("height", Integer.toString(height));
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public AbstractList getLabels()
  {
    if (labels_ == null) {
      labels_ = new LabelsHelper(getDomNodeOrDie());
    }
    return labels_;
  }
  



  @JsxFunction
  public boolean checkValidity()
  {
    return getDomNodeOrDie().isValid();
  }
}
