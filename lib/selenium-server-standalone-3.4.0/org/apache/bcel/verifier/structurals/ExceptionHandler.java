package org.apache.bcel.verifier.structurals;

import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.ObjectType;
































































public class ExceptionHandler
{
  private ObjectType catchtype;
  private InstructionHandle handlerpc;
  
  ExceptionHandler(ObjectType catch_type, InstructionHandle handler_pc)
  {
    catchtype = catch_type;
    handlerpc = handler_pc;
  }
  


  public ObjectType getExceptionType()
  {
    return catchtype;
  }
  


  public InstructionHandle getHandlerStart()
  {
    return handlerpc;
  }
}
