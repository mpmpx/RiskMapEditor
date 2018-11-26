package risk.game;

/**
 * This class maintains status of current phase.
 */
public class Phase {

	public final static int STARTUP = -1;
	public final static int REINFORCEMENT = 0;
	public final static int ATTACK = 1;
	public final static int FORTIFICATION = 2;
	
	private int currentPhase;

	/**
	 * Initialize the phase and set the current phase as start up phase.
	 */
	public Phase() {
		currentPhase = STARTUP;
	}

	/**
	 * Proceeds to the next phase.
	 */
	public void nextPhase() {
		currentPhase = (currentPhase + 1) % 3;
	}
	
	/**
	 * Returns the current phase.
	 * @return the current phase.
	 */
	public int getCurrentPhase() {
		return currentPhase;
	}
}
