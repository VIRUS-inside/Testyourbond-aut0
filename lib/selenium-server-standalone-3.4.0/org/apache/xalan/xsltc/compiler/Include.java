package org.apache.xalan.xsltc.compiler;

import java.util.Enumeration;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xml.utils.SystemIDResolver;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;


























final class Include
  extends TopLevelElement
{
  Include() {}
  
  private Stylesheet _included = null;
  
  public Stylesheet getIncludedStylesheet() {
    return _included;
  }
  
  public void parseContents(Parser parser) {
    XSLTC xsltc = parser.getXSLTC();
    Stylesheet context = parser.getCurrentStylesheet();
    
    String docToLoad = getAttribute("href");
    try {
      if (context.checkForLoop(docToLoad)) {
        ErrorMsg msg = new ErrorMsg("CIRCULAR_INCLUDE_ERR", docToLoad, this);
        
        parser.reportError(2, msg);
      }
      else
      {
        InputSource input = null;
        XMLReader reader = null;
        String currLoadedDoc = context.getSystemId();
        SourceLoader loader = context.getSourceLoader();
        

        if (loader != null) {
          input = loader.loadSource(docToLoad, currLoadedDoc, xsltc);
          if (input != null) {
            docToLoad = input.getSystemId();
            reader = xsltc.getXMLReader();
          }
        }
        

        if (input == null) {
          docToLoad = SystemIDResolver.getAbsoluteURI(docToLoad, currLoadedDoc);
          input = new InputSource(docToLoad);
        }
        

        if (input == null) {
          ErrorMsg msg = new ErrorMsg("FILE_NOT_FOUND_ERR", docToLoad, this);
          
          parser.reportError(2, msg);
        }
        else {
          SyntaxTreeNode root;
          SyntaxTreeNode root;
          if (reader != null) {
            root = parser.parse(reader, input);
          }
          else {
            root = parser.parse(input);
          }
          
          if (root == null) return;
          _included = parser.makeStylesheet(root);
          if (_included == null)
            return;
          _included.setSourceLoader(loader);
          _included.setSystemId(docToLoad);
          _included.setParentStylesheet(context);
          _included.setIncludingStylesheet(context);
          _included.setTemplateInlining(context.getTemplateInlining());
          


          int precedence = context.getImportPrecedence();
          _included.setImportPrecedence(precedence);
          parser.setCurrentStylesheet(_included);
          _included.parseContents(parser);
          
          Enumeration elements = _included.elements();
          Stylesheet topStylesheet = parser.getTopLevelStylesheet();
          while (elements.hasMoreElements()) {
            Object element = elements.nextElement();
            if ((element instanceof TopLevelElement)) {
              if ((element instanceof Variable)) {
                topStylesheet.addVariable((Variable)element);
              }
              else if ((element instanceof Param)) {
                topStylesheet.addParam((Param)element);
              }
              else
                topStylesheet.addElement((TopLevelElement)element);
            }
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    finally {
      parser.setCurrentStylesheet(context);
    }
  }
  
  public Type typeCheck(SymbolTable stable) throws TypeCheckError {
    return Type.Void;
  }
  
  public void translate(ClassGenerator classGen, MethodGenerator methodGen) {}
}
