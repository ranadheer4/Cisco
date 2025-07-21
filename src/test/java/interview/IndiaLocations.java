package interview;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class IndiaLocations {

    public static WebDriver driver;

    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        try {
            getData();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    private static void getData() throws InterruptedException, IOException {
        String excelPath = "C:\\Users\\RanadheerDurgi\\Downloads\\Projcet\\Cicso\\IndiaLocation.xls";
        FileInputStream fis = new FileInputStream(excelPath);
        HSSFWorkbook workbook = new HSSFWorkbook(fis);
        HSSFSheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();
        if (!rows.hasNext()) {
            fis.close();
            workbook.close();
            return;
        }

        // Skip the header row
        rows.next();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        while (rows.hasNext()) {
            Row currentRow = rows.next();
            Cell companyNameCell = currentRow.getCell(0);
            String companyName = companyNameCell.getStringCellValue();

            driver.get("https://www.glassdoor.co.in/Reviews/");
            WebElement companyNameInput = driver.findElement(By.xpath("//button//span[text()='Search']"));
            companyNameInput.click();

            WebElement companyText = driver.findElement(By.xpath("//div//button//descendant::span[text()='Search']/../../../div//input[@aria-label='Search']"));
            companyText.sendKeys(companyName);
            companyText.sendKeys(Keys.ENTER);

            // Wait for the search results to load
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a//h3")));

            List<WebElement> companyLinks = driver.findElements(By.xpath("//a//h3"));
            boolean companyFound = false;

            for (WebElement companyLink : companyLinks) {
                String listedCompanyName = companyLink.getText().trim();

                // Compare the listed company name with the name from the Excel file
                if (listedCompanyName.equalsIgnoreCase(companyName)) {
                    companyLink.click();
                    companyFound = true;
                    break;
                }
            }

            if (!companyFound) {
                System.out.println("Company not found: " + companyName);
                continue;
            }

            // Wait for the jobs tab to be clickable and then click
            try {
                // Wait for the element to be clickable and click it
                WebElement jobsTab = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='jobs']")));
                jobsTab.click();
            } catch (ElementClickInterceptedException e) {
                // If element is still not clickable, use JavaScript to click
                WebElement jobsTab = driver.findElement(By.xpath("//div[@id='jobs']"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", jobsTab);
            }
            // Wait for the job profiles to load
         // Wait for the job profiles to load
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//ul//li[contains(@class, 'JobsList_jobListItem__wjTHv')]")));

            List<WebElement> jobCards = driver.findElements(By.xpath("//ul//li[contains(@class, 'JobsList_jobListItem__wjTHv')]"));

            System.out.println("Job Profiles for " + companyName + ":");
            for (WebElement jobCard : jobCards) {
                // Extract job title
                String jobTitle = jobCard.findElement(By.xpath(".//a[contains(@class, 'JobCard_jobTitle__')]")).getText().trim();
                
                // Extract location
                String location = jobCard.findElement(By.xpath(".//div[contains(@class, 'JobCard_location')]")).getText().trim();
                
                // Print job title and location
                System.out.println(jobTitle + " : " + location);
            }
            System.out.println("====================================");
        }

        FileOutputStream fos = new FileOutputStream(excelPath);
        workbook.write(fos);
        fis.close();
        fos.close();
        workbook.close();
    }
}
