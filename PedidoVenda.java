import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PedidoVenda {

    private int idVenda;
    private int clienteId;
    private double total;
    private String logradouro;
    private String num_casa;
    private String tipoCliente;
    private List<ItemVenda> carrinho;

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

    public PedidoVenda() {
        this.carrinho = new ArrayList<>();
    }

    //=========================================================================
    // --- MÉTODOS PRINCIPAIS (CRUD) ---
    //=========================================================================

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

        adicionarProdutoAoCarrinho(scanner);

        System.out.println("\n--- Resumo do Pedido ---");
        exibirCarrinhoAtual();

        GravarCadastroVendaLog();
        ImprimirCadastroVenda();
    }

    public void AlterarVenda() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o ID do pedido de venda que deseja alterar: ");
        int idBusca = scanner.nextInt();
        scanner.nextLine(); // Limpa o buffer

        List<String> linhasArquivo = new ArrayList<>();
        boolean encontrado = false;
        int linhaParaAlterar = -1;

        try (BufferedReader reader = new BufferedReader(new FileReader("OutputVendas.txt"))) {
            String linha;
            int i = 0;
            while ((linha = reader.readLine()) != null) {
                linhasArquivo.add(linha);
                if (linha.startsWith("Id do pedido: " + idBusca + ";")) {
                    encontrado = true;
                    linhaParaAlterar = i;
                }
                i++;
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo de vendas: " + e.getMessage());
            return;
        }

        if (!encontrado) {
            System.out.println("Pedido com ID " + idBusca + " não encontrado.");
            return;
        }

        String linhaOriginal = linhasArquivo.get(linhaParaAlterar);
        String[] partes = linhaOriginal.split(";");

        this.carrinho = parseCarrinho(partes[5].replace("Lista de produtos: ", ""));
        this.idVenda = Integer.parseInt(partes[0].replaceAll("[^0-9]", ""));
        this.clienteId = Integer.parseInt(partes[1].replaceAll("[^0-9]", ""));
        this.logradouro = partes[2].replace("Logadouro:", "").trim();
        this.tipoCliente = partes[3].replace("Tipo cliente:", "").trim();
        this.num_casa = partes[4].replace("Número:", "").trim();
        recalcularTotal();

        int escolha;
        do {
            System.out.println("\n--- Alterando Pedido ID: " + this.idVenda + " ---");
            exibirCarrinhoAtual();
            System.out.println("1 - Adicionar Produto");
            System.out.println("2 - Remover Produto");
            System.out.println("3 - Alterar Quantidade de um Produto");
            System.out.println("4 - Salvar e Sair");
            System.out.print("Escolha uma opção: ");
            escolha = scanner.nextInt();
            scanner.nextLine();

            switch (escolha) {
                case 1:
                    adicionarProdutoIndividual(scanner);
                    break;
                case 2:
                    removerProdutoDoCarrinho(scanner);
                    break;
                case 3:
                    alterarQuantidadeProduto(scanner);
                    break;
                case 4:
                    System.out.println("Salvando alterações...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
            recalcularTotal();
        } while (escolha != 4);

        String carrinhoFormatado = this.carrinho.toString();
        if (carrinhoFormatado.length() > 2) {
            carrinhoFormatado = carrinhoFormatado.substring(1, carrinhoFormatado.length() - 1);
        }
        String novaLinha = "Id do pedido: " + this.idVenda + ";" +
                "Id do Cliente: " + this.clienteId + ";" +
                "Logadouro: " + this.logradouro + ";" +
                "Tipo cliente: " + this.tipoCliente + ";" +
                "Número: " + this.num_casa + ";" +
                "Lista de produtos: " + carrinhoFormatado + ";" +
                "Total: R$" + String.format("%.2f", this.total) + ";";

        linhasArquivo.set(linhaParaAlterar, novaLinha);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("OutputVendas.txt"))) {
            for (String linha : linhasArquivo) {
                writer.write(linha);
                writer.newLine();
            }
            try (BufferedWriter logWriter = new BufferedWriter(new FileWriter("Log.txt", true))) {
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                logWriter.write("[" + timestamp + "] usuário admin alterou a venda com o id " + idBusca + ".");
                logWriter.newLine();
            }
            System.out.println("Pedido alterado com sucesso!");

        } catch (IOException e) {
            System.err.println("Erro ao salvar as alterações no arquivo: " + e.getMessage());
        }
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


    //=========================================================================
    // --- MÉTODOS AUXILIARES PARA ALTERAÇÃO DE VENDA ---
    //=========================================================================


    private void adicionarProdutoIndividual(Scanner scanner) {
        Printprodutos();
        System.out.print("Digite o ID do produto a adicionar: ");
        int idProdutoEscolhido = scanner.nextInt();
        scanner.nextLine();

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
                System.out.println("==> Adicionado(s) " + quantidade + "x " + nomeProduto + " ao carrinho.");
            } else {
                System.out.println("Quantidade inválida. O item não foi adicionado.");
            }
        } else {
            System.out.println("ERRO: Produto com ID " + idProdutoEscolhido + " não encontrado.");
        }
    }


    private void removerProdutoDoCarrinho(Scanner scanner) {
        if(carrinho.isEmpty()) {
            System.out.println("Não há produtos para remover.");
            return;
        }
        System.out.print("Digite o ID do produto a remover do carrinho: ");
        int idParaRemover = scanner.nextInt();
        scanner.nextLine();

        boolean removido = carrinho.removeIf(item -> item.idProduto == idParaRemover);

        if (removido) {
            System.out.println("Produto removido com sucesso.");
        } else {
            System.out.println("Produto com ID " + idParaRemover + " não encontrado no carrinho.");
        }
    }


    private void alterarQuantidadeProduto(Scanner scanner) {
        if(carrinho.isEmpty()) {
            System.out.println("Não há produtos para alterar.");
            return;
        }
        System.out.print("Digite o ID do produto que deseja alterar a quantidade: ");
        int idParaAlterar = scanner.nextInt();
        scanner.nextLine();

        for (ItemVenda item : carrinho) {
            if (item.idProduto == idParaAlterar) {
                System.out.print("Digite a nova quantidade para '" + item.nomeProduto + "': ");
                int novaQuantidade = scanner.nextInt();
                scanner.nextLine();

                if (novaQuantidade > 0) {
                    item.quantidade = novaQuantidade;
                    System.out.println("Quantidade alterada com sucesso.");
                } else {
                    System.out.println("Quantidade inválida. A alteração não foi feita.");
                }
                return;
            }
        }
        System.out.println("Produto com ID " + idParaAlterar + " não encontrado no carrinho.");
    }

    //=========================================================================
    // --- MÉTODOS AUXILIARES GERAIS (Parse, Cálculo, Busca, I/O) ---
    //=========================================================================


    private void adicionarProdutoAoCarrinho(Scanner scanner) {
        while (true) {
            System.out.println("\n--- Adição de Produtos ao Carrinho ---");
            Printprodutos();
            System.out.print("Digite o ID do produto a adicionar (ou 0 para finalizar): ");
            int idProdutoEscolhido = scanner.nextInt();
            scanner.nextLine();

            if (idProdutoEscolhido == 0) {
                break;
            }

            adicionarProdutoIndividual(scanner); // Reutiliza a lógica de adicionar um produto
            Confirmar(scanner);
        }
    }


    private List<ItemVenda> parseCarrinho(String carrinhoStr) {
        List<ItemVenda> carrinhoParseado = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\[(.*?)\\]");
        Matcher matcher = pattern.matcher(carrinhoStr);

        while (matcher.find()) {
            String itemStr = matcher.group(1);
            String[] props = itemStr.split(", ");

            try {
                int id = Integer.parseInt(props[0].split(":")[1]);
                String nome = props[1].split(":")[1];
                int qtd = Integer.parseInt(props[2].split(":")[1]);
                double preco = Double.parseDouble(props[3].split(":")[1].replace("R$", "").replace(",", "."));

                carrinhoParseado.add(new ItemVenda(id, nome, qtd, preco));
            } catch (Exception e) {
                System.err.println("Erro ao fazer o parse do item do carrinho: " + itemStr);
            }
        }
        return carrinhoParseado;
    }


    private void recalcularTotal() {
        this.total = 0;
        for (ItemVenda item : this.carrinho) {
            this.total += item.getSubtotal();
        }
    }


    private String[] getDadosProdutoPorId(int idProduto) {
        try (BufferedReader reader = new BufferedReader(new FileReader("OutputProduto.txt"))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.startsWith("Id: " + idProduto + ";")) {
                    String[] partes = linha.split(";");
                    if (partes.length >= 5) {
                        String nome = partes[1].replace("Nome: ", "").trim();
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

    //=========================================================================
    // --- MÉTODOS AUXILIARES DE INTERFACE (UI/CONSOLE) ---
    //=========================================================================

    private void exibirCarrinhoAtual() {
        System.out.println("\n--- Carrinho Atual ---");
        if (carrinho.isEmpty()) {
            System.out.println("O carrinho está vazio.");
        } else {
            for (ItemVenda item : carrinho) {
                System.out.printf("Item: %s (ID: %d) | Qtd: %d | Preço Unit: R$%.2f | Subtotal: R$%.2f\n",
                        item.nomeProduto, item.idProduto, item.quantidade, item.precoUnitario, item.getSubtotal());
            }
        }
        System.out.printf("TOTAL ATUAL DO PEDIDO: R$%.2f\n", this.total);
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

    public void Confirmar(Scanner scanner) {
        System.out.println("Aperte ENTER para confirmar. ");
        scanner.nextLine();
    }
}
