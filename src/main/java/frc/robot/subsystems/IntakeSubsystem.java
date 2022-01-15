package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Ids;

public class IntakeSubsystem extends SubsystemBase {
    private final CANSparkMax intakeLeadMotor = new CANSparkMax(Ids.INTAKE_LEAD_DEVICE, MotorType.kBrushless);
    private final MotorControllerGroup intakeMotors = new MotorControllerGroup(intakeLeadMotor);

    /** Creates a new Shooter. */
    public IntakeSubsystem() {

    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }

    public void startIntake(double speed) {
        intakeMotors.set(speed);

    }

    public void stop() {
        intakeMotors.set(0);
    }
}
