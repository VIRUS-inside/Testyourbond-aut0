package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.util.StringUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;





































public class HtmlSubmitInput
  extends HtmlInput
{
  private static final String DEFAULT_VALUE = "Submit Query";
  
  HtmlSubmitInput(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, addValueIfNeeded(page, attributes));
  }
  






  private static Map<String, DomAttr> addValueIfNeeded(SgmlPage page, Map<String, DomAttr> attributes)
  {
    BrowserVersion browserVersion = page.getWebClient().getBrowserVersion();
    if (browserVersion.hasFeature(BrowserVersionFeatures.SUBMITINPUT_DEFAULT_VALUE_IF_VALUE_NOT_DEFINED)) {
      for (String key : attributes.keySet()) {
        if ("value".equalsIgnoreCase(key)) {
          return attributes;
        }
      }
      

      DomAttr newAttr = new DomAttr(page, null, "value", "Submit Query", true);
      attributes.put("value", newAttr);
    }
    
    return attributes;
  }
  


  protected boolean doClickStateUpdate(boolean shiftKey, boolean ctrlKey)
    throws IOException
  {
    HtmlForm form = getEnclosingForm();
    if (form != null) {
      form.submit(this);
      return false;
    }
    super.doClickStateUpdate(shiftKey, ctrlKey);
    return false;
  }
  





  public void reset() {}
  





  public String asText()
  {
    String text = getValueAttribute();
    if (text == ATTRIBUTE_NOT_DEFINED) {
      text = "Submit Query";
    }
    return text;
  }
  



  protected void printOpeningTagContentAsXml(PrintWriter printWriter)
  {
    printWriter.print(getTagName());
    
    for (DomAttr attribute : getAttributesMap().values()) {
      String name = attribute.getNodeName();
      String value = attribute.getValue();
      if ((!"value".equals(name)) || (!"Submit Query".equals(value))) {
        printWriter.print(" ");
        printWriter.print(name);
        printWriter.print("=\"");
        printWriter.print(StringUtils.escapeXmlAttributeValue(value));
        printWriter.print("\"");
      }
    }
  }
  





  public NameValuePair[] getSubmitNameValuePairs()
  {
    if ((!getNameAttribute().isEmpty()) && (!hasAttribute("value"))) {
      return new NameValuePair[] { new NameValuePair(getNameAttribute(), "Submit Query") };
    }
    return super.getSubmitNameValuePairs();
  }
  




  public void setAttributeNS(String namespaceURI, String qualifiedName, String attributeValue, boolean notifyAttributeChangeListeners)
  {
    if ("value".equals(qualifiedName)) {
      setDefaultValue(attributeValue, false);
    }
    super.setAttributeNS(namespaceURI, qualifiedName, attributeValue, notifyAttributeChangeListeners);
  }
  



  protected boolean propagateClickStateUpdateToParent()
  {
    return true;
  }
  



  protected boolean isRequiredSupported()
  {
    return false;
  }
}
