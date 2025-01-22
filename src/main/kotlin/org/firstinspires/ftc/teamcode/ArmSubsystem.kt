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
import dev.frozenmilk.mercurial.commands.Lambda
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
    var clawScale: Double = .3
    var wristScale: Double = .48
    var wristOffset: Double = 0.1
    @JvmField var maxArmAngle: Double = 200.0

    //PIDF Coefficients
    @JvmField var p = 0.005
    @JvmField var i = 0.0
    @JvmField var d = 0.0003
    @JvmField var f = 0.1
    var ticksPerDegree = 500.0/135.0
    @JvmField var targetArmAngle = -40.0
    @JvmField var scale = .3
    var angleOffset = -45.0
    var liveOffset = 0.0

    var pidController: PIDController = PIDController(p,i,d)

    //loop
    override fun preUserLoopHook(opMode: Wrapper) {
        targetArmAngle =  if(targetArmAngle > maxArmAngle) maxArmAngle else targetArmAngle //calculate target angle, limit if it would make the arm go over the maximum
        pidController.setPID(p,i,d) //i feel like this could go in an init or something now, but when tuning this needs to be here
        var armTicks = motor.currentPosition //could probably be cached or something
        armAngle =(armTicks/ticksPerDegree) + angleOffset
        var result = pidController.calculate(armAngle, targetArmAngle)
        var ff= cos(Math.toRadians(armAngle))*f
        opMode.opMode.telemetry
            .addData("pos", armTicks)
            .addData("angle", armAngle)
            .addData("target angle", targetArmAngle)
            .addData("arm power", motorPwr)
            .addData("wrist pos", wristPos)
            .addData("claw pos", clawPos)

        var dashboard = FtcDashboard.getInstance()
        var dashboardTelemetry = dashboard.getTelemetry()

        dashboardTelemetry.addData("real", armAngle)
        dashboardTelemetry.addData("target", targetArmAngle)
        dashboardTelemetry.addData("ff", ff)
        dashboardTelemetry.addData("result", result)

        motor.power=-((result)/scale+ff)
        wrist.position=(wristPos*wristScale)+wristOffset
        claw.position=clawPos*clawScale
    }

    //BASIC METHODS
    fun setArmVals(a: Double, w: Double, c: Double){
        targetArmAngle=a+liveOffset
        wristPos=w
        clawPos=c
    }

    fun setArmVals(a: Double, w: Double){
        targetArmAngle=a
        wristPos=w
    }

    fun setArmVals(vals: Vals){
        setArmVals(vals.arm,vals.wrist)
    }

    fun resetLocation(){
        motor.mode= DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motor.mode= DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }


    //COMMANDS
    @JvmField var frontFloorVals = Vals(-25.0, 1.0)
    val frontFloor = Lambda("front floor ")
        .setInterruptible(true)
        .setInit{
            setArmVals(frontFloorVals)
        }

    @JvmField var frontWallVals = Vals(10.0, 0.3)
    val frontWall = Lambda("wall ")
        .setInterruptible(true)
        .setInit{
            setArmVals(frontWallVals)
        }

    @JvmField var backWallVals = Vals(170.0, 0.5)
    val backWall = Lambda("wall ")
        .setInterruptible(true)
        .setInit{
            setArmVals(backWallVals)
        }

    @JvmField var backFloorVals = Vals(190.0, 0.0)
    val backFloor = Lambda("backFloor")
        .setInterruptible(true)
        .setInit{
            setArmVals(backFloorVals)
        }

    @JvmField var maxFrontVals = Vals(0.0, 0.3)
    val maxFront = Lambda("maxFront")
        .setInterruptible(true)
        .setInit{
            setArmVals(maxFrontVals)
        }

    @JvmField var maxBackVals = Vals(180.0, 0.5)
    val maxBack = Lambda("maxBack")
        .setInterruptible(true)
        .setInit{
            setArmVals(maxBackVals)
        }
    
    @JvmField var highBarVals = Vals(100.0, 0.5)
    val highBar = Lambda("highBar")
        .setInterruptible(true)
        .setInit{
            setArmVals(highBarVals)
        }

    @JvmField var highBarApproachingVals = Vals(90.0,0.5)
    val approachHighBar = Lambda("Approach high bar")
        .setInterruptible(true)
        .setInit{
            setArmVals(highBarApproachingVals)
        }

    @JvmField var lowBarVals = Vals(190.0, 0.5)
    val lowBar = Lambda("lowBar")
        .setInterruptible(true)
        .setInit{
            setArmVals(lowBarVals)
        }

    @JvmField var basketVals = Vals(80.0, 0.5)
    val basket = Lambda("basket ")
        .setInterruptible(true)
        .setInit{
            setArmVals(basketVals)
        }

    @JvmField var verticalVals = Vals(90.0,0.5)
    val vertical = Lambda("vertical")
        .setInterruptible(true)
        .setInit{
            setArmVals(verticalVals)
        }

    //claw commands
    val closeClaw = Lambda("close claw")
        .setInterruptible(true)
        .setInit{
            clawPos = 1.0
        }

    val openClaw = Lambda("open claw")
        .setInterruptible(true)
        .setInit{
            clawPos = 0.0
        }

    val nudgeOffsetUp = Lambda("nudge offset up")
        .setInterruptible(true)
        .setInit{
            liveOffset+=0.1
            targetArmAngle += liveOffset
        }

    val nudgeOffsetDown = Lambda("nudge offset down")
        .setInterruptible(true)
        .setInit{
            liveOffset+=0.1
            targetArmAngle += liveOffset
        }
@Config
    data class Vals(@JvmField var arm: Double, @JvmField var wrist: Double)
}