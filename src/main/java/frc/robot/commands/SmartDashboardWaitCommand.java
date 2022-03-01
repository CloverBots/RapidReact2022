// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SmartDashboardWaitCommand extends CommandBase {
    private Timer timer = new Timer();
    private final double duration;

    /**
     * Creates a new WaitCommand. This command will do nothing, and end after the
     * specified duration.
     *
     * @param seconds the time to wait, in seconds
     */
    public SmartDashboardWaitCommand(String name) {
        duration = Math.min(Math.abs(SmartDashboard.getNumber(name, 0)), 8);
    }

    @Override
    public void initialize() {
        timer.reset();
        timer.start();
    }

    @Override
    public void end(boolean interrupted) {
        timer.stop();
    }

    @Override
    public boolean isFinished() {
        return timer.hasElapsed(duration);
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }
}
