// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;

public class DriveToDistance extends CommandBase {
  private final DriveSubsystem driveSubsystem;
  private double intitialEncoderPosition;
  private double driveTicks;
  private double distance; 
  private double speed;

  /**Creates a new DriveToDistance.
   * 
   * @param driveSubsystem what subsystem to use
   * @param distance distance driven forward in meters
   * @param speed percentage of motor power (0-1) | Note: Keep speed low until a ramp down system is implemented
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
    // fetch the initial encoder position in order to calculate the distance traveled
    intitialEncoderPosition = driveSubsystem.getEncoderPosition();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    //calculate the amount distance in meters
    // double distancePerRotation = 10*DriveSubsystem.ENCODER_POSITION_CONVERSION_FACTOR;
    // double distancePerTick = distancePerRotation/ DriveSubsystem.ENCODER_TICKS_PER_ROTATION;
    // driveTicks = distance/distancePerTick;
    driveSubsystem.autoDrive(speed, 0); //TODO add a ramp down in order to keep the robot from breaking too hard

    double currentEncoderPosition = driveSubsystem.getEncoderPosition();
    System.out.println("Drive ticks: " + driveTicks);
    System.out.println("Current: " + currentEncoderPosition);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    driveSubsystem.autoDrive(0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // get the current encoder position- used to get the total ticks traveled from the start of the command
    double currentEncoderPosition = driveSubsystem.getEncoderPosition();
    // end the command when the difference between the desired distance and the actual distance is within a certain threshold (100 for now)
    return ((Math.abs(currentEncoderPosition-intitialEncoderPosition) > distance - 0.1));
  }
}
