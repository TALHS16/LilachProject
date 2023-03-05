package org.lilachshop.requests;


import org.lilachshop.entities.ExampleEntity;
import org.lilachshop.entities.ExampleEnum;

// FOR DEBUGGING PURPOSES ONLY!!!!
public class DebugRequest extends Request {

    private int id_to_update;
    private ExampleEnum update_to;

    private ExampleEntity exampleEntityToUpdate;

    public DebugRequest(String request) {
        super(request);
    }

    public DebugRequest(String request, ExampleEntity exampleEntityToUpdate) {
        super(request);
        this.exampleEntityToUpdate = exampleEntityToUpdate;
    }

    public DebugRequest(String request, int id_to_update, ExampleEnum update_to) {
        super(request);
        this.id_to_update = id_to_update;
        this.update_to = update_to;
    }

    public int getIDToUpdate() {
        return id_to_update;
    }

    public ExampleEnum getUpdateToEnum(){
        return update_to;
    }
}