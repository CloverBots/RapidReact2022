package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.TestFalconSubsystem;

public class TestFalconCommand extends CommandBase {
    private final TestFalconSubsystem falconTestSubsystem;

    public TestFalconCommand(TestFalconSubsystem falconTestSubsystem) {
        this.falconTestSubsystem = falconTestSubsystem;

        addRequirements(falconTestSubsystem);
    }

    @Override
    public void initialize() {
        SmartDashboard.putNumber("Falcon RPM", 0);
    }

    @Override
    public void execute() {
        double rpm = SmartDashboard.getNumber("Falcon RPM", 0);
        falconTestSubsystem.setRPM(rpm);
    }
}
