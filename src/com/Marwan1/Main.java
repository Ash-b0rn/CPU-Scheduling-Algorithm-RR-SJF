package com.Marwan1;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        String choice = "", choice1 = "";
        int quantum = 0;

            System.out.println("R for RR and S for SJF.");
            choice = s.next().toLowerCase(Locale.ROOT);
            if (choice.equals("r")){
                System.out.println("Enter the quantum desired.");
                quantum = s.nextInt();
            }

        if (choice.equals("s")){
            System.out.println("SJF preemptive or non\nP for preemptive and N for non");
            choice1 = s.next().toLowerCase(Locale.ROOT);
        }

        System.out.println("Enter the number of processes: ");
        int n = s.nextInt(), l, k;

        int [] ArrivalT = new int[n];
        int [] BurstT = new int[n];
        int [] CompletionT = new int[n];
        int [] WaitT = new int[n];
        int [] TurnaroundT = new int[n];
        int [] RemainingT = new int[n];

        for (int i = 0; i < n; i++){
            l = i; k = l + 1;
            System.out.println("Process number " + k + ".\nEnter the arrival time: ");
            ArrivalT[i] = s.nextInt();
            System.out.println("Enter the burst time: ");
            BurstT[i] = s.nextInt();
            RemainingT[i] = BurstT[i];

        }


        if (choice.equals("s")) {
                if (choice1.equals("n")) {
                    SJF_non(ArrivalT, BurstT, CompletionT, WaitT, TurnaroundT, RemainingT, n);
                } else if (choice1.equals("p")) {
                    SJF(ArrivalT, BurstT, CompletionT, WaitT, TurnaroundT, RemainingT, n);
                }
        }else if (choice.equals("r")){
            RR(ArrivalT, BurstT, CompletionT, WaitT, TurnaroundT, RemainingT, n, quantum);
        }


    }

    public static void RR (int [] ArrivalT, int [] BurstT, int [] CompletionT, int [] WaitT, int [] TurnaroundT, int [] RemainingT, int n, int quantum){
        int count = 0, totalWT = 0, totalTT = 0, completed = 0, index = 0, currentT = 0;
        List<Integer> [] wait = new List[n];

        for (int i = 0; i < n; i++){
            wait[i] = new LinkedList<>();
        }

        while (completed != 5){
            if (count == quantum){
                if (wait[index].isEmpty()) {
                    wait[index].add(Math.abs(currentT - count));
                }else if (!wait[index].isEmpty()){
                    wait[index].add(Math.abs(currentT - count));
                }
                count %= quantum;
                index++;
            }
            index %= n;

            if (RemainingT[index] == -1){
                index++;
                count = 0;
                continue;
            }

            if (ArrivalT[index] <= currentT){
                currentT++;
                RemainingT[index]--;
                count++;
            }

            if (RemainingT[index] == 0) {
                CompletionT[index] = currentT;
                wait[index].add(Math.abs(currentT - count));
                WaitT[index] = add(wait[index]);
                TurnaroundT[index] = WaitT[index] + BurstT[index];
                totalWT += WaitT[index];
                totalTT += TurnaroundT[index];
                RemainingT[index] = -1;
                completed++;
                index++;
                count = 0;
            }

        }
        print(n, ArrivalT, BurstT, CompletionT, WaitT, TurnaroundT, totalWT, totalTT);
    }

    public static int add(List<Integer> list){
        int size = list.size(), total = 0;
        for (int i = 0; i < size - 1; i++){
            total += (Math.abs(list.get(i) - list.get(i + 1)) - 2);
        }
        return total;
    }

    public static void SJF(int [] ArrivalT, int [] BurstT, int [] CompletionT, int [] WaitT, int [] TurnaroundT, int [] RemainingT, int n){
        int currentT = 0, completed = 0, totalWT = 0, totalTT = 0;
        while (completed != n){
                int shortestT = Integer.MAX_VALUE, shortestP = -1;

                for (int i = 0; i < n; i++) {
                    if (ArrivalT[i] <= currentT && RemainingT[i] < shortestT && RemainingT[i] > 0) {
                        shortestT = RemainingT[i];
                        shortestP = i;
                    }
                }

            if (shortestP != -1) {
                RemainingT[shortestP]--;
                currentT++;
            }else {
                continue;
            }

            if (RemainingT[shortestP] == 0){
                completed++;
                CompletionT[shortestP] = currentT;

                WaitT[shortestP] = CompletionT[shortestP] - ArrivalT[shortestP] - BurstT[shortestP];
                TurnaroundT[shortestP] = CompletionT[shortestP] - ArrivalT[shortestP];
                totalWT += WaitT[shortestP];
                totalTT += TurnaroundT[shortestP];
            }

        }

        print(n, ArrivalT, BurstT, CompletionT, WaitT, TurnaroundT, totalWT, totalTT);
    }

    public static void SJF_non(int [] ArrivalT, int [] BurstT, int [] CompletionT, int [] WaitT, int [] TurnaroundT, int [] RemainingT, int n){
        int currentT = 0, completed = 0, totalWT = 0, totalTT = 0, count = 0, shortestT, shortestP = -1;

        while (completed != n){
            if (count == 0) {
                shortestT = Integer.MAX_VALUE;
                shortestP = -1;
                for (int i = 0; i < n; i++) {
                    if (ArrivalT[i] <= currentT && RemainingT[i] < shortestT && RemainingT[i] > 0) {
                        shortestT = RemainingT[i];
                        shortestP = i;
                    }
                }
            }else {
                if (RemainingT[shortestP] == 0) {
                    shortestT = Integer.MAX_VALUE;
                    shortestP = -1;
                    for (int i = 0; i < n; i++) {
                        if (ArrivalT[i] <= currentT && RemainingT[i] < shortestT && RemainingT[i] > 0) {
                            shortestT = RemainingT[i];
                            shortestP = i;
                        }
                    }
                }
            }

            if (shortestP != -1) {
                RemainingT[shortestP]--;
                currentT++;
            }else {
                continue;
            }

            if (RemainingT[shortestP] == 0){
                completed++;
                CompletionT[shortestP] = currentT;

                WaitT[shortestP] = CompletionT[shortestP] - ArrivalT[shortestP] - BurstT[shortestP];
                TurnaroundT[shortestP] = WaitT[shortestP] + BurstT[shortestP];
                totalWT += WaitT[shortestP];
                totalTT += TurnaroundT[shortestP];
            }


            count++;
        }

        print(n, ArrivalT, BurstT, CompletionT, WaitT, TurnaroundT, totalWT, totalTT);
    }

    public static void print(int n, int [] ArrivalT, int [] BurstT, int [] CompletionT, int [] WaitT, int [] TurnaroundT, int totalWT, int totalTT){
        System.out.println("\nProcess\t Arrival Time\t Burst Time\t Completion Time\t Waiting Time\t Turnaround Time");
        for (int i = 0; i < n; i++) {
            System.out.println((i+1) + "\t\t\t\t" + ArrivalT[i] + "\t\t\t\t" + BurstT[i] + "\t\t\t\t" + CompletionT[i] + "\t\t\t\t" + WaitT[i] + "\t\t\t\t" + TurnaroundT[i]);
        }
        double avgWT = (double) totalWT/ (double) n;
        double avgTT = (double) totalTT/(double) n;
        System.out.println("\nAverage waiting time: " + avgWT);
        System.out.println("Average turnaround time: " + avgTT);

    }

}
