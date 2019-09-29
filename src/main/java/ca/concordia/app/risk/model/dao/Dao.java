package ca.concordia.app.risk.model.dao;

import ca.concordia.app.risk.model.xmlbeans.GameModel;

import javax.validation.constraints.NotNull;

/**
 * @param <T>
 * @author i857625
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
     * @param t
     */
    void delete(@NotNull GameModel gameModel, T t);
}