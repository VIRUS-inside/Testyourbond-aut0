package org.apache.xml.dtm.ref;

import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;

/**
 * @deprecated
 */
public abstract interface CoroutineParser
{
  public abstract int getParserCoroutineID();
  
  public abstract CoroutineManager getCoroutineManager();
  
  public abstract void setContentHandler(ContentHandler paramContentHandler);
  
  public abstract void setLexHandler(LexicalHandler paramLexicalHandler);
  
  public abstract Object doParse(InputSource paramInputSource, int paramInt);
  
  public abstract Object doMore(boolean paramBoolean, int paramInt);
  
  public abstract void doTerminate(int paramInt);
  
  public abstract void init(CoroutineManager paramCoroutineManager, int paramInt, XMLReader paramXMLReader);
}
