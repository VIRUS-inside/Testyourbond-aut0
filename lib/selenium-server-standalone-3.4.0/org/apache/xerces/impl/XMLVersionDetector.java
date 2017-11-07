package org.apache.xerces.impl;

import java.io.CharConversionException;
import java.io.EOFException;
import java.io.IOException;
import org.apache.xerces.impl.io.MalformedByteSequenceException;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLInputSource;

public class XMLVersionDetector
{
  private static final char[] XML11_VERSION = { '1', '.', '1' };
  protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
  protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
  protected static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
  protected static final String fVersionSymbol = "version".intern();
  protected static final String fXMLSymbol = "[xml]".intern();
  protected SymbolTable fSymbolTable;
  protected XMLErrorReporter fErrorReporter;
  protected XMLEntityManager fEntityManager;
  protected String fEncoding = null;
  private final char[] fExpectedVersionString = { '<', '?', 'x', 'm', 'l', ' ', 'v', 'e', 'r', 's', 'i', 'o', 'n', '=', ' ', ' ', ' ', ' ', ' ' };
  
  public XMLVersionDetector() {}
  
  public void reset(XMLComponentManager paramXMLComponentManager)
    throws XMLConfigurationException
  {
    fSymbolTable = ((SymbolTable)paramXMLComponentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table"));
    fErrorReporter = ((XMLErrorReporter)paramXMLComponentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter"));
    fEntityManager = ((XMLEntityManager)paramXMLComponentManager.getProperty("http://apache.org/xml/properties/internal/entity-manager"));
    for (int i = 14; i < fExpectedVersionString.length; i++) {
      fExpectedVersionString[i] = ' ';
    }
  }
  
  public void startDocumentParsing(XMLEntityHandler paramXMLEntityHandler, short paramShort)
  {
    if (paramShort == 1) {
      fEntityManager.setScannerVersion((short)1);
    } else {
      fEntityManager.setScannerVersion((short)2);
    }
    fErrorReporter.setDocumentLocator(fEntityManager.getEntityScanner());
    fEntityManager.setEntityHandler(paramXMLEntityHandler);
    paramXMLEntityHandler.startEntity(fXMLSymbol, fEntityManager.getCurrentResourceIdentifier(), fEncoding, null);
  }
  
  public short determineDocVersion(XMLInputSource paramXMLInputSource)
    throws IOException
  {
    fEncoding = fEntityManager.setupCurrentEntity(fXMLSymbol, paramXMLInputSource, false, true);
    fEntityManager.setScannerVersion((short)1);
    XMLEntityScanner localXMLEntityScanner = fEntityManager.getEntityScanner();
    try
    {
      if (!localXMLEntityScanner.skipString("<?xml")) {
        return 1;
      }
      if (!localXMLEntityScanner.skipDeclSpaces())
      {
        fixupCurrentEntity(fEntityManager, fExpectedVersionString, 5);
        return 1;
      }
      if (!localXMLEntityScanner.skipString("version"))
      {
        fixupCurrentEntity(fEntityManager, fExpectedVersionString, 6);
        return 1;
      }
      localXMLEntityScanner.skipDeclSpaces();
      if (localXMLEntityScanner.peekChar() != 61)
      {
        fixupCurrentEntity(fEntityManager, fExpectedVersionString, 13);
        return 1;
      }
      localXMLEntityScanner.scanChar();
      localXMLEntityScanner.skipDeclSpaces();
      int i = localXMLEntityScanner.scanChar();
      fExpectedVersionString[14] = ((char)i);
      for (int j = 0; j < XML11_VERSION.length; j++) {
        fExpectedVersionString[(15 + j)] = ((char)localXMLEntityScanner.scanChar());
      }
      fExpectedVersionString[18] = ((char)localXMLEntityScanner.scanChar());
      fixupCurrentEntity(fEntityManager, fExpectedVersionString, 19);
      for (int k = 0; k < XML11_VERSION.length; k++) {
        if (fExpectedVersionString[(15 + k)] != XML11_VERSION[k]) {
          break;
        }
      }
      return k == XML11_VERSION.length ? 2 : 1;
    }
    catch (MalformedByteSequenceException localMalformedByteSequenceException)
    {
      fErrorReporter.reportError(localMalformedByteSequenceException.getDomain(), localMalformedByteSequenceException.getKey(), localMalformedByteSequenceException.getArguments(), (short)2, localMalformedByteSequenceException);
      return -1;
    }
    catch (CharConversionException localCharConversionException)
    {
      fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "CharConversionFailure", null, (short)2, localCharConversionException);
      return -1;
    }
    catch (EOFException localEOFException)
    {
      fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "PrematureEOF", null, (short)2);
    }
    return -1;
  }
  
  private void fixupCurrentEntity(XMLEntityManager paramXMLEntityManager, char[] paramArrayOfChar, int paramInt)
  {
    XMLEntityManager.ScannedEntity localScannedEntity = paramXMLEntityManager.getCurrentEntity();
    if (count - position + paramInt > ch.length)
    {
      char[] arrayOfChar = ch;
      ch = new char[paramInt + count - position + 1];
      System.arraycopy(arrayOfChar, 0, ch, 0, arrayOfChar.length);
    }
    if (position < paramInt)
    {
      System.arraycopy(ch, position, ch, paramInt, count - position);
      count += paramInt - position;
    }
    else
    {
      for (int i = paramInt; i < position; i++) {
        ch[i] = ' ';
      }
    }
    System.arraycopy(paramArrayOfChar, 0, ch, 0, paramInt);
    position = 0;
    baseCharOffset = 0;
    startPosition = 0;
    columnNumber = (localScannedEntity.lineNumber = 1);
  }
}
