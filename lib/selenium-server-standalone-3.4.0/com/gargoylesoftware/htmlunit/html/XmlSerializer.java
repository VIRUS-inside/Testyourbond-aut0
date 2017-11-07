package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.util.MimeType;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;























class XmlSerializer
{
  private static final String FILE_SEPARATOR = "/";
  private static final Pattern CREATE_FILE_PATTERN = Pattern.compile(".*/");
  
  private final StringBuilder builder_ = new StringBuilder();
  private final StringBuilder indent_ = new StringBuilder();
  
  XmlSerializer() {}
  
  public void save(SgmlPage page, File file) throws IOException { String fileName = file.getName();
    if ((!fileName.endsWith(".htm")) && (!fileName.endsWith(".html"))) {
      fileName = fileName + ".html";
    }
    File outputFile = new File(file.getParentFile(), fileName);
    if (outputFile.exists()) {
      throw new IOException("File already exists: " + outputFile);
    }
    fileName = fileName.substring(0, fileName.lastIndexOf('.'));
    outputDir_ = new File(file.getParentFile(), fileName);
    FileUtils.writeStringToFile(outputFile, asXml(page.getDocumentElement()), StandardCharsets.ISO_8859_1);
  }
  



  public String asXml(DomElement node)
    throws IOException
  {
    builder_.setLength(0);
    indent_.setLength(0);
    SgmlPage page = node.getPage();
    if ((page != null) && (page.isHtmlPage())) {
      Charset charsetName = page.getCharset();
      if ((charsetName != null) && ((node instanceof HtmlHtml))) {
        builder_.append("<?xml version=\"1.0\" encoding=\"").append(charsetName).append("\"?>").append('\n');
      }
    }
    printXml(node);
    String response = builder_.toString();
    builder_.setLength(0);
    return response;
  }
  
  protected void printXml(DomElement node) throws IOException {
    if (!isExcluded(node)) {
      boolean hasChildren = node.getFirstChild() != null;
      builder_.append(indent_).append('<');
      printOpeningTag(node);
      
      if ((!hasChildren) && (!node.isEmptyXmlTagExpanded())) {
        builder_.append("/>").append('\n');
      }
      else {
        builder_.append(">").append('\n');
        for (DomNode child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
          indent_.append("  ");
          if ((child instanceof DomElement)) {
            printXml((DomElement)child);
          }
          else {
            builder_.append(child);
          }
          indent_.setLength(indent_.length() - 2);
        }
        builder_.append(indent_).append("</").append(node.getTagName()).append('>').append('\n');
      }
    }
  }
  




  public String asText(DomNode node)
  {
    builder_.setLength(0);
    
    if ((node instanceof DomText)) {
      builder_.append(((DomText)node).getData());
    }
    else {
      printText(node);
    }
    
    String response = builder_.toString();
    builder_.setLength(0);
    return response;
  }
  
  protected void printText(DomNode node) {
    for (DomNode child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
      if ((child instanceof DomText)) {
        builder_.append(((DomText)child).getData());
      }
      else {
        printText(child);
      }
    }
  }
  




  protected void printOpeningTag(DomElement node)
    throws IOException
  {
    builder_.append(node.getTagName());
    Map<String, DomAttr> attributes = readAttributes(node);
    
    for (Map.Entry<String, DomAttr> entry : attributes.entrySet()) {
      builder_.append(" ");
      builder_.append((String)entry.getKey());
      builder_.append("=\"");
      String value = ((DomAttr)entry.getValue()).getNodeValue();
      builder_.append(com.gargoylesoftware.htmlunit.util.StringUtils.escapeXmlAttributeValue(value));
      builder_.append('"');
    }
  }
  
  private Map<String, DomAttr> readAttributes(DomElement node) throws IOException {
    if ((node instanceof HtmlImage)) {
      return getAttributesFor((HtmlImage)node);
    }
    if ((node instanceof HtmlLink)) {
      return getAttributesFor((HtmlLink)node);
    }
    if ((node instanceof BaseFrameElement)) {
      return getAttributesFor((BaseFrameElement)node);
    }
    
    Map<String, DomAttr> attributes = node.getAttributesMap();
    if ((node instanceof HtmlOption)) {
      attributes = new HashMap(attributes);
      HtmlOption option = (HtmlOption)node;
      if (option.isSelected()) {
        if (!attributes.containsKey("selected")) {
          attributes.put("selected", new DomAttr(node.getPage(), null, "selected", "selected", false));
        }
      }
      else {
        attributes.remove("selected");
      }
    }
    return attributes;
  }
  
  private Map<String, DomAttr> getAttributesFor(BaseFrameElement frame) throws IOException {
    Map<String, DomAttr> map = createAttributesCopyWithClonedAttribute(frame, "src");
    DomAttr srcAttr = (DomAttr)map.get("src");
    if (srcAttr == null) {
      return map;
    }
    
    Page enclosedPage = frame.getEnclosedPage();
    String suffix = getFileExtension(enclosedPage);
    File file = createFile(srcAttr.getValue(), "." + suffix);
    
    if (enclosedPage != null) {
      if (enclosedPage.isHtmlPage()) {
        file.delete();
        
        ((HtmlPage)enclosedPage).save(file);
      }
      else {
        Object localObject1 = null;Object localObject4 = null; Object localObject3; label260: try { is = enclosedPage.getWebResponse().getContentAsStream(); } finally { InputStream is;
          Object localObject5;
          Object localObject8;
          FileOutputStream fos;
          Object localObject7; localObject3 = localThrowable1; break label260; if (localObject3 != localThrowable1) localObject3.addSuppressed(localThrowable1);
        }
      }
    }
    srcAttr.setValue(file.getParentFile().getName() + "/" + file.getName());
    return map;
  }
  
  private static String getFileExtension(Page enclosedPage) {
    if (enclosedPage != null) {
      if (enclosedPage.isHtmlPage()) {
        return "html";
      }
      
      URL url = enclosedPage.getUrl();
      if (url.getPath().contains(".")) {
        return org.apache.commons.lang3.StringUtils.substringAfterLast(url.getPath(), ".");
      }
    }
    
    return ".unknown";
  }
  
  protected Map<String, DomAttr> getAttributesFor(HtmlLink link) throws IOException {
    Map<String, DomAttr> map = createAttributesCopyWithClonedAttribute(link, "href");
    DomAttr hrefAttr = (DomAttr)map.get("href");
    if ((hrefAttr != null) && (org.apache.commons.lang3.StringUtils.isNotBlank(hrefAttr.getValue()))) {
      String protocol = link.getWebRequest().getUrl().getProtocol();
      if (("http".equals(protocol)) || ("https".equals(protocol))) {
        File file = createFile(hrefAttr.getValue(), ".css");
        FileUtils.writeStringToFile(file, link.getWebResponse(true).getContentAsString(), StandardCharsets.ISO_8859_1);
        hrefAttr.setValue(outputDir_.getName() + "/" + file.getName());
      }
    }
    
    return map;
  }
  
  protected Map<String, DomAttr> getAttributesFor(HtmlImage image) throws IOException {
    Map<String, DomAttr> map = createAttributesCopyWithClonedAttribute(image, "src");
    DomAttr srcAttr = (DomAttr)map.get("src");
    if ((srcAttr != null) && (org.apache.commons.lang3.StringUtils.isNotBlank(srcAttr.getValue()))) {
      WebResponse response = image.getWebResponse(true);
      
      File file = createFile(srcAttr.getValue(), "." + getSuffix(response));
      FileUtils.copyInputStreamToFile(response.getContentAsStream(), file);
      String valueOnFileSystem = outputDir_.getName() + "/" + file.getName();
      srcAttr.setValue(valueOnFileSystem);
    }
    
    return map;
  }
  
  private static String getSuffix(WebResponse response)
  {
    String url = response.getWebRequest().getUrl().toString();
    String fileName = org.apache.commons.lang3.StringUtils.substringAfterLast(org.apache.commons.lang3.StringUtils.substringBefore(url, "?"), "/");
    
    String suffix = org.apache.commons.lang3.StringUtils.substringAfterLast(fileName, ".");
    if ((suffix.length() > 1) && (suffix.length() < 5)) {
      return suffix;
    }
    

    return MimeType.getFileExtension(response.getContentType());
  }
  
  private static Map<String, DomAttr> createAttributesCopyWithClonedAttribute(HtmlElement elt, String attrName)
  {
    Map<String, DomAttr> newMap = new HashMap(elt.getAttributesMap());
    

    DomAttr attr = (DomAttr)newMap.get(attrName);
    if (attr == null) {
      return newMap;
    }
    
    DomAttr clonedAttr = new DomAttr(attr.getPage(), attr.getNamespaceURI(), 
      attr.getQualifiedName(), attr.getValue(), attr.getSpecified());
    
    newMap.put(attrName, clonedAttr);
    
    return newMap;
  }
  
  protected boolean isExcluded(DomElement element) {
    return element instanceof HtmlScript;
  }
  


  private File outputDir_;
  

  private File createFile(String url, String extension)
    throws IOException
  {
    String name = url.replaceFirst("/$", "");
    name = CREATE_FILE_PATTERN.matcher(name).replaceAll("");
    name = org.apache.commons.lang3.StringUtils.substringBefore(name, "?");
    name = org.apache.commons.lang3.StringUtils.substringBefore(name, ";");
    name = org.apache.commons.lang3.StringUtils.substring(name, 0, 30);
    name = com.gargoylesoftware.htmlunit.util.StringUtils.sanitizeForFileName(name);
    if (!name.endsWith(extension)) {
      name = name + extension;
    }
    int counter = 0;
    for (;;) { String fileName;
      String fileName;
      if (counter != 0) {
        fileName = 
          org.apache.commons.lang3.StringUtils.substringBeforeLast(name, ".") + "_" + counter + "." + org.apache.commons.lang3.StringUtils.substringAfterLast(name, ".");
      }
      else {
        fileName = name;
      }
      outputDir_.mkdirs();
      File f = new File(outputDir_, fileName);
      if (f.createNewFile()) {
        return f;
      }
      counter++;
    }
  }
}
