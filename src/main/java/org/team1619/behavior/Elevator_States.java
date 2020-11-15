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

public class Elevator_States implements Behavior {

	private static final Logger sLogger = LogManager.getLogger(Elevator_States.class);
	private static final Set<String> sSubsystems = Set.of("ss_elevator");

	private final InputValues fSharedInputValues;
	private final OutputValues fSharedOutputValues;

	private final Timer fTimer;

	private Double elevatorHeight;

	public Elevator_States(InputValues inputValues, OutputValues outputValues, Config config, RobotConfiguration robotConfiguration) {
		fSharedInputValues = inputValues;
		fSharedOutputValues = outputValues;

		fTimer = new Timer();
	}

	@Override
	public void initialize(String stateName, Config config) {
		sLogger.debug("Entering state {}", stateName);

		elevatorHeight = config.getDouble("elevator_height");

		fSharedOutputValues.setNumeric("opb_elevator", "position", elevatorHeight);

		fTimer.start(1000);

	}

	@Override
	public void update() {

	}

	@Override
	public void dispose() {

	}

	@Override
	public boolean isDone() {
		if(fTimer.isDone()){
			fSharedInputValues.setNumeric("ipn_elevator_height", elevatorHeight);
			fSharedInputValues.setNumeric("ipn_elevator_position", elevatorHeight.intValue() +1);
			return true;
		};
		return false;
	}

	@Override
	public Set<String> getSubsystems() {
		return sSubsystems;
	}
}