/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.interactive;

import com.civcraft.object.Resident;

public interface InteractiveResponse {
	void respond(String message, Resident resident);
}
