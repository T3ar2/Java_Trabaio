import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Menu {
    public void MontarMenu() { //classe para ler o arquivo "Menu1.txt" e imprimir os menus que estão escritos nele.
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("Menu1.txt"))) {
            String linha;
            while ((linha = bufferedReader.readLine()) != null) {
                String[] fields = linha.split(";");
                if (fields.length == 4 && fields[0].trim().equals("1")) {
                    System.out.println(fields[2].trim() + " - " + fields[3].trim());
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler Menu1.txt: " + e.getMessage());
        }
    }

    public void MontarMenu(int nivel, int opcaoPai) { //classe para converter o arquivo "Menu1.txt" e imprimir os menus que estão escritos nele.
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("Menu1.txt"))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] fields = line.split(";");
                if (fields.length == 4 &&
                        Integer.parseInt(fields[0].trim()) == nivel &&
                        Integer.parseInt(fields[1].trim()) == opcaoPai) {
                    System.out.println(fields[2].trim() + " - " + fields[3].trim());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the menu.txt file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error converting level or parent option to integer: " + e.getMessage());
        }
    }

    public int OpcaoEscolhida(int posIni, int posFim, String mensagem) { // Classe para ler a escolha do usuário, possuindo mensagens de erro para cada número digitado errado.
        Scanner scanner = new Scanner(System.in);
        int opcao;
        while (true) {
            System.out.print(mensagem + " (entre " + posIni + " e " + posFim + "): ");
            if (scanner.hasNextInt()) {
                opcao = scanner.nextInt();
                if (opcao >= posIni && opcao <= posFim) {
                    break; // Opção válida, sai do loop
                } else {
                    System.out.println("Erro: Opção inválida. Por favor, digite um valor entre " + posIni + " e " + posFim + ".");
                }
            } else {
                System.out.println("Erro: Entrada inválida. Por favor, digite um número inteiro.");
                scanner.next(); // Limpa a entrada inválida do scanner
            }
        }
        return opcao;
    }
}
