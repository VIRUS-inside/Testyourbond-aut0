package org.apache.bcel.generic;











































































public abstract interface InstructionConstants
{
  public static final Instruction NOP = new NOP();
  public static final Instruction ACONST_NULL = new ACONST_NULL();
  public static final Instruction ICONST_M1 = new ICONST(-1);
  public static final Instruction ICONST_0 = new ICONST(0);
  public static final Instruction ICONST_1 = new ICONST(1);
  public static final Instruction ICONST_2 = new ICONST(2);
  public static final Instruction ICONST_3 = new ICONST(3);
  public static final Instruction ICONST_4 = new ICONST(4);
  public static final Instruction ICONST_5 = new ICONST(5);
  public static final Instruction LCONST_0 = new LCONST(0L);
  public static final Instruction LCONST_1 = new LCONST(1L);
  public static final Instruction FCONST_0 = new FCONST(0.0F);
  public static final Instruction FCONST_1 = new FCONST(1.0F);
  public static final Instruction FCONST_2 = new FCONST(2.0F);
  public static final Instruction DCONST_0 = new DCONST(0.0D);
  public static final Instruction DCONST_1 = new DCONST(1.0D);
  public static final ArrayInstruction IALOAD = new IALOAD();
  public static final ArrayInstruction LALOAD = new LALOAD();
  public static final ArrayInstruction FALOAD = new FALOAD();
  public static final ArrayInstruction DALOAD = new DALOAD();
  public static final ArrayInstruction AALOAD = new AALOAD();
  public static final ArrayInstruction BALOAD = new BALOAD();
  public static final ArrayInstruction CALOAD = new CALOAD();
  public static final ArrayInstruction SALOAD = new SALOAD();
  public static final ArrayInstruction IASTORE = new IASTORE();
  public static final ArrayInstruction LASTORE = new LASTORE();
  public static final ArrayInstruction FASTORE = new FASTORE();
  public static final ArrayInstruction DASTORE = new DASTORE();
  public static final ArrayInstruction AASTORE = new AASTORE();
  public static final ArrayInstruction BASTORE = new BASTORE();
  public static final ArrayInstruction CASTORE = new CASTORE();
  public static final ArrayInstruction SASTORE = new SASTORE();
  public static final StackInstruction POP = new POP();
  public static final StackInstruction POP2 = new POP2();
  public static final StackInstruction DUP = new DUP();
  public static final StackInstruction DUP_X1 = new DUP_X1();
  public static final StackInstruction DUP_X2 = new DUP_X2();
  public static final StackInstruction DUP2 = new DUP2();
  public static final StackInstruction DUP2_X1 = new DUP2_X1();
  public static final StackInstruction DUP2_X2 = new DUP2_X2();
  public static final StackInstruction SWAP = new SWAP();
  public static final ArithmeticInstruction IADD = new IADD();
  public static final ArithmeticInstruction LADD = new LADD();
  public static final ArithmeticInstruction FADD = new FADD();
  public static final ArithmeticInstruction DADD = new DADD();
  public static final ArithmeticInstruction ISUB = new ISUB();
  public static final ArithmeticInstruction LSUB = new LSUB();
  public static final ArithmeticInstruction FSUB = new FSUB();
  public static final ArithmeticInstruction DSUB = new DSUB();
  public static final ArithmeticInstruction IMUL = new IMUL();
  public static final ArithmeticInstruction LMUL = new LMUL();
  public static final ArithmeticInstruction FMUL = new FMUL();
  public static final ArithmeticInstruction DMUL = new DMUL();
  public static final ArithmeticInstruction IDIV = new IDIV();
  public static final ArithmeticInstruction LDIV = new LDIV();
  public static final ArithmeticInstruction FDIV = new FDIV();
  public static final ArithmeticInstruction DDIV = new DDIV();
  public static final ArithmeticInstruction IREM = new IREM();
  public static final ArithmeticInstruction LREM = new LREM();
  public static final ArithmeticInstruction FREM = new FREM();
  public static final ArithmeticInstruction DREM = new DREM();
  public static final ArithmeticInstruction INEG = new INEG();
  public static final ArithmeticInstruction LNEG = new LNEG();
  public static final ArithmeticInstruction FNEG = new FNEG();
  public static final ArithmeticInstruction DNEG = new DNEG();
  public static final ArithmeticInstruction ISHL = new ISHL();
  public static final ArithmeticInstruction LSHL = new LSHL();
  public static final ArithmeticInstruction ISHR = new ISHR();
  public static final ArithmeticInstruction LSHR = new LSHR();
  public static final ArithmeticInstruction IUSHR = new IUSHR();
  public static final ArithmeticInstruction LUSHR = new LUSHR();
  public static final ArithmeticInstruction IAND = new IAND();
  public static final ArithmeticInstruction LAND = new LAND();
  public static final ArithmeticInstruction IOR = new IOR();
  public static final ArithmeticInstruction LOR = new LOR();
  public static final ArithmeticInstruction IXOR = new IXOR();
  public static final ArithmeticInstruction LXOR = new LXOR();
  public static final ConversionInstruction I2L = new I2L();
  public static final ConversionInstruction I2F = new I2F();
  public static final ConversionInstruction I2D = new I2D();
  public static final ConversionInstruction L2I = new L2I();
  public static final ConversionInstruction L2F = new L2F();
  public static final ConversionInstruction L2D = new L2D();
  public static final ConversionInstruction F2I = new F2I();
  public static final ConversionInstruction F2L = new F2L();
  public static final ConversionInstruction F2D = new F2D();
  public static final ConversionInstruction D2I = new D2I();
  public static final ConversionInstruction D2L = new D2L();
  public static final ConversionInstruction D2F = new D2F();
  public static final ConversionInstruction I2B = new I2B();
  public static final ConversionInstruction I2C = new I2C();
  public static final ConversionInstruction I2S = new I2S();
  public static final Instruction LCMP = new LCMP();
  public static final Instruction FCMPL = new FCMPL();
  public static final Instruction FCMPG = new FCMPG();
  public static final Instruction DCMPL = new DCMPL();
  public static final Instruction DCMPG = new DCMPG();
  public static final ReturnInstruction IRETURN = new IRETURN();
  public static final ReturnInstruction LRETURN = new LRETURN();
  public static final ReturnInstruction FRETURN = new FRETURN();
  public static final ReturnInstruction DRETURN = new DRETURN();
  public static final ReturnInstruction ARETURN = new ARETURN();
  public static final ReturnInstruction RETURN = new RETURN();
  public static final Instruction ARRAYLENGTH = new ARRAYLENGTH();
  public static final Instruction ATHROW = new ATHROW();
  public static final Instruction MONITORENTER = new MONITORENTER();
  public static final Instruction MONITOREXIT = new MONITOREXIT();
  



  public static final LocalVariableInstruction THIS = new ALOAD(0);
  public static final LocalVariableInstruction ALOAD_0 = THIS;
  public static final LocalVariableInstruction ALOAD_1 = new ALOAD(1);
  public static final LocalVariableInstruction ALOAD_2 = new ALOAD(2);
  public static final LocalVariableInstruction ILOAD_0 = new ILOAD(0);
  public static final LocalVariableInstruction ILOAD_1 = new ILOAD(1);
  public static final LocalVariableInstruction ILOAD_2 = new ILOAD(2);
  public static final LocalVariableInstruction ASTORE_0 = new ASTORE(0);
  public static final LocalVariableInstruction ASTORE_1 = new ASTORE(1);
  public static final LocalVariableInstruction ASTORE_2 = new ASTORE(2);
  public static final LocalVariableInstruction ISTORE_0 = new ISTORE(0);
  public static final LocalVariableInstruction ISTORE_1 = new ISTORE(1);
  public static final LocalVariableInstruction ISTORE_2 = new ISTORE(2);
  




  public static final Instruction[] INSTRUCTIONS = new Instruction['Ā'];
  



  public static final Clinit bla = new Clinit();
  
  public static class Clinit {
    Clinit() {
      InstructionConstants.INSTRUCTIONS[0] = InstructionConstants.NOP;
      InstructionConstants.INSTRUCTIONS[1] = InstructionConstants.ACONST_NULL;
      InstructionConstants.INSTRUCTIONS[2] = InstructionConstants.ICONST_M1;
      InstructionConstants.INSTRUCTIONS[3] = InstructionConstants.ICONST_0;
      InstructionConstants.INSTRUCTIONS[4] = InstructionConstants.ICONST_1;
      InstructionConstants.INSTRUCTIONS[5] = InstructionConstants.ICONST_2;
      InstructionConstants.INSTRUCTIONS[6] = InstructionConstants.ICONST_3;
      InstructionConstants.INSTRUCTIONS[7] = InstructionConstants.ICONST_4;
      InstructionConstants.INSTRUCTIONS[8] = InstructionConstants.ICONST_5;
      InstructionConstants.INSTRUCTIONS[9] = InstructionConstants.LCONST_0;
      InstructionConstants.INSTRUCTIONS[10] = InstructionConstants.LCONST_1;
      InstructionConstants.INSTRUCTIONS[11] = InstructionConstants.FCONST_0;
      InstructionConstants.INSTRUCTIONS[12] = InstructionConstants.FCONST_1;
      InstructionConstants.INSTRUCTIONS[13] = InstructionConstants.FCONST_2;
      InstructionConstants.INSTRUCTIONS[14] = InstructionConstants.DCONST_0;
      InstructionConstants.INSTRUCTIONS[15] = InstructionConstants.DCONST_1;
      InstructionConstants.INSTRUCTIONS[46] = InstructionConstants.IALOAD;
      InstructionConstants.INSTRUCTIONS[47] = InstructionConstants.LALOAD;
      InstructionConstants.INSTRUCTIONS[48] = InstructionConstants.FALOAD;
      InstructionConstants.INSTRUCTIONS[49] = InstructionConstants.DALOAD;
      InstructionConstants.INSTRUCTIONS[50] = InstructionConstants.AALOAD;
      InstructionConstants.INSTRUCTIONS[51] = InstructionConstants.BALOAD;
      InstructionConstants.INSTRUCTIONS[52] = InstructionConstants.CALOAD;
      InstructionConstants.INSTRUCTIONS[53] = InstructionConstants.SALOAD;
      InstructionConstants.INSTRUCTIONS[79] = InstructionConstants.IASTORE;
      InstructionConstants.INSTRUCTIONS[80] = InstructionConstants.LASTORE;
      InstructionConstants.INSTRUCTIONS[81] = InstructionConstants.FASTORE;
      InstructionConstants.INSTRUCTIONS[82] = InstructionConstants.DASTORE;
      InstructionConstants.INSTRUCTIONS[83] = InstructionConstants.AASTORE;
      InstructionConstants.INSTRUCTIONS[84] = InstructionConstants.BASTORE;
      InstructionConstants.INSTRUCTIONS[85] = InstructionConstants.CASTORE;
      InstructionConstants.INSTRUCTIONS[86] = InstructionConstants.SASTORE;
      InstructionConstants.INSTRUCTIONS[87] = InstructionConstants.POP;
      InstructionConstants.INSTRUCTIONS[88] = InstructionConstants.POP2;
      InstructionConstants.INSTRUCTIONS[89] = InstructionConstants.DUP;
      InstructionConstants.INSTRUCTIONS[90] = InstructionConstants.DUP_X1;
      InstructionConstants.INSTRUCTIONS[91] = InstructionConstants.DUP_X2;
      InstructionConstants.INSTRUCTIONS[92] = InstructionConstants.DUP2;
      InstructionConstants.INSTRUCTIONS[93] = InstructionConstants.DUP2_X1;
      InstructionConstants.INSTRUCTIONS[94] = InstructionConstants.DUP2_X2;
      InstructionConstants.INSTRUCTIONS[95] = InstructionConstants.SWAP;
      InstructionConstants.INSTRUCTIONS[96] = InstructionConstants.IADD;
      InstructionConstants.INSTRUCTIONS[97] = InstructionConstants.LADD;
      InstructionConstants.INSTRUCTIONS[98] = InstructionConstants.FADD;
      InstructionConstants.INSTRUCTIONS[99] = InstructionConstants.DADD;
      InstructionConstants.INSTRUCTIONS[100] = InstructionConstants.ISUB;
      InstructionConstants.INSTRUCTIONS[101] = InstructionConstants.LSUB;
      InstructionConstants.INSTRUCTIONS[102] = InstructionConstants.FSUB;
      InstructionConstants.INSTRUCTIONS[103] = InstructionConstants.DSUB;
      InstructionConstants.INSTRUCTIONS[104] = InstructionConstants.IMUL;
      InstructionConstants.INSTRUCTIONS[105] = InstructionConstants.LMUL;
      InstructionConstants.INSTRUCTIONS[106] = InstructionConstants.FMUL;
      InstructionConstants.INSTRUCTIONS[107] = InstructionConstants.DMUL;
      InstructionConstants.INSTRUCTIONS[108] = InstructionConstants.IDIV;
      InstructionConstants.INSTRUCTIONS[109] = InstructionConstants.LDIV;
      InstructionConstants.INSTRUCTIONS[110] = InstructionConstants.FDIV;
      InstructionConstants.INSTRUCTIONS[111] = InstructionConstants.DDIV;
      InstructionConstants.INSTRUCTIONS[112] = InstructionConstants.IREM;
      InstructionConstants.INSTRUCTIONS[113] = InstructionConstants.LREM;
      InstructionConstants.INSTRUCTIONS[114] = InstructionConstants.FREM;
      InstructionConstants.INSTRUCTIONS[115] = InstructionConstants.DREM;
      InstructionConstants.INSTRUCTIONS[116] = InstructionConstants.INEG;
      InstructionConstants.INSTRUCTIONS[117] = InstructionConstants.LNEG;
      InstructionConstants.INSTRUCTIONS[118] = InstructionConstants.FNEG;
      InstructionConstants.INSTRUCTIONS[119] = InstructionConstants.DNEG;
      InstructionConstants.INSTRUCTIONS[120] = InstructionConstants.ISHL;
      InstructionConstants.INSTRUCTIONS[121] = InstructionConstants.LSHL;
      InstructionConstants.INSTRUCTIONS[122] = InstructionConstants.ISHR;
      InstructionConstants.INSTRUCTIONS[123] = InstructionConstants.LSHR;
      InstructionConstants.INSTRUCTIONS[124] = InstructionConstants.IUSHR;
      InstructionConstants.INSTRUCTIONS[125] = InstructionConstants.LUSHR;
      InstructionConstants.INSTRUCTIONS[126] = InstructionConstants.IAND;
      InstructionConstants.INSTRUCTIONS[127] = InstructionConstants.LAND;
      InstructionConstants.INSTRUCTIONS[''] = InstructionConstants.IOR;
      InstructionConstants.INSTRUCTIONS[''] = InstructionConstants.LOR;
      InstructionConstants.INSTRUCTIONS[''] = InstructionConstants.IXOR;
      InstructionConstants.INSTRUCTIONS[''] = InstructionConstants.LXOR;
      InstructionConstants.INSTRUCTIONS[''] = InstructionConstants.I2L;
      InstructionConstants.INSTRUCTIONS[''] = InstructionConstants.I2F;
      InstructionConstants.INSTRUCTIONS[''] = InstructionConstants.I2D;
      InstructionConstants.INSTRUCTIONS[''] = InstructionConstants.L2I;
      InstructionConstants.INSTRUCTIONS[''] = InstructionConstants.L2F;
      InstructionConstants.INSTRUCTIONS[''] = InstructionConstants.L2D;
      InstructionConstants.INSTRUCTIONS[''] = InstructionConstants.F2I;
      InstructionConstants.INSTRUCTIONS[''] = InstructionConstants.F2L;
      InstructionConstants.INSTRUCTIONS[''] = InstructionConstants.F2D;
      InstructionConstants.INSTRUCTIONS[''] = InstructionConstants.D2I;
      InstructionConstants.INSTRUCTIONS[''] = InstructionConstants.D2L;
      InstructionConstants.INSTRUCTIONS[''] = InstructionConstants.D2F;
      InstructionConstants.INSTRUCTIONS[''] = InstructionConstants.I2B;
      InstructionConstants.INSTRUCTIONS[''] = InstructionConstants.I2C;
      InstructionConstants.INSTRUCTIONS[''] = InstructionConstants.I2S;
      InstructionConstants.INSTRUCTIONS[''] = InstructionConstants.LCMP;
      InstructionConstants.INSTRUCTIONS[''] = InstructionConstants.FCMPL;
      InstructionConstants.INSTRUCTIONS[''] = InstructionConstants.FCMPG;
      InstructionConstants.INSTRUCTIONS[''] = InstructionConstants.DCMPL;
      InstructionConstants.INSTRUCTIONS[''] = InstructionConstants.DCMPG;
      InstructionConstants.INSTRUCTIONS['¬'] = InstructionConstants.IRETURN;
      InstructionConstants.INSTRUCTIONS['­'] = InstructionConstants.LRETURN;
      InstructionConstants.INSTRUCTIONS['®'] = InstructionConstants.FRETURN;
      InstructionConstants.INSTRUCTIONS['¯'] = InstructionConstants.DRETURN;
      InstructionConstants.INSTRUCTIONS['°'] = InstructionConstants.ARETURN;
      InstructionConstants.INSTRUCTIONS['±'] = InstructionConstants.RETURN;
      InstructionConstants.INSTRUCTIONS['¾'] = InstructionConstants.ARRAYLENGTH;
      InstructionConstants.INSTRUCTIONS['¿'] = InstructionConstants.ATHROW;
      InstructionConstants.INSTRUCTIONS['Â'] = InstructionConstants.MONITORENTER;
      InstructionConstants.INSTRUCTIONS['Ã'] = InstructionConstants.MONITOREXIT;
    }
  }
}
