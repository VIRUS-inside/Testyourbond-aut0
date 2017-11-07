package org.jsoup.examples;

import java.io.PrintStream;
import org.jsoup.Connection;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ListLinks
{
  public ListLinks() {}
  
  public static void main(String[] args) throws java.io.IOException
  {
    Validate.isTrue(args.length == 1, "usage: supply url to fetch");
    String url = args[0];
    print("Fetching %s...", new Object[] { url });
    
    Document doc = org.jsoup.Jsoup.connect(url).get();
    Elements links = doc.select("a[href]");
    Elements media = doc.select("[src]");
    Elements imports = doc.select("link[href]");
    
    print("\nMedia: (%d)", new Object[] { Integer.valueOf(media.size()) });
    for (Element src : media) {
      if (src.tagName().equals("img")) {
        print(" * %s: <%s> %sx%s (%s)", new Object[] {src
          .tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"), 
          trim(src.attr("alt"), 20) });
      } else {
        print(" * %s: <%s>", new Object[] { src.tagName(), src.attr("abs:src") });
      }
    }
    print("\nImports: (%d)", new Object[] { Integer.valueOf(imports.size()) });
    for (Element link : imports) {
      print(" * %s <%s> (%s)", new Object[] { link.tagName(), link.attr("abs:href"), link.attr("rel") });
    }
    
    print("\nLinks: (%d)", new Object[] { Integer.valueOf(links.size()) });
    for (Element link : links) {
      print(" * a: <%s>  (%s)", new Object[] { link.attr("abs:href"), trim(link.text(), 35) });
    }
  }
  
  private static void print(String msg, Object... args) {
    System.out.println(String.format(msg, args));
  }
  
  private static String trim(String s, int width) {
    if (s.length() > width) {
      return s.substring(0, width - 1) + ".";
    }
    return s;
  }
}
