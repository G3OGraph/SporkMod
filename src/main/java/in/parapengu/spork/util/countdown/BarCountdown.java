package in.parapengu.spork.util.countdown;

import in.parapengu.commons.utils.countdown.Countdown;
import in.parapengu.spork.util.Log;
import me.confuser.barapi.BarAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class BarCountdown extends Countdown {

	private boolean bar = true;

	public BarCountdown(JavaPlugin plugin, String message, String replace, int duration, boolean async) {
		super(plugin, message, replace, duration, async);
	}

	public BarCountdown(JavaPlugin plugin, String message, int duration, boolean async) {
		super(plugin, message, duration, async);
	}

	public BarCountdown(JavaPlugin plugin, String message, String replace, int duration) {
		super(plugin, message, replace, duration);
	}

	public BarCountdown(JavaPlugin plugin, String message, int duration) {
		super(plugin, message, duration);
	}

	@Override
	public void interval() {
		if(!bar) {
			return;
		}

		try {
			BarAPI.getPlugin(BarAPI.class);
		} catch(Exception ex) {
			Log.exception(ex);
			bar = false;
			return;
		}

		float duration = getDuration();
		float remaining = getRemainingTime();

		float percent;
		try {
			percent = duration / remaining;
		} catch(Exception ex) {
			percent = 0F;
		}

		percent *= 100;
		if(percent <= 0) {
			percent = 0.001F;
		} else if(percent > 100) {
			percent = 100F;
		}

		for(Player player : Bukkit.getOnlinePlayers()) {
			BarAPI.setMessage(player, message(), percent);
		}
	}

}
