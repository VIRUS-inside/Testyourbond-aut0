package org.apache.xerces.impl.dtd;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Hashtable;
import org.apache.xerces.impl.dtd.models.CMAny;
import org.apache.xerces.impl.dtd.models.CMBinOp;
import org.apache.xerces.impl.dtd.models.CMLeaf;
import org.apache.xerces.impl.dtd.models.CMNode;
import org.apache.xerces.impl.dtd.models.CMUniOp;
import org.apache.xerces.impl.dtd.models.ContentModelValidator;
import org.apache.xerces.impl.dtd.models.DFAContentModel;
import org.apache.xerces.impl.dtd.models.MixedContentModel;
import org.apache.xerces.impl.dtd.models.SimpleContentModel;
import org.apache.xerces.impl.dv.DatatypeValidator;
import org.apache.xerces.impl.validation.EntityState;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLDTDContentModelHandler;
import org.apache.xerces.xni.XMLDTDHandler;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.parser.XMLDTDContentModelSource;
import org.apache.xerces.xni.parser.XMLDTDSource;

public class DTDGrammar
  implements XMLDTDHandler, XMLDTDContentModelHandler, EntityState, Grammar
{
  public static final int TOP_LEVEL_SCOPE = -1;
  private static final int CHUNK_SHIFT = 8;
  private static final int CHUNK_SIZE = 256;
  private static final int CHUNK_MASK = 255;
  private static final int INITIAL_CHUNK_COUNT = 4;
  private static final short LIST_FLAG = 128;
  private static final short LIST_MASK = -129;
  private static final boolean DEBUG = false;
  protected XMLDTDSource fDTDSource = null;
  protected XMLDTDContentModelSource fDTDContentModelSource = null;
  protected int fCurrentElementIndex;
  protected int fCurrentAttributeIndex;
  protected boolean fReadingExternalDTD = false;
  private SymbolTable fSymbolTable;
  protected XMLDTDDescription fGrammarDescription = null;
  private int fElementDeclCount = 0;
  private QName[][] fElementDeclName = new QName[4][];
  private short[][] fElementDeclType = new short[4][];
  private int[][] fElementDeclContentSpecIndex = new int[4][];
  private ContentModelValidator[][] fElementDeclContentModelValidator = new ContentModelValidator[4][];
  private int[][] fElementDeclFirstAttributeDeclIndex = new int[4][];
  private int[][] fElementDeclLastAttributeDeclIndex = new int[4][];
  private int fAttributeDeclCount = 0;
  private QName[][] fAttributeDeclName = new QName[4][];
  private boolean fIsImmutable = false;
  private short[][] fAttributeDeclType = new short[4][];
  private String[][][] fAttributeDeclEnumeration = new String[4][][];
  private short[][] fAttributeDeclDefaultType = new short[4][];
  private DatatypeValidator[][] fAttributeDeclDatatypeValidator = new DatatypeValidator[4][];
  private String[][] fAttributeDeclDefaultValue = new String[4][];
  private String[][] fAttributeDeclNonNormalizedDefaultValue = new String[4][];
  private int[][] fAttributeDeclNextAttributeDeclIndex = new int[4][];
  private int fContentSpecCount = 0;
  private short[][] fContentSpecType = new short[4][];
  private Object[][] fContentSpecValue = new Object[4][];
  private Object[][] fContentSpecOtherValue = new Object[4][];
  private int fEntityCount = 0;
  private String[][] fEntityName = new String[4][];
  private String[][] fEntityValue = new String[4][];
  private String[][] fEntityPublicId = new String[4][];
  private String[][] fEntitySystemId = new String[4][];
  private String[][] fEntityBaseSystemId = new String[4][];
  private String[][] fEntityNotation = new String[4][];
  private byte[][] fEntityIsPE = new byte[4][];
  private byte[][] fEntityInExternal = new byte[4][];
  private int fNotationCount = 0;
  private String[][] fNotationName = new String[4][];
  private String[][] fNotationPublicId = new String[4][];
  private String[][] fNotationSystemId = new String[4][];
  private String[][] fNotationBaseSystemId = new String[4][];
  private QNameHashtable fElementIndexMap = new QNameHashtable();
  private QNameHashtable fEntityIndexMap = new QNameHashtable();
  private QNameHashtable fNotationIndexMap = new QNameHashtable();
  private boolean fMixed;
  private final QName fQName = new QName();
  private final QName fQName2 = new QName();
  protected final XMLAttributeDecl fAttributeDecl = new XMLAttributeDecl();
  private int fLeafCount = 0;
  private int fEpsilonIndex = -1;
  private XMLElementDecl fElementDecl = new XMLElementDecl();
  private XMLEntityDecl fEntityDecl = new XMLEntityDecl();
  private XMLSimpleType fSimpleType = new XMLSimpleType();
  private XMLContentSpec fContentSpec = new XMLContentSpec();
  Hashtable fElementDeclTab = new Hashtable();
  private short[] fOpStack = null;
  private int[] fNodeIndexStack = null;
  private int[] fPrevNodeIndexStack = null;
  private int fDepth = 0;
  private boolean[] fPEntityStack = new boolean[4];
  private int fPEDepth = 0;
  private int[][] fElementDeclIsExternal = new int[4][];
  private int[][] fAttributeDeclIsExternal = new int[4][];
  int valueIndex = -1;
  int prevNodeIndex = -1;
  int nodeIndex = -1;
  
  public DTDGrammar(SymbolTable paramSymbolTable, XMLDTDDescription paramXMLDTDDescription)
  {
    fSymbolTable = paramSymbolTable;
    fGrammarDescription = paramXMLDTDDescription;
  }
  
  public XMLGrammarDescription getGrammarDescription()
  {
    return fGrammarDescription;
  }
  
  public boolean getElementDeclIsExternal(int paramInt)
  {
    if (paramInt < 0) {
      return false;
    }
    int i = paramInt >> 8;
    int j = paramInt & 0xFF;
    return fElementDeclIsExternal[i][j] != 0;
  }
  
  public boolean getAttributeDeclIsExternal(int paramInt)
  {
    if (paramInt < 0) {
      return false;
    }
    int i = paramInt >> 8;
    int j = paramInt & 0xFF;
    return fAttributeDeclIsExternal[i][j] != 0;
  }
  
  public int getAttributeDeclIndex(int paramInt, String paramString)
  {
    if (paramInt == -1) {
      return -1;
    }
    for (int i = getFirstAttributeDeclIndex(paramInt); i != -1; i = getNextAttributeDeclIndex(i))
    {
      getAttributeDecl(i, fAttributeDecl);
      if ((fAttributeDecl.name.rawname == paramString) || (paramString.equals(fAttributeDecl.name.rawname))) {
        return i;
      }
    }
    return -1;
  }
  
  public void startDTD(XMLLocator paramXMLLocator, Augmentations paramAugmentations)
    throws XNIException
  {
    fOpStack = null;
    fNodeIndexStack = null;
    fPrevNodeIndexStack = null;
  }
  
  public void startParameterEntity(String paramString1, XMLResourceIdentifier paramXMLResourceIdentifier, String paramString2, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fPEDepth == fPEntityStack.length)
    {
      boolean[] arrayOfBoolean = new boolean[fPEntityStack.length * 2];
      System.arraycopy(fPEntityStack, 0, arrayOfBoolean, 0, fPEntityStack.length);
      fPEntityStack = arrayOfBoolean;
    }
    fPEntityStack[fPEDepth] = fReadingExternalDTD;
    fPEDepth += 1;
  }
  
  public void startExternalSubset(XMLResourceIdentifier paramXMLResourceIdentifier, Augmentations paramAugmentations)
    throws XNIException
  {
    fReadingExternalDTD = true;
  }
  
  public void endParameterEntity(String paramString, Augmentations paramAugmentations)
    throws XNIException
  {
    fPEDepth -= 1;
    fReadingExternalDTD = fPEntityStack[fPEDepth];
  }
  
  public void endExternalSubset(Augmentations paramAugmentations)
    throws XNIException
  {
    fReadingExternalDTD = false;
  }
  
  public void elementDecl(String paramString1, String paramString2, Augmentations paramAugmentations)
    throws XNIException
  {
    XMLElementDecl localXMLElementDecl1 = (XMLElementDecl)fElementDeclTab.get(paramString1);
    if (localXMLElementDecl1 != null)
    {
      if (type == -1) {
        fCurrentElementIndex = getElementDeclIndex(paramString1);
      }
    }
    else {
      fCurrentElementIndex = createElementDecl();
    }
    XMLElementDecl localXMLElementDecl2 = new XMLElementDecl();
    fQName.setValues(null, paramString1, paramString1, null);
    name.setValues(fQName);
    contentModelValidator = null;
    scope = -1;
    if (paramString2.equals("EMPTY")) {
      type = 1;
    } else if (paramString2.equals("ANY")) {
      type = 0;
    } else if (paramString2.startsWith("(")) {
      if (paramString2.indexOf("#PCDATA") > 0) {
        type = 2;
      } else {
        type = 3;
      }
    }
    fElementDeclTab.put(paramString1, localXMLElementDecl2);
    fElementDecl = localXMLElementDecl2;
    addContentSpecToElement(localXMLElementDecl2);
    setElementDecl(fCurrentElementIndex, fElementDecl);
    int i = fCurrentElementIndex >> 8;
    int j = fCurrentElementIndex & 0xFF;
    ensureElementDeclCapacity(i);
    fElementDeclIsExternal[i][j] = ((fReadingExternalDTD) || (fPEDepth > 0) ? 1 : 0);
  }
  
  public void attributeDecl(String paramString1, String paramString2, String paramString3, String[] paramArrayOfString, String paramString4, XMLString paramXMLString1, XMLString paramXMLString2, Augmentations paramAugmentations)
    throws XNIException
  {
    if (!fElementDeclTab.containsKey(paramString1))
    {
      fCurrentElementIndex = createElementDecl();
      XMLElementDecl localXMLElementDecl = new XMLElementDecl();
      name.setValues(null, paramString1, paramString1, null);
      scope = -1;
      fElementDeclTab.put(paramString1, localXMLElementDecl);
      setElementDecl(fCurrentElementIndex, localXMLElementDecl);
    }
    int i = getElementDeclIndex(paramString1);
    if (getAttributeDeclIndex(i, paramString2) != -1) {
      return;
    }
    fCurrentAttributeIndex = createAttributeDecl();
    fSimpleType.clear();
    if (paramString4 != null) {
      if (paramString4.equals("#FIXED")) {
        fSimpleType.defaultType = 1;
      } else if (paramString4.equals("#IMPLIED")) {
        fSimpleType.defaultType = 0;
      } else if (paramString4.equals("#REQUIRED")) {
        fSimpleType.defaultType = 2;
      }
    }
    fSimpleType.defaultValue = (paramXMLString1 != null ? paramXMLString1.toString() : null);
    fSimpleType.nonNormalizedDefaultValue = (paramXMLString2 != null ? paramXMLString2.toString() : null);
    fSimpleType.enumeration = paramArrayOfString;
    if (paramString3.equals("CDATA"))
    {
      fSimpleType.type = 0;
    }
    else if (paramString3.equals("ID"))
    {
      fSimpleType.type = 3;
    }
    else if (paramString3.startsWith("IDREF"))
    {
      fSimpleType.type = 4;
      if (paramString3.indexOf("S") > 0) {
        fSimpleType.list = true;
      }
    }
    else if (paramString3.equals("ENTITIES"))
    {
      fSimpleType.type = 1;
      fSimpleType.list = true;
    }
    else if (paramString3.equals("ENTITY"))
    {
      fSimpleType.type = 1;
    }
    else if (paramString3.equals("NMTOKENS"))
    {
      fSimpleType.type = 5;
      fSimpleType.list = true;
    }
    else if (paramString3.equals("NMTOKEN"))
    {
      fSimpleType.type = 5;
    }
    else if (paramString3.startsWith("NOTATION"))
    {
      fSimpleType.type = 6;
    }
    else if (paramString3.startsWith("ENUMERATION"))
    {
      fSimpleType.type = 2;
    }
    else
    {
      System.err.println("!!! unknown attribute type " + paramString3);
    }
    fQName.setValues(null, paramString2, paramString2, null);
    fAttributeDecl.setValues(fQName, fSimpleType, false);
    setAttributeDecl(i, fCurrentAttributeIndex, fAttributeDecl);
    int j = fCurrentAttributeIndex >> 8;
    int k = fCurrentAttributeIndex & 0xFF;
    ensureAttributeDeclCapacity(j);
    fAttributeDeclIsExternal[j][k] = ((fReadingExternalDTD) || (fPEDepth > 0) ? 1 : 0);
  }
  
  public void internalEntityDecl(String paramString, XMLString paramXMLString1, XMLString paramXMLString2, Augmentations paramAugmentations)
    throws XNIException
  {
    int i = getEntityDeclIndex(paramString);
    if (i == -1)
    {
      i = createEntityDecl();
      boolean bool1 = paramString.startsWith("%");
      boolean bool2 = (fReadingExternalDTD) || (fPEDepth > 0);
      XMLEntityDecl localXMLEntityDecl = new XMLEntityDecl();
      localXMLEntityDecl.setValues(paramString, null, null, null, null, paramXMLString1.toString(), bool1, bool2);
      setEntityDecl(i, localXMLEntityDecl);
    }
  }
  
  public void externalEntityDecl(String paramString, XMLResourceIdentifier paramXMLResourceIdentifier, Augmentations paramAugmentations)
    throws XNIException
  {
    int i = getEntityDeclIndex(paramString);
    if (i == -1)
    {
      i = createEntityDecl();
      boolean bool1 = paramString.startsWith("%");
      boolean bool2 = (fReadingExternalDTD) || (fPEDepth > 0);
      XMLEntityDecl localXMLEntityDecl = new XMLEntityDecl();
      localXMLEntityDecl.setValues(paramString, paramXMLResourceIdentifier.getPublicId(), paramXMLResourceIdentifier.getLiteralSystemId(), paramXMLResourceIdentifier.getBaseSystemId(), null, null, bool1, bool2);
      setEntityDecl(i, localXMLEntityDecl);
    }
  }
  
  public void unparsedEntityDecl(String paramString1, XMLResourceIdentifier paramXMLResourceIdentifier, String paramString2, Augmentations paramAugmentations)
    throws XNIException
  {
    XMLEntityDecl localXMLEntityDecl = new XMLEntityDecl();
    boolean bool1 = paramString1.startsWith("%");
    boolean bool2 = (fReadingExternalDTD) || (fPEDepth > 0);
    localXMLEntityDecl.setValues(paramString1, paramXMLResourceIdentifier.getPublicId(), paramXMLResourceIdentifier.getLiteralSystemId(), paramXMLResourceIdentifier.getBaseSystemId(), paramString2, null, bool1, bool2);
    int i = getEntityDeclIndex(paramString1);
    if (i == -1)
    {
      i = createEntityDecl();
      setEntityDecl(i, localXMLEntityDecl);
    }
  }
  
  public void notationDecl(String paramString, XMLResourceIdentifier paramXMLResourceIdentifier, Augmentations paramAugmentations)
    throws XNIException
  {
    XMLNotationDecl localXMLNotationDecl = new XMLNotationDecl();
    localXMLNotationDecl.setValues(paramString, paramXMLResourceIdentifier.getPublicId(), paramXMLResourceIdentifier.getLiteralSystemId(), paramXMLResourceIdentifier.getBaseSystemId());
    int i = getNotationDeclIndex(paramString);
    if (i == -1)
    {
      i = createNotationDecl();
      setNotationDecl(i, localXMLNotationDecl);
    }
  }
  
  public void endDTD(Augmentations paramAugmentations)
    throws XNIException
  {
    fIsImmutable = true;
    if (fGrammarDescription.getRootName() == null)
    {
      int j = 0;
      String str = null;
      int k = fElementDeclCount;
      ArrayList localArrayList = new ArrayList(k);
      for (int m = 0; m < k; m++)
      {
        int i = m >> 8;
        j = m & 0xFF;
        str = fElementDeclName[i][j].rawname;
        localArrayList.add(str);
      }
      fGrammarDescription.setPossibleRoots(localArrayList);
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
  
  public void textDecl(String paramString1, String paramString2, Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void comment(XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void processingInstruction(String paramString, XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void startAttlist(String paramString, Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void endAttlist(Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void startConditional(short paramShort, Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void ignoredCharacters(XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void endConditional(Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void setDTDContentModelSource(XMLDTDContentModelSource paramXMLDTDContentModelSource)
  {
    fDTDContentModelSource = paramXMLDTDContentModelSource;
  }
  
  public XMLDTDContentModelSource getDTDContentModelSource()
  {
    return fDTDContentModelSource;
  }
  
  public void startContentModel(String paramString, Augmentations paramAugmentations)
    throws XNIException
  {
    XMLElementDecl localXMLElementDecl = (XMLElementDecl)fElementDeclTab.get(paramString);
    if (localXMLElementDecl != null) {
      fElementDecl = localXMLElementDecl;
    }
    fDepth = 0;
    initializeContentModelStack();
  }
  
  public void startGroup(Augmentations paramAugmentations)
    throws XNIException
  {
    fDepth += 1;
    initializeContentModelStack();
    fMixed = false;
  }
  
  public void pcdata(Augmentations paramAugmentations)
    throws XNIException
  {
    fMixed = true;
  }
  
  public void element(String paramString, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fMixed)
    {
      if (fNodeIndexStack[fDepth] == -1) {
        fNodeIndexStack[fDepth] = addUniqueLeafNode(paramString);
      } else {
        fNodeIndexStack[fDepth] = addContentSpecNode(4, fNodeIndexStack[fDepth], addUniqueLeafNode(paramString));
      }
    }
    else {
      fNodeIndexStack[fDepth] = addContentSpecNode(0, paramString);
    }
  }
  
  public void separator(short paramShort, Augmentations paramAugmentations)
    throws XNIException
  {
    if (!fMixed) {
      if ((fOpStack[fDepth] != 5) && (paramShort == 0))
      {
        if (fPrevNodeIndexStack[fDepth] != -1) {
          fNodeIndexStack[fDepth] = addContentSpecNode(fOpStack[fDepth], fPrevNodeIndexStack[fDepth], fNodeIndexStack[fDepth]);
        }
        fPrevNodeIndexStack[fDepth] = fNodeIndexStack[fDepth];
        fOpStack[fDepth] = 4;
      }
      else if ((fOpStack[fDepth] != 4) && (paramShort == 1))
      {
        if (fPrevNodeIndexStack[fDepth] != -1) {
          fNodeIndexStack[fDepth] = addContentSpecNode(fOpStack[fDepth], fPrevNodeIndexStack[fDepth], fNodeIndexStack[fDepth]);
        }
        fPrevNodeIndexStack[fDepth] = fNodeIndexStack[fDepth];
        fOpStack[fDepth] = 5;
      }
    }
  }
  
  public void occurrence(short paramShort, Augmentations paramAugmentations)
    throws XNIException
  {
    if (!fMixed) {
      if (paramShort == 2) {
        fNodeIndexStack[fDepth] = addContentSpecNode(1, fNodeIndexStack[fDepth], -1);
      } else if (paramShort == 3) {
        fNodeIndexStack[fDepth] = addContentSpecNode(2, fNodeIndexStack[fDepth], -1);
      } else if (paramShort == 4) {
        fNodeIndexStack[fDepth] = addContentSpecNode(3, fNodeIndexStack[fDepth], -1);
      }
    }
  }
  
  public void endGroup(Augmentations paramAugmentations)
    throws XNIException
  {
    if (!fMixed)
    {
      if (fPrevNodeIndexStack[fDepth] != -1) {
        fNodeIndexStack[fDepth] = addContentSpecNode(fOpStack[fDepth], fPrevNodeIndexStack[fDepth], fNodeIndexStack[fDepth]);
      }
      int i = fNodeIndexStack[(fDepth--)];
      fNodeIndexStack[fDepth] = i;
    }
  }
  
  public void any(Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void empty(Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void endContentModel(Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public boolean isNamespaceAware()
  {
    return false;
  }
  
  public SymbolTable getSymbolTable()
  {
    return fSymbolTable;
  }
  
  public int getFirstElementDeclIndex()
  {
    return fElementDeclCount >= 0 ? 0 : -1;
  }
  
  public int getNextElementDeclIndex(int paramInt)
  {
    return paramInt < fElementDeclCount - 1 ? paramInt + 1 : -1;
  }
  
  public int getElementDeclIndex(String paramString)
  {
    int i = fElementIndexMap.get(paramString);
    return i;
  }
  
  public int getElementDeclIndex(QName paramQName)
  {
    return getElementDeclIndex(rawname);
  }
  
  public short getContentSpecType(int paramInt)
  {
    if ((paramInt < 0) || (paramInt >= fElementDeclCount)) {
      return -1;
    }
    int i = paramInt >> 8;
    int j = paramInt & 0xFF;
    if (fElementDeclType[i][j] == -1) {
      return -1;
    }
    return (short)(fElementDeclType[i][j] & 0xFF7F);
  }
  
  public boolean getElementDecl(int paramInt, XMLElementDecl paramXMLElementDecl)
  {
    if ((paramInt < 0) || (paramInt >= fElementDeclCount)) {
      return false;
    }
    int i = paramInt >> 8;
    int j = paramInt & 0xFF;
    name.setValues(fElementDeclName[i][j]);
    if (fElementDeclType[i][j] == -1)
    {
      type = -1;
      simpleType.list = false;
    }
    else
    {
      type = ((short)(fElementDeclType[i][j] & 0xFF7F));
      simpleType.list = ((fElementDeclType[i][j] & 0x80) != 0);
    }
    if ((type == 3) || (type == 2)) {
      contentModelValidator = getElementContentModelValidator(paramInt);
    }
    simpleType.datatypeValidator = null;
    simpleType.defaultType = -1;
    simpleType.defaultValue = null;
    return true;
  }
  
  QName getElementDeclName(int paramInt)
  {
    if ((paramInt < 0) || (paramInt >= fElementDeclCount)) {
      return null;
    }
    int i = paramInt >> 8;
    int j = paramInt & 0xFF;
    return fElementDeclName[i][j];
  }
  
  public int getFirstAttributeDeclIndex(int paramInt)
  {
    int i = paramInt >> 8;
    int j = paramInt & 0xFF;
    return fElementDeclFirstAttributeDeclIndex[i][j];
  }
  
  public int getNextAttributeDeclIndex(int paramInt)
  {
    int i = paramInt >> 8;
    int j = paramInt & 0xFF;
    return fAttributeDeclNextAttributeDeclIndex[i][j];
  }
  
  public boolean getAttributeDecl(int paramInt, XMLAttributeDecl paramXMLAttributeDecl)
  {
    if ((paramInt < 0) || (paramInt >= fAttributeDeclCount)) {
      return false;
    }
    int i = paramInt >> 8;
    int j = paramInt & 0xFF;
    name.setValues(fAttributeDeclName[i][j]);
    short s;
    boolean bool;
    if (fAttributeDeclType[i][j] == -1)
    {
      s = -1;
      bool = false;
    }
    else
    {
      s = (short)(fAttributeDeclType[i][j] & 0xFF7F);
      bool = (fAttributeDeclType[i][j] & 0x80) != 0;
    }
    simpleType.setValues(s, fAttributeDeclName[i][j].localpart, fAttributeDeclEnumeration[i][j], bool, fAttributeDeclDefaultType[i][j], fAttributeDeclDefaultValue[i][j], fAttributeDeclNonNormalizedDefaultValue[i][j], fAttributeDeclDatatypeValidator[i][j]);
    return true;
  }
  
  public boolean isCDATAAttribute(QName paramQName1, QName paramQName2)
  {
    int i = getElementDeclIndex(paramQName1);
    return (!getAttributeDecl(i, fAttributeDecl)) || (fAttributeDecl.simpleType.type == 0);
  }
  
  public int getEntityDeclIndex(String paramString)
  {
    if (paramString == null) {
      return -1;
    }
    return fEntityIndexMap.get(paramString);
  }
  
  public boolean getEntityDecl(int paramInt, XMLEntityDecl paramXMLEntityDecl)
  {
    if ((paramInt < 0) || (paramInt >= fEntityCount)) {
      return false;
    }
    int i = paramInt >> 8;
    int j = paramInt & 0xFF;
    paramXMLEntityDecl.setValues(fEntityName[i][j], fEntityPublicId[i][j], fEntitySystemId[i][j], fEntityBaseSystemId[i][j], fEntityNotation[i][j], fEntityValue[i][j], fEntityIsPE[i][j] != 0, fEntityInExternal[i][j] != 0);
    return true;
  }
  
  public int getNotationDeclIndex(String paramString)
  {
    if (paramString == null) {
      return -1;
    }
    return fNotationIndexMap.get(paramString);
  }
  
  public boolean getNotationDecl(int paramInt, XMLNotationDecl paramXMLNotationDecl)
  {
    if ((paramInt < 0) || (paramInt >= fNotationCount)) {
      return false;
    }
    int i = paramInt >> 8;
    int j = paramInt & 0xFF;
    paramXMLNotationDecl.setValues(fNotationName[i][j], fNotationPublicId[i][j], fNotationSystemId[i][j], fNotationBaseSystemId[i][j]);
    return true;
  }
  
  public boolean getContentSpec(int paramInt, XMLContentSpec paramXMLContentSpec)
  {
    if ((paramInt < 0) || (paramInt >= fContentSpecCount)) {
      return false;
    }
    int i = paramInt >> 8;
    int j = paramInt & 0xFF;
    type = fContentSpecType[i][j];
    value = fContentSpecValue[i][j];
    otherValue = fContentSpecOtherValue[i][j];
    return true;
  }
  
  public int getContentSpecIndex(int paramInt)
  {
    if ((paramInt < 0) || (paramInt >= fElementDeclCount)) {
      return -1;
    }
    int i = paramInt >> 8;
    int j = paramInt & 0xFF;
    return fElementDeclContentSpecIndex[i][j];
  }
  
  public String getContentSpecAsString(int paramInt)
  {
    if ((paramInt < 0) || (paramInt >= fElementDeclCount)) {
      return null;
    }
    int i = paramInt >> 8;
    int j = paramInt & 0xFF;
    int k = fElementDeclContentSpecIndex[i][j];
    XMLContentSpec localXMLContentSpec = new XMLContentSpec();
    if (getContentSpec(k, localXMLContentSpec))
    {
      StringBuffer localStringBuffer = new StringBuffer();
      int m = type & 0xF;
      int n;
      switch (m)
      {
      case 0: 
        localStringBuffer.append('(');
        if ((value == null) && (otherValue == null)) {
          localStringBuffer.append("#PCDATA");
        } else {
          localStringBuffer.append(value);
        }
        localStringBuffer.append(')');
        break;
      case 1: 
        getContentSpec(((int[])value)[0], localXMLContentSpec);
        n = type;
        if (n == 0)
        {
          localStringBuffer.append('(');
          localStringBuffer.append(value);
          localStringBuffer.append(')');
        }
        else if ((n == 3) || (n == 2) || (n == 1))
        {
          localStringBuffer.append('(');
          appendContentSpec(localXMLContentSpec, localStringBuffer, true, m);
          localStringBuffer.append(')');
        }
        else
        {
          appendContentSpec(localXMLContentSpec, localStringBuffer, true, m);
        }
        localStringBuffer.append('?');
        break;
      case 2: 
        getContentSpec(((int[])value)[0], localXMLContentSpec);
        n = type;
        if (n == 0)
        {
          localStringBuffer.append('(');
          if ((value == null) && (otherValue == null)) {
            localStringBuffer.append("#PCDATA");
          } else if (otherValue != null) {
            localStringBuffer.append("##any:uri=").append(otherValue);
          } else if (value == null) {
            localStringBuffer.append("##any");
          } else {
            appendContentSpec(localXMLContentSpec, localStringBuffer, true, m);
          }
          localStringBuffer.append(')');
        }
        else if ((n == 3) || (n == 2) || (n == 1))
        {
          localStringBuffer.append('(');
          appendContentSpec(localXMLContentSpec, localStringBuffer, true, m);
          localStringBuffer.append(')');
        }
        else
        {
          appendContentSpec(localXMLContentSpec, localStringBuffer, true, m);
        }
        localStringBuffer.append('*');
        break;
      case 3: 
        getContentSpec(((int[])value)[0], localXMLContentSpec);
        n = type;
        if (n == 0)
        {
          localStringBuffer.append('(');
          if ((value == null) && (otherValue == null)) {
            localStringBuffer.append("#PCDATA");
          } else if (otherValue != null) {
            localStringBuffer.append("##any:uri=").append(otherValue);
          } else if (value == null) {
            localStringBuffer.append("##any");
          } else {
            localStringBuffer.append(value);
          }
          localStringBuffer.append(')');
        }
        else if ((n == 3) || (n == 2) || (n == 1))
        {
          localStringBuffer.append('(');
          appendContentSpec(localXMLContentSpec, localStringBuffer, true, m);
          localStringBuffer.append(')');
        }
        else
        {
          appendContentSpec(localXMLContentSpec, localStringBuffer, true, m);
        }
        localStringBuffer.append('+');
        break;
      case 4: 
      case 5: 
        appendContentSpec(localXMLContentSpec, localStringBuffer, true, m);
        break;
      case 6: 
        localStringBuffer.append("##any");
        if (otherValue != null)
        {
          localStringBuffer.append(":uri=");
          localStringBuffer.append(otherValue);
        }
        break;
      case 7: 
        localStringBuffer.append("##other:uri=");
        localStringBuffer.append(otherValue);
        break;
      case 8: 
        localStringBuffer.append("##local");
        break;
      default: 
        localStringBuffer.append("???");
      }
      return localStringBuffer.toString();
    }
    return null;
  }
  
  public void printElements()
  {
    int i = 0;
    XMLElementDecl localXMLElementDecl = new XMLElementDecl();
    while (getElementDecl(i++, localXMLElementDecl)) {
      System.out.println("element decl: " + name + ", " + name.rawname);
    }
  }
  
  public void printAttributes(int paramInt)
  {
    int i = getFirstAttributeDeclIndex(paramInt);
    System.out.print(paramInt);
    System.out.print(" [");
    while (i != -1)
    {
      System.out.print(' ');
      System.out.print(i);
      printAttribute(i);
      i = getNextAttributeDeclIndex(i);
      if (i != -1) {
        System.out.print(",");
      }
    }
    System.out.println(" ]");
  }
  
  protected void addContentSpecToElement(XMLElementDecl paramXMLElementDecl)
  {
    if (((fDepth == 0) || ((fDepth == 1) && (type == 2))) && (fNodeIndexStack != null))
    {
      if (type == 2)
      {
        int i = addUniqueLeafNode(null);
        if (fNodeIndexStack[0] == -1) {
          fNodeIndexStack[0] = i;
        } else {
          fNodeIndexStack[0] = addContentSpecNode(4, i, fNodeIndexStack[0]);
        }
      }
      setContentSpecIndex(fCurrentElementIndex, fNodeIndexStack[fDepth]);
    }
  }
  
  protected ContentModelValidator getElementContentModelValidator(int paramInt)
  {
    int i = paramInt >> 8;
    int j = paramInt & 0xFF;
    Object localObject = fElementDeclContentModelValidator[i][j];
    if (localObject != null) {
      return localObject;
    }
    int k = fElementDeclType[i][j];
    if (k == 4) {
      return null;
    }
    int m = fElementDeclContentSpecIndex[i][j];
    XMLContentSpec localXMLContentSpec = new XMLContentSpec();
    getContentSpec(m, localXMLContentSpec);
    if (k == 2)
    {
      ChildrenList localChildrenList = new ChildrenList();
      contentSpecTree(m, localXMLContentSpec, localChildrenList);
      localObject = new MixedContentModel(qname, type, 0, length, false);
    }
    else if (k == 3)
    {
      localObject = createChildModel(m);
    }
    else
    {
      throw new RuntimeException("Unknown content type for a element decl in getElementContentModelValidator() in AbstractDTDGrammar class");
    }
    fElementDeclContentModelValidator[i][j] = localObject;
    return localObject;
  }
  
  protected int createElementDecl()
  {
    int i = fElementDeclCount >> 8;
    int j = fElementDeclCount & 0xFF;
    ensureElementDeclCapacity(i);
    fElementDeclName[i][j] = new QName();
    fElementDeclType[i][j] = -1;
    fElementDeclContentModelValidator[i][j] = null;
    fElementDeclFirstAttributeDeclIndex[i][j] = -1;
    fElementDeclLastAttributeDeclIndex[i][j] = -1;
    return fElementDeclCount++;
  }
  
  protected void setElementDecl(int paramInt, XMLElementDecl paramXMLElementDecl)
  {
    if ((paramInt < 0) || (paramInt >= fElementDeclCount)) {
      return;
    }
    int i = paramInt >> 8;
    int j = paramInt & 0xFF;
    fElementDeclName[i][j].setValues(name);
    fElementDeclType[i][j] = type;
    fElementDeclContentModelValidator[i][j] = contentModelValidator;
    if (simpleType.list == true)
    {
      int tmp86_84 = j;
      short[] tmp86_83 = fElementDeclType[i];
      tmp86_83[tmp86_84] = ((short)(tmp86_83[tmp86_84] | 0x80));
    }
    fElementIndexMap.put(name.rawname, paramInt);
  }
  
  protected void putElementNameMapping(QName paramQName, int paramInt1, int paramInt2) {}
  
  protected void setFirstAttributeDeclIndex(int paramInt1, int paramInt2)
  {
    if ((paramInt1 < 0) || (paramInt1 >= fElementDeclCount)) {
      return;
    }
    int i = paramInt1 >> 8;
    int j = paramInt1 & 0xFF;
    fElementDeclFirstAttributeDeclIndex[i][j] = paramInt2;
  }
  
  protected void setContentSpecIndex(int paramInt1, int paramInt2)
  {
    if ((paramInt1 < 0) || (paramInt1 >= fElementDeclCount)) {
      return;
    }
    int i = paramInt1 >> 8;
    int j = paramInt1 & 0xFF;
    fElementDeclContentSpecIndex[i][j] = paramInt2;
  }
  
  protected int createAttributeDecl()
  {
    int i = fAttributeDeclCount >> 8;
    int j = fAttributeDeclCount & 0xFF;
    ensureAttributeDeclCapacity(i);
    fAttributeDeclName[i][j] = new QName();
    fAttributeDeclType[i][j] = -1;
    fAttributeDeclDatatypeValidator[i][j] = null;
    fAttributeDeclEnumeration[i][j] = null;
    fAttributeDeclDefaultType[i][j] = 0;
    fAttributeDeclDefaultValue[i][j] = null;
    fAttributeDeclNonNormalizedDefaultValue[i][j] = null;
    fAttributeDeclNextAttributeDeclIndex[i][j] = -1;
    return fAttributeDeclCount++;
  }
  
  protected void setAttributeDecl(int paramInt1, int paramInt2, XMLAttributeDecl paramXMLAttributeDecl)
  {
    int i = paramInt2 >> 8;
    int j = paramInt2 & 0xFF;
    fAttributeDeclName[i][j].setValues(name);
    fAttributeDeclType[i][j] = simpleType.type;
    if (simpleType.list)
    {
      int tmp66_64 = j;
      short[] tmp66_63 = fAttributeDeclType[i];
      tmp66_63[tmp66_64] = ((short)(tmp66_63[tmp66_64] | 0x80));
    }
    fAttributeDeclEnumeration[i][j] = simpleType.enumeration;
    fAttributeDeclDefaultType[i][j] = simpleType.defaultType;
    fAttributeDeclDatatypeValidator[i][j] = simpleType.datatypeValidator;
    fAttributeDeclDefaultValue[i][j] = simpleType.defaultValue;
    fAttributeDeclNonNormalizedDefaultValue[i][j] = simpleType.nonNormalizedDefaultValue;
    int k = paramInt1 >> 8;
    int m = paramInt1 & 0xFF;
    for (int n = fElementDeclFirstAttributeDeclIndex[k][m]; n != -1; n = fAttributeDeclNextAttributeDeclIndex[i][j])
    {
      if (n == paramInt2) {
        break;
      }
      i = n >> 8;
      j = n & 0xFF;
    }
    if (n == -1)
    {
      if (fElementDeclFirstAttributeDeclIndex[k][m] == -1)
      {
        fElementDeclFirstAttributeDeclIndex[k][m] = paramInt2;
      }
      else
      {
        n = fElementDeclLastAttributeDeclIndex[k][m];
        i = n >> 8;
        j = n & 0xFF;
        fAttributeDeclNextAttributeDeclIndex[i][j] = paramInt2;
      }
      fElementDeclLastAttributeDeclIndex[k][m] = paramInt2;
    }
  }
  
  protected int createContentSpec()
  {
    int i = fContentSpecCount >> 8;
    int j = fContentSpecCount & 0xFF;
    ensureContentSpecCapacity(i);
    fContentSpecType[i][j] = -1;
    fContentSpecValue[i][j] = null;
    fContentSpecOtherValue[i][j] = null;
    return fContentSpecCount++;
  }
  
  protected void setContentSpec(int paramInt, XMLContentSpec paramXMLContentSpec)
  {
    int i = paramInt >> 8;
    int j = paramInt & 0xFF;
    fContentSpecType[i][j] = type;
    fContentSpecValue[i][j] = value;
    fContentSpecOtherValue[i][j] = otherValue;
  }
  
  protected int createEntityDecl()
  {
    int i = fEntityCount >> 8;
    int j = fEntityCount & 0xFF;
    ensureEntityDeclCapacity(i);
    fEntityIsPE[i][j] = 0;
    fEntityInExternal[i][j] = 0;
    return fEntityCount++;
  }
  
  protected void setEntityDecl(int paramInt, XMLEntityDecl paramXMLEntityDecl)
  {
    int i = paramInt >> 8;
    int j = paramInt & 0xFF;
    fEntityName[i][j] = name;
    fEntityValue[i][j] = value;
    fEntityPublicId[i][j] = publicId;
    fEntitySystemId[i][j] = systemId;
    fEntityBaseSystemId[i][j] = baseSystemId;
    fEntityNotation[i][j] = notation;
    fEntityIsPE[i][j] = (isPE ? 1 : 0);
    fEntityInExternal[i][j] = (inExternal ? 1 : 0);
    fEntityIndexMap.put(name, paramInt);
  }
  
  protected int createNotationDecl()
  {
    int i = fNotationCount >> 8;
    ensureNotationDeclCapacity(i);
    return fNotationCount++;
  }
  
  protected void setNotationDecl(int paramInt, XMLNotationDecl paramXMLNotationDecl)
  {
    int i = paramInt >> 8;
    int j = paramInt & 0xFF;
    fNotationName[i][j] = name;
    fNotationPublicId[i][j] = publicId;
    fNotationSystemId[i][j] = systemId;
    fNotationBaseSystemId[i][j] = baseSystemId;
    fNotationIndexMap.put(name, paramInt);
  }
  
  protected int addContentSpecNode(short paramShort, String paramString)
  {
    int i = createContentSpec();
    fContentSpec.setValues(paramShort, paramString, null);
    setContentSpec(i, fContentSpec);
    return i;
  }
  
  protected int addUniqueLeafNode(String paramString)
  {
    int i = createContentSpec();
    fContentSpec.setValues((short)0, paramString, null);
    setContentSpec(i, fContentSpec);
    return i;
  }
  
  protected int addContentSpecNode(short paramShort, int paramInt1, int paramInt2)
  {
    int i = createContentSpec();
    int[] arrayOfInt1 = new int[1];
    int[] arrayOfInt2 = new int[1];
    arrayOfInt1[0] = paramInt1;
    arrayOfInt2[0] = paramInt2;
    fContentSpec.setValues(paramShort, arrayOfInt1, arrayOfInt2);
    setContentSpec(i, fContentSpec);
    return i;
  }
  
  protected void initializeContentModelStack()
  {
    if (fOpStack == null)
    {
      fOpStack = new short[8];
      fNodeIndexStack = new int[8];
      fPrevNodeIndexStack = new int[8];
    }
    else if (fDepth == fOpStack.length)
    {
      short[] arrayOfShort = new short[fDepth * 2];
      System.arraycopy(fOpStack, 0, arrayOfShort, 0, fDepth);
      fOpStack = arrayOfShort;
      int[] arrayOfInt = new int[fDepth * 2];
      System.arraycopy(fNodeIndexStack, 0, arrayOfInt, 0, fDepth);
      fNodeIndexStack = arrayOfInt;
      arrayOfInt = new int[fDepth * 2];
      System.arraycopy(fPrevNodeIndexStack, 0, arrayOfInt, 0, fDepth);
      fPrevNodeIndexStack = arrayOfInt;
    }
    fOpStack[fDepth] = -1;
    fNodeIndexStack[fDepth] = -1;
    fPrevNodeIndexStack[fDepth] = -1;
  }
  
  boolean isImmutable()
  {
    return fIsImmutable;
  }
  
  private void appendContentSpec(XMLContentSpec paramXMLContentSpec, StringBuffer paramStringBuffer, boolean paramBoolean, int paramInt)
  {
    int i = type & 0xF;
    switch (i)
    {
    case 0: 
      if ((value == null) && (otherValue == null)) {
        paramStringBuffer.append("#PCDATA");
      } else if ((value == null) && (otherValue != null)) {
        paramStringBuffer.append("##any:uri=").append(otherValue);
      } else if (value == null) {
        paramStringBuffer.append("##any");
      } else {
        paramStringBuffer.append(value);
      }
      break;
    case 1: 
      if ((paramInt == 3) || (paramInt == 2) || (paramInt == 1))
      {
        getContentSpec(((int[])value)[0], paramXMLContentSpec);
        paramStringBuffer.append('(');
        appendContentSpec(paramXMLContentSpec, paramStringBuffer, true, i);
        paramStringBuffer.append(')');
      }
      else
      {
        getContentSpec(((int[])value)[0], paramXMLContentSpec);
        appendContentSpec(paramXMLContentSpec, paramStringBuffer, true, i);
      }
      paramStringBuffer.append('?');
      break;
    case 2: 
      if ((paramInt == 3) || (paramInt == 2) || (paramInt == 1))
      {
        getContentSpec(((int[])value)[0], paramXMLContentSpec);
        paramStringBuffer.append('(');
        appendContentSpec(paramXMLContentSpec, paramStringBuffer, true, i);
        paramStringBuffer.append(')');
      }
      else
      {
        getContentSpec(((int[])value)[0], paramXMLContentSpec);
        appendContentSpec(paramXMLContentSpec, paramStringBuffer, true, i);
      }
      paramStringBuffer.append('*');
      break;
    case 3: 
      if ((paramInt == 3) || (paramInt == 2) || (paramInt == 1))
      {
        paramStringBuffer.append('(');
        getContentSpec(((int[])value)[0], paramXMLContentSpec);
        appendContentSpec(paramXMLContentSpec, paramStringBuffer, true, i);
        paramStringBuffer.append(')');
      }
      else
      {
        getContentSpec(((int[])value)[0], paramXMLContentSpec);
        appendContentSpec(paramXMLContentSpec, paramStringBuffer, true, i);
      }
      paramStringBuffer.append('+');
      break;
    case 4: 
    case 5: 
      if (paramBoolean) {
        paramStringBuffer.append('(');
      }
      int j = type;
      int k = ((int[])otherValue)[0];
      getContentSpec(((int[])value)[0], paramXMLContentSpec);
      appendContentSpec(paramXMLContentSpec, paramStringBuffer, type != j, i);
      if (j == 4) {
        paramStringBuffer.append('|');
      } else {
        paramStringBuffer.append(',');
      }
      getContentSpec(k, paramXMLContentSpec);
      appendContentSpec(paramXMLContentSpec, paramStringBuffer, true, i);
      if (paramBoolean) {
        paramStringBuffer.append(')');
      }
      break;
    case 6: 
      paramStringBuffer.append("##any");
      if (otherValue != null)
      {
        paramStringBuffer.append(":uri=");
        paramStringBuffer.append(otherValue);
      }
      break;
    case 7: 
      paramStringBuffer.append("##other:uri=");
      paramStringBuffer.append(otherValue);
      break;
    case 8: 
      paramStringBuffer.append("##local");
      break;
    default: 
      paramStringBuffer.append("???");
    }
  }
  
  private void printAttribute(int paramInt)
  {
    XMLAttributeDecl localXMLAttributeDecl = new XMLAttributeDecl();
    if (getAttributeDecl(paramInt, localXMLAttributeDecl))
    {
      System.out.print(" { ");
      System.out.print(name.localpart);
      System.out.print(" }");
    }
  }
  
  private synchronized ContentModelValidator createChildModel(int paramInt)
  {
    XMLContentSpec localXMLContentSpec1 = new XMLContentSpec();
    getContentSpec(paramInt, localXMLContentSpec1);
    if (((type & 0xF) != 6) && ((type & 0xF) != 7) && ((type & 0xF) != 8))
    {
      if (type == 0)
      {
        if ((value == null) && (otherValue == null)) {
          throw new RuntimeException("ImplementationMessages.VAL_NPCD");
        }
        fQName.setValues(null, (String)value, (String)value, (String)otherValue);
        return new SimpleContentModel(type, fQName, null);
      }
      if ((type == 4) || (type == 5))
      {
        localObject = new XMLContentSpec();
        XMLContentSpec localXMLContentSpec2 = new XMLContentSpec();
        getContentSpec(((int[])value)[0], (XMLContentSpec)localObject);
        getContentSpec(((int[])otherValue)[0], localXMLContentSpec2);
        if ((type == 0) && (type == 0))
        {
          fQName.setValues(null, (String)value, (String)value, (String)otherValue);
          fQName2.setValues(null, (String)value, (String)value, (String)otherValue);
          return new SimpleContentModel(type, fQName, fQName2);
        }
      }
      else if ((type == 1) || (type == 2) || (type == 3))
      {
        localObject = new XMLContentSpec();
        getContentSpec(((int[])value)[0], (XMLContentSpec)localObject);
        if (type == 0)
        {
          fQName.setValues(null, (String)value, (String)value, (String)otherValue);
          return new SimpleContentModel(type, fQName, null);
        }
      }
      else
      {
        throw new RuntimeException("ImplementationMessages.VAL_CST");
      }
    }
    fLeafCount = 0;
    fLeafCount = 0;
    Object localObject = buildSyntaxTree(paramInt, localXMLContentSpec1);
    return new DFAContentModel((CMNode)localObject, fLeafCount, false);
  }
  
  private final CMNode buildSyntaxTree(int paramInt, XMLContentSpec paramXMLContentSpec)
  {
    Object localObject = null;
    getContentSpec(paramInt, paramXMLContentSpec);
    if ((type & 0xF) == 6)
    {
      localObject = new CMAny(type, (String)otherValue, fLeafCount++);
    }
    else if ((type & 0xF) == 7)
    {
      localObject = new CMAny(type, (String)otherValue, fLeafCount++);
    }
    else if ((type & 0xF) == 8)
    {
      localObject = new CMAny(type, null, fLeafCount++);
    }
    else if (type == 0)
    {
      fQName.setValues(null, (String)value, (String)value, (String)otherValue);
      localObject = new CMLeaf(fQName, fLeafCount++);
    }
    else
    {
      int i = ((int[])value)[0];
      int j = ((int[])otherValue)[0];
      if ((type == 4) || (type == 5)) {
        localObject = new CMBinOp(type, buildSyntaxTree(i, paramXMLContentSpec), buildSyntaxTree(j, paramXMLContentSpec));
      } else if (type == 2) {
        localObject = new CMUniOp(type, buildSyntaxTree(i, paramXMLContentSpec));
      } else if ((type == 2) || (type == 1) || (type == 3)) {
        localObject = new CMUniOp(type, buildSyntaxTree(i, paramXMLContentSpec));
      } else {
        throw new RuntimeException("ImplementationMessages.VAL_CST");
      }
    }
    return localObject;
  }
  
  private void contentSpecTree(int paramInt, XMLContentSpec paramXMLContentSpec, ChildrenList paramChildrenList)
  {
    getContentSpec(paramInt, paramXMLContentSpec);
    if ((type == 0) || ((type & 0xF) == 6) || ((type & 0xF) == 8) || ((type & 0xF) == 7))
    {
      if (length == qname.length)
      {
        QName[] arrayOfQName = new QName[length * 2];
        System.arraycopy(qname, 0, arrayOfQName, 0, length);
        qname = arrayOfQName;
        int[] arrayOfInt = new int[length * 2];
        System.arraycopy(type, 0, arrayOfInt, 0, length);
        type = arrayOfInt;
      }
      qname[length] = new QName(null, (String)value, (String)value, (String)otherValue);
      type[length] = type;
      length += 1;
      return;
    }
    int i = value != null ? ((int[])value)[0] : -1;
    int j = -1;
    if (otherValue != null) {
      j = ((int[])otherValue)[0];
    } else {
      return;
    }
    if ((type == 4) || (type == 5))
    {
      contentSpecTree(i, paramXMLContentSpec, paramChildrenList);
      contentSpecTree(j, paramXMLContentSpec, paramChildrenList);
      return;
    }
    if ((type == 1) || (type == 2) || (type == 3))
    {
      contentSpecTree(i, paramXMLContentSpec, paramChildrenList);
      return;
    }
    throw new RuntimeException("Invalid content spec type seen in contentSpecTree() method of AbstractDTDGrammar class : " + type);
  }
  
  private void ensureElementDeclCapacity(int paramInt)
  {
    if (paramInt >= fElementDeclName.length)
    {
      fElementDeclIsExternal = resize(fElementDeclIsExternal, fElementDeclIsExternal.length * 2);
      fElementDeclName = resize(fElementDeclName, fElementDeclName.length * 2);
      fElementDeclType = resize(fElementDeclType, fElementDeclType.length * 2);
      fElementDeclContentModelValidator = resize(fElementDeclContentModelValidator, fElementDeclContentModelValidator.length * 2);
      fElementDeclContentSpecIndex = resize(fElementDeclContentSpecIndex, fElementDeclContentSpecIndex.length * 2);
      fElementDeclFirstAttributeDeclIndex = resize(fElementDeclFirstAttributeDeclIndex, fElementDeclFirstAttributeDeclIndex.length * 2);
      fElementDeclLastAttributeDeclIndex = resize(fElementDeclLastAttributeDeclIndex, fElementDeclLastAttributeDeclIndex.length * 2);
    }
    else if (fElementDeclName[paramInt] != null)
    {
      return;
    }
    fElementDeclIsExternal[paramInt] = new int['Ā'];
    fElementDeclName[paramInt] = new QName['Ā'];
    fElementDeclType[paramInt] = new short['Ā'];
    fElementDeclContentModelValidator[paramInt] = new ContentModelValidator['Ā'];
    fElementDeclContentSpecIndex[paramInt] = new int['Ā'];
    fElementDeclFirstAttributeDeclIndex[paramInt] = new int['Ā'];
    fElementDeclLastAttributeDeclIndex[paramInt] = new int['Ā'];
  }
  
  private void ensureAttributeDeclCapacity(int paramInt)
  {
    if (paramInt >= fAttributeDeclName.length)
    {
      fAttributeDeclIsExternal = resize(fAttributeDeclIsExternal, fAttributeDeclIsExternal.length * 2);
      fAttributeDeclName = resize(fAttributeDeclName, fAttributeDeclName.length * 2);
      fAttributeDeclType = resize(fAttributeDeclType, fAttributeDeclType.length * 2);
      fAttributeDeclEnumeration = resize(fAttributeDeclEnumeration, fAttributeDeclEnumeration.length * 2);
      fAttributeDeclDefaultType = resize(fAttributeDeclDefaultType, fAttributeDeclDefaultType.length * 2);
      fAttributeDeclDatatypeValidator = resize(fAttributeDeclDatatypeValidator, fAttributeDeclDatatypeValidator.length * 2);
      fAttributeDeclDefaultValue = resize(fAttributeDeclDefaultValue, fAttributeDeclDefaultValue.length * 2);
      fAttributeDeclNonNormalizedDefaultValue = resize(fAttributeDeclNonNormalizedDefaultValue, fAttributeDeclNonNormalizedDefaultValue.length * 2);
      fAttributeDeclNextAttributeDeclIndex = resize(fAttributeDeclNextAttributeDeclIndex, fAttributeDeclNextAttributeDeclIndex.length * 2);
    }
    else if (fAttributeDeclName[paramInt] != null)
    {
      return;
    }
    fAttributeDeclIsExternal[paramInt] = new int['Ā'];
    fAttributeDeclName[paramInt] = new QName['Ā'];
    fAttributeDeclType[paramInt] = new short['Ā'];
    fAttributeDeclEnumeration[paramInt] = new String['Ā'][];
    fAttributeDeclDefaultType[paramInt] = new short['Ā'];
    fAttributeDeclDatatypeValidator[paramInt] = new DatatypeValidator['Ā'];
    fAttributeDeclDefaultValue[paramInt] = new String['Ā'];
    fAttributeDeclNonNormalizedDefaultValue[paramInt] = new String['Ā'];
    fAttributeDeclNextAttributeDeclIndex[paramInt] = new int['Ā'];
  }
  
  private void ensureEntityDeclCapacity(int paramInt)
  {
    if (paramInt >= fEntityName.length)
    {
      fEntityName = resize(fEntityName, fEntityName.length * 2);
      fEntityValue = resize(fEntityValue, fEntityValue.length * 2);
      fEntityPublicId = resize(fEntityPublicId, fEntityPublicId.length * 2);
      fEntitySystemId = resize(fEntitySystemId, fEntitySystemId.length * 2);
      fEntityBaseSystemId = resize(fEntityBaseSystemId, fEntityBaseSystemId.length * 2);
      fEntityNotation = resize(fEntityNotation, fEntityNotation.length * 2);
      fEntityIsPE = resize(fEntityIsPE, fEntityIsPE.length * 2);
      fEntityInExternal = resize(fEntityInExternal, fEntityInExternal.length * 2);
    }
    else if (fEntityName[paramInt] != null)
    {
      return;
    }
    fEntityName[paramInt] = new String['Ā'];
    fEntityValue[paramInt] = new String['Ā'];
    fEntityPublicId[paramInt] = new String['Ā'];
    fEntitySystemId[paramInt] = new String['Ā'];
    fEntityBaseSystemId[paramInt] = new String['Ā'];
    fEntityNotation[paramInt] = new String['Ā'];
    fEntityIsPE[paramInt] = new byte['Ā'];
    fEntityInExternal[paramInt] = new byte['Ā'];
  }
  
  private void ensureNotationDeclCapacity(int paramInt)
  {
    if (paramInt >= fNotationName.length)
    {
      fNotationName = resize(fNotationName, fNotationName.length * 2);
      fNotationPublicId = resize(fNotationPublicId, fNotationPublicId.length * 2);
      fNotationSystemId = resize(fNotationSystemId, fNotationSystemId.length * 2);
      fNotationBaseSystemId = resize(fNotationBaseSystemId, fNotationBaseSystemId.length * 2);
    }
    else if (fNotationName[paramInt] != null)
    {
      return;
    }
    fNotationName[paramInt] = new String['Ā'];
    fNotationPublicId[paramInt] = new String['Ā'];
    fNotationSystemId[paramInt] = new String['Ā'];
    fNotationBaseSystemId[paramInt] = new String['Ā'];
  }
  
  private void ensureContentSpecCapacity(int paramInt)
  {
    if (paramInt >= fContentSpecType.length)
    {
      fContentSpecType = resize(fContentSpecType, fContentSpecType.length * 2);
      fContentSpecValue = resize(fContentSpecValue, fContentSpecValue.length * 2);
      fContentSpecOtherValue = resize(fContentSpecOtherValue, fContentSpecOtherValue.length * 2);
    }
    else if (fContentSpecType[paramInt] != null)
    {
      return;
    }
    fContentSpecType[paramInt] = new short['Ā'];
    fContentSpecValue[paramInt] = new Object['Ā'];
    fContentSpecOtherValue[paramInt] = new Object['Ā'];
  }
  
  private static byte[][] resize(byte[][] paramArrayOfByte, int paramInt)
  {
    byte[][] arrayOfByte = new byte[paramInt][];
    System.arraycopy(paramArrayOfByte, 0, arrayOfByte, 0, paramArrayOfByte.length);
    return arrayOfByte;
  }
  
  private static short[][] resize(short[][] paramArrayOfShort, int paramInt)
  {
    short[][] arrayOfShort = new short[paramInt][];
    System.arraycopy(paramArrayOfShort, 0, arrayOfShort, 0, paramArrayOfShort.length);
    return arrayOfShort;
  }
  
  private static int[][] resize(int[][] paramArrayOfInt, int paramInt)
  {
    int[][] arrayOfInt = new int[paramInt][];
    System.arraycopy(paramArrayOfInt, 0, arrayOfInt, 0, paramArrayOfInt.length);
    return arrayOfInt;
  }
  
  private static DatatypeValidator[][] resize(DatatypeValidator[][] paramArrayOfDatatypeValidator, int paramInt)
  {
    DatatypeValidator[][] arrayOfDatatypeValidator; = new DatatypeValidator[paramInt][];
    System.arraycopy(paramArrayOfDatatypeValidator, 0, arrayOfDatatypeValidator;, 0, paramArrayOfDatatypeValidator.length);
    return arrayOfDatatypeValidator;;
  }
  
  private static ContentModelValidator[][] resize(ContentModelValidator[][] paramArrayOfContentModelValidator, int paramInt)
  {
    ContentModelValidator[][] arrayOfContentModelValidator; = new ContentModelValidator[paramInt][];
    System.arraycopy(paramArrayOfContentModelValidator, 0, arrayOfContentModelValidator;, 0, paramArrayOfContentModelValidator.length);
    return arrayOfContentModelValidator;;
  }
  
  private static Object[][] resize(Object[][] paramArrayOfObject, int paramInt)
  {
    Object[][] arrayOfObject; = new Object[paramInt][];
    System.arraycopy(paramArrayOfObject, 0, arrayOfObject;, 0, paramArrayOfObject.length);
    return arrayOfObject;;
  }
  
  private static QName[][] resize(QName[][] paramArrayOfQName, int paramInt)
  {
    QName[][] arrayOfQName; = new QName[paramInt][];
    System.arraycopy(paramArrayOfQName, 0, arrayOfQName;, 0, paramArrayOfQName.length);
    return arrayOfQName;;
  }
  
  private static String[][] resize(String[][] paramArrayOfString, int paramInt)
  {
    String[][] arrayOfString; = new String[paramInt][];
    System.arraycopy(paramArrayOfString, 0, arrayOfString;, 0, paramArrayOfString.length);
    return arrayOfString;;
  }
  
  private static String[][][] resize(String[][][] paramArrayOfString, int paramInt)
  {
    String[][][] arrayOfString; = new String[paramInt][][];
    System.arraycopy(paramArrayOfString, 0, arrayOfString;, 0, paramArrayOfString.length);
    return arrayOfString;;
  }
  
  public boolean isEntityDeclared(String paramString)
  {
    return getEntityDeclIndex(paramString) != -1;
  }
  
  public boolean isEntityUnparsed(String paramString)
  {
    int i = getEntityDeclIndex(paramString);
    if (i > -1)
    {
      int j = i >> 8;
      int k = i & 0xFF;
      return fEntityNotation[j][k] != null;
    }
    return false;
  }
  
  protected static final class QNameHashtable
  {
    private static final int INITIAL_BUCKET_SIZE = 4;
    private static final int HASHTABLE_SIZE = 101;
    private Object[][] fHashTable = new Object[101][];
    
    protected QNameHashtable() {}
    
    public void put(String paramString, int paramInt)
    {
      int i = (paramString.hashCode() & 0x7FFFFFFF) % 101;
      Object localObject = fHashTable[i];
      if (localObject == null)
      {
        localObject = new Object[9];
        localObject[0] = { 1 };
        localObject[1] = paramString;
        localObject[2] = { paramInt };
        fHashTable[i] = localObject;
      }
      else
      {
        int j = ((int[])localObject[0])[0];
        int k = 1 + 2 * j;
        if (k == localObject.length)
        {
          m = j + 4;
          Object[] arrayOfObject = new Object[1 + 2 * m];
          System.arraycopy(localObject, 0, arrayOfObject, 0, k);
          localObject = arrayOfObject;
          fHashTable[i] = localObject;
        }
        int m = 0;
        int n = 1;
        for (int i1 = 0; i1 < j; i1++)
        {
          if ((String)localObject[n] == paramString)
          {
            ((int[])localObject[(n + 1)])[0] = paramInt;
            m = 1;
            break;
          }
          n += 2;
        }
        if (m == 0)
        {
          localObject[(k++)] = paramString;
          localObject[k] = { paramInt };
          ((int[])localObject[0])[0] = (++j);
        }
      }
    }
    
    public int get(String paramString)
    {
      int i = (paramString.hashCode() & 0x7FFFFFFF) % 101;
      Object[] arrayOfObject = fHashTable[i];
      if (arrayOfObject == null) {
        return -1;
      }
      int j = ((int[])arrayOfObject[0])[0];
      int k = 1;
      for (int m = 0; m < j; m++)
      {
        if ((String)arrayOfObject[k] == paramString) {
          return ((int[])arrayOfObject[(k + 1)])[0];
        }
        k += 2;
      }
      return -1;
    }
  }
  
  private static class ChildrenList
  {
    public int length = 0;
    public QName[] qname = new QName[2];
    public int[] type = new int[2];
    
    public ChildrenList() {}
  }
}
