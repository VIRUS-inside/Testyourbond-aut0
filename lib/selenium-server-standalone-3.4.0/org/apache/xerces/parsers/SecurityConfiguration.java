package org.apache.xerces.parsers;

import org.apache.xerces.util.SecurityManager;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLComponentManager;

public class SecurityConfiguration
  extends XIncludeAwareParserConfiguration
{
  protected static final String SECURITY_MANAGER_PROPERTY = "http://apache.org/xml/properties/security-manager";
  
  public SecurityConfiguration()
  {
    this(null, null, null);
  }
  
  public SecurityConfiguration(SymbolTable paramSymbolTable)
  {
    this(paramSymbolTable, null, null);
  }
  
  public SecurityConfiguration(SymbolTable paramSymbolTable, XMLGrammarPool paramXMLGrammarPool)
  {
    this(paramSymbolTable, paramXMLGrammarPool, null);
  }
  
  public SecurityConfiguration(SymbolTable paramSymbolTable, XMLGrammarPool paramXMLGrammarPool, XMLComponentManager paramXMLComponentManager)
  {
    super(paramSymbolTable, paramXMLGrammarPool, paramXMLComponentManager);
    setProperty("http://apache.org/xml/properties/security-manager", new SecurityManager());
  }
}
