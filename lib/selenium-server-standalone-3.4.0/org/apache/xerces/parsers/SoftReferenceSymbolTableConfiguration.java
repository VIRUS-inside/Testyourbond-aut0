package org.apache.xerces.parsers;

import org.apache.xerces.util.SoftReferenceSymbolTable;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLComponentManager;

public class SoftReferenceSymbolTableConfiguration
  extends XIncludeAwareParserConfiguration
{
  public SoftReferenceSymbolTableConfiguration()
  {
    this(new SoftReferenceSymbolTable(), null, null);
  }
  
  public SoftReferenceSymbolTableConfiguration(SymbolTable paramSymbolTable)
  {
    this(paramSymbolTable, null, null);
  }
  
  public SoftReferenceSymbolTableConfiguration(SymbolTable paramSymbolTable, XMLGrammarPool paramXMLGrammarPool)
  {
    this(paramSymbolTable, paramXMLGrammarPool, null);
  }
  
  public SoftReferenceSymbolTableConfiguration(SymbolTable paramSymbolTable, XMLGrammarPool paramXMLGrammarPool, XMLComponentManager paramXMLComponentManager)
  {
    super(paramSymbolTable, paramXMLGrammarPool, paramXMLComponentManager);
  }
}
