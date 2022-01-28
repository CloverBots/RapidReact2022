package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.SequentialCommandGroupExtended;
import frc.robot.VisionTargetTracker;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

public class AutonomousLM extends SequentialCommandGroupExtended {
    /** Creates a new AutonomousLM. */
    public AutonomousLM(DriveSubsystem driveSubsystem, IntakeSubsystem intakeSubsystem,
        // FeederSubsystem feederSubsystem,
        ShooterSubsystem shooterSubsystem,
        VisionTargetTracker visionTargetTracker) {
        
        // Autonomous commands in running order
        addInstant(() -> intakeSubsystem.startIntake(0.7), intakeSubsystem);
        // addInstant(() -> driveSubsystem.distanceDrive(30.0), driveSubsystem);
        addCommands(new AimHighCommand(driveSubsystem, visionTargetTracker));
        // addCommands(new ShooterCommand(shooterSubsystem, visionTargetTracker));
        addInstant(() -> intakeSubsystem.stop(), intakeSubsystem);

        /**
        addInstand : Adds an InstantCommand from the given procedure and subsystem requirements
        addCommands : Adds the given commands to the command group
        */
    }
}
