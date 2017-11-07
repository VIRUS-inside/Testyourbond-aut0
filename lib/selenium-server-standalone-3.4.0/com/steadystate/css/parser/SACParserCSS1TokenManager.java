package com.steadystate.css.parser;

import java.io.IOException;
import java.io.PrintStream;










public class SACParserCSS1TokenManager
  implements SACParserCSS1Constants
{
  public PrintStream debugStream = System.out;
  
  public void setDebugStream(PrintStream ds) { debugStream = ds; }
  
  private final int jjStopStringLiteralDfa_0(int pos, long active0) { switch (pos)
    {
    case 0: 
      if ((active0 & 0x20000000000) != 0L)
      {
        jjmatchedKind = 3;
        return 347;
      }
      if ((active0 & 0x2000) != 0L)
        return 348;
      if ((active0 & 0x10000000) != 0L)
        return 72;
      return -1;
    case 1: 
      if ((active0 & 0x20000000000) != 0L)
      {
        jjmatchedKind = 3;
        jjmatchedPos = 1;
        return 347;
      }
      if ((active0 & 0x10000000) != 0L)
      {
        jjmatchedKind = 30;
        jjmatchedPos = 1;
        return 349;
      }
      return -1;
    case 2: 
      if ((active0 & 0x20000000000) != 0L)
      {
        jjmatchedKind = 3;
        jjmatchedPos = 2;
        return 347;
      }
      if ((active0 & 0x10000000) != 0L)
      {
        jjmatchedKind = 30;
        jjmatchedPos = 2;
        return 349;
      }
      return -1;
    case 3: 
      if ((active0 & 0x10000000) != 0L)
      {
        jjmatchedKind = 30;
        jjmatchedPos = 3;
        return 349;
      }
      return -1;
    case 4: 
      if ((active0 & 0x10000000) != 0L)
      {
        jjmatchedKind = 30;
        jjmatchedPos = 4;
        return 349;
      }
      return -1;
    case 5: 
      if ((active0 & 0x10000000) != 0L)
      {
        jjmatchedKind = 30;
        jjmatchedPos = 5;
        return 349;
      }
      return -1;
    case 6: 
      if ((active0 & 0x10000000) != 0L)
        return 349;
      return -1;
    }
    return -1;
  }
  
  private final int jjStartNfa_0(int pos, long active0) {
    return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
  }
  
  private int jjStopAtPos(int pos, int kind) {
    jjmatchedKind = kind;
    jjmatchedPos = pos;
    return pos + 1;
  }
  
  private int jjMoveStringLiteralDfa0_0() { switch (curChar)
    {
    case 41: 
      return jjStopAtPos(0, 24);
    case 43: 
      return jjStopAtPos(0, 17);
    case 44: 
      return jjStopAtPos(0, 12);
    case 45: 
      jjmatchedKind = 18;
      return jjMoveStringLiteralDfa1_0(134217728L);
    case 46: 
      return jjStartNfaWithStates_0(0, 13, 348);
    case 47: 
      jjmatchedKind = 16;
      return jjMoveStringLiteralDfa1_0(4L);
    case 58: 
      jjmatchedKind = 15;
      return jjMoveStringLiteralDfa1_0(496L);
    case 59: 
      return jjStopAtPos(0, 14);
    case 60: 
      return jjMoveStringLiteralDfa1_0(67108864L);
    case 61: 
      return jjStopAtPos(0, 19);
    case 62: 
      return jjStopAtPos(0, 20);
    case 64: 
      return jjMoveStringLiteralDfa1_0(268435456L);
    case 91: 
      return jjStopAtPos(0, 21);
    case 93: 
      return jjStopAtPos(0, 22);
    case 82: 
    case 114: 
      return jjMoveStringLiteralDfa1_0(2199023255552L);
    case 123: 
      return jjStopAtPos(0, 10);
    case 125: 
      return jjStopAtPos(0, 11);
    }
    return jjMoveNfa_0(1, 0);
  }
  
  private int jjMoveStringLiteralDfa1_0(long active0) {
    try { curChar = input_stream.readChar();
    } catch (IOException e) {
      jjStopStringLiteralDfa_0(0, active0);
      return 1;
    }
    switch (curChar)
    {
    case 33: 
      return jjMoveStringLiteralDfa2_0(active0, 67108864L);
    case 42: 
      if ((active0 & 0x4) != 0L)
        return jjStopAtPos(1, 2);
      break;
    case 45: 
      return jjMoveStringLiteralDfa2_0(active0, 134217728L);
    case 65: 
    case 97: 
      return jjMoveStringLiteralDfa2_0(active0, 64L);
    case 70: 
    case 102: 
      return jjMoveStringLiteralDfa2_0(active0, 384L);
    case 71: 
    case 103: 
      return jjMoveStringLiteralDfa2_0(active0, 2199023255552L);
    case 73: 
    case 105: 
      return jjMoveStringLiteralDfa2_0(active0, 268435456L);
    case 76: 
    case 108: 
      return jjMoveStringLiteralDfa2_0(active0, 16L);
    case 86: 
    case 118: 
      return jjMoveStringLiteralDfa2_0(active0, 32L);
    }
    
    
    return jjStartNfa_0(0, active0);
  }
  
  private int jjMoveStringLiteralDfa2_0(long old0, long active0) { if ((active0 &= old0) == 0L)
      return jjStartNfa_0(0, old0);
    try { curChar = input_stream.readChar();
    } catch (IOException e) {
      jjStopStringLiteralDfa_0(1, active0);
      return 2;
    }
    switch (curChar)
    {
    case 45: 
      return jjMoveStringLiteralDfa3_0(active0, 67108864L);
    case 62: 
      if ((active0 & 0x8000000) != 0L) {
        return jjStopAtPos(2, 27);
      }
      break;
    case 66: case 98: 
      return jjMoveStringLiteralDfa3_0(active0, 2199023255552L);
    case 67: 
    case 99: 
      return jjMoveStringLiteralDfa3_0(active0, 64L);
    case 73: 
    case 105: 
      return jjMoveStringLiteralDfa3_0(active0, 432L);
    case 77: 
    case 109: 
      return jjMoveStringLiteralDfa3_0(active0, 268435456L);
    }
    
    
    return jjStartNfa_0(1, active0);
  }
  
  private int jjMoveStringLiteralDfa3_0(long old0, long active0) { if ((active0 &= old0) == 0L)
      return jjStartNfa_0(1, old0);
    try { curChar = input_stream.readChar();
    } catch (IOException e) {
      jjStopStringLiteralDfa_0(2, active0);
      return 3;
    }
    switch (curChar)
    {
    case 40: 
      if ((active0 & 0x20000000000) != 0L)
        return jjStopAtPos(3, 41);
      break;
    case 45: 
      if ((active0 & 0x4000000) != 0L) {
        return jjStopAtPos(3, 26);
      }
      break;
    case 78: case 110: 
      return jjMoveStringLiteralDfa4_0(active0, 16L);
    case 80: 
    case 112: 
      return jjMoveStringLiteralDfa4_0(active0, 268435456L);
    case 82: 
    case 114: 
      return jjMoveStringLiteralDfa4_0(active0, 384L);
    case 83: 
    case 115: 
      return jjMoveStringLiteralDfa4_0(active0, 32L);
    case 84: 
    case 116: 
      return jjMoveStringLiteralDfa4_0(active0, 64L);
    }
    
    
    return jjStartNfa_0(2, active0);
  }
  
  private int jjMoveStringLiteralDfa4_0(long old0, long active0) { if ((active0 &= old0) == 0L)
      return jjStartNfa_0(2, old0);
    try { curChar = input_stream.readChar();
    } catch (IOException e) {
      jjStopStringLiteralDfa_0(3, active0);
      return 4;
    }
    switch (curChar)
    {
    case 73: 
    case 105: 
      return jjMoveStringLiteralDfa5_0(active0, 96L);
    case 75: 
    case 107: 
      if ((active0 & 0x10) != 0L) {
        return jjStopAtPos(4, 4);
      }
      break;
    case 79: case 111: 
      return jjMoveStringLiteralDfa5_0(active0, 268435456L);
    case 83: 
    case 115: 
      return jjMoveStringLiteralDfa5_0(active0, 384L);
    }
    
    
    return jjStartNfa_0(3, active0);
  }
  
  private int jjMoveStringLiteralDfa5_0(long old0, long active0) { if ((active0 &= old0) == 0L)
      return jjStartNfa_0(3, old0);
    try { curChar = input_stream.readChar();
    } catch (IOException e) {
      jjStopStringLiteralDfa_0(4, active0);
      return 5;
    }
    switch (curChar)
    {
    case 82: 
    case 114: 
      return jjMoveStringLiteralDfa6_0(active0, 268435456L);
    case 84: 
    case 116: 
      return jjMoveStringLiteralDfa6_0(active0, 416L);
    case 86: 
    case 118: 
      return jjMoveStringLiteralDfa6_0(active0, 64L);
    }
    
    
    return jjStartNfa_0(4, active0);
  }
  
  private int jjMoveStringLiteralDfa6_0(long old0, long active0) { if ((active0 &= old0) == 0L)
      return jjStartNfa_0(4, old0);
    try { curChar = input_stream.readChar();
    } catch (IOException e) {
      jjStopStringLiteralDfa_0(5, active0);
      return 6;
    }
    switch (curChar)
    {
    case 45: 
      return jjMoveStringLiteralDfa7_0(active0, 384L);
    case 69: 
    case 101: 
      if ((active0 & 0x40) != 0L)
        return jjStopAtPos(6, 6);
      return jjMoveStringLiteralDfa7_0(active0, 32L);
    case 84: 
    case 116: 
      if ((active0 & 0x10000000) != 0L) {
        return jjStartNfaWithStates_0(6, 28, 349);
      }
      break;
    }
    
    return jjStartNfa_0(5, active0);
  }
  
  private int jjMoveStringLiteralDfa7_0(long old0, long active0) { if ((active0 &= old0) == 0L)
      return jjStartNfa_0(5, old0);
    try { curChar = input_stream.readChar();
    } catch (IOException e) {
      jjStopStringLiteralDfa_0(6, active0);
      return 7;
    }
    switch (curChar)
    {
    case 68: 
    case 100: 
      if ((active0 & 0x20) != 0L) {
        return jjStopAtPos(7, 5);
      }
      break;
    case 76: case 108: 
      return jjMoveStringLiteralDfa8_0(active0, 384L);
    }
    
    
    return jjStartNfa_0(6, active0);
  }
  
  private int jjMoveStringLiteralDfa8_0(long old0, long active0) { if ((active0 &= old0) == 0L)
      return jjStartNfa_0(6, old0);
    try { curChar = input_stream.readChar();
    } catch (IOException e) {
      jjStopStringLiteralDfa_0(7, active0);
      return 8;
    }
    switch (curChar)
    {
    case 69: 
    case 101: 
      return jjMoveStringLiteralDfa9_0(active0, 256L);
    case 73: 
    case 105: 
      return jjMoveStringLiteralDfa9_0(active0, 128L);
    }
    
    
    return jjStartNfa_0(7, active0);
  }
  
  private int jjMoveStringLiteralDfa9_0(long old0, long active0) { if ((active0 &= old0) == 0L)
      return jjStartNfa_0(7, old0);
    try { curChar = input_stream.readChar();
    } catch (IOException e) {
      jjStopStringLiteralDfa_0(8, active0);
      return 9;
    }
    switch (curChar)
    {
    case 78: 
    case 110: 
      return jjMoveStringLiteralDfa10_0(active0, 128L);
    case 84: 
    case 116: 
      return jjMoveStringLiteralDfa10_0(active0, 256L);
    }
    
    
    return jjStartNfa_0(8, active0);
  }
  
  private int jjMoveStringLiteralDfa10_0(long old0, long active0) { if ((active0 &= old0) == 0L)
      return jjStartNfa_0(8, old0);
    try { curChar = input_stream.readChar();
    } catch (IOException e) {
      jjStopStringLiteralDfa_0(9, active0);
      return 10;
    }
    switch (curChar)
    {
    case 69: 
    case 101: 
      if ((active0 & 0x80) != 0L) {
        return jjStopAtPos(10, 7);
      }
      break;
    case 84: case 116: 
      return jjMoveStringLiteralDfa11_0(active0, 256L);
    }
    
    
    return jjStartNfa_0(9, active0);
  }
  
  private int jjMoveStringLiteralDfa11_0(long old0, long active0) { if ((active0 &= old0) == 0L)
      return jjStartNfa_0(9, old0);
    try { curChar = input_stream.readChar();
    } catch (IOException e) {
      jjStopStringLiteralDfa_0(10, active0);
      return 11;
    }
    switch (curChar)
    {
    case 69: 
    case 101: 
      return jjMoveStringLiteralDfa12_0(active0, 256L);
    }
    
    
    return jjStartNfa_0(10, active0);
  }
  
  private int jjMoveStringLiteralDfa12_0(long old0, long active0) { if ((active0 &= old0) == 0L)
      return jjStartNfa_0(10, old0);
    try { curChar = input_stream.readChar();
    } catch (IOException e) {
      jjStopStringLiteralDfa_0(11, active0);
      return 12;
    }
    switch (curChar)
    {
    case 82: 
    case 114: 
      if ((active0 & 0x100) != 0L) {
        return jjStopAtPos(12, 8);
      }
      break;
    }
    
    return jjStartNfa_0(11, active0);
  }
  
  private int jjStartNfaWithStates_0(int pos, int kind, int state) {
    jjmatchedKind = kind;
    jjmatchedPos = pos;
    try { curChar = input_stream.readChar();
    } catch (IOException e) { return pos + 1; }
    return jjMoveNfa_0(state, pos + 1); }
  
  static final long[] jjbitVec0 = { 0L, 0L, -8589934592L, -1L };
  

  static final long[] jjbitVec1 = { -2L, -1L, -1L, -1L };
  

  static final long[] jjbitVec3 = { 0L, 0L, -1L, -1L };
  

  private int jjMoveNfa_0(int startState, int curPos)
  {
    int startsAt = 0;
    jjnewStateCnt = 347;
    int i = 1;
    jjstateSet[0] = startState;
    int kind = Integer.MAX_VALUE;
    for (;;)
    {
      if (++jjround == Integer.MAX_VALUE)
        ReInitRounds();
      if (curChar < 64)
      {
        long l = 1L << curChar;
        do
        {
          switch (jjstateSet[(--i)])
          {
          case 348: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 44)
                kind = 44;
              jjCheckNAdd(271);
            }
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 40)
                kind = 40;
              jjCheckNAdd(270);
            }
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(268, 269);
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(265, 267);
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(262, 264);
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(259, 261);
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(256, 258);
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(253, 255);
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(250, 252);
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(247, 249);
            if ((0x3FF000000000000 & l) != 0L) {
              jjCheckNAddTwoStates(244, 246);
            }
            break;
          case 73: case 349: 
            if ((0x3FF200000000000 & l) != 0L)
            {
              if (kind > 30)
                kind = 30;
              jjCheckNAddTwoStates(73, 74); }
            break;
          case 105: 
          case 347: 
            if ((0x3FF200000000000 & l) != 0L)
            {
              if (kind > 3)
                kind = 3;
              jjCheckNAddTwoStates(105, 106); }
            break;
          case 1: 
            if ((0x3FF200000000000 & l) != 0L)
            {
              if (kind > 54) {
                kind = 54;
              }
            } else if ((0x100003600 & l) != 0L)
            {
              if (kind > 1)
                kind = 1;
              jjCheckNAdd(0);
            }
            else if (curChar == 46) {
              jjCheckNAddStates(0, 10);
            } else if (curChar == 33) {
              jjCheckNAddTwoStates(61, 70);
            } else if (curChar == 39) {
              jjCheckNAddStates(11, 13);
            } else if (curChar == 34) {
              jjCheckNAddStates(14, 16);
            } else if (curChar == 35) {
              jjCheckNAddTwoStates(2, 3); }
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 40)
                kind = 40;
              jjCheckNAddStates(17, 58);
            }
            break;
          case 0: 
            if ((0x100003600 & l) != 0L)
            {
              if (kind > 1)
                kind = 1;
              jjCheckNAdd(0); }
            break;
          case 2: 
            if ((0x3FF200000000000 & l) != 0L)
            {
              if (kind > 9)
                kind = 9;
              jjCheckNAddTwoStates(2, 3); }
            break;
          case 4: 
            if ((0xFFFFFFFF00000000 & l) != 0L)
            {
              if (kind > 9)
                kind = 9;
              jjCheckNAddTwoStates(2, 3); }
            break;
          case 5: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 9)
                kind = 9;
              jjCheckNAddStates(59, 66); }
            break;
          case 6: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 9)
                kind = 9;
              jjCheckNAddStates(67, 69); }
            break;
          case 7: 
            if ((0x100003600 & l) != 0L)
            {
              if (kind > 9)
                kind = 9;
              jjCheckNAddTwoStates(2, 3); }
            break;
          case 8: 
          case 10: 
          case 13: 
          case 17: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(6);
            break;
          case 9: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 10;
            break;
          case 11: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 12;
            break;
          case 12: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 13;
            break;
          case 14: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 15;
            break;
          case 15: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 16;
            break;
          case 16: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 17;
            break;
          case 18: 
            if (curChar == 34)
              jjCheckNAddStates(14, 16);
            break;
          case 19: 
            if ((0xFFFFFFFB00000200 & l) != 0L)
              jjCheckNAddStates(14, 16);
            break;
          case 20: 
            if ((curChar == 34) && (kind > 23))
              kind = 23;
            break;
          case 22: 
            if ((0x3400 & l) != 0L)
              jjCheckNAddStates(14, 16);
            break;
          case 23: 
            if (curChar == 10)
              jjCheckNAddStates(14, 16);
            break;
          case 24: 
            if (curChar == 13)
              jjstateSet[(jjnewStateCnt++)] = 23;
            break;
          case 25: 
            if ((0xFFFFFFFF00000000 & l) != 0L)
              jjCheckNAddStates(14, 16);
            break;
          case 26: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddStates(70, 78);
            break;
          case 27: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddStates(79, 82);
            break;
          case 28: 
            if ((0x100003600 & l) != 0L) {
              jjCheckNAddStates(14, 16);
            }
            break;
          case 29: case 31: 
          case 34: 
          case 38: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(27);
            break;
          case 30: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 31;
            break;
          case 32: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 33;
            break;
          case 33: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 34;
            break;
          case 35: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 36;
            break;
          case 36: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 37;
            break;
          case 37: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 38;
            break;
          case 39: 
            if (curChar == 39)
              jjCheckNAddStates(11, 13);
            break;
          case 40: 
            if ((0xFFFFFF7F00000200 & l) != 0L)
              jjCheckNAddStates(11, 13);
            break;
          case 41: 
            if ((curChar == 39) && (kind > 23))
              kind = 23;
            break;
          case 43: 
            if ((0x3400 & l) != 0L)
              jjCheckNAddStates(11, 13);
            break;
          case 44: 
            if (curChar == 10)
              jjCheckNAddStates(11, 13);
            break;
          case 45: 
            if (curChar == 13)
              jjstateSet[(jjnewStateCnt++)] = 44;
            break;
          case 46: 
            if ((0xFFFFFFFF00000000 & l) != 0L)
              jjCheckNAddStates(11, 13);
            break;
          case 47: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddStates(83, 91);
            break;
          case 48: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddStates(92, 95);
            break;
          case 49: 
            if ((0x100003600 & l) != 0L) {
              jjCheckNAddStates(11, 13);
            }
            break;
          case 50: case 52: 
          case 55: 
          case 59: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(48);
            break;
          case 51: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 52;
            break;
          case 53: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 54;
            break;
          case 54: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 55;
            break;
          case 56: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 57;
            break;
          case 57: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 58;
            break;
          case 58: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 59;
            break;
          case 60: 
            if (curChar == 33)
              jjCheckNAddTwoStates(61, 70);
            break;
          case 61: 
            if ((0x100003600 & l) != 0L)
              jjCheckNAddTwoStates(61, 70);
            break;
          case 75: 
            if ((0xFFFFFFFF00000000 & l) != 0L)
            {
              if (kind > 30)
                kind = 30;
              jjCheckNAddTwoStates(73, 74); }
            break;
          case 76: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 30)
                kind = 30;
              jjCheckNAddStates(96, 103); }
            break;
          case 77: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 30)
                kind = 30;
              jjCheckNAddStates(104, 106); }
            break;
          case 78: 
            if ((0x100003600 & l) != 0L)
            {
              if (kind > 30)
                kind = 30;
              jjCheckNAddTwoStates(73, 74); }
            break;
          case 79: 
          case 81: 
          case 84: 
          case 88: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(77);
            break;
          case 80: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 81;
            break;
          case 82: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 83;
            break;
          case 83: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 84;
            break;
          case 85: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 86;
            break;
          case 86: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 87;
            break;
          case 87: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 88;
            break;
          case 90: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 30)
                kind = 30;
              jjCheckNAddStates(107, 114); }
            break;
          case 91: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 30)
                kind = 30;
              jjCheckNAddStates(115, 117); }
            break;
          case 92: 
          case 94: 
          case 97: 
          case 101: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(91);
            break;
          case 93: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 94;
            break;
          case 95: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 96;
            break;
          case 96: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 97;
            break;
          case 98: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 99;
            break;
          case 99: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 100;
            break;
          case 100: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 101;
            break;
          case 103: 
            if (((0x3FF200000000000 & l) != 0L) && (kind > 54))
              kind = 54;
            break;
          case 107: 
            if ((0xFFFFFFFF00000000 & l) != 0L)
            {
              if (kind > 3)
                kind = 3;
              jjCheckNAddTwoStates(105, 106); }
            break;
          case 108: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 3)
                kind = 3;
              jjCheckNAddStates(118, 125); }
            break;
          case 109: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 3)
                kind = 3;
              jjCheckNAddStates(126, 128); }
            break;
          case 110: 
            if ((0x100003600 & l) != 0L)
            {
              if (kind > 3)
                kind = 3;
              jjCheckNAddTwoStates(105, 106); }
            break;
          case 111: 
          case 113: 
          case 116: 
          case 120: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(109);
            break;
          case 112: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 113;
            break;
          case 114: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 115;
            break;
          case 115: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 116;
            break;
          case 117: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 118;
            break;
          case 118: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 119;
            break;
          case 119: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 120;
            break;
          case 122: 
            if (curChar == 40)
              jjCheckNAddStates(129, 134);
            break;
          case 123: 
            if ((0xFFFFFC7A00000000 & l) != 0L)
              jjCheckNAddStates(135, 138);
            break;
          case 124: 
            if ((0x100003600 & l) != 0L)
              jjCheckNAddTwoStates(124, 125);
            break;
          case 125: 
            if ((curChar == 41) && (kind > 25))
              kind = 25;
            break;
          case 127: 
            if ((0xFFFFFFFF00000000 & l) != 0L)
              jjCheckNAddStates(135, 138);
            break;
          case 128: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddStates(139, 147);
            break;
          case 129: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddStates(148, 151);
            break;
          case 130: 
            if ((0x100003600 & l) != 0L) {
              jjCheckNAddStates(135, 138);
            }
            break;
          case 131: case 133: 
          case 136: 
          case 140: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(129);
            break;
          case 132: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 133;
            break;
          case 134: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 135;
            break;
          case 135: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 136;
            break;
          case 137: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 138;
            break;
          case 138: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 139;
            break;
          case 139: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 140;
            break;
          case 141: 
            if (curChar == 39)
              jjCheckNAddStates(152, 154);
            break;
          case 142: 
            if ((0xFFFFFF7F00000200 & l) != 0L)
              jjCheckNAddStates(152, 154);
            break;
          case 143: 
            if (curChar == 39)
              jjCheckNAddTwoStates(124, 125);
            break;
          case 145: 
            if ((0x3400 & l) != 0L)
              jjCheckNAddStates(152, 154);
            break;
          case 146: 
            if (curChar == 10)
              jjCheckNAddStates(152, 154);
            break;
          case 147: 
            if (curChar == 13)
              jjstateSet[(jjnewStateCnt++)] = 146;
            break;
          case 148: 
            if ((0xFFFFFFFF00000000 & l) != 0L)
              jjCheckNAddStates(152, 154);
            break;
          case 149: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddStates(155, 163);
            break;
          case 150: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddStates(164, 167);
            break;
          case 151: 
            if ((0x100003600 & l) != 0L) {
              jjCheckNAddStates(152, 154);
            }
            break;
          case 152: case 154: 
          case 157: 
          case 161: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(150);
            break;
          case 153: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 154;
            break;
          case 155: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 156;
            break;
          case 156: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 157;
            break;
          case 158: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 159;
            break;
          case 159: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 160;
            break;
          case 160: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 161;
            break;
          case 162: 
            if (curChar == 34)
              jjCheckNAddStates(168, 170);
            break;
          case 163: 
            if ((0xFFFFFFFB00000200 & l) != 0L)
              jjCheckNAddStates(168, 170);
            break;
          case 164: 
            if (curChar == 34)
              jjCheckNAddTwoStates(124, 125);
            break;
          case 166: 
            if ((0x3400 & l) != 0L)
              jjCheckNAddStates(168, 170);
            break;
          case 167: 
            if (curChar == 10)
              jjCheckNAddStates(168, 170);
            break;
          case 168: 
            if (curChar == 13)
              jjstateSet[(jjnewStateCnt++)] = 167;
            break;
          case 169: 
            if ((0xFFFFFFFF00000000 & l) != 0L)
              jjCheckNAddStates(168, 170);
            break;
          case 170: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddStates(171, 179);
            break;
          case 171: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddStates(180, 183);
            break;
          case 172: 
            if ((0x100003600 & l) != 0L) {
              jjCheckNAddStates(168, 170);
            }
            break;
          case 173: case 175: 
          case 178: 
          case 182: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(171);
            break;
          case 174: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 175;
            break;
          case 176: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 177;
            break;
          case 177: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 178;
            break;
          case 179: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 180;
            break;
          case 180: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 181;
            break;
          case 181: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 182;
            break;
          case 183: 
            if ((0x100003600 & l) != 0L)
              jjCheckNAddStates(184, 190);
            break;
          case 186: 
            if (curChar == 43) {
              jjCheckNAddStates(191, 193);
            }
            break;
          case 187: case 216: 
            if ((curChar == 63) && (kind > 45))
              kind = 45;
            break;
          case 188: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 45)
                kind = 45;
              jjCheckNAddStates(194, 202); }
            break;
          case 189: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(190);
            break;
          case 190: 
            if (curChar == 45)
              jjstateSet[(jjnewStateCnt++)] = 191;
            break;
          case 191: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 45)
                kind = 45;
              jjCheckNAddStates(203, 207); }
            break;
          case 192: 
            if (((0x3FF000000000000 & l) != 0L) && (kind > 45)) {
              kind = 45;
            }
            break;
          case 193: case 195: 
          case 198: 
          case 202: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(192);
            break;
          case 194: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 195;
            break;
          case 196: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 197;
            break;
          case 197: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 198;
            break;
          case 199: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 200;
            break;
          case 200: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 201;
            break;
          case 201: 
            if ((0x3FF000000000000 & l) != 0L) {
              jjstateSet[(jjnewStateCnt++)] = 202;
            }
            break;
          case 203: case 205: 
          case 208: 
          case 212: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(189);
            break;
          case 204: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 205;
            break;
          case 206: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 207;
            break;
          case 207: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 208;
            break;
          case 209: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 210;
            break;
          case 210: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 211;
            break;
          case 211: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 212;
            break;
          case 213: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 45)
                kind = 45;
              jjCheckNAddStates(208, 210); }
            break;
          case 214: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 45)
                kind = 45;
              jjCheckNAddStates(211, 213); }
            break;
          case 215: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 45)
                kind = 45;
              jjCheckNAddStates(214, 216); }
            break;
          case 217: 
          case 220: 
          case 222: 
          case 223: 
          case 226: 
          case 227: 
          case 229: 
          case 233: 
          case 237: 
          case 240: 
          case 242: 
            if (curChar == 63)
              jjCheckNAdd(216);
            break;
          case 218: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 45)
                kind = 45;
              jjCheckNAddTwoStates(187, 192); }
            break;
          case 219: 
            if (curChar == 63)
              jjCheckNAddTwoStates(216, 220);
            break;
          case 221: 
            if (curChar == 63)
              jjCheckNAddStates(217, 219);
            break;
          case 224: 
            if (curChar == 63)
              jjstateSet[(jjnewStateCnt++)] = 223;
            break;
          case 225: 
            if (curChar == 63)
              jjCheckNAddStates(220, 223);
            break;
          case 228: 
            if (curChar == 63)
              jjstateSet[(jjnewStateCnt++)] = 227;
            break;
          case 230: 
            if (curChar == 63)
              jjstateSet[(jjnewStateCnt++)] = 229;
            break;
          case 231: 
            if (curChar == 63)
              jjstateSet[(jjnewStateCnt++)] = 230;
            break;
          case 232: 
            if (curChar == 63)
              jjCheckNAddStates(224, 228);
            break;
          case 234: 
            if (curChar == 63)
              jjstateSet[(jjnewStateCnt++)] = 233;
            break;
          case 235: 
            if (curChar == 63)
              jjstateSet[(jjnewStateCnt++)] = 234;
            break;
          case 236: 
            if (curChar == 63)
              jjstateSet[(jjnewStateCnt++)] = 235;
            break;
          case 238: 
            if (curChar == 63)
              jjstateSet[(jjnewStateCnt++)] = 237;
            break;
          case 239: 
            if (curChar == 63)
              jjstateSet[(jjnewStateCnt++)] = 238;
            break;
          case 241: 
            if (curChar == 63)
              jjstateSet[(jjnewStateCnt++)] = 240;
            break;
          case 243: 
            if (curChar == 46)
              jjCheckNAddStates(0, 10);
            break;
          case 244: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(244, 246);
            break;
          case 247: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(247, 249);
            break;
          case 250: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(250, 252);
            break;
          case 253: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(253, 255);
            break;
          case 256: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(256, 258);
            break;
          case 259: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(259, 261);
            break;
          case 262: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(262, 264);
            break;
          case 265: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(265, 267);
            break;
          case 268: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(268, 269);
            break;
          case 269: 
            if ((curChar == 37) && (kind > 39))
              kind = 39;
            break;
          case 270: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 40)
                kind = 40;
              jjCheckNAdd(270); }
            break;
          case 271: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 44)
                kind = 44;
              jjCheckNAdd(271); }
            break;
          case 273: 
            if (((0xFFFFFFFF00000000 & l) != 0L) && (kind > 54))
              kind = 54;
            break;
          case 274: 
            if (((0xFFFFFFFF00000000 & l) != 0L) && (kind > 55))
              kind = 55;
            break;
          case 275: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 55)
                kind = 55;
              jjCheckNAddStates(229, 234); }
            break;
          case 276: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 55)
                kind = 55;
              jjCheckNAdd(277); }
            break;
          case 277: 
            if (((0x100003600 & l) != 0L) && (kind > 55)) {
              kind = 55;
            }
            break;
          case 278: case 280: 
          case 283: 
          case 287: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(276);
            break;
          case 279: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 280;
            break;
          case 281: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 282;
            break;
          case 282: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 283;
            break;
          case 284: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 285;
            break;
          case 285: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 286;
            break;
          case 286: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 287;
            break;
          case 288: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 54)
                kind = 54;
              jjCheckNAddStates(235, 240); }
            break;
          case 289: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 54)
                kind = 54;
              jjCheckNAdd(290); }
            break;
          case 290: 
            if (((0x100003600 & l) != 0L) && (kind > 54)) {
              kind = 54;
            }
            break;
          case 291: case 293: 
          case 296: 
          case 300: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(289);
            break;
          case 292: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 293;
            break;
          case 294: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 295;
            break;
          case 295: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 296;
            break;
          case 297: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 298;
            break;
          case 298: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 299;
            break;
          case 299: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 300;
            break;
          case 301: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 3)
                kind = 3;
              jjCheckNAddStates(241, 248); }
            break;
          case 302: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 3)
                kind = 3;
              jjCheckNAddStates(249, 251); }
            break;
          case 303: 
          case 305: 
          case 308: 
          case 312: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(302);
            break;
          case 304: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 305;
            break;
          case 306: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 307;
            break;
          case 307: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 308;
            break;
          case 309: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 310;
            break;
          case 310: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 311;
            break;
          case 311: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 312;
            break;
          case 313: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 40)
                kind = 40;
              jjCheckNAddStates(17, 58); }
            break;
          case 314: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(314, 246);
            break;
          case 315: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(315, 316);
            break;
          case 316: 
            if (curChar == 46)
              jjCheckNAdd(244);
            break;
          case 317: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(317, 249);
            break;
          case 318: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(318, 319);
            break;
          case 319: 
            if (curChar == 46)
              jjCheckNAdd(247);
            break;
          case 320: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(320, 252);
            break;
          case 321: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(321, 322);
            break;
          case 322: 
            if (curChar == 46)
              jjCheckNAdd(250);
            break;
          case 323: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(323, 255);
            break;
          case 324: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(324, 325);
            break;
          case 325: 
            if (curChar == 46)
              jjCheckNAdd(253);
            break;
          case 326: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(326, 258);
            break;
          case 327: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(327, 328);
            break;
          case 328: 
            if (curChar == 46)
              jjCheckNAdd(256);
            break;
          case 329: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(329, 261);
            break;
          case 330: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(330, 331);
            break;
          case 331: 
            if (curChar == 46)
              jjCheckNAdd(259);
            break;
          case 332: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(332, 264);
            break;
          case 333: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(333, 334);
            break;
          case 334: 
            if (curChar == 46)
              jjCheckNAdd(262);
            break;
          case 335: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(335, 267);
            break;
          case 336: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(336, 337);
            break;
          case 337: 
            if (curChar == 46)
              jjCheckNAdd(265);
            break;
          case 338: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(338, 269);
            break;
          case 339: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(339, 340);
            break;
          case 340: 
            if (curChar == 46)
              jjCheckNAdd(268);
            break;
          case 341: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 40)
                kind = 40;
              jjCheckNAdd(341); }
            break;
          case 342: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(342, 343);
            break;
          case 343: 
            if (curChar == 46)
              jjCheckNAdd(270);
            break;
          case 344: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 44)
                kind = 44;
              jjCheckNAdd(344); }
            break;
          case 345: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(345, 346);
            break;
          case 346: 
            if (curChar == 46) {
              jjCheckNAdd(271);
            }
            break;
          }
        } while (i != startsAt);
      }
      else if (curChar < 128)
      {
        long l = 1L << (curChar & 0x3F);
        do
        {
          switch (jjstateSet[(--i)])
          {
          case 72: 
            if ((0x7FFFFFE07FFFFFE & l) != 0L)
            {
              if (kind > 30)
                kind = 30;
              jjCheckNAddTwoStates(73, 74);
            }
            else if (curChar == 92) {
              jjCheckNAddTwoStates(75, 90);
            }
            break;
          case 349:  if ((0x7FFFFFE07FFFFFE & l) != 0L)
            {
              if (kind > 30)
                kind = 30;
              jjCheckNAddTwoStates(73, 74);
            }
            else if (curChar == 92) {
              jjCheckNAddTwoStates(75, 76);
            }
            break;
          case 347:  if ((0x7FFFFFE07FFFFFE & l) != 0L)
            {
              if (kind > 3)
                kind = 3;
              jjCheckNAddTwoStates(105, 106);
            }
            else if (curChar == 92) {
              jjCheckNAddTwoStates(107, 108);
            }
            break;
          case 1:  if ((0x7FFFFFE07FFFFFE & l) != 0L)
            {
              if (kind > 3)
                kind = 3;
              jjCheckNAddTwoStates(105, 106);
            }
            else if (curChar == 92) {
              jjCheckNAddStates(252, 257);
            } else if (curChar == 64) {
              jjAddStates(258, 259); }
            if ((0x7FFFFFE07FFFFFE & l) != 0L)
            {
              if (kind > 54)
                kind = 54;
            }
            if ((0x20000000200000 & l) != 0L)
              jjAddStates(260, 261);
            break;
          case 2: 
            if ((0x7FFFFFE07FFFFFE & l) != 0L)
            {
              if (kind > 9)
                kind = 9;
              jjCheckNAddTwoStates(2, 3); }
            break;
          case 3: 
            if (curChar == 92)
              jjAddStates(262, 263);
            break;
          case 4: 
            if ((0x7FFFFFFFFFFFFFFF & l) != 0L)
            {
              if (kind > 9)
                kind = 9;
              jjCheckNAddTwoStates(2, 3); }
            break;
          case 5: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 9)
                kind = 9;
              jjCheckNAddStates(59, 66); }
            break;
          case 6: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 9)
                kind = 9;
              jjCheckNAddStates(67, 69); }
            break;
          case 8: 
          case 10: 
          case 13: 
          case 17: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(6);
            break;
          case 9: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 10;
            break;
          case 11: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 12;
            break;
          case 12: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 13;
            break;
          case 14: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 15;
            break;
          case 15: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 16;
            break;
          case 16: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 17;
            break;
          case 19: 
            if ((0x7FFFFFFFEFFFFFFF & l) != 0L)
              jjCheckNAddStates(14, 16);
            break;
          case 21: 
            if (curChar == 92)
              jjAddStates(264, 267);
            break;
          case 25: 
            if ((0x7FFFFFFFFFFFFFFF & l) != 0L)
              jjCheckNAddStates(14, 16);
            break;
          case 26: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAddStates(70, 78);
            break;
          case 27: 
            if ((0x7E0000007E & l) != 0L) {
              jjCheckNAddStates(79, 82);
            }
            break;
          case 29: case 31: 
          case 34: 
          case 38: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(27);
            break;
          case 30: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 31;
            break;
          case 32: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 33;
            break;
          case 33: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 34;
            break;
          case 35: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 36;
            break;
          case 36: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 37;
            break;
          case 37: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 38;
            break;
          case 40: 
            if ((0x7FFFFFFFEFFFFFFF & l) != 0L)
              jjCheckNAddStates(11, 13);
            break;
          case 42: 
            if (curChar == 92)
              jjAddStates(268, 271);
            break;
          case 46: 
            if ((0x7FFFFFFFFFFFFFFF & l) != 0L)
              jjCheckNAddStates(11, 13);
            break;
          case 47: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAddStates(83, 91);
            break;
          case 48: 
            if ((0x7E0000007E & l) != 0L) {
              jjCheckNAddStates(92, 95);
            }
            break;
          case 50: case 52: 
          case 55: 
          case 59: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(48);
            break;
          case 51: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 52;
            break;
          case 53: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 54;
            break;
          case 54: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 55;
            break;
          case 56: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 57;
            break;
          case 57: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 58;
            break;
          case 58: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 59;
            break;
          case 62: 
            if (((0x10000000100000 & l) != 0L) && (kind > 29))
              kind = 29;
            break;
          case 63: 
            if ((0x400000004000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 62;
            break;
          case 64: 
            if ((0x200000002 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 63;
            break;
          case 65: 
            if ((0x10000000100000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 64;
            break;
          case 66: 
            if ((0x4000000040000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 65;
            break;
          case 67: 
            if ((0x800000008000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 66;
            break;
          case 68: 
            if ((0x1000000010000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 67;
            break;
          case 69: 
            if ((0x200000002000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 68;
            break;
          case 70: 
            if ((0x20000000200 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 69;
            break;
          case 71: 
            if (curChar == 64)
              jjAddStates(258, 259);
            break;
          case 73: 
            if ((0x7FFFFFE07FFFFFE & l) != 0L)
            {
              if (kind > 30)
                kind = 30;
              jjCheckNAddTwoStates(73, 74); }
            break;
          case 74: 
            if (curChar == 92)
              jjCheckNAddTwoStates(75, 76);
            break;
          case 75: 
            if ((0x7FFFFFFFFFFFFFFF & l) != 0L)
            {
              if (kind > 30)
                kind = 30;
              jjCheckNAddTwoStates(73, 74); }
            break;
          case 76: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 30)
                kind = 30;
              jjCheckNAddStates(96, 103); }
            break;
          case 77: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 30)
                kind = 30;
              jjCheckNAddStates(104, 106); }
            break;
          case 79: 
          case 81: 
          case 84: 
          case 88: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(77);
            break;
          case 80: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 81;
            break;
          case 82: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 83;
            break;
          case 83: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 84;
            break;
          case 85: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 86;
            break;
          case 86: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 87;
            break;
          case 87: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 88;
            break;
          case 89: 
            if (curChar == 92)
              jjCheckNAddTwoStates(75, 90);
            break;
          case 90: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 30)
                kind = 30;
              jjCheckNAddStates(107, 114); }
            break;
          case 91: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 30)
                kind = 30;
              jjCheckNAddStates(115, 117); }
            break;
          case 92: 
          case 94: 
          case 97: 
          case 101: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(91);
            break;
          case 93: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 94;
            break;
          case 95: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 96;
            break;
          case 96: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 97;
            break;
          case 98: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 99;
            break;
          case 99: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 100;
            break;
          case 100: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 101;
            break;
          case 103: 
            if (((0x7FFFFFE07FFFFFE & l) != 0L) && (kind > 54))
              kind = 54;
            break;
          case 104: 
            if ((0x7FFFFFE07FFFFFE & l) != 0L)
            {
              if (kind > 3)
                kind = 3;
              jjCheckNAddTwoStates(105, 106); }
            break;
          case 105: 
            if ((0x7FFFFFE07FFFFFE & l) != 0L)
            {
              if (kind > 3)
                kind = 3;
              jjCheckNAddTwoStates(105, 106); }
            break;
          case 106: 
            if (curChar == 92)
              jjCheckNAddTwoStates(107, 108);
            break;
          case 107: 
            if ((0x7FFFFFFFFFFFFFFF & l) != 0L)
            {
              if (kind > 3)
                kind = 3;
              jjCheckNAddTwoStates(105, 106); }
            break;
          case 108: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 3)
                kind = 3;
              jjCheckNAddStates(118, 125); }
            break;
          case 109: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 3)
                kind = 3;
              jjCheckNAddStates(126, 128); }
            break;
          case 111: 
          case 113: 
          case 116: 
          case 120: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(109);
            break;
          case 112: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 113;
            break;
          case 114: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 115;
            break;
          case 115: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 116;
            break;
          case 117: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 118;
            break;
          case 118: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 119;
            break;
          case 119: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 120;
            break;
          case 121: 
            if ((0x20000000200000 & l) != 0L)
              jjAddStates(260, 261);
            break;
          case 123: 
            if ((0x7FFFFFFFEFFFFFFF & l) != 0L)
              jjCheckNAddStates(135, 138);
            break;
          case 126: 
            if (curChar == 92)
              jjAddStates(272, 273);
            break;
          case 127: 
            if ((0x7FFFFFFFFFFFFFFF & l) != 0L)
              jjCheckNAddStates(135, 138);
            break;
          case 128: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAddStates(139, 147);
            break;
          case 129: 
            if ((0x7E0000007E & l) != 0L) {
              jjCheckNAddStates(148, 151);
            }
            break;
          case 131: case 133: 
          case 136: 
          case 140: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(129);
            break;
          case 132: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 133;
            break;
          case 134: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 135;
            break;
          case 135: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 136;
            break;
          case 137: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 138;
            break;
          case 138: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 139;
            break;
          case 139: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 140;
            break;
          case 142: 
            if ((0x7FFFFFFFEFFFFFFF & l) != 0L)
              jjCheckNAddStates(152, 154);
            break;
          case 144: 
            if (curChar == 92)
              jjAddStates(274, 277);
            break;
          case 148: 
            if ((0x7FFFFFFFFFFFFFFF & l) != 0L)
              jjCheckNAddStates(152, 154);
            break;
          case 149: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAddStates(155, 163);
            break;
          case 150: 
            if ((0x7E0000007E & l) != 0L) {
              jjCheckNAddStates(164, 167);
            }
            break;
          case 152: case 154: 
          case 157: 
          case 161: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(150);
            break;
          case 153: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 154;
            break;
          case 155: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 156;
            break;
          case 156: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 157;
            break;
          case 158: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 159;
            break;
          case 159: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 160;
            break;
          case 160: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 161;
            break;
          case 163: 
            if ((0x7FFFFFFFEFFFFFFF & l) != 0L)
              jjCheckNAddStates(168, 170);
            break;
          case 165: 
            if (curChar == 92)
              jjAddStates(278, 281);
            break;
          case 169: 
            if ((0x7FFFFFFFFFFFFFFF & l) != 0L)
              jjCheckNAddStates(168, 170);
            break;
          case 170: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAddStates(171, 179);
            break;
          case 171: 
            if ((0x7E0000007E & l) != 0L) {
              jjCheckNAddStates(180, 183);
            }
            break;
          case 173: case 175: 
          case 178: 
          case 182: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(171);
            break;
          case 174: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 175;
            break;
          case 176: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 177;
            break;
          case 177: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 178;
            break;
          case 179: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 180;
            break;
          case 180: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 181;
            break;
          case 181: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 182;
            break;
          case 184: 
            if ((0x100000001000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 122;
            break;
          case 185: 
            if ((0x4000000040000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 184;
            break;
          case 188: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 45)
                kind = 45;
              jjCheckNAddStates(194, 202); }
            break;
          case 189: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(190);
            break;
          case 191: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 45)
                kind = 45;
              jjCheckNAddStates(203, 207); }
            break;
          case 192: 
            if (((0x7E0000007E & l) != 0L) && (kind > 45)) {
              kind = 45;
            }
            break;
          case 193: case 195: 
          case 198: 
          case 202: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(192);
            break;
          case 194: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 195;
            break;
          case 196: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 197;
            break;
          case 197: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 198;
            break;
          case 199: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 200;
            break;
          case 200: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 201;
            break;
          case 201: 
            if ((0x7E0000007E & l) != 0L) {
              jjstateSet[(jjnewStateCnt++)] = 202;
            }
            break;
          case 203: case 205: 
          case 208: 
          case 212: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(189);
            break;
          case 204: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 205;
            break;
          case 206: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 207;
            break;
          case 207: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 208;
            break;
          case 209: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 210;
            break;
          case 210: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 211;
            break;
          case 211: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 212;
            break;
          case 213: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 45)
                kind = 45;
              jjCheckNAddStates(208, 210); }
            break;
          case 214: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 45)
                kind = 45;
              jjCheckNAddStates(211, 213); }
            break;
          case 215: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 45)
                kind = 45;
              jjCheckNAddStates(214, 216); }
            break;
          case 218: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 45)
                kind = 45;
              jjCheckNAddTwoStates(187, 192); }
            break;
          case 245: 
            if (((0x200000002000 & l) != 0L) && (kind > 31))
              kind = 31;
            break;
          case 246: 
            if ((0x2000000020 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 245;
            break;
          case 248: 
            if (((0x100000001000000 & l) != 0L) && (kind > 32))
              kind = 32;
            break;
          case 249: 
            if ((0x2000000020 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 248;
            break;
          case 251: 
            if (((0x100000001000000 & l) != 0L) && (kind > 33))
              kind = 33;
            break;
          case 252: 
            if ((0x1000000010000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 251;
            break;
          case 254: 
            if (((0x200000002000 & l) != 0L) && (kind > 34))
              kind = 34;
            break;
          case 255: 
            if ((0x800000008 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 254;
            break;
          case 257: 
            if (((0x200000002000 & l) != 0L) && (kind > 35))
              kind = 35;
            break;
          case 258: 
            if ((0x200000002000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 257;
            break;
          case 260: 
            if (((0x400000004000 & l) != 0L) && (kind > 36))
              kind = 36;
            break;
          case 261: 
            if ((0x20000000200 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 260;
            break;
          case 263: 
            if (((0x10000000100000 & l) != 0L) && (kind > 37))
              kind = 37;
            break;
          case 264: 
            if ((0x1000000010000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 263;
            break;
          case 266: 
            if (((0x800000008 & l) != 0L) && (kind > 38))
              kind = 38;
            break;
          case 267: 
            if ((0x1000000010000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 266;
            break;
          case 272: 
            if (curChar == 92)
              jjCheckNAddStates(252, 257);
            break;
          case 273: 
            if (((0x7FFFFFFFFFFFFFFF & l) != 0L) && (kind > 54))
              kind = 54;
            break;
          case 274: 
            if (((0x7FFFFFFFFFFFFFFF & l) != 0L) && (kind > 55))
              kind = 55;
            break;
          case 275: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 55)
                kind = 55;
              jjCheckNAddStates(229, 234); }
            break;
          case 276: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 55)
                kind = 55;
              jjCheckNAdd(277); }
            break;
          case 278: 
          case 280: 
          case 283: 
          case 287: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(276);
            break;
          case 279: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 280;
            break;
          case 281: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 282;
            break;
          case 282: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 283;
            break;
          case 284: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 285;
            break;
          case 285: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 286;
            break;
          case 286: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 287;
            break;
          case 288: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 54)
                kind = 54;
              jjCheckNAddStates(235, 240); }
            break;
          case 289: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 54)
                kind = 54;
              jjCheckNAdd(290); }
            break;
          case 291: 
          case 293: 
          case 296: 
          case 300: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(289);
            break;
          case 292: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 293;
            break;
          case 294: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 295;
            break;
          case 295: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 296;
            break;
          case 297: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 298;
            break;
          case 298: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 299;
            break;
          case 299: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 300;
            break;
          case 301: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 3)
                kind = 3;
              jjCheckNAddStates(241, 248); }
            break;
          case 302: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 3)
                kind = 3;
              jjCheckNAddStates(249, 251); }
            break;
          case 303: 
          case 305: 
          case 308: 
          case 312: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(302);
            break;
          case 304: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 305;
            break;
          case 306: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 307;
            break;
          case 307: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 308;
            break;
          case 309: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 310;
            break;
          case 310: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 311;
            break;
          case 311: 
            if ((0x7E0000007E & l) != 0L) {
              jjstateSet[(jjnewStateCnt++)] = 312;
            }
            break;
          }
        } while (i != startsAt);
      }
      else
      {
        int hiByte = curChar >> 8;
        int i1 = hiByte >> 6;
        long l1 = 1L << (hiByte & 0x3F);
        int i2 = (curChar & 0xFF) >> 6;
        long l2 = 1L << (curChar & 0x3F);
        do
        {
          switch (jjstateSet[(--i)])
          {
          case 72: 
          case 75: 
            if (jjCanMove_0(hiByte, i1, i2, l1, l2))
            {
              if (kind > 30)
                kind = 30;
              jjCheckNAddTwoStates(73, 74); }
            break;
          case 73: 
          case 349: 
            if (jjCanMove_0(hiByte, i1, i2, l1, l2))
            {
              if (kind > 30)
                kind = 30;
              jjCheckNAddTwoStates(73, 74); }
            break;
          case 105: 
          case 107: 
          case 347: 
            if (jjCanMove_0(hiByte, i1, i2, l1, l2))
            {
              if (kind > 3)
                kind = 3;
              jjCheckNAddTwoStates(105, 106); }
            break;
          case 1: 
            if (jjCanMove_0(hiByte, i1, i2, l1, l2))
            {
              if (kind > 53)
                kind = 53;
            }
            if (jjCanMove_0(hiByte, i1, i2, l1, l2))
            {
              if (kind > 54)
                kind = 54;
            }
            if (jjCanMove_0(hiByte, i1, i2, l1, l2))
            {
              if (kind > 3)
                kind = 3;
              jjCheckNAddTwoStates(105, 106);
            }
            break;
          case 2: 
          case 4: 
            if (jjCanMove_0(hiByte, i1, i2, l1, l2))
            {
              if (kind > 9)
                kind = 9;
              jjCheckNAddTwoStates(2, 3); }
            break;
          case 19: 
            if (jjCanMove_1(hiByte, i1, i2, l1, l2))
              jjCheckNAddStates(14, 16);
            break;
          case 25: 
            if (jjCanMove_0(hiByte, i1, i2, l1, l2))
              jjCheckNAddStates(14, 16);
            break;
          case 40: 
            if (jjCanMove_1(hiByte, i1, i2, l1, l2))
              jjCheckNAddStates(11, 13);
            break;
          case 46: 
            if (jjCanMove_0(hiByte, i1, i2, l1, l2))
              jjCheckNAddStates(11, 13);
            break;
          case 102: 
            if ((jjCanMove_0(hiByte, i1, i2, l1, l2)) && (kind > 53)) {
              kind = 53;
            }
            break;
          case 103: case 273: 
            if ((jjCanMove_0(hiByte, i1, i2, l1, l2)) && (kind > 54))
              kind = 54;
            break;
          case 104: 
            if (jjCanMove_0(hiByte, i1, i2, l1, l2))
            {
              if (kind > 3)
                kind = 3;
              jjCheckNAddTwoStates(105, 106); }
            break;
          case 123: 
            if (jjCanMove_1(hiByte, i1, i2, l1, l2))
              jjCheckNAddStates(135, 138);
            break;
          case 127: 
            if (jjCanMove_0(hiByte, i1, i2, l1, l2))
              jjCheckNAddStates(135, 138);
            break;
          case 142: 
            if (jjCanMove_1(hiByte, i1, i2, l1, l2))
              jjCheckNAddStates(152, 154);
            break;
          case 148: 
            if (jjCanMove_0(hiByte, i1, i2, l1, l2))
              jjCheckNAddStates(152, 154);
            break;
          case 163: 
            if (jjCanMove_1(hiByte, i1, i2, l1, l2))
              jjCheckNAddStates(168, 170);
            break;
          case 169: 
            if (jjCanMove_0(hiByte, i1, i2, l1, l2))
              jjCheckNAddStates(168, 170);
            break;
          case 274: 
            if ((jjCanMove_0(hiByte, i1, i2, l1, l2)) && (kind > 55))
              kind = 55;
            break;
          default:  if ((i1 == 0) || (l1 == 0L) || (i2 == 0) || (l2 != 0L))
              break; }
        } while (i != startsAt);
      }
      if (kind != Integer.MAX_VALUE)
      {
        jjmatchedKind = kind;
        jjmatchedPos = curPos;
        kind = Integer.MAX_VALUE;
      }
      curPos++;
      if ((i = jjnewStateCnt) == (startsAt = 347 - (this.jjnewStateCnt = startsAt)))
        return curPos;
      try { curChar = input_stream.readChar(); } catch (IOException e) {} }
    return curPos;
  }
  
  private int jjMoveStringLiteralDfa0_1() {
    switch (curChar)
    {
    case 42: 
      return jjMoveStringLiteralDfa1_1(1L);
    }
    return 1;
  }
  
  private int jjMoveStringLiteralDfa1_1(long active1) {
    try { curChar = input_stream.readChar();
    } catch (IOException e) {
      return 1;
    }
    switch (curChar)
    {
    case 47: 
      if ((active1 & 1L) != 0L)
        return jjStopAtPos(1, 64);
      break;
    default: 
      return 2;
    }
    return 2; }
  
  static final int[] jjnextStates = { 244, 247, 250, 253, 256, 259, 262, 265, 268, 270, 271, 40, 41, 42, 19, 20, 21, 314, 315, 316, 246, 317, 318, 319, 249, 320, 321, 322, 252, 323, 324, 325, 255, 326, 327, 328, 258, 329, 330, 331, 261, 332, 333, 334, 264, 335, 336, 337, 267, 338, 339, 340, 269, 341, 342, 343, 344, 345, 346, 2, 6, 8, 9, 11, 14, 7, 3, 2, 7, 3, 19, 27, 29, 30, 32, 35, 28, 20, 21, 19, 28, 20, 21, 40, 48, 50, 51, 53, 56, 49, 41, 42, 40, 49, 41, 42, 73, 77, 79, 80, 82, 85, 78, 74, 73, 78, 74, 91, 92, 93, 95, 98, 78, 73, 74, 78, 73, 74, 105, 109, 111, 112, 114, 117, 110, 106, 105, 110, 106, 123, 141, 162, 125, 126, 183, 123, 124, 125, 126, 123, 129, 131, 132, 134, 137, 125, 126, 130, 123, 125, 126, 130, 142, 143, 144, 142, 150, 152, 153, 155, 158, 151, 143, 144, 142, 151, 143, 144, 163, 164, 165, 163, 171, 173, 174, 176, 179, 172, 164, 165, 163, 172, 164, 165, 123, 141, 162, 124, 125, 126, 183, 187, 188, 232, 189, 203, 204, 206, 209, 190, 187, 213, 225, 192, 193, 194, 196, 199, 187, 214, 221, 187, 215, 219, 187, 217, 218, 216, 222, 224, 216, 226, 228, 231, 236, 239, 241, 242, 216, 276, 278, 279, 281, 284, 277, 289, 291, 292, 294, 297, 290, 302, 303, 304, 306, 309, 110, 105, 106, 110, 105, 106, 107, 273, 274, 275, 288, 301, 72, 89, 185, 186, 4, 5, 22, 24, 25, 26, 43, 45, 46, 47, 127, 128, 145, 147, 148, 149, 166, 168, 169, 170 };
  


















  private static final boolean jjCanMove_0(int hiByte, int i1, int i2, long l1, long l2)
  {
    switch (hiByte)
    {
    case 0: 
      return (jjbitVec0[i2] & l2) != 0L;
    }
    return false;
  }
  
  private static final boolean jjCanMove_1(int hiByte, int i1, int i2, long l1, long l2)
  {
    switch (hiByte)
    {
    case 0: 
      return (jjbitVec3[i2] & l2) != 0L;
    }
    if ((jjbitVec1[i1] & l1) != 0L)
      return true;
    return false;
  }
  


  public static final String[] jjstrLiteralImages = { "", null, null, null, null, null, null, null, null, null, "{", "}", ",", ".", ";", ":", "/", "+", "-", "=", ">", "[", "]", null, ")", null, "<!--", "-->", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null };
  











  protected Token jjFillToken()
  {
    String im = jjstrLiteralImages[jjmatchedKind];
    String curTokenImage = im == null ? input_stream.GetImage() : im;
    int beginLine = input_stream.getBeginLine();
    int beginColumn = input_stream.getBeginColumn();
    int endLine = input_stream.getEndLine();
    int endColumn = input_stream.getEndColumn();
    Token t = Token.newToken(jjmatchedKind, curTokenImage);
    
    beginLine = beginLine;
    endLine = endLine;
    beginColumn = beginColumn;
    endColumn = endColumn;
    
    return t;
  }
  
  int curLexState = 0;
  int defaultLexState = 0;
  
  int jjnewStateCnt;
  
  int jjround;
  int jjmatchedPos;
  int jjmatchedKind;
  
  public Token getNextToken()
  {
    int curPos = 0;
    



    try
    {
      curChar = input_stream.BeginToken();
    }
    catch (Exception e)
    {
      jjmatchedKind = 0;
      jjmatchedPos = -1;
      return jjFillToken();
    }
    
    image = jjimage;
    image.setLength(0);
    jjimageLen = 0;
    
    for (;;)
    {
      switch (curLexState)
      {
      case 0: 
        jjmatchedKind = Integer.MAX_VALUE;
        jjmatchedPos = 0;
        curPos = jjMoveStringLiteralDfa0_0();
        if ((jjmatchedPos == 0) && (jjmatchedKind > 66))
        {
          jjmatchedKind = 66;
        }
        break;
      case 1: 
        jjmatchedKind = Integer.MAX_VALUE;
        jjmatchedPos = 0;
        curPos = jjMoveStringLiteralDfa0_1();
        if ((jjmatchedPos == 0) && (jjmatchedKind > 65))
        {
          jjmatchedKind = 65;
        }
        break;
      }
      if (jjmatchedKind != Integer.MAX_VALUE)
      {
        if (jjmatchedPos + 1 < curPos)
          input_stream.backup(curPos - jjmatchedPos - 1);
        if ((jjtoToken[(jjmatchedKind >> 6)] & 1L << (jjmatchedKind & 0x3F)) != 0L)
        {
          Token matchedToken = jjFillToken();
          TokenLexicalActions(matchedToken);
          if (jjnewLexState[jjmatchedKind] != -1)
            curLexState = jjnewLexState[jjmatchedKind];
          return matchedToken;
        }
        if ((jjtoSkip[(jjmatchedKind >> 6)] & 1L << (jjmatchedKind & 0x3F)) != 0L)
        {
          if (jjnewLexState[jjmatchedKind] == -1) break;
          curLexState = jjnewLexState[jjmatchedKind]; break;
        }
        
        jjimageLen += jjmatchedPos + 1;
        if (jjnewLexState[jjmatchedKind] != -1)
          curLexState = jjnewLexState[jjmatchedKind];
        curPos = 0;
        jjmatchedKind = Integer.MAX_VALUE;
        try {
          curChar = input_stream.readChar();
        }
        catch (IOException e1) {}
      }
    }
    int error_line = input_stream.getEndLine();
    int error_column = input_stream.getEndColumn();
    String error_after = null;
    boolean EOFSeen = false;
    try { input_stream.readChar();input_stream.backup(1);
    } catch (IOException e1) {
      EOFSeen = true;
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
      if ((curChar == 10) || (curChar == 13)) {
        error_line++;
        error_column = 0;
      }
      else {
        error_column++;
      } }
    if (!EOFSeen) {
      input_stream.backup(1);
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
    }
    throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, 0);
  }
  


  void TokenLexicalActions(Token matchedToken)
  {
    switch (jjmatchedKind)
    {
    case 4: 
      image.append(input_stream.GetSuffix(jjimageLen + (this.lengthOfMatch = jjmatchedPos + 1)));
      image = ParserUtils.trimBy(image, 1, 0);
      break;
    case 5: 
      image.append(input_stream.GetSuffix(jjimageLen + (this.lengthOfMatch = jjmatchedPos + 1)));
      image = ParserUtils.trimBy(image, 1, 0);
      break;
    case 6: 
      image.append(input_stream.GetSuffix(jjimageLen + (this.lengthOfMatch = jjmatchedPos + 1)));
      image = ParserUtils.trimBy(image, 1, 0);
      break;
    case 7: 
      image.append(input_stream.GetSuffix(jjimageLen + (this.lengthOfMatch = jjmatchedPos + 1)));
      image = ParserUtils.trimBy(image, 1, 0);
      break;
    case 8: 
      image.append(input_stream.GetSuffix(jjimageLen + (this.lengthOfMatch = jjmatchedPos + 1)));
      image = ParserUtils.trimBy(image, 1, 0);
      break;
    case 23: 
      image.append(input_stream.GetSuffix(jjimageLen + (this.lengthOfMatch = jjmatchedPos + 1)));
      image = ParserUtils.trimBy(image, 1, 1);
      break;
    case 25: 
      image.append(input_stream.GetSuffix(jjimageLen + (this.lengthOfMatch = jjmatchedPos + 1)));
      image = ParserUtils.trimUrl(image);
      break;
    case 31: 
      image.append(input_stream.GetSuffix(jjimageLen + (this.lengthOfMatch = jjmatchedPos + 1)));
      image = ParserUtils.trimBy(image, 0, 2);
      break;
    case 32: 
      image.append(input_stream.GetSuffix(jjimageLen + (this.lengthOfMatch = jjmatchedPos + 1)));
      image = ParserUtils.trimBy(image, 0, 2);
      break;
    case 33: 
      image.append(input_stream.GetSuffix(jjimageLen + (this.lengthOfMatch = jjmatchedPos + 1)));
      image = ParserUtils.trimBy(image, 0, 2);
      break;
    case 34: 
      image.append(input_stream.GetSuffix(jjimageLen + (this.lengthOfMatch = jjmatchedPos + 1)));
      image = ParserUtils.trimBy(image, 0, 2);
      break;
    case 35: 
      image.append(input_stream.GetSuffix(jjimageLen + (this.lengthOfMatch = jjmatchedPos + 1)));
      image = ParserUtils.trimBy(image, 0, 2);
      break;
    case 36: 
      image.append(input_stream.GetSuffix(jjimageLen + (this.lengthOfMatch = jjmatchedPos + 1)));
      image = ParserUtils.trimBy(image, 0, 2);
      break;
    case 37: 
      image.append(input_stream.GetSuffix(jjimageLen + (this.lengthOfMatch = jjmatchedPos + 1)));
      image = ParserUtils.trimBy(image, 0, 2);
      break;
    case 38: 
      image.append(input_stream.GetSuffix(jjimageLen + (this.lengthOfMatch = jjmatchedPos + 1)));
      image = ParserUtils.trimBy(image, 0, 2);
      break;
    case 39: 
      image.append(input_stream.GetSuffix(jjimageLen + (this.lengthOfMatch = jjmatchedPos + 1)));
      image = ParserUtils.trimBy(image, 0, 1);
      break;
    }
    
  }
  
  private void jjCheckNAdd(int state)
  {
    if (jjrounds[state] != jjround)
    {
      jjstateSet[(jjnewStateCnt++)] = state;
      jjrounds[state] = jjround;
    }
  }
  
  private void jjAddStates(int start, int end) {
    do {
      jjstateSet[(jjnewStateCnt++)] = jjnextStates[start];
    } while (start++ != end);
  }
  
  private void jjCheckNAddTwoStates(int state1, int state2) {
    jjCheckNAdd(state1);
    jjCheckNAdd(state2);
  }
  
  private void jjCheckNAddStates(int start, int end)
  {
    do {
      jjCheckNAdd(jjnextStates[start]);
    } while (start++ != end);
  }
  


  public SACParserCSS1TokenManager(CharStream stream)
  {
    input_stream = stream;
  }
  
  public SACParserCSS1TokenManager(CharStream stream, int lexState)
  {
    ReInit(stream);
    SwitchTo(lexState);
  }
  


  public void ReInit(CharStream stream)
  {
    jjmatchedPos = (this.jjnewStateCnt = 0);
    curLexState = defaultLexState;
    input_stream = stream;
    ReInitRounds();
  }
  

  private void ReInitRounds()
  {
    jjround = -2147483647;
    for (int i = 347; i-- > 0;) {
      jjrounds[i] = Integer.MIN_VALUE;
    }
  }
  

  public void ReInit(CharStream stream, int lexState)
  {
    ReInit(stream);
    SwitchTo(lexState);
  }
  

  public void SwitchTo(int lexState)
  {
    if ((lexState >= 2) || (lexState < 0)) {
      throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", 2);
    }
    curLexState = lexState;
  }
  

  public static final String[] lexStateNames = { "DEFAULT", "COMMENT" };
  




  public static final int[] jjnewLexState = { -1, -1, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, -1, -1 };
  



  static final long[] jjtoToken = { 63107569387831291L, 4L };
  

  static final long[] jjtoSkip = { 0L, 1L };
  

  static final long[] jjtoMore = { 4L, 2L };
  

  protected CharStream input_stream;
  
  private final int[] jjrounds = new int['ś'];
  private final int[] jjstateSet = new int['ʶ'];
  
  private final StringBuilder jjimage = new StringBuilder();
  private StringBuilder image = jjimage;
  private int jjimageLen;
  private int lengthOfMatch;
  protected int curChar;
}
