package org.apache.xerces.dom3.as;

/**
 * @deprecated
 */
public abstract interface CharacterDataEditAS
  extends NodeEditAS
{
  public abstract boolean getIsWhitespaceOnly();
  
  public abstract boolean canSetData(int paramInt1, int paramInt2);
  
  public abstract boolean canAppendData(String paramString);
  
  public abstract boolean canReplaceData(int paramInt1, int paramInt2, String paramString);
  
  public abstract boolean canInsertData(int paramInt, String paramString);
  
  public abstract boolean canDeleteData(int paramInt1, int paramInt2);
}
