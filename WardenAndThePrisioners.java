// B)
public class Prisoner extends Thread {
    int id;
    Room room;
    Warden warden;
    int state = 0;
    int count = 0;
    Object doorClosed;

    public static final int
            IN_CELL    = 1,
            IN_ROOM    = 2,
            FREE       = 3,
            DEAD       = 4;

    public Prisoner(int id, Room room, Warden warden){
        this.id = id;
        this.room = room;
        this.warden = warden;
        this.doorClosed = new Object();
    }

    public void run() {
        try {
            this.live();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    synchronized void live() throws InterruptedException {
        while (true) {
            // go back to the cell
            // stay there until asked to go to the room
            //      or until set free
            // go to the room when asked
            // do something in the room
            // get out of the room
            this.state = IN_CELL;
            System.out.println("Prisoner: "+this.id+" wait in cell");
            while (this.state == IN_CELL)
                doorClosed.wait();

            doorClosed.notifyAll();
            // I got out of the cell...
            // am I free or dead? or should I go to the room?
            if (this.state != IN_ROOM)
                break;

            // I am in the room
            // do my custom action
            this.roomAction();

            // tell warden that I'm leaving the room
            this.warden.notifyPrisonerLeavingRoom();
        }

        if (this.state == FREE)
            // I'm free... I'm free!!!
            System.out.println("Prisoner " + this.id + " is free!");
        else
            // DEAD
            System.out.println("Prisoner " + this.id + " is dead!");
    }

    protected void roomAction() {
        // If I didn't switch the trigger on yet, then do it
        if (this.count < 1 && !this.room.isTriggerSet())
        {
            System.out.println("Prisoner: " + this.id + " turns trigger on!");
            this.count++;
            this.room.setTrigger();
        }
        else {
            System.out.println("Prisoner: " + this.id + " in room... nothing to do!");
        }
    }

    public synchronized void free() {
        this.state = FREE;
        this.notifyAll();
    }

    public synchronized void kill() {
        this.state = DEAD;
        this.notifyAll();
    }
}

// D)
// A implementação do líder deve considerar o estado do switch
// Com isso, o líder deve contar 2n - 1 prisioneiros

public class Leader extends Prisoner {
    //Reimplementamos o construtor para contar os prisioneiros
    public Leader(int id, Room room, Warden warden, int prisoners, boolean switchStateUnknown) {
        super(id, room, warden);
        if(switchStateUnknown){
            this.count = 2 * prisoners - 1;
        }
        else{
            this.count = prisoners - 1;
        }
    }

    protected void roomAction() {
        // I'm the leader... did someone turn the trigger on?
        // If so, decrement the number of remaining prisoners and turn if off.
        if (this.room.isTriggerSet())
        {
            this.count--;
            System.out.println("Leader: turns trigger off, and counts " + this.count + " prisoners remaining.");
            if (this.count == 0)
                this.warden.requestFreedom();
            this.room.clearTrigger();
        }
        else {
            System.out.println("Leader: in room... nothing to do!");
        }
    }
}