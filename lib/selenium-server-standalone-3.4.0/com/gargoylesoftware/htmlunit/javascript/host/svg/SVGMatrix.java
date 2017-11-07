package com.gargoylesoftware.htmlunit.javascript.host.svg;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.Window;























@JsxClass
public class SVGMatrix
  extends SimpleScriptable
{
  private double fieldA_ = 1.0D;
  private double fieldB_ = 0.0D;
  private double fieldC_ = 0.0D;
  private double fieldD_ = 1.0D;
  private double fieldE_ = 0.0D;
  private double fieldF_ = 0.0D;
  




  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public SVGMatrix() {}
  



  public SVGMatrix(Window scope)
  {
    setParentScope(scope);
    setPrototype(getPrototype(getClass()));
  }
  



  @JsxGetter
  public double getA()
  {
    return fieldA_;
  }
  



  @JsxGetter
  public double getB()
  {
    return fieldB_;
  }
  



  @JsxGetter
  public double getC()
  {
    return fieldC_;
  }
  



  @JsxGetter
  public double getD()
  {
    return fieldD_;
  }
  



  @JsxGetter
  public double getE()
  {
    return fieldE_;
  }
  



  @JsxGetter
  public double getF()
  {
    return fieldF_;
  }
  



  @JsxSetter
  public void setA(double newValue)
  {
    fieldA_ = newValue;
  }
  



  @JsxSetter
  public void setB(double newValue)
  {
    fieldB_ = newValue;
  }
  



  @JsxSetter
  public void setC(double newValue)
  {
    fieldC_ = newValue;
  }
  



  @JsxSetter
  public void setD(double newValue)
  {
    fieldD_ = newValue;
  }
  



  @JsxSetter
  public void setE(double newValue)
  {
    fieldE_ = newValue;
  }
  



  @JsxSetter
  public void setF(double newValue)
  {
    fieldF_ = newValue;
  }
  



  @JsxFunction
  public SVGMatrix flipX()
  {
    SVGMatrix result = new SVGMatrix(getWindow());
    
    result.setA(fieldA_ * -1.0D);
    result.setB(fieldB_ * -1.0D);
    result.setC(fieldC_);
    result.setD(fieldD_);
    result.setE(fieldE_);
    result.setF(fieldF_);
    
    return result;
  }
  



  @JsxFunction
  public SVGMatrix flipY()
  {
    SVGMatrix result = new SVGMatrix(getWindow());
    
    result.setA(fieldA_);
    result.setB(fieldB_);
    result.setC(fieldC_ * -1.0D);
    result.setD(fieldD_ * -1.0D);
    result.setE(fieldE_);
    result.setF(fieldF_);
    
    return result;
  }
  



  @JsxFunction
  public SVGMatrix inverse()
  {
    return new SVGMatrix(getWindow());
  }
  




  @JsxFunction
  public SVGMatrix multiply(SVGMatrix by)
  {
    return new SVGMatrix(getWindow());
  }
  




  @JsxFunction
  public SVGMatrix rotate(double angle)
  {
    return new SVGMatrix(getWindow());
  }
  





  @JsxFunction
  public SVGMatrix rotateFromVector(double x, double y)
  {
    return new SVGMatrix(getWindow());
  }
  




  @JsxFunction
  public SVGMatrix scale(double factor)
  {
    SVGMatrix result = new SVGMatrix(getWindow());
    
    result.setA(fieldA_ * factor);
    result.setB(fieldB_ * factor);
    result.setC(fieldC_ * factor);
    result.setD(fieldD_ * factor);
    result.setE(fieldE_);
    result.setF(fieldF_);
    
    return result;
  }
  





  @JsxFunction
  public SVGMatrix scaleNonUniform(double factorX, double factorY)
  {
    return new SVGMatrix(getWindow());
  }
  




  @JsxFunction
  public SVGMatrix skewX(double angle)
  {
    return new SVGMatrix(getWindow());
  }
  




  @JsxFunction
  public SVGMatrix skewY(double angle)
  {
    return new SVGMatrix(getWindow());
  }
  





  @JsxFunction
  public SVGMatrix translate(double x, double y)
  {
    return new SVGMatrix(getWindow());
  }
}
