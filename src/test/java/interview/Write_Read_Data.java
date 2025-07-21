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

import io.github.bonigarcia.wdm.WebDriverManager;

public class Write_Read_Data {

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
        String excelPath = "C:\\Users\\RanadheerDurgi\\git\\Cisco\\Cicso\\AllLocations.xls";
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

        // Create new row for writing company name and addresses
        int rowIndex = sheet.getPhysicalNumberOfRows(); // Get the next available row
        Row headerRow = sheet.createRow(rowIndex);
        Cell headerCell1 = headerRow.createCell(0);
        headerCell1.setCellValue("Company Name");
        Cell headerCell2 = headerRow.createCell(1);
        headerCell2.setCellValue("Addresses");

        while (rows.hasNext()) {
            Row currentRow = rows.next();
            Cell companyNameCell = currentRow.getCell(0);
            String companyName = companyNameCell.getStringCellValue().trim().toLowerCase();

            driver.get("https://craft.co/aar/locations");
            WebElement companyNameInput = driver.findElement(By.xpath("//div//input[@placeholder='Search for companies']"));
            companyNameInput.sendKeys(companyName);
            companyNameInput.sendKeys(Keys.ENTER);
            driver.findElement(By.xpath("//div//button[@title='Search']")).click();
            Thread.sleep(4000);

            List<WebElement> companyNamesOnPage = driver.findElements(By.xpath("//ul//li//div[@class='_2iVCc']"));
            boolean companyFound = false;

            for (WebElement companyNameOnPage : companyNamesOnPage) {
                String pageText = companyNameOnPage.getText().trim().toLowerCase();

                // Check for contains, starts with, ends with, or equals ignoring case
                if (pageText.contains(companyName) || pageText.startsWith(companyName) ||
                    pageText.endsWith(companyName) || pageText.equals(companyName)) {

                    WebElement viewCompanyButton = companyNameOnPage.findElement(By.xpath("../div//following-sibling::a[text()='View company']"));
                    Thread.sleep(4000);
                    viewCompanyButton.click();
                    companyFound = true;
                    Thread.sleep(4000);
                    break;
                }
            }

            if (companyFound) {
                WebElement locationButton = driver.findElement(By.xpath("//ul[@class='CompanyNavigationStyled__Nav-sc-6kvpde-4 gDwHaA']//li//a[text()='Locations']"));
                locationButton.click();
                Thread.sleep(2000);

                List<WebElement> addressElements = driver.findElements(By.xpath("//div[@data-testid='locations-table-container']//table//tbody//tr"));
                StringBuilder addresses = new StringBuilder();
                for (WebElement addressElement : addressElements) {
                    String address = addressElement.getText();
                    addresses.append(address).append("; ");
                    System.out.println(companyName + " : " + address); // Print each address
                }

                // Write company name and addresses to Excel
                Row newRow = sheet.createRow(rowIndex + 1); // Create a new row after the header
                Cell cell1 = newRow.createCell(0);
                cell1.setCellValue(companyName);
                Cell cell2 = newRow.createCell(1);
                cell2.setCellValue(addresses.toString());
                rowIndex++;
            } else {
                System.out.println(companyName + " : " + "Address Not Found Or Company name Incorrect/Ignored");

                // Write company name and message for not found companies
                Row newRow = sheet.createRow(rowIndex + 1);
                Cell cell1 = newRow.createCell(0);
                cell1.setCellValue(companyName);
                Cell cell2 = newRow.createCell(1);
                cell2.setCellValue("Address Not Found Or Company name Incorrect/Ignored");
                rowIndex++;
            }
        }

        // Write changes to the Excel file
        FileOutputStream fos = new FileOutputStream("C:\\Users\\RanadheerDurgi\\git\\Cisco\\Cicso\\Locations.xls");
        workbook.write(fos);
        fos.close();
        fis.close();
        workbook.close();
    }
}
