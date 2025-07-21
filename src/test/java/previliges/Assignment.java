package previliges;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Assignment {

	public static void main(String[] args) throws EncryptedDocumentException, IOException, InterruptedException {

		File f = new File("C:\\Users\\RanadheerDurgi\\Downloads\\Projcet\\Cicso\\CompanyData.xls");
		FileInputStream fis = new FileInputStream(f);

		Workbook wb = WorkbookFactory.create(fis);
		Sheet sheet1 = wb.getSheetAt(0);
		
		ChromeOptions options = new ChromeOptions();

		// Disable the built-in PDF viewer and download at a specific path
		HashMap<String, Object> prefs = new HashMap<>();
		prefs.put("plugins.always_open_pdf_externally", true);
		prefs.put("download.default_directory", "C:\\Users\\RanadheerDurgi\\Downloads");
		options.setExperimentalOption("prefs", prefs);
		
		//Iterate over the company names upto 20
		for (int i = 1; i <= 20; i++) {
			Row r0 = sheet1.getRow(i);
			Cell c0 = r0.getCell(0);

		// Create an instance of ChromeDriver with the options
		WebDriver driver = new ChromeDriver(options);
		driver.manage().window().maximize();
		driver.get("https://www.annualreports.com");
		driver.findElement(By.xpath("//span[text()='Search for a Report']/following-sibling::input[@name='search']"))
				.sendKeys(c0.toString());
		Thread.sleep(3000);
		driver.findElement(By.xpath("//span[text()='Search for a Report']/following-sibling::input[@type='submit']")).click();
		driver.findElement(By.xpath("//span[@class='companyName']/a")).click();
	
		WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10))
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@aria-label='View PDF - open in a new tab']")));
		Thread.sleep(3000);
		driver.findElement(By.xpath("//a[@aria-label='View PDF - open in a new tab']")).click();
		driver.close();
		}
		fis.close();

	}

}
