import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;

public class RequiredData {
	
	protected final String baseUrl = "https://www.amazon.in";
	protected FileInputStream xlsxFile = null;
	protected Workbook wb = null;
	protected String loginTestDataFile = "/home/intern/git/Testing/AmazonTesting/src/LoginTestData.xlsx";
	protected String Pwindow ;
	protected String sheetName = "Sheet1";
	protected String firefoxDriverLocation =  "/home/intern/git/Testing/AmazonTesting/Browser Driver/geckodriver";
	protected String chromeDriverLocation = "/home/intern/git/Testing/AmazonTesting/Browser Driver/chromedriver";
	
	@DataProvider(name = "Login")
	public Object[][] credentials() {
		try {
			xlsxFile = new FileInputStream(loginTestDataFile);
			wb = new XSSFWorkbook(xlsxFile);
		}catch(FileNotFoundException e) {
			System.out.println("File Not Found");
		}
		catch(IOException e) {
			System.out.println("Input Output");
		}


		XSSFSheet sheet = (XSSFSheet) wb.getSheet(sheetName);

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
}
