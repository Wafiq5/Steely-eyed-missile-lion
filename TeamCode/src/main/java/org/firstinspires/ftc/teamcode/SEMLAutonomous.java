package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


@Autonomous(name = "SEMLAutonomous", group = "Steely Eyed Missile Lion")
public class SEMLAutonomous extends LinearOpMode {
//!  All variables declaration
    // Driving wheels
    DcMotor backLeftDrive;
    DcMotor backRightDrive;
    DcMotor frontLeftDrive;
    DcMotor frontRightDrive;

    // Sensors
    // TODO define sensors once added to the robot
//    ColorSensor color1;
//    DistanceSensor distance1;
//    BNO055IMU imu;

//  Tick calculations
    double wheelInchesToTicks = 1445.0/18.0;  // number of ticks per 18 inches forward/backward
    double degreesTurnToTicks = 5200.0/360.0; // number of ticks per full rotation degrees
    double strafeInchesToTicks = 1450.0/18.0;  // number of ticks per 18 inches strafing
    double CMtoInches = 1.0/2.54;  // convert inches to cm
    double forwardTicks;
    double turnTicks;
    double strafeTicks;

//  Power, Time and Distance
    double power;
    double time;
    double distanceCM = 0.0;



    public void resetEncoders(){
        // Reset encoders
        backLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Set motors to run using encoders
        backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }


    //
//    public void getDistance(int waitTime) {
//        distanceCM = distance1.getDistance(DistanceUnit.CM);
//        telemetry.addData("test2", distanceCM);
//        telemetry.update();
//
//        sleep(waitTime);
//    }

    public void stop(int milliseconds){
        backLeftDrive.setPower(0);
        backRightDrive.setPower(0);
        frontLeftDrive.setPower(0);
        frontRightDrive.setPower(0);

        sleep(milliseconds);
    }

    public void driveFwdCM(double power, double forwardCM){
        moveWEncoders( power,  forwardCM * CMtoInches,  0,  0);
    }

    /**
     * Autonomous directional movements function: moveWEncoders
     *
     * @param power Power can go from 0.0 to +1.0
     * @param forwardInches Distance to go forward
     * @param turnDegrees Degrees to turn
     * @param strafeInches Distance to strafe
     * <ul>
     *     <li><b>+ values go forward , turn right, and strafe right</b></li>
     *     <li><b>- values go backward, turn left , and strafe left<b></li>
     * </ul>
     */
    public void moveWEncoders(double power, double forwardInches, double turnDegrees, double strafeInches){
        resetEncoders();

        forwardTicks = forwardInches * wheelInchesToTicks;
        turnTicks = turnDegrees   * degreesTurnToTicks;
        strafeTicks = strafeInches  * strafeInchesToTicks;

        backLeftDrive.setTargetPosition((int) (forwardTicks + turnTicks - strafeTicks));
        backRightDrive.setTargetPosition((int) (forwardTicks - turnTicks + strafeTicks));
        frontLeftDrive.setTargetPosition((int) (forwardTicks + turnTicks + strafeTicks));
        frontRightDrive.setTargetPosition((int) (forwardTicks - turnTicks - strafeTicks));

        // Set motors to RUN_TO_POSITION mode
        backLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Start moving
        backLeftDrive.setPower(power);
        backRightDrive.setPower(power);
        frontLeftDrive.setPower(power);
        frontRightDrive.setPower(power);

        // Keep looping while the motors are still running AND the OpMode is active
        while( opModeIsActive() &&
                (
                backLeftDrive.isBusy() &&
                backRightDrive.isBusy() &&
                frontLeftDrive.isBusy() &&
                frontRightDrive.isBusy()
                )
        ){
            // Display encoder telemetry
            telemetry.update();

        }
        stop(200);
    }

    /**
     *Autonomous actions function: runOpMode
     * <br><br>
     *Use runOpMode() to pre-define actions to be performed autonomously
    */
    @Override
    public void runOpMode() {
        //! Hardware mapping
        // Driving motors
        // TODO (for oficial robot) When assigning motor names, keep in mind to use the following names (deviceName) to assign each motor
        backLeftDrive = hardwareMap.get(DcMotor.class, "backLeftDrive");
        backRightDrive = hardwareMap.get(DcMotor.class, "backRightDrive");
        frontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeftDrive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRightDrive");

        // Sensors
        // TODO declare sensors once added to the robot
//        color1 = hardwareMap.get(ColorSensor.class, "color1");
//        distance1 = hardwareMap.get(DistanceSensor.class, "distance1");
//        imu = hardwareMap.get(BNO055IMU.class, "imu");

        //Fix motor direction
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);

        //! AUTONOMOUS CODES GO HERE
        waitForStart();

    }
}

