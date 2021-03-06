package com.steadystate.css.parser;

import java.io.IOException;
import java.io.PrintStream;










public class SACParserCSS2TokenManager
  implements SACParserCSS2Constants
{
  public PrintStream debugStream = System.out;
  
  public void setDebugStream(PrintStream ds) { debugStream = ds; }
  
  private final int jjStopStringLiteralDfa_0(int pos, long active0) { switch (pos)
    {
    case 0: 
      if ((active0 & 0x180001000000000) != 0L)
      {
        jjmatchedKind = 58;
        jjmatchedPos = 0;
        return 428;
      }
      if ((active0 & 0x3E0000000) != 0L)
        return 60;
      if ((active0 & 0x200) != 0L)
        return 429;
      return -1;
    case 1: 
      if ((active0 & 0x180001000000000) != 0L)
      {
        jjmatchedKind = 58;
        jjmatchedPos = 1;
        return 428;
      }
      if ((active0 & 0x3E0000000) != 0L)
      {
        jjmatchedKind = 34;
        jjmatchedPos = 1;
        return 430;
      }
      return -1;
    case 2: 
      if ((active0 & 0x180001000000000) != 0L)
      {
        jjmatchedKind = 58;
        jjmatchedPos = 2;
        return 428;
      }
      if ((active0 & 0x3E0000000) != 0L)
      {
        jjmatchedKind = 34;
        jjmatchedPos = 2;
        return 430;
      }
      return -1;
    case 3: 
      if ((active0 & 0x100001000000000) != 0L)
      {
        jjmatchedKind = 58;
        jjmatchedPos = 3;
        return 428;
      }
      if ((active0 & 0x3E0000000) != 0L)
      {
        jjmatchedKind = 34;
        jjmatchedPos = 3;
        return 430;
      }
      return -1;
    case 4: 
      if ((active0 & 0x40000000) != 0L)
        return 430;
      if ((active0 & 0x1000000000) != 0L)
      {
        jjmatchedKind = 58;
        jjmatchedPos = 4;
        return 428;
      }
      if ((active0 & 0x3A0000000) != 0L)
      {
        jjmatchedKind = 34;
        jjmatchedPos = 4;
        return 430;
      }
      return -1;
    case 5: 
      if ((active0 & 0x1000000000) != 0L)
      {
        jjmatchedKind = 58;
        jjmatchedPos = 5;
        return 428;
      }
      if ((active0 & 0x320000000) != 0L)
      {
        jjmatchedKind = 34;
        jjmatchedPos = 5;
        return 430;
      }
      if ((active0 & 0x80000000) != 0L)
        return 430;
      return -1;
    case 6: 
      if ((active0 & 0x300000000) != 0L)
      {
        jjmatchedKind = 34;
        jjmatchedPos = 6;
        return 430;
      }
      if ((active0 & 0x1000000000) != 0L)
        return 428;
      if ((active0 & 0x20000000) != 0L)
        return 430;
      return -1;
    case 7: 
      if ((active0 & 0x200000000) != 0L)
        return 430;
      if ((active0 & 0x100000000) != 0L)
      {
        jjmatchedKind = 34;
        jjmatchedPos = 7;
        return 430;
      }
      return -1;
    case 8: 
      if ((active0 & 0x100000000) != 0L)
      {
        jjmatchedKind = 34;
        jjmatchedPos = 8;
        return 430;
      }
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
      return jjStopAtPos(0, 22);
    case 42: 
      return jjStopAtPos(0, 12);
    case 43: 
      return jjStopAtPos(0, 14);
    case 44: 
      return jjStopAtPos(0, 8);
    
    case 45: 
      jjmatchedKind = 15;
      jjmatchedPos = 0;
      
      return jjMoveStringLiteralDfa1_0(67108864L);
    case 46: 
      return jjStartNfaWithStates_0(0, 9, 429);
    
    case 47: 
      jjmatchedKind = 13;
      jjmatchedPos = 0;
      
      return jjMoveStringLiteralDfa1_0(8L);
    case 58: 
      return jjStopAtPos(0, 11);
    case 59: 
      return jjStopAtPos(0, 10);
    case 60: 
      return jjMoveStringLiteralDfa1_0(33554432L);
    case 61: 
      return jjStopAtPos(0, 16);
    case 62: 
      return jjStopAtPos(0, 17);
    case 64: 
      return jjMoveStringLiteralDfa1_0(16642998272L);
    case 91: 
      return jjStopAtPos(0, 18);
    case 93: 
      return jjStopAtPos(0, 19);
    case 73: 
    case 105: 
      return jjMoveStringLiteralDfa1_0(68719476736L);
    case 76: 
    case 108: 
      return jjMoveStringLiteralDfa1_0(72057594037927936L);
    case 82: 
    case 114: 
      return jjMoveStringLiteralDfa1_0(36028797018963968L);
    case 123: 
      return jjStopAtPos(0, 6);
    case 124: 
      return jjMoveStringLiteralDfa1_0(268435456L);
    case 125: 
      return jjStopAtPos(0, 7);
    case 126: 
      return jjMoveStringLiteralDfa1_0(134217728L);
    }
    return jjMoveNfa_0(0, 0);
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
      return jjMoveStringLiteralDfa2_0(active0, 33554432L);
    case 42: 
      if ((active0 & 0x8) != 0L)
        return jjStopAtPos(1, 3);
      break;
    case 45: 
      return jjMoveStringLiteralDfa2_0(active0, 67108864L);
    case 61: 
      if ((active0 & 0x8000000) != 0L)
        return jjStopAtPos(1, 27);
      if ((active0 & 0x10000000) != 0L) {
        return jjStopAtPos(1, 28);
      }
      break;
    case 65: case 97: 
      return jjMoveStringLiteralDfa2_0(active0, 72057594037927936L);
    case 67: 
    case 99: 
      return jjMoveStringLiteralDfa2_0(active0, 8589934592L);
    case 70: 
    case 102: 
      return jjMoveStringLiteralDfa2_0(active0, 4294967296L);
    case 71: 
    case 103: 
      return jjMoveStringLiteralDfa2_0(active0, 36028797018963968L);
    case 73: 
    case 105: 
      return jjMoveStringLiteralDfa2_0(active0, 536870912L);
    case 77: 
    case 109: 
      return jjMoveStringLiteralDfa2_0(active0, 2147483648L);
    case 78: 
    case 110: 
      return jjMoveStringLiteralDfa2_0(active0, 68719476736L);
    case 80: 
    case 112: 
      return jjMoveStringLiteralDfa2_0(active0, 1073741824L);
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
      return jjMoveStringLiteralDfa3_0(active0, 33554432L);
    case 62: 
      if ((active0 & 0x4000000) != 0L) {
        return jjStopAtPos(2, 26);
      }
      break;
    case 65: case 97: 
      return jjMoveStringLiteralDfa3_0(active0, 1073741824L);
    case 66: 
    case 98: 
      return jjMoveStringLiteralDfa3_0(active0, 36028797018963968L);
    case 69: 
    case 101: 
      return jjMoveStringLiteralDfa3_0(active0, 2147483648L);
    case 72: 
    case 104: 
      return jjMoveStringLiteralDfa3_0(active0, 77309411328L);
    case 77: 
    case 109: 
      return jjMoveStringLiteralDfa3_0(active0, 536870912L);
    case 78: 
    case 110: 
      return jjMoveStringLiteralDfa3_0(active0, 72057594037927936L);
    case 79: 
    case 111: 
      return jjMoveStringLiteralDfa3_0(active0, 4294967296L);
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
      if ((active0 & 0x80000000000000) != 0L)
        return jjStopAtPos(3, 55);
      break;
    case 45: 
      if ((active0 & 0x2000000) != 0L) {
        return jjStopAtPos(3, 25);
      }
      break;
    case 65: case 97: 
      return jjMoveStringLiteralDfa4_0(active0, 8589934592L);
    case 68: 
    case 100: 
      return jjMoveStringLiteralDfa4_0(active0, 2147483648L);
    case 69: 
    case 101: 
      return jjMoveStringLiteralDfa4_0(active0, 68719476736L);
    case 71: 
    case 103: 
      return jjMoveStringLiteralDfa4_0(active0, 72057595111669760L);
    case 78: 
    case 110: 
      return jjMoveStringLiteralDfa4_0(active0, 4294967296L);
    case 80: 
    case 112: 
      return jjMoveStringLiteralDfa4_0(active0, 536870912L);
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
    case 40: 
      if ((active0 & 0x100000000000000) != 0L) {
        return jjStopAtPos(4, 56);
      }
      break;
    case 69: case 101: 
      if ((active0 & 0x40000000) != 0L) {
        return jjStartNfaWithStates_0(4, 30, 430);
      }
      break;
    case 73: case 105: 
      return jjMoveStringLiteralDfa5_0(active0, 2147483648L);
    case 79: 
    case 111: 
      return jjMoveStringLiteralDfa5_0(active0, 536870912L);
    case 82: 
    case 114: 
      return jjMoveStringLiteralDfa5_0(active0, 77309411328L);
    case 84: 
    case 116: 
      return jjMoveStringLiteralDfa5_0(active0, 4294967296L);
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
    case 45: 
      return jjMoveStringLiteralDfa6_0(active0, 4294967296L);
    case 65: 
    case 97: 
      if ((active0 & 0x80000000) != 0L) {
        return jjStartNfaWithStates_0(5, 31, 430);
      }
      break;
    case 73: case 105: 
      return jjMoveStringLiteralDfa6_0(active0, 68719476736L);
    case 82: 
    case 114: 
      return jjMoveStringLiteralDfa6_0(active0, 536870912L);
    case 83: 
    case 115: 
      return jjMoveStringLiteralDfa6_0(active0, 8589934592L);
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
    case 69: 
    case 101: 
      return jjMoveStringLiteralDfa7_0(active0, 8589934592L);
    case 70: 
    case 102: 
      return jjMoveStringLiteralDfa7_0(active0, 4294967296L);
    case 84: 
    case 116: 
      if ((active0 & 0x20000000) != 0L)
        return jjStartNfaWithStates_0(6, 29, 430);
      if ((active0 & 0x1000000000) != 0L) {
        return jjStartNfaWithStates_0(6, 36, 428);
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
    case 65: 
    case 97: 
      return jjMoveStringLiteralDfa8_0(active0, 4294967296L);
    case 84: 
    case 116: 
      if ((active0 & 0x200000000) != 0L) {
        return jjStartNfaWithStates_0(7, 33, 430);
      }
      break;
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
    case 67: 
    case 99: 
      return jjMoveStringLiteralDfa9_0(active0, 4294967296L);
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
    case 69: 
    case 101: 
      if ((active0 & 0x100000000) != 0L) {
        return jjStartNfaWithStates_0(9, 32, 430);
      }
      break;
    }
    
    return jjStartNfa_0(8, active0);
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
    jjnewStateCnt = 428;
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
          case 61: 
          case 430: 
            if ((0x3FF200000000000 & l) != 0L)
            {
              if (kind > 34)
                kind = 34;
              jjCheckNAddTwoStates(61, 62); }
            break;
          case 0: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 54)
                kind = 54;
              jjCheckNAddStates(0, 74);
            }
            else if ((0x100003600 & l) != 0L)
            {
              if (kind > 1)
                kind = 1;
              jjCheckNAddTwoStates(102, 103);
            }
            else if (curChar == 46) {
              jjCheckNAddStates(75, 93);
            } else if (curChar == 33) {
              jjCheckNAddTwoStates(91, 100);
            } else if (curChar == 39) {
              jjCheckNAddStates(94, 96);
            } else if (curChar == 34) {
              jjCheckNAddStates(97, 99);
            } else if (curChar == 35) {
              jjCheckNAddTwoStates(1, 2);
            }
            break;
          case 429:  if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 60)
                kind = 60;
              jjCheckNAdd(310);
            }
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 54)
                kind = 54;
              jjCheckNAdd(309);
            }
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddStates(100, 102);
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(276, 277);
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(272, 275);
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(269, 271);
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(267, 268);
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(264, 266);
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(259, 263);
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(255, 258);
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(251, 254);
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(248, 250);
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(245, 247);
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(242, 244);
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(239, 241);
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(236, 238);
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(233, 235);
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(230, 232);
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(227, 229);
            break;
          case 428: 
            if ((0x3FF200000000000 & l) != 0L)
            {
              if (kind > 58)
                kind = 58;
              jjCheckNAddTwoStates(329, 330);
            }
            else if (curChar == 40)
            {
              if (kind > 57)
                kind = 57;
            }
            if ((0x3FF200000000000 & l) != 0L)
              jjCheckNAddStates(103, 105);
            break;
          case 1: 
            if ((0x3FF200000000000 & l) != 0L)
            {
              if (kind > 20)
                kind = 20;
              jjCheckNAddTwoStates(1, 2); }
            break;
          case 3: 
            if ((0xFFFFFFFF00000000 & l) != 0L)
            {
              if (kind > 20)
                kind = 20;
              jjCheckNAddTwoStates(1, 2); }
            break;
          case 4: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 20)
                kind = 20;
              jjCheckNAddStates(106, 113); }
            break;
          case 5: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 20)
                kind = 20;
              jjCheckNAddStates(114, 116); }
            break;
          case 6: 
            if ((0x100003600 & l) != 0L)
            {
              if (kind > 20)
                kind = 20;
              jjCheckNAddTwoStates(1, 2); }
            break;
          case 7: 
          case 9: 
          case 12: 
          case 16: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(5);
            break;
          case 8: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 9;
            break;
          case 10: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 11;
            break;
          case 11: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 12;
            break;
          case 13: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 14;
            break;
          case 14: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 15;
            break;
          case 15: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 16;
            break;
          case 17: 
            if (curChar == 34)
              jjCheckNAddStates(97, 99);
            break;
          case 18: 
            if ((0xFFFFFFFB00000200 & l) != 0L)
              jjCheckNAddStates(97, 99);
            break;
          case 19: 
            if ((curChar == 34) && (kind > 21))
              kind = 21;
            break;
          case 21: 
            if ((0x3400 & l) != 0L)
              jjCheckNAddStates(97, 99);
            break;
          case 22: 
            if (curChar == 10)
              jjCheckNAddStates(97, 99);
            break;
          case 23: 
            if (curChar == 13)
              jjstateSet[(jjnewStateCnt++)] = 22;
            break;
          case 24: 
            if ((0xFFFFFFFF00000000 & l) != 0L)
              jjCheckNAddStates(97, 99);
            break;
          case 25: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddStates(117, 125);
            break;
          case 26: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddStates(126, 129);
            break;
          case 27: 
            if ((0x100003600 & l) != 0L) {
              jjCheckNAddStates(97, 99);
            }
            break;
          case 28: case 30: 
          case 33: 
          case 37: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(26);
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
          case 34: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 35;
            break;
          case 35: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 36;
            break;
          case 36: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 37;
            break;
          case 38: 
            if (curChar == 39)
              jjCheckNAddStates(94, 96);
            break;
          case 39: 
            if ((0xFFFFFF7F00000200 & l) != 0L)
              jjCheckNAddStates(94, 96);
            break;
          case 40: 
            if ((curChar == 39) && (kind > 21))
              kind = 21;
            break;
          case 42: 
            if ((0x3400 & l) != 0L)
              jjCheckNAddStates(94, 96);
            break;
          case 43: 
            if (curChar == 10)
              jjCheckNAddStates(94, 96);
            break;
          case 44: 
            if (curChar == 13)
              jjstateSet[(jjnewStateCnt++)] = 43;
            break;
          case 45: 
            if ((0xFFFFFFFF00000000 & l) != 0L)
              jjCheckNAddStates(94, 96);
            break;
          case 46: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddStates(130, 138);
            break;
          case 47: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddStates(139, 142);
            break;
          case 48: 
            if ((0x100003600 & l) != 0L) {
              jjCheckNAddStates(94, 96);
            }
            break;
          case 49: case 51: 
          case 54: 
          case 58: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(47);
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
          case 55: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 56;
            break;
          case 56: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 57;
            break;
          case 57: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 58;
            break;
          case 63: 
            if ((0xFFFFFFFF00000000 & l) != 0L)
            {
              if (kind > 34)
                kind = 34;
              jjCheckNAddTwoStates(61, 62); }
            break;
          case 64: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 34)
                kind = 34;
              jjCheckNAddStates(143, 150); }
            break;
          case 65: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 34)
                kind = 34;
              jjCheckNAddStates(151, 153); }
            break;
          case 66: 
            if ((0x100003600 & l) != 0L)
            {
              if (kind > 34)
                kind = 34;
              jjCheckNAddTwoStates(61, 62); }
            break;
          case 67: 
          case 69: 
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
          case 78: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 34)
                kind = 34;
              jjCheckNAddStates(154, 161); }
            break;
          case 79: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 34)
                kind = 34;
              jjCheckNAddStates(162, 164); }
            break;
          case 80: 
          case 82: 
          case 85: 
          case 89: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(79);
            break;
          case 81: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 82;
            break;
          case 83: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 84;
            break;
          case 84: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 85;
            break;
          case 86: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 87;
            break;
          case 87: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 88;
            break;
          case 88: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 89;
            break;
          case 90: 
            if (curChar == 33)
              jjCheckNAddTwoStates(91, 100);
            break;
          case 91: 
            if ((0x100003600 & l) != 0L)
              jjCheckNAddTwoStates(91, 100);
            break;
          case 101: 
            if ((0x100003600 & l) != 0L)
            {
              if (kind > 1)
                kind = 1;
              jjCheckNAddTwoStates(102, 103); }
            break;
          case 102: 
            if ((0x100003600 & l) != 0L)
            {
              if (kind > 1)
                kind = 1;
              jjCheckNAdd(102); }
            break;
          case 103: 
            if ((0x100003600 & l) != 0L)
            {
              if (kind > 2)
                kind = 2;
              jjCheckNAdd(103); }
            break;
          case 105: 
            if (curChar == 40)
              jjCheckNAddStates(165, 170);
            break;
          case 106: 
            if ((0xFFFFFC7A00000000 & l) != 0L)
              jjCheckNAddStates(171, 174);
            break;
          case 107: 
            if ((0x100003600 & l) != 0L)
              jjCheckNAddTwoStates(107, 108);
            break;
          case 108: 
            if ((curChar == 41) && (kind > 24))
              kind = 24;
            break;
          case 110: 
            if ((0xFFFFFFFF00000000 & l) != 0L)
              jjCheckNAddStates(171, 174);
            break;
          case 111: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddStates(175, 183);
            break;
          case 112: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddStates(184, 187);
            break;
          case 113: 
            if ((0x100003600 & l) != 0L) {
              jjCheckNAddStates(171, 174);
            }
            break;
          case 114: case 116: 
          case 119: 
          case 123: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(112);
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
          case 120: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 121;
            break;
          case 121: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 122;
            break;
          case 122: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 123;
            break;
          case 124: 
            if (curChar == 39)
              jjCheckNAddStates(188, 190);
            break;
          case 125: 
            if ((0xFFFFFF7F00000200 & l) != 0L)
              jjCheckNAddStates(188, 190);
            break;
          case 126: 
            if (curChar == 39)
              jjCheckNAddTwoStates(107, 108);
            break;
          case 128: 
            if ((0x3400 & l) != 0L)
              jjCheckNAddStates(188, 190);
            break;
          case 129: 
            if (curChar == 10)
              jjCheckNAddStates(188, 190);
            break;
          case 130: 
            if (curChar == 13)
              jjstateSet[(jjnewStateCnt++)] = 129;
            break;
          case 131: 
            if ((0xFFFFFFFF00000000 & l) != 0L)
              jjCheckNAddStates(188, 190);
            break;
          case 132: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddStates(191, 199);
            break;
          case 133: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddStates(200, 203);
            break;
          case 134: 
            if ((0x100003600 & l) != 0L) {
              jjCheckNAddStates(188, 190);
            }
            break;
          case 135: case 137: 
          case 140: 
          case 144: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(133);
            break;
          case 136: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 137;
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
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 142;
            break;
          case 142: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 143;
            break;
          case 143: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 144;
            break;
          case 145: 
            if (curChar == 34)
              jjCheckNAddStates(204, 206);
            break;
          case 146: 
            if ((0xFFFFFFFB00000200 & l) != 0L)
              jjCheckNAddStates(204, 206);
            break;
          case 147: 
            if (curChar == 34)
              jjCheckNAddTwoStates(107, 108);
            break;
          case 149: 
            if ((0x3400 & l) != 0L)
              jjCheckNAddStates(204, 206);
            break;
          case 150: 
            if (curChar == 10)
              jjCheckNAddStates(204, 206);
            break;
          case 151: 
            if (curChar == 13)
              jjstateSet[(jjnewStateCnt++)] = 150;
            break;
          case 152: 
            if ((0xFFFFFFFF00000000 & l) != 0L)
              jjCheckNAddStates(204, 206);
            break;
          case 153: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddStates(207, 215);
            break;
          case 154: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddStates(216, 219);
            break;
          case 155: 
            if ((0x100003600 & l) != 0L) {
              jjCheckNAddStates(204, 206);
            }
            break;
          case 156: case 158: 
          case 161: 
          case 165: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(154);
            break;
          case 157: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 158;
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
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 163;
            break;
          case 163: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 164;
            break;
          case 164: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 165;
            break;
          case 166: 
            if ((0x100003600 & l) != 0L)
              jjCheckNAddStates(220, 226);
            break;
          case 169: 
            if (curChar == 43) {
              jjCheckNAddStates(227, 229);
            }
            break;
          case 170: case 199: 
            if ((curChar == 63) && (kind > 61))
              kind = 61;
            break;
          case 171: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 61)
                kind = 61;
              jjCheckNAddStates(230, 238); }
            break;
          case 172: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(173);
            break;
          case 173: 
            if (curChar == 45)
              jjstateSet[(jjnewStateCnt++)] = 174;
            break;
          case 174: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 61)
                kind = 61;
              jjCheckNAddStates(239, 243); }
            break;
          case 175: 
            if (((0x3FF000000000000 & l) != 0L) && (kind > 61)) {
              kind = 61;
            }
            break;
          case 176: case 178: 
          case 181: 
          case 185: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(175);
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
          case 182: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 183;
            break;
          case 183: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 184;
            break;
          case 184: 
            if ((0x3FF000000000000 & l) != 0L) {
              jjstateSet[(jjnewStateCnt++)] = 185;
            }
            break;
          case 186: case 188: 
          case 191: 
          case 195: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(172);
            break;
          case 187: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 188;
            break;
          case 189: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 190;
            break;
          case 190: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 191;
            break;
          case 192: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 193;
            break;
          case 193: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 194;
            break;
          case 194: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 195;
            break;
          case 196: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 61)
                kind = 61;
              jjCheckNAddStates(244, 246); }
            break;
          case 197: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 61)
                kind = 61;
              jjCheckNAddStates(247, 249); }
            break;
          case 198: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 61)
                kind = 61;
              jjCheckNAddStates(250, 252); }
            break;
          case 200: 
          case 203: 
          case 205: 
          case 206: 
          case 209: 
          case 210: 
          case 212: 
          case 216: 
          case 220: 
          case 223: 
          case 225: 
            if (curChar == 63)
              jjCheckNAdd(199);
            break;
          case 201: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 61)
                kind = 61;
              jjCheckNAddTwoStates(170, 175); }
            break;
          case 202: 
            if (curChar == 63)
              jjCheckNAddTwoStates(199, 203);
            break;
          case 204: 
            if (curChar == 63)
              jjCheckNAddStates(253, 255);
            break;
          case 207: 
            if (curChar == 63)
              jjstateSet[(jjnewStateCnt++)] = 206;
            break;
          case 208: 
            if (curChar == 63)
              jjCheckNAddStates(256, 259);
            break;
          case 211: 
            if (curChar == 63)
              jjstateSet[(jjnewStateCnt++)] = 210;
            break;
          case 213: 
            if (curChar == 63)
              jjstateSet[(jjnewStateCnt++)] = 212;
            break;
          case 214: 
            if (curChar == 63)
              jjstateSet[(jjnewStateCnt++)] = 213;
            break;
          case 215: 
            if (curChar == 63)
              jjCheckNAddStates(260, 264);
            break;
          case 217: 
            if (curChar == 63)
              jjstateSet[(jjnewStateCnt++)] = 216;
            break;
          case 218: 
            if (curChar == 63)
              jjstateSet[(jjnewStateCnt++)] = 217;
            break;
          case 219: 
            if (curChar == 63)
              jjstateSet[(jjnewStateCnt++)] = 218;
            break;
          case 221: 
            if (curChar == 63)
              jjstateSet[(jjnewStateCnt++)] = 220;
            break;
          case 222: 
            if (curChar == 63)
              jjstateSet[(jjnewStateCnt++)] = 221;
            break;
          case 224: 
            if (curChar == 63)
              jjstateSet[(jjnewStateCnt++)] = 223;
            break;
          case 226: 
            if (curChar == 46)
              jjCheckNAddStates(75, 93);
            break;
          case 227: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(227, 229);
            break;
          case 230: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(230, 232);
            break;
          case 233: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(233, 235);
            break;
          case 236: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(236, 238);
            break;
          case 239: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(239, 241);
            break;
          case 242: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(242, 244);
            break;
          case 245: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(245, 247);
            break;
          case 248: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(248, 250);
            break;
          case 251: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(251, 254);
            break;
          case 255: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(255, 258);
            break;
          case 259: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(259, 263);
            break;
          case 264: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(264, 266);
            break;
          case 267: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(267, 268);
            break;
          case 269: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(269, 271);
            break;
          case 272: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(272, 275);
            break;
          case 276: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(276, 277);
            break;
          case 277: 
            if ((curChar == 37) && (kind > 52))
              kind = 52;
            break;
          case 278: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddStates(100, 102);
            break;
          case 280: 
            if ((0x3FF200000000000 & l) != 0L)
            {
              if (kind > 53)
                kind = 53;
              jjCheckNAddTwoStates(280, 281); }
            break;
          case 282: 
            if ((0xFFFFFFFF00000000 & l) != 0L)
            {
              if (kind > 53)
                kind = 53;
              jjCheckNAddTwoStates(280, 281); }
            break;
          case 283: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 53)
                kind = 53;
              jjCheckNAddStates(265, 272); }
            break;
          case 284: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 53)
                kind = 53;
              jjCheckNAddStates(273, 275); }
            break;
          case 285: 
            if ((0x100003600 & l) != 0L)
            {
              if (kind > 53)
                kind = 53;
              jjCheckNAddTwoStates(280, 281); }
            break;
          case 286: 
          case 288: 
          case 291: 
          case 295: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(284);
            break;
          case 287: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 288;
            break;
          case 289: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 290;
            break;
          case 290: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 291;
            break;
          case 292: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 293;
            break;
          case 293: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 294;
            break;
          case 294: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 295;
            break;
          case 297: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 53)
                kind = 53;
              jjCheckNAddStates(276, 283); }
            break;
          case 298: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 53)
                kind = 53;
              jjCheckNAddStates(284, 286); }
            break;
          case 299: 
          case 301: 
          case 304: 
          case 308: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(298);
            break;
          case 300: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 301;
            break;
          case 302: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 303;
            break;
          case 303: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 304;
            break;
          case 305: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 306;
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
            {
              if (kind > 54)
                kind = 54;
              jjCheckNAdd(309); }
            break;
          case 310: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 60)
                kind = 60;
              jjCheckNAdd(310); }
            break;
          case 312: 
            if ((0x3FF200000000000 & l) != 0L)
              jjCheckNAddStates(103, 105);
            break;
          case 313: 
            if ((curChar == 40) && (kind > 57))
              kind = 57;
            break;
          case 315: 
            if ((0xFFFFFFFF00000000 & l) != 0L)
              jjCheckNAddStates(103, 105);
            break;
          case 316: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddStates(287, 295);
            break;
          case 317: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddStates(296, 299);
            break;
          case 318: 
            if ((0x100003600 & l) != 0L) {
              jjCheckNAddStates(103, 105);
            }
            break;
          case 319: case 321: 
          case 324: 
          case 328: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(317);
            break;
          case 320: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 321;
            break;
          case 322: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 323;
            break;
          case 323: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 324;
            break;
          case 325: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 326;
            break;
          case 326: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 327;
            break;
          case 327: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 328;
            break;
          case 329: 
            if ((0x3FF200000000000 & l) != 0L)
            {
              if (kind > 58)
                kind = 58;
              jjCheckNAddTwoStates(329, 330); }
            break;
          case 331: 
            if ((0xFFFFFFFF00000000 & l) != 0L)
            {
              if (kind > 58)
                kind = 58;
              jjCheckNAddTwoStates(329, 330); }
            break;
          case 332: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 58)
                kind = 58;
              jjCheckNAddStates(300, 307); }
            break;
          case 333: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 58)
                kind = 58;
              jjCheckNAddStates(308, 310); }
            break;
          case 334: 
            if ((0x100003600 & l) != 0L)
            {
              if (kind > 58)
                kind = 58;
              jjCheckNAddTwoStates(329, 330); }
            break;
          case 335: 
          case 337: 
          case 340: 
          case 344: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(333);
            break;
          case 336: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 337;
            break;
          case 338: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 339;
            break;
          case 339: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 340;
            break;
          case 341: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 342;
            break;
          case 342: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 343;
            break;
          case 343: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 344;
            break;
          case 345: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 54)
                kind = 54;
              jjCheckNAddStates(0, 74); }
            break;
          case 346: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(346, 229);
            break;
          case 347: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(347, 348);
            break;
          case 348: 
            if (curChar == 46)
              jjCheckNAdd(227);
            break;
          case 349: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(349, 232);
            break;
          case 350: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(350, 351);
            break;
          case 351: 
            if (curChar == 46)
              jjCheckNAdd(230);
            break;
          case 352: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(352, 235);
            break;
          case 353: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(353, 354);
            break;
          case 354: 
            if (curChar == 46)
              jjCheckNAdd(233);
            break;
          case 355: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(355, 238);
            break;
          case 356: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(356, 357);
            break;
          case 357: 
            if (curChar == 46)
              jjCheckNAdd(236);
            break;
          case 358: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(358, 241);
            break;
          case 359: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(359, 360);
            break;
          case 360: 
            if (curChar == 46)
              jjCheckNAdd(239);
            break;
          case 361: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(361, 244);
            break;
          case 362: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(362, 363);
            break;
          case 363: 
            if (curChar == 46)
              jjCheckNAdd(242);
            break;
          case 364: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(364, 247);
            break;
          case 365: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(365, 366);
            break;
          case 366: 
            if (curChar == 46)
              jjCheckNAdd(245);
            break;
          case 367: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(367, 250);
            break;
          case 368: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(368, 369);
            break;
          case 369: 
            if (curChar == 46)
              jjCheckNAdd(248);
            break;
          case 370: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(370, 254);
            break;
          case 371: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(371, 372);
            break;
          case 372: 
            if (curChar == 46)
              jjCheckNAdd(251);
            break;
          case 373: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(373, 258);
            break;
          case 374: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(374, 375);
            break;
          case 375: 
            if (curChar == 46)
              jjCheckNAdd(255);
            break;
          case 376: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(376, 263);
            break;
          case 377: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(377, 378);
            break;
          case 378: 
            if (curChar == 46)
              jjCheckNAdd(259);
            break;
          case 379: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(379, 266);
            break;
          case 380: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(380, 381);
            break;
          case 381: 
            if (curChar == 46)
              jjCheckNAdd(264);
            break;
          case 382: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(382, 268);
            break;
          case 383: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(383, 384);
            break;
          case 384: 
            if (curChar == 46)
              jjCheckNAdd(267);
            break;
          case 385: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(385, 271);
            break;
          case 386: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(386, 387);
            break;
          case 387: 
            if (curChar == 46)
              jjCheckNAdd(269);
            break;
          case 388: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(388, 275);
            break;
          case 389: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(389, 390);
            break;
          case 390: 
            if (curChar == 46)
              jjCheckNAdd(272);
            break;
          case 391: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(391, 277);
            break;
          case 392: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(392, 393);
            break;
          case 393: 
            if (curChar == 46)
              jjCheckNAdd(276);
            break;
          case 394: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddStates(311, 313);
            break;
          case 395: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(395, 396);
            break;
          case 396: 
            if (curChar == 46)
              jjCheckNAdd(278);
            break;
          case 397: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 54)
                kind = 54;
              jjCheckNAdd(397); }
            break;
          case 398: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(398, 399);
            break;
          case 399: 
            if (curChar == 46)
              jjCheckNAdd(309);
            break;
          case 400: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 60)
                kind = 60;
              jjCheckNAdd(400); }
            break;
          case 401: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddTwoStates(401, 402);
            break;
          case 402: 
            if (curChar == 46)
              jjCheckNAdd(310);
            break;
          case 404: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 58)
                kind = 58;
              jjCheckNAddStates(314, 321); }
            break;
          case 405: 
            if ((0x3FF000000000000 & l) != 0L)
            {
              if (kind > 58)
                kind = 58;
              jjCheckNAddStates(322, 324); }
            break;
          case 406: 
          case 408: 
          case 411: 
          case 415: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(405);
            break;
          case 407: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 408;
            break;
          case 409: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 410;
            break;
          case 410: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 411;
            break;
          case 412: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 413;
            break;
          case 413: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 414;
            break;
          case 414: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 415;
            break;
          case 416: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAddStates(325, 333);
            break;
          case 417: 
            if ((0x3FF000000000000 & l) != 0L) {
              jjCheckNAddStates(334, 337);
            }
            break;
          case 418: case 420: 
          case 423: 
          case 427: 
            if ((0x3FF000000000000 & l) != 0L)
              jjCheckNAdd(417);
            break;
          case 419: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 420;
            break;
          case 421: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 422;
            break;
          case 422: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 423;
            break;
          case 424: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 425;
            break;
          case 425: 
            if ((0x3FF000000000000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 426;
            break;
          case 426: 
            if ((0x3FF000000000000 & l) != 0L) {
              jjstateSet[(jjnewStateCnt++)] = 427;
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
          case 60: 
            if ((0x7FFFFFE87FFFFFE & l) != 0L)
            {
              if (kind > 34)
                kind = 34;
              jjCheckNAddTwoStates(61, 62);
            }
            else if (curChar == 92) {
              jjCheckNAddTwoStates(63, 78);
            }
            break;
          case 430:  if ((0x7FFFFFE87FFFFFE & l) != 0L)
            {
              if (kind > 34)
                kind = 34;
              jjCheckNAddTwoStates(61, 62);
            }
            else if (curChar == 92) {
              jjCheckNAddTwoStates(63, 64);
            }
            break;
          case 0:  if ((0x7FFFFFE87FFFFFE & l) != 0L)
            {
              if (kind > 58)
                kind = 58;
              jjCheckNAddStates(338, 342);
            }
            else if (curChar == 92) {
              jjCheckNAddStates(343, 346);
            } else if (curChar == 64) {
              jjAddStates(347, 348); }
            if ((0x20000000200000 & l) != 0L)
              jjAddStates(349, 350);
            break;
          case 428: 
            if ((0x7FFFFFE87FFFFFE & l) != 0L)
            {
              if (kind > 58)
                kind = 58;
              jjCheckNAddTwoStates(329, 330);
            }
            else if (curChar == 92) {
              jjCheckNAddTwoStates(315, 316); }
            if ((0x7FFFFFE87FFFFFE & l) != 0L) {
              jjCheckNAddStates(103, 105);
            } else if (curChar == 92)
              jjCheckNAddTwoStates(331, 332);
            break;
          case 1: 
            if ((0x7FFFFFE87FFFFFE & l) != 0L)
            {
              if (kind > 20)
                kind = 20;
              jjCheckNAddTwoStates(1, 2); }
            break;
          case 2: 
            if (curChar == 92)
              jjAddStates(351, 352);
            break;
          case 3: 
            if ((0x7FFFFFFFFFFFFFFF & l) != 0L)
            {
              if (kind > 20)
                kind = 20;
              jjCheckNAddTwoStates(1, 2); }
            break;
          case 4: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 20)
                kind = 20;
              jjCheckNAddStates(106, 113); }
            break;
          case 5: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 20)
                kind = 20;
              jjCheckNAddStates(114, 116); }
            break;
          case 7: 
          case 9: 
          case 12: 
          case 16: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(5);
            break;
          case 8: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 9;
            break;
          case 10: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 11;
            break;
          case 11: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 12;
            break;
          case 13: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 14;
            break;
          case 14: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 15;
            break;
          case 15: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 16;
            break;
          case 18: 
            if ((0x7FFFFFFFEFFFFFFF & l) != 0L)
              jjCheckNAddStates(97, 99);
            break;
          case 20: 
            if (curChar == 92)
              jjAddStates(353, 356);
            break;
          case 24: 
            if ((0x7FFFFFFFFFFFFFFF & l) != 0L)
              jjCheckNAddStates(97, 99);
            break;
          case 25: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAddStates(117, 125);
            break;
          case 26: 
            if ((0x7E0000007E & l) != 0L) {
              jjCheckNAddStates(126, 129);
            }
            break;
          case 28: case 30: 
          case 33: 
          case 37: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(26);
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
          case 34: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 35;
            break;
          case 35: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 36;
            break;
          case 36: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 37;
            break;
          case 39: 
            if ((0x7FFFFFFFEFFFFFFF & l) != 0L)
              jjCheckNAddStates(94, 96);
            break;
          case 41: 
            if (curChar == 92)
              jjAddStates(357, 360);
            break;
          case 45: 
            if ((0x7FFFFFFFFFFFFFFF & l) != 0L)
              jjCheckNAddStates(94, 96);
            break;
          case 46: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAddStates(130, 138);
            break;
          case 47: 
            if ((0x7E0000007E & l) != 0L) {
              jjCheckNAddStates(139, 142);
            }
            break;
          case 49: case 51: 
          case 54: 
          case 58: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(47);
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
          case 55: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 56;
            break;
          case 56: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 57;
            break;
          case 57: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 58;
            break;
          case 59: 
            if (curChar == 64)
              jjAddStates(347, 348);
            break;
          case 61: 
            if ((0x7FFFFFE87FFFFFE & l) != 0L)
            {
              if (kind > 34)
                kind = 34;
              jjCheckNAddTwoStates(61, 62); }
            break;
          case 62: 
            if (curChar == 92)
              jjCheckNAddTwoStates(63, 64);
            break;
          case 63: 
            if ((0x7FFFFFFFFFFFFFFF & l) != 0L)
            {
              if (kind > 34)
                kind = 34;
              jjCheckNAddTwoStates(61, 62); }
            break;
          case 64: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 34)
                kind = 34;
              jjCheckNAddStates(143, 150); }
            break;
          case 65: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 34)
                kind = 34;
              jjCheckNAddStates(151, 153); }
            break;
          case 67: 
          case 69: 
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
          case 77: 
            if (curChar == 92)
              jjCheckNAddTwoStates(63, 78);
            break;
          case 78: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 34)
                kind = 34;
              jjCheckNAddStates(154, 161); }
            break;
          case 79: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 34)
                kind = 34;
              jjCheckNAddStates(162, 164); }
            break;
          case 80: 
          case 82: 
          case 85: 
          case 89: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(79);
            break;
          case 81: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 82;
            break;
          case 83: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 84;
            break;
          case 84: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 85;
            break;
          case 86: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 87;
            break;
          case 87: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 88;
            break;
          case 88: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 89;
            break;
          case 92: 
            if (((0x10000000100000 & l) != 0L) && (kind > 35))
              kind = 35;
            break;
          case 93: 
            if ((0x400000004000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 92;
            break;
          case 94: 
            if ((0x200000002 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 93;
            break;
          case 95: 
            if ((0x10000000100000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 94;
            break;
          case 96: 
            if ((0x4000000040000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 95;
            break;
          case 97: 
            if ((0x800000008000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 96;
            break;
          case 98: 
            if ((0x1000000010000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 97;
            break;
          case 99: 
            if ((0x200000002000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 98;
            break;
          case 100: 
            if ((0x20000000200 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 99;
            break;
          case 104: 
            if ((0x20000000200000 & l) != 0L)
              jjAddStates(349, 350);
            break;
          case 106: 
            if ((0x7FFFFFFFEFFFFFFF & l) != 0L)
              jjCheckNAddStates(171, 174);
            break;
          case 109: 
            if (curChar == 92)
              jjAddStates(361, 362);
            break;
          case 110: 
            if ((0x7FFFFFFFFFFFFFFF & l) != 0L)
              jjCheckNAddStates(171, 174);
            break;
          case 111: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAddStates(175, 183);
            break;
          case 112: 
            if ((0x7E0000007E & l) != 0L) {
              jjCheckNAddStates(184, 187);
            }
            break;
          case 114: case 116: 
          case 119: 
          case 123: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(112);
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
          case 120: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 121;
            break;
          case 121: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 122;
            break;
          case 122: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 123;
            break;
          case 125: 
            if ((0x7FFFFFFFEFFFFFFF & l) != 0L)
              jjCheckNAddStates(188, 190);
            break;
          case 127: 
            if (curChar == 92)
              jjAddStates(363, 366);
            break;
          case 131: 
            if ((0x7FFFFFFFFFFFFFFF & l) != 0L)
              jjCheckNAddStates(188, 190);
            break;
          case 132: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAddStates(191, 199);
            break;
          case 133: 
            if ((0x7E0000007E & l) != 0L) {
              jjCheckNAddStates(200, 203);
            }
            break;
          case 135: case 137: 
          case 140: 
          case 144: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(133);
            break;
          case 136: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 137;
            break;
          case 138: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 139;
            break;
          case 139: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 140;
            break;
          case 141: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 142;
            break;
          case 142: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 143;
            break;
          case 143: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 144;
            break;
          case 146: 
            if ((0x7FFFFFFFEFFFFFFF & l) != 0L)
              jjCheckNAddStates(204, 206);
            break;
          case 148: 
            if (curChar == 92)
              jjAddStates(367, 370);
            break;
          case 152: 
            if ((0x7FFFFFFFFFFFFFFF & l) != 0L)
              jjCheckNAddStates(204, 206);
            break;
          case 153: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAddStates(207, 215);
            break;
          case 154: 
            if ((0x7E0000007E & l) != 0L) {
              jjCheckNAddStates(216, 219);
            }
            break;
          case 156: case 158: 
          case 161: 
          case 165: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(154);
            break;
          case 157: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 158;
            break;
          case 159: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 160;
            break;
          case 160: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 161;
            break;
          case 162: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 163;
            break;
          case 163: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 164;
            break;
          case 164: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 165;
            break;
          case 167: 
            if ((0x100000001000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 105;
            break;
          case 168: 
            if ((0x4000000040000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 167;
            break;
          case 171: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 61)
                kind = 61;
              jjCheckNAddStates(230, 238); }
            break;
          case 172: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(173);
            break;
          case 174: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 61)
                kind = 61;
              jjCheckNAddStates(239, 243); }
            break;
          case 175: 
            if (((0x7E0000007E & l) != 0L) && (kind > 61)) {
              kind = 61;
            }
            break;
          case 176: case 178: 
          case 181: 
          case 185: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(175);
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
          case 182: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 183;
            break;
          case 183: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 184;
            break;
          case 184: 
            if ((0x7E0000007E & l) != 0L) {
              jjstateSet[(jjnewStateCnt++)] = 185;
            }
            break;
          case 186: case 188: 
          case 191: 
          case 195: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(172);
            break;
          case 187: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 188;
            break;
          case 189: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 190;
            break;
          case 190: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 191;
            break;
          case 192: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 193;
            break;
          case 193: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 194;
            break;
          case 194: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 195;
            break;
          case 196: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 61)
                kind = 61;
              jjCheckNAddStates(244, 246); }
            break;
          case 197: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 61)
                kind = 61;
              jjCheckNAddStates(247, 249); }
            break;
          case 198: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 61)
                kind = 61;
              jjCheckNAddStates(250, 252); }
            break;
          case 201: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 61)
                kind = 61;
              jjCheckNAddTwoStates(170, 175); }
            break;
          case 228: 
            if (((0x200000002000 & l) != 0L) && (kind > 37))
              kind = 37;
            break;
          case 229: 
            if ((0x2000000020 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 228;
            break;
          case 231: 
            if (((0x100000001000000 & l) != 0L) && (kind > 38))
              kind = 38;
            break;
          case 232: 
            if ((0x2000000020 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 231;
            break;
          case 234: 
            if (((0x100000001000000 & l) != 0L) && (kind > 39))
              kind = 39;
            break;
          case 235: 
            if ((0x1000000010000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 234;
            break;
          case 237: 
            if (((0x200000002000 & l) != 0L) && (kind > 40))
              kind = 40;
            break;
          case 238: 
            if ((0x800000008 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 237;
            break;
          case 240: 
            if (((0x200000002000 & l) != 0L) && (kind > 41))
              kind = 41;
            break;
          case 241: 
            if ((0x200000002000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 240;
            break;
          case 243: 
            if (((0x400000004000 & l) != 0L) && (kind > 42))
              kind = 42;
            break;
          case 244: 
            if ((0x20000000200 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 243;
            break;
          case 246: 
            if (((0x10000000100000 & l) != 0L) && (kind > 43))
              kind = 43;
            break;
          case 247: 
            if ((0x1000000010000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 246;
            break;
          case 249: 
            if (((0x800000008 & l) != 0L) && (kind > 44))
              kind = 44;
            break;
          case 250: 
            if ((0x1000000010000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 249;
            break;
          case 252: 
            if (((0x8000000080 & l) != 0L) && (kind > 45))
              kind = 45;
            break;
          case 253: 
            if ((0x2000000020 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 252;
            break;
          case 254: 
            if ((0x1000000010 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 253;
            break;
          case 256: 
            if (((0x1000000010 & l) != 0L) && (kind > 46))
              kind = 46;
            break;
          case 257: 
            if ((0x200000002 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 256;
            break;
          case 258: 
            if ((0x4000000040000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 257;
            break;
          case 260: 
            if (((0x1000000010 & l) != 0L) && (kind > 47))
              kind = 47;
            break;
          case 261: 
            if ((0x200000002 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 260;
            break;
          case 262: 
            if ((0x4000000040000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 261;
            break;
          case 263: 
            if ((0x8000000080 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 262;
            break;
          case 265: 
            if (((0x8000000080000 & l) != 0L) && (kind > 48))
              kind = 48;
            break;
          case 266: 
            if ((0x200000002000 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 265;
            break;
          case 268: 
            if (((0x8000000080000 & l) != 0L) && (kind > 49))
              kind = 49;
            break;
          case 270: 
            if (((0x400000004000000 & l) != 0L) && (kind > 50))
              kind = 50;
            break;
          case 271: 
            if ((0x10000000100 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 270;
            break;
          case 273: 
            if (((0x400000004000000 & l) != 0L) && (kind > 51))
              kind = 51;
            break;
          case 274: 
            if ((0x10000000100 & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 273;
            break;
          case 275: 
            if ((0x80000000800 & l) != 0L) {
              jjstateSet[(jjnewStateCnt++)] = 274;
            }
            break;
          case 279: case 280: 
            if ((0x7FFFFFE87FFFFFE & l) != 0L)
            {
              if (kind > 53)
                kind = 53;
              jjCheckNAddTwoStates(280, 281); }
            break;
          case 281: 
            if (curChar == 92)
              jjCheckNAddTwoStates(282, 283);
            break;
          case 282: 
            if ((0x7FFFFFFFFFFFFFFF & l) != 0L)
            {
              if (kind > 53)
                kind = 53;
              jjCheckNAddTwoStates(280, 281); }
            break;
          case 283: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 53)
                kind = 53;
              jjCheckNAddStates(265, 272); }
            break;
          case 284: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 53)
                kind = 53;
              jjCheckNAddStates(273, 275); }
            break;
          case 286: 
          case 288: 
          case 291: 
          case 295: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(284);
            break;
          case 287: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 288;
            break;
          case 289: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 290;
            break;
          case 290: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 291;
            break;
          case 292: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 293;
            break;
          case 293: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 294;
            break;
          case 294: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 295;
            break;
          case 296: 
            if (curChar == 92)
              jjCheckNAddTwoStates(282, 297);
            break;
          case 297: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 53)
                kind = 53;
              jjCheckNAddStates(276, 283); }
            break;
          case 298: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 53)
                kind = 53;
              jjCheckNAddStates(284, 286); }
            break;
          case 299: 
          case 301: 
          case 304: 
          case 308: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(298);
            break;
          case 300: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 301;
            break;
          case 302: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 303;
            break;
          case 303: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 304;
            break;
          case 305: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 306;
            break;
          case 306: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 307;
            break;
          case 307: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 308;
            break;
          case 311: 
            if ((0x7FFFFFE87FFFFFE & l) != 0L)
            {
              if (kind > 58)
                kind = 58;
              jjCheckNAddStates(338, 342); }
            break;
          case 312: 
            if ((0x7FFFFFE87FFFFFE & l) != 0L)
              jjCheckNAddStates(103, 105);
            break;
          case 314: 
            if (curChar == 92)
              jjCheckNAddTwoStates(315, 316);
            break;
          case 315: 
            if ((0x7FFFFFFFFFFFFFFF & l) != 0L)
              jjCheckNAddStates(103, 105);
            break;
          case 316: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAddStates(287, 295);
            break;
          case 317: 
            if ((0x7E0000007E & l) != 0L) {
              jjCheckNAddStates(296, 299);
            }
            break;
          case 319: case 321: 
          case 324: 
          case 328: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(317);
            break;
          case 320: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 321;
            break;
          case 322: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 323;
            break;
          case 323: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 324;
            break;
          case 325: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 326;
            break;
          case 326: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 327;
            break;
          case 327: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 328;
            break;
          case 329: 
            if ((0x7FFFFFE87FFFFFE & l) != 0L)
            {
              if (kind > 58)
                kind = 58;
              jjCheckNAddTwoStates(329, 330); }
            break;
          case 330: 
            if (curChar == 92)
              jjCheckNAddTwoStates(331, 332);
            break;
          case 331: 
            if ((0x7FFFFFFFFFFFFFFF & l) != 0L)
            {
              if (kind > 58)
                kind = 58;
              jjCheckNAddTwoStates(329, 330); }
            break;
          case 332: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 58)
                kind = 58;
              jjCheckNAddStates(300, 307); }
            break;
          case 333: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 58)
                kind = 58;
              jjCheckNAddStates(308, 310); }
            break;
          case 335: 
          case 337: 
          case 340: 
          case 344: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(333);
            break;
          case 336: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 337;
            break;
          case 338: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 339;
            break;
          case 339: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 340;
            break;
          case 341: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 342;
            break;
          case 342: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 343;
            break;
          case 343: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 344;
            break;
          case 403: 
            if (curChar == 92)
              jjCheckNAddStates(343, 346);
            break;
          case 404: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 58)
                kind = 58;
              jjCheckNAddStates(314, 321); }
            break;
          case 405: 
            if ((0x7E0000007E & l) != 0L)
            {
              if (kind > 58)
                kind = 58;
              jjCheckNAddStates(322, 324); }
            break;
          case 406: 
          case 408: 
          case 411: 
          case 415: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(405);
            break;
          case 407: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 408;
            break;
          case 409: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 410;
            break;
          case 410: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 411;
            break;
          case 412: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 413;
            break;
          case 413: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 414;
            break;
          case 414: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 415;
            break;
          case 416: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAddStates(325, 333);
            break;
          case 417: 
            if ((0x7E0000007E & l) != 0L) {
              jjCheckNAddStates(334, 337);
            }
            break;
          case 418: case 420: 
          case 423: 
          case 427: 
            if ((0x7E0000007E & l) != 0L)
              jjCheckNAdd(417);
            break;
          case 419: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 420;
            break;
          case 421: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 422;
            break;
          case 422: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 423;
            break;
          case 424: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 425;
            break;
          case 425: 
            if ((0x7E0000007E & l) != 0L)
              jjstateSet[(jjnewStateCnt++)] = 426;
            break;
          case 426: 
            if ((0x7E0000007E & l) != 0L) {
              jjstateSet[(jjnewStateCnt++)] = 427;
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
          case 60: 
          case 63: 
            if (jjCanMove_0(hiByte, i1, i2, l1, l2))
            {
              if (kind > 34)
                kind = 34;
              jjCheckNAddTwoStates(61, 62); }
            break;
          case 61: 
          case 430: 
            if (jjCanMove_0(hiByte, i1, i2, l1, l2))
            {
              if (kind > 34)
                kind = 34;
              jjCheckNAddTwoStates(61, 62); }
            break;
          case 0: 
            if (jjCanMove_0(hiByte, i1, i2, l1, l2))
            {
              if (kind > 58)
                kind = 58;
              jjCheckNAddStates(338, 342); }
            break;
          case 428: 
            if (jjCanMove_0(hiByte, i1, i2, l1, l2))
              jjCheckNAddStates(103, 105);
            if (jjCanMove_0(hiByte, i1, i2, l1, l2))
            {
              if (kind > 58)
                kind = 58;
              jjCheckNAddTwoStates(329, 330);
            }
            break;
          case 1: 
          case 3: 
            if (jjCanMove_0(hiByte, i1, i2, l1, l2))
            {
              if (kind > 20)
                kind = 20;
              jjCheckNAddTwoStates(1, 2); }
            break;
          case 18: 
          case 24: 
            if (jjCanMove_0(hiByte, i1, i2, l1, l2)) {
              jjCheckNAddStates(97, 99);
            }
            break;
          case 39: case 45: 
            if (jjCanMove_0(hiByte, i1, i2, l1, l2)) {
              jjCheckNAddStates(94, 96);
            }
            break;
          case 106: case 110: 
            if (jjCanMove_0(hiByte, i1, i2, l1, l2)) {
              jjCheckNAddStates(171, 174);
            }
            break;
          case 125: case 131: 
            if (jjCanMove_0(hiByte, i1, i2, l1, l2)) {
              jjCheckNAddStates(188, 190);
            }
            break;
          case 146: case 152: 
            if (jjCanMove_0(hiByte, i1, i2, l1, l2)) {
              jjCheckNAddStates(204, 206);
            }
            break;
          case 279: case 280: 
          case 282: 
            if (jjCanMove_0(hiByte, i1, i2, l1, l2))
            {
              if (kind > 53)
                kind = 53;
              jjCheckNAddTwoStates(280, 281); }
            break;
          case 312: 
          case 315: 
            if (jjCanMove_0(hiByte, i1, i2, l1, l2)) {
              jjCheckNAddStates(103, 105);
            }
            break;
          case 329: case 331: 
            if (jjCanMove_0(hiByte, i1, i2, l1, l2))
            {
              if (kind > 58)
                kind = 58;
              jjCheckNAddTwoStates(329, 330); }
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
      if ((i = jjnewStateCnt) == (startsAt = 428 - (this.jjnewStateCnt = startsAt)))
        return curPos;
      try { curChar = input_stream.readChar(); } catch (IOException e) {} }
    return curPos;
  }
  
  private int jjMoveStringLiteralDfa0_1() {
    switch (curChar)
    {
    case 42: 
      return jjMoveStringLiteralDfa1_1(16L);
    }
    return 1;
  }
  
  private int jjMoveStringLiteralDfa1_1(long active0) {
    try { curChar = input_stream.readChar();
    } catch (IOException e) {
      return 1;
    }
    switch (curChar)
    {
    case 47: 
      if ((active0 & 0x10) != 0L)
        return jjStopAtPos(1, 4);
      break;
    default: 
      return 2;
    }
    return 2; }
  
  static final int[] jjnextStates = { 346, 347, 348, 229, 349, 350, 351, 232, 352, 353, 354, 235, 355, 356, 357, 238, 358, 359, 360, 241, 361, 362, 363, 244, 364, 365, 366, 247, 367, 368, 369, 250, 370, 371, 372, 254, 373, 374, 375, 258, 376, 377, 378, 263, 379, 380, 381, 266, 382, 383, 384, 268, 385, 386, 387, 271, 388, 389, 390, 275, 391, 392, 393, 277, 394, 395, 396, 279, 397, 398, 399, 400, 401, 402, 296, 227, 230, 233, 236, 239, 242, 245, 248, 251, 255, 259, 264, 267, 269, 272, 276, 278, 309, 310, 39, 40, 41, 18, 19, 20, 278, 279, 296, 312, 313, 314, 1, 5, 7, 8, 10, 13, 6, 2, 1, 6, 2, 18, 26, 28, 29, 31, 34, 27, 19, 20, 18, 27, 19, 20, 39, 47, 49, 50, 52, 55, 48, 40, 41, 39, 48, 40, 41, 61, 65, 67, 68, 70, 73, 66, 62, 61, 66, 62, 79, 80, 81, 83, 86, 66, 61, 62, 66, 61, 62, 106, 124, 145, 108, 109, 166, 106, 107, 108, 109, 106, 112, 114, 115, 117, 120, 108, 109, 113, 106, 108, 109, 113, 125, 126, 127, 125, 133, 135, 136, 138, 141, 134, 126, 127, 125, 134, 126, 127, 146, 147, 148, 146, 154, 156, 157, 159, 162, 155, 147, 148, 146, 155, 147, 148, 106, 124, 145, 107, 108, 109, 166, 170, 171, 215, 172, 186, 187, 189, 192, 173, 170, 196, 208, 175, 176, 177, 179, 182, 170, 197, 204, 170, 198, 202, 170, 200, 201, 199, 205, 207, 199, 209, 211, 214, 219, 222, 224, 225, 199, 280, 284, 286, 287, 289, 292, 285, 281, 280, 285, 281, 298, 299, 300, 302, 305, 285, 280, 281, 285, 280, 281, 312, 317, 319, 320, 322, 325, 318, 313, 314, 312, 318, 313, 314, 329, 333, 335, 336, 338, 341, 334, 330, 329, 334, 330, 394, 279, 296, 405, 406, 407, 409, 412, 334, 329, 330, 334, 329, 330, 417, 418, 419, 421, 424, 318, 312, 313, 314, 318, 312, 313, 314, 312, 313, 329, 330, 314, 315, 331, 404, 416, 60, 77, 168, 169, 3, 4, 21, 23, 24, 25, 42, 44, 45, 46, 110, 111, 128, 130, 131, 132, 149, 151, 152, 153 };
  
























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
  


  public static final String[] jjstrLiteralImages = { "", null, null, null, null, null, "{", "}", ",", ".", ";", ":", "*", "/", "+", "-", "=", ">", "[", "]", null, null, ")", null, null, "<!--", "-->", "~=", "|=", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null };
  

  protected Token jjFillToken()
  {
    int beginColumn;
    
    String curTokenImage;
    
    int beginLine;
    
    int beginColumn;
    
    int endLine;
    
    int endColumn;
    if (jjmatchedPos < 0) { String curTokenImage;
      String curTokenImage;
      if (image == null) {
        curTokenImage = "";
      } else
        curTokenImage = image.toString();
      int endLine; int beginLine = endLine = input_stream.getEndLine();
      int endColumn; beginColumn = endColumn = input_stream.getEndColumn();
    }
    else
    {
      String im = jjstrLiteralImages[jjmatchedKind];
      curTokenImage = im == null ? input_stream.GetImage() : im;
      beginLine = input_stream.getBeginLine();
      beginColumn = input_stream.getBeginColumn();
      endLine = input_stream.getEndLine();
      endColumn = input_stream.getEndColumn();
    }
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
        jjmatchedKind = 2;
        jjmatchedPos = -1;
        curPos = 0;
        curPos = jjMoveStringLiteralDfa0_0();
        if ((jjmatchedPos < 0) || ((jjmatchedPos == 0) && (jjmatchedKind > 79)))
        {
          jjmatchedKind = 79;
          jjmatchedPos = 0;
        }
        break;
      case 1: 
        jjmatchedKind = Integer.MAX_VALUE;
        jjmatchedPos = 0;
        curPos = jjMoveStringLiteralDfa0_1();
        if ((jjmatchedPos == 0) && (jjmatchedKind > 5))
        {
          jjmatchedKind = 5;
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
    case 21: 
      image.append(input_stream.GetSuffix(jjimageLen + (this.lengthOfMatch = jjmatchedPos + 1)));
      image = ParserUtils.trimBy(image, 1, 1);
      break;
    case 24: 
      image.append(input_stream.GetSuffix(jjimageLen + (this.lengthOfMatch = jjmatchedPos + 1)));
      image = ParserUtils.trimUrl(image);
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
      image = ParserUtils.trimBy(image, 0, 2);
      break;
    case 42: 
      image.append(input_stream.GetSuffix(jjimageLen + (this.lengthOfMatch = jjmatchedPos + 1)));
      image = ParserUtils.trimBy(image, 0, 2);
      break;
    case 43: 
      image.append(input_stream.GetSuffix(jjimageLen + (this.lengthOfMatch = jjmatchedPos + 1)));
      image = ParserUtils.trimBy(image, 0, 2);
      break;
    case 44: 
      image.append(input_stream.GetSuffix(jjimageLen + (this.lengthOfMatch = jjmatchedPos + 1)));
      image = ParserUtils.trimBy(image, 0, 2);
      break;
    case 45: 
      image.append(input_stream.GetSuffix(jjimageLen + (this.lengthOfMatch = jjmatchedPos + 1)));
      image = ParserUtils.trimBy(image, 0, 3);
      break;
    case 46: 
      image.append(input_stream.GetSuffix(jjimageLen + (this.lengthOfMatch = jjmatchedPos + 1)));
      image = ParserUtils.trimBy(image, 0, 3);
      break;
    case 47: 
      image.append(input_stream.GetSuffix(jjimageLen + (this.lengthOfMatch = jjmatchedPos + 1)));
      image = ParserUtils.trimBy(image, 0, 4);
      break;
    case 48: 
      image.append(input_stream.GetSuffix(jjimageLen + (this.lengthOfMatch = jjmatchedPos + 1)));
      image = ParserUtils.trimBy(image, 0, 2);
      break;
    case 49: 
      image.append(input_stream.GetSuffix(jjimageLen + (this.lengthOfMatch = jjmatchedPos + 1)));
      image = ParserUtils.trimBy(image, 0, 1);
      break;
    case 50: 
      image.append(input_stream.GetSuffix(jjimageLen + (this.lengthOfMatch = jjmatchedPos + 1)));
      image = ParserUtils.trimBy(image, 0, 2);
      break;
    case 51: 
      image.append(input_stream.GetSuffix(jjimageLen + (this.lengthOfMatch = jjmatchedPos + 1)));
      image = ParserUtils.trimBy(image, 0, 3);
      break;
    case 52: 
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
  


  public SACParserCSS2TokenManager(CharStream stream)
  {
    input_stream = stream;
  }
  
  public SACParserCSS2TokenManager(CharStream stream, int lexState)
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
    for (int i = 428; i-- > 0;) {
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
  




  public static final int[] jjnewLexState = { -1, -1, -1, 1, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
  




  static final long[] jjtoToken = { 4035225266115575751L, 32768L };
  

  static final long[] jjtoSkip = { 16L, 0L };
  

  static final long[] jjtoMore = { 40L, 0L };
  

  protected CharStream input_stream;
  
  private final int[] jjrounds = new int['Ƭ'];
  private final int[] jjstateSet = new int['͘'];
  
  private final StringBuilder jjimage = new StringBuilder();
  private StringBuilder image = jjimage;
  private int jjimageLen;
  private int lengthOfMatch;
  protected int curChar;
}
