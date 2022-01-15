package frc;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class VisionTargetTracker {
    public enum LedMode {
        CURRENT(0),
        FORCE_OFF(1),
        FORCE_BLINK(2),
        FORCE_ON(3);

        private final int value;

        private LedMode(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private static final String LIMELIGHT_TABLE_NAME = "limelight";
    private static final String LIMELIGHT_TABLE_ENTRY_X = "tx";
    private static final String LIMELIGHT_TABLE_ENTRY_Y = "ty";
    private static final String LIMELIGHT_TABLE_ENTRY_A = "ta";
    private static final String LIMELIGHT_TABLE_ENTRY_LED_MODE = "ledMode";

    private final NetworkTable table;
    private final NetworkTableEntry tx;
    private final NetworkTableEntry ty;
    private final NetworkTableEntry ta;
    private final NetworkTableEntry ledMode;

    public VisionTargetTracker() {
        table = NetworkTableInstance.getDefault().getTable(LIMELIGHT_TABLE_NAME);
        tx = table.getEntry(LIMELIGHT_TABLE_ENTRY_X);
        ty = table.getEntry(LIMELIGHT_TABLE_ENTRY_Y);
        ta = table.getEntry(LIMELIGHT_TABLE_ENTRY_A);
        ledMode = table.getEntry(LIMELIGHT_TABLE_ENTRY_LED_MODE);
    }

    public double getX() {
        return tx.getDouble(0.0);
    }

    public double getY() {
        return ty.getDouble(0.0);
    }

    public double getArea() {
        return ta.getDouble(0.0);
    }

    public LedMode getLedMode() {
        var intValue = ledMode.getNumber(0).intValue();
        return LedMode.values()[intValue];
    }

    public void setLedMode(LedMode mode) {
        ledMode.setNumber(mode.getValue());
    }
}
