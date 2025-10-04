package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


@Autonomous(name = "SEMLAutonomous_blueGoal", group = "Steely Eyed Missile Lion")

public class SEMLAutonomous_blueGoal extends LinearOpMode {

//  All variables declaration

    public DcMotor backLeftDrive, backRightDrive, frontLeftDrive, frontRightDrive;
    public DcMotor flyWheelLeft;
    public DcMotor flyWheelRight;
    public Servo flyWheelPusher;

    public double flyWheelSpeed;

    /**
     * Movement function for mecanum drive
     * @param speed overall speed multiplier
     * @param drive forward/backward motion
     * @param strafe left/right strafe
     * @param turn rotation motion
     * <ul>
     *     <li><b>+ values go forward , turn right, and strafe right</b></li>
     *     <li><b>- values go backward, turn left , and strafe left<b></li>
     * </ul>
     */
    public void move(double speed, double drive, double strafe, double turn){
        double backLeftPower = drive + strafe + turn;
        double backRightPower = drive - strafe - turn;
        double frontLeftPower = drive - strafe + turn;
        double frontRightPower = drive + strafe - turn;

        double max = Math.max(1.0,
                Math.max(Math.abs(backLeftPower),
                        Math.max(Math.abs(backRightPower),
                                Math.max(Math.abs(frontLeftPower),
                                        Math.abs(frontRightPower)))));

        backLeftPower /= max;
        backRightPower /= max;
        frontLeftPower /= max;
        frontRightPower /= max;

        backLeftDrive.setPower(speed * (backLeftPower));
        backRightDrive.setPower(speed * (backRightPower));
        frontLeftDrive.setPower(speed * (frontLeftPower));
        frontRightDrive.setPower(speed * (frontRightPower));

    }

    public void stopMove(){
        frontLeftDrive.setPower(0);
        frontRightDrive.setPower(0);
        backLeftDrive.setPower(0);
        backRightDrive.setPower(0);
    }

    public void flyWheel(){
        flyWheelLeft.setPower(flyWheelSpeed);
        flyWheelRight.setPower(-flyWheelSpeed);
    }

    /**

     *Autonomous actions function: runOpMode

     * <br><br>

     *Use runOpMode() to pre-define actions to be performed autonomously

     */

    @Override

    public void runOpMode() {

        //Hardware mapping
        backLeftDrive = hardwareMap.get(DcMotor.class, "backLeftDrive");
        backRightDrive = hardwareMap.get(DcMotor.class, "backRightDrive");
        frontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeftDrive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRightDrive");

        flyWheelLeft = hardwareMap.get(DcMotor.class, "flyWheelLeft");
        flyWheelRight = hardwareMap.get(DcMotor.class, "flyWheelRight");
        flyWheelPusher = hardwareMap.get(Servo.class, "flyWheelPusher");

        //Fix motor direction

        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);

        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);

        //AUTONOMOUS CODES GO HERE
        flyWheelSpeed = 0.30;
        flyWheelPusher.setPosition(0.5);
        waitForStart();

        move(0.5, -1, 0, 0);
        sleep(1900);

        stopMove();
        sleep(300);

        flyWheel();
        sleep(5000);

        flyWheelPusher.setPosition(0.1);
        sleep(3000);

        flyWheelPusher.setPosition(0.5);

        move(0.5, 0, 1, 0);
        sleep(1000);


    }

}

