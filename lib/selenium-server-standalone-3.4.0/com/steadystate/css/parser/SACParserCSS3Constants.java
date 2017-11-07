package com.steadystate.css.parser;



public abstract interface SACParserCSS3Constants
{
  public static final int EOF = 0;
  

  public static final int S = 1;
  

  public static final int W = 2;
  

  public static final int H = 6;
  

  public static final int HNUM = 7;
  

  public static final int NONASCII = 8;
  

  public static final int UNICODE = 9;
  

  public static final int ESCAPE = 10;
  
  public static final int NMSTART = 11;
  
  public static final int NMCHAR = 12;
  
  public static final int NL = 13;
  
  public static final int STRING1 = 14;
  
  public static final int STRING2 = 15;
  
  public static final int COMMENT_ = 16;
  
  public static final int AND = 17;
  
  public static final int NOT = 18;
  
  public static final int ONLY = 19;
  
  public static final int NUMBER = 20;
  
  public static final int INHERIT = 21;
  
  public static final int IDENT = 22;
  
  public static final int NAME = 23;
  
  public static final int NUM = 24;
  
  public static final int STRING = 25;
  
  public static final int URL = 26;
  
  public static final int A_LETTER = 27;
  
  public static final int C_LETTER = 28;
  
  public static final int D_LETTER = 29;
  
  public static final int E_LETTER = 30;
  
  public static final int F_LETTER = 31;
  
  public static final int G_LETTER = 32;
  
  public static final int H_LETTER = 33;
  
  public static final int I_LETTER = 34;
  
  public static final int K_LETTER = 35;
  
  public static final int L_LETTER = 36;
  
  public static final int M_LETTER = 37;
  
  public static final int N_LETTER = 38;
  
  public static final int O_LETTER = 39;
  
  public static final int P_LETTER = 40;
  
  public static final int R_LETTER = 41;
  
  public static final int S_LETTER = 42;
  
  public static final int T_LETTER = 43;
  
  public static final int U_LETTER = 44;
  
  public static final int V_LETTER = 45;
  
  public static final int X_LETTER = 46;
  
  public static final int Z_LETTER = 47;
  
  public static final int CDO = 48;
  
  public static final int CDC = 49;
  
  public static final int INCLUDES = 50;
  
  public static final int DASHMATCH = 51;
  
  public static final int PREFIXMATCH = 52;
  
  public static final int SUFFIXMATCH = 53;
  
  public static final int SUBSTRINGMATCH = 54;
  
  public static final int LBRACE = 55;
  
  public static final int RBRACE = 56;
  
  public static final int LROUND = 57;
  
  public static final int RROUND = 58;
  
  public static final int DOT = 59;
  
  public static final int SEMICOLON = 60;
  
  public static final int COLON = 61;
  
  public static final int ASTERISK = 62;
  
  public static final int SLASH = 63;
  
  public static final int MINUS = 64;
  
  public static final int EQUALS = 65;
  
  public static final int LSQUARE = 66;
  
  public static final int RSQUARE = 67;
  
  public static final int PLUS = 68;
  
  public static final int GREATER = 69;
  
  public static final int TILDE = 70;
  
  public static final int COMMA = 71;
  
  public static final int HASH = 72;
  
  public static final int IMPORT_SYM = 73;
  
  public static final int PAGE_SYM = 74;
  
  public static final int MEDIA_SYM = 75;
  
  public static final int FONT_FACE_SYM = 76;
  
  public static final int CHARSET_SYM = 77;
  
  public static final int IMPORTANT_SYM = 78;
  
  public static final int EMS = 79;
  
  public static final int EXS = 80;
  
  public static final int LENGTH_PX = 81;
  
  public static final int LENGTH_CM = 82;
  
  public static final int LENGTH_MM = 83;
  
  public static final int LENGTH_IN = 84;
  
  public static final int LENGTH_PT = 85;
  
  public static final int LENGTH_PC = 86;
  
  public static final int ANGLE_DEG = 87;
  
  public static final int ANGLE_RAD = 88;
  
  public static final int ANGLE_GRAD = 89;
  
  public static final int TIME_MS = 90;
  
  public static final int TIME_S = 91;
  
  public static final int FREQ_HZ = 92;
  
  public static final int FREQ_KHZ = 93;
  
  public static final int RESOLUTION_DPI = 94;
  
  public static final int RESOLUTION_DPCM = 95;
  
  public static final int PERCENTAGE = 96;
  
  public static final int DIMENSION = 97;
  
  public static final int H_PLACEHOLDER = 98;
  
  public static final int UNICODE_RANGE = 99;
  
  public static final int URI = 100;
  
  public static final int FUNCTION_NOT = 101;
  
  public static final int FUNCTION_LANG = 102;
  
  public static final int FUNCTION = 103;
  
  public static final int ATKEYWORD = 104;
  
  public static final int UNKNOWN = 105;
  
  public static final int DEFAULT = 0;
  
  public static final int COMMENT = 1;
  
  public static final String[] tokenImage = { "<EOF>", "<S>", "<W>", "\"/*\"", "\"*/\"", "<token of kind 5>", "<H>", "<HNUM>", "<NONASCII>", "<UNICODE>", "<ESCAPE>", "<NMSTART>", "<NMCHAR>", "<NL>", "<STRING1>", "<STRING2>", "<COMMENT_>", "\"and\"", "\"not\"", "\"only\"", "<NUMBER>", "\"inherit\"", "<IDENT>", "<NAME>", "<NUM>", "<STRING>", "<URL>", "<A_LETTER>", "<C_LETTER>", "<D_LETTER>", "<E_LETTER>", "<F_LETTER>", "<G_LETTER>", "<H_LETTER>", "<I_LETTER>", "<K_LETTER>", "<L_LETTER>", "<M_LETTER>", "<N_LETTER>", "<O_LETTER>", "<P_LETTER>", "<R_LETTER>", "<S_LETTER>", "<T_LETTER>", "<U_LETTER>", "<V_LETTER>", "<X_LETTER>", "<Z_LETTER>", "\"<!--\"", "\"-->\"", "\"~=\"", "\"|=\"", "\"^=\"", "\"$=\"", "\"*=\"", "<LBRACE>", "\"}\"", "\"(\"", "\")\"", "\".\"", "\";\"", "\":\"", "\"*\"", "\"/\"", "\"-\"", "\"=\"", "\"[\"", "\"]\"", "<PLUS>", "<GREATER>", "\"~\"", "<COMMA>", "<HASH>", "<IMPORT_SYM>", "<PAGE_SYM>", "<MEDIA_SYM>", "<FONT_FACE_SYM>", "<CHARSET_SYM>", "<IMPORTANT_SYM>", "<EMS>", "<EXS>", "<LENGTH_PX>", "<LENGTH_CM>", "<LENGTH_MM>", "<LENGTH_IN>", "<LENGTH_PT>", "<LENGTH_PC>", "<ANGLE_DEG>", "<ANGLE_RAD>", "<ANGLE_GRAD>", "<TIME_MS>", "<TIME_S>", "<FREQ_HZ>", "<FREQ_KHZ>", "<RESOLUTION_DPI>", "<RESOLUTION_DPCM>", "<PERCENTAGE>", "<DIMENSION>", "<H_PLACEHOLDER>", "<UNICODE_RANGE>", "<URI>", "<FUNCTION_NOT>", "<FUNCTION_LANG>", "<FUNCTION>", "<ATKEYWORD>", "<UNKNOWN>", "\"progid:\"" };
}
