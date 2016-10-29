package il.ac.shenkar.Details;

import il.ac.shenkar.Utils.FileUtils;
import il.ac.shenkar.errors.DateFormatException;

import java.io.File;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * File Details contains necessary information about the new file in process
 *
 */
public class FileDetails implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int index;
	private String path = null;
	private String documentName = null;
	private String author = null;
	private String subject = null;
	private String description  = null;
	private Date date = null;
	private boolean active;
	private SimpleDateFormat format = null;
	private String extension;


	public FileDetails(int _index, String _path, String docName, String _author, String _subject
			, String _description, String _date, String _extension,boolean _active) throws ParseException
	{
		index = _index;
		active = _active;
		extension = _extension;
		path = _path;
		documentName = docName;
		author = _author;
		subject = _subject;
		description = _description;
		String pattern = "MM/dd/yyyy";
	    format = new SimpleDateFormat(pattern);
	    date = new Date();
	    	 date = format.parse(_date);
	    	 
	    	
	}
	
	

	public String getExtension() {
		return extension;
	}



	public void setExtension(String extension) {
		this.extension = extension;
	}



	/**
	 * is this file active
	 * @return true - if the document is active for searches
	 * 		   false - if the document is not active for searches
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * set if the file is active for searches 
	 * @param active - boolean, true if active, otherwise false
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * returns the index of the file
	 * @return int index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * returns the file name
	 * @return string - fileName
	 */
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getDocumentName() {
		return documentName;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}


	
	@Override
	public String toString() {
		return getIndex() + ";" + getDocumentName() + ";" + getPath() + ";" + getAuthor() + ";" + getSubject()
		+";" + getDescription() + ";" + format.format(getDate()) + ";" + getExtension()+ ";" + isActive() + ";";
	}
}
