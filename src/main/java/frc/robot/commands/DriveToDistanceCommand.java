// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;

public class DriveToDistanceCommand extends CommandBase {
    private final DriveSubsystem driveSubsystem;
    private final double distance;
    private final double maxSpeed;
    private double intitialEncoderPosition;
    private double rotate;

    /**
     * Creates a new DriveToDistance.
     * 
     * @param driveSubsystem what subsystem to use
     * @param distance       distance driven forward in meters
     * @param maxSpeed          set the maximum speed
     * @param rotate         how much to roatate in degrees
     */

    public DriveToDistanceCommand(DriveSubsystem driveSubsystem, double distance, double maxSpeed, double rotate) {
        this.distance = distance;
        this.maxSpeed = maxSpeed;
        this.driveSubsystem = driveSubsystem;
        this.rotate = rotate;
        addRequirements(driveSubsystem);
        // Use addRequirements() here to declare subsystem dependencies.
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        // fetch the initial encoder position in order to calculate the distance
        // traveled
        intitialEncoderPosition = driveSubsystem.getAverageEncoderPosition();
        driveSubsystem.driveStraightPidController.setSetpoint(distance);
        driveSubsystem.driveRotatePidController.setSetpoint(rotate);
        // Tolerance within 3 cm
        driveSubsystem.driveStraightPidController.setTolerance(0.03);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // Begin the ramp down process when the robot is within 50 centimeters of the
        // target distance
        double currentEncoderPosition = driveSubsystem.getAverageEncoderPosition();
        double distanceTraveled = Math.abs(currentEncoderPosition - intitialEncoderPosition);
        double rotateSpeed = driveSubsystem.driveRotatePidController.calculate(driveSubsystem.navXGyro.getHeading());
        double drivingSpeed = driveSubsystem.driveStraightPidController.calculate(distanceTraveled);
        drivingSpeed = Math.max(Math.min(drivingSpeed, -maxSpeed), maxSpeed);
        driveSubsystem.autoDrive(drivingSpeed, rotateSpeed);
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
        // end the command when the difference between the desired distance and the
        // actual distance is within a certain threshold calculated by the PID controller
        return driveSubsystem.driveStraightPidController.atSetpoint();
    }
}
