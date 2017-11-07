package org.seleniumhq.jetty9.http.pathmap;

public enum PathSpecGroup
{
  ROOT,  EXACT,  MIDDLE_GLOB,  PREFIX_GLOB,  SUFFIX_GLOB,  DEFAULT;
  
  private PathSpecGroup() {}
}
