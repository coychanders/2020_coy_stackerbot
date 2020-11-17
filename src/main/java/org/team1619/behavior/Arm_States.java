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

	private final String fYAxisID;

	private final Timer fTimer;
	private double armAngle;

	public Arm_States(InputValues inputValues, OutputValues outputValues, Config config, RobotConfiguration robotConfiguration) {
		fSharedInputValues = inputValues;
		fSharedOutputValues = outputValues;

		fYAxisID = robotConfiguration.getString("global_arm", "arm_adjust");
		fTimer = new Timer();
	}

	@Override
	public void initialize(String stateName, Config config) {
		sLogger.debug("Entering state {}", stateName);

		armAngle = config.getDouble("arm_angle");
		fTimer.start(1000);
	}

	@Override
	public void update() {

		double armAdjust = fSharedInputValues.getNumeric(fYAxisID);
		armAngle += armAdjust/10;
		armAngle = Math.max(armAngle, 0);
		armAngle = Math.min(armAngle, 180);
		fSharedOutputValues.setNumeric("opn_arm", "position", armAngle);
	}

	@Override
	public void dispose() {

	}

	@Override
	public boolean isDone() {

		// Simulates a period of time for the arm to move and then updates a way to track where it is.
		// Would probably use encoder values in real life
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