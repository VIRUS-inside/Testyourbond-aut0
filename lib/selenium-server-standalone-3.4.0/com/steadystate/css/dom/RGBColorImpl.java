package com.steadystate.css.dom;

import com.steadystate.css.format.CSSFormat;
import com.steadystate.css.format.CSSFormatable;
import java.io.Serializable;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.RGBColor;



























public class RGBColorImpl
  implements RGBColor, CSSFormatable, Serializable
{
  private static final long serialVersionUID = 8152675334081993160L;
  private CSSPrimitiveValue red_;
  private CSSPrimitiveValue green_;
  private CSSPrimitiveValue blue_;
  
  public RGBColorImpl(LexicalUnit lu)
    throws DOMException
  {
    LexicalUnit next = lu;
    red_ = new CSSValueImpl(next, true);
    next = next.getNextLexicalUnit();
    if (next != null) {
      if (next.getLexicalUnitType() != 0)
      {
        throw new DOMException((short)12, "rgb parameters must be separated by ','.");
      }
      
      next = next.getNextLexicalUnit();
      if (next != null) {
        green_ = new CSSValueImpl(next, true);
        next = next.getNextLexicalUnit();
        if (next != null) {
          if (next.getLexicalUnitType() != 0)
          {
            throw new DOMException((short)12, "rgb parameters must be separated by ','.");
          }
          
          next = next.getNextLexicalUnit();
          blue_ = new CSSValueImpl(next, true);
          next = next.getNextLexicalUnit();
          if (next != null)
          {
            throw new DOMException((short)12, "Too many parameters for rgb function.");
          }
        }
      }
    }
  }
  





  public RGBColorImpl() {}
  




  public CSSPrimitiveValue getRed()
  {
    return red_;
  }
  



  public void setRed(CSSPrimitiveValue red)
  {
    red_ = red;
  }
  


  public CSSPrimitiveValue getGreen()
  {
    return green_;
  }
  



  public void setGreen(CSSPrimitiveValue green)
  {
    green_ = green;
  }
  


  public CSSPrimitiveValue getBlue()
  {
    return blue_;
  }
  



  public void setBlue(CSSPrimitiveValue blue)
  {
    blue_ = blue;
  }
  




  public String getCssText()
  {
    return getCssText(null);
  }
  


  public String getCssText(CSSFormat format)
  {
    StringBuilder sb = new StringBuilder();
    if ((null != format) && (format.isRgbAsHex()))
    {



      sb.append("#").append(getColorAsHex(red_)).append(getColorAsHex(green_)).append(getColorAsHex(blue_));
      return sb.toString();
    }
    







    sb.append("rgb(").append(red_).append(", ").append(green_).append(", ").append(blue_).append(")");
    return sb.toString();
  }
  



  public String toString()
  {
    return getCssText(null);
  }
  
  private String getColorAsHex(CSSPrimitiveValue color) {
    return String.format("%02x", new Object[] { Integer.valueOf(Math.round(color.getFloatValue(13))) });
  }
}
