package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;

@TeleOp(name="SELMTeleop_NormalDrive", group="SEML")
public class SEMLTeleop_NormalDrive extends OpMode{
    public double drive, turn, strafe;
    double speed = 0.5;

    //* Hardware
    public DcMotor backLeftDrive, backRightDrive, frontLeftDrive, frontRightDrive;
    public Limelight3A limelight;

    //* Bearing Align
    Double lastTx = null;
    boolean bearingAlignMode = false;

    @Override
    public void init() {
        backLeftDrive = hardwareMap.get(DcMotor.class, "backLeftDrive");
        backRightDrive = hardwareMap.get(DcMotor.class, "backRightDrive");
        frontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeftDrive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRightDrive");

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
        //* Fine tuning bearing align
        double bearingThreshold = 1;
        double Kp = 0.03;

        //* Toggle bearing align mode
        boolean gamepadAState = gamepad1.a;
        if (gamepad1.a && !gamepadAState) {
            bearingAlignMode = !bearingAlignMode;
        }

        //* Normal Drive (Turn w/ left joystick)
        drive = gamepad1.right_stick_y;
        strafe = gamepad1.right_stick_x;
        turn = gamepad1.left_stick_x;

        LLResult result = limelight.getLatestResult();

        //* If AprilTag is in view, use bearingAlign
        //* Else, turn in the direction of the last known target X
        if (result != null && result.isValid()) {
            bearingAlign(result, Kp, bearingThreshold);
        } else{
            lastBearing();
        }

        move(speed, drive, strafe, turn);

        //* Telemetry
        telemetry.addData("Align mode active", bearingAlignMode);
        telemetry.addData("LastTX", lastTx);
        telemetry.addData("Result", result != null && result.isValid());
        telemetry.update();
    }

    /*!Helper Methods*/
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
        double frontLeftPower = drive - strafe - turn;
        double frontRightPower = drive + strafe + turn;
        double backLeftPower = - drive + strafe - turn;
        double backRightPower = - drive - strafe + turn;

        double max = Math.max(1.0,
                Math.max(Math.abs(frontLeftPower),
                        Math.max(Math.abs(frontRightPower),
                                Math.max(Math.abs(backLeftPower),
                                        Math.abs(backRightPower)))));

        frontLeftPower /= max;
        frontRightPower /= max;
        backLeftPower /= max;
        backRightPower /= max;

        frontLeftDrive.setPower(speed * (frontLeftPower));
        frontRightDrive.setPower(speed * (frontRightPower));
        backLeftDrive.setPower(speed * (backLeftPower));
        backRightDrive.setPower(speed * (backRightPower));
    }

    /**
     * Bearing alignment function
     * @param result Latest Limelight result
     * @param Kp Proportional constant for steering
     * @param bearingThreshold Acceptable error range in degrees
     */
    public void bearingAlign(LLResult result, double Kp, double bearingThreshold){
        double tx = result.getTx();

        if (bearingAlignMode) {
            strafe = 0;
            telemetry.addData("tx", tx);
            telemetry.update();

            if (Math.abs(tx) > bearingThreshold) {
                strafe = Kp * tx;
                strafe = Math.max(- speed, Math.min(speed, strafe)); // clamp
            }
        }

        //* Telemetry
        lastTx = tx;
        telemetry.addData("Target X", tx);
    }

    /**
     * Turn in the direction of the last known target X
     * <br><br>
     * Used when the target is outside of view
     */
    public void lastBearing(){
        if (bearingAlignMode) {
            if(lastTx >= 0){
                turn = speed;
            }else if(lastTx < 0){
                turn = - speed;
            }
        }
    }

    /**
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {

    }
}
