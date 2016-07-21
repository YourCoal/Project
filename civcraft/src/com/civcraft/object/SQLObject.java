/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.object;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.civcraft.exception.CivException;
import com.civcraft.exception.InvalidNameException;
import com.civcraft.exception.InvalidObjectException;

/*
 * Any object that needs to be saved will extend this object so it can be
 * saved in the database.
 */
public abstract class SQLObject extends NamedObject {
	
	private boolean isDeleted = false;

	public abstract void load(ResultSet rs) throws SQLException, InvalidNameException, InvalidObjectException, CivException;
		
	public abstract void save();
	
	public abstract void saveNow() throws SQLException;
	
	public abstract void delete() throws SQLException;

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
}
