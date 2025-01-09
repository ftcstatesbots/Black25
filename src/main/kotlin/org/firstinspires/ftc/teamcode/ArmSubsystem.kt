package org.firstinspires.ftc.teamcode

import com.ThermalEquilibrium.homeostasis.Controllers.Feedback.AngleController
import com.ThermalEquilibrium.homeostasis.Controllers.Feedback.PIDEx
import com.ThermalEquilibrium.homeostasis.Parameters.PIDCoefficientsEx
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.Servo
import dev.frozenmilk.dairy.core.FeatureRegistrar
import dev.frozenmilk.dairy.core.dependency.Dependency
import dev.frozenmilk.mercurial.subsystems.Subsystem
import java.lang.annotation.Inherited

//first subsystem, so be patient
class ArmSubsystem : Subsystem {

    override var dependency: Dependency<*> = Dependency {
        opMode, resolvedFeatures, yielding ->
    }
    @Target(AnnotationTarget.CLASS)
    @Retention(AnnotationRetention.RUNTIME)
    @MustBeDocumented
    @Inherited
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
    private var targetArmPos = 0.0

    private var Kp = 0.0
    private var Ki = 0.0
    private var Kd = 0.0
    private var iSumMax = 0.0

    private var pidCoefficientsEx = PIDCoefficientsEx(Kp, Ki, Kd, iSumMax,0.0,0.0)
    private var pidEx = PIDEx(pidCoefficientsEx)
    private var angleController = AngleController(pidEx)
    private var

}