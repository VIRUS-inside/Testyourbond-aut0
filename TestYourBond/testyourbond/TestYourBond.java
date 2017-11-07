package testyourbond;

import java.io.PrintStream;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.Timeouts;
import org.openqa.selenium.WebElement;

public class TestYourBond
{
  WebDriver driver;
  
  public static void main(String[] args) throws java.io.IOException
  {
    new TestYourBond();
  }
  

  static int counter = 6493903;
  String Name;
  String url;
  
  public TestYourBond()
  {
    setup();
    connect();
    hackNow();
  }
  
  TestYourBond(String url, String name)
  {
    Name = name;
    this.url = url;
    setup();
    connect();
    hackNow();
  }
  
  void setup() {
    System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
    driver = new org.openqa.selenium.chrome.ChromeDriver();
  }
  







  void connect() {}
  






  void hackNow()
  {
    for (;;)
    {
      System.out.println(counter);
      
      driver.navigate().to(url);
      driver.manage().timeouts().implicitlyWait(5L, TimeUnit.SECONDS);
      
      WebElement name = driver.findElement(By.id("name"));
      
      name.sendKeys(new CharSequence[] { Name });
      
      WebElement start = driver.findElement(By.id("start"));
      start.click();
      

      for (int i = 0; i <= 15; i++)
      {
        int x = 1;
        boolean bool = true;
        while ((bool == true) && (x <= 15))
        {
          WebElement curr_ques = null;
          try {
            curr_ques = driver.findElement(By.cssSelector("div.question.unanswered"));
          } catch (NoSuchElementException e) {
            bool = false;
          }
          try
          {
            driver.manage().timeouts().implicitlyWait(5L, TimeUnit.SECONDS);
            curr_ques.findElement(By.cssSelector("td.answer.correct")).click();
            WebElement last_ques = curr_ques;
            x++;
          }
          catch (Exception localException) {}
        }
      }
      






      driver.quit();
      counter += 1;
    }
  }
}
