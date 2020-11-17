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

	private final String fElevatorAdjustID;
	private final Timer fTimer;
	private Double elevatorHeight;

	public Elevator_States(InputValues inputValues, OutputValues outputValues, Config config, RobotConfiguration robotConfiguration) {
		fSharedInputValues = inputValues;
		fSharedOutputValues = outputValues;

		fElevatorAdjustID = robotConfiguration.getString("global_elevator", "elevator_adjust");
		fTimer = new Timer();
	}

	@Override
	public void initialize(String stateName, Config config) {
		sLogger.debug("Entering state {}", stateName);

		elevatorHeight = config.getDouble("elevator_height");
		fTimer.start(1000);

	}

	@Override
	public void update() {

		double elevatorAdjust = fSharedInputValues.getNumeric(fElevatorAdjustID);
		elevatorHeight += elevatorAdjust/100;
		elevatorHeight = Math.max(elevatorHeight, 0.5);
		elevatorHeight = Math.min(elevatorHeight, 6.5);
		fSharedOutputValues.setNumeric("opn_elevator", "position", elevatorHeight);
	}

	@Override
	public void dispose() {

	}

	@Override
	public boolean isDone() {

		// Simulates the time for the elevator to move. Then updates a way to track where it is
		// Would probably use the encoder in real life
		if(fTimer.isDone()){
			fSharedInputValues.setNumeric("ipn_elevator_height", elevatorHeight.intValue() +1);
			return true;
		}
		return false;
	}

	@Override
	public Set<String> getSubsystems() {
		return sSubsystems;
	}
}