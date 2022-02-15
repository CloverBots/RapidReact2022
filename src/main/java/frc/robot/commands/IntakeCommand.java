package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class IntakeCommand extends CommandBase {
    public enum IntakeConfig {
        ONE_BALL,
        TWO_BALLS
    }
    private final IntakeSubsystem intakeSubsystem;
    private final FeederSubsystem feederSubsystem;
    private final IntakeConfig intakeConfig;
    private final double speed;

    /** Creates a new IntakeCommand. */
    public IntakeCommand(IntakeSubsystem intakeSubsystem, FeederSubsystem feederSubsystem, IntakeConfig intakeConfig, double speed) {
        this.intakeSubsystem = intakeSubsystem;
        this.feederSubsystem = feederSubsystem;
        this.intakeConfig = intakeConfig;
        this.speed = speed;
        addRequirements(intakeSubsystem);
        addRequirements(feederSubsystem);
        SmartDashboard.putNumber("Intake Speed", 0);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        double testSpeed = SmartDashboard.getNumber("Intake Speed", 0);
        intakeSubsystem.startIntake(testSpeed);
        // feederSubsystem.loadLower(1); //TODO: determine proper value
        // feederSubsystem.loadUpper(1);
        
        feederSubsystem.setLowerFeederSpeed(testSpeed);
        feederSubsystem.setUpperFeederSpeed(testSpeed);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        intakeSubsystem.stop();
        feederSubsystem.setLowerFeederSpeed(0);
        feederSubsystem.setUpperFeederSpeed(0);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        // TODO: fix this when sensors are actually on the robot or dont
        return false;/*
        switch (intakeConfig) {
            case ONE_BALL: 
                return feederSubsystem.getUpperSensor();
            case TWO_BALLS:
                return feederSubsystem.getUpperSensor() && feederSubsystem.getLowerSensor();
            default: 
                System.err.println("Unknown intake configuration " + intakeConfig);
                return true;       
        }*/
    }
}
