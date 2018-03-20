package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.ChangeGroupColorCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.group.Group;

/**
 * Parses the given {@code String} of arguments in the context of the ChangeGroupColorCommand
 * and returns an ChangeGroupColorCommand object for execution.
 * @throws ParseException if the user input does not conform the expected format
 */
public class ChangeGroupColorCommandParser implements Parser<ChangeGroupColorCommand> {

    @Override
    public ChangeGroupColorCommand parse(String userInput) throws ParseException {
        String[] args = userInput.trim().split(" ");
        if (args.length != 2) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ChangeGroupColorCommand.MESSAGE_USAGE));
        }
        try {
            Group group = ParserUtil.parseGroup(args[0]);
            String color = ParserUtil.parseColor((args[1]));
            return new ChangeGroupColorCommand(group.name, color);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }
}
