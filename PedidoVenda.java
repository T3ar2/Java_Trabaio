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

public class PedidoVenda {
    private int idVenda;
    private int clienteId;
    private double total;
    private String logradouro;
    private String num_casa;
    private String tipoCliente;

    private static class ItemVenda {
        int idProduto;
        String nomeProduto;
        int quantidade;
        double precoUnitario;

        public ItemVenda(int idProduto, String nomeProduto, int quantidade, double precoUnitario) {
            this.idProduto = idProduto;
            this.nomeProduto = nomeProduto;
            this.quantidade = quantidade;
            this.precoUnitario = precoUnitario;
        }

        public double getSubtotal() {
            return this.quantidade * this.precoUnitario;
        }

        @Override
        public String toString() {
            return String.format("[ID:%d, Prod:%s, Qtd:%d, Preço:R$%.2f]",
                    idProduto, nomeProduto, quantidade, precoUnitario);
        }
    }

    private List<ItemVenda> carrinho;

    public PedidoVenda() {
        this.carrinho = new ArrayList<>();
    }

    private String[] getDadosProdutoPorId(int idProduto) {
        try (BufferedReader reader = new BufferedReader(new FileReader("OutputProduto.txt"))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.startsWith("Id: " + idProduto + ";")) {
                    String[] partes = linha.split(";");
                    if (partes.length >= 5) {
                        String nome = partes[1].replace("Nome:", "").trim();
                        String precoStr = partes[4].replaceAll("[^0-9.]", "");
                        return new String[]{nome, precoStr};
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Erro ao buscar dados do produto: " + e.getMessage());
        }
        return null;
    }

    private String getTipoPessoaPorId(int idCliente) {
        try (BufferedReader reader = new BufferedReader(new FileReader("OutputPessoas.txt"))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.startsWith("Id: " + idCliente + ";")) {
                    String[] partes = linha.split(";");
                    if (partes.length >= 3) {
                        return partes[2].replace("Tipo:", "").trim();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao buscar tipo de pessoa: " + e.getMessage());
        }
        return "";
    }

    public void CadastroVenda() {
        total = 0.00;
        carrinho.clear();
        Scanner scanner = new Scanner(System.in);

        int verificadorIntPedido;
        while (true) {
            System.out.println("Número do pedido:");
            verificadorIntPedido = scanner.nextInt();
            scanner.nextLine();
            if (verificadorIntPedido > 0 && verificadorIntPedido <= 999999 && !idExiste(verificadorIntPedido)) {
                this.idVenda = verificadorIntPedido;
                break;
            } else {
                System.out.println("ID de pedido inválido ou já existente. Tente novamente.");
            }
        }
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
                            } catch (NumberFormatException e) { System.out.println("ID inválido no arquivo: " + partes[0]); }
                        }
                    }
                }
            } catch (IOException e) { System.out.println("Erro ao ler os IDs de clientes: " + e.getMessage());}
            System.out.print("\nPara selecionar, digite o Id do cliente: ");
            int idClienteEscolhido = scanner.nextInt();
            scanner.nextLine();
            Confirmar(scanner);
            if (listaIdsClientes.contains(idClienteEscolhido)) {
                this.clienteId = idClienteEscolhido;
                this.tipoCliente = getTipoPessoaPorId(this.clienteId);
                BuscarEnderecoDoCliente(this.clienteId);
                System.out.println("Cliente com ID " + idClienteEscolhido + " selecionado com sucesso.");
                idValido = 1;
            } else {
                System.out.println("Erro: Não existe cliente com esse ID! Tente novamente.");
            }
        }
        
        while (true) {
            System.out.println("\n--- Adição de Produtos ao Carrinho ---");
            Printprodutos();
            System.out.print("Digite o ID do produto a adicionar (ou 0 para finalizar): ");
            int idProdutoEscolhido = scanner.nextInt();
            scanner.nextLine();

            if (idProdutoEscolhido == 0) {
                break;
            }

            String[] dadosProduto = getDadosProdutoPorId(idProdutoEscolhido);

            if (dadosProduto != null) {
                String nomeProduto = dadosProduto[0];
                double precoProduto = Double.parseDouble(dadosProduto[1]);
                
                System.out.print("Digite a quantidade para o produto '" + nomeProduto + "': ");
                int quantidade = scanner.nextInt();
                scanner.nextLine();

                if (quantidade > 0) {
                    ItemVenda novoItem = new ItemVenda(idProdutoEscolhido, nomeProduto, quantidade, precoProduto);
                    carrinho.add(novoItem);
                    total += novoItem.getSubtotal();
                    System.out.println("==> Adicionado(s) " + quantidade + "x " + nomeProduto + " ao carrinho.");
                } else {
                    System.out.println("Quantidade inválida. O item não foi adicionado.");
                }
            } else {
                System.out.println("ERRO: Produto com ID " + idProdutoEscolhido + " não encontrado.");
            }
            Confirmar(scanner);
        }

        System.out.println("\n--- Resumo do Pedido ---");
        for (ItemVenda item : carrinho) {
            System.out.printf("Item: %s (ID: %d) | Qtd: %d | Preço Unit: R$%.2f | Subtotal: R$%.2f\n",
                    item.nomeProduto, item.idProduto, item.quantidade, item.precoUnitario, item.getSubtotal());
        }
        System.out.printf("TOTAL DO PEDIDO: R$%.2f\n", total);
        
        GravarCadastroVendaLog();
        ImprimirCadastroVenda();
    }
    
    public void ImprimirCadastroVenda() {
        try (FileWriter fileWriter = new FileWriter("OutputVendas.txt", true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            
            String listaDeProdutosComColchetesDuplos = this.carrinho.toString();
            String textoFinalFormatado = "";

            if (listaDeProdutosComColchetesDuplos.length() > 2) {
                textoFinalFormatado = listaDeProdutosComColchetesDuplos.substring(1, listaDeProdutosComColchetesDuplos.length() - 1);
            }

            bufferedWriter.write(
                "Id do pedido: " + this.idVenda + ";" +
                "Id do Cliente: " + this.clienteId + ";" +
                "Logadouro: " + this.logradouro + ";" +
                "Tipo cliente: " + this.tipoCliente + ";" +
                "Número: " + this.num_casa + ";" +
                "Lista de produtos: " + textoFinalFormatado + ";" +
                "Total: R$" + String.format("%.2f", this.total) + ";"
            );
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
                if (partes.length >= 4) {
                    try {
                        int idNoArquivo = Integer.parseInt(partes[0].replaceAll("[^0-9]", ""));
                        if (idNoArquivo == idCliente) {
                            this.logradouro = partes[2].replace("Logadouro:", "").trim();
                            this.num_casa = partes[3].replace("Número:", "").trim();
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
                this.logradouro = "N/A";
                this.num_casa = "N/A";
            }

        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo de endereços: " + e.getMessage());
        }
    }
    
    public void Printprodutos() {
        try (BufferedReader leitor = new BufferedReader(new FileReader("OutputProduto.txt"))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                System.out.println(linha.replace(";", " "));
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }
    }

    public boolean idExiste(int idVerificar) {
        try (BufferedReader reader = new BufferedReader(new FileReader("OutputVendas.txt"))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.startsWith("Id do pedido: " + idVerificar + ";")) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao verificar ID existente: " + e.getMessage());
        }
        return false;
    }

    public void ExcluirVenda() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o ID do pedido de venda a ser excluído: ");
        int idBusca = scanner.nextInt();
        scanner.nextLine();

        boolean encontrado = false;
        StringBuilder novoConteudo = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader("OutputVendas.txt"))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.startsWith("Id do pedido: " + idBusca + ";")) {
                    encontrado = true;
                    System.out.println("Pedido de venda encontrado: " + linha);
                    System.out.print("Tem certeza que deseja excluir este pedido? (s para sim e n para não): ");
                    String confirmacao = scanner.nextLine();

                    if (confirmacao.equalsIgnoreCase("s")) {
                        try (BufferedWriter logWriter = new BufferedWriter(new FileWriter("Log.txt", true))) {
                            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                            logWriter.write("[" + timestamp + "] usuário admin excluiu a venda com o id " + idBusca + ".");
                            logWriter.newLine();
                        }
                        System.out.println("Pedido de venda excluído com sucesso.");
                    } else {
                        novoConteudo.append(linha).append(System.lineSeparator());
                        System.out.println("Exclusão cancelada.");
                    }
                } else {
                    novoConteudo.append(linha).append(System.lineSeparator());
                }
            }

            if (encontrado) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("OutputVendas.txt"))) {
                    writer.write(novoConteudo.toString());
                }
            } else {
                System.out.println("Pedido de venda com ID " + idBusca + " não encontrado.");
            }
        } catch (IOException e) {
            System.err.println("Erro ao excluir o pedido de venda: " + e.getMessage());
        }
    }
}