package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;


@TeleOp(name="SEMLTeleop", group="Steely Eyed Missile Lion")
public class SEMLTeleOp extends OpMode{
    public double drive, turn, strafe;

    public DcMotor  backLeftDrive;
    public DcMotor  backRightDrive;
    public DcMotor  frontLeftDrive;
    public DcMotor  frontRightDrive;

    @Override
    public void init() {
        // Define and Initialize Motors
        backLeftDrive = hardwareMap.get(DcMotor.class, "backLeftDrive");
        backRightDrive = hardwareMap.get(DcMotor.class, "backRightDrive");
        frontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeftDrive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRightDrive");

        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);

    }

    /**
     * Code to run ONCE when the driver hits START
     */
    @Override
    public void start() {

    }

    /**
     * Code to run REPEATEDLY after the driver hits START but before they hit STOP
     */
    @Override
    public void loop() {
        /* Normal Drive (Turn w/ left joystick) */
        double drive;
        double strafe;
        double turn;
        double speed = 0.75;

        drive = gamepad1.right_stick_y;
        strafe = gamepad1.right_stick_x;
        turn = gamepad1.left_stick_x;

        backLeftDrive.setPower(drive - strafe + turn);
        backRightDrive.setPower(drive + strafe - turn);
        frontLeftDrive.setPower( - drive + strafe + turn);
        frontRightDrive.setPower( - drive - strafe - turn);

        /* Tank-style drive */
        /*
          Replace this with the following code for Tank-Style drive
        */
        /*
            double driveRight;
            double driveLeft;
            double strafe;
            double speed = 0.75;

            // Run wheels in tank mode (note: The joystick goes negative when pushed forward, so negate it)
            driveRight = gamepad1.right_stick_y;
            driveLeft = gamepad1.left_stick_y;
            strafe = gamepad1.right_stick_x;

            backLeftDrive.setPower(speed * (driveLeft + strafe));
            backRightDrive.setPower(speed * (driveRight - strafe));
            frontLeftDrive.setPower( - speed * (driveLeft - strafe));
            frontRightDrive.setPower( - speed * (driveRight + strafe));

         */
    }

    /**
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {

    }

}

