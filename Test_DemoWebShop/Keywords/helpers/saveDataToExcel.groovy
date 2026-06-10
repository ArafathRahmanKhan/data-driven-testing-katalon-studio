package helpers

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows

import internal.GlobalVariable

public class saveDataToExcel {
	
	@Keyword
	def saveOrderNumber(String orderNumber, ArrayList resultArrayList) {
		//initializing the file system framework to save order id into an excel
		String outputExcelPath = 'C:\\Users\\Riseup\\Katalon Studio\\Test_Demo\\Result\\OrderDetails.xlsx'
		File outputFile = new File(outputExcelPath)
		
		if(outputFile.exists()) {
			FileInputStream fis = new FileInputStream(outputFile)
			Workbook workbook = new XSSFWorkbook(fis)
			Sheet sheet = workbook.getSheetAt(0)
		
			//getting the last row index and calculating the next row index
			int lastRowIndex = sheet.getLastRowNum()
			int nextRowIndex
			if(sheet.getPhysicalNumberOfRows() == 0) {
				nextRowIndex = 0
			} else {
				nextRowIndex = lastRowIndex + 1
			}
			
			//creating the row and input order number
			Row newRow = sheet.createRow(nextRowIndex)
			Cell cell = newRow.createCell(0)
			cell.setCellValue(orderNumber)
			
			//looping through the ArrayList and writing in excel file
			for(int i=1; i<=resultArrayList.size(); i++) {
				Cell cell1 = newRow.createCell(i)
				cell1.setCellValue(resultArrayList.get(i-1))
			}
			
			fis.close()
			
			FileOutputStream fos = new FileOutputStream(outputFile)
			workbook.write(fos)
			
			fos.close()
			workbook.close()
		}
	}

}
