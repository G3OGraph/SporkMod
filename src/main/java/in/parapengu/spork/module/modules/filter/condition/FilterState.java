package in.parapengu.spork.module.modules.filter.condition;

public enum FilterState {

	ALLOW(true),
	ABSTAIN(true),
	DENY(false);

	private boolean allowed;

	FilterState(boolean allowed) {
		this.allowed = allowed;
	}

	public boolean isAllowed() {
		return allowed;
	}
}
