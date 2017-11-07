package javax.servlet.descriptor;

import java.util.Collection;

public abstract interface JspConfigDescriptor
{
  public abstract Collection<TaglibDescriptor> getTaglibs();
  
  public abstract Collection<JspPropertyGroupDescriptor> getJspPropertyGroups();
}
