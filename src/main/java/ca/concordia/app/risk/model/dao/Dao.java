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
	T findByName(@NotNull GameModel gameModel, String name);

	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	T findById(@NotNull GameModel gameModel, int id);

	/**
	 * 
	 * @param t
	 * @throws Exception
	 */
	void assignID(@NotNull GameModel gameModel, T t);

	/**
	 * 
	 * @param t
	 */
	void delete(@NotNull GameModel gameModel, T t);
}