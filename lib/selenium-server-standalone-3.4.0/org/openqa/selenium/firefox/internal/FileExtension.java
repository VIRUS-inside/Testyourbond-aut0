package org.openqa.selenium.firefox.internal;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.io.TemporaryFilesystem;
import org.openqa.selenium.io.Zip;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

















public class FileExtension
  implements Extension
{
  private static final String EM_NAMESPACE_URI = "http://www.mozilla.org/2004/em-rdf#";
  private final File toInstall;
  
  public FileExtension(File toInstall)
  {
    this.toInstall = toInstall;
  }
  
  public void writeTo(File extensionsDir) throws IOException {
    if ((!toInstall.isDirectory()) && 
      (!FileHandler.isZipped(toInstall.getAbsolutePath())))
    {
      throw new IOException(String.format("Can only install from a zip file, an XPI or a directory: %s", new Object[] {toInstall
        .getAbsolutePath() }));
    }
    
    File root = obtainRootDirectory(toInstall);
    
    String id = readIdFromInstallRdf(root);
    
    File extensionDirectory = new File(extensionsDir, id);
    
    if ((extensionDirectory.exists()) && (!FileHandler.delete(extensionDirectory))) {
      throw new IOException("Unable to delete existing extension directory: " + extensionDirectory);
    }
    

    FileHandler.createDir(extensionDirectory);
    FileHandler.makeWritable(extensionDirectory);
    FileHandler.copy(root, extensionDirectory);
    TemporaryFilesystem.getDefaultTmpFS().deleteTempDir(root);
  }
  
  private File obtainRootDirectory(File extensionToInstall) throws IOException {
    File root = extensionToInstall;
    if (!extensionToInstall.isDirectory()) {
      BufferedInputStream bis = new BufferedInputStream(new FileInputStream(extensionToInstall));
      try
      {
        root = Zip.unzipToTempDir(bis, "unzip", "stream");
      } finally {
        bis.close();
      }
    }
    return root;
  }
  
  private String readIdFromInstallRdf(File root)
  {
    try {
      File installRdf = new File(root, "install.rdf");
      
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document doc = builder.parse(installRdf);
      
      XPath xpath = XPathFactory.newInstance().newXPath();
      xpath.setNamespaceContext(new NamespaceContext() {
        public String getNamespaceURI(String prefix) {
          if ("em".equals(prefix))
            return "http://www.mozilla.org/2004/em-rdf#";
          if ("RDF".equals(prefix)) {
            return "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
          }
          
          return "";
        }
        
        public String getPrefix(String uri) {
          throw new UnsupportedOperationException("getPrefix");
        }
        
        public Iterator<?> getPrefixes(String uri) {
          throw new UnsupportedOperationException("getPrefixes");
        }
        
      });
      Node idNode = (Node)xpath.compile("//em:id").evaluate(doc, XPathConstants.NODE);
      
      String id = null;
      if (idNode == null)
      {
        Node descriptionNode = (Node)xpath.compile("//RDF:Description").evaluate(doc, XPathConstants.NODE);
        Node idAttr = descriptionNode.getAttributes().getNamedItemNS("http://www.mozilla.org/2004/em-rdf#", "id");
        if (idAttr == null)
        {
          throw new WebDriverException("Cannot locate node containing extension id: " + installRdf.getAbsolutePath());
        }
        id = idAttr.getNodeValue();
      } else {
        id = idNode.getTextContent();
      }
      
      if ((id == null) || ("".equals(id.trim()))) {
        throw new FileNotFoundException("Cannot install extension with ID: " + id);
      }
      return id;
    } catch (Exception e) {
      throw new WebDriverException(e);
    }
  }
}
