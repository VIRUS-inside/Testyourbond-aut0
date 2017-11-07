package org.apache.bcel.util;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.BitSet;
import org.apache.bcel.Constants;
import org.apache.bcel.classfile.AccessFlags;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.CodeException;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantCP;
import org.apache.bcel.classfile.ConstantFieldref;
import org.apache.bcel.classfile.ConstantInterfaceMethodref;
import org.apache.bcel.classfile.ConstantMethodref;
import org.apache.bcel.classfile.ConstantNameAndType;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.FieldOrMethod;
import org.apache.bcel.classfile.LocalVariable;
import org.apache.bcel.classfile.LocalVariableTable;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.Utility;







































final class CodeHTML
  implements Constants
{
  private String class_name;
  private Method[] methods;
  private PrintWriter file;
  private BitSet goto_set;
  private ConstantPool constant_pool;
  private ConstantHTML constant_html;
  private static boolean wide = false;
  

  CodeHTML(String dir, String class_name, Method[] methods, ConstantPool constant_pool, ConstantHTML constant_html)
    throws IOException
  {
    this.class_name = class_name;
    this.methods = methods;
    this.constant_pool = constant_pool;
    this.constant_html = constant_html;
    
    file = new PrintWriter(new FileOutputStream(dir + class_name + "_code.html"));
    file.println("<HTML><BODY BGCOLOR=\"#C0C0C0\">");
    
    for (int i = 0; i < methods.length; i++) {
      writeMethod(methods[i], i);
    }
    file.println("</BODY></HTML>");
    file.close();
  }
  







  private final String codeToHTML(ByteSequence bytes, int method_number)
    throws IOException
  {
    short opcode = (short)bytes.readUnsignedByte();
    

    int default_offset = 0;
    

    int no_pad_bytes = 0;
    
    StringBuffer buf = new StringBuffer("<TT>" + Constants.OPCODE_NAMES[opcode] + "</TT></TD><TD>");
    



    if ((opcode == 170) || (opcode == 171)) {
      int remainder = bytes.getIndex() % 4;
      no_pad_bytes = remainder == 0 ? 0 : 4 - remainder;
      
      for (int i = 0; i < no_pad_bytes; i++) {
        bytes.readByte();
      }
      
      default_offset = bytes.readInt(); }
    int offset;
    int[] jump_table;
    int index; int vindex; int class_index; String name; switch (opcode) {
    case 170: 
      int low = bytes.readInt();
      int high = bytes.readInt();
      
      offset = bytes.getIndex() - 12 - no_pad_bytes - 1;
      default_offset += offset;
      
      buf.append("<TABLE BORDER=1><TR>");
      

      jump_table = new int[high - low + 1];
      for (int i = 0; i < jump_table.length; i++) {
        jump_table[i] = (offset + bytes.readInt());
        
        buf.append("<TH>" + (low + i) + "</TH>");
      }
      buf.append("<TH>default</TH></TR>\n<TR>");
      

      for (int i = 0; i < jump_table.length; i++) {
        buf.append("<TD><A HREF=\"#code" + method_number + "@" + jump_table[i] + "\">" + jump_table[i] + "</A></TD>");
      }
      buf.append("<TD><A HREF=\"#code" + method_number + "@" + default_offset + "\">" + default_offset + "</A></TD></TR>\n</TABLE>\n");
      

      break;
    


    case 171: 
      int npairs = bytes.readInt();
      offset = bytes.getIndex() - 8 - no_pad_bytes - 1;
      jump_table = new int[npairs];
      default_offset += offset;
      
      buf.append("<TABLE BORDER=1><TR>");
      

      for (int i = 0; i < npairs; i++) {
        int match = bytes.readInt();
        
        jump_table[i] = (offset + bytes.readInt());
        buf.append("<TH>" + match + "</TH>");
      }
      buf.append("<TH>default</TH></TR>\n<TR>");
      

      for (int i = 0; i < npairs; i++) {
        buf.append("<TD><A HREF=\"#code" + method_number + "@" + jump_table[i] + "\">" + jump_table[i] + "</A></TD>");
      }
      buf.append("<TD><A HREF=\"#code" + method_number + "@" + default_offset + "\">" + default_offset + "</A></TD></TR>\n</TABLE>\n");
      
      break;
    case 153: case 154: 
    case 155: case 156: 
    case 157: case 158: 
    case 159: case 160: 
    case 161: case 162: 
    case 163: case 164: 
    case 165: case 166: 
    case 167: case 168: 
    case 198: 
    case 199: 
      index = bytes.getIndex() + bytes.readShort() - 1;
      
      buf.append("<A HREF=\"#code" + method_number + "@" + index + "\">" + index + "</A>");
      break;
    

    case 200: 
    case 201: 
      int windex = bytes.getIndex() + bytes.readInt() - 1;
      buf.append("<A HREF=\"#code" + method_number + "@" + windex + "\">" + windex + "</A>");
      
      break;
    case 21: case 22: 
    case 23: case 24: 
    case 25: case 54: 
    case 55: case 56: 
    case 57: case 58: 
    case 169: 
      if (wide) {
        vindex = bytes.readShort();
        wide = false;
      }
      else {
        vindex = bytes.readUnsignedByte();
      }
      buf.append("%" + vindex);
      break;
    





    case 196: 
      wide = true;
      buf.append("(wide)");
      break;
    


    case 188: 
      buf.append("<FONT COLOR=\"#00FF00\">" + Constants.TYPE_NAMES[bytes.readByte()] + "</FONT>");
      break;
    case 178: 
    case 179: 
    case 180: 
    case 181: 
      index = bytes.readShort();
      ConstantFieldref c1 = (ConstantFieldref)constant_pool.getConstant(index, (byte)9);
      
      class_index = c1.getClassIndex();
      name = constant_pool.getConstantString(class_index, (byte)7);
      name = Utility.compactClassName(name, false);
      
      index = c1.getNameAndTypeIndex();
      String field_name = constant_pool.constantToString(index, (byte)12);
      
      if (name.equals(class_name)) {
        buf.append("<A HREF=\"" + class_name + "_methods.html#field" + field_name + "\" TARGET=Methods>" + field_name + "</A>\n");
      }
      else
      {
        buf.append(constant_html.referenceConstant(class_index) + "." + field_name);
      }
      break;
    
    case 187: 
    case 192: 
    case 193: 
      index = bytes.readShort();
      buf.append(constant_html.referenceConstant(index));
      break;
    case 182: 
    case 183: 
    case 184: 
    case 185: 
      int m_index = bytes.readShort();
      

      if (opcode == 185) {
        int nargs = bytes.readUnsignedByte();
        int reserved = bytes.readUnsignedByte();
        
        ConstantInterfaceMethodref c = (ConstantInterfaceMethodref)constant_pool.getConstant(m_index, (byte)11);
        
        class_index = c.getClassIndex();
        str = constant_pool.constantToString(c);
        index = c.getNameAndTypeIndex();
      }
      else {
        ConstantMethodref c = (ConstantMethodref)constant_pool.getConstant(m_index, (byte)10);
        class_index = c.getClassIndex();
        
        str = constant_pool.constantToString(c);
        index = c.getNameAndTypeIndex();
      }
      
      name = Class2HTML.referenceClass(class_index);
      String str = Class2HTML.toHTML(constant_pool.constantToString(constant_pool.getConstant(index, (byte)12)));
      

      ConstantNameAndType c2 = (ConstantNameAndType)constant_pool.getConstant(index, (byte)12);
      
      String signature = constant_pool.constantToString(c2.getSignatureIndex(), (byte)1);
      
      String[] args = Utility.methodSignatureArgumentTypes(signature, false);
      String type = Utility.methodSignatureReturnType(signature, false);
      
      buf.append(name + ".<A HREF=\"" + class_name + "_cp.html#cp" + m_index + "\" TARGET=ConstantPool>" + str + "</A>" + "(");
      


      for (int i = 0; i < args.length; i++) {
        buf.append(Class2HTML.referenceType(args[i]));
        
        if (i < args.length - 1) {
          buf.append(", ");
        }
      }
      buf.append("):" + Class2HTML.referenceType(type));
      
      break;
    

    case 19: 
    case 20: 
      index = bytes.readShort();
      
      buf.append("<A HREF=\"" + class_name + "_cp.html#cp" + index + "\" TARGET=\"ConstantPool\">" + Class2HTML.toHTML(constant_pool.constantToString(index, constant_pool.getConstant(index).getTag())) + "</a>");
      




      break;
    
    case 18: 
      index = bytes.readUnsignedByte();
      buf.append("<A HREF=\"" + class_name + "_cp.html#cp" + index + "\" TARGET=\"ConstantPool\">" + Class2HTML.toHTML(constant_pool.constantToString(index, constant_pool.getConstant(index).getTag())) + "</a>");
      




      break;
    


    case 189: 
      index = bytes.readShort();
      
      buf.append(constant_html.referenceConstant(index));
      break;
    


    case 197: 
      index = bytes.readShort();
      int dimensions = bytes.readByte();
      buf.append(constant_html.referenceConstant(index) + ":" + dimensions + "-dimensional");
      break;
    case 132: 
      int constant;
      

      if (wide) {
        vindex = bytes.readShort();
        constant = bytes.readShort();
        wide = false;
      }
      else {
        vindex = bytes.readUnsignedByte();
        constant = bytes.readByte();
      }
      buf.append("%" + vindex + " " + constant);
      break;
    case 26: case 27: case 28: case 29: case 30: case 31: case 32: case 33: case 34: case 35: case 36: case 37: case 38: case 39: case 40: case 41: case 42: case 43: case 44: case 45: case 46: case 47: case 48: case 49: case 50: case 51: case 52: case 53: case 59: case 60: case 61: case 62: case 63: case 64: case 65: case 66: case 67: case 68: case 69: case 70: case 71: case 72: case 73: case 74: case 75: case 76: case 77: case 78: case 79: case 80: case 81: case 82: case 83: case 84: case 85: case 86: case 87: case 88: case 89: case 90: case 91: case 92: case 93: case 94: case 95: case 96: case 97: 
    case 98: case 99: case 100: case 101: case 102: case 103: case 104: case 105: case 106: case 107: case 108: case 109: case 110: case 111: case 112: case 113: case 114: case 115: case 116: case 117: case 118: case 119: case 120: case 121: case 122: case 123: case 124: case 125: case 126: case 127: case 128: case 129: case 130: case 131: case 133: case 134: case 135: case 136: case 137: case 138: case 139: case 140: case 141: case 142: case 143: case 144: case 145: case 146: case 147: case 148: case 149: case 150: case 151: case 152: case 172: case 173: case 174: case 175: case 176: case 177: case 186: case 190: case 191: case 194: case 195: default: 
      if (Constants.NO_OF_OPERANDS[opcode] > 0) {
        for (int i = 0; i < Constants.TYPE_OF_OPERANDS[opcode].length; i++) {
          switch (Constants.TYPE_OF_OPERANDS[opcode][i]) {
          case 8: 
            buf.append(bytes.readUnsignedByte());
            break;
          
          case 9: 
            buf.append(bytes.readShort());
            break;
          
          case 10: 
            buf.append(bytes.readInt());
            break;
          
          default: 
            System.err.println("Unreachable default case reached!");
            System.exit(-1);
          }
          buf.append("&nbsp;");
        }
      }
      break;
    }
    buf.append("</TD>");
    return buf.toString();
  }
  





  private final void findGotos(ByteSequence bytes, Method method, Code code)
    throws IOException
  {
    goto_set = new BitSet(bytes.available());
    





    if (code != null) {
      CodeException[] ce = code.getExceptionTable();
      int len = ce.length;
      
      for (int i = 0; i < len; i++) {
        goto_set.set(ce[i].getStartPC());
        goto_set.set(ce[i].getEndPC());
        goto_set.set(ce[i].getHandlerPC());
      }
      

      Attribute[] attributes = code.getAttributes();
      for (int i = 0; i < attributes.length; i++) {
        if (attributes[i].getTag() == 5) {
          LocalVariable[] vars = ((LocalVariableTable)attributes[i]).getLocalVariableTable();
          
          for (int j = 0; j < vars.length; j++) {
            int start = vars[j].getStartPC();
            int end = start + vars[j].getLength();
            goto_set.set(start);
            goto_set.set(end);
          }
          break;
        }
      }
    }
    

    for (int i = 0; bytes.available() > 0; i++) {
      int opcode = bytes.readUnsignedByte();
      int index;
      switch (opcode)
      {
      case 170: 
      case 171: 
        int remainder = bytes.getIndex() % 4;
        int no_pad_bytes = remainder == 0 ? 0 : 4 - remainder;
        

        for (int j = 0; j < no_pad_bytes; j++) {
          bytes.readByte();
        }
        
        int default_offset = bytes.readInt();
        int offset;
        if (opcode == 170) {
          int low = bytes.readInt();
          int high = bytes.readInt();
          
          offset = bytes.getIndex() - 12 - no_pad_bytes - 1;
          default_offset += offset;
          goto_set.set(default_offset);
          
          for (int j = 0; j < high - low + 1; j++) {
            index = offset + bytes.readInt();
            goto_set.set(index);
          }
        }
        else {
          int npairs = bytes.readInt();
          
          offset = bytes.getIndex() - 8 - no_pad_bytes - 1;
          default_offset += offset;
          goto_set.set(default_offset);
          
          for (int j = 0; j < npairs; j++) {
            int match = bytes.readInt();
            
            index = offset + bytes.readInt();
            goto_set.set(index);
          }
        }
        break;
      case 153: case 154: case 155: 
      case 156: case 157: case 158: 
      case 159: case 160: case 161: 
      case 162: case 163: case 164: 
      case 165: case 166: 
      case 167: case 168: 
      case 198: case 199: 
        index = bytes.getIndex() + bytes.readShort() - 1;
        
        goto_set.set(index);
        break;
      
      case 200: 
      case 201: 
        index = bytes.getIndex() + bytes.readInt() - 1;
        goto_set.set(index);
        break;
      case 169: case 172: case 173: case 174: case 175: case 176: case 177: case 178: case 179: case 180: case 181: case 182: case 183: case 184: 
      case 185: case 186: case 187: case 188: case 189: case 190: case 191: case 192: case 193: case 194: case 195: case 196: case 197: default: 
        bytes.unreadByte();
        codeToHTML(bytes, 0);
      }
      
    }
  }
  



  private void writeMethod(Method method, int method_number)
    throws IOException
  {
    String signature = method.getSignature();
    
    String[] args = Utility.methodSignatureArgumentTypes(signature, false);
    
    String type = Utility.methodSignatureReturnType(signature, false);
    
    String name = method.getName();
    String html_name = Class2HTML.toHTML(name);
    
    String access = Utility.accessToString(method.getAccessFlags());
    access = Utility.replace(access, " ", "&nbsp;");
    
    Attribute[] attributes = method.getAttributes();
    
    file.print("<P><B><FONT COLOR=\"#FF0000\">" + access + "</FONT>&nbsp;" + "<A NAME=method" + method_number + ">" + Class2HTML.referenceType(type) + "</A>&nbsp<A HREF=\"" + class_name + "_methods.html#method" + method_number + "\" TARGET=Methods>" + html_name + "</A>(");
    



    for (int i = 0; i < args.length; i++) {
      file.print(Class2HTML.referenceType(args[i]));
      if (i < args.length - 1) {
        file.print(",&nbsp;");
      }
    }
    file.println(")</B></P>");
    
    Code c = null;
    byte[] code = null;
    
    if (attributes.length > 0) {
      file.print("<H4>Attributes</H4><UL>\n");
      for (int i = 0; i < attributes.length; i++) {
        byte tag = attributes[i].getTag();
        
        if (tag != -1) {
          file.print("<LI><A HREF=\"" + class_name + "_attributes.html#method" + method_number + "@" + i + "\" TARGET=Attributes>" + Constants.ATTRIBUTE_NAMES[tag] + "</A></LI>\n");
        }
        else {
          file.print("<LI>" + attributes[i] + "</LI>");
        }
        if (tag == 2) {
          c = (Code)attributes[i];
          Attribute[] attributes2 = c.getAttributes();
          code = c.getCode();
          
          file.print("<UL>");
          for (int j = 0; j < attributes2.length; j++) {
            tag = attributes2[j].getTag();
            file.print("<LI><A HREF=\"" + class_name + "_attributes.html#" + "method" + method_number + "@" + i + "@" + j + "\" TARGET=Attributes>" + Constants.ATTRIBUTE_NAMES[tag] + "</A></LI>\n");
          }
          


          file.print("</UL>");
        }
      }
      file.println("</UL>");
    }
    
    if (code != null)
    {


      ByteSequence stream = new ByteSequence(code);
      stream.mark(stream.available());
      findGotos(stream, method, c);
      stream.reset();
      
      file.println("<TABLE BORDER=0><TR><TH ALIGN=LEFT>Byte<BR>offset</TH><TH ALIGN=LEFT>Instruction</TH><TH ALIGN=LEFT>Argument</TH>");
      

      for (int i = 0; stream.available() > 0; i++) {
        int offset = stream.getIndex();
        String str = codeToHTML(stream, method_number);
        String anchor = "";
        



        if (goto_set.get(offset)) {
          anchor = "<A NAME=code" + method_number + "@" + offset + "></A>";
        }
        String anchor2;
        if (stream.getIndex() == code.length) {
          anchor2 = "<A NAME=code" + method_number + "@" + code.length + ">" + offset + "</A>";
        } else {
          anchor2 = "" + offset;
        }
        file.println("<TR VALIGN=TOP><TD>" + anchor2 + "</TD><TD>" + anchor + str + "</TR>");
      }
      

      file.println("<TR><TD> </A></TD></TR>");
      file.println("</TABLE>");
    }
  }
}
