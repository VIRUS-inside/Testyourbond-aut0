package org.apache.xalan.trace;

public abstract interface TraceListenerEx3
  extends TraceListenerEx2
{
  public abstract void extension(ExtensionEvent paramExtensionEvent);
  
  public abstract void extensionEnd(ExtensionEvent paramExtensionEvent);
}
