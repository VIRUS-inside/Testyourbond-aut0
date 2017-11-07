package org.apache.xerces.xni;

import org.apache.xerces.xni.parser.XMLDTDContentModelSource;

public abstract interface XMLDTDContentModelHandler
{
  public static final short SEPARATOR_CHOICE = 0;
  public static final short SEPARATOR_SEQUENCE = 1;
  public static final short OCCURS_ZERO_OR_ONE = 2;
  public static final short OCCURS_ZERO_OR_MORE = 3;
  public static final short OCCURS_ONE_OR_MORE = 4;
  
  public abstract void startContentModel(String paramString, Augmentations paramAugmentations)
    throws XNIException;
  
  public abstract void any(Augmentations paramAugmentations)
    throws XNIException;
  
  public abstract void empty(Augmentations paramAugmentations)
    throws XNIException;
  
  public abstract void startGroup(Augmentations paramAugmentations)
    throws XNIException;
  
  public abstract void pcdata(Augmentations paramAugmentations)
    throws XNIException;
  
  public abstract void element(String paramString, Augmentations paramAugmentations)
    throws XNIException;
  
  public abstract void separator(short paramShort, Augmentations paramAugmentations)
    throws XNIException;
  
  public abstract void occurrence(short paramShort, Augmentations paramAugmentations)
    throws XNIException;
  
  public abstract void endGroup(Augmentations paramAugmentations)
    throws XNIException;
  
  public abstract void endContentModel(Augmentations paramAugmentations)
    throws XNIException;
  
  public abstract void setDTDContentModelSource(XMLDTDContentModelSource paramXMLDTDContentModelSource);
  
  public abstract XMLDTDContentModelSource getDTDContentModelSource();
}
