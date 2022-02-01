package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.SequentialCommandGroupExtended;
import frc.robot.subsystems.DriveSubsystem;

public class AutonomousDelayedDriveStraightCommand extends SequentialCommandGroupExtended {
    private final static double DRIVE_SPEED = 0.5;
    private final static double DRIVE_DISTANCE = 1;

    /** Creates a new AutonomousDelayedDriveStraightCommand. */
    public AutonomousDelayedDriveStraightCommand(DriveSubsystem driveSubsystem) {

        // Autonomous commands in running order
        SmartDashboard.putNumber("AutoWaitTime", 0);

        addCommands(new SmartDashboardWaitCommand("AutoWaitTime"));
        addCommands(new DriveToDistanceCommand(driveSubsystem, DRIVE_DISTANCE, DRIVE_SPEED, 0));
    }
}
