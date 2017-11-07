package net.sf.cglib.core;

import net.sf.cglib.asm..Label;
import net.sf.cglib.asm..MethodVisitor;
import net.sf.cglib.asm..Type;








































public class LocalVariablesSorter
  extends .MethodVisitor
{
  protected final int firstLocal;
  private final State state;
  
  private static class State
  {
    int[] mapping = new int[40];
    

    int nextLocal;
    

    private State() {}
  }
  

  public LocalVariablesSorter(int access, String desc, .MethodVisitor mv)
  {
    super(327680, mv);
    state = new State(null);
    .Type[] args = .Type.getArgumentTypes(desc);
    state.nextLocal = ((0x8 & access) != 0 ? 0 : 1);
    for (int i = 0; i < args.length; i++) {
      state.nextLocal += args[i].getSize();
    }
    firstLocal = state.nextLocal;
  }
  
  public LocalVariablesSorter(LocalVariablesSorter lvs) {
    super(327680, mv);
    state = state;
    firstLocal = firstLocal;
  }
  
  public void visitVarInsn(int opcode, int var) { int size;
    int size;
    switch (opcode) {
    case 22: 
    case 24: 
    case 55: 
    case 57: 
      size = 2;
      break;
    default: 
      size = 1;
    }
    mv.visitVarInsn(opcode, remap(var, size));
  }
  
  public void visitIincInsn(int var, int increment) {
    mv.visitIincInsn(remap(var, 1), increment);
  }
  
  public void visitMaxs(int maxStack, int maxLocals) {
    mv.visitMaxs(maxStack, state.nextLocal);
  }
  






  public void visitLocalVariable(String name, String desc, String signature, .Label start, .Label end, int index)
  {
    mv.visitLocalVariable(name, desc, signature, start, end, remap(index));
  }
  

  protected int newLocal(int size)
  {
    int var = state.nextLocal;
    state.nextLocal += size;
    return var;
  }
  
  private int remap(int var, int size) {
    if (var < firstLocal) {
      return var;
    }
    int key = 2 * var + size - 1;
    int length = state.mapping.length;
    if (key >= length) {
      int[] newMapping = new int[Math.max(2 * length, key + 1)];
      System.arraycopy(state.mapping, 0, newMapping, 0, length);
      state.mapping = newMapping;
    }
    int value = state.mapping[key];
    if (value == 0) {
      value = state.nextLocal + 1;
      state.mapping[key] = value;
      state.nextLocal += size;
    }
    return value - 1;
  }
  
  private int remap(int var) {
    if (var < firstLocal) {
      return var;
    }
    int key = 2 * var;
    int value = key < state.mapping.length ? state.mapping[key] : 0;
    if (value == 0) {
      value = key + 1 < state.mapping.length ? state.mapping[(key + 1)] : 0;
    }
    if (value == 0) {
      throw new IllegalStateException("Unknown local variable " + var);
    }
    return value - 1;
  }
}
