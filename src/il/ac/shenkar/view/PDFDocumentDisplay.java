package il.ac.shenkar.view;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import il.ac.shenkar.Details.FileDetails;
import il.ac.shenkar.pdf.utils.PDFReader;

public class PDFDocumentDisplay extends TextDocumentDisplay
{

	public PDFDocumentDisplay(FileDetails fd, ArrayList<Integer> _locations) {
		super(fd, _locations);
		
	}

	@Override
	public String getText() 
	{
		String text = PDFReader.getText(getPath());
		return text;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		// TODO Auto-generated method stub
		super.actionPerformed(event);
	}

	
	
	
}
