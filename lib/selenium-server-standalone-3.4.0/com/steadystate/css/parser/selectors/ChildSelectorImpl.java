package com.steadystate.css.parser.selectors;

import com.steadystate.css.format.CSSFormat;
import com.steadystate.css.format.CSSFormatable;
import com.steadystate.css.parser.Locatable;
import com.steadystate.css.parser.LocatableImpl;
import java.io.Serializable;
import org.w3c.css.sac.DescendantSelector;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SimpleSelector;




















public class ChildSelectorImpl
  extends LocatableImpl
  implements DescendantSelector, CSSFormatable, Serializable
{
  private static final long serialVersionUID = -5843289529637921083L;
  private Selector ancestorSelector_;
  private SimpleSelector simpleSelector_;
  
  public void setAncestorSelector(Selector ancestorSelector)
  {
    ancestorSelector_ = ancestorSelector;
    if ((ancestorSelector instanceof Locatable)) {
      setLocator(((Locatable)ancestorSelector).getLocator());
    }
    else if (ancestorSelector == null) {
      setLocator(null);
    }
  }
  
  public void setSimpleSelector(SimpleSelector simpleSelector) {
    simpleSelector_ = simpleSelector;
  }
  
  public ChildSelectorImpl(Selector parent, SimpleSelector simpleSelector) {
    setAncestorSelector(parent);
    setSimpleSelector(simpleSelector);
  }
  
  public short getSelectorType() {
    return 11;
  }
  
  public Selector getAncestorSelector() {
    return ancestorSelector_;
  }
  
  public SimpleSelector getSimpleSelector() {
    return simpleSelector_;
  }
  


  public String getCssText(CSSFormat format)
  {
    StringBuilder sb = new StringBuilder();
    
    if (null != ancestorSelector_) {
      sb.append(((CSSFormatable)ancestorSelector_).getCssText(format));
    }
    
    sb.append(" > ");
    
    if (null != simpleSelector_) {
      sb.append(((CSSFormatable)simpleSelector_).getCssText(format));
    }
    
    return sb.toString();
  }
  
  public String toString()
  {
    return getCssText(null);
  }
}
