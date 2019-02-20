package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_MEDICINES;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;

/**
 * Lists all medicines in the address book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all medicines";


    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        model.updateFilteredMedicineList(PREDICATE_SHOW_ALL_MEDICINES);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
