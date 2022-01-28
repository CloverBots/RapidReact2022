package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.TestTalonFXSubsystem;

public class TestTalonFXCommand extends CommandBase {
    private final TestTalonFXSubsystem testTalonFXSubsystem;

    public TestTalonFXCommand(TestTalonFXSubsystem testTalonFXSubsystem) {
        this.testTalonFXSubsystem = testTalonFXSubsystem;

        addRequirements(testTalonFXSubsystem);
    }

    @Override
    public void initialize() {
        SmartDashboard.putNumber("TalonFX RPM", 0);
    }

    @Override
    public void execute() {
        double rpm = SmartDashboard.getNumber("TalonFX RPM", 0);
        testTalonFXSubsystem.setRPM(rpm);
    }
}
