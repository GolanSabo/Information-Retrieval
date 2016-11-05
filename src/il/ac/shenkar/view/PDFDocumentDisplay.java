package il.ac.shenkar.view;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import il.ac.shenkar.Details.FileDetails;
import il.ac.shenkar.pdf.utils.PDFHandler;

public class PDFDocumentDisplay extends TextDocumentDisplay
{

	public PDFDocumentDisplay(FileDetails fd, ArrayList<Integer> _locations,ArrayList<String> words) {
		super(fd, _locations,words);
		
	}

	@Override
	public String getText() 
	{
		String text = PDFHandler.getText(getPath());
		return text;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		// TODO Auto-generated method stub
		super.actionPerformed(event);
	}

	
	
	
}
