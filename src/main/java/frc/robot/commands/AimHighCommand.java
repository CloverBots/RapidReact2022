// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.VisionTargetTracker;
import frc.robot.VisionTargetTracker.LedMode;
import frc.robot.subsystems.DriveSubsystem;

public class AimHighCommand extends CommandBase {
    private static final double MAX_OUTPUT = .05;
    //public final ShooterSubsystem shooterSubsystem;
    public final DriveSubsystem driveSubsystem;
    public final VisionTargetTracker visionTargetTracker;

    /** Creates a new AimRobotForHigh. */
    public AimHighCommand(DriveSubsystem driveSubsystem, VisionTargetTracker visionTargetTracker) {
        //this.shooterSubsystem = shooterSubsystem;
        this.driveSubsystem = driveSubsystem;
        this.visionTargetTracker = visionTargetTracker;

        // Use addRequirements() here to declare subsystem dependencies.
        //addRequirements(shooterSubsystem);
        addRequirements(driveSubsystem);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        visionTargetTracker.setLedMode(LedMode.FORCE_ON);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        double xOffset = visionTargetTracker.getX();
        double rotation = Math.min(MAX_OUTPUT, Math.max(-MAX_OUTPUT, xOffset*.01));
        driveSubsystem.autoDrive(0, rotation);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        visionTargetTracker.setLedMode(LedMode.FORCE_OFF);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
