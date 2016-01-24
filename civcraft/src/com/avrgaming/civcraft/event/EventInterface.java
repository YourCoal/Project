package com.avrgaming.civcraft.event;

import java.util.Calendar;

import com.avrgaming.civcraft.exception.InvalidConfiguration;

public interface EventInterface {
	void process();
	Calendar getNextDate() throws InvalidConfiguration;
}
