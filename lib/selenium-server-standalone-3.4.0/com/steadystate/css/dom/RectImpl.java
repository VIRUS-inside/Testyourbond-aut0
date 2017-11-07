package com.steadystate.css.dom;

import com.steadystate.css.format.CSSFormat;
import com.steadystate.css.format.CSSFormatable;
import java.io.Serializable;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.Rect;




























public class RectImpl
  implements Rect, CSSFormatable, Serializable
{
  private static final long serialVersionUID = -7031248513917920621L;
  private CSSPrimitiveValue top_;
  private CSSPrimitiveValue right_;
  private CSSPrimitiveValue bottom_;
  private CSSPrimitiveValue left_;
  
  public RectImpl(LexicalUnit lu)
    throws DOMException
  {
    if (lu == null) {
      throw new DOMException((short)12, "Rect misses first parameter.");
    }
    top_ = new CSSValueImpl(lu, true);
    

    LexicalUnit next = lu.getNextLexicalUnit();
    if (next == null) {
      throw new DOMException((short)12, "Rect misses second parameter.");
    }
    
    boolean isCommaSeparated = false;
    if (next.getLexicalUnitType() == 0) {
      isCommaSeparated = true;
      next = next.getNextLexicalUnit();
      if (next == null) {
        throw new DOMException((short)12, "Rect misses second parameter.");
      }
    }
    right_ = new CSSValueImpl(next, true);
    

    next = next.getNextLexicalUnit();
    if (next == null) {
      throw new DOMException((short)12, "Rect misses third parameter.");
    }
    if (isCommaSeparated) {
      if (next.getLexicalUnitType() != 0) {
        throw new DOMException((short)12, "All or none rect parameters must be separated by ','.");
      }
      
      next = next.getNextLexicalUnit();
      if (next == null) {
        throw new DOMException((short)12, "Rect misses third parameter.");
      }
      
    }
    else if (next.getLexicalUnitType() == 0) {
      throw new DOMException((short)12, "All or none rect parameters must be separated by ','.");
    }
    

    bottom_ = new CSSValueImpl(next, true);
    

    next = next.getNextLexicalUnit();
    if (next == null) {
      throw new DOMException((short)12, "Rect misses fourth parameter.");
    }
    if (isCommaSeparated) {
      if (next.getLexicalUnitType() != 0) {
        throw new DOMException((short)12, "All or none rect parameters must be separated by ','.");
      }
      
      next = next.getNextLexicalUnit();
      if (next == null) {
        throw new DOMException((short)12, "Rect misses fourth parameter.");
      }
      
    }
    else if (next.getLexicalUnitType() == 0) {
      throw new DOMException((short)12, "All or none rect parameters must be separated by ','.");
    }
    

    left_ = new CSSValueImpl(next, true);
    

    next = next.getNextLexicalUnit();
    if (next != null) {
      throw new DOMException((short)12, "Too many parameters for rect function.");
    }
  }
  




  public RectImpl() {}
  




  public CSSPrimitiveValue getTop()
  {
    return top_;
  }
  



  public void setTop(CSSPrimitiveValue top)
  {
    top_ = top;
  }
  


  public CSSPrimitiveValue getRight()
  {
    return right_;
  }
  



  public void setRight(CSSPrimitiveValue right)
  {
    right_ = right;
  }
  


  public CSSPrimitiveValue getBottom()
  {
    return bottom_;
  }
  



  public void setBottom(CSSPrimitiveValue bottom)
  {
    bottom_ = bottom;
  }
  


  public CSSPrimitiveValue getLeft()
  {
    return left_;
  }
  



  public void setLeft(CSSPrimitiveValue left)
  {
    left_ = left;
  }
  




  public String getCssText()
  {
    return getCssText(null);
  }
  






  public String getCssText(CSSFormat format)
  {
    return "rect(" + top_ + ", " + right_ + ", " + bottom_ + ", " + left_ + ")";
  }
  



  public String toString()
  {
    return getCssText(null);
  }
}
