/* Copyright (c) 2021 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Holonomic drives provide the ability for the robot to move in three axes (directions) simultaneously.
 * Each motion axis is controlled by one Joystick axis.
 * 1) Axial:    Driving forward and backward               Left-joystick Forward/Backward
 * 2) Lateral:  Strafing right and left                     Left-joystick Right and Left
 * 3) Yaw:      Rotating Clockwise and counter clockwise    Right-joystick Right and Left
 */

@TeleOp(name="Basic: Omni Linear OpMode", group="Linear OpMode")
//@Disabled
public class Telop extends LinearOpMode {

    // Declare OpMode members for each of the 4 motors.
    private final ElapsedTime runtime = new ElapsedTime();

    int Triangle = 855;
      int Square = 1135;
      int OpCross = 1220;
      int Circle = 0;

    @Override
    public void runOpMode() {

        // Initialize the hardware variables. Note that the strings used here must correspond
        // to the names assigned during the robot configuration step on the DS or RC devices.
        DcMotor leftFrontDrive = hardwareMap.get(DcMotor.class, "leftFront");
        DcMotor leftBackDrive = hardwareMap.get(DcMotor.class, "leftRear");
        DcMotor rightFrontDrive = hardwareMap.get(DcMotor.class, "rightFront");
        DcMotor rightBackDrive = hardwareMap.get(DcMotor.class, "rightRear");
        //left_lift_mtr;
        DcMotor leftLiftMtr = hardwareMap.get(DcMotor.class, "left_lift_mtr");
        //right_lift_mtr;
        DcMotor rightLiftMtr = hardwareMap.get(DcMotor.class, "right_lift_mtr");
        DcMotor arm = hardwareMap.get(DcMotor.class, "arm_mtr");
        Servo claw = hardwareMap.get(Servo.class, "Intake");


        leftFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        leftBackDrive.setDirection(DcMotor.Direction.FORWARD);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);
        leftLiftMtr.setDirection(DcMotor.Direction.FORWARD);
        rightLiftMtr.setDirection(DcMotor.Direction.REVERSE);
        arm.setDirection(DcMotor.Direction.REVERSE);

        // Wait for the game to start (driver presses START)
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            double max;

            // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
            double axial   = -gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value
            double lateral =  gamepad1.left_stick_x;
            double yaw     =  gamepad1.right_stick_x;

            if (-gamepad2.left_stick_y > 0.05 || -gamepad2.left_stick_y < -0.05) {
                leftLiftMtr.setPower(gamepad2.left_stick_y * 1);
                rightLiftMtr.setPower(gamepad2.left_stick_y * 1);
            } else {
                leftLiftMtr.setPower(0.1);
                rightLiftMtr.setPower(0.1);}

            // Combine the joystick requests for each axis-motion to determine each wheel's power.
            // Set up a variable for each drive wheel to save the power level for telemetry.
            double leftFrontPower  = axial + lateral + yaw;
            double rightFrontPower = axial - lateral - yaw;
            double leftBackPower   = axial - lateral + yaw;
            double rightBackPower  = axial + lateral - yaw;

            // Normalize the values so no wheel power exceeds 100%
            // This ensures that the robot maintains the desired motion.
            max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
            max = Math.max(max, Math.abs(leftBackPower));
            max = Math.max(max, Math.abs(rightBackPower));

            if (max > 1.0) {
                leftFrontPower  /= max;
                rightFrontPower /= max;
                leftBackPower   /= max;
                rightBackPower  /= max;
            }

            // This is test code:
            //
            // Uncomment the following code to test your motor directions.
            // Each button should make the corresponding motor run FORWARD.
            //   1) First get all the motors to take to correct positions on the robot
            //      by adjusting your Robot Configuration if necessary.
            //   2) Then make sure they run in the correct direction by modifying the
            //      the setDirection() calls above.
            // Once the correct motors move in the correct direction re-comment this code.


           // leftFrontPower  = gamepad1.x ? 1.0 : 0.0;  // X gamepad
           // leftBackPower   = gamepad1.a ? 1.0 : 0.0;  // A gamepad
           // rightFrontPower = gamepad1.y ? 1.0 : 0.0;  // Y gamepad
          //  rightBackPower  = gamepad1.b ? 1.0 : 0.0;  // B gamepad


            // Send calculated power to wheels
            leftFrontDrive.setPower(leftFrontPower);
            rightFrontDrive.setPower(rightFrontPower);
            leftBackDrive.setPower(leftBackPower);
            rightBackDrive.setPower(rightBackPower);

            if (gamepad2.triangle) {
                arm.setTargetPosition(Triangle);
                arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                arm.setPower(0.4);
            }
            if (gamepad2.square) {
                arm.setTargetPosition(Square);
                arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                arm.setPower(0.4);
            }
            if (gamepad2.cross) {
                // high bucket
                arm.setTargetPosition(OpCross);
                arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                arm.setPower(0.4);
            }
            if (gamepad2.circle) {
                arm.setTargetPosition(Circle);
                arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                arm.setPower(0.3);
            }
            if (gamepad2.right_bumper){
            claw.setPosition(1);}
            if (gamepad2.left_bumper){
                claw.setPosition(-1);
            }


            // Show the elapsed game time and wheel power.
           // telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Front left/Right", "%4.2f, %4.2f", leftFrontPower, rightFrontPower);
            telemetry.addData("Back  left/Right", "%4.2f, %4.2f", leftBackPower, rightBackPower);
            telemetry.update();
        }
    }}
