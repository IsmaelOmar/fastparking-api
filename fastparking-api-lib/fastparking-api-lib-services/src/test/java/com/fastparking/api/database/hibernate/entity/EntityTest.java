package com.fastparking.api.database.hibernate.entity;

import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

public class EntityTest {

    @Test
    public void validate() {
        Validator validator = ValidatorBuilder.create()
                .with(new SetterMustExistRule(),
                        new GetterMustExistRule())
                .with(new SetterTester(),
                        new GetterTester())
                .build();
        validator.validate("com.fastparking.api.database.hibernate.entity");
    }

    @Test
    public void equalsContract() {

        EqualsVerifier.forClass(UserEntity.class)
                .usingGetClass()
                .suppress(Warning.SURROGATE_KEY)
                .verify();

        EqualsVerifier.forClass(UserLoginEntity.class)
                .usingGetClass()
                .suppress(Warning.SURROGATE_KEY)
                .verify();

        EqualsVerifier.forClass(ParkingBaysEntity.class)
                .usingGetClass()
                .suppress(Warning.SURROGATE_KEY)
                .verify();

        EqualsVerifier.forClass(BookingEntity.class)
                .usingGetClass()
                .suppress(Warning.SURROGATE_KEY)
                .verify();
    }
}
