package org.apache.bcel.generic;

import java.util.StringTokenizer;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantPool;
































































public abstract class InvokeInstruction
  extends FieldOrMethod
  implements ExceptionThrower, TypedInstruction, StackConsumer, StackProducer
{
  InvokeInstruction() {}
  
  protected InvokeInstruction(short opcode, int index)
  {
    super(opcode, index);
  }
  


  public String toString(ConstantPool cp)
  {
    Constant c = cp.getConstant(index);
    StringTokenizer tok = new StringTokenizer(cp.constantToString(c));
    
    return org.apache.bcel.Constants.OPCODE_NAMES[opcode] + " " + tok.nextToken().replace('.', '/') + tok.nextToken();
  }
  





  public int consumeStack(ConstantPoolGen cpg)
  {
    String signature = getSignature(cpg);
    Type[] args = Type.getArgumentTypes(signature);
    
    int sum;
    if (opcode == 184) {
      sum = 0;
    } else {
      sum = 1;
    }
    int n = args.length;
    for (int i = 0; i < n; i++) {
      sum += args[i].getSize();
    }
    return sum;
  }
  




  public int produceStack(ConstantPoolGen cpg)
  {
    return getReturnType(cpg).getSize();
  }
  

  public Type getType(ConstantPoolGen cpg)
  {
    return getReturnType(cpg);
  }
  

  public String getMethodName(ConstantPoolGen cpg)
  {
    return getName(cpg);
  }
  

  public Type getReturnType(ConstantPoolGen cpg)
  {
    return Type.getReturnType(getSignature(cpg));
  }
  

  public Type[] getArgumentTypes(ConstantPoolGen cpg)
  {
    return Type.getArgumentTypes(getSignature(cpg));
  }
  
  public abstract Class[] getExceptions();
}
