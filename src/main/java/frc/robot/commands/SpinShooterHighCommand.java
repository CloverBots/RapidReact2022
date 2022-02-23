// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.VisionTargetTracker;
import frc.robot.subsystems.ShooterSubsystem;

public class SpinShooterHighCommand extends CommandBase {
    private final ShooterSubsystem shooterSubsystem;
    private final VisionTargetTracker visionTargetTracker;

    /** Creates a new SpinShooterHighCommand. */
    public SpinShooterHighCommand(ShooterSubsystem shooterSubsystem, VisionTargetTracker visionTargetTracker) {
        this.shooterSubsystem = shooterSubsystem;
        this.visionTargetTracker = visionTargetTracker;

        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(shooterSubsystem);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        double targetDistance = visionTargetTracker.computeTargetDistance();
        shooterSubsystem.setShooterRPM(targetDistance * 10);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
