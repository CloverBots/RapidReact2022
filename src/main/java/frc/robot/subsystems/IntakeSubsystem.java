package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Ids;

public class IntakeSubsystem extends SubsystemBase {
    private final CANSparkMax intakeLeadMotor = new CANSparkMax(Ids.INTAKE_LEAD_DEVICE, MotorType.kBrushless);
    private final MotorControllerGroup intakeMotors = new MotorControllerGroup(intakeLeadMotor);

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
        intakeMotors.set(speed);
    }

    public void stop() {
        intakeMotors.set(0);
    }
}
