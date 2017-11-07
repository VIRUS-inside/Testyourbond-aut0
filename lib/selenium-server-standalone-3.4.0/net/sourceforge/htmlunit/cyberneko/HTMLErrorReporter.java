package net.sourceforge.htmlunit.cyberneko;

import org.apache.xerces.xni.parser.XMLParseException;

public abstract interface HTMLErrorReporter
{
  public abstract String formatMessage(String paramString, Object[] paramArrayOfObject);
  
  public abstract void reportWarning(String paramString, Object[] paramArrayOfObject)
    throws XMLParseException;
  
  public abstract void reportError(String paramString, Object[] paramArrayOfObject)
    throws XMLParseException;
}
