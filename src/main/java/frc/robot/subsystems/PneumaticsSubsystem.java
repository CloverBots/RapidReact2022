package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Ids;
import frc.robot.RobotLifecycleCallbacks;

public class PneumaticsSubsystem extends SubsystemBase implements RobotLifecycleCallbacks {
    DoubleSolenoid solenoid1 = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, Ids.TEST_SOLENOID1_FORWARD,
            Ids.TEST_SOLENOID1_REVERSE);
    Compressor compressor = new Compressor(PneumaticsModuleType.CTREPCM);

    /** Creates a new PneumaticsSubsystem. */
    public PneumaticsSubsystem() {
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }

    public void setSoleonoid(boolean position) {
        if (position) {
            solenoid1.set(Value.kForward);
        }
        else {
            solenoid1.set(Value.kReverse);
        }
    }

    private void compressorStart() {
        compressor.enableDigital();
    }

    private void compressorStop() {
        compressor.disable();
    }

    @Override
    public void autonomousInit() {
        compressorStart();
    }

    @Override
    public void teleopInit() {
        compressorStart();
    }
    @Override
    public void disabledInit() {
        compressorStop();
    }

}
