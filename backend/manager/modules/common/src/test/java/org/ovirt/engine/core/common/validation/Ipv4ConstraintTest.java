package org.ovirt.engine.core.common.validation;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.ovirt.engine.core.common.validation.annotation.Ipv4;

@RunWith(MockitoJUnitRunner.class)
public class Ipv4ConstraintTest {

    @Test
    public void testStandardIsValid() {
        doTest("1.2.3.4", true);
    }

    @Test
    public void testNullIsValid() {
        doTest(null, true);
    }

    @Test
    public void testEmptyStringIsValid() {
        doTest("", true);
    }

    @Test
    public void testOtherTextThanIpv4IsInvalid() {
        doTest("0123:1234:7:8:12CD:0ABC:ABcd:cdef", false);
    }

    private void doTest(String input, boolean expectedResult) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<TestObject>> validationResult = validator.validate(new TestObject(input));
        boolean valid = validationResult.isEmpty();

        assertThat(valid, is(expectedResult));
    }

    public static class TestObject {
        @Ipv4(message = "IPV4_ADDR_BAD_FORMAT")
        private final String address;

        public TestObject(String address) {
            this.address = address;
        }

        public String getAddress() {
            return address;
        }
    }

}
