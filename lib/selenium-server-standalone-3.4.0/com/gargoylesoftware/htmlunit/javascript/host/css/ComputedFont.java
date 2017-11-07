package com.gargoylesoftware.htmlunit.javascript.host.css;






final class ComputedFont
{
  static final int FONT_SIZE_INDEX = 3;
  




  static final int LINE_HEIGHT_INDEX = 4;
  




  static final int FONT_FAMILY_INDEX = 5;
  





  static String[] getDetails(String font, boolean handleSpaceAfterSlash)
  {
    while (font.contains("  ")) {
      font = font.replace("  ", " ");
    }
    if ((!handleSpaceAfterSlash) && (font.contains("/ "))) {
      return null;
    }
    String[] tokens = font.split(" ");
    if (tokens.length > 1) {
      String[] fontSizeDetails = getFontSizeDetails(tokens[(tokens.length - 2)]);
      if (fontSizeDetails == null) {
        return null;
      }
      String[] details = new String[6];
      details[3] = fontSizeDetails[0];
      details[4] = fontSizeDetails[1];
      details[5] = tokens[(tokens.length - 1)];
      return details;
    }
    return null;
  }
  


  private static String[] getFontSizeDetails(String fontSize)
  {
    int slash = fontSize.indexOf('/');
    String actualFontSize = slash == -1 ? fontSize : fontSize.substring(0, slash);
    String actualLineHeight = slash == -1 ? "" : fontSize.substring(slash + 1);
    if (!CSSStyleDeclaration.isLength(actualFontSize)) {
      return null;
    }
    if (actualLineHeight.isEmpty()) {
      actualLineHeight = null;
    }
    else if (!isValidLineHeight(actualLineHeight)) {
      return null;
    }
    return new String[] { actualFontSize, actualLineHeight };
  }
  
  private static boolean isValidLineHeight(String lineHeight) {
    return (CSSStyleDeclaration.isLength(lineHeight)) || ("normal".equals(lineHeight));
  }
  
  private ComputedFont() {}
}
