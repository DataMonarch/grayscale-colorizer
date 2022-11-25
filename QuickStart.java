class QuickStart {
    /**
     * @param args
     * @throws InterruptedException
     */
    public static void main (String[] args) throws InterruptedException {

        // String filename = args[0];
        // int sq_size = Integer.parseInt(args[1]);
        // String processing_mode = args[2];

        // System.out.println(filename);
        // System.out.println(sq_size);
        // System.out.println(processing_mode);

        for (int i = 0; i < 5; i++) {
            MultiThreadedThing myThing = new MultiThreadedThing(i);
            Thread myThread = new Thread(myThing);
            myThread.start();
            myThread.join();
        }        

    }
}

