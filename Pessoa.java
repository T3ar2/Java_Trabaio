import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

public class Pessoa {
    private int id_pessoa;
    private String nome;
    private String tipo_pessoa;
    private int positivoid;
    private int positivotipo;

    Scanner scanner = new Scanner(System.in);
    public int getId_pessoa() {
        return id_pessoa;
    }

    public String getNome() {
        return nome;
    }

    public String getTipo_pessoa() {
        return tipo_pessoa;
    }

    public int getPositivotipo() {
        return positivotipo;
    }

    public int getPositivoid() {
        return positivoid;
    }

    public void setId_pessoa(int id_pessoa) {
        this.id_pessoa = id_pessoa;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTipo_pessoa(String tipo_pessoa) {
        this.tipo_pessoa = tipo_pessoa;
    }

    public void setPositivoid(int positivoid) {this.positivoid = positivoid;}

    public void setPositivotipo(int positivotipo) {
        this.positivotipo = positivotipo;
    }

    private String extrairEnderecoExistente(String linha, String campo) {
        int inicio = linha.indexOf(campo + ":");
        if (inicio == -1) return "";

        int fim = linha.indexOf(";", inicio);
        if (fim == -1) fim = linha.length();

        return linha.substring(inicio + campo.length() + 1, fim).trim();
    }

    public void Cadastro_Cliente(){
        Enderecos enderecos = new Enderecos();
        System.out.println("Insira o ID do Cliente: ");
        int verificadorInt = scanner.nextInt();
        scanner.nextLine();

        if (verificadorInt > 0 && verificadorInt <= 999999){
        setId_pessoa(verificadorInt);
        setPositivoid(1);
        enderecos.setVinculoIdpessoaEndereco(id_pessoa);
        }
        Confirmar();

        System.out.println("Insira o Nome do Cliente:");
        String N = scanner.nextLine();
        setNome(N);
        Confirmar();


        System.out.println("Insira o Tipo da Pessoa (Cliente, Fornecedor ou ambos):");
        String verificadorString = scanner.nextLine().toLowerCase();
        Confirmar();
            if (verificadorString.contains("cliente") || verificadorString.contains("fornecedor") || verificadorString.contains("ambos")){
                setTipo_pessoa(verificadorString);
                setPositivotipo(1);
            }
        enderecos.CadastroEndereco();
    }

    public void ImprimirCadastro(){

        try (FileWriter fileWriter = new FileWriter("OutputPesoas.txt", true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("Id: " + id_pessoa + ";" +"Nome: " + nome + ";" + "Tipo: " + tipo_pessoa + ";");
            bufferedWriter.newLine();
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }

    public void GravarCadastroLog(){
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println( nome + " Cadastrado com sucesso, verifique o arquivo Output para visualizar seu cadastro. ");
        try (FileWriter fileWriter = new FileWriter("Log.txt", true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("["+ timestamp + "] " + "usuário admin cadastrou "  + nome + " no banco de dados. ");
            bufferedWriter.newLine();
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }

    public void AtualizarCadastroCliente() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o ID do cliente que deseja atualizar: ");
        int idBusca = scanner.nextInt();
        scanner.nextLine(); // consumir quebra de linha

        boolean encontrado = false;
        StringBuilder novoConteudo = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader("OutputPesoas.txt"))) {
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
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("OutputPesoas.txt"))) {
                    writer.write(novoConteudo.toString());
                }

                System.out.println("Cadastro atualizado com sucesso!");

                System.out.print("Deseja alterar o(s) endereço(s) do cliente? (s/n): ");
                String alterarEndereco = scanner.nextLine().trim().toLowerCase();

                if (alterarEndereco.equals("s")) {
                    System.out.print("Quantos endereços deseja atualizar para o cliente? ");
                    int quantidade = scanner.nextInt();
                    scanner.nextLine(); // consumir quebra de linha

                    List<String> novosEnderecos = new ArrayList<>();

                    for (int i = 0; i < quantidade; i++) {
                        System.out.println("Atualizando endereço " + (i + 1));

                        int cep = 0;
                        while (cep <= 0 || cep > 99999999) {
                            System.out.print("Insira o CEP: ");
                            cep = scanner.nextInt();
                            scanner.nextLine(); // consumir quebra de linha
                        }

                        System.out.print("Insira o nome da rua (sem o número da casa): ");
                        String logradouro = scanner.nextLine();

                        System.out.print("Insira o número: ");
                        int numero = scanner.nextInt();
                        scanner.nextLine(); // consumir quebra de linha

                        System.out.print("Insira o complemento (pressione Enter se não houver): ");
                        String complemento = scanner.nextLine();

                        String tipoEndereco = "";
                        while (true) {
                            System.out.print("Insira o tipo de endereço (Comercial, Residencial, Entrega ou Correspondência): ");
                            tipoEndereco = scanner.nextLine().toLowerCase();
                            if (tipoEndereco.contains("comercial") || tipoEndereco.contains("residencial")
                                    || tipoEndereco.contains("entrega") || tipoEndereco.contains("correspondência")) {
                                break;
                            } else {
                                System.out.println("Tipo de endereço inválido. Tente novamente.");
                            }
                        }

                        String linhaEndereco = "Cliente Id: " + idBusca + "; CEP: " + cep + "; Logadouro: " + logradouro +
                                "; Número: " + numero + "; Complemento: " + complemento + "; Tipo: " + tipoEndereco + ";";
                        novosEnderecos.add(linhaEndereco);
                    }

                    // Reescrevendo os endereços no arquivo (removendo os antigos do cliente)
                    try (BufferedReader readerEndereco = new BufferedReader(new FileReader("EnderecoOutput.txt"))) {
                        StringBuilder enderecoAtualizado = new StringBuilder();
                        String linhaEnderecoAtual;

                        while ((linhaEnderecoAtual = readerEndereco.readLine()) != null) {
                            if (!linhaEnderecoAtual.startsWith("Cliente Id: " + idBusca + ";")) {
                                enderecoAtualizado.append(linhaEnderecoAtual).append("\n");
                            }
                        }

                        for (String endereco : novosEnderecos) {
                            enderecoAtualizado.append(endereco).append("\n");
                        }

                        try (BufferedWriter writerEndereco = new BufferedWriter(new FileWriter("EnderecoOutput.txt"))) {
                            writerEndereco.write(enderecoAtualizado.toString());
                        }

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

        try (BufferedReader reader = new BufferedReader(new FileReader("OutputPesoas.txt"))) {
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

    try (BufferedReader reader = new BufferedReader(new FileReader("OutputPesoas.txt"))) {
        String linha;

        while ((linha = reader.readLine()) != null) {
            if (linha.startsWith("Id: " + idBusca + ";")) {
                encontrado = true;
                System.out.println("Cliente encontrado: " + linha);
                System.out.print("Tem certeza que deseja excluir este cliente? (s para sim e n para não): ");
                String confirmacao = scanner.nextLine();

                if (!confirmacao.equalsIgnoreCase("s")) {
                    novoConteudo.append(linha).append("\n");
                    System.out.println("Exclusão cancelada.");
                } else {
                    try (BufferedWriter logWriter = new BufferedWriter(new FileWriter("Log.txt", true))) {
                        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        logWriter.write("[" + timestamp + "] Usuário admin excluiu a pessoa com o id " + idBusca + ".");
                        logWriter.newLine();
                    }
                    System.out.println("Cadastro excluído com sucesso.");
                }
            } else {
                novoConteudo.append(linha).append("\n");
            }
        }

        if (encontrado) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("OutputPesoas.txt"))) {
                writer.write(novoConteudo.toString());
            }
        } else {
            System.out.println("Cliente com ID " + idBusca + " não encontrado.");
        }

    } catch (IOException e) {
        System.err.println("Erro ao excluir cadastro: " + e.getMessage());
    }
}

    public void Confirmar(){
        System.out.println("Aperte ENTER para confirmar. ");
        scanner.nextLine();
    }

}
