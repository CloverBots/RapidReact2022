package frc.robot.subsystems;

import frc.robot.LiftPosition;

public class LiftSubsystemDummy implements LiftObserver {

    @Override
    public LiftPosition getLiftPosition() {
        // TODO Auto-generated method stub
        return LiftPosition.DOWN;
    }
    
}
