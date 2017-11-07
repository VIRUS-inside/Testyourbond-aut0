package org.apache.xalan.xsltc.compiler.util;

import java.util.Vector;























public final class MethodType
  extends Type
{
  private final Type _resultType;
  private final Vector _argsType;
  
  public MethodType(Type resultType)
  {
    _argsType = null;
    _resultType = resultType;
  }
  
  public MethodType(Type resultType, Type arg1) {
    if (arg1 != Type.Void) {
      _argsType = new Vector();
      _argsType.addElement(arg1);
    }
    else {
      _argsType = null;
    }
    _resultType = resultType;
  }
  
  public MethodType(Type resultType, Type arg1, Type arg2) {
    _argsType = new Vector(2);
    _argsType.addElement(arg1);
    _argsType.addElement(arg2);
    _resultType = resultType;
  }
  
  public MethodType(Type resultType, Type arg1, Type arg2, Type arg3) {
    _argsType = new Vector(3);
    _argsType.addElement(arg1);
    _argsType.addElement(arg2);
    _argsType.addElement(arg3);
    _resultType = resultType;
  }
  
  public MethodType(Type resultType, Vector argsType) {
    _resultType = resultType;
    _argsType = (argsType.size() > 0 ? argsType : null);
  }
  
  public String toString() {
    StringBuffer result = new StringBuffer("method{");
    if (_argsType != null) {
      int count = _argsType.size();
      for (int i = 0; i < count; i++) {
        result.append(_argsType.elementAt(i));
        if (i != count - 1) result.append(',');
      }
    }
    else {
      result.append("void");
    }
    result.append('}');
    return result.toString();
  }
  
  public String toSignature() {
    return toSignature("");
  }
  



  public String toSignature(String lastArgSig)
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append('(');
    if (_argsType != null) {
      int n = _argsType.size();
      for (int i = 0; i < n; i++) {
        buffer.append(((Type)_argsType.elementAt(i)).toSignature());
      }
    }
    return lastArgSig + ')' + _resultType.toSignature();
  }
  



  public org.apache.bcel.generic.Type toJCType()
  {
    return null;
  }
  
  public boolean identicalTo(Type other) {
    boolean result = false;
    if ((other instanceof MethodType)) {
      MethodType temp = (MethodType)other;
      if (_resultType.identicalTo(_resultType)) {
        int len = argsCount();
        result = len == temp.argsCount();
        for (int i = 0; (i < len) && (result); i++) {
          Type arg1 = (Type)_argsType.elementAt(i);
          Type arg2 = (Type)_argsType.elementAt(i);
          result = arg1.identicalTo(arg2);
        }
      }
    }
    return result;
  }
  
  public int distanceTo(Type other) {
    int result = Integer.MAX_VALUE;
    if ((other instanceof MethodType)) {
      MethodType mtype = (MethodType)other;
      if (_argsType != null) {
        int len = _argsType.size();
        if (len == _argsType.size()) {
          result = 0;
          for (int i = 0; i < len; i++) {
            Type arg1 = (Type)_argsType.elementAt(i);
            Type arg2 = (Type)_argsType.elementAt(i);
            int temp = arg1.distanceTo(arg2);
            if (temp == Integer.MAX_VALUE) {
              result = temp;
              break;
            }
            
            result += arg1.distanceTo(arg2);
          }
          
        }
      }
      else if (_argsType == null) {
        result = 0;
      }
    }
    return result;
  }
  
  public Type resultType() {
    return _resultType;
  }
  
  public Vector argsType() {
    return _argsType;
  }
  
  public int argsCount() {
    return _argsType == null ? 0 : _argsType.size();
  }
}
