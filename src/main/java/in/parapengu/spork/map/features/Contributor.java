package in.parapengu.spork.map.features;

public class Contributor {

	private String name;
	private String contribution;

	public Contributor(String name, String contribution) {
		this.name = name;
		this.contribution = contribution;
	}

	public String getName() {
		return name;
	}

	public String getContribution() {
		return contribution;
	}

	public boolean hasContribution() {
		return contribution != null;
	}

}
