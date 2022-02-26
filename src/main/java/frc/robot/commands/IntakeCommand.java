package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.LowerFeederSubsystem;

public class IntakeCommand extends CommandBase {
    public enum IntakeConfig {
        ONE_BALL,
        TWO_BALLS
    }
    private final IntakeSubsystem intakeSubsystem;
    private final LowerFeederSubsystem lowerFeederSubsystem;
    private final double feederSpeed;
    private final DoubleSupplier intakeSpeed;

    /** Creates a new IntakeCommand. */
    public IntakeCommand(IntakeSubsystem intakeSubsystem, LowerFeederSubsystem lowerFeederSubsystem, double feederSpeed, DoubleSupplier intakeSpeed) {
        this.intakeSubsystem = intakeSubsystem;
        this.lowerFeederSubsystem = lowerFeederSubsystem;
        this.feederSpeed = feederSpeed;
        this.intakeSpeed = intakeSpeed;
        addRequirements(intakeSubsystem);
        addRequirements(lowerFeederSubsystem);
        // SmartDashboard.putNumber("Intake Speed", 0);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // double testSpeed = SmartDashboard.getNumber("Intake Speed", 0);
        intakeSubsystem.startIntake(intakeSpeed.getAsDouble());
        // feederSubsystem.loadLower(1); //TODO: determine proper value
        // feederSubsystem.loadUpper(1);
        
        lowerFeederSubsystem.setSpeed(feederSpeed);
        // feederSubsystem.setUpperFeederSpeed(testSpeed);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        intakeSubsystem.stop();
        lowerFeederSubsystem.setSpeed(0);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
