package de.acme.vsf.deserializer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerTest {

    private static final Logger FINAL_LOGGER = LogManager.getLogger("FinalOutput");

    public static void main(String[] args) {
        FINAL_LOGGER.info("LoggerTest!");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[").append('\n');
        for (int i = 0; i < 10; i++) {
            stringBuilder.append("   {").append("element_id: ").append(i).append(", ").append(" value: ").append(i).append("").append("}").append('\n');
        }
        stringBuilder.append("]");
        FINAL_LOGGER.info(stringBuilder);
    }
}
