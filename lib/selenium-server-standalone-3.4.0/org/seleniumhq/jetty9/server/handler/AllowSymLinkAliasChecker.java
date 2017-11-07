package org.seleniumhq.jetty9.server.handler;

import java.nio.file.Files;
import java.nio.file.Path;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
import org.seleniumhq.jetty9.util.resource.PathResource;
import org.seleniumhq.jetty9.util.resource.Resource;






























public class AllowSymLinkAliasChecker
  implements ContextHandler.AliasCheck
{
  private static final Logger LOG = Log.getLogger(AllowSymLinkAliasChecker.class);
  
  public AllowSymLinkAliasChecker() {}
  
  public boolean check(String uri, Resource resource)
  {
    if (!(resource instanceof PathResource)) {
      return false;
    }
    PathResource pathResource = (PathResource)resource;
    
    try
    {
      Path path = pathResource.getPath();
      Path alias = pathResource.getAliasPath();
      
      if (path.equals(alias)) {
        return false;
      }
      if ((hasSymbolicLink(path)) && (Files.isSameFile(path, alias)))
      {
        if (LOG.isDebugEnabled())
          LOG.debug("Allow symlink {} --> {}", new Object[] { resource, pathResource.getAliasPath() });
        return true;
      }
    }
    catch (Exception e)
    {
      LOG.ignore(e);
    }
    
    return false;
  }
  

  private boolean hasSymbolicLink(Path path)
  {
    if (Files.isSymbolicLink(path))
    {
      return true;
    }
    

    Path base = path.getRoot();
    for (Path segment : path)
    {
      base = base.resolve(segment);
      if (Files.isSymbolicLink(base))
      {
        return true;
      }
    }
    
    return false;
  }
}
