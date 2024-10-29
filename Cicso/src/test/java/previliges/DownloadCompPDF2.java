package previliges;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DownloadCompPDF2 {

	public static void main(String[] args) throws InterruptedException {
		String fileName = "C:\\Users\\RanadheerDurgi\\Downloads\\Projcet\\Cicso\\CompanyData.xls";
		String url = "https://www.annualreports.com/Company/";
				
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get(url);

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
		
		NALExcelXLSReader reader = new NALExcelXLSReader(fileName);
		
		String sheetName = "Sheet1";
		int row = 2;
		
		int rowCount = reader.getRowCount(sheetName);
		String cellData = reader.getCellData(sheetName,"Name", row);	
		int rownbr = reader.getCellRowNum(sheetName, "Name", cellData);
		
		for(int i=rownbr; i<rowCount ;i++) {
			
			String rowData = reader.getCellData(sheetName,"Name",i);
			if(rowData != "") {	
				//Thread.sleep(10000);
				System.out.println(rowData);
				driver.findElement(By.cssSelector("input[placeholder='Company Name or Ticker Symbol']")).sendKeys(rowData);
				driver.findElement(By.cssSelector("input[placeholder='Company Name or Ticker Symbol']")).sendKeys(Keys.ENTER);
				
				String textStr = driver.findElement(By.className("header_block")).getText();

				if(textStr.contains("Select a company name to view their online annual reports")) {
					WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
			    	wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='companyName']/a")));
					WebElement compClick = driver.findElement(By.xpath("//span[@class='companyName']/a"));
					compClick.click();
					
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".bold_txt")));
					String reportStr = driver.findElement(By.cssSelector(".bold_txt")).getText();
					
			    	if(reportStr.contains("2023 Annual Report and Form 10K")) {
			    		
			        	wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[aria-label*='View PDF'")));
			        	driver.findElement(By.cssSelector("a[aria-label*='View PDF']")).click();
			        	
			        	Set<String> windows = driver.getWindowHandles();
			        	Iterator<String> it =windows.iterator();
			        	String parentId = it.next();
			        	String childId = it.next();
			        	driver.switchTo().window(childId);
			        	String urlPDF = driver.getCurrentUrl();
			        	driver.switchTo().window(parentId);
			        	driver.navigate().to(url);
			        	 	
			        	ChromeOptions chromeOptions = new ChromeOptions(); 
			        	HashMap<String,Object> chromeOptionsMap = new HashMap<String,Object>();

			        	chromeOptionsMap.put("plugins.always_open_pdf_externally", true);
			        	chromeOptionsMap.put("download.default_directory", "C:\\\\Users\\\\RanadheerDurgi\\\\Downloads");
			        	chromeOptions.setExperimentalOption("prefs", chromeOptionsMap);
			        	
			        	WebDriver pdfdriver = new ChromeDriver(chromeOptions);
			        	pdfdriver.get(urlPDF);
			        	Thread.sleep(3000);
			        	pdfdriver.close();
			        	File f=new File("C:\\Users\\RanadheerDurgi\\Downloads\\PDF");
			        	if(f.exists()) {
			        		System.out.println("file found" + i);
			        	}
					}else {
			    		System.out.println("Annual PDF Report for 2023 could not found");
			    		driver.navigate().to(url);
			    		  }
					}else {	
						String compText = driver.findElement(By.cssSelector("div[class='apparel_stores_company_list'] ul li strong")).getText();
						if(compText.contains("No results could be found for your search criteria.")) {
						System.out.println("Annual Report for 2023 could not found");
						driver.navigate().to(url);
			    	}	
				}
				
			}
			else {
	    		break;
	    	}
	
		}

	}

}
