package com.steadystate.css.parser.selectors;

import com.steadystate.css.format.CSSFormat;
import com.steadystate.css.format.CSSFormatable;
import com.steadystate.css.parser.Locatable;
import com.steadystate.css.parser.LocatableImpl;
import java.io.Serializable;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.SimpleSelector;






















public class ConditionalSelectorImpl
  extends LocatableImpl
  implements ConditionalSelector, CSSFormatable, Serializable
{
  private static final long serialVersionUID = 7217145899707580586L;
  private SimpleSelector simpleSelector_;
  private Condition condition_;
  
  public void setSimpleSelector(SimpleSelector simpleSelector)
  {
    simpleSelector_ = simpleSelector;
    if ((simpleSelector instanceof Locatable)) {
      setLocator(((Locatable)simpleSelector).getLocator());
    }
    else if (simpleSelector == null) {
      setLocator(null);
    }
  }
  
  public void setCondition(Condition condition) {
    condition_ = condition;
    if (getLocator() == null) {
      if ((condition instanceof Locatable)) {
        setLocator(((Locatable)condition).getLocator());
      }
      else if (condition == null) {
        setLocator(null);
      }
    }
  }
  
  public ConditionalSelectorImpl(SimpleSelector simpleSelector, Condition condition) {
    setSimpleSelector(simpleSelector);
    setCondition(condition);
  }
  
  public short getSelectorType() {
    return 0;
  }
  
  public SimpleSelector getSimpleSelector() {
    return simpleSelector_;
  }
  
  public Condition getCondition() {
    return condition_;
  }
  


  public String getCssText(CSSFormat format)
  {
    StringBuilder sb = new StringBuilder();
    
    if (null != simpleSelector_) {
      sb.append(((CSSFormatable)simpleSelector_).getCssText(format));
    }
    
    if (null != condition_) {
      sb.append(((CSSFormatable)condition_).getCssText(format));
    }
    
    return sb.toString();
  }
  
  public String toString()
  {
    return getCssText(null);
  }
}
