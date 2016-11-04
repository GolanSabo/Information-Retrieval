package il.ac.shenkar.errors;

/**
 * A class that represents that a file name already exists
 */
public class DuplicateNameException extends Exception
{
	
	public DuplicateNameException(String msg)
	{
		super(msg);
	}
}
