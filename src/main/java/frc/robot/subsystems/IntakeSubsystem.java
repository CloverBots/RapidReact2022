package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Ids;

public class IntakeSubsystem extends SubsystemBase {

    private final double INTAKE_DEFAULT_SPEED = 10.0;
    private final IntakeDeploySubsystem intakeDeploySubsystem;

    private final TalonSRX intakeLeadMotor = new TalonSRX(Ids.INTAKE_LEAD_DEVICE);
    //private final MotorControllerGroup intakeMotors = new MotorControllerGroup(intakeLeadMotor);

    private double speed;

    /** Creates a new Intake. */
    public IntakeSubsystem(IntakeDeploySubsystem intakeDeploySubsystem) {
        this.intakeDeploySubsystem = intakeDeploySubsystem;

        intakeLeadMotor.setInverted(true);
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        double limitedSpeed = intakeDeploySubsystem.getSolenoid() ? speed : 0;
        intakeLeadMotor.set(TalonSRXControlMode.PercentOutput, limitedSpeed);
    }

    // trying to set values to a port that does not exist leads to inconsistant
    // behavior
    public void startIntake(double speed) {
        this.speed = speed;
    }

    public void startIntake() {
        startIntake(INTAKE_DEFAULT_SPEED);
    }

    public void stop() {
        speed = 0;
        intakeLeadMotor.set(TalonSRXControlMode.PercentOutput, 0);
    }
}
