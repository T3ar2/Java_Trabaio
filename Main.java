import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Menu menu_chan = new Menu();
        Scanner scanner = new Scanner(System.in);
        Pessoa pessoa_chan = new Pessoa();
        int OpcaoUsuario;
        do {
            System.out.println("\n=== Menu Primário ===");
            menu_chan.MontarMenu();
            OpcaoUsuario = menu_chan.OpcaoEscolhida(1, 4, "Escolha uma opção: ");

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
                                produto.AlterarProduto();
                            break;
                            case 2:
                                produto.ConsultarProduto();
                            break;
                            case 3:
                                produto.ExcluirProduto();
                            break;
                            case 4:
                                produto.CadastrarProduto();
                            break;
                        }
                    break;
                case 3:
                    System.out.println("\n=== Menu de Registro de Venda ===");
                    menu_chan.MontarMenu(2, 3);
                    int opcao_venda = menu_chan.OpcaoEscolhida(1, 5, "Escolha uma opção");
                    System.out.println("Opção escolhida: " + opcao_venda);
                    PedidoVenda pedidovenda = new PedidoVenda();
                    switch(opcao_venda){
                        case 1:
                            pedidovenda.AlterarVenda();
                        break;
                        case 2:
                        break;
                        case 3:
                            pedidovenda.ExcluirVenda();
                        break;
                        case 4:
                            pedidovenda.CadastroVenda();
                        break;
                    }
                    break;
                case 4:
                    System.out.println("Programa Fechando.");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
        while (OpcaoUsuario != 4) ;
        scanner.close();
    }
}