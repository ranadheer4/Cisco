package careers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

public class Demo {

    public static void main(String[] args) {
        String excelFilePath = "C:\\Users\\RanadheerDurgi\\Downloads\\Projcet\\Cicso\\primr.xls";
        WebDriver driver = null;
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        Workbook workbook = null;

        try {
            // Set the path to the ChromeDriver executable
           // System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");

            // Initialize the WebDriver
            driver = new ChromeDriver();
            driver.get("https://careers.alight.com/us/en/c/operations-tech-services-group-jobs");
            Thread.sleep(5000);

            // Read data from Excel file
            fileInputStream = new FileInputStream(new File(excelFilePath));
            workbook = new HSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0);

            // Loop through each row in the first column (Company Name)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                Cell companyNameCell = row.getCell(0);

                if (companyNameCell != null) {
                    String companyName = companyNameCell.getStringCellValue();

                    // Scrape job profiles from the website
                    List<WebElement> allProfiles = driver.findElements(By.xpath("//li[@class='jobs-list-item']//h4"));
                    int cellIndex = 2;
                    for (WebElement eleTopics : allProfiles) {
                        String myText = eleTopics.getText();
                        Cell cell = row.createCell(cellIndex++);
                        cell.setCellValue(myText);
                    }
                }
            }

            // Close the input stream before writing to the file
            fileInputStream.close();

            // Write the updated data back to the Excel file
            fileOutputStream = new FileOutputStream("C:\\Users\\RanadheerDurgi\\Downloads\\Projcet\\Cicso\\primr.xls");
            workbook.write(fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close all resources
            try {
                if (fileInputStream != null) fileInputStream.close();
                if (fileOutputStream != null) fileOutputStream.close();
                if (workbook != null) workbook.close();
                if (driver != null) driver.quit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
