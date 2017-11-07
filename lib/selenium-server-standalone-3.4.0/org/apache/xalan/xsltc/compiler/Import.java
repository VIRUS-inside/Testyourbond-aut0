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


























final class Import
  extends TopLevelElement
{
  Import() {}
  
  private Stylesheet _imported = null;
  
  public Stylesheet getImportedStylesheet() {
    return _imported;
  }
  
  public void parseContents(Parser parser) {
    XSLTC xsltc = parser.getXSLTC();
    Stylesheet context = parser.getCurrentStylesheet();
    try
    {
      String docToLoad = getAttribute("href");
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
          _imported = parser.makeStylesheet(root);
          if (_imported == null)
            return;
          _imported.setSourceLoader(loader);
          _imported.setSystemId(docToLoad);
          _imported.setParentStylesheet(context);
          _imported.setImportingStylesheet(context);
          _imported.setTemplateInlining(context.getTemplateInlining());
          

          int currPrecedence = parser.getCurrentImportPrecedence();
          int nextPrecedence = parser.getNextImportPrecedence();
          _imported.setImportPrecedence(currPrecedence);
          context.setImportPrecedence(nextPrecedence);
          parser.setCurrentStylesheet(_imported);
          _imported.parseContents(parser);
          
          Enumeration elements = _imported.elements();
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
