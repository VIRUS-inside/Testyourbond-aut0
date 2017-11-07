package org.apache.xml.dtm.ref;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.xerces.parsers.SAXParser;
import org.apache.xml.res.XMLMessages;
import org.apache.xml.serialize.XMLSerializer;
import org.apache.xml.utils.WrappedRuntimeException;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;





























public class IncrementalSAXSource_Xerces
  implements IncrementalSAXSource
{
  Method fParseSomeSetup = null;
  Method fParseSome = null;
  Object fPullParserConfig = null;
  Method fConfigSetInput = null;
  Method fConfigParse = null;
  Method fSetInputSource = null;
  Constructor fConfigInputSourceCtor = null;
  Method fConfigSetByteStream = null;
  Method fConfigSetCharStream = null;
  Method fConfigSetEncoding = null;
  Method fReset = null;
  

  SAXParser fIncrementalParser;
  

  private boolean fParseInProgress = false;
  























  public IncrementalSAXSource_Xerces()
    throws NoSuchMethodException
  {
    try
    {
      Class xniConfigClass = ObjectFactory.findProviderClass("org.apache.xerces.xni.parser.XMLParserConfiguration", ObjectFactory.findClassLoader(), true);
      

      Class[] args1 = { xniConfigClass };
      Constructor ctor = SAXParser.class.getConstructor(args1);
      



      Class xniStdConfigClass = ObjectFactory.findProviderClass("org.apache.xerces.parsers.StandardParserConfiguration", ObjectFactory.findClassLoader(), true);
      

      fPullParserConfig = xniStdConfigClass.newInstance();
      Object[] args2 = { fPullParserConfig };
      fIncrementalParser = ((SAXParser)ctor.newInstance(args2));
      



      Class fXniInputSourceClass = ObjectFactory.findProviderClass("org.apache.xerces.xni.parser.XMLInputSource", ObjectFactory.findClassLoader(), true);
      

      Class[] args3 = { fXniInputSourceClass };
      fConfigSetInput = xniStdConfigClass.getMethod("setInputSource", args3);
      
      Class[] args4 = { String.class, String.class, String.class };
      fConfigInputSourceCtor = fXniInputSourceClass.getConstructor(args4);
      Class[] args5 = { InputStream.class };
      fConfigSetByteStream = fXniInputSourceClass.getMethod("setByteStream", args5);
      Class[] args6 = { Reader.class };
      fConfigSetCharStream = fXniInputSourceClass.getMethod("setCharacterStream", args6);
      Class[] args7 = { String.class };
      fConfigSetEncoding = fXniInputSourceClass.getMethod("setEncoding", args7);
      
      Class[] argsb = { Boolean.TYPE };
      fConfigParse = xniStdConfigClass.getMethod("parse", argsb);
      Class[] noargs = new Class[0];
      fReset = fIncrementalParser.getClass().getMethod("reset", noargs);


    }
    catch (Exception e)
    {


      IncrementalSAXSource_Xerces dummy = new IncrementalSAXSource_Xerces(new SAXParser());
      fParseSomeSetup = fParseSomeSetup;
      fParseSome = fParseSome;
      fIncrementalParser = fIncrementalParser;
    }
  }
  
















  public IncrementalSAXSource_Xerces(SAXParser parser)
    throws NoSuchMethodException
  {
    fIncrementalParser = parser;
    Class me = parser.getClass();
    Class[] parms = { InputSource.class };
    fParseSomeSetup = me.getMethod("parseSomeSetup", parms);
    parms = new Class[0];
    fParseSome = me.getMethod("parseSome", parms);
  }
  





  public static IncrementalSAXSource createIncrementalSAXSource()
  {
    try
    {
      return new IncrementalSAXSource_Xerces();

    }
    catch (NoSuchMethodException e)
    {

      IncrementalSAXSource_Filter iss = new IncrementalSAXSource_Filter();
      iss.setXMLReader(new SAXParser());
      return iss;
    }
  }
  
  public static IncrementalSAXSource createIncrementalSAXSource(SAXParser parser)
  {
    try
    {
      return new IncrementalSAXSource_Xerces(parser);

    }
    catch (NoSuchMethodException e)
    {

      IncrementalSAXSource_Filter iss = new IncrementalSAXSource_Filter();
      iss.setXMLReader(parser);
      return iss;
    }
  }
  







  public void setContentHandler(ContentHandler handler)
  {
    fIncrementalParser.setContentHandler(handler);
  }
  




  public void setLexicalHandler(LexicalHandler handler)
  {
    try
    {
      fIncrementalParser.setProperty("http://xml.org/sax/properties/lexical-handler", handler);
    }
    catch (SAXNotRecognizedException e) {}catch (SAXNotSupportedException e) {}
  }
  











  public void setDTDHandler(DTDHandler handler)
  {
    fIncrementalParser.setDTDHandler(handler);
  }
  






  public void startParse(InputSource source)
    throws SAXException
  {
    if (fIncrementalParser == null)
      throw new SAXException(XMLMessages.createXMLMessage("ER_STARTPARSE_NEEDS_SAXPARSER", null));
    if (fParseInProgress) {
      throw new SAXException(XMLMessages.createXMLMessage("ER_STARTPARSE_WHILE_PARSING", null));
    }
    boolean ok = false;
    
    try
    {
      ok = parseSomeSetup(source);
    }
    catch (Exception ex)
    {
      throw new SAXException(ex);
    }
    
    if (!ok) {
      throw new SAXException(XMLMessages.createXMLMessage("ER_COULD_NOT_INIT_PARSER", null));
    }
  }
  












  public Object deliverMoreNodes(boolean parsemore)
  {
    if (!parsemore)
    {
      fParseInProgress = false;
      return Boolean.FALSE;
    }
    Object arg;
    try
    {
      boolean keepgoing = parseSome();
      arg = keepgoing ? Boolean.TRUE : Boolean.FALSE;
    } catch (SAXException ex) {
      arg = ex;
    } catch (IOException ex) {
      arg = ex;
    } catch (Exception ex) {
      arg = new SAXException(ex);
    }
    return arg;
  }
  



  private boolean parseSomeSetup(InputSource source)
    throws SAXException, IOException, IllegalAccessException, InvocationTargetException, InstantiationException
  {
    if (fConfigSetInput != null)
    {


      Object[] parms1 = { source.getPublicId(), source.getSystemId(), null };
      Object xmlsource = fConfigInputSourceCtor.newInstance(parms1);
      Object[] parmsa = { source.getByteStream() };
      fConfigSetByteStream.invoke(xmlsource, parmsa);
      parmsa[0] = source.getCharacterStream();
      fConfigSetCharStream.invoke(xmlsource, parmsa);
      parmsa[0] = source.getEncoding();
      fConfigSetEncoding.invoke(xmlsource, parmsa);
      




      Object[] noparms = new Object[0];
      fReset.invoke(fIncrementalParser, noparms);
      
      parmsa[0] = xmlsource;
      fConfigSetInput.invoke(fPullParserConfig, parmsa);
      

      return parseSome();
    }
    

    Object[] parm = { source };
    Object ret = fParseSomeSetup.invoke(fIncrementalParser, parm);
    return ((Boolean)ret).booleanValue();
  }
  

  private static final Object[] noparms = new Object[0];
  private static final Object[] parmsfalse = { Boolean.FALSE };
  

  private boolean parseSome()
    throws SAXException, IOException, IllegalAccessException, InvocationTargetException
  {
    if (fConfigSetInput != null)
    {
      Object ret = (Boolean)fConfigParse.invoke(fPullParserConfig, parmsfalse);
      return ((Boolean)ret).booleanValue();
    }
    

    Object ret = fParseSome.invoke(fIncrementalParser, noparms);
    return ((Boolean)ret).booleanValue();
  }
  






  public static void main(String[] args)
  {
    System.out.println("Starting...");
    
    CoroutineManager co = new CoroutineManager();
    int appCoroutineID = co.co_joinCoroutineSet(-1);
    if (appCoroutineID == -1)
    {
      System.out.println("ERROR: Couldn't allocate coroutine number.\n");
      return;
    }
    IncrementalSAXSource parser = createIncrementalSAXSource();
    



    XMLSerializer trace = new XMLSerializer(System.out, null);
    parser.setContentHandler(trace);
    parser.setLexicalHandler(trace);
    


    for (int arg = 0; arg < args.length; arg++)
    {
      try
      {
        InputSource source = new InputSource(args[arg]);
        Object result = null;
        boolean more = true;
        parser.startParse(source);
        for (result = parser.deliverMoreNodes(more); 
            result == Boolean.TRUE; 
            result = parser.deliverMoreNodes(more))
        {
          System.out.println("\nSome parsing successful, trying more.\n");
          

          if ((arg + 1 < args.length) && ("!".equals(args[(arg + 1)])))
          {
            arg++;
            more = false;
          }
        }
        

        if (((result instanceof Boolean)) && ((Boolean)result == Boolean.FALSE))
        {
          System.out.println("\nParser ended (EOF or on request).\n");
        }
        else if (result == null) {
          System.out.println("\nUNEXPECTED: Parser says shut down prematurely.\n");
        }
        else if ((result instanceof Exception)) {
          throw new WrappedRuntimeException((Exception)result);

        }
        

      }
      catch (SAXException e)
      {

        e.printStackTrace();
      }
    }
  }
}
