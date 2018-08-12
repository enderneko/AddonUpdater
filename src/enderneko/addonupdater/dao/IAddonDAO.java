package enderneko.addonupdater.dao;

import java.util.Vector;

import enderneko.addonupdater.domain.Addon;

public interface IAddonDAO {
	/**
	 * add an addon to db.
	 * @param a
	 */
	public void add(Addon a);
	
	/**
	 * get an addon by name.
	 * @param name
	 * @return
	 */
	public Addon get(String name);
	
	/**
	 * 
	 * @return
	 */
	public Vector<Addon> getAll();
	
	/**
	 * whether this addon is managed by AU.
	 * @return
	 */
	public boolean isManaged(String name);
	
	/**
	 * update an addon with the same name.
	 * @param a
	 */
	public void update(Addon a);

	/**
	 * delete an addon from db by name.
	 * @param id
	 */
	public void delete(String name);
}
