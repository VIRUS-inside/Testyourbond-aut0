package org.apache.xml.serializer;

import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

public abstract interface ExtendedLexicalHandler
  extends LexicalHandler
{
  public abstract void comment(String paramString)
    throws SAXException;
}
