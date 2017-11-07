package org.apache.xerces.impl.xs.models;

import org.apache.xerces.impl.dtd.models.CMNode;
import org.apache.xerces.impl.xs.XSComplexTypeDecl;
import org.apache.xerces.impl.xs.XSDeclarationPool;
import org.apache.xerces.impl.xs.XSElementDecl;
import org.apache.xerces.impl.xs.XSModelGroupImpl;
import org.apache.xerces.impl.xs.XSParticleDecl;

public class CMBuilder
{
  private XSDeclarationPool fDeclPool = null;
  private static final XSEmptyCM fEmptyCM = new XSEmptyCM();
  private int fLeafCount;
  private int fParticleCount;
  private final CMNodeFactory fNodeFactory;
  
  public CMBuilder(CMNodeFactory paramCMNodeFactory)
  {
    fNodeFactory = paramCMNodeFactory;
  }
  
  public void setDeclPool(XSDeclarationPool paramXSDeclarationPool)
  {
    fDeclPool = paramXSDeclarationPool;
  }
  
  public XSCMValidator getContentModel(XSComplexTypeDecl paramXSComplexTypeDecl, boolean paramBoolean)
  {
    int i = paramXSComplexTypeDecl.getContentType();
    if ((i == 1) || (i == 0)) {
      return null;
    }
    XSParticleDecl localXSParticleDecl = (XSParticleDecl)paramXSComplexTypeDecl.getParticle();
    if (localXSParticleDecl == null) {
      return fEmptyCM;
    }
    Object localObject = null;
    if ((fType == 3) && (fValue).fCompositor == 103)) {
      localObject = createAllCM(localXSParticleDecl);
    } else {
      localObject = createDFACM(localXSParticleDecl, paramBoolean);
    }
    fNodeFactory.resetNodeCount();
    if (localObject == null) {
      localObject = fEmptyCM;
    }
    return localObject;
  }
  
  XSCMValidator createAllCM(XSParticleDecl paramXSParticleDecl)
  {
    if (fMaxOccurs == 0) {
      return null;
    }
    XSModelGroupImpl localXSModelGroupImpl = (XSModelGroupImpl)fValue;
    XSAllCM localXSAllCM = new XSAllCM(fMinOccurs == 0, fParticleCount);
    for (int i = 0; i < fParticleCount; i++) {
      localXSAllCM.addElement((XSElementDecl)fParticles[i].fValue, fParticles[i].fMinOccurs == 0);
    }
    return localXSAllCM;
  }
  
  XSCMValidator createDFACM(XSParticleDecl paramXSParticleDecl, boolean paramBoolean)
  {
    fLeafCount = 0;
    fParticleCount = 0;
    CMNode localCMNode = useRepeatingLeafNodes(paramXSParticleDecl) ? buildCompactSyntaxTree(paramXSParticleDecl) : buildSyntaxTree(paramXSParticleDecl, paramBoolean);
    if (localCMNode == null) {
      return null;
    }
    return new XSDFACM(localCMNode, fLeafCount);
  }
  
  private CMNode buildSyntaxTree(XSParticleDecl paramXSParticleDecl, boolean paramBoolean)
  {
    int i = fMaxOccurs;
    int j = fMinOccurs;
    boolean bool = false;
    if (paramBoolean)
    {
      if (j > 1) {
        if ((i > j) || (paramXSParticleDecl.getMaxOccursUnbounded()))
        {
          j = 1;
          bool = true;
        }
        else
        {
          j = 2;
          bool = true;
        }
      }
      if (i > 1)
      {
        i = 2;
        bool = true;
      }
    }
    int k = fType;
    Object localObject = null;
    if ((k == 2) || (k == 1))
    {
      localObject = fNodeFactory.getCMLeafNode(fType, fValue, fParticleCount++, fLeafCount++);
      localObject = expandContentModel((CMNode)localObject, j, i);
      if (localObject != null) {
        ((CMNode)localObject).setIsCompactUPAModel(bool);
      }
    }
    else if (k == 3)
    {
      XSModelGroupImpl localXSModelGroupImpl = (XSModelGroupImpl)fValue;
      CMNode localCMNode = null;
      int m = 0;
      for (int n = 0; n < fParticleCount; n++)
      {
        localCMNode = buildSyntaxTree(fParticles[n], paramBoolean);
        if (localCMNode != null)
        {
          bool |= localCMNode.isCompactedForUPA();
          m++;
          if (localObject == null) {
            localObject = localCMNode;
          } else {
            localObject = fNodeFactory.getCMBinOpNode(fCompositor, (CMNode)localObject, localCMNode);
          }
        }
      }
      if (localObject != null)
      {
        if ((fCompositor == 101) && (m < fParticleCount)) {
          localObject = fNodeFactory.getCMUniOpNode(5, (CMNode)localObject);
        }
        localObject = expandContentModel((CMNode)localObject, j, i);
        ((CMNode)localObject).setIsCompactUPAModel(bool);
      }
    }
    return localObject;
  }
  
  private CMNode expandContentModel(CMNode paramCMNode, int paramInt1, int paramInt2)
  {
    CMNode localCMNode = null;
    if ((paramInt1 == 1) && (paramInt2 == 1))
    {
      localCMNode = paramCMNode;
    }
    else if ((paramInt1 == 0) && (paramInt2 == 1))
    {
      localCMNode = fNodeFactory.getCMUniOpNode(5, paramCMNode);
    }
    else if ((paramInt1 == 0) && (paramInt2 == -1))
    {
      localCMNode = fNodeFactory.getCMUniOpNode(4, paramCMNode);
    }
    else if ((paramInt1 == 1) && (paramInt2 == -1))
    {
      localCMNode = fNodeFactory.getCMUniOpNode(6, paramCMNode);
    }
    else if (paramInt2 == -1)
    {
      localCMNode = fNodeFactory.getCMUniOpNode(6, paramCMNode);
      localCMNode = fNodeFactory.getCMBinOpNode(102, multiNodes(paramCMNode, paramInt1 - 1, true), localCMNode);
    }
    else
    {
      if (paramInt1 > 0) {
        localCMNode = multiNodes(paramCMNode, paramInt1, false);
      }
      if (paramInt2 > paramInt1)
      {
        paramCMNode = fNodeFactory.getCMUniOpNode(5, paramCMNode);
        if (localCMNode == null) {
          localCMNode = multiNodes(paramCMNode, paramInt2 - paramInt1, false);
        } else {
          localCMNode = fNodeFactory.getCMBinOpNode(102, localCMNode, multiNodes(paramCMNode, paramInt2 - paramInt1, true));
        }
      }
    }
    return localCMNode;
  }
  
  private CMNode multiNodes(CMNode paramCMNode, int paramInt, boolean paramBoolean)
  {
    if (paramInt == 0) {
      return null;
    }
    if (paramInt == 1) {
      return paramBoolean ? copyNode(paramCMNode) : paramCMNode;
    }
    int i = paramInt / 2;
    return fNodeFactory.getCMBinOpNode(102, multiNodes(paramCMNode, i, paramBoolean), multiNodes(paramCMNode, paramInt - i, true));
  }
  
  private CMNode copyNode(CMNode paramCMNode)
  {
    int i = paramCMNode.type();
    Object localObject;
    if ((i == 101) || (i == 102))
    {
      localObject = (XSCMBinOp)paramCMNode;
      paramCMNode = fNodeFactory.getCMBinOpNode(i, copyNode(((XSCMBinOp)localObject).getLeft()), copyNode(((XSCMBinOp)localObject).getRight()));
    }
    else if ((i == 4) || (i == 6) || (i == 5))
    {
      localObject = (XSCMUniOp)paramCMNode;
      paramCMNode = fNodeFactory.getCMUniOpNode(i, copyNode(((XSCMUniOp)localObject).getChild()));
    }
    else if ((i == 1) || (i == 2))
    {
      localObject = (XSCMLeaf)paramCMNode;
      paramCMNode = fNodeFactory.getCMLeafNode(((XSCMLeaf)localObject).type(), ((XSCMLeaf)localObject).getLeaf(), ((XSCMLeaf)localObject).getParticleId(), fLeafCount++);
    }
    return paramCMNode;
  }
  
  private CMNode buildCompactSyntaxTree(XSParticleDecl paramXSParticleDecl)
  {
    int i = fMaxOccurs;
    int j = fMinOccurs;
    int k = fType;
    Object localObject = null;
    if ((k == 2) || (k == 1)) {
      return buildCompactSyntaxTree2(paramXSParticleDecl, j, i);
    }
    if (k == 3)
    {
      XSModelGroupImpl localXSModelGroupImpl = (XSModelGroupImpl)fValue;
      if ((fParticleCount == 1) && ((j != 1) || (i != 1))) {
        return buildCompactSyntaxTree2(fParticles[0], j, i);
      }
      CMNode localCMNode = null;
      int m = 0;
      for (int n = 0; n < fParticleCount; n++)
      {
        localCMNode = buildCompactSyntaxTree(fParticles[n]);
        if (localCMNode != null)
        {
          m++;
          if (localObject == null) {
            localObject = localCMNode;
          } else {
            localObject = fNodeFactory.getCMBinOpNode(fCompositor, (CMNode)localObject, localCMNode);
          }
        }
      }
      if ((localObject != null) && (fCompositor == 101) && (m < fParticleCount)) {
        localObject = fNodeFactory.getCMUniOpNode(5, (CMNode)localObject);
      }
    }
    return localObject;
  }
  
  private CMNode buildCompactSyntaxTree2(XSParticleDecl paramXSParticleDecl, int paramInt1, int paramInt2)
  {
    CMNode localCMNode = null;
    if ((paramInt1 == 1) && (paramInt2 == 1))
    {
      localCMNode = fNodeFactory.getCMLeafNode(fType, fValue, fParticleCount++, fLeafCount++);
    }
    else if ((paramInt1 == 0) && (paramInt2 == 1))
    {
      localCMNode = fNodeFactory.getCMLeafNode(fType, fValue, fParticleCount++, fLeafCount++);
      localCMNode = fNodeFactory.getCMUniOpNode(5, localCMNode);
    }
    else if ((paramInt1 == 0) && (paramInt2 == -1))
    {
      localCMNode = fNodeFactory.getCMLeafNode(fType, fValue, fParticleCount++, fLeafCount++);
      localCMNode = fNodeFactory.getCMUniOpNode(4, localCMNode);
    }
    else if ((paramInt1 == 1) && (paramInt2 == -1))
    {
      localCMNode = fNodeFactory.getCMLeafNode(fType, fValue, fParticleCount++, fLeafCount++);
      localCMNode = fNodeFactory.getCMUniOpNode(6, localCMNode);
    }
    else
    {
      localCMNode = fNodeFactory.getCMRepeatingLeafNode(fType, fValue, paramInt1, paramInt2, fParticleCount++, fLeafCount++);
      if (paramInt1 == 0) {
        localCMNode = fNodeFactory.getCMUniOpNode(4, localCMNode);
      } else {
        localCMNode = fNodeFactory.getCMUniOpNode(6, localCMNode);
      }
    }
    return localCMNode;
  }
  
  private boolean useRepeatingLeafNodes(XSParticleDecl paramXSParticleDecl)
  {
    int i = fMaxOccurs;
    int j = fMinOccurs;
    int k = fType;
    if (k == 3)
    {
      XSModelGroupImpl localXSModelGroupImpl = (XSModelGroupImpl)fValue;
      if ((j != 1) || (i != 1))
      {
        if (fParticleCount == 1)
        {
          XSParticleDecl localXSParticleDecl = fParticles[0];
          int n = fType;
          return ((n == 1) || (n == 2)) && (fMinOccurs == 1) && (fMaxOccurs == 1);
        }
        return fParticleCount == 0;
      }
      for (int m = 0; m < fParticleCount; m++) {
        if (!useRepeatingLeafNodes(fParticles[m])) {
          return false;
        }
      }
    }
    return true;
  }
}
