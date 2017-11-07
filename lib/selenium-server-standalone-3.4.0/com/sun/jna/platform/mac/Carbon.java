package com.sun.jna.platform.mac;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.ByValue;
import com.sun.jna.ptr.PointerByReference;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.List;




















public abstract interface Carbon
  extends Library
{
  public static final Carbon INSTANCE = (Carbon)Native.loadLibrary("Carbon", Carbon.class);
  

  public static final int cmdKey = 256;
  

  public static final int shiftKey = 512;
  
  public static final int optionKey = 2048;
  
  public static final int controlKey = 4096;
  

  public abstract Pointer GetEventDispatcherTarget();
  

  public abstract int InstallEventHandler(Pointer paramPointer1, EventHandlerProcPtr paramEventHandlerProcPtr, int paramInt, EventTypeSpec[] paramArrayOfEventTypeSpec, Pointer paramPointer2, PointerByReference paramPointerByReference);
  

  public abstract int RegisterEventHotKey(int paramInt1, int paramInt2, Carbon.EventHotKeyID.ByValue paramByValue, Pointer paramPointer, int paramInt3, PointerByReference paramPointerByReference);
  

  public abstract int GetEventParameter(Pointer paramPointer1, int paramInt1, int paramInt2, Pointer paramPointer2, int paramInt3, IntBuffer paramIntBuffer, EventHotKeyID paramEventHotKeyID);
  

  public abstract int RemoveEventHandler(Pointer paramPointer);
  

  public abstract int UnregisterEventHotKey(Pointer paramPointer);
  

  public static class EventTypeSpec
    extends Structure
  {
    public int eventClass;
    
    public int eventKind;
    

    public EventTypeSpec() {}
    
    protected List getFieldOrder() { return Arrays.asList(new String[] { "eventClass", "eventKind" }); }
  }
  
  public static class EventHotKeyID extends Structure {
    public int signature;
    public int id;
    
    public EventHotKeyID() {}
    
    protected List getFieldOrder() { return Arrays.asList(new String[] { "signature", "id" }); }
    
    public static class ByValue
      extends Carbon.EventHotKeyID
      implements Structure.ByValue
    {
      public ByValue() {}
    }
  }
  
  public static abstract interface EventHandlerProcPtr
    extends Callback
  {
    public abstract int callback(Pointer paramPointer1, Pointer paramPointer2, Pointer paramPointer3);
  }
}
