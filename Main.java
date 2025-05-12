import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) {
/*TESTANDO-CHAN */
        /*RUBY CHAN!!! NANI GA SUKI?????*/
        /*HAIIIIIII, CHOKO MINTO, NARE MA NA ANATA!!*/
        /*        Erick-chan é um Safado*/
        Menu menu = new Menu();
        Scanner scanner = new Scanner(System.in);
        Pessoa pessoa = new Pessoa();
        int OpcaoUsuario;
        do {
            System.out.println("\n--- Main Menu ---");
            menu.MontarMenu();
            OpcaoUsuario = menu.OpcaoEscolhida(1, 3, "Choose an option");

            switch (OpcaoUsuario) {
                case 1: // Customer Registration
                    System.out.println("\n--- Customer Registration Submenu ---");
                    menu.MontarMenu(2, 1);
                    int customerOption = menu.OpcaoEscolhida(1, 5, "Choose an option");
                    System.out.println("Option chosen in Customer Registration: " + customerOption);
                    if (customerOption == 1){}
                    else if (customerOption == 2) {}
                    else if (customerOption == 3) {}
                    else if  (customerOption == 4){
                        pessoa.Cadastro_Cliente();
                        pessoa.ImprimirCadastro();
                    }
                    else if (customerOption == 5){}
                    else{}
                    break;
                case 2: // Product Registration
                    System.out.println("\n--- Product Registration Submenu ---");
                    menu.MontarMenu(2, 2);
                    int productOption = menu.OpcaoEscolhida(1, 5, "Choose an option");
                    System.out.println("Option chosen in Product Registration: " + productOption);
                    break;
                case 3: // End
                    System.out.println("Ending the program.");
                    break;
                default:
                    System.out.println("Invalid option."); // This shouldn't happen due to validation
            }
        } while (OpcaoUsuario != 3);

        scanner.close();
    }
}