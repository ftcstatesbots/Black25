package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import dev.frozenmilk.mercurial.Mercurial

@Mercurial.Attach
@ArmSubsystem.Attach
@DrivetrainSubsystem.Attach
@Autonomous
class BasicAuto : OpMode() {
    override fun init() {

    }

    override fun loop() {

    }

    override fun start() {
        DrivetrainSubsystem.setDrivePower(0.0,.3,0.0)
    }
}