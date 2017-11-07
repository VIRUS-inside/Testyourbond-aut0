package com.steadystate.css.dom;

import com.steadystate.css.format.CSSFormat;
import com.steadystate.css.format.CSSFormatable;
import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.util.LangUtils;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSValue;

























public class CSSStyleDeclarationImpl
  implements CSSStyleDeclaration, CSSFormatable, Serializable
{
  private static final long serialVersionUID = -2373755821317100189L;
  private static final String PRIORITY_IMPORTANT = "important";
  private CSSRule parentRule_;
  private List<Property> properties_ = new ArrayList();
  
  public void setParentRule(CSSRule parentRule) {
    parentRule_ = parentRule;
  }
  
  public List<Property> getProperties() {
    return properties_;
  }
  
  public void setProperties(List<Property> properties) {
    properties_ = properties;
  }
  
  public CSSStyleDeclarationImpl(CSSRule parentRule) {
    parentRule_ = parentRule;
  }
  


  public CSSStyleDeclarationImpl() {}
  


  public String getCssText()
  {
    return getCssText(null);
  }
  


  public String getCssText(CSSFormat format)
  {
    boolean nl = (format != null) && (format.getPropertiesInSeparateLines());
    
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < properties_.size(); i++) {
      Property p = (Property)properties_.get(i);
      if (p != null) {
        if (nl) {
          sb.append(format.getNewLine());
          sb.append(format.getPropertiesIndent());
        }
        sb.append(p.getCssText(format));
      }
      if (i < properties_.size() - 1) {
        sb.append(";");
        if (!nl) {
          sb.append(' ');
        }
      }
      else if (nl) {
        sb.append(format.getNewLine());
      }
    }
    return sb.toString();
  }
  
  public void setCssText(String cssText) throws DOMException {
    try {
      InputSource is = new InputSource(new StringReader(cssText));
      CSSOMParser parser = new CSSOMParser();
      properties_.clear();
      parser.parseStyleDeclaration(this, is);

    }
    catch (Exception e)
    {

      throw new DOMExceptionImpl(12, 0, e.getMessage());
    }
  }
  
  public String getPropertyValue(String propertyName) {
    Property p = getPropertyDeclaration(propertyName);
    if ((p == null) || (p.getValue() == null)) {
      return "";
    }
    return p.getValue().toString();
  }
  
  public CSSValue getPropertyCSSValue(String propertyName) {
    Property p = getPropertyDeclaration(propertyName);
    return p == null ? null : p.getValue();
  }
  
  public String removeProperty(String propertyName) throws DOMException {
    if (null == propertyName) {
      return "";
    }
    for (int i = 0; i < properties_.size(); i++) {
      Property p = (Property)properties_.get(i);
      if ((p != null) && (propertyName.equalsIgnoreCase(p.getName()))) {
        properties_.remove(i);
        if (p.getValue() == null) {
          return "";
        }
        return p.getValue().toString();
      }
    }
    return "";
  }
  
  public String getPropertyPriority(String propertyName) {
    Property p = getPropertyDeclaration(propertyName);
    if (p == null) {
      return "";
    }
    return p.isImportant() ? "important" : "";
  }
  
  public void setProperty(String propertyName, String value, String priority)
    throws DOMException
  {
    try
    {
      CSSValue expr = null;
      if (!value.isEmpty()) {
        CSSOMParser parser = new CSSOMParser();
        InputSource is = new InputSource(new StringReader(value));
        expr = parser.parsePropertyValue(is);
      }
      Property p = getPropertyDeclaration(propertyName);
      boolean important = "important".equalsIgnoreCase(priority);
      if (p == null) {
        p = new Property(propertyName, expr, important);
        addProperty(p);
      }
      else {
        p.setValue(expr);
        p.setImportant(important);
      }
      

    }
    catch (Exception e)
    {
      throw new DOMExceptionImpl(12, 0, e.getMessage());
    }
  }
  
  public int getLength() {
    return properties_.size();
  }
  
  public String item(int index) {
    Property p = (Property)properties_.get(index);
    return p == null ? "" : p.getName();
  }
  
  public CSSRule getParentRule() {
    return parentRule_;
  }
  
  public void addProperty(Property p) {
    if (null == p) {
      return;
    }
    properties_.add(p);
  }
  
  public Property getPropertyDeclaration(String propertyName) {
    if (null == propertyName) {
      return null;
    }
    for (int i = properties_.size() - 1; i > -1; i--) {
      Property p = (Property)properties_.get(i);
      if ((p != null) && (propertyName.equalsIgnoreCase(p.getName()))) {
        return p;
      }
    }
    return null;
  }
  
  public String toString()
  {
    return getCssText();
  }
  
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof CSSStyleDeclaration)) {
      return false;
    }
    CSSStyleDeclaration csd = (CSSStyleDeclaration)obj;
    


    return equalsProperties(csd);
  }
  
  private boolean equalsProperties(CSSStyleDeclaration csd) {
    if ((csd == null) || (getLength() != csd.getLength())) {
      return false;
    }
    for (int i = 0; i < getLength(); i++) {
      String propertyName = item(i);
      

      String propertyValue1 = getPropertyValue(propertyName);
      String propertyValue2 = csd.getPropertyValue(propertyName);
      if (!LangUtils.equals(propertyValue1, propertyValue2)) {
        return false;
      }
      String propertyPriority1 = getPropertyPriority(propertyName);
      String propertyPriority2 = csd.getPropertyPriority(propertyName);
      if (!LangUtils.equals(propertyPriority1, propertyPriority2)) {
        return false;
      }
    }
    return true;
  }
  
  public int hashCode()
  {
    int hash = 17;
    

    hash = LangUtils.hashCode(hash, properties_);
    return hash;
  }
}
