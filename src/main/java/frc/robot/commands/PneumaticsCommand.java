package frc.robot.commands;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.PneumaticsSubsystem;

public class PneumaticsCommand extends CommandBase {
    PneumaticsSubsystem pneumaticsSubsystem;
    BooleanSupplier position;

    /** Creates a new PneumaticsCommand. */
    public PneumaticsCommand(PneumaticsSubsystem pneumaticsSubsystem, BooleanSupplier position) {
        this.pneumaticsSubsystem = pneumaticsSubsystem;
        this.position = position;
        addRequirements(pneumaticsSubsystem);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        pneumaticsSubsystem.setSolenoid(position.getAsBoolean());
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
