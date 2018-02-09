

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class emptyCart extends RequiredData{
	private WebDriver driver;
	public String Pwindow ;
	Boolean flag;
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

	}

	@Test(dependsOnMethods = "signIn")
	public void emptyCart() throws Exception {
		driver.findElement(By.id("nav-cart")).click();
		try {
			driver.findElement(By.cssSelector(".sc-list-body"));
		}catch(NoSuchElementException e) {
			System.out.println("Your Shopping Cart is empty.");
			return;
		}
		WebElement ele = driver.findElement(By.cssSelector(".sc-list-body"));
		ele.findElements(By.tagName("input"));


		List<WebElement> inputList = ele.findElements(By.tagName("input"));

		if(inputList.size()>0) {
			List<WebElement> deleteList = new ArrayList<WebElement>();
			String value;
			for(WebElement inp:inputList) {
				value=inp.getAttribute("value");
				if(value.equals("Delete")) {
					deleteList.add(inp);
				}

			}
			System.out.println(deleteList.size());

			Iterator<WebElement> itr = deleteList.iterator();

			while(itr.hasNext()) {
				Thread.sleep(3000);
				itr.next().click();
			}

		}
	}

}
