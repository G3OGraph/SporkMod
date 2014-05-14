package io.sporkpgm.rotation;

import io.sporkpgm.map.SporkLoader;
import io.sporkpgm.match.MatchPhase;
import io.sporkpgm.util.Log;

import java.util.List;

public class RotationSet {

	private int current = 0;
	private List<RotationSlot> slots;

	public RotationSet(List<RotationSlot> slots) {
		this.slots = slots;
	}

	public List<RotationSlot> getSlots() {
		return slots;
	}

	public RotationSlot getCurrent() {
		return slots.get(current);
	}

	public boolean isEnded() {
		try {
			slots.get(current + 1);
			return false;
		} catch(IndexOutOfBoundsException e) {
			return true;
		}
	}

	public boolean cycle() {
		if(isEnded()) {
			return false;
		}

		current++;
		return true;
	}

	public RotationSlot getNext() {
		if(isEnded()) {
			return null;
		}

		return slots.get(current + 1);
	}

	public RotationSlot setNext(SporkLoader loader, boolean force) {
		Log.info(Rotation.get().isRestarting() + " && " + !force + " == " + (Rotation.get().isRestarting() && !force));
		if(Rotation.get().isRestarting() && !force) {
			Log.debug("Can't setNext() because the server is restarting and you didn't force!!!");
			return null;
		}

		RotationSlot slot = slots.get(current + 1);
		if(slot.getSet()) {
			slot.unload();
			slots.remove(slot);
		}

		slot = new RotationSlot(loader, true);

		int current = this.current + 1;
		List<RotationSlot> list = slots.subList(0, current);
		Log.debug("list[" + 0 + ", " + current + "] = {" + list.size() + ": " + list + "}");

		list.add(slot);
		try {
			if(getNext() != null) {
				list.addAll(slots.subList(current + 1, slots.size() - 1));
			}
		} catch(Exception e) { /* nothing */ }
		Log.debug("list = {" + list.size() + ": " + list + "}");

		if(Rotation.get().getRotation().getCurrent().getMatch().getPhase() == MatchPhase.CYCLING) {
			slot.load(true);
		}

		return slot;
	}

}
