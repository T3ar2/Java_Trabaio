import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        Scanner scannerint = new Scanner(System.in);
        Scanner scannerString = new Scanner(System.in);
        System.out.println("Insira o ID do Cliente: ");
        int verificadorInt = scanner.nextInt();

        if (verificadorInt > 0 && verificadorInt <= 999999){
        setId_pessoa(verificadorInt);
        setPositivoid(1);
        }
        Confirmar();

        System.out.println("Insira o Nome do Cliente:");
        String N = scannerString.nextLine();
        setNome(N);
        Confirmar();


        System.out.println("Insira o Tipo da Pessoa (Cliente, Fornecedor ou ambos):");
        String verificadorString = scannerString.nextLine().toLowerCase();
        Confirmar();
            if (verificadorString.contains("cliente") || verificadorString.contains("fornecedor") || verificadorString.contains("ambos")){
                setTipo_pessoa(verificadorString);
                setPositivotipo(1);
            }
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

        try (BufferedReader reader = new BufferedReader(new FileReader("OutputCliente.txt"))) {
            String linha;

            while ((linha = reader.readLine()) != null) {
                if (linha.startsWith("Id: " + idBusca + ";")) {
                    encontrado = true;
                    System.out.println("Cliente encontrado: " + linha);

                    System.out.print("Digite o novo nome do cliente: ");
                    String novoNome = scanner.nextLine();

                    System.out.print("Digite o novo tipo (Física ou Jurídica): ");
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
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("OutputCliente.txt"))) {
                    writer.write(novoConteudo.toString());
                }

                System.out.println("Cadastro atualizado com sucesso!");

                System.out.print("Deseja alterar o(s) endereço(s) do cliente? (s/n): ");
                String alterarEndereco = scanner.nextLine().trim().toLowerCase();

                if (alterarEndereco.equals("s")) {
                    System.out.print("Deseja alterar qual endereço? (1, 2 ou ambos): ");
                    String escolhaEndereco = scanner.nextLine().trim();

                    try (BufferedReader readerEndereco = new BufferedReader(new FileReader("Endereco.txt"))) {
                        StringBuilder novoEnderecoConteudo = new StringBuilder();
                        boolean enderecoEncontrado = false;

                        String linhaEndereco;
                        while ((linhaEndereco = readerEndereco.readLine()) != null) {
                            if (linhaEndereco.startsWith("IdPessoa: " + idBusca + ";")) {
                                enderecoEncontrado = true;
                                String endereco1 = "", endereco2 = "";

                                if (escolhaEndereco.equals("1") || escolhaEndereco.equalsIgnoreCase("ambos")) {
                                    System.out.print("Novo endereço 1: ");
                                    endereco1 = scanner.nextLine();
                                } else {
                                    endereco1 = extrairEnderecoExistente(linhaEndereco, "Endereco1");
                                }

                                if (escolhaEndereco.equals("2") || escolhaEndereco.equalsIgnoreCase("ambos")) {
                                    System.out.print("Novo endereço 2: ");
                                    endereco2 = scanner.nextLine();
                                } else {
                                    endereco2 = extrairEnderecoExistente(linhaEndereco, "Endereco2");
                                }

                                String novaLinhaEndereco = "IdPessoa: " + idBusca + ";Endereco1: " + endereco1 + ";Endereco2: " + endereco2 + ";";
                                novoEnderecoConteudo.append(novaLinhaEndereco).append("\n");
                            } else {
                                novoEnderecoConteudo.append(linhaEndereco).append("\n");
                            }
                        }

                        if (enderecoEncontrado) {
                            try (BufferedWriter writerEndereco = new BufferedWriter(new FileWriter("Endereco.txt"))) {
                                writerEndereco.write(novoEnderecoConteudo.toString());
                            }

                            try (BufferedWriter logWriter = new BufferedWriter(new FileWriter("Log.txt", true))) {
                                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                                logWriter.write("[" + timestamp + "] usuário admin alterou o endereço do cliente ID " + idBusca + ".");
                                logWriter.newLine();
                            }

                            System.out.println("Endereço(s) atualizado(s) com sucesso.");
                        } else {
                            System.out.println("Endereço para o cliente de ID " + idBusca + " não encontrado.");
                        }
                    } catch (IOException e) {
                        System.err.println("Erro ao atualizar o(s) endereço(s): " + e.getMessage());
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

        try (BufferedReader reader = new BufferedReader(new FileReader("OutputPesoas.txt"))) {
            StringBuilder novoConteudo = new StringBuilder();
            String linha;

            while ((linha = reader.readLine()) != null) {
                if (linha.startsWith("Id: " + idBusca + ";")) {
                    encontrado = true;
                } else {
                    novoConteudo.append(linha).append("\n");
                }
            }

            if (encontrado) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("OutputPesoas.txt"))) {
                    writer.write(novoConteudo.toString());
                }


                System.out.println("Cadastro excluído com sucesso.");

                try (BufferedWriter logWriter = new BufferedWriter(new FileWriter("Log.txt", true))) {
                    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    logWriter.write("[" + timestamp + "] Usuário admin excluiu a pessoa com o id " + idBusca + ".");
                    logWriter.newLine();
                }
            } else {
                System.out.println("Cliente com ID " + idBusca + " não encontrado.");
            }

        } catch (IOException e) {
            System.err.println("Erro ao excluir cadastro: " + e.getMessage());
        }
    }

    public void Confirmar(){
        Scanner ConfirmarScanner = new Scanner(System.in);
        System.out.println("Aperte ENTER para confirmar. ");
        ConfirmarScanner.nextLine();
    };

}
