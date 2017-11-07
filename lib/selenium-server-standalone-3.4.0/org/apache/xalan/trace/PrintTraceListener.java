package org.apache.xalan.trace;

import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.ElemTemplate;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.ElemTextLiteral;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.ref.DTMNodeProxy;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.Node;




























public class PrintTraceListener
  implements TraceListenerEx3
{
  PrintWriter m_pw;
  
  public PrintTraceListener(PrintWriter pw)
  {
    m_pw = pw;
  }
  








  public boolean m_traceTemplates = false;
  



  public boolean m_traceElements = false;
  



  public boolean m_traceGeneration = false;
  



  public boolean m_traceSelection = false;
  



  public boolean m_traceExtension = false;
  






  public void _trace(TracerEvent ev)
  {
    switch (m_styleNode.getXSLToken())
    {
    case 78: 
      if (m_traceElements)
      {
        m_pw.print(m_styleNode.getSystemId() + " Line #" + m_styleNode.getLineNumber() + ", " + "Column #" + m_styleNode.getColumnNumber() + " -- " + m_styleNode.getNodeName() + ": ");
        


        ElemTextLiteral etl = (ElemTextLiteral)m_styleNode;
        String chars = new String(etl.getChars(), 0, etl.getChars().length);
        
        m_pw.println("    " + chars.trim()); }
      break;
    
    case 19: 
      if ((m_traceTemplates) || (m_traceElements))
      {
        ElemTemplate et = (ElemTemplate)m_styleNode;
        
        m_pw.print(et.getSystemId() + " Line #" + et.getLineNumber() + ", " + "Column #" + et.getColumnNumber() + ": " + et.getNodeName() + " ");
        

        if (null != et.getMatch())
        {
          m_pw.print("match='" + et.getMatch().getPatternString() + "' ");
        }
        
        if (null != et.getName())
        {
          m_pw.print("name='" + et.getName() + "' ");
        }
        
        m_pw.println(); }
      break;
    
    default: 
      if (m_traceElements)
      {
        m_pw.println(m_styleNode.getSystemId() + " Line #" + m_styleNode.getLineNumber() + ", " + "Column #" + m_styleNode.getColumnNumber() + ": " + m_styleNode.getNodeName());
      }
      break;
    }
    
  }
  
  int m_indent = 0;
  












  public void trace(TracerEvent ev)
  {
    _trace(ev);
  }
  












  public void traceEnd(TracerEvent ev) {}
  












  public void selected(SelectionEvent ev)
    throws TransformerException
  {
    if (m_traceSelection) {
      ElemTemplateElement ete = m_styleNode;
      Node sourceNode = m_sourceNode;
      
      SourceLocator locator = null;
      if ((sourceNode instanceof DTMNodeProxy)) {
        int nodeHandler = ((DTMNodeProxy)sourceNode).getDTMNodeNumber();
        locator = ((DTMNodeProxy)sourceNode).getDTM().getSourceLocatorFor(nodeHandler);
      }
      


      if (locator != null) {
        m_pw.println("Selected source node '" + sourceNode.getNodeName() + "', at " + locator);

      }
      else
      {

        m_pw.println("Selected source node '" + sourceNode.getNodeName() + "'");
      }
      
      if (m_styleNode.getLineNumber() == 0)
      {


        ElemTemplateElement parent = ete.getParentElem();
        

        if (parent == ete.getStylesheetRoot().getDefaultRootRule()) {
          m_pw.print("(default root rule) ");
        } else if (parent == ete.getStylesheetRoot().getDefaultTextRule())
        {
          m_pw.print("(default text rule) ");
        } else if (parent == ete.getStylesheetRoot().getDefaultRule()) {
          m_pw.print("(default rule) ");
        }
        
        m_pw.print(ete.getNodeName() + ", " + m_attributeName + "='" + m_xpath.getPatternString() + "': ");


      }
      else
      {


        m_pw.print(m_styleNode.getSystemId() + " Line #" + m_styleNode.getLineNumber() + ", " + "Column #" + m_styleNode.getColumnNumber() + ": " + ete.getNodeName() + ", " + m_attributeName + "='" + m_xpath.getPatternString() + "': ");
      }
      













      if (m_selection.getType() == 4) {
        m_pw.println();
        
        DTMIterator nl = m_selection.iter();
        




        int currentPos = -1;
        currentPos = nl.getCurrentPos();
        nl.setShouldCacheNodes(true);
        DTMIterator clone = null;
        
        try
        {
          clone = nl.cloneWithReset();
        } catch (CloneNotSupportedException cnse) {
          m_pw.println("     [Can't trace nodelist because it it threw a CloneNotSupportedException]");
          
          return;
        }
        int pos = clone.nextNode();
        
        if (-1 == pos) {
          m_pw.println("     [empty node list]");
        } else {
          while (-1 != pos)
          {
            DTM dtm = m_processor.getXPathContext().getDTM(pos);
            m_pw.print("     ");
            m_pw.print(Integer.toHexString(pos));
            m_pw.print(": ");
            m_pw.println(dtm.getNodeName(pos));
            pos = clone.nextNode();
          }
        }
        

        nl.runTo(-1);
        nl.setCurrentPos(currentPos);
      }
      else
      {
        m_pw.println(m_selection.str());
      }
    }
  }
  








  public void selectEnd(EndSelectionEvent ev)
    throws TransformerException
  {}
  








  public void generated(GenerateEvent ev)
  {
    if (m_traceGeneration)
    {
      switch (m_eventtype)
      {
      case 1: 
        m_pw.println("STARTDOCUMENT");
        break;
      case 2: 
        m_pw.println("ENDDOCUMENT");
        break;
      case 3: 
        m_pw.println("STARTELEMENT: " + m_name);
        break;
      case 4: 
        m_pw.println("ENDELEMENT: " + m_name);
        break;
      
      case 5: 
        String chars = new String(m_characters, m_start, m_length);
        
        m_pw.println("CHARACTERS: " + chars);
        
        break;
      
      case 10: 
        String chars = new String(m_characters, m_start, m_length);
        
        m_pw.println("CDATA: " + chars);
        
        break;
      case 8: 
        m_pw.println("COMMENT: " + m_data);
        break;
      case 7: 
        m_pw.println("PI: " + m_name + ", " + m_data);
        break;
      case 9: 
        m_pw.println("ENTITYREF: " + m_name);
        break;
      case 6: 
        m_pw.println("IGNORABLEWHITESPACE");
      }
      
    }
  }
  




  public void extension(ExtensionEvent ev)
  {
    if (m_traceExtension) {
      switch (m_callType) {
      case 0: 
        m_pw.println("EXTENSION: " + ((Class)m_method).getName() + "#<init>");
        break;
      case 1: 
        m_pw.println("EXTENSION: " + ((Method)m_method).getDeclaringClass().getName() + "#" + ((Method)m_method).getName());
        break;
      case 2: 
        m_pw.println("EXTENSION: " + ((Constructor)m_method).getDeclaringClass().getName() + "#<init>");
      }
    }
  }
  
  public void extensionEnd(ExtensionEvent ev) {}
}
