package com.steadystate.css.dom;

import com.steadystate.css.format.CSSFormat;
import com.steadystate.css.format.CSSFormatable;
import java.io.Serializable;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.Counter;























public class CounterImpl
  implements Counter, CSSFormatable, Serializable
{
  private static final long serialVersionUID = 7996279151817598904L;
  private String identifier_;
  private String listStyle_;
  private String separator_;
  
  public void setIdentifier(String identifier)
  {
    identifier_ = identifier;
  }
  
  public void setListStyle(String listStyle) {
    listStyle_ = listStyle;
  }
  
  public void setSeparator(String separator) {
    separator_ = separator;
  }
  

  public CounterImpl(boolean separatorSpecified, LexicalUnit lu)
    throws DOMException
  {
    LexicalUnit next = lu;
    identifier_ = next.getStringValue();
    next = next.getNextLexicalUnit();
    if (next != null) {
      if (next.getLexicalUnitType() != 0)
      {
        throw new DOMException((short)12, "Counter parameters must be separated by ','.");
      }
      
      next = next.getNextLexicalUnit();
      if ((separatorSpecified) && (next != null)) {
        separator_ = next.getStringValue();
        next = next.getNextLexicalUnit();
        if (next != null) {
          if (next.getLexicalUnitType() != 0)
          {
            throw new DOMException((short)12, "Counter parameters must be separated by ','.");
          }
          
          next = next.getNextLexicalUnit();
        }
      }
      if (next != null) {
        listStyle_ = next.getStringValue();
        next = next.getNextLexicalUnit();
        if (next != null)
        {
          throw new DOMException((short)12, "Too many parameters for counter function.");
        }
      }
    }
  }
  

  public CounterImpl() {}
  

  public String getIdentifier()
  {
    return identifier_;
  }
  
  public String getListStyle() {
    return listStyle_;
  }
  
  public String getSeparator() {
    return separator_;
  }
  




  public String getCssText()
  {
    return getCssText(null);
  }
  


  public String getCssText(CSSFormat format)
  {
    StringBuilder sb = new StringBuilder();
    if (separator_ == null)
    {
      sb.append("counter(");
    }
    else
    {
      sb.append("counters(");
    }
    sb.append(identifier_);
    if (separator_ != null) {
      sb.append(", \"").append(separator_).append("\"");
    }
    if (listStyle_ != null) {
      sb.append(", ").append(listStyle_);
    }
    sb.append(")");
    return sb.toString();
  }
  
  public String toString()
  {
    return getCssText(null);
  }
}
