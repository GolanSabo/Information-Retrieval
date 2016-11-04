package il.ac.shenkar.pdf.utils;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessBuffer;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFHandler{
	/*public static void  main(String[] args)
	{
		PDFHandler.getText("C:/Users/Tal/Documents/Unit_03_Color.pdf");
	}*/
    public static String getText(String path) {
        PDFTextStripper pdfStripper = null;
        PDDocument pdDoc = null;
        COSDocument cosDoc = null;
        File file = new File(path);
        String parsedText = null;
        try {
        	FileInputStream fis = new FileInputStream(file);
        	PDFParser parser = new PDFParser(new RandomAccessBuffer(fis));
        	parser.parse();
        	cosDoc = parser.getDocument();
        	
        	pdfStripper = new PDFTextStripper();
        	pdDoc = new PDDocument(cosDoc);
        	pdfStripper.setStartPage(1);
        	pdfStripper.setEndPage(1);
        	parsedText = pdfStripper.getText(pdDoc);
        	pdDoc.close();
        	cosDoc.close();
        	fis.close();
        } catch (IOException e) {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
        } 
        return parsedText;
    }
}