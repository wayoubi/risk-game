package ca.concordia.app.risk.model.dao;

/**
 * 
 * @author i857625
 *
 * @param <T>
 */
public interface Dao<T> {
	/**
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	T findByName(String name) throws Exception;
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	T findById(int id) throws Exception;
    
	/**
	 * 
	 * @param t
	 * @throws Exception
	 */
	void assignID(T t) throws Exception;
    
	/**
	 * 
	 * @param t
	 */
	void delete(T t);
}