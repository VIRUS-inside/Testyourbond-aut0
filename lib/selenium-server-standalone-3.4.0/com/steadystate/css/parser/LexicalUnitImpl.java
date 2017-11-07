package com.steadystate.css.parser;

import com.steadystate.css.format.CSSFormat;
import com.steadystate.css.format.CSSFormatable;
import java.io.Serializable;
import org.w3c.css.sac.LexicalUnit;
























public class LexicalUnitImpl
  extends LocatableImpl
  implements LexicalUnit, CSSFormatable, Serializable
{
  private static final long serialVersionUID = -7260032046960116891L;
  private short lexicalUnitType_;
  private LexicalUnit nextLexicalUnit_;
  private LexicalUnit previousLexicalUnit_;
  private float floatValue_;
  private String dimension_;
  private String functionName_;
  private LexicalUnit parameters_;
  private String stringValue_;
  private transient String toString_;
  
  public void setLexicalUnitType(short type)
  {
    lexicalUnitType_ = type;
    toString_ = null;
  }
  
  public void setNextLexicalUnit(LexicalUnit next) {
    nextLexicalUnit_ = next;
  }
  
  public void setPreviousLexicalUnit(LexicalUnit prev) {
    previousLexicalUnit_ = prev;
  }
  
  public void setFloatValue(float floatVal) {
    floatValue_ = floatVal;
    toString_ = null;
  }
  
  public String getDimension() {
    return dimension_;
  }
  
  public void setDimension(String dimension) {
    dimension_ = dimension;
    toString_ = null;
  }
  
  public void setFunctionName(String function) {
    functionName_ = function;
    toString_ = null;
  }
  
  public void setParameters(LexicalUnit params) {
    parameters_ = params;
    toString_ = null;
  }
  
  public void setStringValue(String stringVal) {
    stringValue_ = stringVal;
    toString_ = null;
  }
  
  protected LexicalUnitImpl(LexicalUnit previous, short type) {
    this();
    lexicalUnitType_ = type;
    previousLexicalUnit_ = previous;
    if (previousLexicalUnit_ != null) {
      previousLexicalUnit_).nextLexicalUnit_ = this;
    }
  }
  


  protected LexicalUnitImpl(LexicalUnit previous, int value)
  {
    this(previous, (short)13);
    floatValue_ = value;
  }
  


  protected LexicalUnitImpl(LexicalUnit previous, short type, float value)
  {
    this(previous, type);
    floatValue_ = value;
  }
  






  protected LexicalUnitImpl(LexicalUnit previous, short type, String dimension, float value)
  {
    this(previous, type);
    dimension_ = dimension;
    floatValue_ = value;
  }
  


  protected LexicalUnitImpl(LexicalUnit previous, short type, String value)
  {
    this(previous, type);
    stringValue_ = value;
  }
  






  protected LexicalUnitImpl(LexicalUnit previous, short type, String name, LexicalUnit params)
  {
    this(previous, type);
    functionName_ = name;
    parameters_ = params;
  }
  
  protected LexicalUnitImpl(LexicalUnit previous, short type, String name, String stringValue)
  {
    this(previous, type);
    functionName_ = name;
    stringValue_ = stringValue;
  }
  


  protected LexicalUnitImpl() {}
  

  public short getLexicalUnitType()
  {
    return lexicalUnitType_;
  }
  
  public LexicalUnit getNextLexicalUnit() {
    return nextLexicalUnit_;
  }
  
  public LexicalUnit getPreviousLexicalUnit() {
    return previousLexicalUnit_;
  }
  
  public int getIntegerValue() {
    return (int)floatValue_;
  }
  
  public float getFloatValue() {
    return floatValue_;
  }
  
  public String getDimensionUnitText() {
    switch (lexicalUnitType_) {
    case 15: 
      return "em";
    case 16: 
      return "ex";
    case 17: 
      return "px";
    case 18: 
      return "in";
    case 19: 
      return "cm";
    case 20: 
      return "mm";
    case 21: 
      return "pt";
    case 22: 
      return "pc";
    case 23: 
      return "%";
    case 28: 
      return "deg";
    case 29: 
      return "grad";
    case 30: 
      return "rad";
    case 31: 
      return "ms";
    case 32: 
      return "s";
    case 33: 
      return "Hz";
    case 34: 
      return "kHz";
    case 42: 
      return dimension_;
    }
    return "";
  }
  
  public String getFunctionName()
  {
    return functionName_;
  }
  
  public LexicalUnit getParameters() {
    return parameters_;
  }
  
  public String getStringValue() {
    return stringValue_;
  }
  
  public LexicalUnit getSubValues() {
    return parameters_;
  }
  




  public String getCssText()
  {
    return getCssText(null);
  }
  


  public String getCssText(CSSFormat format)
  {
    if (null != toString_) {
      return toString_;
    }
    
    StringBuilder sb = new StringBuilder();
    switch (lexicalUnitType_) {
    case 0: 
      sb.append(",");
      break;
    case 1: 
      sb.append("+");
      break;
    case 2: 
      sb.append("-");
      break;
    case 3: 
      sb.append("*");
      break;
    case 4: 
      sb.append("/");
      break;
    case 5: 
      sb.append("%");
      break;
    case 6: 
      sb.append("^");
      break;
    case 7: 
      sb.append("<");
      break;
    case 8: 
      sb.append(">");
      break;
    case 9: 
      sb.append("<=");
      break;
    case 10: 
      sb.append(">=");
      break;
    case 11: 
      sb.append("~");
      break;
    case 12: 
      sb.append("inherit");
      break;
    case 13: 
      sb.append(String.valueOf(getIntegerValue()));
      break;
    case 14: 
      sb.append(getTrimedFloatValue());
      break;
    case 15: 
    case 16: 
    case 17: 
    case 18: 
    case 19: 
    case 20: 
    case 21: 
    case 22: 
    case 23: 
    case 28: 
    case 29: 
    case 30: 
    case 31: 
    case 32: 
    case 33: 
    case 34: 
    case 42: 
      sb.append(getTrimedFloatValue());
      String dimUnitText = getDimensionUnitText();
      if (null != dimUnitText) {
        sb.append(dimUnitText);
      }
      break;
    case 24: 
      sb.append("url(").append(getStringValue()).append(")");
      break;
    case 25: 
      sb.append("counter(");
      appendParams(sb);
      sb.append(")");
      break;
    case 26: 
      sb.append("counters(");
      appendParams(sb);
      sb.append(")");
      break;
    case 27: 
      sb.append("rgb(");
      appendParams(sb);
      sb.append(")");
      break;
    case 35: 
      sb.append(getStringValue());
      break;
    case 36: 
      sb.append("\"");
      

      String value = getStringValue();
      value = value.replace("\n", "\\A ").replace("\r", "\\D ");
      sb.append(value);
      
      sb.append("\"");
      break;
    

    case 37: 
      sb.append("attr(").append(getStringValue()).append(")");
      break;
    case 38: 
      sb.append("rect(");
      appendParams(sb);
      sb.append(")");
      break;
    case 39: 
      String range = getStringValue();
      if (null != range) {
        sb.append(range);
      }
      break;
    case 40: 
      String subExpression = getStringValue();
      if (null != subExpression) {
        sb.append(subExpression);
      }
      break;
    case 41: 
      String functName = getFunctionName();
      if (null != functName) {
        sb.append(functName);
      }
      sb.append('(');
      appendParams(sb);
      sb.append(")");
      break;
    }
    
    
    toString_ = sb.toString();
    return toString_;
  }
  
  public String toString()
  {
    return getCssText(null);
  }
  
  public String toDebugString() {
    StringBuilder sb = new StringBuilder();
    switch (lexicalUnitType_) {
    case 0: 
      sb.append("SAC_OPERATOR_COMMA");
      break;
    case 1: 
      sb.append("SAC_OPERATOR_PLUS");
      break;
    case 2: 
      sb.append("SAC_OPERATOR_MINUS");
      break;
    case 3: 
      sb.append("SAC_OPERATOR_MULTIPLY");
      break;
    case 4: 
      sb.append("SAC_OPERATOR_SLASH");
      break;
    case 5: 
      sb.append("SAC_OPERATOR_MOD");
      break;
    case 6: 
      sb.append("SAC_OPERATOR_EXP");
      break;
    case 7: 
      sb.append("SAC_OPERATOR_LT");
      break;
    case 8: 
      sb.append("SAC_OPERATOR_GT");
      break;
    case 9: 
      sb.append("SAC_OPERATOR_LE");
      break;
    case 10: 
      sb.append("SAC_OPERATOR_GE");
      break;
    case 11: 
      sb.append("SAC_OPERATOR_TILDE");
      break;
    case 12: 
      sb.append("SAC_INHERIT");
      break;
    

    case 13: 
      sb.append("SAC_INTEGER(").append(String.valueOf(getIntegerValue())).append(")");
      break;
    

    case 14: 
      sb.append("SAC_REAL(").append(getTrimedFloatValue()).append(")");
      break;
    


    case 15: 
      sb.append("SAC_EM(").append(getTrimedFloatValue()).append(getDimensionUnitText()).append(")");
      break;
    


    case 16: 
      sb.append("SAC_EX(").append(getTrimedFloatValue()).append(getDimensionUnitText()).append(")");
      break;
    


    case 17: 
      sb.append("SAC_PIXEL(").append(getTrimedFloatValue()).append(getDimensionUnitText()).append(")");
      break;
    


    case 18: 
      sb.append("SAC_INCH(").append(getTrimedFloatValue()).append(getDimensionUnitText()).append(")");
      break;
    


    case 19: 
      sb.append("SAC_CENTIMETER(").append(getTrimedFloatValue()).append(getDimensionUnitText()).append(")");
      break;
    


    case 20: 
      sb.append("SAC_MILLIMETER(").append(getTrimedFloatValue()).append(getDimensionUnitText()).append(")");
      break;
    


    case 21: 
      sb.append("SAC_POINT(").append(getTrimedFloatValue()).append(getDimensionUnitText()).append(")");
      break;
    


    case 22: 
      sb.append("SAC_PICA(").append(getTrimedFloatValue()).append(getDimensionUnitText()).append(")");
      break;
    


    case 23: 
      sb.append("SAC_PERCENTAGE(").append(getTrimedFloatValue()).append(getDimensionUnitText()).append(")");
      break;
    


    case 28: 
      sb.append("SAC_DEGREE(").append(getTrimedFloatValue()).append(getDimensionUnitText()).append(")");
      break;
    


    case 29: 
      sb.append("SAC_GRADIAN(").append(getTrimedFloatValue()).append(getDimensionUnitText()).append(")");
      break;
    


    case 30: 
      sb.append("SAC_RADIAN(").append(getTrimedFloatValue()).append(getDimensionUnitText()).append(")");
      break;
    


    case 31: 
      sb.append("SAC_MILLISECOND(").append(getTrimedFloatValue()).append(getDimensionUnitText()).append(")");
      break;
    


    case 32: 
      sb.append("SAC_SECOND(").append(getTrimedFloatValue()).append(getDimensionUnitText()).append(")");
      break;
    


    case 33: 
      sb.append("SAC_HERTZ(").append(getTrimedFloatValue()).append(getDimensionUnitText()).append(")");
      break;
    


    case 34: 
      sb.append("SAC_KILOHERTZ(").append(getTrimedFloatValue()).append(getDimensionUnitText()).append(")");
      break;
    


    case 42: 
      sb.append("SAC_DIMENSION(").append(getTrimedFloatValue()).append(getDimensionUnitText()).append(")");
      break;
    

    case 24: 
      sb.append("SAC_URI(url(").append(getStringValue()).append("))");
      break;
    case 25: 
      sb.append("SAC_COUNTER_FUNCTION(counter(");
      appendParams(sb);
      sb.append("))");
      break;
    case 26: 
      sb.append("SAC_COUNTERS_FUNCTION(counters(");
      appendParams(sb);
      sb.append("))");
      break;
    case 27: 
      sb.append("SAC_RGBCOLOR(rgb(");
      appendParams(sb);
      sb.append("))");
      break;
    

    case 35: 
      sb.append("SAC_IDENT(").append(getStringValue()).append(")");
      break;
    

    case 36: 
      sb.append("SAC_STRING_VALUE(\"").append(getStringValue()).append("\")");
      break;
    

    case 37: 
      sb.append("SAC_ATTR(attr(").append(getStringValue()).append("))");
      break;
    case 38: 
      sb.append("SAC_RECT_FUNCTION(rect(");
      appendParams(sb);
      sb.append("))");
      break;
    

    case 39: 
      sb.append("SAC_UNICODERANGE(").append(getStringValue()).append(")");
      break;
    

    case 40: 
      sb.append("SAC_SUB_EXPRESSION(").append(getStringValue()).append(")");
      break;
    

    case 41: 
      sb.append("SAC_FUNCTION(").append(getFunctionName()).append("(");
      LexicalUnit l = parameters_;
      while (l != null) {
        sb.append(l.toString());
        l = l.getNextLexicalUnit();
      }
      sb.append("))");
      break;
    }
    
    
    return sb.toString();
  }
  
  private void appendParams(StringBuilder sb) {
    LexicalUnit l = parameters_;
    if (l != null) {
      sb.append(l.toString());
      l = l.getNextLexicalUnit();
      while (l != null) {
        if (l.getLexicalUnitType() != 0) {
          sb.append(" ");
        }
        sb.append(l.toString());
        l = l.getNextLexicalUnit();
      }
    }
  }
  
  private String getTrimedFloatValue() {
    float f = getFloatValue();
    int i = (int)f;
    
    if (f - i == 0.0F) {
      return Integer.toString((int)f);
    }
    return Float.toString(f);
  }
  
  public static LexicalUnit createNumber(LexicalUnit prev, int i) {
    return new LexicalUnitImpl(prev, i);
  }
  
  public static LexicalUnit createNumber(LexicalUnit prev, float f) {
    return new LexicalUnitImpl(prev, (short)14, f);
  }
  
  public static LexicalUnit createPercentage(LexicalUnit prev, float f) {
    return new LexicalUnitImpl(prev, (short)23, f);
  }
  
  public static LexicalUnit createPixel(LexicalUnit prev, float f) {
    return new LexicalUnitImpl(prev, (short)17, f);
  }
  
  public static LexicalUnit createCentimeter(LexicalUnit prev, float f) {
    return new LexicalUnitImpl(prev, (short)19, f);
  }
  
  public static LexicalUnit createMillimeter(LexicalUnit prev, float f) {
    return new LexicalUnitImpl(prev, (short)20, f);
  }
  
  public static LexicalUnit createInch(LexicalUnit prev, float f) {
    return new LexicalUnitImpl(prev, (short)18, f);
  }
  
  public static LexicalUnit createPoint(LexicalUnit prev, float f) {
    return new LexicalUnitImpl(prev, (short)21, f);
  }
  
  public static LexicalUnit createPica(LexicalUnit prev, float f) {
    return new LexicalUnitImpl(prev, (short)22, f);
  }
  
  public static LexicalUnit createEm(LexicalUnit prev, float f) {
    return new LexicalUnitImpl(prev, (short)15, f);
  }
  
  public static LexicalUnit createEx(LexicalUnit prev, float f) {
    return new LexicalUnitImpl(prev, (short)16, f);
  }
  
  public static LexicalUnit createDegree(LexicalUnit prev, float f) {
    return new LexicalUnitImpl(prev, (short)28, f);
  }
  
  public static LexicalUnit createRadian(LexicalUnit prev, float f) {
    return new LexicalUnitImpl(prev, (short)30, f);
  }
  
  public static LexicalUnit createGradian(LexicalUnit prev, float f) {
    return new LexicalUnitImpl(prev, (short)29, f);
  }
  
  public static LexicalUnit createMillisecond(LexicalUnit prev, float f) {
    return new LexicalUnitImpl(prev, (short)31, f);
  }
  
  public static LexicalUnit createSecond(LexicalUnit prev, float f) {
    return new LexicalUnitImpl(prev, (short)32, f);
  }
  
  public static LexicalUnit createHertz(LexicalUnit prev, float f) {
    return new LexicalUnitImpl(prev, (short)33, f);
  }
  
  public static LexicalUnit createDimension(LexicalUnit prev, float f, String dim) {
    return new LexicalUnitImpl(prev, (short)42, dim, f);
  }
  
  public static LexicalUnit createKiloHertz(LexicalUnit prev, float f) {
    return new LexicalUnitImpl(prev, (short)34, f);
  }
  
  public static LexicalUnit createCounter(LexicalUnit prev, LexicalUnit params) {
    return new LexicalUnitImpl(prev, (short)25, "counter", params);
  }
  
  public static LexicalUnit createCounters(LexicalUnit prev, LexicalUnit params) {
    return new LexicalUnitImpl(prev, (short)26, "counters", params);
  }
  

  public static LexicalUnit createAttr(LexicalUnit prev, String value)
  {
    return new LexicalUnitImpl(prev, (short)37, "name", value);
  }
  
  public static LexicalUnit createRect(LexicalUnit prev, LexicalUnit params) {
    return new LexicalUnitImpl(prev, (short)38, "rect", params);
  }
  
  public static LexicalUnit createRgbColor(LexicalUnit prev, LexicalUnit params) {
    return new LexicalUnitImpl(prev, (short)27, "rgb", params);
  }
  
  public static LexicalUnit createFunction(LexicalUnit prev, String name, LexicalUnit params) {
    return new LexicalUnitImpl(prev, (short)41, name, params);
  }
  
  public static LexicalUnit createString(LexicalUnit prev, String value) {
    return new LexicalUnitImpl(prev, (short)36, value);
  }
  
  public static LexicalUnit createIdent(LexicalUnit prev, String value) {
    return new LexicalUnitImpl(prev, (short)35, value);
  }
  
  public static LexicalUnit createURI(LexicalUnit prev, String value) {
    return new LexicalUnitImpl(prev, (short)24, value);
  }
  
  public static LexicalUnit createComma(LexicalUnit prev) {
    return new LexicalUnitImpl(prev, (short)0);
  }
}
