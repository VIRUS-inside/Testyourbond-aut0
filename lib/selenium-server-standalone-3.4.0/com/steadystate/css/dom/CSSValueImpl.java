package com.steadystate.css.dom;

import com.steadystate.css.format.CSSFormat;
import com.steadystate.css.format.CSSFormatable;
import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.LexicalUnitImpl;
import com.steadystate.css.userdata.UserDataConstants;
import com.steadystate.css.util.LangUtils;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.Locator;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;
import org.w3c.dom.css.CSSValueList;
import org.w3c.dom.css.Counter;
import org.w3c.dom.css.RGBColor;
import org.w3c.dom.css.Rect;




























public class CSSValueImpl
  extends CSSOMObjectImpl
  implements CSSPrimitiveValue, CSSValueList, CSSFormatable
{
  private static final long serialVersionUID = 406281136418322579L;
  private Object value_;
  
  public Object getValue()
  {
    return value_;
  }
  
  public void setValue(Object value) {
    value_ = value;
  }
  


  public CSSValueImpl(LexicalUnit value, boolean forcePrimitive)
  {
    LexicalUnit parameters = null;
    try {
      parameters = value.getParameters();
    }
    catch (IllegalStateException e) {}
    


    if ((!forcePrimitive) && (value.getNextLexicalUnit() != null)) {
      value_ = getValues(value);
    }
    else if (parameters != null) {
      if (value.getLexicalUnitType() == 38)
      {
        value_ = new RectImpl(value.getParameters());
      }
      else if (value.getLexicalUnitType() == 27)
      {
        value_ = new RGBColorImpl(value.getParameters());
      }
      else if (value.getLexicalUnitType() == 25)
      {
        value_ = new CounterImpl(false, value.getParameters());
      }
      else if (value.getLexicalUnitType() == 26)
      {
        value_ = new CounterImpl(true, value.getParameters());
      }
      else {
        value_ = value;
      }
      
    }
    else {
      value_ = value;
    }
    
    if ((value instanceof LexicalUnitImpl)) {
      Locator locator = ((LexicalUnitImpl)value).getLocator();
      if (locator != null) {
        setUserData(UserDataConstants.KEY_LOCATOR, locator);
      }
    }
  }
  

  public CSSValueImpl() {}
  
  private List<CSSValueImpl> getValues(LexicalUnit value)
  {
    List<CSSValueImpl> values = new ArrayList();
    LexicalUnit lu = value;
    while (lu != null) {
      values.add(new CSSValueImpl(lu, true));
      lu = lu.getNextLexicalUnit();
    }
    return values;
  }
  
  public CSSValueImpl(LexicalUnit value) {
    this(value, false);
  }
  
  public String getCssText() {
    return getCssText(null);
  }
  


  public String getCssText(CSSFormat format)
  {
    if (getCssValueType() == 2)
    {


      StringBuilder sb = new StringBuilder();
      List<?> list = (List)value_;
      Iterator<?> it = list.iterator();
      
      boolean separate = false;
      while (it.hasNext()) {
        Object o = it.next();
        
        CSSValueImpl cssValue = (CSSValueImpl)o;
        if (separate) {
          if ((value_ instanceof LexicalUnit)) {
            LexicalUnit lu = (LexicalUnit)value_;
            if (lu.getLexicalUnitType() != 0) {
              sb.append(" ");
            }
          }
          else {
            sb.append(" ");
          }
        }
        
        if ((value_ instanceof CSSFormatable)) {
          sb.append(((CSSFormatable)o).getCssText(format));
        }
        else {
          sb.append(o.toString());
        }
        separate = true;
      }
      return sb.toString();
    }
    if ((value_ instanceof CSSFormatable)) {
      return ((CSSFormatable)value_).getCssText(format);
    }
    return value_ != null ? value_.toString() : "";
  }
  
  public void setCssText(String cssText) throws DOMException {
    try {
      InputSource is = new InputSource(new StringReader(cssText));
      CSSOMParser parser = new CSSOMParser();
      CSSValueImpl v2 = (CSSValueImpl)parser.parsePropertyValue(is);
      value_ = value_;
      setUserDataMap(v2.getUserDataMap());

    }
    catch (Exception e)
    {

      throw new DOMExceptionImpl(12, 0, e.getMessage());
    }
  }
  
  public short getCssValueType() {
    if ((value_ instanceof List)) {
      return 2;
    }
    if (((value_ instanceof LexicalUnit)) && 
      (((LexicalUnit)value_).getLexicalUnitType() == 12)) {
      return 0;
    }
    return 1;
  }
  
  public short getPrimitiveType() {
    if ((value_ instanceof LexicalUnit)) {
      LexicalUnit lu = (LexicalUnit)value_;
      switch (lu.getLexicalUnitType()) {
      case 12: 
        return 21;
      case 13: 
      case 14: 
        return 1;
      case 15: 
        return 3;
      case 16: 
        return 4;
      case 17: 
        return 5;
      case 18: 
        return 8;
      case 19: 
        return 6;
      case 20: 
        return 7;
      case 21: 
        return 9;
      case 22: 
        return 10;
      case 23: 
        return 2;
      case 24: 
        return 20;
      
      case 25: 
        return 23;
      

      case 28: 
        return 11;
      case 29: 
        return 13;
      case 30: 
        return 12;
      case 31: 
        return 14;
      case 32: 
        return 15;
      case 33: 
        return 16;
      case 34: 
        return 17;
      case 35: 
        return 21;
      case 36: 
        return 19;
      case 37: 
        return 22;
      

      case 39: 
      case 40: 
      case 41: 
        return 19;
      case 42: 
        return 18;
      }
      return 0;
    }
    
    if ((value_ instanceof RectImpl)) {
      return 24;
    }
    if ((value_ instanceof RGBColorImpl)) {
      return 25;
    }
    if ((value_ instanceof CounterImpl)) {
      return 23;
    }
    return 0;
  }
  
  public void setFloatValue(short unitType, float floatValue) throws DOMException {
    value_ = LexicalUnitImpl.createNumber(null, floatValue);
  }
  
  public float getFloatValue(short unitType) throws DOMException {
    if ((value_ instanceof LexicalUnit)) {
      LexicalUnit lu = (LexicalUnit)value_;
      return lu.getFloatValue();
    }
    throw new DOMExceptionImpl((short)15, 10);
  }
  



  public void setStringValue(short stringType, String stringValue)
    throws DOMException
  {
    switch (stringType) {
    case 19: 
      value_ = LexicalUnitImpl.createString(null, stringValue);
      break;
    case 20: 
      value_ = LexicalUnitImpl.createURI(null, stringValue);
      break;
    case 21: 
      value_ = LexicalUnitImpl.createIdent(null, stringValue);
      break;
    

    case 22: 
      throw new DOMExceptionImpl((short)9, 19);
    

    default: 
      throw new DOMExceptionImpl((short)15, 11);
    }
    
  }
  


  public String getStringValue()
    throws DOMException
  {
    if ((value_ instanceof LexicalUnit)) {
      LexicalUnit lu = (LexicalUnit)value_;
      if ((lu.getLexicalUnitType() == 35) || 
        (lu.getLexicalUnitType() == 36) || 
        (lu.getLexicalUnitType() == 24) || 
        (lu.getLexicalUnitType() == 12) || 
        (lu.getLexicalUnitType() == 37)) {
        return lu.getStringValue();
      }
      

      if (lu.getLexicalUnitType() == 41) {
        return lu.toString();
      }
    }
    else if ((value_ instanceof List)) {
      return null;
    }
    
    throw new DOMExceptionImpl((short)15, 11);
  }
  
  public Counter getCounterValue()
    throws DOMException
  {
    if ((value_ instanceof Counter)) {
      return (Counter)value_;
    }
    
    throw new DOMExceptionImpl((short)15, 12);
  }
  
  public Rect getRectValue()
    throws DOMException
  {
    if ((value_ instanceof Rect)) {
      return (Rect)value_;
    }
    
    throw new DOMExceptionImpl((short)15, 13);
  }
  
  public RGBColor getRGBColorValue()
    throws DOMException
  {
    if ((value_ instanceof RGBColor)) {
      return (RGBColor)value_;
    }
    
    throw new DOMExceptionImpl((short)15, 14);
  }
  


  public int getLength()
  {
    if ((value_ instanceof List)) {
      return ((List)value_).size();
    }
    return 0;
  }
  
  public CSSValue item(int index)
  {
    if ((value_ instanceof List)) {
      List<CSSValue> list = (List)value_;
      return (CSSValue)list.get(index);
    }
    return null;
  }
  
  public String toString()
  {
    return getCssText(null);
  }
  
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof CSSValue)) {
      return false;
    }
    CSSValue cv = (CSSValue)obj;
    


    return (super.equals(obj)) && (getCssValueType() == cv.getCssValueType()) && (LangUtils.equals(getCssText(), cv.getCssText()));
  }
  
  public int hashCode()
  {
    int hash = super.hashCode();
    hash = LangUtils.hashCode(hash, value_);
    return hash;
  }
}
