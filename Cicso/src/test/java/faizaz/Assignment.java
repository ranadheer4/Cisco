package faizaz;

import java.io.File;
import java.io.FileFilter;
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
		
		WebDriver driver = null;
		// Reading data from this file
		File f = new File("C:\\Users\\RanadheerDurgi\\Downloads\\Projcet\\Cicso\\CompanyData.xls");
		FileInputStream fis = new FileInputStream(f);

		Workbook wb = WorkbookFactory.create(fis);
		Sheet sheet1 = wb.getSheetAt(0);

		ChromeOptions options = new ChromeOptions();

		// Disable the built-in PDF viewer and download at a specific path
		HashMap<String, Object> prefs = new HashMap<>();
		prefs.put("plugins.always_open_pdf_externally", true);
		prefs.put("download.default_directory", "C:\\Rana durgi\\MVP");
		options.setExperimentalOption("prefs", prefs);
		
		int rowCount = sheet1.getLastRowNum(); //Getting the last row of excel sheet
		
		// Iterating over the company names
		for (int i = 0; i <= rowCount; i++) {
			try {
				Row r0 = sheet1.getRow(i);
				Cell c0 = r0.getCell(0); //getting cell value (company name)
				// Create an instance of ChromeDriver with the options
				driver = new ChromeDriver(options);
				driver.manage().window().maximize();
				//loading website
				driver.get("https://www.annualreports.com");
				driver.findElement(
						By.xpath("//span[text()='Search for a Report']/following-sibling::input[@name='search']"))
						.sendKeys(c0.toString());
				driver.findElement(
						By.xpath("//span[text()='Search for a Report']/following-sibling::input[@type='submit']")).click();
				driver.findElement(By.xpath("//span[@class='companyName']/a")).click();
				Thread.sleep(9000);
				WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions
						.elementToBeClickable(By.xpath("//a[@aria-label='View PDF - open in a new tab']")));
				driver.findElement(By.xpath("//a[@aria-label='View PDF - open in a new tab']")).click();
				// Wait for the file to be downloaded and return file path that got downloaded
				String downloadedFilePath = waitForFileDownload();
				if(downloadedFilePath != null) {
				// Renaming the file
				File downloadedFile = new File(downloadedFilePath);
				File renamedFile = new File("C:\\Rana durgi\\MVP\\"+ c0.toString() + " "+ "Annual_Report"+ ".pdf");
				
				boolean flag=downloadedFile.renameTo(renamedFile);
				System.out.println(flag);
				// Closing all tabs of browser instance
				driver.quit();
				}else {
					System.out.println("PDF File couldn't download within 10 seconds for: " + c0);
					driver.quit();
				}
			}catch(NoSuchElementException e) {
				System.out.println("Element not found: " + "//a[@aria-label='View PDF - open in a new tab']" + ", continuing with next iteration");
				if (driver != null) {
		            driver.quit(); // Close the browser instance even if exception occurs
		        }
				continue;
			}
		}
		fis.close();

	}

	public static String waitForFileDownload() {

		File f = new File("C:\\Rana durgi\\MVP");
		// Create a FileFilter to filter pdf files
		FileFilter filter = new FileFilter() {
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith(".pdf");
			}
		};

		int initialFileCount = f.listFiles(filter).length;
		int waitTime = 0;
		File downloadedFile = null;

		// Wait for a new file to appear in the directory
		while (waitTime < 60) {
			File[] files = f.listFiles(filter);
			int currentFileCount = files.length;
			if (currentFileCount > initialFileCount) {
				// Find the newest file
				long lastModifiedTime = Long.MIN_VALUE;
				for (File file : files) {
					if (file.lastModified() > lastModifiedTime) {
						lastModifiedTime = file.lastModified();
						downloadedFile = file;
					}
				}
				break;
			}
			try {
				Thread.sleep(1000); // sleep for 1 second
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			waitTime++;
		}

		if (downloadedFile != null) {
			System.out.println("Downloaded file: " + downloadedFile);
		} else {
			System.out.println("No new file got downloaded.");
		}

		return downloadedFile.getAbsolutePath();
	}
}
