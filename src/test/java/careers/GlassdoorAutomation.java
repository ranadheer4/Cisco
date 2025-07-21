package careers;

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

public class GlassdoorAutomation {

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
        String excelPath = "C:\\Users\\RanadheerDurgi\\Downloads\\Projcet\\Cicso\\Career.xls";
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
            Thread.sleep(4000);
            driver.get("https://www.glassdoor.co.in/Search/");
            Thread.sleep(4000);
            WebElement companyNameInput = driver.findElement(By.xpath("//span[text()='Search']"));
            companyNameInput.sendKeys(companyName);
            companyNameInput.sendKeys(Keys.ENTER);
            driver.findElement(By.xpath("//div//button[@title='Search']")).click();
            Thread.sleep(4000);
            
            
            FileOutputStream fos = new FileOutputStream(excelPath);
            workbook.write(fos);
            fis.close();
            fos.close();
            workbook.close();
}}}