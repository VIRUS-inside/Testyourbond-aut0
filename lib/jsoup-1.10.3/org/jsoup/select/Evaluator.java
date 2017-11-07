package org.jsoup.select;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.helper.Validate;
import org.jsoup.internal.Normalizer;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Tag;












public abstract class Evaluator
{
  protected Evaluator() {}
  
  public abstract boolean matches(Element paramElement1, Element paramElement2);
  
  public static final class Tag
    extends Evaluator
  {
    private String tagName;
    
    public Tag(String tagName)
    {
      this.tagName = tagName;
    }
    
    public boolean matches(Element root, Element element)
    {
      return element.tagName().equalsIgnoreCase(tagName);
    }
    
    public String toString()
    {
      return String.format("%s", new Object[] { tagName });
    }
  }
  

  public static final class TagEndsWith
    extends Evaluator
  {
    private String tagName;
    
    public TagEndsWith(String tagName)
    {
      this.tagName = tagName;
    }
    
    public boolean matches(Element root, Element element)
    {
      return element.tagName().endsWith(tagName);
    }
    
    public String toString()
    {
      return String.format("%s", new Object[] { tagName });
    }
  }
  
  public static final class Id
    extends Evaluator
  {
    private String id;
    
    public Id(String id)
    {
      this.id = id;
    }
    
    public boolean matches(Element root, Element element)
    {
      return id.equals(element.id());
    }
    
    public String toString()
    {
      return String.format("#%s", new Object[] { id });
    }
  }
  

  public static final class Class
    extends Evaluator
  {
    private String className;
    
    public Class(String className)
    {
      this.className = className;
    }
    
    public boolean matches(Element root, Element element)
    {
      return element.hasClass(className);
    }
    
    public String toString()
    {
      return String.format(".%s", new Object[] { className });
    }
  }
  

  public static final class Attribute
    extends Evaluator
  {
    private String key;
    
    public Attribute(String key)
    {
      this.key = key;
    }
    
    public boolean matches(Element root, Element element)
    {
      return element.hasAttr(key);
    }
    
    public String toString()
    {
      return String.format("[%s]", new Object[] { key });
    }
  }
  

  public static final class AttributeStarting
    extends Evaluator
  {
    private String keyPrefix;
    
    public AttributeStarting(String keyPrefix)
    {
      Validate.notEmpty(keyPrefix);
      this.keyPrefix = Normalizer.lowerCase(keyPrefix);
    }
    
    public boolean matches(Element root, Element element)
    {
      List<Attribute> values = element.attributes().asList();
      for (Attribute attribute : values) {
        if (Normalizer.lowerCase(attribute.getKey()).startsWith(keyPrefix))
          return true;
      }
      return false;
    }
    
    public String toString()
    {
      return String.format("[^%s]", new Object[] { keyPrefix });
    }
  }
  

  public static final class AttributeWithValue
    extends Evaluator.AttributeKeyPair
  {
    public AttributeWithValue(String key, String value)
    {
      super(value);
    }
    
    public boolean matches(Element root, Element element)
    {
      return (element.hasAttr(key)) && (value.equalsIgnoreCase(element.attr(key).trim()));
    }
    
    public String toString()
    {
      return String.format("[%s=%s]", new Object[] { key, value });
    }
  }
  

  public static final class AttributeWithValueNot
    extends Evaluator.AttributeKeyPair
  {
    public AttributeWithValueNot(String key, String value)
    {
      super(value);
    }
    
    public boolean matches(Element root, Element element)
    {
      return !value.equalsIgnoreCase(element.attr(key));
    }
    
    public String toString()
    {
      return String.format("[%s!=%s]", new Object[] { key, value });
    }
  }
  

  public static final class AttributeWithValueStarting
    extends Evaluator.AttributeKeyPair
  {
    public AttributeWithValueStarting(String key, String value)
    {
      super(value);
    }
    
    public boolean matches(Element root, Element element)
    {
      return (element.hasAttr(key)) && (Normalizer.lowerCase(element.attr(key)).startsWith(value));
    }
    
    public String toString()
    {
      return String.format("[%s^=%s]", new Object[] { key, value });
    }
  }
  

  public static final class AttributeWithValueEnding
    extends Evaluator.AttributeKeyPair
  {
    public AttributeWithValueEnding(String key, String value)
    {
      super(value);
    }
    
    public boolean matches(Element root, Element element)
    {
      return (element.hasAttr(key)) && (Normalizer.lowerCase(element.attr(key)).endsWith(value));
    }
    
    public String toString()
    {
      return String.format("[%s$=%s]", new Object[] { key, value });
    }
  }
  

  public static final class AttributeWithValueContaining
    extends Evaluator.AttributeKeyPair
  {
    public AttributeWithValueContaining(String key, String value)
    {
      super(value);
    }
    
    public boolean matches(Element root, Element element)
    {
      return (element.hasAttr(key)) && (Normalizer.lowerCase(element.attr(key)).contains(value));
    }
    
    public String toString()
    {
      return String.format("[%s*=%s]", new Object[] { key, value });
    }
  }
  

  public static final class AttributeWithValueMatching
    extends Evaluator
  {
    String key;
    Pattern pattern;
    
    public AttributeWithValueMatching(String key, Pattern pattern)
    {
      this.key = Normalizer.normalize(key);
      this.pattern = pattern;
    }
    
    public boolean matches(Element root, Element element)
    {
      return (element.hasAttr(key)) && (pattern.matcher(element.attr(key)).find());
    }
    
    public String toString()
    {
      return String.format("[%s~=%s]", new Object[] { key, pattern.toString() });
    }
  }
  

  public static abstract class AttributeKeyPair
    extends Evaluator
  {
    String key;
    String value;
    
    public AttributeKeyPair(String key, String value)
    {
      Validate.notEmpty(key);
      Validate.notEmpty(value);
      
      this.key = Normalizer.normalize(key);
      if (((value.startsWith("\"")) && (value.endsWith("\""))) || (
        (value.startsWith("'")) && (value.endsWith("'")))) {
        value = value.substring(1, value.length() - 1);
      }
      this.value = Normalizer.normalize(value);
    }
  }
  
  public static final class AllElements
    extends Evaluator
  {
    public AllElements() {}
    
    public boolean matches(Element root, Element element)
    {
      return true;
    }
    
    public String toString()
    {
      return "*";
    }
  }
  
  public static final class IndexLessThan
    extends Evaluator.IndexEvaluator
  {
    public IndexLessThan(int index)
    {
      super();
    }
    
    public boolean matches(Element root, Element element)
    {
      return element.elementSiblingIndex() < index;
    }
    
    public String toString()
    {
      return String.format(":lt(%d)", new Object[] { Integer.valueOf(index) });
    }
  }
  

  public static final class IndexGreaterThan
    extends Evaluator.IndexEvaluator
  {
    public IndexGreaterThan(int index)
    {
      super();
    }
    
    public boolean matches(Element root, Element element)
    {
      return element.elementSiblingIndex() > index;
    }
    
    public String toString()
    {
      return String.format(":gt(%d)", new Object[] { Integer.valueOf(index) });
    }
  }
  

  public static final class IndexEquals
    extends Evaluator.IndexEvaluator
  {
    public IndexEquals(int index)
    {
      super();
    }
    
    public boolean matches(Element root, Element element)
    {
      return element.elementSiblingIndex() == index;
    }
    
    public String toString()
    {
      return String.format(":eq(%d)", new Object[] { Integer.valueOf(index) });
    }
  }
  
  public static final class IsLastChild
    extends Evaluator
  {
    public IsLastChild() {}
    
    public boolean matches(Element root, Element element)
    {
      Element p = element.parent();
      return (p != null) && (!(p instanceof Document)) && (element.elementSiblingIndex() == p.children().size() - 1);
    }
    
    public String toString()
    {
      return ":last-child";
    }
  }
  
  public static final class IsFirstOfType extends Evaluator.IsNthOfType {
    public IsFirstOfType() {
      super(1);
    }
    
    public String toString() {
      return ":first-of-type";
    }
  }
  
  public static final class IsLastOfType extends Evaluator.IsNthLastOfType {
    public IsLastOfType() {
      super(1);
    }
    
    public String toString() {
      return ":last-of-type";
    }
  }
  
  public static abstract class CssNthEvaluator extends Evaluator {
    protected final int a;
    protected final int b;
    
    public CssNthEvaluator(int a, int b) {
      this.a = a;
      this.b = b;
    }
    
    public CssNthEvaluator(int b) { this(0, b); }
    

    public boolean matches(Element root, Element element)
    {
      Element p = element.parent();
      if ((p == null) || ((p instanceof Document))) { return false;
      }
      int pos = calculatePosition(root, element);
      if (a == 0) { return pos == b;
      }
      return ((pos - b) * a >= 0) && ((pos - b) % a == 0);
    }
    
    public String toString()
    {
      if (a == 0)
        return String.format(":%s(%d)", new Object[] { getPseudoClass(), Integer.valueOf(b) });
      if (b == 0)
        return String.format(":%s(%dn)", new Object[] { getPseudoClass(), Integer.valueOf(a) });
      return String.format(":%s(%dn%+d)", new Object[] { getPseudoClass(), Integer.valueOf(a), Integer.valueOf(b) });
    }
    

    protected abstract String getPseudoClass();
    

    protected abstract int calculatePosition(Element paramElement1, Element paramElement2);
  }
  

  public static final class IsNthChild
    extends Evaluator.CssNthEvaluator
  {
    public IsNthChild(int a, int b)
    {
      super(b);
    }
    
    protected int calculatePosition(Element root, Element element) {
      return element.elementSiblingIndex() + 1;
    }
    
    protected String getPseudoClass()
    {
      return "nth-child";
    }
  }
  


  public static final class IsNthLastChild
    extends Evaluator.CssNthEvaluator
  {
    public IsNthLastChild(int a, int b)
    {
      super(b);
    }
    
    protected int calculatePosition(Element root, Element element)
    {
      return element.parent().children().size() - element.elementSiblingIndex();
    }
    
    protected String getPseudoClass()
    {
      return "nth-last-child";
    }
  }
  

  public static class IsNthOfType
    extends Evaluator.CssNthEvaluator
  {
    public IsNthOfType(int a, int b)
    {
      super(b);
    }
    
    protected int calculatePosition(Element root, Element element) {
      int pos = 0;
      Elements family = element.parent().children();
      for (Element el : family) {
        if (el.tag().equals(element.tag())) pos++;
        if (el == element) break;
      }
      return pos;
    }
    
    protected String getPseudoClass()
    {
      return "nth-of-type";
    }
  }
  
  public static class IsNthLastOfType extends Evaluator.CssNthEvaluator
  {
    public IsNthLastOfType(int a, int b) {
      super(b);
    }
    
    protected int calculatePosition(Element root, Element element)
    {
      int pos = 0;
      Elements family = element.parent().children();
      for (int i = element.elementSiblingIndex(); i < family.size(); i++) {
        if (((Element)family.get(i)).tag().equals(element.tag())) pos++;
      }
      return pos;
    }
    
    protected String getPseudoClass()
    {
      return "nth-last-of-type";
    }
  }
  
  public static final class IsFirstChild extends Evaluator
  {
    public IsFirstChild() {}
    
    public boolean matches(Element root, Element element)
    {
      Element p = element.parent();
      return (p != null) && (!(p instanceof Document)) && (element.elementSiblingIndex() == 0);
    }
    
    public String toString()
    {
      return ":first-child";
    }
  }
  

  public static final class IsRoot
    extends Evaluator
  {
    public IsRoot() {}
    
    public boolean matches(Element root, Element element)
    {
      Element r = (root instanceof Document) ? root.child(0) : root;
      return element == r;
    }
    

    public String toString() { return ":root"; }
  }
  
  public static final class IsOnlyChild extends Evaluator {
    public IsOnlyChild() {}
    
    public boolean matches(Element root, Element element) {
      Element p = element.parent();
      return (p != null) && (!(p instanceof Document)) && (element.siblingElements().size() == 0);
    }
    

    public String toString() { return ":only-child"; }
  }
  
  public static final class IsOnlyOfType extends Evaluator {
    public IsOnlyOfType() {}
    
    public boolean matches(Element root, Element element) {
      Element p = element.parent();
      if ((p == null) || ((p instanceof Document))) { return false;
      }
      int pos = 0;
      Elements family = p.children();
      for (Element el : family) {
        if (el.tag().equals(element.tag())) pos++;
      }
      return pos == 1;
    }
    

    public String toString() { return ":only-of-type"; }
  }
  
  public static final class IsEmpty extends Evaluator {
    public IsEmpty() {}
    
    public boolean matches(Element root, Element element) {
      List<Node> family = element.childNodes();
      for (Iterator localIterator = family.iterator(); localIterator.hasNext(); 
          return false)
      {
        Node n = (Node)localIterator.next();
        if (((n instanceof Comment)) || ((n instanceof XmlDeclaration)) || ((n instanceof DocumentType))) {}
      }
      return true;
    }
    
    public String toString() {
      return ":empty";
    }
  }
  

  public static abstract class IndexEvaluator
    extends Evaluator
  {
    int index;
    

    public IndexEvaluator(int index)
    {
      this.index = index;
    }
  }
  
  public static final class ContainsText
    extends Evaluator
  {
    private String searchText;
    
    public ContainsText(String searchText)
    {
      this.searchText = Normalizer.lowerCase(searchText);
    }
    
    public boolean matches(Element root, Element element)
    {
      return Normalizer.lowerCase(element.text()).contains(searchText);
    }
    
    public String toString()
    {
      return String.format(":contains(%s)", new Object[] { searchText });
    }
  }
  
  public static final class ContainsData
    extends Evaluator
  {
    private String searchText;
    
    public ContainsData(String searchText)
    {
      this.searchText = Normalizer.lowerCase(searchText);
    }
    
    public boolean matches(Element root, Element element)
    {
      return Normalizer.lowerCase(element.data()).contains(searchText);
    }
    
    public String toString()
    {
      return String.format(":containsData(%s)", new Object[] { searchText });
    }
  }
  
  public static final class ContainsOwnText
    extends Evaluator
  {
    private String searchText;
    
    public ContainsOwnText(String searchText)
    {
      this.searchText = Normalizer.lowerCase(searchText);
    }
    
    public boolean matches(Element root, Element element)
    {
      return Normalizer.lowerCase(element.ownText()).contains(searchText);
    }
    
    public String toString()
    {
      return String.format(":containsOwn(%s)", new Object[] { searchText });
    }
  }
  
  public static final class Matches
    extends Evaluator
  {
    private Pattern pattern;
    
    public Matches(Pattern pattern)
    {
      this.pattern = pattern;
    }
    
    public boolean matches(Element root, Element element)
    {
      Matcher m = pattern.matcher(element.text());
      return m.find();
    }
    
    public String toString()
    {
      return String.format(":matches(%s)", new Object[] { pattern });
    }
  }
  
  public static final class MatchesOwn
    extends Evaluator
  {
    private Pattern pattern;
    
    public MatchesOwn(Pattern pattern)
    {
      this.pattern = pattern;
    }
    
    public boolean matches(Element root, Element element)
    {
      Matcher m = pattern.matcher(element.ownText());
      return m.find();
    }
    
    public String toString()
    {
      return String.format(":matchesOwn(%s)", new Object[] { pattern });
    }
  }
}
