package org.apache.xerces.impl;

import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XNIException;

public abstract interface XMLEntityHandler
{
  public abstract void startEntity(String paramString1, XMLResourceIdentifier paramXMLResourceIdentifier, String paramString2, Augmentations paramAugmentations)
    throws XNIException;
  
  public abstract void endEntity(String paramString, Augmentations paramAugmentations)
    throws XNIException;
}
