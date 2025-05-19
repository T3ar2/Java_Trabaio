import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        /*TESTANDO-CHAN */
        /*RUBY CHAN!!! NANI GA SUKI?????*/
        /*HAIIIIIII, CHOKO MINTO, NARE MA NA ANATA!!*/
        /*Erick-chan é um Safado*/
        Menu menu_chan = new Menu();
        Scanner scanner_chan = new Scanner(System.in);
        Pessoa pessoa_chan = new Pessoa();
        int OpcaoUsuario;
        do {
            System.out.println("\n=== Menu Primário ===");
            menu_chan.MontarMenu();
            OpcaoUsuario = menu_chan.OpcaoEscolhida(1, 3, "Escolha uma opção: ");

            int opcao_selecao;
            switch (OpcaoUsuario) {
                case 1: // Customer Registration
                    System.out.println("\n--- Registro de Empresa ---");
                    menu_chan.MontarMenu(2, 1);
                    opcao_selecao = menu_chan.OpcaoEscolhida(1, 5, "Choose an option");
                    System.out.println("Escolha uma opção: " + opcao_selecao);
                    if (opcao_selecao == 1) {
                        pessoa_chan.AtualizarCadastroCliente();
                    } else if (opcao_selecao == 2) {
                        pessoa_chan.ConsultarCliente();
                    } else if (opcao_selecao == 3) {
                    } else if (opcao_selecao == 4) {
                        pessoa_chan.Cadastro_Cliente();
                        pessoa_chan.ImprimirCadastro();
                        pessoa_chan.GravarCadastroLog();

                    }
                         else if (opcao_selecao == 5) {
                    }
                    break;
                case 2:
                    System.out.println("\n=== Menu de Registro de Produto ===");
                    menu_chan.MontarMenu(2, 2);
                    int opcao_produto = menu_chan.OpcaoEscolhida(1, 5, "Escolha uma opção");
                    break;
                case 3:
                    System.out.println("Programa Fechando.");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
        while (OpcaoUsuario != 3) ;
        scanner_chan.close();
    }
}