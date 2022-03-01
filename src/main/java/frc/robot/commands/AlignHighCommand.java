// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.VisionTargetTracker;
import frc.robot.VisionTargetTracker.LedMode;
import frc.robot.subsystems.DriveSubsystem;

public class AlignHighCommand extends CommandBase {
    private static final double MAX_OUTPUT = .2;

    private final DriveSubsystem driveSubsystem;
    private final VisionTargetTracker visionTargetTracker;
    private final DoubleSupplier foward;

    /** Creates a new AimRobotForHigh. */
    public AlignHighCommand(DriveSubsystem driveSubsystem, DoubleSupplier forward, VisionTargetTracker visionTargetTracker) {

        this.driveSubsystem = driveSubsystem;
        this.visionTargetTracker = visionTargetTracker;
        this.foward = forward;

        // Use addRequirements() here to declare subsystem dependencies.
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
        
        double rotation = Math.min(MAX_OUTPUT, Math.max(-MAX_OUTPUT, driveSubsystem.calculateLimePIDOutput(xOffset)));
        double forwardSpeed = foward.getAsDouble();
        driveSubsystem.autoDrive(-computeInputCurve(forwardSpeed, 2) * .5, rotation);
    }

    private double computeInputCurve(double rawInput, double power) {
        var sign = rawInput < 0 ? 1 : -1;
        return Math.pow(Math.abs(rawInput), power) * sign;
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
