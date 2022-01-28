// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;

public class DriveToDistance extends CommandBase {
  private final DriveSubsystem driveSubsystem;
  private final double distance;
  private final double speed;
  private double intitialEncoderPosition;

  /**
   * Creates a new DriveToDistance.
   * 
   * @param driveSubsystem what subsystem to use
   * @param distance       distance driven forward in meters
   * @param speed          percentage of motor power (0-1) | Note: Keep speed low
   *                       until a ramp down system is implemented
   */

  public DriveToDistance(DriveSubsystem driveSubsystem, double distance, double speed) {
    this.distance = distance;
    this.speed = speed;
    this.driveSubsystem = driveSubsystem;
    addRequirements(driveSubsystem);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    // fetch the initial encoder position in order to calculate the distance
    // traveled
    intitialEncoderPosition = driveSubsystem.getEncoderPosition();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    driveSubsystem.autoDrive(speed, 0); 
    // TODO add a ramp down (probably via PID) in order to keep the robot from
    // breaking too hard
    // Also add a PID rotation control to avoid turning
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    // Stop the robot from driving forward when the robot has reached the target
    // position
    driveSubsystem.autoDrive(0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // get the current encoder position- used to get the total ticks traveled from
    // the start of the command
    double currentEncoderPosition = driveSubsystem.getEncoderPosition(); // TODO get both encoder positions and average
                                                                         // rather than just the left
    // end the command when the difference between the desired distance and the
    // actual distance is within a certain threshold (100 for now)
    return ((Math.abs(currentEncoderPosition - intitialEncoderPosition) > distance - 0.1));
  }
}
