package org.apache.xalan.xsltc.compiler;

import java.util.Iterator;
import java.util.Vector;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;

























public final class FlowList
{
  private Vector _elements;
  
  public FlowList()
  {
    _elements = null;
  }
  
  public FlowList(InstructionHandle bh) {
    _elements = new Vector();
    _elements.addElement(bh);
  }
  
  public FlowList(FlowList list) {
    _elements = _elements;
  }
  
  public FlowList add(InstructionHandle bh) {
    if (_elements == null) {
      _elements = new Vector();
    }
    _elements.addElement(bh);
    return this;
  }
  
  public FlowList append(FlowList right) {
    if (_elements == null) {
      _elements = _elements;
    }
    else {
      Vector temp = _elements;
      if (temp != null) {
        int n = temp.size();
        for (int i = 0; i < n; i++) {
          _elements.addElement(temp.elementAt(i));
        }
      }
    }
    return this;
  }
  


  public void backPatch(InstructionHandle target)
  {
    if (_elements != null) {
      int n = _elements.size();
      for (int i = 0; i < n; i++) {
        BranchHandle bh = (BranchHandle)_elements.elementAt(i);
        bh.setTarget(target);
      }
      _elements.clear();
    }
  }
  





  public FlowList copyAndRedirect(InstructionList oldList, InstructionList newList)
  {
    FlowList result = new FlowList();
    if (_elements == null) {
      return result;
    }
    
    int n = _elements.size();
    Iterator oldIter = oldList.iterator();
    Iterator newIter = newList.iterator();
    
    while (oldIter.hasNext()) {
      InstructionHandle oldIh = (InstructionHandle)oldIter.next();
      InstructionHandle newIh = (InstructionHandle)newIter.next();
      
      for (int i = 0; i < n; i++) {
        if (_elements.elementAt(i) == oldIh) {
          result.add(newIh);
        }
      }
    }
    return result;
  }
}
