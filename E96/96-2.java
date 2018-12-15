
//2.Implement this class using synchronized , wait() , notify() , and notifyAll() For each implementation, explain why it satisfies mutual exclusion and starvation-freedom.
public class BathroomSync {

    Object lock;
    volatile boolean ocupado;
    volatile int homens,mulheres;

    public BathroomSync(){
        ocupado = false;
        lock = new Object();
        homens = 0;
        mulheres = 0;
    }

    public void enterMale(){
        synchronized (lock){
            try {
                //Checo se existem pessoas de outro sexo    
                while (ocupado && (mulheres > 0)){
                    lock.wait();
                }
            } 
            catch (Exception e) { }
            finally {
                ocupado = true; 
                homens++;
            }
        }
    }
    public void enterFemale(){
        synchronized (lock){
            try {
                //Checo se existem pessoas de outro sexo
                while (ocupado && (homens > 0)){
                    lock.wait();
                }
            } 
            catch (Exception e) { }
            finally { 
                ocupado = true; 
                mulheres++;
            }
        }
    }
    public void leaveMale(){
        synchronized (lock){
            try{
                ocupado = false;
                homens--;
                lock.notifyAll();
            }
            catch (Exception e){ }
        }
    }
    public void leaveFemale(){
        synchronized (lock){
            try{
                ocupado = false;
                mulheres--;
                lock.notifyAll();
            }
            catch (Exception e){ }
        }
    }
}