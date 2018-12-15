public class Rooms {

    Object lock;
    int room, count;
    final int roomLimit;
    volatile bool entering,handlerExecuting;


    public interface Handler {
        void onEmpty();
    }

    public class RoomHandler implements Handler{
        @Override
        public void onEmpty(){
            lock.notifyAll();
            handlerExecuting = false;
        }
    }

    public Rooms(int m) {
        rooms = -1;
        count = 0;
        lock = new Object();
        roomLimit = m;
    };

    synchronized void enter(int i) {
        
        while((room != i && count != 0) || handlerExecuting){
            lock.wait();
        }
        entering = true;
        room = i;
        count++;
        entering = false;
    };

    synchronized boolean exit() {
        count--;
        if(!entering && count == 0){
            handlerExecuting = true;
            setExitHandler(room, h);
        }

    };

    public void setExitHandler(int i, Rooms.Handler h) {
        h.onEmpty();
    };
}
