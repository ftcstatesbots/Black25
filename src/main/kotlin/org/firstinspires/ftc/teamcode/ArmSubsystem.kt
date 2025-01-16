package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.config.Config
import com.arcrobotics.ftclib.controller.PIDController
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.Servo
import dev.frozenmilk.dairy.core.FeatureRegistrar
import dev.frozenmilk.dairy.core.dependency.Dependency
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation
import dev.frozenmilk.dairy.core.wrapper.Wrapper
import dev.frozenmilk.mercurial.subsystems.Subsystem
import java.lang.annotation.Inherited
import kotlin.math.cos
@Config
//first subsystem, so be patient
object ArmSubsystem : Subsystem {
    override var dependency: Dependency<*> = Subsystem.DEFAULT_DEPENDENCY and SingleAnnotation(
        DrivetrainSubsystem.Attach::class.java)

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

    var motorPwr: Double = 0.0
    var wristPos: Double = 0.0
    var clawPos: Double = 0.0
    var armAngle: Double = -45.0
    @JvmField var clawScale: Double = .3
    @JvmField var wristScale: Double = .48
    @JvmField var wristOffset: Double = .41

    //PIDF Coefficients
    @JvmField var p = 0.005
    @JvmField var i = 0.0
    @JvmField var d = 0.0003
    @JvmField var f = 0.1
    @JvmField var ticksPerDegree = 500.0/135.0
    @JvmField var targetArmPosition = 0.0
    @JvmField var angleOffset = -45.0

    var pidController: PIDController = PIDController(p,i,d)

    override fun preUserLoopHook(opMode: Wrapper) {
        pidController.setPID(p,i,d)
        var armTicks = motor.currentPosition
        armAngle = (armTicks/ticksPerDegree) + angleOffset
        var result = pidController.calculate(armTicks.toDouble(), targetArmPosition)
        var ff= cos(Math.toRadians((targetArmPosition/ticksPerDegree) + angleOffset)) * f
        var reactiveFeedforward= cos(Math.toRadians(armAngle)) * f
        opMode.opMode.telemetry
            .addData("pos", armTicks)
            .addData("angle", armAngle)
            .addData("target pos", targetArmPosition)
            .addData("arm power", motorPwr)
            .addData("wrist pos", wristPos)
            .addData("claw pos", clawPos)

        var dashboard = FtcDashboard.getInstance();
        var dashboardTelemetry = dashboard.getTelemetry();

        dashboardTelemetry.addData("real", armTicks)
        dashboardTelemetry.addData("target", targetArmPosition)
        dashboardTelemetry.addData("ff", ff);
        dashboardTelemetry.addData("result", result);

        motor.power=-(result+ff)
        wrist.position=(wristPos*wristScale)+wristOffset
        claw.position=clawPos*clawScale
    }

    fun setArmVals(a: Double, w: Double, c: Double){
        targetArmPosition+=a
        wristPos=w
        clawPos=c
    }

    fun resetLocation(){
        motor.mode= DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motor.mode= DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }
}