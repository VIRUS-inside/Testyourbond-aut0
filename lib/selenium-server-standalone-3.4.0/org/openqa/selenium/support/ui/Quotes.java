package org.openqa.selenium.support.ui;


















public class Quotes
{
  public Quotes() {}
  
















  public static String escape(String toEscape)
  {
    if ((toEscape.contains("\"")) && (toEscape.contains("'"))) {
      boolean quoteIsLast = false;
      if (toEscape.lastIndexOf("\"") == toEscape.length() - 1) {
        quoteIsLast = true;
      }
      String[] substringsWithoutQuotes = toEscape.split("\"");
      
      StringBuilder quoted = new StringBuilder("concat(");
      for (int i = 0; i < substringsWithoutQuotes.length; i++) {
        quoted.append("\"").append(substringsWithoutQuotes[i]).append("\"");
        quoted
          .append(i == substringsWithoutQuotes.length - 1 ? ")" : quoteIsLast ? ", '\"')" : ", '\"', ");
      }
      
      return quoted.toString();
    }
    

    if (toEscape.contains("\"")) {
      return String.format("'%s'", new Object[] { toEscape });
    }
    

    return String.format("\"%s\"", new Object[] { toEscape });
  }
}
