package org.apache.xalan.xsltc.runtime.output;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xalan.xsltc.trax.SAX2DOM;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.serializer.ToHTMLStream;
import org.apache.xml.serializer.ToTextStream;
import org.apache.xml.serializer.ToUnknownStream;
import org.apache.xml.serializer.ToXMLSAXHandler;
import org.apache.xml.serializer.ToXMLStream;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.ext.LexicalHandler;


























public class TransletOutputHandlerFactory
{
  public static final int STREAM = 0;
  public static final int SAX = 1;
  public static final int DOM = 2;
  
  public TransletOutputHandlerFactory() {}
  
  private String _encoding = "utf-8";
  private String _method = null;
  private int _outputType = 0;
  private OutputStream _ostream = System.out;
  private Writer _writer = null;
  private Node _node = null;
  private Node _nextSibling = null;
  private int _indentNumber = -1;
  private ContentHandler _handler = null;
  private LexicalHandler _lexHandler = null;
  
  public static TransletOutputHandlerFactory newInstance() {
    return new TransletOutputHandlerFactory();
  }
  
  public void setOutputType(int outputType) {
    _outputType = outputType;
  }
  
  public void setEncoding(String encoding) {
    if (encoding != null) {
      _encoding = encoding;
    }
  }
  
  public void setOutputMethod(String method) {
    _method = method;
  }
  
  public void setOutputStream(OutputStream ostream) {
    _ostream = ostream;
  }
  
  public void setWriter(Writer writer) {
    _writer = writer;
  }
  
  public void setHandler(ContentHandler handler) {
    _handler = handler;
  }
  
  public void setLexicalHandler(LexicalHandler lex) {
    _lexHandler = lex;
  }
  
  public void setNode(Node node) {
    _node = node;
  }
  
  public Node getNode() {
    return (_handler instanceof SAX2DOM) ? ((SAX2DOM)_handler).getDOM() : null;
  }
  
  public void setNextSibling(Node nextSibling)
  {
    _nextSibling = nextSibling;
  }
  
  public void setIndentNumber(int value) {
    _indentNumber = value;
  }
  
  public SerializationHandler getSerializationHandler()
    throws IOException, ParserConfigurationException
  {
    SerializationHandler result = null;
    switch (_outputType)
    {

    case 0: 
      if (_method == null)
      {
        result = new ToUnknownStream();
      }
      else if (_method.equalsIgnoreCase("xml"))
      {

        result = new ToXMLStream();

      }
      else if (_method.equalsIgnoreCase("html"))
      {

        result = new ToHTMLStream();

      }
      else if (_method.equalsIgnoreCase("text"))
      {

        result = new ToTextStream();
      }
      

      if ((result != null) && (_indentNumber >= 0))
      {
        result.setIndentAmount(_indentNumber);
      }
      
      result.setEncoding(_encoding);
      
      if (_writer != null)
      {
        result.setWriter(_writer);
      }
      else
      {
        result.setOutputStream(_ostream);
      }
      return result;
    
    case 2: 
      _handler = (_node != null ? new SAX2DOM(_node, _nextSibling) : new SAX2DOM());
      _lexHandler = ((LexicalHandler)_handler);
    
    case 1: 
      if (_method == null)
      {
        _method = "xml";
      }
      
      if (_lexHandler == null)
      {
        result = new ToXMLSAXHandler(_handler, _encoding);
      }
      else
      {
        result = new ToXMLSAXHandler(_handler, _lexHandler, _encoding);
      }
      




      return result; }
    
    return null;
  }
}
