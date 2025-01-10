package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import dev.frozenmilk.dairy.pasteurized.SDKGamepad
import dev.frozenmilk.mercurial.Mercurial
import dev.frozenmilk.mercurial.bindings.BoundGamepad

@Mercurial.Attach
@ArmSubsystem.Attach
@DrivetrainSubsystem.Attach
@TeleOp
class BasicOPMode : OpMode() {
    override fun init() {
        val boundGamepad = BoundGamepad(SDKGamepad(gamepad1))
    }

    override fun loop() {
        telemetry.addLine("test")
        telemetry.update()

        DrivetrainSubsystem.setDrivePower(
            gamepad1.left_stick_x.toDouble(),
            gamepad1.left_stick_y.toDouble(),
            gamepad1.right_stick_x.toDouble()
        )
    }
}