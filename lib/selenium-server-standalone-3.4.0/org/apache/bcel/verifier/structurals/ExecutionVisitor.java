package org.apache.bcel.verifier.structurals;

import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantFloat;
import org.apache.bcel.classfile.ConstantInteger;
import org.apache.bcel.classfile.ConstantString;
import org.apache.bcel.generic.AALOAD;
import org.apache.bcel.generic.AASTORE;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ANEWARRAY;
import org.apache.bcel.generic.ARRAYLENGTH;
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.BALOAD;
import org.apache.bcel.generic.BASTORE;
import org.apache.bcel.generic.CASTORE;
import org.apache.bcel.generic.CPInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.DADD;
import org.apache.bcel.generic.DCMPL;
import org.apache.bcel.generic.DDIV;
import org.apache.bcel.generic.DMUL;
import org.apache.bcel.generic.DNEG;
import org.apache.bcel.generic.DREM;
import org.apache.bcel.generic.DRETURN;
import org.apache.bcel.generic.DUP;
import org.apache.bcel.generic.DUP2;
import org.apache.bcel.generic.DUP_X1;
import org.apache.bcel.generic.DUP_X2;
import org.apache.bcel.generic.F2D;
import org.apache.bcel.generic.F2I;
import org.apache.bcel.generic.F2L;
import org.apache.bcel.generic.FADD;
import org.apache.bcel.generic.FALOAD;
import org.apache.bcel.generic.FCONST;
import org.apache.bcel.generic.FMUL;
import org.apache.bcel.generic.FRETURN;
import org.apache.bcel.generic.FSUB;
import org.apache.bcel.generic.FieldInstruction;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.GETSTATIC;
import org.apache.bcel.generic.GOTO_W;
import org.apache.bcel.generic.IADD;
import org.apache.bcel.generic.IALOAD;
import org.apache.bcel.generic.IAND;
import org.apache.bcel.generic.IASTORE;
import org.apache.bcel.generic.ICONST;
import org.apache.bcel.generic.IDIV;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.IFGE;
import org.apache.bcel.generic.IFGT;
import org.apache.bcel.generic.IFNE;
import org.apache.bcel.generic.IFNONNULL;
import org.apache.bcel.generic.IF_ACMPEQ;
import org.apache.bcel.generic.IF_ACMPNE;
import org.apache.bcel.generic.IF_ICMPGE;
import org.apache.bcel.generic.IF_ICMPLE;
import org.apache.bcel.generic.IF_ICMPLT;
import org.apache.bcel.generic.IINC;
import org.apache.bcel.generic.IMUL;
import org.apache.bcel.generic.INEG;
import org.apache.bcel.generic.INSTANCEOF;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.IOR;
import org.apache.bcel.generic.IREM;
import org.apache.bcel.generic.ISHL;
import org.apache.bcel.generic.ISHR;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.IUSHR;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.JSR;
import org.apache.bcel.generic.LADD;
import org.apache.bcel.generic.LAND;
import org.apache.bcel.generic.LASTORE;
import org.apache.bcel.generic.LCONST;
import org.apache.bcel.generic.LDC;
import org.apache.bcel.generic.LDIV;
import org.apache.bcel.generic.LLOAD;
import org.apache.bcel.generic.LMUL;
import org.apache.bcel.generic.LNEG;
import org.apache.bcel.generic.LOOKUPSWITCH;
import org.apache.bcel.generic.LOR;
import org.apache.bcel.generic.LREM;
import org.apache.bcel.generic.LRETURN;
import org.apache.bcel.generic.LSHL;
import org.apache.bcel.generic.LSHR;
import org.apache.bcel.generic.LocalVariableInstruction;
import org.apache.bcel.generic.MONITORENTER;
import org.apache.bcel.generic.MULTIANEWARRAY;
import org.apache.bcel.generic.NEWARRAY;
import org.apache.bcel.generic.NOP;
import org.apache.bcel.generic.POP;
import org.apache.bcel.generic.POP2;
import org.apache.bcel.generic.RETURN;
import org.apache.bcel.generic.ReturnaddressType;
import org.apache.bcel.generic.SASTORE;
import org.apache.bcel.generic.SIPUSH;
import org.apache.bcel.generic.SWAP;
import org.apache.bcel.generic.Type;

public class ExecutionVisitor extends org.apache.bcel.generic.EmptyVisitor implements org.apache.bcel.generic.Visitor
{
  private Frame frame = null;
  




  private ConstantPoolGen cpg = null;
  



  public ExecutionVisitor() {}
  



  private OperandStack stack()
  {
    return frame.getStack();
  }
  



  private LocalVariables locals()
  {
    return frame.getLocals();
  }
  


  public void setConstantPoolGen(ConstantPoolGen cpg)
  {
    this.cpg = cpg;
  }
  





  public void setFrame(Frame f)
  {
    frame = f;
  }
  










  public void visitAALOAD(AALOAD o)
  {
    stack().pop();
    
    Type t = stack().pop();
    if (t == Type.NULL) {
      stack().push(Type.NULL);
    }
    else {
      ArrayType at = (ArrayType)t;
      stack().push(at.getElementType());
    }
  }
  
  public void visitAASTORE(AASTORE o) {
    stack().pop();
    stack().pop();
    stack().pop();
  }
  
  public void visitACONST_NULL(org.apache.bcel.generic.ACONST_NULL o) {
    stack().push(Type.NULL);
  }
  
  public void visitALOAD(ALOAD o) {
    stack().push(locals().get(o.getIndex()));
  }
  
  public void visitANEWARRAY(ANEWARRAY o) {
    stack().pop();
    stack().push(new ArrayType(o.getType(cpg), 1));
  }
  
  public void visitARETURN(org.apache.bcel.generic.ARETURN o) {
    stack().pop();
  }
  
  public void visitARRAYLENGTH(ARRAYLENGTH o) {
    stack().pop();
    stack().push(Type.INT);
  }
  
  public void visitASTORE(org.apache.bcel.generic.ASTORE o)
  {
    locals().set(o.getIndex(), stack().pop());
  }
  

  public void visitATHROW(org.apache.bcel.generic.ATHROW o)
  {
    Type t = stack().pop();
    stack().clear();
    if (t.equals(Type.NULL)) {
      stack().push(Type.getType("Ljava/lang/NullPointerException;"));
    } else {
      stack().push(t);
    }
  }
  
  public void visitBALOAD(BALOAD o) {
    stack().pop();
    stack().pop();
    stack().push(Type.INT);
  }
  
  public void visitBASTORE(BASTORE o)
  {
    stack().pop();
    stack().pop();
    stack().pop();
  }
  
  public void visitBIPUSH(org.apache.bcel.generic.BIPUSH o)
  {
    stack().push(Type.INT);
  }
  
  public void visitCALOAD(org.apache.bcel.generic.CALOAD o)
  {
    stack().pop();
    stack().pop();
    stack().push(Type.INT);
  }
  
  public void visitCASTORE(CASTORE o) {
    stack().pop();
    stack().pop();
    stack().pop();
  }
  




  public void visitCHECKCAST(org.apache.bcel.generic.CHECKCAST o)
  {
    stack().pop();
    stack().push(o.getType(cpg));
  }
  
  public void visitD2F(org.apache.bcel.generic.D2F o)
  {
    stack().pop();
    stack().push(Type.FLOAT);
  }
  
  public void visitD2I(org.apache.bcel.generic.D2I o) {
    stack().pop();
    stack().push(Type.INT);
  }
  
  public void visitD2L(org.apache.bcel.generic.D2L o) {
    stack().pop();
    stack().push(Type.LONG);
  }
  
  public void visitDADD(DADD o) {
    stack().pop();
    stack().pop();
    stack().push(Type.DOUBLE);
  }
  
  public void visitDALOAD(org.apache.bcel.generic.DALOAD o) {
    stack().pop();
    stack().pop();
    stack().push(Type.DOUBLE);
  }
  
  public void visitDASTORE(org.apache.bcel.generic.DASTORE o) {
    stack().pop();
    stack().pop();
    stack().pop();
  }
  
  public void visitDCMPG(org.apache.bcel.generic.DCMPG o) {
    stack().pop();
    stack().pop();
    stack().push(Type.INT);
  }
  
  public void visitDCMPL(DCMPL o) {
    stack().pop();
    stack().pop();
    stack().push(Type.INT);
  }
  
  public void visitDCONST(org.apache.bcel.generic.DCONST o) {
    stack().push(Type.DOUBLE);
  }
  
  public void visitDDIV(DDIV o) {
    stack().pop();
    stack().pop();
    stack().push(Type.DOUBLE);
  }
  
  public void visitDLOAD(org.apache.bcel.generic.DLOAD o) {
    stack().push(Type.DOUBLE);
  }
  
  public void visitDMUL(DMUL o) {
    stack().pop();
    stack().pop();
    stack().push(Type.DOUBLE);
  }
  
  public void visitDNEG(DNEG o) {
    stack().pop();
    stack().push(Type.DOUBLE);
  }
  
  public void visitDREM(DREM o) {
    stack().pop();
    stack().pop();
    stack().push(Type.DOUBLE);
  }
  
  public void visitDRETURN(DRETURN o) {
    stack().pop();
  }
  
  public void visitDSTORE(org.apache.bcel.generic.DSTORE o) {
    locals().set(o.getIndex(), stack().pop());
    locals().set(o.getIndex() + 1, Type.UNKNOWN);
  }
  
  public void visitDSUB(org.apache.bcel.generic.DSUB o) {
    stack().pop();
    stack().pop();
    stack().push(Type.DOUBLE);
  }
  
  public void visitDUP(DUP o) {
    Type t = stack().pop();
    stack().push(t);
    stack().push(t);
  }
  
  public void visitDUP_X1(DUP_X1 o) {
    Type w1 = stack().pop();
    Type w2 = stack().pop();
    stack().push(w1);
    stack().push(w2);
    stack().push(w1);
  }
  
  public void visitDUP_X2(DUP_X2 o) {
    Type w1 = stack().pop();
    Type w2 = stack().pop();
    if (w2.getSize() == 2) {
      stack().push(w1);
      stack().push(w2);
      stack().push(w1);
    }
    else {
      Type w3 = stack().pop();
      stack().push(w1);
      stack().push(w3);
      stack().push(w2);
      stack().push(w1);
    }
  }
  
  public void visitDUP2(DUP2 o) {
    Type t = stack().pop();
    if (t.getSize() == 2) {
      stack().push(t);
      stack().push(t);
    }
    else {
      Type u = stack().pop();
      stack().push(u);
      stack().push(t);
      stack().push(u);
      stack().push(t);
    }
  }
  
  public void visitDUP2_X1(org.apache.bcel.generic.DUP2_X1 o) {
    Type t = stack().pop();
    if (t.getSize() == 2) {
      Type u = stack().pop();
      stack().push(t);
      stack().push(u);
      stack().push(t);
    }
    else {
      Type u = stack().pop();
      Type v = stack().pop();
      stack().push(u);
      stack().push(t);
      stack().push(v);
      stack().push(u);
      stack().push(t);
    }
  }
  
  public void visitDUP2_X2(org.apache.bcel.generic.DUP2_X2 o) {
    Type t = stack().pop();
    if (t.getSize() == 2) {
      Type u = stack().pop();
      if (u.getSize() == 2) {
        stack().push(t);
        stack().push(u);
        stack().push(t);
      } else {
        Type v = stack().pop();
        stack().push(t);
        stack().push(v);
        stack().push(u);
        stack().push(t);
      }
    }
    else {
      Type u = stack().pop();
      Type v = stack().pop();
      if (v.getSize() == 2) {
        stack().push(u);
        stack().push(t);
        stack().push(v);
        stack().push(u);
        stack().push(t);
      } else {
        Type w = stack().pop();
        stack().push(u);
        stack().push(t);
        stack().push(w);
        stack().push(v);
        stack().push(u);
        stack().push(t);
      }
    }
  }
  
  public void visitF2D(F2D o) {
    stack().pop();
    stack().push(Type.DOUBLE);
  }
  
  public void visitF2I(F2I o) {
    stack().pop();
    stack().push(Type.INT);
  }
  
  public void visitF2L(F2L o) {
    stack().pop();
    stack().push(Type.LONG);
  }
  
  public void visitFADD(FADD o) {
    stack().pop();
    stack().pop();
    stack().push(Type.FLOAT);
  }
  
  public void visitFALOAD(FALOAD o) {
    stack().pop();
    stack().pop();
    stack().push(Type.FLOAT);
  }
  
  public void visitFASTORE(org.apache.bcel.generic.FASTORE o) {
    stack().pop();
    stack().pop();
    stack().pop();
  }
  
  public void visitFCMPG(org.apache.bcel.generic.FCMPG o) {
    stack().pop();
    stack().pop();
    stack().push(Type.INT);
  }
  
  public void visitFCMPL(org.apache.bcel.generic.FCMPL o) {
    stack().pop();
    stack().pop();
    stack().push(Type.INT);
  }
  
  public void visitFCONST(FCONST o) {
    stack().push(Type.FLOAT);
  }
  
  public void visitFDIV(org.apache.bcel.generic.FDIV o) {
    stack().pop();
    stack().pop();
    stack().push(Type.FLOAT);
  }
  
  public void visitFLOAD(org.apache.bcel.generic.FLOAD o) {
    stack().push(Type.FLOAT);
  }
  
  public void visitFMUL(FMUL o) {
    stack().pop();
    stack().pop();
    stack().push(Type.FLOAT);
  }
  
  public void visitFNEG(org.apache.bcel.generic.FNEG o) {
    stack().pop();
    stack().push(Type.FLOAT);
  }
  
  public void visitFREM(org.apache.bcel.generic.FREM o) {
    stack().pop();
    stack().pop();
    stack().push(Type.FLOAT);
  }
  
  public void visitFRETURN(FRETURN o) {
    stack().pop();
  }
  
  public void visitFSTORE(org.apache.bcel.generic.FSTORE o) {
    locals().set(o.getIndex(), stack().pop());
  }
  
  public void visitFSUB(FSUB o) {
    stack().pop();
    stack().pop();
    stack().push(Type.FLOAT);
  }
  
  public void visitGETFIELD(GETFIELD o) {
    stack().pop();
    Type t = o.getFieldType(cpg);
    if ((t.equals(Type.BOOLEAN)) || (t.equals(Type.CHAR)) || (t.equals(Type.BYTE)) || (t.equals(Type.SHORT)))
    {


      t = Type.INT; }
    stack().push(t);
  }
  
  public void visitGETSTATIC(GETSTATIC o) {
    Type t = o.getFieldType(cpg);
    if ((t.equals(Type.BOOLEAN)) || (t.equals(Type.CHAR)) || (t.equals(Type.BYTE)) || (t.equals(Type.SHORT)))
    {


      t = Type.INT; }
    stack().push(t);
  }
  

  public void visitGOTO(org.apache.bcel.generic.GOTO o) {}
  

  public void visitGOTO_W(GOTO_W o) {}
  

  public void visitI2B(org.apache.bcel.generic.I2B o)
  {
    stack().pop();
    stack().push(Type.INT);
  }
  
  public void visitI2C(org.apache.bcel.generic.I2C o) {
    stack().pop();
    stack().push(Type.INT);
  }
  
  public void visitI2D(org.apache.bcel.generic.I2D o) {
    stack().pop();
    stack().push(Type.DOUBLE);
  }
  
  public void visitI2F(org.apache.bcel.generic.I2F o) {
    stack().pop();
    stack().push(Type.FLOAT);
  }
  
  public void visitI2L(org.apache.bcel.generic.I2L o) {
    stack().pop();
    stack().push(Type.LONG);
  }
  
  public void visitI2S(org.apache.bcel.generic.I2S o) {
    stack().pop();
    stack().push(Type.INT);
  }
  
  public void visitIADD(IADD o) {
    stack().pop();
    stack().pop();
    stack().push(Type.INT);
  }
  
  public void visitIALOAD(IALOAD o) {
    stack().pop();
    stack().pop();
    stack().push(Type.INT);
  }
  
  public void visitIAND(IAND o) {
    stack().pop();
    stack().pop();
    stack().push(Type.INT);
  }
  
  public void visitIASTORE(IASTORE o) {
    stack().pop();
    stack().pop();
    stack().pop();
  }
  
  public void visitICONST(ICONST o) {
    stack().push(Type.INT);
  }
  
  public void visitIDIV(IDIV o) {
    stack().pop();
    stack().pop();
    stack().push(Type.INT);
  }
  
  public void visitIF_ACMPEQ(IF_ACMPEQ o) {
    stack().pop();
    stack().pop();
  }
  
  public void visitIF_ACMPNE(IF_ACMPNE o) {
    stack().pop();
    stack().pop();
  }
  
  public void visitIF_ICMPEQ(org.apache.bcel.generic.IF_ICMPEQ o) {
    stack().pop();
    stack().pop();
  }
  
  public void visitIF_ICMPGE(IF_ICMPGE o) {
    stack().pop();
    stack().pop();
  }
  
  public void visitIF_ICMPGT(org.apache.bcel.generic.IF_ICMPGT o) {
    stack().pop();
    stack().pop();
  }
  
  public void visitIF_ICMPLE(IF_ICMPLE o) {
    stack().pop();
    stack().pop();
  }
  
  public void visitIF_ICMPLT(IF_ICMPLT o) {
    stack().pop();
    stack().pop();
  }
  
  public void visitIF_ICMPNE(org.apache.bcel.generic.IF_ICMPNE o) {
    stack().pop();
    stack().pop();
  }
  
  public void visitIFEQ(IFEQ o) {
    stack().pop();
  }
  
  public void visitIFGE(IFGE o) {
    stack().pop();
  }
  
  public void visitIFGT(IFGT o) {
    stack().pop();
  }
  
  public void visitIFLE(org.apache.bcel.generic.IFLE o) {
    stack().pop();
  }
  
  public void visitIFLT(org.apache.bcel.generic.IFLT o) {
    stack().pop();
  }
  
  public void visitIFNE(IFNE o) {
    stack().pop();
  }
  
  public void visitIFNONNULL(IFNONNULL o) {
    stack().pop();
  }
  
  public void visitIFNULL(org.apache.bcel.generic.IFNULL o) {
    stack().pop();
  }
  

  public void visitIINC(IINC o) {}
  
  public void visitILOAD(org.apache.bcel.generic.ILOAD o)
  {
    stack().push(Type.INT);
  }
  
  public void visitIMUL(IMUL o) {
    stack().pop();
    stack().pop();
    stack().push(Type.INT);
  }
  
  public void visitINEG(INEG o) {
    stack().pop();
    stack().push(Type.INT);
  }
  
  public void visitINSTANCEOF(INSTANCEOF o) {
    stack().pop();
    stack().push(Type.INT);
  }
  
  public void visitINVOKEINTERFACE(INVOKEINTERFACE o) {
    stack().pop();
    for (int i = 0; i < o.getArgumentTypes(cpg).length; i++) {
      stack().pop();
    }
    



    if (o.getReturnType(cpg) != Type.VOID) {
      Type t = o.getReturnType(cpg);
      if ((t.equals(Type.BOOLEAN)) || (t.equals(Type.CHAR)) || (t.equals(Type.BYTE)) || (t.equals(Type.SHORT)))
      {


        t = Type.INT; }
      stack().push(t);
    }
  }
  
  public void visitINVOKESPECIAL(INVOKESPECIAL o) {
    if (o.getMethodName(cpg).equals("<init>")) {
      UninitializedObjectType t = (UninitializedObjectType)stack().peek(o.getArgumentTypes(cpg).length);
      if (t == Frame._this) {
        Frame._this = null;
      }
      stack().initializeObject(t);
      locals().initializeObject(t);
    }
    stack().pop();
    for (int i = 0; i < o.getArgumentTypes(cpg).length; i++) {
      stack().pop();
    }
    



    if (o.getReturnType(cpg) != Type.VOID) {
      Type t = o.getReturnType(cpg);
      if ((t.equals(Type.BOOLEAN)) || (t.equals(Type.CHAR)) || (t.equals(Type.BYTE)) || (t.equals(Type.SHORT)))
      {


        t = Type.INT; }
      stack().push(t);
    }
  }
  
  public void visitINVOKESTATIC(INVOKESTATIC o) {
    for (int i = 0; i < o.getArgumentTypes(cpg).length; i++) {
      stack().pop();
    }
    



    if (o.getReturnType(cpg) != Type.VOID) {
      Type t = o.getReturnType(cpg);
      if ((t.equals(Type.BOOLEAN)) || (t.equals(Type.CHAR)) || (t.equals(Type.BYTE)) || (t.equals(Type.SHORT)))
      {


        t = Type.INT; }
      stack().push(t);
    }
  }
  
  public void visitINVOKEVIRTUAL(INVOKEVIRTUAL o) {
    stack().pop();
    for (int i = 0; i < o.getArgumentTypes(cpg).length; i++) {
      stack().pop();
    }
    



    if (o.getReturnType(cpg) != Type.VOID) {
      Type t = o.getReturnType(cpg);
      if ((t.equals(Type.BOOLEAN)) || (t.equals(Type.CHAR)) || (t.equals(Type.BYTE)) || (t.equals(Type.SHORT)))
      {


        t = Type.INT; }
      stack().push(t);
    }
  }
  
  public void visitIOR(IOR o) {
    stack().pop();
    stack().pop();
    stack().push(Type.INT);
  }
  
  public void visitIREM(IREM o) {
    stack().pop();
    stack().pop();
    stack().push(Type.INT);
  }
  
  public void visitIRETURN(org.apache.bcel.generic.IRETURN o) {
    stack().pop();
  }
  
  public void visitISHL(ISHL o) {
    stack().pop();
    stack().pop();
    stack().push(Type.INT);
  }
  
  public void visitISHR(ISHR o) {
    stack().pop();
    stack().pop();
    stack().push(Type.INT);
  }
  
  public void visitISTORE(ISTORE o) {
    locals().set(o.getIndex(), stack().pop());
  }
  
  public void visitISUB(org.apache.bcel.generic.ISUB o) {
    stack().pop();
    stack().pop();
    stack().push(Type.INT);
  }
  
  public void visitIUSHR(IUSHR o) {
    stack().pop();
    stack().pop();
    stack().push(Type.INT);
  }
  
  public void visitIXOR(org.apache.bcel.generic.IXOR o) {
    stack().pop();
    stack().pop();
    stack().push(Type.INT);
  }
  
  public void visitJSR(JSR o)
  {
    stack().push(new ReturnaddressType(o.physicalSuccessor()));
  }
  

  public void visitJSR_W(org.apache.bcel.generic.JSR_W o)
  {
    stack().push(new ReturnaddressType(o.physicalSuccessor()));
  }
  
  public void visitL2D(org.apache.bcel.generic.L2D o)
  {
    stack().pop();
    stack().push(Type.DOUBLE);
  }
  
  public void visitL2F(org.apache.bcel.generic.L2F o) {
    stack().pop();
    stack().push(Type.FLOAT);
  }
  
  public void visitL2I(org.apache.bcel.generic.L2I o) {
    stack().pop();
    stack().push(Type.INT);
  }
  
  public void visitLADD(LADD o) {
    stack().pop();
    stack().pop();
    stack().push(Type.LONG);
  }
  
  public void visitLALOAD(org.apache.bcel.generic.LALOAD o) {
    stack().pop();
    stack().pop();
    stack().push(Type.LONG);
  }
  
  public void visitLAND(LAND o) {
    stack().pop();
    stack().pop();
    stack().push(Type.LONG);
  }
  
  public void visitLASTORE(LASTORE o) {
    stack().pop();
    stack().pop();
    stack().pop();
  }
  
  public void visitLCMP(org.apache.bcel.generic.LCMP o) {
    stack().pop();
    stack().pop();
    stack().push(Type.INT);
  }
  
  public void visitLCONST(LCONST o) {
    stack().push(Type.LONG);
  }
  
  public void visitLDC(LDC o) {
    Constant c = cpg.getConstant(o.getIndex());
    if ((c instanceof ConstantInteger)) {
      stack().push(Type.INT);
    }
    if ((c instanceof ConstantFloat)) {
      stack().push(Type.FLOAT);
    }
    if ((c instanceof ConstantString)) {
      stack().push(Type.STRING);
    }
  }
  
  public void visitLDC_W(org.apache.bcel.generic.LDC_W o) {
    Constant c = cpg.getConstant(o.getIndex());
    if ((c instanceof ConstantInteger)) {
      stack().push(Type.INT);
    }
    if ((c instanceof ConstantFloat)) {
      stack().push(Type.FLOAT);
    }
    if ((c instanceof ConstantString)) {
      stack().push(Type.STRING);
    }
  }
  
  public void visitLDC2_W(org.apache.bcel.generic.LDC2_W o) {
    Constant c = cpg.getConstant(o.getIndex());
    if ((c instanceof org.apache.bcel.classfile.ConstantLong)) {
      stack().push(Type.LONG);
    }
    if ((c instanceof org.apache.bcel.classfile.ConstantDouble)) {
      stack().push(Type.DOUBLE);
    }
  }
  
  public void visitLDIV(LDIV o) {
    stack().pop();
    stack().pop();
    stack().push(Type.LONG);
  }
  
  public void visitLLOAD(LLOAD o) {
    stack().push(locals().get(o.getIndex()));
  }
  
  public void visitLMUL(LMUL o) {
    stack().pop();
    stack().pop();
    stack().push(Type.LONG);
  }
  
  public void visitLNEG(LNEG o) {
    stack().pop();
    stack().push(Type.LONG);
  }
  
  public void visitLOOKUPSWITCH(LOOKUPSWITCH o) {
    stack().pop();
  }
  
  public void visitLOR(LOR o) {
    stack().pop();
    stack().pop();
    stack().push(Type.LONG);
  }
  
  public void visitLREM(LREM o) {
    stack().pop();
    stack().pop();
    stack().push(Type.LONG);
  }
  
  public void visitLRETURN(LRETURN o) {
    stack().pop();
  }
  
  public void visitLSHL(LSHL o) {
    stack().pop();
    stack().pop();
    stack().push(Type.LONG);
  }
  
  public void visitLSHR(LSHR o) {
    stack().pop();
    stack().pop();
    stack().push(Type.LONG);
  }
  
  public void visitLSTORE(org.apache.bcel.generic.LSTORE o) {
    locals().set(o.getIndex(), stack().pop());
    locals().set(o.getIndex() + 1, Type.UNKNOWN);
  }
  
  public void visitLSUB(org.apache.bcel.generic.LSUB o) {
    stack().pop();
    stack().pop();
    stack().push(Type.LONG);
  }
  
  public void visitLUSHR(org.apache.bcel.generic.LUSHR o) {
    stack().pop();
    stack().pop();
    stack().push(Type.LONG);
  }
  
  public void visitLXOR(org.apache.bcel.generic.LXOR o) {
    stack().pop();
    stack().pop();
    stack().push(Type.LONG);
  }
  
  public void visitMONITORENTER(MONITORENTER o) {
    stack().pop();
  }
  
  public void visitMONITOREXIT(org.apache.bcel.generic.MONITOREXIT o) {
    stack().pop();
  }
  
  public void visitMULTIANEWARRAY(MULTIANEWARRAY o) {
    for (int i = 0; i < o.getDimensions(); i++) {
      stack().pop();
    }
    stack().push(o.getType(cpg));
  }
  
  public void visitNEW(org.apache.bcel.generic.NEW o) {
    stack().push(new UninitializedObjectType((org.apache.bcel.generic.ObjectType)o.getType(cpg)));
  }
  
  public void visitNEWARRAY(NEWARRAY o) {
    stack().pop();
    stack().push(o.getType());
  }
  
  public void visitNOP(NOP o) {}
  
  public void visitPOP(POP o)
  {
    stack().pop();
  }
  
  public void visitPOP2(POP2 o) {
    Type t = stack().pop();
    if (t.getSize() == 1) {
      stack().pop();
    }
  }
  
  public void visitPUTFIELD(org.apache.bcel.generic.PUTFIELD o) {
    stack().pop();
    stack().pop();
  }
  
  public void visitPUTSTATIC(org.apache.bcel.generic.PUTSTATIC o) {
    stack().pop();
  }
  


  public void visitRET(org.apache.bcel.generic.RET o) {}
  

  public void visitRETURN(RETURN o) {}
  

  public void visitSALOAD(org.apache.bcel.generic.SALOAD o)
  {
    stack().pop();
    stack().pop();
    stack().push(Type.INT);
  }
  
  public void visitSASTORE(SASTORE o) {
    stack().pop();
    stack().pop();
    stack().pop();
  }
  
  public void visitSIPUSH(SIPUSH o) {
    stack().push(Type.INT);
  }
  
  public void visitSWAP(SWAP o) {
    Type t = stack().pop();
    Type u = stack().pop();
    stack().push(t);
    stack().push(u);
  }
  
  public void visitTABLESWITCH(org.apache.bcel.generic.TABLESWITCH o) {
    stack().pop();
  }
}
