/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.object;

import org.bukkit.block.Sign;
import com.civcraft.structure.Buildable;
import com.civcraft.util.BlockCoord;

public class StructureSign {

	private String text;
	private Buildable owner;
	private String type;
	private String action;
	private BlockCoord coord;
	private int direction;
	private boolean allowRightClick = false;
	
	public StructureSign(BlockCoord coord, Buildable owner) {
		this.coord = coord;
		this.owner = owner;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Buildable getOwner() {
		return owner;
	}

	public void setOwner(Buildable owner) {
		this.owner = owner;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public BlockCoord getCoord() {
		return coord;
	}

	public void setCoord(BlockCoord coord) {
		this.coord = coord;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public void setText(String[] message) {
		this.text = "";
		for (String str : message) {
			text += str+"\n";
		}
	}
	
	public void update() {
		if (coord.getBlock().getState() instanceof Sign) {
			Sign sign = (Sign)coord.getBlock().getState();
			String[] lines = this.text.split("\\n");
			
			for (int i = 0; i < 4; i++) {
				if (i < lines.length) {
					sign.setLine(i, lines[i]);
				} else {
					sign.setLine(i, "");
				}
			}
			sign.update();
		}
	}

	public boolean isAllowRightClick() {
		return allowRightClick;
	}

	public void setAllowRightClick(boolean allowRightClick) {
		this.allowRightClick = allowRightClick;
	}

}
