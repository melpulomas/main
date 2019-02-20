package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.beans.property.ReadOnlyProperty;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.medicine.Medicine;
import seedu.address.testutil.MedicineBuilder;

public class AddCommandTest {

    private static final CommandHistory EMPTY_COMMAND_HISTORY = new CommandHistory();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void constructor_nullMedicine_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddCommand(null);
    }

    @Test
    public void execute_medicineAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingMedicineAdded modelStub = new ModelStubAcceptingMedicineAdded();
        Medicine validMedicine = new MedicineBuilder().build();

        CommandResult commandResult = new AddCommand(validMedicine).execute(modelStub, commandHistory);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validMedicine), commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validMedicine), modelStub.medicinesAdded);
        assertEquals(EMPTY_COMMAND_HISTORY, commandHistory);
    }

    @Test
    public void execute_duplicateMedicine_throwsCommandException() throws Exception {
        Medicine validMedicine = new MedicineBuilder().build();
        AddCommand addCommand = new AddCommand(validMedicine);
        ModelStub modelStub = new ModelStubWithMedicine(validMedicine);

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.MESSAGE_DUPLICATE_MEDICINE);
        addCommand.execute(modelStub, commandHistory);
    }

    @Test
    public void equals() {
        Medicine alice = new MedicineBuilder().withName("Alice").build();
        Medicine bob = new MedicineBuilder().withName("Bob").build();
        AddCommand addAliceCommand = new AddCommand(alice);
        AddCommand addBobCommand = new AddCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different medicine -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addMedicine(Medicine medicine) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasMedicine(Medicine medicine) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteMedicine(Medicine target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setMedicine(Medicine target, Medicine editedMedicine) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Medicine> getFilteredMedicineList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredMedicineList(Predicate<Medicine> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean canUndoAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean canRedoAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void undoAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void redoAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void commitAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyProperty<Medicine> selectedMedicineProperty() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Medicine getSelectedMedicine() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setSelectedMedicine(Medicine medicine) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single medicine.
     */
    private class ModelStubWithMedicine extends ModelStub {
        private final Medicine medicine;

        ModelStubWithMedicine(Medicine medicine) {
            requireNonNull(medicine);
            this.medicine = medicine;
        }

        @Override
        public boolean hasMedicine(Medicine medicine) {
            requireNonNull(medicine);
            return this.medicine.isSameMedicine(medicine);
        }
    }

    /**
     * A Model stub that always accept the medicine being added.
     */
    private class ModelStubAcceptingMedicineAdded extends ModelStub {
        final ArrayList<Medicine> medicinesAdded = new ArrayList<>();

        @Override
        public boolean hasMedicine(Medicine medicine) {
            requireNonNull(medicine);
            return medicinesAdded.stream().anyMatch(medicine::isSameMedicine);
        }

        @Override
        public void addMedicine(Medicine medicine) {
            requireNonNull(medicine);
            medicinesAdded.add(medicine);
        }

        @Override
        public void commitAddressBook() {
            // called by {@code AddCommand#execute()}
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

}
