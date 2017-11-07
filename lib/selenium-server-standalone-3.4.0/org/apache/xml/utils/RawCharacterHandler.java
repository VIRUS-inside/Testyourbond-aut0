package org.apache.xml.utils;

import javax.xml.transform.TransformerException;

public abstract interface RawCharacterHandler
{
  public abstract void charactersRaw(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws TransformerException;
}
