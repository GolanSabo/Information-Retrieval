package il.ac.shenkar.errors;

/**
 * A class that represents that empty search results
 */
public class NoResultsException extends Exception
{
	public NoResultsException(String msg)
	{
		super(msg);
	}
}
