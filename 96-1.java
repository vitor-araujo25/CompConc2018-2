//1.Implement this class using locks and condition variables.

public class Banheiro {
    Lock lock;
    Condition concicao;
    int homens,mulheres;

    public Banheiro(){
        lock = new ReentrantLock();
        concicao = lock.newCondition();
        homens = 0;
        mulheres = 0;
    }

    public void enterMale(){
        try {
            lock.lock();
            try {
                while (mulheres>0) {concicao.await();}
            } 
            catch (Exception e) { }

            homens++;
        }
        finally {
            lock.unlock();
        }
    }
    public void enterFemale(){
        try {
            lock.lock();
            try {
                while (homens>0) {
                    concicao.await();
                }
            } 
            catch (Exception e) { }

            mulheres++;
        }
        finally {
            lock.unlock();
        }
    }
    public void leaveMale(){
        try {
            lock.lock();
            homens--;
            concicao.signalAll();
        }
        finally {
            lock.unlock();
        }
    }
    public void leaveFemale(){
        try {
            lock.lock();
            mulheres--;
            concicao.signalAll();
        }
        finally {
            lock.unlock();
        }
    }
}