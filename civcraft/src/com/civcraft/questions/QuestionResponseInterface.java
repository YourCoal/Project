/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.questions;

import com.civcraft.object.Resident;

public interface QuestionResponseInterface {	
	void processResponse(String param);
	void processResponse(String response, Resident responder);
}
