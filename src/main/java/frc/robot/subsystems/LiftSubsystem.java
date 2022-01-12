package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Ids;
import frc.robot.LiftPosition;

public class LiftSubsystem extends SubsystemBase implements LiftObserver {
    private final CANSparkMax winch = new CANSparkMax(Ids.LIFT_WINCH_DEVICE, MotorType.kBrushless);

    private final DoubleSolenoid liftSolenoid = new DoubleSolenoid(
            PneumaticsModuleType.CTREPCM,
            Ids.LIFT_SOLENOID_FORWARD,
            Ids.LIFT_SOLENOID_REVERSE);

    private final DoubleSolenoid ratchetSolenoid = new DoubleSolenoid(
            PneumaticsModuleType.CTREPCM,
            Ids.RATCHET_SOLENOID_FORWARD,
            Ids.RATCHET_SOLENOID_REVERSE);

    private LiftPosition liftPosition;
    
    /**
     * Constructs a new {@link LiftSubsystem} instance.
     */
    public LiftSubsystem() {
        winch.getEncoder().setPosition(0);
        ratchetSolenoid.set(Value.kReverse);

        setLiftPosition(LiftPosition.UP);
    }

    /**
     * Sets the lift's position.
     * @param position The new position of the lift.
     */
    public void setLiftPosition(LiftPosition position) {
        liftPosition = position;
        switch (liftPosition) {
            case DOWN:
                liftSolenoid.set(Value.kReverse);
                break;
            case UP:
                liftSolenoid.set(Value.kForward);
                break;
        }
    }

    /**
     * Returns the position of the lift.
     */
    public LiftPosition getLiftPosition() {
        return liftPosition;
    }
}
