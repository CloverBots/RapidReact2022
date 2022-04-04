package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.SequentialCommandGroupExtended;
import frc.robot.VisionTargetTracker;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.LowerFeederSubsystem;
import frc.robot.subsystems.UpperFeederSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

public class AutoOneBallHighCommand extends SequentialCommandGroupExtended {
    private final static double DRIVE_SPEED = 0.25;
    private final static double DRIVE_DISTANCE = 0.3;
    private final static double DRIVE_ROTATE = 0;
    private static final String AUTO_FEEDER_SPEED = "Auto feeder speed";

    /** Creates a new AutonomousLM. */
    public AutoOneBallHighCommand(
        DriveSubsystem driveSubsystem,
        LowerFeederSubsystem lowerFeederSubsystem,
        UpperFeederSubsystem upperFeederSubsystem,
        ShooterSubsystem shooterSubsystem,
        VisionTargetTracker visionTargetTracker) {
        
        //   Autonomous commands in running order
        addCommands(new DriveToDistanceCommand(driveSubsystem, DRIVE_DISTANCE, DRIVE_SPEED, DRIVE_ROTATE, 0.08));
        addCommands(new WaitCommand(1));
        addCommands(new AutoAlignHighCommand(driveSubsystem, visionTargetTracker, 1));
        addInstant(() -> shooterSubsystem.setShooterRPM(4120));
        addCommands(new WaitCommand(1));
        addInstant(() -> lowerFeederSubsystem.setSpeed(SmartDashboard.getNumber(AUTO_FEEDER_SPEED, 1)), lowerFeederSubsystem);
        addInstant(() -> upperFeederSubsystem.setSpeed(SmartDashboard.getNumber(AUTO_FEEDER_SPEED, 1)), upperFeederSubsystem);
        addCommands(new WaitCommand(3));
        addInstant(() -> lowerFeederSubsystem.setSpeed(0), lowerFeederSubsystem);
        addInstant(() -> upperFeederSubsystem.setSpeed(0), upperFeederSubsystem);
        addInstant(() -> shooterSubsystem.setShooterRPM(0));
        addCommands(new DriveToDistanceCommand(driveSubsystem, 1.15, DRIVE_SPEED, DRIVE_ROTATE, 0.08));
    }
}
