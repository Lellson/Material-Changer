package de.lellson.materialchanger;

import java.util.ArrayList;
import java.util.List;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class ErrorMessenger {
	
	public final List<String> msgs = new ArrayList<String>();
	
	public ErrorMessenger() {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public void add(String error, String msg) {
		
		if (msg == null)
			msg = "Unexpected error!";
		
		String message = "[" + MaterialChanger.NAME + "] " + error + " - " + msg;
		FMLLog.bigWarning(message);
		msgs.add(message);
	}
	
	@SubscribeEvent
	public void logIn(PlayerLoggedInEvent event) {
		
		if (msgs.isEmpty()) return;
		for (String msg : msgs)
		{
			event.player.sendMessage(new TextComponentString(ChatFormatting.RED + "--------------ERROR--------------"));
			event.player.sendMessage(new TextComponentString(ChatFormatting.RED +  msg));
			event.player.sendMessage(new TextComponentString(ChatFormatting.RED +  "---------------------------------"));
		}
	}
}
