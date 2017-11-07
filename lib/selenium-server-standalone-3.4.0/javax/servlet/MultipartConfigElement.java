package javax.servlet;

import javax.servlet.annotation.MultipartConfig;


















































public class MultipartConfigElement
{
  private String location;
  private long maxFileSize;
  private long maxRequestSize;
  private int fileSizeThreshold;
  
  public MultipartConfigElement(String location)
  {
    if (location == null) {
      this.location = "";
    } else {
      this.location = location;
    }
    maxFileSize = -1L;
    maxRequestSize = -1L;
    fileSizeThreshold = 0;
  }
  










  public MultipartConfigElement(String location, long maxFileSize, long maxRequestSize, int fileSizeThreshold)
  {
    if (location == null) {
      this.location = "";
    } else {
      this.location = location;
    }
    this.maxFileSize = maxFileSize;
    this.maxRequestSize = maxRequestSize;
    this.fileSizeThreshold = fileSizeThreshold;
  }
  




  public MultipartConfigElement(MultipartConfig annotation)
  {
    location = annotation.location();
    fileSizeThreshold = annotation.fileSizeThreshold();
    maxFileSize = annotation.maxFileSize();
    maxRequestSize = annotation.maxRequestSize();
  }
  




  public String getLocation()
  {
    return location;
  }
  




  public long getMaxFileSize()
  {
    return maxFileSize;
  }
  




  public long getMaxRequestSize()
  {
    return maxRequestSize;
  }
  




  public int getFileSizeThreshold()
  {
    return fileSizeThreshold;
  }
}
