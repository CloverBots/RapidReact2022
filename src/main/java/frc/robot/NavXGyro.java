package frc.robot;

import com.kauailabs.navx.frc.AHRS;

public class NavXGyro {
    private final AHRS ahrs = new AHRS(Ids.AHRS_PORT_ID);

    public void setup() {
        ahrs.calibrate();
        ahrs.reset();
    }

    public double getHeading() {
        return ahrs.getYaw();
    }
}
