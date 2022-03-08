package frc.robot.commands;

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
    private final static double DRIVE_DISTANCE_ONE = 1.8;
    private final static double DRIVE_DISTANCE_TWO = 1.3;
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
        
        //   Autonomous commands in running order
        addCommands(new DriveToDistanceCommand(driveSubsystem, DRIVE_DISTANCE_ONE, DRIVE_SPEED, DRIVE_ROTATE));
        // addCommands(new AlignHighCommand(driveSubsystem, visionTargetTracker));
        // addCommands(new SpinShooterHighCommand(shooterSubsystem, visionTargetTracker));
        addInstant(() -> shooterSubsystem.setAutoHighGoalRPM(), shooterSubsystem);
        addCommands(new WaitCommand(1));
        addInstant(() -> lowerFeederSubsystem.setSpeed(1), lowerFeederSubsystem);
        addInstant(()-> upperFeederSubsystem.setSpeed(1), upperFeederSubsystem);
        addCommands(new WaitCommand(3));
        addInstant(() -> lowerFeederSubsystem.setSpeed(0), lowerFeederSubsystem);
        addInstant(()-> upperFeederSubsystem.setSpeed(0), upperFeederSubsystem);
        addInstant(()-> shooterSubsystem.setShooterRPM(0), shooterSubsystem);
        addCommands(new SmartDashboardWaitCommand(SMART_DASHBOARD_AUTO_WAIT_TIME));
        addCommands(new DriveToDistanceCommand(driveSubsystem, DRIVE_DISTANCE_TWO, DRIVE_SPEED, DRIVE_ROTATE));
    }
}
