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
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class AddressWithCompanies {

    public static WebDriver driver;
    public static WebDriverWait wait;

    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        //driver.manage().timeouts().implicitlyWait(10, java.util.concurrent.TimeUnit.SECONDS); // Implicit wait
       // wait = new WebDriverWait(driver, 20); // Explicit wait
        
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

        while (rows.hasNext()) {
            Row currentRow = rows.next();
            Cell companyNameCell = currentRow.getCell(0);
            String companyName = companyNameCell.getStringCellValue();

            driver.get("https://craft.co/aar/locations");

            WebElement companyNameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div//input[@placeholder='Search for companies']")));
            companyNameInput.sendKeys(companyName);
            companyNameInput.sendKeys(Keys.ENTER);

            WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div//button[@title='Search']")));
            searchButton.click();

            List<WebElement> companyNamesOnPage = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//ul//li//a//h3")));
            boolean companyFound = false;

            for (WebElement companyNameOnPage : companyNamesOnPage) {
                if (companyNameOnPage.getText().equalsIgnoreCase(companyName)) {
                    WebElement viewCompanyButton = companyNameOnPage.findElement(By.xpath("./../../div//following-sibling::a[text()='View company']"));
                    wait.until(ExpectedConditions.elementToBeClickable(viewCompanyButton)).click();
                    companyFound = true;
                    break;
                }
            }
            Thread.sleep(4000);
            if (companyFound) {
                WebElement locationButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ul[@class='CompanyNavigationStyled__Nav-sc-6kvpde-4 gDwHaA']//li//a[text()='Locations']")));
                locationButton.click();

                List<WebElement> addressElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@data-testid='locations-table-container']//table//tbody//tr")));
                for (WebElement addressElement : addressElements) {
                    String address = addressElement.getText();
                    System.out.println(companyName + " : " + address); // Print each address
                }

            } else {
                System.out.println(companyName + " : " + "Address Not Found Or Company name Incorrect");
            }
        }

        FileOutputStream fos = new FileOutputStream(excelPath);
        workbook.write(fos);
        fis.close();
        fos.close();
        workbook.close();
    }
}
