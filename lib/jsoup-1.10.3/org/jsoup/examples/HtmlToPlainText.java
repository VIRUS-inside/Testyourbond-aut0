package org.jsoup.examples;

import java.io.IOException;
import java.io.PrintStream;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;









public class HtmlToPlainText
{
  private static final String userAgent = "Mozilla/5.0 (jsoup)";
  private static final int timeout = 5000;
  
  public HtmlToPlainText() {}
  
  public static void main(String... args)
    throws IOException
  {
    Validate.isTrue((args.length == 1) || (args.length == 2), "usage: java -cp jsoup.jar org.jsoup.examples.HtmlToPlainText url [selector]");
    String url = args[0];
    String selector = args.length == 2 ? args[1] : null;
    

    Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (jsoup)").timeout(5000).get();
    
    HtmlToPlainText formatter = new HtmlToPlainText();
    
    if (selector != null) {
      Elements elements = doc.select(selector);
      for (Element element : elements) {
        String plainText = formatter.getPlainText(element);
        System.out.println(plainText);
      }
    } else {
      String plainText = formatter.getPlainText(doc);
      System.out.println(plainText);
    }
  }
  




  public String getPlainText(Element element)
  {
    FormattingVisitor formatter = new FormattingVisitor(null);
    NodeTraversor traversor = new NodeTraversor(formatter);
    traversor.traverse(element);
    
    return formatter.toString();
  }
  
  private class FormattingVisitor implements NodeVisitor
  {
    private static final int maxWidth = 80;
    private int width = 0;
    private StringBuilder accum = new StringBuilder();
    
    private FormattingVisitor() {}
    
    public void head(Node node, int depth) { String name = node.nodeName();
      if ((node instanceof TextNode)) {
        append(((TextNode)node).text());
      } else if (name.equals("li")) {
        append("\n * ");
      } else if (name.equals("dt")) {
        append("  ");
      } else if (StringUtil.in(name, new String[] { "p", "h1", "h2", "h3", "h4", "h5", "tr" })) {
        append("\n");
      }
    }
    
    public void tail(Node node, int depth) {
      String name = node.nodeName();
      if (StringUtil.in(name, new String[] { "br", "dd", "dt", "p", "h1", "h2", "h3", "h4", "h5" })) {
        append("\n");
      } else if (name.equals("a")) {
        append(String.format(" <%s>", new Object[] { node.absUrl("href") }));
      }
    }
    
    private void append(String text) {
      if (text.startsWith("\n"))
        width = 0;
      if (text.equals(" ")) {
        if (accum.length() != 0) { if (!StringUtil.in(accum.substring(accum.length() - 1), new String[] { " ", "\n" })) {}
        } else return;
      }
      if (text.length() + width > 80) {
        String[] words = text.split("\\s+");
        for (int i = 0; i < words.length; i++) {
          String word = words[i];
          boolean last = i == words.length - 1;
          if (!last)
            word = word + " ";
          if (word.length() + width > 80) {
            accum.append("\n").append(word);
            width = word.length();
          } else {
            accum.append(word);
            width += word.length();
          }
        }
      } else {
        accum.append(text);
        width += text.length();
      }
    }
    
    public String toString()
    {
      return accum.toString();
    }
  }
}
