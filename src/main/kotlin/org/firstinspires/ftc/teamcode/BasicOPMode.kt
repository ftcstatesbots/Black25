package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import dev.frozenmilk.dairy.pasteurized.SDKGamepad
import dev.frozenmilk.dairy.pasteurized.layering.LayeredGamepad
import dev.frozenmilk.dairy.pasteurized.layering.MapLayeringSystem
import dev.frozenmilk.mercurial.Mercurial
import dev.frozenmilk.mercurial.bindings.BoundBooleanSupplier
import dev.frozenmilk.mercurial.bindings.BoundDoubleSupplier
import dev.frozenmilk.mercurial.bindings.BoundGamepad
import dev.frozenmilk.mercurial.commands.Lambda

@Mercurial.Attach
@ArmSubsystem.Attach
@DrivetrainSubsystem.Attach
@Config
@TeleOp
class BasicOPMode : OpMode() {
    var t:Double = 0.0
    enum class Layers {
        INTAKING,
        DEPOSITING,
        DEFAULT
    }

    override fun init() {
        val boundGamepad1 = BoundGamepad(SDKGamepad(gamepad1))
        val intakingGamepad = BoundGamepad(SDKGamepad(gamepad1))
        val depositingGamepad = BoundGamepad(SDKGamepad(gamepad1))
        val defaultGamepad = BoundGamepad(SDKGamepad(gamepad1))

        val gamepadMap = mapOf(
            Layers.INTAKING to intakingGamepad,
            Layers.DEPOSITING to depositingGamepad,
            Layers.DEFAULT to defaultGamepad
        )

        val enumLayeringSystem = MapLayeringSystem(Layers.DEFAULT, gamepadMap)
        val layeredGamepad = LayeredGamepad(enumLayeringSystem)

        defaultGamepad.leftStickButton.or(depositingGamepad.leftStickButton)
            .onTrue (Lambda("intaking layer transition")
                .setInit{ enumLayeringSystem.layer = Layers.INTAKING}
            )
        defaultGamepad.rightStickButton.or(intakingGamepad.rightStickButton)
            .onTrue (Lambda("depositing layer transition")
                .setInit{enumLayeringSystem.layer = Layers.DEPOSITING})
        boundGamepad1.leftStickButton.or(
            boundGamepad1.rightStickButton
        ).onFalse(
            Lambda("return to default layer")
                .setInit{enumLayeringSystem.layer = Layers.DEFAULT}
        )

        intakingGamepad.leftTrigger.conditionalBindState()
            .greaterThan(0.1).bind()
            .onTrue(ArmSubsystem.frontFloor)
        intakingGamepad.leftBumper
            .onTrue(ArmSubsystem.frontWall)
        intakingGamepad.rightTrigger.conditionalBindState()
            .greaterThan(0.1).bind()
            .onTrue(ArmSubsystem.backFloor)
        intakingGamepad.rightBumper
            .onTrue(ArmSubsystem.backWall)

        depositingGamepad.leftTrigger.conditionalBindState()
            .greaterThan(0.1).bind()
            .onTrue(ArmSubsystem.maxBack)
        depositingGamepad.leftBumper
            .onTrue(ArmSubsystem.highBar)
        depositingGamepad.rightTrigger.conditionalBindState()
            .greaterThan(0.1).bind()
            .onTrue(ArmSubsystem.maxFront)
        depositingGamepad.rightBumper
            .onTrue(ArmSubsystem.lowBar)
        depositingGamepad.leftTrigger.conditionalBindState()
            .greaterThan(.1).bind()
            .and(
                depositingGamepad.rightTrigger.conditionalBindState()
                    .greaterThan(0.1).bind()
            )
            .onTrue(ArmSubsystem.basket)

        defaultGamepad.rightBumper
            .onTrue(ArmSubsystem.openClaw)
        defaultGamepad.leftBumper
            .onTrue(ArmSubsystem.closeClaw)
    }

    override fun loop() {
        t+=gamepad2.right_stick_y.toDouble()
        DrivetrainSubsystem.setDrivePower(
            gamepad1.left_stick_x.toDouble(),
            -gamepad1.left_stick_y.toDouble(),
            gamepad1.right_stick_x.toDouble()
        )
        if(gamepad1.start || gamepad1.options) ArmSubsystem.resetLocation()

        var dashboard = FtcDashboard.getInstance()
        var dashboardTelemetry = dashboard.getTelemetry()

        dashboardTelemetry.update()

    }
}