package com.steadystate.css.parser;

import java.io.IOException;
import java.io.PrintStream;










public class SACParserCSSmobileOKBasic1TokenManager
  implements SACParserCSSmobileOKBasic1Constants
{
  public PrintStream debugStream = System.out;
  
  public void setDebugStream(PrintStream ds) { debugStream = ds; }
  
  private final int jjStopStringLiteralDfa_0(int pos, long active0) { switch (pos)
    {
    case 0: 
      if ((active0 & 0x2000) != 0L)
        return 317;
      if ((active0 & 0x80000000000) != 0L)
      {
        jjmatchedKind = 3;
        return 318;
      }
      if ((active0 & 0x60000000) != 0L)
        return 89;
      return -1;
    case 1: 
      if ((active0 & 0x60000000) != 0L)
      {
        jjmatchedKind = 32;
        jjmatchedPos = 1;
        return 319;
      }
      if ((active0 & 0x80000000000) != 0L)
      {
        jjmatchedKind = 3;
        jjmatchedPos = 1;
        return 318;
      }
      return -1;
    case 2: 
      if ((active0 & 0x80000000000) != 0L)
      {
        jjmatchedKind = 3;
        jjmatchedPos = 2;
        return 318;
      }
      if ((active0 & 0x60000000) != 0L)
      {
        jjmatchedKind = 32;
        jjmatchedPos = 2;
        return 319;
      }
      return -1;
    case 3: 
      if ((active0 & 0x60000000) != 0L)
      {
        jjmatchedKind = 32;
        jjmatchedPos = 3;
        return 319;
      }
      return -1;
    case 4: 
      if ((active0 & 0x60000000) != 0L)
      {
        jjmatchedKind = 32;
        jjmatchedPos = 4;
        return 319;
      }
      return -1;
    case 5: 
      if ((active0 & 0x20000000) != 0L)
      {
        jjmatchedKind = 32;
        jjmatchedPos = 5;
        return 319;
      }
      if ((active0 & 0x40000000) != 0L)
        return 319;
      return -1;
    case 6: 
      if ((active0 & 0x20000000) != 0L)
        return 319;
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
      return jjStopAtPos(0, 25);
    case 42: 
      return jjStopAtPos(0, 16);
    case 43: 
      return jjStopAtPos(0, 18);
    case 44: 
      return jjStopAtPos(0, 12);
    case 45: 
      jjmatchedKind = 19;
      return jjMoveStringLiteralDfa1_0(268435456L);
    case 46: 
      return jjStartNfaWithStates_0(0, 13, 317);
    case 47: 
      jjmatchedKind = 17;
      return jjMoveStringLiteralDfa1_0(4L);
    case 58: 
      jjmatchedKind = 15;
      return jjMoveStringLiteralDfa1_0(496L);
    case 59: 
      return jjStopAtPos(0, 14);
    case 60: 
      return jjMoveStringLiteralDfa1_0(134217728L);
    case 61: 
      return jjStopAtPos(0, 20);
    case 62: 
      return jjStopAtPos(0, 21);
    case 64: 
      return jjMoveStringLiteralDfa1_0(1610612736L);
    case 91: 
      return jjStopAtPos(0, 22);
    case 93: 
      return jjStopAtPos(0, 23);
    case 82: 
    case 114: 
      return jjMoveStringLiteralDfa1_0(8796093022208L);
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
      return jjMoveStringLiteralDfa2_0(active0, 134217728L);
    case 42: 
      if ((active0 & 0x4) != 0L)
        return jjStopAtPos(1, 2);
      break;
    case 45: 
      return jjMoveStringLiteralDfa2_0(active0, 268435456L);
    case 65: 
    case 97: 
      return jjMoveStringLiteralDfa2_0(active0, 64L);
    case 70: 
    case 102: 
      return jjMoveStringLiteralDfa2_0(active0, 384L);
    case 71: 
    case 103: 
      return jjMoveStringLiteralDfa2_0(active0, 8796093022208L);
    case 73: 
    case 105: 
      return jjMoveStringLiteralDfa2_0(active0, 536870912L);
    case 76: 
    case 108: 
      return jjMoveStringLiteralDfa2_0(active0, 16L);
    case 77: 
    case 109: 
      return jjMoveStringLiteralDfa2_0(active0, 1073741824L);
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
      return jjMoveStringLiteralDfa3_0(active0, 134217728L);
    case 62: 
      if ((active0 & 0x10000000) != 0L) {
        return jjStopAtPos(2, 28);
      }
      break;
    case 66: case 98: 
      return jjMoveStringLiteralDfa3_0(active0, 8796093022208L);
    case 67: 
    case 99: 
      return jjMoveStringLiteralDfa3_0(active0, 64L);
    case 69: 
    case 101: 
      return jjMoveStringLiteralDfa3_0(active0, 1073741824L);
    case 73: 
    case 105: 
      return jjMoveStringLiteralDfa3_0(active0, 432L);
    case 77: 
    case 109: 
      return jjMoveStringLiteralDfa3_0(active0, 536870912L);
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
      if ((active0 & 0x80000000000) != 0L)
        return jjStopAtPos(3, 43);
      break;
    case 45: 
      if ((active0 & 0x8000000) != 0L) {
        return jjStopAtPos(3, 27);
      }
      break;
    case 68: case 100: 
      return jjMoveStringLiteralDfa4_0(active0, 1073741824L);
    case 78: 
    case 110: 
      return jjMoveStringLiteralDfa4_0(active0, 16L);
    case 80: 
    case 112: 
      return jjMoveStringLiteralDfa4_0(active0, 536870912L);
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
      return jjMoveStringLiteralDfa5_0(active0, 1073741920L);
    case 75: 
    case 107: 
      if ((active0 & 0x10) != 0L) {
        return jjStopAtPos(4, 4);
      }
      break;
    case 79: case 111: 
      return jjMoveStringLiteralDfa5_0(active0, 536870912L);
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
    case 65: 
    case 97: 
      if ((active0 & 0x40000000) != 0L) {
        return jjStartNfaWithStates_0(5, 30, 319);
      }
      break;
    case 82: case 114: 
      return jjMoveStringLiteralDfa6_0(active0, 536870912L);
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
      if ((active0 & 0x20000000) != 0L) {
        return jjStartNfaWithStates_0(6, 29, 319);
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
  
  static final long[] jjbitVec0 = { -2L, -1L, -1L, -1L };
  

  static final long[] jjbitVec2 = { 0L, 0L, -1L, -1L };
  

  private int jjMoveNfa_0(int startState, int curPos)
  {
    int startsAt = 0;
    jjnewStateCnt = 317;
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
          case 317: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 46)
                kind = 46;
              jjCheckNAdd(282);
            }
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 42)
                kind = 42;
              jjCheckNAdd(281);
            }
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(279, 280);
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(276, 278);
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(273, 275);
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(270, 272);
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(267, 269);
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(264, 266);
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(261, 263);
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(258, 260);
            if ((0x3FF000000000000 & l) != 0L) {
              jjCheckNAddTwoStates(255, 257);
            }
            break;
          case 2: case 318: 
            if ((0x3FF200000000000 & l) != 0L)
            {
              if (kind > 3)
                kind = 3;
              jjCheckNAddTwoStates(2, 3); }
            break;
          case 1: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 42)
                kind = 42;
              jjCheckNAddStates(0, 41);
            }
            else if ((0x100003600 & l) != 0L)
            {
              if (kind > 1)
                kind = 1;
              jjCheckNAdd(0);
            }
            else if (curChar == 46) {
              jjCheckNAddStates(42, 52);
            } else if (curChar == 33) {
              jjCheckNAddTwoStates(78, 87);
            } else if (curChar == 39) {
              jjCheckNAddStates(53, 55);
            } else if (curChar == 34) {
              jjCheckNAddStates(56, 58);
            } else if (curChar == 35) {
              jjCheckNAddTwoStates(19, 20);
            }
            break;
          case 90: case 319: 
            if ((0x3FF200000000000 & l) != 0L)
            {
              if (kind > 32)
                kind = 32;
              jjCheckNAddTwoStates(90, 91); }
            break;
          case 0: 
            if ((0x100003600 & l) != 0L)
            {
              if (kind > 1)
                kind = 1;
              jjCheckNAdd(0); }
            break;
          case 4: 
            if ((0xFFFFFFFF00000000 & l) != 0L)
            {
              if (kind > 3)
                kind = 3;
              jjCheckNAddTwoStates(2, 3); }
            break;
          case 5: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 3)
                kind = 3;
              jjCheckNAddStates(59, 66); }
            break;
          case 6: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 3)
                kind = 3;
              jjCheckNAddStates(67, 69); }
            break;
          case 7: 
            if ((0x100003600 & l) != 0L)
            {
              if (kind > 3)
                kind = 3;
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
            if (curChar == 35)
              jjCheckNAddTwoStates(19, 20);
            break;
          case 19: 
            if ((0x3FF200000000000 & l) != 0L)
            {
              if (kind > 9)
                kind = 9;
              jjCheckNAddTwoStates(19, 20); }
            break;
          case 21: 
            if ((0xFFFFFFFF00000000 & l) != 0L)
            {
              if (kind > 9)
                kind = 9;
              jjCheckNAddTwoStates(19, 20); }
            break;
          case 22: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 9)
                kind = 9;
              jjCheckNAddStates(70, 77); }
            break;
          case 23: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 9)
                kind = 9;
              jjCheckNAddStates(78, 80); }
            break;
          case 24: 
            if ((0x100003600 & l) != 0L)
            {
              if (kind > 9)
                kind = 9;
              jjCheckNAddTwoStates(19, 20); }
            break;
          case 25: 
          case 27: 
          case 30: 
          case 34: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(23);
            break;
          case 26: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 27;
            break;
          case 28: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 29;
            break;
          case 29: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 30;
            break;
          case 31: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 32;
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
            if (curChar == 34)
              jjCheckNAddStates(56, 58);
            break;
          case 36: 
            if ((0xFFFFFFFB00000200 & l) != 0L)
              jjCheckNAddStates(56, 58);
            break;
          case 37: 
            if ((curChar == 34) && (kind > 24))
              kind = 24;
            break;
          case 39: 
            if ((0x3400 & l) != 0L)
              jjCheckNAddStates(56, 58);
            break;
          case 40: 
            if (curChar == 10)
              jjCheckNAddStates(56, 58);
            break;
          case 41: 
            if (curChar == 13)
              jjstateSet[(jjnewStateCnt++)] = 40;
            break;
          case 42: 
            if ((0xFFFFFFFF00000000 & l) != 0L)
              jjCheckNAddStates(56, 58);
            break;
          case 43: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddStates(81, 89);
            break;
          case 44: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddStates(90, 93);
            break;
          case 45: 
            if ((0x100003600 & l) != 0L) {
              jjCheckNAddStates(56, 58);
            }
            break;
          case 46: case 48: 
          case 51: 
          case 55: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(44);
            break;
          case 47: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 48;
            break;
          case 49: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 50;
            break;
          case 50: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 51;
            break;
          case 52: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 53;
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
            if (curChar == 39)
              jjCheckNAddStates(53, 55);
            break;
          case 57: 
            if ((0xFFFFFF7F00000200 & l) != 0L)
              jjCheckNAddStates(53, 55);
            break;
          case 58: 
            if ((curChar == 39) && (kind > 24))
              kind = 24;
            break;
          case 60: 
            if ((0x3400 & l) != 0L)
              jjCheckNAddStates(53, 55);
            break;
          case 61: 
            if (curChar == 10)
              jjCheckNAddStates(53, 55);
            break;
          case 62: 
            if (curChar == 13)
              jjstateSet[(jjnewStateCnt++)] = 61;
            break;
          case 63: 
            if ((0xFFFFFFFF00000000 & l) != 0L)
              jjCheckNAddStates(53, 55);
            break;
          case 64: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddStates(94, 102);
            break;
          case 65: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddStates(103, 106);
            break;
          case 66: 
            if ((0x100003600 & l) != 0L) {
              jjCheckNAddStates(53, 55);
            }
            break;
          case 67: case 69: 
          case 72: 
          case 76: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(65);
            break;
          case 68: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 69;
            break;
          case 70: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 71;
            break;
          case 71: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 72;
            break;
          case 73: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 74;
            break;
          case 74: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 75;
            break;
          case 75: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 76;
            break;
          case 77: 
            if (curChar == 33)
              jjCheckNAddTwoStates(78, 87);
            break;
          case 78: 
            if ((0x100003600 & l) != 0L)
              jjCheckNAddTwoStates(78, 87);
            break;
          case 92: 
            if ((0xFFFFFFFF00000000 & l) != 0L)
            {
              if (kind > 32)
                kind = 32;
              jjCheckNAddTwoStates(90, 91); }
            break;
          case 93: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 32)
                kind = 32;
              jjCheckNAddStates(107, 114); }
            break;
          case 94: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 32)
                kind = 32;
              jjCheckNAddStates(115, 117); }
            break;
          case 95: 
            if ((0x100003600 & l) != 0L)
            {
              if (kind > 32)
                kind = 32;
              jjCheckNAddTwoStates(90, 91); }
            break;
          case 96: 
          case 98: 
          case 101: 
          case 105: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(94);
            break;
          case 97: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 98;
            break;
          case 99: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 100;
            break;
          case 100: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 101;
            break;
          case 102: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 103;
            break;
          case 103: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 104;
            break;
          case 104: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 105;
            break;
          case 107: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 32)
                kind = 32;
              jjCheckNAddStates(118, 125); }
            break;
          case 108: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 32)
                kind = 32;
              jjCheckNAddStates(126, 128); }
            break;
          case 109: 
          case 111: 
          case 114: 
          case 118: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(108);
            break;
          case 110: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 111;
            break;
          case 112: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 113;
            break;
          case 113: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 114;
            break;
          case 115: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 116;
            break;
          case 116: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 117;
            break;
          case 117: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 118;
            break;
          case 120: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 3)
                kind = 3;
              jjCheckNAddStates(129, 136); }
            break;
          case 121: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 3)
                kind = 3;
              jjCheckNAddStates(137, 139); }
            break;
          case 122: 
          case 124: 
          case 127: 
          case 131: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(121);
            break;
          case 123: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 124;
            break;
          case 125: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 126;
            break;
          case 126: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 127;
            break;
          case 128: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 129;
            break;
          case 129: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 130;
            break;
          case 130: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 131;
            break;
          case 133: 
            if (curChar == 40)
              jjCheckNAddStates(140, 145);
            break;
          case 134: 
            if ((0xFFFFFC7A00000000 & l) != 0L)
              jjCheckNAddStates(146, 149);
            break;
          case 135: 
            if ((0x100003600 & l) != 0L)
              jjCheckNAddTwoStates(135, 136);
            break;
          case 136: 
            if ((curChar == 41) && (kind > 26))
              kind = 26;
            break;
          case 138: 
            if ((0xFFFFFFFF00000000 & l) != 0L)
              jjCheckNAddStates(146, 149);
            break;
          case 139: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddStates(150, 158);
            break;
          case 140: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddStates(159, 162);
            break;
          case 141: 
            if ((0x100003600 & l) != 0L) {
              jjCheckNAddStates(146, 149);
            }
            break;
          case 142: case 144: 
          case 147: 
          case 151: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(140);
            break;
          case 143: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 144;
            break;
          case 145: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 146;
            break;
          case 146: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 147;
            break;
          case 148: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 149;
            break;
          case 149: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 150;
            break;
          case 150: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 151;
            break;
          case 152: 
            if (curChar == 39)
              jjCheckNAddStates(163, 165);
            break;
          case 153: 
            if ((0xFFFFFF7F00000200 & l) != 0L)
              jjCheckNAddStates(163, 165);
            break;
          case 154: 
            if (curChar == 39)
              jjCheckNAddTwoStates(135, 136);
            break;
          case 156: 
            if ((0x3400 & l) != 0L)
              jjCheckNAddStates(163, 165);
            break;
          case 157: 
            if (curChar == 10)
              jjCheckNAddStates(163, 165);
            break;
          case 158: 
            if (curChar == 13)
              jjstateSet[(jjnewStateCnt++)] = 157;
            break;
          case 159: 
            if ((0xFFFFFFFF00000000 & l) != 0L)
              jjCheckNAddStates(163, 165);
            break;
          case 160: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddStates(166, 174);
            break;
          case 161: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddStates(175, 178);
            break;
          case 162: 
            if ((0x100003600 & l) != 0L) {
              jjCheckNAddStates(163, 165);
            }
            break;
          case 163: case 165: 
          case 168: 
          case 172: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(161);
            break;
          case 164: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 165;
            break;
          case 166: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 167;
            break;
          case 167: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 168;
            break;
          case 169: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 170;
            break;
          case 170: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 171;
            break;
          case 171: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 172;
            break;
          case 173: 
            if (curChar == 34)
              jjCheckNAddStates(179, 181);
            break;
          case 174: 
            if ((0xFFFFFFFB00000200 & l) != 0L)
              jjCheckNAddStates(179, 181);
            break;
          case 175: 
            if (curChar == 34)
              jjCheckNAddTwoStates(135, 136);
            break;
          case 177: 
            if ((0x3400 & l) != 0L)
              jjCheckNAddStates(179, 181);
            break;
          case 178: 
            if (curChar == 10)
              jjCheckNAddStates(179, 181);
            break;
          case 179: 
            if (curChar == 13)
              jjstateSet[(jjnewStateCnt++)] = 178;
            break;
          case 180: 
            if ((0xFFFFFFFF00000000 & l) != 0L)
              jjCheckNAddStates(179, 181);
            break;
          case 181: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddStates(182, 190);
            break;
          case 182: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddStates(191, 194);
            break;
          case 183: 
            if ((0x100003600 & l) != 0L) {
              jjCheckNAddStates(179, 181);
            }
            break;
          case 184: case 186: 
          case 189: 
          case 193: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(182);
            break;
          case 185: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 186;
            break;
          case 187: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 188;
            break;
          case 188: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 189;
            break;
          case 190: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 191;
            break;
          case 191: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 192;
            break;
          case 192: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 193;
            break;
          case 194: 
            if ((0x100003600 & l) != 0L)
              jjCheckNAddStates(195, 201);
            break;
          case 197: 
            if (curChar == 43) {
              jjCheckNAddStates(202, 204);
            }
            break;
          case 198: case 227: 
            if ((curChar == 63) && (kind > 47))
              kind = 47;
            break;
          case 199: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 47)
                kind = 47;
              jjCheckNAddStates(205, 213); }
            break;
          case 200: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(201);
            break;
          case 201: 
            if (curChar == 45)
              jjstateSet[(jjnewStateCnt++)] = 202;
            break;
          case 202: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 47)
                kind = 47;
              jjCheckNAddStates(214, 218); }
            break;
          case 203: 
            if (((0x3FF000000000000 & l) != 0L) && (kind > 47)) {
              kind = 47;
            }
            break;
          case 204: case 206: 
          case 209: 
          case 213: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(203);
            break;
          case 205: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 206;
            break;
          case 207: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 208;
            break;
          case 208: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 209;
            break;
          case 210: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 211;
            break;
          case 211: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 212;
            break;
          case 212: 
            if ((0x3FF000000000000 & l) != 0L) {
              jjstateSet[(jjnewStateCnt++)] = 213;
            }
            break;
          case 214: case 216: 
          case 219: 
          case 223: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(200);
            break;
          case 215: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 216;
            break;
          case 217: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 218;
            break;
          case 218: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 219;
            break;
          case 220: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 221;
            break;
          case 221: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 222;
            break;
          case 222: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 223;
            break;
          case 224: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 47)
                kind = 47;
              jjCheckNAddStates(219, 221); }
            break;
          case 225: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 47)
                kind = 47;
              jjCheckNAddStates(222, 224); }
            break;
          case 226: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 47)
                kind = 47;
              jjCheckNAddStates(225, 227); }
            break;
          case 228: 
          case 231: 
          case 233: 
          case 234: 
          case 237: 
          case 238: 
          case 240: 
          case 244: 
          case 248: 
          case 251: 
          case 253: 
            if (curChar == 63)
              jjCheckNAdd(227);
            break;
          case 229: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 47)
                kind = 47;
              jjCheckNAddTwoStates(198, 203); }
            break;
          case 230: 
            if (curChar == 63)
              jjCheckNAddTwoStates(227, 231);
            break;
          case 232: 
            if (curChar == 63)
              jjCheckNAddStates(228, 230);
            break;
          case 235: 
            if (curChar == 63)
              jjstateSet[(jjnewStateCnt++)] = 234;
            break;
          case 236: 
            if (curChar == 63)
              jjCheckNAddStates(231, 234);
            break;
          case 239: 
            if (curChar == 63)
              jjstateSet[(jjnewStateCnt++)] = 238;
            break;
          case 241: 
            if (curChar == 63)
              jjstateSet[(jjnewStateCnt++)] = 240;
            break;
          case 242: 
            if (curChar == 63)
              jjstateSet[(jjnewStateCnt++)] = 241;
            break;
          case 243: 
            if (curChar == 63)
              jjCheckNAddStates(235, 239);
            break;
          case 245: 
            if (curChar == 63)
              jjstateSet[(jjnewStateCnt++)] = 244;
            break;
          case 246: 
            if (curChar == 63)
              jjstateSet[(jjnewStateCnt++)] = 245;
            break;
          case 247: 
            if (curChar == 63)
              jjstateSet[(jjnewStateCnt++)] = 246;
            break;
          case 249: 
            if (curChar == 63)
              jjstateSet[(jjnewStateCnt++)] = 248;
            break;
          case 250: 
            if (curChar == 63)
              jjstateSet[(jjnewStateCnt++)] = 249;
            break;
          case 252: 
            if (curChar == 63)
              jjstateSet[(jjnewStateCnt++)] = 251;
            break;
          case 254: 
            if (curChar == 46)
              jjCheckNAddStates(42, 52);
            break;
          case 255: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(255, 257);
            break;
          case 258: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(258, 260);
            break;
          case 261: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(261, 263);
            break;
          case 264: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(264, 266);
            break;
          case 267: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(267, 269);
            break;
          case 270: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(270, 272);
            break;
          case 273: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(273, 275);
            break;
          case 276: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(276, 278);
            break;
          case 279: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(279, 280);
            break;
          case 280: 
            if ((curChar == 37) && (kind > 41))
              kind = 41;
            break;
          case 281: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 42)
                kind = 42;
              jjCheckNAdd(281); }
            break;
          case 282: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 46)
                kind = 46;
              jjCheckNAdd(282); }
            break;
          case 283: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 42)
                kind = 42;
              jjCheckNAddStates(0, 41); }
            break;
          case 284: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(284, 257);
            break;
          case 285: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(285, 286);
            break;
          case 286: 
            if (curChar == 46)
              jjCheckNAdd(255);
            break;
          case 287: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(287, 260);
            break;
          case 288: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(288, 289);
            break;
          case 289: 
            if (curChar == 46)
              jjCheckNAdd(258);
            break;
          case 290: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(290, 263);
            break;
          case 291: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(291, 292);
            break;
          case 292: 
            if (curChar == 46)
              jjCheckNAdd(261);
            break;
          case 293: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(293, 266);
            break;
          case 294: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(294, 295);
            break;
          case 295: 
            if (curChar == 46)
              jjCheckNAdd(264);
            break;
          case 296: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(296, 269);
            break;
          case 297: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(297, 298);
            break;
          case 298: 
            if (curChar == 46)
              jjCheckNAdd(267);
            break;
          case 299: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(299, 272);
            break;
          case 300: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(300, 301);
            break;
          case 301: 
            if (curChar == 46)
              jjCheckNAdd(270);
            break;
          case 302: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(302, 275);
            break;
          case 303: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(303, 304);
            break;
          case 304: 
            if (curChar == 46)
              jjCheckNAdd(273);
            break;
          case 305: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(305, 278);
            break;
          case 306: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(306, 307);
            break;
          case 307: 
            if (curChar == 46)
              jjCheckNAdd(276);
            break;
          case 308: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(308, 280);
            break;
          case 309: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(309, 310);
            break;
          case 310: 
            if (curChar == 46)
              jjCheckNAdd(279);
            break;
          case 311: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 42)
                kind = 42;
              jjCheckNAdd(311); }
            break;
          case 312: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(312, 313);
            break;
          case 313: 
            if (curChar == 46)
              jjCheckNAdd(281);
            break;
          case 314: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 46)
                kind = 46;
              jjCheckNAdd(314); }
            break;
          case 315: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(315, 316);
            break;
          case 316: 
            if (curChar == 46) {
              jjCheckNAdd(282);
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
          case 318: 
            if ((0x7FFFFFE07FFFFFE & l) != 0L)
            {
              if (kind > 3)
                kind = 3;
              jjCheckNAddTwoStates(2, 3);
            }
            else if (curChar == 92) {
              jjCheckNAddTwoStates(4, 5);
            }
            break;
          case 1:  if ((0x7FFFFFE07FFFFFE & l) != 0L)
            {
              if (kind > 3)
                kind = 3;
              jjCheckNAddTwoStates(2, 3);
            }
            else if (curChar == 92) {
              jjCheckNAddTwoStates(4, 120);
            } else if (curChar == 64) {
              jjAddStates(240, 241); }
            if ((0x20000000200000 & l) != 0L)
              jjAddStates(242, 243);
            break;
          case 89: 
            if ((0x7FFFFFE07FFFFFE & l) != 0L)
            {
              if (kind > 32)
                kind = 32;
              jjCheckNAddTwoStates(90, 91);
            }
            else if (curChar == 92) {
              jjCheckNAddTwoStates(92, 107);
            }
            break;
          case 319:  if ((0x7FFFFFE07FFFFFE & l) != 0L)
            {
              if (kind > 32)
                kind = 32;
              jjCheckNAddTwoStates(90, 91);
            }
            else if (curChar == 92) {
              jjCheckNAddTwoStates(92, 93);
            }
            break;
          case 2:  if ((0x7FFFFFE07FFFFFE & l) != 0L)
            {
              if (kind > 3)
                kind = 3;
              jjCheckNAddTwoStates(2, 3); }
            break;
          case 3: 
            if (curChar == 92)
              jjCheckNAddTwoStates(4, 5);
            break;
          case 4: 
            if ((0x7FFFFFFFFFFFFFFF & l) != 0L)
            {
              if (kind > 3)
                kind = 3;
              jjCheckNAddTwoStates(2, 3); }
            break;
          case 5: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 3)
                kind = 3;
              jjCheckNAddStates(59, 66); }
            break;
          case 6: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 3)
                kind = 3;
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
            if ((0x7FFFFFE07FFFFFE & l) != 0L)
            {
              if (kind > 9)
                kind = 9;
              jjCheckNAddTwoStates(19, 20); }
            break;
          case 20: 
            if (curChar == 92)
              jjAddStates(244, 245);
            break;
          case 21: 
            if ((0x7FFFFFFFFFFFFFFF & l) != 0L)
            {
              if (kind > 9)
                kind = 9;
              jjCheckNAddTwoStates(19, 20); }
            break;
          case 22: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 9)
                kind = 9;
              jjCheckNAddStates(70, 77); }
            break;
          case 23: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 9)
                kind = 9;
              jjCheckNAddStates(78, 80); }
            break;
          case 25: 
          case 27: 
          case 30: 
          case 34: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(23);
            break;
          case 26: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 27;
            break;
          case 28: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 29;
            break;
          case 29: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 30;
            break;
          case 31: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 32;
            break;
          case 32: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 33;
            break;
          case 33: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 34;
            break;
          case 36: 
            if ((0x7FFFFFFFEFFFFFFF & l) != 0L)
              jjCheckNAddStates(56, 58);
            break;
          case 38: 
            if (curChar == 92)
              jjAddStates(246, 249);
            break;
          case 42: 
            if ((0x7FFFFFFFFFFFFFFF & l) != 0L)
              jjCheckNAddStates(56, 58);
            break;
          case 43: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAddStates(81, 89);
            break;
          case 44: 
            if ((0x7E0000007E & l) != 0L) {
              jjCheckNAddStates(90, 93);
            }
            break;
          case 46: case 48: 
          case 51: 
          case 55: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(44);
            break;
          case 47: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 48;
            break;
          case 49: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 50;
            break;
          case 50: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 51;
            break;
          case 52: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 53;
            break;
          case 53: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 54;
            break;
          case 54: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 55;
            break;
          case 57: 
            if ((0x7FFFFFFFEFFFFFFF & l) != 0L)
              jjCheckNAddStates(53, 55);
            break;
          case 59: 
            if (curChar == 92)
              jjAddStates(250, 253);
            break;
          case 63: 
            if ((0x7FFFFFFFFFFFFFFF & l) != 0L)
              jjCheckNAddStates(53, 55);
            break;
          case 64: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAddStates(94, 102);
            break;
          case 65: 
            if ((0x7E0000007E & l) != 0L) {
              jjCheckNAddStates(103, 106);
            }
            break;
          case 67: case 69: 
          case 72: 
          case 76: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(65);
            break;
          case 68: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 69;
            break;
          case 70: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 71;
            break;
          case 71: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 72;
            break;
          case 73: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 74;
            break;
          case 74: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 75;
            break;
          case 75: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 76;
            break;
          case 79: 
            if (((0x10000000100000 & l) != 0L) && (kind > 31))
              kind = 31;
            break;
          case 80: 
            if ((0x400000004000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 79;
            break;
          case 81: 
            if ((0x200000002 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 80;
            break;
          case 82: 
            if ((0x10000000100000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 81;
            break;
          case 83: 
            if ((0x4000000040000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 82;
            break;
          case 84: 
            if ((0x800000008000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 83;
            break;
          case 85: 
            if ((0x1000000010000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 84;
            break;
          case 86: 
            if ((0x200000002000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 85;
            break;
          case 87: 
            if ((0x20000000200 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 86;
            break;
          case 88: 
            if (curChar == 64)
              jjAddStates(240, 241);
            break;
          case 90: 
            if ((0x7FFFFFE07FFFFFE & l) != 0L)
            {
              if (kind > 32)
                kind = 32;
              jjCheckNAddTwoStates(90, 91); }
            break;
          case 91: 
            if (curChar == 92)
              jjCheckNAddTwoStates(92, 93);
            break;
          case 92: 
            if ((0x7FFFFFFFFFFFFFFF & l) != 0L)
            {
              if (kind > 32)
                kind = 32;
              jjCheckNAddTwoStates(90, 91); }
            break;
          case 93: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 32)
                kind = 32;
              jjCheckNAddStates(107, 114); }
            break;
          case 94: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 32)
                kind = 32;
              jjCheckNAddStates(115, 117); }
            break;
          case 96: 
          case 98: 
          case 101: 
          case 105: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(94);
            break;
          case 97: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 98;
            break;
          case 99: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 100;
            break;
          case 100: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 101;
            break;
          case 102: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 103;
            break;
          case 103: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 104;
            break;
          case 104: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 105;
            break;
          case 106: 
            if (curChar == 92)
              jjCheckNAddTwoStates(92, 107);
            break;
          case 107: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 32)
                kind = 32;
              jjCheckNAddStates(118, 125); }
            break;
          case 108: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 32)
                kind = 32;
              jjCheckNAddStates(126, 128); }
            break;
          case 109: 
          case 111: 
          case 114: 
          case 118: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(108);
            break;
          case 110: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 111;
            break;
          case 112: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 113;
            break;
          case 113: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 114;
            break;
          case 115: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 116;
            break;
          case 116: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 117;
            break;
          case 117: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 118;
            break;
          case 119: 
            if (curChar == 92)
              jjCheckNAddTwoStates(4, 120);
            break;
          case 120: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 3)
                kind = 3;
              jjCheckNAddStates(129, 136); }
            break;
          case 121: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 3)
                kind = 3;
              jjCheckNAddStates(137, 139); }
            break;
          case 122: 
          case 124: 
          case 127: 
          case 131: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(121);
            break;
          case 123: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 124;
            break;
          case 125: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 126;
            break;
          case 126: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 127;
            break;
          case 128: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 129;
            break;
          case 129: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 130;
            break;
          case 130: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 131;
            break;
          case 132: 
            if ((0x20000000200000 & l) != 0L)
              jjAddStates(242, 243);
            break;
          case 134: 
            if ((0x7FFFFFFFEFFFFFFF & l) != 0L)
              jjCheckNAddStates(146, 149);
            break;
          case 137: 
            if (curChar == 92)
              jjAddStates(254, 255);
            break;
          case 138: 
            if ((0x7FFFFFFFFFFFFFFF & l) != 0L)
              jjCheckNAddStates(146, 149);
            break;
          case 139: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAddStates(150, 158);
            break;
          case 140: 
            if ((0x7E0000007E & l) != 0L) {
              jjCheckNAddStates(159, 162);
            }
            break;
          case 142: case 144: 
          case 147: 
          case 151: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(140);
            break;
          case 143: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 144;
            break;
          case 145: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 146;
            break;
          case 146: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 147;
            break;
          case 148: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 149;
            break;
          case 149: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 150;
            break;
          case 150: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 151;
            break;
          case 153: 
            if ((0x7FFFFFFFEFFFFFFF & l) != 0L)
              jjCheckNAddStates(163, 165);
            break;
          case 155: 
            if (curChar == 92)
              jjAddStates(256, 259);
            break;
          case 159: 
            if ((0x7FFFFFFFFFFFFFFF & l) != 0L)
              jjCheckNAddStates(163, 165);
            break;
          case 160: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAddStates(166, 174);
            break;
          case 161: 
            if ((0x7E0000007E & l) != 0L) {
              jjCheckNAddStates(175, 178);
            }
            break;
          case 163: case 165: 
          case 168: 
          case 172: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(161);
            break;
          case 164: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 165;
            break;
          case 166: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 167;
            break;
          case 167: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 168;
            break;
          case 169: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 170;
            break;
          case 170: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 171;
            break;
          case 171: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 172;
            break;
          case 174: 
            if ((0x7FFFFFFFEFFFFFFF & l) != 0L)
              jjCheckNAddStates(179, 181);
            break;
          case 176: 
            if (curChar == 92)
              jjAddStates(260, 263);
            break;
          case 180: 
            if ((0x7FFFFFFFFFFFFFFF & l) != 0L)
              jjCheckNAddStates(179, 181);
            break;
          case 181: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAddStates(182, 190);
            break;
          case 182: 
            if ((0x7E0000007E & l) != 0L) {
              jjCheckNAddStates(191, 194);
            }
            break;
          case 184: case 186: 
          case 189: 
          case 193: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(182);
            break;
          case 185: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 186;
            break;
          case 187: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 188;
            break;
          case 188: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 189;
            break;
          case 190: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 191;
            break;
          case 191: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 192;
            break;
          case 192: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 193;
            break;
          case 195: 
            if ((0x100000001000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 133;
            break;
          case 196: 
            if ((0x4000000040000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 195;
            break;
          case 199: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 47)
                kind = 47;
              jjCheckNAddStates(205, 213); }
            break;
          case 200: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(201);
            break;
          case 202: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 47)
                kind = 47;
              jjCheckNAddStates(214, 218); }
            break;
          case 203: 
            if (((0x7E0000007E & l) != 0L) && (kind > 47)) {
              kind = 47;
            }
            break;
          case 204: case 206: 
          case 209: 
          case 213: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(203);
            break;
          case 205: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 206;
            break;
          case 207: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 208;
            break;
          case 208: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 209;
            break;
          case 210: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 211;
            break;
          case 211: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 212;
            break;
          case 212: 
            if ((0x7E0000007E & l) != 0L) {
              jjstateSet[(jjnewStateCnt++)] = 213;
            }
            break;
          case 214: case 216: 
          case 219: 
          case 223: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(200);
            break;
          case 215: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 216;
            break;
          case 217: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 218;
            break;
          case 218: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 219;
            break;
          case 220: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 221;
            break;
          case 221: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 222;
            break;
          case 222: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 223;
            break;
          case 224: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 47)
                kind = 47;
              jjCheckNAddStates(219, 221); }
            break;
          case 225: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 47)
                kind = 47;
              jjCheckNAddStates(222, 224); }
            break;
          case 226: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 47)
                kind = 47;
              jjCheckNAddStates(225, 227); }
            break;
          case 229: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 47)
                kind = 47;
              jjCheckNAddTwoStates(198, 203); }
            break;
          case 256: 
            if (((0x200000002000 & l) != 0L) && (kind > 33))
              kind = 33;
            break;
          case 257: 
            if ((0x2000000020 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 256;
            break;
          case 259: 
            if (((0x100000001000000 & l) != 0L) && (kind > 34))
              kind = 34;
            break;
          case 260: 
            if ((0x2000000020 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 259;
            break;
          case 262: 
            if (((0x100000001000000 & l) != 0L) && (kind > 35))
              kind = 35;
            break;
          case 263: 
            if ((0x1000000010000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 262;
            break;
          case 265: 
            if (((0x200000002000 & l) != 0L) && (kind > 36))
              kind = 36;
            break;
          case 266: 
            if ((0x800000008 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 265;
            break;
          case 268: 
            if (((0x200000002000 & l) != 0L) && (kind > 37))
              kind = 37;
            break;
          case 269: 
            if ((0x200000002000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 268;
            break;
          case 271: 
            if (((0x400000004000 & l) != 0L) && (kind > 38))
              kind = 38;
            break;
          case 272: 
            if ((0x20000000200 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 271;
            break;
          case 274: 
            if (((0x10000000100000 & l) != 0L) && (kind > 39))
              kind = 39;
            break;
          case 275: 
            if ((0x1000000010000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 274;
            break;
          case 277: 
            if (((0x800000008 & l) != 0L) && (kind > 40))
              kind = 40;
            break;
          case 278: 
            if ((0x1000000010000 & l) != 0L) {
              jjstateSet[(jjnewStateCnt++)] = 277;
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
          case 2: 
          case 4: 
          case 318: 
            if (jjCanMove_0(hiByte, i1, i2, l1, l2))
            {
              if (kind > 3)
                kind = 3;
              jjCheckNAddTwoStates(2, 3); }
            break;
          case 1: 
            if (jjCanMove_0(hiByte, i1, i2, l1, l2))
            {
              if (kind > 3)
                kind = 3;
              jjCheckNAddTwoStates(2, 3); }
            break;
          case 89: 
          case 92: 
            if (jjCanMove_0(hiByte, i1, i2, l1, l2))
            {
              if (kind > 32)
                kind = 32;
              jjCheckNAddTwoStates(90, 91); }
            break;
          case 90: 
          case 319: 
            if (jjCanMove_0(hiByte, i1, i2, l1, l2))
            {
              if (kind > 32)
                kind = 32;
              jjCheckNAddTwoStates(90, 91); }
            break;
          case 19: 
          case 21: 
            if (jjCanMove_0(hiByte, i1, i2, l1, l2))
            {
              if (kind > 9)
                kind = 9;
              jjCheckNAddTwoStates(19, 20); }
            break;
          case 36: 
          case 42: 
            if (jjCanMove_0(hiByte, i1, i2, l1, l2)) {
              jjCheckNAddStates(56, 58);
            }
            break;
          case 57: case 63: 
            if (jjCanMove_0(hiByte, i1, i2, l1, l2)) {
              jjCheckNAddStates(53, 55);
            }
            break;
          case 134: case 138: 
            if (jjCanMove_0(hiByte, i1, i2, l1, l2)) {
              jjCheckNAddStates(146, 149);
            }
            break;
          case 153: case 159: 
            if (jjCanMove_0(hiByte, i1, i2, l1, l2)) {
              jjCheckNAddStates(163, 165);
            }
            break;
          case 174: case 180: 
            if (jjCanMove_0(hiByte, i1, i2, l1, l2))
              jjCheckNAddStates(179, 181);
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
      if ((i = jjnewStateCnt) == (startsAt = 317 - (this.jjnewStateCnt = startsAt)))
        return curPos;
      try { curChar = input_stream.readChar(); } catch (IOException e) {} }
    return curPos;
  }
  
  private int jjMoveStringLiteralDfa0_1() {
    switch (curChar)
    {
    case 42: 
      return jjMoveStringLiteralDfa1_1(2L);
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
      if ((active1 & 0x2) != 0L)
        return jjStopAtPos(1, 65);
      break;
    default: 
      return 2;
    }
    return 2; }
  
  static final int[] jjnextStates = { 284, 285, 286, 257, 287, 288, 289, 260, 290, 291, 292, 263, 293, 294, 295, 266, 296, 297, 298, 269, 299, 300, 301, 272, 302, 303, 304, 275, 305, 306, 307, 278, 308, 309, 310, 280, 311, 312, 313, 314, 315, 316, 255, 258, 261, 264, 267, 270, 273, 276, 279, 281, 282, 57, 58, 59, 36, 37, 38, 2, 6, 8, 9, 11, 14, 7, 3, 2, 7, 3, 19, 23, 25, 26, 28, 31, 24, 20, 19, 24, 20, 36, 44, 46, 47, 49, 52, 45, 37, 38, 36, 45, 37, 38, 57, 65, 67, 68, 70, 73, 66, 58, 59, 57, 66, 58, 59, 90, 94, 96, 97, 99, 102, 95, 91, 90, 95, 91, 108, 109, 110, 112, 115, 95, 90, 91, 95, 90, 91, 121, 122, 123, 125, 128, 7, 2, 3, 7, 2, 3, 134, 152, 173, 136, 137, 194, 134, 135, 136, 137, 134, 140, 142, 143, 145, 148, 136, 137, 141, 134, 136, 137, 141, 153, 154, 155, 153, 161, 163, 164, 166, 169, 162, 154, 155, 153, 162, 154, 155, 174, 175, 176, 174, 182, 184, 185, 187, 190, 183, 175, 176, 174, 183, 175, 176, 134, 152, 173, 135, 136, 137, 194, 198, 199, 243, 200, 214, 215, 217, 220, 201, 198, 224, 236, 203, 204, 205, 207, 210, 198, 225, 232, 198, 226, 230, 198, 228, 229, 227, 233, 235, 227, 237, 239, 242, 247, 250, 252, 253, 227, 89, 106, 196, 197, 21, 22, 39, 41, 42, 43, 60, 62, 63, 64, 138, 139, 156, 158, 159, 160, 177, 179, 180, 181 };
  

















  private static final boolean jjCanMove_0(int hiByte, int i1, int i2, long l1, long l2)
  {
    switch (hiByte)
    {
    case 0: 
      return (jjbitVec2[i2] & l2) != 0L;
    }
    if ((jjbitVec0[i1] & l1) != 0L)
      return true;
    return false;
  }
  


  public static final String[] jjstrLiteralImages = { "", null, null, null, null, null, null, null, null, null, "{", "}", ",", ".", ";", ":", "*", "/", "+", "-", "=", ">", "[", "]", null, ")", null, "<!--", "-->", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null };
  











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
        if ((jjmatchedPos == 0) && (jjmatchedKind > 67))
        {
          jjmatchedKind = 67;
        }
        break;
      case 1: 
        jjmatchedKind = Integer.MAX_VALUE;
        jjmatchedPos = 0;
        curPos = jjMoveStringLiteralDfa0_1();
        if ((jjmatchedPos == 0) && (jjmatchedKind > 66))
        {
          jjmatchedKind = 66;
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
    case 24: 
      image.append(input_stream.GetSuffix(jjimageLen + (this.lengthOfMatch = jjmatchedPos + 1)));
      image = ParserUtils.trimBy(image, 1, 1);
      break;
    case 26: 
      image.append(input_stream.GetSuffix(jjimageLen + (this.lengthOfMatch = jjmatchedPos + 1)));
      image = ParserUtils.trimUrl(image);
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
      image = ParserUtils.trimBy(image, 0, 2);
      break;
    case 40: 
      image.append(input_stream.GetSuffix(jjimageLen + (this.lengthOfMatch = jjmatchedPos + 1)));
      image = ParserUtils.trimBy(image, 0, 2);
      break;
    case 41: 
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
  


  public SACParserCSSmobileOKBasic1TokenManager(CharStream stream)
  {
    input_stream = stream;
  }
  
  public SACParserCSSmobileOKBasic1TokenManager(CharStream stream, int lexState)
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
    for (int i = 317; i-- > 0;) {
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
  




  public static final int[] jjnewLexState = { -1, -1, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, -1, -1 };
  



  static final long[] jjtoToken = { 228698418577403L, 8L };
  

  static final long[] jjtoSkip = { 0L, 2L };
  

  static final long[] jjtoMore = { 4L, 4L };
  

  protected CharStream input_stream;
  
  private final int[] jjrounds = new int['Ľ'];
  private final int[] jjstateSet = new int['ɺ'];
  
  private final StringBuilder jjimage = new StringBuilder();
  private StringBuilder image = jjimage;
  private int jjimageLen;
  private int lengthOfMatch;
  protected int curChar;
}
