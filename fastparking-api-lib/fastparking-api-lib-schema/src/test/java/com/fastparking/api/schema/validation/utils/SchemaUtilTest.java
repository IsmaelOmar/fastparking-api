package com.fastparking.api.schema.validation.utils;

import com.fastparking.api.lib.commons.exceptions.FastParkingApplicationException;
import com.fastparking.api.lib.schema.validation.utils.SchemaUtil;
import com.fastparking.api.lib.schema.validation.utils.SchemaValidator;
import com.fastparking.api.schema.UserRegistrationRequest;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;

import static com.fastparking.api.lib.commons.constants.DataConstants.TECHNICAL_ERROR;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;

@Disabled
@RunWith(PowerMockRunner.class)
@PrepareForTest(SchemaUtil.class)
public class SchemaUtilTest {

    @Test
    public void givenSchemaFileNameIsWrongWhenLoadSchemaIsCalledExpectIOException() throws Exception {
        try {
            PowerMockito.mockStatic(SchemaUtil.class);
            PowerMockito.doThrow(new IOException())
                    .when(SchemaUtil.class, "loadSchema", any(String.class));
            SchemaValidator.validateUserRegistrationRequestAgainstSchema("", new UserRegistrationRequest());
        } catch (FastParkingApplicationException e) {
            assertThat(200, is(e.getStatusCode()));
            assertThat("9000", is(e.getErrorCode()));
            assertThat(TECHNICAL_ERROR, is(e.getMessage()));
        }
    }
}
