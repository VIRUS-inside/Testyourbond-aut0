package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.generic.ACONST_NULL;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.DCONST;
import org.apache.bcel.generic.ICONST;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.PUTFIELD;
import org.apache.xalan.xsltc.compiler.util.BooleanType;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.IntType;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeType;
import org.apache.xalan.xsltc.compiler.util.RealType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

















final class Variable
  extends VariableBase
{
  Variable() {}
  
  public int getIndex()
  {
    return _local != null ? _local.getIndex() : -1;
  }
  



  public void parseContents(Parser parser)
  {
    super.parseContents(parser);
    

    SyntaxTreeNode parent = getParent();
    if ((parent instanceof Stylesheet))
    {
      _isLocal = false;
      
      Variable var = parser.getSymbolTable().lookupVariable(_name);
      
      if (var != null) {
        int us = getImportPrecedence();
        int them = var.getImportPrecedence();
        
        if (us == them) {
          String name = _name.toString();
          reportError(this, parser, "VARIABLE_REDEF_ERR", name);
        }
        else {
          if (them > us) {
            _ignore = true;
            return;
          }
          
          var.disable();
        }
      }
      
      ((Stylesheet)parent).addVariable(this);
      parser.getSymbolTable().addVariable(this);
    }
    else {
      _isLocal = true;
    }
  }
  




  public Type typeCheck(SymbolTable stable)
    throws TypeCheckError
  {
    if (_select != null) {
      _type = _select.typeCheck(stable);

    }
    else if (hasContents()) {
      typeCheckContents(stable);
      _type = Type.ResultTree;
    }
    else {
      _type = Type.Reference;
    }
    


    return Type.Void;
  }
  




  public void initialize(ClassGenerator classGen, MethodGenerator methodGen)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    

    if ((isLocal()) && (!_refs.isEmpty()))
    {
      if (_local == null) {
        _local = methodGen.addLocalVariable2(getEscapedName(), _type.toJCType(), null);
      }
      


      if (((_type instanceof IntType)) || ((_type instanceof NodeType)) || ((_type instanceof BooleanType)))
      {

        il.append(new ICONST(0));
      } else if ((_type instanceof RealType)) {
        il.append(new DCONST(0.0D));
      } else {
        il.append(new ACONST_NULL());
      }
      
      _local.setStart(il.append(_type.STORE(_local.getIndex())));
    }
  }
  
  public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    

    if (_refs.isEmpty()) {
      _ignore = true;
    }
    

    if (_ignore) return;
    _ignore = true;
    
    String name = getEscapedName();
    
    if (isLocal())
    {
      translateValue(classGen, methodGen);
      

      boolean createLocal = _local == null;
      if (createLocal) {
        mapRegister(methodGen);
      }
      InstructionHandle storeInst = il.append(_type.STORE(_local.getIndex()));
      





      if (createLocal) {
        _local.setStart(storeInst);
      }
    }
    else {
      String signature = _type.toSignature();
      

      if (classGen.containsField(name) == null) {
        classGen.addField(new Field(1, cpg.addUtf8(name), cpg.addUtf8(signature), null, cpg.getConstantPool()));
        




        il.append(classGen.loadTranslet());
        
        translateValue(classGen, methodGen);
        
        il.append(new PUTFIELD(cpg.addFieldref(classGen.getClassName(), name, signature)));
      }
    }
  }
}
