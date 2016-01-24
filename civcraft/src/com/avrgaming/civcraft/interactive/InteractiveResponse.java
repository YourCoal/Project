package com.avrgaming.civcraft.interactive;

import com.avrgaming.civcraft.object.Resident;

public interface InteractiveResponse {
	void respond(String message, Resident resident);
}
