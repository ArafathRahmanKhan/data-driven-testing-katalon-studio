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
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testdata.TestDataFactory as TestDataFactory
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows

import groovy.sql.Sql
import internal.GlobalVariable as GlobalVariable
import org.apache.poi.ss.usermodel.Sheet as Sheet
import org.apache.poi.ss.usermodel.Cell as Cell
import org.apache.poi.ss.usermodel.Row as Row
import org.apache.poi.ss.usermodel.Workbook as Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook as XSSFWorkbook
import org.openqa.selenium.Keys as Keys

//initialization
CustomKeywords.'helpers.init.projectInitialization'()

//declaring an array list to save results
ArrayList<String> resultArrayList = new ArrayList<>()

//performing login
boolean loginStatus = CustomKeywords.'helpers.login.performLogin'()
if (loginStatus) {
	resultArrayList.add("Login Successful")
} else {
	resultArrayList.add("Login Failed")
}

//-------------------get the item data using data binding-----------------------
String[] itemsArray = itemsWithQuantity.split(',')

String resText = "These items were added to cart- ";
for (String item : itemsArray) {
    //getting each item and their quantity
    String[] itemsWithQ = item.trim().split(':') //splitting each item
    String itemName = (itemsWithQ[0]).trim() //getting the item
    String quantity = (itemsWithQ[1]).trim() //getting the quantity

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
	
	
	//saving into result
	resText = resText + itemName + ":" + quantity + ", "
}

//added-to-cart products added to result text
resultArrayList.add(resText)

//go to cart
WebUI.click(findTestObject('Object Repository/DemoWebShop/Page_Search/btn_shoppingCart'))

//select country
WebUI.selectOptionByValue(findTestObject('DemoWebShop/Page_Shopping Cart/select_country'), countryCode, false)

//enter zipcode
WebUI.setText(findTestObject('Object Repository/DemoWebShop/Page_Shopping Cart/input_zipCode'), zipCode)

//click estimate shipping cost
WebUI.click(findTestObject('Object Repository/DemoWebShop/Page_Shopping Cart/btn_estimateShipping'))

//agree to the terms&conditions
WebUI.click(findTestObject('Object Repository/DemoWebShop/Page_Shopping Cart/checkbox_termsOfService'))

//click checkout
WebUI.click(findTestObject('Object Repository/DemoWebShop/Page_Shopping Cart/btn_checkout'))

//clicking continue untill order placed successfully
WebUI.waitForElementClickable(findTestObject('DemoWebShop/Page_Checkout/btn_billingAddressContinue'), 30)
WebUI.click(findTestObject('Object Repository/DemoWebShop/Page_Checkout/btn_billingAddressContinue'))

WebUI.waitForElementClickable(findTestObject('DemoWebShop/Page_Checkout/btn_shippingAddressContinue'), 30)
WebUI.click(findTestObject('Object Repository/DemoWebShop/Page_Checkout/btn_shippingAddressContinue'))

if(shippingMethod === '0') {
	WebUI.click(findTestObject("Object Repository/DemoWebShop/Page_Checkout/radioBtn_ShippingOption(Ground)"))
	resultArrayList.add("Ground")
} else if(shippingMethod === '1') {
	WebUI.click(findTestObject("Object Repository/DemoWebShop/Page_Checkout/radioBtn_ShippingOption(NextDayAir)"))
	resultArrayList.add("Next Day Air")
} else if(shippingMethod === '2') {
	WebUI.click(findTestObject("Object Repository/DemoWebShop/Page_Checkout/radioBtn_ShippingOption(2ndDayAir)"))
	resultArrayList.add("2nd Day Air")
}

WebUI.waitForElementClickable(findTestObject('DemoWebShop/Page_Checkout/btn_shippingMethodContinue'), 30)
WebUI.click(findTestObject('Object Repository/DemoWebShop/Page_Checkout/btn_shippingMethodContinue'))

WebUI.waitForElementClickable(findTestObject('DemoWebShop/Page_Checkout/btn_paymentMethodContinue'), 30)
WebUI.click(findTestObject('Object Repository/DemoWebShop/Page_Checkout/btn_paymentMethodContinue'))

WebUI.waitForElementClickable(findTestObject('DemoWebShop/Page_Checkout/btn_paymentInfoContinue'), 30)
WebUI.click(findTestObject('Object Repository/DemoWebShop/Page_Checkout/btn_paymentInfoContinue'))

WebUI.waitForElementClickable(findTestObject('DemoWebShop/Page_Checkout/btn_confirmOrder'), 30)
WebUI.click(findTestObject('Object Repository/DemoWebShop/Page_Checkout/btn_confirmOrder'))

//assert that order has been confirmed
boolean orderConfirmationStatus = WebUI.verifyElementText(findTestObject('Object Repository/DemoWebShop/Page_Checkout/div_Your order has been successfully processed'), 
    'Your order has been successfully processed!')

if(orderConfirmationStatus) {
	resultArrayList.add("Confirmed")
} else {
	resultArrayList.add("Not Confirmed")
}
println(resultArrayList)
//getting the order number
String orderNumberText = WebUI.getText(findTestObject('Object Repository/DemoWebShop/Page_Checkout/text_orderNumber'))

String orderNumber = orderNumberText.split(': ')[1]

//clicking order details
WebUI.click(findTestObject('Object Repository/DemoWebShop/Page_Checkout/a_orderDetails'))

//taking screenshots of the order details page
String ssPath = 'C:\\Users\\Riseup\\Katalon Studio\\Test_DemoWebShop\\ScreenShots\\'+orderNumber+'.png'
WebUI.takeFullPageScreenshot(ssPath)

//click to download pdf
WebUI.click(findTestObject('Object Repository/DemoWebShop/Page_Checkout/a_PDFInvoiceDownload'))
WebUI.delay(3)

//Verifying pdf download and delete
String expectedFileName = "order_" + orderNumber + ".pdf"
File downloadFolder = new File("C:\\Users\\Riseup\\Downloads\\")
boolean pdfFound = false
File foundFile = null

File[] files = downloadFolder.listFiles()
if(files != null) {
	for(File file : files) {
		if(file.isFile() && file.getName().equals(expectedFileName)) {
			pdfFound = true
			foundFile = file
		}
	}
}

if(pdfFound) {
	resultArrayList.add("downloaded " + expectedFileName)
	foundFile.delete()
} else {
	resultArrayList.add("Could'nt download " + expectedFileName)
}


//saving the output in another excel file
CustomKeywords.'helpers.saveDataToExcel.saveOrderNumber'(orderNumber, resultArrayList)

//-----------saving output into DataBase---------------------
// Connection settings
//def dbUrl = 'jdbc:mysql://sql12.freesqldatabase.com/sql12829951'
//def username = 'sql12829951'
//def password = '4gQ3lGtAK9'
//def driver = 'com.mysql.cj.jdbc.Driver'

// Open database connection
def sql = Sql.newInstance(dbURL, dbUsername, dbPassword, dbDriver)

if(sql) {
	println("DB Connection established.")
} else {
	println("DB Connection failed!")
}

// Run the insert command
def query = "INSERT INTO `OrderDetails` (`Order Number`, `Login Status`, `Products Addded-to-cart`, `Shipping Method`, `Order Status`, `PDF Download Status`) VALUES (?, ?, ?, ?, ?, ?);"
sql.executeInsert(query, [orderNumber, resultArrayList[0], resultArrayList[1], resultArrayList[2], resultArrayList[3], resultArrayList[4]])

//Close the connection
sql.close()
println("Data written to database successfully!")

//closing the browser
WebUI.delay(1)
WebUI.closeBrowser()

