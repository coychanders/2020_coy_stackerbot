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

public class Arm_States implements Behavior {

	private static final Logger sLogger = LogManager.getLogger(Arm_States.class);
	private static final Set<String> sSubsystems = Set.of("ss_arm");

	private final InputValues fSharedInputValues;
	private final OutputValues fSharedOutputValues;

	private final Timer fTimer;

	private Double armAngle;

	public Arm_States(InputValues inputValues, OutputValues outputValues, Config config, RobotConfiguration robotConfiguration) {
		fSharedInputValues = inputValues;
		fSharedOutputValues = outputValues;

		fTimer = new Timer();

	}

	@Override
	public void initialize(String stateName, Config config) {
		sLogger.debug("Entering state {}", stateName);

		armAngle = config.getDouble("arm_angle");
		fSharedOutputValues.setNumeric("opn_arm", "position", armAngle);

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
			if(armAngle <= 90)
			{
				fSharedInputValues.setString("ips_arm_facing", "front");
			}else {
				fSharedInputValues.setString("ips_arm_facing", "back");
			}
			if((armAngle < 40) || (armAngle > 140))
			{
				fSharedInputValues.setString("ips_arm_position", "out");
			}else {
				fSharedInputValues.setString("ips_arm_position", "protect");
			}
			return true;
		}
		return false;
	}

	@Override
	public Set<String> getSubsystems() {
		return sSubsystems;
	}
}