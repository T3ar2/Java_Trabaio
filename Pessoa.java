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

public class Pessoa {

    // --- Atributos da Classe ---
    private int id_pessoa;
    private String nome;
    private String tipo_pessoa;
    private int positivoid;
    private int positivotipo;
    Scanner scanner = new Scanner(System.in);

    //=========================================================================
    // --- GETTERS E SETTERS ---
    //=========================================================================

    public int getId_pessoa() {
        return id_pessoa;
    }
    public void setId_pessoa(int id_pessoa) {
        this.id_pessoa = id_pessoa;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getTipo_pessoa() {
        return tipo_pessoa;
    }
    public void setTipo_pessoa(String tipo_pessoa) {
        this.tipo_pessoa = tipo_pessoa;
    }
    public int getPositivoid() {
        return positivoid;
    }
    public void setPositivoid(int positivoid) {
        this.positivoid = positivoid;
    }
    public int getPositivotipo() {
        return positivotipo;
    }
    public void setPositivotipo(int positivotipo) {
        this.positivotipo = positivotipo;
    }

    //=========================================================================
    // --- MÉTODOS PRINCIPAIS (CRUD) ---
    //=========================================================================

    public void Cadastro_Cliente() {
        Enderecos enderecos = new Enderecos();
        int verificadorInt;
        while (true) {
            System.out.println("Insira o ID do Cliente: ");
            verificadorInt = scanner.nextInt();
            scanner.nextLine();

            if (verificadorInt <= 0 || verificadorInt > 999999) {
                System.out.println("ID inválido. Digite um número entre 1 e 999999.");
            } else if (idExiste(verificadorInt)) {
                System.out.println("Este ID já está em uso. Por favor, insira outro ID.");
            } else {
                break;
            }
        }
        setId_pessoa(verificadorInt);
        setPositivoid(1);
        enderecos.setVinculoIdpessoaEndereco(id_pessoa);
        Confirmar();

        System.out.println("Insira o Nome do Cliente:");
        String N = scanner.nextLine();
        setNome(N);
        Confirmar();

        System.out.println("Insira o Tipo da Pessoa (Cliente, Fornecedor ou ambos):");
        String verificadorString = scanner.nextLine().toLowerCase();
        Confirmar();
        if (verificadorString.contains("cliente") || verificadorString.contains("fornecedor") || verificadorString.contains("ambos")) {
            setTipo_pessoa(verificadorString);
            setPositivotipo(1);
        }
        enderecos.CadastroEndereco();
        ImprimirCadastro();
        GravarCadastroLog();
    }

    public void AtualizarCadastroCliente() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o ID do cliente que deseja atualizar: ");
        int idBusca = scanner.nextInt();
        scanner.nextLine();

        boolean encontrado = false;
        StringBuilder novoConteudo = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader("OutputPessoas.txt"))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.startsWith("Id: " + idBusca + ";")) {
                    encontrado = true;
                    System.out.println("Pessoa encontrado: " + linha);
                    System.out.print("Digite o novo nome do cliente: ");
                    String novoNome = scanner.nextLine();
                    System.out.print("Digite o novo tipo (Cliente, Fornecedor ou ambos): ");
                    String novoTipo = scanner.nextLine();

                    String novaLinha = "Id: " + idBusca + ";Nome: " + novoNome + ";Tipo: " + novoTipo + ";";
                    novoConteudo.append(novaLinha).append("\n");

                    try (BufferedWriter logWriter = new BufferedWriter(new FileWriter("Log.txt", true))) {
                        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        logWriter.write("[" + timestamp + "] usuário admin alterou os dados do cliente ID " + idBusca + ".");
                        logWriter.newLine();
                    }
                } else {
                    novoConteudo.append(linha).append("\n");
                }
            }

            if (encontrado) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("OutputPessoas.txt"))) {
                    writer.write(novoConteudo.toString());
                }
                System.out.println("Cadastro atualizado com sucesso!");

                System.out.print("Deseja alterar o(s) endereço(s) do cliente? (s/n): ");
                String alterarEndereco = scanner.nextLine().trim().toLowerCase();

                if (alterarEndereco.equals("s")) {
                    atualizarEnderecosCliente(idBusca);
                }
            } else {
                System.out.println("Cliente com ID " + idBusca + " não encontrado.");
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }
    }

    public void ConsultarCliente() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o ID do cliente a ser consultado: ");
        int idBusca = scanner.nextInt();
        scanner.nextLine();
        boolean encontrado = false;

        try (BufferedReader reader = new BufferedReader(new FileReader("OutputPessoas.txt"))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.startsWith("Id: " + idBusca + ";")) {
                    System.out.println("Cliente encontrado:");
                    System.out.println(linha);
                    encontrado = true;
                    try (BufferedWriter logWriter = new BufferedWriter(new FileWriter("Log.txt", true))) {
                        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        logWriter.write("[" + timestamp + "] Usuário admin consultou o cliente de id " + idBusca + ".");
                        logWriter.newLine();
                    }
                    break;
                }
            }
            if (!encontrado) {
                System.out.println("O cliente de ID " + idBusca + " não foi encontrado.");
            }
        } catch (IOException e) {
            System.err.println("Erro ao consultar cadastro: " + e.getMessage());
        }
    }

    public void ExcluirCadastroCliente() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o ID do cliente a ser excluído: ");
        int idBusca = scanner.nextInt();
        scanner.nextLine();

        boolean encontrado = false;
        StringBuilder novoConteudo = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader("OutputPessoas.txt"))) {
            String linha;

            while ((linha = reader.readLine()) != null) {
                if (linha.startsWith("Id: " + idBusca + ";")) {
                    encontrado = true;
                    System.out.println("Cliente encontrado: " + linha);
                    System.out.print("Tem certeza que deseja excluir este cliente e todos os seus endereços? (s para sim e n para não): ");
                    String confirmacao = scanner.nextLine();

                    if (confirmacao.equalsIgnoreCase("s")) {
                        // Escreve no log antes de remover os dados
                        try (BufferedWriter logWriter = new BufferedWriter(new FileWriter("Log.txt", true))) {
                            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                            logWriter.write("[" + timestamp + "] Usuário admin excluiu a pessoa com o id " + idBusca + ".");
                            logWriter.newLine();
                        }
                        // Exclui os endereços associados
                        excluirEnderecosPorClienteId(idBusca);
                        System.out.println("Cadastro e endereços associados foram excluídos com sucesso.");
                    } else {
                        novoConteudo.append(linha).append("\n");
                        System.out.println("Exclusão cancelada.");
                    }
                } else {
                    novoConteudo.append(linha).append("\n");
                }
            }

            if (encontrado) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("OutputPessoas.txt"))) {
                    writer.write(novoConteudo.toString());
                }
            } else {
                System.out.println("Cliente com ID " + idBusca + " não encontrado.");
            }
        } catch (IOException e) {
            System.err.println("Erro ao excluir cadastro: " + e.getMessage());
        }
    }

    //=========================================================================
    // --- MÉTODOS AUXILIARES (I/O, Validação, UI) ---
    //=========================================================================

    public void ImprimirCadastro() {
        try (FileWriter fileWriter = new FileWriter("OutputPessoas.txt", true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("Id: " + id_pessoa + ";" + "Nome: " + nome + ";" + "Tipo: " + tipo_pessoa + ";");
            bufferedWriter.newLine();
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }

    public void GravarCadastroLog() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println(nome + " Cadastrado com sucesso, verifique o arquivo Output para visualizar seu cadastro. ");
        try (FileWriter fileWriter = new FileWriter("Log.txt", true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("[" + timestamp + "] " + "usuário admin cadastrou " + nome + " no banco de dados. ");
            bufferedWriter.newLine();
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }

    private void atualizarEnderecosCliente(int idBusca) {
        System.out.print("Quantos endereços deseja atualizar para o cliente? ");
        int quantidade = scanner.nextInt();
        scanner.nextLine();

        List<String> novosEnderecos = new ArrayList<>();
        for (int i = 0; i < quantidade; i++) {
            System.out.println("Atualizando endereço " + (i + 1));
            // Coleta de dados do novo endereço
            int cep = 0;
            while (cep <= 0 || cep > 99999999) {
                System.out.print("Insira o CEP: ");
                cep = scanner.nextInt();
                scanner.nextLine();
            }
            System.out.print("Insira o nome da rua (sem o número da casa): ");
            String logradouro = scanner.nextLine();
            System.out.print("Insira o número: ");
            int numero = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Insira o complemento (pressione Enter se não houver): ");
            String complemento = scanner.nextLine();
            String tipoEndereco = "";
            while (true) {
                System.out.print("Insira o tipo de endereço (Comercial, Residencial, Entrega ou Correspondência): ");
                tipoEndereco = scanner.nextLine().toLowerCase();
                if (tipoEndereco.contains("comercial") || tipoEndereco.contains("residencial") || tipoEndereco.contains("entrega") || tipoEndereco.contains("correspondência")) {
                    break;
                } else {
                    System.out.println("Tipo de endereço inválido. Tente novamente.");
                }
            }
            String linhaEndereco = "Cliente Id: " + idBusca + "; CEP: " + cep + "; Logadouro: " + logradouro + "; Número: " + numero + "; Complemento: " + complemento + "; Tipo: " + tipoEndereco + ";";
            novosEnderecos.add(linhaEndereco);
        }

        // Reescrevendo os endereços no arquivo
        excluirEnderecosPorClienteId(idBusca); // Remove os antigos
        try (BufferedWriter writerEndereco = new BufferedWriter(new FileWriter("EnderecoOutput.txt", true))) {
            for (String endereco : novosEnderecos) {
                writerEndereco.write(endereco);
                writerEndereco.newLine();
            }
            // Log da atualização
            try (BufferedWriter logWriter = new BufferedWriter(new FileWriter("Log.txt", true))) {
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                logWriter.write("[" + timestamp + "] usuário admin atualizou os endereços do cliente ID " + idBusca + ".");
                logWriter.newLine();
            }
            System.out.println("Endereço(s) atualizado(s) com sucesso.");
        } catch (IOException e) {
            System.err.println("Erro ao atualizar os endereços: " + e.getMessage());
        }
    }

    private void excluirEnderecosPorClienteId(int idBusca) {
        StringBuilder novoConteudoEnderecos = new StringBuilder();
        try (BufferedReader readerEndereco = new BufferedReader(new FileReader("EnderecoOutput.txt"))) {
            String linhaEndereco;
            while ((linhaEndereco = readerEndereco.readLine()) != null) {
                if (!linhaEndereco.startsWith("Cliente Id: " + idBusca + ";")) {
                    novoConteudoEnderecos.append(linhaEndereco).append(System.lineSeparator());
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler os endereços para exclusão: " + e.getMessage());
            return;
        }

        try (BufferedWriter writerEndereco = new BufferedWriter(new FileWriter("EnderecoOutput.txt"))) {
            writerEndereco.write(novoConteudoEnderecos.toString());
        } catch (IOException e) {
            System.err.println("Erro ao reescrever arquivo de endereços: " + e.getMessage());
        }
    }

    public void Confirmar() {
        System.out.println("Aperte ENTER para confirmar. ");
        scanner.nextLine();
    }

    public boolean idExiste(int idVerificar) {
        try (BufferedReader reader = new BufferedReader(new FileReader("OutputPessoas.txt"))) {
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
