package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IntakeSubsystem;

public class IntakeCommand extends CommandBase {
    private final IntakeSubsystem intakeSubsystem;
    private final DoubleSupplier forwardSpeed;
    private final DoubleSupplier reverseSpeed;

    /** Creates a new ShootBall. */
    public IntakeCommand(IntakeSubsystem intakeSubsystem, DoubleSupplier forwardSpeed, DoubleSupplier reverseSpeed) {
        this.intakeSubsystem = intakeSubsystem;
        this.forwardSpeed = forwardSpeed;
        this.reverseSpeed = reverseSpeed;
        addRequirements(intakeSubsystem);

        // Use addRequirements() here to declare subsystem dependencies.
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        intakeSubsystem.startIntake(forwardSpeed.getAsDouble()-reverseSpeed.getAsDouble());
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        intakeSubsystem.stop();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}