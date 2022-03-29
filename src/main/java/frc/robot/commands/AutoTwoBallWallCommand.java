// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.SequentialCommandGroupExtended;
import frc.robot.VisionTargetTracker;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.LowerFeederSubsystem;
import frc.robot.subsystems.UpperFeederSubsystem;
import frc.robot.subsystems.IntakeDeploySubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class AutoTwoBallWallCommand extends SequentialCommandGroupExtended {
  private final static double DRIVE_SPEED = 0.25;
  private final static double DRIVE_DISTANCE = 1.35;
  private final static double DRIVE_ROTATE = 0;
  private static final String AUTO_FEEDER_SPEED = "Auto feeder speed";

  /** Creates a new AutoTwoBallWallCommand. */
  public AutoTwoBallWallCommand(
    DriveSubsystem driveSubsystem,
    IntakeSubsystem intakeSubsystem, 
    IntakeDeploySubsystem intakeDeploySubsystem,
    LowerFeederSubsystem lowerFeederSubsystem,
    UpperFeederSubsystem upperFeederSubsystem,
    ShooterSubsystem shooterSubsystem,
    VisionTargetTracker visionTargetTracker) {
    
    //   Autonomous commands in running order
    addInstant(() -> intakeDeploySubsystem.setSolenoid(true), intakeDeploySubsystem);
    addCommands(new WaitCommand(1));
    addInstant(() -> intakeSubsystem.startIntake(), intakeSubsystem);
    addInstant(() -> lowerFeederSubsystem.setSpeed(0.5), lowerFeederSubsystem);
    addCommands(new DriveToDistanceCommand(driveSubsystem, DRIVE_DISTANCE, DRIVE_SPEED, DRIVE_ROTATE, 0.08));
    addCommands(new WaitCommand(1));
    addCommands(new DriveToDistanceCommand(driveSubsystem, -1.06, DRIVE_SPEED, DRIVE_ROTATE, 0.08));
    addCommands(new AutoAlignHighCommand(driveSubsystem, visionTargetTracker, 1));
    addInstant(() -> shooterSubsystem.setShooterRPM(4000));
    addCommands(new WaitCommand(1));
    addInstant(() -> lowerFeederSubsystem.setSpeed(SmartDashboard.getNumber(AUTO_FEEDER_SPEED, 1)), lowerFeederSubsystem);
    addInstant(() -> upperFeederSubsystem.setSpeed(SmartDashboard.getNumber(AUTO_FEEDER_SPEED, 1)), upperFeederSubsystem);
    addCommands(new WaitCommand(3));
    addInstant(() -> lowerFeederSubsystem.setSpeed(0), lowerFeederSubsystem);
    addInstant(() -> upperFeederSubsystem.setSpeed(0), upperFeederSubsystem);
    addInstant(() -> intakeSubsystem.stop(), intakeSubsystem);
    addInstant(() -> intakeDeploySubsystem.setSolenoid(false), intakeDeploySubsystem);
    addInstant(() -> shooterSubsystem.setShooterRPM(0));
    addCommands(new DriveToDistanceCommand(driveSubsystem, 1.1, DRIVE_SPEED, DRIVE_ROTATE, 0.08));
  }
}
