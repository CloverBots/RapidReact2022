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
  private static final double DISTANCE_TO_DRIVE = 0;
  private static final double INTAKE_SPEED = 0;
  private static final double SHOOTER_SPEED = 0;
  private static final double DRIVE_SPEED = 0;
  IntakeSubsystem intakeSubsystem;
  DriveSubsystem driveSubsystem;
  ShooterSubsystem shooterSubSystem;
  VisionTargetTracker visionTargetTracker;
  FeederSubsystem feederSubsystem;

  /** Creates a new AutonomousRightRight. */
  public AutonomousRightRight() {
    // Add your commands in the addCommands() call, e.g.
    addCommands(new DriveToDistance(driveSubsystem, DISTANCE_TO_DRIVE, DRIVE_SPEED));
    
    addInstant(() -> intakeSubsystem.startIntake(INTAKE_SPEED), intakeSubsystem);
    // or
    // addCommands(new IntakeCommand(intakeSubsystem, forwardSpeed, reverseSpeed));
    
    addCommands(new AimHighCommand(driveSubsystem, visionTargetTracker));
    
    addInstant(() -> shooterSubSystem.startShooter(SHOOTER_SPEED), shooterSubSystem);
    // or
    // addCommands(new ShooterCommand(shooterSubSystem, visionTargetTracker));
    
    addCommands(new FeederCommand(feederSubsystem));
    addCommands(new WaitCommand(3));
  }
}
