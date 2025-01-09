package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.config.Config
import com.arcrobotics.ftclib.controller.PIDController
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.Servo
import dev.frozenmilk.dairy.core.FeatureRegistrar
import dev.frozenmilk.dairy.core.dependency.Dependency
import dev.frozenmilk.dairy.core.wrapper.Wrapper
import dev.frozenmilk.mercurial.subsystems.Subsystem
import java.lang.annotation.Inherited
import kotlin.math.cos

//first subsystem, so be patient
class ArmSubsystem : Subsystem {

    override var dependency: Dependency<*> = Dependency {
        opMode, resolvedFeatures, yielding ->
    }
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

    //PIDF Coefficents
    public val p = 0.0
    public val i = 0.0
    public val d = 0.0
    public val f = 0.0
    public val ticksPerDegree = 0.0
    public var targetArmPosition = 0.0


    private var pidController: PIDController = PIDController(p,i,d)

    override fun preUserLoopHook(opMode: Wrapper) {
        pidController.setPID(p,i,d)
        var armPos = motor.currentPosition
        var result = pidController.calculate(armPos.toDouble(), targetArmPosition)
        var ff= cos(Math.toRadians(targetArmPosition / ticksPerDegree)) * f

        opMode.opMode.telemetry.addData("pos", armPos)
        opMode.opMode.telemetry.addData("target", targetArmPosition)
    }
}