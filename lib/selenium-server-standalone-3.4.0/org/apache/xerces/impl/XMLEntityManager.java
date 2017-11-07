package org.apache.xerces.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;
import org.apache.xerces.impl.io.ASCIIReader;
import org.apache.xerces.impl.io.Latin1Reader;
import org.apache.xerces.impl.io.UCSReader;
import org.apache.xerces.impl.io.UTF16Reader;
import org.apache.xerces.impl.io.UTF8Reader;
import org.apache.xerces.impl.validation.ValidationManager;
import org.apache.xerces.util.AugmentationsImpl;
import org.apache.xerces.util.EncodingMap;
import org.apache.xerces.util.HTTPInputSource;
import org.apache.xerces.util.SecurityManager;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.URI;
import org.apache.xerces.util.URI.MalformedURIException;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.util.XMLEntityDescriptionImpl;
import org.apache.xerces.util.XMLResourceIdentifierImpl;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLComponent;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLInputSource;

public class XMLEntityManager
  implements XMLComponent, XMLEntityResolver
{
  public static final int DEFAULT_BUFFER_SIZE = 2048;
  public static final int DEFAULT_XMLDECL_BUFFER_SIZE = 64;
  public static final int DEFAULT_INTERNAL_BUFFER_SIZE = 512;
  protected static final String VALIDATION = "http://xml.org/sax/features/validation";
  protected static final String EXTERNAL_GENERAL_ENTITIES = "http://xml.org/sax/features/external-general-entities";
  protected static final String EXTERNAL_PARAMETER_ENTITIES = "http://xml.org/sax/features/external-parameter-entities";
  protected static final String ALLOW_JAVA_ENCODINGS = "http://apache.org/xml/features/allow-java-encodings";
  protected static final String WARN_ON_DUPLICATE_ENTITYDEF = "http://apache.org/xml/features/warn-on-duplicate-entitydef";
  protected static final String STANDARD_URI_CONFORMANT = "http://apache.org/xml/features/standard-uri-conformant";
  protected static final String PARSER_SETTINGS = "http://apache.org/xml/features/internal/parser-settings";
  protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
  protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
  protected static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
  protected static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
  protected static final String BUFFER_SIZE = "http://apache.org/xml/properties/input-buffer-size";
  protected static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
  private static final String[] RECOGNIZED_FEATURES = { "http://xml.org/sax/features/validation", "http://xml.org/sax/features/external-general-entities", "http://xml.org/sax/features/external-parameter-entities", "http://apache.org/xml/features/allow-java-encodings", "http://apache.org/xml/features/warn-on-duplicate-entitydef", "http://apache.org/xml/features/standard-uri-conformant" };
  private static final Boolean[] FEATURE_DEFAULTS = { null, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE };
  private static final String[] RECOGNIZED_PROPERTIES = { "http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/entity-resolver", "http://apache.org/xml/properties/internal/validation-manager", "http://apache.org/xml/properties/input-buffer-size", "http://apache.org/xml/properties/security-manager" };
  private static final Object[] PROPERTY_DEFAULTS = { null, null, null, null, new Integer(2048), null };
  private static final String XMLEntity = "[xml]".intern();
  private static final String DTDEntity = "[dtd]".intern();
  private static final boolean DEBUG_BUFFER = false;
  private static final boolean DEBUG_ENTITIES = false;
  private static final boolean DEBUG_ENCODINGS = false;
  private static final boolean DEBUG_RESOLVER = false;
  protected boolean fValidation;
  protected boolean fExternalGeneralEntities = true;
  protected boolean fExternalParameterEntities = true;
  protected boolean fAllowJavaEncodings;
  protected boolean fWarnDuplicateEntityDef;
  protected boolean fStrictURI;
  protected SymbolTable fSymbolTable;
  protected XMLErrorReporter fErrorReporter;
  protected XMLEntityResolver fEntityResolver;
  protected ValidationManager fValidationManager;
  protected int fBufferSize = 2048;
  protected SecurityManager fSecurityManager = null;
  protected boolean fStandalone;
  protected boolean fHasPEReferences;
  protected boolean fInExternalSubset = false;
  protected XMLEntityHandler fEntityHandler;
  protected XMLEntityScanner fEntityScanner;
  protected XMLEntityScanner fXML10EntityScanner;
  protected XMLEntityScanner fXML11EntityScanner;
  protected int fEntityExpansionLimit = 0;
  protected int fEntityExpansionCount = 0;
  protected final Hashtable fEntities = new Hashtable();
  protected final Stack fEntityStack = new Stack();
  protected ScannedEntity fCurrentEntity;
  protected Hashtable fDeclaredEntities;
  private final XMLResourceIdentifierImpl fResourceIdentifier = new XMLResourceIdentifierImpl();
  private final Augmentations fEntityAugs = new AugmentationsImpl();
  private final ByteBufferPool fSmallByteBufferPool = new ByteBufferPool(fBufferSize);
  private final ByteBufferPool fLargeByteBufferPool = new ByteBufferPool(fBufferSize << 1);
  private byte[] fTempByteBuffer = null;
  private final CharacterBufferPool fCharacterBufferPool = new CharacterBufferPool(fBufferSize, 512);
  protected Stack fReaderStack = new Stack();
  private static String gUserDir;
  private static URI gUserDirURI;
  private static final boolean[] gNeedEscaping = new boolean[''];
  private static final char[] gAfterEscaping1 = new char[''];
  private static final char[] gAfterEscaping2 = new char[''];
  private static final char[] gHexChs = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
  private static PrivilegedAction GET_USER_DIR_SYSTEM_PROPERTY = new PrivilegedAction()
  {
    public Object run()
    {
      return System.getProperty("user.dir");
    }
  };
  
  public XMLEntityManager()
  {
    this(null);
  }
  
  public XMLEntityManager(XMLEntityManager paramXMLEntityManager)
  {
    fDeclaredEntities = (paramXMLEntityManager != null ? paramXMLEntityManager.getDeclaredEntities() : null);
    setScannerVersion((short)1);
  }
  
  public void setStandalone(boolean paramBoolean)
  {
    fStandalone = paramBoolean;
  }
  
  public boolean isStandalone()
  {
    return fStandalone;
  }
  
  final void notifyHasPEReferences()
  {
    fHasPEReferences = true;
  }
  
  final boolean hasPEReferences()
  {
    return fHasPEReferences;
  }
  
  public void setEntityHandler(XMLEntityHandler paramXMLEntityHandler)
  {
    fEntityHandler = paramXMLEntityHandler;
  }
  
  public XMLResourceIdentifier getCurrentResourceIdentifier()
  {
    return fResourceIdentifier;
  }
  
  public ScannedEntity getCurrentEntity()
  {
    return fCurrentEntity;
  }
  
  public void addInternalEntity(String paramString1, String paramString2)
  {
    if (!fEntities.containsKey(paramString1))
    {
      InternalEntity localInternalEntity = new InternalEntity(paramString1, paramString2, fInExternalSubset);
      fEntities.put(paramString1, localInternalEntity);
    }
    else if (fWarnDuplicateEntityDef)
    {
      fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_DUPLICATE_ENTITY_DEFINITION", new Object[] { paramString1 }, (short)0);
    }
  }
  
  public void addExternalEntity(String paramString1, String paramString2, String paramString3, String paramString4)
    throws IOException
  {
    if (!fEntities.containsKey(paramString1))
    {
      if (paramString4 == null)
      {
        int i = fEntityStack.size();
        if ((i == 0) && (fCurrentEntity != null) && (fCurrentEntity.entityLocation != null)) {
          paramString4 = fCurrentEntity.entityLocation.getExpandedSystemId();
        }
        for (int j = i - 1; j >= 0; j--)
        {
          ScannedEntity localScannedEntity = (ScannedEntity)fEntityStack.elementAt(j);
          if ((entityLocation != null) && (entityLocation.getExpandedSystemId() != null))
          {
            paramString4 = entityLocation.getExpandedSystemId();
            break;
          }
        }
      }
      ExternalEntity localExternalEntity = new ExternalEntity(paramString1, new XMLEntityDescriptionImpl(paramString1, paramString2, paramString3, paramString4, expandSystemId(paramString3, paramString4, false)), null, fInExternalSubset);
      fEntities.put(paramString1, localExternalEntity);
    }
    else if (fWarnDuplicateEntityDef)
    {
      fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_DUPLICATE_ENTITY_DEFINITION", new Object[] { paramString1 }, (short)0);
    }
  }
  
  public boolean isExternalEntity(String paramString)
  {
    Entity localEntity = (Entity)fEntities.get(paramString);
    if (localEntity == null) {
      return false;
    }
    return localEntity.isExternal();
  }
  
  public boolean isEntityDeclInExternalSubset(String paramString)
  {
    Entity localEntity = (Entity)fEntities.get(paramString);
    if (localEntity == null) {
      return false;
    }
    return localEntity.isEntityDeclInExternalSubset();
  }
  
  public void addUnparsedEntity(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
  {
    if (!fEntities.containsKey(paramString1))
    {
      ExternalEntity localExternalEntity = new ExternalEntity(paramString1, new XMLEntityDescriptionImpl(paramString1, paramString2, paramString3, paramString4, null), paramString5, fInExternalSubset);
      fEntities.put(paramString1, localExternalEntity);
    }
    else if (fWarnDuplicateEntityDef)
    {
      fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_DUPLICATE_ENTITY_DEFINITION", new Object[] { paramString1 }, (short)0);
    }
  }
  
  public boolean isUnparsedEntity(String paramString)
  {
    Entity localEntity = (Entity)fEntities.get(paramString);
    if (localEntity == null) {
      return false;
    }
    return localEntity.isUnparsed();
  }
  
  public boolean isDeclaredEntity(String paramString)
  {
    Entity localEntity = (Entity)fEntities.get(paramString);
    return localEntity != null;
  }
  
  public XMLInputSource resolveEntity(XMLResourceIdentifier paramXMLResourceIdentifier)
    throws IOException, XNIException
  {
    if (paramXMLResourceIdentifier == null) {
      return null;
    }
    String str1 = paramXMLResourceIdentifier.getPublicId();
    String str2 = paramXMLResourceIdentifier.getLiteralSystemId();
    String str3 = paramXMLResourceIdentifier.getBaseSystemId();
    String str4 = paramXMLResourceIdentifier.getExpandedSystemId();
    int i = str4 == null ? 1 : 0;
    if ((str3 == null) && (fCurrentEntity != null) && (fCurrentEntity.entityLocation != null))
    {
      str3 = fCurrentEntity.entityLocation.getExpandedSystemId();
      if (str3 != null) {
        i = 1;
      }
    }
    XMLInputSource localXMLInputSource = null;
    if (fEntityResolver != null)
    {
      if (i != 0) {
        str4 = expandSystemId(str2, str3, false);
      }
      paramXMLResourceIdentifier.setBaseSystemId(str3);
      paramXMLResourceIdentifier.setExpandedSystemId(str4);
      localXMLInputSource = fEntityResolver.resolveEntity(paramXMLResourceIdentifier);
    }
    if (localXMLInputSource == null) {
      localXMLInputSource = new XMLInputSource(str1, str2, str3);
    }
    return localXMLInputSource;
  }
  
  public void startEntity(String paramString, boolean paramBoolean)
    throws IOException, XNIException
  {
    Entity localEntity = (Entity)fEntities.get(paramString);
    if (localEntity == null)
    {
      if (fEntityHandler != null)
      {
        String str1 = null;
        fResourceIdentifier.clear();
        fEntityAugs.removeAllItems();
        fEntityAugs.putItem("ENTITY_SKIPPED", Boolean.TRUE);
        fEntityHandler.startEntity(paramString, fResourceIdentifier, str1, fEntityAugs);
        fEntityAugs.removeAllItems();
        fEntityAugs.putItem("ENTITY_SKIPPED", Boolean.TRUE);
        fEntityHandler.endEntity(paramString, fEntityAugs);
      }
      return;
    }
    boolean bool1 = localEntity.isExternal();
    Object localObject2;
    String str2;
    Object localObject3;
    String str3;
    if ((bool1) && ((fValidationManager == null) || (!fValidationManager.isCachedDTD())))
    {
      boolean bool2 = localEntity.isUnparsed();
      j = paramString.startsWith("%");
      int k = j == 0 ? 1 : 0;
      if ((bool2) || ((k != 0) && (!fExternalGeneralEntities)) || ((j != 0) && (!fExternalParameterEntities)))
      {
        if (fEntityHandler != null)
        {
          fResourceIdentifier.clear();
          localObject2 = null;
          ExternalEntity localExternalEntity = (ExternalEntity)localEntity;
          str2 = entityLocation != null ? entityLocation.getLiteralSystemId() : null;
          localObject3 = entityLocation != null ? entityLocation.getBaseSystemId() : null;
          str3 = expandSystemId(str2, (String)localObject3, false);
          fResourceIdentifier.setValues(entityLocation != null ? entityLocation.getPublicId() : null, str2, (String)localObject3, str3);
          fEntityAugs.removeAllItems();
          fEntityAugs.putItem("ENTITY_SKIPPED", Boolean.TRUE);
          fEntityHandler.startEntity(paramString, fResourceIdentifier, (String)localObject2, fEntityAugs);
          fEntityAugs.removeAllItems();
          fEntityAugs.putItem("ENTITY_SKIPPED", Boolean.TRUE);
          fEntityHandler.endEntity(paramString, fEntityAugs);
        }
        return;
      }
    }
    int i = fEntityStack.size();
    for (int j = i; j >= 0; j--)
    {
      localObject1 = j == i ? fCurrentEntity : (Entity)fEntityStack.elementAt(j);
      if (name == paramString)
      {
        localObject2 = new StringBuffer(paramString);
        for (int m = j + 1; m < i; m++)
        {
          localObject1 = (Entity)fEntityStack.elementAt(m);
          ((StringBuffer)localObject2).append(" -> ");
          ((StringBuffer)localObject2).append(name);
        }
        ((StringBuffer)localObject2).append(" -> ");
        ((StringBuffer)localObject2).append(fCurrentEntity.name);
        ((StringBuffer)localObject2).append(" -> ");
        ((StringBuffer)localObject2).append(paramString);
        fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "RecursiveReference", new Object[] { paramString, ((StringBuffer)localObject2).toString() }, (short)2);
        if (fEntityHandler != null)
        {
          fResourceIdentifier.clear();
          str2 = null;
          if (bool1)
          {
            localObject3 = (ExternalEntity)localEntity;
            str3 = entityLocation != null ? entityLocation.getLiteralSystemId() : null;
            String str4 = entityLocation != null ? entityLocation.getBaseSystemId() : null;
            String str5 = expandSystemId(str3, str4, false);
            fResourceIdentifier.setValues(entityLocation != null ? entityLocation.getPublicId() : null, str3, str4, str5);
          }
          fEntityAugs.removeAllItems();
          fEntityAugs.putItem("ENTITY_SKIPPED", Boolean.TRUE);
          fEntityHandler.startEntity(paramString, fResourceIdentifier, str2, fEntityAugs);
          fEntityAugs.removeAllItems();
          fEntityAugs.putItem("ENTITY_SKIPPED", Boolean.TRUE);
          fEntityHandler.endEntity(paramString, fEntityAugs);
        }
        return;
      }
    }
    Object localObject1 = null;
    if (bool1)
    {
      localObject2 = (ExternalEntity)localEntity;
      localObject1 = resolveEntity(entityLocation);
    }
    else
    {
      localObject2 = (InternalEntity)localEntity;
      StringReader localStringReader = new StringReader(text);
      localObject1 = new XMLInputSource(null, null, null, localStringReader, null);
    }
    startEntity(paramString, (XMLInputSource)localObject1, paramBoolean, bool1);
  }
  
  public void startDocumentEntity(XMLInputSource paramXMLInputSource)
    throws IOException, XNIException
  {
    startEntity(XMLEntity, paramXMLInputSource, false, true);
  }
  
  public void startDTDEntity(XMLInputSource paramXMLInputSource)
    throws IOException, XNIException
  {
    startEntity(DTDEntity, paramXMLInputSource, false, true);
  }
  
  public void startExternalSubset()
  {
    fInExternalSubset = true;
  }
  
  public void endExternalSubset()
  {
    fInExternalSubset = false;
  }
  
  public void startEntity(String paramString, XMLInputSource paramXMLInputSource, boolean paramBoolean1, boolean paramBoolean2)
    throws IOException, XNIException
  {
    String str = setupCurrentEntity(paramString, paramXMLInputSource, paramBoolean1, paramBoolean2);
    if ((fSecurityManager != null) && (fEntityExpansionCount++ > fEntityExpansionLimit))
    {
      fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "EntityExpansionLimitExceeded", new Object[] { new Integer(fEntityExpansionLimit) }, (short)2);
      fEntityExpansionCount = 0;
    }
    if (fEntityHandler != null) {
      fEntityHandler.startEntity(paramString, fResourceIdentifier, str, null);
    }
  }
  
  public String setupCurrentEntity(String paramString, XMLInputSource paramXMLInputSource, boolean paramBoolean1, boolean paramBoolean2)
    throws IOException, XNIException
  {
    String str1 = paramXMLInputSource.getPublicId();
    Object localObject1 = paramXMLInputSource.getSystemId();
    Object localObject2 = paramXMLInputSource.getBaseSystemId();
    String str2 = paramXMLInputSource.getEncoding();
    boolean bool = str2 != null;
    Boolean localBoolean = null;
    fTempByteBuffer = null;
    Object localObject3 = null;
    Reader localReader = paramXMLInputSource.getCharacterStream();
    Object localObject4 = expandSystemId((String)localObject1, (String)localObject2, fStrictURI);
    if (localObject2 == null) {
      localObject2 = localObject4;
    }
    if (localReader == null)
    {
      localObject3 = paramXMLInputSource.getByteStream();
      Object localObject6;
      int i;
      Object localObject7;
      if (localObject3 == null)
      {
        localObject5 = new URL((String)localObject4);
        localObject6 = ((URL)localObject5).openConnection();
        if (!(localObject6 instanceof HttpURLConnection))
        {
          localObject3 = ((URLConnection)localObject6).getInputStream();
        }
        else
        {
          i = 1;
          if ((paramXMLInputSource instanceof HTTPInputSource))
          {
            localObject7 = (HttpURLConnection)localObject6;
            HTTPInputSource localHTTPInputSource = (HTTPInputSource)paramXMLInputSource;
            Iterator localIterator = localHTTPInputSource.getHTTPRequestProperties();
            while (localIterator.hasNext())
            {
              Map.Entry localEntry = (Map.Entry)localIterator.next();
              ((HttpURLConnection)localObject7).setRequestProperty((String)localEntry.getKey(), (String)localEntry.getValue());
            }
            i = localHTTPInputSource.getFollowHTTPRedirects();
            if (i == 0) {
              ((HttpURLConnection)localObject7).setInstanceFollowRedirects(i);
            }
          }
          localObject3 = ((URLConnection)localObject6).getInputStream();
          if (i != 0)
          {
            localObject7 = ((URLConnection)localObject6).getURL().toString();
            if (!((String)localObject7).equals(localObject4))
            {
              localObject1 = localObject7;
              localObject4 = localObject7;
            }
          }
        }
      }
      Object localObject5 = new RewindableInputStream((InputStream)localObject3);
      localObject3 = localObject5;
      if (str2 == null)
      {
        localObject6 = new byte[4];
        for (i = 0; i < 4; i++) {
          localObject6[i] = ((byte)((RewindableInputStream)localObject5).readAndBuffer());
        }
        if (i == 4)
        {
          localObject7 = getEncodingInfo((byte[])localObject6, i);
          str2 = encoding;
          localBoolean = isBigEndian;
          ((InputStream)localObject3).reset();
          if (hasBOM) {
            if (str2 == "UTF-8") {
              ((InputStream)localObject3).skip(3L);
            } else if (str2 == "UTF-16") {
              ((InputStream)localObject3).skip(2L);
            }
          }
          localReader = createReader((InputStream)localObject3, str2, localBoolean);
        }
        else
        {
          localReader = createReader((InputStream)localObject3, str2, localBoolean);
        }
      }
      else
      {
        str2 = str2.toUpperCase(Locale.ENGLISH);
        int j;
        if (str2.equals("UTF-8"))
        {
          localObject6 = new int[3];
          for (j = 0; j < 3; j++)
          {
            localObject6[j] = ((RewindableInputStream)localObject5).readAndBuffer();
            if (localObject6[j] == -1) {
              break;
            }
          }
          if (j == 3)
          {
            if ((localObject6[0] != 239) || (localObject6[1] != 187) || (localObject6[2] != 191)) {
              ((InputStream)localObject3).reset();
            }
          }
          else {
            ((InputStream)localObject3).reset();
          }
          localReader = createReader((InputStream)localObject3, "UTF-8", localBoolean);
        }
        else if (str2.equals("UTF-16"))
        {
          localObject6 = new int[4];
          for (j = 0; j < 4; j++)
          {
            localObject6[j] = ((RewindableInputStream)localObject5).readAndBuffer();
            if (localObject6[j] == -1) {
              break;
            }
          }
          ((InputStream)localObject3).reset();
          if (j >= 2)
          {
            int k = localObject6[0];
            int m = localObject6[1];
            if ((k == 254) && (m == 255))
            {
              localBoolean = Boolean.TRUE;
              ((InputStream)localObject3).skip(2L);
            }
            else if ((k == 255) && (m == 254))
            {
              localBoolean = Boolean.FALSE;
              ((InputStream)localObject3).skip(2L);
            }
            else if (j == 4)
            {
              int n = localObject6[2];
              int i1 = localObject6[3];
              if ((k == 0) && (m == 60) && (n == 0) && (i1 == 63)) {
                localBoolean = Boolean.TRUE;
              }
              if ((k == 60) && (m == 0) && (n == 63) && (i1 == 0)) {
                localBoolean = Boolean.FALSE;
              }
            }
          }
          localReader = createReader((InputStream)localObject3, "UTF-16", localBoolean);
        }
        else if (str2.equals("ISO-10646-UCS-4"))
        {
          localObject6 = new int[4];
          for (j = 0; j < 4; j++)
          {
            localObject6[j] = ((RewindableInputStream)localObject5).readAndBuffer();
            if (localObject6[j] == -1) {
              break;
            }
          }
          ((InputStream)localObject3).reset();
          if (j == 4) {
            if ((localObject6[0] == 0) && (localObject6[1] == 0) && (localObject6[2] == 0) && (localObject6[3] == 60)) {
              localBoolean = Boolean.TRUE;
            } else if ((localObject6[0] == 60) && (localObject6[1] == 0) && (localObject6[2] == 0) && (localObject6[3] == 0)) {
              localBoolean = Boolean.FALSE;
            }
          }
          localReader = createReader((InputStream)localObject3, str2, localBoolean);
        }
        else if (str2.equals("ISO-10646-UCS-2"))
        {
          localObject6 = new int[4];
          for (j = 0; j < 4; j++)
          {
            localObject6[j] = ((RewindableInputStream)localObject5).readAndBuffer();
            if (localObject6[j] == -1) {
              break;
            }
          }
          ((InputStream)localObject3).reset();
          if (j == 4) {
            if ((localObject6[0] == 0) && (localObject6[1] == 60) && (localObject6[2] == 0) && (localObject6[3] == 63)) {
              localBoolean = Boolean.TRUE;
            } else if ((localObject6[0] == 60) && (localObject6[1] == 0) && (localObject6[2] == 63) && (localObject6[3] == 0)) {
              localBoolean = Boolean.FALSE;
            }
          }
          localReader = createReader((InputStream)localObject3, str2, localBoolean);
        }
        else
        {
          localReader = createReader((InputStream)localObject3, str2, localBoolean);
        }
      }
    }
    fReaderStack.push(localReader);
    if (fCurrentEntity != null) {
      fEntityStack.push(fCurrentEntity);
    }
    fCurrentEntity = new ScannedEntity(paramString, new XMLResourceIdentifierImpl(str1, (String)localObject1, (String)localObject2, (String)localObject4), (InputStream)localObject3, localReader, fTempByteBuffer, str2, paramBoolean1, false, paramBoolean2);
    fCurrentEntity.setEncodingExternallySpecified(bool);
    fEntityScanner.setCurrentEntity(fCurrentEntity);
    fResourceIdentifier.setValues(str1, (String)localObject1, (String)localObject2, (String)localObject4);
    return str2;
  }
  
  public void setScannerVersion(short paramShort)
  {
    if (paramShort == 1)
    {
      if (fXML10EntityScanner == null) {
        fXML10EntityScanner = new XMLEntityScanner();
      }
      fXML10EntityScanner.reset(fSymbolTable, this, fErrorReporter);
      fEntityScanner = fXML10EntityScanner;
      fEntityScanner.setCurrentEntity(fCurrentEntity);
    }
    else
    {
      if (fXML11EntityScanner == null) {
        fXML11EntityScanner = new XML11EntityScanner();
      }
      fXML11EntityScanner.reset(fSymbolTable, this, fErrorReporter);
      fEntityScanner = fXML11EntityScanner;
      fEntityScanner.setCurrentEntity(fCurrentEntity);
    }
  }
  
  public XMLEntityScanner getEntityScanner()
  {
    if (fEntityScanner == null)
    {
      if (fXML10EntityScanner == null) {
        fXML10EntityScanner = new XMLEntityScanner();
      }
      fXML10EntityScanner.reset(fSymbolTable, this, fErrorReporter);
      fEntityScanner = fXML10EntityScanner;
    }
    return fEntityScanner;
  }
  
  public void closeReaders()
  {
    for (int i = fReaderStack.size() - 1; i >= 0; i--) {
      try
      {
        ((Reader)fReaderStack.pop()).close();
      }
      catch (IOException localIOException) {}
    }
  }
  
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
      fExternalGeneralEntities = paramXMLComponentManager.getFeature("http://xml.org/sax/features/external-general-entities");
    }
    catch (XMLConfigurationException localXMLConfigurationException3)
    {
      fExternalGeneralEntities = true;
    }
    try
    {
      fExternalParameterEntities = paramXMLComponentManager.getFeature("http://xml.org/sax/features/external-parameter-entities");
    }
    catch (XMLConfigurationException localXMLConfigurationException4)
    {
      fExternalParameterEntities = true;
    }
    try
    {
      fAllowJavaEncodings = paramXMLComponentManager.getFeature("http://apache.org/xml/features/allow-java-encodings");
    }
    catch (XMLConfigurationException localXMLConfigurationException5)
    {
      fAllowJavaEncodings = false;
    }
    try
    {
      fWarnDuplicateEntityDef = paramXMLComponentManager.getFeature("http://apache.org/xml/features/warn-on-duplicate-entitydef");
    }
    catch (XMLConfigurationException localXMLConfigurationException6)
    {
      fWarnDuplicateEntityDef = false;
    }
    try
    {
      fStrictURI = paramXMLComponentManager.getFeature("http://apache.org/xml/features/standard-uri-conformant");
    }
    catch (XMLConfigurationException localXMLConfigurationException7)
    {
      fStrictURI = false;
    }
    fSymbolTable = ((SymbolTable)paramXMLComponentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table"));
    fErrorReporter = ((XMLErrorReporter)paramXMLComponentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter"));
    try
    {
      fEntityResolver = ((XMLEntityResolver)paramXMLComponentManager.getProperty("http://apache.org/xml/properties/internal/entity-resolver"));
    }
    catch (XMLConfigurationException localXMLConfigurationException8)
    {
      fEntityResolver = null;
    }
    try
    {
      fValidationManager = ((ValidationManager)paramXMLComponentManager.getProperty("http://apache.org/xml/properties/internal/validation-manager"));
    }
    catch (XMLConfigurationException localXMLConfigurationException9)
    {
      fValidationManager = null;
    }
    try
    {
      fSecurityManager = ((SecurityManager)paramXMLComponentManager.getProperty("http://apache.org/xml/properties/security-manager"));
    }
    catch (XMLConfigurationException localXMLConfigurationException10)
    {
      fSecurityManager = null;
    }
    reset();
  }
  
  public void reset()
  {
    fEntityExpansionLimit = (fSecurityManager != null ? fSecurityManager.getEntityExpansionLimit() : 0);
    fStandalone = false;
    fHasPEReferences = false;
    fEntities.clear();
    fEntityStack.removeAllElements();
    fEntityExpansionCount = 0;
    fCurrentEntity = null;
    if (fXML10EntityScanner != null) {
      fXML10EntityScanner.reset(fSymbolTable, this, fErrorReporter);
    }
    if (fXML11EntityScanner != null) {
      fXML11EntityScanner.reset(fSymbolTable, this, fErrorReporter);
    }
    if (fDeclaredEntities != null)
    {
      Iterator localIterator = fDeclaredEntities.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        Object localObject1 = localEntry.getKey();
        Object localObject2 = localEntry.getValue();
        fEntities.put(localObject1, localObject2);
      }
    }
    fEntityHandler = null;
  }
  
  public String[] getRecognizedFeatures()
  {
    return (String[])RECOGNIZED_FEATURES.clone();
  }
  
  public void setFeature(String paramString, boolean paramBoolean)
    throws XMLConfigurationException
  {
    if (paramString.startsWith("http://apache.org/xml/features/"))
    {
      int i = paramString.length() - "http://apache.org/xml/features/".length();
      if ((i == "allow-java-encodings".length()) && (paramString.endsWith("allow-java-encodings"))) {
        fAllowJavaEncodings = paramBoolean;
      }
    }
  }
  
  public String[] getRecognizedProperties()
  {
    return (String[])RECOGNIZED_PROPERTIES.clone();
  }
  
  public void setProperty(String paramString, Object paramObject)
    throws XMLConfigurationException
  {
    if (paramString.startsWith("http://apache.org/xml/properties/"))
    {
      int i = paramString.length() - "http://apache.org/xml/properties/".length();
      if ((i == "internal/symbol-table".length()) && (paramString.endsWith("internal/symbol-table")))
      {
        fSymbolTable = ((SymbolTable)paramObject);
        return;
      }
      if ((i == "internal/error-reporter".length()) && (paramString.endsWith("internal/error-reporter")))
      {
        fErrorReporter = ((XMLErrorReporter)paramObject);
        return;
      }
      if ((i == "internal/entity-resolver".length()) && (paramString.endsWith("internal/entity-resolver")))
      {
        fEntityResolver = ((XMLEntityResolver)paramObject);
        return;
      }
      if ((i == "input-buffer-size".length()) && (paramString.endsWith("input-buffer-size")))
      {
        Integer localInteger = (Integer)paramObject;
        if ((localInteger != null) && (localInteger.intValue() > 64))
        {
          fBufferSize = localInteger.intValue();
          fEntityScanner.setBufferSize(fBufferSize);
          fSmallByteBufferPool.setBufferSize(fBufferSize);
          fLargeByteBufferPool.setBufferSize(fBufferSize << 1);
          fCharacterBufferPool.setExternalBufferSize(fBufferSize);
        }
      }
      if ((i == "security-manager".length()) && (paramString.endsWith("security-manager")))
      {
        fSecurityManager = ((SecurityManager)paramObject);
        fEntityExpansionLimit = (fSecurityManager != null ? fSecurityManager.getEntityExpansionLimit() : 0);
      }
    }
  }
  
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
  
  private static synchronized URI getUserDir()
    throws URI.MalformedURIException
  {
    String str = "";
    try
    {
      str = (String)AccessController.doPrivileged(GET_USER_DIR_SYSTEM_PROPERTY);
    }
    catch (SecurityException localSecurityException) {}
    if (str.length() == 0) {
      return new URI("file", "", "", null, null);
    }
    if ((gUserDirURI != null) && (str.equals(gUserDir))) {
      return gUserDirURI;
    }
    gUserDir = str;
    char c = File.separatorChar;
    str = str.replace(c, '/');
    int i = str.length();
    StringBuffer localStringBuffer = new StringBuffer(i * 3);
    int j;
    if ((i >= 2) && (str.charAt(1) == ':'))
    {
      j = Character.toUpperCase(str.charAt(0));
      if ((j >= 65) && (j <= 90)) {
        localStringBuffer.append('/');
      }
    }
    for (int k = 0; k < i; k++)
    {
      j = str.charAt(k);
      if (j >= 128) {
        break;
      }
      if (gNeedEscaping[j] != 0)
      {
        localStringBuffer.append('%');
        localStringBuffer.append(gAfterEscaping1[j]);
        localStringBuffer.append(gAfterEscaping2[j]);
      }
      else
      {
        localStringBuffer.append((char)j);
      }
    }
    if (k < i)
    {
      byte[] arrayOfByte = null;
      try
      {
        arrayOfByte = str.substring(k).getBytes("UTF-8");
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
        return new URI("file", "", str, null, null);
      }
      i = arrayOfByte.length;
      for (k = 0; k < i; k++)
      {
        int m = arrayOfByte[k];
        if (m < 0)
        {
          j = m + 256;
          localStringBuffer.append('%');
          localStringBuffer.append(gHexChs[(j >> 4)]);
          localStringBuffer.append(gHexChs[(j & 0xF)]);
        }
        else if (gNeedEscaping[m] != 0)
        {
          localStringBuffer.append('%');
          localStringBuffer.append(gAfterEscaping1[m]);
          localStringBuffer.append(gAfterEscaping2[m]);
        }
        else
        {
          localStringBuffer.append((char)m);
        }
      }
    }
    if (!str.endsWith("/")) {
      localStringBuffer.append('/');
    }
    gUserDirURI = new URI("file", "", localStringBuffer.toString(), null, null);
    return gUserDirURI;
  }
  
  public static void absolutizeAgainstUserDir(URI paramURI)
    throws URI.MalformedURIException
  {
    paramURI.absolutize(getUserDir());
  }
  
  public static String expandSystemId(String paramString1, String paramString2, boolean paramBoolean)
    throws URI.MalformedURIException
  {
    if (paramString1 == null) {
      return null;
    }
    if (paramBoolean) {
      return expandSystemIdStrictOn(paramString1, paramString2);
    }
    try
    {
      return expandSystemIdStrictOff(paramString1, paramString2);
    }
    catch (URI.MalformedURIException localMalformedURIException1)
    {
      if (paramString1.length() == 0) {
        return paramString1;
      }
      String str = fixURI(paramString1);
      URI localURI1 = null;
      URI localURI2 = null;
      try
      {
        if ((paramString2 == null) || (paramString2.length() == 0) || (paramString2.equals(paramString1))) {
          localURI1 = getUserDir();
        } else {
          try
          {
            localURI1 = new URI(fixURI(paramString2).trim());
          }
          catch (URI.MalformedURIException localMalformedURIException2)
          {
            if (paramString2.indexOf(':') != -1) {
              localURI1 = new URI("file", "", fixURI(paramString2).trim(), null, null);
            } else {
              localURI1 = new URI(getUserDir(), fixURI(paramString2));
            }
          }
        }
        localURI2 = new URI(localURI1, str.trim());
      }
      catch (Exception localException) {}
      if (localURI2 == null) {
        return paramString1;
      }
      return localURI2.toString();
    }
  }
  
  private static String expandSystemIdStrictOn(String paramString1, String paramString2)
    throws URI.MalformedURIException
  {
    URI localURI1 = new URI(paramString1, true);
    if (localURI1.isAbsoluteURI()) {
      return paramString1;
    }
    URI localURI2 = null;
    if ((paramString2 == null) || (paramString2.length() == 0))
    {
      localURI2 = getUserDir();
    }
    else
    {
      localURI2 = new URI(paramString2, true);
      if (!localURI2.isAbsoluteURI()) {
        localURI2.absolutize(getUserDir());
      }
    }
    localURI1.absolutize(localURI2);
    return localURI1.toString();
  }
  
  private static String expandSystemIdStrictOff(String paramString1, String paramString2)
    throws URI.MalformedURIException
  {
    URI localURI1 = new URI(paramString1, true);
    if (localURI1.isAbsoluteURI())
    {
      if (localURI1.getScheme().length() > 1) {
        return paramString1;
      }
      throw new URI.MalformedURIException();
    }
    URI localURI2 = null;
    if ((paramString2 == null) || (paramString2.length() == 0))
    {
      localURI2 = getUserDir();
    }
    else
    {
      localURI2 = new URI(paramString2, true);
      if (!localURI2.isAbsoluteURI()) {
        localURI2.absolutize(getUserDir());
      }
    }
    localURI1.absolutize(localURI2);
    return localURI1.toString();
  }
  
  public static OutputStream createOutputStream(String paramString)
    throws IOException
  {
    String str1 = expandSystemId(paramString, null, true);
    URL localURL = new URL(str1 != null ? str1 : paramString);
    Object localObject1 = null;
    String str2 = localURL.getProtocol();
    String str3 = localURL.getHost();
    Object localObject2;
    Object localObject3;
    if ((str2.equals("file")) && ((str3 == null) || (str3.length() == 0) || (str3.equals("localhost"))))
    {
      localObject2 = new File(getPathWithoutEscapes(localURL.getPath()));
      if (!((File)localObject2).exists())
      {
        localObject3 = ((File)localObject2).getParentFile();
        if ((localObject3 != null) && (!((File)localObject3).exists())) {
          ((File)localObject3).mkdirs();
        }
      }
      localObject1 = new FileOutputStream((File)localObject2);
    }
    else
    {
      localObject2 = localURL.openConnection();
      ((URLConnection)localObject2).setDoInput(false);
      ((URLConnection)localObject2).setDoOutput(true);
      ((URLConnection)localObject2).setUseCaches(false);
      if ((localObject2 instanceof HttpURLConnection))
      {
        localObject3 = (HttpURLConnection)localObject2;
        ((HttpURLConnection)localObject3).setRequestMethod("PUT");
      }
      localObject1 = ((URLConnection)localObject2).getOutputStream();
    }
    return localObject1;
  }
  
  private static String getPathWithoutEscapes(String paramString)
  {
    if ((paramString != null) && (paramString.length() != 0) && (paramString.indexOf('%') != -1))
    {
      StringTokenizer localStringTokenizer = new StringTokenizer(paramString, "%");
      StringBuffer localStringBuffer = new StringBuffer(paramString.length());
      int i = localStringTokenizer.countTokens();
      localStringBuffer.append(localStringTokenizer.nextToken());
      for (int j = 1; j < i; j++)
      {
        String str = localStringTokenizer.nextToken();
        localStringBuffer.append((char)Integer.valueOf(str.substring(0, 2), 16).intValue());
        localStringBuffer.append(str.substring(2));
      }
      return localStringBuffer.toString();
    }
    return paramString;
  }
  
  void endEntity()
    throws XNIException
  {
    if (fEntityHandler != null) {
      fEntityHandler.endEntity(fCurrentEntity.name, null);
    }
    try
    {
      fCurrentEntity.reader.close();
    }
    catch (IOException localIOException) {}
    if (!fReaderStack.isEmpty()) {
      fReaderStack.pop();
    }
    fCharacterBufferPool.returnBuffer(fCurrentEntity.fCharacterBuffer);
    if (fCurrentEntity.fByteBuffer != null) {
      if (fCurrentEntity.fByteBuffer.length == fBufferSize) {
        fSmallByteBufferPool.returnBuffer(fCurrentEntity.fByteBuffer);
      } else {
        fLargeByteBufferPool.returnBuffer(fCurrentEntity.fByteBuffer);
      }
    }
    fCurrentEntity = (fEntityStack.size() > 0 ? (ScannedEntity)fEntityStack.pop() : null);
    fEntityScanner.setCurrentEntity(fCurrentEntity);
  }
  
  protected EncodingInfo getEncodingInfo(byte[] paramArrayOfByte, int paramInt)
  {
    if (paramInt < 2) {
      return EncodingInfo.UTF_8;
    }
    int i = paramArrayOfByte[0] & 0xFF;
    int j = paramArrayOfByte[1] & 0xFF;
    if ((i == 254) && (j == 255)) {
      return EncodingInfo.UTF_16_BIG_ENDIAN_WITH_BOM;
    }
    if ((i == 255) && (j == 254)) {
      return EncodingInfo.UTF_16_LITTLE_ENDIAN_WITH_BOM;
    }
    if (paramInt < 3) {
      return EncodingInfo.UTF_8;
    }
    int k = paramArrayOfByte[2] & 0xFF;
    if ((i == 239) && (j == 187) && (k == 191)) {
      return EncodingInfo.UTF_8_WITH_BOM;
    }
    if (paramInt < 4) {
      return EncodingInfo.UTF_8;
    }
    int m = paramArrayOfByte[3] & 0xFF;
    if ((i == 0) && (j == 0) && (k == 0) && (m == 60)) {
      return EncodingInfo.UCS_4_BIG_ENDIAN;
    }
    if ((i == 60) && (j == 0) && (k == 0) && (m == 0)) {
      return EncodingInfo.UCS_4_LITTLE_ENDIAN;
    }
    if ((i == 0) && (j == 0) && (k == 60) && (m == 0)) {
      return EncodingInfo.UCS_4_UNUSUAL_BYTE_ORDER;
    }
    if ((i == 0) && (j == 60) && (k == 0) && (m == 0)) {
      return EncodingInfo.UCS_4_UNUSUAL_BYTE_ORDER;
    }
    if ((i == 0) && (j == 60) && (k == 0) && (m == 63)) {
      return EncodingInfo.UTF_16_BIG_ENDIAN;
    }
    if ((i == 60) && (j == 0) && (k == 63) && (m == 0)) {
      return EncodingInfo.UTF_16_LITTLE_ENDIAN;
    }
    if ((i == 76) && (j == 111) && (k == 167) && (m == 148)) {
      return EncodingInfo.EBCDIC;
    }
    return EncodingInfo.UTF_8;
  }
  
  protected Reader createReader(InputStream paramInputStream, String paramString, Boolean paramBoolean)
    throws IOException
  {
    if ((paramString == "UTF-8") || (paramString == null)) {
      return createUTF8Reader(paramInputStream);
    }
    if ((paramString == "UTF-16") && (paramBoolean != null)) {
      return createUTF16Reader(paramInputStream, paramBoolean.booleanValue());
    }
    String str1 = paramString.toUpperCase(Locale.ENGLISH);
    if (str1.equals("UTF-8")) {
      return createUTF8Reader(paramInputStream);
    }
    if (str1.equals("UTF-16BE")) {
      return createUTF16Reader(paramInputStream, true);
    }
    if (str1.equals("UTF-16LE")) {
      return createUTF16Reader(paramInputStream, false);
    }
    if (str1.equals("ISO-10646-UCS-4"))
    {
      if (paramBoolean != null)
      {
        bool1 = paramBoolean.booleanValue();
        if (bool1) {
          return new UCSReader(paramInputStream, (short)8);
        }
        return new UCSReader(paramInputStream, (short)4);
      }
      fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "EncodingByteOrderUnsupported", new Object[] { paramString }, (short)2);
    }
    if (str1.equals("ISO-10646-UCS-2"))
    {
      if (paramBoolean != null)
      {
        bool1 = paramBoolean.booleanValue();
        if (bool1) {
          return new UCSReader(paramInputStream, (short)2);
        }
        return new UCSReader(paramInputStream, (short)1);
      }
      fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "EncodingByteOrderUnsupported", new Object[] { paramString }, (short)2);
    }
    boolean bool1 = XMLChar.isValidIANAEncoding(paramString);
    boolean bool2 = XMLChar.isValidJavaEncoding(paramString);
    if ((!bool1) || ((fAllowJavaEncodings) && (!bool2)))
    {
      fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "EncodingDeclInvalid", new Object[] { paramString }, (short)2);
      return createLatin1Reader(paramInputStream);
    }
    String str2 = EncodingMap.getIANA2JavaMapping(str1);
    if (str2 == null)
    {
      if (fAllowJavaEncodings)
      {
        str2 = paramString;
      }
      else
      {
        fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "EncodingDeclInvalid", new Object[] { paramString }, (short)2);
        return createLatin1Reader(paramInputStream);
      }
    }
    else
    {
      if (str2.equals("ASCII")) {
        return createASCIIReader(paramInputStream);
      }
      if (str2.equals("ISO8859_1")) {
        return createLatin1Reader(paramInputStream);
      }
    }
    return new InputStreamReader(paramInputStream, str2);
  }
  
  private Reader createUTF8Reader(InputStream paramInputStream)
  {
    if (fTempByteBuffer == null) {
      fTempByteBuffer = fSmallByteBufferPool.getBuffer();
    }
    return new UTF8Reader(paramInputStream, fTempByteBuffer, fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210"), fErrorReporter.getLocale());
  }
  
  private Reader createUTF16Reader(InputStream paramInputStream, boolean paramBoolean)
  {
    if (fTempByteBuffer == null)
    {
      fTempByteBuffer = fLargeByteBufferPool.getBuffer();
    }
    else if (fTempByteBuffer.length == fBufferSize)
    {
      fSmallByteBufferPool.returnBuffer(fTempByteBuffer);
      fTempByteBuffer = fLargeByteBufferPool.getBuffer();
    }
    return new UTF16Reader(paramInputStream, fTempByteBuffer, paramBoolean, fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210"), fErrorReporter.getLocale());
  }
  
  private Reader createASCIIReader(InputStream paramInputStream)
  {
    if (fTempByteBuffer == null) {
      fTempByteBuffer = fSmallByteBufferPool.getBuffer();
    }
    return new ASCIIReader(paramInputStream, fTempByteBuffer, fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210"), fErrorReporter.getLocale());
  }
  
  private Reader createLatin1Reader(InputStream paramInputStream)
  {
    if (fTempByteBuffer == null) {
      fTempByteBuffer = fSmallByteBufferPool.getBuffer();
    }
    return new Latin1Reader(paramInputStream, fTempByteBuffer);
  }
  
  protected static String fixURI(String paramString)
  {
    paramString = paramString.replace(File.separatorChar, '/');
    StringBuffer localStringBuffer = null;
    int j;
    if (paramString.length() >= 2)
    {
      i = paramString.charAt(1);
      if (i == 58)
      {
        j = Character.toUpperCase(paramString.charAt(0));
        if ((j >= 65) && (j <= 90))
        {
          localStringBuffer = new StringBuffer(paramString.length() + 8);
          localStringBuffer.append("file:///");
        }
      }
      else if ((i == 47) && (paramString.charAt(0) == '/'))
      {
        localStringBuffer = new StringBuffer(paramString.length() + 5);
        localStringBuffer.append("file:");
      }
    }
    int i = paramString.indexOf(' ');
    if (i < 0)
    {
      if (localStringBuffer != null)
      {
        localStringBuffer.append(paramString);
        paramString = localStringBuffer.toString();
      }
    }
    else
    {
      if (localStringBuffer == null) {
        localStringBuffer = new StringBuffer(paramString.length());
      }
      for (j = 0; j < i; j++) {
        localStringBuffer.append(paramString.charAt(j));
      }
      localStringBuffer.append("%20");
      for (int k = i + 1; k < paramString.length(); k++) {
        if (paramString.charAt(k) == ' ') {
          localStringBuffer.append("%20");
        } else {
          localStringBuffer.append(paramString.charAt(k));
        }
      }
      paramString = localStringBuffer.toString();
    }
    return paramString;
  }
  
  Hashtable getDeclaredEntities()
  {
    return fEntities;
  }
  
  static final void print(ScannedEntity paramScannedEntity) {}
  
  static
  {
    for (int i = 0; i <= 31; i++)
    {
      gNeedEscaping[i] = true;
      gAfterEscaping1[i] = gHexChs[(i >> 4)];
      gAfterEscaping2[i] = gHexChs[(i & 0xF)];
    }
    gNeedEscaping[127] = true;
    gAfterEscaping1[127] = '7';
    gAfterEscaping2[127] = 'F';
    char[] arrayOfChar = { ' ', '<', '>', '#', '%', '"', '{', '}', '|', '\\', '^', '~', '[', ']', '`' };
    int j = arrayOfChar.length;
    for (int m = 0; m < j; m++)
    {
      int k = arrayOfChar[m];
      gNeedEscaping[k] = true;
      gAfterEscaping1[k] = gHexChs[(k >> 4)];
      gAfterEscaping2[k] = gHexChs[(k & 0xF)];
    }
  }
  
  protected final class RewindableInputStream
    extends InputStream
  {
    private InputStream fInputStream;
    private byte[] fData = new byte[64];
    private int fStartOffset;
    private int fEndOffset;
    private int fOffset;
    private int fLength;
    private int fMark;
    
    public RewindableInputStream(InputStream paramInputStream)
    {
      fInputStream = paramInputStream;
      fStartOffset = 0;
      fEndOffset = -1;
      fOffset = 0;
      fLength = 0;
      fMark = 0;
    }
    
    public void setStartOffset(int paramInt)
    {
      fStartOffset = paramInt;
    }
    
    public void rewind()
    {
      fOffset = fStartOffset;
    }
    
    public int readAndBuffer()
      throws IOException
    {
      if (fOffset == fData.length)
      {
        byte[] arrayOfByte = new byte[fOffset << 1];
        System.arraycopy(fData, 0, arrayOfByte, 0, fOffset);
        fData = arrayOfByte;
      }
      int i = fInputStream.read();
      if (i == -1)
      {
        fEndOffset = fOffset;
        return -1;
      }
      fData[(fLength++)] = ((byte)i);
      fOffset += 1;
      return i & 0xFF;
    }
    
    public int read()
      throws IOException
    {
      if (fOffset < fLength) {
        return fData[(fOffset++)] & 0xFF;
      }
      if (fOffset == fEndOffset) {
        return -1;
      }
      if (fCurrentEntity.mayReadChunks) {
        return fInputStream.read();
      }
      return readAndBuffer();
    }
    
    public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException
    {
      int i = fLength - fOffset;
      if (i == 0)
      {
        if (fOffset == fEndOffset) {
          return -1;
        }
        if (fCurrentEntity.mayReadChunks) {
          return fInputStream.read(paramArrayOfByte, paramInt1, paramInt2);
        }
        int j = readAndBuffer();
        if (j == -1)
        {
          fEndOffset = fOffset;
          return -1;
        }
        paramArrayOfByte[paramInt1] = ((byte)j);
        return 1;
      }
      if (paramInt2 < i)
      {
        if (paramInt2 <= 0) {
          return 0;
        }
      }
      else {
        paramInt2 = i;
      }
      if (paramArrayOfByte != null) {
        System.arraycopy(fData, fOffset, paramArrayOfByte, paramInt1, paramInt2);
      }
      fOffset += paramInt2;
      return paramInt2;
    }
    
    public long skip(long paramLong)
      throws IOException
    {
      if (paramLong <= 0L) {
        return 0L;
      }
      int i = fLength - fOffset;
      if (i == 0)
      {
        if (fOffset == fEndOffset) {
          return 0L;
        }
        return fInputStream.skip(paramLong);
      }
      if (paramLong <= i)
      {
        fOffset = ((int)(fOffset + paramLong));
        return paramLong;
      }
      fOffset += i;
      if (fOffset == fEndOffset) {
        return i;
      }
      paramLong -= i;
      return fInputStream.skip(paramLong) + i;
    }
    
    public int available()
      throws IOException
    {
      int i = fLength - fOffset;
      if (i == 0)
      {
        if (fOffset == fEndOffset) {
          return -1;
        }
        return fCurrentEntity.mayReadChunks ? fInputStream.available() : 0;
      }
      return i;
    }
    
    public void mark(int paramInt)
    {
      fMark = fOffset;
    }
    
    public void reset()
    {
      fOffset = fMark;
    }
    
    public boolean markSupported()
    {
      return true;
    }
    
    public void close()
      throws IOException
    {
      if (fInputStream != null)
      {
        fInputStream.close();
        fInputStream = null;
      }
    }
  }
  
  private static final class CharacterBufferPool
  {
    private static final int DEFAULT_POOL_SIZE = 3;
    private XMLEntityManager.CharacterBuffer[] fInternalBufferPool;
    private XMLEntityManager.CharacterBuffer[] fExternalBufferPool;
    private int fExternalBufferSize;
    private int fInternalBufferSize;
    private int fPoolSize;
    private int fInternalTop;
    private int fExternalTop;
    
    public CharacterBufferPool(int paramInt1, int paramInt2)
    {
      this(3, paramInt1, paramInt2);
    }
    
    public CharacterBufferPool(int paramInt1, int paramInt2, int paramInt3)
    {
      fExternalBufferSize = paramInt2;
      fInternalBufferSize = paramInt3;
      fPoolSize = paramInt1;
      init();
    }
    
    private void init()
    {
      fInternalBufferPool = new XMLEntityManager.CharacterBuffer[fPoolSize];
      fExternalBufferPool = new XMLEntityManager.CharacterBuffer[fPoolSize];
      fInternalTop = -1;
      fExternalTop = -1;
    }
    
    public XMLEntityManager.CharacterBuffer getBuffer(boolean paramBoolean)
    {
      if (paramBoolean)
      {
        if (fExternalTop > -1) {
          return fExternalBufferPool[(fExternalTop--)];
        }
        return new XMLEntityManager.CharacterBuffer(true, fExternalBufferSize);
      }
      if (fInternalTop > -1) {
        return fInternalBufferPool[(fInternalTop--)];
      }
      return new XMLEntityManager.CharacterBuffer(false, fInternalBufferSize);
    }
    
    public void returnBuffer(XMLEntityManager.CharacterBuffer paramCharacterBuffer)
    {
      if (XMLEntityManager.CharacterBuffer.access$500(paramCharacterBuffer))
      {
        if (fExternalTop < fExternalBufferPool.length - 1) {
          fExternalBufferPool[(++fExternalTop)] = paramCharacterBuffer;
        }
      }
      else if (fInternalTop < fInternalBufferPool.length - 1) {
        fInternalBufferPool[(++fInternalTop)] = paramCharacterBuffer;
      }
    }
    
    public void setExternalBufferSize(int paramInt)
    {
      fExternalBufferSize = paramInt;
      fExternalBufferPool = new XMLEntityManager.CharacterBuffer[fPoolSize];
      fExternalTop = -1;
    }
  }
  
  private static final class CharacterBuffer
  {
    private final char[] ch;
    private final boolean isExternal;
    
    public CharacterBuffer(boolean paramBoolean, int paramInt)
    {
      isExternal = paramBoolean;
      ch = new char[paramInt];
    }
  }
  
  private static final class ByteBufferPool
  {
    private static final int DEFAULT_POOL_SIZE = 3;
    private int fPoolSize;
    private int fBufferSize;
    private byte[][] fByteBufferPool;
    private int fDepth;
    
    public ByteBufferPool(int paramInt)
    {
      this(3, paramInt);
    }
    
    public ByteBufferPool(int paramInt1, int paramInt2)
    {
      fPoolSize = paramInt1;
      fBufferSize = paramInt2;
      fByteBufferPool = new byte[fPoolSize][];
      fDepth = 0;
    }
    
    public byte[] getBuffer()
    {
      return fDepth > 0 ? fByteBufferPool[(--fDepth)] : new byte[fBufferSize];
    }
    
    public void returnBuffer(byte[] paramArrayOfByte)
    {
      if (fDepth < fByteBufferPool.length) {
        fByteBufferPool[(fDepth++)] = paramArrayOfByte;
      }
    }
    
    public void setBufferSize(int paramInt)
    {
      fBufferSize = paramInt;
      fByteBufferPool = new byte[fPoolSize][];
      fDepth = 0;
    }
  }
  
  private static class EncodingInfo
  {
    public static final EncodingInfo UTF_8 = new EncodingInfo("UTF-8", null, false);
    public static final EncodingInfo UTF_8_WITH_BOM = new EncodingInfo("UTF-8", null, true);
    public static final EncodingInfo UTF_16_BIG_ENDIAN = new EncodingInfo("UTF-16", Boolean.TRUE, false);
    public static final EncodingInfo UTF_16_BIG_ENDIAN_WITH_BOM = new EncodingInfo("UTF-16", Boolean.TRUE, true);
    public static final EncodingInfo UTF_16_LITTLE_ENDIAN = new EncodingInfo("UTF-16", Boolean.FALSE, false);
    public static final EncodingInfo UTF_16_LITTLE_ENDIAN_WITH_BOM = new EncodingInfo("UTF-16", Boolean.FALSE, true);
    public static final EncodingInfo UCS_4_BIG_ENDIAN = new EncodingInfo("ISO-10646-UCS-4", Boolean.TRUE, false);
    public static final EncodingInfo UCS_4_LITTLE_ENDIAN = new EncodingInfo("ISO-10646-UCS-4", Boolean.FALSE, false);
    public static final EncodingInfo UCS_4_UNUSUAL_BYTE_ORDER = new EncodingInfo("ISO-10646-UCS-4", null, false);
    public static final EncodingInfo EBCDIC = new EncodingInfo("CP037", null, false);
    public final String encoding;
    public final Boolean isBigEndian;
    public final boolean hasBOM;
    
    private EncodingInfo(String paramString, Boolean paramBoolean, boolean paramBoolean1)
    {
      encoding = paramString;
      isBigEndian = paramBoolean;
      hasBOM = paramBoolean1;
    }
  }
  
  public class ScannedEntity
    extends XMLEntityManager.Entity
  {
    public InputStream stream;
    public Reader reader;
    public XMLResourceIdentifier entityLocation;
    public int lineNumber = 1;
    public int columnNumber = 1;
    public String encoding;
    boolean externallySpecifiedEncoding = false;
    public String xmlVersion = "1.0";
    public boolean literal;
    public boolean isExternal;
    public char[] ch = null;
    public int position;
    public int baseCharOffset;
    public int startPosition;
    public int count;
    public boolean mayReadChunks;
    private XMLEntityManager.CharacterBuffer fCharacterBuffer;
    private byte[] fByteBuffer;
    
    public ScannedEntity(String paramString1, XMLResourceIdentifier paramXMLResourceIdentifier, InputStream paramInputStream, Reader paramReader, byte[] paramArrayOfByte, String paramString2, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
    {
      super(fInExternalSubset);
      entityLocation = paramXMLResourceIdentifier;
      stream = paramInputStream;
      reader = paramReader;
      encoding = paramString2;
      literal = paramBoolean1;
      mayReadChunks = paramBoolean2;
      isExternal = paramBoolean3;
      fCharacterBuffer = fCharacterBufferPool.getBuffer(paramBoolean3);
      ch = fCharacterBuffer.ch;
      fByteBuffer = paramArrayOfByte;
    }
    
    public final boolean isExternal()
    {
      return isExternal;
    }
    
    public final boolean isUnparsed()
    {
      return false;
    }
    
    public void setReader(InputStream paramInputStream, String paramString, Boolean paramBoolean)
      throws IOException
    {
      fTempByteBuffer = fByteBuffer;
      reader = createReader(paramInputStream, paramString, paramBoolean);
      fByteBuffer = fTempByteBuffer;
    }
    
    public String getExpandedSystemId()
    {
      int i = fEntityStack.size();
      for (int j = i - 1; j >= 0; j--)
      {
        ScannedEntity localScannedEntity = (ScannedEntity)fEntityStack.elementAt(j);
        if ((entityLocation != null) && (entityLocation.getExpandedSystemId() != null)) {
          return entityLocation.getExpandedSystemId();
        }
      }
      return null;
    }
    
    public String getLiteralSystemId()
    {
      int i = fEntityStack.size();
      for (int j = i - 1; j >= 0; j--)
      {
        ScannedEntity localScannedEntity = (ScannedEntity)fEntityStack.elementAt(j);
        if ((entityLocation != null) && (entityLocation.getLiteralSystemId() != null)) {
          return entityLocation.getLiteralSystemId();
        }
      }
      return null;
    }
    
    public int getLineNumber()
    {
      int i = fEntityStack.size();
      for (int j = i - 1; j >= 0; j--)
      {
        ScannedEntity localScannedEntity = (ScannedEntity)fEntityStack.elementAt(j);
        if (localScannedEntity.isExternal()) {
          return lineNumber;
        }
      }
      return -1;
    }
    
    public int getColumnNumber()
    {
      int i = fEntityStack.size();
      for (int j = i - 1; j >= 0; j--)
      {
        ScannedEntity localScannedEntity = (ScannedEntity)fEntityStack.elementAt(j);
        if (localScannedEntity.isExternal()) {
          return columnNumber;
        }
      }
      return -1;
    }
    
    public int getCharacterOffset()
    {
      int i = fEntityStack.size();
      for (int j = i - 1; j >= 0; j--)
      {
        ScannedEntity localScannedEntity = (ScannedEntity)fEntityStack.elementAt(j);
        if (localScannedEntity.isExternal()) {
          return baseCharOffset + (position - startPosition);
        }
      }
      return -1;
    }
    
    public String getEncoding()
    {
      int i = fEntityStack.size();
      for (int j = i - 1; j >= 0; j--)
      {
        ScannedEntity localScannedEntity = (ScannedEntity)fEntityStack.elementAt(j);
        if (localScannedEntity.isExternal()) {
          return encoding;
        }
      }
      return null;
    }
    
    public String getXMLVersion()
    {
      int i = fEntityStack.size();
      for (int j = i - 1; j >= 0; j--)
      {
        ScannedEntity localScannedEntity = (ScannedEntity)fEntityStack.elementAt(j);
        if (localScannedEntity.isExternal()) {
          return xmlVersion;
        }
      }
      return null;
    }
    
    public boolean isEncodingExternallySpecified()
    {
      return externallySpecifiedEncoding;
    }
    
    public void setEncodingExternallySpecified(boolean paramBoolean)
    {
      externallySpecifiedEncoding = paramBoolean;
    }
    
    public String toString()
    {
      StringBuffer localStringBuffer = new StringBuffer();
      localStringBuffer.append("name=\"").append(name).append('"');
      localStringBuffer.append(",ch=");
      localStringBuffer.append(ch);
      localStringBuffer.append(",position=").append(position);
      localStringBuffer.append(",count=").append(count);
      localStringBuffer.append(",baseCharOffset=").append(baseCharOffset);
      localStringBuffer.append(",startPosition=").append(startPosition);
      return localStringBuffer.toString();
    }
  }
  
  protected static class ExternalEntity
    extends XMLEntityManager.Entity
  {
    public XMLResourceIdentifier entityLocation;
    public String notation;
    
    public ExternalEntity()
    {
      clear();
    }
    
    public ExternalEntity(String paramString1, XMLResourceIdentifier paramXMLResourceIdentifier, String paramString2, boolean paramBoolean)
    {
      super(paramBoolean);
      entityLocation = paramXMLResourceIdentifier;
      notation = paramString2;
    }
    
    public final boolean isExternal()
    {
      return true;
    }
    
    public final boolean isUnparsed()
    {
      return notation != null;
    }
    
    public void clear()
    {
      super.clear();
      entityLocation = null;
      notation = null;
    }
    
    public void setValues(XMLEntityManager.Entity paramEntity)
    {
      super.setValues(paramEntity);
      entityLocation = null;
      notation = null;
    }
    
    public void setValues(ExternalEntity paramExternalEntity)
    {
      super.setValues(paramExternalEntity);
      entityLocation = entityLocation;
      notation = notation;
    }
  }
  
  protected static class InternalEntity
    extends XMLEntityManager.Entity
  {
    public String text;
    
    public InternalEntity()
    {
      clear();
    }
    
    public InternalEntity(String paramString1, String paramString2, boolean paramBoolean)
    {
      super(paramBoolean);
      text = paramString2;
    }
    
    public final boolean isExternal()
    {
      return false;
    }
    
    public final boolean isUnparsed()
    {
      return false;
    }
    
    public void clear()
    {
      super.clear();
      text = null;
    }
    
    public void setValues(XMLEntityManager.Entity paramEntity)
    {
      super.setValues(paramEntity);
      text = null;
    }
    
    public void setValues(InternalEntity paramInternalEntity)
    {
      super.setValues(paramInternalEntity);
      text = text;
    }
  }
  
  public static abstract class Entity
  {
    public String name;
    public boolean inExternalSubset;
    
    public Entity()
    {
      clear();
    }
    
    public Entity(String paramString, boolean paramBoolean)
    {
      name = paramString;
      inExternalSubset = paramBoolean;
    }
    
    public boolean isEntityDeclInExternalSubset()
    {
      return inExternalSubset;
    }
    
    public abstract boolean isExternal();
    
    public abstract boolean isUnparsed();
    
    public void clear()
    {
      name = null;
      inExternalSubset = false;
    }
    
    public void setValues(Entity paramEntity)
    {
      name = name;
      inExternalSubset = inExternalSubset;
    }
  }
}
