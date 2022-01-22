package frc.robot.commands;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.PneumaticsSubsystem;

public class PneumaticsCommand extends CommandBase {
    PneumaticsSubsystem pneumaticsSubsystem;
    BooleanSupplier supplier;

    /** Creates a new PneumaticsCommand. */
    public PneumaticsCommand(PneumaticsSubsystem pneumaticsSubsystem, BooleanSupplier supplier) {
        this.pneumaticsSubsystem = pneumaticsSubsystem;
        this.supplier = supplier;
        addRequirements(pneumaticsSubsystem);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        pneumaticsSubsystem.setSoleonoid(supplier.getAsBoolean());
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
