package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.hardware.DcMotorEx
import dev.frozenmilk.dairy.core.FeatureRegistrar
import dev.frozenmilk.dairy.core.dependency.Dependency
import dev.frozenmilk.dairy.core.wrapper.Wrapper
import dev.frozenmilk.mercurial.subsystems.Subsystem
import java.lang.annotation.Inherited

class DrivetrainSubsystem : Subsystem{
    override var dependency : Dependency<*> = Dependency{opMode, resolvedFeatures, yielding, ->}
    @Target(AnnotationTarget.CLASS)
    @Retention(AnnotationRetention.RUNTIME)
    @MustBeDocumented
    @Inherited
    annotation class Attach

    private val leftFront by subsystemCell {
        FeatureRegistrar.activeOpMode.hardwareMap.get(DcMotorEx::class.java, "lf")
    }
    private val rightFront by subsystemCell {
        FeatureRegistrar.activeOpMode.hardwareMap.get(DcMotorEx::class.java, "rf")
    }
    private val rightBack by subsystemCell {
        FeatureRegistrar.activeOpMode.hardwareMap.get(DcMotorEx::class.java, "rb")
    }
    private val leftBack by subsystemCell {
        FeatureRegistrar.activeOpMode.hardwareMap.get(DcMotorEx::class.java, "lb")
    }

    fun setDriveMotorPower(lf: Double, rf: Double,rb: Double, lb: Double ){
        leftFront.power=lf
        rightFront.power=rf
        rightBack.power=rb
        leftBack.power=lb
    }

    fun setDriveMotorPower(list: DoubleArray){
        setDriveMotorPower(lf = list[0], rf = list[1], rb = list[2], lb = list[3])
    }

    fun setDrivePower(x:Double, y:Double, r:Double){
        setDriveMotorPower(driveKine(x,y,r))
    }

    fun driveKine(x:Double, y:Double, r:Double): DoubleArray{
        return doubleArrayOf(y+x-r, y+x+r, y-x+r, y-x-r)
    }
    fun driveKine(x:Int, y:Int, r:Int): IntArray{
        return intArrayOf(y+x-r, y+x+r, y-x+r, y-x-r)
    }

    override fun preUserLoopHook(opMode: Wrapper) {
        setDrivePower(
            opMode.opMode.gamepad1.left_stick_x.toDouble(),
            -opMode.opMode.gamepad1.left_stick_y.toDouble(),
            opMode.opMode.gamepad1.right_stick_x.toDouble()
        )
    }
}