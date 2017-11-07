package org.apache.xalan.client;

import java.applet.Applet;
import java.awt.Graphics;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.xalan.res.XSLMessages;











































public class XSLTProcessorApplet
  extends Applet
{
  transient TransformerFactory m_tfactory = null;
  





  private String m_styleURL;
  




  private String m_documentURL;
  




  private final String PARAM_styleURL = "styleURL";
  



  private final String PARAM_documentURL = "documentURL";
  







  private String m_styleURLOfCached = null;
  



  private String m_documentURLOfCached = null;
  




  private URL m_codeBase = null;
  



  private String m_treeURL = null;
  




  private URL m_documentBase = null;
  



  private transient Thread m_callThread = null;
  


  private transient TrustedAgent m_trustedAgent = null;
  



  private transient Thread m_trustedWorker = null;
  



  private transient String m_htmlText = null;
  



  private transient String m_sourceText = null;
  



  private transient String m_nameOfIDAttrOfElemToModify = null;
  


  private transient String m_elemIdToModify = null;
  


  private transient String m_attrNameToSet = null;
  


  private transient String m_attrValueToSet = null;
  

  transient Hashtable m_parameters;
  
  private static final long serialVersionUID = 4618876841979251422L;
  

  public XSLTProcessorApplet() {}
  

  public String getAppletInfo()
  {
    return "Name: XSLTProcessorApplet\r\nAuthor: Scott Boag";
  }
  






  public String[][] getParameterInfo()
  {
    String[][] info = { { "styleURL", "String", "URL to an XSL stylesheet" }, { "documentURL", "String", "URL to an XML document" } };
    




    return info;
  }
  













  public void init()
  {
    String param = getParameter("styleURL");
    

    m_parameters = new Hashtable();
    
    if (param != null) {
      setStyleURL(param);
    }
    

    param = getParameter("documentURL");
    
    if (param != null) {
      setDocumentURL(param);
    }
    m_codeBase = getCodeBase();
    m_documentBase = getDocumentBase();
    






    resize(320, 240);
  }
  





  public void start()
  {
    m_trustedAgent = new TrustedAgent();
    Thread currentThread = Thread.currentThread();
    m_trustedWorker = new Thread(currentThread.getThreadGroup(), m_trustedAgent);
    
    m_trustedWorker.start();
    try
    {
      m_tfactory = TransformerFactory.newInstance();
      showStatus("Causing Transformer and Parser to Load and JIT...");
      

      StringReader xmlbuf = new StringReader("<?xml version='1.0'?><foo/>");
      StringReader xslbuf = new StringReader("<?xml version='1.0'?><xsl:stylesheet xmlns:xsl='http://www.w3.org/1999/XSL/Transform' version='1.0'><xsl:template match='foo'><out/></xsl:template></xsl:stylesheet>");
      
      PrintWriter pw = new PrintWriter(new StringWriter());
      
      synchronized (m_tfactory)
      {
        Templates templates = m_tfactory.newTemplates(new StreamSource(xslbuf));
        Transformer transformer = templates.newTransformer();
        transformer.transform(new StreamSource(xmlbuf), new StreamResult(pw));
      }
      System.out.println("Primed the pump!");
      showStatus("Ready to go!");
    }
    catch (Exception e)
    {
      showStatus("Could not prime the pump!");
      System.out.println("Could not prime the pump!");
      e.printStackTrace();
    }
  }
  




  public void paint(Graphics g) {}
  




  public void stop()
  {
    if (null != m_trustedWorker)
    {
      m_trustedWorker.stop();
      

      m_trustedWorker = null;
    }
    
    m_styleURLOfCached = null;
    m_documentURLOfCached = null;
  }
  



  public void destroy()
  {
    if (null != m_trustedWorker)
    {
      m_trustedWorker.stop();
      

      m_trustedWorker = null;
    }
    m_styleURLOfCached = null;
    m_documentURLOfCached = null;
  }
  





  public void setStyleURL(String urlString)
  {
    m_styleURL = urlString;
  }
  





  public void setDocumentURL(String urlString)
  {
    m_documentURL = urlString;
  }
  





  public void freeCache()
  {
    m_styleURLOfCached = null;
    m_documentURLOfCached = null;
  }
  










  public void setStyleSheetAttribute(String nameOfIDAttrOfElemToModify, String elemId, String attrName, String value)
  {
    m_nameOfIDAttrOfElemToModify = nameOfIDAttrOfElemToModify;
    m_elemIdToModify = elemId;
    m_attrNameToSet = attrName;
    m_attrValueToSet = value;
  }
  













  public void setStylesheetParam(String key, String expr)
  {
    m_parameters.put(key, expr);
  }
  








  public String escapeString(String s)
  {
    StringBuffer sb = new StringBuffer();
    int length = s.length();
    
    for (int i = 0; i < length; i++)
    {
      char ch = s.charAt(i);
      
      if ('<' == ch)
      {
        sb.append("&lt;");
      }
      else if ('>' == ch)
      {
        sb.append("&gt;");
      }
      else if ('&' == ch)
      {
        sb.append("&amp;");
      }
      else if ((55296 <= ch) && (ch < 56320))
      {



        if (i + 1 >= length)
        {
          throw new RuntimeException(XSLMessages.createMessage("ER_INVALID_UTF16_SURROGATE", new Object[] { Integer.toHexString(ch) }));
        }
        






        int next = s.charAt(++i);
        
        if ((56320 > next) || (next >= 57344)) {
          throw new RuntimeException(XSLMessages.createMessage("ER_INVALID_UTF16_SURROGATE", new Object[] { Integer.toHexString(ch) + " " + Integer.toHexString(next) }));
        }
        





        next = (ch - 55296 << 10) + next - 56320 + 65536;
        
        sb.append("&#x");
        sb.append(Integer.toHexString(next));
        sb.append(";");
      }
      else
      {
        sb.append(ch);
      }
    }
    return sb.toString();
  }
  







  public String getHtmlText()
  {
    m_trustedAgent.m_getData = true;
    m_callThread = Thread.currentThread();
    try
    {
      synchronized (m_callThread)
      {
        m_callThread.wait();
      }
    }
    catch (InterruptedException ie)
    {
      System.out.println(ie.getMessage());
    }
    return m_htmlText;
  }
  








  public String getTreeAsText(String treeURL)
    throws IOException
  {
    m_treeURL = treeURL;
    m_trustedAgent.m_getData = true;
    m_trustedAgent.m_getSource = true;
    m_callThread = Thread.currentThread();
    try
    {
      synchronized (m_callThread)
      {
        m_callThread.wait();
      }
    }
    catch (InterruptedException ie)
    {
      System.out.println(ie.getMessage());
    }
    return m_sourceText;
  }
  





  private String getSource()
    throws TransformerException
  {
    StringWriter osw = new StringWriter();
    PrintWriter pw = new PrintWriter(osw, false);
    String text = "";
    try
    {
      URL docURL = new URL(m_documentBase, m_treeURL);
      synchronized (m_tfactory)
      {
        Transformer transformer = m_tfactory.newTransformer();
        StreamSource source = new StreamSource(docURL.toString());
        StreamResult result = new StreamResult(pw);
        transformer.transform(source, result);
        text = osw.toString();
      }
    }
    catch (MalformedURLException e)
    {
      e.printStackTrace();
      throw new RuntimeException(e.getMessage());
    }
    catch (Exception any_error)
    {
      any_error.printStackTrace();
    }
    return text;
  }
  







  public String getSourceTreeAsText()
    throws Exception
  {
    return getTreeAsText(m_documentURL);
  }
  







  public String getStyleTreeAsText()
    throws Exception
  {
    return getTreeAsText(m_styleURL);
  }
  







  public String getResultTreeAsText()
    throws Exception
  {
    return escapeString(getHtmlText());
  }
  











  public String transformToHtml(String doc, String style)
  {
    if (null != doc)
    {
      m_documentURL = doc;
    }
    
    if (null != style)
    {
      m_styleURL = style;
    }
    
    return getHtmlText();
  }
  










  public String transformToHtml(String doc)
  {
    if (null != doc)
    {
      m_documentURL = doc;
    }
    
    m_styleURL = null;
    
    return getHtmlText();
  }
  







  private String processTransformation()
    throws TransformerException
  {
    String htmlData = null;
    showStatus("Waiting for Transformer and Parser to finish loading and JITing...");
    
    synchronized (m_tfactory)
    {
      URL documentURL = null;
      URL styleURL = null;
      StringWriter osw = new StringWriter();
      PrintWriter pw = new PrintWriter(osw, false);
      StreamResult result = new StreamResult(pw);
      
      showStatus("Begin Transformation...");
      try
      {
        documentURL = new URL(m_codeBase, m_documentURL);
        StreamSource xmlSource = new StreamSource(documentURL.toString());
        
        styleURL = new URL(m_codeBase, m_styleURL);
        StreamSource xslSource = new StreamSource(styleURL.toString());
        
        Transformer transformer = m_tfactory.newTransformer(xslSource);
        
        Iterator m_entries = m_parameters.entrySet().iterator();
        while (m_entries.hasNext()) {
          Map.Entry entry = (Map.Entry)m_entries.next();
          Object key = entry.getKey();
          Object expression = entry.getValue();
          transformer.setParameter((String)key, expression);
        }
        transformer.transform(xmlSource, result);
      }
      catch (TransformerConfigurationException tfe)
      {
        tfe.printStackTrace();
        throw new RuntimeException(tfe.getMessage());
      }
      catch (MalformedURLException e)
      {
        e.printStackTrace();
        throw new RuntimeException(e.getMessage());
      }
      
      showStatus("Transformation Done!");
      htmlData = osw.toString();
    }
    return htmlData;
  }
  









  class TrustedAgent
    implements Runnable
  {
    public boolean m_getData = false;
    



    public boolean m_getSource = false;
    

    TrustedAgent() {}
    

    public void run()
    {
      for (;;)
      {
        
        
        if (m_getData)
        {
          try
          {
            m_getData = false;
            m_htmlText = null;
            m_sourceText = null;
            if (m_getSource)
            {
              m_getSource = false;
              m_sourceText = XSLTProcessorApplet.this.getSource();
            }
            else {
              m_htmlText = XSLTProcessorApplet.this.processTransformation();
            }
          }
          catch (Exception e) {
            e.printStackTrace();
          }
          finally
          {
            synchronized (m_callThread)
            {
              m_callThread.notify();
            }
            
          }
          
        } else {
          try
          {
            Thread.sleep(50L);
          }
          catch (InterruptedException ie)
          {
            ie.printStackTrace();
          }
        }
      }
    }
  }
  





  private void readObject(ObjectInputStream inStream)
    throws IOException, ClassNotFoundException
  {
    inStream.defaultReadObject();
    




    m_tfactory = TransformerFactory.newInstance();
  }
}
