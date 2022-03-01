// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.SequentialCommandGroupExtended;
import frc.robot.VisionTargetTracker;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeDeploySubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.LowerFeederSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.UpperFeederSubsystem;

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
        // addCommands(new AlignHighCommand(driveSubsystem, visionTargetTracker));
        addCommands(new SpinShooterHighCommand(shooterSubsystem, visionTargetTracker));
        addInstant(() -> lowerFeederSubsystem.setSpeed(1), lowerFeederSubsystem);
        addInstant(() -> upperFeederSubsystem.setSpeed(1), upperFeederSubsystem);
        addCommands(new WaitCommand(3));
        addInstant(() -> lowerFeederSubsystem.setSpeed(0), lowerFeederSubsystem);
        addInstant(() -> upperFeederSubsystem.setSpeed(0), upperFeederSubsystem);
        addInstant(() -> intakeSubsystem.stop(), intakeSubsystem);
        addInstant(() -> intakeDeploySubsystem.setSolenoid(false), intakeDeploySubsystem);

    }
}
