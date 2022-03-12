
package frc.robot.commands;

import java.util.function.DoubleSupplier;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.LiftHookSubsystem;

public class LiftHookCommand extends CommandBase {
  private final LiftHookSubsystem liftHookSubsystem;
  private final DoubleSupplier trigger;

    public LiftHookCommand(LiftHookSubsystem liftHookSubsystem, DoubleSupplier trigger) {
        this.liftHookSubsystem = liftHookSubsystem;
        this.trigger = trigger;

        addRequirements(liftHookSubsystem);
        }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if(trigger.getAsDouble() > .5) {
            liftHookSubsystem.invertLift();
        }
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

