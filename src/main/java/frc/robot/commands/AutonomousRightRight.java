// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.SequentialCommandGroupExtended;
import frc.robot.VisionTargetTracker;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class AutonomousRightRight extends SequentialCommandGroupExtended {
  private static final double DISTANCE_TO_DRIVE = 5;
  private static final double speed = 1;
  IntakeSubsystem intakeSubsystem;
  DriveSubsystem driveSubsystem;
  AimSubsystem aimSubsystem;
  ShooterSubsystem shooterSubSystem;
  FeederSubSystem feederSubSystem;
  VisionTargetTracker visionTargetTracker;
  FeederSubsystem feederSubsystem;

  /** Creates a new AutonomousRightRight. */
  public AutonomousRightRight() {
    // Add your commands in the addCommands() call, e.g.
    addInstant(() -> driveSubsystem.autoDriveDistance(DISTANCE_TO_DRIVE), driveSubsystem);
    addInstant(() -> intakeSubsystem.startIntake(speed), intakeSubsystem);
    addCommands(new AimHighCommand(driveSubsystem, visionTargetTracker));
    addInstant(() -> shooterSubSystem.startShooter(speed), shooterSubSystem);
    addCommands(new FeederCommand(feederSubsystem));
    addCommands(new WaitCommand(3));
  }
}
