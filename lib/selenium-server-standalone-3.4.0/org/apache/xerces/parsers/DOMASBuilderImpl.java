package org.apache.xerces.parsers;

import java.util.Vector;
import org.apache.xerces.dom.ASModelImpl;
import org.apache.xerces.dom3.as.ASModel;
import org.apache.xerces.dom3.as.DOMASBuilder;
import org.apache.xerces.dom3.as.DOMASException;
import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.XSGrammarBucket;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLGrammarPoolImpl;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xni.parser.XMLParserConfiguration;
import org.w3c.dom.ls.LSInput;

/**
 * @deprecated
 */
public class DOMASBuilderImpl
  extends DOMParserImpl
  implements DOMASBuilder
{
  protected static final String SCHEMA_FULL_CHECKING = "http://apache.org/xml/features/validation/schema-full-checking";
  protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
  protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
  protected static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
  protected XSGrammarBucket fGrammarBucket;
  protected ASModelImpl fAbstractSchema;
  
  public DOMASBuilderImpl()
  {
    super(new XMLGrammarCachingConfiguration());
  }
  
  public DOMASBuilderImpl(XMLGrammarCachingConfiguration paramXMLGrammarCachingConfiguration)
  {
    super(paramXMLGrammarCachingConfiguration);
  }
  
  public DOMASBuilderImpl(SymbolTable paramSymbolTable)
  {
    super(new XMLGrammarCachingConfiguration(paramSymbolTable));
  }
  
  public DOMASBuilderImpl(SymbolTable paramSymbolTable, XMLGrammarPool paramXMLGrammarPool)
  {
    super(new XMLGrammarCachingConfiguration(paramSymbolTable, paramXMLGrammarPool));
  }
  
  public ASModel getAbstractSchema()
  {
    return fAbstractSchema;
  }
  
  public void setAbstractSchema(ASModel paramASModel)
  {
    fAbstractSchema = ((ASModelImpl)paramASModel);
    Object localObject = (XMLGrammarPool)fConfiguration.getProperty("http://apache.org/xml/properties/internal/grammar-pool");
    if (localObject == null)
    {
      localObject = new XMLGrammarPoolImpl();
      fConfiguration.setProperty("http://apache.org/xml/properties/internal/grammar-pool", localObject);
    }
    if (fAbstractSchema != null) {
      initGrammarPool(fAbstractSchema, (XMLGrammarPool)localObject);
    }
  }
  
  public ASModel parseASURI(String paramString)
    throws DOMASException, Exception
  {
    XMLInputSource localXMLInputSource = new XMLInputSource(null, paramString, null);
    return parseASInputSource(localXMLInputSource);
  }
  
  public ASModel parseASInputSource(LSInput paramLSInput)
    throws DOMASException, Exception
  {
    XMLInputSource localXMLInputSource = dom2xmlInputSource(paramLSInput);
    try
    {
      return parseASInputSource(localXMLInputSource);
    }
    catch (XNIException localXNIException)
    {
      Exception localException = localXNIException.getException();
      throw localException;
    }
  }
  
  ASModel parseASInputSource(XMLInputSource paramXMLInputSource)
    throws Exception
  {
    if (fGrammarBucket == null) {
      fGrammarBucket = new XSGrammarBucket();
    }
    initGrammarBucket();
    XMLGrammarCachingConfiguration localXMLGrammarCachingConfiguration = (XMLGrammarCachingConfiguration)fConfiguration;
    localXMLGrammarCachingConfiguration.lockGrammarPool();
    SchemaGrammar localSchemaGrammar = localXMLGrammarCachingConfiguration.parseXMLSchema(paramXMLInputSource);
    localXMLGrammarCachingConfiguration.unlockGrammarPool();
    ASModelImpl localASModelImpl = null;
    if (localSchemaGrammar != null)
    {
      localASModelImpl = new ASModelImpl();
      fGrammarBucket.putGrammar(localSchemaGrammar, true);
      addGrammars(localASModelImpl, fGrammarBucket);
    }
    return localASModelImpl;
  }
  
  private void initGrammarBucket()
  {
    fGrammarBucket.reset();
    if (fAbstractSchema != null) {
      initGrammarBucketRecurse(fAbstractSchema);
    }
  }
  
  private void initGrammarBucketRecurse(ASModelImpl paramASModelImpl)
  {
    if (paramASModelImpl.getGrammar() != null) {
      fGrammarBucket.putGrammar(paramASModelImpl.getGrammar());
    }
    for (int i = 0; i < paramASModelImpl.getInternalASModels().size(); i++)
    {
      ASModelImpl localASModelImpl = (ASModelImpl)paramASModelImpl.getInternalASModels().elementAt(i);
      initGrammarBucketRecurse(localASModelImpl);
    }
  }
  
  private void addGrammars(ASModelImpl paramASModelImpl, XSGrammarBucket paramXSGrammarBucket)
  {
    SchemaGrammar[] arrayOfSchemaGrammar = paramXSGrammarBucket.getGrammars();
    for (int i = 0; i < arrayOfSchemaGrammar.length; i++)
    {
      ASModelImpl localASModelImpl = new ASModelImpl();
      localASModelImpl.setGrammar(arrayOfSchemaGrammar[i]);
      paramASModelImpl.addASModel(localASModelImpl);
    }
  }
  
  private void initGrammarPool(ASModelImpl paramASModelImpl, XMLGrammarPool paramXMLGrammarPool)
  {
    Grammar[] arrayOfGrammar = new Grammar[1];
    if ((arrayOfGrammar[0] =  = paramASModelImpl.getGrammar()) != null) {
      paramXMLGrammarPool.cacheGrammars(arrayOfGrammar[0].getGrammarDescription().getGrammarType(), arrayOfGrammar);
    }
    Vector localVector = paramASModelImpl.getInternalASModels();
    for (int i = 0; i < localVector.size(); i++) {
      initGrammarPool((ASModelImpl)localVector.elementAt(i), paramXMLGrammarPool);
    }
  }
}
