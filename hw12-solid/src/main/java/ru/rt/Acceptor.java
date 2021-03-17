package ru.rt;

import ru.rt.exceptions.ExceptionMessages;
import ru.rt.exceptions.NoteIsNotValid;
import ru.rt.interfaces.IAccepting;
import ru.rt.interfaces.IMediator;
import ru.rt.interfaces.IVaultCassettes;

public class Acceptor extends Component implements IAccepting {
    private final IVaultCassettes vault;

    public Acceptor(IMediator atm, IVaultCassettes vault){
        super(atm);
        this.vault = vault;
    }

    @Override
    public void takeNote(Note note){
        try {
            openShutter();
            grabNote(note);
        }
        catch(NoteIsNotValid e){
            returnNote(note);
        }
        finally{
            closeShutter();
        }
    }

    private void openShutter(){
    }

    private void closeShutter(){
    }

    private void grabNote(Note note) throws NoteIsNotValid{
        checkNote(note);
        vault.takeNote(note);
    }

    private void returnNote(Note note){
    }

    private void checkNote(Note note) throws NoteIsNotValid {
        if (!true){
            atm.notify(this, Events.NOT_VALID_NOTE);
            throw new NoteIsNotValid(ExceptionMessages.NOTE_IS_NOT_VALID.toString());
        }
    }

}
