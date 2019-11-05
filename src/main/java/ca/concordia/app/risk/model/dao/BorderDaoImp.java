package ca.concordia.app.risk.model.dao;

import javax.validation.constraints.NotNull;

import ca.concordia.app.risk.model.xmlbeans.BorderModel;
import ca.concordia.app.risk.model.xmlbeans.CountryModel;
import ca.concordia.app.risk.model.xmlbeans.GameModel;

/**
 * This class is DAO(Data Access Layer) for borders - edges of the graph
 *
 * @author i857625
 *
 */
public class BorderDaoImp implements Dao<BorderModel> {

	/**
	 * This method finds border of a country by countryName
	 * {@inheritDoc}
	 * @param gameModel   game model
	 * @param countryName country name
	 * @return border object
	 */
	@Override
	public BorderModel findByName(@NotNull GameModel gameModel, String countryName) {
		CountryDaoImpl countryDaoImpl = new CountryDaoImpl();
		CountryModel countryModel = countryDaoImpl.findByName(gameModel, countryName);
		return this.findById(gameModel, countryModel.getId());
	}

	/**
	 * This method finds a border by id
	 * {@inheritDoc}
	 * @param gameModel game model
	 * @param countryId country id
	 * @return border object
	 */
	@Override
	public BorderModel findById(@NotNull GameModel gameModel, int countryId) {
		return gameModel.getBorders().getList().stream().filter(b -> b.getCountryId() == countryId).findAny()
				.orElse(null);
	}

	/**
	 * This method assign id
	 * {@inheritDoc}
	 * @param gameModel game model
	 * @param t         border Model
	 * @throws UnsupportedOperationException unsupported operation exception
	 */
	@Override
	public void assignID(@NotNull GameModel gameModel, BorderModel t) {
		throw new UnsupportedOperationException("Border Object does need to be assigned an Id.");
	}

	/**
	 * This method removes a list of borders - edges
	 * {@inheritDoc}
	 * @param gameModel game model
	 * @param t         border model
	 */
	@Override
	public void delete(@NotNull GameModel gameModel, BorderModel t) {
		gameModel.getBorders().getList().forEach(b -> b.getNeighbours().remove(Integer.valueOf(t.getCountryId())));
		gameModel.getBorders().getList().remove(t);
	}

}