package uoc.ds.pr.model;

import edu.uoc.ds.adt.helpers.Position;
import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.traversal.Iterator;

import java.util.Comparator;


public class Role {

    private String id;

    private String name;

    private LinkedList<Worker> workers;

    public Role(String id, String name) {
        this.setId(id);
        this.setName(name);
        this.workers = new LinkedList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedList<Worker> getWorkers() {
        return workers;
    }

    public void addWorker(Worker worker){
        workers.insertEnd(worker);
    }

    public void updateWorker(Worker worker){


???????????????????????????????

//        Position<Worker> p;
//        Iterator<Worker> i = workers.values();
//        while(i.hasNext()){
//            p= workers.positions().next();
//            Worker w = i.next();
//            if (w.getWorkerId().equals(worker.getWorkerId()))
//                workers.delete(p);
//        }
//        workers.insertEnd(worker);


//        Position<Worker> currentPosition = workers.positions().next();
//        Position<Worker> positionToDelete = null;
//
//        while (currentPosition != null) {
//            Worker currentEntity = currentPosition.getElem();
//            if (currentEntity.equals(worker)) {
//                positionToDelete = currentPosition;
//                break;
//            }
//            currentPosition = workers.positions().next();
//        }
//
//        if (positionToDelete != null) {
//            Worker deletedEntity = workers.delete(positionToDelete);
//            System.out.println("Objeto eliminado: " + deletedEntity);
//        } else {
//            System.out.println("Objeto no encontrado en la lista.");
//        }



    }
}
