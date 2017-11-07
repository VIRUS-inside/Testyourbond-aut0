package org.apache.xml.serializer.dom3;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Properties;
import java.util.StringTokenizer;
import org.apache.xml.serializer.DOM3Serializer;
import org.apache.xml.serializer.Encodings;
import org.apache.xml.serializer.OutputPropertiesFactory;
import org.apache.xml.serializer.Serializer;
import org.apache.xml.serializer.SerializerFactory;
import org.apache.xml.serializer.utils.Messages;
import org.apache.xml.serializer.utils.SystemIDResolver;
import org.apache.xml.serializer.utils.Utils;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DOMStringList;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.ls.LSException;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.w3c.dom.ls.LSSerializerFilter;

































public final class LSSerializerImpl
  implements DOMConfiguration, LSSerializer
{
  private static final String DEFAULT_END_OF_LINE;
  
  static
  {
    String lineSeparator = (String)AccessController.doPrivileged(new PrivilegedAction() {
      public Object run() {
        try {
          return System.getProperty("line.separator");
        }
        catch (SecurityException ex) {}
        return null;

      }
      

    });
    DEFAULT_END_OF_LINE = (lineSeparator != null) && ((lineSeparator.equals("\r\n")) || (lineSeparator.equals("\r"))) ? lineSeparator : "\n";
  }
  


  private Serializer fXMLSerializer = null;
  

  protected int fFeatures = 0;
  

  private DOM3Serializer fDOMSerializer = null;
  

  private LSSerializerFilter fSerializerFilter = null;
  

  private Node fVisitedNode = null;
  

  private String fEndOfLine = DEFAULT_END_OF_LINE;
  

  private DOMErrorHandler fDOMErrorHandler = null;
  

  private Properties fDOMConfigProperties = null;
  


  private String fEncoding;
  


  private static final int CANONICAL = 1;
  


  private static final int CDATA = 2;
  


  private static final int CHARNORMALIZE = 4;
  


  private static final int COMMENTS = 8;
  


  private static final int DTNORMALIZE = 16;
  

  private static final int ELEM_CONTENT_WHITESPACE = 32;
  

  private static final int ENTITIES = 64;
  

  private static final int INFOSET = 128;
  

  private static final int NAMESPACES = 256;
  

  private static final int NAMESPACEDECLS = 512;
  

  private static final int NORMALIZECHARS = 1024;
  

  private static final int SPLITCDATA = 2048;
  

  private static final int VALIDATE = 4096;
  

  private static final int SCHEMAVALIDATE = 8192;
  

  private static final int WELLFORMED = 16384;
  

  private static final int DISCARDDEFAULT = 32768;
  

  private static final int PRETTY_PRINT = 65536;
  

  private static final int IGNORE_CHAR_DENORMALIZE = 131072;
  

  private static final int XMLDECL = 262144;
  

  private String[] fRecognizedParameters = { "canonical-form", "cdata-sections", "check-character-normalization", "comments", "datatype-normalization", "element-content-whitespace", "entities", "infoset", "namespaces", "namespace-declarations", "split-cdata-sections", "validate", "validate-if-schema", "well-formed", "discard-default-content", "format-pretty-print", "ignore-unknown-character-denormalizations", "xml-declaration", "error-handler" };
  





























  public LSSerializerImpl()
  {
    fFeatures |= 0x2;
    fFeatures |= 0x8;
    fFeatures |= 0x20;
    fFeatures |= 0x40;
    fFeatures |= 0x100;
    fFeatures |= 0x200;
    fFeatures |= 0x800;
    fFeatures |= 0x4000;
    fFeatures |= 0x8000;
    fFeatures |= 0x40000;
    

    fDOMConfigProperties = new Properties();
    

    initializeSerializerProps();
    

    Properties configProps = OutputPropertiesFactory.getDefaultMethodProperties("xml");
    





    fXMLSerializer = SerializerFactory.getSerializer(configProps);
    

    fXMLSerializer.setOutputFormat(fDOMConfigProperties);
  }
  







  public void initializeSerializerProps()
  {
    fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}canonical-form", "default:no");
    


    fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}cdata-sections", "default:yes");
    


    fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}check-character-normalization", "default:no");
    



    fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}comments", "default:yes");
    


    fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}datatype-normalization", "default:no");
    



    fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}element-content-whitespace", "default:yes");
    



    fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}entities", "default:yes");
    

    fDOMConfigProperties.setProperty("{http://xml.apache.org/xerces-2j}entities", "default:yes");
    










    if ((fFeatures & 0x80) != 0) {
      fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}namespaces", "default:yes");
      
      fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}namespace-declarations", "default:yes");
      

      fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}comments", "default:yes");
      
      fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}element-content-whitespace", "default:yes");
      

      fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}well-formed", "default:yes");
      
      fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}entities", "default:no");
      

      fDOMConfigProperties.setProperty("{http://xml.apache.org/xerces-2j}entities", "default:no");
      
      fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}cdata-sections", "default:no");
      

      fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}validate-if-schema", "default:no");
      

      fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}datatype-normalization", "default:no");
    }
    



    fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}namespaces", "default:yes");
    


    fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}namespace-declarations", "default:yes");
    










    fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}split-cdata-sections", "default:yes");
    


    fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}validate", "default:no");
    


    fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}validate-if-schema", "default:no");
    



    fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}well-formed", "default:yes");
    


    fDOMConfigProperties.setProperty("indent", "default:yes");
    

    fDOMConfigProperties.setProperty("{http://xml.apache.org/xalan}indent-amount", Integer.toString(3));
    




    fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}discard-default-content", "default:yes");
    



    fDOMConfigProperties.setProperty("omit-xml-declaration", "no");
  }
  












  public boolean canSetParameter(String name, Object value)
  {
    if ((value instanceof Boolean)) {
      if ((name.equalsIgnoreCase("cdata-sections")) || (name.equalsIgnoreCase("comments")) || (name.equalsIgnoreCase("entities")) || (name.equalsIgnoreCase("infoset")) || (name.equalsIgnoreCase("element-content-whitespace")) || (name.equalsIgnoreCase("namespaces")) || (name.equalsIgnoreCase("namespace-declarations")) || (name.equalsIgnoreCase("split-cdata-sections")) || (name.equalsIgnoreCase("well-formed")) || (name.equalsIgnoreCase("discard-default-content")) || (name.equalsIgnoreCase("format-pretty-print")) || (name.equalsIgnoreCase("xml-declaration")))
      {











        return true;
      }
      if ((name.equalsIgnoreCase("canonical-form")) || (name.equalsIgnoreCase("check-character-normalization")) || (name.equalsIgnoreCase("datatype-normalization")) || (name.equalsIgnoreCase("validate-if-schema")) || (name.equalsIgnoreCase("validate")))
      {






        return !((Boolean)value).booleanValue();
      }
      if (name.equalsIgnoreCase("ignore-unknown-character-denormalizations"))
      {
        return ((Boolean)value).booleanValue();
      }
    }
    else if (((name.equalsIgnoreCase("error-handler")) && (value == null)) || ((value instanceof DOMErrorHandler)))
    {
      return true;
    }
    return false;
  }
  






  public Object getParameter(String name)
    throws DOMException
  {
    if (name.equalsIgnoreCase("comments"))
      return (fFeatures & 0x8) != 0 ? Boolean.TRUE : Boolean.FALSE;
    if (name.equalsIgnoreCase("cdata-sections"))
      return (fFeatures & 0x2) != 0 ? Boolean.TRUE : Boolean.FALSE;
    if (name.equalsIgnoreCase("entities"))
      return (fFeatures & 0x40) != 0 ? Boolean.TRUE : Boolean.FALSE;
    if (name.equalsIgnoreCase("namespaces"))
      return (fFeatures & 0x100) != 0 ? Boolean.TRUE : Boolean.FALSE;
    if (name.equalsIgnoreCase("namespace-declarations"))
      return (fFeatures & 0x200) != 0 ? Boolean.TRUE : Boolean.FALSE;
    if (name.equalsIgnoreCase("split-cdata-sections"))
      return (fFeatures & 0x800) != 0 ? Boolean.TRUE : Boolean.FALSE;
    if (name.equalsIgnoreCase("well-formed"))
      return (fFeatures & 0x4000) != 0 ? Boolean.TRUE : Boolean.FALSE;
    if (name.equalsIgnoreCase("discard-default-content"))
      return (fFeatures & 0x8000) != 0 ? Boolean.TRUE : Boolean.FALSE;
    if (name.equalsIgnoreCase("format-pretty-print"))
      return (fFeatures & 0x10000) != 0 ? Boolean.TRUE : Boolean.FALSE;
    if (name.equalsIgnoreCase("xml-declaration"))
      return (fFeatures & 0x40000) != 0 ? Boolean.TRUE : Boolean.FALSE;
    if (name.equalsIgnoreCase("element-content-whitespace"))
      return (fFeatures & 0x20) != 0 ? Boolean.TRUE : Boolean.FALSE;
    if (name.equalsIgnoreCase("format-pretty-print"))
      return (fFeatures & 0x10000) != 0 ? Boolean.TRUE : Boolean.FALSE;
    if (name.equalsIgnoreCase("ignore-unknown-character-denormalizations"))
      return Boolean.TRUE;
    if ((name.equalsIgnoreCase("canonical-form")) || (name.equalsIgnoreCase("check-character-normalization")) || (name.equalsIgnoreCase("datatype-normalization")) || (name.equalsIgnoreCase("validate")) || (name.equalsIgnoreCase("validate-if-schema")))
    {




      return Boolean.FALSE; }
    if (name.equalsIgnoreCase("infoset")) {
      if (((fFeatures & 0x40) == 0) && ((fFeatures & 0x2) == 0) && ((fFeatures & 0x20) != 0) && ((fFeatures & 0x100) != 0) && ((fFeatures & 0x200) != 0) && ((fFeatures & 0x4000) != 0) && ((fFeatures & 0x8) != 0))
      {





        return Boolean.TRUE;
      }
      return Boolean.FALSE; }
    if (name.equalsIgnoreCase("error-handler"))
      return fDOMErrorHandler;
    if ((name.equalsIgnoreCase("schema-location")) || (name.equalsIgnoreCase("schema-type")))
    {

      return null;
    }
    
    String msg = Utils.messages.createMessage("FEATURE_NOT_FOUND", new Object[] { name });
    

    throw new DOMException((short)8, msg);
  }
  









  public DOMStringList getParameterNames()
  {
    return new DOMStringListImpl(fRecognizedParameters);
  }
  







  public void setParameter(String name, Object value)
    throws DOMException
  {
    if ((value instanceof Boolean)) {
      boolean state = ((Boolean)value).booleanValue();
      
      if (name.equalsIgnoreCase("comments")) {
        fFeatures = (state ? fFeatures | 0x8 : fFeatures & 0xFFFFFFF7);
        

        if (state) {
          fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}comments", "explicit:yes");
        }
        else {
          fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}comments", "explicit:no");
        }
      }
      else if (name.equalsIgnoreCase("cdata-sections")) {
        fFeatures = (state ? fFeatures | 0x2 : fFeatures & 0xFFFFFFFD);
        

        if (state) {
          fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}cdata-sections", "explicit:yes");
        }
        else {
          fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}cdata-sections", "explicit:no");
        }
      }
      else if (name.equalsIgnoreCase("entities")) {
        fFeatures = (state ? fFeatures | 0x40 : fFeatures & 0xFFFFFFBF);
        

        if (state) {
          fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}entities", "explicit:yes");
          
          fDOMConfigProperties.setProperty("{http://xml.apache.org/xerces-2j}entities", "explicit:yes");
        }
        else
        {
          fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}entities", "explicit:no");
          
          fDOMConfigProperties.setProperty("{http://xml.apache.org/xerces-2j}entities", "explicit:no");
        }
        
      }
      else if (name.equalsIgnoreCase("namespaces")) {
        fFeatures = (state ? fFeatures | 0x100 : fFeatures & 0xFEFF);
        

        if (state) {
          fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}namespaces", "explicit:yes");
        }
        else {
          fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}namespaces", "explicit:no");
        }
      }
      else if (name.equalsIgnoreCase("namespace-declarations"))
      {
        fFeatures = (state ? fFeatures | 0x200 : fFeatures & 0xFDFF);
        

        if (state) {
          fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}namespace-declarations", "explicit:yes");
        }
        else {
          fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}namespace-declarations", "explicit:no");
        }
      }
      else if (name.equalsIgnoreCase("split-cdata-sections")) {
        fFeatures = (state ? fFeatures | 0x800 : fFeatures & 0xF7FF);
        

        if (state) {
          fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}split-cdata-sections", "explicit:yes");
        }
        else {
          fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}split-cdata-sections", "explicit:no");
        }
      }
      else if (name.equalsIgnoreCase("well-formed")) {
        fFeatures = (state ? fFeatures | 0x4000 : fFeatures & 0xBFFF);
        

        if (state) {
          fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}well-formed", "explicit:yes");
        }
        else {
          fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}well-formed", "explicit:no");
        }
      }
      else if (name.equalsIgnoreCase("discard-default-content"))
      {
        fFeatures = (state ? fFeatures | 0x8000 : fFeatures & 0xFFFF7FFF);
        

        if (state) {
          fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}discard-default-content", "explicit:yes");
        }
        else {
          fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}discard-default-content", "explicit:no");
        }
      }
      else if (name.equalsIgnoreCase("format-pretty-print")) {
        fFeatures = (state ? fFeatures | 0x10000 : fFeatures & 0xFFFEFFFF);
        

        if (state) {
          fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}format-pretty-print", "explicit:yes");
        }
        else
        {
          fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}format-pretty-print", "explicit:no");
        }
      }
      else if (name.equalsIgnoreCase("xml-declaration")) {
        fFeatures = (state ? fFeatures | 0x40000 : fFeatures & 0xFFFBFFFF);
        
        if (state) {
          fDOMConfigProperties.setProperty("omit-xml-declaration", "no");
        } else {
          fDOMConfigProperties.setProperty("omit-xml-declaration", "yes");
        }
      } else if (name.equalsIgnoreCase("element-content-whitespace")) {
        fFeatures = (state ? fFeatures | 0x20 : fFeatures & 0xFFFFFFDF);
        

        if (state) {
          fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}element-content-whitespace", "explicit:yes");
        }
        else {
          fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}element-content-whitespace", "explicit:no");
        }
      }
      else if (name.equalsIgnoreCase("ignore-unknown-character-denormalizations"))
      {
        if (!state)
        {
          String msg = Utils.messages.createMessage("FEATURE_NOT_SUPPORTED", new Object[] { name });
          

          throw new DOMException((short)9, msg);
        }
        fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}ignore-unknown-character-denormalizations", "explicit:yes");

      }
      else if ((name.equalsIgnoreCase("canonical-form")) || (name.equalsIgnoreCase("validate-if-schema")) || (name.equalsIgnoreCase("validate")) || (name.equalsIgnoreCase("check-character-normalization")) || (name.equalsIgnoreCase("datatype-normalization")))
      {






        if (state) {
          String msg = Utils.messages.createMessage("FEATURE_NOT_SUPPORTED", new Object[] { name });
          

          throw new DOMException((short)9, msg);
        }
        if (name.equalsIgnoreCase("canonical-form")) {
          fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}canonical-form", "explicit:no");
        }
        else if (name.equalsIgnoreCase("validate-if-schema")) {
          fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}validate-if-schema", "explicit:no");
        }
        else if (name.equalsIgnoreCase("validate")) {
          fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}validate", "explicit:no");
        }
        else if (name.equalsIgnoreCase("validate-if-schema")) {
          fDOMConfigProperties.setProperty("check-character-normalizationcheck-character-normalization", "explicit:no");
        }
        else if (name.equalsIgnoreCase("datatype-normalization")) {
          fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}datatype-normalization", "explicit:no");

        }
        


      }
      else if (name.equalsIgnoreCase("infoset"))
      {
        if (state) {
          fFeatures &= 0xFFFFFFBF;
          fFeatures &= 0xFFFFFFFD;
          fFeatures &= 0xDFFF;
          fFeatures &= 0xFFFFFFEF;
          fFeatures |= 0x100;
          fFeatures |= 0x200;
          fFeatures |= 0x4000;
          fFeatures |= 0x20;
          fFeatures |= 0x8;
          
          fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}namespaces", "explicit:yes");
          
          fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}namespace-declarations", "explicit:yes");
          
          fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}comments", "explicit:yes");
          
          fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}element-content-whitespace", "explicit:yes");
          
          fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}well-formed", "explicit:yes");
          

          fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}entities", "explicit:no");
          
          fDOMConfigProperties.setProperty("{http://xml.apache.org/xerces-2j}entities", "explicit:no");
          

          fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}cdata-sections", "explicit:no");
          
          fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}validate-if-schema", "explicit:no");
          
          fDOMConfigProperties.setProperty("{http://www.w3.org/TR/DOM-Level-3-LS}datatype-normalization", "explicit:no");
        }
      }
      else
      {
        if ((name.equalsIgnoreCase("error-handler")) || (name.equalsIgnoreCase("schema-location")) || (name.equalsIgnoreCase("schema-type")))
        {

          String msg = Utils.messages.createMessage("TYPE_MISMATCH_ERR", new Object[] { name });
          

          throw new DOMException((short)17, msg);
        }
        

        String msg = Utils.messages.createMessage("FEATURE_NOT_FOUND", new Object[] { name });
        

        throw new DOMException((short)8, msg);
      }
    }
    else if (name.equalsIgnoreCase("error-handler")) {
      if ((value == null) || ((value instanceof DOMErrorHandler))) {
        fDOMErrorHandler = ((DOMErrorHandler)value);
      } else {
        String msg = Utils.messages.createMessage("TYPE_MISMATCH_ERR", new Object[] { name });
        

        throw new DOMException((short)17, msg);
      }
    } else if ((name.equalsIgnoreCase("schema-location")) || (name.equalsIgnoreCase("schema-type")))
    {

      if (value != null) {
        if (!(value instanceof String)) {
          String msg = Utils.messages.createMessage("TYPE_MISMATCH_ERR", new Object[] { name });
          

          throw new DOMException((short)17, msg);
        }
        String msg = Utils.messages.createMessage("FEATURE_NOT_SUPPORTED", new Object[] { name });
        

        throw new DOMException((short)9, msg);
      }
    }
    else {
      if ((name.equalsIgnoreCase("comments")) || (name.equalsIgnoreCase("cdata-sections")) || (name.equalsIgnoreCase("entities")) || (name.equalsIgnoreCase("namespaces")) || (name.equalsIgnoreCase("namespace-declarations")) || (name.equalsIgnoreCase("split-cdata-sections")) || (name.equalsIgnoreCase("well-formed")) || (name.equalsIgnoreCase("discard-default-content")) || (name.equalsIgnoreCase("format-pretty-print")) || (name.equalsIgnoreCase("xml-declaration")) || (name.equalsIgnoreCase("element-content-whitespace")) || (name.equalsIgnoreCase("ignore-unknown-character-denormalizations")) || (name.equalsIgnoreCase("canonical-form")) || (name.equalsIgnoreCase("validate-if-schema")) || (name.equalsIgnoreCase("validate")) || (name.equalsIgnoreCase("check-character-normalization")) || (name.equalsIgnoreCase("datatype-normalization")) || (name.equalsIgnoreCase("infoset")))
      {
















        String msg = Utils.messages.createMessage("TYPE_MISMATCH_ERR", new Object[] { name });
        

        throw new DOMException((short)17, msg);
      }
      

      String msg = Utils.messages.createMessage("FEATURE_NOT_FOUND", new Object[] { name });
      

      throw new DOMException((short)8, msg);
    }
  }
  












  public DOMConfiguration getDomConfig()
  {
    return this;
  }
  






  public LSSerializerFilter getFilter()
  {
    return fSerializerFilter;
  }
  








  public String getNewLine()
  {
    return fEndOfLine;
  }
  








  public void setFilter(LSSerializerFilter filter)
  {
    fSerializerFilter = filter;
  }
  









  public void setNewLine(String newLine)
  {
    fEndOfLine = (newLine != null ? newLine : DEFAULT_END_OF_LINE);
  }
  










  public boolean write(Node nodeArg, LSOutput destination)
    throws LSException
  {
    if (destination == null) {
      String msg = Utils.messages.createMessage("no-output-specified", null);
      


      if (fDOMErrorHandler != null) {
        fDOMErrorHandler.handleError(new DOMErrorImpl((short)3, msg, "no-output-specified"));
      }
      

      throw new LSException((short)82, msg);
    }
    

    if (nodeArg == null) {
      return false;
    }
    


    Serializer serializer = fXMLSerializer;
    serializer.reset();
    

    if (nodeArg != fVisitedNode)
    {
      String xmlVersion = getXMLVersion(nodeArg);
      

      fEncoding = destination.getEncoding();
      if (fEncoding == null) {
        fEncoding = getInputEncoding(nodeArg);
        fEncoding = (getXMLEncoding(nodeArg) == null ? "UTF-8" : fEncoding != null ? fEncoding : getXMLEncoding(nodeArg));
      }
      


      if (!Encodings.isRecognizedEncoding(fEncoding)) {
        String msg = Utils.messages.createMessage("unsupported-encoding", null);
        


        if (fDOMErrorHandler != null) {
          fDOMErrorHandler.handleError(new DOMErrorImpl((short)3, msg, "unsupported-encoding"));
        }
        

        throw new LSException((short)82, msg);
      }
      
      serializer.getOutputFormat().setProperty("version", xmlVersion);
      

      fDOMConfigProperties.setProperty("{http://xml.apache.org/xerces-2j}xml-version", xmlVersion);
      fDOMConfigProperties.setProperty("encoding", fEncoding);
      




      if (((nodeArg.getNodeType() != 9) || (nodeArg.getNodeType() != 1) || (nodeArg.getNodeType() != 6)) && ((fFeatures & 0x40000) != 0))
      {


        fDOMConfigProperties.setProperty("omit-xml-declaration", "default:no");
      }
      


      fVisitedNode = nodeArg;
    }
    

    fXMLSerializer.setOutputFormat(fDOMConfigProperties);
    







    try
    {
      Writer writer = destination.getCharacterStream();
      if (writer == null)
      {

        OutputStream outputStream = destination.getByteStream();
        if (outputStream == null)
        {

          String uri = destination.getSystemId();
          if (uri == null) {
            String msg = Utils.messages.createMessage("no-output-specified", null);
            


            if (fDOMErrorHandler != null) {
              fDOMErrorHandler.handleError(new DOMErrorImpl((short)3, msg, "no-output-specified"));
            }
            

            throw new LSException((short)82, msg);
          }
          

          String absoluteURI = SystemIDResolver.getAbsoluteURI(uri);
          
          URL url = new URL(absoluteURI);
          OutputStream urlOutStream = null;
          String protocol = url.getProtocol();
          String host = url.getHost();
          








          if ((protocol.equalsIgnoreCase("file")) && ((host == null) || (host.length() == 0) || (host.equals("localhost"))))
          {

            urlOutStream = new FileOutputStream(getPathWithoutEscapes(url.getPath()));

          }
          else
          {

            URLConnection urlCon = url.openConnection();
            urlCon.setDoInput(false);
            urlCon.setDoOutput(true);
            urlCon.setUseCaches(false);
            urlCon.setAllowUserInteraction(false);
            

            if ((urlCon instanceof HttpURLConnection)) {
              HttpURLConnection httpCon = (HttpURLConnection)urlCon;
              httpCon.setRequestMethod("PUT");
            }
            urlOutStream = urlCon.getOutputStream();
          }
          
          serializer.setOutputStream(urlOutStream);
        }
        else
        {
          serializer.setOutputStream(outputStream);
        }
      }
      else {
        serializer.setWriter(writer);
      }
      





      if (fDOMSerializer == null) {
        fDOMSerializer = ((DOM3Serializer)serializer.asDOM3Serializer());
      }
      

      if (fDOMErrorHandler != null) {
        fDOMSerializer.setErrorHandler(fDOMErrorHandler);
      }
      

      if (fSerializerFilter != null) {
        fDOMSerializer.setNodeFilter(fSerializerFilter);
      }
      

      fDOMSerializer.setNewLine(fEndOfLine.toCharArray());
      


      fDOMSerializer.serializeDOM3(nodeArg);
    }
    catch (UnsupportedEncodingException ue)
    {
      String msg = Utils.messages.createMessage("unsupported-encoding", null);
      


      if (fDOMErrorHandler != null) {
        fDOMErrorHandler.handleError(new DOMErrorImpl((short)3, msg, "unsupported-encoding", ue));
      }
      

      throw ((LSException)createLSException((short)82, ue).fillInStackTrace());
    }
    catch (LSException lse) {
      throw lse;
    } catch (RuntimeException e) {
      throw ((LSException)createLSException((short)82, e).fillInStackTrace());
    } catch (Exception e) {
      if (fDOMErrorHandler != null) {
        fDOMErrorHandler.handleError(new DOMErrorImpl((short)3, e.getMessage(), null, e));
      }
      

      throw ((LSException)createLSException((short)82, e).fillInStackTrace());
    }
    return true;
  }
  










  public String writeToString(Node nodeArg)
    throws DOMException, LSException
  {
    if (nodeArg == null) {
      return null;
    }
    


    Serializer serializer = fXMLSerializer;
    serializer.reset();
    
    if (nodeArg != fVisitedNode)
    {
      String xmlVersion = getXMLVersion(nodeArg);
      
      serializer.getOutputFormat().setProperty("version", xmlVersion);
      

      fDOMConfigProperties.setProperty("{http://xml.apache.org/xerces-2j}xml-version", xmlVersion);
      fDOMConfigProperties.setProperty("encoding", "UTF-16");
      




      if (((nodeArg.getNodeType() != 9) || (nodeArg.getNodeType() != 1) || (nodeArg.getNodeType() != 6)) && ((fFeatures & 0x40000) != 0))
      {


        fDOMConfigProperties.setProperty("omit-xml-declaration", "default:no");
      }
      


      fVisitedNode = nodeArg;
    }
    
    fXMLSerializer.setOutputFormat(fDOMConfigProperties);
    

    StringWriter output = new StringWriter();
    


    try
    {
      serializer.setWriter(output);
      


      if (fDOMSerializer == null) {
        fDOMSerializer = ((DOM3Serializer)serializer.asDOM3Serializer());
      }
      

      if (fDOMErrorHandler != null) {
        fDOMSerializer.setErrorHandler(fDOMErrorHandler);
      }
      

      if (fSerializerFilter != null) {
        fDOMSerializer.setNodeFilter(fSerializerFilter);
      }
      

      fDOMSerializer.setNewLine(fEndOfLine.toCharArray());
      

      fDOMSerializer.serializeDOM3(nodeArg);
    }
    catch (LSException lse) {
      throw lse;
    } catch (RuntimeException e) {
      throw ((LSException)createLSException((short)82, e).fillInStackTrace());
    } catch (Exception e) {
      if (fDOMErrorHandler != null) {
        fDOMErrorHandler.handleError(new DOMErrorImpl((short)3, e.getMessage(), null, e));
      }
      

      throw ((LSException)createLSException((short)82, e).fillInStackTrace());
    }
    

    return output.toString();
  }
  










  public boolean writeToURI(Node nodeArg, String uri)
    throws LSException
  {
    if (nodeArg == null) {
      return false;
    }
    

    Serializer serializer = fXMLSerializer;
    serializer.reset();
    
    if (nodeArg != fVisitedNode)
    {
      String xmlVersion = getXMLVersion(nodeArg);
      


      fEncoding = getInputEncoding(nodeArg);
      if (fEncoding == null) {
        fEncoding = (getXMLEncoding(nodeArg) == null ? "UTF-8" : fEncoding != null ? fEncoding : getXMLEncoding(nodeArg));
      }
      
      serializer.getOutputFormat().setProperty("version", xmlVersion);
      

      fDOMConfigProperties.setProperty("{http://xml.apache.org/xerces-2j}xml-version", xmlVersion);
      fDOMConfigProperties.setProperty("encoding", fEncoding);
      




      if (((nodeArg.getNodeType() != 9) || (nodeArg.getNodeType() != 1) || (nodeArg.getNodeType() != 6)) && ((fFeatures & 0x40000) != 0))
      {


        fDOMConfigProperties.setProperty("omit-xml-declaration", "default:no");
      }
      


      fVisitedNode = nodeArg;
    }
    

    fXMLSerializer.setOutputFormat(fDOMConfigProperties);
    


    try
    {
      if (uri == null) {
        String msg = Utils.messages.createMessage("no-output-specified", null);
        
        if (fDOMErrorHandler != null) {
          fDOMErrorHandler.handleError(new DOMErrorImpl((short)3, msg, "no-output-specified"));
        }
        

        throw new LSException((short)82, msg);
      }
      

      String absoluteURI = SystemIDResolver.getAbsoluteURI(uri);
      
      URL url = new URL(absoluteURI);
      OutputStream urlOutStream = null;
      String protocol = url.getProtocol();
      String host = url.getHost();
      









      if ((protocol.equalsIgnoreCase("file")) && ((host == null) || (host.length() == 0) || (host.equals("localhost"))))
      {


        urlOutStream = new FileOutputStream(getPathWithoutEscapes(url.getPath()));

      }
      else
      {

        URLConnection urlCon = url.openConnection();
        urlCon.setDoInput(false);
        urlCon.setDoOutput(true);
        urlCon.setUseCaches(false);
        urlCon.setAllowUserInteraction(false);
        

        if ((urlCon instanceof HttpURLConnection)) {
          HttpURLConnection httpCon = (HttpURLConnection)urlCon;
          httpCon.setRequestMethod("PUT");
        }
        urlOutStream = urlCon.getOutputStream();
      }
      
      serializer.setOutputStream(urlOutStream);
      



      if (fDOMSerializer == null) {
        fDOMSerializer = ((DOM3Serializer)serializer.asDOM3Serializer());
      }
      

      if (fDOMErrorHandler != null) {
        fDOMSerializer.setErrorHandler(fDOMErrorHandler);
      }
      

      if (fSerializerFilter != null) {
        fDOMSerializer.setNodeFilter(fSerializerFilter);
      }
      

      fDOMSerializer.setNewLine(fEndOfLine.toCharArray());
      



      fDOMSerializer.serializeDOM3(nodeArg);
    }
    catch (LSException lse)
    {
      throw lse;
    } catch (RuntimeException e) {
      throw ((LSException)createLSException((short)82, e).fillInStackTrace());
    } catch (Exception e) {
      if (fDOMErrorHandler != null) {
        fDOMErrorHandler.handleError(new DOMErrorImpl((short)3, e.getMessage(), null, e));
      }
      

      throw ((LSException)createLSException((short)82, e).fillInStackTrace());
    }
    
    return true;
  }
  














  protected String getXMLVersion(Node nodeArg)
  {
    Document doc = null;
    

    if (nodeArg != null) {
      if (nodeArg.getNodeType() == 9)
      {
        doc = (Document)nodeArg;
      }
      else {
        doc = nodeArg.getOwnerDocument();
      }
      

      if ((doc != null) && (doc.getImplementation().hasFeature("Core", "3.0"))) {
        return doc.getXmlVersion();
      }
    }
    


    return "1.0";
  }
  







  protected String getXMLEncoding(Node nodeArg)
  {
    Document doc = null;
    

    if (nodeArg != null) {
      if (nodeArg.getNodeType() == 9)
      {
        doc = (Document)nodeArg;
      }
      else {
        doc = nodeArg.getOwnerDocument();
      }
      

      if ((doc != null) && (doc.getImplementation().hasFeature("Core", "3.0"))) {
        return doc.getXmlEncoding();
      }
    }
    
    return "UTF-8";
  }
  






  protected String getInputEncoding(Node nodeArg)
  {
    Document doc = null;
    

    if (nodeArg != null) {
      if (nodeArg.getNodeType() == 9)
      {
        doc = (Document)nodeArg;
      }
      else {
        doc = nodeArg.getOwnerDocument();
      }
      

      if ((doc != null) && (doc.getImplementation().hasFeature("Core", "3.0"))) {
        return doc.getInputEncoding();
      }
    }
    
    return null;
  }
  




  public DOMErrorHandler getErrorHandler()
  {
    return fDOMErrorHandler;
  }
  


  private static String getPathWithoutEscapes(String origPath)
  {
    if ((origPath != null) && (origPath.length() != 0) && (origPath.indexOf('%') != -1))
    {
      StringTokenizer tokenizer = new StringTokenizer(origPath, "%");
      StringBuffer result = new StringBuffer(origPath.length());
      int size = tokenizer.countTokens();
      result.append(tokenizer.nextToken());
      for (int i = 1; i < size; i++) {
        String token = tokenizer.nextToken();
        if ((token.length() >= 2) && (isHexDigit(token.charAt(0))) && (isHexDigit(token.charAt(1))))
        {

          result.append((char)Integer.valueOf(token.substring(0, 2), 16).intValue());
          token = token.substring(2);
        }
        result.append(token);
      }
      return result.toString();
    }
    return origPath;
  }
  


  private static boolean isHexDigit(char c)
  {
    return ((c >= '0') && (c <= '9')) || ((c >= 'a') && (c <= 'f')) || ((c >= 'A') && (c <= 'F'));
  }
  




  private static LSException createLSException(short code, Throwable cause)
  {
    LSException lse = new LSException(code, cause != null ? cause.getMessage() : null);
    if ((cause != null) && (ThrowableMethods.fgThrowableMethodsAvailable)) {
      try {
        ThrowableMethods.fgThrowableInitCauseMethod.invoke(lse, new Object[] { cause });
      }
      catch (Exception e) {}
    }
    
    return lse;
  }
  




  static class ThrowableMethods
  {
    private static Method fgThrowableInitCauseMethod = null;
    

    private static boolean fgThrowableMethodsAvailable = false;
    
    private ThrowableMethods() {}
    
    static
    {
      try {
        fgThrowableInitCauseMethod = LSSerializerImpl.class$java$lang$Throwable.getMethod("initCause", new Class[] { Throwable.class });
        fgThrowableMethodsAvailable = true;

      }
      catch (Exception exc)
      {
        fgThrowableInitCauseMethod = null;
        fgThrowableMethodsAvailable = false;
      }
    }
  }
}
