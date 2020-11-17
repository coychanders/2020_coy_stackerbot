### 2020 FRC Coy Stackerbot Code
Base on basebot (Nov 2020)

Coded up the whiteboard challenge to test out our solution.

Robot has a drivetrain, elevator, arm and claw
The elevator has 6 heights (1.5 feet, 1.5, 2.5, 3.5, 4.5, 5.5)
The arm has 4 positions (0 degrees - out front, 45 degrees - protect front, 135 degrees - protect back, 180 degress - out back)
The arm can swing over the top when the the elevator is in the 6th height

The goal is to pick up 1 foot square cubes off a 1 foot high table and stack them on another 1 foot high table
More points are scored if cubes are stacked on top of each other.

Driver:
Right Trigger - toggles claw open / closed

Operator:
Right Trigger - elevator is at 1.5 feet, arm is front out
Right bumper - elevator is at 1.5 feet, arm is in front protect
Left bumper - elevator is at 1.5 feet, arm is in back protect
Back bumper - elevator is at 1.5 feet, arm is in back out

dpad up increases the elevator hight
dpad down decreases the elevator height
If the arm is in protect, the new height is not executed until the arm is commanded to go out.
This create a seqence where a cube is held in protect, the operator sets the height to 4, then the operator hits the right trigger for the arm to go out.
- The elevator then goes to height 4 and then the arm goes out




