package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import dev.frozenmilk.mercurial.Mercurial

@Mercurial.Attach
@ArmSubsystem.Attach
@DrivetrainSubsystem.Attach
@TeleOp
class MotorTester : OpMode() {
    override fun init() {

    }

    override fun loop() {
        var pwr = gamepad1.left_stick_y.toDouble()
        var a = 0.0
        var b = 0.0
        var x = 0.0
        var y = 0.0
        var lt = 0.0
        var rt = 0.0
        var dP = 0.0

        if(gamepad1.a) a = pwr
        if(gamepad1.b) b = pwr
        if(gamepad1.x) x = pwr
        if(gamepad1.y) y = pwr
        if(gamepad1.left_bumper) lt = pwr
        if(gamepad1.right_bumper) rt = pwr
        if(gamepad1.dpad_down) dP = pwr

        DrivetrainSubsystem.setDriveMotorPower(a,b,x,y)
        ArmSubsystem.setArmVals(dP, rt, lt)
    }
}