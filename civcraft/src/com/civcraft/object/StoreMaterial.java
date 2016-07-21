/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.object;

public class StoreMaterial {
	public int type;
	public byte data;
	public String name;
	public double price;
	
	public StoreMaterial(String strtype, String strdata, String strname, String strprice) {		
		type = Integer.valueOf(strtype);
		data = Integer.valueOf(strdata).byteValue();
		name = strname;
		price = Double.valueOf(strprice);
	}
}
