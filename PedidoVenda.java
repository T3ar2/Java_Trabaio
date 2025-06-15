import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PedidoVenda {
    private int idVenda;
    private int clienteId;
    private String enderecos;
    private String produtos;
    private double total;
    private String num_casa;

    public void setIdVenda(int idVenda) {
        this.idVenda = idVenda;
    }

    public void setProdutos(String produtos) {
        this.produtos = produtos;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getTotal() {
        return total;
    }

    public int getIdVenda() {
        return idVenda;
    }

    public String getProdutos() {
        return produtos;
    }

    public void setNum_casa(String num_casa) {
        this.num_casa = num_casa;
    }
    ArrayList<String> produtosVendidos = new ArrayList<>();
    ArrayList<Double> precosVenda = new ArrayList<>();
    public void CadastroVenda() {
        total = 0.00;
        Scanner scanner = new Scanner(System.in);
        int verificadorIntPedido;
        while (true) {
            System.out.println("Número do pedido:");
            verificadorIntPedido = scanner.nextInt();
            scanner.nextLine();

            if (verificadorIntPedido <= 0 || verificadorIntPedido > 999999) {
                System.out.println("ID inválido. Digite um número entre 1 e 999999.");
            } else if (idExiste(verificadorIntPedido)) {
                System.out.println("Este ID já está em uso. Por favor, insira outro ID.");
            } else {
                break;
            }
        }
        setIdVenda(scanner.nextInt());
        scanner.nextLine();
        Confirmar(scanner);


        int idValido = 0;
        while (idValido != 1) {
            System.out.println("Tabela de Clientes disponível: \n");
            PrintCliente();
            List<Integer> listaIdsClientes = new ArrayList<>();
            try (BufferedReader leitor = new BufferedReader(new FileReader("OutputPessoas.txt"))) {
                String linha;
                while ((linha = leitor.readLine()) != null) {
                    if (linha.contains("Tipo: cliente") || linha.contains("Tipo: ambos")) {
                        String[] partes = linha.split(";");
                        if (partes.length > 0) {
                            try {
                                int id = Integer.parseInt(partes[0].replaceAll("[^0-9]", ""));
                                listaIdsClientes.add(id);
                            } catch (NumberFormatException e) {
                                System.out.println("ID inválido no arquivo: " + partes[0]);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Erro ao ler os IDs de clientes: " + e.getMessage());
            }
            System.out.print("\nPara selecionar, digite o Id do cliente: ");
            int idClienteEscolhido = scanner.nextInt();
            scanner.nextLine();
            Confirmar(scanner);

            if (listaIdsClientes.contains(idClienteEscolhido)) {
                clienteId = idClienteEscolhido;
                System.out.println("Cliente com ID " + idClienteEscolhido + " selecionado com sucesso.");
                idValido = 1;
            } else {
                System.out.println("Erro: Não existe cliente com esse ID! Tente novamente.");
            }
        }
        BuscarEnderecoDoCliente(clienteId);

        System.out.print("Digite o número de itens que deseja inserir no carrinho: ");
        int itens = scanner.nextInt();
        scanner.nextLine();
        Confirmar(scanner);


            for (int i = 0; i < itens; i++) {

                System.out.println("Lista de produtos dísponível: ");
                Printprodutos();
                System.out.print("insira o nome do produto para adicionar ao carrinho: ");
                String letProduto = scanner.nextLine();
                System.out.print("Insira o preço de custo do produto: ");
                double PProduto = scanner.nextDouble();
                scanner.nextLine();
                Confirmar(scanner);
                total += PProduto;
                produtosVendidos.add(letProduto);
                precosVenda.add(PProduto);
            }

            System.out.println("Montante: ");
        for (int i = 0; i < produtosVendidos.size(); i++) {
            String nomeFormatado = produtosVendidos.get(i).replace("[", "").replace("]", "");
            String precoFormatado = String.format("R$%.1f", precosVenda.get(i));

            System.out.println("Produto: " + nomeFormatado + " " + precoFormatado);
        }
        System.out.println("TOTAL: R$" + total);
        GravarCadastroVendaLog();
        ImprimirCadastroVenda();

    }

    public void ImprimirCadastroVenda() {
        try (FileWriter fileWriter = new FileWriter("OutputVendas.txt", true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("Id do pedido: " + idVenda + ";" +
                    "Id do Cliente: " + clienteId + ";" + enderecos + ";" + num_casa + ";"+
                    "Lista de produto: " + produtosVendidos + ";" +
                    "Total: R$" + total + ";");
            bufferedWriter.newLine();
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }

    public void GravarCadastroVendaLog() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("Venda de id " + idVenda + " Cadastrado com sucesso, verifique o arquivo OutputVendas para visualizar seu cadastro. ");
        try (FileWriter fileWriter = new FileWriter("Log.txt", true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("[" + timestamp + "] " +
                    "usuário admin cadastrou a venda de id " + idVenda + " no banco de dados.");
            bufferedWriter.newLine();
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }

    public void Confirmar(Scanner scanner) {
        System.out.println("Aperte ENTER para confirmar. ");
        scanner.nextLine();
    }

    public void PrintCliente() {
        try (BufferedReader leitor = new BufferedReader(new FileReader("OutputPessoas.txt"))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                String linhaSemPontoEVirgula = linha.replace(";", " ");
                if (linha.contains("Tipo: cliente") || linha.contains("Tipo: ambos")) {
                    System.out.println(linhaSemPontoEVirgula);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }
    }

    private void BuscarEnderecoDoCliente(int idCliente) {
        try (BufferedReader leitor = new BufferedReader(new FileReader("EnderecoOutput.txt"))) {
            String linha;
            boolean enderecoEncontrado = false;

            while ((linha = leitor.readLine()) != null) {
                String[] partes = linha.split(";");
                if (partes.length >= 2) {
                    try {
                        int idNoArquivo = Integer.parseInt(partes[0].replaceAll("[^0-9]", ""));
                        if (idNoArquivo == idCliente) {
                            String logradouro = partes[2];
                            num_casa = partes[3];
/*                            System.out.println(logradouro + num_casa);*/
                            enderecos = logradouro;
                            enderecoEncontrado = true;
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Formato de ID inválido no arquivo de endereços: " + partes[0]);
                    }
                }
            }

            if (!enderecoEncontrado) {
                System.out.println("Nenhum endereço encontrado para o cliente com ID: " + idCliente);
            }

        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo de endereços: " + e.getMessage());
        }
    }
    public void Printprodutos() {
        try (BufferedReader leitor = new BufferedReader(new FileReader("OutputProduto.txt"))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                String linhaSemPontoEVirgula = linha.replace(";", " ");
                    System.out.println(linhaSemPontoEVirgula);
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }
    }

    public boolean idExiste(int idVerificar) {
        try (BufferedReader reader = new BufferedReader(new FileReader("OutputVendas.txt"))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.startsWith("Id: " + idVerificar + ";")) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao verificar ID existente: " + e.getMessage());
        }
        return false;
    }

}