package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.IntakeSubsystem;

public class IntakeCommand extends CommandBase {
    private final IntakeSubsystem intakeSubsystem;
    private final FeederSubsystem feederSubsystem;
    private final double speed;

    /** Creates a new IntakeCommand. */
    public IntakeCommand(IntakeSubsystem intakeSubsystem, FeederSubsystem feederSubsystem, double speed) {
        this.intakeSubsystem = intakeSubsystem;
        this.feederSubsystem = feederSubsystem;
        this.speed = speed;
        addRequirements(intakeSubsystem);
        addRequirements(feederSubsystem);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        intakeSubsystem.startIntake(speed);
        feederSubsystem.runUntilLowerSensor(1);
        feederSubsystem.runUntilUpperSensor(1);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {

    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return true;
    }
}