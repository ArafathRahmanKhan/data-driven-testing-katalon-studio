package helpers

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

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

public class login {
	@Keyword
	public static boolean performLogin() {
		//click on Login to enter Login credentials page
		WebUI.click(findTestObject('Object Repository/DemoWebShop/Page_Demo Web Shop/btn_takeToLoginPage'))
		
		//Enter Login credentials - user name
		WebUI.setText(findTestObject('Object Repository/DemoWebShop/Page_Login/input_Email'), GlobalVariable.G_username)
		
		//Enter Login credentials - password
		WebUI.setEncryptedText(findTestObject('DemoWebShop/Page_Login/input_Password'), GlobalVariable.G_password)
		
		//click on Login
		WebUI.click(findTestObject('Object Repository/DemoWebShop/Page_Login/btn_login'))
		
		//assert text 'Log out' to ensure login is successful
		return WebUI.verifyElementText(findTestObject('DemoWebShop/Page_Login/btn_logOut'), 'Log out')
	}

}
