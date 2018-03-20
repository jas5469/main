package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_GROUP_COLOR;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_GROUP_DESC;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GROUP_COLOR_RED;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GROUP_CS1010;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.logic.commands.ChangeGroupColorCommand;
import seedu.address.model.group.Group;

/**
 * Tests for the parsing of input arguments and creating a new ChangeGroupColorCommand object
 */
public class ChangeGroupColorCommandParserTest {
    private static final String GROUP_EMPTY = " " + PREFIX_GROUP;

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ChangeGroupColorCommand.MESSAGE_USAGE);

    private ChangeGroupColorCommandParser parser = new ChangeGroupColorCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no color specified
        assertParseFailure(parser, VALID_GROUP_CS1010, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        // unsupported color specified
        assertParseFailure(parser, VALID_GROUP_CS1010 + INVALID_GROUP_COLOR,
                Group.MESSAGE_GROUP_COLOR_CONSTRAINTS);

        // invalid group name
        assertParseFailure(parser, INVALID_GROUP_DESC + " " + VALID_GROUP_COLOR_RED,
                Group.MESSAGE_GROUP_CONSTRAINTS);
    }

    @Test
    public void parse_validValue_success() {
        // unsupported color specified
        assertParseSuccess(parser, VALID_GROUP_CS1010 + " " + VALID_GROUP_COLOR_RED,
                new ChangeGroupColorCommand(VALID_GROUP_CS1010,VALID_GROUP_COLOR_RED));
    }
}
