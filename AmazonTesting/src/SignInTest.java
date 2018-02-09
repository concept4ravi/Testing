

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class SignInTest extends RequiredData{

	Boolean flag;
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

	@Test
	public void launchSite() {
		driver.get(baseUrl);
		try {
			flag=driver.findElement(By.id("nav-logo")).isDisplayed();

		}catch(Exception e) {
			flag=false;
			System.out.println(e.getMessage());
		}
		Assert.assertTrue(flag, "Unable to access "+baseUrl);
	}

	@Test
	public void openSignInPage() {
		try {
			flag=driver.findElement(By.cssSelector("#nav-link-yourAccount > span.nav-line-1")).isDisplayed();

		}catch(Exception e) {
			flag=false;
			System.out.println(e.getMessage());
		}
		Assert.assertTrue(flag, "Sign In Option Not available");
	}

	@Test(dataProvider = "Login")
	public void login(String uname,String password) {
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
	}

	@Test
	public void validateLogin() throws InterruptedException {
		Thread.sleep(1000);
		WebElement link = driver.findElement(By.id("nav-link-yourAccount"));
		boolean b = link.findElement(By.cssSelector(".nav-line-1")).getText().contains("Hello");
		Assert.assertTrue(b,"Login Failed Failed");
	}

	@Test
	public void deleteCookies() {
		driver.manage().deleteAllCookies();
	}


}
