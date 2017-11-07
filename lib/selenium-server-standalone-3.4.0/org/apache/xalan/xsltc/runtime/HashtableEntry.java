package org.apache.xalan.xsltc.runtime;






class HashtableEntry
{
  int hash;
  




  Object key;
  




  Object value;
  




  HashtableEntry next;
  





  HashtableEntry() {}
  





  protected Object clone()
  {
    HashtableEntry entry = new HashtableEntry();
    hash = hash;
    key = key;
    value = value;
    next = (next != null ? (HashtableEntry)next.clone() : null);
    return entry;
  }
}
