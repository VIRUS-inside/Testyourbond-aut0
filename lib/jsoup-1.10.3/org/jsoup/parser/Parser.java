package org.jsoup.parser;

import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;






public class Parser
{
  private static final int DEFAULT_MAX_ERRORS = 0;
  private TreeBuilder treeBuilder;
  private int maxErrors = 0;
  
  private ParseErrorList errors;
  
  private ParseSettings settings;
  

  public Parser(TreeBuilder treeBuilder)
  {
    this.treeBuilder = treeBuilder;
    settings = treeBuilder.defaultSettings();
  }
  
  public Document parseInput(String html, String baseUri) {
    errors = (isTrackErrors() ? ParseErrorList.tracking(maxErrors) : ParseErrorList.noTracking());
    return treeBuilder.parse(html, baseUri, errors, settings);
  }
  




  public TreeBuilder getTreeBuilder()
  {
    return treeBuilder;
  }
  




  public Parser setTreeBuilder(TreeBuilder treeBuilder)
  {
    this.treeBuilder = treeBuilder;
    return this;
  }
  



  public boolean isTrackErrors()
  {
    return maxErrors > 0;
  }
  




  public Parser setTrackErrors(int maxErrors)
  {
    this.maxErrors = maxErrors;
    return this;
  }
  



  public List<ParseError> getErrors()
  {
    return errors;
  }
  
  public Parser settings(ParseSettings settings) {
    this.settings = settings;
    return this;
  }
  
  public ParseSettings settings() {
    return settings;
  }
  








  public static Document parse(String html, String baseUri)
  {
    TreeBuilder treeBuilder = new HtmlTreeBuilder();
    return treeBuilder.parse(html, baseUri, ParseErrorList.noTracking(), treeBuilder.defaultSettings());
  }
  









  public static List<Node> parseFragment(String fragmentHtml, Element context, String baseUri)
  {
    HtmlTreeBuilder treeBuilder = new HtmlTreeBuilder();
    return treeBuilder.parseFragment(fragmentHtml, context, baseUri, ParseErrorList.noTracking(), treeBuilder.defaultSettings());
  }
  










  public static List<Node> parseFragment(String fragmentHtml, Element context, String baseUri, ParseErrorList errorList)
  {
    HtmlTreeBuilder treeBuilder = new HtmlTreeBuilder();
    return treeBuilder.parseFragment(fragmentHtml, context, baseUri, errorList, treeBuilder.defaultSettings());
  }
  






  public static List<Node> parseXmlFragment(String fragmentXml, String baseUri)
  {
    XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
    return treeBuilder.parseFragment(fragmentXml, baseUri, ParseErrorList.noTracking(), treeBuilder.defaultSettings());
  }
  







  public static Document parseBodyFragment(String bodyHtml, String baseUri)
  {
    Document doc = Document.createShell(baseUri);
    Element body = doc.body();
    List<Node> nodeList = parseFragment(bodyHtml, body, baseUri);
    Node[] nodes = (Node[])nodeList.toArray(new Node[nodeList.size()]);
    for (int i = nodes.length - 1; i > 0; i--) {
      nodes[i].remove();
    }
    for (Node node : nodes) {
      body.appendChild(node);
    }
    return doc;
  }
  





  public static String unescapeEntities(String string, boolean inAttribute)
  {
    Tokeniser tokeniser = new Tokeniser(new CharacterReader(string), ParseErrorList.noTracking());
    return tokeniser.unescapeEntities(inAttribute);
  }
  



  /**
   * @deprecated
   */
  public static Document parseBodyFragmentRelaxed(String bodyHtml, String baseUri)
  {
    return parse(bodyHtml, baseUri);
  }
  






  public static Parser htmlParser()
  {
    return new Parser(new HtmlTreeBuilder());
  }
  




  public static Parser xmlParser()
  {
    return new Parser(new XmlTreeBuilder());
  }
}
