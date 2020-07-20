package com.fastparking.api.lib.commons.util;

import java.util.UUID;

import static java.util.UUID.fromString;
import static java.util.UUID.randomUUID;

public class MessageIdGenerator {
    private MessageIdGenerator() {
    }

    /**
     * Static method to retrieve a type 4 (pseudo randomly generated) UUID
     * using methods from {@link UUID}
     *
     * @return  A randomly generated {@code UUID}
     */
    public static UUID getMessageId() {
        return randomUUID();
    }

    /**
     * Static method to retrieve UUID from given string represenattion
     *
     * @param messageIdString A string specifying a {@code UUID}
     *
     * @return  A {@code UUID} with the specified value
     */
    public static UUID getMessageIdFromString(String messageIdString) { return fromString(messageIdString);}
}
