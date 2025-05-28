import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Menu menu_chan = new Menu();
        Scanner scanner_chan = new Scanner(System.in);
        Pessoa pessoa_chan = new Pessoa();
        Enderecos enderecos = new Enderecos();
        int OpcaoUsuario;
        do {
            System.out.println("\n=== Menu Primário ===");
            menu_chan.MontarMenu();
            OpcaoUsuario = menu_chan.OpcaoEscolhida(1, 3, "Escolha uma opção: ");

            int opcao_selecao;
            switch (OpcaoUsuario) {
                case 1:
                    System.out.println("\n--- Registro de Empresa ---");
                    menu_chan.MontarMenu(2, 1);
                    opcao_selecao = menu_chan.OpcaoEscolhida(1, 5, "Escolha uma opção");
                    System.out.println("Opção escolhida: " + opcao_selecao);
                        switch (opcao_selecao) {
                            case 1:
                                pessoa_chan.AtualizarCadastroCliente();
                            break;
                            case 2:
                                pessoa_chan.ConsultarCliente();
                            break;
                            case 3:
                                pessoa_chan.ExcluirCadastroCliente();
                            break;
                            case 4:
                                pessoa_chan.Cadastro_Cliente();
                                if (pessoa_chan.getPositivoid() == 1 && pessoa_chan.getPositivotipo() == 1) {
                                    pessoa_chan.ImprimirCadastro();
                                    /*enderecos.CadastroEndereco();*/
                                    pessoa_chan.GravarCadastroLog();
                                }
                                else {
                                    System.out.println("Falha ao adicionar, insira um id ou um tipo válido");
                                }

                            break;

                        }
                    break;
                case 2:
                    System.out.println("\n=== Menu de Registro de Produto ===");
                    menu_chan.MontarMenu(2, 2);
                    int opcao_produto = menu_chan.OpcaoEscolhida(1, 5, "Escolha uma opção");
                    System.out.println("Opção escolhida: " + opcao_produto);
                    Produto produto = new Produto();
                        switch (opcao_produto){

                            case 1:
                            break;
                            case 2:
                            break;
                            case 3:
                            break;
                            case 4:
                                produto.CadastrarProduto();
                            break;
                        }
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