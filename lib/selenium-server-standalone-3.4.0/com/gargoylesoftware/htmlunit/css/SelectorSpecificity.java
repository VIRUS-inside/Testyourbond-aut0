package com.gargoylesoftware.htmlunit.css;

import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.css.sac.AttributeCondition;
import org.w3c.css.sac.CombinatorCondition;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.DescendantSelector;
import org.w3c.css.sac.ElementSelector;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SiblingSelector;
import org.w3c.css.sac.SimpleSelector;























public class SelectorSpecificity
  implements Comparable<SelectorSpecificity>, Serializable
{
  private static final Log LOG = LogFactory.getLog(SelectorSpecificity.class);
  



  public static final SelectorSpecificity FROM_STYLE_ATTRIBUTE = new SelectorSpecificity(1, 0, 0, 0);
  
  private int fieldA_;
  
  private int fieldB_;
  
  private int fieldC_;
  
  private int fieldD_;
  
  public SelectorSpecificity(Selector selector)
  {
    readSelectorSpecificity(selector);
  }
  
  private SelectorSpecificity(int a, int b, int c, int d) {
    fieldA_ = a;
    fieldB_ = b;
    fieldC_ = c;
    fieldD_ = d;
  }
  
  private void readSelectorSpecificity(Selector selector) {
    switch (selector.getSelectorType()) {
    case 1: 
      return;
    case 10: 
      DescendantSelector ds = (DescendantSelector)selector;
      readSelectorSpecificity(ds.getAncestorSelector());
      readSelectorSpecificity(ds.getSimpleSelector());
      return;
    case 11: 
      DescendantSelector cs = (DescendantSelector)selector;
      readSelectorSpecificity(cs.getAncestorSelector());
      readSelectorSpecificity(cs.getSimpleSelector());
      return;
    case 0: 
      ConditionalSelector conditional = (ConditionalSelector)selector;
      SimpleSelector simpleSel = conditional.getSimpleSelector();
      if (simpleSel != null) {
        readSelectorSpecificity(simpleSel);
      }
      readSelectorSpecificity(conditional.getCondition());
      return;
    case 4: 
      ElementSelector es = (ElementSelector)selector;
      String esName = es.getLocalName();
      if (esName != null) {
        fieldD_ += 1;
      }
      return;
    case 9: 
      ElementSelector pes = (ElementSelector)selector;
      String pesName = pes.getLocalName();
      if (pesName != null) {
        fieldD_ += 1;
      }
      return;
    case 12: 
      SiblingSelector ss = (SiblingSelector)selector;
      readSelectorSpecificity(ss.getSelector());
      readSelectorSpecificity(ss.getSiblingSelector());
      return;
    }
    LOG.warn("Unhandled CSS selector type for specificity computation: '" + 
      selector.getSelectorType() + "'.");
  }
  

  private void readSelectorSpecificity(Condition condition)
  {
    switch (condition.getConditionType()) {
    case 5: 
      fieldB_ += 1;
      return;
    case 9: 
      fieldC_ += 1;
      return;
    case 0: 
      CombinatorCondition cc1 = (CombinatorCondition)condition;
      readSelectorSpecificity(cc1.getFirstCondition());
      readSelectorSpecificity(cc1.getSecondCondition());
      return;
    case 4: 
      AttributeCondition ac1 = (AttributeCondition)condition;
      if ("id".equalsIgnoreCase(ac1.getLocalName())) {
        fieldB_ += 1;
      }
      else {
        fieldC_ += 1;
      }
      return;
    case 10: 
      fieldD_ += 1;
      return;
    }
    LOG.warn("Unhandled CSS condition type for specifity computation: '" + 
      condition.getConditionType() + "'.");
  }
  





  public String toString()
  {
    return fieldA_ + "," + fieldB_ + "," + fieldC_ + "," + fieldD_;
  }
  



  public int compareTo(SelectorSpecificity other)
  {
    if (fieldA_ != fieldA_) {
      return fieldA_ - fieldA_;
    }
    if (fieldB_ != fieldB_) {
      return fieldB_ - fieldB_;
    }
    if (fieldC_ != fieldC_) {
      return fieldC_ - fieldC_;
    }
    if (fieldD_ != fieldD_) {
      return fieldD_ - fieldD_;
    }
    return 0;
  }
  
  public int hashCode()
  {
    int prime = 31;
    int result = 1;
    result = 31 * result + fieldA_;
    result = 31 * result + fieldB_;
    result = 31 * result + fieldC_;
    result = 31 * result + fieldD_;
    return result;
  }
  
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    SelectorSpecificity other = (SelectorSpecificity)obj;
    if (fieldA_ != fieldA_) {
      return false;
    }
    if (fieldB_ != fieldB_) {
      return false;
    }
    if (fieldC_ != fieldC_) {
      return false;
    }
    if (fieldD_ != fieldD_) {
      return false;
    }
    return true;
  }
}
