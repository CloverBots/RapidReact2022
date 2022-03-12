    // Copyright (c) FIRST and other WPILib contributors.
    // Open Source Software; you can modify and/or share it under the terms of
    // the WPILib BSD license file in the root directory of this project.

    package frc.robot.subsystems;

    import edu.wpi.first.wpilibj.Compressor;
    import edu.wpi.first.wpilibj.DoubleSolenoid;
    import edu.wpi.first.wpilibj.PneumaticsModuleType;
    import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
    import edu.wpi.first.wpilibj2.command.SubsystemBase;
    import frc.robot.Ids;
    import frc.robot.RobotLifecycleCallbacks;

    public class LiftHookSubsystem extends SubsystemBase implements RobotLifecycleCallbacks {
    DoubleSolenoid solenoid1 = new DoubleSolenoid(Ids.PCM_ID, PneumaticsModuleType.CTREPCM,
            Ids.LIFT_HOOK_SOLENOID_FORWARD,
            Ids.LIFT_HOOK_SOLENOID_REVERSE);

    public LiftHookSubsystem(){
        setSolenoid(false);
    }

    public void invertLift() {
        if(solenoid1.get() == DoubleSolenoid.Value.kForward) {
            solenoid1.set(Value.kReverse);
        }
        else if(solenoid1.get() == DoubleSolenoid.Value.kReverse) {
            solenoid1.set(Value.kForward);
        }
        else {
            System.out.println("Unrecognized liftHook solenoid position");
        }
    }

    public void setSolenoid(boolean position) {
        if (position) {
            solenoid1.set(Value.kForward);
        } else {
            solenoid1.set(Value.kReverse);
        }
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }


    @Override
    public void autonomousInit() {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void teleopInit() {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void disabledInit() {
        // TODO Auto-generated method stub
        
    }
}
