package org.apache.bcel.generic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.util.ByteSequence;



























































public class JSR_W
  extends JsrInstruction
{
  JSR_W() {}
  
  public JSR_W(InstructionHandle target)
  {
    super((short)201, target);
    length = 5;
  }
  


  public void dump(DataOutputStream out)
    throws IOException
  {
    index = getTargetOffset();
    out.writeByte(opcode);
    out.writeInt(index);
  }
  


  protected void initFromFile(ByteSequence bytes, boolean wide)
    throws IOException
  {
    index = bytes.readInt();
    length = 5;
  }
  







  public void accept(Visitor v)
  {
    v.visitStackProducer(this);
    v.visitBranchInstruction(this);
    v.visitJsrInstruction(this);
    v.visitJSR_W(this);
  }
}
