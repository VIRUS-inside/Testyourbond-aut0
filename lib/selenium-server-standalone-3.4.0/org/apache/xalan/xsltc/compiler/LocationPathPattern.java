package org.apache.xalan.xsltc.compiler;

import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;























public abstract class LocationPathPattern
  extends Pattern
{
  private Template _template;
  private int _importPrecedence;
  
  public LocationPathPattern() {}
  
  private double _priority = NaN.0D;
  private int _position = 0;
  
  public Type typeCheck(SymbolTable stable) throws TypeCheckError {
    return Type.Void;
  }
  

  public void translate(ClassGenerator classGen, MethodGenerator methodGen) {}
  
  public void setTemplate(Template template)
  {
    _template = template;
    _priority = template.getPriority();
    _importPrecedence = template.getImportPrecedence();
    _position = template.getPosition();
  }
  
  public Template getTemplate() {
    return _template;
  }
  
  public final double getPriority() {
    return Double.isNaN(_priority) ? getDefaultPriority() : _priority;
  }
  
  public double getDefaultPriority() {
    return 0.5D;
  }
  







  public boolean noSmallerThan(LocationPathPattern other)
  {
    if (_importPrecedence > _importPrecedence) {
      return true;
    }
    if (_importPrecedence == _importPrecedence) {
      if (_priority > _priority) {
        return true;
      }
      if ((_priority == _priority) && 
        (_position > _position)) {
        return true;
      }
    }
    
    return false;
  }
  
  public abstract StepPattern getKernelPattern();
  
  public abstract void reduceKernelPattern();
  
  public abstract boolean isWildcard();
  
  public int getAxis() {
    StepPattern sp = getKernelPattern();
    return sp != null ? sp.getAxis() : 3;
  }
  
  public String toString() {
    return "root()";
  }
}
