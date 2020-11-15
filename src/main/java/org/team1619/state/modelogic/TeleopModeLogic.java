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

	private Boolean mCurrentlyFront;
	private Boolean mCurrentlyOut;
	private int mCurrentHeight;

	private Boolean mRequestedFront;
	private Boolean mRequestedOut;
	private int mRequestedHeight;

	private Boolean mClawOpen;

	private Boolean mNewCommand;


	public TeleopModeLogic(InputValues inputValues, RobotConfiguration robotConfiguration) {
		super(inputValues, robotConfiguration);
	}

	@Override
	public void initialize() {
		sLogger.info("***** TELEOP *****");

		mCurrentlyFront = true;
		mCurrentlyOut = true;
		mCurrentHeight = 1;

		mRequestedFront = true;
		mRequestedOut = false;
		mRequestedHeight = 2;

		mClawOpen = true;

		fSharedInputValues.setBoolean("ipb_arm_front", mCurrentlyFront);
		fSharedInputValues.setBoolean("ipb_arm_out", mCurrentlyOut);
		fSharedInputValues.setNumeric("ipb_elevator_height", mCurrentHeight);

		mNewCommand = false;
	}

	@Override
	public void update() {

		mNewCommand = false;

		mCurrentlyFront = fSharedInputValues.getBoolean("ipb_arm_front");
		mCurrentlyOut =  fSharedInputValues.getBoolean("ipb_arm_out");
		mCurrentHeight = (int)fSharedInputValues.getNumeric("ipn_elevator_position");

		if(fSharedInputValues.getBooleanRisingEdge("ipb_operator_right_trigger")) {
			mRequestedFront = true;
			mRequestedOut = true;
			mRequestedHeight = 2;
			mNewCommand = true;
		}

		if(fSharedInputValues.getBooleanRisingEdge("ipb_operator_right_bumper")) {
			mRequestedFront = true;
			mRequestedOut = false;
			mRequestedHeight = 2;
			mNewCommand = true;
		}

		if(fSharedInputValues.getBooleanRisingEdge("ipb_operator_left_trigger")) {
			mRequestedFront = false;
			mRequestedOut = true;
			mRequestedHeight = 2;
			mNewCommand = true;
		}

		if(fSharedInputValues.getBooleanRisingEdge("ipb_operator_left_bumper")) {
			mRequestedFront = false;
			mRequestedOut = false;
			mRequestedHeight = 2;
			mNewCommand = true;
		}

		if(fSharedInputValues.getBooleanRisingEdge("ipb_operator_dpad_up")) {
			mRequestedHeight = (mRequestedHeight >= 6) ? 6 : ++mRequestedHeight;
			mNewCommand = true;
		}

		if(fSharedInputValues.getBooleanRisingEdge("ipb_operator_dpad_down")) {
			mRequestedHeight = (mRequestedHeight <= 1) ? 1 : --mRequestedHeight;
			mNewCommand = true;
		}

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
		switch (name) {
			case "st_drivetrain_zero":
				return !fSharedInputValues.getBoolean("ipb_drivetrain_has_been_zeroed");
			case "st_arm_zero":
				return !fSharedInputValues.getBoolean("ipb_arm_has_been_zeroed");
			case "st_elevator_zero":
				return !fSharedInputValues.getBoolean("ipb_elevator_has_been_zeroed");

			// Starting from the front
			case "sq_front_out_front":
				return mNewCommand && mCurrentlyFront && mRequestedFront && mRequestedOut && mRequestedHeight == 2;
			case "sq_front_protect_front":
				return mNewCommand && mCurrentlyFront && mRequestedFront && !mRequestedOut && mRequestedHeight == 2;
			case "sq_front_protect_back":
				return mNewCommand && mCurrentlyFront && !mRequestedFront && !mRequestedOut && mRequestedHeight == 2;
			case "sq_front_out_back":
				return mNewCommand && mCurrentlyFront && !mRequestedFront && mRequestedOut && mRequestedHeight == 2;

			// Starting from the back
			case "sq_back_out_back":
				return mNewCommand && !mCurrentlyFront && !mRequestedFront && mRequestedOut && mRequestedHeight == 2;
			case "sq_back_protect_back":
				return mNewCommand && !mCurrentlyFront && !mRequestedFront && !mRequestedOut && mRequestedHeight == 2;
			case "sq_back_protect_front":
				return mNewCommand && !mCurrentlyFront && mRequestedFront && !mRequestedOut && mRequestedHeight == 2;
			case "sq_back_out_front":
				return mNewCommand && !mCurrentlyFront && mRequestedFront && mRequestedOut && mRequestedHeight == 2;

			// Change heights when out front
			case "sq_front_h1_out_front":
				return mNewCommand && mCurrentlyFront && mCurrentlyOut && mRequestedHeight == 1;
//			case "sq_front_h2_out_front":
//				return mCurrentlyFront && mCurrentlyOut && mRequestedHeight == 2;
			case "sq_front_h3_out_front":
				return mNewCommand && mCurrentlyFront && mCurrentlyOut && mRequestedHeight == 3;
			case "sq_front_h4_out_front":
				return mNewCommand && mCurrentlyFront && mCurrentlyOut && mRequestedHeight == 4;
			case "sq_front_h5_out_front":
				return mNewCommand && mCurrentlyFront && mCurrentlyOut && mRequestedHeight == 5;
			case "sq_front_h6_out_front":
				return mNewCommand && mCurrentlyFront && mCurrentlyOut && mRequestedHeight == 6;

			// Change heights when out back
			case "sq_back_h1_out_back":
				return mNewCommand && !mCurrentlyFront && mCurrentlyOut && mRequestedHeight == 1;
//			case "sq_back_h2_out_back":
//				return mNewCommand && !mCurrentlyFront && mCurrentlyOut && mRequestedHeight == 2;
			case "sq_back_h3_out_back":
				return mNewCommand && !mCurrentlyFront && mCurrentlyOut && mRequestedHeight == 3;
			case "sq_back_h4_out_back":
				return mNewCommand && !mCurrentlyFront && mCurrentlyOut && mRequestedHeight == 4;
			case "sq_back_h5_out_back":
				return mNewCommand && !mCurrentlyFront && mCurrentlyOut && mRequestedHeight == 5;
			case "sq_back_h6_out_back":
				return mNewCommand && !mCurrentlyFront && mCurrentlyOut && mRequestedHeight == 6;

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
/*
			// Starting from the front
			case "sq_front_out_front":
				return !mCurrentlyFront && !mRequestedFront && !mRequestedOut;
			case "sq_front_protect_front":
				return !mCurrentlyFront && !mRequestedFront && mRequestedOut;
			case "sq_front_protect_back":
				return !mCurrentlyFront && mRequestedFront && !mRequestedOut;
			case "sq_front_out_back":
				return !mCurrentlyFront && mRequestedFront && mRequestedOut;

			// Starting from the back
			case "sq_back_out_back":
				return mCurrentlyFront && mRequestedFront && !mRequestedOut;
			case "sq_back_protect_back":
				return mCurrentlyFront && mRequestedFront && mRequestedOut;
			case "sq_back_protect_front":
				return mCurrentlyFront && !mRequestedFront && !mRequestedOut;
			case "sq_back_out_front":
				return mCurrentlyFront && !mRequestedFront && mRequestedOut;

			// Changing heights when out front
			case "sq_front_h1_out_front":
				return !mCurrentlyFront || !mCurrentlyOut || !(mRequestedHeight == 1);
//			case "sq_front_h2_out_front":
//				return !mCurrentlyFront || !mCurrentlyOut || !(mRequestedHeight == 2);
			case "sq_front_h3_out_front":
				return !mCurrentlyFront || !mCurrentlyOut || !(mRequestedHeight == 3);
			case "sq_front_h4_out_front":
				return !mCurrentlyFront || !mCurrentlyOut || !(mRequestedHeight == 4);
			case "sq_front_h5_out_front":
				return !mCurrentlyFront || !mCurrentlyOut || !(mRequestedHeight == 5);
			case "sq_front_h6_out_front":
				return !mCurrentlyFront || !mCurrentlyOut || !(mRequestedHeight == 6);

			// Change heights when out back
			case "sq_back_h1_out_back":
				return mCurrentlyFront || !mCurrentlyOut || !(mRequestedHeight == 1);
//			case "sq_back_h2_out_back":
//				return mCurrentlyFront || !mCurrentlyOut && !(mRequestedHeight == 2);
			case "sq_back_h3_out_back":
				return mCurrentlyFront || !mCurrentlyOut || !(mRequestedHeight == 3);
			case "sq_back_h4_out_back":
				return mCurrentlyFront || !mCurrentlyOut || !(mRequestedHeight == 4);
			case "sq_back_h5_out_back":
				return mCurrentlyFront || !mCurrentlyOut || !(mRequestedHeight == 5);
			case "sq_back_h6_out_back":
				return mCurrentlyFront || !mCurrentlyOut || !(mRequestedHeight == 6);
*/

			default:
				return state.isDone();
		}
	}
}
