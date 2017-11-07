package net.sourceforge.htmlunit.corejs.javascript;

import java.io.IOException;
import java.io.Reader;




















class TokenStream
{
  private static final int EOF_CHAR = -1;
  private static final char BYTE_ORDER_MARK = '﻿';
  private boolean dirtyLine;
  String regExpFlags;
  
  TokenStream(Parser parser, Reader sourceReader, String sourceString, int lineno)
  {
    this.parser = parser;
    this.lineno = lineno;
    if (sourceReader != null) {
      if (sourceString != null)
        Kit.codeBug();
      this.sourceReader = sourceReader;
      sourceBuffer = new char['Ȁ'];
      sourceEnd = 0;
    } else {
      if (sourceString == null)
        Kit.codeBug();
      this.sourceString = sourceString;
      sourceEnd = sourceString.length();
    }
    sourceCursor = (this.cursor = 0);
  }
  



















  String tokenToString(int token)
  {
    return "";
  }
  
  static boolean isKeyword(String s) {
    return 0 != stringToKeyword(s);
  }
  

  private static int stringToKeyword(String name)
  {
    int Id_break = 120;int Id_case = 115;
    int Id_continue = 121;int Id_default = 116;
    int Id_delete = 31;int Id_do = 118;
    int Id_else = 113;int Id_export = 127;
    int Id_false = 44;int Id_for = 119;
    int Id_function = 109;int Id_if = 112;
    int Id_in = 52;int Id_let = 153;
    int Id_new = 30;int Id_null = 42;
    int Id_return = 4;int Id_switch = 114;
    int Id_this = 43;int Id_true = 45;
    int Id_typeof = 32;int Id_var = 122;
    int Id_void = 126;int Id_while = 117;
    int Id_with = 123;int Id_yield = 72;
    


    int Id_abstract = 127;
    int Id_boolean = 127;
    int Id_byte = 127;
    int Id_catch = 124;int Id_char = 127;
    int Id_class = 127;int Id_const = 154;
    int Id_debugger = 160;int Id_double = 127;
    
    int Id_enum = 127;int Id_extends = 127;
    int Id_final = 127;
    int Id_finally = 125;int Id_float = 127;
    
    int Id_goto = 127;
    int Id_implements = 127;
    int Id_import = 127;int Id_instanceof = 53;
    int Id_int = 127;
    int Id_interface = 127;
    int Id_long = 127;
    int Id_native = 127;
    int Id_package = 127;
    int Id_private = 127;
    int Id_protected = 127;
    int Id_public = 127;
    int Id_short = 127;
    int Id_static = 127;
    int Id_super = 127;int Id_synchronized = 127;
    
    int Id_throw = 50;int Id_throws = 127;
    int Id_transient = 127;
    int Id_try = 81;int Id_volatile = 127;
    

    String s = name;
    

    int id = 0;
    String X = null;
    
    switch (s.length()) {
    case 2: 
      int c = s.charAt(1);
      if (c == 102) {
        if (s.charAt(0) == 'i') {
          id = 112;
          break label2041;
        }
      } else if (c == 110) {
        if (s.charAt(0) == 'i') {
          id = 52;
          break label2041;
        }
      } else if ((c == 111) && 
        (s.charAt(0) == 'd'))
        id = 118;
      break;
    


    case 3: 
      switch (s.charAt(0)) {
      case 'f': 
        if ((s.charAt(2) == 'r') && (s.charAt(1) == 'o'))
          id = 119;
        break;
      

      case 'i': 
        if ((s.charAt(2) == 't') && (s.charAt(1) == 'n'))
          id = 127;
        break;
      

      case 'l': 
        if ((s.charAt(2) == 't') && (s.charAt(1) == 'e'))
          id = 153;
        break;
      

      case 'n': 
        if ((s.charAt(2) == 'w') && (s.charAt(1) == 'e'))
          id = 30;
        break;
      

      case 't': 
        if ((s.charAt(2) == 'y') && (s.charAt(1) == 'r'))
          id = 81;
        break;
      

      case 'v': 
        if ((s.charAt(2) == 'r') && (s.charAt(1) == 'a')) {
          id = 122;
          break label2041;
        }
        break;
      }
      break;
    case 4: 
      switch (s.charAt(0)) {
      case 'b': 
        X = "byte";
        id = 127;
        break;
      case 'c': 
        int c = s.charAt(3);
        if (c == 101) {
          if ((s.charAt(2) == 's') && (s.charAt(1) == 'a')) {
            id = 115;
            break label2041;
          }
        } else if ((c == 114) && 
          (s.charAt(2) == 'a') && (s.charAt(1) == 'h'))
          id = 127;
        break;
      


      case 'e': 
        int c = s.charAt(3);
        if (c == 101) {
          if ((s.charAt(2) == 's') && (s.charAt(1) == 'l')) {
            id = 113;
            break label2041;
          }
        } else if ((c == 109) && 
          (s.charAt(2) == 'u') && (s.charAt(1) == 'n'))
          id = 127;
        break;
      


      case 'g': 
        X = "goto";
        id = 127;
        break;
      case 'l': 
        X = "long";
        id = 127;
        break;
      case 'n': 
        X = "null";
        id = 42;
        break;
      case 't': 
        int c = s.charAt(3);
        if (c == 101) {
          if ((s.charAt(2) == 'u') && (s.charAt(1) == 'r')) {
            id = 45;
            break label2041;
          }
        } else if ((c == 115) && 
          (s.charAt(2) == 'i') && (s.charAt(1) == 'h'))
          id = 43;
        break;
      


      case 'v': 
        X = "void";
        id = 126;
        break;
      case 'w': 
        X = "with";
        id = 123;
      }
      
      break;
    case 5: 
      switch (s.charAt(2)) {
      case 'a': 
        X = "class";
        id = 127;
        break;
      case 'e': 
        int c = s.charAt(0);
        if (c == 98) {
          X = "break";
          id = 120;
        } else if (c == 121) {
          X = "yield";
          id = 72;
        }
        break;
      case 'i': 
        X = "while";
        id = 117;
        break;
      case 'l': 
        X = "false";
        id = 44;
        break;
      case 'n': 
        int c = s.charAt(0);
        if (c == 99) {
          X = "const";
          id = 154;
        } else if (c == 102) {
          X = "final";
          id = 127;
        }
        break;
      case 'o': 
        int c = s.charAt(0);
        if (c == 102) {
          X = "float";
          id = 127;
        } else if (c == 115) {
          X = "short";
          id = 127;
        }
        break;
      case 'p': 
        X = "super";
        id = 127;
        break;
      case 'r': 
        X = "throw";
        id = 50;
        break;
      case 't': 
        X = "catch";
        id = 124;
      }
      
      break;
    case 6: 
      switch (s.charAt(1)) {
      case 'a': 
        X = "native";
        id = 127;
        break;
      case 'e': 
        int c = s.charAt(0);
        if (c == 100) {
          X = "delete";
          id = 31;
        } else if (c == 114) {
          X = "return";
          id = 4;
        }
        break;
      case 'h': 
        X = "throws";
        id = 127;
        break;
      case 'm': 
        X = "import";
        id = 127;
        break;
      case 'o': 
        X = "double";
        id = 127;
        break;
      case 't': 
        X = "static";
        id = 127;
        break;
      case 'u': 
        X = "public";
        id = 127;
        break;
      case 'w': 
        X = "switch";
        id = 114;
        break;
      case 'x': 
        X = "export";
        id = 127;
        break;
      case 'y': 
        X = "typeof";
        id = 32;
      }
      
      break;
    case 7: 
      switch (s.charAt(1)) {
      case 'a': 
        X = "package";
        id = 127;
        break;
      case 'e': 
        X = "default";
        id = 116;
        break;
      case 'i': 
        X = "finally";
        id = 125;
        break;
      case 'o': 
        X = "boolean";
        id = 127;
        break;
      case 'r': 
        X = "private";
        id = 127;
        break;
      case 'x': 
        X = "extends";
        id = 127;
      }
      
      break;
    case 8: 
      switch (s.charAt(0)) {
      case 'a': 
        X = "abstract";
        id = 127;
        break;
      case 'c': 
        X = "continue";
        id = 121;
        break;
      case 'd': 
        X = "debugger";
        id = 160;
        break;
      case 'f': 
        X = "function";
        id = 109;
        break;
      case 'v': 
        X = "volatile";
        id = 127;
      }
      
      break;
    case 9: 
      int c = s.charAt(0);
      if (c == 105) {
        X = "interface";
        id = 127;
      } else if (c == 112) {
        X = "protected";
        id = 127;
      } else if (c == 116) {
        X = "transient";
        id = 127;
      }
      break;
    case 10: 
      int c = s.charAt(1);
      if (c == 109) {
        X = "implements";
        id = 127;
      } else if (c == 110) {
        X = "instanceof";
        id = 53;
      }
      break;
    case 12: 
      X = "synchronized";
      id = 127;
      break;
    }
    if ((X != null) && (X != s) && (!X.equals(s))) {
      id = 0;
    }
    
    label2041:
    if (id == 0) {
      return 0;
    }
    return id & 0xFF;
  }
  
  final String getSourceString() {
    return sourceString;
  }
  
  final int getLineno() {
    return lineno;
  }
  
  final String getString() {
    return string;
  }
  
  final char getQuoteChar() {
    return (char)quoteChar;
  }
  
  final double getNumber() {
    return number;
  }
  
  final boolean isNumberOctal() {
    return isOctal;
  }
  
  final boolean isNumberHex() {
    return isHex;
  }
  
  final boolean eof() {
    return hitEOF;
  }
  
  final int getToken()
    throws IOException
  {
    int c;
    do
    {
      c = getChar();
      if (c == -1) {
        tokenBeg = (cursor - 1);
        tokenEnd = cursor;
        return 0; }
      if (c == 10) {
        dirtyLine = false;
        tokenBeg = (cursor - 1);
        tokenEnd = cursor;
        return 1;
      } } while (isJSSpace(c));
    if (c != 45) {
      dirtyLine = true;
    }
    




    tokenBeg = (cursor - 1);
    tokenEnd = cursor;
    
    if (c == 64) {
      return 147;
    }
    


    boolean isUnicodeEscapeStart = false;
    boolean identifierStart; if (c == 92) {
      c = getChar();
      if (c == 117) {
        boolean identifierStart = true;
        isUnicodeEscapeStart = true;
        stringBufferTop = 0;
      } else {
        boolean identifierStart = false;
        ungetChar(c);
        c = 92;
      }
    } else {
      identifierStart = Character.isJavaIdentifierStart((char)c);
      if (identifierStart) {
        stringBufferTop = 0;
        addToString(c);
      }
    }
    
    if (identifierStart) {
      boolean containsEscape = isUnicodeEscapeStart;
      for (;;) {
        if (isUnicodeEscapeStart)
        {





          int escapeVal = 0;
          for (int i = 0; i != 4; i++) {
            c = getChar();
            escapeVal = Kit.xDigitToInt(c, escapeVal);
            
            if (escapeVal < 0) {
              break;
            }
          }
          if (escapeVal < 0) {
            parser.addError("msg.invalid.escape");
            return -1;
          }
          addToString(escapeVal);
          isUnicodeEscapeStart = false;
        } else {
          c = getChar();
          if (c == 92) {
            c = getChar();
            if (c == 117) {
              isUnicodeEscapeStart = true;
              containsEscape = true;
            } else {
              parser.addError("msg.illegal.character");
              return -1;
            }
          } else {
            if ((c == -1) || (c == 65279))
              break;
            if (!Character.isJavaIdentifierPart((char)c)) {
              break;
            }
            addToString(c);
          }
        }
      }
      ungetChar(c);
      
      String str = getStringFromBuffer();
      if (!containsEscape)
      {



        int result = stringToKeyword(str);
        if (result != 0) {
          if ((result == 153) || (result == 72))
          {
            if (parser.compilerEnv.getLanguageVersion() < 170)
            {
              string = (result == 153 ? "let" : "yield");
              result = 39;
            }
          }
          
          string = ((String)allStrings.intern(str));
          if (result != 127) {
            return result;
          }
          if (!parser.compilerEnv.isReservedKeywordAsIdentifier()) {
            return result;
          }
        }
      } else if (isKeyword(str))
      {


        str = convertLastCharToHex(str);
      }
      string = ((String)allStrings.intern(str));
      return 39;
    }
    

    if ((isDigit(c)) || ((c == 46) && (isDigit(peekChar())))) {
      isOctal = false;
      stringBufferTop = 0;
      int base = 10;
      isHex = (this.isOctal = 0);
      
      if (c == 48) {
        c = getChar();
        if ((c == 120) || (c == 88)) {
          base = 16;
          isHex = true;
          c = getChar();
        } else if (isDigit(c)) {
          base = 8;
          isOctal = true;
        } else {
          addToString(48);
        }
      }
      
      if (base == 16) {
        while (0 <= Kit.xDigitToInt(c, 0)) {
          addToString(c);
          c = getChar();
        }
      }
      while ((48 <= c) && (c <= 57))
      {





        if ((base == 8) && (c >= 56)) {
          parser.addWarning("msg.bad.octal.literal", c == 56 ? "8" : "9");
          
          base = 10;
        }
        addToString(c);
        c = getChar();
      }
      

      boolean isInteger = true;
      
      if ((base == 10) && ((c == 46) || (c == 101) || (c == 69))) {
        isInteger = false;
        if (c == 46) {
          do {
            addToString(c);
            c = getChar();
          } while (isDigit(c));
        }
        if ((c == 101) || (c == 69)) {
          addToString(c);
          c = getChar();
          if ((c == 43) || (c == 45)) {
            addToString(c);
            c = getChar();
          }
          if (!isDigit(c)) {
            parser.addError("msg.missing.exponent");
            return -1;
          }
          do {
            addToString(c);
            c = getChar();
          } while (isDigit(c));
        }
      }
      ungetChar(c);
      String numString = getStringFromBuffer();
      string = numString;
      
      double dval;
      if ((base == 10) && (!isInteger)) {
        try
        {
          dval = Double.parseDouble(numString);
        } catch (NumberFormatException ex) { double dval;
          parser.addError("msg.caught.nfe");
          return -1;
        }
      } else {
        dval = ScriptRuntime.stringToNumber(numString, 0, base);
      }
      
      number = dval;
      return 40;
    }
    

    if ((c == 34) || (c == 39))
    {




      quoteChar = c;
      stringBufferTop = 0;
      
      c = getChar(false);
      while (c != quoteChar) {
        if ((c == 10) || (c == -1)) {
          ungetChar(c);
          tokenEnd = cursor;
          parser.addError("msg.unterminated.string.lit");
          return -1;
        }
        
        if (c == 92)
        {


          c = getChar();
          switch (c) {
          case 98: 
            c = 8;
            break;
          case 102: 
            c = 12;
            break;
          case 110: 
            c = 10;
            break;
          case 114: 
            c = 13;
            break;
          case 116: 
            c = 9;
            break;
          


          case 118: 
            c = 11;
            break;
          



          case 117: 
            int escapeStart = stringBufferTop;
            addToString(117);
            int escapeVal = 0;
            for (int i = 0;; i++) { if (i == 4) break label1152;
              c = getChar();
              escapeVal = Kit.xDigitToInt(c, escapeVal);
              if (escapeVal < 0) {
                break;
              }
              addToString(c);
            }
            

            stringBufferTop = escapeStart;
            c = escapeVal;
            break;
          

          case 120: 
            c = getChar();
            int escapeVal = Kit.xDigitToInt(c, 0);
            if (escapeVal < 0) {
              addToString(120);
              continue;
            }
            int c1 = c;
            c = getChar();
            escapeVal = Kit.xDigitToInt(c, escapeVal);
            if (escapeVal < 0) {
              addToString(120);
              addToString(c1);
              continue;
            }
            
            c = escapeVal;
            

            break;
          


          case 10: 
            c = getChar();
            break;
          default: 
            label1152:
            if ((48 <= c) && (c < 56)) {
              int val = c - 48;
              c = getChar();
              if ((48 <= c) && (c < 56)) {
                val = 8 * val + c - 48;
                c = getChar();
                if ((48 <= c) && (c < 56) && (val <= 31))
                {

                  val = 8 * val + c - 48;
                  c = getChar();
                }
              }
              ungetChar(c);
              c = val;
            }
            break; }
        } else {
          addToString(c);
          c = getChar(false);
        }
      }
      String str = getStringFromBuffer();
      string = ((String)allStrings.intern(str));
      return 41;
    }
    
    switch (c) {
    case 59: 
      return 82;
    case 91: 
      return 83;
    case 93: 
      return 84;
    case 123: 
      return 85;
    case 125: 
      return 86;
    case 40: 
      return 87;
    case 41: 
      return 88;
    case 44: 
      return 89;
    case 63: 
      return 102;
    case 58: 
      if (matchChar(58)) {
        return 144;
      }
      return 103;
    
    case 46: 
      if (matchChar(46))
        return 143;
      if (matchChar(40)) {
        return 146;
      }
      return 108;
    

    case 124: 
      if (matchChar(124))
        return 104;
      if (matchChar(61)) {
        return 91;
      }
      return 9;
    

    case 94: 
      if (matchChar(61)) {
        return 92;
      }
      return 10;
    

    case 38: 
      if (matchChar(38))
        return 105;
      if (matchChar(61)) {
        return 93;
      }
      return 11;
    

    case 61: 
      if (matchChar(61)) {
        if (matchChar(61)) {
          return 46;
        }
        return 12;
      }
      
      return 90;
    

    case 33: 
      if (matchChar(61)) {
        if (matchChar(61)) {
          return 47;
        }
        return 13;
      }
      
      return 26;
    


    case 60: 
      if (matchChar(33)) {
        if (matchChar(45)) {
          if (matchChar(45)) {
            tokenBeg = (cursor - 4);
            skipLine();
            commentType = Token.CommentType.HTML;
            return 161;
          }
          ungetCharIgnoreLineEnd(45);
        }
        ungetCharIgnoreLineEnd(33);
      }
      if (matchChar(60)) {
        if (matchChar(61)) {
          return 94;
        }
        return 18;
      }
      
      if (matchChar(61)) {
        return 15;
      }
      return 14;
    


    case 62: 
      if (matchChar(62)) {
        if (matchChar(62)) {
          if (matchChar(61)) {
            return 96;
          }
          return 20;
        }
        
        if (matchChar(61)) {
          return 95;
        }
        return 19;
      }
      

      if (matchChar(61)) {
        return 17;
      }
      return 16;
    


    case 42: 
      if (matchChar(61)) {
        return 99;
      }
      return 23;
    

    case 47: 
      markCommentStart();
      
      if (matchChar(47)) {
        tokenBeg = (cursor - 2);
        skipLine();
        commentType = Token.CommentType.LINE;
        return 161;
      }
      
      if (matchChar(42)) {
        boolean lookForSlash = false;
        tokenBeg = (cursor - 2);
        if (matchChar(42)) {
          lookForSlash = true;
          commentType = Token.CommentType.JSDOC;
        } else {
          commentType = Token.CommentType.BLOCK_COMMENT;
        }
        for (;;) {
          c = getChar();
          if (c == -1) {
            tokenEnd = (cursor - 1);
            parser.addError("msg.unterminated.comment");
            return 161; }
          if (c == 42) {
            lookForSlash = true;
          } else if (c == 47) {
            if (lookForSlash) {
              tokenEnd = cursor;
              return 161;
            }
          } else {
            lookForSlash = false;
            tokenEnd = cursor;
          }
        }
      }
      
      if (matchChar(61)) {
        return 100;
      }
      return 24;
    

    case 37: 
      if (matchChar(61)) {
        return 101;
      }
      return 25;
    

    case 126: 
      return 27;
    
    case 43: 
      if (matchChar(61))
        return 97;
      if (matchChar(43)) {
        return 106;
      }
      return 21;
    

    case 45: 
      if (matchChar(61)) {
        c = 98;
      } else if (matchChar(45)) {
        if (!dirtyLine)
        {

          if (matchChar(62)) {
            markCommentStart("--");
            skipLine();
            commentType = Token.CommentType.HTML;
            return 161;
          }
        }
        c = 107;
      } else {
        c = 22;
      }
      dirtyLine = true;
      return c;
    }
    
    parser.addError("msg.illegal.character");
    return -1;
  }
  


  private static boolean isAlpha(int c)
  {
    if (c <= 90) {
      return 65 <= c;
    }
    return (97 <= c) && (c <= 122);
  }
  
  static boolean isDigit(int c)
  {
    return (48 <= c) && (c <= 57);
  }
  



  static boolean isJSSpace(int c)
  {
    if (c <= 127) {
      return (c == 32) || (c == 9) || (c == 12) || (c == 11);
    }
    return (c == 160) || (c == 65279) || 
      (Character.getType((char)c) == 12);
  }
  
  private static boolean isJSFormatChar(int c)
  {
    return (c > 127) && (Character.getType((char)c) == 16);
  }
  

  void readRegExp(int startToken)
    throws IOException
  {
    int start = tokenBeg;
    stringBufferTop = 0;
    if (startToken == 100)
    {
      addToString(61);
    }
    else if (startToken != 24) {
      Kit.codeBug();
    }
    
    boolean inCharSet = false;
    int c;
    while (((c = getChar()) != 47) || (inCharSet)) {
      if ((c == 10) || (c == -1)) {
        ungetChar(c);
        tokenEnd = (cursor - 1);
        string = new String(stringBuffer, 0, stringBufferTop);
        parser.reportError("msg.unterminated.re.lit");
        return;
      }
      if (c == 92) {
        addToString(c);
        c = getChar();
      } else if (c == 91) {
        inCharSet = true;
      } else if (c == 93) {
        inCharSet = false;
      }
      addToString(c);
    }
    int reEnd = stringBufferTop;
    for (;;)
    {
      if (matchChar(103)) {
        addToString(103);
      } else if (matchChar(105)) {
        addToString(105);
      } else if (matchChar(109)) {
        addToString(109);
      } else { if (!matchChar(121)) break;
        addToString(121);
      }
    }
    
    tokenEnd = (start + stringBufferTop + 2);
    
    if (isAlpha(peekChar())) {
      parser.reportError("msg.invalid.re.flag");
    }
    
    string = new String(stringBuffer, 0, reEnd);
    regExpFlags = new String(stringBuffer, reEnd, stringBufferTop - reEnd);
  }
  
  String readAndClearRegExpFlags()
  {
    String flags = regExpFlags;
    regExpFlags = null;
    return flags;
  }
  
  boolean isXMLAttribute() {
    return xmlIsAttribute;
  }
  
  int getFirstXMLToken() throws IOException {
    xmlOpenTagsCount = 0;
    xmlIsAttribute = false;
    xmlIsTagContent = false;
    if (!canUngetChar())
      return -1;
    ungetChar(60);
    return getNextXMLToken();
  }
  
  int getNextXMLToken() throws IOException {
    tokenBeg = cursor;
    stringBufferTop = 0;
    
    for (int c = getChar(); c != -1; c = getChar()) {
      if (xmlIsTagContent) {
        switch (c) {
        case 62: 
          addToString(c);
          xmlIsTagContent = false;
          xmlIsAttribute = false;
          break;
        case 47: 
          addToString(c);
          if (peekChar() == 62) {
            c = getChar();
            addToString(c);
            xmlIsTagContent = false;
            xmlOpenTagsCount -= 1;
          }
          break;
        case 123: 
          ungetChar(c);
          string = getStringFromBuffer();
          return 145;
        case 34: 
        case 39: 
          addToString(c);
          if (!readQuotedString(c))
            return -1;
          break;
        case 61: 
          addToString(c);
          xmlIsAttribute = true;
          break;
        case 9: 
        case 10: 
        case 13: 
        case 32: 
          addToString(c);
          break;
        default: 
          addToString(c);
          xmlIsAttribute = false;
        }
        
        
        if ((!xmlIsTagContent) && (xmlOpenTagsCount == 0)) {
          string = getStringFromBuffer();
          return 148;
        }
      } else {
        switch (c) {
        case 60: 
          addToString(c);
          c = peekChar();
          switch (c) {
          case 33: 
            c = getChar();
            addToString(c);
            c = peekChar();
            switch (c) {
            case 45: 
              c = getChar();
              addToString(c);
              c = getChar();
              if (c == 45) {
                addToString(c);
                if (!readXmlComment()) {
                  return -1;
                }
              } else {
                stringBufferTop = 0;
                string = null;
                parser.addError("msg.XML.bad.form");
                return -1;
              }
              break;
            case 91: 
              c = getChar();
              addToString(c);
              if ((getChar() == 67) && (getChar() == 68) && 
                (getChar() == 65) && (getChar() == 84) && 
                (getChar() == 65) && (getChar() == 91)) {
                addToString(67);
                addToString(68);
                addToString(65);
                addToString(84);
                addToString(65);
                addToString(91);
                if (!readCDATA()) {
                  return -1;
                }
              }
              else {
                stringBufferTop = 0;
                string = null;
                parser.addError("msg.XML.bad.form");
                return -1;
              }
              break;
            default: 
              if (!readEntity())
                return -1;
              break;
            }
            break;
          case 63: 
            c = getChar();
            addToString(c);
            if (!readPI()) {
              return -1;
            }
            break;
          case 47: 
            c = getChar();
            addToString(c);
            if (xmlOpenTagsCount == 0)
            {
              stringBufferTop = 0;
              string = null;
              parser.addError("msg.XML.bad.form");
              return -1;
            }
            xmlIsTagContent = true;
            xmlOpenTagsCount -= 1;
            break;
          
          default: 
            xmlIsTagContent = true;
            xmlOpenTagsCount += 1; }
          break;
        

        case 123: 
          ungetChar(c);
          string = getStringFromBuffer();
          return 145;
        default: 
          addToString(c);
        }
        
      }
    }
    
    tokenEnd = cursor;
    stringBufferTop = 0;
    string = null;
    parser.addError("msg.XML.bad.form");
    return -1;
  }
  

  private boolean readQuotedString(int quote)
    throws IOException
  {
    for (int c = getChar(); c != -1; c = getChar()) {
      addToString(c);
      if (c == quote) {
        return true;
      }
    }
    stringBufferTop = 0;
    string = null;
    parser.addError("msg.XML.bad.form");
    return false;
  }
  

  private boolean readXmlComment()
    throws IOException
  {
    for (int c = getChar(); c != -1;) {
      addToString(c);
      if ((c == 45) && (peekChar() == 45)) {
        c = getChar();
        addToString(c);
        if (peekChar() == 62) {
          c = getChar();
          addToString(c);
          return true;
        }
      }
      else
      {
        c = getChar();
      }
    }
    stringBufferTop = 0;
    string = null;
    parser.addError("msg.XML.bad.form");
    return false;
  }
  

  private boolean readCDATA()
    throws IOException
  {
    for (int c = getChar(); c != -1;) {
      addToString(c);
      if ((c == 93) && (peekChar() == 93)) {
        c = getChar();
        addToString(c);
        if (peekChar() == 62) {
          c = getChar();
          addToString(c);
          return true;
        }
      }
      else
      {
        c = getChar();
      }
    }
    stringBufferTop = 0;
    string = null;
    parser.addError("msg.XML.bad.form");
    return false;
  }
  

  private boolean readEntity()
    throws IOException
  {
    int declTags = 1;
    for (int c = getChar(); c != -1; c = getChar()) {
      addToString(c);
      switch (c) {
      case 60: 
        declTags++;
        break;
      case 62: 
        declTags--;
        if (declTags == 0) {
          return true;
        }
        break;
      }
    }
    stringBufferTop = 0;
    string = null;
    parser.addError("msg.XML.bad.form");
    return false;
  }
  

  private boolean readPI()
    throws IOException
  {
    for (int c = getChar(); c != -1; c = getChar()) {
      addToString(c);
      if ((c == 63) && (peekChar() == 62)) {
        c = getChar();
        addToString(c);
        return true;
      }
    }
    
    stringBufferTop = 0;
    string = null;
    parser.addError("msg.XML.bad.form");
    return false;
  }
  
  private String getStringFromBuffer() {
    tokenEnd = cursor;
    return new String(stringBuffer, 0, stringBufferTop);
  }
  
  private void addToString(int c) {
    int N = stringBufferTop;
    if (N == stringBuffer.length) {
      char[] tmp = new char[stringBuffer.length * 2];
      System.arraycopy(stringBuffer, 0, tmp, 0, N);
      stringBuffer = tmp;
    }
    stringBuffer[N] = ((char)c);
    stringBufferTop = (N + 1);
  }
  
  private boolean canUngetChar() {
    return (ungetCursor == 0) || (ungetBuffer[(ungetCursor - 1)] != 10);
  }
  
  private void ungetChar(int c)
  {
    if ((ungetCursor != 0) && (ungetBuffer[(ungetCursor - 1)] == 10))
      Kit.codeBug();
    ungetBuffer[(ungetCursor++)] = c;
    cursor -= 1;
  }
  
  private boolean matchChar(int test) throws IOException {
    int c = getCharIgnoreLineEnd();
    if (c == test) {
      tokenEnd = cursor;
      return true;
    }
    ungetCharIgnoreLineEnd(c);
    return false;
  }
  
  private int peekChar() throws IOException
  {
    int c = getChar();
    ungetChar(c);
    return c;
  }
  
  private int getChar() throws IOException {
    return getChar(true);
  }
  
  private int getChar(boolean skipFormattingChars) throws IOException {
    if (ungetCursor != 0) {
      cursor += 1;
      return ungetBuffer[(--ungetCursor)];
    }
    int c;
    label199:
    do { for (;;) { int c;
        if (sourceString != null) {
          if (sourceCursor == sourceEnd) {
            hitEOF = true;
            return -1;
          }
          cursor += 1;
          c = sourceString.charAt(sourceCursor++);
        } else {
          if ((sourceCursor == sourceEnd) && 
            (!fillSourceBuffer())) {
            hitEOF = true;
            return -1;
          }
          
          cursor += 1;
          c = sourceBuffer[(sourceCursor++)];
        }
        
        if (lineEndChar < 0) break label199;
        if ((lineEndChar != 13) || (c != 10)) break;
        lineEndChar = 10;
      }
      
      lineEndChar = -1;
      lineStart = (sourceCursor - 1);
      lineno += 1;
      

      if (c <= 127) {
        if ((c != 10) && (c != 13)) break;
        lineEndChar = c;
        c = 10; break;
      }
      
      if (c == 65279)
        return c;
    } while ((skipFormattingChars) && (isJSFormatChar(c)));
    

    if (ScriptRuntime.isJSLineTerminator(c)) {
      lineEndChar = c;
      c = 10;
    }
    
    return c;
  }
  
  private int getCharIgnoreLineEnd() throws IOException
  {
    if (ungetCursor != 0) {
      cursor += 1;
      return ungetBuffer[(--ungetCursor)];
    }
    int c;
    do {
      int c;
      if (sourceString != null) {
        if (sourceCursor == sourceEnd) {
          hitEOF = true;
          return -1;
        }
        cursor += 1;
        c = sourceString.charAt(sourceCursor++);
      } else {
        if ((sourceCursor == sourceEnd) && 
          (!fillSourceBuffer())) {
          hitEOF = true;
          return -1;
        }
        
        cursor += 1;
        c = sourceBuffer[(sourceCursor++)];
      }
      
      if (c <= 127) {
        if ((c != 10) && (c != 13)) break;
        lineEndChar = c;
        c = 10; break;
      }
      
      if (c == 65279)
        return c;
    } while (isJSFormatChar(c));
    

    if (ScriptRuntime.isJSLineTerminator(c)) {
      lineEndChar = c;
      c = 10;
    }
    
    return c;
  }
  
  private void ungetCharIgnoreLineEnd(int c)
  {
    ungetBuffer[(ungetCursor++)] = c;
    cursor -= 1;
  }
  
  private void skipLine() throws IOException
  {
    int c;
    while (((c = getChar()) != -1) && (c != 10)) {}
    
    ungetChar(c);
    tokenEnd = cursor;
  }
  


  final int getOffset()
  {
    int n = sourceCursor - lineStart;
    if (lineEndChar >= 0) {
      n--;
    }
    return n;
  }
  
  private final int charAt(int index) {
    if (index < 0) {
      return -1;
    }
    if (sourceString != null) {
      if (index >= sourceEnd) {
        return -1;
      }
      return sourceString.charAt(index);
    }
    if (index >= sourceEnd) {
      int oldSourceCursor = sourceCursor;
      try {
        if (!fillSourceBuffer()) {
          return -1;
        }
      }
      catch (IOException ioe) {
        return -1;
      }
      

      index -= oldSourceCursor - sourceCursor;
    }
    return sourceBuffer[index];
  }
  
  private final String substring(int beginIndex, int endIndex)
  {
    if (sourceString != null) {
      return sourceString.substring(beginIndex, endIndex);
    }
    int count = endIndex - beginIndex;
    return new String(sourceBuffer, beginIndex, count);
  }
  
  final String getLine()
  {
    int lineEnd = sourceCursor;
    if (lineEndChar >= 0)
    {
      lineEnd--;
      if ((lineEndChar == 10) && (charAt(lineEnd - 1) == 13)) {
        lineEnd--;
      }
    }
    else {
      int lineLength = lineEnd - lineStart;
      for (;; lineLength++) {
        int c = charAt(lineStart + lineLength);
        if ((c == -1) || (ScriptRuntime.isJSLineTerminator(c))) {
          break;
        }
      }
      lineEnd = lineStart + lineLength;
    }
    return substring(lineStart, lineEnd);
  }
  
  final String getLine(int position, int[] linep) {
    assert ((position >= 0) && (position <= cursor));
    assert (linep.length == 2);
    int delta = cursor + ungetCursor - position;
    int cur = sourceCursor;
    if (delta > cur)
    {
      return null;
    }
    
    int end = 0;int lines = 0;
    for (; delta > 0; cur--) {
      assert (cur > 0);
      int c = charAt(cur - 1);
      if (ScriptRuntime.isJSLineTerminator(c)) {
        if ((c == 10) && (charAt(cur - 2) == 13))
        {
          delta--;
          cur--;
        }
        lines++;
        end = cur - 1;
      }
      delta--;
    }
    











    int start = 0; for (int offset = 0; 
        cur > 0; offset++) {
      int c = charAt(cur - 1);
      if (ScriptRuntime.isJSLineTerminator(c)) {
        start = cur;
        break;
      }
      cur--;
    }
    




    linep[0] = (lineno - lines + (lineEndChar >= 0 ? 1 : 0));
    linep[1] = offset;
    if (lines == 0) {
      return getLine();
    }
    return substring(start, end);
  }
  
  private boolean fillSourceBuffer() throws IOException
  {
    if (sourceString != null)
      Kit.codeBug();
    if (sourceEnd == sourceBuffer.length) {
      if ((lineStart != 0) && (!isMarkingComment())) {
        System.arraycopy(sourceBuffer, lineStart, sourceBuffer, 0, sourceEnd - lineStart);
        
        sourceEnd -= lineStart;
        sourceCursor -= lineStart;
        lineStart = 0;
      } else {
        char[] tmp = new char[sourceBuffer.length * 2];
        System.arraycopy(sourceBuffer, 0, tmp, 0, sourceEnd);
        sourceBuffer = tmp;
      }
    }
    int n = sourceReader.read(sourceBuffer, sourceEnd, sourceBuffer.length - sourceEnd);
    
    if (n < 0) {
      return false;
    }
    sourceEnd += n;
    return true;
  }
  


  public int getCursor()
  {
    return cursor;
  }
  


  public int getTokenBeg()
  {
    return tokenBeg;
  }
  


  public int getTokenEnd()
  {
    return tokenEnd;
  }
  


  public int getTokenLength()
  {
    return tokenEnd - tokenBeg;
  }
  




  public Token.CommentType getCommentType()
  {
    return commentType;
  }
  
  private void markCommentStart() {
    markCommentStart("");
  }
  
  private void markCommentStart(String prefix) {
    if ((parser.compilerEnv.isRecordingComments()) && (sourceReader != null)) {
      commentPrefix = prefix;
      commentCursor = (sourceCursor - 1);
    }
  }
  
  private boolean isMarkingComment() {
    return commentCursor != -1;
  }
  
  final String getAndResetCurrentComment() {
    if (sourceString != null) {
      if (isMarkingComment())
        Kit.codeBug();
      return sourceString.substring(tokenBeg, tokenEnd);
    }
    if (!isMarkingComment())
      Kit.codeBug();
    StringBuilder comment = new StringBuilder(commentPrefix);
    comment.append(sourceBuffer, commentCursor, 
      getTokenLength() - commentPrefix.length());
    commentCursor = -1;
    return comment.toString();
  }
  
  private String convertLastCharToHex(String str)
  {
    int lastIndex = str.length() - 1;
    StringBuffer buf = new StringBuffer(str.substring(0, lastIndex));
    buf.append("\\u");
    String hexCode = Integer.toHexString(str.charAt(lastIndex));
    for (int i = 0; i < 4 - hexCode.length(); i++) {
      buf.append('0');
    }
    buf.append(hexCode);
    return buf.toString();
  }
  









  private String string = "";
  
  private double number;
  
  private boolean isOctal;
  
  private boolean isHex;
  private int quoteChar;
  private char[] stringBuffer = new char[''];
  private int stringBufferTop;
  private ObjToIntMap allStrings = new ObjToIntMap(50);
  

  private final int[] ungetBuffer = new int[3];
  
  private int ungetCursor;
  private boolean hitEOF = false;
  
  private int lineStart = 0;
  private int lineEndChar = -1;
  

  int lineno;
  
  private String sourceString;
  
  private Reader sourceReader;
  
  private char[] sourceBuffer;
  
  private int sourceEnd;
  
  int sourceCursor;
  
  int cursor;
  
  int tokenBeg;
  
  int tokenEnd;
  
  Token.CommentType commentType;
  
  private boolean xmlIsAttribute;
  
  private boolean xmlIsTagContent;
  
  private int xmlOpenTagsCount;
  
  private Parser parser;
  
  private String commentPrefix = "";
  private int commentCursor = -1;
}
