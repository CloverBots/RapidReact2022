// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.VisionTargetTracker;
import frc.robot.VisionTargetTracker.LedMode;
import frc.robot.subsystems.DriveSubsystem;

public class AutoAlignHighCommand extends CommandBase {
    private static final double MAX_OUTPUT = .2;

    private final DriveSubsystem driveSubsystem;
    private final VisionTargetTracker visionTargetTracker;
    private final double timeoutInSeconds;
    private Timer timer = new Timer();

    /** Creates a new AimRobotForHigh. */
    public AutoAlignHighCommand(DriveSubsystem driveSubsystem, VisionTargetTracker visionTargetTracker, double timeoutInSeconds) {

        this.driveSubsystem = driveSubsystem;
        this.visionTargetTracker = visionTargetTracker;
        this.timeoutInSeconds = timeoutInSeconds;

        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(driveSubsystem);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        visionTargetTracker.setLedMode(LedMode.FORCE_ON);
        timer.reset();
        timer.start();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        double xOffset = visionTargetTracker.getX();
        
        double rotation = Math.min(MAX_OUTPUT, Math.max(-MAX_OUTPUT, driveSubsystem.calculateLimePIDOutput(xOffset)));
        driveSubsystem.autoDrive(0, rotation);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        visionTargetTracker.setLedMode(LedMode.FORCE_OFF);
        driveSubsystem.autoDrive(0, 0);
        timer.stop();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return timer.hasElapsed(timeoutInSeconds);
    }
}
