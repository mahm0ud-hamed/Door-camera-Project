package gpiostream_v1;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GpioStream_v1 {

    private static final Logger LOGGER = Logger.getLogger(GpioStream_v1.class.getName());
    private static final int PIN_NUM = 22;
    private static final File EXPORT_FILE_PATH = new File("/sys/class/gpio/export");
    private static final File DIRECTION_PATH = new File("/sys/class/gpio/gpio" + PIN_NUM + "/direction");
    private static final File VALUE_FILE_PATH = new File("/sys/class/gpio/gpio" + PIN_NUM + "/value");
    private static final char[] VALUE_BUFFER = new char[1];

    public static void main(String[] args) {
        GpioStream_v1 gpioStream = new GpioStream_v1();
        try {
            gpioStream.initializeGpio();
            gpioStream.startReadingGpioValue();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize or read GPIO", e);
        }
    }

    private void initializeGpio() throws IOException {
        exportGpioPin();
        setGpioDirection("in");
    }

    private void exportGpioPin() throws IOException {
        if (!DIRECTION_PATH.exists()) {
            try (FileWriter exportWriter = new FileWriter(EXPORT_FILE_PATH)) {
                exportWriter.write(String.valueOf(PIN_NUM));
            }
        }
    }

    private void setGpioDirection(String direction) throws IOException {
        try (FileWriter directionWriter = new FileWriter(DIRECTION_PATH)) {
            directionWriter.write(direction);
        }
    }

    private void startReadingGpioValue() {
        new Thread(() -> {
            while (true) {
                try (FileReader valueReader = new FileReader(VALUE_FILE_PATH)) {
                    if (valueReader.read(VALUE_BUFFER) != -1) {
                        System.out.println(VALUE_BUFFER[0]);
                    }
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Error reading GPIO value", e);
                    break; // Break out of the loop if an error occurs
                }
            }
        }).start();
    }
}
