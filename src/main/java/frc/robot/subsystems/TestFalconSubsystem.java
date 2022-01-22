package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class TestFalconSubsystem extends SubsystemBase {
   private class Gains {
        public final double kP;
        public final double kI;
        public final double kD;
        public final double kF;
        public final int kIzone;
        public final double kPeakOutput;

        public Gains(double _kP, double _kI, double _kD, double _kF, int _kIzone, double _kPeakOutput) {
            kP = _kP;
            kI = _kI;
            kD = _kD;
            kF = _kF;
            kIzone = _kIzone;
            kPeakOutput = _kPeakOutput;
        }
    }

    /**
     * Talon SRX/ Victor SPX will supported multiple (cascaded) PID loops. For
     * now we just want the primary one.
     */
    private static final int kPIDLoopIdx = 0;

    /**
     * Set to zero to skip waiting for confirmation, set to nonzero to wait and
     * report to DS if action fails.
     */
    private static final int kTimeoutMs = 30;

    /**
     * PID Gains may have to be adjusted based on the responsiveness of control
     * loop.
     * kF: 1023 represents output value to Talon at 100%, 7200 represents Velocity
     * units at 100% output
     * 
     * kP kI kD kF Iz PeakOut
     */
    public final Gains kGains_Velocit = new Gains(0.1, 0.001, 5, 1023.0 / 20660.0, 300, 1.00);

    private static final TalonFX talonFx = new TalonFX(18);

    public TestFalconSubsystem() {
        talonFx.configFactoryDefault();
        /* Config sensor used for Primary PID [Velocity] */
        talonFx.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor,
                kPIDLoopIdx,
                kTimeoutMs);

        /**
         * Phase sensor accordingly.
         * Positive Sensor Reading should match Green (blinking) Leds on Talon
         */
        talonFx.setSensorPhase(true);

        /* Config the peak and nominal outputs */
        talonFx.configNominalOutputForward(0, kTimeoutMs);
        talonFx.configNominalOutputReverse(0, kTimeoutMs);
        talonFx.configPeakOutputForward(1, kTimeoutMs);
        talonFx.configPeakOutputReverse(-1, kTimeoutMs);

        /* Config the Velocity closed loop gains in slot0 */
        talonFx.config_kF(kPIDLoopIdx, kGains_Velocit.kF, kTimeoutMs);
        talonFx.config_kP(kPIDLoopIdx, kGains_Velocit.kP, kTimeoutMs);
        talonFx.config_kI(kPIDLoopIdx, kGains_Velocit.kI, kTimeoutMs);
        talonFx.config_kD(kPIDLoopIdx, kGains_Velocit.kD, kTimeoutMs);
    }

    public void setRPM(double rpm) {
        // 4096 encoder ticks per revolution / 100 ms / minute
        double targetVelocity_UnitsPer100ms = rpm * 2048 / 600;

        talonFx.set(ControlMode.Velocity, targetVelocity_UnitsPer100ms);

        SmartDashboard.putNumber("Falcon Output RPM", talonFx.getSelectedSensorVelocity() / 2048 * 600);
    }
}
