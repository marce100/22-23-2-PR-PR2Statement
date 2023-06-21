package uoc.ds.pr.model;

import edu.uoc.ds.adt.helpers.Position;
import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.traversal.Iterator;
import edu.uoc.ds.traversal.Traversal;
import org.w3c.dom.Node;

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
        System.out.println("voy a añadir el trabajador: "+worker.getWorkerId()+" role: "+getId());
        workers.insertEnd(worker);
    }

    public void deleteWorker(Worker worker){

//        Traversal<Worker> traversal = workers.positions();
//        while(traversal.hasNext()) {
//            Position<Worker> position = traversal.next();
//            if (position.getElem().equals(worker)){
//                workers.update(position, worker);
//                return;
//            }
//        }

//        Traversal<Worker> traversal = workers.positions();
//        while(traversal.hasNext()) {
//            Position<Worker> position = traversal.next();
//            if (position.getElem().equals(worker)){
//                workers.delete(position);
//                return;
//            }
//        }
//        workers.insertEnd(worker);

//        Traversal<Worker> traversal = workers.positions();
//        while(traversal.hasNext()) {
//            Position<Worker> position = traversal.next();
//            if (position.getElem().equals(worker)){
//                System.out.println("voy a borrar el trabajador: "+worker.getWorkerId()+" role: "+getId());
//                System.out.println("pos: "+position.getElem().getWorkerId());
//                workers.delete(position);
//                return;
//            }
//        }


//        LinkedList<Worker> newList = new LinkedList<>();
//
//        Traversal<Worker> traversal = workers.positions();
//        while (traversal.hasNext()) {
//            Position<Worker> position = traversal.next();
//            if (!position.getElem().equals(worker)) {
//                newList.insertEnd(position.getElem());
//            }
//        }
//
//        // Reemplaza la lista original con la nueva lista sin el trabajador
//        workers = newList;




        System.out.println("voy a borrar el trabajador: "+worker.getWorkerId()+" role: "+getId());

        LinkedList<Worker> newList = new LinkedList<>();

        Iterator<Worker> i = workers.values();
        while (i.hasNext()){
            Worker w= i.next();
            if(!w.getWorkerId().equals(worker.getWorkerId()))
                newList.insertEnd(w);
        }
        workers=newList;


        Iterator<Worker> i1 = workers.values();
        System.out.print(getId()+ ":   ");
        while (i1.hasNext()){
            Worker w= i1.next();
            System.out.print( w.getWorkerId()+" ");
        }
        System.out.println("");




    }
}
