
public class WizardsLab extends Room {

	/** max number of turns the player can take before failing */
	private int maxTurns;

	/** the number of turns the player has taken */
	private int numTurns;

	/**
	 * Creates a Wizardslab with the given description, intro, and maxTurns
	 * @param description the description of the room
	 * @param intro the introduction describing the scenario
	 * @param maxTurns the number of turns the player can take before failing
	 */
	public WizardsLab(String description, String intro, int maxTurns) {
	    super(description, intro);
	    add(new WandOfKeySummoningRecipe());
	    Container chest = new PasswordLockedContainer("silver_chest", "a silver chest decorated with pictures of locusts.", "hocus pocus");
	    chest.add(new UselessItem("runed_stick", "a stick decorated with many magical runes"));
	    chest.add(new UselessItem("phoenix_feather", "a stick decorated with many magical runes"));
	    chest.add(new UselessItem("sapphire", "a stick decorated with many magical runes"));
	    chest.add(new UselessItem("unicorn_tears", "a stick decorated with many magical runes"));
	    add(chest);
	    TextItem scrapOfPaper = new TextItem("scrap_of_paper", "a scrap of paper with an unfinished limerick scrawled on it.", 
	            "The paper contains the following text:\n" +
	            "There once was a wizard with focus\n" + 
	            "when facing a swarm of locusts\n" +
	            "he said in a puff\n" +
	            "I know just the stuff\n" + 
	            "and he chanted the spell ***** *****.");
	    add(scrapOfPaper);
	    this.numTurns = 0;
	    this.maxTurns = maxTurns;
	}

	@Override
	public void printRoomPrompt() {
	    System.out.println("You have taken " + numTurns + " turns. You have " + (maxTurns - numTurns) + " turns left to escape.");
	}

	@Override
	public void onCommandAttempted(String command, boolean handled) {
	    if (handled) {
	        numTurns++;
	    }
	}

	@Override
	public boolean escaped() {
	    return getItem("gold_key") != null;
	}

	@Override
	public void onEscaped() {
	    System.out.println("Using the gold key, you open the magical door and escape to freedom! Congratulations, you have escaped in " + numTurns + " turns!");
	}

	@Override
	public void onFailed() {
	    System.out.println("Oh no! Your professor has returned and now you are in big trouble!");
	    System.out.println("Game Over");
	}

	@Override
	public boolean failed() {
	    return numTurns >= maxTurns;
	}


}
