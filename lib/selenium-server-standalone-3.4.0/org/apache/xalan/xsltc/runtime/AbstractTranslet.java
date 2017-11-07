package org.apache.xalan.xsltc.runtime;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Templates;
import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.DOMCache;
import org.apache.xalan.xsltc.DOMEnhancedForDTM;
import org.apache.xalan.xsltc.Translet;
import org.apache.xalan.xsltc.TransletException;
import org.apache.xalan.xsltc.dom.DOMAdapter;
import org.apache.xalan.xsltc.dom.KeyIndex;
import org.apache.xalan.xsltc.runtime.output.TransletOutputHandlerFactory;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.serializer.SerializationHandler;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;































public abstract class AbstractTranslet
  implements Translet
{
  public String _version = "1.0";
  public String _method = null;
  public String _encoding = "UTF-8";
  public boolean _omitHeader = false;
  public String _standalone = null;
  public String _doctypePublic = null;
  public String _doctypeSystem = null;
  public boolean _indent = false;
  public String _mediaType = null;
  public Vector _cdata = null;
  public int _indentamount = -1;
  

  public static final int FIRST_TRANSLET_VERSION = 100;
  

  public static final int VER_SPLIT_NAMES_ARRAY = 101;
  

  public static final int CURRENT_TRANSLET_VERSION = 101;
  
  protected int transletVersion = 100;
  
  protected String[] namesArray;
  
  protected String[] urisArray;
  
  protected int[] typesArray;
  
  protected String[] namespaceArray;
  protected Templates _templates = null;
  

  protected boolean _hasIdCall = false;
  

  protected StringValueHandler stringValueHandler = new StringValueHandler();
  

  private static final String EMPTYSTRING = "";
  
  private static final String ID_INDEX_NAME = "##id";
  

  public AbstractTranslet() {}
  

  public void printInternalState()
  {
    System.out.println("-------------------------------------");
    System.out.println("AbstractTranslet this = " + this);
    System.out.println("pbase = " + pbase);
    System.out.println("vframe = " + pframe);
    System.out.println("paramsStack.size() = " + paramsStack.size());
    System.out.println("namesArray.size = " + namesArray.length);
    System.out.println("namespaceArray.size = " + namespaceArray.length);
    System.out.println("");
    System.out.println("Total memory = " + Runtime.getRuntime().totalMemory());
  }
  




  public final DOMAdapter makeDOMAdapter(DOM dom)
    throws TransletException
  {
    setRootForKeys(dom.getDocument());
    return new DOMAdapter(dom, namesArray, urisArray, typesArray, namespaceArray);
  }
  






  protected int pbase = 0; protected int pframe = 0;
  protected ArrayList paramsStack = new ArrayList();
  


  public final void pushParamFrame()
  {
    paramsStack.add(pframe, new Integer(pbase));
    pbase = (++pframe);
  }
  


  public final void popParamFrame()
  {
    if (pbase > 0) {
      int oldpbase = ((Integer)paramsStack.get(--pbase)).intValue();
      for (int i = pframe - 1; i >= pbase; i--) {
        paramsStack.remove(i);
      }
      pframe = pbase;pbase = oldpbase;
    }
  }
  







  public final Object addParameter(String name, Object value)
  {
    name = BasisLibrary.mapQNameToJavaName(name);
    return addParameter(name, value, false);
  }
  








  public final Object addParameter(String name, Object value, boolean isDefault)
  {
    for (int i = pframe - 1; i >= pbase; i--) {
      Parameter param = (Parameter)paramsStack.get(i);
      
      if (_name.equals(name))
      {

        if ((_isDefault) || (!isDefault)) {
          _value = value;
          _isDefault = isDefault;
          return value;
        }
        return _value;
      }
    }
    

    paramsStack.add(pframe++, new Parameter(name, value, isDefault));
    return value;
  }
  


  public void clearParameters()
  {
    pbase = (this.pframe = 0);
    paramsStack.clear();
  }
  




  public final Object getParameter(String name)
  {
    name = BasisLibrary.mapQNameToJavaName(name);
    
    for (int i = pframe - 1; i >= pbase; i--) {
      Parameter param = (Parameter)paramsStack.get(i);
      if (_name.equals(name)) return _value;
    }
    return null;
  }
  







  private MessageHandler _msgHandler = null;
  


  public final void setMessageHandler(MessageHandler handler)
  {
    _msgHandler = handler;
  }
  


  public final void displayMessage(String msg)
  {
    if (_msgHandler == null) {
      System.err.println(msg);
    }
    else {
      _msgHandler.displayMessage(msg);
    }
  }
  





  public Hashtable _formatSymbols = null;
  




  public void addDecimalFormat(String name, DecimalFormatSymbols symbols)
  {
    if (_formatSymbols == null) { _formatSymbols = new Hashtable();
    }
    
    if (name == null) { name = "";
    }
    
    DecimalFormat df = new DecimalFormat();
    if (symbols != null) {
      df.setDecimalFormatSymbols(symbols);
    }
    _formatSymbols.put(name, df);
  }
  



  public final DecimalFormat getDecimalFormat(String name)
  {
    if (_formatSymbols != null)
    {
      if (name == null) { name = "";
      }
      DecimalFormat df = (DecimalFormat)_formatSymbols.get(name);
      if (df == null) df = (DecimalFormat)_formatSymbols.get("");
      return df;
    }
    return null;
  }
  





  public final void prepassDocument(DOM document)
  {
    setIndexSize(document.getSize());
    buildIDIndex(document);
  }
  




  private final void buildIDIndex(DOM document)
  {
    setRootForKeys(document.getDocument());
    
    if ((document instanceof DOMEnhancedForDTM)) {
      DOMEnhancedForDTM enhancedDOM = (DOMEnhancedForDTM)document;
      



      if (enhancedDOM.hasDOMSource()) {
        buildKeyIndex("##id", document);
        return;
      }
      
      Hashtable elementsByID = enhancedDOM.getElementsWithIDs();
      
      if (elementsByID == null) {
        return;
      }
      



      Enumeration idValues = elementsByID.keys();
      boolean hasIDValues = false;
      
      while (idValues.hasMoreElements()) {
        Object idValue = idValues.nextElement();
        int element = document.getNodeHandle(((Integer)elementsByID.get(idValue)).intValue());
        



        buildKeyIndex("##id", element, idValue);
        hasIDValues = true;
      }
      
      if (hasIDValues) {
        setKeyIndexDom("##id", document);
      }
    }
  }
  






  public final void postInitialization()
  {
    if (transletVersion < 101) {
      int arraySize = namesArray.length;
      String[] newURIsArray = new String[arraySize];
      String[] newNamesArray = new String[arraySize];
      int[] newTypesArray = new int[arraySize];
      
      for (int i = 0; i < arraySize; i++) {
        String name = namesArray[i];
        int colonIndex = name.lastIndexOf(':');
        int lNameStartIdx = colonIndex + 1;
        
        if (colonIndex > -1) {
          newURIsArray[i] = name.substring(0, colonIndex);
        }
        


        if (name.charAt(lNameStartIdx) == '@') {
          lNameStartIdx++;
          newTypesArray[i] = 2;
        } else if (name.charAt(lNameStartIdx) == '?') {
          lNameStartIdx++;
          newTypesArray[i] = 13;
        } else {
          newTypesArray[i] = 1;
        }
        newNamesArray[i] = (lNameStartIdx == 0 ? name : name.substring(lNameStartIdx));
      }
      


      namesArray = newNamesArray;
      urisArray = newURIsArray;
      typesArray = newTypesArray;
    }
    



    if (transletVersion > 101) {
      BasisLibrary.runTimeError("UNKNOWN_TRANSLET_VERSION_ERR", getClass().getName());
    }
  }
  






  private Hashtable _keyIndexes = null;
  private KeyIndex _emptyKeyIndex = null;
  private int _indexSize = 0;
  private int _currentRootForKeys = 0;
  



  public void setIndexSize(int size)
  {
    if (size > _indexSize) { _indexSize = size;
    }
  }
  

  public KeyIndex createKeyIndex()
  {
    return new KeyIndex(_indexSize);
  }
  





  public void buildKeyIndex(String name, int node, Object value)
  {
    if (_keyIndexes == null) { _keyIndexes = new Hashtable();
    }
    KeyIndex index = (KeyIndex)_keyIndexes.get(name);
    if (index == null) {
      _keyIndexes.put(name, index = new KeyIndex(_indexSize));
    }
    index.add(value, node, _currentRootForKeys);
  }
  




  public void buildKeyIndex(String name, DOM dom)
  {
    if (_keyIndexes == null) { _keyIndexes = new Hashtable();
    }
    KeyIndex index = (KeyIndex)_keyIndexes.get(name);
    if (index == null) {
      _keyIndexes.put(name, index = new KeyIndex(_indexSize));
    }
    index.setDom(dom);
  }
  




  public KeyIndex getKeyIndex(String name)
  {
    if (_keyIndexes == null) {
      return this._emptyKeyIndex = new KeyIndex(1);
    }
    



    KeyIndex index = (KeyIndex)_keyIndexes.get(name);
    

    if (index == null) {
      return this._emptyKeyIndex = new KeyIndex(1);
    }
    


    return index;
  }
  
  private void setRootForKeys(int root) {
    _currentRootForKeys = root;
  }
  





  public void buildKeys(DOM document, DTMAxisIterator iterator, SerializationHandler handler, int root)
    throws TransletException
  {}
  




  public void setKeyIndexDom(String name, DOM document)
  {
    getKeyIndex(name).setDom(document);
  }
  






  private DOMCache _domCache = null;
  



  public void setDOMCache(DOMCache cache)
  {
    _domCache = cache;
  }
  



  public DOMCache getDOMCache()
  {
    return _domCache;
  }
  




  public SerializationHandler openOutputHandler(String filename, boolean append)
    throws TransletException
  {
    try
    {
      TransletOutputHandlerFactory factory = TransletOutputHandlerFactory.newInstance();
      

      String dirStr = new File(filename).getParent();
      if ((null != dirStr) && (dirStr.length() > 0)) {
        File dir = new File(dirStr);
        dir.mkdirs();
      }
      
      factory.setEncoding(_encoding);
      factory.setOutputMethod(_method);
      factory.setWriter(new FileWriter(filename, append));
      factory.setOutputType(0);
      
      SerializationHandler handler = factory.getSerializationHandler();
      

      transferOutputSettings(handler);
      handler.startDocument();
      return handler;
    }
    catch (Exception e) {
      throw new TransletException(e);
    }
  }
  
  public SerializationHandler openOutputHandler(String filename)
    throws TransletException
  {
    return openOutputHandler(filename, false);
  }
  
  public void closeOutputHandler(SerializationHandler handler) {
    try {
      handler.endDocument();
      handler.close();
    }
    catch (Exception e) {}
  }
  






  public abstract void transform(DOM paramDOM, DTMAxisIterator paramDTMAxisIterator, SerializationHandler paramSerializationHandler)
    throws TransletException;
  





  public final void transform(DOM document, SerializationHandler handler)
    throws TransletException
  {
    try
    {
      transform(document, document.getIterator(), handler);
    } finally {
      _keyIndexes = null;
    }
  }
  




  public final void characters(String string, SerializationHandler handler)
    throws TransletException
  {
    if (string != null) {
      try
      {
        handler.characters(string);
      } catch (Exception e) {
        throw new TransletException(e);
      }
    }
  }
  


  public void addCdataElement(String name)
  {
    if (_cdata == null) {
      _cdata = new Vector();
    }
    
    int lastColon = name.lastIndexOf(':');
    
    if (lastColon > 0) {
      String uri = name.substring(0, lastColon);
      String localName = name.substring(lastColon + 1);
      _cdata.addElement(uri);
      _cdata.addElement(localName);
    } else {
      _cdata.addElement(null);
      _cdata.addElement(name);
    }
  }
  


  protected void transferOutputSettings(SerializationHandler handler)
  {
    if (_method != null) {
      if (_method.equals("xml")) {
        if (_standalone != null) {
          handler.setStandalone(_standalone);
        }
        if (_omitHeader) {
          handler.setOmitXMLDeclaration(true);
        }
        handler.setCdataSectionElements(_cdata);
        if (_version != null) {
          handler.setVersion(_version);
        }
        handler.setIndent(_indent);
        handler.setIndentAmount(_indentamount);
        if (_doctypeSystem != null) {
          handler.setDoctype(_doctypeSystem, _doctypePublic);
        }
      }
      else if (_method.equals("html")) {
        handler.setIndent(_indent);
        handler.setDoctype(_doctypeSystem, _doctypePublic);
        if (_mediaType != null) {
          handler.setMediaType(_mediaType);
        }
      }
    }
    else {
      handler.setCdataSectionElements(_cdata);
      if (_version != null) {
        handler.setVersion(_version);
      }
      if (_standalone != null) {
        handler.setStandalone(_standalone);
      }
      if (_omitHeader) {
        handler.setOmitXMLDeclaration(true);
      }
      handler.setIndent(_indent);
      handler.setDoctype(_doctypeSystem, _doctypePublic);
    }
  }
  
  private Hashtable _auxClasses = null;
  
  public void addAuxiliaryClass(Class auxClass) {
    if (_auxClasses == null) _auxClasses = new Hashtable();
    _auxClasses.put(auxClass.getName(), auxClass);
  }
  
  public void setAuxiliaryClasses(Hashtable auxClasses) {
    _auxClasses = auxClasses;
  }
  
  public Class getAuxiliaryClass(String className) {
    if (_auxClasses == null) return null;
    return (Class)_auxClasses.get(className);
  }
  
  public String[] getNamesArray()
  {
    return namesArray;
  }
  
  public String[] getUrisArray() {
    return urisArray;
  }
  
  public int[] getTypesArray() {
    return typesArray;
  }
  
  public String[] getNamespaceArray() {
    return namespaceArray;
  }
  
  public boolean hasIdCall() {
    return _hasIdCall;
  }
  
  public Templates getTemplates() {
    return _templates;
  }
  
  public void setTemplates(Templates templates) {
    _templates = templates;
  }
  



  protected DOMImplementation _domImplementation = null;
  
  public Document newDocument(String uri, String qname)
    throws ParserConfigurationException
  {
    if (_domImplementation == null) {
      _domImplementation = DocumentBuilderFactory.newInstance().newDocumentBuilder().getDOMImplementation();
    }
    
    return _domImplementation.createDocument(uri, qname, null);
  }
}
