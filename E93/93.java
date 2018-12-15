public class SimpleReadWriteLock implements ReadWriteLock {
  int readers;
  boolean writer;
  Object lock;
  Lock readLock, writeLock;
  
  public SimpleReadWriteLock(){
    writer = false;
    readers = 0;
    lock = new Object();
    readLock = new ReadLock();
    writeLock = new WriteLock(); 
  }

  public Lock readLock() {
    return readLock;
  }
  public Lock writeLock() {
    return writeLock;
  }
}

class ReadLock implements Lock {
  @Override
  public void lock() {
    synchronized (lock) {
      try {
        while(writer){
          lock.wait();
        }
        readers++;;
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  } 
  @Override
  public void unlock() {
    synchronized (lock) {
      readers--;
      if(readers == 0)
        lock.notifyAll();    
    }
  }
}

protected class WriteLock implements Lock {
  @Override
  public void lock() {
    synchronized (lock) {
      try{
        while(readers > 0 || writer){
          lock.wait();
        }
        writer = true;
      } catch (InterruptedException e) {
        e.printStackTrace();
      } 
    }
  }
    
  @Override
  public void unlock() {
    synchronized (lock) {
      writer = false;
      lock.notifyAll();
    }
  }
}