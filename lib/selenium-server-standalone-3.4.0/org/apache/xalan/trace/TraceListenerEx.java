package org.apache.xalan.trace;

import javax.xml.transform.TransformerException;

public abstract interface TraceListenerEx
  extends TraceListener
{
  public abstract void selectEnd(EndSelectionEvent paramEndSelectionEvent)
    throws TransformerException;
}
