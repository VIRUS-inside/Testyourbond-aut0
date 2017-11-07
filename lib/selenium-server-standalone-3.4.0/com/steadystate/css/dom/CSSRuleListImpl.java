package com.steadystate.css.dom;

import com.steadystate.css.format.CSSFormat;
import com.steadystate.css.format.CSSFormatable;
import com.steadystate.css.util.LangUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;























public class CSSRuleListImpl
  implements CSSRuleList, CSSFormatable, Serializable
{
  private static final long serialVersionUID = -1269068897476453290L;
  private List<CSSRule> rules_;
  
  public List<CSSRule> getRules()
  {
    if (rules_ == null) {
      rules_ = new ArrayList();
    }
    return rules_;
  }
  
  public void setRules(List<CSSRule> rules) {
    rules_ = rules;
  }
  

  public CSSRuleListImpl() {}
  
  public int getLength()
  {
    return getRules().size();
  }
  
  public CSSRule item(int index) {
    if ((index < 0) || (null == rules_) || (index >= rules_.size())) {
      return null;
    }
    return (CSSRule)rules_.get(index);
  }
  
  public void add(CSSRule rule) {
    getRules().add(rule);
  }
  
  public void insert(CSSRule rule, int index) {
    getRules().add(index, rule);
  }
  
  public void delete(int index) {
    getRules().remove(index);
  }
  




  public String getCssText()
  {
    return getCssText(null);
  }
  


  public String getCssText(CSSFormat format)
  {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < getLength(); i++) {
      if (i > 0) {
        sb.append("\r\n");
      }
      
      CSSRule rule = item(i);
      sb.append(((CSSFormatable)rule).getCssText(format));
    }
    return sb.toString();
  }
  
  public String toString()
  {
    return getCssText(null);
  }
  
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof CSSRuleList)) {
      return false;
    }
    CSSRuleList crl = (CSSRuleList)obj;
    return equalsRules(crl);
  }
  
  private boolean equalsRules(CSSRuleList crl) {
    if ((crl == null) || (getLength() != crl.getLength())) {
      return false;
    }
    for (int i = 0; i < getLength(); i++) {
      CSSRule cssRule1 = item(i);
      CSSRule cssRule2 = crl.item(i);
      if (!LangUtils.equals(cssRule1, cssRule2)) {
        return false;
      }
    }
    return true;
  }
  
  public int hashCode()
  {
    int hash = 17;
    hash = LangUtils.hashCode(hash, rules_);
    return hash;
  }
}
