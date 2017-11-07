package org.apache.bcel.verifier.structurals;

import java.awt.Color;
import java.io.PrintStream;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ATHROW;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.CodeExceptionGen;
import org.apache.bcel.generic.GotoInstruction;
import org.apache.bcel.generic.IndexedInstruction;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.JsrInstruction;
import org.apache.bcel.generic.LocalVariableInstruction;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.RET;
import org.apache.bcel.generic.ReturnInstruction;
import org.apache.bcel.generic.Select;
import org.apache.bcel.generic.Type;
import org.apache.bcel.verifier.exc.AssertionViolatedException;
import org.apache.bcel.verifier.exc.StructuralCodeConstraintException;

































































public class Subroutines
{
  private class SubroutineImpl
    implements Subroutine
  {
    private final int UNSET = -1;
    






    private int localVariable = -1;
    

    private HashSet instructions = new HashSet();
    


    public boolean contains(InstructionHandle inst)
    {
      return instructions.contains(inst);
    }
    




    private HashSet theJSRs = new HashSet();
    




    private InstructionHandle theRET;
    





    public String toString()
    {
      String ret = "Subroutine: Local variable is '" + localVariable + "', JSRs are '" + theJSRs + "', RET is '" + theRET + "', Instructions: '" + instructions.toString() + "'.";
      
      ret = ret + " Accessed local variable slots: '";
      int[] alv = getAccessedLocalsIndices();
      for (int i = 0; i < alv.length; i++) {
        ret = ret + alv[i] + " ";
      }
      ret = ret + "'.";
      
      ret = ret + " Recursively (via subsub...routines) accessed local variable slots: '";
      alv = getRecursivelyAccessedLocalsIndices();
      for (int i = 0; i < alv.length; i++) {
        ret = ret + alv[i] + " ";
      }
      ret = ret + "'.";
      
      return ret;
    }
    



    void setLeavingRET()
    {
      if (localVariable == -1) {
        throw new AssertionViolatedException("setLeavingRET() called for top-level 'subroutine' or forgot to set local variable first.");
      }
      Iterator iter = instructions.iterator();
      InstructionHandle ret = null;
      while (iter.hasNext()) {
        InstructionHandle actual = (InstructionHandle)iter.next();
        if ((actual.getInstruction() instanceof RET)) {
          if (ret != null) {
            throw new StructuralCodeConstraintException("Subroutine with more then one RET detected: '" + ret + "' and '" + actual + "'.");
          }
          
          ret = actual;
        }
      }
      
      if (ret == null) {
        throw new StructuralCodeConstraintException("Subroutine without a RET detected.");
      }
      if (((RET)ret.getInstruction()).getIndex() != localVariable) {
        throw new StructuralCodeConstraintException("Subroutine uses '" + ret + "' which does not match the correct local variable '" + localVariable + "'.");
      }
      theRET = ret;
    }
    


    public InstructionHandle[] getEnteringJsrInstructions()
    {
      if (this == TOPLEVEL) {
        throw new AssertionViolatedException("getLeavingRET() called on top level pseudo-subroutine.");
      }
      InstructionHandle[] jsrs = new InstructionHandle[theJSRs.size()];
      return (InstructionHandle[])theJSRs.toArray(jsrs);
    }
    


    public void addEnteringJsrInstruction(InstructionHandle jsrInst)
    {
      if ((jsrInst == null) || (!(jsrInst.getInstruction() instanceof JsrInstruction))) {
        throw new AssertionViolatedException("Expecting JsrInstruction InstructionHandle.");
      }
      if (localVariable == -1) {
        throw new AssertionViolatedException("Set the localVariable first!");
      }
      



      if (localVariable != ((ASTORE)((JsrInstruction)jsrInst.getInstruction()).getTarget().getInstruction()).getIndex()) {
        throw new AssertionViolatedException("Setting a wrong JsrInstruction.");
      }
      
      theJSRs.add(jsrInst);
    }
    


    public InstructionHandle getLeavingRET()
    {
      if (this == TOPLEVEL) {
        throw new AssertionViolatedException("getLeavingRET() called on top level pseudo-subroutine.");
      }
      return theRET;
    }
    


    public InstructionHandle[] getInstructions()
    {
      InstructionHandle[] ret = new InstructionHandle[instructions.size()];
      return (InstructionHandle[])instructions.toArray(ret);
    }
    




    void addInstruction(InstructionHandle ih)
    {
      if (theRET != null) {
        throw new AssertionViolatedException("All instructions must have been added before invoking setLeavingRET().");
      }
      instructions.add(ih);
    }
    
    public int[] getRecursivelyAccessedLocalsIndices()
    {
      HashSet s = new HashSet();
      int[] lvs = getAccessedLocalsIndices();
      for (int j = 0; j < lvs.length; j++) {
        s.add(new Integer(lvs[j]));
      }
      _getRecursivelyAccessedLocalsIndicesHelper(s, subSubs());
      int[] ret = new int[s.size()];
      Iterator i = s.iterator();
      int j = -1;
      while (i.hasNext()) {
        j++;
        ret[j] = ((Integer)i.next()).intValue();
      }
      return ret;
    }
    



    private void _getRecursivelyAccessedLocalsIndicesHelper(HashSet s, Subroutine[] subs)
    {
      for (int i = 0; i < subs.length; i++) {
        int[] lvs = subs[i].getAccessedLocalsIndices();
        for (int j = 0; j < lvs.length; j++) {
          s.add(new Integer(lvs[j]));
        }
        if (subs[i].subSubs().length != 0) {
          _getRecursivelyAccessedLocalsIndicesHelper(s, subs[i].subSubs());
        }
      }
    }
    



    public int[] getAccessedLocalsIndices()
    {
      HashSet acc = new HashSet();
      if ((theRET == null) && (this != TOPLEVEL)) {
        throw new AssertionViolatedException("This subroutine object must be built up completely before calculating accessed locals.");
      }
      Iterator i = instructions.iterator();
      while (i.hasNext()) {
        InstructionHandle ih = (InstructionHandle)i.next();
        
        if (((ih.getInstruction() instanceof LocalVariableInstruction)) || ((ih.getInstruction() instanceof RET))) {
          int idx = ((IndexedInstruction)ih.getInstruction()).getIndex();
          acc.add(new Integer(idx));
          

          try
          {
            if ((ih.getInstruction() instanceof LocalVariableInstruction)) {
              int s = ((LocalVariableInstruction)ih.getInstruction()).getType(null).getSize();
              if (s == 2) acc.add(new Integer(idx + 1));
            }
          }
          catch (RuntimeException re) {
            throw new AssertionViolatedException("Oops. BCEL did not like NULL as a ConstantPoolGen object.");
          }
        }
      }
      
      int[] ret = new int[acc.size()];
      i = acc.iterator();
      int j = -1;
      while (i.hasNext()) {
        j++;
        ret[j] = ((Integer)i.next()).intValue();
      }
      return ret;
    }
    


    public Subroutine[] subSubs()
    {
      HashSet h = new HashSet();
      
      Iterator i = instructions.iterator();
      while (i.hasNext()) {
        Instruction inst = ((InstructionHandle)i.next()).getInstruction();
        if ((inst instanceof JsrInstruction)) {
          InstructionHandle targ = ((JsrInstruction)inst).getTarget();
          h.add(getSubroutine(targ));
        }
      }
      Subroutine[] ret = new Subroutine[h.size()];
      return (Subroutine[])h.toArray(ret);
    }
    





    void setLocalVariable(int i)
    {
      if (localVariable != -1) {
        throw new AssertionViolatedException("localVariable set twice.");
      }
      
      localVariable = i;
    }
    






    public SubroutineImpl() {}
  }
  





  private Hashtable subroutines = new Hashtable();
  





  public final Subroutine TOPLEVEL;
  






  public Subroutines(MethodGen mg)
  {
    InstructionHandle[] all = mg.getInstructionList().getInstructionHandles();
    CodeExceptionGen[] handlers = mg.getExceptionHandlers();
    

    TOPLEVEL = new SubroutineImpl();
    

    HashSet sub_leaders = new HashSet();
    InstructionHandle ih = all[0];
    for (int i = 0; i < all.length; i++) {
      Instruction inst = all[i].getInstruction();
      if ((inst instanceof JsrInstruction)) {
        sub_leaders.add(((JsrInstruction)inst).getTarget());
      }
    }
    

    Iterator iter = sub_leaders.iterator();
    while (iter.hasNext()) {
      SubroutineImpl sr = new SubroutineImpl();
      InstructionHandle astore = (InstructionHandle)iter.next();
      sr.setLocalVariable(((ASTORE)astore.getInstruction()).getIndex());
      subroutines.put(astore, sr);
    }
    

    subroutines.put(all[0], TOPLEVEL);
    sub_leaders.add(all[0]);
    





    for (int i = 0; i < all.length; i++) {
      Instruction inst = all[i].getInstruction();
      if ((inst instanceof JsrInstruction)) {
        InstructionHandle leader = ((JsrInstruction)inst).getTarget();
        ((SubroutineImpl)getSubroutine(leader)).addEnteringJsrInstruction(all[i]);
      }
    }
    


    HashSet instructions_assigned = new HashSet();
    
    Hashtable colors = new Hashtable();
    
    iter = sub_leaders.iterator();
    while (iter.hasNext())
    {
      InstructionHandle actual = (InstructionHandle)iter.next();
      
      for (int i = 0; i < all.length; i++) {
        colors.put(all[i], Color.white);
      }
      colors.put(actual, Color.gray);
      
      ArrayList Q = new ArrayList();
      Q.add(actual);
      

      if (actual == all[0]) {
        for (int j = 0; j < handlers.length; j++) {
          colors.put(handlers[j].getHandlerPC(), Color.gray);
          Q.add(handlers[j].getHandlerPC());
        }
      }
      


      while (Q.size() != 0) {
        InstructionHandle u = (InstructionHandle)Q.remove(0);
        InstructionHandle[] successors = getSuccessors(u);
        for (int i = 0; i < successors.length; i++) {
          if ((Color)colors.get(successors[i]) == Color.white) {
            colors.put(successors[i], Color.gray);
            Q.add(successors[i]);
          }
        }
        colors.put(u, Color.black);
      }
      
      for (int i = 0; i < all.length; i++) {
        if (colors.get(all[i]) == Color.black) {
          ((SubroutineImpl)(actual == all[0] ? getTopLevel() : getSubroutine(actual))).addInstruction(all[i]);
          if (instructions_assigned.contains(all[i])) {
            throw new StructuralCodeConstraintException("Instruction '" + all[i] + "' is part of more than one subroutine (or of the top level and a subroutine).");
          }
          
          instructions_assigned.add(all[i]);
        }
      }
      
      if (actual != all[0]) {
        ((SubroutineImpl)getSubroutine(actual)).setLeavingRET();
      }
    }
    


    for (int i = 0; i < handlers.length; i++) {
      InstructionHandle _protected = handlers[i].getStartPC();
      while (_protected != handlers[i].getEndPC().getNext()) {
        Enumeration subs = subroutines.elements();
        while (subs.hasMoreElements()) {
          Subroutine sub = (Subroutine)subs.nextElement();
          if ((sub != subroutines.get(all[0])) && 
            (sub.contains(_protected))) {
            throw new StructuralCodeConstraintException("Subroutine instruction '" + _protected + "' is protected by an exception handler, '" + handlers[i] + "'. This is forbidden by the JustIce verifier due to its clear definition of subroutines.");
          }
        }
        
        _protected = _protected.getNext();
      }
    }
    






    noRecursiveCalls(getTopLevel(), new HashSet());
  }
  











  private void noRecursiveCalls(Subroutine sub, HashSet set)
  {
    Subroutine[] subs = sub.subSubs();
    
    for (int i = 0; i < subs.length; i++) {
      int index = ((RET)subs[i].getLeavingRET().getInstruction()).getIndex();
      
      if (!set.add(new Integer(index)))
      {
        SubroutineImpl si = (SubroutineImpl)subs[i];
        throw new StructuralCodeConstraintException("Subroutine with local variable '" + localVariable + "', JSRs '" + theJSRs + "', RET '" + theRET + "' is called by a subroutine which uses the same local variable index as itself; maybe even a recursive call? JustIce's clean definition of a subroutine forbids both.");
      }
      
      noRecursiveCalls(subs[i], set);
      
      set.remove(new Integer(index));
    }
  }
  







  public Subroutine getSubroutine(InstructionHandle leader)
  {
    Subroutine ret = (Subroutine)subroutines.get(leader);
    
    if (ret == null) {
      throw new AssertionViolatedException("Subroutine requested for an InstructionHandle that is not a leader of a subroutine.");
    }
    
    if (ret == TOPLEVEL) {
      throw new AssertionViolatedException("TOPLEVEL special subroutine requested; use getTopLevel().");
    }
    
    return ret;
  }
  










  public Subroutine subroutineOf(InstructionHandle any)
  {
    Iterator i = subroutines.values().iterator();
    while (i.hasNext()) {
      Subroutine s = (Subroutine)i.next();
      if (s.contains(any)) return s;
    }
    System.err.println("DEBUG: Please verify '" + any + "' lies in dead code.");
    return null;
  }
  










  public Subroutine getTopLevel()
  {
    return TOPLEVEL;
  }
  




  private static InstructionHandle[] getSuccessors(InstructionHandle instruction)
  {
    InstructionHandle[] empty = new InstructionHandle[0];
    InstructionHandle[] single = new InstructionHandle[1];
    InstructionHandle[] pair = new InstructionHandle[2];
    
    Instruction inst = instruction.getInstruction();
    
    if ((inst instanceof RET)) {
      return empty;
    }
    

    if ((inst instanceof ReturnInstruction)) {
      return empty;
    }
    


    if ((inst instanceof ATHROW)) {
      return empty;
    }
    

    if ((inst instanceof JsrInstruction)) {
      single[0] = instruction.getNext();
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
      
      pair[0] = instruction.getNext();
      pair[1] = ((BranchInstruction)inst).getTarget();
      return pair;
    }
    


    single[0] = instruction.getNext();
    return single;
  }
  


  public String toString()
  {
    return "---\n" + subroutines.toString() + "\n---\n";
  }
}
