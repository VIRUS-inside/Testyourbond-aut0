package org.junit.rules;

import java.io.File;
import java.io.IOException;























public class TemporaryFolder
  extends ExternalResource
{
  private final File parentFolder;
  private File folder;
  
  public TemporaryFolder()
  {
    this(null);
  }
  
  public TemporaryFolder(File parentFolder) {
    this.parentFolder = parentFolder;
  }
  
  protected void before() throws Throwable
  {
    create();
  }
  
  protected void after()
  {
    delete();
  }
  



  public void create()
    throws IOException
  {
    folder = createTemporaryFolderIn(parentFolder);
  }
  

  public File newFile(String fileName)
    throws IOException
  {
    File file = new File(getRoot(), fileName);
    if (!file.createNewFile()) {
      throw new IOException("a file with the name '" + fileName + "' already exists in the test folder");
    }
    
    return file;
  }
  

  public File newFile()
    throws IOException
  {
    return File.createTempFile("junit", null, getRoot());
  }
  


  public File newFolder(String folder)
    throws IOException
  {
    return newFolder(new String[] { folder });
  }
  


  public File newFolder(String... folderNames)
    throws IOException
  {
    File file = getRoot();
    for (int i = 0; i < folderNames.length; i++) {
      String folderName = folderNames[i];
      validateFolderName(folderName);
      file = new File(file, folderName);
      if ((!file.mkdir()) && (isLastElementInArray(i, folderNames))) {
        throw new IOException("a folder with the name '" + folderName + "' already exists");
      }
    }
    
    return file;
  }
  




  private void validateFolderName(String folderName)
    throws IOException
  {
    File tempFile = new File(folderName);
    if (tempFile.getParent() != null) {
      String errorMsg = "Folder name cannot consist of multiple path components separated by a file separator. Please use newFolder('MyParentFolder','MyFolder') to create hierarchies of folders";
      
      throw new IOException(errorMsg);
    }
  }
  
  private boolean isLastElementInArray(int index, String[] array) {
    return index == array.length - 1;
  }
  

  public File newFolder()
    throws IOException
  {
    return createTemporaryFolderIn(getRoot());
  }
  
  private File createTemporaryFolderIn(File parentFolder) throws IOException {
    File createdFolder = File.createTempFile("junit", "", parentFolder);
    createdFolder.delete();
    createdFolder.mkdir();
    return createdFolder;
  }
  


  public File getRoot()
  {
    if (folder == null) {
      throw new IllegalStateException("the temporary folder has not yet been created");
    }
    
    return folder;
  }
  



  public void delete()
  {
    if (folder != null) {
      recursiveDelete(folder);
    }
  }
  
  private void recursiveDelete(File file) {
    File[] files = file.listFiles();
    if (files != null) {
      for (File each : files) {
        recursiveDelete(each);
      }
    }
    file.delete();
  }
}
