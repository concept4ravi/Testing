

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class SignInTest extends RequiredData{
	
	Boolean flag;
	private String sheetName = "Sheet1";
	private WebDriver driver;
	@BeforeTest
	@Parameters({"browser"})
	public void setBrowser(String browser) {
		if(browser.equals("firefox")) {
			System.setProperty("webdriver.gecko.driver",firefoxDriverLocation); 
			driver = new FirefoxDriver();
		}else if(browser.equals("chrome")) {
			System.setProperty("webdriver.chrome.driver",chromeDriverLocation);
			driver = new ChromeDriver();
		}
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}

	@Test(dataProvider = "Login")
	public void signIn(String uname,String password) throws InterruptedException {
		driver.get(baseUrl);
		try {
			flag=driver.findElement(By.id("nav-logo")).isDisplayed();
		
		}catch(Exception e) {
			flag=false;
			System.out.println(e.getMessage());
		}
		Assert.assertTrue(flag, "Unable to access "+baseUrl);
		try {
			flag=driver.findElement(By.cssSelector("#nav-link-yourAccount > span.nav-line-1")).isDisplayed();
		
		}catch(Exception e) {
			flag=false;
			System.out.println(e.getMessage());
		}
		Assert.assertTrue(flag, "Sign In Option Not available");
		driver.findElement(By.cssSelector("#nav-link-yourAccount > span.nav-line-1")).click();
		try {
			flag=driver.findElement(By.id("ap_email")).isDisplayed();
		
		}catch(Exception e) {
			flag=false;
			System.out.println(e.getMessage());
		}
		Assert.assertTrue(flag, "Email id field not available");
		driver.findElement(By.id("ap_email")).click();
		driver.findElement(By.id("ap_email")).clear();
		driver.findElement(By.id("ap_email")).sendKeys(uname);

		driver.findElement(By.cssSelector("span.a-button-inner > #continue")).click();
		try {
			flag=driver.findElement(By.id("ap_password")).isDisplayed();
		
		}catch(Exception e) {
			flag=false;
			System.out.println(e.getMessage());
		}
		Assert.assertTrue(flag, "Password field not available");
		driver.findElement(By.id("ap_password")).click();
		driver.findElement(By.id("ap_password")).clear();
		driver.findElement(By.id("ap_password")).sendKeys(password);
		driver.findElement(By.id("signInSubmit")).click();
		Thread.sleep(1000);
		WebElement link = driver.findElement(By.id("nav-link-yourAccount"));
		boolean b = link.findElement(By.cssSelector(".nav-line-1")).getText().contains("Hello");
		Assert.assertTrue(b,"Login Failed for Username "+uname+" with Password '"+password+"'");
		System.out.println("Username "+uname+" with Password '"+password+"' is Successfully Logged In");
		driver.manage().deleteAllCookies();
	}

}
