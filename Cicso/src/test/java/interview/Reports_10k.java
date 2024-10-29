package interview;

import java.io.FileInputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Reports_10k {
	
	public static WebDriver driver;

	public static void main(String[] args) throws Throwable {
		// TODO Auto-generated method stub
		WebDriverManager.chromedriver().setup();
		driver= new ChromeDriver();
		driver.manage().window().maximize();
		getData();
		driver.close();
	}
	
	private static void getData() throws  InterruptedException, Throwable {
		
		FileInputStream fis= new FileInputStream(System.getProperty("user.dir")+"\\testDataResources\\Nvidia_Event_Update2.xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet=workbook.getSheetAt(0);
		Iterator<Row> rows=sheet.iterator();
		Row firstRow=rows.next();
		
		int i=1;
		while(rows.hasNext()) {
			Row getRow = sheet.getRow(i);
			Iterator<Cell> cell=getRow.iterator();
			Cell c=cell.next();
			String CompanyName=c.getStringCellValue();
			driver.get("https://www.annualreports.com/Company/");
			Thread.sleep(5000);
			driver.findElement(By.xpath("//input[@placeholder='Company or Ticker Symbol']")).sendKeys(CompanyName,Keys.ENTER);
			WebElement companyLocater=driver.findElement(By.xpath("//a[contains(@title,'"+CompanyName+"')]"));
			String companyNameInPage=companyLocater.getAttribute("title");
	
				if(companyNameInPage.contains(CompanyName)) {
					//Click on Company Name
					companyLocater.click();
	}
			}
		}
	}


			