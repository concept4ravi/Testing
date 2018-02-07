

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
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
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class purchaseTest {
	WebDriver driver;
	protected final String baseUrl = "https://www.amazon.in";
	public FileInputStream xlsxFile = null;
	public Workbook wb = null;
	public String filename = "/home/intern/eclipse-workspace/AmazonTesting/src/LoginTestData.xlsx";
	public String Pwindow ;
	Boolean flag;
	@DataProvider(name = "Login")
	public Object[][] credentials() {
		try {
			xlsxFile = new FileInputStream(filename);
			wb = new XSSFWorkbook(xlsxFile);
		}catch(FileNotFoundException e) {
			System.out.println("File Not Found");
		}
		catch(IOException e) {
			System.out.println("Input Output");
		}


		XSSFSheet sheet = (XSSFSheet) wb.getSheet("Sheet1");

		int i=0,j,maxRow=sheet.getLastRowNum();

		XSSFRow row;
		XSSFCell cell;
		Iterator<Row> rows = sheet.rowIterator();

		Object[][] obj = new Object[maxRow][2];
		rows = sheet.rowIterator();
		rows.next();
		while(rows.hasNext()) {
			j=0;
			row = (XSSFRow) rows.next();
			Iterator<Cell> cells = row.cellIterator();
			while(cells.hasNext()) {
				cell = (XSSFCell) cells.next();
				obj[i][j++] = cell.getStringCellValue();

			}
			++i;
		}

		try {
			xlsxFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return obj;

	}  
	@BeforeTest
	@Parameters({"browser"})
	public void setBrowser(String browser) {
		if(browser.equals("firefox")) {
			System.setProperty("webdriver.gecko.driver","/home/intern/eclipse-workspace/AmazonTesting/Browser Driver/geckodriver"); 
			driver = new FirefoxDriver();
		}else if(browser.equals("chrome")) {
			System.setProperty("webdriver.chrome.driver","/home/intern/eclipse-workspace/AmazonTesting/Browser Driver/chromedriver");
			driver = new ChromeDriver();
		}
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
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
		System.out.println("Successfully Logged In");

	}

	@Test(dependsOnMethods = "signIn")
	public void signOut() throws InterruptedException {
		Actions action = new Actions(driver);
		action.moveToElement(driver.findElement(By.id("nav-link-yourAccount"))).perform();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("#nav-item-signout-sa > span.nav-text")).click();
	}
	//@Test(dependsOnMethods="signIn")
	public void purchaseItemTest() throws Exception {
		System.out.println("Waiting for the Item Selection./nOnce you reach the details page of the item, Order will automatiacally get placed to your default address.");
		System.out.println("Max wait 30 seconds");
		try {
			driver.findElement(By.id("add-to-cart-button"));
		}catch(NoSuchElementException e) {
			Pwindow = driver.getWindowHandle();
			ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
			driver.switchTo().window(tabs2.get(1));
		}
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("add-to-cart-button")));
		driver.findElement(By.id("add-to-cart-button")).click();
		driver.findElement(By.id("hlb-view-cart-announce")).click();
		driver.findElement(By.name("proceedToCheckout")).click();
		/*try {

		driver.findElement(By.linkText("Deliver to this address Room No. 36, PG Hostel")).click();
		}catch(Exception e) {}*/
		try {
			wait = new WebDriverWait(driver,3);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input.a-button-text")));
			driver.findElement(By.cssSelector("input.a-button-text")).click();
		}catch(Exception e) {}
		try {
			wait = new WebDriverWait(driver,3);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//input[@name='paymentMethod'])[6]")));
			driver.findElement(By.xpath("(//input[@name='paymentMethod'])[6]")).click();
		}catch(Exception e) {}
		try {
			wait = new WebDriverWait(driver,3);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.id("continue-top")));
			driver.findElement(By.id("continue-top")).click();
		}catch(Exception e) {}
		driver.findElement(By.name("placeYourOrder1")).click();
		try {
			wait = new WebDriverWait(driver,10);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".a-color-success")));
			driver.findElement(By.cssSelector(".a-color-success"));
			flag=true;
		}catch(Exception e) {
			flag = false;

		}

		Assert.assertTrue(flag,"Order InComplete");

		System.out.println(driver.findElement(By.cssSelector(".a-color-success")).getText());

	}

}
