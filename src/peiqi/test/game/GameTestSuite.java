package game;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestAttack.class,
				TestAttackerDefenderValidation.class,
				TestConqueringValidation.class,
				TestEndOfGame.class,
				TestFortification.class,
				TestGetCardValidation.class,
				TestReinforcementArmies.class,
				TestStartup.class,
				CardsTest.class,
				ContinentTest.class,
				PlayerTest.class,
				PlayerColorTest.class,
				RiskMapTest.class,
				TerritoryTest.class,
				PhaseTest.class
				})

public class GameTestSuite {   
}  