import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import controller.ControllerTestSuite;
import game.GameTestSuite;

@RunWith(Suite.class)
@SuiteClasses({ GameTestSuite.class,
				ControllerTestSuite.class
				})

public class TestSuite {   
}  