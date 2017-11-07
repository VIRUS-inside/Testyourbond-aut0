package org.apache.bcel.verifier.structurals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import org.apache.bcel.generic.ATHROW;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.GotoInstruction;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.JsrInstruction;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.RET;
import org.apache.bcel.generic.ReturnInstruction;
import org.apache.bcel.generic.Select;
import org.apache.bcel.verifier.exc.AssertionViolatedException;
import org.apache.bcel.verifier.exc.StructuralCodeConstraintException;
import org.apache.bcel.verifier.exc.VerifierConstraintViolatedException;






































































public class ControlFlowGraph
{
  private final MethodGen method_gen;
  private final Subroutines subroutines;
  private final ExceptionHandlers exceptionhandlers;
  
  private class InstructionContextImpl
    implements InstructionContext
  {
    private int TAG;
    private InstructionHandle instruction;
    private HashMap inFrames;
    private HashMap outFrames;
    private ArrayList executionPredecessors = null;
    



    public InstructionContextImpl(InstructionHandle inst)
    {
      if (inst == null) { throw new AssertionViolatedException("Cannot instantiate InstructionContextImpl from NULL.");
      }
      instruction = inst;
      inFrames = new HashMap();
      outFrames = new HashMap();
    }
    
    public int getTag()
    {
      return TAG;
    }
    
    public void setTag(int tag)
    {
      TAG = tag;
    }
    


    public ExceptionHandler[] getExceptionHandlers()
    {
      return exceptionhandlers.getExceptionHandlers(getInstruction());
    }
    


    public Frame getOutFrame(ArrayList execChain)
    {
      executionPredecessors = execChain;
      


      InstructionContext jsr = lastExecutionJSR();
      
      Frame org = (Frame)outFrames.get(jsr);
      
      if (org == null) {
        throw new AssertionViolatedException("outFrame not set! This:\n" + this + "\nExecutionChain: " + getExecutionChain() + "\nOutFrames: '" + outFrames + "'.");
      }
      return org.getClone();
    }
    















    public boolean execute(Frame inFrame, ArrayList execPreds, InstConstraintVisitor icv, ExecutionVisitor ev)
    {
      executionPredecessors = ((ArrayList)execPreds.clone());
      

      if ((lastExecutionJSR() == null) && (subroutines.subroutineOf(getInstruction()) != subroutines.getTopLevel())) {
        throw new AssertionViolatedException("Huh?! Am I '" + this + "' part of a subroutine or not?");
      }
      if ((lastExecutionJSR() != null) && (subroutines.subroutineOf(getInstruction()) == subroutines.getTopLevel())) {
        throw new AssertionViolatedException("Huh?! Am I '" + this + "' part of a subroutine or not?");
      }
      
      Frame inF = (Frame)inFrames.get(lastExecutionJSR());
      if (inF == null) {
        inFrames.put(lastExecutionJSR(), inFrame);
        inF = inFrame;
      }
      else {
        if (inF.equals(inFrame)) {
          return false;
        }
        if (!mergeInFrames(inFrame)) {
          return false;
        }
      }
      



      Frame workingFrame = inF.getClone();
      


      try
      {
        icv.setFrame(workingFrame);
        getInstruction().accept(icv);
      }
      catch (StructuralCodeConstraintException ce) {
        ce.extendMessage("", "\nInstructionHandle: " + getInstruction() + "\n");
        ce.extendMessage("", "\nExecution Frame:\n" + workingFrame);
        extendMessageWithFlow(ce);
        throw ce;
      }
      



      ev.setFrame(workingFrame);
      getInstruction().accept(ev);
      
      outFrames.put(lastExecutionJSR(), workingFrame);
      
      return true;
    }
    






    public String toString()
    {
      String ret = getInstruction().toString(false) + "\t[InstructionContext]";
      return ret;
    }
    




    private boolean mergeInFrames(Frame inFrame)
    {
      Frame inF = (Frame)inFrames.get(lastExecutionJSR());
      OperandStack oldstack = inF.getStack().getClone();
      LocalVariables oldlocals = inF.getLocals().getClone();
      try {
        inF.getStack().merge(inFrame.getStack());
        inF.getLocals().merge(inFrame.getLocals());
      }
      catch (StructuralCodeConstraintException sce) {
        extendMessageWithFlow(sce);
        throw sce;
      }
      if ((oldstack.equals(inF.getStack())) && (oldlocals.equals(inF.getLocals())))
      {
        return false;
      }
      
      return true;
    }
    





    private String getExecutionChain()
    {
      String s = toString();
      for (int i = executionPredecessors.size() - 1; i >= 0; i--) {
        s = executionPredecessors.get(i) + "\n" + s;
      }
      return s;
    }
    





    private void extendMessageWithFlow(StructuralCodeConstraintException e)
    {
      String s = "Execution flow:\n";
      e.extendMessage("", s + getExecutionChain());
    }
    


    public InstructionHandle getInstruction()
    {
      return instruction;
    }
    







    private InstructionContextImpl lastExecutionJSR()
    {
      int size = executionPredecessors.size();
      int retcount = 0;
      
      for (int i = size - 1; i >= 0; i--) {
        InstructionContextImpl current = (InstructionContextImpl)executionPredecessors.get(i);
        Instruction currentlast = current.getInstruction().getInstruction();
        if ((currentlast instanceof RET)) retcount++;
        if ((currentlast instanceof JsrInstruction)) {
          retcount--;
          if (retcount == -1) return current;
        }
      }
      return null;
    }
    
    public InstructionContext[] getSuccessors()
    {
      return contextsOf(_getSuccessors());
    }
    






    private InstructionHandle[] _getSuccessors()
    {
      InstructionHandle[] empty = new InstructionHandle[0];
      InstructionHandle[] single = new InstructionHandle[1];
      InstructionHandle[] pair = new InstructionHandle[2];
      
      Instruction inst = getInstruction().getInstruction();
      
      if ((inst instanceof RET)) {
        Subroutine s = subroutines.subroutineOf(getInstruction());
        if (s == null) {
          throw new AssertionViolatedException("Asking for successors of a RET in dead code?!");
        }
        
        throw new AssertionViolatedException("DID YOU REALLY WANT TO ASK FOR RET'S SUCCS?");
      }
      









      if ((inst instanceof ReturnInstruction)) {
        return empty;
      }
      


      if ((inst instanceof ATHROW)) {
        return empty;
      }
      

      if ((inst instanceof JsrInstruction)) {
        single[0] = ((JsrInstruction)inst).getTarget();
        return single;
      }
      
      if ((inst instanceof GotoInstruction)) {
        single[0] = ((GotoInstruction)inst).getTarget();
        return single;
      }
      
      if ((inst instanceof BranchInstruction)) {
        if ((inst instanceof Select))
        {

          InstructionHandle[] matchTargets = ((Select)inst).getTargets();
          InstructionHandle[] ret = new InstructionHandle[matchTargets.length + 1];
          ret[0] = ((Select)inst).getTarget();
          System.arraycopy(matchTargets, 0, ret, 1, matchTargets.length);
          return ret;
        }
        
        pair[0] = getInstruction().getNext();
        pair[1] = ((BranchInstruction)inst).getTarget();
        return pair;
      }
      


      single[0] = getInstruction().getNext();
      return single;
    }
  }
  











  private Hashtable instructionContexts = new Hashtable();
  


  public ControlFlowGraph(MethodGen method_gen)
  {
    subroutines = new Subroutines(method_gen);
    exceptionhandlers = new ExceptionHandlers(method_gen);
    
    InstructionHandle[] instructionhandles = method_gen.getInstructionList().getInstructionHandles();
    for (int i = 0; i < instructionhandles.length; i++) {
      instructionContexts.put(instructionhandles[i], new InstructionContextImpl(instructionhandles[i]));
    }
    
    this.method_gen = method_gen;
  }
  


  public InstructionContext contextOf(InstructionHandle inst)
  {
    InstructionContext ic = (InstructionContext)instructionContexts.get(inst);
    if (ic == null) {
      throw new AssertionViolatedException("InstructionContext requested for an InstructionHandle that's not known!");
    }
    return ic;
  }
  



  public InstructionContext[] contextsOf(InstructionHandle[] insts)
  {
    InstructionContext[] ret = new InstructionContext[insts.length];
    for (int i = 0; i < insts.length; i++) {
      ret[i] = contextOf(insts[i]);
    }
    return ret;
  }
  




  public InstructionContext[] getInstructionContexts()
  {
    InstructionContext[] ret = new InstructionContext[instructionContexts.values().size()];
    return (InstructionContext[])instructionContexts.values().toArray(ret);
  }
  



  public boolean isDead(InstructionHandle i)
  {
    return instructionContexts.containsKey(i);
  }
}
