// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.FeederSubsystem;

public class FeederCommand extends CommandBase {

    private static final double FEEDER_RPM = 10; // TODO Find feeder speed

    private final FeederSubsystem feederSubsystem;

    /** Creates a new ShootBall. */
    public FeederCommand(FeederSubsystem feederSubsystem) {
        this.feederSubsystem = feederSubsystem;

        addRequirements(feederSubsystem);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        feederSubsystem.startFeeder(FEEDER_RPM);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        feederSubsystem.stopFeeder();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
