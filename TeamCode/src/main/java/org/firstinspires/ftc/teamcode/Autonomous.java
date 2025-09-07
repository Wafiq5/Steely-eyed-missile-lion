package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Autonomous extends LinearOpMode {

    //*************************** Create all variables needed for robot

    DcMotor backLeftDrive;
    DcMotor backRightDrive;
    DcMotor frontLeftDrive;
    DcMotor frontRightDrive;
    ColorSensor color1;
    DistanceSensor distance1;
    BNO055IMU imu;


    double wheelInchesToTicks  = 1445/18;  // number of ticks per 18 inches forward/backward
    double degreesTurnToTicks  = 5200/360; // number of ticks per full rotation degrees
    double strafeInchesToTicks = 1450/18;  // number of ticks per 18 inches strafing
    double CMtoInches          = 18/60;  // convert inches to cm
    double forwardTicks;
    double turnTicks;
    double strafeTicks;

    Double power;
    Double time;

    double distanceCM = 0.0;

    //***********************************************

    //*************************** Create all methods needed for robot

    // drive forward for a certain amount of time

    // depreciated

    // public void drive_forward(String power, String time){

    //   backLeftDrive.setPower(power);

    //   backRightDrive.setPower(power);

    //   frontLeftDrive.setPower(power);

    //   frontRightDrive.setPower(power);

    //   sleep(time);

    //   stop2();

    // }

    public void stop2(){

        backLeftDrive.setPower(0);

        backRightDrive.setPower(0);

        frontLeftDrive.setPower(0);

        frontRightDrive.setPower(0);

        sleep(200);

    }

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

    public void getdistance(int waitTime) {

        distanceCM = distance1.getDistance(DistanceUnit.CM);

        telemetry.addData("test2", distanceCM);

        telemetry.update();

        sleep(waitTime);

    }

    public void driveFwdCM(double power, double forwardCM){
        moveWEncoders( power,  forwardCM * CMtoInches,  0,  0);
    }

    public void moveWEncoders(double power, double forwardInches, double turnDegrees, double strafeInches){

        // Power can go from 0.0 to  +1.0

        // Forward, turn and Strafe should be negative or positive integers

        // + values go forward , turn right, and strafe right

        // - values go backward, turn left , and strafe left

        resetEncoders();

        forwardTicks     = forwardInches * wheelInchesToTicks;

        turnTicks        = turnDegrees   * degreesTurnToTicks;

        strafeTicks      = strafeInches  * strafeInchesToTicks;

        backLeftDrive.setTargetPosition      ((int) (forwardTicks + turnTicks - strafeTicks));

        backRightDrive.setTargetPosition     ((int) (forwardTicks - turnTicks + strafeTicks));

        frontLeftDrive.setTargetPosition     ((int) (forwardTicks + turnTicks + strafeTicks));

        frontRightDrive.setTargetPosition    ((int) (forwardTicks - turnTicks - strafeTicks));

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

        while (opModeIsActive() &&

                (backLeftDrive.isBusy() && backRightDrive.isBusy() &&

                        frontLeftDrive.isBusy() && frontRightDrive.isBusy())) {

            // Display encoder telemetry

            //telemetry.addData("Current Position",  backLeftDrive.getCurrentPosition());

            telemetry.update();

        }

        stop2();

    }



    @Override

    public void runOpMode() {

        //*************************** assign hardware to a variable

        backLeftDrive = hardwareMap.get(DcMotor.class, "backLeftDrive");

        backRightDrive = hardwareMap.get(DcMotor.class, "backRightDrive");

        frontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeftDrive");

        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRightDrive");

        color1 = hardwareMap.get(ColorSensor.class, "color1");

        distance1 = hardwareMap.get(DistanceSensor.class, "distance1");

        imu = hardwareMap.get(BNO055IMU.class, "imu");

        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);

        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);

        // Put initialization blocks here

        // code if you want the robot to do something when you press init


        waitForStart();



        getdistance(3000);

        moveWEncoders( 1.0,  3.5,   0,  0);

        moveWEncoders( 1.0,    0,  90,  0);

        driveFwdCM(1.0, -1);

        getdistance(3000);

        driveFwdCM(1.0, 60);

        getdistance(3000);


    }



    // another method.  this one runs an autonomous set of code

    public void getAllBlues(){

        // call this funtion with getAllBlues();

        // gets all blues from the starting position and rests in 3 point parking zone

        int moveValue = 30;

        moveWEncoders( 1,  3.5,  0,  0);

        moveWEncoders( 1,  0,  0, 18 + 9 );


        moveWEncoders( 1,  33,  0,  0);

        moveWEncoders( 1,  0,  0, 9);

        moveWEncoders( 1,  -moveValue,  0,  0);

        // got first blue

        moveWEncoders( 1,  moveValue,  0,  0);

        moveWEncoders( 1,  0,  0, 8);


        moveWEncoders( 1,  -moveValue,  0,  0);

        // got 2nd blue

        moveWEncoders( 1,  moveValue,  0,  0);

        moveWEncoders( 1,  0,  0, 4);

        moveWEncoders( 1,  -33,  0,  0);

        // got 3rd blue

    }


}

