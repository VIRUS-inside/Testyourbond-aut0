package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.configuration.JavaScriptConfiguration;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.xml.sax.Attributes;















































































class DefaultElementFactory
  implements ElementFactory
{
  static final List<String> SUPPORTED_TAGS_ = Arrays.asList(new String[] { "abbr", "acronym", "a", "address", "applet", "area", "article", "aside", "audio", "bgsound", "base", "basefont", "bdi", "bdo", "big", "blink", "blockquote", "body", "b", "br", "button", "canvas", "caption", "center", "cite", "code", "command", "content", "data", "datalist", "dfn", "dd", "del", "details", "dialog", "dir", "div", "dl", "dt", "embed", "em", "fieldset", "figcaption", "figure", "font", "form", "footer", "frame", "frameset", "head", "header", "h1", "h2", "h3", "h4", "h5", "h6", "hr", "html", "iframe", "q", "img", "image", "ins", "isindex", "i", "kbd", "keygen", "label", "layer", "legend", "listing", "li", "link", "main", "map", "mark", "marquee", "menu", "menuitem", "meta", "meter", "multicol", "nav", "nextid", "nobr", "noembed", "noframes", "nolayer", "noscript", "object", "ol", "optgroup", "option", "output", "p", "param", "picture", "plaintext", "pre", "progress", "rp", "rt", "ruby", "s", "samp", "script", "section", "select", "slot", "small", "source", "span", "strike", "strong", "style", "sub", "summary", "sup", "table", "col", "colgroup", "tbody", "td", "th", "tr", "textarea", "tfoot", "thead", "tt", "template", "time", "title", "track", "u", "ul", "var", "video", "wbr", "xmp" });
  



  DefaultElementFactory() {}
  


  public HtmlElement createElement(SgmlPage page, String tagName, Attributes attributes)
  {
    return createElementNS(page, null, tagName, attributes);
  }
  








  public HtmlElement createElementNS(SgmlPage page, String namespaceURI, String qualifiedName, Attributes attributes)
  {
    return createElementNS(page, namespaceURI, qualifiedName, attributes, false);
  }
  









  public HtmlElement createElementNS(SgmlPage page, String namespaceURI, String qualifiedName, Attributes attributes, boolean checkBrowserCompatibility)
  {
    Map<String, DomAttr> attributeMap = setAttributes(page, attributes);
    


    int colonIndex = qualifiedName.indexOf(':');
    String tagName; String tagName; if (colonIndex == -1) {
      tagName = qualifiedName.toLowerCase(Locale.ROOT);
    }
    else
      tagName = qualifiedName.substring(colonIndex + 1).toLowerCase(Locale.ROOT);
    String str1;
    HtmlElement element;
    switch ((str1 = tagName).hashCode()) {case -1857640538:  if (str1.equals("summary")) {} break; case -1720958304:  if (str1.equals("basefont")) {} break; case -1644953643:  if (str1.equals("frameset")) {} break; case -1411061670:  if (str1.equals("applet")) {} break; case -1377687758:  if (str1.equals("button")) {} break; case -1367706280:  if (str1.equals("canvas")) {} break; case -1364013995:  if (str1.equals("center")) {} break; case -1332085432:  if (str1.equals("dialog")) {} break; case -1321546630:  if (str1.equals("template")) {} break; case -1274639644:  if (str1.equals("figure")) {} break; case -1268861541:  if (str1.equals("footer")) {} break; case -1221270899:  if (str1.equals("header")) {} break; case -1191214428:  if (str1.equals("iframe")) {} break; case -1163472445:  if (str1.equals("acronym")) {} break; case -1147692044:  if (str1.equals("address")) {} break; case -1134665583:  if (str1.equals("keygen")) {} break; case -1106574323:  if (str1.equals("legend")) {} break; case -1048795314:  if (str1.equals("nextid")) {} break; case -1023368385:  if (str1.equals("object")) {} break; case -1010136971:  if (str1.equals("option")) {} break; case -1005512447:  if (str1.equals("output")) {} break; case -1003243718:  if (str1.equals("textarea")) {} break; case -1001078227:  if (str1.equals("progress")) {} break; case -928988888:  if (str1.equals("fieldset")) {} break; case -907685685:  if (str1.equals("script")) {} break; case -906021636:  if (str1.equals("select")) {} break; case -896505829:  if (str1.equals("source")) {} break; case -891985998:  if (str1.equals("strike")) {} break; case -891980137:  if (str1.equals("strong")) {} break; case -732377866:  if (str1.equals("article")) {} break; case -636197633:  if (str1.equals("colgroup")) {} break; case -603141902:  if (str1.equals("menuitem")) {} break; case -577741570:  if (str1.equals("picture")) {} break; case -160522262:  if (str1.equals("bgsound")) {} break; case -80773204:  if (str1.equals("optgroup")) {} break; case 97:  if (str1.equals("a")) {} break; case 98:  if (str1.equals("b")) {} break; case 105:  if (str1.equals("i")) {} break; case 112:  if (str1.equals("p")) {} break; case 113:  if (str1.equals("q")) {} break; case 115:  if (str1.equals("s")) {} break; case 117:  if (str1.equals("u")) {} break; case 3152:  if (str1.equals("br")) {} break; case 3200:  if (str1.equals("dd")) {} break; case 3208:  if (str1.equals("dl")) {} break; case 3216:  if (str1.equals("dt")) {} break; case 3240:  if (str1.equals("em")) {} break; case 3273:  if (str1.equals("h1")) {} break; case 3274:  if (str1.equals("h2")) {} break; case 3275:  if (str1.equals("h3")) {} break; case 3276:  if (str1.equals("h4")) {} break; case 3277:  if (str1.equals("h5")) {} break; case 3278:  if (str1.equals("h6")) {} break; case 3338:  if (str1.equals("hr")) {} break; case 3453:  if (str1.equals("li")) {} break; case 3549:  if (str1.equals("ol")) {} break; case 3646:  if (str1.equals("rp")) {} break; case 3650:  if (str1.equals("rt")) {} break; case 3696:  if (str1.equals("td")) {} break; case 3700:  if (str1.equals("th")) {} break; case 3710:  if (str1.equals("tr")) {} break; case 3712:  if (str1.equals("tt")) {} break; case 3735:  if (str1.equals("ul")) {} break; case 97383:  if (str1.equals("bdi")) {} break; case 97389:  if (str1.equals("bdo")) {} break; case 97536:  if (str1.equals("big")) {} break; case 98688:  if (str1.equals("col")) {} break; case 99339:  if (str1.equals("del")) {} break; case 99372:  if (str1.equals("dfn")) {} break; case 99469:  if (str1.equals("dir")) {} break; case 99473:  if (str1.equals("div")) {} break; case 104387:  if (str1.equals("img")) {} break; case 104430:  if (str1.equals("ins")) {} break; case 105965:  if (str1.equals("kbd")) {} break; case 107868:  if (str1.equals("map")) {} break; case 108835:  if (str1.equals("nav")) {} break; case 111267:  if (str1.equals("pre")) {} break; case 114240:  if (str1.equals("sub")) {} break; case 114254:  if (str1.equals("sup")) {} break; case 116519:  if (str1.equals("var")) {} break; case 117511:  if (str1.equals("wbr")) {} break; case 118811:  if (str1.equals("xmp")) {} break; case 2987057:  if (str1.equals("abbr")) break; break; case 3002509:  if (str1.equals("area")) {} break; case 3016401:  if (str1.equals("base")) {} break; case 3029410:  if (str1.equals("body")) {} break; case 3053911:  if (str1.equals("cite")) {} break; case 3059181:  if (str1.equals("code")) {} break; case 3076010:  if (str1.equals("data")) {} break; case 3148879:  if (str1.equals("font")) {} break; case 3148996:  if (str1.equals("form")) {} break; case 3198432:  if (str1.equals("head")) {} break; case 3213227:  if (str1.equals("html")) {} break; case 3321850:  if (str1.equals("link")) {} break; case 3343801:  if (str1.equals("main")) {} break; case 3344077:  if (str1.equals("mark")) {} break; case 3347807:  if (str1.equals("menu")) {} break; case 3347973:  if (str1.equals("meta")) {} break; case 3386833:  if (str1.equals("nobr")) {} break; case 3511770:  if (str1.equals("ruby")) {} break; case 3522673:  if (str1.equals("samp")) {} break; case 3533310:  if (str1.equals("slot")) {} break; case 3536714:  if (str1.equals("span")) {} break; case 3560141:  if (str1.equals("time")) {} break; case 93111608:  if (str1.equals("aside")) {} break; case 93166550:  if (str1.equals("audio")) {} break; case 93826908:  if (str1.equals("blink")) {} break; case 96620249:  if (str1.equals("embed")) {} break; case 97692013:  if (str1.equals("frame")) {} break; case 100313435:  if (str1.equals("image")) {} break; case 102727412:  if (str1.equals("label")) {} break; case 102749521:  if (str1.equals("layer")) {} break; case 103787401:  if (str1.equals("meter")) {} break; case 106436749:  if (str1.equals("param")) {} break; case 109548807:  if (str1.equals("small")) {} break; case 109780401:  if (str1.equals("style")) {} break; case 110115790:  if (str1.equals("table")) {} break; case 110157846:  if (str1.equals("tbody")) {} break; case 110277346:  if (str1.equals("tfoot")) {} break; case 110326868:  if (str1.equals("thead")) {} break; case 110371416:  if (str1.equals("title")) {} break; case 110621003:  if (str1.equals("track")) {} break; case 112202875:  if (str1.equals("video")) {} break; case 181975684:  if (str1.equals("listing")) {} break; case 299712866:  if (str1.equals("figcaption")) {} break; case 552573414:  if (str1.equals("caption")) {} break; case 653817255:  if (str1.equals("multicol")) {} break; case 839444514:  if (str1.equals("marquee")) {} break; case 950394699:  if (str1.equals("command")) {} break; case 951530617:  if (str1.equals("content")) {} break; case 1192721831:  if (str1.equals("noframes")) {} break; case 1303202319:  if (str1.equals("blockquote")) {} break; case 1551550924:  if (str1.equals("noscript")) {} break; case 1557721666:  if (str1.equals("details")) {} break; case 1789770568:  if (str1.equals("datalist")) {} break; case 1970241253:  if (str1.equals("section")) {} break; case 1973234167:  if (str1.equals("plaintext")) {} break; case 2091304424:  if (str1.equals("isindex")) {} break; case 2115613112:  if (str1.equals("noembed")) {} break; case 2121742384:  if (!str1.equals("nolayer")) {
        break label5329;
        HtmlElement element = new HtmlAbbreviated(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlAcronym(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlAddress(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlAnchor(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlApplet(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlArea(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlArticle(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlAside(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlAudio(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlBackgroundSound(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlBase(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlBaseFont(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlBidirectionalIsolation(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlBidirectionalOverride(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlBig(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlBlink(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlBlockQuote(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlBody(qualifiedName, page, attributeMap, false);
        
        break label5354;
        
        HtmlElement element = new HtmlBold(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlBreak(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlButton(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlCanvas(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlCaption(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlCenter(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlCitation(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlCode(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlCommand(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlContent(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlData(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlDataList(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlDefinition(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlDefinitionDescription(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlDefinitionList(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlDefinitionTerm(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlDeletedText(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlDetails(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlDialog(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlDirectory(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlDivision(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlEmbed(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlEmphasis(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlExample(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlFieldSet(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlFigure(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlFigureCaption(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlFont(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlForm(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlFooter(qualifiedName, page, attributeMap);
        
        break label5354;
        
        if (attributeMap != null) {
          DomAttr srcAttribute = (DomAttr)attributeMap.get("src");
          if (srcAttribute != null) {
            srcAttribute.setValue(srcAttribute.getValue().trim());
          }
        }
        HtmlElement element = new HtmlFrame(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlFrameSet(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlHead(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlHeader(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlHeading1(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlHeading2(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlHeading3(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlHeading4(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlHeading5(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlHeading6(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlHorizontalRule(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlHtml(qualifiedName, page, attributeMap);
        

        break label5354;
        
        HtmlElement element = new HtmlImage(qualifiedName, page, attributeMap);
        
        break label5354;
        
        if (attributeMap != null) {
          DomAttr srcAttribute = (DomAttr)attributeMap.get("src");
          if (srcAttribute != null) {
            srcAttribute.setValue(srcAttribute.getValue().trim());
          }
        }
        HtmlElement element = new HtmlInlineFrame(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlInlineQuotation(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlInsertedText(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlIsIndex(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlItalic(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlKeyboard(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlKeygen(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlLabel(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlLayer(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlLegend(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlLink(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlListing(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlListItem(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlMain(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlMap(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlMark(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlMarquee(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlMenu(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlMenuItem(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlMeta(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlMeter(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlMultiColumn(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlNav(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlNextId(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlNoBreak(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlNoEmbed(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlNoFrames(qualifiedName, page, attributeMap);
        break label5354;
      }
      else {
        HtmlElement element = new HtmlNoLayer(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlNoScript(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlObject(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlOption(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlOptionGroup(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlOrderedList(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlOutput(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlParagraph(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlParameter(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlPicture(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlPlainText(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlPreformattedText(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlProgress(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlRp(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlRt(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlRuby(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlS(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlSample(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlScript(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlSection(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlSelect(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlSmall(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlSlot(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlSource(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlSpan(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlStrike(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlStrong(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlStyle(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlSubscript(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlSummary(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlSuperscript(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlTable(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlTableBody(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlTableColumn(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlTableColumnGroup(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlTableDataCell(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlTableFooter(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlTableHeader(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlTableHeaderCell(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlTableRow(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlTeletype(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlTemplate(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlTextArea(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlTime(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlTitle(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlTrack(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlUnderlined(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlUnorderedList(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlVariable(qualifiedName, page, attributeMap);
        
        break label5354;
        
        HtmlElement element = new HtmlVideo(qualifiedName, page, attributeMap);
        
        break label5354;
        
        element = new HtmlWordBreak(qualifiedName, page, attributeMap); }
      break;
    }
    label5329:
    throw new IllegalStateException("Cannot find HtmlElement for " + qualifiedName);
    label5354:
    HtmlElement element;
    JavaScriptConfiguration config = page.getWebClient().getJavaScriptEngine().getJavaScriptConfiguration();
    if ((!"td".equals(tagName)) && (!"th".equals(tagName)) && 
      (checkBrowserCompatibility) && (config.getDomJavaScriptMapping().get(element.getClass()) == null)) {
      return UnknownElementFactory.instance.createElementNS(page, namespaceURI, qualifiedName, attributes);
    }
    return element;
  }
  






  static Map<String, DomAttr> setAttributes(SgmlPage page, Attributes attributes)
  {
    Map<String, DomAttr> attributeMap = null;
    if (attributes != null) {
      attributeMap = new LinkedHashMap(attributes.getLength());
      for (int i = 0; i < attributes.getLength(); i++) {
        String qName = attributes.getQName(i);
        
        if (!attributeMap.containsKey(qName)) {
          String namespaceURI = attributes.getURI(i);
          if ((namespaceURI != null) && (namespaceURI.isEmpty())) {
            namespaceURI = null;
          }
          DomAttr newAttr = new DomAttr(page, namespaceURI, qName, attributes.getValue(i), true);
          attributeMap.put(qName, newAttr);
        }
      }
    }
    return attributeMap;
  }
}
