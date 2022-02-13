package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Ids;

public class IntakeSubsystem extends SubsystemBase {

    private final double INTAKE_DEFAULT_SPEED = 10.0;

    private final WPI_TalonSRX intakeLeadMotor = new WPI_TalonSRX(Ids.INTAKE_LEAD_DEVICE);
    //private final MotorControllerGroup intakeMotors = new MotorControllerGroup(intakeLeadMotor);

    /** Creates a new Intake. */
    public IntakeSubsystem() {

    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }

    // trying to set values to a port that does not exist leads to inconsistant
    // behavior
    public void startIntake(double speed) {
        intakeLeadMotor.set(speed);
    }

    public void startIntake() {
        startIntake(INTAKE_DEFAULT_SPEED);
    }

    public void stop() {
        intakeLeadMotor.set(0);
    }
}
