package org.apache.xalan.xsltc.cmdline;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.Vector;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;
import org.apache.xalan.xsltc.DOMEnhancedForDTM;
import org.apache.xalan.xsltc.StripFilter;
import org.apache.xalan.xsltc.TransletException;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.dom.DOMWSFilter;
import org.apache.xalan.xsltc.dom.XSLTCDTMManager;
import org.apache.xalan.xsltc.runtime.AbstractTranslet;
import org.apache.xalan.xsltc.runtime.Parameter;
import org.apache.xalan.xsltc.runtime.output.TransletOutputHandlerFactory;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.serializer.SerializationHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

































public final class Transform
{
  private SerializationHandler _handler;
  private String _fileName;
  private String _className;
  private String _jarFileSrc;
  private boolean _isJarFileSpecified = false;
  private Vector _params = null;
  private boolean _uri;
  private boolean _debug;
  private int _iterations;
  
  public Transform(String className, String fileName, boolean uri, boolean debug, int iterations) {
    _fileName = fileName;
    _className = className;
    _uri = uri;
    _debug = debug;
    _iterations = iterations;
  }
  
  public String getFileName() { return _fileName; }
  public String getClassName() { return _className; }
  
  public void setParameters(Vector params) {
    _params = params;
  }
  




  private void setJarFileInputSrc(boolean flag, String jarFile)
  {
    _isJarFileSpecified = flag;
    
    _jarFileSrc = jarFile;
  }
  
  private void doTransform() {
    try {
      Class clazz = ObjectFactory.findProviderClass(_className, ObjectFactory.findClassLoader(), true);
      
      AbstractTranslet translet = (AbstractTranslet)clazz.newInstance();
      translet.postInitialization();
      

      SAXParserFactory factory = SAXParserFactory.newInstance();
      try {
        factory.setFeature("http://xml.org/sax/features/namespaces", true);
      }
      catch (Exception e) {
        factory.setNamespaceAware(true);
      }
      SAXParser parser = factory.newSAXParser();
      XMLReader reader = parser.getXMLReader();
      

      XSLTCDTMManager dtmManager = (XSLTCDTMManager)XSLTCDTMManager.getDTMManagerClass().newInstance();
      
      DTMWSFilter wsfilter;
      
      DTMWSFilter wsfilter;
      if ((translet != null) && ((translet instanceof StripFilter))) {
        wsfilter = new DOMWSFilter(translet);
      } else {
        wsfilter = null;
      }
      
      DOMEnhancedForDTM dom = (DOMEnhancedForDTM)dtmManager.getDTM(new SAXSource(reader, new InputSource(_fileName)), false, wsfilter, true, false, translet.hasIdCall());
      



      dom.setDocumentURI(_fileName);
      translet.prepassDocument(dom);
      

      int n = _params.size();
      for (int i = 0; i < n; i++) {
        Parameter param = (Parameter)_params.elementAt(i);
        translet.addParameter(_name, _value);
      }
      

      TransletOutputHandlerFactory tohFactory = TransletOutputHandlerFactory.newInstance();
      
      tohFactory.setOutputType(0);
      tohFactory.setEncoding(_encoding);
      tohFactory.setOutputMethod(_method);
      
      if (_iterations == -1) {
        translet.transform(dom, tohFactory.getSerializationHandler());
      }
      else if (_iterations > 0) {
        long mm = System.currentTimeMillis();
        for (int i = 0; i < _iterations; i++) {
          translet.transform(dom, tohFactory.getSerializationHandler());
        }
        
        mm = System.currentTimeMillis() - mm;
        
        System.err.println("\n<!--");
        System.err.println("  transform  = " + mm / _iterations + " ms");
        

        System.err.println("  throughput = " + 1000.0D / (mm / _iterations) + " tps");
        


        System.err.println("-->");
      }
    }
    catch (TransletException e) {
      if (_debug) e.printStackTrace();
      System.err.println(new ErrorMsg("RUNTIME_ERROR_KEY") + e.getMessage());
    }
    catch (RuntimeException e)
    {
      if (_debug) e.printStackTrace();
      System.err.println(new ErrorMsg("RUNTIME_ERROR_KEY") + e.getMessage());
    }
    catch (FileNotFoundException e)
    {
      if (_debug) e.printStackTrace();
      ErrorMsg err = new ErrorMsg("FILE_NOT_FOUND_ERR", _fileName);
      System.err.println(new ErrorMsg("RUNTIME_ERROR_KEY") + err.toString());
    }
    catch (MalformedURLException e)
    {
      if (_debug) e.printStackTrace();
      ErrorMsg err = new ErrorMsg("INVALID_URI_ERR", _fileName);
      System.err.println(new ErrorMsg("RUNTIME_ERROR_KEY") + err.toString());
    }
    catch (ClassNotFoundException e)
    {
      if (_debug) e.printStackTrace();
      ErrorMsg err = new ErrorMsg("CLASS_NOT_FOUND_ERR", _className);
      System.err.println(new ErrorMsg("RUNTIME_ERROR_KEY") + err.toString());
    }
    catch (UnknownHostException e)
    {
      if (_debug) e.printStackTrace();
      ErrorMsg err = new ErrorMsg("INVALID_URI_ERR", _fileName);
      System.err.println(new ErrorMsg("RUNTIME_ERROR_KEY") + err.toString());
    }
    catch (SAXException e)
    {
      Exception ex = e.getException();
      if (_debug) {
        if (ex != null) ex.printStackTrace();
        e.printStackTrace();
      }
      System.err.print(new ErrorMsg("RUNTIME_ERROR_KEY"));
      if (ex != null) {
        System.err.println(ex.getMessage());
      } else {
        System.err.println(e.getMessage());
      }
    } catch (Exception e) {
      if (_debug) e.printStackTrace();
      System.err.println(new ErrorMsg("RUNTIME_ERROR_KEY") + e.getMessage());
    }
  }
  
  public static void printUsage()
  {
    System.err.println(new ErrorMsg("TRANSFORM_USAGE_STR"));
  }
  
  public static void main(String[] args) {
    try {
      if (args.length > 0)
      {
        int iterations = -1;
        boolean uri = false;boolean debug = false;
        boolean isJarFileSpecified = false;
        String jarFile = null;
        

        for (int i = 0; (i < args.length) && (args[i].charAt(0) == '-'); i++) {
          if (args[i].equals("-u")) {
            uri = true;
          }
          else if (args[i].equals("-x")) {
            debug = true;
          }
          else if (args[i].equals("-j")) {
            isJarFileSpecified = true;
            jarFile = args[(++i)];
          }
          else if (args[i].equals("-n")) {
            try {
              iterations = Integer.parseInt(args[(++i)]);

            }
            catch (NumberFormatException e) {}
          }
          else
          {
            printUsage();
          }
        }
        

        if (args.length - i < 2) { printUsage();
        }
        
        Transform handler = new Transform(args[(i + 1)], args[i], uri, debug, iterations);
        
        handler.setJarFileInputSrc(isJarFileSpecified, jarFile);
        

        Vector params = new Vector();
        for (i += 2; i < args.length; i++) {
          int equal = args[i].indexOf('=');
          if (equal > 0) {
            String name = args[i].substring(0, equal);
            String value = args[i].substring(equal + 1);
            params.addElement(new Parameter(name, value));
          }
          else {
            printUsage();
          }
        }
        
        if (i == args.length) {
          handler.setParameters(params);
          handler.doTransform();
        }
      } else {
        printUsage();
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
