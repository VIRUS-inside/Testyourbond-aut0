package org.apache.commons.codec.language;

import java.util.Locale;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.StringUtils;





































public class DoubleMetaphone
  implements StringEncoder
{
  private static final String VOWELS = "AEIOUY";
  private static final String[] SILENT_START = { "GN", "KN", "PN", "WR", "PS" };
  
  private static final String[] L_R_N_M_B_H_F_V_W_SPACE = { "L", "R", "N", "M", "B", "H", "F", "V", "W", " " };
  
  private static final String[] ES_EP_EB_EL_EY_IB_IL_IN_IE_EI_ER = { "ES", "EP", "EB", "EL", "EY", "IB", "IL", "IN", "IE", "EI", "ER" };
  
  private static final String[] L_T_K_S_N_M_B_Z = { "L", "T", "K", "S", "N", "M", "B", "Z" };
  




  private int maxCodeLen = 4;
  





  public DoubleMetaphone() {}
  





  public String doubleMetaphone(String value)
  {
    return doubleMetaphone(value, false);
  }
  






  public String doubleMetaphone(String value, boolean alternate)
  {
    value = cleanInput(value);
    if (value == null) {
      return null;
    }
    
    boolean slavoGermanic = isSlavoGermanic(value);
    int index = isSilentStart(value) ? 1 : 0;
    
    DoubleMetaphoneResult result = new DoubleMetaphoneResult(getMaxCodeLen());
    
    while ((!result.isComplete()) && (index <= value.length() - 1)) {
      switch (value.charAt(index)) {
      case 'A': 
      case 'E': 
      case 'I': 
      case 'O': 
      case 'U': 
      case 'Y': 
        index = handleAEIOUY(result, index);
        break;
      case 'B': 
        result.append('P');
        index = charAt(value, index + 1) == 'B' ? index + 2 : index + 1;
        break;
      
      case 'Ç': 
        result.append('S');
        index++;
        break;
      case 'C': 
        index = handleC(value, result, index);
        break;
      case 'D': 
        index = handleD(value, result, index);
        break;
      case 'F': 
        result.append('F');
        index = charAt(value, index + 1) == 'F' ? index + 2 : index + 1;
        break;
      case 'G': 
        index = handleG(value, result, index, slavoGermanic);
        break;
      case 'H': 
        index = handleH(value, result, index);
        break;
      case 'J': 
        index = handleJ(value, result, index, slavoGermanic);
        break;
      case 'K': 
        result.append('K');
        index = charAt(value, index + 1) == 'K' ? index + 2 : index + 1;
        break;
      case 'L': 
        index = handleL(value, result, index);
        break;
      case 'M': 
        result.append('M');
        index = conditionM0(value, index) ? index + 2 : index + 1;
        break;
      case 'N': 
        result.append('N');
        index = charAt(value, index + 1) == 'N' ? index + 2 : index + 1;
        break;
      
      case 'Ñ': 
        result.append('N');
        index++;
        break;
      case 'P': 
        index = handleP(value, result, index);
        break;
      case 'Q': 
        result.append('K');
        index = charAt(value, index + 1) == 'Q' ? index + 2 : index + 1;
        break;
      case 'R': 
        index = handleR(value, result, index, slavoGermanic);
        break;
      case 'S': 
        index = handleS(value, result, index, slavoGermanic);
        break;
      case 'T': 
        index = handleT(value, result, index);
        break;
      case 'V': 
        result.append('F');
        index = charAt(value, index + 1) == 'V' ? index + 2 : index + 1;
        break;
      case 'W': 
        index = handleW(value, result, index);
        break;
      case 'X': 
        index = handleX(value, result, index);
        break;
      case 'Z': 
        index = handleZ(value, result, index, slavoGermanic);
        break;
      default: 
        index++;
      }
      
    }
    
    return alternate ? result.getAlternate() : result.getPrimary();
  }
  







  public Object encode(Object obj)
    throws EncoderException
  {
    if (!(obj instanceof String)) {
      throw new EncoderException("DoubleMetaphone encode parameter is not of type String");
    }
    return doubleMetaphone((String)obj);
  }
  






  public String encode(String value)
  {
    return doubleMetaphone(value);
  }
  









  public boolean isDoubleMetaphoneEqual(String value1, String value2)
  {
    return isDoubleMetaphoneEqual(value1, value2, false);
  }
  









  public boolean isDoubleMetaphoneEqual(String value1, String value2, boolean alternate)
  {
    return StringUtils.equals(doubleMetaphone(value1, alternate), doubleMetaphone(value2, alternate));
  }
  



  public int getMaxCodeLen()
  {
    return maxCodeLen;
  }
  



  public void setMaxCodeLen(int maxCodeLen)
  {
    this.maxCodeLen = maxCodeLen;
  }
  




  private int handleAEIOUY(DoubleMetaphoneResult result, int index)
  {
    if (index == 0) {
      result.append('A');
    }
    return index + 1;
  }
  


  private int handleC(String value, DoubleMetaphoneResult result, int index)
  {
    if (conditionC0(value, index)) {
      result.append('K');
      index += 2;
    } else { if (index == 0) if (contains(value, index, 6, new String[] { "CAESAR" })) {
          result.append('S');
          index += 2; return index; }
      if (contains(value, index, 2, new String[] { "CH" })) {
        index = handleCH(value, result, index);
      } else { if (contains(value, index, 2, new String[] { "CZ" })) if (!contains(value, index - 2, 4, new String[] { "WICZ" }))
          {

            result.append('S', 'X');
            index += 2; return index; }
        if (contains(value, index + 1, 3, new String[] { "CIA" }))
        {
          result.append('X');
          index += 3;
        } else { if ((contains(value, index, 2, new String[] { "CC" })) && ((index != 1) || (charAt(value, 0) != 'M')))
          {

            return handleCC(value, result, index); }
          if (contains(value, index, 2, new String[] { "CK", "CG", "CQ" })) {
            result.append('K');
            index += 2;
          } else if (contains(value, index, 2, new String[] { "CI", "CE", "CY" }))
          {
            if (contains(value, index, 3, new String[] { "CIO", "CIE", "CIA" })) {
              result.append('S', 'X');
            } else {
              result.append('S');
            }
            index += 2;
          } else {
            result.append('K');
            if (contains(value, index + 1, 2, new String[] { " C", " Q", " G" }))
            {
              index += 3;
            } else { if (contains(value, index + 1, 1, new String[] { "C", "K", "Q" })) if (!contains(value, index + 1, 2, new String[] { "CE", "CI" }))
                {
                  index += 2; return index;
                }
              index++;
            }
          }
        } } }
    return index;
  }
  


  private int handleCC(String value, DoubleMetaphoneResult result, int index)
  {
    if (contains(value, index + 2, 1, new String[] { "I", "E", "H" })) if (!contains(value, index + 2, 2, new String[] { "HU" }))
      {

        if ((index != 1) || (charAt(value, index - 1) != 'A')) { if (!contains(value, index - 1, 5, new String[] { "UCCEE", "UCCES" })) {}
        }
        else {
          result.append("KS");
          break label108;
        }
        result.append('X');
        label108:
        index += 3;
        return index; }
    result.append('K');
    index += 2;
    

    return index;
  }
  


  private int handleCH(String value, DoubleMetaphoneResult result, int index)
  {
    if (index > 0) if (contains(value, index, 4, new String[] { "CHAE" })) {
        result.append('K', 'X');
        return index + 2; }
    if (conditionCH0(value, index))
    {
      result.append('K');
      return index + 2; }
    if (conditionCH1(value, index))
    {
      result.append('K');
      return index + 2;
    }
    if (index > 0) {
      if (contains(value, 0, 2, new String[] { "MC" })) {
        result.append('K');
      } else {
        result.append('X', 'K');
      }
    } else {
      result.append('X');
    }
    return index + 2;
  }
  



  private int handleD(String value, DoubleMetaphoneResult result, int index)
  {
    if (contains(value, index, 2, new String[] { "DG" }))
    {
      if (contains(value, index + 2, 1, new String[] { "I", "E", "Y" })) {
        result.append('J');
        index += 3;
      }
      else {
        result.append("TK");
        index += 2;
      }
    } else if (contains(value, index, 2, new String[] { "DT", "DD" })) {
      result.append('T');
      index += 2;
    } else {
      result.append('T');
      index++;
    }
    return index;
  }
  



  private int handleG(String value, DoubleMetaphoneResult result, int index, boolean slavoGermanic)
  {
    if (charAt(value, index + 1) == 'H') {
      index = handleGH(value, result, index);
    } else if (charAt(value, index + 1) == 'N') {
      if ((index == 1) && (isVowel(charAt(value, 0))) && (!slavoGermanic)) {
        result.append("KN", "N");
      } else if ((!contains(value, index + 2, 2, new String[] { "EY" })) && (charAt(value, index + 1) != 'Y') && (!slavoGermanic))
      {
        result.append("N", "KN");
      } else {
        result.append("KN");
      }
      index += 2;
    } else if ((contains(value, index + 1, 2, new String[] { "LI" })) && (!slavoGermanic)) {
      result.append("KL", "L");
      index += 2;
    } else if ((index == 0) && ((charAt(value, index + 1) == 'Y') || (contains(value, index + 1, 2, ES_EP_EB_EL_EY_IB_IL_IN_IE_EI_ER))))
    {


      result.append('K', 'J');
      index += 2;
    } else { if ((contains(value, index + 1, 2, new String[] { "ER" })) || (charAt(value, index + 1) == 'Y')) if (!contains(value, 0, 6, new String[] { "DANGER", "RANGER", "MANGER" })) if (!contains(value, index - 1, 1, new String[] { "E", "I" })) if (!contains(value, index - 1, 3, new String[] { "RGY", "OGY" }))
            {




              result.append('K', 'J');
              index += 2; return index; }
      if (!contains(value, index + 1, 1, new String[] { "E", "I", "Y" })) { if (!contains(value, index - 1, 4, new String[] { "AGGI", "OGGI" })) {}
      }
      else {
        if (!contains(value, 0, 4, new String[] { "VAN ", "VON " })) if (!contains(value, 0, 3, new String[] { "SCH" })) { if (!contains(value, index + 1, 2, new String[] { "ET" })) {
              break label468;
            }
          }
        result.append('K');
        break label505; label468: if (contains(value, index + 1, 3, new String[] { "IER" })) {
          result.append('J');
        } else
          result.append('J', 'K');
        label505:
        index += 2;
        return index; } if (charAt(value, index + 1) == 'G') {
        index += 2;
        result.append('K');
      } else {
        index++;
        result.append('K');
      } }
    return index;
  }
  


  private int handleGH(String value, DoubleMetaphoneResult result, int index)
  {
    if ((index > 0) && (!isVowel(charAt(value, index - 1)))) {
      result.append('K');
      index += 2;
    } else if (index == 0) {
      if (charAt(value, index + 2) == 'I') {
        result.append('J');
      } else {
        result.append('K');
      }
      index += 2;
    } else { if (index > 1) { if (contains(value, index - 2, 1, new String[] { "B", "H", "D" })) {} } else if (index > 2) { if (contains(value, index - 3, 1, new String[] { "B", "H", "D" })) {} } else { if (index <= 3) break label175; if (!contains(value, index - 4, 1, new String[] { "B", "H" })) {
          break label175;
        }
      }
      index += 2; return index;
      label175:
      if ((index > 2) && (charAt(value, index - 1) == 'U')) if (contains(value, index - 3, 1, new String[] { "C", "G", "L", "R", "T" }))
        {

          result.append('F');
          break label265; } if ((index > 0) && (charAt(value, index - 1) != 'I'))
        result.append('K');
      label265:
      index += 2;
    }
    return index;
  }
  



  private int handleH(String value, DoubleMetaphoneResult result, int index)
  {
    if (((index == 0) || (isVowel(charAt(value, index - 1)))) && (isVowel(charAt(value, index + 1))))
    {
      result.append('H');
      index += 2;
    }
    else {
      index++;
    }
    return index;
  }
  



  private int handleJ(String value, DoubleMetaphoneResult result, int index, boolean slavoGermanic)
  {
    if (!contains(value, index, 4, new String[] { "JOSE" })) { if (!contains(value, 0, 4, new String[] { "SAN " })) {}
    } else {
      if (((index != 0) || (charAt(value, index + 4) != ' ')) && (value.length() != 4)) { if (!contains(value, 0, 4, new String[] { "SAN " })) {}
      } else {
        result.append('H');
        break label96; }
      result.append('J', 'H');
      label96:
      index++;
      return index; }
    if (index == 0) if (!contains(value, index, 4, new String[] { "JOSE" })) {
        result.append('J', 'A');
        break label263; } if ((isVowel(charAt(value, index - 1))) && (!slavoGermanic) && ((charAt(value, index + 1) == 'A') || (charAt(value, index + 1) == 'O')))
    {
      result.append('J', 'H');
    } else if (index == value.length() - 1) {
      result.append('J', ' ');
    } else if (!contains(value, index + 1, 1, L_T_K_S_N_M_B_Z)) if (!contains(value, index - 1, 1, new String[] { "S", "K", "L" }))
      {
        result.append('J');
      }
    label263:
    if (charAt(value, index + 1) == 'J') {
      index += 2;
    } else {
      index++;
    }
    
    return index;
  }
  


  private int handleL(String value, DoubleMetaphoneResult result, int index)
  {
    if (charAt(value, index + 1) == 'L') {
      if (conditionL0(value, index)) {
        result.appendPrimary('L');
      } else {
        result.append('L');
      }
      index += 2;
    } else {
      index++;
      result.append('L');
    }
    return index;
  }
  


  private int handleP(String value, DoubleMetaphoneResult result, int index)
  {
    if (charAt(value, index + 1) == 'H') {
      result.append('F');
      index += 2;
    } else {
      result.append('P');
      index = contains(value, index + 1, 1, tmp45_40) ? index + 2 : index + 1;
    }
    return index;
  }
  



  private int handleR(String value, DoubleMetaphoneResult result, int index, boolean slavoGermanic)
  {
    if ((index == value.length() - 1) && (!slavoGermanic)) if (contains(value, index - 2, 2, new String[] { "IE" })) if (!contains(value, index - 4, 2, new String[] { "ME", "MA" }))
        {

          result.appendAlternate('R');
          break label75; }
    result.append('R');
    label75:
    return charAt(value, index + 1) == 'R' ? index + 2 : index + 1;
  }
  



  private int handleS(String value, DoubleMetaphoneResult result, int index, boolean slavoGermanic)
  {
    if (contains(value, index - 1, 3, new String[] { "ISL", "YSL" }))
    {
      index++;
    } else { if (index == 0) if (contains(value, index, 5, new String[] { "SUGAR" }))
        {
          result.append('X', 'S');
          index++; return index; }
      if (contains(value, index, 2, new String[] { "SH" })) {
        if (contains(value, index + 1, 4, new String[] { "HEIM", "HOEK", "HOLM", "HOLZ" }))
        {
          result.append('S');
        } else {
          result.append('X');
        }
        index += 2;
      } else { if (!contains(value, index, 3, new String[] { "SIO", "SIA" })) { if (!contains(value, index, 4, new String[] { "SIAN" })) {}
        } else {
          if (slavoGermanic) {
            result.append('S');
          } else {
            result.append('S', 'X');
          }
          index += 3; return index; }
        if (index == 0) { if (contains(value, index + 1, 1, new String[] { "M", "N", "L", "W" })) {} } else { if (!contains(value, index + 1, 1, new String[] { "Z" })) {
            break label310;
          }
        }
        

        result.append('S', 'X');
        index = contains(value, index + 1, 1, tmp286_283) ? index + 2 : index + 1; return index;
        label310: if (contains(value, index, 2, new String[] { "SC" })) {
          index = handleSC(value, result, index);
        } else {
          if (index == value.length() - 1) if (contains(value, index - 2, 2, new String[] { "AI", "OI" }))
            {
              result.appendAlternate('S');
              break label389; }
          result.append('S');
          label389:
          index = contains(value, index + 1, 1, tmp403_398) ? index + 2 : index + 1;
        } } }
    return index;
  }
  


  private int handleSC(String value, DoubleMetaphoneResult result, int index)
  {
    if (charAt(value, index + 2) == 'H')
    {
      if (contains(value, index + 3, 2, new String[] { "OO", "ER", "EN", "UY", "ED", "EM" }))
      {
        if (contains(value, index + 3, 2, new String[] { "ER", "EN" }))
        {
          result.append("X", "SK");
        } else {
          result.append("SK");
        }
      }
      else if ((index == 0) && (!isVowel(charAt(value, 3))) && (charAt(value, 3) != 'W')) {
        result.append('X', 'S');
      } else {
        result.append('X');
      }
    }
    else if (contains(value, index + 2, 1, new String[] { "I", "E", "Y" })) {
      result.append('S');
    } else {
      result.append("SK");
    }
    return index + 3;
  }
  


  private int handleT(String value, DoubleMetaphoneResult result, int index)
  {
    if (contains(value, index, 4, new String[] { "TION" })) {
      result.append('X');
      index += 3;
    } else if (contains(value, index, 3, new String[] { "TIA", "TCH" })) {
      result.append('X');
      index += 3;
    } else { if (!contains(value, index, 2, new String[] { "TH" })) { if (!contains(value, index, 3, new String[] { "TTH" })) {}
      } else { if (!contains(value, index + 2, 2, new String[] { "OM", "AM" })) if (!contains(value, 0, 4, new String[] { "VAN ", "VON " })) { if (!contains(value, 0, 3, new String[] { "SCH" })) {
              break label176;
            }
          }
        result.append('T');
        break label184;
        label176: result.append('0', 'T');
        label184:
        index += 2;
        return index; }
      result.append('T');
      index = contains(value, index + 1, 1, tmp210_205) ? index + 2 : index + 1;
    }
    return index;
  }
  


  private int handleW(String value, DoubleMetaphoneResult result, int index)
  {
    if (contains(value, index, 2, new String[] { "WR" }))
    {
      result.append('R');
      index += 2;
    } else {
      if (index == 0) if (!isVowel(charAt(value, index + 1))) { if (!contains(value, index, 2, new String[] { "WH" })) {}
        } else {
          if (isVowel(charAt(value, index + 1)))
          {
            result.append('A', 'F');
          }
          else {
            result.append('A');
          }
          index++; return index; }
      if ((index != value.length() - 1) || (!isVowel(charAt(value, index - 1)))) if (!contains(value, index - 1, 5, new String[] { "EWSKI", "EWSKY", "OWSKI", "OWSKY" })) { if (!contains(value, 0, 3, new String[] { "SCH" })) {
            break label195;
          }
        }
      result.appendAlternate('F');
      index++; return index;
      label195: if (contains(value, index, 4, new String[] { "WICZ", "WITZ" }))
      {
        result.append("TS", "FX");
        index += 4;
      } else {
        index++;
      }
    }
    return index;
  }
  


  private int handleX(String value, DoubleMetaphoneResult result, int index)
  {
    if (index == 0) {
      result.append('S');
      index++;
    } else {
      if (index == value.length() - 1) { if (!contains(value, index - 3, 3, new String[] { "IAU", "EAU" })) { if (contains(value, index - 2, 2, new String[] { "AU", "OU" })) {}
        }
      }
      else {
        result.append("KS");
      }
      index = contains(value, index + 1, 1, tmp96_91) ? index + 2 : index + 1;
    }
    return index;
  }
  



  private int handleZ(String value, DoubleMetaphoneResult result, int index, boolean slavoGermanic)
  {
    if (charAt(value, index + 1) == 'H')
    {
      result.append('J');
      index += 2;
    } else {
      if ((contains(value, index + 1, 2, new String[] { "ZO", "ZI", "ZA" })) || ((slavoGermanic) && (index > 0) && (charAt(value, index - 1) != 'T')))
      {
        result.append("S", "TS");
      } else {
        result.append('S');
      }
      index = charAt(value, index + 1) == 'Z' ? index + 2 : index + 1;
    }
    return index;
  }
  




  private boolean conditionC0(String value, int index)
  {
    if (contains(value, index, 4, new String[] { "CHIA" }))
      return true;
    if (index <= 1)
      return false;
    if (isVowel(charAt(value, index - 2)))
      return false;
    if (!contains(value, index - 1, 3, new String[] { "ACH" })) {
      return false;
    }
    char c = charAt(value, index + 2);
    if ((c == 'I') || (c == 'E')) {} return contains(value, index - 2, 6, new String[] { "BACHER", "MACHER" });
  }
  




  private boolean conditionCH0(String value, int index)
  {
    if (index != 0)
      return false;
    if (!contains(value, index + 1, 5, new String[] { "HARAC", "HARIS" })) if (!contains(value, index + 1, 3, new String[] { "HOR", "HYM", "HIA", "HEM" }))
      {
        return false; }
    if (contains(value, 0, 5, new String[] { "CHORE" })) {
      return false;
    }
    return true;
  }
  



  private boolean conditionCH1(String value, int index)
  {
    if (!contains(value, 0, 4, new String[] { "VAN ", "VON " })) if (!contains(value, 0, 3, new String[] { "SCH" })) if (!contains(value, index - 2, 6, new String[] { "ORCHES", "ARCHIT", "ORCHID" })) if (contains(value, index + 2, 1, new String[] { "T", "S" })) {} return ((contains(value, index - 1, 1, new String[] { "A", "O", "U", "E" })) || (index == 0)) && ((contains(value, index + 2, 1, L_R_N_M_B_H_F_V_W_SPACE)) || (index + 1 == value.length() - 1));
  }
  






  private boolean conditionL0(String value, int index)
  {
    if (index == value.length() - 3) if (contains(value, index - 1, 4, new String[] { "ILLO", "ILLA", "ALLE" }))
      {
        return true; }
    if (!contains(value, value.length() - 2, 2, new String[] { "AS", "OS" })) { if (!contains(value, value.length() - 1, 1, new String[] { "A", "O" })) {} } else if (contains(value, index - 1, 4, new String[] { "ALLE" }))
    {

      return true;
    }
    return false;
  }
  



  private boolean conditionM0(String value, int index)
  {
    if (charAt(value, index + 1) == 'M') {
      return true;
    }
    if (contains(value, index - 1, 3, new String[] { "UMB" })) if (index + 1 == value.length() - 1) {} return contains(value, index + 2, 2, new String[] { "ER" });
  }
  






  private boolean isSlavoGermanic(String value)
  {
    return (value.indexOf('W') > -1) || (value.indexOf('K') > -1) || (value.indexOf("CZ") > -1) || (value.indexOf("WITZ") > -1);
  }
  



  private boolean isVowel(char ch)
  {
    return "AEIOUY".indexOf(ch) != -1;
  }
  




  private boolean isSilentStart(String value)
  {
    boolean result = false;
    for (String element : SILENT_START) {
      if (value.startsWith(element)) {
        result = true;
        break;
      }
    }
    return result;
  }
  


  private String cleanInput(String input)
  {
    if (input == null) {
      return null;
    }
    input = input.trim();
    if (input.length() == 0) {
      return null;
    }
    return input.toUpperCase(Locale.ENGLISH);
  }
  




  protected char charAt(String value, int index)
  {
    if ((index < 0) || (index >= value.length())) {
      return '\000';
    }
    return value.charAt(index);
  }
  




  protected static boolean contains(String value, int start, int length, String... criteria)
  {
    boolean result = false;
    if ((start >= 0) && (start + length <= value.length())) {
      String target = value.substring(start, start + length);
      
      for (String element : criteria) {
        if (target.equals(element)) {
          result = true;
          break;
        }
      }
    }
    return result;
  }
  





  public class DoubleMetaphoneResult
  {
    private final StringBuilder primary = new StringBuilder(getMaxCodeLen());
    private final StringBuilder alternate = new StringBuilder(getMaxCodeLen());
    private final int maxLength;
    
    public DoubleMetaphoneResult(int maxLength) {
      this.maxLength = maxLength;
    }
    
    public void append(char value) {
      appendPrimary(value);
      appendAlternate(value);
    }
    
    public void append(char primary, char alternate) {
      appendPrimary(primary);
      appendAlternate(alternate);
    }
    
    public void appendPrimary(char value) {
      if (primary.length() < maxLength) {
        primary.append(value);
      }
    }
    
    public void appendAlternate(char value) {
      if (alternate.length() < maxLength) {
        alternate.append(value);
      }
    }
    
    public void append(String value) {
      appendPrimary(value);
      appendAlternate(value);
    }
    
    public void append(String primary, String alternate) {
      appendPrimary(primary);
      appendAlternate(alternate);
    }
    
    public void appendPrimary(String value) {
      int addChars = maxLength - primary.length();
      if (value.length() <= addChars) {
        primary.append(value);
      } else {
        primary.append(value.substring(0, addChars));
      }
    }
    
    public void appendAlternate(String value) {
      int addChars = maxLength - alternate.length();
      if (value.length() <= addChars) {
        alternate.append(value);
      } else {
        alternate.append(value.substring(0, addChars));
      }
    }
    
    public String getPrimary() {
      return primary.toString();
    }
    
    public String getAlternate() {
      return alternate.toString();
    }
    
    public boolean isComplete() {
      return (primary.length() >= maxLength) && (alternate.length() >= maxLength);
    }
  }
}
