import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testdata.TestDataFactory
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row as Row
import org.apache.poi.ss.usermodel.Workbook as Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook as XSSFWorkbook
import org.openqa.selenium.Keys as Keys


//opening browser & Maximizing window
WebUI.openBrowser('')
WebUI.maximizeWindow()

//navigating to DemoWebShop website
WebUI.navigateToUrl('https://demowebshop.tricentis.com/')

//click on Login to enter Login credentials page
WebUI.click(findTestObject('Object Repository/DemoWebShop/Page_Demo Web Shop/btn_takeToLoginPage'))

//Enter Login credentials - username
WebUI.setText(findTestObject('Object Repository/DemoWebShop/Page_Login/input_Email'), GlobalVariable.G_username)

//Enter Login credentials - password
WebUI.setEncryptedText(findTestObject('DemoWebShop/Page_Login/input_Password'), GlobalVariable.G_password)

//click on Login
WebUI.click(findTestObject('Object Repository/DemoWebShop/Page_Login/btn_login'))

//assert text 'Log out' to ensure login is successfull
WebUI.assertElementText(findTestObject('DemoWebShop/Page_Login/btn_logOut'), 'Log out', 0)

//-------------------get the item data using data binding-----------------------
String[] itemsArray = itemsWithQuantity.split(",")

for(String item : itemsArray) {
	//getting each item and their quantity
	String[] itemsWithQ = item.trim().split(":") //splitting each item
	String itemName = itemsWithQ[0].trim() //getting the item
	String quantity = itemsWithQ[1].trim() //getting the quantity

	//clearing the search bar
	WebUI.clearText(findTestObject('DemoWebShop/Page_Demo Web Shop/input_searchStore'))
	
	//searching for Items
	WebUI.setText(findTestObject('DemoWebShop/Page_Demo Web Shop/input_searchStore'), itemName)
	
	//clicking search button
	WebUI.click(findTestObject('DemoWebShop/Page_Demo Web Shop/btn_searchStore'))
	
	//going to product page
	WebUI.click(findTestObject('Object Repository/DemoWebShop/Page_Demo Web Shop/a_searchedProduct'))
	
	//set quantity
	WebUI.clearText(findTestObject('Object Repository/DemoWebShop/Page_Demo Web Shop/input_quantity'))
	WebUI.sendKeys(findTestObject('Object Repository/DemoWebShop/Page_Demo Web Shop/input_quantity'), quantity)
	
	//add to cart
	WebUI.click(findTestObject('Object Repository/DemoWebShop/Page_Demo Web Shop/btn_addToCart'))
}


//go to cart
WebUI.click(findTestObject('Object Repository/DemoWebShop/Page_Search/btn_shoppingCart'))

//select country
WebUI.selectOptionByValue(findTestObject('DemoWebShop/Page_Shopping Cart/select_country'), '10', false)

//enter zipcode
WebUI.setText(findTestObject('Object Repository/DemoWebShop/Page_Shopping Cart/input_zipCode'), '1229')

//click estimate shipping cost
WebUI.click(findTestObject('Object Repository/DemoWebShop/Page_Shopping Cart/btn_estimateShipping'))

//agree to the terms&conditions
WebUI.click(findTestObject('Object Repository/DemoWebShop/Page_Shopping Cart/checkbox_termsOfService'))

//click checkout
WebUI.click(findTestObject('Object Repository/DemoWebShop/Page_Shopping Cart/btn_checkout'))

//clicking continue untill order placed successfully
WebUI.click(findTestObject('Object Repository/DemoWebShop/Page_Checkout/btn_billingAddressContinue'))
WebUI.click(findTestObject('Object Repository/DemoWebShop/Page_Checkout/btn_shippingAddressContinue'))
WebUI.click(findTestObject('Object Repository/DemoWebShop/Page_Checkout/btn_shippingMethodContinue'))
WebUI.click(findTestObject('Object Repository/DemoWebShop/Page_Checkout/btn_paymentMethodContinue'))
WebUI.click(findTestObject('Object Repository/DemoWebShop/Page_Checkout/btn_paymentInfoContinue'))
WebUI.click(findTestObject('Object Repository/DemoWebShop/Page_Checkout/btn_confirmOrder'))

//assert that order has been confirmed
WebUI.verifyElementText(findTestObject('Object Repository/DemoWebShop/Page_Checkout/div_Your order has been successfully processed'), 
    'Your order has been successfully processed!')

//getting the order number
String orderNumberText = WebUI.getText(findTestObject('Object Repository/DemoWebShop/Page_Checkout/text_orderNumber'))
String orderNumber = orderNumberText.split(":")[1]

//initializing the file system framework to save order id into an excel
String outputExcelPath = 'C:\\Users\\Riseup\\Katalon Studio\\Test_Demo\\Output\\OrderDetails.xlsx'
File outputFile = new File(outputExcelPath)

if(outputFile.exists()) {
	FileInputStream fis = new FileInputStream(outputFile)
	Workbook workbook = new XSSFWorkbook(fis)
	Sheet sheet = workbook.getSheetAt(0)
	int nextRow = sheet.getLastRowNum() + 1
	
	Row newRow = sheet.createRow(nextRow)
	Cell cell = newRow.createCell(0)
	cell.setCellValue(orderNumber)
	
	fis.close()
	
	FileOutputStream fos = new FileOutputStream(outputFile)
	workbook.write(fos)
	
	fos.close()
	workbook.close()
}



//closing the browser
WebUI.delay(2)

WebUI.closeBrowser()

