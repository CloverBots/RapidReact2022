package frc.robot.commands;

import frc.robot.SequentialCommandGroupExtended;
import frc.robot.subsystems.DriveSubsystem;

public class AutoDelayedTaxiCommand extends SequentialCommandGroupExtended {
    private final static double DRIVE_SPEED = 0.5;
    private final static double DRIVE_DISTANCE = 2.5;
    private final static String SMART_DASHBOARD_AUTO_WAIT_TIME = "AutoWaitTime";

    /** Creates a new AutonomousDelayedDriveStraightCommand. */
    public AutoDelayedTaxiCommand(DriveSubsystem driveSubsystem) {

        // Autonomous commands in running order

        addCommands(new SmartDashboardWaitCommand(SMART_DASHBOARD_AUTO_WAIT_TIME));
        addCommands(new DriveToDistanceCommand(driveSubsystem, DRIVE_DISTANCE, DRIVE_SPEED, 0, 0.03));
    }
}
