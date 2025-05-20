import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;
import java.io.PrintWriter;
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

    public void Cadastro_Cliente(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insira o ID do Cliente: ");
        int verificadorInt = scanner.nextInt();
        if (verificadorInt > 0 && verificadorInt <= 999999){
        setId_pessoa(verificadorInt);
        setPositivoid(1);
        }
        System.out.println("Aperte ENTER para confirmar. ");
        scanner.nextLine();
        System.out.println("Insira o Nome do Cliente:");
        setNome(scanner.nextLine());
        System.out.println("Aperte ENTER para confirmar. ");
        scanner.nextLine(); /*Tive que usar um novo scanner por que o java estava imprimindo o Tipo pessoa em cima do SetNome*/
        System.out.println("Insira o Tipo da Pessoa (Cliente, Fornecedor ou ambos):");
        String verificadorString = scanner.nextLine().toLowerCase();
        System.out.println("Aperte ENTER para confirmar. ");/*Tive que usar um novo scanner por que o java estava finalizando sem inserir o tipo*/
        scanner.nextLine();
            if (verificadorString.contains("cliente") || verificadorString.contains("fornecedor") || verificadorString.contains("ambos")){
                setTipo_pessoa(verificadorString);
                setPositivotipo(1);
            }
    }

    public void ImprimirCadastro(){
        try (FileWriter fileWriter = new FileWriter("Output.txt", true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("Id: " + id_pessoa + ";" +"Nome: " + nome + ";" + "Tipo: " + tipo_pessoa + ".");
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
        System.out.print("Digite o ID do cliente a ser atualizado: ");
        int idBusca = scanner.nextInt();
        scanner.nextLine(); // limpar o buffer

        boolean encontrado = false;

        try (BufferedReader reader = new BufferedReader(new FileReader("Output.txt"))) {
            StringBuilder novoConteudo = new StringBuilder();
            String linha;

            while ((linha = reader.readLine()) != null) {
                if (linha.startsWith("Id: " + idBusca + ";")) {
                    encontrado = true;
                    System.out.println("Cadastro atual: " + linha);

                    System.out.print("Novo nome: ");
                    String novoNome = scanner.nextLine();

                    System.out.print("Novo tipo de pessoa: ");
                    String novoTipo = scanner.nextLine();

                    String novaLinha = "Id: " + idBusca + ";" + "Nome: " + novoNome + ";" + "Tipo: " + novoTipo + ".";
                    novoConteudo.append(novaLinha).append("\n");
                } else {
                    novoConteudo.append(linha).append("\n");
                }
            }

            if (encontrado) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("Output.txt"))) {
                    writer.write(novoConteudo.toString());
                }

                System.out.println("Cadastro atualizado com sucesso.");

                try (BufferedWriter logWriter = new BufferedWriter(new FileWriter("Log.txt", true))) {
                    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    logWriter.write( "["+ timestamp + "] usuário admin atualizou o cliente com ID " + id_pessoa);
                    logWriter.newLine();
                }
            } else {

                System.out.println("Cliente com ID " + idBusca + " não encontrado.");
            }

        } catch (IOException e) {
            System.err.println("Erro ao atualizar cadastro: " + e.getMessage());
        }
    }
    public void ConsultarCliente() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o ID do cliente a ser consultado: ");
        int idBusca = scanner.nextInt();
        scanner.nextLine();
        boolean encontrado = false;

        try (BufferedReader reader = new BufferedReader(new FileReader("Output.txt"))) {
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

        try (BufferedReader reader = new BufferedReader(new FileReader("Output.txt"))) {
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
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("Output.txt"))) {
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

}
