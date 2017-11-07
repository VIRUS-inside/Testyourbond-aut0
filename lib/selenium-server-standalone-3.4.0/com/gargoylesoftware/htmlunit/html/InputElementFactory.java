package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
























public final class InputElementFactory
  implements ElementFactory
{
  private static final Log LOG = LogFactory.getLog(InputElementFactory.class);
  

  public static final InputElementFactory instance = new InputElementFactory();
  







  private InputElementFactory() {}
  







  public HtmlElement createElement(SgmlPage page, String tagName, Attributes attributes)
  {
    return createElementNS(page, null, tagName, attributes);
  }
  




  public HtmlElement createElementNS(SgmlPage page, String namespaceURI, String qualifiedName, Attributes attributes)
  {
    return createElementNS(page, namespaceURI, qualifiedName, attributes, false);
  }
  





  public HtmlElement createElementNS(SgmlPage page, String namespaceURI, String qualifiedName, Attributes attributes, boolean asdf)
  {
    Map<String, DomAttr> attributeMap = DefaultElementFactory.setAttributes(page, attributes);
    if (attributeMap == null) {
      attributeMap = new HashMap();
    }
    
    String type = null;
    for (Object localObject = attributeMap.entrySet().iterator(); ((Iterator)localObject).hasNext();) { Map.Entry<String, DomAttr> entry = (Map.Entry)((Iterator)localObject).next();
      if ("type".equalsIgnoreCase((String)entry.getKey())) {
        type = ((DomAttr)entry.getValue()).getValue();
      }
    }
    if (type == null) {
      type = "";
    }
    
    HtmlInput result;
    switch ((localObject = type.toLowerCase(Locale.ROOT)).hashCode()) {case -1377687758:  if (((String)localObject).equals("button")) {} break; case -1217487446:  if (((String)localObject).equals("hidden")) {} break; case -1034364087:  if (((String)localObject).equals("number")) {} break; case -1023416679:  if (((String)localObject).equals("datetime-local")) {} break; case -906336856:  if (((String)localObject).equals("search")) {} break; case -891535336:  if (((String)localObject).equals("submit")) {} break; case 0:  if (((String)localObject).equals("")) break; break; case 114715:  if (((String)localObject).equals("tel")) {} break; case 116079:  if (((String)localObject).equals("url")) {} break; case 3076014:  if (((String)localObject).equals("date")) {} break; case 3143036:  if (((String)localObject).equals("file")) {} break; case 3556653:  if (((String)localObject).equals("text")) break; break; case 3560141:  if (((String)localObject).equals("time")) {} break; case 3645428:  if (((String)localObject).equals("week")) {} break; case 94842723:  if (((String)localObject).equals("color")) {} break; case 96619420:  if (((String)localObject).equals("email")) {} break; case 100313435:  if (((String)localObject).equals("image")) {} break; case 104080000:  if (((String)localObject).equals("month")) {} break; case 108270587:  if (((String)localObject).equals("radio")) {} break; case 108280125:  if (((String)localObject).equals("range")) {} break; case 108404047:  if (((String)localObject).equals("reset")) {} break; case 1216985755:  if (((String)localObject).equals("password")) {} break; case 1536891843:  if (!((String)localObject).equals("checkbox"))
      {
        break label967;
        


        HtmlInput result = new HtmlTextInput(qualifiedName, page, attributeMap);
        
        break label1010;
        
        HtmlInput result = new HtmlSubmitInput(qualifiedName, page, attributeMap);
        break label1010;
      }
      else {
        HtmlInput result = new HtmlCheckBoxInput(qualifiedName, page, attributeMap);
        
        break label1010;
        
        HtmlInput result = new HtmlRadioButtonInput(qualifiedName, page, attributeMap);
        
        break label1010;
        
        HtmlInput result = new HtmlHiddenInput(qualifiedName, page, attributeMap);
        
        break label1010;
        
        HtmlInput result = new HtmlPasswordInput(qualifiedName, page, attributeMap);
        
        break label1010;
        
        HtmlInput result = new HtmlImageInput(qualifiedName, page, attributeMap);
        
        break label1010;
        
        HtmlInput result = new HtmlResetInput(qualifiedName, page, attributeMap);
        
        break label1010;
        
        HtmlInput result = new HtmlButtonInput(qualifiedName, page, attributeMap);
        
        break label1010;
        
        HtmlInput result = new HtmlFileInput(qualifiedName, page, attributeMap);
        
        break label1010;
        
        HtmlInput result = new HtmlColorInput(qualifiedName, page, attributeMap);
        
        break label1010;
        
        HtmlInput result = new HtmlDateInput(qualifiedName, page, attributeMap);
        
        break label1010;
        
        HtmlInput result = new HtmlDateTimeLocalInput(qualifiedName, page, attributeMap);
        
        break label1010;
        
        HtmlInput result = new HtmlEmailInput(qualifiedName, page, attributeMap);
        
        break label1010;
        
        HtmlInput result = new HtmlMonthInput(qualifiedName, page, attributeMap);
        
        break label1010;
        
        HtmlInput result = new HtmlNumberInput(qualifiedName, page, attributeMap);
        
        break label1010;
        
        HtmlInput result = new HtmlRangeInput(qualifiedName, page, attributeMap);
        
        break label1010;
        
        HtmlInput result = new HtmlSearchInput(qualifiedName, page, attributeMap);
        
        break label1010;
        
        HtmlInput result = new HtmlTelInput(qualifiedName, page, attributeMap);
        
        break label1010;
        
        HtmlInput result = new HtmlTimeInput(qualifiedName, page, attributeMap);
        
        break label1010;
        
        HtmlInput result = new HtmlUrlInput(qualifiedName, page, attributeMap);
        
        break label1010;
        
        result = new HtmlWeekInput(qualifiedName, page, attributeMap); }
      break;
    }
    label967:
    LOG.info("Bad input type: \"" + type + "\", creating a text input");
    HtmlInput result = new HtmlTextInput(qualifiedName, page, attributeMap);
    
    label1010:
    return result;
  }
  
  /* Error */
  public static boolean isSupported(String type)
  {
    // Byte code:
    //   0: iconst_0
    //   1: istore_1
    //   2: aload_0
    //   3: dup
    //   4: astore_2
    //   5: invokevirtual 112	java/lang/String:hashCode	()I
    //   8: lookupswitch	default:+454->462, -1377687758:+188->196, -1217487446:+200->208, -1034364087:+212->220, -1023416679:+224->232, -906336856:+236->244, -891535336:+248->256, 114715:+260->268, 116079:+272->280, 3076014:+284->292, 3143036:+296->304, 3556653:+308->316, 3560141:+320->328, 3645428:+332->340, 94842723:+344->352, 96619420:+356->364, 100313435:+368->376, 104080000:+380->388, 108270587:+392->400, 108280125:+404->412, 108404047:+416->424, 1216985755:+428->436, 1536891843:+440->448
    //   196: aload_2
    //   197: ldc 116
    //   199: invokevirtual 118	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   202: ifne +258 -> 460
    //   205: goto +257 -> 462
    //   208: aload_2
    //   209: ldc 122
    //   211: invokevirtual 118	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   214: ifne +246 -> 460
    //   217: goto +245 -> 462
    //   220: aload_2
    //   221: ldc 124
    //   223: invokevirtual 118	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   226: ifne +234 -> 460
    //   229: goto +233 -> 462
    //   232: aload_2
    //   233: ldc 126
    //   235: invokevirtual 118	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   238: ifne +222 -> 460
    //   241: goto +221 -> 462
    //   244: aload_2
    //   245: ldc -128
    //   247: invokevirtual 118	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   250: ifne +210 -> 460
    //   253: goto +209 -> 462
    //   256: aload_2
    //   257: ldc -126
    //   259: invokevirtual 118	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   262: ifne +198 -> 460
    //   265: goto +197 -> 462
    //   268: aload_2
    //   269: ldc -124
    //   271: invokevirtual 118	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   274: ifne +186 -> 460
    //   277: goto +185 -> 462
    //   280: aload_2
    //   281: ldc -122
    //   283: invokevirtual 118	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   286: ifne +174 -> 460
    //   289: goto +173 -> 462
    //   292: aload_2
    //   293: ldc -120
    //   295: invokevirtual 118	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   298: ifne +162 -> 460
    //   301: goto +161 -> 462
    //   304: aload_2
    //   305: ldc -118
    //   307: invokevirtual 118	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   310: ifne +150 -> 460
    //   313: goto +149 -> 462
    //   316: aload_2
    //   317: ldc -116
    //   319: invokevirtual 118	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   322: ifne +138 -> 460
    //   325: goto +137 -> 462
    //   328: aload_2
    //   329: ldc -114
    //   331: invokevirtual 118	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   334: ifne +126 -> 460
    //   337: goto +125 -> 462
    //   340: aload_2
    //   341: ldc -112
    //   343: invokevirtual 118	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   346: ifne +114 -> 460
    //   349: goto +113 -> 462
    //   352: aload_2
    //   353: ldc -110
    //   355: invokevirtual 118	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   358: ifne +102 -> 460
    //   361: goto +101 -> 462
    //   364: aload_2
    //   365: ldc -108
    //   367: invokevirtual 118	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   370: ifne +90 -> 460
    //   373: goto +89 -> 462
    //   376: aload_2
    //   377: ldc -106
    //   379: invokevirtual 118	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   382: ifne +78 -> 460
    //   385: goto +77 -> 462
    //   388: aload_2
    //   389: ldc -104
    //   391: invokevirtual 118	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   394: ifne +66 -> 460
    //   397: goto +65 -> 462
    //   400: aload_2
    //   401: ldc -102
    //   403: invokevirtual 118	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   406: ifne +54 -> 460
    //   409: goto +53 -> 462
    //   412: aload_2
    //   413: ldc -100
    //   415: invokevirtual 118	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   418: ifne +42 -> 460
    //   421: goto +41 -> 462
    //   424: aload_2
    //   425: ldc -98
    //   427: invokevirtual 118	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   430: ifne +30 -> 460
    //   433: goto +29 -> 462
    //   436: aload_2
    //   437: ldc -96
    //   439: invokevirtual 118	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   442: ifne +18 -> 460
    //   445: goto +17 -> 462
    //   448: aload_2
    //   449: ldc -94
    //   451: invokevirtual 118	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   454: ifne +6 -> 460
    //   457: goto +5 -> 462
    //   460: iconst_1
    //   461: istore_1
    //   462: iload_1
    //   463: ireturn
    // Line number table:
    //   Java source line #205	-> byte code offset #0
    //   Java source line #206	-> byte code offset #2
    //   Java source line #229	-> byte code offset #460
    //   Java source line #234	-> byte code offset #462
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	464	0	type	String
    //   1	462	1	supported	boolean
    //   4	445	2	str	String
  }
}
