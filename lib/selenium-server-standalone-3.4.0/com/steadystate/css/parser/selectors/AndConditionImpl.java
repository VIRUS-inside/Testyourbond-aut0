package com.steadystate.css.parser.selectors;

import com.steadystate.css.format.CSSFormat;
import com.steadystate.css.format.CSSFormatable;
import com.steadystate.css.parser.Locatable;
import com.steadystate.css.parser.LocatableImpl;
import java.io.Serializable;
import org.w3c.css.sac.CombinatorCondition;
import org.w3c.css.sac.Condition;





















public class AndConditionImpl
  extends LocatableImpl
  implements CombinatorCondition, CSSFormatable, Serializable
{
  private static final long serialVersionUID = -3180583860092672742L;
  private Condition firstCondition_;
  private Condition secondCondition_;
  
  public void setFirstCondition(Condition c1)
  {
    firstCondition_ = c1;
    if ((c1 instanceof Locatable)) {
      setLocator(((Locatable)c1).getLocator());
    }
    else if (c1 == null) {
      setLocator(null);
    }
  }
  
  public void setSecondCondition(Condition c2) {
    secondCondition_ = c2;
  }
  
  public AndConditionImpl(Condition c1, Condition c2) {
    setFirstCondition(c1);
    setSecondCondition(c2);
  }
  
  public short getConditionType() {
    return 0;
  }
  
  public Condition getFirstCondition() {
    return firstCondition_;
  }
  
  public Condition getSecondCondition() {
    return secondCondition_;
  }
  


  public String getCssText(CSSFormat format)
  {
    StringBuilder sb = new StringBuilder();
    
    Condition cond = getFirstCondition();
    if (null != cond) {
      sb.append(((CSSFormatable)cond).getCssText(format));
    }
    
    cond = getSecondCondition();
    if (null != cond) {
      sb.append(((CSSFormatable)cond).getCssText(format));
    }
    
    return sb.toString();
  }
  
  public String toString()
  {
    return getCssText(null);
  }
}
