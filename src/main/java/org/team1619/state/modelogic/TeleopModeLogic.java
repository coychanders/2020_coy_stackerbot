package org.team1619.state.modelogic;

import org.uacr.models.state.State;
import org.uacr.robot.AbstractModeLogic;
import org.uacr.shared.abstractions.InputValues;
import org.uacr.shared.abstractions.RobotConfiguration;
import org.uacr.utilities.logging.LogManager;
import org.uacr.utilities.logging.Logger;

/**
 * Handles the isReady and isDone logic for teleop mode on competition bot
 */

public class TeleopModeLogic extends AbstractModeLogic {

	private static final Logger sLogger = LogManager.getLogger(TeleopModeLogic.class);

	private String mCurrentArmFacing = "front";
	private String mCurrentArmPosition = "protect";
	private int mCurrentElevatorHeight = 1;

	private String mRequestedArmFacing = "front";
	private String mRequestedArmPosition = "protect";
	private int mRequestedElevatorHeight = 2;

	private Boolean mClawOpen = true;

	private Boolean mNewCommand = false;
	private Boolean mFirstCommand = true;


	public TeleopModeLogic(InputValues inputValues, RobotConfiguration robotConfiguration) {
		super(inputValues, robotConfiguration);
	}

	@Override
	public void initialize() {
		sLogger.info("***** TELEOP *****");

		mCurrentArmFacing = "front";
		mCurrentArmPosition = "protect";
		mCurrentElevatorHeight = 1;

		mRequestedArmFacing = "front";
		mRequestedArmPosition = "protect";
		mRequestedElevatorHeight = 2;

		mClawOpen = true;

		fSharedInputValues.setString("ips_arm_facing", mCurrentArmFacing);
		fSharedInputValues.setString("ips_arm_position", mCurrentArmPosition);
		fSharedInputValues.setNumeric("ipn_elevator_height", mCurrentElevatorHeight);

		mFirstCommand = true;
		mNewCommand = false;
	}

	@Override
	public void update() {

		//Todo - Is there a better way to do this?
		// This causes a specific sequence to run right after the robot has been zeroed to put the robot into the right position
		if(mFirstCommand){
			if(fSharedInputValues.getBoolean("ipb_robot_has_been_zeroed")){
				mFirstCommand = false;
				mNewCommand = true;
			}
		}
		else {
			mNewCommand = false;
		}

		mCurrentArmFacing = fSharedInputValues.getString("ips_arm_facing");
		mCurrentArmPosition =  fSharedInputValues.getString("ips_arm_position");
		mCurrentElevatorHeight = (int)fSharedInputValues.getNumeric("ipn_elevator_height");

		if(fSharedInputValues.getBooleanRisingEdge("ipb_operator_right_trigger")) {
			mRequestedArmFacing = "front";
			mRequestedArmPosition = "out";
			mNewCommand = true;
		}

		if(fSharedInputValues.getBooleanRisingEdge("ipb_operator_right_bumper")) {
			mRequestedArmFacing = "front";
			mRequestedArmPosition = "protect";
			mRequestedElevatorHeight = 2;
			mNewCommand = true;
		}

		if(fSharedInputValues.getBooleanRisingEdge("ipb_operator_left_trigger")) {
			mRequestedArmFacing = "back";
			mRequestedArmPosition = "out";
			mNewCommand = true;
		}

		if(fSharedInputValues.getBooleanRisingEdge("ipb_operator_left_bumper")) {
			mRequestedArmFacing = "back";
			mRequestedArmPosition = "protect";
			mRequestedElevatorHeight = 2;
			mNewCommand = true;
		}

		if(fSharedInputValues.getBooleanRisingEdge("ipb_operator_dpad_up")) {
			mRequestedElevatorHeight = (mRequestedElevatorHeight >= 6) ? 6 : ++mRequestedElevatorHeight;
			if(mCurrentArmPosition.equals("out")) {
				mNewCommand = true;
			}
		}

		if(fSharedInputValues.getBooleanRisingEdge("ipb_operator_dpad_down")) {
			mRequestedElevatorHeight = (mRequestedElevatorHeight <= 1) ? 1 : --mRequestedElevatorHeight;
			if(mCurrentArmPosition.equals("out")) {
				mNewCommand = true;
			}
		}

		fSharedInputValues.setNumeric("ipn_elevator_requested_height", mRequestedElevatorHeight);

		if(fSharedInputValues.getBooleanRisingEdge("ipb_driver_right_trigger")){
			mClawOpen = !mClawOpen;
			mNewCommand = true;
		}

	}

	@Override
	public void dispose() {

	}

	@Override
	public boolean isReady(String name) {

		// Create the sequence name that should be true in this frame
		String command = "sq_" + mCurrentArmFacing + "_to_h" + mRequestedElevatorHeight + "_" + mRequestedArmFacing + "_" + mRequestedArmPosition;
		if(name.equals(command) && mNewCommand){
			return true;
		}

		switch (name) {
			case "st_drivetrain_zero":
				return !fSharedInputValues.getBoolean("ipb_drivetrain_has_been_zeroed");
			case "st_arm_zero":
				return !fSharedInputValues.getBoolean("ipb_arm_has_been_zeroed");
			case "st_elevator_zero":
				return !fSharedInputValues.getBoolean("ipb_elevator_has_been_zeroed");

			case "st_claw_open":
				return mNewCommand && mClawOpen;
			case "st_claw_closed":
				return mNewCommand && !mClawOpen;

			default:
				return false;
		}
	}

	@Override
	public boolean isDone(String name, State state) {
		switch (name) {
			default:
				return state.isDone();
		}
	}
}
