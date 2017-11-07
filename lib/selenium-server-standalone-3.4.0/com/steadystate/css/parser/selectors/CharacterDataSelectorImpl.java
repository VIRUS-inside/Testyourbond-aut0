package com.steadystate.css.parser.selectors;

import com.steadystate.css.format.CSSFormat;
import com.steadystate.css.format.CSSFormatable;
import com.steadystate.css.parser.LocatableImpl;
import java.io.Serializable;
import org.w3c.css.sac.CharacterDataSelector;























public class CharacterDataSelectorImpl
  extends LocatableImpl
  implements CharacterDataSelector, CSSFormatable, Serializable
{
  private static final long serialVersionUID = 4635511567927852889L;
  private String data_;
  
  public void setData(String data)
  {
    data_ = data;
  }
  
  public CharacterDataSelectorImpl(String data) {
    setData(data);
  }
  
  public short getSelectorType() {
    return 6;
  }
  
  public String getData() {
    return data_;
  }
  


  public String getCssText(CSSFormat format)
  {
    String data = getData();
    if (data == null) {
      return "";
    }
    return data;
  }
  
  public String toString()
  {
    return getCssText(null);
  }
}
