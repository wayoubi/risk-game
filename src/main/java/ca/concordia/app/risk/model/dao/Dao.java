package ca.concordia.app.risk.model.dao;

import javax.validation.constraints.NotNull;

import ca.concordia.app.risk.model.xmlbeans.GameModel;

/**
 * Dao<T> Interface
 * 
 * @author i857625
 *
 * @param <T> T
 */
public interface Dao<T> {
	/**
	 * 
	 * @param gameModel game model
	 * @param name      name
	 * @return T
	 */
	T findByName(@NotNull GameModel gameModel, String name);

	/**
	 * 
	 * @param gameModel game model
	 * @param id        id
	 * @return T
	 */
	T findById(@NotNull GameModel gameModel, int id);

	/**
	 * 
	 * @param gameModel game model
	 * @param t         t
	 */
	void assignID(@NotNull GameModel gameModel, T t);

	/**
	 * 
	 * @param gameModel game model
	 * @param t         t
	 */
	void delete(@NotNull GameModel gameModel, T t);
}