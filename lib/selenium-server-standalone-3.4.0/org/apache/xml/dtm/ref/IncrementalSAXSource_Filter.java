package org.apache.xml.dtm.ref;

import java.io.IOException;
import java.io.PrintStream;
import org.apache.xml.res.XMLMessages;
import org.apache.xml.utils.ThreadControllerWrapper;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;
















































public class IncrementalSAXSource_Filter
  implements IncrementalSAXSource, ContentHandler, DTDHandler, LexicalHandler, ErrorHandler, Runnable
{
  boolean DEBUG = false;
  



  private CoroutineManager fCoroutineManager = null;
  private int fControllerCoroutineID = -1;
  private int fSourceCoroutineID = -1;
  
  private ContentHandler clientContentHandler = null;
  private LexicalHandler clientLexicalHandler = null;
  private DTDHandler clientDTDHandler = null;
  private ErrorHandler clientErrorHandler = null;
  private int eventcounter;
  private int frequency = 5;
  



  private boolean fNoMoreEvents = false;
  

  private XMLReader fXMLReader = null;
  private InputSource fXMLReaderInputSource = null;
  



  public IncrementalSAXSource_Filter()
  {
    init(new CoroutineManager(), -1, -1);
  }
  



  public IncrementalSAXSource_Filter(CoroutineManager co, int controllerCoroutineID)
  {
    init(co, controllerCoroutineID, -1);
  }
  


  public static IncrementalSAXSource createIncrementalSAXSource(CoroutineManager co, int controllerCoroutineID)
  {
    return new IncrementalSAXSource_Filter(co, controllerCoroutineID);
  }
  





  public void init(CoroutineManager co, int controllerCoroutineID, int sourceCoroutineID)
  {
    if (co == null)
      co = new CoroutineManager();
    fCoroutineManager = co;
    fControllerCoroutineID = co.co_joinCoroutineSet(controllerCoroutineID);
    fSourceCoroutineID = co.co_joinCoroutineSet(sourceCoroutineID);
    if ((fControllerCoroutineID == -1) || (fSourceCoroutineID == -1)) {
      throw new RuntimeException(XMLMessages.createXMLMessage("ER_COJOINROUTINESET_FAILED", null));
    }
    fNoMoreEvents = false;
    eventcounter = frequency;
  }
  





  public void setXMLReader(XMLReader eventsource)
  {
    fXMLReader = eventsource;
    eventsource.setContentHandler(this);
    eventsource.setDTDHandler(this);
    eventsource.setErrorHandler(this);
    

    try
    {
      eventsource.setProperty("http://xml.org/sax/properties/lexical-handler", this);
    }
    catch (SAXNotRecognizedException e) {}catch (SAXNotSupportedException e) {}
  }
  













  public void setContentHandler(ContentHandler handler)
  {
    clientContentHandler = handler;
  }
  
  public void setDTDHandler(DTDHandler handler)
  {
    clientDTDHandler = handler;
  }
  



  public void setLexicalHandler(LexicalHandler handler)
  {
    clientLexicalHandler = handler;
  }
  

  public void setErrHandler(ErrorHandler handler)
  {
    clientErrorHandler = handler;
  }
  


  public void setReturnFrequency(int events)
  {
    if (events < 1) events = 1;
    frequency = (this.eventcounter = events);
  }
  



















  public void characters(char[] ch, int start, int length)
    throws SAXException
  {
    if (--eventcounter <= 0)
    {
      co_yield(true);
      eventcounter = frequency;
    }
    if (clientContentHandler != null) {
      clientContentHandler.characters(ch, start, length);
    }
  }
  
  public void endDocument() throws SAXException
  {
    if (clientContentHandler != null) {
      clientContentHandler.endDocument();
    }
    eventcounter = 0;
    co_yield(false);
  }
  
  public void endElement(String namespaceURI, String localName, String qName)
    throws SAXException
  {
    if (--eventcounter <= 0)
    {
      co_yield(true);
      eventcounter = frequency;
    }
    if (clientContentHandler != null) {
      clientContentHandler.endElement(namespaceURI, localName, qName);
    }
  }
  
  public void endPrefixMapping(String prefix) throws SAXException {
    if (--eventcounter <= 0)
    {
      co_yield(true);
      eventcounter = frequency;
    }
    if (clientContentHandler != null) {
      clientContentHandler.endPrefixMapping(prefix);
    }
  }
  
  public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
    if (--eventcounter <= 0)
    {
      co_yield(true);
      eventcounter = frequency;
    }
    if (clientContentHandler != null) {
      clientContentHandler.ignorableWhitespace(ch, start, length);
    }
  }
  
  public void processingInstruction(String target, String data) throws SAXException {
    if (--eventcounter <= 0)
    {
      co_yield(true);
      eventcounter = frequency;
    }
    if (clientContentHandler != null)
      clientContentHandler.processingInstruction(target, data);
  }
  
  public void setDocumentLocator(Locator locator) {
    if (--eventcounter <= 0)
    {


      eventcounter = frequency;
    }
    if (clientContentHandler != null) {
      clientContentHandler.setDocumentLocator(locator);
    }
  }
  
  public void skippedEntity(String name) throws SAXException {
    if (--eventcounter <= 0)
    {
      co_yield(true);
      eventcounter = frequency;
    }
    if (clientContentHandler != null) {
      clientContentHandler.skippedEntity(name);
    }
  }
  
  public void startDocument() throws SAXException {
    co_entry_pause();
    

    if (--eventcounter <= 0)
    {
      co_yield(true);
      eventcounter = frequency;
    }
    if (clientContentHandler != null) {
      clientContentHandler.startDocument();
    }
  }
  
  public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException
  {
    if (--eventcounter <= 0)
    {
      co_yield(true);
      eventcounter = frequency;
    }
    if (clientContentHandler != null) {
      clientContentHandler.startElement(namespaceURI, localName, qName, atts);
    }
  }
  
  public void startPrefixMapping(String prefix, String uri) throws SAXException {
    if (--eventcounter <= 0)
    {
      co_yield(true);
      eventcounter = frequency;
    }
    if (clientContentHandler != null) {
      clientContentHandler.startPrefixMapping(prefix, uri);
    }
  }
  









  public void comment(char[] ch, int start, int length)
    throws SAXException
  {
    if (null != clientLexicalHandler) {
      clientLexicalHandler.comment(ch, start, length);
    }
  }
  
  public void endCDATA() throws SAXException {
    if (null != clientLexicalHandler) {
      clientLexicalHandler.endCDATA();
    }
  }
  
  public void endDTD() throws SAXException {
    if (null != clientLexicalHandler) {
      clientLexicalHandler.endDTD();
    }
  }
  
  public void endEntity(String name) throws SAXException {
    if (null != clientLexicalHandler) {
      clientLexicalHandler.endEntity(name);
    }
  }
  
  public void startCDATA() throws SAXException {
    if (null != clientLexicalHandler) {
      clientLexicalHandler.startCDATA();
    }
  }
  
  public void startDTD(String name, String publicId, String systemId) throws SAXException
  {
    if (null != clientLexicalHandler) {
      clientLexicalHandler.startDTD(name, publicId, systemId);
    }
  }
  
  public void startEntity(String name) throws SAXException {
    if (null != clientLexicalHandler) {
      clientLexicalHandler.startEntity(name);
    }
  }
  

  public void notationDecl(String a, String b, String c)
    throws SAXException
  {
    if (null != clientDTDHandler)
      clientDTDHandler.notationDecl(a, b, c);
  }
  
  public void unparsedEntityDecl(String a, String b, String c, String d) throws SAXException {
    if (null != clientDTDHandler) {
      clientDTDHandler.unparsedEntityDecl(a, b, c, d);
    }
  }
  













  public void error(SAXParseException exception)
    throws SAXException
  {
    if (null != clientErrorHandler) {
      clientErrorHandler.error(exception);
    }
  }
  
  public void fatalError(SAXParseException exception)
    throws SAXException
  {
    if (null != clientErrorHandler) {
      clientErrorHandler.error(exception);
    }
    eventcounter = 0;
    co_yield(false);
  }
  
  public void warning(SAXParseException exception)
    throws SAXException
  {
    if (null != clientErrorHandler) {
      clientErrorHandler.error(exception);
    }
  }
  



  public int getSourceCoroutineID()
  {
    return fSourceCoroutineID;
  }
  
  public int getControllerCoroutineID() { return fControllerCoroutineID; }
  






  public CoroutineManager getCoroutineManager()
  {
    return fCoroutineManager;
  }
  










  protected void count_and_yield(boolean moreExpected)
    throws SAXException
  {
    if (!moreExpected) { eventcounter = 0;
    }
    if (--eventcounter <= 0)
    {
      co_yield(true);
      eventcounter = frequency;
    }
  }
  






  private void co_entry_pause()
    throws SAXException
  {
    if (fCoroutineManager == null)
    {

      init(null, -1, -1);
    }
    
    try
    {
      Object arg = fCoroutineManager.co_entry_pause(fSourceCoroutineID);
      if (arg == Boolean.FALSE) {
        co_yield(false);
      }
      
    }
    catch (NoSuchMethodException e)
    {
      if (DEBUG) e.printStackTrace();
      throw new SAXException(e);
    }
  }
  






















  private void co_yield(boolean moreRemains)
    throws SAXException
  {
    if (fNoMoreEvents) {
      return;
    }
    try
    {
      Object arg = Boolean.FALSE;
      if (moreRemains)
      {

        arg = fCoroutineManager.co_resume(Boolean.TRUE, fSourceCoroutineID, fControllerCoroutineID);
      }
      



      if (arg == Boolean.FALSE)
      {
        fNoMoreEvents = true;
        
        if (fXMLReader != null) {
          throw new StopException();
        }
        
        fCoroutineManager.co_exit_to(Boolean.FALSE, fSourceCoroutineID, fControllerCoroutineID);
      }
      

    }
    catch (NoSuchMethodException e)
    {

      fNoMoreEvents = true;
      fCoroutineManager.co_exit(fSourceCoroutineID);
      throw new SAXException(e);
    }
  }
  












  public void startParse(InputSource source)
    throws SAXException
  {
    if (fNoMoreEvents)
      throw new SAXException(XMLMessages.createXMLMessage("ER_INCRSAXSRCFILTER_NOT_RESTARTABLE", null));
    if (fXMLReader == null) {
      throw new SAXException(XMLMessages.createXMLMessage("ER_XMLRDR_NOT_BEFORE_STARTPARSE", null));
    }
    fXMLReaderInputSource = source;
    


    ThreadControllerWrapper.runThread(this, -1);
  }
  



  public void run()
  {
    if (fXMLReader == null) { return;
    }
    if (DEBUG) { System.out.println("IncrementalSAXSource_Filter parse thread launched");
    }
    
    Object arg = Boolean.FALSE;
    




    try
    {
      fXMLReader.parse(fXMLReaderInputSource);
    }
    catch (IOException ex)
    {
      arg = ex;

    }
    catch (StopException ex)
    {
      if (DEBUG) System.out.println("Active IncrementalSAXSource_Filter normal stop exception");
    }
    catch (SAXException ex)
    {
      Exception inner = ex.getException();
      if ((inner instanceof StopException))
      {
        if (DEBUG) { System.out.println("Active IncrementalSAXSource_Filter normal stop exception");
        }
      }
      else
      {
        if (DEBUG)
        {
          System.out.println("Active IncrementalSAXSource_Filter UNEXPECTED SAX exception: " + inner);
          inner.printStackTrace();
        }
        arg = ex;
      }
    }
    

    fXMLReader = null;
    

    try
    {
      fNoMoreEvents = true;
      fCoroutineManager.co_exit_to(arg, fSourceCoroutineID, fControllerCoroutineID);


    }
    catch (NoSuchMethodException e)
    {

      e.printStackTrace(System.err);
      fCoroutineManager.co_exit(fSourceCoroutineID);
    }
  }
  

























  public Object deliverMoreNodes(boolean parsemore)
  {
    if (fNoMoreEvents) {
      return Boolean.FALSE;
    }
    try
    {
      Object result = fCoroutineManager.co_resume(parsemore ? Boolean.TRUE : Boolean.FALSE, fControllerCoroutineID, fSourceCoroutineID);
      

      if (result == Boolean.FALSE) {
        fCoroutineManager.co_exit(fControllerCoroutineID);
      }
      return result;


    }
    catch (NoSuchMethodException e)
    {


      return e;
    }
  }
  
  static class StopException
    extends RuntimeException
  {
    static final long serialVersionUID = -1129245796185754956L;
    
    StopException() {}
  }
}
