package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.CHECKCAST;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeSetType;
import org.apache.xalan.xsltc.compiler.util.Type;

























final class VariableRef
  extends VariableRefBase
{
  public VariableRef(Variable variable)
  {
    super(variable);
  }
  
  public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    

    if (_type.implementedAsMethod()) { return;
    }
    String name = _variable.getEscapedName();
    String signature = _type.toSignature();
    
    if (_variable.isLocal()) {
      if (classGen.isExternal()) {
        Closure variableClosure = _closure;
        while ((variableClosure != null) && 
          (!variableClosure.inInnerClass())) {
          variableClosure = variableClosure.getParentClosure();
        }
        
        if (variableClosure != null) {
          il.append(ALOAD_0);
          il.append(new GETFIELD(cpg.addFieldref(variableClosure.getInnerClassName(), name, signature)));

        }
        else
        {
          il.append(_variable.loadInstruction());
        }
      }
      else {
        il.append(_variable.loadInstruction());
      }
    }
    else {
      String className = classGen.getClassName();
      il.append(classGen.loadTranslet());
      if (classGen.isExternal()) {
        il.append(new CHECKCAST(cpg.addClass(className)));
      }
      il.append(new GETFIELD(cpg.addFieldref(className, name, signature)));
    }
    
    if ((_variable.getType() instanceof NodeSetType))
    {
      int clone = cpg.addInterfaceMethodref("org.apache.xml.dtm.DTMAxisIterator", "cloneIterator", "()Lorg/apache/xml/dtm/DTMAxisIterator;");
      


      il.append(new INVOKEINTERFACE(clone, 1));
    }
  }
}
