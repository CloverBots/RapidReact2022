package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.SequentialCommandGroupExtended;
import frc.robot.VisionTargetTracker;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.IntakeDeploySubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;


public class AutonomousLeftMiddleCommand extends SequentialCommandGroupExtended {
    private final static double DRIVE_SPEED = 0.5;
    private final static double DRIVE_DISTANCE = 1;

    /** Creates a new AutonomousLM. */
    public AutonomousLeftMiddleCommand(DriveSubsystem driveSubsystem,
        IntakeSubsystem intakeSubsystem, 
        IntakeDeploySubsystem intakeDeploySubsystem,
        FeederSubsystem feederSubsystem,
        ShooterSubsystem shooterSubsystem,
        VisionTargetTracker visionTargetTracker) {
        
        //   Autonomous commands in running order
        addInstant(() -> intakeDeploySubsystem.setSolenoid(true), intakeDeploySubsystem); 
        addInstant(() -> intakeSubsystem.startIntake(), intakeSubsystem);
        addCommands(new DriveToDistanceCommand(driveSubsystem, DRIVE_DISTANCE, DRIVE_SPEED));
        addCommands(new AimHighCommand(driveSubsystem, visionTargetTracker));
        addCommands(new ShooterCommand(shooterSubsystem, visionTargetTracker));
        addInstant(() -> feederSubsystem.startFeeder(), feederSubsystem);
        addCommands(new WaitCommand(3));
        addInstant(() -> feederSubsystem.stopFeeder(), feederSubsystem);
        addInstant(() -> intakeSubsystem.stop(), intakeSubsystem);
        addInstant(() -> intakeDeploySubsystem.setSolenoid(false), intakeDeploySubsystem);
    }
}