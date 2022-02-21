// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.LiftSubsystem;

public class LiftCommand extends CommandBase {
    private final LiftSubsystem liftSubsystem;
    private final DoubleSupplier trigger;
    private final DoubleSupplier leftJoystickY;

    /** Creates a new LiftCommand. */
    public LiftCommand(LiftSubsystem liftSubsystem, DoubleSupplier trigger, DoubleSupplier leftJoystickY) {
        this.liftSubsystem = liftSubsystem;
        this.trigger = trigger;
        this.leftJoystickY = leftJoystickY;

        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(liftSubsystem);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (trigger.getAsDouble() > .5) {
            liftSubsystem.setLiftSpeed(leftJoystickY.getAsDouble());
        } else {
            liftSubsystem.setLiftSpeed(0);
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        liftSubsystem.setLiftSpeed(0);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
