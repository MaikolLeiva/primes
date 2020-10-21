package cl.ucn.disc.hpc.primes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.lang3.time.StopWatch;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The main
 *
 * @autor: Maikol Leiva Carrasco
 */

public class main {

    private static final Logger log = LoggerFactory.getLogger(main.class);

    /**
     *
     *
     *  1. Escribir una función que retorne true/false si un numero es primo.
     *  2. Contar la cantidad de numeros primos que existen entre 2 y 100000.
     *  3. Escribir codigo que resuelva el punto 2, utilizando un nucleo, 2 nucleos, 4 nucles, ... n nucleos.
     *
     */



    public static void main(String[] args) throws InterruptedException {

        //el max
        final long MAX = 1000000;

        final StopWatch stopWatch = StopWatch.createStarted();

        log.debug("Starting the main");

        //El "Ejecutador"
        final ExecutorService executorService = Executors.newFixedThreadPool(16);

        // create the max runnables and pass to the executor
        for (int i = 1; i < MAX; i++) {
            executorService.submit(new PrimeTask(i));
        }

        //no recibe más tareas
        executorService.shutdown();

        //espera el tiempo asignado (1 hora por parámetro)
        if(executorService.awaitTermination(1, TimeUnit.HOURS)) {
            log.debug("Primes founded: {} in {}.", PrimeTask.getPrimes(), stopWatch);
        } else {

            //time
            log.info("Done in {}.", stopWatch);

        }



    }

    private static class PrimeTask implements Runnable{
        /**
         *
         */
        private final long number;

        /**
         * Contador
         */
        private final static AtomicInteger counter = new AtomicInteger(0);

        /**
         * Constructor
         * @param number
         */

        public PrimeTask(final long number){
            this.number = number;
        }

        /**
         *
         * @return la cantidad de pares
         */
        public static int getPrimes(){
            return counter.get();
        }


        /**
         * Run the code.
         */
        public void run() {
            //si es primo lo contamos
            if (isPrime(this.number)){
                counter.getAndIncrement();
            }

        }

        public static boolean isPrime(final long n){

            // Can't process negative numbers
            if (n<=0){
                throw new IllegalArgumentException("Error in n: can't procces negative numbers.");
            }
            if (n == 1){
                return false;
            }

            // Testing for all numbers
            for (long i = 2; i < n; i++) {
                if (n%i == 0){
                    return false;
                }
            }
            //TODO: probando el TODO
            return true;

        }
    }


}


