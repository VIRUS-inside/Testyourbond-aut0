package org.apache.xerces.impl.xs.traversers;

import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.impl.xs.XSAnnotationImpl;
import org.apache.xerces.impl.xs.XSModelGroupImpl;
import org.apache.xerces.impl.xs.XSParticleDecl;
import org.apache.xerces.impl.xs.util.XInt;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.util.DOMUtil;
import org.apache.xerces.xs.XSObject;
import org.w3c.dom.Element;

abstract class XSDAbstractParticleTraverser
  extends XSDAbstractTraverser
{
  ParticleArray fPArray = new ParticleArray();
  
  XSDAbstractParticleTraverser(XSDHandler paramXSDHandler, XSAttributeChecker paramXSAttributeChecker)
  {
    super(paramXSDHandler, paramXSAttributeChecker);
  }
  
  XSParticleDecl traverseAll(Element paramElement, XSDocumentInfo paramXSDocumentInfo, SchemaGrammar paramSchemaGrammar, int paramInt, XSObject paramXSObject)
  {
    Object[] arrayOfObject = fAttrChecker.checkAttributes(paramElement, false, paramXSDocumentInfo);
    Element localElement = DOMUtil.getFirstChildElement(paramElement);
    XSAnnotationImpl localXSAnnotationImpl = null;
    if ((localElement != null) && (DOMUtil.getLocalName(localElement).equals(SchemaSymbols.ELT_ANNOTATION)))
    {
      localXSAnnotationImpl = traverseAnnotationDecl(localElement, arrayOfObject, false, paramXSDocumentInfo);
      localElement = DOMUtil.getNextSiblingElement(localElement);
    }
    else
    {
      str = DOMUtil.getSyntheticAnnotation(paramElement);
      if (str != null) {
        localXSAnnotationImpl = traverseSyntheticAnnotation(paramElement, str, arrayOfObject, false, paramXSDocumentInfo);
      }
    }
    String str = null;
    fPArray.pushContext();
    while (localElement != null)
    {
      localXSParticleDecl = null;
      str = DOMUtil.getLocalName(localElement);
      if (str.equals(SchemaSymbols.ELT_ELEMENT))
      {
        localXSParticleDecl = fSchemaHandler.fElementTraverser.traverseLocal(localElement, paramXSDocumentInfo, paramSchemaGrammar, 1, paramXSObject);
      }
      else
      {
        localObject = new Object[] { "all", "(annotation?, element*)", DOMUtil.getLocalName(localElement) };
        reportSchemaError("s4s-elt-must-match.1", (Object[])localObject, localElement);
      }
      if (localXSParticleDecl != null) {
        fPArray.addParticle(localXSParticleDecl);
      }
      localElement = DOMUtil.getNextSiblingElement(localElement);
    }
    XSParticleDecl localXSParticleDecl = null;
    Object localObject = (XInt)arrayOfObject[XSAttributeChecker.ATTIDX_MINOCCURS];
    XInt localXInt = (XInt)arrayOfObject[XSAttributeChecker.ATTIDX_MAXOCCURS];
    Long localLong = (Long)arrayOfObject[XSAttributeChecker.ATTIDX_FROMDEFAULT];
    XSModelGroupImpl localXSModelGroupImpl = new XSModelGroupImpl();
    fCompositor = 103;
    fParticleCount = fPArray.getParticleCount();
    fParticles = fPArray.popContext();
    XSObjectListImpl localXSObjectListImpl;
    if (localXSAnnotationImpl != null)
    {
      localXSObjectListImpl = new XSObjectListImpl();
      ((XSObjectListImpl)localXSObjectListImpl).addXSObject(localXSAnnotationImpl);
    }
    else
    {
      localXSObjectListImpl = XSObjectListImpl.EMPTY_LIST;
    }
    fAnnotations = localXSObjectListImpl;
    localXSParticleDecl = new XSParticleDecl();
    fType = 3;
    fMinOccurs = ((XInt)localObject).intValue();
    fMaxOccurs = localXInt.intValue();
    fValue = localXSModelGroupImpl;
    fAnnotations = localXSObjectListImpl;
    localXSParticleDecl = checkOccurrences(localXSParticleDecl, SchemaSymbols.ELT_ALL, (Element)paramElement.getParentNode(), paramInt, localLong.longValue());
    fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
    return localXSParticleDecl;
  }
  
  XSParticleDecl traverseSequence(Element paramElement, XSDocumentInfo paramXSDocumentInfo, SchemaGrammar paramSchemaGrammar, int paramInt, XSObject paramXSObject)
  {
    return traverseSeqChoice(paramElement, paramXSDocumentInfo, paramSchemaGrammar, paramInt, false, paramXSObject);
  }
  
  XSParticleDecl traverseChoice(Element paramElement, XSDocumentInfo paramXSDocumentInfo, SchemaGrammar paramSchemaGrammar, int paramInt, XSObject paramXSObject)
  {
    return traverseSeqChoice(paramElement, paramXSDocumentInfo, paramSchemaGrammar, paramInt, true, paramXSObject);
  }
  
  private XSParticleDecl traverseSeqChoice(Element paramElement, XSDocumentInfo paramXSDocumentInfo, SchemaGrammar paramSchemaGrammar, int paramInt, boolean paramBoolean, XSObject paramXSObject)
  {
    Object[] arrayOfObject = fAttrChecker.checkAttributes(paramElement, false, paramXSDocumentInfo);
    Element localElement = DOMUtil.getFirstChildElement(paramElement);
    XSAnnotationImpl localXSAnnotationImpl = null;
    if ((localElement != null) && (DOMUtil.getLocalName(localElement).equals(SchemaSymbols.ELT_ANNOTATION)))
    {
      localXSAnnotationImpl = traverseAnnotationDecl(localElement, arrayOfObject, false, paramXSDocumentInfo);
      localElement = DOMUtil.getNextSiblingElement(localElement);
    }
    else
    {
      str = DOMUtil.getSyntheticAnnotation(paramElement);
      if (str != null) {
        localXSAnnotationImpl = traverseSyntheticAnnotation(paramElement, str, arrayOfObject, false, paramXSDocumentInfo);
      }
    }
    String str = null;
    fPArray.pushContext();
    while (localElement != null)
    {
      localXSParticleDecl = null;
      str = DOMUtil.getLocalName(localElement);
      if (str.equals(SchemaSymbols.ELT_ELEMENT))
      {
        localXSParticleDecl = fSchemaHandler.fElementTraverser.traverseLocal(localElement, paramXSDocumentInfo, paramSchemaGrammar, 0, paramXSObject);
      }
      else if (str.equals(SchemaSymbols.ELT_GROUP))
      {
        localXSParticleDecl = fSchemaHandler.fGroupTraverser.traverseLocal(localElement, paramXSDocumentInfo, paramSchemaGrammar);
        if (hasAllContent(localXSParticleDecl))
        {
          localXSParticleDecl = null;
          reportSchemaError("cos-all-limited.1.2", null, localElement);
        }
      }
      else if (str.equals(SchemaSymbols.ELT_CHOICE))
      {
        localXSParticleDecl = traverseChoice(localElement, paramXSDocumentInfo, paramSchemaGrammar, 0, paramXSObject);
      }
      else if (str.equals(SchemaSymbols.ELT_SEQUENCE))
      {
        localXSParticleDecl = traverseSequence(localElement, paramXSDocumentInfo, paramSchemaGrammar, 0, paramXSObject);
      }
      else if (str.equals(SchemaSymbols.ELT_ANY))
      {
        localXSParticleDecl = fSchemaHandler.fWildCardTraverser.traverseAny(localElement, paramXSDocumentInfo, paramSchemaGrammar);
      }
      else
      {
        if (paramBoolean) {
          localObject = new Object[] { "choice", "(annotation?, (element | group | choice | sequence | any)*)", DOMUtil.getLocalName(localElement) };
        } else {
          localObject = new Object[] { "sequence", "(annotation?, (element | group | choice | sequence | any)*)", DOMUtil.getLocalName(localElement) };
        }
        reportSchemaError("s4s-elt-must-match.1", (Object[])localObject, localElement);
      }
      if (localXSParticleDecl != null) {
        fPArray.addParticle(localXSParticleDecl);
      }
      localElement = DOMUtil.getNextSiblingElement(localElement);
    }
    XSParticleDecl localXSParticleDecl = null;
    Object localObject = (XInt)arrayOfObject[XSAttributeChecker.ATTIDX_MINOCCURS];
    XInt localXInt = (XInt)arrayOfObject[XSAttributeChecker.ATTIDX_MAXOCCURS];
    Long localLong = (Long)arrayOfObject[XSAttributeChecker.ATTIDX_FROMDEFAULT];
    XSModelGroupImpl localXSModelGroupImpl = new XSModelGroupImpl();
    fCompositor = (paramBoolean ? 101 : 102);
    fParticleCount = fPArray.getParticleCount();
    fParticles = fPArray.popContext();
    XSObjectListImpl localXSObjectListImpl;
    if (localXSAnnotationImpl != null)
    {
      localXSObjectListImpl = new XSObjectListImpl();
      ((XSObjectListImpl)localXSObjectListImpl).addXSObject(localXSAnnotationImpl);
    }
    else
    {
      localXSObjectListImpl = XSObjectListImpl.EMPTY_LIST;
    }
    fAnnotations = localXSObjectListImpl;
    localXSParticleDecl = new XSParticleDecl();
    fType = 3;
    fMinOccurs = ((XInt)localObject).intValue();
    fMaxOccurs = localXInt.intValue();
    fValue = localXSModelGroupImpl;
    fAnnotations = localXSObjectListImpl;
    localXSParticleDecl = checkOccurrences(localXSParticleDecl, paramBoolean ? SchemaSymbols.ELT_CHOICE : SchemaSymbols.ELT_SEQUENCE, (Element)paramElement.getParentNode(), paramInt, localLong.longValue());
    fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
    return localXSParticleDecl;
  }
  
  protected boolean hasAllContent(XSParticleDecl paramXSParticleDecl)
  {
    if ((paramXSParticleDecl != null) && (fType == 3)) {
      return fValue).fCompositor == 103;
    }
    return false;
  }
  
  static class ParticleArray
  {
    XSParticleDecl[] fParticles = new XSParticleDecl[10];
    int[] fPos = new int[5];
    int fContextCount = 0;
    
    ParticleArray() {}
    
    void pushContext()
    {
      fContextCount += 1;
      if (fContextCount == fPos.length)
      {
        int i = fContextCount * 2;
        int[] arrayOfInt = new int[i];
        System.arraycopy(fPos, 0, arrayOfInt, 0, fContextCount);
        fPos = arrayOfInt;
      }
      fPos[fContextCount] = fPos[(fContextCount - 1)];
    }
    
    int getParticleCount()
    {
      return fPos[fContextCount] - fPos[(fContextCount - 1)];
    }
    
    void addParticle(XSParticleDecl paramXSParticleDecl)
    {
      if (fPos[fContextCount] == fParticles.length)
      {
        int i = fPos[fContextCount] * 2;
        XSParticleDecl[] arrayOfXSParticleDecl = new XSParticleDecl[i];
        System.arraycopy(fParticles, 0, arrayOfXSParticleDecl, 0, fPos[fContextCount]);
        fParticles = arrayOfXSParticleDecl;
      }
      int tmp70_67 = fContextCount;
      int[] tmp70_63 = fPos;
      int tmp72_71 = tmp70_63[tmp70_67];
      tmp70_63[tmp70_67] = (tmp72_71 + 1);
      fParticles[tmp72_71] = paramXSParticleDecl;
    }
    
    XSParticleDecl[] popContext()
    {
      int i = fPos[fContextCount] - fPos[(fContextCount - 1)];
      XSParticleDecl[] arrayOfXSParticleDecl = null;
      if (i != 0)
      {
        arrayOfXSParticleDecl = new XSParticleDecl[i];
        System.arraycopy(fParticles, fPos[(fContextCount - 1)], arrayOfXSParticleDecl, 0, i);
        for (int j = fPos[(fContextCount - 1)]; j < fPos[fContextCount]; j++) {
          fParticles[j] = null;
        }
      }
      fContextCount -= 1;
      return arrayOfXSParticleDecl;
    }
  }
}
