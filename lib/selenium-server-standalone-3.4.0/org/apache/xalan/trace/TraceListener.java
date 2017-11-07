package org.apache.xalan.trace;

import java.util.EventListener;
import javax.xml.transform.TransformerException;

public abstract interface TraceListener
  extends EventListener
{
  public abstract void trace(TracerEvent paramTracerEvent);
  
  public abstract void selected(SelectionEvent paramSelectionEvent)
    throws TransformerException;
  
  public abstract void generated(GenerateEvent paramGenerateEvent);
}
