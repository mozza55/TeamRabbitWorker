public class WorkerThread implements Runnable {
    Worker worker;
    int workerNumber;
    Byte taskType;

    public WorkerThread(Worker worker, int workerNumber, Byte taskType) {
        this.worker = worker;
        this.workerNumber = workerNumber;
        this.taskType = taskType;
    }

    @Override
    public void run() {
        //System.out.printf("%dth worker :run\n",workerNumber);
        try {
            worker.connect("localhost",8080,workerNumber,taskType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
