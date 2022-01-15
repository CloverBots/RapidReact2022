package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Ids;

public class ShooterSubsystem extends SubsystemBase {
  private final CANSparkMax shooterLeadMotor = new CANSparkMax(Ids.SHOOTER_LEAD_DEVICE, MotorType.kBrushless);
  private final CANSparkMax shooterFollowMotor1 = new CANSparkMax(Ids.SHOOTER_FOLLOW_DEVICE_1, MotorType.kBrushless);
  private final CANSparkMax shooterFollowMotor2 = new CANSparkMax(Ids.SHOOTER_FOLLOW_DEVICE_2, MotorType.kBrushless);
  private final CANSparkMax shooterFollowMotor3 = new CANSparkMax(Ids.SHOOTER_FOLLOW_DEVICE_3, MotorType.kBrushless);
  private final MotorControllerGroup shooterMotors = new MotorControllerGroup(shooterLeadMotor, shooterFollowMotor1, 
    shooterFollowMotor2, shooterFollowMotor3);


  /** Creates a new Shooter. */
  public ShooterSubsystem() {
      
  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
  public void startShooter(double speed)
  {
    shooterMotors.set(speed);
    
  }
  public void stop()
  {
    shooterMotors.set(0);
  }
}
