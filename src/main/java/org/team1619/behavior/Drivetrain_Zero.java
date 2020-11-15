package org.team1619.behavior;

import org.uacr.models.behavior.Behavior;
import org.uacr.shared.abstractions.InputValues;
import org.uacr.shared.abstractions.OutputValues;
import org.uacr.shared.abstractions.RobotConfiguration;
import org.uacr.utilities.Config;
import org.uacr.utilities.Timer;
import org.uacr.utilities.logging.LogManager;
import org.uacr.utilities.logging.Logger;

import java.util.Set;

/**
 * Example behavior to copy for other behaviors
 */

public class Drivetrain_Zero implements Behavior {

	private static final Logger sLogger = LogManager.getLogger(Drivetrain_Zero.class);
	private static final Set<String> sSubsystems = Set.of("ss_drivetrain");

	private final InputValues fSharedInputValues;
	private final OutputValues fSharedOutputValues;

	private final Timer fTimer;
	private final int fZero_Timeout;

	public Drivetrain_Zero(InputValues inputValues, OutputValues outputValues, Config config, RobotConfiguration robotConfiguration) {
		fSharedInputValues = inputValues;
		fSharedOutputValues = outputValues;

		fZero_Timeout = robotConfiguration.getInt("global_all", "zero_timeout");
		fTimer = new Timer();
	}

	@Override
	public void initialize(String stateName, Config config) {
		sLogger.debug("Entering state {}", stateName);

		fTimer.start(fZero_Timeout);
	}

	@Override
	public void update() {

		if(fTimer.isDone())
		{
			fSharedInputValues.setBoolean("ipb_drivetrain_has_been_zeroed", true);
		}
	}

	@Override
	public void dispose() {

	}

	@Override
	public boolean isDone() {
		return fSharedInputValues.getBoolean("ipb_drivetrain_has_been_zeroed");
	}

	@Override
	public Set<String> getSubsystems() {
		return sSubsystems;
	}
}