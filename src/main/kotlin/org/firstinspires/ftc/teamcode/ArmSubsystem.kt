package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.config.Config
import com.arcrobotics.ftclib.controller.PIDController
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.Servo
import dev.frozenmilk.dairy.core.FeatureRegistrar
import dev.frozenmilk.dairy.core.dependency.Dependency
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation
import dev.frozenmilk.dairy.core.wrapper.OpModeWrapper
import dev.frozenmilk.dairy.core.wrapper.Wrapper
import dev.frozenmilk.mercurial.subsystems.Subsystem
import java.lang.annotation.Inherited
import kotlin.math.cos

//first subsystem, so be patient
object ArmSubsystem : Subsystem {
    override var dependency: Dependency<*> = Subsystem.DEFAULT_DEPENDENCY and SingleAnnotation(
        DrivetrainSubsystem.Attach::class.java)

    @Target(AnnotationTarget.CLASS)
    @Retention(AnnotationRetention.RUNTIME)
    @MustBeDocumented
    @Inherited
    @Config
    annotation class Attach

    private var motor: DcMotorEx by subsystemCell {
        FeatureRegistrar.activeOpMode.hardwareMap.get(DcMotorEx::class.java, "arm")
    }

    private var wrist: Servo by subsystemCell {
        FeatureRegistrar.activeOpMode.hardwareMap.get(Servo::class.java, "wrist")
    }

    private var claw: Servo by subsystemCell{
        FeatureRegistrar.activeOpMode.hardwareMap.get(Servo::class.java, "claw")
    }

    var motorPwr: Double = 0.0
    var wristPos: Double = 0.0
    var clawPos: Double = 0.0
    //PIDF Coefficients
    const val p = 0.0
    const val i = 0.0
    const val d = 0.0
    const val f = 0.0
    const val ticksPerDegree = 500.0/135.0
    const val targetArmPosition = 0.0


    private var pidController: PIDController = PIDController(p,i,d)

    override fun preUserLoopHook(opMode: Wrapper) {
        pidController.setPID(p,i,d)
        var armPos = motor.currentPosition
        var result = pidController.calculate(armPos.toDouble(), targetArmPosition)
        var ff= cos(Math.toRadians(targetArmPosition / ticksPerDegree)) * f

        opMode.opMode.telemetry
            .addData("pos", armPos)
            .addData("target pos", targetArmPosition)
            .addData("arm power", motorPwr)
            .addData("wrist pos", wristPos)
            .addData("claw pos", clawPos)

        motor.power=motorPwr
        wrist.position=wristPos
        claw.position=clawPos
    }

    fun setArmVals(a: Double, w: Double, c: Double){
        motorPwr=a
        wristPos=w
        clawPos=c
    }
}