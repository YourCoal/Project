/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.event;

import java.util.Calendar;

import com.civcraft.exception.InvalidConfiguration;

public interface EventInterface {
	void process();
	Calendar getNextDate() throws InvalidConfiguration;
}
