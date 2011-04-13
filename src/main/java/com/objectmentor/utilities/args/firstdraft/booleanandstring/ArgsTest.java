package com.objectmentor.utilities.args.firstdraft.booleanandstring;

import org.junit.Test;

import java.text.ParseException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ArgsTest {

    @Test
    public void checkThatOneLoggingFlagIsEnabled() throws ParseException {
        Args arg = new Args("l", new String[]{"-l"});
        boolean logging = arg.getBoolean('l');
        assertThat("Logging", logging, is(true));
    }

    @Test
    public void checkThatTwoLoggingFlagsAreEnabled() throws ParseException {
        Args arg = new Args("l,X", new String[]{"-l", "-X"});
        boolean loggingFlag1 = arg.getBoolean('l');
        boolean loggingFlag2 = arg.getBoolean('X');
        assertThat("First logging flag", loggingFlag1, is(true));
        assertThat("Second logging flag", loggingFlag2, is(true));
    }

    @Test
    public void onlyOneOfTwoLoggingFlagsAreEnabled() throws ParseException {
        Args arg = new Args("l,X", new String[]{"-X"});
        boolean loggingFlag1 = arg.getBoolean('l');
        boolean loggingFlag2 = arg.getBoolean('X');
        assertThat("First logging flag", loggingFlag1, is(false));
        assertThat("Second logging flag", loggingFlag2, is(true));
    }

    @Test
    public void checkThatOneStringFlagIsEnabledAndCorrespondingArgumentIsPassed() throws ParseException {
        Args arg = new Args("d*", new String[]{"-d", "C:/Temp"});
        String stringArgument = arg.getString('d');
        assertThat("String argument", stringArgument, is(equalTo("C:/Temp")));
    }

    @Test
    public void loggingAndStringFlagsEnabledAndBothAreUsed() throws ParseException {
        Args arg = new Args("f*,l", new String[]{"-l", "-f", "C:/Temp/hello.txt"});
        boolean logging = arg.getBoolean('l');
        String stringArgument = arg.getString('f');
        assertThat("Logging", logging, is(true));
        assertThat("String argument", stringArgument, is(equalTo("C:/Temp/hello.txt")));
    }

    @Test
    public void checkThatArgsHasFoundLoggingArgument() throws ParseException {
        Args arg = new Args("l", new String[]{"-l"});
        boolean hasLogging = arg.has('l');
        assertThat("Logging was found", hasLogging, is(true));
    }

    @Test
    public void ifNothingIsPassedArgsIsValid() throws ParseException {
        Args arg = new Args("", new String[]{});
        boolean isValid = arg.isValid();
        assertThat("Arg must be valid", isValid, is(true));
    }

    @Test
    public void invalidArgumentIsPassedForBoolean() throws ParseException {
        Args arg = new Args("l", new String[]{"-p"});
        boolean isValid = arg.isValid();
        assertThat("Arg must be invalid", isValid, is(false));
    }

    @Test
    public void checkErrorMessageForMissingArgumentOfStringSchema() throws Exception {
        Args arg = new Args("d*", new String[]{"-d"});
        String errorMessage = arg.errorMessage();
        assertThat("Error message", errorMessage, is(equalTo("Could not find string parameter for -d.")));
    }

    @Test
    public void ifArgumentIsMissingForStringSchemaArgsMustBeInvalid() throws Exception {
        Args arg = new Args("d*", new String[]{"-d"});
        boolean isValid = arg.isValid();
        assertThat("Args must be invalid", isValid, is(false));
    }

    @Test
    public void ifArgumentIsMissingForStringSchemaABlankMustBeReturned() throws Exception {
        Args arg = new Args("d*", new String[]{"-d"});
        String stringArgument = arg.getString('d');
        assertThat("String argument", stringArgument, is(equalTo("")));
    }

    @Test
    public void printErrorMessageForWrongArgument() throws Exception {
        Args arg = new Args("l", new String[]{"-p"});
        String errorMessage = arg.errorMessage();
        assertThat("Error message", errorMessage, is(equalTo("Argument(s) -p unexpected.")));
    }

    @Test(expected = Exception.class)
    public void printEmptyErrorMessageForValidArgument() throws Exception {
        Args arg = new Args("l", new String[]{"-l"});
        String errorMessage = arg.errorMessage();
        assertThat("Error message", errorMessage, is(equalTo("")));
    }

    @Test
    public void printUsageForLoggingFlag() throws ParseException {
        Args arg = new Args("l", new String[]{"-l"});
        String usage = arg.usage();
        assertThat("Usage", usage, is(equalTo("-[l]")));
    }

    @Test
    public void cardinalityForOneValidBooleanArgumentMustBeOne() throws ParseException {
        Args arg = new Args("l", new String[]{"-l"});
        int cardinality = arg.cardinality();
        assertThat("Cardinality", cardinality, is(1));
    }

    @Test
    public void printEmptyUsageIfNoArgumentsArePassed() throws ParseException {
        Args arg = new Args("", new String[]{});
        String usage = arg.usage();
        assertThat("Usage", usage, is(equalTo("")));
    }

    @Test
    public void loggingFlagMustNotBeFollowedByArgument() throws ParseException {
        Args arg = new Args("l", new String[]{"-l", "p"});
        boolean isValid = arg.isValid();
        assertThat("Arg must be invalid", isValid, is(false));
    }

    @Test(expected = ParseException.class)
    public void ifSchemaElementIdIsNotACharacterThrowParseException() throws ParseException {
        Args arg = new Args("1", null);
    }

    @Test
    public void checkErrorMessageForNoneCharacterSchemaElementId() throws ParseException {
        try {
            Args arg = new Args("1", null);
        } catch (ParseException e) {
            String errorMessage = e.getMessage();
            assertThat("Error message", errorMessage, is(equalTo("Bad character:1in Args format: 1")));
        }
    }

    @Test
    public void errorMessageOnlyContainsInvalidCharacter() throws ParseException {
        try {
            Args arg = new Args("g,d*,2", null);
        } catch (ParseException e) {
            String errorMessage = e.getMessage();
            assertThat("Error message", errorMessage, is(equalTo("Bad character:2in Args format: g,d*,2")));
        }
    }
}
