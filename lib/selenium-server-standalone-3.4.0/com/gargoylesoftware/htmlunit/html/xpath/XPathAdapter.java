package com.gargoylesoftware.htmlunit.html.xpath;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xml.utils.DefaultErrorHandler;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionNode;
import org.apache.xpath.XPathContext;
import org.apache.xpath.compiler.Compiler;
import org.apache.xpath.compiler.FunctionTable;
import org.apache.xpath.compiler.XPathParser;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.res.XPATHMessages;






















class XPathAdapter
{
  private static final Pattern PREPROCESS_XPATH_PATTERN = Pattern.compile("(@[a-zA-Z]+)");
  
  private Expression mainExp_;
  
  private FunctionTable funcTable_;
  

  private void initFunctionTable()
  {
    funcTable_ = new FunctionTable();
    funcTable_.installFunction("lower-case", LowerCaseFunction.class);
  }
  











  XPathAdapter(String exprString, SourceLocator locator, PrefixResolver prefixResolver, ErrorListener errorListener, boolean caseSensitive, boolean attributeCaseSensitive)
    throws TransformerException
  {
    initFunctionTable();
    
    if (errorListener == null) {
      errorListener = new DefaultErrorHandler();
    }
    
    exprString = preProcessXPath(exprString, caseSensitive, attributeCaseSensitive);
    
    XPathParser parser = new XPathParser(errorListener, locator);
    Compiler compiler = new Compiler(errorListener, locator, funcTable_);
    
    parser.initXPath(compiler, exprString, prefixResolver);
    
    Expression expr = compiler.compile(0);
    
    mainExp_ = expr;
    
    if ((locator != null) && ((locator instanceof ExpressionNode))) {
      expr.exprSetParent((ExpressionNode)locator);
    }
  }
  









  private static String preProcessXPath(String xpath, boolean caseSensitive, boolean attributeCaseSensitive)
  {
    if (!caseSensitive) {
      char[] charArray = xpath.toCharArray();
      processOutsideBrackets(charArray);
      xpath = new String(charArray);
    }
    
    if (!attributeCaseSensitive) {
      Matcher matcher = PREPROCESS_XPATH_PATTERN.matcher(xpath);
      while (matcher.find()) {
        String attribute = matcher.group(1);
        xpath = xpath.replace(attribute, attribute.toLowerCase(Locale.ROOT));
      }
    }
    
    return xpath;
  }
  



  private static void processOutsideBrackets(char[] array)
  {
    int length = array.length;
    int insideBrackets = 0;
    for (int i = 0; i < length; i++) {
      char ch = array[i];
      switch (ch) {
      case '(': 
      case '[': 
        insideBrackets++;
        break;
      
      case ')': 
      case ']': 
        insideBrackets--;
        break;
      
      default: 
        if (insideBrackets == 0) {
          array[i] = Character.toLowerCase(ch);
        }
        


        break;
      }
      
    }
  }
  



  XObject execute(XPathContext xpathContext, int contextNode, PrefixResolver namespaceContext)
    throws TransformerException
  {
    xpathContext.pushNamespaceContext(namespaceContext);
    
    xpathContext.pushCurrentNodeAndExpression(contextNode, contextNode);
    
    XObject xobj = null;
    try
    {
      xobj = mainExp_.execute(xpathContext);
    }
    catch (TransformerException te) {
      te.setLocator(mainExp_);
      ErrorListener el = xpathContext.getErrorListener();
      if (el != null) {
        el.error(te);
      }
      else {
        throw te;
      }
    }
    catch (Exception e) {
      while ((e instanceof WrappedRuntimeException)) {
        e = ((WrappedRuntimeException)e).getException();
      }
      String msg = e.getMessage();
      
      if ((msg == null) || (msg.isEmpty())) {
        msg = XPATHMessages.createXPATHMessage("ER_XPATH_ERROR", null);
      }
      TransformerException te = new TransformerException(msg, mainExp_, e);
      ErrorListener el = xpathContext.getErrorListener();
      if (el != null) {
        el.fatalError(te);
      }
      else {
        throw te;
      }
    }
    finally {
      xpathContext.popNamespaceContext();
      xpathContext.popCurrentNodeAndExpression();
    }
    
    return xobj;
  }
}
