package org.apache.xerces.impl.xs;

import org.apache.xerces.impl.dv.xs.SchemaDVFactoryImpl;
import org.apache.xerces.impl.dv.xs.XSSimpleTypeDecl;

public final class XSDeclarationPool
{
  private static final int CHUNK_SHIFT = 8;
  private static final int CHUNK_SIZE = 256;
  private static final int CHUNK_MASK = 255;
  private static final int INITIAL_CHUNK_COUNT = 4;
  private XSElementDecl[][] fElementDecl = new XSElementDecl[4][];
  private int fElementDeclIndex = 0;
  private XSParticleDecl[][] fParticleDecl = new XSParticleDecl[4][];
  private int fParticleDeclIndex = 0;
  private XSModelGroupImpl[][] fModelGroup = new XSModelGroupImpl[4][];
  private int fModelGroupIndex = 0;
  private XSAttributeDecl[][] fAttrDecl = new XSAttributeDecl[4][];
  private int fAttrDeclIndex = 0;
  private XSComplexTypeDecl[][] fCTDecl = new XSComplexTypeDecl[4][];
  private int fCTDeclIndex = 0;
  private XSSimpleTypeDecl[][] fSTDecl = new XSSimpleTypeDecl[4][];
  private int fSTDeclIndex = 0;
  private XSAttributeUseImpl[][] fAttributeUse = new XSAttributeUseImpl[4][];
  private int fAttributeUseIndex = 0;
  private SchemaDVFactoryImpl dvFactory;
  
  public XSDeclarationPool() {}
  
  public void setDVFactory(SchemaDVFactoryImpl paramSchemaDVFactoryImpl)
  {
    dvFactory = paramSchemaDVFactoryImpl;
  }
  
  public final XSElementDecl getElementDecl()
  {
    int i = fElementDeclIndex >> 8;
    int j = fElementDeclIndex & 0xFF;
    ensureElementDeclCapacity(i);
    if (fElementDecl[i][j] == null) {
      fElementDecl[i][j] = new XSElementDecl();
    } else {
      fElementDecl[i][j].reset();
    }
    fElementDeclIndex += 1;
    return fElementDecl[i][j];
  }
  
  public final XSAttributeDecl getAttributeDecl()
  {
    int i = fAttrDeclIndex >> 8;
    int j = fAttrDeclIndex & 0xFF;
    ensureAttrDeclCapacity(i);
    if (fAttrDecl[i][j] == null) {
      fAttrDecl[i][j] = new XSAttributeDecl();
    } else {
      fAttrDecl[i][j].reset();
    }
    fAttrDeclIndex += 1;
    return fAttrDecl[i][j];
  }
  
  public final XSAttributeUseImpl getAttributeUse()
  {
    int i = fAttributeUseIndex >> 8;
    int j = fAttributeUseIndex & 0xFF;
    ensureAttributeUseCapacity(i);
    if (fAttributeUse[i][j] == null) {
      fAttributeUse[i][j] = new XSAttributeUseImpl();
    } else {
      fAttributeUse[i][j].reset();
    }
    fAttributeUseIndex += 1;
    return fAttributeUse[i][j];
  }
  
  public final XSComplexTypeDecl getComplexTypeDecl()
  {
    int i = fCTDeclIndex >> 8;
    int j = fCTDeclIndex & 0xFF;
    ensureCTDeclCapacity(i);
    if (fCTDecl[i][j] == null) {
      fCTDecl[i][j] = new XSComplexTypeDecl();
    } else {
      fCTDecl[i][j].reset();
    }
    fCTDeclIndex += 1;
    return fCTDecl[i][j];
  }
  
  public final XSSimpleTypeDecl getSimpleTypeDecl()
  {
    int i = fSTDeclIndex >> 8;
    int j = fSTDeclIndex & 0xFF;
    ensureSTDeclCapacity(i);
    if (fSTDecl[i][j] == null) {
      fSTDecl[i][j] = dvFactory.newXSSimpleTypeDecl();
    } else {
      fSTDecl[i][j].reset();
    }
    fSTDeclIndex += 1;
    return fSTDecl[i][j];
  }
  
  public final XSParticleDecl getParticleDecl()
  {
    int i = fParticleDeclIndex >> 8;
    int j = fParticleDeclIndex & 0xFF;
    ensureParticleDeclCapacity(i);
    if (fParticleDecl[i][j] == null) {
      fParticleDecl[i][j] = new XSParticleDecl();
    } else {
      fParticleDecl[i][j].reset();
    }
    fParticleDeclIndex += 1;
    return fParticleDecl[i][j];
  }
  
  public final XSModelGroupImpl getModelGroup()
  {
    int i = fModelGroupIndex >> 8;
    int j = fModelGroupIndex & 0xFF;
    ensureModelGroupCapacity(i);
    if (fModelGroup[i][j] == null) {
      fModelGroup[i][j] = new XSModelGroupImpl();
    } else {
      fModelGroup[i][j].reset();
    }
    fModelGroupIndex += 1;
    return fModelGroup[i][j];
  }
  
  private boolean ensureElementDeclCapacity(int paramInt)
  {
    if (paramInt >= fElementDecl.length) {
      fElementDecl = resize(fElementDecl, fElementDecl.length * 2);
    } else if (fElementDecl[paramInt] != null) {
      return false;
    }
    fElementDecl[paramInt] = new XSElementDecl['Ā'];
    return true;
  }
  
  private static XSElementDecl[][] resize(XSElementDecl[][] paramArrayOfXSElementDecl, int paramInt)
  {
    XSElementDecl[][] arrayOfXSElementDecl; = new XSElementDecl[paramInt][];
    System.arraycopy(paramArrayOfXSElementDecl, 0, arrayOfXSElementDecl;, 0, paramArrayOfXSElementDecl.length);
    return arrayOfXSElementDecl;;
  }
  
  private boolean ensureParticleDeclCapacity(int paramInt)
  {
    if (paramInt >= fParticleDecl.length) {
      fParticleDecl = resize(fParticleDecl, fParticleDecl.length * 2);
    } else if (fParticleDecl[paramInt] != null) {
      return false;
    }
    fParticleDecl[paramInt] = new XSParticleDecl['Ā'];
    return true;
  }
  
  private boolean ensureModelGroupCapacity(int paramInt)
  {
    if (paramInt >= fModelGroup.length) {
      fModelGroup = resize(fModelGroup, fModelGroup.length * 2);
    } else if (fModelGroup[paramInt] != null) {
      return false;
    }
    fModelGroup[paramInt] = new XSModelGroupImpl['Ā'];
    return true;
  }
  
  private static XSParticleDecl[][] resize(XSParticleDecl[][] paramArrayOfXSParticleDecl, int paramInt)
  {
    XSParticleDecl[][] arrayOfXSParticleDecl; = new XSParticleDecl[paramInt][];
    System.arraycopy(paramArrayOfXSParticleDecl, 0, arrayOfXSParticleDecl;, 0, paramArrayOfXSParticleDecl.length);
    return arrayOfXSParticleDecl;;
  }
  
  private static XSModelGroupImpl[][] resize(XSModelGroupImpl[][] paramArrayOfXSModelGroupImpl, int paramInt)
  {
    XSModelGroupImpl[][] arrayOfXSModelGroupImpl; = new XSModelGroupImpl[paramInt][];
    System.arraycopy(paramArrayOfXSModelGroupImpl, 0, arrayOfXSModelGroupImpl;, 0, paramArrayOfXSModelGroupImpl.length);
    return arrayOfXSModelGroupImpl;;
  }
  
  private boolean ensureAttrDeclCapacity(int paramInt)
  {
    if (paramInt >= fAttrDecl.length) {
      fAttrDecl = resize(fAttrDecl, fAttrDecl.length * 2);
    } else if (fAttrDecl[paramInt] != null) {
      return false;
    }
    fAttrDecl[paramInt] = new XSAttributeDecl['Ā'];
    return true;
  }
  
  private static XSAttributeDecl[][] resize(XSAttributeDecl[][] paramArrayOfXSAttributeDecl, int paramInt)
  {
    XSAttributeDecl[][] arrayOfXSAttributeDecl; = new XSAttributeDecl[paramInt][];
    System.arraycopy(paramArrayOfXSAttributeDecl, 0, arrayOfXSAttributeDecl;, 0, paramArrayOfXSAttributeDecl.length);
    return arrayOfXSAttributeDecl;;
  }
  
  private boolean ensureAttributeUseCapacity(int paramInt)
  {
    if (paramInt >= fAttributeUse.length) {
      fAttributeUse = resize(fAttributeUse, fAttributeUse.length * 2);
    } else if (fAttributeUse[paramInt] != null) {
      return false;
    }
    fAttributeUse[paramInt] = new XSAttributeUseImpl['Ā'];
    return true;
  }
  
  private static XSAttributeUseImpl[][] resize(XSAttributeUseImpl[][] paramArrayOfXSAttributeUseImpl, int paramInt)
  {
    XSAttributeUseImpl[][] arrayOfXSAttributeUseImpl; = new XSAttributeUseImpl[paramInt][];
    System.arraycopy(paramArrayOfXSAttributeUseImpl, 0, arrayOfXSAttributeUseImpl;, 0, paramArrayOfXSAttributeUseImpl.length);
    return arrayOfXSAttributeUseImpl;;
  }
  
  private boolean ensureSTDeclCapacity(int paramInt)
  {
    if (paramInt >= fSTDecl.length) {
      fSTDecl = resize(fSTDecl, fSTDecl.length * 2);
    } else if (fSTDecl[paramInt] != null) {
      return false;
    }
    fSTDecl[paramInt] = new XSSimpleTypeDecl['Ā'];
    return true;
  }
  
  private static XSSimpleTypeDecl[][] resize(XSSimpleTypeDecl[][] paramArrayOfXSSimpleTypeDecl, int paramInt)
  {
    XSSimpleTypeDecl[][] arrayOfXSSimpleTypeDecl; = new XSSimpleTypeDecl[paramInt][];
    System.arraycopy(paramArrayOfXSSimpleTypeDecl, 0, arrayOfXSSimpleTypeDecl;, 0, paramArrayOfXSSimpleTypeDecl.length);
    return arrayOfXSSimpleTypeDecl;;
  }
  
  private boolean ensureCTDeclCapacity(int paramInt)
  {
    if (paramInt >= fCTDecl.length) {
      fCTDecl = resize(fCTDecl, fCTDecl.length * 2);
    } else if (fCTDecl[paramInt] != null) {
      return false;
    }
    fCTDecl[paramInt] = new XSComplexTypeDecl['Ā'];
    return true;
  }
  
  private static XSComplexTypeDecl[][] resize(XSComplexTypeDecl[][] paramArrayOfXSComplexTypeDecl, int paramInt)
  {
    XSComplexTypeDecl[][] arrayOfXSComplexTypeDecl; = new XSComplexTypeDecl[paramInt][];
    System.arraycopy(paramArrayOfXSComplexTypeDecl, 0, arrayOfXSComplexTypeDecl;, 0, paramArrayOfXSComplexTypeDecl.length);
    return arrayOfXSComplexTypeDecl;;
  }
  
  public void reset()
  {
    fElementDeclIndex = 0;
    fParticleDeclIndex = 0;
    fModelGroupIndex = 0;
    fSTDeclIndex = 0;
    fCTDeclIndex = 0;
    fAttrDeclIndex = 0;
    fAttributeUseIndex = 0;
  }
}
