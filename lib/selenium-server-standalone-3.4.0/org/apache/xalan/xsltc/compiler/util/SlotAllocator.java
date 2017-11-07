package org.apache.xalan.xsltc.compiler.util;

import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.Type;






















final class SlotAllocator
{
  private int _firstAvailableSlot;
  
  SlotAllocator() {}
  
  private int _size = 8;
  private int _free = 0;
  private int[] _slotsTaken = new int[_size];
  
  public void initialize(LocalVariableGen[] vars) {
    int length = vars.length;
    int slot = 0;
    
    for (int i = 0; i < length; i++) {
      int size = vars[i].getType().getSize();
      int index = vars[i].getIndex();
      slot = Math.max(slot, index + size);
    }
    _firstAvailableSlot = slot;
  }
  
  public int allocateSlot(Type type) {
    int size = type.getSize();
    int limit = _free;
    int slot = _firstAvailableSlot;int where = 0;
    
    if (_free + size > _size) {
      int[] array = new int[this._size *= 2];
      for (int j = 0; j < limit; j++)
        array[j] = _slotsTaken[j];
      _slotsTaken = array;
    }
    
    while (where < limit) {
      if (slot + size <= _slotsTaken[where])
      {
        for (int j = limit - 1; j >= where; j--)
          _slotsTaken[(j + size)] = _slotsTaken[j];
        break;
      }
      
      slot = _slotsTaken[(where++)] + 1;
    }
    

    for (int j = 0; j < size; j++) {
      _slotsTaken[(where + j)] = (slot + j);
    }
    _free += size;
    return slot;
  }
  
  public void releaseSlot(LocalVariableGen lvg) {
    int size = lvg.getType().getSize();
    int slot = lvg.getIndex();
    int limit = _free;
    
    for (int i = 0; i < limit; i++) {
      if (_slotsTaken[i] == slot) {
        int j = i + size;
        while (j < limit) {
          _slotsTaken[(i++)] = _slotsTaken[(j++)];
        }
        _free -= size;
        return;
      }
    }
    String state = "Variable slot allocation error(size=" + size + ", slot=" + slot + ", limit=" + limit + ")";
    
    ErrorMsg err = new ErrorMsg("INTERNAL_ERR", state);
    throw new Error(err.toString());
  }
}
