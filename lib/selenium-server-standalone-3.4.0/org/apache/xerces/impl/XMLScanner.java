package org.apache.xerces.impl;

import java.io.IOException;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.util.XMLResourceIdentifierImpl;
import org.apache.xerces.util.XMLStringBuffer;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLComponent;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;

public abstract class XMLScanner
  implements XMLComponent
{
  protected static final String VALIDATION = "http://xml.org/sax/features/validation";
  protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
  protected static final String NOTIFY_CHAR_REFS = "http://apache.org/xml/features/scanner/notify-char-refs";
  protected static final String PARSER_SETTINGS = "http://apache.org/xml/features/internal/parser-settings";
  protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
  protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
  protected static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
  protected static final boolean DEBUG_ATTR_NORMALIZATION = false;
  protected boolean fValidation = false;
  protected boolean fNamespaces;
  protected boolean fNotifyCharRefs = false;
  protected boolean fParserSettings = true;
  protected SymbolTable fSymbolTable;
  protected XMLErrorReporter fErrorReporter;
  protected XMLEntityManager fEntityManager;
  protected XMLEntityScanner fEntityScanner;
  protected int fEntityDepth;
  protected String fCharRefLiteral = null;
  protected boolean fScanningAttribute;
  protected boolean fReportEntity;
  protected static final String fVersionSymbol = "version".intern();
  protected static final String fEncodingSymbol = "encoding".intern();
  protected static final String fStandaloneSymbol = "standalone".intern();
  protected static final String fAmpSymbol = "amp".intern();
  protected static final String fLtSymbol = "lt".intern();
  protected static final String fGtSymbol = "gt".intern();
  protected static final String fQuotSymbol = "quot".intern();
  protected static final String fAposSymbol = "apos".intern();
  private final XMLString fString = new XMLString();
  private final XMLStringBuffer fStringBuffer = new XMLStringBuffer();
  private final XMLStringBuffer fStringBuffer2 = new XMLStringBuffer();
  private final XMLStringBuffer fStringBuffer3 = new XMLStringBuffer();
  protected final XMLResourceIdentifierImpl fResourceIdentifier = new XMLResourceIdentifierImpl();
  
  public XMLScanner() {}
  
  public void reset(XMLComponentManager paramXMLComponentManager)
    throws XMLConfigurationException
  {
    try
    {
      fParserSettings = paramXMLComponentManager.getFeature("http://apache.org/xml/features/internal/parser-settings");
    }
    catch (XMLConfigurationException localXMLConfigurationException1)
    {
      fParserSettings = true;
    }
    if (!fParserSettings)
    {
      init();
      return;
    }
    fSymbolTable = ((SymbolTable)paramXMLComponentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table"));
    fErrorReporter = ((XMLErrorReporter)paramXMLComponentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter"));
    fEntityManager = ((XMLEntityManager)paramXMLComponentManager.getProperty("http://apache.org/xml/properties/internal/entity-manager"));
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
      fNamespaces = paramXMLComponentManager.getFeature("http://xml.org/sax/features/namespaces");
    }
    catch (XMLConfigurationException localXMLConfigurationException3)
    {
      fNamespaces = true;
    }
    try
    {
      fNotifyCharRefs = paramXMLComponentManager.getFeature("http://apache.org/xml/features/scanner/notify-char-refs");
    }
    catch (XMLConfigurationException localXMLConfigurationException4)
    {
      fNotifyCharRefs = false;
    }
    init();
  }
  
  public void setProperty(String paramString, Object paramObject)
    throws XMLConfigurationException
  {
    if (paramString.startsWith("http://apache.org/xml/properties/"))
    {
      int i = paramString.length() - "http://apache.org/xml/properties/".length();
      if ((i == "internal/symbol-table".length()) && (paramString.endsWith("internal/symbol-table"))) {
        fSymbolTable = ((SymbolTable)paramObject);
      } else if ((i == "internal/error-reporter".length()) && (paramString.endsWith("internal/error-reporter"))) {
        fErrorReporter = ((XMLErrorReporter)paramObject);
      } else if ((i == "internal/entity-manager".length()) && (paramString.endsWith("internal/entity-manager"))) {
        fEntityManager = ((XMLEntityManager)paramObject);
      }
    }
  }
  
  public void setFeature(String paramString, boolean paramBoolean)
    throws XMLConfigurationException
  {
    if ("http://xml.org/sax/features/validation".equals(paramString)) {
      fValidation = paramBoolean;
    } else if ("http://apache.org/xml/features/scanner/notify-char-refs".equals(paramString)) {
      fNotifyCharRefs = paramBoolean;
    }
  }
  
  public boolean getFeature(String paramString)
    throws XMLConfigurationException
  {
    if ("http://xml.org/sax/features/validation".equals(paramString)) {
      return fValidation;
    }
    if ("http://apache.org/xml/features/scanner/notify-char-refs".equals(paramString)) {
      return fNotifyCharRefs;
    }
    throw new XMLConfigurationException((short)0, paramString);
  }
  
  protected void reset()
  {
    init();
    fValidation = true;
    fNotifyCharRefs = false;
  }
  
  protected void scanXMLDeclOrTextDecl(boolean paramBoolean, String[] paramArrayOfString)
    throws IOException, XNIException
  {
    String str1 = null;
    String str2 = null;
    String str3 = null;
    int i = 0;
    int j = 0;
    boolean bool1 = fEntityScanner.skipDeclSpaces();
    XMLEntityManager.ScannedEntity localScannedEntity = fEntityManager.getCurrentEntity();
    boolean bool2 = literal;
    literal = false;
    while (fEntityScanner.peekChar() != 63)
    {
      j = 1;
      String str4 = scanPseudoAttribute(paramBoolean, fString);
      switch (i)
      {
      case 0: 
        if (str4 == fVersionSymbol)
        {
          if (!bool1) {
            reportFatalError(paramBoolean ? "SpaceRequiredBeforeVersionInTextDecl" : "SpaceRequiredBeforeVersionInXMLDecl", null);
          }
          str1 = fString.toString();
          i = 1;
          if (!versionSupported(str1)) {
            reportFatalError(getVersionNotSupportedKey(), new Object[] { str1 });
          }
        }
        else if (str4 == fEncodingSymbol)
        {
          if (!paramBoolean) {
            reportFatalError("VersionInfoRequired", null);
          }
          if (!bool1) {
            reportFatalError(paramBoolean ? "SpaceRequiredBeforeEncodingInTextDecl" : "SpaceRequiredBeforeEncodingInXMLDecl", null);
          }
          str2 = fString.toString();
          i = paramBoolean ? 3 : 2;
        }
        else if (paramBoolean)
        {
          reportFatalError("EncodingDeclRequired", null);
        }
        else
        {
          reportFatalError("VersionInfoRequired", null);
        }
        break;
      case 1: 
        if (str4 == fEncodingSymbol)
        {
          if (!bool1) {
            reportFatalError(paramBoolean ? "SpaceRequiredBeforeEncodingInTextDecl" : "SpaceRequiredBeforeEncodingInXMLDecl", null);
          }
          str2 = fString.toString();
          i = paramBoolean ? 3 : 2;
        }
        else if ((!paramBoolean) && (str4 == fStandaloneSymbol))
        {
          if (!bool1) {
            reportFatalError("SpaceRequiredBeforeStandalone", null);
          }
          str3 = fString.toString();
          i = 3;
          if ((!str3.equals("yes")) && (!str3.equals("no"))) {
            reportFatalError("SDDeclInvalid", new Object[] { str3 });
          }
        }
        else
        {
          reportFatalError("EncodingDeclRequired", null);
        }
        break;
      case 2: 
        if (str4 == fStandaloneSymbol)
        {
          if (!bool1) {
            reportFatalError("SpaceRequiredBeforeStandalone", null);
          }
          str3 = fString.toString();
          i = 3;
          if ((!str3.equals("yes")) && (!str3.equals("no"))) {
            reportFatalError("SDDeclInvalid", new Object[] { str3 });
          }
        }
        else
        {
          reportFatalError("EncodingDeclRequired", null);
        }
        break;
      default: 
        reportFatalError("NoMorePseudoAttributes", null);
      }
      bool1 = fEntityScanner.skipDeclSpaces();
    }
    if (bool2) {
      literal = true;
    }
    if ((paramBoolean) && (i != 3)) {
      reportFatalError("MorePseudoAttributes", null);
    }
    if (paramBoolean)
    {
      if ((j == 0) && (str2 == null)) {
        reportFatalError("EncodingDeclRequired", null);
      }
    }
    else if ((j == 0) && (str1 == null)) {
      reportFatalError("VersionInfoRequired", null);
    }
    if (!fEntityScanner.skipChar(63)) {
      reportFatalError("XMLDeclUnterminated", null);
    }
    if (!fEntityScanner.skipChar(62)) {
      reportFatalError("XMLDeclUnterminated", null);
    }
    paramArrayOfString[0] = str1;
    paramArrayOfString[1] = str2;
    paramArrayOfString[2] = str3;
  }
  
  public String scanPseudoAttribute(boolean paramBoolean, XMLString paramXMLString)
    throws IOException, XNIException
  {
    String str1 = fEntityScanner.scanName();
    XMLEntityManager.print(fEntityManager.getCurrentEntity());
    if (str1 == null) {
      reportFatalError("PseudoAttrNameExpected", null);
    }
    fEntityScanner.skipDeclSpaces();
    if (!fEntityScanner.skipChar(61)) {
      reportFatalError(paramBoolean ? "EqRequiredInTextDecl" : "EqRequiredInXMLDecl", new Object[] { str1 });
    }
    fEntityScanner.skipDeclSpaces();
    int i = fEntityScanner.peekChar();
    if ((i != 39) && (i != 34)) {
      reportFatalError(paramBoolean ? "QuoteRequiredInTextDecl" : "QuoteRequiredInXMLDecl", new Object[] { str1 });
    }
    fEntityScanner.scanChar();
    int j = fEntityScanner.scanLiteral(i, paramXMLString);
    if (j != i)
    {
      fStringBuffer2.clear();
      do
      {
        fStringBuffer2.append(paramXMLString);
        if (j != -1) {
          if ((j == 38) || (j == 37) || (j == 60) || (j == 93))
          {
            fStringBuffer2.append((char)fEntityScanner.scanChar());
          }
          else if (XMLChar.isHighSurrogate(j))
          {
            scanSurrogates(fStringBuffer2);
          }
          else if (isInvalidLiteral(j))
          {
            String str2 = paramBoolean ? "InvalidCharInTextDecl" : "InvalidCharInXMLDecl";
            reportFatalError(str2, new Object[] { Integer.toString(j, 16) });
            fEntityScanner.scanChar();
          }
        }
        j = fEntityScanner.scanLiteral(i, paramXMLString);
      } while (j != i);
      fStringBuffer2.append(paramXMLString);
      paramXMLString.setValues(fStringBuffer2);
    }
    if (!fEntityScanner.skipChar(i)) {
      reportFatalError(paramBoolean ? "CloseQuoteMissingInTextDecl" : "CloseQuoteMissingInXMLDecl", new Object[] { str1 });
    }
    return str1;
  }
  
  protected void scanPI()
    throws IOException, XNIException
  {
    fReportEntity = false;
    String str = null;
    if (fNamespaces) {
      str = fEntityScanner.scanNCName();
    } else {
      str = fEntityScanner.scanName();
    }
    if (str == null) {
      reportFatalError("PITargetRequired", null);
    }
    scanPIData(str, fString);
    fReportEntity = true;
  }
  
  protected void scanPIData(String paramString, XMLString paramXMLString)
    throws IOException, XNIException
  {
    if (paramString.length() == 3)
    {
      int i = Character.toLowerCase(paramString.charAt(0));
      int k = Character.toLowerCase(paramString.charAt(1));
      int m = Character.toLowerCase(paramString.charAt(2));
      if ((i == 120) && (k == 109) && (m == 108)) {
        reportFatalError("ReservedPITarget", null);
      }
    }
    if (!fEntityScanner.skipSpaces())
    {
      if (fEntityScanner.skipString("?>"))
      {
        paramXMLString.clear();
        return;
      }
      if ((fNamespaces) && (fEntityScanner.peekChar() == 58))
      {
        fEntityScanner.scanChar();
        XMLStringBuffer localXMLStringBuffer = new XMLStringBuffer(paramString);
        localXMLStringBuffer.append(':');
        String str = fEntityScanner.scanName();
        if (str != null) {
          localXMLStringBuffer.append(str);
        }
        reportFatalError("ColonNotLegalWithNS", new Object[] { localXMLStringBuffer.toString() });
        fEntityScanner.skipSpaces();
      }
      else
      {
        reportFatalError("SpaceRequiredInPI", null);
      }
    }
    fStringBuffer.clear();
    if (fEntityScanner.scanData("?>", fStringBuffer)) {
      do
      {
        int j = fEntityScanner.peekChar();
        if (j != -1) {
          if (XMLChar.isHighSurrogate(j))
          {
            scanSurrogates(fStringBuffer);
          }
          else if (isInvalidLiteral(j))
          {
            reportFatalError("InvalidCharInPI", new Object[] { Integer.toHexString(j) });
            fEntityScanner.scanChar();
          }
        }
      } while (fEntityScanner.scanData("?>", fStringBuffer));
    }
    paramXMLString.setValues(fStringBuffer);
  }
  
  protected void scanComment(XMLStringBuffer paramXMLStringBuffer)
    throws IOException, XNIException
  {
    paramXMLStringBuffer.clear();
    while (fEntityScanner.scanData("--", paramXMLStringBuffer))
    {
      int i = fEntityScanner.peekChar();
      if (i != -1) {
        if (XMLChar.isHighSurrogate(i))
        {
          scanSurrogates(paramXMLStringBuffer);
        }
        else if (isInvalidLiteral(i))
        {
          reportFatalError("InvalidCharInComment", new Object[] { Integer.toHexString(i) });
          fEntityScanner.scanChar();
        }
      }
    }
    if (!fEntityScanner.skipChar(62)) {
      reportFatalError("DashDashInComment", null);
    }
  }
  
  protected boolean scanAttributeValue(XMLString paramXMLString1, XMLString paramXMLString2, String paramString1, boolean paramBoolean, String paramString2)
    throws IOException, XNIException
  {
    int i = fEntityScanner.peekChar();
    if ((i != 39) && (i != 34)) {
      reportFatalError("OpenQuoteExpected", new Object[] { paramString2, paramString1 });
    }
    fEntityScanner.scanChar();
    int j = fEntityDepth;
    int k = fEntityScanner.scanLiteral(i, paramXMLString1);
    int m = 0;
    int n;
    if ((k == i) && ((m = isUnchangedByNormalization(paramXMLString1)) == -1))
    {
      paramXMLString2.setValues(paramXMLString1);
      n = fEntityScanner.scanChar();
      if (n != i) {
        reportFatalError("CloseQuoteExpected", new Object[] { paramString2, paramString1 });
      }
      return true;
    }
    fStringBuffer2.clear();
    fStringBuffer2.append(paramXMLString1);
    normalizeWhitespace(paramXMLString1, m);
    if (k != i)
    {
      fScanningAttribute = true;
      fStringBuffer.clear();
      do
      {
        fStringBuffer.append(paramXMLString1);
        if (k == 38)
        {
          fEntityScanner.skipChar(38);
          if (j == fEntityDepth) {
            fStringBuffer2.append('&');
          }
          if (fEntityScanner.skipChar(35))
          {
            if (j == fEntityDepth) {
              fStringBuffer2.append('#');
            }
            n = scanCharReferenceValue(fStringBuffer, fStringBuffer2);
            if (n == -1) {}
          }
          else
          {
            String str = fEntityScanner.scanName();
            if (str == null) {
              reportFatalError("NameRequiredInReference", null);
            } else if (j == fEntityDepth) {
              fStringBuffer2.append(str);
            }
            if (!fEntityScanner.skipChar(59)) {
              reportFatalError("SemicolonRequiredInReference", new Object[] { str });
            } else if (j == fEntityDepth) {
              fStringBuffer2.append(';');
            }
            if (str == fAmpSymbol)
            {
              fStringBuffer.append('&');
            }
            else if (str == fAposSymbol)
            {
              fStringBuffer.append('\'');
            }
            else if (str == fLtSymbol)
            {
              fStringBuffer.append('<');
            }
            else if (str == fGtSymbol)
            {
              fStringBuffer.append('>');
            }
            else if (str == fQuotSymbol)
            {
              fStringBuffer.append('"');
            }
            else if (fEntityManager.isExternalEntity(str))
            {
              reportFatalError("ReferenceToExternalEntity", new Object[] { str });
            }
            else
            {
              if (!fEntityManager.isDeclaredEntity(str)) {
                if (paramBoolean)
                {
                  if (fValidation) {
                    fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "EntityNotDeclared", new Object[] { str }, (short)1);
                  }
                }
                else {
                  reportFatalError("EntityNotDeclared", new Object[] { str });
                }
              }
              fEntityManager.startEntity(str, true);
            }
          }
        }
        else if (k == 60)
        {
          reportFatalError("LessthanInAttValue", new Object[] { paramString2, paramString1 });
          fEntityScanner.scanChar();
          if (j == fEntityDepth) {
            fStringBuffer2.append((char)k);
          }
        }
        else if ((k == 37) || (k == 93))
        {
          fEntityScanner.scanChar();
          fStringBuffer.append((char)k);
          if (j == fEntityDepth) {
            fStringBuffer2.append((char)k);
          }
        }
        else if ((k == 10) || (k == 13))
        {
          fEntityScanner.scanChar();
          fStringBuffer.append(' ');
          if (j == fEntityDepth) {
            fStringBuffer2.append('\n');
          }
        }
        else if ((k != -1) && (XMLChar.isHighSurrogate(k)))
        {
          fStringBuffer3.clear();
          if (scanSurrogates(fStringBuffer3))
          {
            fStringBuffer.append(fStringBuffer3);
            if (j == fEntityDepth) {
              fStringBuffer2.append(fStringBuffer3);
            }
          }
        }
        else if ((k != -1) && (isInvalidLiteral(k)))
        {
          reportFatalError("InvalidCharInAttValue", new Object[] { paramString2, paramString1, Integer.toString(k, 16) });
          fEntityScanner.scanChar();
          if (j == fEntityDepth) {
            fStringBuffer2.append((char)k);
          }
        }
        k = fEntityScanner.scanLiteral(i, paramXMLString1);
        if (j == fEntityDepth) {
          fStringBuffer2.append(paramXMLString1);
        }
        normalizeWhitespace(paramXMLString1);
      } while ((k != i) || (j != fEntityDepth));
      fStringBuffer.append(paramXMLString1);
      paramXMLString1.setValues(fStringBuffer);
      fScanningAttribute = false;
    }
    paramXMLString2.setValues(fStringBuffer2);
    int i1 = fEntityScanner.scanChar();
    if (i1 != i) {
      reportFatalError("CloseQuoteExpected", new Object[] { paramString2, paramString1 });
    }
    return paramXMLString2.equals(ch, offset, length);
  }
  
  protected void scanExternalID(String[] paramArrayOfString, boolean paramBoolean)
    throws IOException, XNIException
  {
    String str1 = null;
    String str2 = null;
    if (fEntityScanner.skipString("PUBLIC"))
    {
      if (!fEntityScanner.skipSpaces()) {
        reportFatalError("SpaceRequiredAfterPUBLIC", null);
      }
      scanPubidLiteral(fString);
      str2 = fString.toString();
      if ((!fEntityScanner.skipSpaces()) && (!paramBoolean)) {
        reportFatalError("SpaceRequiredBetweenPublicAndSystem", null);
      }
    }
    if ((str2 != null) || (fEntityScanner.skipString("SYSTEM")))
    {
      if ((str2 == null) && (!fEntityScanner.skipSpaces())) {
        reportFatalError("SpaceRequiredAfterSYSTEM", null);
      }
      int i = fEntityScanner.peekChar();
      if ((i != 39) && (i != 34))
      {
        if ((str2 != null) && (paramBoolean))
        {
          paramArrayOfString[0] = null;
          paramArrayOfString[1] = str2;
          return;
        }
        reportFatalError("QuoteRequiredInSystemID", null);
      }
      fEntityScanner.scanChar();
      Object localObject = fString;
      if (fEntityScanner.scanLiteral(i, (XMLString)localObject) != i)
      {
        fStringBuffer.clear();
        do
        {
          fStringBuffer.append((XMLString)localObject);
          int j = fEntityScanner.peekChar();
          if ((XMLChar.isMarkup(j)) || (j == 93))
          {
            fStringBuffer.append((char)fEntityScanner.scanChar());
          }
          else if (XMLChar.isHighSurrogate(j))
          {
            scanSurrogates(fStringBuffer);
          }
          else if (isInvalidLiteral(j))
          {
            reportFatalError("InvalidCharInSystemID", new Object[] { Integer.toHexString(j) });
            fEntityScanner.scanChar();
          }
        } while (fEntityScanner.scanLiteral(i, (XMLString)localObject) != i);
        fStringBuffer.append((XMLString)localObject);
        localObject = fStringBuffer;
      }
      str1 = ((XMLString)localObject).toString();
      if (!fEntityScanner.skipChar(i)) {
        reportFatalError("SystemIDUnterminated", null);
      }
    }
    paramArrayOfString[0] = str1;
    paramArrayOfString[1] = str2;
  }
  
  protected boolean scanPubidLiteral(XMLString paramXMLString)
    throws IOException, XNIException
  {
    int i = fEntityScanner.scanChar();
    if ((i != 39) && (i != 34))
    {
      reportFatalError("QuoteRequiredInPublicID", null);
      return false;
    }
    fStringBuffer.clear();
    int j = 1;
    boolean bool = true;
    for (;;)
    {
      int k = fEntityScanner.scanChar();
      if ((k == 32) || (k == 10) || (k == 13))
      {
        if (j == 0)
        {
          fStringBuffer.append(' ');
          j = 1;
        }
      }
      else
      {
        if (k == i)
        {
          if (j != 0) {
            fStringBuffer.length -= 1;
          }
          paramXMLString.setValues(fStringBuffer);
          break;
        }
        if (XMLChar.isPubid(k))
        {
          fStringBuffer.append((char)k);
          j = 0;
        }
        else
        {
          if (k == -1)
          {
            reportFatalError("PublicIDUnterminated", null);
            return false;
          }
          bool = false;
          reportFatalError("InvalidCharInPublicID", new Object[] { Integer.toHexString(k) });
        }
      }
    }
    return bool;
  }
  
  protected void normalizeWhitespace(XMLString paramXMLString)
  {
    int i = offset + length;
    for (int j = offset; j < i; j++)
    {
      int k = ch[j];
      if (k < 32) {
        ch[j] = ' ';
      }
    }
  }
  
  protected void normalizeWhitespace(XMLString paramXMLString, int paramInt)
  {
    int i = offset + length;
    for (int j = offset + paramInt; j < i; j++)
    {
      int k = ch[j];
      if (k < 32) {
        ch[j] = ' ';
      }
    }
  }
  
  protected int isUnchangedByNormalization(XMLString paramXMLString)
  {
    int i = offset + length;
    for (int j = offset; j < i; j++)
    {
      int k = ch[j];
      if (k < 32) {
        return j - offset;
      }
    }
    return -1;
  }
  
  public void startEntity(String paramString1, XMLResourceIdentifier paramXMLResourceIdentifier, String paramString2, Augmentations paramAugmentations)
    throws XNIException
  {
    fEntityDepth += 1;
    fEntityScanner = fEntityManager.getEntityScanner();
  }
  
  public void endEntity(String paramString, Augmentations paramAugmentations)
    throws XNIException
  {
    fEntityDepth -= 1;
  }
  
  protected int scanCharReferenceValue(XMLStringBuffer paramXMLStringBuffer1, XMLStringBuffer paramXMLStringBuffer2)
    throws IOException, XNIException
  {
    int i = 0;
    int k;
    if (fEntityScanner.skipChar(120))
    {
      if (paramXMLStringBuffer2 != null) {
        paramXMLStringBuffer2.append('x');
      }
      i = 1;
      fStringBuffer3.clear();
      j = 1;
      k = fEntityScanner.peekChar();
      j = ((k >= 48) && (k <= 57)) || ((k >= 97) && (k <= 102)) || ((k >= 65) && (k <= 70)) ? 1 : 0;
      if (j != 0)
      {
        if (paramXMLStringBuffer2 != null) {
          paramXMLStringBuffer2.append((char)k);
        }
        fEntityScanner.scanChar();
        fStringBuffer3.append((char)k);
        do
        {
          k = fEntityScanner.peekChar();
          j = ((k >= 48) && (k <= 57)) || ((k >= 97) && (k <= 102)) || ((k >= 65) && (k <= 70)) ? 1 : 0;
          if (j != 0)
          {
            if (paramXMLStringBuffer2 != null) {
              paramXMLStringBuffer2.append((char)k);
            }
            fEntityScanner.scanChar();
            fStringBuffer3.append((char)k);
          }
        } while (j != 0);
      }
      else
      {
        reportFatalError("HexdigitRequiredInCharRef", null);
      }
    }
    else
    {
      fStringBuffer3.clear();
      j = 1;
      k = fEntityScanner.peekChar();
      j = (k >= 48) && (k <= 57) ? 1 : 0;
      if (j != 0)
      {
        if (paramXMLStringBuffer2 != null) {
          paramXMLStringBuffer2.append((char)k);
        }
        fEntityScanner.scanChar();
        fStringBuffer3.append((char)k);
        do
        {
          k = fEntityScanner.peekChar();
          j = (k >= 48) && (k <= 57) ? 1 : 0;
          if (j != 0)
          {
            if (paramXMLStringBuffer2 != null) {
              paramXMLStringBuffer2.append((char)k);
            }
            fEntityScanner.scanChar();
            fStringBuffer3.append((char)k);
          }
        } while (j != 0);
      }
      else
      {
        reportFatalError("DigitRequiredInCharRef", null);
      }
    }
    if (!fEntityScanner.skipChar(59)) {
      reportFatalError("SemicolonRequiredInCharRef", null);
    }
    if (paramXMLStringBuffer2 != null) {
      paramXMLStringBuffer2.append(';');
    }
    int j = -1;
    try
    {
      j = Integer.parseInt(fStringBuffer3.toString(), i != 0 ? 16 : 10);
      if (isInvalid(j))
      {
        StringBuffer localStringBuffer1 = new StringBuffer(fStringBuffer3.length + 1);
        if (i != 0) {
          localStringBuffer1.append('x');
        }
        localStringBuffer1.append(fStringBuffer3.ch, fStringBuffer3.offset, fStringBuffer3.length);
        reportFatalError("InvalidCharRef", new Object[] { localStringBuffer1.toString() });
      }
    }
    catch (NumberFormatException localNumberFormatException)
    {
      StringBuffer localStringBuffer2 = new StringBuffer(fStringBuffer3.length + 1);
      if (i != 0) {
        localStringBuffer2.append('x');
      }
      localStringBuffer2.append(fStringBuffer3.ch, fStringBuffer3.offset, fStringBuffer3.length);
      reportFatalError("InvalidCharRef", new Object[] { localStringBuffer2.toString() });
    }
    if (!XMLChar.isSupplemental(j))
    {
      paramXMLStringBuffer1.append((char)j);
    }
    else
    {
      paramXMLStringBuffer1.append(XMLChar.highSurrogate(j));
      paramXMLStringBuffer1.append(XMLChar.lowSurrogate(j));
    }
    if ((fNotifyCharRefs) && (j != -1))
    {
      String str = "#" + (i != 0 ? "x" : "") + fStringBuffer3.toString();
      if (!fScanningAttribute) {
        fCharRefLiteral = str;
      }
    }
    return j;
  }
  
  protected boolean isInvalid(int paramInt)
  {
    return XMLChar.isInvalid(paramInt);
  }
  
  protected boolean isInvalidLiteral(int paramInt)
  {
    return XMLChar.isInvalid(paramInt);
  }
  
  protected boolean isValidNameChar(int paramInt)
  {
    return XMLChar.isName(paramInt);
  }
  
  protected boolean isValidNameStartChar(int paramInt)
  {
    return XMLChar.isNameStart(paramInt);
  }
  
  protected boolean isValidNCName(int paramInt)
  {
    return XMLChar.isNCName(paramInt);
  }
  
  protected boolean isValidNameStartHighSurrogate(int paramInt)
  {
    return false;
  }
  
  protected boolean versionSupported(String paramString)
  {
    return paramString.equals("1.0");
  }
  
  protected String getVersionNotSupportedKey()
  {
    return "VersionNotSupported";
  }
  
  protected boolean scanSurrogates(XMLStringBuffer paramXMLStringBuffer)
    throws IOException, XNIException
  {
    int i = fEntityScanner.scanChar();
    int j = fEntityScanner.peekChar();
    if (!XMLChar.isLowSurrogate(j))
    {
      reportFatalError("InvalidCharInContent", new Object[] { Integer.toString(i, 16) });
      return false;
    }
    fEntityScanner.scanChar();
    int k = XMLChar.supplemental((char)i, (char)j);
    if (isInvalid(k))
    {
      reportFatalError("InvalidCharInContent", new Object[] { Integer.toString(k, 16) });
      return false;
    }
    paramXMLStringBuffer.append((char)i);
    paramXMLStringBuffer.append((char)j);
    return true;
  }
  
  protected void reportFatalError(String paramString, Object[] paramArrayOfObject)
    throws XNIException
  {
    fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", paramString, paramArrayOfObject, (short)2);
  }
  
  private void init()
  {
    fEntityScanner = null;
    fEntityDepth = 0;
    fReportEntity = true;
    fResourceIdentifier.clear();
  }
  
  public abstract Object getPropertyDefault(String paramString);
  
  public abstract Boolean getFeatureDefault(String paramString);
  
  public abstract String[] getRecognizedProperties();
  
  public abstract String[] getRecognizedFeatures();
}
