package com.avrgaming.civcraft.questions;

import com.avrgaming.civcraft.object.Resident;

public interface QuestionResponseInterface {	
	void processResponse(String param);
	void processResponse(String response, Resident responder);
}
