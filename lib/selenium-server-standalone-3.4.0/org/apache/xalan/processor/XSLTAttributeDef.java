package org.apache.xalan.processor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.AVT;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xml.utils.QName;
import org.apache.xml.utils.StringToIntTable;
import org.apache.xml.utils.StringVector;
import org.apache.xml.utils.XML11Char;
import org.apache.xpath.XPath;
import org.xml.sax.SAXException;
























public class XSLTAttributeDef
{
  static final int FATAL = 0;
  static final int ERROR = 1;
  static final int WARNING = 2;
  static final int T_CDATA = 1;
  static final int T_URL = 2;
  static final int T_AVT = 3;
  static final int T_PATTERN = 4;
  static final int T_EXPR = 5;
  static final int T_CHAR = 6;
  static final int T_NUMBER = 7;
  static final int T_YESNO = 8;
  static final int T_QNAME = 9;
  static final int T_QNAMES = 10;
  static final int T_ENUM = 11;
  static final int T_SIMPLEPATTERNLIST = 12;
  static final int T_NMTOKEN = 13;
  static final int T_STRINGLIST = 14;
  static final int T_PREFIX_URLLIST = 15;
  static final int T_ENUM_OR_PQNAME = 16;
  static final int T_NCNAME = 17;
  static final int T_AVT_QNAME = 18;
  static final int T_QNAMES_RESOLVE_NULL = 19;
  static final int T_PREFIXLIST = 20;
  
  XSLTAttributeDef(String namespace, String name, int type, boolean required, boolean supportsAVT, int errorType)
  {
    m_namespace = namespace;
    m_name = name;
    m_type = type;
    m_required = required;
    m_supportsAVT = supportsAVT;
    m_errorType = errorType;
  }
  














  XSLTAttributeDef(String namespace, String name, int type, boolean supportsAVT, int errorType, String defaultVal)
  {
    m_namespace = namespace;
    m_name = name;
    m_type = type;
    m_required = false;
    m_supportsAVT = supportsAVT;
    m_errorType = errorType;
    m_default = defaultVal;
  }
  

















  XSLTAttributeDef(String namespace, String name, boolean required, boolean supportsAVT, boolean prefixedQNameValAllowed, int errorType, String k1, int v1, String k2, int v2)
  {
    m_namespace = namespace;
    m_name = name;
    m_type = (prefixedQNameValAllowed ? 16 : 11);
    m_required = required;
    m_supportsAVT = supportsAVT;
    m_errorType = errorType;
    m_enums = new StringToIntTable(2);
    
    m_enums.put(k1, v1);
    m_enums.put(k2, v2);
  }
  



















  XSLTAttributeDef(String namespace, String name, boolean required, boolean supportsAVT, boolean prefixedQNameValAllowed, int errorType, String k1, int v1, String k2, int v2, String k3, int v3)
  {
    m_namespace = namespace;
    m_name = name;
    m_type = (prefixedQNameValAllowed ? 16 : 11);
    m_required = required;
    m_supportsAVT = supportsAVT;
    m_errorType = errorType;
    m_enums = new StringToIntTable(3);
    
    m_enums.put(k1, v1);
    m_enums.put(k2, v2);
    m_enums.put(k3, v3);
  }
  





















  XSLTAttributeDef(String namespace, String name, boolean required, boolean supportsAVT, boolean prefixedQNameValAllowed, int errorType, String k1, int v1, String k2, int v2, String k3, int v3, String k4, int v4)
  {
    m_namespace = namespace;
    m_name = name;
    m_type = (prefixedQNameValAllowed ? 16 : 11);
    m_required = required;
    m_supportsAVT = supportsAVT;
    m_errorType = errorType;
    m_enums = new StringToIntTable(4);
    
    m_enums.put(k1, v1);
    m_enums.put(k2, v2);
    m_enums.put(k3, v3);
    m_enums.put(k4, v4);
  }
  






































































  static final XSLTAttributeDef m_foreignAttr = new XSLTAttributeDef("*", "*", 1, false, false, 2);
  
  static final String S_FOREIGNATTR_SETTER = "setForeignAttr";
  
  private String m_namespace;
  
  private String m_name;
  
  private int m_type;
  
  private StringToIntTable m_enums;
  
  private String m_default;
  private boolean m_required;
  private boolean m_supportsAVT;
  
  String getNamespace()
  {
    return m_namespace;
  }
  










  String getName()
  {
    return m_name;
  }
  












  int getType()
  {
    return m_type;
  }
  
















  private int getEnum(String key)
  {
    return m_enums.get(key);
  }
  








  private String[] getEnumNames()
  {
    return m_enums.keys();
  }
  










  String getDefault()
  {
    return m_default;
  }
  





  void setDefault(String def)
  {
    m_default = def;
  }
  










  boolean getRequired()
  {
    return m_required;
  }
  










  boolean getSupportsAVT()
  {
    return m_supportsAVT;
  }
  
  int m_errorType = 2;
  





  int getErrorType()
  {
    return m_errorType;
  }
  



  String m_setterString = null;
  











  public String getSetterMethodName()
  {
    if (null == m_setterString)
    {
      if (m_foreignAttr == this)
      {
        return "setForeignAttr";
      }
      if (m_name.equals("*"))
      {
        m_setterString = "addLiteralResultAttribute";
        
        return m_setterString;
      }
      
      StringBuffer outBuf = new StringBuffer();
      
      outBuf.append("set");
      
      if ((m_namespace != null) && (m_namespace.equals("http://www.w3.org/XML/1998/namespace")))
      {

        outBuf.append("Xml");
      }
      
      int n = m_name.length();
      
      for (int i = 0; i < n; i++)
      {
        char c = m_name.charAt(i);
        
        if ('-' == c)
        {
          i++;
          
          c = m_name.charAt(i);
          c = Character.toUpperCase(c);
        }
        else if (0 == i)
        {
          c = Character.toUpperCase(c);
        }
        
        outBuf.append(c);
      }
      
      m_setterString = outBuf.toString();
    }
    
    return m_setterString;
  }
  



















  AVT processAVT(StylesheetHandler handler, String uri, String name, String rawName, String value, ElemTemplateElement owner)
    throws SAXException
  {
    try
    {
      return new AVT(handler, uri, name, rawName, value, owner);

    }
    catch (TransformerException te)
    {

      throw new SAXException(te);
    }
  }
  















  Object processCDATA(StylesheetHandler handler, String uri, String name, String rawName, String value, ElemTemplateElement owner)
    throws SAXException
  {
    if (getSupportsAVT()) {
      try
      {
        return new AVT(handler, uri, name, rawName, value, owner);

      }
      catch (TransformerException te)
      {
        throw new SAXException(te);
      }
    }
    return value;
  }
  
















  Object processCHAR(StylesheetHandler handler, String uri, String name, String rawName, String value, ElemTemplateElement owner)
    throws SAXException
  {
    if (getSupportsAVT()) {
      try
      {
        AVT avt = new AVT(handler, uri, name, rawName, value, owner);
        

        if ((avt.isSimple()) && (value.length() != 1)) {
          handleError(handler, "INVALID_TCHAR", new Object[] { name, value }, null);
          return null;
        }
        return avt;
      }
      catch (TransformerException te)
      {
        throw new SAXException(te);
      }
    }
    if (value.length() != 1)
    {
      handleError(handler, "INVALID_TCHAR", new Object[] { name, value }, null);
      return null;
    }
    
    return new Character(value.charAt(0));
  }
  

















  Object processENUM(StylesheetHandler handler, String uri, String name, String rawName, String value, ElemTemplateElement owner)
    throws SAXException
  {
    AVT avt = null;
    if (getSupportsAVT()) {
      try
      {
        avt = new AVT(handler, uri, name, rawName, value, owner);
        

        if (!avt.isSimple()) return avt;
      }
      catch (TransformerException te)
      {
        throw new SAXException(te);
      }
    }
    
    int retVal = getEnum(value);
    
    if (retVal == 55536)
    {
      StringBuffer enumNamesList = getListOfEnums();
      handleError(handler, "INVALID_ENUM", new Object[] { name, value, enumNamesList.toString() }, null);
      return null;
    }
    
    if (getSupportsAVT()) return avt;
    return new Integer(retVal);
  }
  



















  Object processENUM_OR_PQNAME(StylesheetHandler handler, String uri, String name, String rawName, String value, ElemTemplateElement owner)
    throws SAXException
  {
    Object objToReturn = null;
    
    if (getSupportsAVT()) {
      try
      {
        AVT avt = new AVT(handler, uri, name, rawName, value, owner);
        if (!avt.isSimple()) return avt;
        objToReturn = avt;
      }
      catch (TransformerException te)
      {
        throw new SAXException(te);
      }
    }
    

    int key = getEnum(value);
    
    if (key != 55536)
    {
      if (objToReturn == null) { objToReturn = new Integer(key);
      }
      

    }
    else {
      try
      {
        QName qname = new QName(value, handler, true);
        if (objToReturn == null) { objToReturn = qname;
        }
        if (qname.getPrefix() == null) {
          StringBuffer enumNamesList = getListOfEnums();
          
          enumNamesList.append(" <qname-but-not-ncname>");
          handleError(handler, "INVALID_ENUM", new Object[] { name, value, enumNamesList.toString() }, null);
          return null;
        }
        
      }
      catch (IllegalArgumentException ie)
      {
        StringBuffer enumNamesList = getListOfEnums();
        enumNamesList.append(" <qname-but-not-ncname>");
        
        handleError(handler, "INVALID_ENUM", new Object[] { name, value, enumNamesList.toString() }, ie);
        return null;

      }
      catch (RuntimeException re)
      {
        StringBuffer enumNamesList = getListOfEnums();
        enumNamesList.append(" <qname-but-not-ncname>");
        
        handleError(handler, "INVALID_ENUM", new Object[] { name, value, enumNamesList.toString() }, re);
        return null;
      }
    }
    
    return objToReturn;
  }
  



















  Object processEXPR(StylesheetHandler handler, String uri, String name, String rawName, String value, ElemTemplateElement owner)
    throws SAXException
  {
    try
    {
      return handler.createXPath(value, owner);

    }
    catch (TransformerException te)
    {

      throw new SAXException(te);
    }
  }
  
















  Object processNMTOKEN(StylesheetHandler handler, String uri, String name, String rawName, String value, ElemTemplateElement owner)
    throws SAXException
  {
    if (getSupportsAVT()) {
      try
      {
        AVT avt = new AVT(handler, uri, name, rawName, value, owner);
        

        if ((avt.isSimple()) && (!XML11Char.isXML11ValidNmtoken(value))) {
          handleError(handler, "INVALID_NMTOKEN", new Object[] { name, value }, null);
          return null;
        }
        return avt;
      }
      catch (TransformerException te)
      {
        throw new SAXException(te);
      }
    }
    if (!XML11Char.isXML11ValidNmtoken(value)) {
      handleError(handler, "INVALID_NMTOKEN", new Object[] { name, value }, null);
      return null;
    }
    
    return value;
  }
  



















  Object processPATTERN(StylesheetHandler handler, String uri, String name, String rawName, String value, ElemTemplateElement owner)
    throws SAXException
  {
    try
    {
      return handler.createMatchPatternXPath(value, owner);

    }
    catch (TransformerException te)
    {

      throw new SAXException(te);
    }
  }
  




















  Object processNUMBER(StylesheetHandler handler, String uri, String name, String rawName, String value, ElemTemplateElement owner)
    throws SAXException
  {
    if (getSupportsAVT())
    {

      AVT avt = null;
      try
      {
        avt = new AVT(handler, uri, name, rawName, value, owner);
        

        if (avt.isSimple())
        {
          Double localDouble = Double.valueOf(value);
        }
      }
      catch (TransformerException te)
      {
        throw new SAXException(te);
      }
      catch (NumberFormatException nfe)
      {
        handleError(handler, "INVALID_NUMBER", new Object[] { name, value }, nfe);
        return null;
      }
      return avt;
    }
    


    try
    {
      return Double.valueOf(value);
    }
    catch (NumberFormatException nfe)
    {
      handleError(handler, "INVALID_NUMBER", new Object[] { name, value }, nfe); }
    return null;
  }
  




















  Object processQNAME(StylesheetHandler handler, String uri, String name, String rawName, String value, ElemTemplateElement owner)
    throws SAXException
  {
    try
    {
      return new QName(value, handler, true);

    }
    catch (IllegalArgumentException ie)
    {

      handleError(handler, "INVALID_QNAME", new Object[] { name, value }, ie);
      return null;
    }
    catch (RuntimeException re)
    {
      handleError(handler, "INVALID_QNAME", new Object[] { name, value }, re); }
    return null;
  }
  



















  Object processAVT_QNAME(StylesheetHandler handler, String uri, String name, String rawName, String value, ElemTemplateElement owner)
    throws SAXException
  {
    AVT avt = null;
    try
    {
      avt = new AVT(handler, uri, name, rawName, value, owner);
      

      if (avt.isSimple())
      {
        int indexOfNSSep = value.indexOf(':');
        
        if (indexOfNSSep >= 0)
        {
          String prefix = value.substring(0, indexOfNSSep);
          if (!XML11Char.isXML11ValidNCName(prefix))
          {
            handleError(handler, "INVALID_QNAME", new Object[] { name, value }, null);
            return null;
          }
        }
        
        String localName = indexOfNSSep < 0 ? value : value.substring(indexOfNSSep + 1);
        

        if ((localName == null) || (localName.length() == 0) || (!XML11Char.isXML11ValidNCName(localName)))
        {

          handleError(handler, "INVALID_QNAME", new Object[] { name, value }, null);
          return null;
        }
        
      }
    }
    catch (TransformerException te)
    {
      throw new SAXException(te);
    }
    
    return avt;
  }
  


















  Object processNCNAME(StylesheetHandler handler, String uri, String name, String rawName, String value, ElemTemplateElement owner)
    throws SAXException
  {
    if (getSupportsAVT())
    {
      AVT avt = null;
      try
      {
        avt = new AVT(handler, uri, name, rawName, value, owner);
        

        if ((avt.isSimple()) && (!XML11Char.isXML11ValidNCName(value)))
        {
          handleError(handler, "INVALID_NCNAME", new Object[] { name, value }, null);
          return null;
        }
        return avt;

      }
      catch (TransformerException te)
      {
        throw new SAXException(te);
      }
    }
    
    if (!XML11Char.isXML11ValidNCName(value))
    {
      handleError(handler, "INVALID_NCNAME", new Object[] { name, value }, null);
      return null;
    }
    return value;
  }
  




















  Vector processQNAMES(StylesheetHandler handler, String uri, String name, String rawName, String value)
    throws SAXException
  {
    StringTokenizer tokenizer = new StringTokenizer(value, " \t\n\r\f");
    int nQNames = tokenizer.countTokens();
    Vector qnames = new Vector(nQNames);
    
    for (int i = 0; i < nQNames; i++)
    {

      qnames.addElement(new QName(tokenizer.nextToken(), handler));
    }
    
    return qnames;
  }
  





















  final Vector processQNAMESRNU(StylesheetHandler handler, String uri, String name, String rawName, String value)
    throws SAXException
  {
    StringTokenizer tokenizer = new StringTokenizer(value, " \t\n\r\f");
    int nQNames = tokenizer.countTokens();
    Vector qnames = new Vector(nQNames);
    
    String defaultURI = handler.getNamespaceForPrefix("");
    for (int i = 0; i < nQNames; i++)
    {
      String tok = tokenizer.nextToken();
      if (tok.indexOf(':') == -1) {
        qnames.addElement(new QName(defaultURI, tok));
      } else {
        qnames.addElement(new QName(tok, handler));
      }
    }
    return qnames;
  }
  



















  Vector processSIMPLEPATTERNLIST(StylesheetHandler handler, String uri, String name, String rawName, String value, ElemTemplateElement owner)
    throws SAXException
  {
    try
    {
      StringTokenizer tokenizer = new StringTokenizer(value, " \t\n\r\f");
      int nPatterns = tokenizer.countTokens();
      Vector patterns = new Vector(nPatterns);
      
      for (int i = 0; i < nPatterns; i++)
      {
        XPath pattern = handler.createMatchPatternXPath(tokenizer.nextToken(), owner);
        

        patterns.addElement(pattern);
      }
      
      return patterns;
    }
    catch (TransformerException te)
    {
      throw new SAXException(te);
    }
  }
  














  StringVector processSTRINGLIST(StylesheetHandler handler, String uri, String name, String rawName, String value)
  {
    StringTokenizer tokenizer = new StringTokenizer(value, " \t\n\r\f");
    int nStrings = tokenizer.countTokens();
    StringVector strings = new StringVector(nStrings);
    
    for (int i = 0; i < nStrings; i++)
    {
      strings.addElement(tokenizer.nextToken());
    }
    
    return strings;
  }
  
















  StringVector processPREFIX_URLLIST(StylesheetHandler handler, String uri, String name, String rawName, String value)
    throws SAXException
  {
    StringTokenizer tokenizer = new StringTokenizer(value, " \t\n\r\f");
    int nStrings = tokenizer.countTokens();
    StringVector strings = new StringVector(nStrings);
    
    for (int i = 0; i < nStrings; i++)
    {
      String prefix = tokenizer.nextToken();
      String url = handler.getNamespaceForPrefix(prefix);
      
      if (url != null) {
        strings.addElement(url);
      } else {
        throw new SAXException(XSLMessages.createMessage("ER_CANT_RESOLVE_NSPREFIX", new Object[] { prefix }));
      }
    }
    
    return strings;
  }
  
















  StringVector processPREFIX_LIST(StylesheetHandler handler, String uri, String name, String rawName, String value)
    throws SAXException
  {
    StringTokenizer tokenizer = new StringTokenizer(value, " \t\n\r\f");
    int nStrings = tokenizer.countTokens();
    StringVector strings = new StringVector(nStrings);
    
    for (int i = 0; i < nStrings; i++)
    {
      String prefix = tokenizer.nextToken();
      String url = handler.getNamespaceForPrefix(prefix);
      if ((prefix.equals("#default")) || (url != null)) {
        strings.addElement(prefix);
      } else {
        throw new SAXException(XSLMessages.createMessage("ER_CANT_RESOLVE_NSPREFIX", new Object[] { prefix }));
      }
    }
    



    return strings;
  }
  


















  Object processURL(StylesheetHandler handler, String uri, String name, String rawName, String value, ElemTemplateElement owner)
    throws SAXException
  {
    if (getSupportsAVT()) {
      try
      {
        return new AVT(handler, uri, name, rawName, value, owner);




      }
      catch (TransformerException te)
      {




        throw new SAXException(te);
      }
    }
    



    return value;
  }
  


















  private Boolean processYESNO(StylesheetHandler handler, String uri, String name, String rawName, String value)
    throws SAXException
  {
    if ((!value.equals("yes")) && (!value.equals("no")))
    {
      handleError(handler, "INVALID_BOOLEAN", new Object[] { name, value }, null);
      return null;
    }
    
    return new Boolean(value.equals("yes"));
  }
  
















  Object processValue(StylesheetHandler handler, String uri, String name, String rawName, String value, ElemTemplateElement owner)
    throws SAXException
  {
    int type = getType();
    Object processedValue = null;
    
    switch (type)
    {
    case 3: 
      processedValue = processAVT(handler, uri, name, rawName, value, owner);
      break;
    case 1: 
      processedValue = processCDATA(handler, uri, name, rawName, value, owner);
      break;
    case 6: 
      processedValue = processCHAR(handler, uri, name, rawName, value, owner);
      break;
    case 11: 
      processedValue = processENUM(handler, uri, name, rawName, value, owner);
      break;
    case 5: 
      processedValue = processEXPR(handler, uri, name, rawName, value, owner);
      break;
    case 13: 
      processedValue = processNMTOKEN(handler, uri, name, rawName, value, owner);
      break;
    case 4: 
      processedValue = processPATTERN(handler, uri, name, rawName, value, owner);
      break;
    case 7: 
      processedValue = processNUMBER(handler, uri, name, rawName, value, owner);
      break;
    case 9: 
      processedValue = processQNAME(handler, uri, name, rawName, value, owner);
      break;
    case 10: 
      processedValue = processQNAMES(handler, uri, name, rawName, value);
      break;
    case 19: 
      processedValue = processQNAMESRNU(handler, uri, name, rawName, value);
      break;
    case 12: 
      processedValue = processSIMPLEPATTERNLIST(handler, uri, name, rawName, value, owner);
      
      break;
    case 2: 
      processedValue = processURL(handler, uri, name, rawName, value, owner);
      break;
    case 8: 
      processedValue = processYESNO(handler, uri, name, rawName, value);
      break;
    case 14: 
      processedValue = processSTRINGLIST(handler, uri, name, rawName, value);
      break;
    case 15: 
      processedValue = processPREFIX_URLLIST(handler, uri, name, rawName, value);
      
      break;
    case 16: 
      processedValue = processENUM_OR_PQNAME(handler, uri, name, rawName, value, owner);
      break;
    case 17: 
      processedValue = processNCNAME(handler, uri, name, rawName, value, owner);
      break;
    case 18: 
      processedValue = processAVT_QNAME(handler, uri, name, rawName, value, owner);
      break;
    case 20: 
      processedValue = processPREFIX_LIST(handler, uri, name, rawName, value);
      
      break;
    }
    
    

    return processedValue;
  }
  









  void setDefAttrValue(StylesheetHandler handler, ElemTemplateElement elem)
    throws SAXException
  {
    setAttrValue(handler, getNamespace(), getName(), getName(), getDefault(), elem);
  }
  













  private Class getPrimativeClass(Object obj)
  {
    if ((obj instanceof XPath)) {
      return XPath.class;
    }
    Class cl = obj.getClass();
    
    if (cl == Double.class)
    {
      cl = Double.TYPE;
    }
    
    if (cl == Float.class)
    {
      cl = Float.TYPE;
    }
    else if (cl == Boolean.class)
    {
      cl = Boolean.TYPE;
    }
    else if (cl == Byte.class)
    {
      cl = Byte.TYPE;
    }
    else if (cl == Character.class)
    {
      cl = Character.TYPE;
    }
    else if (cl == Short.class)
    {
      cl = Short.TYPE;
    }
    else if (cl == Integer.class)
    {
      cl = Integer.TYPE;
    }
    else if (cl == Long.class)
    {
      cl = Long.TYPE;
    }
    
    return cl;
  }
  




  private StringBuffer getListOfEnums()
  {
    StringBuffer enumNamesList = new StringBuffer();
    String[] enumValues = getEnumNames();
    
    for (int i = 0; i < enumValues.length; i++)
    {
      if (i > 0)
      {
        enumNamesList.append(' ');
      }
      enumNamesList.append(enumValues[i]);
    }
    return enumNamesList;
  }
  














  boolean setAttrValue(StylesheetHandler handler, String attrUri, String attrLocalName, String attrRawName, String attrValue, ElemTemplateElement elem)
    throws SAXException
  {
    if ((attrRawName.equals("xmlns")) || (attrRawName.startsWith("xmlns:"))) {
      return true;
    }
    String setterString = getSetterMethodName();
    


    if (null != setterString) {
      try
      {
        Object[] args;
        
        Method meth;
        Object[] args;
        if (setterString.equals("setForeignAttr"))
        {

          if (attrUri == null) { attrUri = "";
          }
          Class sclass = attrUri.getClass();
          Class[] argTypes = { sclass, sclass, sclass, sclass };
          

          Method meth = elem.getClass().getMethod(setterString, argTypes);
          
          args = new Object[] { attrUri, attrLocalName, attrRawName, attrValue };

        }
        else
        {
          Object value = processValue(handler, attrUri, attrLocalName, attrRawName, attrValue, elem);
          


          if (null == value) { return false;
          }
          
          Class[] argTypes = { getPrimativeClass(value) };
          
          try
          {
            meth = elem.getClass().getMethod(setterString, argTypes);
          }
          catch (NoSuchMethodException nsme)
          {
            Class cl = value.getClass();
            

            argTypes[0] = cl;
            meth = elem.getClass().getMethod(setterString, argTypes);
          }
          
          args = new Object[] { value };
        }
        
        meth.invoke(elem, args);
      }
      catch (NoSuchMethodException nsme)
      {
        if (!setterString.equals("setForeignAttr"))
        {
          handler.error("ER_FAILED_CALLING_METHOD", new Object[] { setterString }, nsme);
          return false;
        }
      }
      catch (IllegalAccessException iae)
      {
        handler.error("ER_FAILED_CALLING_METHOD", new Object[] { setterString }, iae);
        return false;
      }
      catch (InvocationTargetException nsme)
      {
        handleError(handler, "WG_ILLEGAL_ATTRIBUTE_VALUE", new Object[] { "name", getName() }, nsme);
        
        return false;
      }
    }
    
    return true;
  }
  
  private void handleError(StylesheetHandler handler, String msg, Object[] args, Exception exc) throws SAXException
  {
    switch (getErrorType())
    {
    case 0: 
    case 1: 
      handler.error(msg, args, exc);
      break;
    case 2: 
      handler.warn(msg, args);
    }
  }
}
