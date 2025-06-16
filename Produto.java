import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Produto extends Pessoa {

    // --- Atributos da Classe ---
    private int idProduto;
    private String nomeProduto;
    private String descricao;
    private double custo;
    private double precoVenda;
    private int CodFornecedor;
    private int verificadorIdProduto;
    private int verificadorCusto;
    private int verificadorPrecoVenda;

    //=========================================================================
    // --- GETTERS E SETTERS ---
    //=========================================================================

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }
    public int getIdProduto() {
        return idProduto;
    }
    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }
    public String getNomeProduto() {
        return nomeProduto;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setCusto(double custo) {
        this.custo = custo;
    }
    public double getCusto() {
        return custo;
    }
    public void setPrecoVenda(double precoVenda) {
        this.precoVenda = precoVenda;
    }
    public double getPrecoVenda() {
        return precoVenda;
    }
    public void setCodFornecedor(int codFornecedor) {
        CodFornecedor = codFornecedor;
    }
    public int getCodFornecedor() {
        return CodFornecedor;
    }
    public void setVerificadorIdProduto(int verificadorIdProduto) {
        this.verificadorIdProduto = verificadorIdProduto;
    }
    public void setVerificadorCusto(int verificadorCusto) {
        this.verificadorCusto = verificadorCusto;
    }
    public void setVerificadorPrecoVenda(int verificadorPrecoVenda) {
        this.verificadorPrecoVenda = verificadorPrecoVenda;
    }

    //=========================================================================
    // --- MÉTODOS PRINCIPAIS (CRUD) ---
    //=========================================================================

    public void CadastrarProduto() {
        do {
            System.out.print("iD do Produto: ");
            int num = scanner.nextInt();
            scanner.nextLine();
            if (num > 0 && num < 999999 && !idExiste(num)) {
                setIdProduto(num);
                Confirmar();
                setVerificadorIdProduto(1);
            } else if (idExiste(num)) {
                System.out.println("ID já existe. Por favor, insira um ID único.");
            } else {
                System.out.println("Id inserido incorretamente. Por favor, insira um número válido.");
            }
        } while (verificadorIdProduto != 1);

        System.out.print("Nome do produto: ");
        setNomeProduto(scanner.nextLine());
        Confirmar();

        System.out.print("Descrição: ");
        setDescricao(scanner.nextLine());
        Confirmar();

        do {
            System.out.print("Custo: R$");
            double preco = scanner.nextDouble();
            scanner.nextLine();
            if (preco > 0) {
                setCusto(preco);
                Confirmar();
                setVerificadorCusto(1);
            } else {
                System.out.print("O valor de custo inserido é inválido.");
            }
        } while (verificadorCusto != 1);

        do {
            System.out.print("Venda: R$");
            double preco2 = scanner.nextDouble();
            scanner.nextLine();
            if (preco2 > 0) {
                setPrecoVenda(preco2);
                Confirmar();
                setVerificadorPrecoVenda(1);
            } else {
                System.out.println("O valor de custo inserido é inválido.");
            }
        } while (verificadorPrecoVenda != 1);

        do {
            System.out.print("Código do Fornecedor (ID de pessoa já cadastrada): ");
            int cod = scanner.nextInt();
            scanner.nextLine();
            if (idExistefornecedor(cod)) {
                setCodFornecedor(cod);
                Confirmar();
                break;
            } else {
                System.out.println("Fornecedor com esse ID não encontrado. Cadastre a pessoa primeiro.");
            }
        } while (true);

        ImprimirCadastro();
        GravarCadastroLog();
    }

    public void AlterarProduto() {
        System.out.print("Digite o ID do produto que deseja alterar: ");
        int idBusca = scanner.nextInt();
        scanner.nextLine();

        boolean encontrado = false;
        StringBuilder novoConteudo = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader("OutputProduto.txt"))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.startsWith("Id: " + idBusca)) {
                    encontrado = true;
                    System.out.println("Produto encontrado: " + linha);

                    System.out.print("Digite o novo nome do produto: ");
                    String novoNome = scanner.nextLine();

                    System.out.print("Digite a nova descrição do produto: ");
                    String novaDescricao = scanner.nextLine();

                    double novoCusto = 0;
                    boolean custoValido = false;
                    do {
                        System.out.print("Digite o novo custo do produto: R$");
                        try {
                            novoCusto = Double.parseDouble(scanner.nextLine());
                            if (novoCusto > 0) {
                                custoValido = true;
                            } else {
                                System.out.println("Valor inválido, deve ser maior que zero.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Entrada inválida, digite um número válido.");
                        }
                    } while (!custoValido);

                    double novoPrecoVenda = 0;
                    boolean precoValido = false;
                    do {
                        System.out.print("Digite o novo preço de venda do produto: R$");
                        try {
                            novoPrecoVenda = Double.parseDouble(scanner.nextLine());
                            if (novoPrecoVenda > 0) {
                                precoValido = true;
                            } else {
                                System.out.println("Valor inválido, deve ser maior que zero.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Entrada inválida, digite um número válido.");
                        }
                    } while (!precoValido);

                    String[] partes = linha.split(";");
                    String codFornecedor = partes.length > 5 ? partes[5] : "";

                    String novaLinha = "Id: " + idBusca + ";" + "Nome: " + novoNome + ";" + "Descrição: " + novaDescricao + ";" + "Custo: R$" + String.format("%.2f", novoCusto) + ";" + "Preço de Venda: R$" + String.format("%.2f", novoPrecoVenda) + ";" + codFornecedor + ";";
                    novoConteudo.append(novaLinha).append("\n");

                    try (BufferedWriter logWriter = new BufferedWriter(new FileWriter("Log.txt", true))) {
                        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        logWriter.write("[" + timestamp + "] usuário admin alterou os dados do produto ID " + idBusca + ".");
                        logWriter.newLine();
                    }

                } else {
                    novoConteudo.append(linha).append("\n");
                }
            }

            if (encontrado) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("OutputProduto.txt"))) {
                    writer.write(novoConteudo.toString());
                }
                System.out.println("Cadastro do produto atualizado com sucesso!");
            } else {
                System.out.println("Produto com ID " + idBusca + " não encontrado.");
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler ou escrever o arquivo: " + e.getMessage());
        }
    }

    public void ConsultarProduto() {
        System.out.print("Digite o ID do produto que deseja consultar: ");
        int idBusca = scanner.nextInt();
        scanner.nextLine();

        boolean encontrado = false;

        try (BufferedReader reader = new BufferedReader(new FileReader("OutputProduto.txt"))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.startsWith("Id: " + idBusca)) {
                    System.out.println("Produto encontrado:");
                    System.out.println(linha);
                    encontrado = true;
                    try (BufferedWriter logWriter = new BufferedWriter(new FileWriter("Log.txt", true))) {
                        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        logWriter.write("[" + timestamp + "] usuário admin consultou o produto ID " + idBusca + ".");
                        logWriter.newLine();
                    }
                    break;
                }
            }
            if (!encontrado) {
                System.out.println("Produto com ID " + idBusca + " não encontrado.");
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }
    }

    public void ExcluirProduto() {
        System.out.print("Digite o ID do produto que deseja excluir: ");
        int idExcluir = scanner.nextInt();
        scanner.nextLine();

        boolean encontrado = false;
        StringBuilder novoConteudo = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader("OutputProduto.txt"))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.startsWith("Id: " + idExcluir)) {
                    encontrado = true;
                    System.out.println("Produto encontrado: " + linha);
                    System.out.print("Tem certeza que deseja excluir este produto? (s para sim e n para não): ");
                    String confirmacao = scanner.nextLine();
                    if (!confirmacao.equalsIgnoreCase("s")) {
                        novoConteudo.append(linha).append("\n");
                        System.out.println("Exclusão cancelada.");
                    } else {
                        try (BufferedWriter logWriter = new BufferedWriter(new FileWriter("Log.txt", true))) {
                            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                            logWriter.write("[" + timestamp + "] usuário admin excluiu o produto ID " + idExcluir + ".");
                            logWriter.newLine();
                        }
                        System.out.println("Produto excluído com sucesso.");
                    }
                } else {
                    novoConteudo.append(linha).append("\n");
                }
            }

            if (encontrado) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("OutputProduto.txt"))) {
                    writer.write(novoConteudo.toString());
                }
            } else {
                System.out.println("Produto com ID " + idExcluir + " não encontrado.");
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler ou escrever o arquivo: " + e.getMessage());
        }
    }

    //=========================================================================
    // --- MÉTODOS AUXILIARES (I/O, Validação) ---
    //=========================================================================

    @Override
    public void ImprimirCadastro() {
        try (FileWriter fileWriter = new FileWriter("OutputProduto.txt", true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("Id: " + idProduto + ";" + "Nome: " + nomeProduto + ";" + "Descrição: " + descricao + ";" + "Custo: R$" + custo + ";" + "Preço de Venda: R$" + precoVenda + ";" + "Código do Fornecedor: " + CodFornecedor + ";");
            bufferedWriter.newLine();
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }

    @Override
    public void GravarCadastroLog() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("Produto " + nomeProduto + " Cadastrado com sucesso, verifique o arquivo OutputProduto para visualizar seu cadastro. ");
        try (FileWriter fileWriter = new FileWriter("Log.txt", true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("[" + timestamp + "] " + "usuário admin cadastrou o produto " + nomeProduto + " no banco de dados. ");
            bufferedWriter.newLine();
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }

    public boolean idExiste(int idVerificar) {
        try (BufferedReader reader = new BufferedReader(new FileReader("OutputProduto.txt"))) {
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

    public boolean idExistefornecedor(int idVerificarFornecedor) {
        try (BufferedReader reader = new BufferedReader(new FileReader("OutputPessoas.txt"))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.startsWith("Id: " + idVerificarFornecedor + ";")) {
                    return linha.contains("Tipo: fornecedor") || linha.contains("Tipo: ambos");
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao verificar ID de fornecedor: " + e.getMessage());
        }
        return false;
    }
}
