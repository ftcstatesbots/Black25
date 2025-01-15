package org.firstinspires.ftc.teamcode

import android.icu.util.TimeUnit
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.util.ElapsedTime
import dev.frozenmilk.mercurial.Mercurial

@DrivetrainSubsystem.Attach
@Mercurial.Attach
@Autonomous
class ReturnAuto : OpMode() {
    var et: ElapsedTime = ElapsedTime()
    override fun init() {
        TODO("Not yet implemented")
    }

    override fun loop() {
        if(et.seconds()<2){
            DrivetrainSubsystem.setDrivePower(0.0,0.3,0.0)
            return
        } else if (et.seconds()<20){
            DrivetrainSubsystem.setDrivePower(0.0,0.0,0.0)
            return
        } else if (et.seconds()<24){
            DrivetrainSubsystem.setDrivePower(0.0,-0.3,0.0)
            return
        } else {
            DrivetrainSubsystem.setDrivePower(0.0,0.0,0.0)
            return
        }
    }

    override fun start() {
        et.reset()
    }
}