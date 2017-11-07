package org.apache.http.config;

import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import org.apache.http.Consts;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.util.Args;

































@Contract(threading=ThreadingBehavior.IMMUTABLE)
public class ConnectionConfig
  implements Cloneable
{
  public static final ConnectionConfig DEFAULT = new Builder().build();
  
  private final int bufferSize;
  
  private final int fragmentSizeHint;
  
  private final Charset charset;
  
  private final CodingErrorAction malformedInputAction;
  
  private final CodingErrorAction unmappableInputAction;
  
  private final MessageConstraints messageConstraints;
  

  ConnectionConfig(int bufferSize, int fragmentSizeHint, Charset charset, CodingErrorAction malformedInputAction, CodingErrorAction unmappableInputAction, MessageConstraints messageConstraints)
  {
    this.bufferSize = bufferSize;
    this.fragmentSizeHint = fragmentSizeHint;
    this.charset = charset;
    this.malformedInputAction = malformedInputAction;
    this.unmappableInputAction = unmappableInputAction;
    this.messageConstraints = messageConstraints;
  }
  
  public int getBufferSize() {
    return bufferSize;
  }
  
  public int getFragmentSizeHint() {
    return fragmentSizeHint;
  }
  
  public Charset getCharset() {
    return charset;
  }
  
  public CodingErrorAction getMalformedInputAction() {
    return malformedInputAction;
  }
  
  public CodingErrorAction getUnmappableInputAction() {
    return unmappableInputAction;
  }
  
  public MessageConstraints getMessageConstraints() {
    return messageConstraints;
  }
  
  protected ConnectionConfig clone() throws CloneNotSupportedException
  {
    return (ConnectionConfig)super.clone();
  }
  
  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    builder.append("[bufferSize=").append(bufferSize).append(", fragmentSizeHint=").append(fragmentSizeHint).append(", charset=").append(charset).append(", malformedInputAction=").append(malformedInputAction).append(", unmappableInputAction=").append(unmappableInputAction).append(", messageConstraints=").append(messageConstraints).append("]");
    





    return builder.toString();
  }
  
  public static Builder custom() {
    return new Builder();
  }
  
  public static Builder copy(ConnectionConfig config) {
    Args.notNull(config, "Connection config");
    return new Builder().setBufferSize(config.getBufferSize()).setCharset(config.getCharset()).setFragmentSizeHint(config.getFragmentSizeHint()).setMalformedInputAction(config.getMalformedInputAction()).setUnmappableInputAction(config.getUnmappableInputAction()).setMessageConstraints(config.getMessageConstraints());
  }
  

  public static class Builder
  {
    private int bufferSize;
    
    private int fragmentSizeHint;
    
    private Charset charset;
    
    private CodingErrorAction malformedInputAction;
    
    private CodingErrorAction unmappableInputAction;
    private MessageConstraints messageConstraints;
    
    Builder()
    {
      fragmentSizeHint = -1;
    }
    
    public Builder setBufferSize(int bufferSize) {
      this.bufferSize = bufferSize;
      return this;
    }
    
    public Builder setFragmentSizeHint(int fragmentSizeHint) {
      this.fragmentSizeHint = fragmentSizeHint;
      return this;
    }
    
    public Builder setCharset(Charset charset) {
      this.charset = charset;
      return this;
    }
    
    public Builder setMalformedInputAction(CodingErrorAction malformedInputAction) {
      this.malformedInputAction = malformedInputAction;
      if ((malformedInputAction != null) && (charset == null)) {
        charset = Consts.ASCII;
      }
      return this;
    }
    
    public Builder setUnmappableInputAction(CodingErrorAction unmappableInputAction) {
      this.unmappableInputAction = unmappableInputAction;
      if ((unmappableInputAction != null) && (charset == null)) {
        charset = Consts.ASCII;
      }
      return this;
    }
    
    public Builder setMessageConstraints(MessageConstraints messageConstraints) {
      this.messageConstraints = messageConstraints;
      return this;
    }
    
    public ConnectionConfig build() {
      Charset cs = charset;
      if ((cs == null) && ((malformedInputAction != null) || (unmappableInputAction != null))) {
        cs = Consts.ASCII;
      }
      int bufSize = bufferSize > 0 ? bufferSize : 8192;
      int fragmentHintSize = fragmentSizeHint >= 0 ? fragmentSizeHint : bufSize;
      return new ConnectionConfig(bufSize, fragmentHintSize, cs, malformedInputAction, unmappableInputAction, messageConstraints);
    }
  }
}
