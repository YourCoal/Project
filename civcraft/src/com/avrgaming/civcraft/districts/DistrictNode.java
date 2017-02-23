package com.avrgaming.civcraft.districts;

import com.avrgaming.civcraft.exception.CivException;

public class DistrictNode {

	/* Named type of district. */
	private String type;
	private Integer id;
	
	//XXX Structure Reference From structures.yml
//	#row 1: townhall, capitol, shipyard, trade outpost, fishing boat, store, stable
//	#row 2: bank, market, trommel, blacksmith, mine, lab, monument, library
//	#row 3: farm, windmill, granary, cottage, pasture, temple, grocer
//	#row 4: wall, barracks, scout tower, arrow tower, cannon tower
	
	/*
	 Different Districts:
	  - Default (Town Hall, Capitol, Any Structure [but no benefits])
	  - Campus (Library, School, University)
	  - Encampment (Barracks, Blacksmith, Stable, Scout Tower, Arrow Tower, Cannon Tower, Walls)
	  - Harbor (Shipyard, Fishing Boat, Lighthouse)
	  - Commercial Hub (Bank, Market, Trade Outpost, Stock Exchange)
	  - Industrial (Trommel, Quarry, Factory, Grocer, Store)
	  - Agricultural (Farm, Granary, Windmill, Lumber Mill, Pasture)
	  - Holy Site (Temple)
	  - Production (Mine, Lab, Monument)
	  - NeighborHood (Cottage)
	  All structures built in their districts need buffs, but IDK what yet.
	 */
	
	public DistrictNode(String type) {
		this.setType(type);
	}
	
	public void loadFromString(String src) throws CivException {
		setType(src);
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public Integer getID() {
		return id;
	}
	
	public void setID(int id) {
		this.id = id;
	}
}
