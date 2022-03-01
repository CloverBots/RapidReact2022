package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.SequentialCommandGroupExtended;
import frc.robot.VisionTargetTracker;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeDeploySubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.LowerFeederSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.UpperFeederSubsystem;

public class AutoHighGoalTaxiCommand extends SequentialCommandGroupExtended {
    private final static double DRIVE_SPEED = 0.5;
    private final static double DRIVE_DISTANCE_ONE = 1;
    private final static double DRIVE_DISTANCE_TWO = 1;
    private final static double DRIVE_ROTATE = 0;
    private final static String SMART_DASHBOARD_AUTO_WAIT_TIME = "AutoWaitTime"; 

    /** Creates a new AutonomousLM. */
    public AutoHighGoalTaxiCommand(
        DriveSubsystem driveSubsystem,
        IntakeSubsystem intakeSubsystem, 
        IntakeDeploySubsystem intakeDeploySubsystem,
        LowerFeederSubsystem lowerFeederSubsystem,
        UpperFeederSubsystem upperFeederSubsystem,
        ShooterSubsystem shooterSubsystem,
        VisionTargetTracker visionTargetTracker) {

        SmartDashboard.putNumber(SMART_DASHBOARD_AUTO_WAIT_TIME, 0);
        SmartDashboard.putNumber("Distance one", 1);
        SmartDashboard.putNumber("Distance two", 1);
        
        //   Autonomous commands in running order
        addCommands(new DriveToDistanceCommand(driveSubsystem, SmartDashboard.getNumber("Distance one", 0), DRIVE_SPEED, DRIVE_ROTATE));
        addCommands(new AlignHighCommand(driveSubsystem, visionTargetTracker));
        addCommands(new SpinShooterHighCommand(shooterSubsystem, visionTargetTracker));
        addCommands(new WaitCommand(1));
        addInstant(() -> lowerFeederSubsystem.setSpeed(1), lowerFeederSubsystem);
        addInstant(()-> upperFeederSubsystem.setSpeed(1), upperFeederSubsystem);
        addCommands(new WaitCommand(3));
        addInstant(() -> lowerFeederSubsystem.setSpeed(0), lowerFeederSubsystem);
        addInstant(()-> upperFeederSubsystem.setSpeed(0), upperFeederSubsystem);
        addInstant(()-> shooterSubsystem.setShooterRPM(0), shooterSubsystem);
        addCommands(new SmartDashboardWaitCommand(SMART_DASHBOARD_AUTO_WAIT_TIME));
        addCommands(new DriveToDistanceCommand(driveSubsystem, SmartDashboard.getNumber("Distance two", 0), DRIVE_SPEED, DRIVE_ROTATE));
    }
}
