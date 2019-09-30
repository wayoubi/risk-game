package ca.concordia.app.risk.model.dao;

import javax.naming.OperationNotSupportedException;
import javax.validation.constraints.NotNull;

import ca.concordia.app.risk.model.xmlbeans.BorderModel;
import ca.concordia.app.risk.model.xmlbeans.CountryModel;
import ca.concordia.app.risk.model.xmlbeans.GameModel;

/**
 * 
 * @author i857625
 *
 */
public class BorderDaoImp implements Dao<BorderModel>{

	/**
	 * 
	 */
	@Override
	public BorderModel findByName(@NotNull GameModel gameModel, String countryName) throws Exception {
		CountryDaoImpl countryDaoImpl = new CountryDaoImpl();
		CountryModel countryModel = countryDaoImpl.findByName(gameModel, countryName);
		return this.findById(gameModel, new Integer(countryModel.getId()));
	}

	/**
	 * 
	 */
	@Override
	public BorderModel findById(@NotNull GameModel gameModel, int countryId){
		return gameModel.getBorders().getList().stream().filter(b -> b.getCountryId()==countryId).findAny().orElse(null);
	}
	
	/**
	 * 
	 */
	@Override
	public void assignID(@NotNull GameModel gameModel, BorderModel t) throws Exception {
		throw new OperationNotSupportedException("Border Object does need to be assigned an Id.");
	}

	/**
	 * 
	 */
	@Override
	public void delete(@NotNull GameModel gameModel, BorderModel t) {
		gameModel.getBorders().getList().forEach(b -> b.getNeighbours().remove(new Integer(t.getCountryId())));
		gameModel.getBorders().getList().remove(t);
	}

}
