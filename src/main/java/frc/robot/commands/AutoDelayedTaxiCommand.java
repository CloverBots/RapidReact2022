package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.SequentialCommandGroupExtended;
import frc.robot.subsystems.DriveSubsystem;

public class AutoDelayedTaxiCommand extends SequentialCommandGroupExtended {
    private final static double DRIVE_SPEED = 0.5;
    private final static double DRIVE_DISTANCE = 1;
    private final static String SMART_DASHBOARD_AUTO_WAIT_TIME = "AutoWaitTime"; 

    /** Creates a new AutonomousDelayedDriveStraightCommand. */
    public AutoDelayedTaxiCommand(DriveSubsystem driveSubsystem) {

        // Autonomous commands in running order
        SmartDashboard.putNumber(SMART_DASHBOARD_AUTO_WAIT_TIME, 0);
        SmartDashboard.putNumber("Distance", 1);

        addCommands(new SmartDashboardWaitCommand(SMART_DASHBOARD_AUTO_WAIT_TIME));
        addCommands(new DriveToDistanceCommand(driveSubsystem, SmartDashboard.getNumber("Distance", 0) , DRIVE_SPEED, 0));
    }
}
