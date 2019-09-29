package ca.concordia.app.risk.model.dao;

import javax.validation.constraints.NotNull;

import ca.concordia.app.risk.model.xmlbeans.GameModel;

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
	T findByName(@NotNull GameModel gameModel, String name) throws Exception;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	T findById(@NotNull GameModel gameModel, int id) throws Exception;

	/**
	 * 
	 * @param t
	 * @throws Exception
	 */
	void assignID(@NotNull GameModel gameModel, T t) throws Exception;

	/**
	 * 
	 * @param t
	 */
	void delete(@NotNull GameModel gameModel, T t);
}