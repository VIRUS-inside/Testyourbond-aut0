package com.steadystate.css.parser.selectors;

import com.steadystate.css.format.CSSFormat;
import com.steadystate.css.format.CSSFormatable;
import com.steadystate.css.parser.Locatable;
import com.steadystate.css.parser.LocatableImpl;
import java.io.Serializable;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SiblingSelector;
import org.w3c.css.sac.SimpleSelector;




















public class DirectAdjacentSelectorImpl
  extends LocatableImpl
  implements SiblingSelector, CSSFormatable, Serializable
{
  private static final long serialVersionUID = -7328602345833826516L;
  private short nodeType_;
  private Selector selector_;
  private SimpleSelector siblingSelector_;
  
  public void setNodeType(short nodeType)
  {
    nodeType_ = nodeType;
  }
  
  public void setSelector(Selector child) {
    selector_ = child;
    if ((child instanceof Locatable)) {
      setLocator(((Locatable)child).getLocator());
    }
    else if (child == null) {
      setLocator(null);
    }
  }
  
  public void setSiblingSelector(SimpleSelector directAdjacent) {
    siblingSelector_ = directAdjacent;
  }
  
  public DirectAdjacentSelectorImpl(short nodeType, Selector child, SimpleSelector directAdjacent)
  {
    setNodeType(nodeType);
    setSelector(child);
    setSiblingSelector(directAdjacent);
  }
  
  public short getNodeType() {
    return nodeType_;
  }
  
  public short getSelectorType() {
    return 12;
  }
  
  public Selector getSelector() {
    return selector_;
  }
  
  public SimpleSelector getSiblingSelector() {
    return siblingSelector_;
  }
  


  public String getCssText(CSSFormat format)
  {
    StringBuilder sb = new StringBuilder();
    
    if (null != selector_) {
      sb.append(((CSSFormatable)selector_).getCssText(format));
    }
    
    sb.append(" + ");
    
    if (null != siblingSelector_) {
      sb.append(((CSSFormatable)siblingSelector_).getCssText(format));
    }
    
    return sb.toString();
  }
  
  public String toString()
  {
    return getCssText(null);
  }
}
