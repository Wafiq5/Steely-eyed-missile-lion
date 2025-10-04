package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="SEMLTeleop_v2_Emiliano", group="Steely Eyed Missile Lion")
public class SEMLTeleop_v2_Emiliano extends OpMode{
    public double drive, turn, strafe;
    double speed = .75;

    //* Hardware
    public DcMotor backLeftDrive, backRightDrive, frontLeftDrive, frontRightDrive;
    public DcMotor flyWheelLeft;
    public DcMotor flyWheelRight;
    public Servo flyWheelPusher;
    public Limelight3A limelight;

    //* FlyWheel Speed
    double flyWheelSpeed_closeRange;
    double flyWheelSpeed_highRange;

    @Override
    public void init() {
        backLeftDrive = hardwareMap.get(DcMotor.class, "backLeftDrive");
        backRightDrive = hardwareMap.get(DcMotor.class, "backRightDrive");
        frontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeftDrive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRightDrive");

        flyWheelLeft = hardwareMap.get(DcMotor.class, "flyWheelLeft");
        flyWheelRight = hardwareMap.get(DcMotor.class, "flyWheelRight");
        flyWheelPusher = hardwareMap.get(Servo.class, "flyWheelPusher");

        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100);

        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
    }

    /**
     * Code to run ONCE when the driver hits START
     */
    @Override
    public void start() {
        limelight.start();
    }

    /**
     * Code to run REPEATEDLY after the driver hits START but before they hit STOP
     */
    @Override
    public void loop() {
        //*Bearing
        double bearingThreshold = 1;
        double Kp = 0.03;
        LLResult result = limelight.getLatestResult();

        //* Toggle flyWheel Mode
        flyWheelSpeed_closeRange = 0.35;
        flyWheelSpeed_highRange = 0.40;

        //* Normal Drive (Turn w/ left joystick)
        drive = -gamepad1.right_stick_y;
        strafe = -gamepad1.right_stick_x;
        turn = gamepad1.left_stick_x;




        //* Bearing
        if (result != null && result.isValid()) {
            bearingAlign(result, Kp, bearingThreshold);
        }

        flywheel();
        controlSpeed();
        //* Move robot
        move(speed, drive, strafe, turn);
    }

    //! Helper Methods
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

    /**
     * Bearing alignment function
     * @param result Latest Limelight result
     * @param Kp Proportional constant for steering
     * @param bearingThreshold Acceptable error range in degrees
     */
    public void bearingAlign(LLResult result, double Kp, double bearingThreshold){
        double tx = result.getTx();
        if(gamepad1.left_bumper){
            if (Math.abs(tx) > bearingThreshold) {
                turn = Kp * tx;
                turn = Math.max(- speed, Math.min(speed, turn)); // clamp
            }
        }

        //* Telemetry
        telemetry.addData("Target X", tx);
    }

    public void controlSpeed(){
        if(gamepad1.left_trigger > 0){
            speed = 0.3;
        }else{
            speed = .75;
        }
    }

    public void flywheel(){
        if(gamepad1.right_trigger > 0){
            flyWheelLeft.setPower(flyWheelSpeed_highRange);
            flyWheelRight.setPower(-flyWheelSpeed_highRange);
        }else if(gamepad1.right_bumper){
            flyWheelLeft.setPower(flyWheelSpeed_closeRange);
            flyWheelRight.setPower(-flyWheelSpeed_closeRange);
        }else{
            flyWheelLeft.setPower(0);
            flyWheelRight.setPower(0);
        }

        if(gamepad1.a){
            flyWheelPusher.setPosition(0.1);
        }else{
            flyWheelPusher.setPosition(0.5);
        }
    }

    /**
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {

    }
}
