package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import dev.frozenmilk.dairy.pasteurized.SDKGamepad
import dev.frozenmilk.mercurial.Mercurial
import dev.frozenmilk.mercurial.bindings.BoundGamepad

@Mercurial.Attach
@ArmSubsystem.Attach
@DrivetrainSubsystem.Attach
@Config
@TeleOp
class BasicOPMode : OpMode() {
    override fun init() {
        val boundGamepad = BoundGamepad(SDKGamepad(gamepad1))
    }

    override fun loop() {
        DrivetrainSubsystem.setDrivePower(
            gamepad1.left_stick_x.toDouble(),
            -gamepad1.left_stick_y.toDouble(),
            gamepad1.right_stick_x.toDouble()
        )
        ArmSubsystem.setArmVals(
            gamepad1.right_stick_y.toDouble(),
            gamepad1.right_trigger.toDouble(),
            gamepad1.left_trigger.toDouble()
        )

        if(gamepad1.start) ArmSubsystem.resetLocation()

        var dashboard = FtcDashboard.getInstance();
        var dashboardTelemetry = dashboard.getTelemetry();

        dashboardTelemetry.update();

    }
}