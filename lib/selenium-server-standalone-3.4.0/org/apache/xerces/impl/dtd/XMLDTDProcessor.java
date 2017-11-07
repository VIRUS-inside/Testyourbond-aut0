package org.apache.xerces.impl.dtd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLDTDContentModelHandler;
import org.apache.xerces.xni.XMLDTDHandler;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLComponent;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLDTDContentModelFilter;
import org.apache.xerces.xni.parser.XMLDTDContentModelSource;
import org.apache.xerces.xni.parser.XMLDTDFilter;
import org.apache.xerces.xni.parser.XMLDTDSource;

public class XMLDTDProcessor
  implements XMLComponent, XMLDTDFilter, XMLDTDContentModelFilter
{
  private static final int TOP_LEVEL_SCOPE = -1;
  protected static final String VALIDATION = "http://xml.org/sax/features/validation";
  protected static final String NOTIFY_CHAR_REFS = "http://apache.org/xml/features/scanner/notify-char-refs";
  protected static final String WARN_ON_DUPLICATE_ATTDEF = "http://apache.org/xml/features/validation/warn-on-duplicate-attdef";
  protected static final String WARN_ON_UNDECLARED_ELEMDEF = "http://apache.org/xml/features/validation/warn-on-undeclared-elemdef";
  protected static final String PARSER_SETTINGS = "http://apache.org/xml/features/internal/parser-settings";
  protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
  protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
  protected static final String GRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
  protected static final String DTD_VALIDATOR = "http://apache.org/xml/properties/internal/validator/dtd";
  private static final String[] RECOGNIZED_FEATURES = { "http://xml.org/sax/features/validation", "http://apache.org/xml/features/validation/warn-on-duplicate-attdef", "http://apache.org/xml/features/validation/warn-on-undeclared-elemdef", "http://apache.org/xml/features/scanner/notify-char-refs" };
  private static final Boolean[] FEATURE_DEFAULTS = { null, Boolean.FALSE, Boolean.FALSE, null };
  private static final String[] RECOGNIZED_PROPERTIES = { "http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/grammar-pool", "http://apache.org/xml/properties/internal/validator/dtd" };
  private static final Object[] PROPERTY_DEFAULTS = { null, null, null, null };
  protected boolean fValidation;
  protected boolean fDTDValidation;
  protected boolean fWarnDuplicateAttdef;
  protected boolean fWarnOnUndeclaredElemdef;
  protected SymbolTable fSymbolTable;
  protected XMLErrorReporter fErrorReporter;
  protected DTDGrammarBucket fGrammarBucket;
  protected XMLDTDValidator fValidator;
  protected XMLGrammarPool fGrammarPool;
  protected Locale fLocale;
  protected XMLDTDHandler fDTDHandler;
  protected XMLDTDSource fDTDSource;
  protected XMLDTDContentModelHandler fDTDContentModelHandler;
  protected XMLDTDContentModelSource fDTDContentModelSource;
  protected DTDGrammar fDTDGrammar;
  private boolean fPerformValidation;
  protected boolean fInDTDIgnore;
  private boolean fMixed;
  private final XMLEntityDecl fEntityDecl = new XMLEntityDecl();
  private final HashMap fNDataDeclNotations = new HashMap();
  private String fDTDElementDeclName = null;
  private final ArrayList fMixedElementTypes = new ArrayList();
  private final ArrayList fDTDElementDecls = new ArrayList();
  private HashMap fTableOfIDAttributeNames;
  private HashMap fTableOfNOTATIONAttributeNames;
  private HashMap fNotationEnumVals;
  
  public XMLDTDProcessor() {}
  
  public void reset(XMLComponentManager paramXMLComponentManager)
    throws XMLConfigurationException
  {
    boolean bool;
    try
    {
      bool = paramXMLComponentManager.getFeature("http://apache.org/xml/features/internal/parser-settings");
    }
    catch (XMLConfigurationException localXMLConfigurationException1)
    {
      bool = true;
    }
    if (!bool)
    {
      reset();
      return;
    }
    try
    {
      fValidation = paramXMLComponentManager.getFeature("http://xml.org/sax/features/validation");
    }
    catch (XMLConfigurationException localXMLConfigurationException2)
    {
      fValidation = false;
    }
    try
    {
      fDTDValidation = (!paramXMLComponentManager.getFeature("http://apache.org/xml/features/validation/schema"));
    }
    catch (XMLConfigurationException localXMLConfigurationException3)
    {
      fDTDValidation = true;
    }
    try
    {
      fWarnDuplicateAttdef = paramXMLComponentManager.getFeature("http://apache.org/xml/features/validation/warn-on-duplicate-attdef");
    }
    catch (XMLConfigurationException localXMLConfigurationException4)
    {
      fWarnDuplicateAttdef = false;
    }
    try
    {
      fWarnOnUndeclaredElemdef = paramXMLComponentManager.getFeature("http://apache.org/xml/features/validation/warn-on-undeclared-elemdef");
    }
    catch (XMLConfigurationException localXMLConfigurationException5)
    {
      fWarnOnUndeclaredElemdef = false;
    }
    fErrorReporter = ((XMLErrorReporter)paramXMLComponentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter"));
    fSymbolTable = ((SymbolTable)paramXMLComponentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table"));
    try
    {
      fGrammarPool = ((XMLGrammarPool)paramXMLComponentManager.getProperty("http://apache.org/xml/properties/internal/grammar-pool"));
    }
    catch (XMLConfigurationException localXMLConfigurationException6)
    {
      fGrammarPool = null;
    }
    try
    {
      fValidator = ((XMLDTDValidator)paramXMLComponentManager.getProperty("http://apache.org/xml/properties/internal/validator/dtd"));
    }
    catch (XMLConfigurationException localXMLConfigurationException7)
    {
      fValidator = null;
    }
    catch (ClassCastException localClassCastException)
    {
      fValidator = null;
    }
    if (fValidator != null) {
      fGrammarBucket = fValidator.getGrammarBucket();
    } else {
      fGrammarBucket = null;
    }
    reset();
  }
  
  protected void reset()
  {
    fDTDGrammar = null;
    fInDTDIgnore = false;
    fNDataDeclNotations.clear();
    if (fValidation)
    {
      if (fNotationEnumVals == null) {
        fNotationEnumVals = new HashMap();
      }
      fNotationEnumVals.clear();
      fTableOfIDAttributeNames = new HashMap();
      fTableOfNOTATIONAttributeNames = new HashMap();
    }
  }
  
  public String[] getRecognizedFeatures()
  {
    return (String[])RECOGNIZED_FEATURES.clone();
  }
  
  public void setFeature(String paramString, boolean paramBoolean)
    throws XMLConfigurationException
  {}
  
  public String[] getRecognizedProperties()
  {
    return (String[])RECOGNIZED_PROPERTIES.clone();
  }
  
  public void setProperty(String paramString, Object paramObject)
    throws XMLConfigurationException
  {}
  
  public Boolean getFeatureDefault(String paramString)
  {
    for (int i = 0; i < RECOGNIZED_FEATURES.length; i++) {
      if (RECOGNIZED_FEATURES[i].equals(paramString)) {
        return FEATURE_DEFAULTS[i];
      }
    }
    return null;
  }
  
  public Object getPropertyDefault(String paramString)
  {
    for (int i = 0; i < RECOGNIZED_PROPERTIES.length; i++) {
      if (RECOGNIZED_PROPERTIES[i].equals(paramString)) {
        return PROPERTY_DEFAULTS[i];
      }
    }
    return null;
  }
  
  public void setDTDHandler(XMLDTDHandler paramXMLDTDHandler)
  {
    fDTDHandler = paramXMLDTDHandler;
  }
  
  public XMLDTDHandler getDTDHandler()
  {
    return fDTDHandler;
  }
  
  public void setDTDContentModelHandler(XMLDTDContentModelHandler paramXMLDTDContentModelHandler)
  {
    fDTDContentModelHandler = paramXMLDTDContentModelHandler;
  }
  
  public XMLDTDContentModelHandler getDTDContentModelHandler()
  {
    return fDTDContentModelHandler;
  }
  
  public void startExternalSubset(XMLResourceIdentifier paramXMLResourceIdentifier, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDTDGrammar != null) {
      fDTDGrammar.startExternalSubset(paramXMLResourceIdentifier, paramAugmentations);
    }
    if (fDTDHandler != null) {
      fDTDHandler.startExternalSubset(paramXMLResourceIdentifier, paramAugmentations);
    }
  }
  
  public void endExternalSubset(Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDTDGrammar != null) {
      fDTDGrammar.endExternalSubset(paramAugmentations);
    }
    if (fDTDHandler != null) {
      fDTDHandler.endExternalSubset(paramAugmentations);
    }
  }
  
  protected static void checkStandaloneEntityRef(String paramString, DTDGrammar paramDTDGrammar, XMLEntityDecl paramXMLEntityDecl, XMLErrorReporter paramXMLErrorReporter)
    throws XNIException
  {
    int i = paramDTDGrammar.getEntityDeclIndex(paramString);
    if (i > -1)
    {
      paramDTDGrammar.getEntityDecl(i, paramXMLEntityDecl);
      if (inExternal) {
        paramXMLErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_REFERENCE_TO_EXTERNALLY_DECLARED_ENTITY_WHEN_STANDALONE", new Object[] { paramString }, (short)1);
      }
    }
  }
  
  public void comment(XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDTDGrammar != null) {
      fDTDGrammar.comment(paramXMLString, paramAugmentations);
    }
    if (fDTDHandler != null) {
      fDTDHandler.comment(paramXMLString, paramAugmentations);
    }
  }
  
  public void processingInstruction(String paramString, XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDTDGrammar != null) {
      fDTDGrammar.processingInstruction(paramString, paramXMLString, paramAugmentations);
    }
    if (fDTDHandler != null) {
      fDTDHandler.processingInstruction(paramString, paramXMLString, paramAugmentations);
    }
  }
  
  public void startDTD(XMLLocator paramXMLLocator, Augmentations paramAugmentations)
    throws XNIException
  {
    fNDataDeclNotations.clear();
    fDTDElementDecls.clear();
    if (!fGrammarBucket.getActiveGrammar().isImmutable()) {
      fDTDGrammar = fGrammarBucket.getActiveGrammar();
    }
    if (fDTDGrammar != null) {
      fDTDGrammar.startDTD(paramXMLLocator, paramAugmentations);
    }
    if (fDTDHandler != null) {
      fDTDHandler.startDTD(paramXMLLocator, paramAugmentations);
    }
  }
  
  public void ignoredCharacters(XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDTDGrammar != null) {
      fDTDGrammar.ignoredCharacters(paramXMLString, paramAugmentations);
    }
    if (fDTDHandler != null) {
      fDTDHandler.ignoredCharacters(paramXMLString, paramAugmentations);
    }
  }
  
  public void textDecl(String paramString1, String paramString2, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDTDGrammar != null) {
      fDTDGrammar.textDecl(paramString1, paramString2, paramAugmentations);
    }
    if (fDTDHandler != null) {
      fDTDHandler.textDecl(paramString1, paramString2, paramAugmentations);
    }
  }
  
  public void startParameterEntity(String paramString1, XMLResourceIdentifier paramXMLResourceIdentifier, String paramString2, Augmentations paramAugmentations)
    throws XNIException
  {
    if ((fPerformValidation) && (fDTDGrammar != null) && (fGrammarBucket.getStandalone())) {
      checkStandaloneEntityRef(paramString1, fDTDGrammar, fEntityDecl, fErrorReporter);
    }
    if (fDTDGrammar != null) {
      fDTDGrammar.startParameterEntity(paramString1, paramXMLResourceIdentifier, paramString2, paramAugmentations);
    }
    if (fDTDHandler != null) {
      fDTDHandler.startParameterEntity(paramString1, paramXMLResourceIdentifier, paramString2, paramAugmentations);
    }
  }
  
  public void endParameterEntity(String paramString, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDTDGrammar != null) {
      fDTDGrammar.endParameterEntity(paramString, paramAugmentations);
    }
    if (fDTDHandler != null) {
      fDTDHandler.endParameterEntity(paramString, paramAugmentations);
    }
  }
  
  public void elementDecl(String paramString1, String paramString2, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fValidation) {
      if (fDTDElementDecls.contains(paramString1)) {
        fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_ELEMENT_ALREADY_DECLARED", new Object[] { paramString1 }, (short)1);
      } else {
        fDTDElementDecls.add(paramString1);
      }
    }
    if (fDTDGrammar != null) {
      fDTDGrammar.elementDecl(paramString1, paramString2, paramAugmentations);
    }
    if (fDTDHandler != null) {
      fDTDHandler.elementDecl(paramString1, paramString2, paramAugmentations);
    }
  }
  
  public void startAttlist(String paramString, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDTDGrammar != null) {
      fDTDGrammar.startAttlist(paramString, paramAugmentations);
    }
    if (fDTDHandler != null) {
      fDTDHandler.startAttlist(paramString, paramAugmentations);
    }
  }
  
  public void attributeDecl(String paramString1, String paramString2, String paramString3, String[] paramArrayOfString, String paramString4, XMLString paramXMLString1, XMLString paramXMLString2, Augmentations paramAugmentations)
    throws XNIException
  {
    if ((paramString3 != XMLSymbols.fCDATASymbol) && (paramXMLString1 != null)) {
      normalizeDefaultAttrValue(paramXMLString1);
    }
    if (fValidation)
    {
      int i = 0;
      DTDGrammar localDTDGrammar = fDTDGrammar != null ? fDTDGrammar : fGrammarBucket.getActiveGrammar();
      int j = localDTDGrammar.getElementDeclIndex(paramString1);
      if (localDTDGrammar.getAttributeDeclIndex(j, paramString2) != -1)
      {
        i = 1;
        if (fWarnDuplicateAttdef) {
          fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_DUPLICATE_ATTRIBUTE_DEFINITION", new Object[] { paramString1, paramString2 }, (short)0);
        }
      }
      if (paramString3 == XMLSymbols.fIDSymbol)
      {
        if ((paramXMLString1 != null) && (length != 0) && ((paramString4 == null) || ((paramString4 != XMLSymbols.fIMPLIEDSymbol) && (paramString4 != XMLSymbols.fREQUIREDSymbol)))) {
          fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "IDDefaultTypeInvalid", new Object[] { paramString2 }, (short)1);
        }
        if (!fTableOfIDAttributeNames.containsKey(paramString1))
        {
          fTableOfIDAttributeNames.put(paramString1, paramString2);
        }
        else if (i == 0)
        {
          String str1 = (String)fTableOfIDAttributeNames.get(paramString1);
          fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_MORE_THAN_ONE_ID_ATTRIBUTE", new Object[] { paramString1, str1, paramString2 }, (short)1);
        }
      }
      if (paramString3 == XMLSymbols.fNOTATIONSymbol)
      {
        for (k = 0; k < paramArrayOfString.length; k++) {
          fNotationEnumVals.put(paramArrayOfString[k], paramString2);
        }
        if (!fTableOfNOTATIONAttributeNames.containsKey(paramString1))
        {
          fTableOfNOTATIONAttributeNames.put(paramString1, paramString2);
        }
        else if (i == 0)
        {
          String str2 = (String)fTableOfNOTATIONAttributeNames.get(paramString1);
          fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_MORE_THAN_ONE_NOTATION_ATTRIBUTE", new Object[] { paramString1, str2, paramString2 }, (short)1);
        }
      }
      if ((paramString3 == XMLSymbols.fENUMERATIONSymbol) || (paramString3 == XMLSymbols.fNOTATIONSymbol)) {
        for (k = 0; k < paramArrayOfString.length; k++) {
          for (int m = k + 1; m < paramArrayOfString.length; m++) {
            if (paramArrayOfString[k].equals(paramArrayOfString[m]))
            {
              fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", paramString3 == XMLSymbols.fENUMERATIONSymbol ? "MSG_DISTINCT_TOKENS_IN_ENUMERATION" : "MSG_DISTINCT_NOTATION_IN_ENUMERATION", new Object[] { paramString1, paramArrayOfString[k], paramString2 }, (short)1);
              break;
            }
          }
        }
      }
      int k = 1;
      if ((paramXMLString1 != null) && ((paramString4 == null) || ((paramString4 != null) && (paramString4 == XMLSymbols.fFIXEDSymbol))))
      {
        String str3 = paramXMLString1.toString();
        if ((paramString3 == XMLSymbols.fNMTOKENSSymbol) || (paramString3 == XMLSymbols.fENTITIESSymbol) || (paramString3 == XMLSymbols.fIDREFSSymbol))
        {
          StringTokenizer localStringTokenizer = new StringTokenizer(str3, " ");
          if (localStringTokenizer.hasMoreTokens()) {
            do
            {
              String str4 = localStringTokenizer.nextToken();
              if (paramString3 == XMLSymbols.fNMTOKENSSymbol)
              {
                if (!isValidNmtoken(str4))
                {
                  k = 0;
                  break;
                }
              }
              else if (((paramString3 == XMLSymbols.fENTITIESSymbol) || (paramString3 == XMLSymbols.fIDREFSSymbol)) && (!isValidName(str4)))
              {
                k = 0;
                break;
              }
            } while (localStringTokenizer.hasMoreTokens());
          }
        }
        else
        {
          if ((paramString3 == XMLSymbols.fENTITYSymbol) || (paramString3 == XMLSymbols.fIDSymbol) || (paramString3 == XMLSymbols.fIDREFSymbol) || (paramString3 == XMLSymbols.fNOTATIONSymbol))
          {
            if (!isValidName(str3)) {
              k = 0;
            }
          }
          else if (((paramString3 == XMLSymbols.fNMTOKENSymbol) || (paramString3 == XMLSymbols.fENUMERATIONSymbol)) && (!isValidNmtoken(str3))) {
            k = 0;
          }
          if ((paramString3 == XMLSymbols.fNOTATIONSymbol) || (paramString3 == XMLSymbols.fENUMERATIONSymbol))
          {
            k = 0;
            for (int n = 0; n < paramArrayOfString.length; n++) {
              if (paramXMLString1.equals(paramArrayOfString[n])) {
                k = 1;
              }
            }
          }
        }
        if (k == 0) {
          fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_ATT_DEFAULT_INVALID", new Object[] { paramString2, str3 }, (short)1);
        }
      }
    }
    if (fDTDGrammar != null) {
      fDTDGrammar.attributeDecl(paramString1, paramString2, paramString3, paramArrayOfString, paramString4, paramXMLString1, paramXMLString2, paramAugmentations);
    }
    if (fDTDHandler != null) {
      fDTDHandler.attributeDecl(paramString1, paramString2, paramString3, paramArrayOfString, paramString4, paramXMLString1, paramXMLString2, paramAugmentations);
    }
  }
  
  public void endAttlist(Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDTDGrammar != null) {
      fDTDGrammar.endAttlist(paramAugmentations);
    }
    if (fDTDHandler != null) {
      fDTDHandler.endAttlist(paramAugmentations);
    }
  }
  
  public void internalEntityDecl(String paramString, XMLString paramXMLString1, XMLString paramXMLString2, Augmentations paramAugmentations)
    throws XNIException
  {
    DTDGrammar localDTDGrammar = fDTDGrammar != null ? fDTDGrammar : fGrammarBucket.getActiveGrammar();
    int i = localDTDGrammar.getEntityDeclIndex(paramString);
    if (i == -1)
    {
      if (fDTDGrammar != null) {
        fDTDGrammar.internalEntityDecl(paramString, paramXMLString1, paramXMLString2, paramAugmentations);
      }
      if (fDTDHandler != null) {
        fDTDHandler.internalEntityDecl(paramString, paramXMLString1, paramXMLString2, paramAugmentations);
      }
    }
  }
  
  public void externalEntityDecl(String paramString, XMLResourceIdentifier paramXMLResourceIdentifier, Augmentations paramAugmentations)
    throws XNIException
  {
    DTDGrammar localDTDGrammar = fDTDGrammar != null ? fDTDGrammar : fGrammarBucket.getActiveGrammar();
    int i = localDTDGrammar.getEntityDeclIndex(paramString);
    if (i == -1)
    {
      if (fDTDGrammar != null) {
        fDTDGrammar.externalEntityDecl(paramString, paramXMLResourceIdentifier, paramAugmentations);
      }
      if (fDTDHandler != null) {
        fDTDHandler.externalEntityDecl(paramString, paramXMLResourceIdentifier, paramAugmentations);
      }
    }
  }
  
  public void unparsedEntityDecl(String paramString1, XMLResourceIdentifier paramXMLResourceIdentifier, String paramString2, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fValidation) {
      fNDataDeclNotations.put(paramString1, paramString2);
    }
    if (fDTDGrammar != null) {
      fDTDGrammar.unparsedEntityDecl(paramString1, paramXMLResourceIdentifier, paramString2, paramAugmentations);
    }
    if (fDTDHandler != null) {
      fDTDHandler.unparsedEntityDecl(paramString1, paramXMLResourceIdentifier, paramString2, paramAugmentations);
    }
  }
  
  public void notationDecl(String paramString, XMLResourceIdentifier paramXMLResourceIdentifier, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fValidation)
    {
      DTDGrammar localDTDGrammar = fDTDGrammar != null ? fDTDGrammar : fGrammarBucket.getActiveGrammar();
      if (localDTDGrammar.getNotationDeclIndex(paramString) != -1) {
        fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "UniqueNotationName", new Object[] { paramString }, (short)1);
      }
    }
    if (fDTDGrammar != null) {
      fDTDGrammar.notationDecl(paramString, paramXMLResourceIdentifier, paramAugmentations);
    }
    if (fDTDHandler != null) {
      fDTDHandler.notationDecl(paramString, paramXMLResourceIdentifier, paramAugmentations);
    }
  }
  
  public void startConditional(short paramShort, Augmentations paramAugmentations)
    throws XNIException
  {
    fInDTDIgnore = (paramShort == 1);
    if (fDTDGrammar != null) {
      fDTDGrammar.startConditional(paramShort, paramAugmentations);
    }
    if (fDTDHandler != null) {
      fDTDHandler.startConditional(paramShort, paramAugmentations);
    }
  }
  
  public void endConditional(Augmentations paramAugmentations)
    throws XNIException
  {
    fInDTDIgnore = false;
    if (fDTDGrammar != null) {
      fDTDGrammar.endConditional(paramAugmentations);
    }
    if (fDTDHandler != null) {
      fDTDHandler.endConditional(paramAugmentations);
    }
  }
  
  public void endDTD(Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDTDGrammar != null)
    {
      fDTDGrammar.endDTD(paramAugmentations);
      if (fGrammarPool != null) {
        fGrammarPool.cacheGrammars("http://www.w3.org/TR/REC-xml", new Grammar[] { fDTDGrammar });
      }
    }
    if (fValidation)
    {
      DTDGrammar localDTDGrammar = fDTDGrammar != null ? fDTDGrammar : fGrammarBucket.getActiveGrammar();
      Iterator localIterator = fNDataDeclNotations.entrySet().iterator();
      Object localObject3;
      while (localIterator.hasNext())
      {
        localObject1 = (Map.Entry)localIterator.next();
        localObject2 = (String)((Map.Entry)localObject1).getValue();
        if (localDTDGrammar.getNotationDeclIndex((String)localObject2) == -1)
        {
          localObject3 = (String)((Map.Entry)localObject1).getKey();
          fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_NOTATION_NOT_DECLARED_FOR_UNPARSED_ENTITYDECL", new Object[] { localObject3, localObject2 }, (short)1);
        }
      }
      Object localObject1 = fNotationEnumVals.entrySet().iterator();
      String str1;
      while (((Iterator)localObject1).hasNext())
      {
        localObject2 = (Map.Entry)((Iterator)localObject1).next();
        localObject3 = (String)((Map.Entry)localObject2).getKey();
        if (localDTDGrammar.getNotationDeclIndex((String)localObject3) == -1)
        {
          str1 = (String)((Map.Entry)localObject2).getValue();
          fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_NOTATION_NOT_DECLARED_FOR_NOTATIONTYPE_ATTRIBUTE", new Object[] { str1, localObject3 }, (short)1);
        }
      }
      Object localObject2 = fTableOfNOTATIONAttributeNames.entrySet().iterator();
      while (((Iterator)localObject2).hasNext())
      {
        localObject3 = (Map.Entry)((Iterator)localObject2).next();
        str1 = (String)((Map.Entry)localObject3).getKey();
        int i = localDTDGrammar.getElementDeclIndex(str1);
        if (localDTDGrammar.getContentSpecType(i) == 1)
        {
          String str2 = (String)((Map.Entry)localObject3).getValue();
          fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "NoNotationOnEmptyElement", new Object[] { str1, str2 }, (short)1);
        }
      }
      fTableOfIDAttributeNames = null;
      fTableOfNOTATIONAttributeNames = null;
      if (fWarnOnUndeclaredElemdef) {
        checkDeclaredElements(localDTDGrammar);
      }
    }
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
    if (fValidation)
    {
      fDTDElementDeclName = paramString;
      fMixedElementTypes.clear();
    }
    if (fDTDGrammar != null) {
      fDTDGrammar.startContentModel(paramString, paramAugmentations);
    }
    if (fDTDContentModelHandler != null) {
      fDTDContentModelHandler.startContentModel(paramString, paramAugmentations);
    }
  }
  
  public void any(Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDTDGrammar != null) {
      fDTDGrammar.any(paramAugmentations);
    }
    if (fDTDContentModelHandler != null) {
      fDTDContentModelHandler.any(paramAugmentations);
    }
  }
  
  public void empty(Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDTDGrammar != null) {
      fDTDGrammar.empty(paramAugmentations);
    }
    if (fDTDContentModelHandler != null) {
      fDTDContentModelHandler.empty(paramAugmentations);
    }
  }
  
  public void startGroup(Augmentations paramAugmentations)
    throws XNIException
  {
    fMixed = false;
    if (fDTDGrammar != null) {
      fDTDGrammar.startGroup(paramAugmentations);
    }
    if (fDTDContentModelHandler != null) {
      fDTDContentModelHandler.startGroup(paramAugmentations);
    }
  }
  
  public void pcdata(Augmentations paramAugmentations)
  {
    fMixed = true;
    if (fDTDGrammar != null) {
      fDTDGrammar.pcdata(paramAugmentations);
    }
    if (fDTDContentModelHandler != null) {
      fDTDContentModelHandler.pcdata(paramAugmentations);
    }
  }
  
  public void element(String paramString, Augmentations paramAugmentations)
    throws XNIException
  {
    if ((fMixed) && (fValidation)) {
      if (fMixedElementTypes.contains(paramString)) {
        fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "DuplicateTypeInMixedContent", new Object[] { fDTDElementDeclName, paramString }, (short)1);
      } else {
        fMixedElementTypes.add(paramString);
      }
    }
    if (fDTDGrammar != null) {
      fDTDGrammar.element(paramString, paramAugmentations);
    }
    if (fDTDContentModelHandler != null) {
      fDTDContentModelHandler.element(paramString, paramAugmentations);
    }
  }
  
  public void separator(short paramShort, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDTDGrammar != null) {
      fDTDGrammar.separator(paramShort, paramAugmentations);
    }
    if (fDTDContentModelHandler != null) {
      fDTDContentModelHandler.separator(paramShort, paramAugmentations);
    }
  }
  
  public void occurrence(short paramShort, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDTDGrammar != null) {
      fDTDGrammar.occurrence(paramShort, paramAugmentations);
    }
    if (fDTDContentModelHandler != null) {
      fDTDContentModelHandler.occurrence(paramShort, paramAugmentations);
    }
  }
  
  public void endGroup(Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDTDGrammar != null) {
      fDTDGrammar.endGroup(paramAugmentations);
    }
    if (fDTDContentModelHandler != null) {
      fDTDContentModelHandler.endGroup(paramAugmentations);
    }
  }
  
  public void endContentModel(Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDTDGrammar != null) {
      fDTDGrammar.endContentModel(paramAugmentations);
    }
    if (fDTDContentModelHandler != null) {
      fDTDContentModelHandler.endContentModel(paramAugmentations);
    }
  }
  
  private boolean normalizeDefaultAttrValue(XMLString paramXMLString)
  {
    int i = 1;
    int j = offset;
    int k = offset + length;
    for (int m = offset; m < k; m++) {
      if (ch[m] == ' ')
      {
        if (i == 0)
        {
          ch[(j++)] = ' ';
          i = 1;
        }
      }
      else
      {
        if (j != m) {
          ch[j] = ch[m];
        }
        j++;
        i = 0;
      }
    }
    if (j != k)
    {
      if (i != 0) {
        j--;
      }
      length = (j - offset);
      return true;
    }
    return false;
  }
  
  protected boolean isValidNmtoken(String paramString)
  {
    return XMLChar.isValidNmtoken(paramString);
  }
  
  protected boolean isValidName(String paramString)
  {
    return XMLChar.isValidName(paramString);
  }
  
  private void checkDeclaredElements(DTDGrammar paramDTDGrammar)
  {
    int i = paramDTDGrammar.getFirstElementDeclIndex();
    XMLContentSpec localXMLContentSpec = new XMLContentSpec();
    while (i >= 0)
    {
      int j = paramDTDGrammar.getContentSpecType(i);
      if ((j == 3) || (j == 2)) {
        checkDeclaredElements(paramDTDGrammar, i, paramDTDGrammar.getContentSpecIndex(i), localXMLContentSpec);
      }
      i = paramDTDGrammar.getNextElementDeclIndex(i);
    }
  }
  
  private void checkDeclaredElements(DTDGrammar paramDTDGrammar, int paramInt1, int paramInt2, XMLContentSpec paramXMLContentSpec)
  {
    paramDTDGrammar.getContentSpec(paramInt2, paramXMLContentSpec);
    if (type == 0)
    {
      String str = (String)value;
      if ((str != null) && (paramDTDGrammar.getElementDeclIndex(str) == -1)) {
        fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "UndeclaredElementInContentSpec", new Object[] { getElementDeclNamerawname, str }, (short)0);
      }
    }
    else
    {
      int i;
      if ((type == 4) || (type == 5))
      {
        i = ((int[])value)[0];
        int j = ((int[])otherValue)[0];
        checkDeclaredElements(paramDTDGrammar, paramInt1, i, paramXMLContentSpec);
        checkDeclaredElements(paramDTDGrammar, paramInt1, j, paramXMLContentSpec);
      }
      else if ((type == 2) || (type == 1) || (type == 3))
      {
        i = ((int[])value)[0];
        checkDeclaredElements(paramDTDGrammar, paramInt1, i, paramXMLContentSpec);
      }
    }
  }
}
