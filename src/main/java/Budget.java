package main.java;

import java.util.Scanner;

public class Budget {
    Scanner scanner = new Scanner(System.in);
    double balance;
    boolean isRunning = true;
    int coiche;

    public Budget() {


        System.out.println("1. Show balance");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. exit");

        while(isRunning){
            switch(coiche){
                case 1: showBalance(balance);
                break;
                case 2: balance += makeDeposit();
                break;
                case 3: balance -= withDraw(balance);
                break;
                case 4: isRunning = false;
                default:
                    System.out.println("Invalid choice");

            }
        }

    }

    public  void showBalance(double balance){
        System.out.printf("kr%f");
    }

    public double makeDeposit(){
        double amount;
        System.out.println("Enter amount to deposit");
        amount = scanner.nextDouble();

        if(amount < 0){
            System.out.println("Amount cant be negative");
            return 0;
        } else {
            return amount;
        }
    }

    public double withDraw(double balance){
        double amount;
        System.out.println("Enter amount to withdraw");
        amount = scanner.nextDouble();

        if(amount > balance){
            System.out.println("invalid");
            return 0;
        } else if (amount < 0) {
            System.out.println("invalid");
            return 0;
        }else {
            return amount;
        }
    }


}
