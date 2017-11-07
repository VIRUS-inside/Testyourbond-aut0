package org.apache.xerces.jaxp;

import java.util.HashMap;
import org.apache.xerces.impl.validation.EntityState;
import org.apache.xerces.impl.validation.ValidationManager;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.XMLDTDHandler;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLDTDFilter;
import org.apache.xerces.xni.parser.XMLDTDSource;

final class UnparsedEntityHandler
  implements XMLDTDFilter, EntityState
{
  private XMLDTDSource fDTDSource;
  private XMLDTDHandler fDTDHandler;
  private final ValidationManager fValidationManager;
  private HashMap fUnparsedEntities = null;
  
  UnparsedEntityHandler(ValidationManager paramValidationManager)
  {
    fValidationManager = paramValidationManager;
  }
  
  public void startDTD(XMLLocator paramXMLLocator, Augmentations paramAugmentations)
    throws XNIException
  {
    fValidationManager.setEntityState(this);
    if (fDTDHandler != null) {
      fDTDHandler.startDTD(paramXMLLocator, paramAugmentations);
    }
  }
  
  public void startParameterEntity(String paramString1, XMLResourceIdentifier paramXMLResourceIdentifier, String paramString2, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDTDHandler != null) {
      fDTDHandler.startParameterEntity(paramString1, paramXMLResourceIdentifier, paramString2, paramAugmentations);
    }
  }
  
  public void textDecl(String paramString1, String paramString2, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDTDHandler != null) {
      fDTDHandler.textDecl(paramString1, paramString2, paramAugmentations);
    }
  }
  
  public void endParameterEntity(String paramString, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDTDHandler != null) {
      fDTDHandler.endParameterEntity(paramString, paramAugmentations);
    }
  }
  
  public void startExternalSubset(XMLResourceIdentifier paramXMLResourceIdentifier, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDTDHandler != null) {
      fDTDHandler.startExternalSubset(paramXMLResourceIdentifier, paramAugmentations);
    }
  }
  
  public void endExternalSubset(Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDTDHandler != null) {
      fDTDHandler.endExternalSubset(paramAugmentations);
    }
  }
  
  public void comment(XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDTDHandler != null) {
      fDTDHandler.comment(paramXMLString, paramAugmentations);
    }
  }
  
  public void processingInstruction(String paramString, XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDTDHandler != null) {
      fDTDHandler.processingInstruction(paramString, paramXMLString, paramAugmentations);
    }
  }
  
  public void elementDecl(String paramString1, String paramString2, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDTDHandler != null) {
      fDTDHandler.elementDecl(paramString1, paramString2, paramAugmentations);
    }
  }
  
  public void startAttlist(String paramString, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDTDHandler != null) {
      fDTDHandler.startAttlist(paramString, paramAugmentations);
    }
  }
  
  public void attributeDecl(String paramString1, String paramString2, String paramString3, String[] paramArrayOfString, String paramString4, XMLString paramXMLString1, XMLString paramXMLString2, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDTDHandler != null) {
      fDTDHandler.attributeDecl(paramString1, paramString2, paramString3, paramArrayOfString, paramString4, paramXMLString1, paramXMLString2, paramAugmentations);
    }
  }
  
  public void endAttlist(Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDTDHandler != null) {
      fDTDHandler.endAttlist(paramAugmentations);
    }
  }
  
  public void internalEntityDecl(String paramString, XMLString paramXMLString1, XMLString paramXMLString2, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDTDHandler != null) {
      fDTDHandler.internalEntityDecl(paramString, paramXMLString1, paramXMLString2, paramAugmentations);
    }
  }
  
  public void externalEntityDecl(String paramString, XMLResourceIdentifier paramXMLResourceIdentifier, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDTDHandler != null) {
      fDTDHandler.externalEntityDecl(paramString, paramXMLResourceIdentifier, paramAugmentations);
    }
  }
  
  public void unparsedEntityDecl(String paramString1, XMLResourceIdentifier paramXMLResourceIdentifier, String paramString2, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fUnparsedEntities == null) {
      fUnparsedEntities = new HashMap();
    }
    fUnparsedEntities.put(paramString1, paramString1);
    if (fDTDHandler != null) {
      fDTDHandler.unparsedEntityDecl(paramString1, paramXMLResourceIdentifier, paramString2, paramAugmentations);
    }
  }
  
  public void notationDecl(String paramString, XMLResourceIdentifier paramXMLResourceIdentifier, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDTDHandler != null) {
      fDTDHandler.notationDecl(paramString, paramXMLResourceIdentifier, paramAugmentations);
    }
  }
  
  public void startConditional(short paramShort, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDTDHandler != null) {
      fDTDHandler.startConditional(paramShort, paramAugmentations);
    }
  }
  
  public void ignoredCharacters(XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDTDHandler != null) {
      fDTDHandler.ignoredCharacters(paramXMLString, paramAugmentations);
    }
  }
  
  public void endConditional(Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDTDHandler != null) {
      fDTDHandler.endConditional(paramAugmentations);
    }
  }
  
  public void endDTD(Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDTDHandler != null) {
      fDTDHandler.endDTD(paramAugmentations);
    }
  }
  
  public void setDTDSource(XMLDTDSource paramXMLDTDSource)
  {
    fDTDSource = paramXMLDTDSource;
  }
  
  public XMLDTDSource getDTDSource()
  {
    return fDTDSource;
  }
  
  public void setDTDHandler(XMLDTDHandler paramXMLDTDHandler)
  {
    fDTDHandler = paramXMLDTDHandler;
  }
  
  public XMLDTDHandler getDTDHandler()
  {
    return fDTDHandler;
  }
  
  public boolean isEntityDeclared(String paramString)
  {
    return false;
  }
  
  public boolean isEntityUnparsed(String paramString)
  {
    if (fUnparsedEntities != null) {
      return fUnparsedEntities.containsKey(paramString);
    }
    return false;
  }
  
  public void reset()
  {
    if ((fUnparsedEntities != null) && (!fUnparsedEntities.isEmpty())) {
      fUnparsedEntities.clear();
    }
  }
}
