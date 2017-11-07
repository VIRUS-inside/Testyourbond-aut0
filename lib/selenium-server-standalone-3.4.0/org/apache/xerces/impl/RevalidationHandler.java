package org.apache.xerces.impl;

import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.parser.XMLDocumentFilter;

public abstract interface RevalidationHandler
  extends XMLDocumentFilter
{
  public abstract boolean characterData(String paramString, Augmentations paramAugmentations);
}
