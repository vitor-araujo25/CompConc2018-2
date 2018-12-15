
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