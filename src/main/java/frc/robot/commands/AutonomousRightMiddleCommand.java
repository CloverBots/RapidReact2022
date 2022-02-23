// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.SequentialCommandGroupExtended;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.VisionTargetTracker;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.LowerFeederSubsystem;
import frc.robot.subsystems.UpperFeederSubsystem;
import frc.robot.subsystems.IntakeDeploySubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

public class AutonomousRightMiddleCommand extends SequentialCommandGroupExtended {
    private final static double DRIVE_SPEED = 0.5;
    private final static double DRIVE_DISTANCE = 1;
    private final static double DRIVE_ROTATE = 0;

    public AutonomousRightMiddleCommand(DriveSubsystem driveSubsystem,
            IntakeSubsystem intakeSubsystem,
            IntakeDeploySubsystem intakeDeploySubsystem,
            LowerFeederSubsystem lowerFeederSubsystem,
            UpperFeederSubsystem upperFeederSubsystem,
            ShooterSubsystem shooterSubsystem,
            VisionTargetTracker visionTargetTracker) {

        addInstant(() -> intakeDeploySubsystem.setSolenoid(true), intakeDeploySubsystem);
        addInstant(() -> intakeSubsystem.startIntake(), intakeSubsystem);
        addCommands(new DriveToDistanceCommand(driveSubsystem, DRIVE_DISTANCE, DRIVE_SPEED, DRIVE_ROTATE));
        addCommands(new AlignHighCommand(driveSubsystem, visionTargetTracker));
        addCommands(new ShooterCommand(shooterSubsystem, visionTargetTracker));
        // addInstant(() -> feederSubsystem.startFeeder(), feederSubsystem);
        addCommands(new WaitCommand(3));
        // addInstant(() -> feederSubsystem.stopFeeder(), feederSubsystem);
        addInstant(() -> intakeSubsystem.stop(), intakeSubsystem);
        addInstant(() -> intakeDeploySubsystem.setSolenoid(false), intakeDeploySubsystem);

    }
}
