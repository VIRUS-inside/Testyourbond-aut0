package org.apache.xerces.xs;

import java.util.List;
import org.w3c.dom.ls.LSInput;

public abstract interface LSInputList
  extends List
{
  public abstract int getLength();
  
  public abstract LSInput item(int paramInt);
}
