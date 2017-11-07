package net.sourceforge.htmlunit.cyberneko;

import java.util.HashMap;
import java.util.Map;









































public class HTMLElements
{
  public static final short A = 0;
  public static final short ABBR = 1;
  public static final short ACRONYM = 2;
  public static final short ADDRESS = 3;
  public static final short APPLET = 4;
  public static final short AREA = 5;
  public static final short ARTICLE = 6;
  public static final short ASIDE = 7;
  public static final short B = 8;
  public static final short BASE = 9;
  public static final short BASEFONT = 10;
  public static final short BDO = 11;
  public static final short BGSOUND = 12;
  public static final short BIG = 13;
  public static final short BLINK = 14;
  public static final short BLOCKQUOTE = 15;
  public static final short BODY = 16;
  public static final short BR = 17;
  public static final short BUTTON = 18;
  public static final short CAPTION = 19;
  public static final short CENTER = 20;
  public static final short CITE = 21;
  public static final short CODE = 22;
  public static final short COL = 23;
  public static final short COLGROUP = 24;
  public static final short COMMAND = 25;
  public static final short COMMENT = 26;
  public static final short DEL = 27;
  public static final short DETAILS = 28;
  public static final short DFN = 29;
  public static final short DIR = 30;
  public static final short DIV = 31;
  public static final short DD = 32;
  public static final short DL = 33;
  public static final short DT = 34;
  public static final short EM = 35;
  public static final short EMBED = 36;
  public static final short FIELDSET = 37;
  public static final short FIGCAPTION = 38;
  public static final short FIGURE = 39;
  public static final short FONT = 40;
  public static final short FOOTER = 41;
  public static final short FORM = 42;
  public static final short FRAME = 43;
  public static final short FRAMESET = 44;
  public static final short H1 = 45;
  public static final short H2 = 46;
  public static final short H3 = 47;
  public static final short H4 = 48;
  public static final short H5 = 49;
  public static final short H6 = 50;
  public static final short HEAD = 51;
  public static final short HEADER = 52;
  public static final short HR = 53;
  public static final short HTML = 54;
  public static final short I = 55;
  public static final short IFRAME = 56;
  public static final short ILAYER = 57;
  public static final short IMG = 58;
  public static final short IMAGE = 59;
  public static final short INPUT = 60;
  public static final short INS = 61;
  public static final short ISINDEX = 62;
  public static final short KBD = 63;
  public static final short KEYGEN = 64;
  public static final short LABEL = 65;
  public static final short LAYER = 66;
  public static final short LEGEND = 67;
  public static final short LI = 68;
  public static final short LINK = 69;
  public static final short LISTING = 70;
  public static final short MAIN = 71;
  public static final short MAP = 72;
  public static final short MARQUEE = 73;
  public static final short MENU = 74;
  public static final short META = 75;
  public static final short MULTICOL = 76;
  public static final short NAV = 77;
  public static final short NEXTID = 78;
  public static final short NOBR = 79;
  public static final short NOEMBED = 80;
  public static final short NOFRAMES = 81;
  public static final short NOLAYER = 82;
  public static final short NOSCRIPT = 83;
  public static final short OBJECT = 84;
  public static final short OL = 85;
  public static final short OPTGROUP = 86;
  public static final short OPTION = 87;
  public static final short P = 88;
  public static final short PARAM = 89;
  public static final short PLAINTEXT = 90;
  public static final short PRE = 91;
  public static final short Q = 92;
  public static final short RB = 93;
  public static final short RBC = 94;
  public static final short RP = 95;
  public static final short RT = 96;
  public static final short RTC = 97;
  public static final short RUBY = 98;
  public static final short S = 99;
  public static final short SAMP = 100;
  public static final short SCRIPT = 101;
  public static final short SECTION = 102;
  public static final short SELECT = 103;
  public static final short SMALL = 104;
  public static final short SOUND = 105;
  public static final short SOURCE = 106;
  public static final short SPACER = 107;
  public static final short SPAN = 108;
  public static final short STRIKE = 109;
  public static final short STRONG = 110;
  public static final short STYLE = 111;
  public static final short SUB = 112;
  public static final short SUMMARY = 113;
  public static final short SUP = 114;
  public static final short TABLE = 115;
  public static final short TBODY = 116;
  public static final short TD = 117;
  public static final short TEMPLATE = 118;
  public static final short TEXTAREA = 119;
  public static final short TFOOT = 120;
  public static final short TH = 121;
  public static final short THEAD = 122;
  public static final short TITLE = 123;
  public static final short TR = 124;
  public static final short TRACK = 125;
  public static final short TT = 126;
  public static final short U = 127;
  public static final short UL = 128;
  public static final short VAR = 129;
  public static final short WBR = 130;
  public static final short XML = 131;
  public static final short XMP = 132;
  public static final short UNKNOWN = 133;
  public final Element NO_SUCH_ELEMENT = new Element((short)133, "", 8, new short[] { 16, 51 }, null);
  
  public final Map<Short, Element> elementsMap = new HashMap();
  


  public HTMLElements()
  {
    Element[][] elementsArray = new Element[26][];
    









    elementsArray[0] = {
    
      new Element(0, "A", 8, 16, new short[1]), 
      
      new Element(1, "ABBR", 1, 16, null), 
      
      new Element(2, "ACRONYM", 1, 16, null), 
      
      new Element(3, "ADDRESS", 2, 16, new short[] { 88 }), 
      
      new Element(4, "APPLET", 8, 16, null), 
      
      new Element(5, "AREA", 4, 72, null), 
      
      new Element(6, "ARTICLE", 2, 16, new short[] { 88 }), 
      
      new Element(7, "ASIDE", 2, 16, new short[] { 88 }) };
    
    elementsArray[1] = {
    
      new Element(8, "B", 1, 16, null), 
      
      new Element(9, "BASE", 4, 51, null), 
      
      new Element(10, "BASEFONT", 4, 51, null), 
      
      new Element(11, "BDO", 1, 16, null), 
      
      new Element(12, "BGSOUND", 4, 51, null), 
      
      new Element(13, "BIG", 1, 16, null), 
      
      new Element(14, "BLINK", 1, 16, null), 
      
      new Element(15, "BLOCKQUOTE", 2, 16, new short[] { 88 }), 
      
      new Element(16, "BODY", 8, 54, new short[] { 51 }), 
      
      new Element(17, "BR", 4, 16, null), 
      
      new Element(18, "BUTTON", 3, 16, new short[] { 18 }) };
    
    elementsArray[2] = {
    
      new Element(19, "CAPTION", 1, 115, null), 
      
      new Element(20, "CENTER", 8, 16, new short[] { 88 }), 
      
      new Element(21, "CITE", 1, 16, null), 
      
      new Element(22, "CODE", 1, 16, null), 
      
      new Element(23, "COL", 4, 115, null), 
      
      new Element(24, "COLGROUP", 8, 115, new short[] { 23, 24 }), 
      
      new Element(26, "COMMENT", 16, 54, null) };
    
    elementsArray[3] = {
    
      new Element(27, "DEL", 1, 16, null), 
      
      new Element(28, "DETAILS", 2, 16, new short[] { 88 }), 
      
      new Element(29, "DFN", 1, 16, null), 
      
      new Element(30, "DIR", 8, 16, new short[] { 88 }), 
      
      new Element(31, "DIV", 8, 16, new short[] { 88 }), 
      
      new Element(32, "DD", 2, 16, new short[] { 34, 32, 88 }), 
      
      new Element(33, "DL", 10, 16, new short[] { 88 }), 
      
      new Element(34, "DT", 2, 16, new short[] { 34, 32, 88 }) };
    
    elementsArray[4] = {
    
      new Element(35, "EM", 1, 16, null), 
      
      new Element(36, "EMBED", 4, 16, null) };
    
    elementsArray[5] = {
    
      new Element(37, "FIELDSET", 8, 16, new short[] { 88 }), 
      
      new Element(38, "FIGCAPTION", 2, 16, new short[] { 88 }), 
      
      new Element(39, "FIGURE", 2, 16, new short[] { 88 }), 
      
      new Element(40, "FONT", 8, 16, null), 
      
      new Element(41, "FOOTER", 2, 16, new short[] { 88 }), 
      

      new Element(42, "FORM", 8, new short[] { 16, 117, 31 }, new short[] { 18, 88 }), 
      
      new Element(43, "FRAME", 4, 44, null), 
      
      new Element(44, "FRAMESET", 8, 54, null) };
    
    elementsArray[7] = {
    
      new Element(45, "H1", 2, new short[] { 16 }, new short[] { 45, 46, 47, 48, 49, 50, 88 }), 
      new Element(46, "H2", 2, new short[] { 16 }, new short[] { 45, 46, 47, 48, 49, 50, 88 }), 
      new Element(47, "H3", 2, new short[] { 16 }, new short[] { 45, 46, 47, 48, 49, 50, 88 }), 
      new Element(48, "H4", 2, new short[] { 16 }, new short[] { 45, 46, 47, 48, 49, 50, 88 }), 
      new Element(49, "H5", 2, new short[] { 16 }, new short[] { 45, 46, 47, 48, 49, 50, 88 }), 
      new Element(50, "H6", 2, new short[] { 16 }, new short[] { 45, 46, 47, 48, 49, 50, 88 }), 
      
      new Element(51, "HEAD", 0, 54, null), 
      
      new Element(52, "HEADER", 2, 16, new short[] { 88 }), 
      

      new Element(53, "HR", 4, 16, new short[] { 88 }), 
      
      new Element(54, "HTML", 0, null, null) };
    
    elementsArray[8] = {
    
      new Element(55, "I", 1, 16, null), 
      
      new Element(56, "IFRAME", 2, 16, null), 
      
      new Element(57, "ILAYER", 2, 16, null), 
      
      new Element(58, "IMG", 4, 16, null), 
      
      new Element(59, "IMAGE", 4, 16, null), 
      
      new Element(60, "INPUT", 4, 16, null), 
      
      new Element(61, "INS", 1, 16, null), 
      
      new Element(62, "ISINDEX", 2, 16, new short[] { 62, 88 }) };
    
    elementsArray[10] = {
    
      new Element(63, "KBD", 1, 16, null), 
      
      new Element(64, "KEYGEN", 4, 16, null) };
    
    elementsArray[11] = {
    
      new Element(65, "LABEL", 1, 16, null), 
      
      new Element(66, "LAYER", 2, 16, null), 
      
      new Element(67, "LEGEND", 1, 16, null), 
      
      new Element(68, "LI", 8, new short[] { 16, 128, 85 }, new short[] { 68, 88 }), 
      
      new Element(69, "LINK", 4, 51, null), 
      
      new Element(70, "LISTING", 2, 16, new short[] { 88 }) };
    
    elementsArray[12] = {
      new Element(71, "MAIN", 2, 16, new short[] { 88 }), 
      
      new Element(72, "MAP", 1, 16, null), 
      
      new Element(73, "MARQUEE", 8, 16, null), 
      
      new Element(74, "MENU", 8, 16, new short[] { 88 }), 
      
      new Element(75, "META", 4, 51, new short[] { 111, 123 }), 
      
      new Element(76, "MULTICOL", 8, 16, null) };
    
    elementsArray[13] = {
      new Element(77, "NAV", 2, 16, new short[] { 88 }), 
      

      new Element(78, "NEXTID", 1, 16, null), 
      
      new Element(79, "NOBR", 1, 16, new short[] { 79 }), 
      
      new Element(80, "NOEMBED", 8, 16, null), 
      
      new Element(81, "NOFRAMES", 8, null, null), 
      
      new Element(82, "NOLAYER", 8, 16, null), 
      
      new Element(83, "NOSCRIPT", 8, new short[] { 16 }, null) };
    
    elementsArray[14] = {
    
      new Element(84, "OBJECT", 8, 16, null), 
      
      new Element(85, "OL", 2, 16, new short[] { 88 }), 
      
      new Element(86, "OPTGROUP", 1, 16, new short[] { 87 }), 
      
      new Element(87, "OPTION", 1, 16, new short[] { 87 }) };
    
    elementsArray[15] = {
    
      new Element(88, "P", 8, 16, new short[] { 88 }), 
      
      new Element(89, "PARAM", 4, 51, null), 
      
      new Element(90, "PLAINTEXT", 16, 16, null), 
      
      new Element(91, "PRE", 2, 16, new short[] { 88 }) };
    
    elementsArray[16] = {
    
      new Element(92, "Q", 1, 16, null) };
    
    elementsArray[17] = {
    
      new Element(93, "RB", 1, 98, new short[] { 93 }), 
      
      new Element(94, "RBC", 0, 98, null), 
      
      new Element(95, "RP", 1, 16, new short[] { 93 }), 
      
      new Element(96, "RT", 1, 16, new short[] { 93, 95 }), 
      
      new Element(97, "RTC", 0, 98, new short[] { 94 }), 
      
      new Element(98, "RUBY", 8, 16, null) };
    
    elementsArray[18] = {
    
      new Element(99, "S", 1, 16, null), 
      
      new Element(100, "SAMP", 1, 16, null), 
      
      new Element(101, "SCRIPT", 16, new short[] { 51, 16 }, null), 
      
      new Element(102, "SECTION", 2, 16, new short[] { 103, 88 }), 
      
      new Element(103, "SELECT", 8, 16, new short[] { 103 }), 
      
      new Element(104, "SMALL", 1, 16, null), 
      
      new Element(105, "SOUND", 4, 51, null), 
      
      new Element(106, "SOURCE", 4, 51, null), 
      
      new Element(107, "SPACER", 1, 16, null), 
      
      new Element(108, "SPAN", 8, 16, null), 
      
      new Element(109, "STRIKE", 1, 16, null), 
      
      new Element(110, "STRONG", 1, 16, null), 
      
      new Element(111, "STYLE", 16, new short[] { 51, 16 }, new short[] { 111, 123, 75 }), 
      
      new Element(112, "SUB", 1, 16, null), 
      
      new Element(113, "SUMMARY", 2, 16, new short[] { 88 }), 
      
      new Element(114, "SUP", 1, 16, null) };
    
    elementsArray[19] = {
    
      new Element(115, "TABLE", 10, 16, null), 
      
      new Element(116, "TBODY", 0, 115, new short[] { 122, 116, 120, 117, 121, 124, 24 }), 
      
      new Element(117, "TD", 8, 124, 115, new short[] { 117, 121 }), 
      
      new Element(118, "TEMPLATE", 1, 16, new short[] { 118 }), 
      
      new Element(119, "TEXTAREA", 16, 16, null), 
      
      new Element(120, "TFOOT", 0, 115, new short[] { 122, 116, 120, 117, 121, 124 }), 
      
      new Element(121, "TH", 8, 124, 115, new short[] { 117, 121 }), 
      
      new Element(122, "THEAD", 0, 115, new short[] { 122, 116, 120, 117, 121, 124, 24 }), 
      
      new Element(123, "TITLE", 16, new short[] { 51, 16 }, null), 
      
      new Element(124, "TR", 2, new short[] { 116, 122, 120 }, 115, new short[] { 117, 121, 124, 24, 31 }), 
      
      new Element(125, "TRACK", 4, 51, null), 
      
      new Element(126, "TT", 1, 16, null) };
    
    elementsArray[20] = {
    
      new Element(127, "U", 1, 16, null), 
      
      new Element(128, "UL", 8, 16, new short[] { 88 }) };
    
    elementsArray[21] = {
    
      new Element(129, "VAR", 1, 16, null) };
    
    elementsArray[22] = {
    
      new Element(130, "WBR", 4, 16, null) };
    
    elementsArray[23] = {
    
      new Element(131, "XML", 0, 16, null), 
      
      new Element(132, "XMP", 16, 16, new short[] { 88 }) };
    
    Element[] elements;
    
    for (int i = 0; i < elementsArray.length; i++) {
      elements = elementsArray[i];
      if (elements != null) {
        for (int j = 0; j < elements.length; j++) {
          Element element = elements[j];
          elementsMap.put(Short.valueOf(code), element);
        }
      }
    }
    
    elementsMap.put(Short.valueOf(NO_SUCH_ELEMENT.code), NO_SUCH_ELEMENT);
    

    for (Element element : elementsMap.values()) {
      defineParents(element);
    }
  }
  
  public void setElement(Element element)
  {
    elementsMap.put(Short.valueOf(code), element);
    defineParents(element);
  }
  
  private void defineParents(Element element) {
    if (parentCodes != null) {
      parent = new Element[parentCodes.length];
      for (int j = 0; j < parentCodes.length; j++) {
        parent[j] = ((Element)elementsMap.get(Short.valueOf(parentCodes[j])));
      }
      parentCodes = null;
    }
  }
  








  public final Element getElement(short code)
  {
    return (Element)elementsMap.get(Short.valueOf(code));
  }
  




  public final Element getElement(String ename)
  {
    Element element = getElement(ename, NO_SUCH_ELEMENT);
    if (element == NO_SUCH_ELEMENT) {
      element = new Element((short)133, ename.toUpperCase(), 8, new short[] { 16, 51 }, null);
      parent = NO_SUCH_ELEMENT.parent;
      parentCodes = NO_SUCH_ELEMENT.parentCodes;
    }
    return element;
  }
  





  public final Element getElement(String ename, Element element)
  {
    for (Element e : elementsMap.values()) {
      if (name.equalsIgnoreCase(ename)) {
        return e;
      }
    }
    return element;
  }
  





  public static class Element
  {
    public static final int INLINE = 1;
    




    public static final int BLOCK = 2;
    



    public static final int EMPTY = 4;
    



    public static final int CONTAINER = 8;
    



    public static final int SPECIAL = 16;
    



    public short code;
    



    public String name;
    



    public int flags;
    



    public short[] parentCodes;
    



    public Element[] parent;
    



    public short bounds;
    



    public short[] closes;
    




    public Element(short code, String name, int flags, short parent, short[] closes)
    {
      this(code, name, flags, new short[] { parent }, (short)-1, closes);
    }
    









    public Element(short code, String name, int flags, short parent, short bounds, short[] closes)
    {
      this(code, name, flags, new short[] { parent }, bounds, closes);
    }
    









    public Element(short code, String name, int flags, short[] parents, short[] closes)
    {
      this(code, name, flags, parents, (short)-1, closes);
    }
    









    public Element(short code, String name, int flags, short[] parents, short bounds, short[] closes)
    {
      this.code = code;
      this.name = name;
      this.flags = flags;
      parentCodes = parents;
      parent = null;
      this.bounds = bounds;
      this.closes = closes;
    }
    




    public final boolean isInline()
    {
      return (flags & 0x1) != 0;
    }
    
    public final boolean isBlock()
    {
      return (flags & 0x2) != 0;
    }
    
    public final boolean isEmpty()
    {
      return (flags & 0x4) != 0;
    }
    
    public final boolean isContainer()
    {
      return (flags & 0x8) != 0;
    }
    



    public final boolean isSpecial()
    {
      return (flags & 0x10) != 0;
    }
    




    public boolean closes(short tag)
    {
      if (closes != null) {
        for (int i = 0; i < closes.length; i++) {
          if (closes[i] == tag) {
            return true;
          }
        }
      }
      return false;
    }
    






    public int hashCode()
    {
      return name.hashCode();
    }
    

    public boolean equals(Object o)
    {
      return name.equals(o);
    }
    



    public String toString()
    {
      return super.toString() + "(name=" + name + ")";
    }
    




    public boolean isParent(Element element)
    {
      if (parent == null)
        return false;
      for (int i = 0; i < parent.length; i++) {
        if (code == parent[i].code)
          return true;
      }
      return false;
    }
  }
  




  public static class ElementList
  {
    public int size;
    



    public HTMLElements.Element[] data = new HTMLElements.Element[120];
    

    public ElementList() {}
    

    public void addElement(HTMLElements.Element element)
    {
      if (size == data.length) {
        HTMLElements.Element[] newarray = new HTMLElements.Element[size + 20];
        System.arraycopy(data, 0, newarray, 0, size);
        data = newarray;
      }
      data[(size++)] = element;
    }
  }
}
