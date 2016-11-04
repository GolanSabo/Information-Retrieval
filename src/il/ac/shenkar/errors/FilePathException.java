package il.ac.shenkar.errors;

/**
 * A class that represents that the file path does not exist
 */
public class FilePathException extends Exception
{
	public FilePathException(String msg)
	{
		super(msg);
	}
}
