package org.seleniumhq.jetty9.util.component;

import java.io.IOException;
import org.seleniumhq.jetty9.util.annotation.ManagedObject;
import org.seleniumhq.jetty9.util.annotation.ManagedOperation;

@ManagedObject("Dumpable Object")
public abstract interface Dumpable
{
  @ManagedOperation(value="Dump the nested Object state as a String", impact="INFO")
  public abstract String dump();
  
  public abstract void dump(Appendable paramAppendable, String paramString)
    throws IOException;
}
