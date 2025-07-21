package faizaz;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

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

        while (rows.hasNext()) {
            Row currentRow = rows.next();
            Cell companyUrlCell = currentRow.getCell(0);  // Assuming company URL is in column 0 now

            // Check if URL is present
            if (companyUrlCell != null && companyUrlCell.getCellType() == CellType.STRING) {
                String companyUrl = companyUrlCell.getStringCellValue().trim();  // Read the URL

                if (!companyUrl.isEmpty()) {
                    // Launch the URL
                    driver.get(companyUrl);
                    System.out.println("Navigating to: " + companyUrl);
                    Thread.sleep(4000);  // Adjust wait time as needed
                    driver.findElement(By.xpath("//input[@id='username']")).sendKeys("ranadurgi3@gmail.com");
                    driver.findElement(By.xpath("//input[@id='password']")).sendKeys("9963883545Durgi");
                    driver.findElement(By.xpath("//div[@class='card-layout']//button[@aria-label='Sign in']")).click();
                } else {
                    System.out.println("URL not found in row " + currentRow.getRowNum());
                }
            } else {
                System.out.println("Invalid or empty URL in row " + currentRow.getRowNum());
            }
        }

        FileOutputStream fos = new FileOutputStream(excelPath);
        workbook.write(fos);
        fis.close();
        fos.close();
        workbook.close();
    }
}
