package uoc.ds.pr.model;

import edu.uoc.ds.adt.helpers.Position;
import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.traversal.Traversal;

public class Role {

    private String id;
    private String name;
    private LinkedList<Worker> workers;

    public Role(String id, String name) {
        this.setId(id);
        this.setName(name);
        this.workers = new LinkedList<>();
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public LinkedList<Worker> getWorkers() { return workers; }

    public void addWorker(Worker worker){ workers.insertEnd(worker); }

    public void deleteWorker(Worker worker){
        Traversal<Worker> traversal = workers.positions();
        while(traversal.hasNext()) {
            Position<Worker> position = traversal.next();
            if (position.getElem().equals(worker)){
                workers.delete(position);
                return;
            }
        }
        workers.insertEnd(worker);
    }
}
