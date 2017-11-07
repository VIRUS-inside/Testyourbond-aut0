package org.apache.xalan.xsltc.compiler;

import java.util.Dictionary;
import java.util.Vector;
import org.apache.bcel.generic.GOTO_W;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;















































final class TestSeq
{
  private int _kernelType;
  private Vector _patterns = null;
  



  private Mode _mode = null;
  



  private Template _default = null;
  



  private InstructionList _instructionList;
  



  private InstructionHandle _start = null;
  


  public TestSeq(Vector patterns, Mode mode)
  {
    this(patterns, -2, mode);
  }
  
  public TestSeq(Vector patterns, int kernelType, Mode mode) {
    _patterns = patterns;
    _kernelType = kernelType;
    _mode = mode;
  }
  




  public String toString()
  {
    int count = _patterns.size();
    StringBuffer result = new StringBuffer();
    
    for (int i = 0; i < count; i++) {
      LocationPathPattern pattern = (LocationPathPattern)_patterns.elementAt(i);
      

      if (i == 0) {
        result.append("Testseq for kernel " + _kernelType).append('\n');
      }
      
      result.append("   pattern " + i + ": ").append(pattern.toString()).append('\n');
    }
    

    return result.toString();
  }
  


  public InstructionList getInstructionList()
  {
    return _instructionList;
  }
  




  public double getPriority()
  {
    Template template = _patterns.size() == 0 ? _default : ((Pattern)_patterns.elementAt(0)).getTemplate();
    
    return template.getPriority();
  }
  



  public int getPosition()
  {
    Template template = _patterns.size() == 0 ? _default : ((Pattern)_patterns.elementAt(0)).getTemplate();
    
    return template.getPosition();
  }
  




  public void reduce()
  {
    Vector newPatterns = new Vector();
    
    int count = _patterns.size();
    for (int i = 0; i < count; i++) {
      LocationPathPattern pattern = (LocationPathPattern)_patterns.elementAt(i);
      


      pattern.reduceKernelPattern();
      

      if (pattern.isWildcard()) {
        _default = pattern.getTemplate();
        break;
      }
      
      newPatterns.addElement(pattern);
    }
    
    _patterns = newPatterns;
  }
  




  public void findTemplates(Dictionary templates)
  {
    if (_default != null) {
      templates.put(_default, this);
    }
    for (int i = 0; i < _patterns.size(); i++) {
      LocationPathPattern pattern = (LocationPathPattern)_patterns.elementAt(i);
      
      templates.put(pattern.getTemplate(), this);
    }
  }
  





  private InstructionHandle getTemplateHandle(Template template)
  {
    return _mode.getTemplateInstructionHandle(template);
  }
  


  private LocationPathPattern getPattern(int n)
  {
    return (LocationPathPattern)_patterns.elementAt(n);
  }
  









  public InstructionHandle compile(ClassGenerator classGen, MethodGenerator methodGen, InstructionHandle continuation)
  {
    if (_start != null) {
      return _start;
    }
    

    int count = _patterns.size();
    if (count == 0) {
      return this._start = getTemplateHandle(_default);
    }
    

    InstructionHandle fail = _default == null ? continuation : getTemplateHandle(_default);
    


    for (int n = count - 1; n >= 0; n--) {
      LocationPathPattern pattern = getPattern(n);
      Template template = pattern.getTemplate();
      InstructionList il = new InstructionList();
      

      il.append(methodGen.loadCurrentNode());
      

      InstructionList ilist = methodGen.getInstructionList(pattern);
      if (ilist == null) {
        ilist = pattern.compile(classGen, methodGen);
        methodGen.addInstructionList(pattern, ilist);
      }
      

      InstructionList copyOfilist = ilist.copy();
      
      FlowList trueList = pattern.getTrueList();
      if (trueList != null) {
        trueList = trueList.copyAndRedirect(ilist, copyOfilist);
      }
      FlowList falseList = pattern.getFalseList();
      if (falseList != null) {
        falseList = falseList.copyAndRedirect(ilist, copyOfilist);
      }
      
      il.append(copyOfilist);
      

      InstructionHandle gtmpl = getTemplateHandle(template);
      InstructionHandle success = il.append(new GOTO_W(gtmpl));
      
      if (trueList != null) {
        trueList.backPatch(success);
      }
      if (falseList != null) {
        falseList.backPatch(fail);
      }
      

      fail = il.getStart();
      

      if (_instructionList != null) {
        il.append(_instructionList);
      }
      

      _instructionList = il;
    }
    return this._start = fail;
  }
}
