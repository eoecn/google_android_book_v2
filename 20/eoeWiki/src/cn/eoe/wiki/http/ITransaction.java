package cn.eoe.wiki.http;


/**
 * a callback class. be used to manager a http.
 * @author <a href="mailto:kris@matchmovegames.com">Kris.lee</a>
 * @since 1.0.0 12:39:53 pm
 * @version 1.0.0
 */
public interface ITransaction{
	/**
	 * 
	 * @param result  json result
	 */
	public void transactionOver(String result);
	/**
	 * 
	 * @param erroCode error code,like 300,400,404,5000
	 * @param result response entity
	 * @param e	exception instance
	 */
	public void transactionException(int erroCode,String result,Exception e);
}
