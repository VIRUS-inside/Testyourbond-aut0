package com.steadystate.css.parser;

import com.steadystate.css.format.CSSFormat;
import com.steadystate.css.format.CSSFormatable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SelectorList;




















public class SelectorListImpl
  extends LocatableImpl
  implements SelectorList, CSSFormatable, Serializable
{
  private static final long serialVersionUID = 7313376916207026333L;
  
  public SelectorListImpl() {}
  
  private List<Selector> selectors_ = new ArrayList(10);
  
  public List<Selector> getSelectors() {
    return selectors_;
  }
  
  public void setSelectors(List<Selector> selectors) {
    selectors_ = selectors;
  }
  
  public int getLength() {
    return selectors_.size();
  }
  
  public Selector item(int index) {
    return (Selector)selectors_.get(index);
  }
  
  public void add(Selector sel) {
    selectors_.add(sel);
  }
  


  public String getCssText(CSSFormat format)
  {
    int len = getLength();
    
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < len; i++) {
      CSSFormatable sel = (CSSFormatable)item(i);
      sb.append(sel.getCssText(format));
      if (i < len - 1) {
        sb.append(", ");
      }
    }
    return sb.toString();
  }
  
  public String toString()
  {
    return getCssText(null);
  }
}
