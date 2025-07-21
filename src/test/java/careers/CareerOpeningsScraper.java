package careers;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class CareerOpeningsScraper {

    public static void main(String[] args) throws InterruptedException {
        // Set the path to your Excel file
        String excelFilePath = "C:\\Users\\RanadheerDurgi\\Downloads\\Projcet\\Cicso\\CompanyData3.xls";
        
        // Set the path to your ChromeDriver
        //System.setProperty("webdriver.chrome.driver", "path/to/your/chromedriver");

        WebDriver driver = new ChromeDriver();
        
        try {
            // Read the Excel file
            FileInputStream fileInputStream = new FileInputStream(excelFilePath);
            Workbook workbook = new HSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0);

            // Iterate through each row in the Excel file
            for (Row row : sheet) {
                Cell companyNameCell = row.getCell(0);
                Cell careerPageUrlCell = row.getCell(1);

                if (companyNameCell != null && careerPageUrlCell != null) {
                    String companyName = companyNameCell.getStringCellValue();
                    String careerPageUrl = careerPageUrlCell.getStringCellValue();

                    // Navigate to the company's career page
                    driver.get(careerPageUrl);

                    // Scrape job openings (modify the following line according to the structure of the career page)
                    Thread.sleep(5000);
                    List<WebElement>allProfiles=driver.findElements(By.xpath("//li[@class='jobs-list-item']//h4"));
           		 for (WebElement eleTopics : allProfiles)
           		    {
           			 String myText = eleTopics.getText();
           			// System.out.println(myText);
           			 Cell yearCell=row.createCell(2);
           				yearCell.setCellValue(myText);
           		    }
                       }
                  
            fileInputStream.close();

            // Write the updated data back to the Excel file
            FileOutputStream fileOutputStream = new FileOutputStream(excelFilePath);
            workbook.write(fileOutputStream);
            workbook.close();
            fileOutputStream.close();
            }}
            catch (IOException e) {
            e.printStackTrace();
            } finally {
            driver.quit();
        }
   
}}
