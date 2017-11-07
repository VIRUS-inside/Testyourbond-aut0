package org.apache.xalan.xsltc;

import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.serializer.SerializationHandler;

public abstract interface Translet
{
  public abstract void transform(DOM paramDOM, SerializationHandler paramSerializationHandler)
    throws TransletException;
  
  public abstract void transform(DOM paramDOM, SerializationHandler[] paramArrayOfSerializationHandler)
    throws TransletException;
  
  public abstract void transform(DOM paramDOM, DTMAxisIterator paramDTMAxisIterator, SerializationHandler paramSerializationHandler)
    throws TransletException;
  
  public abstract Object addParameter(String paramString, Object paramObject);
  
  public abstract void buildKeys(DOM paramDOM, DTMAxisIterator paramDTMAxisIterator, SerializationHandler paramSerializationHandler, int paramInt)
    throws TransletException;
  
  public abstract void addAuxiliaryClass(Class paramClass);
  
  public abstract Class getAuxiliaryClass(String paramString);
  
  public abstract String[] getNamesArray();
  
  public abstract String[] getUrisArray();
  
  public abstract int[] getTypesArray();
  
  public abstract String[] getNamespaceArray();
}
