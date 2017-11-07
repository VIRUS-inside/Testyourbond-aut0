package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import java.io.IOException;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;




































public class HtmlImageInput
  extends HtmlInput
{
  private boolean wasPositionSpecified_;
  private int xPosition_;
  private int yPosition_;
  
  HtmlImageInput(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  



  public NameValuePair[] getSubmitNameValuePairs()
  {
    String name = getNameAttribute();
    String prefix;
    String prefix;
    if (StringUtils.isEmpty(name)) {
      prefix = "";
    }
    else {
      prefix = name + ".";
    }
    
    if (wasPositionSpecified_) {
      NameValuePair valueX = new NameValuePair(prefix + 'x', Integer.toString(xPosition_));
      NameValuePair valueY = new NameValuePair(prefix + 'y', Integer.toString(yPosition_));
      if ((!prefix.isEmpty()) && (hasFeature(BrowserVersionFeatures.HTMLIMAGE_NAME_VALUE_PARAMS)) && (!getValueAttribute().isEmpty())) {
        return new NameValuePair[] { valueX, valueY, 
          new NameValuePair(getNameAttribute(), getValueAttribute()) };
      }
      return new NameValuePair[] { valueX, valueY };
    }
    return new NameValuePair[] { new NameValuePair(getNameAttribute(), getValueAttribute()) };
  }
  








  public Page click()
    throws IOException
  {
    return click(0, 0);
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
  










  public <P extends Page> P click(int x, int y)
    throws IOException, ElementNotFoundException
  {
    wasPositionSpecified_ = true;
    xPosition_ = x;
    yPosition_ = y;
    return super.click();
  }
  













  public <P extends Page> P click(Event event, boolean ignoreVisibility)
    throws IOException
  {
    wasPositionSpecified_ = true;
    return super.click(event, ignoreVisibility);
  }
  




  public void setDefaultValue(String defaultValue)
  {
    super.setDefaultValue(defaultValue);
    setValueAttribute(defaultValue);
  }
  




  public void setAttributeNS(String namespaceURI, String qualifiedName, String attributeValue, boolean notifyAttributeChangeListeners)
  {
    if ("value".equals(qualifiedName)) {
      setDefaultValue(attributeValue, false);
    }
    super.setAttributeNS(namespaceURI, qualifiedName, attributeValue, notifyAttributeChangeListeners);
  }
  



  protected boolean isRequiredSupported()
  {
    return false;
  }
}
