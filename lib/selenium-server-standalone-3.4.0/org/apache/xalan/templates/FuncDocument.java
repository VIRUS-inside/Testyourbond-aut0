package org.apache.xalan.templates;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xml.utils.XMLString;
import org.apache.xpath.Expression;
import org.apache.xpath.NodeSetDTM;
import org.apache.xpath.SourceTreeManager;
import org.apache.xpath.XPathContext;
import org.apache.xpath.functions.Function2Args;
import org.apache.xpath.functions.WrongNumberArgsException;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XObject;








































public class FuncDocument
  extends Function2Args
{
  static final long serialVersionUID = 2483304325971281424L;
  
  public FuncDocument() {}
  
  public XObject execute(XPathContext xctxt)
    throws TransformerException
  {
    int context = xctxt.getCurrentNode();
    DTM dtm = xctxt.getDTM(context);
    
    int docContext = dtm.getDocumentRoot(context);
    XObject arg = getArg0().execute(xctxt);
    
    String base = "";
    Expression arg1Expr = getArg1();
    
    if (null != arg1Expr)
    {





      XObject arg2 = arg1Expr.execute(xctxt);
      
      if (4 == arg2.getType())
      {
        int baseNode = arg2.iter().nextNode();
        
        if (baseNode == -1)
        {



          warn(xctxt, "WG_EMPTY_SECOND_ARG", null);
          XNodeSet nodes = new XNodeSet(xctxt.getDTMManager());
          return nodes;
        }
        DTM baseDTM = xctxt.getDTM(baseNode);
        base = baseDTM.getDocumentBaseURI();







      }
      else
      {







        arg2.iter();



      }
      



    }
    else
    {



      assertion(null != xctxt.getNamespaceContext(), "Namespace context can not be null!");
      base = xctxt.getNamespaceContext().getBaseIdentifier();
    }
    
    XNodeSet nodes = new XNodeSet(xctxt.getDTMManager());
    NodeSetDTM mnl = nodes.mutableNodeset();
    DTMIterator iterator = 4 == arg.getType() ? arg.iter() : null;
    
    int pos = -1;
    
    while ((null == iterator) || (-1 != (pos = iterator.nextNode())))
    {
      XMLString ref = null != iterator ? xctxt.getDTM(pos).getStringValue(pos) : arg.xstr();
      









      if ((null == arg1Expr) && (-1 != pos))
      {
        DTM baseDTM = xctxt.getDTM(pos);
        base = baseDTM.getDocumentBaseURI();
      }
      
      if (null != ref)
      {

        if (-1 == docContext)
        {
          error(xctxt, "ER_NO_CONTEXT_OWNERDOC", null);
        }
        






        int indexOfColon = ref.indexOf(58);
        int indexOfSlash = ref.indexOf(47);
        
        if ((indexOfColon != -1) && (indexOfSlash != -1) && (indexOfColon < indexOfSlash))
        {



          base = null;
        }
        
        int newDoc = getDoc(xctxt, context, ref.toString(), base);
        

        if (-1 != newDoc)
        {

          if (!mnl.contains(newDoc))
          {
            mnl.addElement(newDoc);
          }
        }
        
        if ((null == iterator) || (newDoc == -1))
          break;
      }
    }
    return nodes;
  }
  















  int getDoc(XPathContext xctxt, int context, String uri, String base)
    throws TransformerException
  {
    SourceTreeManager treeMgr = xctxt.getSourceTreeManager();
    
    Source source;
    int newDoc;
    try
    {
      source = treeMgr.resolveURI(base, uri, xctxt.getSAXLocator());
      newDoc = treeMgr.getNode(source);
    }
    catch (IOException ioe)
    {
      throw new TransformerException(ioe.getMessage(), xctxt.getSAXLocator(), ioe);

    }
    catch (TransformerException te)
    {
      throw new TransformerException(te);
    }
    
    if (-1 != newDoc) {
      return newDoc;
    }
    
    if (uri.length() == 0)
    {

      uri = xctxt.getNamespaceContext().getBaseIdentifier();
      try
      {
        source = treeMgr.resolveURI(base, uri, xctxt.getSAXLocator());
      }
      catch (IOException ioe)
      {
        throw new TransformerException(ioe.getMessage(), xctxt.getSAXLocator(), ioe);
      }
    }
    

    String diagnosticsString = null;
    
    try
    {
      if ((null != uri) && (uri.length() > 0))
      {
        newDoc = treeMgr.getSourceTree(source, xctxt.getSAXLocator(), xctxt);

      }
      else
      {
        warn(xctxt, "WG_CANNOT_MAKE_URL_FROM", new Object[] { (base == null ? "" : base) + uri });
      }
      

    }
    catch (Throwable throwable)
    {
      newDoc = -1;
      


      while ((throwable instanceof WrappedRuntimeException))
      {
        throwable = ((WrappedRuntimeException)throwable).getException();
      }
      

      if (((throwable instanceof NullPointerException)) || ((throwable instanceof ClassCastException)))
      {

        throw new WrappedRuntimeException((Exception)throwable);
      }
      

      StringWriter sw = new StringWriter();
      PrintWriter diagnosticsWriter = new PrintWriter(sw);
      
      if ((throwable instanceof TransformerException))
      {
        TransformerException spe = (TransformerException)throwable;
        

        Throwable e = spe;
        
        while (null != e)
        {
          if (null != e.getMessage())
          {
            diagnosticsWriter.println(" (" + e.getClass().getName() + "): " + e.getMessage());
          }
          

          if ((e instanceof TransformerException))
          {
            TransformerException spe2 = (TransformerException)e;
            
            SourceLocator locator = spe2.getLocator();
            if ((null != locator) && (null != locator.getSystemId())) {
              diagnosticsWriter.println("   ID: " + locator.getSystemId() + " Line #" + locator.getLineNumber() + " Column #" + locator.getColumnNumber());
            }
            


            e = spe2.getException();
            
            if ((e instanceof WrappedRuntimeException)) {
              e = ((WrappedRuntimeException)e).getException();
            }
          } else {
            e = null;
          }
        }
      }
      else
      {
        diagnosticsWriter.println(" (" + throwable.getClass().getName() + "): " + throwable.getMessage());
      }
      

      diagnosticsString = throwable.getMessage();
    }
    
    if (-1 == newDoc)
    {


      if (null != diagnosticsString)
      {
        warn(xctxt, "WG_CANNOT_LOAD_REQUESTED_DOC", new Object[] { diagnosticsString });
      }
      else
      {
        warn(xctxt, "WG_CANNOT_LOAD_REQUESTED_DOC", new Object[] { uri == null ? (base == null ? "" : base) + uri : uri.toString() });
      }
    }
    








    return newDoc;
  }
  













  public void error(XPathContext xctxt, String msg, Object[] args)
    throws TransformerException
  {
    String formattedMsg = XSLMessages.createMessage(msg, args);
    ErrorListener errHandler = xctxt.getErrorListener();
    TransformerException spe = new TransformerException(formattedMsg, xctxt.getSAXLocator());
    

    if (null != errHandler) {
      errHandler.error(spe);
    } else {
      System.out.println(formattedMsg);
    }
  }
  











  public void warn(XPathContext xctxt, String msg, Object[] args)
    throws TransformerException
  {
    String formattedMsg = XSLMessages.createWarning(msg, args);
    ErrorListener errHandler = xctxt.getErrorListener();
    TransformerException spe = new TransformerException(formattedMsg, xctxt.getSAXLocator());
    

    if (null != errHandler) {
      errHandler.warning(spe);
    } else {
      System.out.println(formattedMsg);
    }
  }
  






  public void checkNumberArgs(int argNum)
    throws WrongNumberArgsException
  {
    if ((argNum < 1) || (argNum > 2)) {
      reportWrongNumberArgs();
    }
  }
  



  protected void reportWrongNumberArgs()
    throws WrongNumberArgsException
  {
    throw new WrongNumberArgsException(XSLMessages.createMessage("ER_ONE_OR_TWO", null));
  }
  




  public boolean isNodesetExpr()
  {
    return true;
  }
}
