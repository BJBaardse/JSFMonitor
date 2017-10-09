package movingballsfx;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RW {
    private Lock monLock;
    private int writersActive;
    private int readersActive;
    private Condition okToRead;
    private Condition okToWrite;
    private int readersWaiting;
    private int writersWaiting;

    public RW() {
        this.monLock = new ReentrantLock();
        this.writersActive = 0;
        this.readersActive = 0;
        this.readersWaiting = 0;
        this.writersWaiting = 0;
        this.okToRead = monLock.newCondition();
        this.okToWrite = monLock.newCondition();
    }

    public void enterReader() throws InterruptedException
    {
        monLock.lock();
        try{

            while (writersActive != 0){
                readersWaiting++;
                okToRead.await();
                readersWaiting--;
            }
            readersActive++;
        }
        finally {
            monLock.unlock();
        }
    }

    public void exitReader()
    {
        monLock.lock();
        try {
            readersActive--;
            if (readersActive == 0) {
                okToWrite.signal();
            }
        }
        finally {
            monLock.unlock();
        }
    }

    public void enterWriter() throws InterruptedException {
        monLock.lock();
        try {
            while (writersActive != 0 || readersActive != 0) {
                writersWaiting++;
                okToWrite.await();
                writersWaiting--;
            }
            writersActive++;
        }
        finally {
            monLock.unlock();
        }
    }

    public void exitWriter()
    {
        monLock.lock();
        try {
            writersActive--;
            if (writersWaiting != 0) {
                okToWrite.signal();
            }
            else
                {
                okToRead.signalAll();
            }
        }
        finally {
            monLock.unlock();
        }
    }

}
