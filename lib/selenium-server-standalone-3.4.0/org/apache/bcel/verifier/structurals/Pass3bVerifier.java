package org.apache.bcel.verifier.structurals;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.AccessFlags;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.FieldGenOrMethodGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.JsrInstruction;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.RET;
import org.apache.bcel.generic.ReturnInstruction;
import org.apache.bcel.generic.ReturnaddressType;
import org.apache.bcel.generic.Type;
import org.apache.bcel.verifier.PassVerifier;
import org.apache.bcel.verifier.VerificationResult;
import org.apache.bcel.verifier.Verifier;
import org.apache.bcel.verifier.exc.AssertionViolatedException;
import org.apache.bcel.verifier.exc.VerifierConstraintViolatedException;































































public final class Pass3bVerifier
  extends PassVerifier
{
  private static final boolean DEBUG = true;
  private Verifier myOwner;
  private int method_no;
  
  private static final class InstructionContextQueue
  {
    InstructionContextQueue(Pass3bVerifier.1 x0) { this(); }
    private Vector ics = new Vector();
    private Vector ecs = new Vector();
    
    public void add(InstructionContext ic, ArrayList executionChain) { ics.add(ic);
      ecs.add(executionChain);
    }
    
    public boolean isEmpty() { return ics.isEmpty(); }
    

    public void remove() { remove(0); }
    
    public void remove(int i) {
      ics.remove(i);
      ecs.remove(i);
    }
    
    public InstructionContext getIC(int i) { return (InstructionContext)ics.get(i); }
    
    public ArrayList getEC(int i) {
      return (ArrayList)ecs.get(i);
    }
    
    public int size() { return ics.size(); }
    






    private InstructionContextQueue() {}
  }
  






  public Pass3bVerifier(Verifier owner, int method_no)
  {
    myOwner = owner;
    this.method_no = method_no;
  }
  






  private void circulationPump(ControlFlowGraph cfg, InstructionContext start, Frame vanillaFrame, InstConstraintVisitor icv, ExecutionVisitor ev)
  {
    Random random = new Random();
    InstructionContextQueue icq = new InstructionContextQueue(null);
    
    start.execute(vanillaFrame, new ArrayList(), icv, ev);
    
    icq.add(start, new ArrayList());
    ExceptionHandler[] exc_hds;
    int s;
    for (; !icq.isEmpty(); 
        









































































        s < exc_hds.length)
    {
      InstructionContext u = icq.getIC(0);
      ArrayList ec = icq.getEC(0);
      icq.remove(0);
      

      ArrayList oldchain = (ArrayList)ec.clone();
      ArrayList newchain = (ArrayList)ec.clone();
      newchain.add(u);
      
      if ((u.getInstruction().getInstruction() instanceof RET))
      {


        RET ret = (RET)u.getInstruction().getInstruction();
        ReturnaddressType t = (ReturnaddressType)u.getOutFrame(oldchain).getLocals().get(ret.getIndex());
        InstructionContext theSuccessor = cfg.contextOf(t.getTarget());
        

        InstructionContext lastJSR = null;
        int skip_jsr = 0;
        for (int ss = oldchain.size() - 1; ss >= 0; ss--) {
          if (skip_jsr < 0) {
            throw new AssertionViolatedException("More RET than JSR in execution chain?!");
          }
          
          if ((((InstructionContext)oldchain.get(ss)).getInstruction().getInstruction() instanceof JsrInstruction)) {
            if (skip_jsr == 0) {
              lastJSR = (InstructionContext)oldchain.get(ss);
              break;
            }
            
            skip_jsr--;
          }
          
          if ((((InstructionContext)oldchain.get(ss)).getInstruction().getInstruction() instanceof RET)) {
            skip_jsr++;
          }
        }
        if (lastJSR == null) {
          throw new AssertionViolatedException("RET without a JSR before in ExecutionChain?! EC: '" + oldchain + "'.");
        }
        JsrInstruction jsr = (JsrInstruction)lastJSR.getInstruction().getInstruction();
        if (theSuccessor != cfg.contextOf(jsr.physicalSuccessor())) {
          throw new AssertionViolatedException("RET '" + u.getInstruction() + "' info inconsistent: jump back to '" + theSuccessor + "' or '" + cfg.contextOf(jsr.physicalSuccessor()) + "'?");
        }
        
        if (theSuccessor.execute(u.getOutFrame(oldchain), newchain, icv, ev)) {
          icq.add(theSuccessor, (ArrayList)newchain.clone());
        }
        
      }
      else
      {
        InstructionContext[] succs = u.getSuccessors();
        for (int s = 0; s < succs.length; s++) {
          InstructionContext v = succs[s];
          if (v.execute(u.getOutFrame(oldchain), newchain, icv, ev)) {
            icq.add(v, (ArrayList)newchain.clone());
          }
        }
      }
      


      exc_hds = u.getExceptionHandlers();
      s = 0; continue;
      InstructionContext v = cfg.contextOf(exc_hds[s].getHandlerStart());
      









      if (v.execute(new Frame(u.getOutFrame(oldchain).getLocals(), new OperandStack(u.getOutFrame(oldchain).getStack().maxStack(), exc_hds[s].getExceptionType() == null ? Type.THROWABLE : exc_hds[s].getExceptionType())), new ArrayList(), icv, ev)) {
        icq.add(v, new ArrayList());
      }
      s++;
    }
    
















    InstructionHandle ih = start.getInstruction();
    do {
      if (((ih.getInstruction() instanceof ReturnInstruction)) && (!cfg.isDead(ih))) {
        InstructionContext ic = cfg.contextOf(ih);
        Frame f = ic.getOutFrame(new ArrayList());
        LocalVariables lvs = f.getLocals();
        for (int i = 0; i < lvs.maxLocals(); i++) {
          if ((lvs.get(i) instanceof UninitializedObjectType)) {
            addMessage("Warning: ReturnInstruction '" + ic + "' may leave method with an uninitialized object in the local variables array '" + lvs + "'.");
          }
        }
        OperandStack os = f.getStack();
        for (int i = 0; i < os.size(); i++) {
          if ((os.peek(i) instanceof UninitializedObjectType)) {
            addMessage("Warning: ReturnInstruction '" + ic + "' may leave method with an uninitialized object on the operand stack '" + os + "'.");
          }
        }
      }
    } while ((ih = ih.getNext()) != null);
  }
  










  public VerificationResult do_verify()
  {
    if (!myOwner.doPass3a(method_no).equals(VerificationResult.VR_OK)) {
      return VerificationResult.VR_NOTYET;
    }
    


    JavaClass jc = Repository.lookupClass(myOwner.getClassName());
    
    ConstantPoolGen constantPoolGen = new ConstantPoolGen(jc.getConstantPool());
    
    InstConstraintVisitor icv = new InstConstraintVisitor();
    icv.setConstantPoolGen(constantPoolGen);
    
    ExecutionVisitor ev = new ExecutionVisitor();
    ev.setConstantPoolGen(constantPoolGen);
    
    Method[] methods = jc.getMethods();
    
    try
    {
      MethodGen mg = new MethodGen(methods[method_no], myOwner.getClassName(), constantPoolGen);
      
      icv.setMethodGen(mg);
      

      if ((!mg.isAbstract()) && (!mg.isNative()))
      {
        ControlFlowGraph cfg = new ControlFlowGraph(mg);
        

        Frame f = new Frame(mg.getMaxLocals(), mg.getMaxStack());
        if (!mg.isStatic()) {
          if (mg.getName().equals("<init>")) {
            Frame._this = new UninitializedObjectType(new ObjectType(jc.getClassName()));
            f.getLocals().set(0, Frame._this);
          }
          else {
            Frame._this = null;
            f.getLocals().set(0, new ObjectType(jc.getClassName()));
          }
        }
        Type[] argtypes = mg.getArgumentTypes();
        int twoslotoffset = 0;
        for (int j = 0; j < argtypes.length; j++) {
          if ((argtypes[j] == Type.SHORT) || (argtypes[j] == Type.BYTE) || (argtypes[j] == Type.CHAR) || (argtypes[j] == Type.BOOLEAN)) {
            argtypes[j] = Type.INT;
          }
          f.getLocals().set(twoslotoffset + j + (mg.isStatic() ? 0 : 1), argtypes[j]);
          if (argtypes[j].getSize() == 2) {
            twoslotoffset++;
            f.getLocals().set(twoslotoffset + j + (mg.isStatic() ? 0 : 1), Type.UNKNOWN);
          }
        }
        circulationPump(cfg, cfg.contextOf(mg.getInstructionList().getStart()), f, icv, ev);
      }
    }
    catch (VerifierConstraintViolatedException ce) {
      ce.extendMessage("Constraint violated in method '" + methods[method_no] + "':\n", "");
      return new VerificationResult(2, ce.getMessage());

    }
    catch (RuntimeException re)
    {
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      re.printStackTrace(pw);
      
      throw new AssertionViolatedException("Some RuntimeException occured while verify()ing class '" + jc.getClassName() + "', method '" + methods[method_no] + "'. Original RuntimeException's stack trace:\n---\n" + sw + "---\n");
    }
    return VerificationResult.VR_OK;
  }
  
  public int getMethodNo()
  {
    return method_no;
  }
}
