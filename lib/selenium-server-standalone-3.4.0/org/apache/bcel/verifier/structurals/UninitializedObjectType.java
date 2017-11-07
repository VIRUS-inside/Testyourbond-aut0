package org.apache.bcel.verifier.structurals;

import org.apache.bcel.Constants;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.ReferenceType;





























































public class UninitializedObjectType
  extends ReferenceType
  implements Constants
{
  private ObjectType initialized;
  
  public UninitializedObjectType(ObjectType t)
  {
    super((byte)15, "<UNINITIALIZED OBJECT OF TYPE '" + t.getClassName() + "'>");
    initialized = t;
  }
  



  public ObjectType getInitialized()
  {
    return initialized;
  }
  





  public boolean equals(Object o)
  {
    if (!(o instanceof UninitializedObjectType)) return false;
    return initialized.equals(initialized);
  }
}
