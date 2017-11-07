package com.gargoylesoftware.htmlunit;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;






















public class PluginConfiguration
  implements Serializable, Cloneable
{
  private final String description_;
  private final String filename_;
  private final String name_;
  private final String version_;
  private final Set<MimeType> mimeTypes_ = new HashSet();
  







  public PluginConfiguration(String name, String description, String version, String filename)
  {
    WebAssert.notNull("name", name);
    name_ = name;
    description_ = description;
    version_ = version;
    filename_ = filename;
  }
  



  public String getDescription()
  {
    return description_;
  }
  



  public String getFilename()
  {
    return filename_;
  }
  



  public String getName()
  {
    return name_;
  }
  



  public String getVersion()
  {
    return version_;
  }
  



  public Set<MimeType> getMimeTypes()
  {
    return mimeTypes_;
  }
  



  public int hashCode()
  {
    return name_.hashCode();
  }
  



  public boolean equals(Object o)
  {
    if (!(o instanceof PluginConfiguration)) {
      return false;
    }
    PluginConfiguration other = (PluginConfiguration)o;
    return name_.equals(name_);
  }
  





  public PluginConfiguration clone()
  {
    PluginConfiguration clone = new PluginConfiguration(getName(), getDescription(), getVersion(), 
      getFilename());
    clone.getMimeTypes().addAll(getMimeTypes());
    
    return clone;
  }
  


  public static class MimeType
    implements Serializable
  {
    private final String description_;
    

    private final String suffixes_;
    

    private final String type_;
    

    public MimeType(String type, String description, String suffixes)
    {
      WebAssert.notNull("type", type);
      type_ = type;
      description_ = description;
      suffixes_ = suffixes;
    }
    



    public String getDescription()
    {
      return description_;
    }
    



    public String getSuffixes()
    {
      return suffixes_;
    }
    



    public String getType()
    {
      return type_;
    }
    



    public int hashCode()
    {
      return type_.hashCode();
    }
    



    public boolean equals(Object o)
    {
      if (!(o instanceof MimeType)) {
        return false;
      }
      MimeType other = (MimeType)o;
      return type_.equals(type_);
    }
  }
}
