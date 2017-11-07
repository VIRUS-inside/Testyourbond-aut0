package org.apache.bcel.verifier.structurals;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.AccessFlags;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantInteger;
import org.apache.bcel.classfile.ConstantInterfaceMethodref;
import org.apache.bcel.classfile.ConstantString;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ANEWARRAY;
import org.apache.bcel.generic.ARETURN;
import org.apache.bcel.generic.ARRAYLENGTH;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ATHROW;
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.CALOAD;
import org.apache.bcel.generic.CPInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.DALOAD;
import org.apache.bcel.generic.DASTORE;
import org.apache.bcel.generic.DLOAD;
import org.apache.bcel.generic.DMUL;
import org.apache.bcel.generic.DNEG;
import org.apache.bcel.generic.DRETURN;
import org.apache.bcel.generic.DSUB;
import org.apache.bcel.generic.DUP2_X1;
import org.apache.bcel.generic.DUP2_X2;
import org.apache.bcel.generic.DUP_X1;
import org.apache.bcel.generic.DUP_X2;
import org.apache.bcel.generic.F2D;
import org.apache.bcel.generic.F2I;
import org.apache.bcel.generic.F2L;
import org.apache.bcel.generic.FADD;
import org.apache.bcel.generic.FCMPL;
import org.apache.bcel.generic.FCONST;
import org.apache.bcel.generic.FRETURN;
import org.apache.bcel.generic.FieldGenOrMethodGen;
import org.apache.bcel.generic.FieldInstruction;
import org.apache.bcel.generic.IALOAD;
import org.apache.bcel.generic.IAND;
import org.apache.bcel.generic.ICONST;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.IFGE;
import org.apache.bcel.generic.IFGT;
import org.apache.bcel.generic.IFLT;
import org.apache.bcel.generic.IFNONNULL;
import org.apache.bcel.generic.IF_ACMPNE;
import org.apache.bcel.generic.IINC;
import org.apache.bcel.generic.IMPDEP1;
import org.apache.bcel.generic.IMPDEP2;
import org.apache.bcel.generic.IMUL;
import org.apache.bcel.generic.INEG;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.LADD;
import org.apache.bcel.generic.LCMP;
import org.apache.bcel.generic.LDC_W;
import org.apache.bcel.generic.LDIV;
import org.apache.bcel.generic.LLOAD;
import org.apache.bcel.generic.LOR;
import org.apache.bcel.generic.LREM;
import org.apache.bcel.generic.LRETURN;
import org.apache.bcel.generic.LSHL;
import org.apache.bcel.generic.LSHR;
import org.apache.bcel.generic.LSTORE;
import org.apache.bcel.generic.LoadClass;
import org.apache.bcel.generic.LocalVariableInstruction;
import org.apache.bcel.generic.MONITORENTER;
import org.apache.bcel.generic.MULTIANEWARRAY;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.NOP;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.PUTSTATIC;
import org.apache.bcel.generic.RET;
import org.apache.bcel.generic.RETURN;
import org.apache.bcel.generic.ReferenceType;
import org.apache.bcel.generic.ReturnaddressType;
import org.apache.bcel.generic.StackConsumer;
import org.apache.bcel.generic.StackInstruction;
import org.apache.bcel.generic.StoreInstruction;
import org.apache.bcel.generic.Type;
import org.apache.bcel.verifier.VerificationResult;
import org.apache.bcel.verifier.Verifier;
import org.apache.bcel.verifier.VerifierFactory;
import org.apache.bcel.verifier.exc.AssertionViolatedException;

public class InstConstraintVisitor extends org.apache.bcel.generic.EmptyVisitor implements org.apache.bcel.generic.Visitor
{
  private static ObjectType GENERIC_ARRAY = new ObjectType("org.apache.bcel.verifier.structurals.GenericArray");
  












  private Frame frame = null;
  





  private ConstantPoolGen cpg = null;
  





  private MethodGen mg = null;
  

  public InstConstraintVisitor() {}
  

  private OperandStack stack()
  {
    return frame.getStack();
  }
  




  private LocalVariables locals()
  {
    return frame.getLocals();
  }
  





  private void constraintViolated(Instruction violator, String description)
  {
    String fq_classname = violator.getClass().getName();
    throw new org.apache.bcel.verifier.exc.StructuralCodeConstraintException("Instruction " + fq_classname.substring(fq_classname.lastIndexOf('.') + 1) + " constraint violated: " + description);
  }
  








  public void setFrame(Frame f)
  {
    frame = f;
  }
  




  public void setConstantPoolGen(ConstantPoolGen cpg)
  {
    this.cpg = cpg;
  }
  



  public void setMethodGen(MethodGen mg)
  {
    this.mg = mg;
  }
  



  private void indexOfInt(Instruction o, Type index)
  {
    if (!index.equals(Type.INT)) {
      constraintViolated(o, "The 'index' is not of type int but of type " + index + ".");
    }
  }
  




  private void referenceTypeIsInitialized(Instruction o, ReferenceType r)
  {
    if ((r instanceof UninitializedObjectType)) {
      constraintViolated(o, "Working on an uninitialized object '" + r + "'.");
    }
  }
  
  private void valueOfInt(Instruction o, Type value)
  {
    if (!value.equals(Type.INT)) {
      constraintViolated(o, "The 'value' is not of type int but of type " + value + ".");
    }
  }
  



  private boolean arrayrefOfArrayType(Instruction o, Type arrayref)
  {
    if ((!(arrayref instanceof ArrayType)) && (!arrayref.equals(Type.NULL)))
      constraintViolated(o, "The 'arrayref' does not refer to an array but is of type " + arrayref + ".");
    return arrayref instanceof ArrayType;
  }
  

















  private void _visitStackAccessor(Instruction o)
  {
    int consume = o.consumeStack(cpg);
    if (consume > stack().slotsUsed()) {
      constraintViolated(o, "Cannot consume " + consume + " stack slots: only " + stack().slotsUsed() + " slot(s) left on stack!\nStack:\n" + stack());
    }
    
    int produce = o.produceStack(cpg) - o.consumeStack(cpg);
    if (produce + stack().slotsUsed() > stack().maxStack()) {
      constraintViolated(o, "Cannot produce " + produce + " stack slots: only " + (stack().maxStack() - stack().slotsUsed()) + " free stack slot(s) left.\nStack:\n" + stack());
    }
  }
  









  public void visitLoadClass(LoadClass o)
  {
    ObjectType t = o.getLoadClassType(cpg);
    if (t != null) {
      Verifier v = VerifierFactory.getVerifier(t.getClassName());
      VerificationResult vr = v.doPass2();
      if (vr.getStatus() != 1) {
        constraintViolated((Instruction)o, "Class '" + o.getLoadClassType(cpg).getClassName() + "' is referenced, but cannot be loaded and resolved: '" + vr + "'.");
      }
    }
  }
  


  public void visitStackConsumer(StackConsumer o)
  {
    _visitStackAccessor((Instruction)o);
  }
  


  public void visitStackProducer(org.apache.bcel.generic.StackProducer o)
  {
    _visitStackAccessor((Instruction)o);
  }
  








  public void visitCPInstruction(CPInstruction o)
  {
    int idx = o.getIndex();
    if ((idx < 0) || (idx >= cpg.getSize())) {
      throw new AssertionViolatedException("Huh?! Constant pool index of instruction '" + o + "' illegal? Pass 3a should have checked this!");
    }
  }
  






  public void visitFieldInstruction(FieldInstruction o)
  {
    Constant c = cpg.getConstant(o.getIndex());
    if (!(c instanceof org.apache.bcel.classfile.ConstantFieldref)) {
      constraintViolated(o, "Index '" + o.getIndex() + "' should refer to a CONSTANT_Fieldref_info structure, but refers to '" + c + "'.");
    }
    
    Type t = o.getType(cpg);
    if ((t instanceof ObjectType)) {
      String name = ((ObjectType)t).getClassName();
      Verifier v = VerifierFactory.getVerifier(name);
      VerificationResult vr = v.doPass2();
      if (vr.getStatus() != 1) {
        constraintViolated(o, "Class '" + name + "' is referenced, but cannot be loaded and resolved: '" + vr + "'.");
      }
    }
  }
  





  public void visitInvokeInstruction(InvokeInstruction o) {}
  





  public void visitStackInstruction(StackInstruction o)
  {
    _visitStackAccessor(o);
  }
  



  public void visitLocalVariableInstruction(LocalVariableInstruction o)
  {
    if (locals().maxLocals() <= (o.getType(cpg).getSize() == 1 ? o.getIndex() : o.getIndex() + 1)) {
      constraintViolated(o, "The 'index' is not a valid index into the local variable array.");
    }
  }
  





  public void visitLoadInstruction(org.apache.bcel.generic.LoadInstruction o)
  {
    if (locals().get(o.getIndex()) == Type.UNKNOWN) {
      constraintViolated(o, "Read-Access on local variable " + o.getIndex() + " with unknown content.");
    }
    



    if ((o.getType(cpg).getSize() == 2) && 
      (locals().get(o.getIndex() + 1) != Type.UNKNOWN)) {
      constraintViolated(o, "Reading a two-locals value from local variables " + o.getIndex() + " and " + (o.getIndex() + 1) + " where the latter one is destroyed.");
    }
    


    if (!(o instanceof ALOAD)) {
      if (locals().get(o.getIndex()) != o.getType(cpg)) {
        constraintViolated(o, "Local Variable type and LOADing Instruction type mismatch: Local Variable: '" + locals().get(o.getIndex()) + "'; Instruction type: '" + o.getType(cpg) + "'.");
      }
      
    }
    else if (!(locals().get(o.getIndex()) instanceof ReferenceType)) {
      constraintViolated(o, "Local Variable type and LOADing Instruction type mismatch: Local Variable: '" + locals().get(o.getIndex()) + "'; Instruction expects a ReferenceType.");
    }
    




    if (stack().maxStack() - stack().slotsUsed() < o.getType(cpg).getSize()) {
      constraintViolated(o, "Not enough free stack slots to load a '" + o.getType(cpg) + "' onto the OperandStack.");
    }
  }
  




  public void visitStoreInstruction(StoreInstruction o)
  {
    if (stack().isEmpty()) {
      constraintViolated(o, "Cannot STORE: Stack to read from is empty.");
    }
    
    if (!(o instanceof ASTORE)) {
      if (stack().peek() != o.getType(cpg)) {
        constraintViolated(o, "Stack top type and STOREing Instruction type mismatch: Stack top: '" + stack().peek() + "'; Instruction type: '" + o.getType(cpg) + "'.");
      }
    }
    else {
      Type stacktop = stack().peek();
      if ((!(stacktop instanceof ReferenceType)) && (!(stacktop instanceof ReturnaddressType))) {
        constraintViolated(o, "Stack top type and STOREing Instruction type mismatch: Stack top: '" + stack().peek() + "'; Instruction expects a ReferenceType or a ReturnadressType.");
      }
      if ((stacktop instanceof ReferenceType)) {
        referenceTypeIsInitialized(o, (ReferenceType)stacktop);
      }
    }
  }
  


  public void visitReturnInstruction(org.apache.bcel.generic.ReturnInstruction o)
  {
    if ((o instanceof RETURN))
      return;
    ReferenceType objectref;
    if ((o instanceof ARETURN)) {
      if (stack().peek() == Type.NULL) {
        return;
      }
      
      if (!(stack().peek() instanceof ReferenceType)) {
        constraintViolated(o, "Reference type expected on top of stack, but is: '" + stack().peek() + "'.");
      }
      referenceTypeIsInitialized(o, (ReferenceType)stack().peek());
      objectref = (ReferenceType)stack().peek();



    }
    else
    {


      Type method_type = mg.getType();
      if ((method_type == Type.BOOLEAN) || (method_type == Type.BYTE) || (method_type == Type.SHORT) || (method_type == Type.CHAR))
      {


        method_type = Type.INT;
      }
      if (!method_type.equals(stack().peek())) {
        constraintViolated(o, "Current method has return type of '" + mg.getType() + "' expecting a '" + method_type + "' on top of the stack. But stack top is a '" + stack().peek() + "'.");
      }
    }
  }
  






  public void visitAALOAD(org.apache.bcel.generic.AALOAD o)
  {
    Type arrayref = stack().peek(1);
    Type index = stack().peek(0);
    
    indexOfInt(o, index);
    if (arrayrefOfArrayType(o, arrayref)) {
      if (!(((ArrayType)arrayref).getElementType() instanceof ReferenceType)) {
        constraintViolated(o, "The 'arrayref' does not refer to an array with elements of a ReferenceType but to an array of " + ((ArrayType)arrayref).getElementType() + ".");
      }
      referenceTypeIsInitialized(o, (ReferenceType)((ArrayType)arrayref).getElementType());
    }
  }
  


  public void visitAASTORE(org.apache.bcel.generic.AASTORE o)
  {
    Type arrayref = stack().peek(2);
    Type index = stack().peek(1);
    Type value = stack().peek(0);
    
    indexOfInt(o, index);
    if (!(value instanceof ReferenceType)) {
      constraintViolated(o, "The 'value' is not of a ReferenceType but of type " + value + ".");
    } else {
      referenceTypeIsInitialized(o, (ReferenceType)value);
    }
    

    if (arrayrefOfArrayType(o, arrayref)) {
      if (!(((ArrayType)arrayref).getElementType() instanceof ReferenceType)) {
        constraintViolated(o, "The 'arrayref' does not refer to an array with elements of a ReferenceType but to an array of " + ((ArrayType)arrayref).getElementType() + ".");
      }
      if (!((ReferenceType)value).isAssignmentCompatibleWith((ReferenceType)((ArrayType)arrayref).getElementType())) {
        constraintViolated(o, "The type of 'value' ('" + value + "') is not assignment compatible to the components of the array 'arrayref' refers to. ('" + ((ArrayType)arrayref).getElementType() + "')");
      }
    }
  }
  





  public void visitACONST_NULL(org.apache.bcel.generic.ACONST_NULL o) {}
  





  public void visitALOAD(ALOAD o) {}
  




  public void visitANEWARRAY(ANEWARRAY o)
  {
    if (!stack().peek().equals(Type.INT)) {
      constraintViolated(o, "The 'count' at the stack top is not of type '" + Type.INT + "' but of type '" + stack().peek() + "'.");
    }
  }
  



  public void visitARETURN(ARETURN o)
  {
    if (!(stack().peek() instanceof ReferenceType)) {
      constraintViolated(o, "The 'objectref' at the stack top is not of a ReferenceType but of type '" + stack().peek() + "'.");
    }
    ReferenceType objectref = (ReferenceType)stack().peek();
    referenceTypeIsInitialized(o, objectref);
  }
  









  public void visitARRAYLENGTH(ARRAYLENGTH o)
  {
    Type arrayref = stack().peek(0);
    arrayrefOfArrayType(o, arrayref);
  }
  


  public void visitASTORE(ASTORE o)
  {
    if ((!(stack().peek() instanceof ReferenceType)) && (!(stack().peek() instanceof ReturnaddressType))) {
      constraintViolated(o, "The 'objectref' is not of a ReferenceType or of ReturnaddressType but of " + stack().peek() + ".");
    }
    if ((stack().peek() instanceof ReferenceType)) {
      referenceTypeIsInitialized(o, (ReferenceType)stack().peek());
    }
  }
  




  public void visitATHROW(ATHROW o)
  {
    if ((!(stack().peek() instanceof ObjectType)) && (!stack().peek().equals(Type.NULL))) {
      constraintViolated(o, "The 'objectref' is not of an (initialized) ObjectType but of type " + stack().peek() + ".");
    }
    

    if (stack().peek().equals(Type.NULL)) { return;
    }
    ObjectType exc = (ObjectType)stack().peek();
    ObjectType throwable = (ObjectType)Type.getType("Ljava/lang/Throwable;");
    if ((!exc.subclassOf(throwable)) && (!exc.equals(throwable))) {
      constraintViolated(o, "The 'objectref' is not of class Throwable or of a subclass of Throwable, but of '" + stack().peek() + "'.");
    }
  }
  


  public void visitBALOAD(org.apache.bcel.generic.BALOAD o)
  {
    Type arrayref = stack().peek(1);
    Type index = stack().peek(0);
    indexOfInt(o, index);
    if ((arrayrefOfArrayType(o, arrayref)) && 
      (!((ArrayType)arrayref).getElementType().equals(Type.BOOLEAN)) && (!((ArrayType)arrayref).getElementType().equals(Type.BYTE)))
    {
      constraintViolated(o, "The 'arrayref' does not refer to an array with elements of a Type.BYTE or Type.BOOLEAN but to an array of '" + ((ArrayType)arrayref).getElementType() + "'.");
    }
  }
  



  public void visitBASTORE(org.apache.bcel.generic.BASTORE o)
  {
    Type arrayref = stack().peek(2);
    Type index = stack().peek(1);
    Type value = stack().peek(0);
    
    indexOfInt(o, index);
    valueOfInt(o, index);
    if ((arrayrefOfArrayType(o, arrayref)) && 
      (!((ArrayType)arrayref).getElementType().equals(Type.BOOLEAN)) && (!((ArrayType)arrayref).getElementType().equals(Type.BYTE)))
    {
      constraintViolated(o, "The 'arrayref' does not refer to an array with elements of a Type.BYTE or Type.BOOLEAN but to an array of '" + ((ArrayType)arrayref).getElementType() + "'.");
    }
  }
  




  public void visitBIPUSH(org.apache.bcel.generic.BIPUSH o) {}
  



  public void visitBREAKPOINT(org.apache.bcel.generic.BREAKPOINT o)
  {
    throw new AssertionViolatedException("In this JustIce verification pass there should not occur an illegal instruction such as BREAKPOINT.");
  }
  


  public void visitCALOAD(CALOAD o)
  {
    Type arrayref = stack().peek(1);
    Type index = stack().peek(0);
    
    indexOfInt(o, index);
    arrayrefOfArrayType(o, arrayref);
  }
  


  public void visitCASTORE(org.apache.bcel.generic.CASTORE o)
  {
    Type arrayref = stack().peek(2);
    Type index = stack().peek(1);
    Type value = stack().peek(0);
    
    indexOfInt(o, index);
    valueOfInt(o, index);
    if ((arrayrefOfArrayType(o, arrayref)) && 
      (!((ArrayType)arrayref).getElementType().equals(Type.CHAR))) {
      constraintViolated(o, "The 'arrayref' does not refer to an array with elements of type char but to an array of type " + ((ArrayType)arrayref).getElementType() + ".");
    }
  }
  




  public void visitCHECKCAST(org.apache.bcel.generic.CHECKCAST o)
  {
    Type objectref = stack().peek(0);
    if (!(objectref instanceof ReferenceType)) {
      constraintViolated(o, "The 'objectref' is not of a ReferenceType but of type " + objectref + ".");
    }
    else {
      referenceTypeIsInitialized(o, (ReferenceType)objectref);
    }
    


    Constant c = cpg.getConstant(o.getIndex());
    if (!(c instanceof org.apache.bcel.classfile.ConstantClass)) {
      constraintViolated(o, "The Constant at 'index' is not a ConstantClass, but '" + c + "'.");
    }
  }
  


  public void visitD2F(org.apache.bcel.generic.D2F o)
  {
    if (stack().peek() != Type.DOUBLE) {
      constraintViolated(o, "The value at the stack top is not of type 'double', but of type '" + stack().peek() + "'.");
    }
  }
  


  public void visitD2I(org.apache.bcel.generic.D2I o)
  {
    if (stack().peek() != Type.DOUBLE) {
      constraintViolated(o, "The value at the stack top is not of type 'double', but of type '" + stack().peek() + "'.");
    }
  }
  


  public void visitD2L(org.apache.bcel.generic.D2L o)
  {
    if (stack().peek() != Type.DOUBLE) {
      constraintViolated(o, "The value at the stack top is not of type 'double', but of type '" + stack().peek() + "'.");
    }
  }
  


  public void visitDADD(org.apache.bcel.generic.DADD o)
  {
    if (stack().peek() != Type.DOUBLE) {
      constraintViolated(o, "The value at the stack top is not of type 'double', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.DOUBLE) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'double', but of type '" + stack().peek(1) + "'.");
    }
  }
  


  public void visitDALOAD(DALOAD o)
  {
    indexOfInt(o, stack().peek());
    if (stack().peek(1) == Type.NULL) {
      return;
    }
    if (!(stack().peek(1) instanceof ArrayType)) {
      constraintViolated(o, "Stack next-to-top must be of type double[] but is '" + stack().peek(1) + "'.");
    }
    Type t = ((ArrayType)stack().peek(1)).getBasicType();
    if (t != Type.DOUBLE) {
      constraintViolated(o, "Stack next-to-top must be of type double[] but is '" + stack().peek(1) + "'.");
    }
  }
  


  public void visitDASTORE(DASTORE o)
  {
    if (stack().peek() != Type.DOUBLE) {
      constraintViolated(o, "The value at the stack top is not of type 'double', but of type '" + stack().peek() + "'.");
    }
    indexOfInt(o, stack().peek(1));
    if (stack().peek(2) == Type.NULL) {
      return;
    }
    if (!(stack().peek(2) instanceof ArrayType)) {
      constraintViolated(o, "Stack next-to-next-to-top must be of type double[] but is '" + stack().peek(2) + "'.");
    }
    Type t = ((ArrayType)stack().peek(2)).getBasicType();
    if (t != Type.DOUBLE) {
      constraintViolated(o, "Stack next-to-next-to-top must be of type double[] but is '" + stack().peek(2) + "'.");
    }
  }
  


  public void visitDCMPG(org.apache.bcel.generic.DCMPG o)
  {
    if (stack().peek() != Type.DOUBLE) {
      constraintViolated(o, "The value at the stack top is not of type 'double', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.DOUBLE) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'double', but of type '" + stack().peek(1) + "'.");
    }
  }
  


  public void visitDCMPL(org.apache.bcel.generic.DCMPL o)
  {
    if (stack().peek() != Type.DOUBLE) {
      constraintViolated(o, "The value at the stack top is not of type 'double', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.DOUBLE) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'double', but of type '" + stack().peek(1) + "'.");
    }
  }
  




  public void visitDCONST(org.apache.bcel.generic.DCONST o) {}
  



  public void visitDDIV(org.apache.bcel.generic.DDIV o)
  {
    if (stack().peek() != Type.DOUBLE) {
      constraintViolated(o, "The value at the stack top is not of type 'double', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.DOUBLE) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'double', but of type '" + stack().peek(1) + "'.");
    }
  }
  





  public void visitDLOAD(DLOAD o) {}
  




  public void visitDMUL(DMUL o)
  {
    if (stack().peek() != Type.DOUBLE) {
      constraintViolated(o, "The value at the stack top is not of type 'double', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.DOUBLE) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'double', but of type '" + stack().peek(1) + "'.");
    }
  }
  


  public void visitDNEG(DNEG o)
  {
    if (stack().peek() != Type.DOUBLE) {
      constraintViolated(o, "The value at the stack top is not of type 'double', but of type '" + stack().peek() + "'.");
    }
  }
  


  public void visitDREM(org.apache.bcel.generic.DREM o)
  {
    if (stack().peek() != Type.DOUBLE) {
      constraintViolated(o, "The value at the stack top is not of type 'double', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.DOUBLE) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'double', but of type '" + stack().peek(1) + "'.");
    }
  }
  


  public void visitDRETURN(DRETURN o)
  {
    if (stack().peek() != Type.DOUBLE) {
      constraintViolated(o, "The value at the stack top is not of type 'double', but of type '" + stack().peek() + "'.");
    }
  }
  





  public void visitDSTORE(org.apache.bcel.generic.DSTORE o) {}
  




  public void visitDSUB(DSUB o)
  {
    if (stack().peek() != Type.DOUBLE) {
      constraintViolated(o, "The value at the stack top is not of type 'double', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.DOUBLE) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'double', but of type '" + stack().peek(1) + "'.");
    }
  }
  


  public void visitDUP(org.apache.bcel.generic.DUP o)
  {
    if (stack().peek().getSize() != 1) {
      constraintViolated(o, "Won't DUP type on stack top '" + stack().peek() + "' because it must occupy exactly one slot, not '" + stack().peek().getSize() + "'.");
    }
  }
  


  public void visitDUP_X1(DUP_X1 o)
  {
    if (stack().peek().getSize() != 1) {
      constraintViolated(o, "Type on stack top '" + stack().peek() + "' should occupy exactly one slot, not '" + stack().peek().getSize() + "'.");
    }
    if (stack().peek(1).getSize() != 1) {
      constraintViolated(o, "Type on stack next-to-top '" + stack().peek(1) + "' should occupy exactly one slot, not '" + stack().peek(1).getSize() + "'.");
    }
  }
  


  public void visitDUP_X2(DUP_X2 o)
  {
    if (stack().peek().getSize() != 1) {
      constraintViolated(o, "Stack top type must be of size 1, but is '" + stack().peek() + "' of size '" + stack().peek().getSize() + "'.");
    }
    if (stack().peek(1).getSize() == 2) {
      return;
    }
    
    if (stack().peek(2).getSize() != 1) {
      constraintViolated(o, "If stack top's size is 1 and stack next-to-top's size is 1, stack next-to-next-to-top's size must also be 1, but is: '" + stack().peek(2) + "' of size '" + stack().peek(2).getSize() + "'.");
    }
  }
  



  public void visitDUP2(org.apache.bcel.generic.DUP2 o)
  {
    if (stack().peek().getSize() == 2) {
      return;
    }
    
    if (stack().peek(1).getSize() != 1) {
      constraintViolated(o, "If stack top's size is 1, then stack next-to-top's size must also be 1. But it is '" + stack().peek(1) + "' of size '" + stack().peek(1).getSize() + "'.");
    }
  }
  



  public void visitDUP2_X1(DUP2_X1 o)
  {
    if (stack().peek().getSize() == 2) {
      if (stack().peek(1).getSize() != 1) {
        constraintViolated(o, "If stack top's size is 2, then stack next-to-top's size must be 1. But it is '" + stack().peek(1) + "' of size '" + stack().peek(1).getSize() + "'.");
      }
      

    }
    else
    {
      if (stack().peek(1).getSize() != 1) {
        constraintViolated(o, "If stack top's size is 1, then stack next-to-top's size must also be 1. But it is '" + stack().peek(1) + "' of size '" + stack().peek(1).getSize() + "'.");
      }
      if (stack().peek(2).getSize() != 1) {
        constraintViolated(o, "If stack top's size is 1, then stack next-to-next-to-top's size must also be 1. But it is '" + stack().peek(2) + "' of size '" + stack().peek(2).getSize() + "'.");
      }
    }
  }
  



  public void visitDUP2_X2(DUP2_X2 o)
  {
    if (stack().peek(0).getSize() == 2) {
      if (stack().peek(1).getSize() == 2) {
        return;
      }
      
      if (stack().peek(2).getSize() != 1) {
        constraintViolated(o, "If stack top's size is 2 and stack-next-to-top's size is 1, then stack next-to-next-to-top's size must also be 1. But it is '" + stack().peek(2) + "' of size '" + stack().peek(2).getSize() + "'.");


      }
      


    }
    else if (stack().peek(1).getSize() == 1) {
      if (stack().peek(2).getSize() == 2) {
        return;
      }
      
      if (stack().peek(3).getSize() == 1) {
        return;
      }
    }
    

    constraintViolated(o, "The operand sizes on the stack do not match any of the four forms of usage of this instruction.");
  }
  


  public void visitF2D(F2D o)
  {
    if (stack().peek() != Type.FLOAT) {
      constraintViolated(o, "The value at the stack top is not of type 'float', but of type '" + stack().peek() + "'.");
    }
  }
  


  public void visitF2I(F2I o)
  {
    if (stack().peek() != Type.FLOAT) {
      constraintViolated(o, "The value at the stack top is not of type 'float', but of type '" + stack().peek() + "'.");
    }
  }
  


  public void visitF2L(F2L o)
  {
    if (stack().peek() != Type.FLOAT) {
      constraintViolated(o, "The value at the stack top is not of type 'float', but of type '" + stack().peek() + "'.");
    }
  }
  


  public void visitFADD(FADD o)
  {
    if (stack().peek() != Type.FLOAT) {
      constraintViolated(o, "The value at the stack top is not of type 'float', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.FLOAT) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'float', but of type '" + stack().peek(1) + "'.");
    }
  }
  


  public void visitFALOAD(org.apache.bcel.generic.FALOAD o)
  {
    indexOfInt(o, stack().peek());
    if (stack().peek(1) == Type.NULL) {
      return;
    }
    if (!(stack().peek(1) instanceof ArrayType)) {
      constraintViolated(o, "Stack next-to-top must be of type float[] but is '" + stack().peek(1) + "'.");
    }
    Type t = ((ArrayType)stack().peek(1)).getBasicType();
    if (t != Type.FLOAT) {
      constraintViolated(o, "Stack next-to-top must be of type float[] but is '" + stack().peek(1) + "'.");
    }
  }
  


  public void visitFASTORE(org.apache.bcel.generic.FASTORE o)
  {
    if (stack().peek() != Type.FLOAT) {
      constraintViolated(o, "The value at the stack top is not of type 'float', but of type '" + stack().peek() + "'.");
    }
    indexOfInt(o, stack().peek(1));
    if (stack().peek(2) == Type.NULL) {
      return;
    }
    if (!(stack().peek(2) instanceof ArrayType)) {
      constraintViolated(o, "Stack next-to-next-to-top must be of type float[] but is '" + stack().peek(2) + "'.");
    }
    Type t = ((ArrayType)stack().peek(2)).getBasicType();
    if (t != Type.FLOAT) {
      constraintViolated(o, "Stack next-to-next-to-top must be of type float[] but is '" + stack().peek(2) + "'.");
    }
  }
  


  public void visitFCMPG(org.apache.bcel.generic.FCMPG o)
  {
    if (stack().peek() != Type.FLOAT) {
      constraintViolated(o, "The value at the stack top is not of type 'float', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.FLOAT) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'float', but of type '" + stack().peek(1) + "'.");
    }
  }
  


  public void visitFCMPL(FCMPL o)
  {
    if (stack().peek() != Type.FLOAT) {
      constraintViolated(o, "The value at the stack top is not of type 'float', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.FLOAT) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'float', but of type '" + stack().peek(1) + "'.");
    }
  }
  




  public void visitFCONST(FCONST o) {}
  



  public void visitFDIV(org.apache.bcel.generic.FDIV o)
  {
    if (stack().peek() != Type.FLOAT) {
      constraintViolated(o, "The value at the stack top is not of type 'float', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.FLOAT) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'float', but of type '" + stack().peek(1) + "'.");
    }
  }
  





  public void visitFLOAD(org.apache.bcel.generic.FLOAD o) {}
  




  public void visitFMUL(org.apache.bcel.generic.FMUL o)
  {
    if (stack().peek() != Type.FLOAT) {
      constraintViolated(o, "The value at the stack top is not of type 'float', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.FLOAT) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'float', but of type '" + stack().peek(1) + "'.");
    }
  }
  


  public void visitFNEG(org.apache.bcel.generic.FNEG o)
  {
    if (stack().peek() != Type.FLOAT) {
      constraintViolated(o, "The value at the stack top is not of type 'float', but of type '" + stack().peek() + "'.");
    }
  }
  


  public void visitFREM(org.apache.bcel.generic.FREM o)
  {
    if (stack().peek() != Type.FLOAT) {
      constraintViolated(o, "The value at the stack top is not of type 'float', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.FLOAT) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'float', but of type '" + stack().peek(1) + "'.");
    }
  }
  


  public void visitFRETURN(FRETURN o)
  {
    if (stack().peek() != Type.FLOAT) {
      constraintViolated(o, "The value at the stack top is not of type 'float', but of type '" + stack().peek() + "'.");
    }
  }
  





  public void visitFSTORE(org.apache.bcel.generic.FSTORE o) {}
  




  public void visitFSUB(org.apache.bcel.generic.FSUB o)
  {
    if (stack().peek() != Type.FLOAT) {
      constraintViolated(o, "The value at the stack top is not of type 'float', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.FLOAT) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'float', but of type '" + stack().peek(1) + "'.");
    }
  }
  


  public void visitGETFIELD(org.apache.bcel.generic.GETFIELD o)
  {
    Type objectref = stack().peek();
    if ((!(objectref instanceof ObjectType)) && (objectref != Type.NULL)) {
      constraintViolated(o, "Stack top should be an object reference that's not an array reference, but is '" + objectref + "'.");
    }
    
    String field_name = o.getFieldName(cpg);
    
    JavaClass jc = Repository.lookupClass(o.getClassType(cpg).getClassName());
    Field[] fields = jc.getFields();
    Field f = null;
    for (int i = 0; i < fields.length; i++) {
      if (fields[i].getName().equals(field_name)) {
        f = fields[i];
        break;
      }
    }
    if (f == null) {
      throw new AssertionViolatedException("Field not found?!?");
    }
    
    if (f.isProtected()) {
      ObjectType classtype = o.getClassType(cpg);
      ObjectType curr = new ObjectType(mg.getClassName());
      
      if ((classtype.equals(curr)) || (curr.subclassOf(classtype)))
      {
        Type t = stack().peek();
        if (t == Type.NULL) {
          return;
        }
        if (!(t instanceof ObjectType)) {
          constraintViolated(o, "The 'objectref' must refer to an object that's not an array. Found instead: '" + t + "'.");
        }
        ObjectType objreftype = (ObjectType)t;
        if ((objreftype.equals(curr)) || (objreftype.subclassOf(curr))) {}
      }
    }
    







    if (f.isStatic()) {
      constraintViolated(o, "Referenced field '" + f + "' is static which it shouldn't be.");
    }
  }
  





  public void visitGETSTATIC(org.apache.bcel.generic.GETSTATIC o) {}
  




  public void visitGOTO(org.apache.bcel.generic.GOTO o) {}
  




  public void visitGOTO_W(org.apache.bcel.generic.GOTO_W o) {}
  




  public void visitI2B(org.apache.bcel.generic.I2B o)
  {
    if (stack().peek() != Type.INT) {
      constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + stack().peek() + "'.");
    }
  }
  


  public void visitI2C(org.apache.bcel.generic.I2C o)
  {
    if (stack().peek() != Type.INT) {
      constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + stack().peek() + "'.");
    }
  }
  


  public void visitI2D(org.apache.bcel.generic.I2D o)
  {
    if (stack().peek() != Type.INT) {
      constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + stack().peek() + "'.");
    }
  }
  


  public void visitI2F(org.apache.bcel.generic.I2F o)
  {
    if (stack().peek() != Type.INT) {
      constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + stack().peek() + "'.");
    }
  }
  


  public void visitI2L(org.apache.bcel.generic.I2L o)
  {
    if (stack().peek() != Type.INT) {
      constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + stack().peek() + "'.");
    }
  }
  


  public void visitI2S(org.apache.bcel.generic.I2S o)
  {
    if (stack().peek() != Type.INT) {
      constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + stack().peek() + "'.");
    }
  }
  


  public void visitIADD(org.apache.bcel.generic.IADD o)
  {
    if (stack().peek() != Type.INT) {
      constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.INT) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '" + stack().peek(1) + "'.");
    }
  }
  


  public void visitIALOAD(IALOAD o)
  {
    indexOfInt(o, stack().peek());
    if (stack().peek(1) == Type.NULL) {
      return;
    }
    if (!(stack().peek(1) instanceof ArrayType)) {
      constraintViolated(o, "Stack next-to-top must be of type int[] but is '" + stack().peek(1) + "'.");
    }
    Type t = ((ArrayType)stack().peek(1)).getBasicType();
    if (t != Type.INT) {
      constraintViolated(o, "Stack next-to-top must be of type int[] but is '" + stack().peek(1) + "'.");
    }
  }
  


  public void visitIAND(IAND o)
  {
    if (stack().peek() != Type.INT) {
      constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.INT) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '" + stack().peek(1) + "'.");
    }
  }
  


  public void visitIASTORE(org.apache.bcel.generic.IASTORE o)
  {
    if (stack().peek() != Type.INT) {
      constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + stack().peek() + "'.");
    }
    indexOfInt(o, stack().peek(1));
    if (stack().peek(2) == Type.NULL) {
      return;
    }
    if (!(stack().peek(2) instanceof ArrayType)) {
      constraintViolated(o, "Stack next-to-next-to-top must be of type int[] but is '" + stack().peek(2) + "'.");
    }
    Type t = ((ArrayType)stack().peek(2)).getBasicType();
    if (t != Type.INT) {
      constraintViolated(o, "Stack next-to-next-to-top must be of type int[] but is '" + stack().peek(2) + "'.");
    }
  }
  




  public void visitICONST(ICONST o) {}
  



  public void visitIDIV(org.apache.bcel.generic.IDIV o)
  {
    if (stack().peek() != Type.INT) {
      constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.INT) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '" + stack().peek(1) + "'.");
    }
  }
  


  public void visitIF_ACMPEQ(org.apache.bcel.generic.IF_ACMPEQ o)
  {
    if (!(stack().peek() instanceof ReferenceType)) {
      constraintViolated(o, "The value at the stack top is not of a ReferenceType, but of type '" + stack().peek() + "'.");
    }
    referenceTypeIsInitialized(o, (ReferenceType)stack().peek());
    
    if (!(stack().peek(1) instanceof ReferenceType)) {
      constraintViolated(o, "The value at the stack next-to-top is not of a ReferenceType, but of type '" + stack().peek(1) + "'.");
    }
    referenceTypeIsInitialized(o, (ReferenceType)stack().peek(1));
  }
  



  public void visitIF_ACMPNE(IF_ACMPNE o)
  {
    if (!(stack().peek() instanceof ReferenceType)) {
      constraintViolated(o, "The value at the stack top is not of a ReferenceType, but of type '" + stack().peek() + "'.");
      referenceTypeIsInitialized(o, (ReferenceType)stack().peek());
    }
    if (!(stack().peek(1) instanceof ReferenceType)) {
      constraintViolated(o, "The value at the stack next-to-top is not of a ReferenceType, but of type '" + stack().peek(1) + "'.");
      referenceTypeIsInitialized(o, (ReferenceType)stack().peek(1));
    }
  }
  


  public void visitIF_ICMPEQ(org.apache.bcel.generic.IF_ICMPEQ o)
  {
    if (stack().peek() != Type.INT) {
      constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.INT) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '" + stack().peek(1) + "'.");
    }
  }
  


  public void visitIF_ICMPGE(org.apache.bcel.generic.IF_ICMPGE o)
  {
    if (stack().peek() != Type.INT) {
      constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.INT) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '" + stack().peek(1) + "'.");
    }
  }
  


  public void visitIF_ICMPGT(org.apache.bcel.generic.IF_ICMPGT o)
  {
    if (stack().peek() != Type.INT) {
      constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.INT) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '" + stack().peek(1) + "'.");
    }
  }
  


  public void visitIF_ICMPLE(org.apache.bcel.generic.IF_ICMPLE o)
  {
    if (stack().peek() != Type.INT) {
      constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.INT) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '" + stack().peek(1) + "'.");
    }
  }
  


  public void visitIF_ICMPLT(org.apache.bcel.generic.IF_ICMPLT o)
  {
    if (stack().peek() != Type.INT) {
      constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.INT) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '" + stack().peek(1) + "'.");
    }
  }
  


  public void visitIF_ICMPNE(org.apache.bcel.generic.IF_ICMPNE o)
  {
    if (stack().peek() != Type.INT) {
      constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.INT) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '" + stack().peek(1) + "'.");
    }
  }
  


  public void visitIFEQ(IFEQ o)
  {
    if (stack().peek() != Type.INT) {
      constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + stack().peek() + "'.");
    }
  }
  


  public void visitIFGE(IFGE o)
  {
    if (stack().peek() != Type.INT) {
      constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + stack().peek() + "'.");
    }
  }
  


  public void visitIFGT(IFGT o)
  {
    if (stack().peek() != Type.INT) {
      constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + stack().peek() + "'.");
    }
  }
  


  public void visitIFLE(org.apache.bcel.generic.IFLE o)
  {
    if (stack().peek() != Type.INT) {
      constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + stack().peek() + "'.");
    }
  }
  


  public void visitIFLT(IFLT o)
  {
    if (stack().peek() != Type.INT) {
      constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + stack().peek() + "'.");
    }
  }
  


  public void visitIFNE(org.apache.bcel.generic.IFNE o)
  {
    if (stack().peek() != Type.INT) {
      constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + stack().peek() + "'.");
    }
  }
  


  public void visitIFNONNULL(IFNONNULL o)
  {
    if (!(stack().peek() instanceof ReferenceType)) {
      constraintViolated(o, "The value at the stack top is not of a ReferenceType, but of type '" + stack().peek() + "'.");
    }
    referenceTypeIsInitialized(o, (ReferenceType)stack().peek());
  }
  


  public void visitIFNULL(org.apache.bcel.generic.IFNULL o)
  {
    if (!(stack().peek() instanceof ReferenceType)) {
      constraintViolated(o, "The value at the stack top is not of a ReferenceType, but of type '" + stack().peek() + "'.");
    }
    referenceTypeIsInitialized(o, (ReferenceType)stack().peek());
  }
  



  public void visitIINC(IINC o)
  {
    if (locals().maxLocals() <= (o.getType(cpg).getSize() == 1 ? o.getIndex() : o.getIndex() + 1)) {
      constraintViolated(o, "The 'index' is not a valid index into the local variable array.");
    }
    
    indexOfInt(o, locals().get(o.getIndex()));
  }
  




  public void visitILOAD(org.apache.bcel.generic.ILOAD o) {}
  



  public void visitIMPDEP1(IMPDEP1 o)
  {
    throw new AssertionViolatedException("In this JustIce verification pass there should not occur an illegal instruction such as IMPDEP1.");
  }
  


  public void visitIMPDEP2(IMPDEP2 o)
  {
    throw new AssertionViolatedException("In this JustIce verification pass there should not occur an illegal instruction such as IMPDEP2.");
  }
  


  public void visitIMUL(IMUL o)
  {
    if (stack().peek() != Type.INT) {
      constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.INT) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '" + stack().peek(1) + "'.");
    }
  }
  


  public void visitINEG(INEG o)
  {
    if (stack().peek() != Type.INT) {
      constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + stack().peek() + "'.");
    }
  }
  



  public void visitINSTANCEOF(org.apache.bcel.generic.INSTANCEOF o)
  {
    Type objectref = stack().peek(0);
    if (!(objectref instanceof ReferenceType)) {
      constraintViolated(o, "The 'objectref' is not of a ReferenceType but of type " + objectref + ".");
    }
    else {
      referenceTypeIsInitialized(o, (ReferenceType)objectref);
    }
    


    Constant c = cpg.getConstant(o.getIndex());
    if (!(c instanceof org.apache.bcel.classfile.ConstantClass)) {
      constraintViolated(o, "The Constant at 'index' is not a ConstantClass, but '" + c + "'.");
    }
  }
  




  public void visitINVOKEINTERFACE(INVOKEINTERFACE o)
  {
    int count = o.getCount();
    if (count == 0) {
      constraintViolated(o, "The 'count' argument must not be 0.");
    }
    
    ConstantInterfaceMethodref cimr = (ConstantInterfaceMethodref)cpg.getConstant(o.getIndex());
    


    Type t = o.getType(cpg);
    if ((t instanceof ObjectType)) {
      String name = ((ObjectType)t).getClassName();
      Verifier v = VerifierFactory.getVerifier(name);
      VerificationResult vr = v.doPass2();
      if (vr.getStatus() != 1) {
        constraintViolated(o, "Class '" + name + "' is referenced, but cannot be loaded and resolved: '" + vr + "'.");
      }
    }
    

    Type[] argtypes = o.getArgumentTypes(cpg);
    int nargs = argtypes.length;
    
    for (int i = nargs - 1; i >= 0; i--) {
      Type fromStack = stack().peek(nargs - 1 - i);
      Type fromDesc = argtypes[i];
      if ((fromDesc == Type.BOOLEAN) || (fromDesc == Type.BYTE) || (fromDesc == Type.CHAR) || (fromDesc == Type.SHORT))
      {


        fromDesc = Type.INT;
      }
      if (!fromStack.equals(fromDesc)) { ReferenceType rFromDesc;
        if (((fromStack instanceof ReferenceType)) && ((fromDesc instanceof ReferenceType))) {
          ReferenceType rFromStack = (ReferenceType)fromStack;
          rFromDesc = (ReferenceType)fromDesc;


        }
        else
        {


          constraintViolated(o, "Expecting a '" + fromDesc + "' but found a '" + fromStack + "' on the stack.");
        }
      }
    }
    
    Type objref = stack().peek(nargs);
    if (objref == Type.NULL) {
      return;
    }
    if (!(objref instanceof ReferenceType)) {
      constraintViolated(o, "Expecting a reference type as 'objectref' on the stack, not a '" + objref + "'.");
    }
    referenceTypeIsInitialized(o, (ReferenceType)objref);
    if (!(objref instanceof ObjectType)) {
      if (!(objref instanceof ArrayType)) {
        constraintViolated(o, "Expecting an ObjectType as 'objectref' on the stack, not a '" + objref + "'.");
      }
      else {
        objref = GENERIC_ARRAY;
      }
    }
    
    String objref_classname = ((ObjectType)objref).getClassName();
    
    String theInterface = o.getClassName(cpg);
    






    int counted_count = 1;
    for (int i = 0; i < nargs; i++) {
      counted_count += argtypes[i].getSize();
    }
    if (count != counted_count) {
      constraintViolated(o, "The 'count' argument should probably read '" + counted_count + "' but is '" + count + "'.");
    }
  }
  



  public void visitINVOKESPECIAL(org.apache.bcel.generic.INVOKESPECIAL o)
  {
    if ((o.getMethodName(cpg).equals("<init>")) && (!(stack().peek(o.getArgumentTypes(cpg).length) instanceof UninitializedObjectType))) {
      constraintViolated(o, "Possibly initializing object twice. A valid instruction sequence must not have an uninitialized object on the operand stack or in a local variable during a backwards branch, or in a local variable in code protected by an exception handler. Please see The Java Virtual Machine Specification, Second Edition, 4.9.4 (pages 147 and 148) for details.");
    }
    


    Type t = o.getType(cpg);
    if ((t instanceof ObjectType)) {
      String name = ((ObjectType)t).getClassName();
      Verifier v = VerifierFactory.getVerifier(name);
      VerificationResult vr = v.doPass2();
      if (vr.getStatus() != 1) {
        constraintViolated(o, "Class '" + name + "' is referenced, but cannot be loaded and resolved: '" + vr + "'.");
      }
    }
    

    Type[] argtypes = o.getArgumentTypes(cpg);
    int nargs = argtypes.length;
    
    for (int i = nargs - 1; i >= 0; i--) {
      Type fromStack = stack().peek(nargs - 1 - i);
      Type fromDesc = argtypes[i];
      if ((fromDesc == Type.BOOLEAN) || (fromDesc == Type.BYTE) || (fromDesc == Type.CHAR) || (fromDesc == Type.SHORT))
      {


        fromDesc = Type.INT;
      }
      if (!fromStack.equals(fromDesc)) { ReferenceType rFromDesc;
        if (((fromStack instanceof ReferenceType)) && ((fromDesc instanceof ReferenceType))) {
          ReferenceType rFromStack = (ReferenceType)fromStack;
          rFromDesc = (ReferenceType)fromDesc;


        }
        else
        {


          constraintViolated(o, "Expecting a '" + fromDesc + "' but found a '" + fromStack + "' on the stack.");
        }
      }
    }
    
    Type objref = stack().peek(nargs);
    if (objref == Type.NULL) {
      return;
    }
    if (!(objref instanceof ReferenceType)) {
      constraintViolated(o, "Expecting a reference type as 'objectref' on the stack, not a '" + objref + "'.");
    }
    String objref_classname = null;
    if (!o.getMethodName(cpg).equals("<init>")) {
      referenceTypeIsInitialized(o, (ReferenceType)objref);
      if (!(objref instanceof ObjectType)) {
        if (!(objref instanceof ArrayType)) {
          constraintViolated(o, "Expecting an ObjectType as 'objectref' on the stack, not a '" + objref + "'.");
        }
        else {
          objref = GENERIC_ARRAY;
        }
      }
      
      objref_classname = ((ObjectType)objref).getClassName();
    }
    else {
      if (!(objref instanceof UninitializedObjectType)) {
        constraintViolated(o, "Expecting an UninitializedObjectType as 'objectref' on the stack, not a '" + objref + "'. Otherwise, you couldn't invoke a method since an array has no methods (not to speak of a return address).");
      }
      objref_classname = ((UninitializedObjectType)objref).getInitialized().getClassName();
    }
    

    String theClass = o.getClassName(cpg);
    if (!Repository.instanceOf(objref_classname, theClass)) {
      constraintViolated(o, "The 'objref' item '" + objref + "' does not implement '" + theClass + "' as expected.");
    }
  }
  





  public void visitINVOKESTATIC(INVOKESTATIC o)
  {
    Type t = o.getType(cpg);
    if ((t instanceof ObjectType)) {
      String name = ((ObjectType)t).getClassName();
      Verifier v = VerifierFactory.getVerifier(name);
      VerificationResult vr = v.doPass2();
      if (vr.getStatus() != 1) {
        constraintViolated(o, "Class '" + name + "' is referenced, but cannot be loaded and resolved: '" + vr + "'.");
      }
    }
    
    Type[] argtypes = o.getArgumentTypes(cpg);
    int nargs = argtypes.length;
    
    for (int i = nargs - 1; i >= 0; i--) {
      Type fromStack = stack().peek(nargs - 1 - i);
      Type fromDesc = argtypes[i];
      if ((fromDesc == Type.BOOLEAN) || (fromDesc == Type.BYTE) || (fromDesc == Type.CHAR) || (fromDesc == Type.SHORT))
      {


        fromDesc = Type.INT;
      }
      if (!fromStack.equals(fromDesc)) { ReferenceType rFromDesc;
        if (((fromStack instanceof ReferenceType)) && ((fromDesc instanceof ReferenceType))) {
          ReferenceType rFromStack = (ReferenceType)fromStack;
          rFromDesc = (ReferenceType)fromDesc;


        }
        else
        {


          constraintViolated(o, "Expecting a '" + fromDesc + "' but found a '" + fromStack + "' on the stack.");
        }
      }
    }
  }
  




  public void visitINVOKEVIRTUAL(INVOKEVIRTUAL o)
  {
    Type t = o.getType(cpg);
    if ((t instanceof ObjectType)) {
      String name = ((ObjectType)t).getClassName();
      Verifier v = VerifierFactory.getVerifier(name);
      VerificationResult vr = v.doPass2();
      if (vr.getStatus() != 1) {
        constraintViolated(o, "Class '" + name + "' is referenced, but cannot be loaded and resolved: '" + vr + "'.");
      }
    }
    

    Type[] argtypes = o.getArgumentTypes(cpg);
    int nargs = argtypes.length;
    
    for (int i = nargs - 1; i >= 0; i--) {
      Type fromStack = stack().peek(nargs - 1 - i);
      Type fromDesc = argtypes[i];
      if ((fromDesc == Type.BOOLEAN) || (fromDesc == Type.BYTE) || (fromDesc == Type.CHAR) || (fromDesc == Type.SHORT))
      {


        fromDesc = Type.INT;
      }
      if (!fromStack.equals(fromDesc)) { ReferenceType rFromDesc;
        if (((fromStack instanceof ReferenceType)) && ((fromDesc instanceof ReferenceType))) {
          ReferenceType rFromStack = (ReferenceType)fromStack;
          rFromDesc = (ReferenceType)fromDesc;


        }
        else
        {


          constraintViolated(o, "Expecting a '" + fromDesc + "' but found a '" + fromStack + "' on the stack.");
        }
      }
    }
    
    Type objref = stack().peek(nargs);
    if (objref == Type.NULL) {
      return;
    }
    if (!(objref instanceof ReferenceType)) {
      constraintViolated(o, "Expecting a reference type as 'objectref' on the stack, not a '" + objref + "'.");
    }
    referenceTypeIsInitialized(o, (ReferenceType)objref);
    if (!(objref instanceof ObjectType)) {
      if (!(objref instanceof ArrayType)) {
        constraintViolated(o, "Expecting an ObjectType as 'objectref' on the stack, not a '" + objref + "'.");
      }
      else {
        objref = GENERIC_ARRAY;
      }
    }
    
    String objref_classname = ((ObjectType)objref).getClassName();
    
    String theClass = o.getClassName(cpg);
    
    if (!Repository.instanceOf(objref_classname, theClass)) {
      constraintViolated(o, "The 'objref' item '" + objref + "' does not implement '" + theClass + "' as expected.");
    }
  }
  


  public void visitIOR(org.apache.bcel.generic.IOR o)
  {
    if (stack().peek() != Type.INT) {
      constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.INT) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '" + stack().peek(1) + "'.");
    }
  }
  


  public void visitIREM(org.apache.bcel.generic.IREM o)
  {
    if (stack().peek() != Type.INT) {
      constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.INT) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '" + stack().peek(1) + "'.");
    }
  }
  


  public void visitIRETURN(org.apache.bcel.generic.IRETURN o)
  {
    if (stack().peek() != Type.INT) {
      constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + stack().peek() + "'.");
    }
  }
  


  public void visitISHL(org.apache.bcel.generic.ISHL o)
  {
    if (stack().peek() != Type.INT) {
      constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.INT) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '" + stack().peek(1) + "'.");
    }
  }
  


  public void visitISHR(org.apache.bcel.generic.ISHR o)
  {
    if (stack().peek() != Type.INT) {
      constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.INT) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '" + stack().peek(1) + "'.");
    }
  }
  





  public void visitISTORE(org.apache.bcel.generic.ISTORE o) {}
  




  public void visitISUB(org.apache.bcel.generic.ISUB o)
  {
    if (stack().peek() != Type.INT) {
      constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.INT) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '" + stack().peek(1) + "'.");
    }
  }
  


  public void visitIUSHR(org.apache.bcel.generic.IUSHR o)
  {
    if (stack().peek() != Type.INT) {
      constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.INT) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '" + stack().peek(1) + "'.");
    }
  }
  


  public void visitIXOR(org.apache.bcel.generic.IXOR o)
  {
    if (stack().peek() != Type.INT) {
      constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.INT) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '" + stack().peek(1) + "'.");
    }
  }
  




  public void visitJSR(org.apache.bcel.generic.JSR o) {}
  




  public void visitJSR_W(org.apache.bcel.generic.JSR_W o) {}
  




  public void visitL2D(org.apache.bcel.generic.L2D o)
  {
    if (stack().peek() != Type.LONG) {
      constraintViolated(o, "The value at the stack top is not of type 'long', but of type '" + stack().peek() + "'.");
    }
  }
  


  public void visitL2F(org.apache.bcel.generic.L2F o)
  {
    if (stack().peek() != Type.LONG) {
      constraintViolated(o, "The value at the stack top is not of type 'long', but of type '" + stack().peek() + "'.");
    }
  }
  


  public void visitL2I(org.apache.bcel.generic.L2I o)
  {
    if (stack().peek() != Type.LONG) {
      constraintViolated(o, "The value at the stack top is not of type 'long', but of type '" + stack().peek() + "'.");
    }
  }
  


  public void visitLADD(LADD o)
  {
    if (stack().peek() != Type.LONG) {
      constraintViolated(o, "The value at the stack top is not of type 'long', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.LONG) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'long', but of type '" + stack().peek(1) + "'.");
    }
  }
  


  public void visitLALOAD(org.apache.bcel.generic.LALOAD o)
  {
    indexOfInt(o, stack().peek());
    if (stack().peek(1) == Type.NULL) {
      return;
    }
    if (!(stack().peek(1) instanceof ArrayType)) {
      constraintViolated(o, "Stack next-to-top must be of type long[] but is '" + stack().peek(1) + "'.");
    }
    Type t = ((ArrayType)stack().peek(1)).getBasicType();
    if (t != Type.LONG) {
      constraintViolated(o, "Stack next-to-top must be of type long[] but is '" + stack().peek(1) + "'.");
    }
  }
  


  public void visitLAND(org.apache.bcel.generic.LAND o)
  {
    if (stack().peek() != Type.LONG) {
      constraintViolated(o, "The value at the stack top is not of type 'long', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.LONG) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'long', but of type '" + stack().peek(1) + "'.");
    }
  }
  


  public void visitLASTORE(org.apache.bcel.generic.LASTORE o)
  {
    if (stack().peek() != Type.LONG) {
      constraintViolated(o, "The value at the stack top is not of type 'long', but of type '" + stack().peek() + "'.");
    }
    indexOfInt(o, stack().peek(1));
    if (stack().peek(2) == Type.NULL) {
      return;
    }
    if (!(stack().peek(2) instanceof ArrayType)) {
      constraintViolated(o, "Stack next-to-next-to-top must be of type long[] but is '" + stack().peek(2) + "'.");
    }
    Type t = ((ArrayType)stack().peek(2)).getBasicType();
    if (t != Type.LONG) {
      constraintViolated(o, "Stack next-to-next-to-top must be of type long[] but is '" + stack().peek(2) + "'.");
    }
  }
  


  public void visitLCMP(LCMP o)
  {
    if (stack().peek() != Type.LONG) {
      constraintViolated(o, "The value at the stack top is not of type 'long', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.LONG) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'long', but of type '" + stack().peek(1) + "'.");
    }
  }
  





  public void visitLCONST(org.apache.bcel.generic.LCONST o) {}
  




  public void visitLDC(org.apache.bcel.generic.LDC o)
  {
    Constant c = cpg.getConstant(o.getIndex());
    if ((!(c instanceof ConstantInteger)) && (!(c instanceof org.apache.bcel.classfile.ConstantFloat)) && (!(c instanceof ConstantString)))
    {

      constraintViolated(o, "Referenced constant should be a CONSTANT_Integer, a CONSTANT_Float or a CONSTANT_String, but is '" + c + "'.");
    }
  }
  




  public void visitLDC_W(LDC_W o)
  {
    Constant c = cpg.getConstant(o.getIndex());
    if ((!(c instanceof ConstantInteger)) && (!(c instanceof org.apache.bcel.classfile.ConstantFloat)) && (!(c instanceof ConstantString)))
    {

      constraintViolated(o, "Referenced constant should be a CONSTANT_Integer, a CONSTANT_Float or a CONSTANT_String, but is '" + c + "'.");
    }
  }
  




  public void visitLDC2_W(org.apache.bcel.generic.LDC2_W o)
  {
    Constant c = cpg.getConstant(o.getIndex());
    if ((!(c instanceof org.apache.bcel.classfile.ConstantLong)) && (!(c instanceof org.apache.bcel.classfile.ConstantDouble)))
    {
      constraintViolated(o, "Referenced constant should be a CONSTANT_Integer, a CONSTANT_Float or a CONSTANT_String, but is '" + c + "'.");
    }
  }
  


  public void visitLDIV(LDIV o)
  {
    if (stack().peek() != Type.LONG) {
      constraintViolated(o, "The value at the stack top is not of type 'long', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.LONG) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'long', but of type '" + stack().peek(1) + "'.");
    }
  }
  





  public void visitLLOAD(LLOAD o) {}
  




  public void visitLMUL(org.apache.bcel.generic.LMUL o)
  {
    if (stack().peek() != Type.LONG) {
      constraintViolated(o, "The value at the stack top is not of type 'long', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.LONG) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'long', but of type '" + stack().peek(1) + "'.");
    }
  }
  


  public void visitLNEG(org.apache.bcel.generic.LNEG o)
  {
    if (stack().peek() != Type.LONG) {
      constraintViolated(o, "The value at the stack top is not of type 'long', but of type '" + stack().peek() + "'.");
    }
  }
  


  public void visitLOOKUPSWITCH(org.apache.bcel.generic.LOOKUPSWITCH o)
  {
    if (stack().peek() != Type.INT) {
      constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + stack().peek() + "'.");
    }
  }
  



  public void visitLOR(LOR o)
  {
    if (stack().peek() != Type.LONG) {
      constraintViolated(o, "The value at the stack top is not of type 'long', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.LONG) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'long', but of type '" + stack().peek(1) + "'.");
    }
  }
  


  public void visitLREM(LREM o)
  {
    if (stack().peek() != Type.LONG) {
      constraintViolated(o, "The value at the stack top is not of type 'long', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.LONG) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'long', but of type '" + stack().peek(1) + "'.");
    }
  }
  


  public void visitLRETURN(LRETURN o)
  {
    if (stack().peek() != Type.LONG) {
      constraintViolated(o, "The value at the stack top is not of type 'long', but of type '" + stack().peek() + "'.");
    }
  }
  


  public void visitLSHL(LSHL o)
  {
    if (stack().peek() != Type.INT) {
      constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.LONG) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'long', but of type '" + stack().peek(1) + "'.");
    }
  }
  


  public void visitLSHR(LSHR o)
  {
    if (stack().peek() != Type.INT) {
      constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.LONG) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'long', but of type '" + stack().peek(1) + "'.");
    }
  }
  





  public void visitLSTORE(LSTORE o) {}
  




  public void visitLSUB(org.apache.bcel.generic.LSUB o)
  {
    if (stack().peek() != Type.LONG) {
      constraintViolated(o, "The value at the stack top is not of type 'long', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.LONG) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'long', but of type '" + stack().peek(1) + "'.");
    }
  }
  


  public void visitLUSHR(org.apache.bcel.generic.LUSHR o)
  {
    if (stack().peek() != Type.INT) {
      constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.LONG) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'long', but of type '" + stack().peek(1) + "'.");
    }
  }
  


  public void visitLXOR(org.apache.bcel.generic.LXOR o)
  {
    if (stack().peek() != Type.LONG) {
      constraintViolated(o, "The value at the stack top is not of type 'long', but of type '" + stack().peek() + "'.");
    }
    if (stack().peek(1) != Type.LONG) {
      constraintViolated(o, "The value at the stack next-to-top is not of type 'long', but of type '" + stack().peek(1) + "'.");
    }
  }
  


  public void visitMONITORENTER(MONITORENTER o)
  {
    if (!(stack().peek() instanceof ReferenceType)) {
      constraintViolated(o, "The stack top should be of a ReferenceType, but is '" + stack().peek() + "'.");
    }
    referenceTypeIsInitialized(o, (ReferenceType)stack().peek());
  }
  


  public void visitMONITOREXIT(org.apache.bcel.generic.MONITOREXIT o)
  {
    if (!(stack().peek() instanceof ReferenceType)) {
      constraintViolated(o, "The stack top should be of a ReferenceType, but is '" + stack().peek() + "'.");
    }
    referenceTypeIsInitialized(o, (ReferenceType)stack().peek());
  }
  


  public void visitMULTIANEWARRAY(MULTIANEWARRAY o)
  {
    int dimensions = o.getDimensions();
    
    for (int i = 0; i < dimensions; i++) {
      if (stack().peek(i) != Type.INT) {
        constraintViolated(o, "The '" + dimensions + "' upper stack types should be 'int' but aren't.");
      }
    }
  }
  







  public void visitNEW(org.apache.bcel.generic.NEW o)
  {
    Type t = o.getType(cpg);
    if (!(t instanceof ReferenceType)) {
      throw new AssertionViolatedException("NEW.getType() returning a non-reference type?!");
    }
    if (!(t instanceof ObjectType)) {
      constraintViolated(o, "Expecting a class type (ObjectType) to work on. Found: '" + t + "'.");
    }
    ObjectType obj = (ObjectType)t;
    

    if (!obj.referencesClass()) {
      constraintViolated(o, "Expecting a class type (ObjectType) to work on. Found: '" + obj + "'.");
    }
  }
  


  public void visitNEWARRAY(org.apache.bcel.generic.NEWARRAY o)
  {
    if (stack().peek() != Type.INT) {
      constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + stack().peek() + "'.");
    }
  }
  




  public void visitNOP(NOP o) {}
  



  public void visitPOP(org.apache.bcel.generic.POP o)
  {
    if (stack().peek().getSize() != 1) {
      constraintViolated(o, "Stack top size should be 1 but stack top is '" + stack().peek() + "' of size '" + stack().peek().getSize() + "'.");
    }
  }
  


  public void visitPOP2(org.apache.bcel.generic.POP2 o)
  {
    if (stack().peek().getSize() != 2) {
      constraintViolated(o, "Stack top size should be 2 but stack top is '" + stack().peek() + "' of size '" + stack().peek().getSize() + "'.");
    }
  }
  



  public void visitPUTFIELD(org.apache.bcel.generic.PUTFIELD o)
  {
    Type objectref = stack().peek(1);
    if ((!(objectref instanceof ObjectType)) && (objectref != Type.NULL)) {
      constraintViolated(o, "Stack next-to-top should be an object reference that's not an array reference, but is '" + objectref + "'.");
    }
    
    String field_name = o.getFieldName(cpg);
    
    JavaClass jc = Repository.lookupClass(o.getClassType(cpg).getClassName());
    Field[] fields = jc.getFields();
    Field f = null;
    for (int i = 0; i < fields.length; i++) {
      if (fields[i].getName().equals(field_name)) {
        f = fields[i];
        break;
      }
    }
    if (f == null) {
      throw new AssertionViolatedException("Field not found?!?");
    }
    
    Type value = stack().peek();
    Type t = Type.getType(f.getSignature());
    Type shouldbe = t;
    if ((shouldbe == Type.BOOLEAN) || (shouldbe == Type.BYTE) || (shouldbe == Type.CHAR) || (shouldbe == Type.SHORT))
    {


      shouldbe = Type.INT;
    }
    if ((t instanceof ReferenceType)) {
      ReferenceType rvalue = null;
      if ((value instanceof ReferenceType)) {
        rvalue = (ReferenceType)value;
        referenceTypeIsInitialized(o, rvalue);
      }
      else {
        constraintViolated(o, "The stack top type '" + value + "' is not of a reference type as expected.");


      }
      



    }
    else if (shouldbe != value) {
      constraintViolated(o, "The stack top type '" + value + "' is not of type '" + shouldbe + "' as expected.");
    }
    

    if (f.isProtected()) {
      ObjectType classtype = o.getClassType(cpg);
      ObjectType curr = new ObjectType(mg.getClassName());
      
      if ((classtype.equals(curr)) || (curr.subclassOf(classtype)))
      {
        Type tp = stack().peek(1);
        if (tp == Type.NULL) {
          return;
        }
        if (!(tp instanceof ObjectType)) {
          constraintViolated(o, "The 'objectref' must refer to an object that's not an array. Found instead: '" + tp + "'.");
        }
        ObjectType objreftype = (ObjectType)tp;
        if ((!objreftype.equals(curr)) && (!objreftype.subclassOf(curr)))
        {
          constraintViolated(o, "The referenced field has the ACC_PROTECTED modifier, and it's a member of the current class or a superclass of the current class. However, the referenced object type '" + stack().peek() + "' is not the current class or a subclass of the current class.");
        }
      }
    }
    

    if (f.isStatic()) {
      constraintViolated(o, "Referenced field '" + f + "' is static which it shouldn't be.");
    }
  }
  


  public void visitPUTSTATIC(PUTSTATIC o)
  {
    String field_name = o.getFieldName(cpg);
    JavaClass jc = Repository.lookupClass(o.getClassType(cpg).getClassName());
    Field[] fields = jc.getFields();
    Field f = null;
    for (int i = 0; i < fields.length; i++) {
      if (fields[i].getName().equals(field_name)) {
        f = fields[i];
        break;
      }
    }
    if (f == null) {
      throw new AssertionViolatedException("Field not found?!?");
    }
    Type value = stack().peek();
    Type t = Type.getType(f.getSignature());
    Type shouldbe = t;
    if ((shouldbe == Type.BOOLEAN) || (shouldbe == Type.BYTE) || (shouldbe == Type.CHAR) || (shouldbe == Type.SHORT))
    {


      shouldbe = Type.INT;
    }
    if ((t instanceof ReferenceType)) {
      ReferenceType rvalue = null;
      if ((value instanceof ReferenceType)) {
        rvalue = (ReferenceType)value;
        referenceTypeIsInitialized(o, rvalue);
      }
      else {
        constraintViolated(o, "The stack top type '" + value + "' is not of a reference type as expected.");
      }
      if (!rvalue.isAssignmentCompatibleWith(shouldbe)) {
        constraintViolated(o, "The stack top type '" + value + "' is not assignment compatible with '" + shouldbe + "'.");
      }
      
    }
    else if (shouldbe != value) {
      constraintViolated(o, "The stack top type '" + value + "' is not of type '" + shouldbe + "' as expected.");
    }
  }
  





  public void visitRET(RET o)
  {
    if (!(locals().get(o.getIndex()) instanceof ReturnaddressType)) {
      constraintViolated(o, "Expecting a ReturnaddressType in local variable " + o.getIndex() + ".");
    }
    if (locals().get(o.getIndex()) == ReturnaddressType.NO_TARGET) {
      throw new AssertionViolatedException("Oops: RET expecting a target!");
    }
  }
  




  public void visitRETURN(RETURN o)
  {
    if ((mg.getName().equals("<init>")) && 
      (Frame._this != null) && (!mg.getClassName().equals(Type.OBJECT.getClassName()))) {
      constraintViolated(o, "Leaving a constructor that itself did not call a constructor.");
    }
  }
  



  public void visitSALOAD(org.apache.bcel.generic.SALOAD o)
  {
    indexOfInt(o, stack().peek());
    if (stack().peek(1) == Type.NULL) {
      return;
    }
    if (!(stack().peek(1) instanceof ArrayType)) {
      constraintViolated(o, "Stack next-to-top must be of type short[] but is '" + stack().peek(1) + "'.");
    }
    Type t = ((ArrayType)stack().peek(1)).getBasicType();
    if (t != Type.SHORT) {
      constraintViolated(o, "Stack next-to-top must be of type short[] but is '" + stack().peek(1) + "'.");
    }
  }
  


  public void visitSASTORE(org.apache.bcel.generic.SASTORE o)
  {
    if (stack().peek() != Type.INT) {
      constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + stack().peek() + "'.");
    }
    indexOfInt(o, stack().peek(1));
    if (stack().peek(2) == Type.NULL) {
      return;
    }
    if (!(stack().peek(2) instanceof ArrayType)) {
      constraintViolated(o, "Stack next-to-next-to-top must be of type short[] but is '" + stack().peek(2) + "'.");
    }
    Type t = ((ArrayType)stack().peek(2)).getBasicType();
    if (t != Type.SHORT) {
      constraintViolated(o, "Stack next-to-next-to-top must be of type short[] but is '" + stack().peek(2) + "'.");
    }
  }
  




  public void visitSIPUSH(org.apache.bcel.generic.SIPUSH o) {}
  



  public void visitSWAP(org.apache.bcel.generic.SWAP o)
  {
    if (stack().peek().getSize() != 1) {
      constraintViolated(o, "The value at the stack top is not of size '1', but of size '" + stack().peek().getSize() + "'.");
    }
    if (stack().peek(1).getSize() != 1) {
      constraintViolated(o, "The value at the stack next-to-top is not of size '1', but of size '" + stack().peek(1).getSize() + "'.");
    }
  }
  


  public void visitTABLESWITCH(org.apache.bcel.generic.TABLESWITCH o)
  {
    indexOfInt(o, stack().peek());
  }
}
