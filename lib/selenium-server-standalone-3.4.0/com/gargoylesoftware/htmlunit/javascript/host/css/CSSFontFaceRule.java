package com.gargoylesoftware.htmlunit.javascript.host.css;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;




























@JsxClass
public class CSSFontFaceRule
  extends CSSRule
{
  private static final Pattern REPLACEMENT_1 = Pattern.compile("font-family: ([^;]*);");
  private static final Pattern REPLACEMENT_2 = Pattern.compile("src: url\\(([^;]*)\\);");
  




  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public CSSFontFaceRule() {}
  




  protected CSSFontFaceRule(CSSStyleSheet stylesheet, org.w3c.dom.css.CSSFontFaceRule rule)
  {
    super(stylesheet, rule);
  }
  



  public short getType()
  {
    return 5;
  }
  



  public String getCssText()
  {
    String cssText = super.getCssText();
    BrowserVersion browserVersion = getBrowserVersion();
    if (browserVersion.hasFeature(BrowserVersionFeatures.CSS_FONTFACERULE_CSSTEXT_CRLF)) {
      cssText = StringUtils.replace(cssText, "{", "{\r\n\t");
      cssText = StringUtils.replace(cssText, "}", ";\r\n}\r\n");
      cssText = StringUtils.replace(cssText, "; ", ";\r\n\t");
    }
    else if (browserVersion.hasFeature(BrowserVersionFeatures.CSS_FONTFACERULE_CSSTEXT_NO_CRLF)) {
      cssText = StringUtils.replace(cssText, "{", "{ ");
      cssText = StringUtils.replace(cssText, "}", "; }");
      cssText = StringUtils.replace(cssText, "; ", "; ");
      cssText = REPLACEMENT_1.matcher(cssText).replaceFirst("font-family: $1;");
      cssText = REPLACEMENT_2.matcher(cssText).replaceFirst("src: url(\"$1\");");
    }
    else {
      cssText = StringUtils.replace(cssText, "{", "{\n  ");
      cssText = StringUtils.replace(cssText, "}", ";\n}");
      cssText = StringUtils.replace(cssText, "; ", ";\n  ");
      cssText = REPLACEMENT_1.matcher(cssText).replaceFirst("font-family: \"$1\";");
      cssText = REPLACEMENT_2.matcher(cssText).replaceFirst("src: url(\"$1\");");
    }
    return cssText;
  }
}
