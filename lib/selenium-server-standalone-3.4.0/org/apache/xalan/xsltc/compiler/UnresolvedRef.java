package org.apache.xalan.xsltc.compiler;

import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
























final class UnresolvedRef
  extends VariableRefBase
{
  private QName _variableName = null;
  private VariableRefBase _ref = null;
  
  public UnresolvedRef(QName name)
  {
    _variableName = name;
  }
  
  public QName getName() {
    return _variableName;
  }
  
  private ErrorMsg reportError() {
    ErrorMsg err = new ErrorMsg("VARIABLE_UNDEF_ERR", _variableName, this);
    
    getParser().reportError(3, err);
    return err;
  }
  

  private VariableRefBase resolve(Parser parser, SymbolTable stable)
  {
    VariableBase ref = parser.lookupVariable(_variableName);
    if (ref == null) {
      ref = (VariableBase)stable.lookupName(_variableName);
    }
    if (ref == null) {
      reportError();
      return null;
    }
    

    _variable = ref;
    addParentDependency();
    
    if ((ref instanceof Variable)) {
      return new VariableRef((Variable)ref);
    }
    if ((ref instanceof Param)) {
      return new ParameterRef((Param)ref);
    }
    return null;
  }
  
  public Type typeCheck(SymbolTable stable) throws TypeCheckError { ErrorMsg err;
    if (_ref != null) {
      String name = _variableName.toString();
      err = new ErrorMsg("CIRCULAR_VARIABLE_ERR", name, this);
    }
    
    if ((this._ref = resolve(getParser(), stable)) != null) {
      return this._type = _ref.typeCheck(stable);
    }
    throw new TypeCheckError(reportError());
  }
  
  public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
    if (_ref != null) {
      _ref.translate(classGen, methodGen);
    } else
      reportError();
  }
  
  public String toString() {
    return "unresolved-ref()";
  }
}
