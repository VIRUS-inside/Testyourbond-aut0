package net.sourceforge.htmlunit.cyberneko;

import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;

public abstract interface HTMLTagBalancingListener
{
  public abstract void ignoredStartElement(QName paramQName, XMLAttributes paramXMLAttributes, Augmentations paramAugmentations);
  
  public abstract void ignoredEndElement(QName paramQName, Augmentations paramAugmentations);
}
