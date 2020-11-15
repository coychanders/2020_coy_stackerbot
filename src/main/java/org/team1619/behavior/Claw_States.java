package org.team1619.behavior;

import org.uacr.models.behavior.Behavior;
import org.uacr.shared.abstractions.InputValues;
import org.uacr.shared.abstractions.OutputValues;
import org.uacr.shared.abstractions.RobotConfiguration;
import org.uacr.utilities.Config;
import org.uacr.utilities.logging.LogManager;
import org.uacr.utilities.logging.Logger;

import java.util.Set;

/**
 * Example behavior to copy for other behaviors
 */

public class Claw_States implements Behavior {

	private static final Logger sLogger = LogManager.getLogger(Claw_States.class);
	private static final Set<String> sSubsystems = Set.of("ss_claw");

	private final InputValues fSharedInputValues;
	private final OutputValues fSharedOutputValues;

	private Boolean mOpenClaw;

	public Claw_States(InputValues inputValues, OutputValues outputValues, Config config, RobotConfiguration robotConfiguration) {
		fSharedInputValues = inputValues;
		fSharedOutputValues = outputValues;
	}

	@Override
	public void initialize(String stateName, Config config) {
		sLogger.debug("Entering state {}", stateName);

		mOpenClaw = config.getBoolean("claw_open");

		fSharedOutputValues.setBoolean("opb_claw", mOpenClaw);
	}

	@Override
	public void update() {

	}

	@Override
	public void dispose() {

	}

	@Override
	public boolean isDone() {
		return true;
	}

	@Override
	public Set<String> getSubsystems() {
		return sSubsystems;
	}
}