/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.randomevents.components;


import com.civcraft.randomevents.RandomEventComponent;

public class MessageTown extends RandomEventComponent {

	@Override
	public void process() {
		String message = this.getString("message");
		sendMessage(message);
	}	
}
