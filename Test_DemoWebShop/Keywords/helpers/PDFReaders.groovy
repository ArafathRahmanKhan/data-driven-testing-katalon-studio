package helpers

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import org.apache.pdfbox.Loader
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows

import internal.GlobalVariable

public class PDFReaders {

	@Keyword
	public static String getPdfText(String filePath) {
        File file = new File(filePath)
        PDDocument document = null
        String pdfText = ""

        try {
            //loading the PDF document
            document = Loader.loadPDF(file)
            
			//stripping the text
            PDFTextStripper pdfStripper = new PDFTextStripper()
            pdfText = pdfStripper.getText(document)
            
        } catch (Exception e) {
            KeywordUtil.markFailed("Failed to read PDF file: " + e.getMessage())
        } finally {
            if (document != null) {
                document.close()
            }
        }
		
        return pdfText
    }
	
	
	
	// read PDF data using REGEX/text flow approach
	public static String verifyPDFTextUsingRegexApproach(String filePath) {
		File file = new File(filePath)
        PDDocument document = null
        String pdfText = ""

        try {
            document = Loader.loadPDF(file)
            
            PDFTextStripper pdfStripper = new PDFTextStripper()
            //ensuring that text matches the visual left-to-right, top-to-bottom layout
            pdfStripper.setSortByPosition(true) 
            pdfText = pdfStripper.getText(document)
            
        } catch (Exception e) {
            KeywordUtil.markFailed("Failed to read PDF file: " + e.getMessage())
        } finally {
            if (document != null) {
                document.close()
            }
        }
        return pdfText
	}
	
}
